package com.jiujie.base.util;

import android.os.Handler;
import android.os.Message;

import com.jiujie.base.jk.MyHandlerInterface;

import java.lang.ref.WeakReference;

/**
 * @author : Created by ChenJiaLiang on 2016/12/13.
 *         Email : 576507648@qq.com
 */
public class MyHandler extends Handler{
    WeakReference<MyHandlerInterface> weakReference;
    public MyHandler(MyHandlerInterface t){
        weakReference = new WeakReference<>(t);
    }
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(weakReference.get()!=null){
            weakReference.get().handleMessage(msg);
        }
    }
}
