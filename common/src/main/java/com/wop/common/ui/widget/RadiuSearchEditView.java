package com.wop.common.ui.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;

import com.wop.common.R;

/**
 * 半圆形输入框
 */
public class RadiuSearchEditView extends RelativeLayout {

    private Style mStyle;
    private int mLeftRes;
    private String mHint;
    private int mHintColor;

    private OnClickViewListener mListener;
    private OnSearchListener mSearchListener;

    private AppCompatImageView radiuEditSearchAciv;
    private AppCompatEditText radiuEditAcet;
    private AppCompatTextView radiuEditCancelActv;

    public RadiuSearchEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //实例化对象
    public RadiuSearchEditView init(Style style) {
         this.mStyle = style;
         if (style == Style.NOT_INPUT) {
             View view = LayoutInflater.from(getContext()).inflate(R.layout.view_search_radiu_edit,null);
             radiuEditSearchAciv = view.findViewById(R.id.radiu_edit_search_aciv);
             radiuEditAcet = view.findViewById(R.id.radiu_edit_acet);
             radiuEditAcet.setFocusable(false);
             radiuEditAcet.setOnClickListener(v -> {
                 if (mListener != null) mListener.onClick();
             });
             setOnClickListener(v -> {
                 if (mListener != null) mListener.onClick();
             });
             addView(view);
         } else if (style == Style.INPUT) {
             View view = LayoutInflater.from(getContext()).inflate(R.layout.view_search_radiu_input_edit,null);
             radiuEditSearchAciv = view.findViewById(R.id.radiu_input_edit_search_aciv);
             radiuEditAcet = view.findViewById(R.id.radiu_input_edit_acet);
             radiuEditCancelActv = view.findViewById(R.id.radiu_input_edit_cancel_actv);
             radiuEditAcet.setOnEditorActionListener((v, actionId, event) -> {
                 if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                     mSearchListener.onSearch(radiuEditAcet.getText().toString());
                 }
                 return false;
             });
             radiuEditCancelActv.setOnClickListener(v -> {
                 if (mSearchListener != null) mSearchListener.onCancel();
             });
             addView(view);
         }
         return this;
    }

    //设置输入框左边图标样式 （init 方法之后调用）
    public RadiuSearchEditView setLeftIcon(int resource) {
        this.mLeftRes = resource;
        radiuEditSearchAciv.setImageResource(resource);
        return this;
    }

    //init 方法之后调用
    public RadiuSearchEditView setHint(String hint, int hintColor) {
        this.mHint = hint;
        this.mHintColor = hintColor;
        radiuEditAcet.setHint(hint);
        radiuEditAcet.setHintTextColor(hintColor);
        return this;
    }

    public RadiuSearchEditView setCancelText(String cancelText) {
        radiuEditCancelActv.setText(cancelText);
        return this;
    }

    //不可编辑状态下，设置view的点击事件
    public RadiuSearchEditView setClickViewListener(OnClickViewListener listener) {
        this.mListener = listener;
        return this;
    }

    //设置search的点击事件
    public RadiuSearchEditView setOnSearchListener(OnSearchListener listener) {
        this.mSearchListener = listener;
        return this;
    }

    //获取输入框的内容
    public String getInputContent() {
        return radiuEditAcet.getText().toString();
    }

    //不输入模式下, 点击click view
    public  interface OnClickViewListener {
        void onClick();
    }

    //输入模式下，点击cancel 按钮
    public interface OnSearchListener {
        void onCancel();
        void onSearch(String inputContent);
    }
    /**
     * 分不需要输入和需要输入两种样式
     */
    public enum Style {
        NOT_INPUT,
        INPUT
    }
}
