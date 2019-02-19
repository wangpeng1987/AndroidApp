package com.wop.common.core.db;

import com.wop.common.util.LogUtil;

import java.util.HashMap;

import javax.annotation.Nullable;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;

/**
 * Created by liuwenji on 2018/9/27.
 */

public class RealmHelper {

    private static final HashMap<String,RealmConfiguration> cacheMap = new HashMap<>();

    public static void initDefaultRealm(Object module, int version) {
        RealmConfiguration configuration =
                new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(version)
                .modules(module)
                .build();
        //Realm.setDefaultConfiguration(configuration);
        cacheMap.put(Realm.DEFAULT_REALM_NAME,configuration);
    }

    public static void initRealm(Object module, int version, String userId) {
        RealmConfiguration configuration =
                new RealmConfiguration.Builder()
                        .schemaVersion(version)
                        .deleteRealmIfMigrationNeeded()
                        .name(userId + ".realm")
                        .modules(module)
                        .build();
        //Realm.setDefaultConfiguration(configuration);
        cacheMap.put(userId,configuration);
    }

    public static Realm getDefaultRealm() {
        return Realm.getInstance(cacheMap.get(Realm.DEFAULT_REALM_NAME));
    }

    public static Realm getRealm(@Nullable String userId) {
        LogUtil.d("cacheMap"," size= " + cacheMap.size());
        if (cacheMap.size() < 2) {
            return Realm.getInstance(new RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()
                    .name(userId + ".realm")
                    .build());
        } else {
            return Realm.getInstance(cacheMap.get(userId));
        }
    }
}
