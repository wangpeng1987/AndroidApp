package com.wop.serverdemo.core.utils;

import com.tencent.mmkv.MMKV;

public class MMKVUtils {

    public static void saveValue(String key, String value) {
        MMKV mmkv = MMKV.defaultMMKV();
        mmkv.encode(key, value);
    }

    /////////////////////////////////////////////////////////////////// 登录 信息 存储 ////////////////////////////////////////////////////////////////////

    private static final String cryptKey = "123boo123_logininfo";
    private static final String LOGININFO = "LoginInfo";

    public static void saveLoginInfoValue(String key, String value) {
        MMKV mmkv = MMKV.mmkvWithID(LOGININFO, MMKV.MULTI_PROCESS_MODE, cryptKey);
        mmkv.encode(key, value);
    }

    public static void saveLoginInfoValue(String key, boolean value) {
        MMKV mmkv = MMKV.mmkvWithID(LOGININFO, MMKV.MULTI_PROCESS_MODE, cryptKey);
        mmkv.encode(key, value);
    }

    public static void saveLoginInfoValue(String key, int value) {
        MMKV mmkv = MMKV.mmkvWithID(LOGININFO, MMKV.MULTI_PROCESS_MODE, cryptKey);
        mmkv.encode(key, value);
    }

    public static boolean getLoginInfoBool(String key) {
        MMKV mmkv = MMKV.mmkvWithID(LOGININFO, MMKV.MULTI_PROCESS_MODE, cryptKey);
        return mmkv.decodeBool(key);
    }

    public static int getLoginInfoInt(String key) {
        MMKV mmkv = MMKV.mmkvWithID(LOGININFO, MMKV.MULTI_PROCESS_MODE, cryptKey);
        return mmkv.decodeInt(key);
    }

    public static String getLoginInfoString(String key) {
        MMKV mmkv = MMKV.mmkvWithID(LOGININFO, MMKV.MULTI_PROCESS_MODE, cryptKey);
        return mmkv.decodeString(key);
    }

}
