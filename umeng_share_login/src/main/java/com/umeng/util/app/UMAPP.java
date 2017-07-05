package com.umeng.util.app;

import android.content.Context;

import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * 调用登录或分享的界面，需 重写onActivityResult调用 UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
 */
public class UMAPP{

    static {
        PlatformConfig.setWeixin("wx1d131a19c98f5fb4", "628a2981b16db60414a2fcf2ab8fd05d");
        PlatformConfig.setSinaWeibo("3569847542","44daf530a053d50af4ac4bbd0165f667","https://api.weibo.com/oauth2/default.html");
        PlatformConfig.setQQZone("1106103078","p9jpMWS1AjrJMtvW");
    }

    public static void init(Context context,boolean isDebug) {
        Config.DEBUG = isDebug;
//        UMShareAPI.get(context);
//        UMShareAPI.init(context,"56aefe07e0f55a250d001a7c");
        UMShareAPI.init(context,"5927f2f41c5dd057e5000b5b");
    }
}
