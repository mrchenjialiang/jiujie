package com.jiujie.base.util;

import android.text.TextUtils;
import android.widget.Toast;

import com.jiujie.base.APP;

/**
 * Created by ChenJiaLiang on 2018/4/17.
 * Email:576507648@qq.com
 */

public class ToastUtil {
    private static Toast toast;
    public static void clear(){
        if(toast!=null){
            toast.cancel();
            toast = null;
        }
    }
    public static void showToastShort(final String text) {
        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(text.trim())) {
            return;
        }
        if(APP.getContext()==null){
            UIHelper.showLog("showToast fail because APP.getContext==null,should do APP.init() when application create");
            return;
        }
        if (UIHelper.isRunInUIThread()) {
            if(toast==null){
                toast = Toast.makeText(APP.getContext(),"",Toast.LENGTH_SHORT);
            }
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }else{
            new TaskManager<Boolean>() {
                @Override
                public Boolean runOnBackgroundThread() {
                    return null;
                }

                @Override
                public void runOnUIThread(Boolean aBoolean) {
                    if(toast==null){
                        toast = Toast.makeText(APP.getContext(),"",Toast.LENGTH_SHORT);
                    }
                    toast.setText(text);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.show();
                }
            }.start();
        }
    }

    public static void showToastLong(final String text) {
        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(text.trim())) {
            return;
        }
        if(APP.getContext()==null){
            UIHelper.showLog("showToast fail because APP.getContext==null,should do APP.init() when application create");
            return;
        }
        if (UIHelper.isRunInUIThread()) {
            if(toast==null){
                toast = Toast.makeText(APP.getContext(),"",Toast.LENGTH_LONG);
            }
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        }else{
            new TaskManager<Boolean>() {
                @Override
                public Boolean runOnBackgroundThread() {
                    return null;
                }

                @Override
                public void runOnUIThread(Boolean aBoolean) {
                    if(toast==null){
                        toast = Toast.makeText(APP.getContext(),"",Toast.LENGTH_LONG);
                    }
                    toast.setText(text);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.show();
                }
            }.start();
        }
    }
}
