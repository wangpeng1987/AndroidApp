package com.wop.serverdemo.core.realmmodules;

import com.wop.serverdemo.me.model.UserInfo;

import io.realm.annotations.RealmModule;

/**
 * Created by liuwenji on 2018/10/17.
 * 注意和用户相关的表，需要添加在这个Modules里
 */
@RealmModule(classes = {UserInfo.class})
public class CustomModules {

}