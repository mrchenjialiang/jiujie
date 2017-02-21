package com.umeng.util.app;

import android.content.Context;

import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

public class UMAPP{

    static {
        PlatformConfig.setWeixin("wxc885594da03d67f1", "659b599369d49fb81abc8b11c15b1132");//Luck
    }

    public static void init(Context context,boolean isDebug) {
        Config.DEBUG = isDebug;
//        UMShareAPI.get(context);
//        UMShareAPI.init(context,"56aefe07e0f55a250d001a7c");//app 提供Luck--不行
        UMShareAPI.init(context,"589d2a3dbbea830fb0001908");//IOS 提供Luck
    }
}
