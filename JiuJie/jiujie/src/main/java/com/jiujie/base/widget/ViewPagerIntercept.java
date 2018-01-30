package com.jiujie.base.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 可拦截的ViewPager
 * author : Created by ChenJiaLiang on 2016/10/14.
 * Email : 576507648@qq.com
 */
public class ViewPagerIntercept extends ViewPager {

    private boolean scrollEnable = true;

    public ViewPagerIntercept(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewPagerIntercept(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!scrollEnable) {
            return false;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!scrollEnable) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setScrollEnable(boolean scrollEnable) {
        this.scrollEnable = scrollEnable;
    }

}
