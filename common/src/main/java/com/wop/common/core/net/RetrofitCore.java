package com.wop.common.core.net;

import android.text.TextUtils;

import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;
import com.wop.common.BuildConfig;
import com.wop.common.core.net.converter.FastJsonConverterFactory;
import com.wop.common.core.net.interceptor.EncodeDecodeInterceptor;
import com.wop.common.core.net.interceptor.HttpHeadInterceptor;
import com.wop.common.core.net.interceptor.NetWorkInterceptor;
import com.wop.common.core.net.interceptor.ParamsHelperInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import static com.wop.common.core.net.NetConstants.BASE_URL;
import static com.wop.common.core.net.NetConstants.CONNECT_TIME_OUT;
import static com.wop.common.core.net.NetConstants.READ_TIME_OUT;


/**
 * @author woniu
 * @title RetrofitCore
 * @description
 * @since 2018/9/15 上午10:41
 */
public abstract class RetrofitCore {

    /**
     * 构建retrofit对象
     *
     * @return
     */
    private Retrofit buildRetrofit() {
        return new Retrofit.Builder()
                .client(buildOkhttpClient())
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .baseUrl(TextUtils.isEmpty(getBaseUrl()) ? BASE_URL : getBaseUrl())
                .build();
    }

    /**
     * 构建okhttp client对象
     *
     * @return
     */
    private OkHttpClient buildOkhttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(getConnectTimeout(), TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(retryOnConnectionFailure())
                .readTimeout(getReadTimeOut(), TimeUnit.MILLISECONDS)
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getX509TrustManager())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier());
        return configInterceptor(builder).build();
    }

    /**
     * 将需要的所有interceptor扩展在这里配置
     *
     * @param builder
     * @return
     */
    private OkHttpClient.Builder configInterceptor(OkHttpClient.Builder builder) {
        List<Interceptor> interceptors = new ArrayList<>();
        interceptors.add(new NetWorkInterceptor(isNetworkConnected()));
        interceptors.add(new HttpHeadInterceptor());
        interceptors.add(new ParamsHelperInterceptor());
        interceptors.add(getLogInterceptor());
        interceptors.add(new EncodeDecodeInterceptor());
//        interceptors.add(new GzipInterceptor());
        List<Interceptor> subInterceptors = getInterceptors();
        if (null != subInterceptors && subInterceptors.size() > 0) {
            interceptors.addAll(subInterceptors);
        }
        for (Interceptor interceptor : interceptors) {
            builder.addInterceptor(interceptor);
        }
        return builder;
    }

    /**
     * 为子模块提供的可扩展的interceptor
     *
     * @return
     */
    protected List<Interceptor> getInterceptors() {
        return new ArrayList<>();
    }

    /**
     * 获取连接超时设定，单位毫秒
     * 各模块如果需要自定义这个值，直接重写此方法就好
     *
     * @return
     */
    protected long getConnectTimeout() {
        return CONNECT_TIME_OUT;
    }

    /**
     * 获取数据读取超时设定，单位毫秒
     * 各模块如果需要自定义这个值，直接重写此方法就好
     *
     * @return
     */
    protected long getReadTimeOut() {
        return READ_TIME_OUT;
    }

    /**
     * 默认连接失败重试，需要修改重写这个方法就行
     *
     * @return
     */
    protected boolean retryOnConnectionFailure() {
        return true;
    }

    /**
     * log日志
     *
     * @return
     */
    private LoggingInterceptor getLogInterceptor() {
        return new LoggingInterceptor.Builder()
                .loggable(BuildConfig.LOG_DEBUG)
                .setLevel(Level.BASIC)
                .log(Platform.INFO)
                .addHeader("version", BuildConfig.VERSION_NAME)
//                .addQueryParam("query", "0")
                .enableAndroidStudio_v3_LogsHack(true)
//                .logger((level, tag, msg) -> Log.w(tag, msg))
                .executor(Executors.newSingleThreadExecutor())
                .build();
    }

    /**
     * 子模块各自的base url
     *
     * @return
     */
    protected abstract String getBaseUrl();

    /**
     * 子模块实现，是否有网络，如果没有，底层抛异常，方便Rxjava 异常捕获
     */
    protected abstract boolean isNetworkConnected();

    /**
     * 用于子模块创建各自的service
     *
     * @param service
     * @param <T>
     * @return
     */
    public <T> T create(final Class<T> service) {
        return buildRetrofit().create(service);
    }
}
