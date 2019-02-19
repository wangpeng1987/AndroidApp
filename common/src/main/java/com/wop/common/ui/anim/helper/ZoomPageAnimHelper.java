package com.wop.common.ui.anim.helper;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.daimajia.androidanimations.library.YoYo;
import com.wop.common.ui.anim.PageZoomInAnim;
import com.wop.common.ui.anim.PageZoomOutAnim;

/**
 * 缩放activity 或者 fragment root view.
 * 实现的是：当进入下级界面，上个界面要有缩小动画，当回到上个界面，动画要恢复到原位
 */
public class ZoomPageAnimHelper {

    private static View mPreView;//保留上一个view

    //缩小上一个view
    public static void zoomPreView(@NonNull View preView) {
        mPreView = preView;
        YoYo.with(new PageZoomOutAnim())
                .duration(200)
                .repeat(0)
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(mPreView);
    }

    //恢复上一个view动画
    public static void rebackPreView() {
        if (mPreView == null) return;
        if (mPreView.getScaleX() != 1) {
            YoYo.with(new PageZoomInAnim())
                    .duration(200)
                    .repeat(0)
                    .interpolate(new AccelerateDecelerateInterpolator())
                    .onEnd(animator -> {
                        mPreView = null;
                    })
                    .playOn(mPreView);
        }
    }
}
