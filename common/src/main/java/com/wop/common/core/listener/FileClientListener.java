package com.wop.common.core.listener;

/**
 * Created by liuwenji on 2018/9/27.
 */

public interface FileClientListener {
    void onProgress(String fileKey, int pro);
    void onFail(String fileKey);
    void onSuccess(String fileKey, String uri);
}
