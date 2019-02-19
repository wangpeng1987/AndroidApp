package com.wop.serverdemo.me.ui.register;

import com.alibaba.fastjson.JSONObject;
import com.wop.serverdemo.me.MeApiDataManager;
import com.wop.serverdemo.me.net.RxObserver;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;


public class RegisterPresenter implements RegisterContract.Presenter {

    private MeApiDataManager mDataManager;
    private RegisterContract.View mView;

    public RegisterPresenter(RegisterContract.View view) {
        mView = view;
        mDataManager = new MeApiDataManager();
    }


    @Override
    public void sigup(String email, String username, String password, String mcc, String avatar, String phone) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("email", email);
        params.put("mcc", mcc);
        params.put("phone", phone);
        params.put("avatar", avatar);

        mDataManager.sigup(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<JSONObject>() {
                    @Override
                    public void onNext(JSONObject jsonObject) {
                        mView.onRegisterSuccess(jsonObject);
                    }

                    @Override
                    public void doOnError(Throwable e) {
                        mView.onRegisterError(e.getMessage());
                    }
                });
    }


}
