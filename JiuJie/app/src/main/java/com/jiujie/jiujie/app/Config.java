package com.jiujie.jiujie.app;

import android.os.Environment;

import java.io.File;

/**
 * Created by ChenJiaLiang on 2018/5/8.
 * Email:576507648@qq.com
 */

public class Config {

    public static boolean isDebug = true;

    //广点通--腾讯联盟广告配置
    static final String GDT_APP_ID = "1106382690";//广点通APP_ID
    static final String GDT_NATIVE_HENG_ID = "6020629533560872";//广点通原生广告，横图
    static final String GDT_NATIVE_SHU_ID = "8080530114966618";//广点通原生广告，竖图
    static final String GDT_BANNER_ID = "5050728563970157";//广点通Banner广告ID
    static final String GDT_SPLASH_ID = "3060021513574179";//广点通启动页广告ID
    static final String GDT_CHAPING_ID = "4060334164750893";//广点通插屏广告ID
    static final String GDT_NATIVE_VIDEO_ID = "6020639138166588";//广点通原生视频广告
    static final String GDT_NATIVE_EXPRESS_ID = "";//广点通原生模板广告


    //视频壁纸的下载地址
    public final static String VIDEO_SAVE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() +
            File.separator + "Download" + File.separator + "libs" + File.separator + "liveWallpaper"+ File.separator;

    //临时目录
    public final static String VIDEO_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() +
            File.separator + "Download" + File.separator + "libs"+ File.separator + "text" + File.separator;
}
