package com.wop.common.ui.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.wop.common.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 有箭头的单列 (主要在设置，需要进入下一级界面使用)
 */
public class RowCommonArrow extends RelativeLayout {

    @BindView(R2.id.row_common_left_aciv)
    AppCompatImageView rowCommonLeftAciv;
    @BindView(R2.id.row_common_left_actv)
    AppCompatTextView rowCommonLeftActv;
    @BindView(R2.id.row_common_right_aciv)
    AppCompatImageView rowCommonRightAciv;
    @BindView(R2.id.row_common_right_actv)
    AutofitTextView rowCommonRightActv;

    public RowCommonArrow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RowCommonArrow init() {
        ButterKnife.bind(this);
        return this;
    }

    //设置左边的图标和文字 (在init 方法之后调用)
    public RowCommonArrow setLeftInfo(int leftRes,String leftText) {
        if (leftRes > 0) {
            rowCommonLeftAciv.setImageResource(leftRes);
        } else {
            rowCommonLeftAciv.setVisibility(GONE);
        }
        rowCommonLeftActv.setText(leftText);
        return this;
    }

    //设置右边的icon图标
    public RowCommonArrow setRightArrowIcon(int rightRes) {
        rowCommonRightAciv.setImageResource(rightRes);
        return this;
    }

    //设置右边的值
    public RowCommonArrow setRightValue(String rightValue) {
        rowCommonRightActv.setText(rightValue);
        return this;
    }
}
