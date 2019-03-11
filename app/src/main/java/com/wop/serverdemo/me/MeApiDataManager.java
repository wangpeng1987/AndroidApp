package com.wop.serverdemo.me;

import com.alibaba.fastjson.JSONObject;
import com.wop.serverdemo.me.model.LoginData;
import com.wop.serverdemo.me.model.ProfileInfo;
import com.wop.serverdemo.me.net.ApiTestService;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;


/**
 * @title MeApiDataManager
 * @description Me模块model层，用来给业务层提供数据，数据的组织数据model的工作
 */
public class MeApiDataManager {

    private ApiTestService mService;
//    private Module02Dao mDao;

    public MeApiDataManager() {
        mService = new ApiTestService();
//        mDao = new Module02Dao();
    }

//    /**
//     * 对外暴露的最终数据
//     *
//     * @return
//     */
//    public Observable<ReturnData> getUserEncode0() {
//        if (NetUtil.isNetworkConnected(WopApplication.getContext())) {     //实际项目中此处应该是满足某些条件拉取net data
//            return getRemoteInfo();
//        } else {
////            return getLocalInfo();
//            return null;
//        }
//    }

    //    /**
//     * 对外暴露的最终数据
//     *
//     * @return
//     */
//    public Observable<ReturnData> getUserEncode1(String uri) {
//        return mService.getEncode1(uri);
//    }
//
//    /**
//     * 对外暴露的最终数据
//     *
//     * @return
//     */
//    public Observable<ReturnData> postUserEncode1(Map<String, String> params) {
//        return mService.postEncode1(params);
//    }
//
//    public Observable<ReturnData> post401(Map<String, String> params) {
//        return mService.post401(params);
//    }

    public Observable<LoginData> login(Map<String, String> params) {
        return mService.login(params);
    }

    public Observable<JSONObject> sigup(Map<String, String> params) {
        return mService.sigup(params);
    }

    public Observable<JSONObject> getPhoneCode(String phone, String mcc) {
        return mService.getPhoneCode(phone, mcc);
    }


    public Observable<JSONObject> getPhoneVerify(String code, String phone, String mcc) {
        return mService.getPhoneVerify(code, phone, mcc);
    }

    public Observable<JSONObject> setUserName(Map<String, String> params) {
        return mService.setUserName(params);
    }

    public Observable<JSONObject> setPhone(Map<String, String> params) {
        return mService.setPhone(params);
    }

    public Observable<JSONObject> getEmailCode(String email) {
        return mService.getEmailCode(email);
    }

    public Observable<JSONObject> getEmailVerify(String passcode, String ticket) {
        return mService.getEmailVerify(passcode, ticket);
    }

    public Observable<JSONObject> logout() {
        return mService.logout();
    }

    public Observable<JSONObject> sync() {
        return mService.sync();
    }

    public Observable<ProfileInfo> profile() {
        return mService.profile();
    }

    public Observable<JSONObject> userInfo(Map<String, ArrayList<Integer>> params) {
        return mService.userInfo(params);
    }

    public Observable<JSONObject> setProfile(Map<String, String> params) {
        return mService.setProfile(params);
    }

    public Observable<JSONObject> getForgetPassword(String phone, String mcc, String email) {
        return mService.getForgetPassword(phone, mcc, email);
    }

    public Observable<JSONObject> resetPassword(Map<String, String> params) {
        return mService.resetPassword(params);
    }

    public Observable<JSONObject> changePassword(Map<String, String> params) {
        return mService.changePassword(params);
    }

    public Observable<JSONObject> setting(Map<String, Integer> params) {
        return mService.setting(params);
    }

    public Observable<JSONObject> getSetting() {
        return mService.getSetting();
    }

    public Observable<JSONObject> setBlock(Map<String, ArrayList<Integer>> blocks) {
        return mService.setBlock(blocks);
    }

    public Observable<JSONObject> getBlock() {
        return mService.getBlock();
    }

    public Observable<JSONObject> delBlock(Map<String, ArrayList<Integer>> blocks) {
        return mService.delBlock(blocks);
    }

    public Observable<JSONObject> searchBook(String name) {
        return mService.searchBook(name);
    }


//    /**
//     * 获取网络数据，里面可能包含一些数据处理逻辑
//     *
//     * @return
//     */
//    private Observable<ReturnData> getRemoteInfo() {
//        return mService.getEncode0()
//                .doOnNext(user -> {
//                    //缓存数据
//                    // mDao.saveUser(user);
//                });
//    }

//    /**
//     * 获取本地db中的数据
//     *
//     * @return
//     */
//    private Observable<ReturnData> getLocalInfo() {
//        return mDao.getUser();
//    }
}
