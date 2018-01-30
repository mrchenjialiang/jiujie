package com.jiujie.base;

import android.content.Context;
import android.os.Build;

import com.jiujie.base.util.UIHelper;
import com.umeng.analytics.MobclickAgent;



/**
 * @author : Created by ChenJiaLiang on 2016/6/12.
 * Email : 576507648@qq.com
 */
public class APP {

    public static boolean isUseUMeng;
    public static boolean isCrashError;
    public static boolean isDeBug;
    public static String defaultDoMain;
    private static Context context;
    private static int statusBarHeightByReadR;
    private static int titleHeight;

    public static void init(Context context, boolean isUseUMeng, boolean isCrashError, String defaultDoMain, boolean isDeBug){
        APP.context = context;
        APP.isUseUMeng = isUseUMeng;
        APP.isCrashError = isCrashError;
        APP.isDeBug = isDeBug;
        APP.defaultDoMain = defaultDoMain;

        if(isUseUMeng)initUMeng();

        if(isCrashError){
            //初始化捕捉异常类
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(context.getApplicationContext());
        }
    }

    private static void initUMeng() {
//        AnalyticsConfig.setAppkey()
        MobclickAgent.setDebugMode(APP.isDeBug);
        MobclickAgent.setSessionContinueMillis(30000);//退出应用，再过多长时间进来表示不同的启动
        MobclickAgent.openActivityDurationTrack(true);//页面统计
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        APP.context = context;
    }

    public static boolean isTitleContainStatusBar(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static int getStatusBarHeight(){
        if(statusBarHeightByReadR!=0){
            return statusBarHeightByReadR;
        }
        statusBarHeightByReadR = UIHelper.getStatusBarHeightByReadR(getContext());
        return statusBarHeightByReadR;
    }

    public static int getTitleHeight(){
        if(titleHeight!=0){
            return titleHeight;
        }
        titleHeight = getContext().getResources().getDimensionPixelOffset(R.dimen.height_of_title);
        return titleHeight;
    }
}
