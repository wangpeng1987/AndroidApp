package com.wop.common.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Region;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.wop.common.util.LogUtil;
import com.wop.common.ui.widget.helper.TouchTravelHelper;


public class TouchTravelView extends RelativeLayout {

    public static final String TAG = TouchTravelView.class.getSimpleName();
    //拖动的三种状态
    public static final int STATUS_NORMAL = 0;
    public static final int STATUS_MOVING = 1;
    public static final int STATUS_REBACK = 2;
    //最多可缩小比例
    public static final float MIN_SCALE_WEIGHT = 0.5f;//0.25f
    //恢复原位需要的时间
    public static final int REBACK_DURATION = 300;//ms
    //最小拖动距离
    public static int DRAG_GAP_PX = 50;
    //拖动距离 大于该值，执行finish
    public static int DRAG_MAX_PX;

    private int currentStatus = STATUS_NORMAL;
    //记录按下的X,Y坐标
    float mDownX;
    float mDownY;
    //是否是多点触摸
    private boolean isMultiPoint;
    //是否是单点
    private boolean isTouchDown;
    //是否是双击
    private boolean isDoubleTip;
    //记录按下和抬起的时间
    private long touchTime;
    //记录最后按下的时间
    private long lastDownTime;
    //速度帮助类
    protected VelocityTracker mVelocityTracker;
    //是否是长按
    protected boolean isLongPress;
    private int screenHeight;
    //触摸滑动view 内部帮助类。处理相关动画等操作
    private TouchTravelHelper mTravelHelper = new TouchTravelHelper();
    //回调长按事件
    private OnTouchTraveListener mLongClickListener;
    //回调结束事件
    private OnTouchTraveListener mTraveFinishListener;
    //回调界面消失时，动画开始执行事件
    private OnTouchTraveListener mEndAnimPlayingListener;
    private Handler mHandler = new Handler();
    private Runnable traveRunnable;

    //拖动状态下需要的一些比例参数
    float scale = 1f;
    float alphaPercent = 1f;

    //圆形style下需要的参数
    private final Path path = new Path();
    float radius;

    //默认为矩形style
    private Style mStyle = Style.NORMAL_TOUCH;

