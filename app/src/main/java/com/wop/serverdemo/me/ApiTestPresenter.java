package com.wop.serverdemo.me;

import com.alibaba.fastjson.JSONObject;
import com.wop.common.core.net.interceptor.HttpHeadInterceptor;
import com.wop.common.util.EmptyUtil;
import com.wop.serverdemo.R;
import com.wop.serverdemo.core.WopApplication;
import com.wop.serverdemo.core.utils.MMKVUtils;
import com.wop.serverdemo.me.model.LoginData;
import com.wop.serverdemo.me.model.ProfileInfo;
import com.wop.serverdemo.me.net.RxObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;


public class ApiTestPresenter implements ApiTestContract.Presenter {

    private MeApiDataManager mDataManager;
    private ApiTestContract.View mView;

    public ApiTestPresenter(ApiTestContract.View view) {
        mView = view;
        mDataManager = new MeApiDataManager();
    }
//
//    @Override
//    public void getUserEncode1(String uri) {
//        mDataManager.getUserEncode1(uri)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new RxObserver<JSONObject>() {
//                    @Override
//                    public void onNext(JSONObject JSONObject) {
//                        mView.showUser(JSONObject);
//                    }
//
//                    @Override
//                    public void doOnError(Throwable e) {
//                        mView.onError(e.getMessage());
//                    }
//                });
//    }
//
////    @Override
////    public void getUserEncode0() {
////        mDataManager.getUserEncode0()
////                .observeOn(AndroidSchedulers.mainThread())
////                .subscribe(new RxObserver<JSONObject>() {
////                    @Override
////                    public void onNext(JSONObject JSONObject) {
////                        mView.showUser(JSONObject);
////                    }
////
////                    @Override
////                    public void doOnError(Throwable e) {
////                        mView.onError(e.getMessage());
////                    }
////                });
//////                .subscribe(user -> mView.showUser(user),
//////                        throwable -> mView.onError(throwable.getMessage()));
////    }
//
//    @Override
//    public void postUserEncode1() {
//        Map<String, String> params = new HashMap<>();
//        mDataManager.postUserEncode1(params)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new RxObserver<JSONObject>() {
//                    @Override
//                    public void onNext(JSONObject JSONObject) {
//                        mView.showUser(JSONObject);
//                    }
//
//                    @Override
//                    public void doOnError(Throwable e) {
//                        mView.onError(e.getMessage());
//                    }
//                });
////                .subscribe(user -> mView.showUser(user),
////                        throwable -> mView.onError(throwable.getMessage()));
//    }
//
//    @Override
//    public void post404() {
//        Map<String, String> params = new HashMap<>();
//        mDataManager.post401(params)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new RxObserver<JSONObject>() {
//                    @Override
//                    public void onNext(JSONObject JSONObject) {
//                        mView.showUser(JSONObject);
//                    }
//
//                    @Override
//                    public void doOnError(Throwable e) {
//                        mView.onError(e.getMessage());
//                    }
//                });
////                .subscribe(user -> mView.showUser(user));
//    }

    @Override
    public void login(String name, String password) {
//        Map<String, String> params = new HashMap<>();
//        params.put("name", name);
//        params.put("password", password);
//        mDataManager.login(params)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new RxObserver<LoginData>() {
//                    @Override
//                    public void onNext(LoginData loginData) {
////                        if (loginData != null) {
////                            HttpHeadInterceptor.ACCESS_TOKEN = loginData.getToken();
////                        }
//                        mView.showUser(null);
//                    }
//
//                    @Override
//                    public void doOnError(Throwable e) {
//                        mView.onError(e.getMessage());
//                    }
//                });
    }

    @Override
    public void sigup(String email, String username, String password, String from, String mcc, int brithyear, String avatar, String phone) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("mcc", mcc);
        params.put("phone", phone);
        params.put("username", username);
        params.put("password", password);
        params.put("from", from);
        params.put("brithyear", brithyear + "");
        params.put("avatar", avatar);

