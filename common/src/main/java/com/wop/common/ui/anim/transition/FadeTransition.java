package com.wop.common.ui.anim.transition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.wop.common.util.LogUtil;

public class FadeTransition extends Transition {

    private static final String PROPNAME_BACKGROUND = "android:faderay:background";
    private static final String PROPNAME_TEXT_COLOR = "android:faderay:textColor";
    private static final String PROPNAME_ALPHA = "android:faderay:alpha";

    private TimeInterpolator timeInterpolator;

    float[] values;
    public FadeTransition(final TimeInterpolator timeInterpolator,@NonNull float... values) {
        this.values = values;
        this.timeInterpolator = timeInterpolator;
    }

    public FadeTransition(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    private void captureValues(final TransitionValues transitionValues,float alpha) {
        transitionValues.values.put(PROPNAME_BACKGROUND, transitionValues.view.getBackground());
        transitionValues.values.put(PROPNAME_ALPHA,alpha);//transitionValues.view.getAlpha()
    }

    @Override
    public void captureStartValues(final TransitionValues transitionValues) {
        LogUtil.d("FadeTransition","captureStartValues");
        captureValues(transitionValues,values[0]);
    }

    @Override
    public void captureEndValues(final TransitionValues transitionValues) {
        LogUtil.d("FadeTransition","captureEndValues");
        captureValues(transitionValues,values[values.length - 1]);
    }

    @SuppressLint("NewApi")
    @Override
    public Animator createAnimator(final ViewGroup sceneRoot, final TransitionValues startValues,
                                   final TransitionValues endValues) {
        View view = endValues.view;
        view.setAlpha(1.0f);
        ObjectAnimator fade;
        fade = ObjectAnimator.ofFloat(view, View.ALPHA, values);
        fade.setInterpolator(timeInterpolator);
        return fade;
    }
}
