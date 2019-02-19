package com.wop.common.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.wop.common.R;

public class RoundCornerRadioBox extends LinearLayout {

    AppCompatTextView roundCornerRadioLeftActv;
    AppCompatTextView roundCornerRadioCenterActv;
    AppCompatTextView roundCornerRadioRightActv;

    private String[] tabs;
    private RoundCornerRadioBoxListener listener;

    private int leftBgRes;
    private int leftBgCheckedRes;
    private int leftTextColor;
    private int leftTextCheckedColor;

    private int centerBgRes;
    private int centerBgCheckedRes;
    private int centerTextColor;
    private int centerTextCheckedColor;

    private int rightBgRes;
    private int rightBgCheckedRes;
    private int rightTextColor;
    private int rightTextCheckedColor;

    public RoundCornerRadioBox(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    //注意：目前strings 支持最大值是3个,最小值是2个
    public void load(RoundCornerRadioBoxListener listener, @NonNull String[] strings, int index) {//index 初始化需要显示第几个tab
        this.listener = listener;
        roundCornerRadioLeftActv = findViewById(R.id.round_corner_radio_left_actv);
        roundCornerRadioCenterActv = findViewById(R.id.round_corner_radio_center_actv);
        roundCornerRadioRightActv = findViewById(R.id.round_corner_radio_right_actv);
        tabs = strings;
        switchTab(index);
        addListener();
    }

    //切换tab
    public void switchTab(int index) {
        if (isNoNullTab()) {
            roundCornerRadioLeftActv.setText(tabs[0]);
            roundCornerRadioCenterActv.setText(tabs[tabs.length - 2]);
            roundCornerRadioRightActv.setText(tabs[tabs.length - 1]);
            if (tabs.length > 2) {
                roundCornerRadioCenterActv.setVisibility(VISIBLE);
            } else {
                roundCornerRadioCenterActv.setVisibility(GONE);
            }
            refreshTabUI(index);
        }
    }

    private void addListener() {
        roundCornerRadioLeftActv.setOnClickListener(v -> {
            switchTab(0);
            if (listener != null) listener.onClickLeft();
        });
        roundCornerRadioCenterActv.setOnClickListener(v -> {
            switchTab(1);
            if (listener != null) listener.onClickCenter();
        });
        roundCornerRadioRightActv.setOnClickListener(v -> {
            switchTab(tabs.length - 1);
            if (listener != null) listener.onClickRight();
        });
    }

    //如果是匿名聊天，boomoji tab 不选择
    public void refreshNoEnableUI() {
        roundCornerRadioRightActv.setClickable(false);
        roundCornerRadioRightActv.setEnabled(false);
//        roundCornerRadioRightActv.setTextColor(getContext().getResources().getColor(R.color.m3329CE85));
//        roundCornerRadioRightActv.setBackgroundResource(R.drawable.round_half_green_right_03);
    }

    private void refreshTabUI(int index) {
        //默认隐藏中间的
        roundCornerRadioRightActv.setClickable(true);
        roundCornerRadioRightActv.setEnabled(true);
        if (index == 0) {//left
            roundCornerRadioLeftActv.setTextColor(leftTextCheckedColor);
            roundCornerRadioRightActv.setTextColor(rightTextColor);
            roundCornerRadioLeftActv.setBackgroundResource(leftBgCheckedRes);
            roundCornerRadioRightActv.setBackgroundResource(rightBgRes);
            roundCornerRadioCenterActv.setTextColor(centerTextColor);
            roundCornerRadioCenterActv.setBackgroundResource(centerBgRes);
        } else if (index > 0 && index < tabs.length - 1) {//center
            roundCornerRadioLeftActv.setTextColor(leftTextColor);
            roundCornerRadioRightActv.setTextColor(rightTextColor);
            roundCornerRadioLeftActv.setBackgroundResource(leftBgRes);
            roundCornerRadioRightActv.setBackgroundResource(rightBgRes);
            roundCornerRadioCenterActv.setTextColor(centerTextCheckedColor);
            roundCornerRadioCenterActv.setBackgroundResource(centerBgCheckedRes);
        } else if (index == tabs.length - 1) {//right
            roundCornerRadioLeftActv.setTextColor(leftTextColor);
            roundCornerRadioRightActv.setTextColor(rightTextCheckedColor);
            roundCornerRadioLeftActv.setBackgroundResource(leftBgRes);
            roundCornerRadioRightActv.setBackgroundResource(rightBgCheckedRes);
            roundCornerRadioCenterActv.setTextColor(centerTextColor);
            roundCornerRadioCenterActv.setBackgroundResource(centerBgRes);
        }
    }

    /**
     * 设置左边按钮的一些属性信息: 背景颜色，选中背景颜色,字体颜色,选中字体颜色
     */
    public RoundCornerRadioBox setLeftView(int bgRes,int bgCheckedRes,int textColor,int textCheckedColor) {
        this.leftBgRes = bgRes;
        this.leftBgCheckedRes = bgCheckedRes;
        this.leftTextColor = textColor;
        this.leftTextCheckedColor = textCheckedColor;
        return this;
    }

    /**
     * 设置中间按钮的一些属性信息: 背景颜色，选中背景颜色,字体颜色,选中字体颜色
     */
    public RoundCornerRadioBox setCenterView(int bgRes,int bgCheckedRes,int textColor,int textCheckedColor) {
        this.centerBgRes = bgRes;
        this.centerBgCheckedRes = bgCheckedRes;
        this.centerTextColor = textColor;
        this.centerTextCheckedColor = textCheckedColor;
        return this;
    }

    /**
     * 设置右边按钮的一些属性信息: 背景颜色，选中背景颜色,字体颜色,选中字体颜色
     */
    public RoundCornerRadioBox setRightView(int bgRes,int bgCheckedRes,int textColor,int textCheckedColor) {
        this.rightBgRes = bgRes;
        this.rightBgCheckedRes = bgCheckedRes;
        this.rightTextColor = textColor;
        this.rightTextCheckedColor = textCheckedColor;
        return this;
    }

    //非空tab
    private boolean isNoNullTab() {
        if (tabs == null || tabs.length == 0) {
            setVisibility(GONE);
            return false;
        }
        return true;
    }

    public interface RoundCornerRadioBoxListener {
        void onClickLeft();
        void onClickCenter();
        void onClickRight();
    }

}
