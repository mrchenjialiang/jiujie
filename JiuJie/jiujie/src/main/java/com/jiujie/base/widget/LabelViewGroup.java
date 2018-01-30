package com.jiujie.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.jiujie.base.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Created by ChenJiaLiang on 2016/10/17.
 * Email : 576507648@qq.com
 */
public class LabelViewGroup extends ViewGroup {

    private List<View> childViewList;
    private int lineSpacing = 16;
    private int spacing = 16;
    private int viewWidth;

    public LabelViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public LabelViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LabelViewGroup(Context context) {
        super(context);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.LabelViewGroup);
            lineSpacing = (int)a.getDimension(R.styleable.LabelViewGroup_lineSpacing, lineSpacing);
            spacing = (int)a.getDimension(R.styleable.LabelViewGroup_spacing, spacing);
            a.recycle();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (childViewList != null && childViewList.size() > 0) {
            int paddingLeft = getPaddingLeft();
            int paddingRight = getPaddingRight();
            int paddingTop = getPaddingTop();
            boolean isLineFirst = true;
            int left, top = 0, right = 0, bottom;

            for (int i = 0; i < childViewList.size(); i++) {
                View child = childViewList.get(i);

                if (i == 0) {
                    left = paddingLeft;
                    top = paddingTop;
                    isLineFirst = false;
                } else {
                    if (right + spacing + child.getMeasuredWidth() > viewWidth - paddingRight) {
                        isLineFirst = true;
                    }
                    if (isLineFirst) {
                        left = paddingLeft;
                        top += (child.getMeasuredHeight() + lineSpacing);
                        isLineFirst = false;
                    } else {
                        left = right + spacing;
                    }
                }
                right = left + child.getMeasuredWidth();
                bottom = top + child.getMeasuredHeight();
                child.layout(left, top, right, bottom);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);


        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int height;


        /**
         * 根据childView计算的出的宽和高，以及设置的margin计算容器的宽和高，主要用于容器是warp_content时
         */
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        boolean isLineFirst = true;
        int left, top = 0, right = 0, bottom = 0;

        removeAllViews();
        for (int i = 0; i < childViewList.size(); i++) {
            View child = childViewList.get(i);
            if(child.getParent()!=null){
                removeView(child);
            }
            addView(child,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            if (i == 0) {
                left = paddingLeft;
                top = paddingTop;
                isLineFirst = false;
            } else {
                if (right + spacing + child.getMeasuredWidth() > viewWidth - paddingRight) {
                    isLineFirst = true;
                }
                if (isLineFirst) {
                    left = paddingLeft;
                    top += (child.getMeasuredHeight() + lineSpacing);
                    isLineFirst = false;
                } else {
                    left = right + spacing;
                }
            }
            right = left + child.getMeasuredWidth();
            bottom = top + child.getMeasuredHeight();
        }

        height = bottom + getPaddingBottom();

        viewWidth = sizeWidth;
        int viewHeight = (heightMode == MeasureSpec.EXACTLY) ? sizeHeight : height;

        /**
         * 如果是wrap_content设置为我们计算的值
         * 否则：直接设置为父容器计算的值
         */
        setMeasuredDimension(viewWidth, viewHeight);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (childViewList == null) {
            childViewList = new ArrayList<>();
        }
        for (int i = 0; i < getChildCount(); i++) {
            childViewList.add(getChildAt(i));
        }
    }

    public void setChildViewList(List<View> childViewList) {
        this.childViewList = childViewList;
        if (this.childViewList != null && this.childViewList.size() > 0){
            requestLayout();
        }
    }

    public void notifyDataSetChange() {
        requestLayout();
    }
}
