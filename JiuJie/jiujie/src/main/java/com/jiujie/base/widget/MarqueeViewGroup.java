package com.jiujie.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.jiujie.base.R;
import com.jiujie.base.jk.MyHandlerInterface;
import com.jiujie.base.util.MyHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author : Created by ChenJiaLiang on 2017/2/6.
 *         Email : 576507648@qq.com
 */
public class MarqueeViewGroup extends ViewGroup implements MyHandlerInterface {

    private Timer timer;
    private MyHandler myHandler;
    private TimerTask timerTask;
    private int showNum = 3;

    public MarqueeViewGroup(Context context) {
        super(context);
    }

    public MarqueeViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MarqueeViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.MarqueeViewGroup);

            showNum = a.getInt(R.styleable.MarqueeViewGroup_showNum, showNum);

            a.recycle();
        }
    }

    public void setShowNum(int showNum) {
        this.showNum = showNum;
        requestLayout();
    }

    protected void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        myHandler = null;
    }

    private void checkAndDoAction(){
        stop();
        if (childViewList != null && childViewList.size() > 0) {
            timer = new Timer("marqueeViewGroup", true);
            myHandler = new MyHandler(this);
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    myHandler.sendEmptyMessage(0);
                }
            };
            timer.schedule(timerTask, 3000, 3000);
        }
    }

    private int moveY;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (childViewList != null && childViewList.size() > 0) {
            int paddingLeft = getPaddingLeft();
            int paddingRight = getPaddingRight();
            int paddingTop = getPaddingTop();
            int left, top = 0, right , bottom;
            top += paddingTop;
            left = paddingLeft;
            if(showNum<childViewList.size()){
                top -= moveY;
                for (int i = 0; i < showNum + 1; i++) {
                    View child = childViewList.get(i);
                    right = getWidth() - paddingRight;//都以撑满算
                    bottom = top + child.getMeasuredHeight();
                    child.layout(left, top, right, bottom);
                    top = bottom;
                }
            }else if(showNum==childViewList.size()){
                for (int i = 0; i < showNum; i++) {
                    View child = childViewList.get(i);
                    right = getWidth() - paddingRight;//都以撑满算
                    bottom = top + child.getMeasuredHeight();
                    child.layout(left, top, right, bottom);
                    top = bottom;
                }
            }else if(showNum>childViewList.size()){
                for (int i = 0; i < childViewList.size(); i++) {
                    View child = childViewList.get(i);
                    right = getWidth() - paddingRight;//都以撑满算
                    bottom = top + child.getMeasuredHeight();
                    child.layout(left, top, right, bottom);
                    top = bottom;
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(childViewList==null||childViewList.size()==0){
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);

        int height = 0;

        removeAllViews();
        for (int i = 0; i < childViewList.size(); i++) {
            View child = childViewList.get(i);
            if (child.getParent() != null) {
                removeView(child);
            }
            addView(child, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        }

        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        height += getPaddingTop();
        if(showNum<=childViewList.size()){
            for (int i = 0; i < showNum; i++) {
                View child = childViewList.get(i);
                height += child.getMeasuredHeight();
            }
        }else{
            //假设每一个都一样高度
            View child = childViewList.get(0);
            for (int i = 0; i < showNum; i++) {
                height += child.getMeasuredHeight();
            }
        }
        height += getPaddingBottom();

        setMeasuredDimension(sizeWidth, height);
    }

    private List<View> childViewList;

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
        if (this.childViewList != null && this.childViewList.size() > 0) {
            requestLayout();
        }
        checkAndDoAction();
    }

    @Override
    public void handleMessage(Message msg) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                int measuredHeight = childViewList.get(0).getMeasuredHeight();
                if(moveY>= measuredHeight){
                    childViewList.add(childViewList.get(0));
                    childViewList.remove(0);
                    moveY = 0;
                }else{
                    moveY+=10;
                    if(moveY>=measuredHeight){
                        moveY = measuredHeight;
                    }
                    postDelayed(this, 30);
                }
                requestLayout();
            }
        },30);
    }
}
