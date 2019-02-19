package com.wop.common.ui.widget.helper;

import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.wop.common.ui.anim.transition.FadeTransition;
import com.wop.common.ui.widget.TouchTravelView;

public class TouchTravelHelper {

    private int targetWidth;
    private int targetHeight;
    private int targetLeft;
    private int targetTop;
    private TouchTravelView mTravelView;
    //view进入
    public void attachView(int targetWidth,int targetHeight,int targetLeft,int targetTop,TouchTravelView travelView) {
        this.targetWidth = targetWidth;
        this.targetHeight = targetHeight;
        this.targetLeft = targetLeft;
        this.targetTop = targetTop;
        this.mTravelView = travelView;
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)travelView.getLayoutParams();
        layoutParams.width = targetWidth;
        layoutParams.height = targetHeight;
        layoutParams.leftMargin = targetLeft;
        layoutParams.topMargin = targetTop;
        travelView.setLayoutParams(layoutParams);
        travelView.post(() -> {
            TransitionManager.beginDelayedTransition(travelView,buildEnterTransition());
            ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams)travelView.getLayoutParams();
            layoutParams.width = ViewGroup.MarginLayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.MarginLayoutParams.MATCH_PARENT;
            layoutParams.leftMargin = 0;
            layoutParams.topMargin = 0;
            travelView.setLayoutParams(marginParams);
        });
    }

    //view进入转场动画
    private Transition buildEnterTransition() {
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.addTransition(new FadeTransition(new LinearInterpolator(),0,1,1,1));
        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(200);
        transitionSet.addTransition(changeBounds);
        return transitionSet;
    }

    //view退出转场动画
    private Transition buildExitTransition() {
        TransitionSet transitionSet = new TransitionSet();
        float[] values;
        if (mTravelView.getStyle() == TouchTravelView.Style.NORMAL_TOUCH) {
            values = new float[] {1,1,0,0};
        } else {
            values = new float[] {1,1,1,0};
        }
        transitionSet.addTransition(new FadeTransition(new LinearInterpolator(),values));
        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(200);
        transitionSet.addTransition(changeBounds);
        transitionSet.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                if (mTravelView.getEndAnimPlayingListener() != null) {
                    mTravelView.getEndAnimPlayingListener().onEndAnimPlaying();
                }
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                if (mTravelView.getTraveFinishListener() != null) {
                    mTravelView.getTraveFinishListener().onFinish();
                }
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
        return transitionSet;
    }

    //view 拖动结束
    public void finishView() {
        int marginLeft = targetLeft - (int)mTravelView.getTranslationX();
        int marginTop = targetTop  - (int)mTravelView.getTranslationY();
        TransitionManager.beginDelayedTransition(mTravelView,buildExitTransition());
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)mTravelView.getLayoutParams();
        layoutParams.width = targetWidth;
        layoutParams.height = targetHeight;
        layoutParams.leftMargin = marginLeft;
        layoutParams.topMargin = marginTop;
        mTravelView.setLayoutParams(layoutParams);
    }
}
