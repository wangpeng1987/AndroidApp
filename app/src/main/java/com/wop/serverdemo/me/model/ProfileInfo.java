package com.wop.serverdemo.me.model;

/**
 * @Description: 当前用户信息
 * @author: wop
 */
public class ProfileInfo {

    /**
     * user : {"created_at":1547120413410,"email":"406663624@qq.com","from":"","id":100004,"mcc":"86","phone":"18066745561","status":1,"updated_at":1547120413410,"username":"WANGPENG1"}
     * user_moji_profile : {}
     * user_profile : {"avatar":"http://www.pptok.com/wp-content/uploads/2012/08/xunguang-4.jpg","bio":"","brithday":0,"brithyear":1988,"created_at":1547120413215,"gender":0,"id":5,"nickname":"wp1111111111","scope":0,"updated_at":1547120413215,"user_id":100004}
     */

    private UserBean user;
    private UserMojiProfileBean user_moji_profile;
    private UserProfileBean user_profile;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public UserMojiProfileBean getUser_moji_profile() {
        return user_moji_profile;
    }

    public void setUser_moji_profile(UserMojiProfileBean user_moji_profile) {
        this.user_moji_profile = user_moji_profile;
    }

    public UserProfileBean getUser_profile() {
        return user_profile;
    }

    public void setUser_profile(UserProfileBean user_profile) {
        this.user_profile = user_profile;
    }

    public static class UserBean {
        /**
         * created_at : 1547120413410
         * email : 406663624@qq.com
         * from :
         * id : 100004
         * mcc : 86
         * phone : 18066745561
         * status : 1
         * updated_at : 1547120413410
         * username : WANGPENG1
         */

        private long created_at;
        private String email;
        private String from;
        private int id;
        private String mcc;
        private String phone;
        private int status;
        private long updated_at;
        private String username;

        public long getCreated_at() {
            return created_at;
        }

        public void setCreated_at(long created_at) {
            this.created_at = created_at;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMcc() {
            return mcc;
        }

        public void setMcc(String mcc) {
            this.mcc = mcc;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(long updated_at) {
            this.updated_at = updated_at;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    public static class UserMojiProfileBean {
    }

    public static class UserProfileBean {
        /**
         * avatar : http://www.pptok.com/wp-content/uploads/2012/08/xunguang-4.jpg
         * bio :
         * brithday : 0
         * brithyear : 1988
         * created_at : 1547120413215
         * gender : 0
         * id : 5
         * nickname : wp1111111111
         * scope : 0
         * updated_at : 1547120413215
         * user_id : 100004
         */

        private String avatar;
        private String bio;
        private int brithday;
        private int brithyear;
        private long created_at;
        private int gender;
        private int id;
        private String nickname;
        private int scope;
        private long updated_at;
        private int user_id;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public int getBrithday() {
            return brithday;
        }

        public void setBrithday(int brithday) {
            this.brithday = brithday;
        }

        public int getBrithyear() {
            return brithyear;
        }

        public void setBrithyear(int brithyear) {
            this.brithyear = brithyear;
        }

        public long getCreated_at() {
            return created_at;
        }

        public void setCreated_at(long created_at) {
            this.created_at = created_at;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getScope() {
            return scope;
        }

        public void setScope(int scope) {
            this.scope = scope;
        }

        public long getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(long updated_at) {
            this.updated_at = updated_at;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }
    }
}
