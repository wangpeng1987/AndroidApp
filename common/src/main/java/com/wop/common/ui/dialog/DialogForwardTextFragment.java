package com.wop.common.ui.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wop.common.R;
import com.wop.common.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DialogForwardTextFragment extends BaseDialogFragment {

    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_BTN_TEXT = "btn_text";
    private static final String KEY_CANCEL_TEXT = "cancel_text";
    private static final String KEY_HINT = "hint";
    private static final String KEY_INPUT_TEXT = "input_text";

    @BindView(R2.id.dialog_title_actv)
    AppCompatTextView dialogTitleActv;
    @BindView(R2.id.dialog_content_actv)
    AppCompatTextView dialogContentActv;
    @BindView(R2.id.dialog_forward_text_acet)
    AppCompatEditText dialogForwardTextAcet;
    @BindView(R2.id.dialog_forward_text_actv)
    AppCompatTextView dialogForwardTextActv;
    @BindView(R2.id.dialog_cancel_actv)
    AppCompatTextView dialogCancelActv;

    private OnDismissListener mListener;
    @Override
    public View inflaterView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_dialog_forward_text, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        dialogTitleActv.setText(bundle.getString(KEY_TITLE));
        dialogContentActv.setText(bundle.getString(KEY_CONTENT));
        dialogForwardTextActv.setText(bundle.getString(KEY_BTN_TEXT));
        //输入框赋值
        if (!TextUtils.isEmpty(bundle.getString(KEY_INPUT_TEXT))) {
            dialogForwardTextAcet.setText(bundle.getString(KEY_INPUT_TEXT));
        }
        dialogForwardTextAcet.setHint(bundle.getString(KEY_HINT));
        //如果cancel 有值,进行显示，否则不显示
        if (!TextUtils.isEmpty(bundle.getString(KEY_CANCEL_TEXT))) {
            dialogCancelActv.setVisibility(View.VISIBLE);
            dialogCancelActv.setText(bundle.getString(KEY_CANCEL_TEXT));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mListener != null) mListener.dismiss();
    }

    //设置对话框消失监听事件
    public DialogForwardTextFragment setDismissListener(OnDismissListener listener) {
        this.mListener = listener;
        return this;
    }

    //点击back建是否允许取消
    public DialogForwardTextFragment setCance(boolean value) {
        setCancelable(value);
        return this;
    }

    @OnClick(R2.id.dialog_forward_text_actv)
    public void onClickBtn() {
        dismiss();
        if (mListener != null) mListener.clickBtn(dialogForwardTextAcet.getText().toString());
    }

    @OnClick(R2.id.dialog_cancel_actv)
    public void onClickCancel() {
        dismiss();
    }

    public static final class Builder {

        //标题
        private String mTitle = "";
        //内容
        private String mContent = "";
        //按钮文字
        private String mBtnText = "";
        //cancel文字
        private String mCancelText = "";
        //hint
        private String mHint = "";
        //输入框内容
        private String mInputText = "";

        public Builder setTitle(@NonNull String title) {
            this.mTitle = title;
            return this;
        }

        public Builder setContent(@NonNull String content) {
            this.mContent = content;
            return this;
        }

        public Builder setBtnText(@NonNull String btnText) {
            mBtnText = btnText;
            return this;
        }

        public Builder setCancelText(@NonNull String cancelText) {
            mCancelText = cancelText;
            return this;
        }

        public Builder setHint(@NonNull String hint) {
            mHint = hint;
            return this;
        }

        public Builder setInputText(@NonNull String inputText) {
            mInputText = inputText;
            return this;
        }

        public DialogForwardTextFragment build() {
            DialogForwardTextFragment fragment = new DialogForwardTextFragment();
            Bundle bundle = new Bundle();
            bundle.putString(KEY_TITLE, mTitle);
            bundle.putString(KEY_CONTENT, mContent);
            bundle.putString(KEY_BTN_TEXT, mBtnText);
            bundle.putString(KEY_CANCEL_TEXT,mCancelText);
            bundle.putString(KEY_HINT,mHint);
            bundle.putString(KEY_INPUT_TEXT,mInputText);
            fragment.setArguments(bundle);
            return fragment;
        }

        public Builder() {}
    }

    public interface OnDismissListener {
        void dismiss();
        void clickBtn(String inputContent);
    }
}
