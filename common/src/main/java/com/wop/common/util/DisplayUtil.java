package com.wop.common.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import java.lang.reflect.Field;

/**
 * @author woniu
 * @title DisplayUtil
 * @description
 * @since 2017/11/23 16:37
 */
public final class DisplayUtil {

    private DisplayUtil() {
    }

    /**
     * 获取屏幕的宽度，单位：px
     *
     * @param context 上下文
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        return display.widthPixels;
    }

    /**
     * 获取屏幕的高度，单位：px
     *
     * @param context 上下文
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        return display.heightPixels;
    }

    /**
     * 获取屏幕的比例 w/h
     *
     * @return
     */
    public static float getScreenAspectRadio(Context context) {
        return ArithUtil.div(getScreenWidth(context), getScreenHeight(context));
    }

    /**
     * 获取布局的比例 w/h
     *
     * @return
     */
    public static float getLayoutAspectRadio(Context context) {
        int layoutHeight = getScreenWidth(context) - getStatusBarHeight(context);
        return ArithUtil.div(layoutHeight, getScreenHeight(context));
    }

    /**
     * 获取状态栏的高度。
     */
    public static int getStatusBarHeight(Context context) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object o = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (Integer) field.get(o);
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }


    /**
     * px值转换成dp
     *
     * @param px
     * @return
     */
    public static float pxToDp(float px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }

    /**
     * dp值转换成px
     *
     * @param dp
     * @return
     */
    public static float dpToPx(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    /**
     * convert px to its equivalent sp
     * <p>
     * 将px转换为sp
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    /**
     * convert sp to its equivalent px
     * <p>
     * 将sp转换为px
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * convert sp to its equivalent px
     * <p>
     * 将sp转换为px
     */
    public static int sp2px(float spValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * convert px to its equivalent sp
     * <p>
     * 将px转换为sp
     */
    public static int px2sp(float pxValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

}
