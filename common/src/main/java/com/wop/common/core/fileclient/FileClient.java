package com.wop.common.core.fileclient;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.wop.common.core.fileclient.domain.FileDownloader;
import com.wop.common.core.fileclient.domain.FileType;
import com.wop.common.core.fileclient.domain.FileUploader;
import com.wop.common.core.listener.FileClientListener;
import com.wop.common.core.listener.ProgressListener;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

import io.reactivex.Observable;

/**
 * Created by liuwenji on 2018/9/27.
 * 文件下载,上传
 */

public class FileClient {

    private static FileClient instance;
    private Context context;
    private File cacheDir;//文件缓存目录
    private final HashMap<String,FileDownloader> cacheDown = new LinkedHashMap();//下载缓存
    private final HashMap<String,FileUploader> cacheUp = new LinkedHashMap();//上传缓存

    private FileClient(Context context) {
        this.context = context;
    }

    public static FileClient getInstance(Context context) {
        if (instance == null) {
            synchronized (FileClient.class) {
                if (instance == null) {
                    instance = new FileClient(context);
                }
            }
        }
        return instance;
    }

    //通过fileKey,判断是否处于下载中
    public synchronized boolean isDownload(@NonNull String fileKey) {
        return cacheDown.containsKey(fileKey);
    }

    //通过fileKey,判断是否处于上传中
    public synchronized boolean isUpload(@NonNull String fileKey) {
        return cacheUp.containsKey(fileKey);
    }

    //注意该方法，如果需要的话，要调用该方法，当文件存在，底层不会重新下载，但是会把下载结果和进度上传给业务层
    public synchronized void addDownListenerWhenExist(@NonNull String fileKey,@NonNull FileClientListener downListener) {
        if (cacheDown != null && cacheDown.size() > 0 && cacheDown.containsKey(fileKey)) {
            cacheDown.get(fileKey).addListener(downListener);
        }
    }

    public synchronized void addUpListenerWhenExist(@NonNull String fileKey,@NonNull FileClientListener downListener) {
        if (cacheUp != null && cacheUp.size() > 0 && cacheUp.containsKey(fileKey)) {
            cacheUp.get(fileKey).addListener(downListener);
        }
    }

    //下载成功或者失败后，需要将相关fileKey 从缓存中移除掉
    public synchronized void removeDownloadCache(@NonNull String fileKey) {
        if (cacheDown.containsKey(fileKey)) cacheDown.remove(fileKey);
    }

    public synchronized void removeUploadCache(@NonNull String fileKey) {
        if (cacheUp.containsKey(fileKey)) cacheUp.remove(fileKey);
    }

    //取消下载，(注意，业务层需要停掉rx 观察者)
    public synchronized void cancelDownload(@NonNull String fileKey) {
        if (cacheDown.containsKey(fileKey)) {
            String outPath = cacheDown.get(fileKey).getOutPath();
            if (!TextUtils.isEmpty(outPath)) {//存在未下载完的本地文件，删除掉
                new File(outPath).delete();
            }
            cacheDown.remove(fileKey);
        }
    }

    //取消上传，(注意，业务层需要停掉rx 观察者)
    public synchronized void cancelUpload(@NonNull String fileKey) {
        cacheUp.remove(fileKey);
    }

    //取消所有下载，(注意，业务层需要停掉rx 观察者)
    public synchronized void cancelAllDownload() {
        for (String fileKey : cacheDown.keySet()) {
            String outPath = cacheDown.get(fileKey).getOutPath();
            if (!TextUtils.isEmpty(outPath)) {//存在未下载完的本地文件，删除掉
                new File(outPath).delete();
            }
            cacheDown.remove(fileKey);
        }
    }

    //取消所有上传，(注意，业务层需要停掉rx 观察者)
    public synchronized void cancelAllUpload() {
        cacheUp.clear();
    }

    /**
     * @param fileKey (标志文件的唯一标识，底层用来避免重复下载)
     * @param url
     * @param type (需要文件下载的type)
     * @param listener
     * @return
     * @throws IOException
     */
    public synchronized Observable<String> downloadFile(@NonNull final String fileKey,@NonNull String url, @NonNull FileType type,int timeOut,
                                                  @NonNull ProgressListener listener) {
        cacheDir = context.getCacheDir();
        if (cacheDown.containsKey(fileKey)) {//已经在下载中,不会触发新的下载
            return Observable.error(new Exception("请求下载文件已经在下载队里中了，你可以调用 addDownListenerWhenExist 回调方法来查看当前文件的下载状态"));
        } else {
            String suffix = "";
            if (type == FileType.BooUploadFileTypeJPG) {
                suffix = ".jpg";
            } else if (type == FileType.BooUploadFileTypePNG) {
                suffix = ".png";
            } else if (type == FileType.BooUploadFileTypeGIF) {
                suffix = ".gif";
            } else if (type == FileType.BooUploadFileTypeMP4) {
                suffix = ".mp4";
            } else if (type == FileType.BooUploadFileTypeAMR) {
                suffix = ".amr";
            } else if (type == FileType.BooUploadFileTypeZIP) {
                suffix = ".zip";
            }
            String outPath = cacheDir.getAbsolutePath() + "/" + UUID.randomUUID() + suffix;
            File file = new File(outPath);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileDownloader fileDownloader =  FileDownloader.newInstance();
            return fileDownloader.setTimeOut(timeOut).downloadFileRx(fileKey,url,file.getPath(),listener);
        }
    }

    public synchronized Observable<String> uploadFile(@NonNull final String fileKey,@NonNull File file,@NonNull String url, @NonNull FileType type,int timeOut,
                                                      @NonNull ProgressListener listener) {
        if (cacheUp.containsKey(fileKey)) {//已经在上传中,不会触发新的上传
            return Observable.error(new Exception("请求上传文件已经在下载队里中了，你可以调用 addUpListenerWhenExist 回调方法来查看当前文件的上传状态"));
        } else {
            FileUploader fileUploader =  FileUploader.newInstance();
            return fileUploader.setTimeOut(timeOut).uploadFileRx(fileKey,file,url,type,listener);
        }
    }
}
