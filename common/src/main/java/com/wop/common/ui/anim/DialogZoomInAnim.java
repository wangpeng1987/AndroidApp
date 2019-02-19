package com.wop.common.ui.anim;

import android.animation.ObjectAnimator;
import android.view.View;

import com.daimajia.androidanimations.library.BaseViewAnimator;

public class DialogZoomInAnim extends BaseViewAnimator {

    @Override
    protected void prepare(View target) {
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target, "scaleX", 0, 1.1f),
                ObjectAnimator.ofFloat(target, "scaleY", 0, 1.1f),
                ObjectAnimator.ofFloat(target, "alpha", 0, 1).setDuration(100)
        );
    }
}
