package com.jiujie.jiujie;

import android.app.Application;

import com.jiujie.base.APP;

/**
 * Created by ChenJiaLiang on 2017/11/14.
 * Email:576507648@qq.com
 */

public class MyApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        APP.init(this,false,false,null,true,"com.jiujie.demo.fileProvider");
    }
}
