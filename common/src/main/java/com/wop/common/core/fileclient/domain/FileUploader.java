package com.wop.common.core.fileclient.domain;

import com.wop.common.BuildConfig;
import com.wop.common.core.listener.FileClientListener;
import com.wop.common.core.listener.ProgressListener;
import com.wop.common.core.net.NetConstants;
import com.wop.common.core.net.SSLSocketClient;
import com.wop.common.util.Base64;
import com.wop.common.util.DigestUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

import android.accounts.NetworkErrorException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONObject;

import io.reactivex.Observable;

/**
 * Created by liuwenji on 2018/9/26.
 * 处理文件上传
 */

public final class FileUploader {

    private final String CHARSET = "utf-8"; // 设置编码
    private final String secret = "hfdaf9923lkfsjjfsf2-f";//秘钥

    private final String BOUNDARY = java.util.UUID.randomUUID().toString();
    private final String CONTENT_TYPE = "multipart/form-data";
    private final String PREFIX = "--";
    private final String LINEND = "\r\n";

    private int timeOut = NetConstants.CONNECT_TIME_OUT;
    private boolean isEncrypt = true;

    private ProgressListener listener;
    private FileClientListener upListener;
    private String fileKey;
    private long file_size;
    private int progress;;

    private FileUploader(){}

    public static FileUploader newInstance() {
        return new FileUploader();
    }

    //设置超时时间 (默认 NetConstants.CONNECT_TIME_OUT)
    public FileUploader setTimeOut(int timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    //设置是否加密 (默认true)
    public void setEncrypt(boolean isEncrypt) {
        this.isEncrypt = isEncrypt;
    }

//    /**
//     * Android上传文件到服务端
//     * @param file 需要上传的文件
//     * @param url 请求的rul
//     * @param contentType 上传的文件类型 (UploadContentType 类常量)
//     * @return 返回响应的内容
//     */
//    public String uploadFile(@NonNull final File file, @NonNull final String url, @NonNull final String contentType,
//                                    @NonNull final String md5) throws Exception {
//        return startUpload(file, url, sha256_HMAC(md5, secret), contentType);
//    }

    /**
     * 支持Rxjava 方式
     * Android上传文件到服务端
     * @param file 需要上传的文件
     * @param url 请求的rul
     * @param fileType 上传的文件类型
     * @return 返回响应的内容
     */
    public Observable<String> uploadFileRx(@NonNull String fileKey, @NonNull final File file, @NonNull final String url,
                                           @NonNull final FileType fileType, @Nullable ProgressListener listener) {
        this.fileKey = fileKey;
        this.listener = listener;
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                String contentType = "";
                if (fileType == FileType.BooUploadFileTypeAMR) {
                    contentType = UploadContentType.UPLOAD_AMR;
                } else if (fileType == FileType.BooUploadFileTypeGIF) {
                    contentType = UploadContentType.UPLOAD_GIF;
                } else if (fileType == FileType.BooUploadFileTypeJPG) {
                    contentType = UploadContentType.UPLOAD_JPG;
                } else if (fileType == FileType.BooUploadFileTypePNG) {
                    contentType = UploadContentType.UPLOAD_PNG;
                } else if (fileType == FileType.BooUploadFileTypeMP4) {
                    contentType = UploadContentType.UPLOAD_MP4;
                } else if (fileType == FileType.BooUploadFileTypeZIP) {
                    contentType = UploadContentType.UPLOAD_ZIP;
                }
                return startUpload(file, url, isEncrypt, contentType);
            }
        });
    }

    public void addListener(FileClientListener listener) {
        this.upListener = listener;
    }

    private String startUpload(@NonNull File file, String RequestURL, boolean isEncrypt, String contentType) throws IOException,NetworkErrorException {
        file_size = file.length();
        progress = 0;
        String upload_url = null;
        ProgressOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (!BuildConfig.DEBUG) {//如果是release，请求https
                ((HttpsURLConnection)conn).setSSLSocketFactory(SSLSocketClient.getSSLSocketFactory());
                ((HttpsURLConnection)conn).setHostnameVerifier(SSLSocketClient.getHostnameVerifier());
            }
            conn.setReadTimeout(timeOut);
            conn.setConnectTimeout(timeOut);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            if (isEncrypt) {
                conn.setRequestProperty("Authorization", "Upload " + sha256_HMAC(DigestUtils.getFileMD5String(file),secret));
            }
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            conn.connect();
            outputStream = new ProgressOutputStream(conn.getOutputStream());

            StringBuilder fileSb = new StringBuilder();
            fileSb.append(PREFIX)
                    .append(BOUNDARY)
                    .append(LINEND)
                    .append("Content-Disposition: form-data; name=\"file\"; filename=\""
                            + file.getName() + "\"" + LINEND)
                    .append("Content-Type: " + contentType + LINEND)
                    .append("Content-Transfer-Encoding: 8bit" + LINEND)
                    .append(LINEND);// 参数头设置完以后需要两个换行，然后才是参数内容
            outputStream.writeBytes(fileSb.toString());
            outputStream.flush();

            inputStream = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            outputStream.write(LINEND.getBytes());
            // 请求结束标志
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            outputStream.write(end_data);
            outputStream.flush();
            if (conn.getResponseCode() == 200) {
                InputStream in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line = null;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                JSONObject jsonObject = new JSONObject(response.toString());
                int responseCode = jsonObject.getInt("code");
                if (responseCode == 200) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    upload_url = data.getString("url");
                    conn.disconnect();
                    if (upListener != null) upListener.onSuccess(fileKey,upload_url);//文件上传存在情况下，也要进行回调
                } else {
                    conn.disconnect();
                    throw new NetworkErrorException();
                }
            } else {
                conn.disconnect();
                throw new NetworkErrorException();
            }
        } catch (Exception e) {
            if (upListener != null) upListener.onFail(fileKey);//文件上传存在情况下，也要进行回调
            if (e instanceof IOException) {
                throw new IOException(e);
            } else if (e instanceof NetworkErrorException) {
                throw new NetworkErrorException(e);
            }
            e.printStackTrace();
        } finally {
            inputStream.close();
            outputStream.close();
        }

        return upload_url;
    }

    /**
     * sha256_HMAC加密
     * @param md5
     * @param secret  秘钥
     * @return 加密后字符串
     */
    private String sha256_HMAC(String md5, String secret) {
        String hash = "";
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(md5.getBytes());
            Base64 base64 = new Base64();
            hash = base64.encode(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }

    private class ProgressOutputStream extends DataOutputStream {

        public ProgressOutputStream(OutputStream out) {
            super(out);
        }

        public void write(byte[] buffer, int byteOffset, int byteCount) throws IOException {
            super.write(buffer, byteOffset, byteCount);
            progress = progress + byteCount;
            //LogUtil.d(TAG,"progress= " + progress);
            if (listener != null) {
                listener.progress(fileKey,(int)((progress * 1.0f / file_size) * 100));
            }
            if (upListener != null) {
                upListener.onProgress(fileKey,(int)((progress * 1.0f / file_size) * 100));//已经存在文件再次下载时需要回调
            }
        }
    }
}
