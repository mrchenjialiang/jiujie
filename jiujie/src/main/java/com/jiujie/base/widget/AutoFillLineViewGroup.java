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
public class AutoFillLineViewGroup extends ViewGroup {

    private static final String TAG = "AutoFillLineViewGroup ";
    private List<View> childViewList;
    private int lineSpacing = 16;
    private int spacing = 8;
    private int lineNum = 5;
    private int childHeight;
    private int childWidth;

    public AutoFillLineViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public AutoFillLineViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AutoFillLineViewGroup(Context context) {
        super(context);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.AutoFillLineViewGroup);
            lineSpacing = (int)a.getDimension(R.styleable.AutoFillLineViewGroup_lineSpacing, lineSpacing);
            lineNum = a.getInteger(R.styleable.AutoFillLineViewGroup_lineNum, lineNum);
            a.recycle();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (childViewList != null && childViewList.size() > 0) {
            int paddingLeft = getPaddingLeft();
            int paddingTop = getPaddingTop();
            int left, top = 0, right = 0, bottom;

            for (int i = 0; i < childViewList.size(); i++) {
                View child = childViewList.get(i);

                if(i%lineNum==0){
                    //一行第一个
                    left = paddingLeft;
                    top = paddingTop + (childHeight + lineSpacing)*(i/lineNum);
                }else{
                    left = right + spacing;
                }
                right = left + childWidth;
                bottom = top + childHeight;
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
        int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        int height;


        /**
         * 根据childView计算的出的宽和高，以及设置的margin计算容器的宽和高，主要用于容器是warp_content时
         */
        childHeight = 0;
        childWidth = 0;

        removeAllViews();
        for (int i = 0; i < childViewList.size(); i++) {
            View child = childViewList.get(i);
            if(child.getParent()!=null){
                removeView(child);
            }
            addView(child,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        }

        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        for (int i = 0; i < childViewList.size(); i++) {
            View child = childViewList.get(i);
//            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            if(childHeight ==0){
                childHeight = child.getMeasuredHeight();
                childWidth = child.getMeasuredWidth();
                if(childWidth*lineNum+getPaddingLeft()+getPaddingRight()+(lineNum-1)* spacing > viewWidth){
                    int resultChildWidth = (viewWidth - ((lineNum-1)* spacing+getPaddingLeft()+getPaddingRight()))/lineNum;
                    childHeight = childHeight*resultChildWidth/childWidth;
                    childWidth = resultChildWidth;
                }else{
                    spacing = (viewWidth - (childWidth*lineNum+getPaddingLeft()+getPaddingRight()))/(lineNum-1);
                }
            }
        }
        int lineCount;
        if(childViewList.size()%lineNum==0){
            lineCount = childViewList.size()/lineNum;
        }else{
            lineCount = childViewList.size()/lineNum + 1;
        }

        height = lineCount* childHeight + getPaddingTop() + getPaddingBottom() + lineSpacing*(lineCount-1);

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

    public void setLineNum(int lineNum) {
        if(this.lineNum!=lineNum){
            this.lineNum = lineNum;
            requestLayout();
        }
    }

    public void setLineSpacing(int lineSpacing) {
        if(this.lineSpacing!=lineSpacing){
            this.lineSpacing = lineSpacing;
            requestLayout();
        }
    }

    public void setChildViewList(List<View> childViewList) {
        this.childViewList = childViewList;
        requestLayout();
    }

    public void notifyDataSetChange() {
        requestLayout();
    }
}
