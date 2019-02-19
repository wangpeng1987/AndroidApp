package com.wop.common.util;

import android.os.Bundle;

import java.util.List;
import java.util.Map;

/**
 * @author woniu
 * @title EmptyUtil
 * @description
 * @since 2018/1/25 下午4:42
 */
public class EmptyUtil {

    public static boolean isEmpty(Object object) {
        return null == object || "".equals(object);
    }

    public static boolean isNotEmpty(Object object) {
        return !isEmpty(object);
    }

    public static boolean isEmpty(byte[] byteArr) {
        return null == byteArr || byteArr.length <= 0;
    }

    public static boolean isNotEmpty(byte[] byteArr) {
        return !isEmpty(byteArr);
    }

    public static boolean isEmpty(String[] strArr) {
        return null == strArr || strArr.length <= 0;
    }

    public static boolean isNotEmpty(String[] strArr) {
        return !isEmpty(strArr);
    }

    public static boolean isEmpty(Map map) {
        return null == map || map.isEmpty();
    }

    public static boolean isNotEmpty(Map map) {
        return !isEmpty(map);
    }

    public static boolean isEmpty(List list) {
        return null == list || list.size() <= 0;
    }

    public static boolean isNotEmpty(List list) {
        return !isEmpty(list);
    }

    public static boolean isEmpty(Bundle bundle) {
        return null == bundle || bundle.isEmpty();
    }

    public static boolean isNotEmpty(Bundle bundle) {
        return !isEmpty(bundle);
    }

}
