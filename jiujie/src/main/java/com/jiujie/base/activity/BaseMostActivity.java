package com.jiujie.base.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.jiujie.base.APP;
import com.jiujie.base.util.PermissionsManager;
import com.umeng.analytics.MobclickAgent;

/**
 * 这里不要包含有任何界面上的操作
 * @author : Created by ChenJiaLiang on 2017/2/27.
 *         Email : 576507648@qq.com
 */
public class BaseMostActivity extends AppCompatActivity {

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
