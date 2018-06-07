package com.jiujie.jiujie.util;

import android.app.KeyguardManager;
import android.content.Context;

import com.jiujie.base.APP;

/**
 * Created by ChenJiaLiang on 2018/5/28.
 * Email:576507648@qq.com
 */

public class Utils {
    public static void checkApp(String packageName){
    }

    public static boolean isSystemHasLockScreen() {
        KeyguardManager systemService = (KeyguardManager) APP.getContext().getSystemService(Context.KEYGUARD_SERVICE);
        return systemService != null && systemService.isKeyguardSecure();
    }

    public static boolean isSystemHasPasswordLockScreen() {
        KeyguardManager systemService = (KeyguardManager) APP.getContext().getSystemService(Context.KEYGUARD_SERVICE);
        return systemService != null && systemService.isKeyguardLocked();
    }

    public static void closeSystemLockScreen(){
        KeyguardManager km = (KeyguardManager) APP.getContext().getSystemService(Context.KEYGUARD_SERVICE);
        if(km==null)return;
        KeyguardManager.KeyguardLock keyguardLock = km.newKeyguardLock(APP.getContext().getPackageName());
        keyguardLock.disableKeyguard();
    }

}
