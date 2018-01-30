package com.jiujie.base.widget;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

import com.jiujie.base.jk.OnListener;

public class ScrollViewCanListen extends NestedScrollView {
	private int scrollY;
	private OnListener<Integer> onScrollListener;

	public ScrollViewCanListen(Context context) {
		super(context);
	}

	public ScrollViewCanListen(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		scrollY = t;
		if(onScrollListener!=null){
			onScrollListener.onListen(scrollY);
		}
	}

	public void setOnScrollListener(OnListener<Integer> onScrollListener){
		this.onScrollListener = onScrollListener;
	}

}
