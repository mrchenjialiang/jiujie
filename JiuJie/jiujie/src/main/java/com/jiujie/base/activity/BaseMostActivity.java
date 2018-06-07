package com.jiujie.base.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jiujie.base.util.EventBusObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 这里不要包含有任何界面上的操作
 * @author : Created by ChenJiaLiang on 2017/2/27.
 *         Email : 576507648@qq.com
 */
public abstract class BaseMostActivity extends AppCompatActivity{

    public Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mActivity = this;
//        // 经测试在代码里直接声明透明状态栏更有效
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
//            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventFinish(EventBusObject.Object object){
        if(object == EventBusObject.Object.FINISH){
            finish();
        }
    }

    public String getClassName(){
        return this.getClass().getName();
    }

    public String getPageName(){
        return this.getClass().getSimpleName();
    }

}
