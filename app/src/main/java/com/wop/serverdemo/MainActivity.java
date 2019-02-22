package com.wop.serverdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wop.common.core.fileclient.FileClient;
import com.wop.common.core.fileclient.domain.FileType;
import com.wop.common.core.listener.FileClientListener;
import com.wop.common.core.listener.ProgressListener;
import com.wop.common.core.permission.RxPermissions;
import com.wop.common.core.rx.RetryWithDelay;
import com.wop.common.ui.matisse.Matisse;
import com.wop.common.ui.matisse.MimeType;
import com.wop.common.ui.matisse.engine.impl.GlideEngine;
import com.wop.common.util.LogUtil;
import com.wop.common.util.ZipUtil;
import com.wop.serverdemo.core.WopApplication;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private final int REQUEST_CODE_CHOOSE = 1000;
    private String TAG = MainActivity.class.getSimpleName();
    /**
     * 文件上传下载
     */
    //自定义Scheduler 线程池大小
    private final Scheduler scheduler = Schedulers.from(Executors.newFixedThreadPool(10));
    private List<Uri> mSelected;
    private List<String> mPath;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @BindView(R.id.updatePhoto)
    Button updatePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        // Example of a call to a native method
//        TextView tv = (TextView) findViewById(R.id.sample_text);
//        tv.setText(stringFromJNI());

        updatePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxPermissions rxPermissions = new RxPermissions(MainActivity.this);
                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Observer<Boolean>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Boolean aBoolean) {
                                if (aBoolean) {
                                    Matisse.from(MainActivity.this)
                                            .choose(MimeType.ofAll(), false) // 选择 mime 的类型
                                            .countable(true)
                                            .maxSelectable(1) // 图片选择的最多数量
                                            .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                            .thumbnailScale(0.85f) // 缩略图的比例
                                            .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                                            .forResult(REQUEST_CODE_CHOOSE); // 设置作为标记的请求码
                                } else {
                                    Toast.makeText(MainActivity.this, R.string.permission_request_denied, Toast.LENGTH_LONG)
                                            .show();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            mPath = Matisse.obtainPathResult(data);
            uploadFile(mPath);
            Log.e("Matisse", "mSelected: " + mSelected + " mPath :" + mPath);
        }
    }

    //上传文件
    private void uploadFile(List<String> paths) {
        for (final String path : paths) {
            if (FileClient.getInstance(WopApplication.getContext()).isUpload(path)) {//已经在上传队列中了,直接call back 调用
                FileClient.getInstance(WopApplication.getContext()).addUpListenerWhenExist(path, new FileClientListener() {
                    @Override
                    public void onProgress(String fileKey, int pro) {
                        LogUtil.d(TAG + " onProgress fileKey= " + fileKey + " pro= " + pro);
                    }

                    @Override
                    public void onFail(String fileKey) {
                        LogUtil.d(TAG + " onFail fileKey= " + fileKey);
                        FileClient.getInstance(WopApplication.getContext()).removeUploadCache(fileKey);//删除缓存
                    }

                    @Override
                    public void onSuccess(String fileKey, String uri) {//uri 上传成功后的url
                        FileClient.getInstance(WopApplication.getContext()).removeUploadCache(fileKey);//删除缓存
                        LogUtil.d(TAG + " onSuccess onFail fileKey= " + fileKey + " uri= " + uri);
                    }
                });
            } else {//开始上传
                FileType fileType = getFileType(path);
                String upload_url = getUploadUrl(path);
                //下面做上传的处理
                LogUtil.d(TAG, " path= " + path);
                FileClient.getInstance(WopApplication.getContext()).uploadFile(path, new File(path), upload_url, fileType, 50 * 1000, new ProgressListener() {
                    @Override
                    public void progress(String fileKey, int pro) {

                    }
                }).subscribeOn(scheduler)
                        .observeOn(AndroidSchedulers.mainThread())
                        .retryWhen(new RetryWithDelay(3, 50 * 1000))
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                FileClient.getInstance(WopApplication.getContext()).removeUploadCache(path);//删除cache

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                LogUtil.w(TAG, " 上传失败 ");
                                FileClient.getInstance(WopApplication.getContext()).removeUploadCache(path);//如果上传失败，清空cache
                                throwable.printStackTrace();
                            }
                        });
            }
        }
    }

    //下载文件
    private void downloadFile(final String url) {
        if (TextUtils.isEmpty(url)) {
            //Toast.makeText(this,"先上传文件",Toast.LENGTH_SHORT).show();
            return;
        }

        if (url.contains(".zip")) {//如果是zip文件，下载下来需要解压缩
            if (FileClient.getInstance(WopApplication.getContext()).isDownload(url)) {//已经在下载中
                FileClient.getInstance(WopApplication.getContext()).addDownListenerWhenExist(url, new FileClientListener() {
                    @Override
                    public void onProgress(String fileKey, int pro) {
                        LogUtil.d(TAG + " onProgress fileKey= " + fileKey + " pro= " + pro);
                    }

                    @Override
                    public void onFail(String fileKey) {
                        LogUtil.d(TAG + " onFail fileKey= " + fileKey);
                        FileClient.getInstance(WopApplication.getContext()).removeDownloadCache(fileKey);//删除缓存
                    }

                    @Override
                    public void onSuccess(String fileKey, String uri) {//uri 为下载完成后的路径
                        LogUtil.d(" onSuccess onFail fileKey= " + fileKey + " uri= " + uri);
                        FileClient.getInstance(WopApplication.getContext()).removeDownloadCache(fileKey);//删除缓存
                        String outPath = Environment.getExternalStorageDirectory().getPath() + "/" + "BooKit/";
                        ZipUtil.unZipRx(fileKey, outPath)
                                .subscribeOn(Schedulers.io())
                                .subscribe(new Consumer<String>() {
                                    @Override
                                    public void accept(String s) throws Exception {
                                        LogUtil.d(TAG + " 解压文件的路径= " + s);
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {

                                    }
                                });
                    }
                });
            } else {
                FileClient.getInstance(WopApplication.getContext())
                        .downloadFile(url, url, getFileType(url), 50 * 1000, new ProgressListener() {
                            @Override
                            public void progress(String fileKey, final int pro) {

                            }
                        }).flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Exception {
                        FileClient.getInstance(WopApplication.getContext()).removeDownloadCache(url);//删除缓存
                        LogUtil.d(TAG + " 下载文件的路径= " + s);
                        String outPath = Environment.getExternalStorageDirectory().getPath() + "/" + "BooKit/";
                        return ZipUtil.unZipRx(s, outPath);
                    }
                }).retryWhen(new RetryWithDelay(3, 50 * 1000))
                        .subscribeOn(scheduler)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                LogUtil.w(TAG, " 下载或者解压失败 ");
                                FileClient.getInstance(WopApplication.getContext()).removeDownloadCache(url);//删除缓存
                                throwable.printStackTrace();
                            }
                        });
            }
        } else {
            FileClient.getInstance(WopApplication.mContext)
                    .downloadFile(url, url, getFileType(url), 50 * 1000, new ProgressListener() {
                        @Override
                        public void progress(String fileKey, final int pro) {
                        }
                    }).retryWhen(new RetryWithDelay(3, 50 * 1000))
                    .subscribeOn(scheduler)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            FileClient.getInstance(WopApplication.mContext).removeDownloadCache(url);//删除缓存
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            FileClient.getInstance(WopApplication.mContext).removeDownloadCache(url);
                            throwable.printStackTrace();
                        }
                    });
        }
    }


    //以下为工具方法
    private String getUploadUrl(String uri) {
        String upload_url = "";
        if (uri.contains(".jpg") || uri.contains(".jpeg") || uri.contains(".png") || uri.contains(".gif")) {
            upload_url += "/uplaod/photo";
        } else if (uri.contains(".mp4") || uri.contains(".mpeg4") || uri.contains(".mp3") || uri.contains(".wav") || uri.contains(".amr")) {
            upload_url += "/upload/video";
        } else if (uri.contains(".zip")) {
            upload_url += "/upload/file";
        }
        return upload_url;
    }

    private FileType getFileType(String uri) {
        FileType fileType = FileType.BooUploadFileTypeNULL;
        if (uri.contains(".jpg") || uri.contains(".jpeg") || uri.contains(".png") || uri.contains(".gif")) {
            if (uri.contains(".jpg") || uri.contains(".jpeg")) {
                fileType = FileType.BooUploadFileTypeJPG;
            } else if (uri.contains(".png")) {
                fileType = FileType.BooUploadFileTypePNG;
            } else if (uri.contains(".gif")) {
                fileType = FileType.BooUploadFileTypeGIF;
            }
        } else if (uri.contains(".mp4") || uri.contains(".mpeg4") || uri.contains(".mp3") || uri.contains(".wav") || uri.contains(".amr")) {
            if (uri.contains(".mp4") || uri.contains(".mpeg4")) {
                fileType = FileType.BooUploadFileTypeMP4;
            } else if (uri.contains(".mp3")) {
                fileType = FileType.BooUploadFileTypeMP3;
            } else if (uri.contains(".wav")) {
                fileType = FileType.BooUploadFileTypeWAV;
            } else if (uri.contains(".amr")) {
                fileType = FileType.BooUploadFileTypeAMR;
            }
        } else if (uri.contains(".zip")) {
            fileType = FileType.BooUploadFileTypeZIP;
        }
        return fileType;
    }

}
