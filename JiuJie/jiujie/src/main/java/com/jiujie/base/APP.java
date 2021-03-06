package com.jiujie.base;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.jiujie.base.jk.AppRequest;
import com.jiujie.base.util.UIHelper;


/**
 * @author : Created by ChenJiaLiang on 2016/6/12.
 *         Email : 576507648@qq.com
 */
public class APP {

    public static boolean isDeBug;
    public static String defaultDoMain;
    private static Context context;
    private static int statusBarHeightByReadR;
    private static int titleHeight;
    private static String providerAuthorities;
    private static AppRequest appRequest;

    public static void init(Context context, boolean isCrashError, String defaultDoMain, boolean isDeBug, String providerAuthorities) {
        APP.context = context;
        APP.isDeBug = isDeBug;
        APP.defaultDoMain = defaultDoMain;
        APP.providerAuthorities = providerAuthorities;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //7.0以上，必须配置 providerAuthorities,注意该值必须和清单文件里配置的 authorities 完全一致，且之后不能更改
            if (TextUtils.isEmpty(providerAuthorities)) {
                throw new NullPointerException("APP.init should have providerAuthorities," +
                        "and this value should be equal authorities of FileProvider in AndroidManifest");
            }
        }

        if (isCrashError) {
            //初始化捕捉异常类
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(context.getApplicationContext());
        }

        UIHelper.initTime();
    }

    public static void init(Context context, boolean isCrashError, boolean isDeBug) {
        init(context, isCrashError, null, isDeBug, context.getPackageName()+".fileProvider");
    }

    public static void init(Context context, boolean isCrashError, String defaultDoMain, boolean isDeBug) {
        init(context, isCrashError, defaultDoMain, isDeBug, context.getPackageName()+".fileProvider");
    }

    public static String getProviderAuthorities() {
        return providerAuthorities;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        APP.context = context;
    }

    public static boolean isTitleContainStatusBar() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static int getStatusBarHeight() {
        if (statusBarHeightByReadR != 0) {
            return statusBarHeightByReadR;
        }
        statusBarHeightByReadR = UIHelper.getStatusBarHeightByReadR(getContext());
        return statusBarHeightByReadR;
    }

    public static int getTitleHeight() {
        if (titleHeight != 0) {
            return titleHeight;
        }
        titleHeight = getContext().getResources().getDimensionPixelOffset(R.dimen.height_of_title);
        return titleHeight;
    }

    public static void setAppRequest(AppRequest appRequest) {
        APP.appRequest = appRequest;
    }

    public static AppRequest getAppRequest() {
        return appRequest;
    }
}
