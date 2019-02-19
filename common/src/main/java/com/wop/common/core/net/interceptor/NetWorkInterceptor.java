package com.wop.common.core.net.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

import static com.wop.common.core.net.NetConstants.HTTP_NOT_NET_WORK_CODE;
import static com.wop.common.core.net.NetConstants.HTTP_NOT_NET_WORK_MSG;

/**
 * @author woniu
 * @title NetWorkInterceptor
 * @description
 * @since 2018/9/18 下午7:12
 */
public class NetWorkInterceptor implements Interceptor {

    private NetWorkInterceptor() {
    }

    private boolean haveNetwork;//是否有网络

    public NetWorkInterceptor(boolean haveNetwork) {
        this.haveNetwork = haveNetwork;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!haveNetwork) {
            try {
                throw new Exception(HTTP_NOT_NET_WORK_CODE + HTTP_NOT_NET_WORK_MSG);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return chain.proceed(chain.request());
    }
}
