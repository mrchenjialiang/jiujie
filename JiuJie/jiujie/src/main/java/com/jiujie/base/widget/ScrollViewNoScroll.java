package com.jiujie.base.widget;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ScrollViewNoScroll extends NestedScrollView {
	private boolean isCanScroll = true;
	private boolean isShowLog = false;

	public ScrollViewNoScroll(Context context) {
		super(context);
	}

	public ScrollViewNoScroll(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setIsCanScroll(boolean isCanScroll) {
		this.isCanScroll = isCanScroll;
	}

	@Override
	public void scrollTo(int x, int y) {
		if (isCanScroll) {
			super.scrollTo(x, y);
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent even) {
		if(isShowLog)System.out.println("isCanScroll:"+isCanScroll);
		if (!isCanScroll)
			return false;
		return super.onInterceptTouchEvent(even);
	}

	private int top;

	public int getScrollTop() {
		return top;
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		top = t;
		super.onScrollChanged(l, t, oldl, oldt);
	}
}
