package com.wop.common.core.net.interceptor;

import android.os.Build;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.wop.common.util.KeyAes;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author woniu
 * @title HttpHeadInterceptor
 * @description
 * @since 2018/9/15 上午10:43
 */
public class HttpHeadInterceptor implements Interceptor {

    private static final String HEAD_APP_KEY = "AppKey";
    private static final String HEAD_ACCEPT_VERSION = "Accept-Version";
    private static final String HEAD_USER_AGENT = "User-Agent";
    private static final String HEAD_CURTIME = "CurTime";
    private static final String HEAD_NONCE = "Nonce";
    private static final String HEAD_CONNECTION = "Connection";
    private static final String HEAD_AUTHORIZATION = "Authorization";
    private static final String HEAD_CHECKSUM = "CheckSum";

    public static String ACCESS_TOKEN = "D989A773A1B2BCB7928CA383E79C42BE";
    public static String APP_VERSION_NAME = "v5.0.0";

    private Map<String, String> headMap;          //用来存储静态的head，只在构造方法找那个初始化一次

    public HttpHeadInterceptor() {
        headMap = new ArrayMap<>();
        headMap.put(HEAD_APP_KEY, getAppKey());
        headMap.put(HEAD_USER_AGENT, getUserAgent());
        headMap.put(HEAD_ACCEPT_VERSION, getAcceptVersion());
        headMap.put(HEAD_CONNECTION, "close");
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = configHeads(request.newBuilder());
        return chain.proceed(builder.build());
//        return chain.proceed(request);
    }

    /**
     * 配置所需的所有heads
     *
     * @param builder
     * @return
     */
    private Request.Builder configHeads(Request.Builder builder) {
        Request.Builder b = configStaticHeads(builder);
        return configDynamicHeads(b);
    }

    /**
     * 动态的head在这里配置
     *
     * @param builder
     * @return
     */
    private Request.Builder configDynamicHeads(Request.Builder builder) {
        String time = getCurrentTimeMillis();
        builder.addHeader(HEAD_CURTIME, time);
        String nonce = getNonce();
        builder.addHeader(HEAD_NONCE, nonce);
        String checkSum = getCheckSum(time, nonce);
        builder.addHeader(HEAD_CHECKSUM, checkSum);
        String authorization = getAuthorization();
        if (!TextUtils.isEmpty(authorization)) {
            builder.addHeader(HEAD_AUTHORIZATION, authorization);
        }
        return builder;
    }

    /**
     * 静态的head在这里配置
     *
     * @param builder
     * @return
     */
    private Request.Builder configStaticHeads(Request.Builder builder) {
        for (Map.Entry<String, String> entry : headMap.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }
        return builder;
    }

    private String getAppKey() {
        return "3L-!eQN!PrVlpMg2HCr!gMODMuQI68s!2CUwFldhGecMnhAQF7";
    }

    private String getUserAgent() {
        String phone = "Boo-" + APP_VERSION_NAME + "-ANDROID (android;" + "brand:" + Build.BRAND + ",phone models:" + Build.MODEL + ",SDKversion :"
                + Build.VERSION.SDK_INT + ",OSVersion:" + Build.VERSION.RELEASE + ";WIDTH:" + 0 + ",HEIGHT:" + 0 + ",DENSITY:" + 0 + ")";
//        String phone = "Boo-" + PreferenceManager.getInstance().getAppVersionName() + "-ANDROID (android;" + "brand:" + android.os.Build.BRAND + ",phone models:" + android.os.Build.MODEL + ",SDKversion :"
//                + Build.VERSION.SDK_INT + ",OSVersion:" + android.os.Build.VERSION.RELEASE + ";WIDTH:" + 0 + ",HEIGHT:" + 0 + ",DENSITY:" + 0 + ")";
        return phone;
    }

    private String getAcceptVersion() {
//        WopConstant.version
        return "1.0.0";
    }

    private String getAuthorization() {
//        String token = PreferenceManager.getInstance().getAccessToken();
        String token = ACCESS_TOKEN;
        if (TextUtils.isEmpty(token)) {
            return null;
        }
        return "Bearer " + token;
    }

    private String getNonce() {
        int nonce = new Random().nextInt(20) % (20 - 10 + 1) + 10;
        return String.valueOf(nonce);
    }

    private String getCurrentTimeMillis() {
        return String.valueOf(System.currentTimeMillis());
    }

    private String getAppSec() {
        return "qkn8R8MuQigRI4!8Sg4G34FK-z4eQq3GQxloyp7YdQuQWVxwPV";
    }

    private String getCheckSum(String time, String nonce) {
        String checkSum = new StringBuilder().append(getAppSec()).append(nonce).append(time).toString();
        return KeyAes.getSHA(checkSum);
    }

}
