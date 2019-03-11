package com.wop.serverdemo.me.ui.book;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.wop.serverdemo.R;
import com.wop.serverdemo.core.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookActivity extends BaseActivity {

    @BindView(R.id.wb)
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        ButterKnife.bind(this);


        WebSettings webSettings = mWebView.getSettings();
        if (webSettings == null) return;
        // 支持 Js 使用
        webSettings.setJavaScriptEnabled(true);
        // 开启DOM缓存,默认状态下是不支持LocalStorage的
        webSettings.setDomStorageEnabled(true);
        // 开启数据库缓存
        webSettings.setDatabaseEnabled(true);
        // 支持自动加载图片
//        webSettings.setLoadsImagesAutomatically(hasKitkat());
        // 设置 WebView 的缓存模式
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 支持启用缓存模式
        webSettings.setAppCacheEnabled(true);
        // 设置 AppCache 最大缓存值(现在官方已经不提倡使用，已废弃)
        webSettings.setAppCacheMaxSize(8 * 1024 * 1024);
        // Android 私有缓存存储，如果你不调用setAppCachePath方法，WebView将不会产生这个目录
        webSettings.setAppCachePath(getCacheDir().getAbsolutePath());
        // 数据库路径
//        if (!hasKitkat()) {
//            webSettings.setDatabasePath(getDatabasePath("html").getPath());
//        }
        // 关闭密码保存提醒功能
        webSettings.setSavePassword(false);
        // 支持缩放
        webSettings.setSupportZoom(true);
        // 设置 UserAgent 属性
        webSettings.setUserAgentString("");
        // 允许加载本地 html 文件/false
        webSettings.setAllowFileAccess(true);
        // 允许通过 file url 加载的 Javascript 读取其他的本地文件,Android 4.1 之前默认是true，在 Android 4.1 及以后默认是false,也就是禁止
        webSettings.setAllowFileAccessFromFileURLs(false);
        // 允许通过 file url 加载的 Javascript 可以访问其他的源，包括其他的文件和 http，https 等其他的源，
        // Android 4.1 之前默认是true，在 Android 4.1 及以后默认是false,也就是禁止
        // 如果此设置是允许，则 setAllowFileAccessFromFileURLs 不起做用
        webSettings.setAllowUniversalAccessFromFileURLs(false);

        mWebView.loadUrl("http://www.17k.com/");// 加载url，也可以执行js函数
        mWebView.setWebViewClient(new SafeWebViewClient());// 设置 WebViewClient
        mWebView.setWebChromeClient(new SafeWebChromeClient());// 设置 WebChromeClient
//        mWebView.onResume();// 生命周期onResume
//        mWebView.resumeTimers();//生命周期resumeTimers
//        mWebView.onPause();//生命周期onPause
//        mWebView.pauseTimers();//生命周期pauseTimers (上数四个方法都是成对出现)
//        mWebView.stopLoading();// 停止当前加载
//        mWebView.clearMatches();// 清除网页查找的高亮匹配字符。
//        mWebView.clearHistory();// 清除当前 WebView 访问的历史记录
//        mWebView.clearSslPreferences();//清除ssl信息
//        mWebView.clearCache(true);//清空网页访问留下的缓存数据。需要注意的时，由于缓存是全局的，所以只要是WebView用到的缓存都会被清空，即便其他地方也会使用到。该方法接受一个参数，从命名即可看出作用。若设为false，则只清空内存里的资源缓存，而不清空磁盘里的。
//        mWebView.loadUrl("about:blank");// 清空当前加载
//        mWebView.removeAllViews();// 清空子 View
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
//            mWebView.removeJavascriptInterface("AndroidNative");// 向 Web端注入 java 对象
//        }
//        mWebView.destroy();// 生命周期销毁




    }
}
