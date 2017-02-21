package com.jiujie.base.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.jiujie.base.APP;
import com.jiujie.base.R;
import com.jiujie.base.util.PermissionsManager;
import com.umeng.analytics.MobclickAgent;

public class BaseToolBarActivity extends AppCompatActivity {

    private LinearLayout rootLayout;
    public Toolbar toolbar;
    public Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 这句很关键，注意是调用父类的方法
        super.setContentView(R.layout.activity_base_toolbar);
        mActivity = this;
        // 经测试在代码里直接声明透明状态栏更有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        initToolbar();
    }

    public void into(Class<?> cls){
        startActivity(new Intent(mActivity, cls));
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    public void setToolBarHeight(int height){
        if(toolbar==null) return;
        ViewGroup.LayoutParams params = toolbar.getLayoutParams();
        if(params!=null){
            params.height = height;
            toolbar.setLayoutParams(params);
        }
    }

    public void setToolBarBackGround(int color){
        if(toolbar==null) return;
        toolbar.setBackgroundColor(color);
    }

    @Override
    public void setContentView(int layoutId) {
        setContentView(View.inflate(this, layoutId, null));
    }

    @Override
    public void setContentView(View view) {
        rootLayout = (LinearLayout) findViewById(R.id.root_layout);
        if (rootLayout == null) return;
        rootLayout.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initToolbar();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(APP.isUseUMeng) MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(APP.isUseUMeng) MobclickAgent.onPause(this);
    }
}
