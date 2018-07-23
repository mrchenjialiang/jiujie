package com.jiujie.jiujie.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.jiujie.base.APP;
import com.jiujie.base.dialog.BaseDialog;
import com.jiujie.base.util.UIHelper;
import com.jiujie.jiujie.R;

import java.io.Serializable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ChenJiaLiang on 2018/7/12.
 * Email:576507648@qq.com
 */

public class WebDialog extends BaseDialog {

    private Timer timer;
    private TimerTask timerTask;

    public WebDialog(Context context) {
        super(context);
        UIHelper.clearClipBoard();
        create(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER, 0);
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override
    protected void initUI(View layout) {
        WebView webView = layout.findViewById(R.id.dw_webView);
        WebSettings webSettings = webView.getSettings();
        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);//必须设置
        // 通过addJavascriptInterface()将Java对象映射到JS对象
        //参数1：Javascript对象名
        //参数2：Java对象名
        AndroidMethodForWebView androidMethodForWebView = new AndroidMethodForWebView();
        UIHelper.showLog("androidMethodForWebView:" + androidMethodForWebView);
        webView.addJavascriptInterface(new AndroidMethodForWebView(), "webDialog");// AndroidMethodForWebView 类对象映射到js的 webDialog 对象

        String jsUrl = "https://logs.newapi.com/jd/ad?f=zq40";

        String htmlModelText = UIHelper.getDataFromAssets(APP.getContext(), "hongbao.html");
        if (TextUtils.isEmpty(htmlModelText)) return;
        htmlModelText = htmlModelText.replace("#key#", jsUrl);
        webView.loadDataWithBaseURL("https://logs.newapi.com", htmlModelText, "text/html", "UTF-8", null);

        getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                stopTime();
            }
        });

        startTime();

    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_web;
    }

    private void startTime() {
        stopTime();
        timer = new Timer(true);
        timerTask = new TimerTask() {
            @Override
            public void run() {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboard == null) return;
                CharSequence clipText = clipboard.getText();
                UIHelper.showLog("剪切板内容："+clipText);
                if (!TextUtils.isEmpty(clipText)) {
                    openZhiFuBao();
                    stopTime();
                    dismiss();
                }
            }
        };
        timer.schedule(timerTask, 500, 500);
    }

    private void stopTime() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    public static class AndroidMethodForWebView implements Serializable {

        private static final long serialVersionUID = -4589032232585125666L;

        // 定义JS需要调用的方法
        // 被JS调用的方法必须加入@JavascriptInterface注解
        @JavascriptInterface
        public void onImageClick() {
            openZhiFuBao();
        }
    }

    public static void openZhiFuBao() {
        Context context = APP.getContext();
        String packageName = "com.eg.android.AlipayGphone";
        PackageInfo pi;
        try {
            pi = context.getPackageManager().getPackageInfo(packageName, 0);
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.setPackage(pi.packageName);
            PackageManager pManager = context.getPackageManager();
            List<ResolveInfo> apps = pManager.queryIntentActivities(
                    resolveIntent, 0);
            ResolveInfo ri = apps.iterator().next();
            if (ri != null) {
                packageName = ri.activityInfo.packageName;
                String className = ri.activityInfo.name;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//重点是加这个
                ComponentName cn = new ComponentName(packageName, className);
                intent.setComponent(cn);
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Log", "openAppByPackageName " + e);
            if (context instanceof Activity) {
                UIHelper.showToastShort("请先安装支付宝客户端");
            }
//            http://sj.qq.com/myapp/detail.htm?apkName=com.eg.android.AlipayGphone
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://sj.qq.com/myapp/detail.htm?apkName=" + packageName));
            context.startActivity(intent);
        }
    }
}
