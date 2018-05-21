package com.jiujie.base.widget;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.jiujie.base.jk.OnListener;

public class ScrollViewCanListen extends NestedScrollView {
	private int scrollY;
	private boolean isTouching;
	private boolean isScrolling;
	private OnListener<Integer> onScrollListener;
	private OnListener<Boolean> onTouchingListener;
	private OnListener<Boolean> onScrollingListener;

	public ScrollViewCanListen(Context context) {
		super(context);
	}

	public ScrollViewCanListen(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onScrollChanged(int l, final int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		scrollY = t;
		if(onScrollListener!=null){
			onScrollListener.onListen(scrollY);
		}
        isScrolling = true;
		if(onScrollingListener!=null){
            onScrollingListener.onListen(true);
        }
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if(scrollY==t){
                    isScrolling = false;
                    if(onScrollingListener!=null)onScrollingListener.onListen(false);
                }
            }
        },200);
	}

	public void setOnScrollListener(OnListener<Integer> onScrollListener){
		this.onScrollListener = onScrollListener;
	}

	public void setOnTouchingListener(OnListener<Boolean> onTouchingListener) {
		this.onTouchingListener = onTouchingListener;
	}

    public void setOnScrollingListener(OnListener<Boolean> onScrollingListener) {
        this.onScrollingListener = onScrollingListener;
    }

    public boolean isTouching() {
		return isTouching;
	}

    public boolean isScrolling() {
        return isScrolling;
    }

    @Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()){
			case MotionEvent.ACTION_DOWN:
				isTouching = true;
				if(onTouchingListener!=null)onTouchingListener.onListen(true);
				break;
			case MotionEvent.ACTION_UP:
				isTouching = false;
				if(onTouchingListener!=null)onTouchingListener.onListen(false);
				break;
			case MotionEvent.ACTION_CANCEL:
				isTouching = false;
				if(onTouchingListener!=null)onTouchingListener.onListen(false);
				break;
		}
		return super.dispatchTouchEvent(ev);
	}

}
