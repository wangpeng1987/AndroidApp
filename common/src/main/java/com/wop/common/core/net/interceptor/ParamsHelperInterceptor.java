package com.wop.common.core.net.interceptor;

import android.support.v4.util.ArrayMap;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

import static com.wop.common.core.net.NetConstants.HTTP_METHOD_POST;

/**
 * @author woniu
 * @title ParamsHelperInterceptor
 * @description 针对接口中的参数的多余嵌套结构，在这里总的处理，让接口的具体调用更加纯净
 * ************【这里的处理主要针对post方法，get方法无此需求】
 * @since 2018/9/15 下午5:08
 */
public class ParamsHelperInterceptor implements Interceptor {

    // TODO: 2018/9/17 添加网络判断interceptor

    Map<String, Object> paramsMap;

    public ParamsHelperInterceptor() {
        paramsMap = new ArrayMap<>();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        //获取到request
        Request request = chain.request();
        //处理request部分
        if (HTTP_METHOD_POST.equalsIgnoreCase(request.method())) {
            try {
                //buffer流
                Buffer buffer = new Buffer();
                request.body().writeTo(buffer);
                String params = buffer.readUtf8();
                MediaType mediaType = request.body().contentType();
                request = request.newBuilder().post(RequestBody.create(mediaType, dealWithParams(params))).build();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return chain.proceed(request);
    }

    private String dealWithParams(String params) {
        paramsMap.put("data", JSON.parse(params));
        //返回结果需要处理成json字符串，这里先用这个代替，等gson解析方案出来之后专门处理
        return JSON.toJSONString(paramsMap);
    }

}
