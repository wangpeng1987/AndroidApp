package com.wop.common.ui.anim;

import android.animation.ObjectAnimator;
import android.view.View;

import com.daimajia.androidanimations.library.BaseViewAnimator;

public class PageZoomOutAnim extends BaseViewAnimator {
    @Override
    protected void prepare(View target) {
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target, "scaleX", 1, 0.99f),
                ObjectAnimator.ofFloat(target, "scaleY", 1, 0.99f)
        );
    }
}
