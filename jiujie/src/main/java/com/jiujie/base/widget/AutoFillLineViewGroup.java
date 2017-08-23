package com.jiujie.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.jiujie.base.R;

import java.util.ArrayList;
import java.util.List;

/**
 * item项的margin被设置忽略，应用于item都一个大小排列用
 * @author : Created by ChenJiaLiang on 2016/10/17.
 * Email : 576507648@qq.com
 */
public class AutoFillLineViewGroup extends ViewGroup {

    private static final String TAG = "AutoFillLineViewGroup ";
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
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.AutoFillLineViewGroup);
            lineSpacing = (int) a.getDimension(R.styleable.AutoFillLineViewGroup_lineSpacing, lineSpacing);
            lineNum = a.getInteger(R.styleable.AutoFillLineViewGroup_lineNum, lineNum);
            spacing = (int) a.getDimension(R.styleable.AutoFillLineViewGroup_spacing, spacing);
            a.recycle();
        }
    }

    public void setLineNum(int lineNum) {
        if (this.lineNum != lineNum) {
            this.lineNum = lineNum;
//            requestLayout();
        }
    }

    public void setLineSpacing(int lineSpacing) {
        if (this.lineSpacing != lineSpacing) {
            this.lineSpacing = lineSpacing;
//            requestLayout();
        }
    }

    public void setChildViewList(List<?extends View> childViewList) {
        removeAllViews();
        for (View view : childViewList) {
            addView(view);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        List<View> visibleChildList = getVisibleChildList();
        if (visibleChildList.size() > 0) {
            int paddingLeft = getPaddingLeft();
            int paddingTop = getPaddingTop();
            int left, top = 0, right = 0, bottom;

            for (int i = 0; i < visibleChildList.size(); i++) {
                View child = visibleChildList.get(i);
                if (i % lineNum == 0) {
                    //一行第一个
                    left = paddingLeft;
                    top = paddingTop + (childHeight + lineSpacing) * (i / lineNum);
                } else {
                    left = right + spacing;
                }
                right = left + childWidth;
                bottom = top + childHeight;
                child.layout(left, top, right, bottom);
            }
        }
    }

    @NonNull
    private List<View> getVisibleChildList() {
        List<View> visibleChildList = new ArrayList<>();
        for (int i = 0; i< getChildCount(); i++){
            View child = getChildAt(i);
            if(child.getVisibility()!=GONE){
                visibleChildList.add(child);
            }
        }
        return visibleChildList;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//        MeasureSpec.EXACTLY:1073741824
//        MeasureSpec.AT_MOST:-2147483648
//        MeasureSpec.UNSPECIFIED:0

        List<View> visibleChildList = getVisibleChildList();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int width;
        int height = getPaddingBottom() + getPaddingTop();
        if (visibleChildList.size() == 0) {
            if (widthMode == MeasureSpec.AT_MOST) {
                //wrap_content
                width = getPaddingLeft() + getPaddingRight();
            } else {
                width = widthSize;
            }
            setMeasuredDimension(width, height);
        } else {
            View firstVisibleChild = visibleChildList.get(0);
            childHeight = firstVisibleChild.getMeasuredHeight();
            childWidth = firstVisibleChild.getMeasuredWidth();
            if (childWidth * lineNum + getPaddingLeft() + getPaddingRight() + (lineNum - 1) * spacing > widthSize) {
                int resultChildWidth = (widthSize - ((lineNum - 1) * spacing + getPaddingLeft() + getPaddingRight())) / lineNum;
                if (resultChildWidth > 0) {
                    childWidth = resultChildWidth;
                    for (int i = 0; i< visibleChildList.size(); i++){
                        View childAt = visibleChildList.get(i);
                        LayoutParams lp = childAt.getLayoutParams();
                        lp.width = childWidth;
                        childAt.setLayoutParams(lp);
                    }
                }
            } else {
                spacing = (widthSize - (childWidth * lineNum + getPaddingLeft() + getPaddingRight())) / (lineNum - 1);
            }
            int lineCount = visibleChildList.size() / lineNum + (visibleChildList.size() % lineNum == 0 ? 0 : 1);
            height += childHeight * lineCount;
            height += lineSpacing * (lineCount - 1);
            if (widthMode == MeasureSpec.AT_MOST) {
                int showLineNum = Math.min(lineNum, visibleChildList.size());
                width = getPaddingLeft() + getPaddingRight() + showLineNum * childWidth + spacing * (showLineNum - 1);
            } else {
                width = widthSize;
            }
            if (heightMode == MeasureSpec.UNSPECIFIED) {

            } else if (heightMode == MeasureSpec.AT_MOST) {

            } else if (heightMode == MeasureSpec.EXACTLY) {
                height = heightSize;
            }
            setMeasuredDimension(width, height);
        }
    }

    //不设置item的margin
//    @Override
//    public LayoutParams generateLayoutParams(AttributeSet attrs) {
//        return new MarginLayoutParams(getContext(),attrs);
//    }
//
//    @Override
//    protected LayoutParams generateDefaultLayoutParams() {
//        return new MarginLayoutParams(LayoutParams.MATCH_PARENT,
//                LayoutParams.MATCH_PARENT);
//    }
//
//    @Override
//    protected LayoutParams generateLayoutParams(LayoutParams p) {
//        return new MarginLayoutParams(p);
//    }
}
