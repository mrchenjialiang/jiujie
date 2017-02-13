package com.jiujie.base.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Found at http://stackoverflow.com/questions/7814017/is-it-possible-to-disable-scrolling-on-a-viewpager.
 * Convenient way to temporarily disable ViewPager navigation while interacting with ImageView.
 * 
 * Julia Zudikova
 */

/**
 * Hacky fix for Issue #4 and
 * http://code.google.com/p/android/issues/detail?id=18990
 * <p/>
 * ScaleGestureDetector seems to mess up the touch events, which means that
 * ViewGroups which make use of onInterceptTouchEvent throw a lot of
 * IllegalArgumentException: pointerIndex out of range.
 * <p/>
 * There's not much I can do in my code for now, but we can mask the result by
 * just catching the problem and ignoring it.
 *
 * @author Chris Banes
 */
public class HackyViewPager extends ViewPager implements OnClickListener {


    public HackyViewPager(Context context) {
        super(context);
    }

    public HackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
		setClickable(true);
        setOnClickListener(this);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
		try {
			return super.onInterceptTouchEvent(ev);//当滑线和水平线夹角<45°时返回true,其他都为false
		} catch (IllegalArgumentException e) {
			//异常不可避免,要么就去改系统源码..
//	        	System.out.println("异常:"+e.getMessage());
//	        	System.out.println("HackyViewPager抛出异常");
			return false;
		}
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
    }

	@Override
	public void onClick(View v) {
//		System.out.println("点击了");
	}
	
}
