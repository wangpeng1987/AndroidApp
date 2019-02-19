package com.wop.common.ui.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;

import com.wop.common.R;

/**
 * 矩形输入框
 */
public class RectSearchEditView extends RelativeLayout {

    private Style mStyle;
    private int mLeftRes;
    private String mHint;
    private int mHintColor;
    private OnClickViewListener mListener;
    private OnSearchListener mSearchListener;

    private AppCompatImageView rectEditSearchAciv;
    private AppCompatEditText rectEditAcet;
    private AppCompatImageView rectEditCancelAciv;

    public RectSearchEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //实例化对象
    public RectSearchEditView init(Style style) {
        this.mStyle = style;
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_search_rect_edit,null);
        rectEditSearchAciv = view.findViewById(R.id.rect_edit_search_aciv);
        rectEditAcet = view.findViewById(R.id.rect_edit_acet);
        rectEditCancelAciv = view.findViewById(R.id.rect_edit_cancel_aciv);
        if (style == Style.NOT_INPUT) {
            rectEditAcet.setFocusable(false);
            rectEditAcet.setOnClickListener(v -> {
                if (mListener != null) mListener.onClick();
            });
            setOnClickListener(v -> {
                if (mListener != null) mListener.onClick();
            });
            addView(view);
        } else if (style == Style.INPUT) {
            rectEditAcet.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 0) {
                        rectEditCancelAciv.setVisibility(VISIBLE);
                    } else {
                        rectEditCancelAciv.setVisibility(INVISIBLE);
                    }
                }
            });
            rectEditAcet.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mSearchListener.onSearch(rectEditAcet.getText().toString());
                }
                return false;
            });
            rectEditCancelAciv.setVisibility(INVISIBLE);
            //X 图标点击
            rectEditCancelAciv.setOnClickListener(v -> rectEditAcet.setText(""));
            addView(view);
        }
        return this;
    }

    //设置输入框左边图标样式 （init 方法之后调用）
    public RectSearchEditView setLeftIcon(int resource) {
        this.mLeftRes = resource;
        rectEditSearchAciv.setImageResource(resource);
        return this;
    }

    //init 方法之后调用
    public RectSearchEditView setHint(String hint, int hintColor) {
        this.mHint = hint;
        this.mHintColor = hintColor;
        rectEditAcet.setHint(hint);
        rectEditAcet.setHintTextColor(hintColor);
        return this;
    }

    public RectSearchEditView setCancelIcon(int resource) {
        rectEditCancelAciv.setImageResource(resource);
        return this;
    }

    public RectSearchEditView setClickViewListener(OnClickViewListener listener) {
        this.mListener = listener;
        return this;
    }

    //设置search的点击事件
    public RectSearchEditView setOnSearchListener(OnSearchListener listener) {
        this.mSearchListener = listener;
        return this;
    }

    //获取输入框的内容
    public String getInputContent() {
        return rectEditAcet.getText().toString();
    }

    //不输入模式下, 点击click view
    public  interface OnClickViewListener {
        void onClick();
    }

    //输入模式下，点击cancel 按钮
    public interface OnSearchListener {
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
