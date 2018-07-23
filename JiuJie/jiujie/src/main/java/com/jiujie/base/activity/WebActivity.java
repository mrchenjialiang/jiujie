package com.jiujie.base.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jiujie.base.R;
import com.jiujie.base.util.UIHelper;


/**
 * WebView页面，需要传参 String url,String title
 *
 * @author ChenJiaLiang
 */
public class WebActivity extends BaseActivity {

    private WebView webView;
    private String title;
    private String url;

    @Override
    public void initUI() {

        getIntentData();
        initTitle();
        initWebView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
            webView.resumeTimers();
        }
    }

    @Override
    protected void onDestroy() {
        webView.setVisibility(View.GONE);
        webView.loadUrl("about:blank");
        ViewParent parent = webView.getParent();
        if (parent != null && parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeAllViews();
        }
        if (webView != null) {
            webView.removeAllViews();
            webView.destroy();
        }
        super.onDestroy();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
        webView.pauseTimers();

        AudioManager systemService = (AudioManager) getSystemService(
                Context.AUDIO_SERVICE);
        if (systemService == null) return;
        systemService.requestAudioFocus(
                new OnAudioFocusChangeListener() {
                    @Override
                    public void onAudioFocusChange(int focusChange) {
                    }
                }, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        url = intent.getStringExtra("url");
    }

    private void initTitle() {
        if (!TextUtils.isEmpty(title)) {
            mTitle.setTitleText(title);
        }
        mTitle.setLeftButtonBack();
    }

    @Override
    public boolean isShowTitle() {
        return true;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        webView = findViewById(R.id.webView1);
        webView.setClickable(true);
        webView.setInitialScale((int) 0.1);
        webView.setWebViewClient(new webViewClient());

        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDefaultTextEncodingName("UTF-8");
        ws.setUseWideViewPort(true);
        ws.setSupportZoom(true);
        ws.setBuiltInZoomControls(true);
        ws.setLoadWithOverviewMode(true);
//		ws.setDefaultZoom(ZoomDensity.FAR);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 240) {
            ws.setDefaultZoom(ZoomDensity.FAR);
        } else if (mDensity == 160) {
            ws.setDefaultZoom(ZoomDensity.MEDIUM);
        } else if (mDensity == 120) {
            ws.setDefaultZoom(ZoomDensity.CLOSE);
        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
            ws.setDefaultZoom(ZoomDensity.FAR);
        } else if (mDensity == DisplayMetrics.DENSITY_TV) {
            ws.setDefaultZoom(ZoomDensity.FAR);
        } else {
            ws.setDefaultZoom(ZoomDensity.MEDIUM);
        }


        webView.loadUrl(url);
    }

    @Override
    public void finish() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        if (view != null) view.removeAllViews();
        super.finish();
    }

    class webViewClient extends WebViewClient {
        //重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。
//		@Override
//		public boolean shouldOverrideUrlLoading(WebView view, String url) {
//			view.loadUrl(url);
//			//如果不需要其他对点击链接事件的处理返回true，否则返回false
//			return true;
//		}
        //以上设置，会导致重定向的页面goBack回定向前的页面，然后再次重定向到该页面，导致goBack不了，得在加载出来前再次goBack才能退出，
        //4.0以后，只需重写该方法，返回false，则不将重定向的页面另做一个历史记录存储，2.3需要loadUrl,return true
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
                return false;
            } else {
                view.loadUrl(url);
                return true;
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            UIHelper.showLog("onPageStarted:"+url);
        }
    }

    @Override
    public void onBackPressed() {
//		WebBackForwardList mWebBackForwardList = webView.copyBackForwardList();
//		for(int i = 0;i<mWebBackForwardList.getSize();i++){
//			WebHistoryItem itemAtIndex = mWebBackForwardList.getItemAtIndex(i);
//			UIHelper.showLog("url:"+itemAtIndex.getUrl());
//		}
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    public void initData() {

    }
}
