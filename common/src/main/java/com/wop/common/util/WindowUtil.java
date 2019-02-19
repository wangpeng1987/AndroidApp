package com.wop.common.util;

import android.content.Context;

public class WindowUtil {

    public static float getScale(Context context) {
        LogUtil.d("dpi"," dpi= " + context.getResources().getDisplayMetrics().densityDpi);
        return context.getResources().getDisplayMetrics().scaledDensity;
    }

    public static int getWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
}
