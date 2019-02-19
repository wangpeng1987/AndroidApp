package com.wop.common.core.listener;

/**
 * Created by liuwenji on 2018/9/27.
 * 下载等进度监听
 */

public interface ProgressListener {
    void progress(String fileKey, int pro);
}
