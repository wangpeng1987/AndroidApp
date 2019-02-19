package com.wop.common.ui.dialog;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
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
import android.widget.TextView;

import com.wop.common.R;
import com.wop.common.R2;
import com.wop.common.util.WindowUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuBottomFragment extends BaseBottomMenuFragment {

    private static final String KEY_TITLE = "title";
    private static final String KEY_ITEMS = "items";
    private static final String KEY_CANCEL = "cancel";

    @BindView(R2.id.dialog_title_actv)
    AppCompatTextView dialogTitleActv;
    @BindView(R2.id.dialog_cancel_actv)
    AppCompatTextView dialogCancelActv;
    @BindView(R2.id.dialog_rv)
    RecyclerView dialogRv;
    private static OnDismissListener mListener;
    private ItemAdapter itemAdapter;

    @Override
    public View inflaterView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_bottom_menu, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        //如果设置title 值的话，view显示，否则隐藏
        if (TextUtils.isEmpty(bundle.getString(KEY_TITLE))) {
            dialogTitleActv.setVisibility(View.GONE);
        } else {
            dialogTitleActv.setVisibility(View.VISIBLE);
            dialogTitleActv.setText(bundle.getString(KEY_TITLE));
        }
        dialogCancelActv.setText(bundle.getString(KEY_CANCEL));
        itemAdapter = new ItemAdapter(bundle.getParcelableArrayList(KEY_ITEMS));
        dialogRv.setLayoutManager(new LinearLayoutManager(getContext()));
        dialogRv.setAdapter(itemAdapter);
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

    public MenuBottomFragment setDismissListener(OnDismissListener listener) {
        this.mListener = listener;
        return this;
    }

    //点击back建是否允许取消
    public MenuBottomFragment setCance(boolean value) {
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
        //按钮集合
        private ArrayList<Item> mItems = new ArrayList<>();
        //cancel
        private String mCancel = "";

        public Builder setTitle(@NonNull String title) {
            this.mTitle = title;
            return this;
        }

        public Builder setItems(@NonNull ArrayList<Item> items) {
            this.mItems = items;
            return this;
        }

        public Builder setCancel(@NonNull String cancel) {
            this.mCancel = cancel;
            return this;
        }

        public MenuBottomFragment build() {
            MenuBottomFragment fragment = new MenuBottomFragment();
            Bundle bundle = new Bundle();
            bundle.putString(KEY_TITLE, mTitle);
            bundle.putParcelableArrayList(KEY_ITEMS,mItems);
            bundle.putString(KEY_CANCEL,mCancel);
            fragment.setArguments(bundle);
            return fragment;
        }

        public Builder() {
        }

        public static class Item implements Parcelable {
            public String name = "";
            public int color = -1;
            public int mLeftResource = -1;
            public Item(String name) {
                this.name = name;
                this.color = -1;
            }

            //color 为 -1 , 为默认
            public Item(String name, int color) {
                this.name = name;
                this.color = color;
            }

            //color 为 -1 , 为默认，leftResource 为 -1，不显示
            public Item(String name,int color,int leftResource) {
                this.name = name;
                this.color = color;
                this.mLeftResource = leftResource;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(name);
                dest.writeInt(color);
                dest.writeInt(mLeftResource);
            }

            public static final Creator<Item> CREATOR = new Creator<Item>() {

                @Override
                public Item createFromParcel(Parcel source) {
                    return new Item(source.readString(), source.readInt(),source.readInt());
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
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_bottom_menu, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.dialog_item_actv.setText(mItems.get(i).name);
            if (mItems.get(i).color != -1) viewHolder.dialog_item_actv.setTextColor(mItems.get(i).color);
//            if (mItems.get(i).leftResource != -1) viewHolder.dialog_item_actv.setCompoundDrawables(
//                    getResources().getDrawable(mItems.get(i).leftResource),null,null,null);

            if (mItems.get(i).mLeftResource != -1) {
                Drawable drawable = getResources().getDrawable(mItems.get(i).mLeftResource);
                drawable.setBounds(0, 0, (int)(40 * WindowUtil.getScale(getActivity())), (int)(40 * WindowUtil.getScale(getActivity())));
                viewHolder.dialog_item_actv.setCompoundDrawables(drawable
                        ,null,null,null);
            }
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickItem(i);
                }
            });
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

    private void clickItem(int position) {
        dismiss();
        if (mListener != null) mListener.clickItem(position);
    }

    public interface OnDismissListener {
        void dismiss();
        void clickItem(int index);
    }
}
