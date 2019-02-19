package com.wop.serverdemo.me.net;

import com.alibaba.fastjson.JSONObject;
import com.wop.common.core.net.NetConstants;
import com.wop.common.core.net.model.ResponseModel;
import com.wop.serverdemo.me.model.LoginData;
import com.wop.serverdemo.me.model.ProfileInfo;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiTestApi {
//    /**
//     * get请求获取数据，记过未加密
//     *
//     * @return
//     */
//    @GET("test_get_down_encode_0")
//    Observable<ResponseModel<ReturnData>> getEncode0();

//    /**
//     * get请求获取数据，结果加密了
//     *
//     * @return
//     */
//    @Headers(NetConstants.HEAD_UP_ENCODE_TRUE)        //接口参数需要加密的时候，添加此header
//    @GET("test_get_down_encode_1")
//    Observable<ResponseModel<ReturnData>> getEncode1();

    //    /**
//     * post请求获取数据，结果加密了
//     *
//     * @return
//     */
//    @POST("test_post_down_encode_1")
//    Observable<ResponseModel<ReturnData>> postEncode1(@Body Map<String, String> params);
//
//    @POST("test_post_401")
//    Observable<ResponseModel<ReturnData>> post401(@Body Map<String, String> params);

    /**
     * @param
     * @return
     * @Description: 登录
     * @author: wop
     * @time: 2019/1/10 3:09 PM
     */
    @Headers(NetConstants.HEAD_UP_ENCODE_TRUE)
    @POST("user/login")
    Observable<ResponseModel<LoginData>> login(@Body Map<String, String> params);

    /**
     * @param
     * @return
     * @Description: 注册
     * @author: wop
     * @time: 2019/1/10 3:09 PM
     */
    @Headers(NetConstants.HEAD_UP_ENCODE_TRUE)
    @POST("user/sigup")
    Observable<ResponseModel<JSONObject>> sigup(@Body Map<String, String> params);

    /**
     * @param phone 手机号
     * @param mcc   mcc
     * @return
     * @Description: 发送短信验证码
     * @author: wop
     * @time: 2019/1/10 3:09 PM
     */
    @GET("v1/users/phone/code")
    Observable<ResponseModel<JSONObject>> getPhoneCode(@Query("phone") String phone, @Query("mcc") String mcc);

    /**
     * @param code  验证码
     * @param phone 手机号
     * @param mcc   mcc
     * @return
     * @Description: 验证 phone 的有效性
     * @author: wop
     * @time: 2019/1/10 3:10 PM
     */
    @GET("v1/users/phone/verify")
    Observable<ResponseModel<JSONObject>> getPhoneVerify(@Query("code") String code, @Query("phone") String phone, @Query("mcc") String mcc);

    /**
     * @param
     * @return
     * @Description: 修改用户的 username
     * @author: wop
     * @time: 2019/1/10 3:10 PM
     */
    @Headers(NetConstants.HEAD_UP_ENCODE_TRUE)
    @POST("v1/users/username/set")
    Observable<ResponseModel<JSONObject>> setUserName(@Body Map<String, String> params);

    /**
     * @param
     * @return
     * @Description: 修改用户的 phone
     * @author: wop
     * @time: 2019/1/10 3:10 PM
     */
    @Headers(NetConstants.HEAD_UP_ENCODE_TRUE)
    @POST("v1/users/phone/set")
    Observable<ResponseModel<JSONObject>> setPhone(@Body Map<String, String> params);


    /**
     * @param email 邮箱
     * @return
     * @Description: 发送 email 验证码
     * @author: wop
     * @time: 2019/1/10 3:10 PM
     */
    @GET("v1/users/email/code")
    Observable<ResponseModel<JSONObject>> getEmailCode(@Query("email") String email);


    /**
     * @param passcode 用户输入的验证码
     * @param ticket   验证的 id ：id 的话就是你先调发送 email 的接口的时候，服务端会返回给你
     * @return
     * @Description: 验证 email 的有效性
     * @author: wop
     * @time: 2019/1/10 3:10 PM
     */
    @GET("v1/users/email/verify")
    Observable<ResponseModel<JSONObject>> getEmailVerify(@Query("passcode") String passcode, @Query("ticket") String ticket);


    /**
     * @return
     * @Description: 用户注销登陆
     * @author: wop
     * @time: 2019/1/10 3:10 PM
     */
    @GET("v1/users/logout")
    Observable<ResponseModel<JSONObject>> logout();


    /**
     * @return
     * @Description: 同步用户设备数据：  做数据统计
     * @author: wop
     * @time: 2019/1/10 3:10 PM
     */
    @GET("v1/users/sync")
    Observable<ResponseModel<JSONObject>> sync();

    /**
     * @return
     * @Description: 获取当前用户信息
     * @author: wop
     * @time: 2019/1/10 3:10 PM
     */
    @GET("v1/user/profile")
    Observable<ResponseModel<ProfileInfo>> profile();

    /**
     * @param
     * @return
     * @Description: 批量获取用户信息
     * @author: wop
     * @time: 2019/1/10 3:10 PM
     */
    @Headers(NetConstants.HEAD_UP_ENCODE_TRUE)
    @POST("v1/user/info")
    Observable<ResponseModel<JSONObject>> userInfo(@Body Map<String, ArrayList<Integer>> params);

    /**
     * @param
     * @return
     * @Description: 用户 profile 更新
     * @author: wop
     * @time: 2019/1/10 3:10 PM
     */
    @Headers(NetConstants.HEAD_UP_ENCODE_TRUE)
    @POST("v1/user/profile/set")
    Observable<ResponseModel<JSONObject>> setProfile(@Body Map<String, String> params);


    /**
     * @param phone 手机号
     * @param mcc   mcc
     *              //     * @param email 邮箱
     * @return
     * @Description: 找回密码发送短信验证
     * @author: wop
     * @time: 2019/1/10 3:10 PM
     */
    @GET("v1/user/password/forget")
    Observable<ResponseModel<JSONObject>> getForgetPassword(@Query("phone") String phone, @Query("mcc") String mcc);//, @Query("email") String email


    /**
     * @param
     * @return
     * @Description: 重置密码
     * @author: wop
     * @time: 2019/1/10 3:10 PM
     */
    @Headers(NetConstants.HEAD_UP_ENCODE_TRUE)
    @POST("v1/user/password/reset")
    Observable<ResponseModel<JSONObject>> resetPassword(@Body Map<String, String> params);


    /**
     * @param
     * @return
     * @Description: 用户修改密码
     * @author: wop
     * @time: 2019/1/10 3:10 PM
     */
    @Headers(NetConstants.HEAD_UP_ENCODE_TRUE)
    @POST("v1/user/password/change")
    Observable<ResponseModel<JSONObject>> changePassword(@Body Map<String, String> params);


    /**
     * @param
     * @return
     * @Description: 修改当前用户的安全和隐私设置
     */
    @Headers(NetConstants.HEAD_UP_ENCODE_TRUE)
    @POST("v1/user/setting")
    Observable<ResponseModel<JSONObject>> setting(@Body Map<String, Integer> params);

    /**
     * @return
     * @Description: 取用户安全隐私设置
     */
    @GET("v1/user/setting")
    Observable<ResponseModel<JSONObject>> getSetting();


    /**
     * @param
     * @return
     * @Description: 设置 block 用户接口
     */
    @Headers(NetConstants.HEAD_UP_ENCODE_TRUE)
    @POST("v1/user/block")
    Observable<ResponseModel<JSONObject>> setBlock(@Body Map<String, ArrayList<Integer>> params);


    /**
     * @return
     * @Description: 获取 block 用户接口
     */
    @GET("v1/user/block")
    Observable<ResponseModel<JSONObject>> getBlock();

    /**
     * @param
     * @return
     * @Description: 删除 block 用户接口
     */
    @Headers(NetConstants.HEAD_UP_ENCODE_TRUE)
    @POST("v1/user/block/del")
    Observable<ResponseModel<JSONObject>> delBlock(@Body Map<String, ArrayList<Integer>> params);


}
