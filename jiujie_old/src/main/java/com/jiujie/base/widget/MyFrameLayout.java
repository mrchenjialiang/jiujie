package com.jiujie.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 在不改变FrameLayout的基础上，设置child的高度==wrap_parent的child的高度，重置FrameLayout的高度，再设置所有child的高度为FrameLayout的高度
 * @author : Created by ChenJiaLiang on 2016/11/4.
 *         Email : 576507648@qq.com
 */
public class MyFrameLayout extends FrameLayout {
    public MyFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public MyFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MyFrameLayout(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxHeight = 0;
        List<View> mathHeightViewList = new ArrayList<>();
        for (int i = 0;i<getChildCount();i++){
            View child = getChildAt(i);
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            ViewGroup.LayoutParams lp = child.getLayoutParams();
            if(lp.height== ViewGroup.LayoutParams.MATCH_PARENT){
                mathHeightViewList.add(child);
            }else{
                int height = child.getMeasuredHeight();
                maxHeight = Math.max(height,maxHeight);
            }
        }
        for (View view:mathHeightViewList){
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            lp.height = maxHeight;
            view.setLayoutParams(lp);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        for (int i = 0;i<getChildCount();i++){
            View child = getChildAt(i);
            ViewGroup.LayoutParams lp = child.getLayoutParams();
            lp.height = getMeasuredHeight();
            child.setLayoutParams(lp);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
