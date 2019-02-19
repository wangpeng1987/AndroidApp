package com.wop.common.core.fileclient.domain;

import android.support.annotation.NonNull;

import com.wop.common.core.listener.FileClientListener;
import com.wop.common.core.listener.ProgressListener;
import com.wop.common.core.net.NetConstants;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;

import io.reactivex.Observable;

/**
 * Created by liuwenji on 2018/9/26.
 * 处理文件下载
 */

public final class FileDownloader {

    private String TAG = FileDownloader.class.getSimpleName();
    private int timeOut = NetConstants.CONNECT_TIME_OUT;
    private int length;
    private int progress;
    private ProgressListener listener;
    private FileClientListener downListener;
    private String outPath;
    private String fileKey;
    private FileDownloader() {}

    public static FileDownloader newInstance() {
        return new FileDownloader();
    }

    //设置超时时间
    public FileDownloader setTimeOut(int timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    public String getOutPath() {
        return outPath;
    }

    /**
     * 支持Rxjava
     * @param fileKey
     * @param url
     * @param outPath
     * @param listener
     * @return
     */
    public Observable<String> downloadFileRx(@NonNull String fileKey,@NonNull final String url,@NonNull final String outPath
            ,ProgressListener listener) {
        this.fileKey = fileKey;
        this.listener = listener;
        this.outPath = outPath;
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return startDownload(url,outPath);
            }
        });
    }

    public void addListener(FileClientListener listener) {
        this.downListener = listener;
    }

    private String startDownload(@NonNull String url,@NonNull String outPath) throws IOException {
        BufferedInputStream inputStream = null;
        ProgressOutputStream progressStream = null;
        BufferedOutputStream outputStream = null;
        try {
            URL mUrl = new URL(url);
            URLConnection connection = mUrl.openConnection();
            length = connection.getContentLength();
            connection.setConnectTimeout(timeOut);
            File localFile = new File(outPath);
            progressStream = new ProgressOutputStream(localFile);
            byte[] buffer = new byte[1024 * 8];
            inputStream = new BufferedInputStream(connection.getInputStream(), 1024 * 8);
            outputStream = new BufferedOutputStream(progressStream, 1024 * 8);
            int lenth = 0;
            while ((lenth = inputStream.read(buffer, 0, 1024 * 8)) != -1) {
                outputStream.write(buffer, 0, lenth);
            }
            outputStream.flush();
            if (downListener != null) downListener.onSuccess(fileKey,outPath);//已经存在文件再次下载时需要回调
        } catch (Exception e) {
            if (downListener != null) downListener.onFail(fileKey);//已经存在文件再次下载时需要回调
            throw new IOException(e);
        } finally {
            if (outputStream != null) outputStream.close();
            if (inputStream != null) inputStream.close();
            if (progressStream != null) progressStream.close();
        }
        return outPath;
    }

    private class ProgressOutputStream extends FileOutputStream {
        public ProgressOutputStream(File file) throws FileNotFoundException {
            super(file);
        }
        public void write(byte[] buffer, int byteOffset, int byteCount) throws IOException {
            super.write(buffer, byteOffset, byteCount);
            progress = progress + byteCount;
            //LogUtil.d(TAG,"progress= " + progress);
            if (listener != null) {
                listener.progress(fileKey,(int)((progress * 1.0f / length) * 100));
            }
            if (downListener != null) {
                downListener.onProgress(fileKey,(int)((progress * 1.0f / length) * 100));//已经存在文件再次下载时需要回调
            }
        }
    }
}
