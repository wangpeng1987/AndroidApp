package com.wop.serverdemo.core.base;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jaeger.library.StatusBarUtil;
import com.wop.common.ui.anim.helper.TransitionHelper;
import com.wop.serverdemo.R;

/**
 * Created by liuwenji on 2018/12/19.
 */

public abstract class BaseActivity extends SupportActivity {

    private String TAG = BaseActivity.class.getSimpleName();

    //    private static KProgressHUD mKProLoading;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (null != savedInstanceState) {
            Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext()
                    .getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            return;
        }
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(false);
        showStatusBar(Color.argb(50, 0, 0, 0));
        setTheme(R.style.AppTheme);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    //显示状态栏，隐藏虚拟键
    public void showStatusBar(int color) {
        Window window = getWindow();////Color.argb( 10,0, 0, 0)
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.setStatusBarColor(getResources().getColor(R.color.colour_8));
        } else {
            StatusBarUtil.setColor(this, color, 0);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    public void hideStatusBar() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.INVISIBLE
        );
    }

    @SuppressWarnings("unchecked")
    public void transitionTo(Intent i) {
        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(this, true);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairs);
        startActivity(i, transitionActivityOptions.toBundle());
    }

//    //显示loading 对话框
//    public void showKpLoading() {
//        //处于显示中,不在进行显示
//        if (mKProLoading != null && mKProLoading.isShowing()) return;
//        mKProLoading = KProgressHUD.create(this)
//                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
//        mKProLoading.show();
//        mKProLoading = null;
//    }

//    //隐藏loading 对话框
//    public void hiddenKpLoading() {
//        if (mKProLoading != null) mKProLoading.dismiss();
//    }

    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