        mDataManager.sigup(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<JSONObject>() {
                    @Override
                    public void onNext(JSONObject jsonObject) {
                        mView.showUser(jsonObject);
                    }

                    @Override
                    public void doOnError(Throwable e) {
                        mView.onError(e.getMessage());
                    }
                });
    }

    @Override
    public void getPhoneCode(String phone, String mcc) {
        mDataManager.getPhoneCode(phone, mcc)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<JSONObject>() {
                    @Override
                    public void onNext(JSONObject jsonObject) {
                        mView.showUser(jsonObject);
                    }

                    @Override
                    public void doOnError(Throwable e) {
                        mView.onError(e.getMessage());
                    }
                });
    }

    @Override
    public void getPhoneVerify(String code, String phone, String mcc) {
        mDataManager.getPhoneVerify(code, phone, mcc)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<JSONObject>() {
                    @Override
                    public void onNext(JSONObject jsonObject) {
                        mView.showUser(jsonObject);
                    }

                    @Override
                    public void doOnError(Throwable e) {
                        mView.onError(e.getMessage());
                    }
                });
    }

    @Override
    public void setUserName(String username) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        mDataManager.setUserName(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<JSONObject>() {
                    @Override
                    public void onNext(JSONObject jsonObject) {
                        mView.showUser(jsonObject);
                    }

                    @Override
                    public void doOnError(Throwable e) {
                        mView.onError(e.getMessage());
                    }
                });
    }

    @Override
    public void setPhone(String phone, String mcc, String code) {
        Map<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("mcc", mcc);
        params.put("code", code);

        mDataManager.setPhone(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<JSONObject>() {
                    @Override
                    public void onNext(JSONObject jsonObject) {
                        mView.showUser(jsonObject);
                    }

                    @Override
                    public void doOnError(Throwable e) {
                        mView.onError(e.getMessage());
                    }
                });
    }

    @Override
    public void getEmailCode(String email) {
        mDataManager.getEmailCode(email)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<JSONObject>() {
                    @Override
                    public void onNext(JSONObject jsonObject) {
                        mView.showUser(jsonObject);
                    }

                    @Override
                    public void doOnError(Throwable e) {
                        mView.onError(e.getMessage());
                    }
                });
    }

    @Override
    public void getEmailVerify(String passcode, String ticket) {
        mDataManager.getEmailVerify(passcode, ticket)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<JSONObject>() {
                    @Override
                    public void onNext(JSONObject jsonObject) {
                        mView.showUser(jsonObject);
                    }

                    @Override
                    public void doOnError(Throwable e) {
                        mView.onError(e.getMessage());
                    }
                });
    }

    @Override
    public void logout() {
        mDataManager.logout()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<JSONObject>() {
                    @Override
                    public void onNext(JSONObject jsonObject) {
                        MMKVUtils.saveLoginInfoValue(WopApplication.mContext.getResources().getString(R.string.login_state), false);
                        mView.showUser(jsonObject);
                    }

                    @Override
                    public void doOnError(Throwable e) {
                        mView.onError(e.getMessage());
                    }
                });
    }

    @Override
    public void sync() {
        mDataManager.sync()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<JSONObject>() {
                    @Override
                    public void onNext(JSONObject jsonObject) {
                        mView.showUser(jsonObject);
                    }

                    @Override
                    public void doOnError(Throwable e) {
                        mView.onError(e.getMessage());
                    }
                });
    }

    @Override
    public void profile() {
        mDataManager.profile()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<ProfileInfo>() {
                    @Override
                    public void onNext(ProfileInfo jsonObject) {
                        mView.showUser(null);
                    }

                    @Override
                    public void doOnError(Throwable e) {
                        mView.onError(e.getMessage());
                    }
                });
    }

    @Override
    public void userInfo(ArrayList ids) {
        Map<String, ArrayList<Integer>> params = new HashMap<>();
        params.put("ids", ids);

        mDataManager.userInfo(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<JSONObject>() {
                    @Override
                    public void onNext(JSONObject jsonObject) {
                        mView.showUser(jsonObject);
                    }

                    @Override
                    public void doOnError(Throwable e) {
                        mView.onError(e.getMessage());
                    }
                });
    }

    @Override
    public void setProfile(String avatar, String nickname, String bio, String gender, String scope) {
        Map<String, String> params = new HashMap<>();
        if (!EmptyUtil.isEmpty(avatar)) {
            params.put("avatar", avatar);
        }
        if (!EmptyUtil.isEmpty(nickname)) {
            params.put("nickname", nickname);
        }
        if (!EmptyUtil.isEmpty(bio)) {
            params.put("bio", bio);
        }
        if (!EmptyUtil.isEmpty(gender)) {
            params.put("gender", gender);
        }
        if (!EmptyUtil.isEmpty(scope)) {
            params.put("scope", scope);
        }
        if (params.size() == 0) {
            mView.onError("上传数据都为空");
            return;
        }

        mDataManager.setProfile(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<JSONObject>() {
                    @Override
                    public void onNext(JSONObject jsonObject) {
                        mView.showUser(jsonObject);
                    }

                    @Override
                    public void doOnError(Throwable e) {
                        mView.onError(e.getMessage());
                    }
                });
    }

    @Override
    public void getForgetPassword(String phone, String mcc, String email) {
        mDataManager.getForgetPassword(phone, mcc, email)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<JSONObject>() {
                    @Override
                    public void onNext(JSONObject jsonObject) {
                        mView.showUser(jsonObject);
                    }

                    @Override
                    public void doOnError(Throwable e) {
                        mView.onError(e.getMessage());
                    }
                });
    }

    @Override
    public void resetPassword(String ticket, String passcode, String new_password) {
        Map<String, String> params = new HashMap<>();
        params.put("ticket", ticket);
        params.put("passcode", passcode);
        params.put("new_password", new_password);

        mDataManager.resetPassword(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<JSONObject>() {
                    @Override
                    public void onNext(JSONObject jsonObject) {
                        mView.showUser(jsonObject);
                    }

                    @Override
                    public void doOnError(Throwable e) {
                        mView.onError(e.getMessage());
                    }
                });
    }

    @Override
    public void changePassword(String new_password, String old_password) {
        Map<String, String> params = new HashMap<>();
        params.put("new_password", new_password);
        params.put("password", old_password);//old_

        mDataManager.changePassword(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<JSONObject>() {
                    @Override
                    public void onNext(JSONObject jsonObject) {
                        mView.showUser(jsonObject);
                    }

                    @Override
                    public void doOnError(Throwable e) {
                        mView.onError(e.getMessage());
                    }
                });
    }

    @Override
    public void setting(Map<String, Integer> params) {
        mDataManager.setting(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<JSONObject>() {
                    @Override
                    public void onNext(JSONObject jsonObject) {
                        mView.showUser(jsonObject);
                    }

                    @Override
                    public void doOnError(Throwable e) {
                        mView.onError(e.getMessage());
                    }
                });
    }

    @Override
    public void getSetting() {
        mDataManager.getSetting()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<JSONObject>() {
                    @Override
                    public void onNext(JSONObject jsonObject) {
                        mView.showUser(jsonObject);
                    }

                    @Override
                    public void doOnError(Throwable e) {
                        mView.onError(e.getMessage());
                    }
                });
    }

    @Override
    public void setBlock(ArrayList<Integer> blocks) {
        Map<String, ArrayList<Integer>> params = new HashMap<>();
        params.put("blocks", blocks);

        mDataManager.setBlock(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<JSONObject>() {
                    @Override
                    public void onNext(JSONObject jsonObject) {
                        mView.showUser(jsonObject);
                    }

                    @Override
                    public void doOnError(Throwable e) {
                        mView.onError(e.getMessage());
                    }
                });
    }

    @Override
    public void getBlock() {
        mDataManager.getBlock()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<JSONObject>() {
                    @Override
                    public void onNext(JSONObject jsonObject) {
                        mView.showUser(jsonObject);
                    }

                    @Override
                    public void doOnError(Throwable e) {
                        mView.onError(e.getMessage());
                    }
                });
    }

    @Override
    public void delBlock(ArrayList<Integer> blocks) {
        Map<String, ArrayList<Integer>> params = new HashMap<>();
        params.put("blocks", blocks);

        mDataManager.delBlock(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<JSONObject>() {
                    @Override
                    public void onNext(JSONObject jsonObject) {
                        mView.showUser(jsonObject);
                    }

                    @Override
                    public void doOnError(Throwable e) {
                        mView.onError(e.getMessage());
                    }
                });
    }
}
