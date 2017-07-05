package com.jiujie.base;

import android.content.Context;

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
}
