package com.wop.serverdemo.me.net;

import com.alibaba.fastjson.JSONObject;
import com.wop.common.core.net.RetrofitCore;
import com.wop.common.core.net.RxTransformer;
import com.wop.common.core.net.model.ResponseModel;
import com.wop.common.util.NetUtil;
import com.wop.serverdemo.core.WopApplication;
import com.wop.serverdemo.me.model.LoginData;
import com.wop.serverdemo.me.model.ProfileInfo;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

/**
 * @author woniu
 * @title Module01Service
 * @description
 * @since 2018/9/15 下午4:33
 */
public class ApiTestService extends RetrofitCore {

    private static final String TAG = "ApiTestService";

    private static final String USERS_BASE_URL = "http://localhost:8080/AppServer/";

    private ApiTestApi mApi;

    public ApiTestService() {
        mApi = create(ApiTestApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return USERS_BASE_URL;
    }

    @Override
    protected boolean isNetworkConnected() {
        return NetUtil.isNetworkConnected(WopApplication.getContext());
    }

//    /**
//     * post 请求，返回结果加密
//     *
//     * @param params 接口所需的参数
//     * @return
//     */
//    public Observable<ReturnData> postEncode1(Map<String, String> params) {
//        return mApi.postEncode1(params)
//                .compose(RxTransformer.handleData());
//    }
//
//    public Observable<ReturnData> getEncode1(String uri) {
//        return mApi.getEncode1(uri)
//                .compose(RxTransformer.handleData());
//    }
//
////    public Observable<ReturnData> getEncode0() {
////        return mApi.getEncode0()
////                .compose(RxTransformer.handleData());
////    }
//
//    public Observable<ReturnData> post401(Map<String, String> params) {
//        return mApi.post401(params)
//                .compose(RxTransformer.handleData());
//    }

    public Observable<LoginData> login(Map<String, String> params) {
        return mApi.login(params)
                .compose((ObservableTransformer<? super ResponseModel<LoginData>, ? extends LoginData>) RxTransformer.handleData());
    }


    public Observable<JSONObject> sigup(Map<String, String> params) {
        return mApi.sigup(params)
                .compose((ObservableTransformer<? super ResponseModel<JSONObject>, ? extends JSONObject>) RxTransformer.handleData());
    }

    public Observable<JSONObject> getPhoneCode(String phone, String mcc) {
        return mApi.getPhoneCode(phone, mcc)
                .compose((ObservableTransformer<? super ResponseModel<JSONObject>, ? extends JSONObject>) RxTransformer.handleData());
    }

    public Observable<JSONObject> getPhoneVerify(String code, String phone, String mcc) {
        return mApi.getPhoneVerify(code, phone, mcc)
                .compose((ObservableTransformer<? super ResponseModel<JSONObject>, ? extends JSONObject>) RxTransformer.handleData());
    }

    public Observable<JSONObject> setUserName(Map<String, String> params) {
        return mApi.setUserName(params)
                .compose((ObservableTransformer<? super ResponseModel<JSONObject>, ? extends JSONObject>) RxTransformer.handleData());
    }

    public Observable<JSONObject> setPhone(Map<String, String> params) {
        return mApi.setPhone(params)
                .compose((ObservableTransformer<? super ResponseModel<JSONObject>, ? extends JSONObject>) RxTransformer.handleData());
    }

    public Observable<JSONObject> getEmailCode(String email) {
        return mApi.getEmailCode(email)
                .compose((ObservableTransformer<? super ResponseModel<JSONObject>, ? extends JSONObject>) RxTransformer.handleData());
    }

    public Observable<JSONObject> getEmailVerify(String passcode, String ticket) {
        return mApi.getEmailVerify(passcode, ticket)
                .compose((ObservableTransformer<? super ResponseModel<JSONObject>, ? extends JSONObject>) RxTransformer.handleData());
    }

    public Observable<JSONObject> logout() {
        return mApi.logout()
                .compose((ObservableTransformer<? super ResponseModel<JSONObject>, ? extends JSONObject>) RxTransformer.handleData());
    }

    public Observable<JSONObject> sync() {
        return mApi.sync()
                .compose((ObservableTransformer<? super ResponseModel<JSONObject>, ? extends JSONObject>) RxTransformer.handleData());
    }

    public Observable<ProfileInfo> profile() {
        return mApi.profile()
                .compose((ObservableTransformer<? super ResponseModel<ProfileInfo>, ? extends ProfileInfo>) RxTransformer.handleData());
    }

    public Observable<JSONObject> userInfo(Map<String, ArrayList<Integer>> params) {
        return mApi.userInfo(params)
                .compose((ObservableTransformer<? super ResponseModel<JSONObject>, ? extends JSONObject>) RxTransformer.handleData());
    }

    public Observable<JSONObject> setProfile(Map<String, String> params) {
        return mApi.setProfile(params)
                .compose((ObservableTransformer<? super ResponseModel<JSONObject>, ? extends JSONObject>) RxTransformer.handleData());
    }

    public Observable<JSONObject> getForgetPassword(String phone, String mcc, String email) {
        return mApi.getForgetPassword(phone, mcc)//email
                .compose((ObservableTransformer<? super ResponseModel<JSONObject>, ? extends JSONObject>) RxTransformer.handleData());
    }

    public Observable<JSONObject> resetPassword(Map<String, String> params) {
        return mApi.resetPassword(params)
                .compose((ObservableTransformer<? super ResponseModel<JSONObject>, ? extends JSONObject>) RxTransformer.handleData());
    }

    public Observable<JSONObject> changePassword(Map<String, String> params) {
        return mApi.changePassword(params)
                .compose((ObservableTransformer<? super ResponseModel<JSONObject>, ? extends JSONObject>) RxTransformer.handleData());
    }

    public Observable<JSONObject> setting(Map<String, Integer> params) {
        return mApi.setting(params)
                .compose((ObservableTransformer<? super ResponseModel<JSONObject>, ? extends JSONObject>) RxTransformer.handleData());
    }

    public Observable<JSONObject> getSetting() {
        return mApi.getSetting()
                .compose((ObservableTransformer<? super ResponseModel<JSONObject>, ? extends JSONObject>) RxTransformer.handleData());
    }

    public Observable<JSONObject> setBlock(Map<String, ArrayList<Integer>> blocks) {
        return mApi.setBlock(blocks)
                .compose((ObservableTransformer<? super ResponseModel<JSONObject>, ? extends JSONObject>) RxTransformer.handleData());
    }

    public Observable<JSONObject> getBlock() {
        return mApi.getBlock()
                .compose((ObservableTransformer<? super ResponseModel<JSONObject>, ? extends JSONObject>) RxTransformer.handleData());
    }

    public Observable<JSONObject> delBlock(Map<String, ArrayList<Integer>> blocks) {
        return mApi.delBlock(blocks)
                .compose((ObservableTransformer<? super ResponseModel<JSONObject>, ? extends JSONObject>) RxTransformer.handleData());
    }

    public Observable<JSONObject> searchBook(String name) {
        return mApi.searchBook(0,name)
                .compose((ObservableTransformer<? super ResponseModel<JSONObject>, ? extends JSONObject>) RxTransformer.handleData());
    }


    @Override
    public int hashCode() {
        return super.hashCode();
    }

//    @Override
//    protected String apiLogTag() {
//        return TAG;
//    }
}
