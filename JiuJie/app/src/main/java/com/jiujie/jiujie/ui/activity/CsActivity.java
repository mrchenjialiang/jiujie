package com.jiujie.jiujie.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.jiujie.base.APP;
import com.jiujie.base.util.UIHelper;
import com.jiujie.jiujie.R;
import com.jiujie.jiujie.ui.dialog.WebDialog;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CsActivity extends MyBaseActivity {

    @Bind(R.id.cs_web)
    WebView webView;

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override
    public void initUI() {
        ButterKnife.bind(this);

        UIHelper.clearClipBoard();

        WebSettings webSettings = webView.getSettings();
        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);//必须设置
        // 通过addJavascriptInterface()将Java对象映射到JS对象
        //参数1：Javascript对象名
        //参数2：Java对象名
        WebDialog.AndroidMethodForWebView androidMethodForWebView = new WebDialog.AndroidMethodForWebView();
        UIHelper.showLog("androidMethodForWebView:" + androidMethodForWebView);
        webView.addJavascriptInterface(new WebDialog.AndroidMethodForWebView(), "webDialog");// AndroidMethodForWebView 类对象映射到js的 webDialog 对象

//        String jsUrl = "https://logs.newapi.com/jd/ad?f=zq40";
//        String htmlModelText = UIHelper.getDataFromAssets(APP.getContext(), "hongbao.html");
//        if (TextUtils.isEmpty(htmlModelText)) return;
//        htmlModelText = htmlModelText.replace("#key#", jsUrl);
//        webView.loadDataWithBaseURL(null, htmlModelText, "text/html", "UTF-8", null);

        //可以获取到
//        webView.loadUrl("http://s.69876.com/testzfb.html");
//        webView.loadUrl("http://s.69876.com/hongbao.html");


        String htmlModelText = UIHelper.getDataFromAssets(APP.getContext(), "testzfb.html");
        webView.loadDataWithBaseURL("https://logs.newapi.com", htmlModelText, "text/html", "UTF-8", null);
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.cs;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard == null) return;
        CharSequence getText = clipboard.getText();
        UIHelper.showLog("剪切板内容：" + getText);
    }
}
