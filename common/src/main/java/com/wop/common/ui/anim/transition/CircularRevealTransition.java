package com.wop.common.ui.anim.transition;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.wop.common.ui.anim.listener.AnimTransitionCallBack;
import com.wop.common.ui.anim.listener.AnimTransitionListener;

public class CircularRevealTransition extends Transition {

    private int centerX;
    private int centerY;
    private float startRadius;
    private float endRadius;
    private float startAlpha;
    private float endAlpha;

    public CircularRevealTransition() {
    }

    public CircularRevealTransition setCenterXY(int centerX,int centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
        return this;
    }

    public CircularRevealTransition setRadius(float startRadius,float endRadius) {
        this.startRadius = startRadius;
        this.endRadius = endRadius;
        return this;
    }

    public CircularRevealTransition setAlpha(float startAlpha,float endAlpha) {
        this.startAlpha = startAlpha;
        this.endAlpha = endAlpha;
        return this;
    }

    public CircularRevealTransition addAnimEndListener(AnimTransitionCallBack listener) {
        addListener(new AnimTransitionListener() {
            @Override
            public void onTransitionEnd(Transition transition) {
                listener.call();
            }
        });
        return this;
    }

    public CircularRevealTransition addAnimStartListener(AnimTransitionCallBack listener) {
        addListener(new AnimTransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                listener.call();
            }
        });
        return this;
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        transitionValues.values.put("radius",startRadius);
        transitionValues.values.put("alpha",startAlpha);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        transitionValues.values.put("radius",endRadius);
        transitionValues.values.put("alpha",endAlpha);
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        AnimatorSet animatorSet = new AnimatorSet();
        Animator anim = ViewAnimationUtils.createCircularReveal(endValues.view, centerX, centerY, startRadius, endRadius);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(endValues.view, "alpha", startAlpha, endAlpha);
        alphaAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.playTogether(alphaAnim,anim);
        return animatorSet;
    }
}
