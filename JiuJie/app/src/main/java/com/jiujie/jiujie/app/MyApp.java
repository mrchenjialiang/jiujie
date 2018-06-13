package com.jiujie.jiujie.app;

import android.app.Application;

import com.jiujie.base.APP;
import com.jiujie.base.util.video.VideoCacheUtil;

/**
 * Created by ChenJiaLiang on 2017/11/14.
 * Email:576507648@qq.com
 */

public class MyApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        APP.init(this,false,null,Config.isDebug,"com.jiujie.demo.fileProvider");
        VideoCacheUtil.instance().init(this,Config.VIDEO_SAVE_PATH,getExternalCacheDir()+"/video/",500*1024*1024);
        initAd();
    }

    private void initAd() {
//        AdManager.instance(this).init(Config.isDebug,
//                Config.GDT_APP_ID,
//                Config.GDT_NATIVE_HENG_ID,
//                Config.GDT_NATIVE_SHU_ID,
//                Config.GDT_BANNER_ID,
//                Config.GDT_SPLASH_ID,
//                Config.GDT_CHAPING_ID,
//                Config.GDT_NATIVE_VIDEO_ID,
//                Config.GDT_NATIVE_EXPRESS_ID);
    }
}
