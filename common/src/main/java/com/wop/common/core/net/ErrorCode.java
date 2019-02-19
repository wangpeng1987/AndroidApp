package com.wop.common.core.net;

/**
 * Created by liuwenji on 2018/10/13.
 * 服务器 response 返回的 错误码
 */

public class ErrorCode {
    //login register
    public static final int CODE_10000 = 10000; //用户名为空！
    public static final int CODE_10001 = 10001; //密码为空！
    public static final int CODE_10002 = 10002; //数据库查询异常
    public static final int CODE_10003 = 10003; //用户名密码不匹配，登录失败！
    public static final int CODE_10004 = 10004; //不存在此用户！
    public static final int CODE_10005 = 10005; //注册失败

}
