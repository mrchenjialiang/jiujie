package com.jiujie.base.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.jiujie.base.jk.OnRecyclerViewTouchScrollListener;

/**
 * Created by ChenJiaLiang on 2017/7/5.
 * Email:576507648@qq.com
 */

public class MyRecyclerView extends RecyclerView {
    private ItemDecoration mItemDecoration;
    private OnRecyclerViewTouchScrollListener onTouchingListener;
    private int scrollX, scrollY;

    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnTouchingListener(OnRecyclerViewTouchScrollListener onTouchingListener) {
        this.onTouchingListener = onTouchingListener;
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollX += dx;
                scrollY += dy;
            }
        });
    }

    @Override
    public void addItemDecoration(ItemDecoration decor) {
        if (mItemDecoration != null) {
            removeItemDecoration(mItemDecoration);
        }
        mItemDecoration = decor;
        super.addItemDecoration(decor);
    }

    @Override
    public void addOnScrollListener(OnScrollListener listener) {
        super.addOnScrollListener(listener);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (onTouchingListener != null) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    onTouchingListener.onListen(true, scrollX, scrollY);
                    break;
                case MotionEvent.ACTION_UP:
                    onTouchingListener.onListen(false, scrollX, scrollY);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    onTouchingListener.onListen(false, scrollX, scrollY);
                    break;
            }
        }
        return super.onInterceptTouchEvent(e);
    }
}
