package com.jiujie.base.util;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

import java.util.Map;

/**
 * Created by Administrator on 2017/5/27.
 */

public class UMengUtil {

    private static UMengUtil util;

    private UMengUtil() {
    }

    public static UMengUtil instance() {
        if(util==null){
            util = new UMengUtil();
        }
        return util;
    }

    public void addEvent(Context mContext, String eventId, Map<String,String> eventMap){
        MobclickAgent.onEvent(mContext, eventId, eventMap);
    }
}
