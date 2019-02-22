package com.wop.serverdemo.me.model;

/**
 * @Description: login 返回 的 数据 data
 * @author: wop
 */
public class LoginData {


    /**
     * username : WANGPENG1
     * password : wwwwww
     * email : 40666@qq.com
     * phone : 13089897878
     * mcc : 86
     * avatar : http://www.pptok.com/wp-content/uploads/2012/08/xunguang-4.jpg
     */

    private String username;
    private String password;
    private String email;
    private String phone;
    private String mcc;
    private String avatar;
    private String token;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
