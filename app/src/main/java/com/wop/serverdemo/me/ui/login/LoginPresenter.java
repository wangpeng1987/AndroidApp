package com.wop.serverdemo.me.ui.login;

import com.alibaba.fastjson.JSONObject;
import com.wop.common.util.JSONUtils;
import com.wop.serverdemo.me.MeApiDataManager;
import com.wop.serverdemo.me.model.LoginData;
import com.wop.serverdemo.me.net.RxObserver;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;


public class LoginPresenter implements LoginContract.Presenter {

    private MeApiDataManager mDataManager;
    private LoginContract.View mView;

    public LoginPresenter(LoginContract.View view) {
        mView = view;
        mDataManager = new MeApiDataManager();
    }

    @Override
    public void login(String name, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("username", name);
        params.put("password", password);
        mDataManager.login(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<LoginData>() {
                    @Override
                    public void onNext(LoginData loginData) {
//                        LoginData loginData = JSONUtils.getPerson(json.toString(), LoginData.class);
                        mView.onLoginSuccess(loginData);
                    }

                    @Override
                    public void doOnError(Throwable e) {
                        mView.onLoginError(e.getMessage());
                    }
                });
    }

}
