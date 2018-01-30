package com.jiujie.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.jiujie.base.jk.OnSelectListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenJiaLiang on 2017/6/13.
 * Email:576507648@qq.com
 */

public class RadioGroupLayout extends LinearLayout implements View.OnClickListener {
    private List<View> childViewList = new ArrayList<>();
    private OnSelectListener onSelectListener;
    private int mCurrentPosition;

    public RadioGroupLayout(Context context) {
        super(context);
    }

    public RadioGroupLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RadioGroupLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        refresh();
    }

    public void refresh(){
        childViewList.clear();
        for (int i = 0;i<getChildCount();i++){
            View childAt = getChildAt(i);
            childAt.setTag(i);
            childAt.setOnClickListener(this);
            childViewList.add(childAt);
        }
        check(mCurrentPosition,true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        childViewList.clear();
        for (int i = 0;i<getChildCount();i++){
            View childAt = getChildAt(i);
            childAt.setTag(i);
            childAt.setOnClickListener(this);
            childViewList.add(childAt);
        }
    }

    public void clearCheck(){
        for (int i = 0;i<childViewList.size();i++){
            childViewList.get(i).setSelected(false);
        }
        mCurrentPosition = -1;
    }

    public int getCheckPosition() {
        return mCurrentPosition;
    }

    public void check(int position){
        check(position,false);
    }

    public void check(int position,boolean isForce){
        if(mCurrentPosition!=position||isForce){
            mCurrentPosition = position;
            for (int i = 0;i<childViewList.size();i++){
                childViewList.get(i).setSelected(i==position);
            }
        }

        if(onSelectListener!=null){
            onSelectListener.onSelect(position);
        }
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        check(position);
    }
}
