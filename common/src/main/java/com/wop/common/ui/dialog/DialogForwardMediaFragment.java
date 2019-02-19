package com.wop.common.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.wop.common.R;
import com.wop.common.R2;
import com.wop.common.util.WindowUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
 * 转发的是 媒体相关的，比如：图片，视频，gif，wepb等
 */
public class DialogForwardMediaFragment extends BaseDialogFragment {

    private static final String KEY_TITLE = "title";
    private static final String KEY_BTN_TEXT = "btn_text";
    private static final String KEY_CANCEL_TEXT = "cancel_text";
    private static final String KEY_HINT = "hint";
    private static final String KEY_INPUT_TEXT = "input_text";

    private static final String KEY_MEDIA_TYPE = "media_type";
    private static final String KEY_URI = "uri";
    private static final String KEY_THUMB_WIDTH = "width";
    private static final String KEY_THUMB_HEIGHT = "height";

    @BindView(R2.id.dialog_title_actv)
    AppCompatTextView dialogTitleActv;
    @BindView(R2.id.dialog_cancel_actv)
    AppCompatTextView dialogCancelActv;

    @BindView(R2.id.dialog_forward_media_aciv)
    AppCompatImageView dialogForwardMediaAciv;
    @BindView(R2.id.dialog_forward_video_tag_aciv)
    AppCompatImageView dialogForwardVideoTagAciv;
    @BindView(R2.id.dialog_forward_media_acet)
    AppCompatEditText dialogForwardMediaAcet;
    @BindView(R2.id.dialog_forward_media_actv)
    AppCompatTextView dialogForwardMediaActv;


    private OnDismissListener mListener;

    @Override
    public View inflaterView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_dialog_forward_media, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        dialogTitleActv.setText(bundle.getString(KEY_TITLE));
        dialogForwardMediaActv.setText(bundle.getString(KEY_BTN_TEXT));
        //输入框赋值
        if (!TextUtils.isEmpty(bundle.getString(KEY_INPUT_TEXT))) {
            dialogForwardMediaAcet.setText(bundle.getString(KEY_INPUT_TEXT));
        }
        dialogForwardMediaAcet.setHint(bundle.getString(KEY_HINT));
        //如果cancel 有值,进行显示，否则不显示
        if (!TextUtils.isEmpty(bundle.getString(KEY_CANCEL_TEXT))) {
            dialogCancelActv.setVisibility(View.VISIBLE);
            dialogCancelActv.setText(bundle.getString(KEY_CANCEL_TEXT));
        }

        int mediaType = bundle.getInt(KEY_MEDIA_TYPE);

        if (mediaType == ForwardMediaType.IMG.ordinal()) {//是图片

        } else if (mediaType == ForwardMediaType.VIDEO.ordinal()) {//是视频

        } else if (mediaType == ForwardMediaType.GIF.ordinal()) {//是Gif

        } else if (mediaType == ForwardMediaType.WEBP.ordinal()) {//是webp

        }

        int thumbWidth = bundle.getInt(KEY_THUMB_WIDTH);
        int thumbHeight = bundle.getInt(KEY_THUMB_HEIGHT);

        int viewWidth;
        int viewHeight;

        //宽大于高，宽充满，计算高比列
        if (thumbWidth > thumbHeight) {
            viewWidth = (int)(110 * WindowUtil.getScale(getContext()));//宽充满
            viewHeight = (int)(viewWidth * (thumbHeight * 1.0f / thumbWidth));//计算高的宽度
        } else {
            viewHeight = (int)(110 * WindowUtil.getScale(getContext()));//宽充满
            viewWidth = (int)(viewHeight * (thumbWidth * 1.0f / thumbHeight));//计算高的宽度
        }
        dialogForwardMediaAciv.setLayoutParams(new RelativeLayout.LayoutParams(viewWidth,viewHeight));
        dialogForwardMediaAcet.requestFocus();
        dialogForwardMediaAcet.postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }, 200);
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
        if (mListener != null) {
            mListener.dismiss();
        }
    }

    //设置对话框消失监听事件
    public DialogForwardMediaFragment setDismissListener(OnDismissListener listener) {
        this.mListener = listener;
        return this;
    }

    //点击back建是否允许取消
    public DialogForwardMediaFragment setCance(boolean value) {
        setCancelable(value);
        return this;
    }

    @OnClick(R2.id.dialog_forward_media_actv)
    public void onClickBtn() {
        dismiss();
        if (mListener != null) mListener.clickBtn(dialogForwardMediaAcet.getText().toString());
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
        //转发的类型
        private ForwardMediaType mMediaType = ForwardMediaType.NONE;
        //uri (本地 或 url)
        private String mUri = "";
        //缩略图宽高
        private int mThumbWidth = 0;
        private int mThumbHeight = 0;

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

        //设置user card上的信息
        public Builder setMediaInfo(@NonNull ForwardMediaType mediaType, @NonNull String uri, int thumbWidth, int thumbHeight) {
            this.mMediaType = mediaType;
            this.mUri = uri;
            this.mThumbWidth = thumbWidth;
            this.mThumbHeight = thumbHeight;
            return this;
        }

        public DialogForwardMediaFragment build() {
            DialogForwardMediaFragment fragment = new DialogForwardMediaFragment();
            Bundle bundle = new Bundle();
            bundle.putString(KEY_TITLE, mTitle);
            bundle.putString(KEY_BTN_TEXT, mBtnText);
            bundle.putString(KEY_CANCEL_TEXT, mCancelText);
            bundle.putString(KEY_HINT, mHint);
            bundle.putString(KEY_INPUT_TEXT, mInputText);
            bundle.putInt(KEY_MEDIA_TYPE, mMediaType.ordinal());
            bundle.putString(KEY_URI, mUri);
            bundle.putInt(KEY_THUMB_WIDTH, mThumbWidth);
            bundle.putInt(KEY_THUMB_HEIGHT, mThumbHeight);
            fragment.setArguments(bundle);
            return fragment;
        }

        public Builder() {
        }
    }

    public interface OnDismissListener {
        void dismiss();

        void clickBtn(String inputContent);
    }

    public enum ForwardMediaType {
        NONE,
        IMG,
        VIDEO,
        GIF,
        WEBP
    }
}
