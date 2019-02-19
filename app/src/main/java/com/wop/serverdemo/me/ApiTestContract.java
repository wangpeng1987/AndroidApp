package com.wop.serverdemo.me;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public interface ApiTestContract {

    interface View {

        void showUser(JSONObject jsonObject);

        void onError(String msg);

    }

    interface Presenter {

//        void getUserEncode1(String uri);
//
////        void getUserEncode0();
//
//        void postUserEncode1();
//
//        void post404();

        void login(String name, String password);

        void sigup(String email, String username, String password, String from, String mcc, int brithyear, String avatar, String phone);

        void getPhoneCode(String phone, String mcc);

        void getPhoneVerify(String code, String phone, String mcc);

        void setUserName(String username);

        void setPhone(String phone, String mcc, String code);

        void getEmailCode(String email);

        void getEmailVerify(String passcode, String ticket);

        void logout();

        void sync();

        void profile();

        void userInfo(ArrayList ids);

        /**
         * @Description: 改啥传啥，别的传 空
         */
        void setProfile(String avatar, String nickname, String bio, String gender, String scope);

        void getForgetPassword(String phone, String mcc, String email);

        void resetPassword(String ticket, String passcode, String new_password);

        void changePassword(String new_password, String old_password);

        void setting(Map<String, Integer> params);

        void getSetting();

        void setBlock(ArrayList<Integer> blocks);

        void getBlock();

        void delBlock(ArrayList<Integer> blocks);
    }

}
