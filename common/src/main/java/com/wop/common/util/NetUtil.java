package com.wop.common.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * @author woniu
 * @title NetUtil
 * @description
 * @since 2018/9/17 上午10:08
 */
public class NetUtil {

    /**
     * 没有网络
     */
    public static final int NET_WORK_TYPE_INVALID = 0;
    /**
     * wap网络
     */
    public static final int NET_WORK_TYPE_WAP = 1;
    /**
     * 2G网络
     */
    public static final int NET_WORK_TYPE_2G = 2;
    /**
     * 3G和3G以上网络，或统称为快速网络
     */
    public static final int NET_WORK_TYPE_3G = 3;
    /**
     * wifi网络
     */
    public static final int NET_WORK_TYPE_WIFI = 4;

//    public static boolean isNetworkConnected() {
//        return isNetworkConnected();
//    }

    public static boolean isNetworkConnected(Context context) {
        boolean isAvailable = false;
        if (context != null) {
            int type = getNetWorkType(context);
            if (type == NET_WORK_TYPE_INVALID) {
                isAvailable = false;
            } else if (type == NET_WORK_TYPE_WAP) {
                // 1,wap网络;
                isAvailable = true;
            } else if (type == NET_WORK_TYPE_2G) {
                // 2,2G网络;
                isAvailable = true;
            } else if (type == NET_WORK_TYPE_3G) {
                // 3,3G和3G以上网络，或统称为快速网络;
                isAvailable = true;
            } else if (type == NET_WORK_TYPE_WIFI) {
                // 4,wifi网络
                isAvailable = true;
            }
        }
        return isAvailable;
    }

    /**
     * 获取网络状态，wifi,wap,2g,3g.
     *
     * @param context 上下文
     * @return int 网络状态
     */
    @SuppressLint("MissingPermission")
    public static int getNetWorkType(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                return NET_WORK_TYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                String proxyHost = android.net.Proxy.getDefaultHost();
                if (TextUtils.isEmpty(proxyHost)) {
                    if (isFastMobileNetwork(context)) {
                        return NET_WORK_TYPE_3G;
                    } else {
                        return NET_WORK_TYPE_2G;
                    }
                } else {
                    return NET_WORK_TYPE_WAP;
                }
            }
        }
        return NET_WORK_TYPE_INVALID;
    }

    private static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true; // ~ 1-2 Mbps
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true; // ~ 5 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true; // ~ 10-20 Mbps
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false; // ~25 kbps
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true; // ~ 10+ Mbps
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
        }
    }

}
