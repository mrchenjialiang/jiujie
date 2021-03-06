package com.jiujie.base.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.jiujie.base.R;
import com.jiujie.base.widget.SlideLayout;

/**
 * @author : Created by ChenJiaLiang on 2017/3/31.
 *         Email : 576507648@qq.com
 */
public abstract class BaseSlideActivity extends BaseMostActivity {

    private SlideLayout swipeBackLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isOpenSlideBack()){
            swipeBackLayout = new SlideLayout(this);
            swipeBackLayout.attachToActivity(this);
        }
    }
//
//    @Override
//    public void setTheme(int resid) {
//        if(isOpenSlideBack()){
//            super.setTheme(R.style.Slide_trans);
//        }else{
//            super.setTheme(resid);
//        }
//    }

    protected boolean isOpenSlideBack(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                && getApplicationInfo().targetSdkVersion > 26){
//            8.0+的手机，并且targetSdkVersion>26的暂不开启侧滑
//            要注意values-v26下的styles选择一套注释掉
            return false;
        }
        return true;
    }

    public void setDrawerScrollEnable(boolean isEnable){
        if(swipeBackLayout!=null)
            swipeBackLayout.setIsCanScroll(isEnable);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        if(swipeBackLayout!=null)
            overridePendingTransition(R.anim.slide_right_in, R.anim.no_anim);
    }

    @Override
    public void finish() {
        super.finish();
        if(swipeBackLayout!=null)
            overridePendingTransition(0, R.anim.slide_right_out);
    }
}
