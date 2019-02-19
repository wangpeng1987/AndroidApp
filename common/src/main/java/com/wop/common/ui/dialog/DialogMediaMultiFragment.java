package com.wop.common.ui.dialog;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wop.common.R;
import com.wop.common.R2;
import com.wop.common.util.ImageLoader;
import com.wop.common.util.WindowUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DialogMediaMultiFragment extends BaseDialogFragment  {

    private static final String KEY_TOP_MEDIA_URI = "top_media_uri";
    private static final String KEY_TOP_MEDIA_TYPE = "top_media_type";
    private static final String KEY_TOP_MEDIA_WIDTH = "top_media_width";
    private static final String KEY_TOP_MEDIA_HEIGHT = "top_media_height";
    private static final String KEY_MEDIA_DIALOG_STYLE = "dialog_style";

    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_ITEMS = "items";
    private static final String KEY_CANCEL = "cancel";

    @BindView(R2.id.dialog_title_actv)
    AppCompatTextView dialogTitleActv;
    @BindView(R2.id.dialog_content_actv)
    AppCompatTextView dialogContentActv;
    @BindView(R2.id.dialog_cancel_actv)
    AppCompatTextView dialogCancelActv;
    @BindView(R2.id.dialog_rv)
    RecyclerView dialogRv;
    @BindView(R2.id.dialog_top_media_aciv)
    SimpleDraweeView dialogTopMediaAciv;
    @BindView(R2.id.rootView)
    LinearLayout rootView;

    private static OnDismissListener mListener;
    private ItemAdapter itemAdapter;

    @Override
    public View inflaterView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_dialog_media_multi, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        dialogTitleActv.setText(bundle.getString(KEY_TITLE));
        dialogContentActv.setText(bundle.getString(KEY_CONTENT));
        itemAdapter = new ItemAdapter(bundle.getParcelableArrayList(KEY_ITEMS));
        dialogRv.setLayoutManager(new LinearLayoutManager(getContext()));
        dialogRv.setAdapter(itemAdapter);
        if (!TextUtils.isEmpty(bundle.getString(KEY_CANCEL))) {
            dialogCancelActv.setVisibility(View.VISIBLE);
            dialogCancelActv.setText(bundle.getString(KEY_CANCEL));
        }
        int dialogStyle = bundle.getInt(KEY_MEDIA_DIALOG_STYLE);
        //小的对话框
        if (dialogStyle == DialogMediaSingleFragment.DialogStyle.SMALL.ordinal()) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rootView.getLayoutParams();
            params.leftMargin = (int)(57 * WindowUtil.getScale(getContext()));
            params.rightMargin = (int)(57 * WindowUtil.getScale(getContext()));
            rootView.setLayoutParams(params);
        } else if (dialogStyle == DialogMediaSingleFragment.DialogStyle.LARGE.ordinal()) {//大的对话框
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rootView.getLayoutParams();
            params.leftMargin = (int)(34 * WindowUtil.getScale(getContext()));
            params.rightMargin = (int)(34 * WindowUtil.getScale(getContext()));
            rootView.setLayoutParams(params);
        }
        int screenWidth = WindowUtil.getWidth(getContext());
        int viewWidth = (int)(screenWidth - 20.0f * WindowUtil.getScale(getContext()));//宽充满
        int viewHeight = (int)(viewWidth / 2.0f);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(viewWidth,viewHeight);
        layoutParams.topMargin = (int)(22.0f * WindowUtil.getScale(getContext()));
        dialogTopMediaAciv.setLayoutParams(layoutParams);

        //加载media 资源
        if (!TextUtils.isEmpty(bundle.getString(KEY_TOP_MEDIA_URI))) {
            int mediaType = bundle.getInt(KEY_TOP_MEDIA_TYPE);
            if (mediaType == DialogMediaSingleFragment.DialogMediaType.GIF.ordinal()) {

            } else if (mediaType == DialogMediaSingleFragment.DialogMediaType.IMG.ordinal()) {

            } else if (mediaType == DialogMediaSingleFragment.DialogMediaType.WEBP.ordinal()) {

            }
            ImageLoader.loadGif(getActivity(),dialogTopMediaAciv,Uri.parse(bundle.getString(KEY_TOP_MEDIA_URI)));

            int mediaWidth = bundle.getInt(KEY_TOP_MEDIA_WIDTH);
            int mediaHeight = bundle.getInt(KEY_TOP_MEDIA_HEIGHT);
            //如果传入宽高参数都大于0,进行动态适配
            if (mediaWidth > 0 && mediaHeight > 0) {
                //宽大于高，宽充满，计算高比列
                if (mediaWidth > mediaHeight) {
                    viewWidth = (int)(screenWidth - 20.0f * WindowUtil.getScale(getContext()));//宽充满
                    viewHeight = (int)(viewWidth * (mediaHeight * 1.0f / mediaWidth));//计算高的宽度
                } else {
                    viewHeight = (int)(screenWidth - 20.0f * WindowUtil.getScale(getContext())) / 2;//高充满
                    viewWidth = (int)(viewHeight * (mediaWidth * 1.0f / mediaHeight));//计算高的宽度
                }
                layoutParams = new LinearLayout.LayoutParams(viewWidth,viewHeight);
                layoutParams.topMargin = (int)(22.0f * WindowUtil.getScale(getContext()));
                dialogTopMediaAciv.setLayoutParams(layoutParams);
            }
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

    public DialogMediaMultiFragment setDismissListener(OnDismissListener listener) {
        this.mListener = listener;
        return this;
    }

    //点击back建是否允许取消
    public DialogMediaMultiFragment setCance(boolean value) {
        setCancelable(value);
        return this;
    }

    @OnClick(R2.id.dialog_cancel_actv)
    public void onClick() {
        dismiss();
        if (mListener != null) mListener.dismiss();
    }

    public static final class Builder {

        //标题
        private String mTitle = "";
        //内容
        private String mContent = "";
        //按钮集合
        private ArrayList<Item> mItems = new ArrayList<>();
        //cancel 按钮
        private String mCancel = "";
        //uri
        private String mUri = "";
        //媒体类型
        private DialogMediaType mMediaType = DialogMediaType.NONE;

        //宽和高
        private int mWidth = 0;
        private int mHeight = 0;
        //dialog style: 区分是大的和小的对话框
        private DialogStyle mDialogStyle = DialogStyle.SMALL;

        public Builder setTitle(@NonNull String title) {
            this.mTitle = title;
            return this;
        }

        public Builder setContent(@NonNull String content) {
            this.mContent = content;
            return this;
        }

        public Builder setItems(@NonNull ArrayList<Item> items) {
            this.mItems = items;
            return this;
        }

        public Builder setCancelText(@NonNull String cancel) {
            this.mCancel = cancel;
            return this;
        }

        //设置弹框顶部的媒体类型的 uri
        public Builder setTopMediaUri(@NonNull String uri, @NonNull DialogMediaType mediaType) {
            mUri = uri;
            mMediaType = mediaType;
            return this;
        }

        //设置媒体类型的宽高，非必须
        public Builder setMediaParams(int width, int height) {
            mWidth = width;
            mHeight = height;
            return this;
        }

        public Builder setDialogStyle(DialogStyle dialogStyle) {
            mDialogStyle = dialogStyle;
            return this;
        }

        public DialogMediaMultiFragment build() {
            DialogMediaMultiFragment fragment = new DialogMediaMultiFragment();
            Bundle bundle = new Bundle();
            bundle.putString(KEY_TITLE, mTitle);
            bundle.putString(KEY_CONTENT, mContent);
            bundle.putParcelableArrayList(KEY_ITEMS,mItems);
            bundle.putString(KEY_CANCEL, mCancel);
            bundle.putString(KEY_TOP_MEDIA_URI,mUri);
            bundle.putInt(KEY_TOP_MEDIA_TYPE,mMediaType.ordinal());
            bundle.putInt(KEY_TOP_MEDIA_WIDTH,mWidth);
            bundle.putInt(KEY_TOP_MEDIA_HEIGHT,mHeight);
            bundle.putInt(KEY_MEDIA_DIALOG_STYLE,mDialogStyle.ordinal());
            fragment.setArguments(bundle);
            return fragment;
        }

        public Builder() {
        }

        public static class Item implements Parcelable {
            public String name = "";
            public int color = -1;

            public Item(String name) {
                this.name = name;
                this.color = color;
            }

            public Item(String name, int color) {
                this.name = name;
                this.color = color;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(name);
                dest.writeInt(color);
            }

            public static final Creator<Item> CREATOR = new Creator<Item>() {

                @Override
                public Item createFromParcel(Parcel source) {
                    return new Item(source.readString(), source.readInt());
                }

                @Override
                public Item[] newArray(int size) {
                    return new Item[size];
                }
            };
        }
    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

        private List<Builder.Item> mItems;
        public ItemAdapter(List<Builder.Item> items) {
            this.mItems = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_dialog_common_multi, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.dialog_item_actv.setText(mItems.get(i).name);
            if (mItems.get(i).color != -1) viewHolder.dialog_item_actv.setTextColor(mItems.get(i).color);
            viewHolder.itemView.setOnClickListener(v -> clickItem(i));
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView dialog_item_actv;
            public ViewHolder(View v) {
                super(v);
                dialog_item_actv = v.findViewById(R.id.dialog_item_actv);
            }
        }
    }

    private  void clickItem(int position) {
        dismiss();
        if (mListener != null) mListener.clickItem(position);
    }

    public interface OnDismissListener {
        void dismiss();
        void clickItem(int position);
    }

    public enum DialogStyle {
        LARGE,
        SMALL
    }

    public enum DialogMediaType {
        NONE,
        IMG,
        GIF,
        WEBP
    }
}
