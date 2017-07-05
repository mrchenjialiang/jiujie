package com.jiujie.base.util;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseIntArray;
import android.view.View;

/**
 * Created by ChenJiaLiang on 2017/7/3.
 * Email:576507648@qq.com
 */
public class MyItemDecoration extends RecyclerView.ItemDecoration {

    private SparseIntArray fullLineCountMap = new SparseIntArray();
    private int spanCount;
    private int spacing;
    private boolean includeEdge;

    public MyItemDecoration(int spacing, int spanCount, boolean includeEdge) {
        this.spacing = spacing;
        this.spanCount = spanCount;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position

        if(includeEdge){
            if (position < spanCount) { // top edge
                outRect.top = spacing;
            }
            outRect.bottom = spacing; // item bottom
        }else{
            if (position >= spanCount) {
                outRect.top = spacing; // item top
            }
        }

        if(position==0){
            fullLineCountMap.put(position,0);
        }else{
            fullLineCountMap.put(position,fullLineCountMap.get(position-1));
        }
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            int spanSize = spanSizeLookup.getSpanSize(position);
            if (spanSize == spanCount) {
                ifItemFullLine(outRect, position);
            }else{
                ifItemNoFullLine(outRect, position);
            }
        }else if(layoutManager instanceof StaggeredGridLayoutManager){
            //没写瀑布流时的
            StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
            if(lp.isFullSpan()){
                ifItemFullLine(outRect, position);
            }else{
                ifItemNoFullLine(outRect, position);
            }
        }else{
            ifItemNoFullLine(outRect, position);
//            int column = position % spanCount; // item column
//            if (includeEdge) {
//                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
//                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)
//            } else {
//                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
//                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
//            }
        }
    }

    private void ifItemNoFullLine(Rect outRect, int position) {
        int count = fullLineCountMap.get(position);
        position = position - count;

        int column = position % spanCount; // item column
        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)
        } else {
            outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
        }
    }

    private void ifItemFullLine(Rect outRect, int position) {
        if(includeEdge){
            outRect.left = spacing;
            outRect.right = spacing;
        }else{
            outRect.left = 0;
            outRect.right = 0;
        }
        if(position==0){
            fullLineCountMap.put(position,1);
        }else{
            fullLineCountMap.put(position,fullLineCountMap.get(position-1)+1);
        }
    }
}
