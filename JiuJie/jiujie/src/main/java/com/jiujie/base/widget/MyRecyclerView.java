package com.jiujie.base.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by ChenJiaLiang on 2017/7/5.
 * Email:576507648@qq.com
 */

public class MyRecyclerView extends RecyclerView{
    private ItemDecoration mItemDecoration;

    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void addItemDecoration(ItemDecoration decor) {
        if(mItemDecoration!=null){
            removeItemDecoration(mItemDecoration);
        }
        mItemDecoration = decor;
        super.addItemDecoration(decor);
    }
}
