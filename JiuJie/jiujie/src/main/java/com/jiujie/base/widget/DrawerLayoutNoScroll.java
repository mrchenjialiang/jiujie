package com.jiujie.base.widget;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * author : Created by ChenJiaLiang on 2016/8/30.
 * Email : 576507648@qq.com
 */
public class DrawerLayoutNoScroll extends DrawerLayout{
    private boolean isCanScroll = true;

    public DrawerLayoutNoScroll(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public DrawerLayoutNoScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public DrawerLayoutNoScroll(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isCanScroll)
            return false;
        return super.onInterceptTouchEvent(ev);
    }

    public void setIsCanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }
}
