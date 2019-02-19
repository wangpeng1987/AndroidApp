package com.wop.serverdemo.me.ui.register;

import com.alibaba.fastjson.JSONObject;

public interface RegisterContract {

    interface View {

        void onRegisterSuccess(JSONObject jsonObject);

        void onRegisterError(String msg);

    }

    interface Presenter {

        void sigup(String email, String username, String password, String mcc, String avatar, String phone);

    }

}
