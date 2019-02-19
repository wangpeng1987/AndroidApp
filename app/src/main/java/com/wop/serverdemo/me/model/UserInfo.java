package com.wop.serverdemo.me.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserInfo extends RealmObject {
    @PrimaryKey
    private String id = "";
    private String username = "";

}
