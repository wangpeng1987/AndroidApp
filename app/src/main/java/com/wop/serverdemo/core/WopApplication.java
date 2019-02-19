package com.wop.serverdemo.core;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.tencent.mmkv.MMKV;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;
import com.wop.common.core.db.RealmHelper;
import com.wop.serverdemo.core.realmmodules.CustomModules;

import io.realm.Realm;

/**
 * Created by liuwenji on 2018/12/19.
 */

public class WopApplication extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        MMKV.initialize(this);// /data/user/0/com.nixi.boo/files/mmkv

        Realm.init(mContext);
        Stetho.initialize(
                Stetho.newInitializerBuilder(mContext)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(mContext))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(mContext).build())
                        .build());

        //以下为测试，有登录用户才能修改
//        RealmHelper.initDefaultRealm(new DefaultModules(),1);
        RealmHelper.initRealm(new CustomModules(), 1, "wop");

        Fresco.initialize(this);
    }

    public static Context getContext() {
        return mContext;
    }
}
