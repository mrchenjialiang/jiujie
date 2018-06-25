package com.jiujie.base.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jiujie.base.APP;
import com.jiujie.base.jk.AppRequest;
import com.jiujie.base.jk.OnSimpleListener;
import com.jiujie.base.util.EventBusObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 这里不要包含有任何界面上的操作
 *
 * @author : Created by ChenJiaLiang on 2017/2/27.
 *         Email : 576507648@qq.com
 */
public abstract class BaseMostActivity extends AppCompatActivity {

    public BaseMostActivity mActivity;
    private List<OnSimpleListener> onDestroyListenerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppRequest appRequest = APP.getAppRequest();
        if (appRequest != null) {
            appRequest.onActivityCreateBeforeSuper(this);
        }
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
    protected void onRestart() {
        super.onRestart();
        AppRequest appRequest = APP.getAppRequest();
        if (appRequest != null) {
            appRequest.onActivityRestart(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        AppRequest appRequest = APP.getAppRequest();
        if (appRequest != null) {
            appRequest.onActivityDestroy(this);
        }
        if (onDestroyListenerList != null) {
            for (OnSimpleListener onSimpleListener : onDestroyListenerList) {
                onSimpleListener.onListen();
            }
            onDestroyListenerList.clear();
            onDestroyListenerList = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppRequest appRequest = APP.getAppRequest();
        if (appRequest != null) {
            appRequest.onActivityResume(this, getPageName());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppRequest appRequest = APP.getAppRequest();
        if (appRequest != null) {
            appRequest.onActivityPause(this, getPageName());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AppRequest appRequest = APP.getAppRequest();
        if (appRequest != null) {
            appRequest.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventFinish(EventBusObject.Object object) {
        if (object == EventBusObject.Object.FINISH) {
            finish();
        }
    }

    public String getClassName() {
        return this.getClass().getName();
    }

    public String getPageName() {
        return this.getClass().getSimpleName();
    }

    public void addOnDestroyListener(OnSimpleListener onSimpleListener) {
        if (onDestroyListenerList == null) {
            onDestroyListenerList = new ArrayList<>();
        }
        onDestroyListenerList.add(onSimpleListener);
    }

}