    public TouchTravelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        screenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        isMultiPoint = false;
        isLongPress = false;
        lastDownTime = -1l;
        currentStatus = STATUS_NORMAL;
        //最多移动到屏幕1/4 位置
        DRAG_MAX_PX = getContext().getResources().getDisplayMetrics().heightPixels - getContext().getResources().getDisplayMetrics().heightPixels / 4;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try{
            return super.onInterceptTouchEvent(ev);
        }catch(IllegalArgumentException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean dispatchTouchEvent(final MotionEvent ev) {
        LogUtil.d(TAG , "currentStatus= " + currentStatus + " action= " + ev.getActionMasked());
        //如果是reback状态，不进行手势操作
        if (currentStatus == STATUS_REBACK)
            return false;
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                LogUtil.d(TAG,"ACTION_DOWN");
                if (currentStatus == STATUS_MOVING) return false;
                //判断是否是双击
                if (lastDownTime > 0 && System.currentTimeMillis() - lastDownTime < 300) {
                    isDoubleTip = true;
                } else {
                    isDoubleTip = false;
                }
                touchTime = System.currentTimeMillis();
                lastDownTime = System.currentTimeMillis();
                isMultiPoint = false;
                isTouchDown = true;
                mDownX = ev.getRawX();
                mDownY = ev.getRawY();
                addIntoVelocity(ev);

                //处理长按逻辑
                if (traveRunnable != null) mHandler.removeCallbacks(traveRunnable);
                traveRunnable = (TraveRunnable) () -> {
                    if (mLongClickListener != null && System.currentTimeMillis() - touchTime > 500) {//&& dragPhotoView != null 500 毫秒内，没有触发其它事件，做长按的逻辑处理，为了避免和双击冲突
                        isLongPress = true;
                        mLongClickListener.onLongClick();
                    }
                };
                if (currentStatus != STATUS_MOVING) {//不在滑动中。
                    mHandler.postDelayed(traveRunnable,500);//500毫秒没有其它操作，算是长按状态
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN://多点触摸
                LogUtil.d(TAG,"ACTION_POINTER_DOWN");
                if (isLongPress) return false;
                if (traveRunnable != null) mHandler.removeCallbacks(traveRunnable);
                isMultiPoint = true;
                for (int i = 0; i < getChildCount(); i++) {
                    getChildAt(i).dispatchTouchEvent(ev);
                }
                return false;
            case MotionEvent.ACTION_MOVE:
                LogUtil.d(TAG,"ACTION_MOVE");
                final float moveX = ev.getRawX();
                final float moveY = ev.getRawY();
                if (!isMove(mDownX,mDownY,moveX,moveY) || isLongPress) {
                    return super.dispatchTouchEvent(ev);
                }
                isTouchDown = false;
                if (traveRunnable != null) mHandler.removeCallbacks(traveRunnable);
                if (isMultiPoint ) {//|| (dragPhotoView != null && dragPhotoView.getScale() != 1.0f && !dragPhotoView.isEndY())是多点触摸，并且没有放大的图，没有滚动到Y最边沿
                    for (int i = 0; i < getChildCount(); i++) {
                        getChildAt(i).dispatchTouchEvent(ev);
                    }
                    return false;
                } else {
                    addIntoVelocity(ev);
                    int deltaY = (int) (ev.getRawY() - mDownY);
                    if (deltaY <= DRAG_GAP_PX && currentStatus != STATUS_MOVING)
                        return super.dispatchTouchEvent(ev);
                    if ((deltaY > DRAG_GAP_PX || currentStatus == STATUS_MOVING)) {//currentPageStatus!=SCROLL_STATE_DRAGGING &&
                        setupMoving(ev.getRawX(),ev.getRawY());
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                LogUtil.d(TAG,"ACTION_UP");
                touchTime = System.currentTimeMillis();
                if (isLongPress) return false;
                if (traveRunnable != null) mHandler.removeCallbacks(traveRunnable);
                final float upX = ev.getRawX();
                final float upY = ev.getRawY();
                if (currentStatus == STATUS_MOVING) {
                    float mUpX = ev.getRawX();//->mDownX
                    float mUpY = ev.getRawY();//->mDownY
                    float vY = computeYVelocity();
                    if (vY >= 1500 || Math.abs(mUpY-mDownY) > screenHeight / 4) {//速度有一定快，或者移动位置超过屏幕一半，那么释放 vY >= 1500||
                        traveFinish();
                    }else {
                        setupReback(mUpX,mUpY);
                    }
                } else if (currentStatus!=STATUS_MOVING && isClick(mDownX,mDownY,upX,upY) && !isMultiPoint) {
                    isTouchDown = false;
                    new Handler().postDelayed(() -> {
                        if(!isTouchDown  && !isDoubleTip && !isMultiPoint) {
                            traveFinish();
                        }
                    },500);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                LogUtil.d(TAG,"ACTION_CANCEL");
                if (isLongPress) return false;
                isTouchDown = false;
                if (traveRunnable != null) mHandler.removeCallbacks(traveRunnable);
                if (currentStatus != STATUS_MOVING)
                    return super.dispatchTouchEvent(ev);
                float mUpX = ev.getRawX();//->mDownX
                float mUpY = ev.getRawY();//->mDownY
                if (Math.abs(mUpY-mDownY) > screenHeight / 4){
                    traveFinish();
                }else {
                    setupReback(mUpX,mUpY);
                }
                break;
            case MotionEvent.ACTION_POINTER_UP://多指触摸抬起
                LogUtil.d(TAG,"ACTION_POINTER_UP");
                if (isLongPress) return false;
                if (traveRunnable != null) mHandler.removeCallbacks(traveRunnable);
                isMultiPoint = false;
                break;
        }
        return true;
    }

    /**
     * 是否是move 手势
     */
    private boolean isMove(float start_x,float start_y,float up_x,float up_y) {
        float scale = getContext().getResources().getDisplayMetrics().scaledDensity;
        int space_size = (int)(10.0f * scale);
        if (Math.abs(up_x - start_x) > space_size || Math.abs(up_y - start_y) > space_size) {
            return true;
        }
        return false;
    }

    /**
     * 是否是单点
     */
    private boolean isClick(float start_x,float start_y,float up_x,float up_y) {
        float scale = getContext().getResources().getDisplayMetrics().scaledDensity;
        int space_size = (int)(10.0f * scale);
        if (Math.abs(up_x - start_x) < space_size && Math.abs(up_y - start_y) < space_size) {
            return true;
        }
        return false;
    }

    /**
     * 手势取消，恢复到原位
     */
    private void setupReback(final float mUpX, final float mUpY){
        currentStatus = STATUS_REBACK;
        this.scale = 1.0f;
        float scale = getContext().getResources().getDisplayMetrics().scaledDensity;
        int space_size = (int)(10.0f * scale);
        if (Math.abs(mUpY - mDownY) > space_size) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(mUpY, mDownY);
            valueAnimator.setDuration(REBACK_DURATION);
            valueAnimator.addUpdateListener(animation -> {
                float mY = (float) animation.getAnimatedValue();
                float percent = (mY - mDownY) / (mUpY - mDownY);
                float mX = percent * (mUpX - mDownX) + mDownX;
                setupMoving(mX, mY);
                if (mY == mDownY) {
                    mDownY = 0;
                    mDownX = 0;
                    currentStatus = STATUS_NORMAL;
                }
            });
            valueAnimator.start();
        }else if (Math.abs(mUpX - mDownX) > space_size){
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(mUpX, mDownX);
            valueAnimator.setDuration(REBACK_DURATION);
            valueAnimator.addUpdateListener(animation -> {
                float mX = (float) animation.getAnimatedValue();
                float percent = (mX - mDownX) / (mUpX - mDownX);
                float mY = percent * (mUpY - mDownY) + mDownY;
                setupMoving(mX, mY);
                if (mX == mDownX) {
                    mDownY = 0;
                    mDownX = 0;
                    currentStatus = STATUS_NORMAL;
                }
            });
            valueAnimator.start();
        }
//        else if (mListener !=null) {
//            mListener.onPictureClick(currentShowView,mime_type);
//        }
        //iAnimClose.onPictureRebackMove();
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        LogUtil.d(TAG , " drawChild currentStatus = " + currentStatus);
        if (mStyle == Style.NORMAL_TOUCH || currentStatus == STATUS_NORMAL) return super.drawChild(canvas, child, drawingTime);
        try {
            canvas.save();
            return clipChild(canvas, child)
                    & super.drawChild(canvas, child, drawingTime);
        } finally {
            canvas.restore();
        }
    }

    //手势移动
    private void setupMoving(float movingX , float movingY) {
        currentStatus = STATUS_MOVING;
        if (mStyle == Style.NORMAL_TOUCH) {//矩形缩放
            float deltaX = movingX - mDownX;
            float deltaY = movingY - mDownY;
            float alphaPercent = 1f;
            scale = 1;
            if(deltaY>0) {
                scale = 1 - Math.abs(deltaY) / screenHeight;
                alphaPercent = 1- Math.abs(deltaY) / (screenHeight/2);
            }
            setTranslationX(deltaX);
            setTranslationY(deltaY);
            setupScale(scale);
            setupBackground(alphaPercent);
        } else {//圆形缩放
            float deltaX = movingX - mDownX;
            float deltaY = movingY - mDownY;
            if(deltaY > 0) {
                scale = 1 - (Math.abs(deltaY) / getHeight() * 3f);
                if (scale <= 0.4f) {
                    scale = 0.4f;
                } else {
                    alphaPercent = 1 - Math.abs(deltaY) / (screenHeight / 2);
                }
                setTranslationX(deltaX);
                setTranslationY(deltaY);
//                setScaleX(scale);
//                setScaleY(scale);
            }
            setupBackground(alphaPercent);
            invalidate();
        }
        //iAnimClose.onPictureDrag();
    }

    //绘制圆形区域
    private boolean clipChild(Canvas canvas, View child) {
        int viewCenterX = getWidth() / 2;//getLeft()
        int viewCenterY =  getHeight() / 2;//getTop()
        path.reset();
        radius = getHeight() * scale / 2;
        LogUtil.d(TAG , " left= " + getLeft() + " width= " + getWidth() + " top= " + getTop() + " height= " + getHeight());
        path.addCircle(viewCenterX, viewCenterY, radius,
                Path.Direction.CW);
        canvas.clipPath(path, Region.Op.INTERSECT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            child.invalidateOutline();
        }
        return false;
    }

    //拖动设置scale 比例
    private void setupScale(float scale) {
        scale = Math.min(Math.max(scale, MIN_SCALE_WEIGHT), 1);
        setScaleX(scale);
        setScaleY(scale);
    }

    protected void addIntoVelocity(MotionEvent event) {
        if (mVelocityTracker == null)
            mVelocityTracker = VelocityTracker.obtain();
        mVelocityTracker.addMovement(event);
    }

    protected float computeYVelocity() {
        float result = 0;
        if (mVelocityTracker != null) {
            mVelocityTracker.computeCurrentVelocity(1000);
            result = mVelocityTracker.getYVelocity();
            releaseVelocity();
        }
        return result;
    }

    protected void releaseVelocity() {
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    //父控件下加alpha渐变
    protected void setupBackground(float percent) {
        ((ViewGroup)getParent()).setBackgroundColor(convertPercentToBlackAlphaColor(percent));
    }

    //计算alpha渐变
    protected int convertPercentToBlackAlphaColor(float percent) {
        percent = Math.min(1, Math.max(0, percent));
        int intAlpha = (int) (percent * 255);
        String stringAlpha = Integer.toHexString(intAlpha).toLowerCase();
        String color = "#" + (stringAlpha.length() < 2 ? "0" : "") + stringAlpha + "000000";
        return Color.parseColor(color);
    }

    //设置style
    public TouchTravelView setStyle(Style style) {
        this.mStyle = style;
        return this;
    }

    //获取style
    public Style getStyle() {
        return mStyle;
    }

    //添加长按事件
    public TouchTravelView addLongClickListener(OnTouchTraveCallBack listener) {
       this.mLongClickListener = new OnTouchTraveListener() {
           @Override
           public void onLongClick() {
               listener.call();
           }
       };
       return this;
    }

    //添加触摸结束事件
    public TouchTravelView addTravelFinishListener(OnTouchTraveCallBack listener) {
        this.mTraveFinishListener = new OnTouchTraveListener() {
            @Override
            public void onFinish() {
                listener.call();
            }
        };
        return this;
    }

    public TouchTravelView addEndAnimPlayingListener(OnTouchTraveCallBack listener) {
        this.mEndAnimPlayingListener = new OnTouchTraveListener() {
            @Override
            public void onEndAnimPlaying() {
                listener.call();
            }
        };
        return this;
    }

    public OnTouchTraveListener getTraveFinishListener() {
        return mTraveFinishListener;
    }

    public OnTouchTraveListener getEndAnimPlayingListener() {
        return mEndAnimPlayingListener;
    }

    /**
     *和 ui的生命周期绑定 ()
     * @param targetWidth  进入view的宽度
     * @param targetHeight 进入view的高度
     * view 在屏幕左上角的坐标
     * @param targetLeft
     * @param targetTop
     */
    public TouchTravelView create(int targetWidth,int targetHeight,int targetLeft,int targetTop) {
        mTravelHelper.attachView(targetWidth,targetHeight,targetLeft,targetTop,this);
        return this;
    }

    /**
     * view 结束
     */
    public void traveFinish() {
        mTravelHelper.finishView();
    }

    /**
     * 定义回调接口
     */
    public interface OnTouchTraveCallBack {
        void call();
    }

    /**
     * 定义接口
     */
    public interface OnTouchTraveInterface {
        void onLongClick();
        void onFinish();
        void onEndAnimPlaying();
    }

    public static class OnTouchTraveListener implements OnTouchTraveInterface {
        @Override
        public void onLongClick() {}
        @Override
        public void onFinish() {}
        @Override
        public void onEndAnimPlaying() {}
    }

    public interface TraveRunnable extends Runnable {}

    public enum Style {
        NORMAL_TOUCH,//拖拽为矩形拖拽
        CIRCLE_TOUCH//
    }
}
