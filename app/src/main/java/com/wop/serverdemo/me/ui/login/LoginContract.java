package com.wop.serverdemo.me.ui.login;


import com.wop.serverdemo.me.model.LoginData;

public interface LoginContract {

    interface View {

        void onLoginSuccess(LoginData loginData);

        void onLoginError(String msg);

    }

    interface Presenter {

        void login(String name, String password);

    }

}
