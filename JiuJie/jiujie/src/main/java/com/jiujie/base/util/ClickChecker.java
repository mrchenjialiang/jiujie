package com.jiujie.base.util;

/**
 * Created by ChenJiaLiang on 2018/3/13.
 * Email:576507648@qq.com
 */

public class ClickChecker {
    private static long lastClickTime;
    public static boolean checkClickEnable(){
        long clickTime = System.currentTimeMillis();
        if(clickTime-lastClickTime>500){
            lastClickTime = clickTime;
            return true;
        }
        return false;
    }
}
