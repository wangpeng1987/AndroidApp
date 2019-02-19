package com.wop.common.ui.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.daimajia.androidanimations.library.YoYo;
import com.wop.common.R;
import com.wop.common.ui.anim.AvatarSlideInAnim;
import com.wop.common.ui.anim.AvatarSlideOutAnim;
import com.wop.common.util.LogUtil;

public class AvatarView extends RelativeLayout {

    private String TAG = AvatarView.class.getSimpleName();
    AppCompatImageView avatarPreAciv;//avatar_pre_aciv
    AppCompatImageView avatarAciv;//avatar_current_aciv

    public AvatarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    //控件大小由include 外部布局指定
    public AvatarView with() {
        avatarPreAciv = findViewById(R.id.avatar_pre_aciv);
        avatarAciv = findViewById(R.id.avata_aciv);
        return this;
    }

    //加载头像
    public void loadAvatar(Uri uri) {
        //如果pre view 和后一个view 都没有被加载过，用pre view 加载
        if (!avatarPreAciv.isShown() && !avatarAciv.isShown()) {
            LogUtil.d(TAG, "start load 上一个view");
            avatarPreAciv.setVisibility(VISIBLE);
            Glide.with(getContext())
                    .load(uri)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(avatarPreAciv);
        } else if (!avatarPreAciv.isShown() && avatarAciv.isShown()) {
            LogUtil.d(TAG, "后一个view加载了，start load 上一个view");
            //如果后一个view加载了，用前一个加载，并且开始做动画
            startLoad(uri,avatarAciv,avatarPreAciv);
        } else {
            LogUtil.d(TAG, "start load 后一个view");
            //pre view 加载了，用后一个view 加载，并且开始做动画
            startLoad(uri,avatarPreAciv,avatarAciv);
        }
    }

    private void startLoad(Uri uri, AppCompatImageView preView,AppCompatImageView view) {
        view.setVisibility(VISIBLE);
        Glide.with(getContext())
                .load(uri)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Drawable> target, boolean b) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable drawable, Object o, Target<Drawable> target, DataSource dataSource, boolean b) {
                        LogUtil.d(TAG, "onResourceReady");
                        view.setVisibility(GONE);
                        YoYo.with(new AvatarSlideOutAnim())
                                .duration(300)
                                .onEnd(animator -> {
                                    view.setVisibility(VISIBLE);
                                    preView.setVisibility(GONE);
                                    //做进入动画
                                    YoYo.with(new AvatarSlideInAnim())
                                            .duration(300)
                                            .playOn(view);
                                })
                                .playOn(preView);
                        return false;
                    }
                }).into(view);
    }
}
