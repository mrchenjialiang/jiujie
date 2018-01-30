package com.jiujie.base.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 *  不被拦截的ViewPager
 * author : Created by ChenJiaLiang on 2016/10/14.
 * Email : 576507648@qq.com
 */
public class ViewPagerNoIntercept extends ViewPager{

    public ViewPagerNoIntercept(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewPagerNoIntercept(Context context) {
        super(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

}
