package com.jiujie.base.util;


import android.view.View;

public abstract class OnNoMulClickListener implements View.OnClickListener{

    private long lastClickTime;
    @Override
    public void onClick(View v) {
        long timeMillis = System.currentTimeMillis();
        if(timeMillis - lastClickTime<300){
            return;
        }
        lastClickTime = timeMillis;
        doClick(v);
    }

    public abstract void doClick(View v);
}
