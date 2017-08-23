package com.jiujie.base.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ChenJiaLiang on 2017/7/3.
 * Email:576507648@qq.com
 */
public class MyItemHDecoration extends RecyclerView.ItemDecoration {

    private int spacing;
    private boolean includeEdge;

    public MyItemHDecoration(int spacing, boolean includeEdge) {
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int count = parent.getAdapter().getItemCount();
        boolean isFirst = position == 0;
        boolean isEnd = position == count - 1;

        if(includeEdge){
            outRect.left = isFirst?spacing:spacing/2;
            outRect.right = isEnd?spacing:spacing/2;
            outRect.top = spacing;
            outRect.bottom = spacing;
        }else{
            outRect.left = isFirst?spacing:spacing/2;
            outRect.right = isEnd?spacing:spacing/2;
        }

    }
}
