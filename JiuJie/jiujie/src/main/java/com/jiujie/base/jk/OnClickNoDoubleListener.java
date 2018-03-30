package com.jiujie.base.jk;

import android.view.View;

/**
 * Created by ChenJiaLiang on 2018/1/24.
 * Email:576507648@qq.com
 */

public abstract class OnClickNoDoubleListener implements View.OnClickListener{

    private long clickTime;
    @Override
    public final void onClick(View v) {
        long timeMillis = System.currentTimeMillis();
        if(timeMillis - clickTime < 800){
            return;
        }
        clickTime = timeMillis;
        onClick1(v);
    }

    public abstract void onClick1(View v);
}
