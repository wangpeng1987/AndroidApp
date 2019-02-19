package com.wop.common.ui.dialog;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.daimajia.androidanimations.library.YoYo;
import com.wop.common.R;
import com.wop.common.ui.anim.MenuSlideInAnim;
import com.wop.common.ui.anim.MenuSlideOutAnim;
import com.wop.common.ui.anim.helper.ZoomPageAnimHelper;
import com.wop.common.util.LogUtil;

public abstract class BaseBottomMenuFragment extends DialogFragment implements DialogInterface.OnKeyListener {

    private String TAG = BaseDialogFragment.class.getSimpleName();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.d(TAG,"onCreateView");
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.ActionSheetDialogStyle);
        Window window = getDialog().getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        //window.getAttributes().windowAnimations = R.style.BottomMenuAnimation;
        View decorView = getDialog().getWindow().getDecorView();
        decorView.setBackground(new ColorDrawable(Color.TRANSPARENT));
        decorView.setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes(layoutParams);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setOnKeyListener(this);
        return inflaterView(inflater,container);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
        new Handler().postDelayed(() -> {
            ZoomPageAnimHelper.zoomPreView(getActivity().getWindow().getDecorView());
            animateViewIn(getDialog().getWindow().getDecorView());
        },100);
    }

    @Override
    public void dismiss() {
        ZoomPageAnimHelper.rebackPreView();
        animateViewOut(getDialog().getWindow().getDecorView());
    }

    private void animateViewIn(View view) {
        YoYo.with(new MenuSlideInAnim())
                .duration(200)
                .repeat(0)
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(view);
    }

    private void animateViewOut(View view) {
        YoYo.with(new MenuSlideOutAnim())
                .duration(200)
                .repeat(0)
                .onEnd(animator -> {
                    super.dismiss();
                })
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(view);
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            dismiss();
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public abstract View inflaterView(LayoutInflater inflater, ViewGroup container);

}
