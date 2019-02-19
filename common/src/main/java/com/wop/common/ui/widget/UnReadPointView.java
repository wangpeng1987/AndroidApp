package com.wop.common.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.wop.common.util.WindowUtil;

/**
 * 未读红点数封装
 */
public class UnReadPointView extends AppCompatTextView {

    private Style mStyle;
    private int mValue;
    public UnReadPointView(Context context) {
        super(context);
    }

    public UnReadPointView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setStyle(Style style) {
        setStyle(style,0);
    }

    public void setStyle(@NonNull Style style, int value) {
        this.mStyle = style;
        this.mValue = value;
        final ViewGroup.LayoutParams lp = getLayoutParams();
        if (mStyle == Style.NONE_NUM_POINT) {//无数字风格
            lp.width = (int)(9 * WindowUtil.getScale(getContext()));
            lp.height = lp.width;
        } else {
            lp.width = (int)(20 * WindowUtil.getScale(getContext()));
            lp.height = lp.width;
            setText(String.valueOf(mValue));
        }
        setLayoutParams(lp);
    }

    //枚举有两种，一种为有数字的，一种是没有数字的
    public enum Style {
        NONE_NUM_POINT,
        NUM_POINT
    }
}
