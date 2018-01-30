package com.jiujie.base.jk;

import android.view.View;

/**
 * Created by ChenJiaLiang on 2017/6/16.
 * Email:576507648@qq.com
 */
public interface OnTouchMoveListen{
    void onYMove(View v, float moveY);
    void onXMove(View v, float moveX);
    void onStop(View v);
}
