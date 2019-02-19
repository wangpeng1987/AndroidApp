package com.wop.common.core.net;

/**
 * @author woniu
 * @title Constants
 * @description net相关常量
 * @since 2018/9/15 上午10:45
 */
public class NetConstants {

    public static final String BASE_URL = "http://api.test.boochat.cn";               //默认的base url

    public static final int CONNECT_TIME_OUT = 60 * 1000;       //单位毫秒
    public static final int READ_TIME_OUT = 60 * 1000;       //单位毫秒

    //代表参数是否加密：1->数据加密，0->数据不加密（只针对post方法）
    public static final String HEAD_KEY_UP_ENCODE = "up-encode";
    //代表接口返回的数据是都加密：1->数据加密，0->数据不加密（get和post方法通用）
    public static final String HEAD_KEY_DOWN_ENCODE = "down-encode";
    //代表数据加密，up_encode和down_encode通用
    public static final String ENCODE_VALUE_TRUE = "1";
    //代表数据不加密，up_encode和down_encode通用
    public static final String ENCODE_VALUE_FALSE = "0";
    //在需要参数加密的接口上面添加此动态head，用来标记对应接口参数需要加密
    public static final String HEAD_UP_ENCODE_TRUE = HEAD_KEY_UP_ENCODE + ": " + ENCODE_VALUE_TRUE;
    public static final String HEAD_UP_ENCODE_FALSE = HEAD_KEY_UP_ENCODE + ": " + ENCODE_VALUE_FALSE;

    //get请求的名称
    public static final String HTTP_METHOD_GET = "get";
    //post请求的名称
    public static final String HTTP_METHOD_POST = "post";

    //response code
    public static final int HTTP_SUCCESS_CODE = 200;
    public static final int HTTP_FAIL_401_CODE = 401;//用户token 过期
    public static final int HTTP_FAIL_400_CODE = 400;//网络错误

    public static final int HTTP_NOT_NET_WORK_CODE = 123456;
    public static final String HTTP_NOT_NET_WORK_MSG = "没有网络";


}
