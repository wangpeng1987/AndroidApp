package com.wop.common.ui.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;

/**
 * Created by on 15/10/12.
 * 放大缩小的ImageView
 */
public class ZoomImageView extends AppCompatImageView {

    private String TAG = this.getClass().getSimpleName();
    private Animator anim1;
    private Animator anim2;
    private int mHeight;
    private int mWidth;
    private float mX, mY;
    private Handler mHandler = new Handler();

    private ClickListener listener;
    private boolean mIsCancel;
    private float mLastMotionX;
    private float mLastMotionY;
    private boolean clickable = true;

    public ZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        mWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        mX = getX();
        mY = getY();
    }

    private void init() {

        PropertyValuesHolder valueHolder_1 = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.2f);
        PropertyValuesHolder valuesHolder_2 = PropertyValuesHolder.ofFloat(
                "scaleY", 1f, 1.2f);
        anim1 = ObjectAnimator.ofPropertyValuesHolder(this, valueHolder_1, valuesHolder_2);
        anim1.setDuration(200);
        anim1.setInterpolator(new LinearInterpolator());

        PropertyValuesHolder valueHolder_3 = PropertyValuesHolder.ofFloat(
                "scaleX", 1.2f, 1f);
        PropertyValuesHolder valuesHolder_4 = PropertyValuesHolder.ofFloat(
                "scaleY", 1.2f, 1f);
        anim2 = ObjectAnimator.ofPropertyValuesHolder(this, valueHolder_3,
                valuesHolder_4);
        anim2.setDuration(100);
        anim2.setInterpolator(new LinearInterpolator());
    }

    public void setClickListener(ClickListener clickListener) {
        this.listener = clickListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!clickable) break;
                mLastMotionX = x;
                mLastMotionY = y;
                mIsCancel = false;
                getParent().requestDisallowInterceptTouchEvent(true);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        anim2.end();
                        anim1.start();
                    }
                });
                break;
            case MotionEvent.ACTION_MOVE:
                if (!clickable) break;

                if (!innerImageView(x, y) || Math.abs(x - mLastMotionX) > dpToPx(50) || Math.abs(y - mLastMotionY) > dpToPx(50)) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                    if (!mIsCancel) {
                        mHandler.post(new Runnable() {
                            @Override public void run() {
                                anim1.end();
                                anim2.start();
                            }
                        });
                    }
                    mIsCancel = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!clickable) break;
                if (!mIsCancel) {
                    mHandler.post(new Runnable() {
                        @Override public void run() {
                            anim1.end();
                            anim2.start();
                        }
                    });
                }
                if (!mIsCancel && listener != null) {
                    listener.onClick();
                }
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                mIsCancel = true;
                break;
        }
        return true;
    }

    int dpToPx(int dps) {
        return Math.round(getResources().getDisplayMetrics().density * dps);
    }

    //按下的点是否在View内
    protected boolean innerImageView(float x, float y) {
//        if (x >= 0 && x < mWidth) {
//            if (y >= 0 && y < mHeight) {
//                return true;
//            }
//        }
//        return false;
        return true;
    }

    /**
     *  点击事件处理回调　
     * @author renzhiqiang
     *
     */
    public interface ClickListener {

        void onClick();
    }

    public void setClickable(boolean click) {
        this.clickable = click;
    }

    @Override
    protected void onDetachedFromWindow() {
        // TODO Auto-generated method stub
        super.onDetachedFromWindow();
    }

    @Override public void setVisibility(int visibility) {
        super.setVisibility(visibility);
    }
}
