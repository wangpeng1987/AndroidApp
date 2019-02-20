package com.wop.common.core.net.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wop.common.core.net.utils.EncodeUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;

import static com.wop.common.core.net.NetConstants.ENCODE_VALUE_TRUE;
import static com.wop.common.core.net.NetConstants.HEAD_KEY_DOWN_ENCODE;
import static com.wop.common.core.net.NetConstants.HEAD_KEY_UP_ENCODE;
import static com.wop.common.core.net.NetConstants.HTTP_METHOD_POST;
import static com.wop.common.core.net.NetConstants.HTTP_SUCCESS_CODE;

/**
 * @author woniu
 * @title EncodeDecodeInterceptor
 * @description 处理接口中需要加密解密的数据
 * @since 2018/9/15 下午5:07
 */
public class EncodeDecodeInterceptor implements Interceptor {

    //加解密需要的key
    public static final String AES256_KEY = "ilc2grp9_d3LcMRYJ8BgYsSXuvnQbHbH";

    @Override
    public Response intercept(Chain chain) throws IOException {
        //获取到request
        Request request = chain.request();
        String upEncode = request.header(HEAD_KEY_UP_ENCODE);
        //处理request部分
        if (ENCODE_VALUE_TRUE.equals(upEncode)
                && HTTP_METHOD_POST.equalsIgnoreCase(request.method())) {
            //需要对请求的参数进行加密
            request = encodeParams(request);
        }
        //处理response部分
        Response response = chain.proceed(request);
        String downEncode = response.header(HEAD_KEY_DOWN_ENCODE);
        if (HTTP_SUCCESS_CODE == response.code()
                && ENCODE_VALUE_TRUE.equals(downEncode)) {
            //请求成功且结果加密,解密在返回
            response = decodeResponseData(response);
        }
        return response;
    }

    /**
     * 对参数进行加密
     */
    private Request encodeParams(Request request) {
        try {
            //buffer流
            Buffer buffer = new Buffer();
            request.body().writeTo(buffer);
            String params = buffer.readUtf8();
            String encodeParams = encodeParams(params);
            MediaType mediaType = request.body().contentType();
            request = request.newBuilder().post(RequestBody.create(mediaType, encodeParams)).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return request;
    }

    /**
     * 对请求结果进行解密
     */
    private Response decodeResponseData(Response response) {
        try {
            BufferedSource source = response.body().source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();
            if ("gzip".equalsIgnoreCase(response.header("Content-Encoding"))) {
                GzipSource gzippedResponseBody = null;
                try {
                    gzippedResponseBody = new GzipSource(buffer.clone());
                    buffer = new Buffer();
                    buffer.writeAll(gzippedResponseBody);
                } finally {
                    if (gzippedResponseBody != null) {
                        gzippedResponseBody.close();
                    }
                }
                String result = buffer.clone().readUtf8();
                String decodeResult = decodeData(result);
                MediaType contentType = response.body().contentType();
                response = response.newBuilder().body(ResponseBody.create(contentType, decodeResult)).build();
            } else {
                String result = buffer.readUtf8();
                String decodeResult = decodeData(result);
                MediaType contentType = response.body().contentType();
                response = response.newBuilder().body(ResponseBody.create(contentType, decodeResult)).build();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 将结果从data中抽出来解密，然后重新加入
     *
     * @param resultStr
     * @return
     */
    private String decodeData(String resultStr) {
        JSONObject obj = JSON.parseObject(resultStr);
        String dataStr = obj.getString("data");
        String data = EncodeUtil.decode(AES256_KEY, dataStr);
        obj.put("data", JSON.parse(data));
        return obj.toJSONString();
    }

    /**
     * 将params中data包含的数据加密
     *
     * @param params
     * @return
     */
    private String encodeParams(String params) {
        JSONObject obj = JSON.parseObject(params);
        if (obj.containsKey("data")) {
            String dataStr = obj.getJSONObject("data").toJSONString();
            String encodeStr = EncodeUtil.encode(AES256_KEY, dataStr);
            obj.put("data", encodeStr);
        }
        return obj.toJSONString();
    }

}
