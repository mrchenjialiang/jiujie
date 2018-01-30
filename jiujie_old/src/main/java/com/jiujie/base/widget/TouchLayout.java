package com.jiujie.base.widget;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.jiujie.base.R;
import com.jiujie.base.util.UIHelper;


/**
 * Created by ChenJiaLiang on 2017/6/16.
 * Email:576507648@qq.com
 */

public class TouchLayout extends FrameLayout{

    private static final int STYLE_LEFT = 0;
    private static final int STYLE_TOP = 1;
    private static final int STYLE_RIGHT = 2;
    private static final int STYLE_BOTTOM = 3;
    private int mTouchSlop;
    private float downX,finalDownX;
    private float downY,finalDownY;
    private boolean isEnable = true;
    private int index = 0;
    private int style = STYLE_BOTTOM;
    private View childView;
    private int childWidth;
    private int childHeight;
    private ValueAnimator scrollAnimator;
    private boolean isOpen;
    private OnTouchListen onTouchListen;

    public void setOnTouchListen(OnTouchListen onTouchListen) {
        this.onTouchListen = onTouchListen;
    }

    public interface OnTouchListen{
        void onClose();
        void onOpen();
    }

    public TouchLayout(Context context) {
        super(context);
        init(context, null);
    }

    public TouchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public TouchLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        setClickable(true);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        if(attrs!=null){
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TouchLayout);
            index = a.getInt(R.styleable.TouchLayout_jj_index, index);
            style = a.getInt(R.styleable.TouchLayout_jj_style, style);
            a.recycle();
        }
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    private boolean isShouldDoTouch;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(!isEnable||childView==null){
            return super.onInterceptTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = finalDownX = ev.getRawX();
                downY = finalDownY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(style==STYLE_TOP||style==STYLE_BOTTOM){
                    int moveY = (int) ev.getRawY();
                    // 满足此条件屏蔽子类的touch事件
                    if (Math.abs(moveY - downY) > mTouchSlop && Math.abs((int) ev.getRawX() - downX) < mTouchSlop) {
                        return true;
                    }
                }else{
                    int moveX = (int) ev.getRawX();
                    // 满足此条件屏蔽子类的touch事件
                    if (Math.abs(moveX - downX) > mTouchSlop && Math.abs((int) ev.getRawY() - downY) < mTouchSlop) {
                        return true;
                    }
                }
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    private int moveYSum,moveXSum;
    private boolean isPanDuan;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnable || childView == null) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = finalDownX = event.getRawX();
                downY = finalDownY = event.getRawY();
                isPanDuan = false;
                isShouldDoTouch = false;
                currentMoveTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                float currentY = event.getRawY();
                float currentX = event.getRawX();
                float deltaY = (currentY - downY);
                float deltaX = (currentX - downX);
                if (!isPanDuan) {
                    if (style == STYLE_TOP || style == STYLE_BOTTOM) {
                        if (Math.abs(deltaY) > Math.abs(deltaX)) {
                            isShouldDoTouch = true;
                        }
                    } else {
                        if (Math.abs(deltaX) > Math.abs(deltaY)) {
                            isShouldDoTouch = true;
                        }
                    }
                }

                if(isShouldDoTouch){
                    downX = currentX;
                    downY = currentY;
                    lastMoveTime = currentMoveTime;
                    currentMoveTime = System.currentTimeMillis();
                    if (style == STYLE_TOP || style == STYLE_BOTTOM) {
                        if (Math.abs(deltaY) > Math.abs(deltaX)) {
                            moveYSum += deltaY;
                            if (style == STYLE_BOTTOM) {
                                if (moveYSum <= 0) {
                                    moveYSum = Math.max(moveYSum, -childHeight);
                                } else {
                                    moveYSum = 0;
                                }
                                childView.layout(0, bottom + moveYSum, childWidth, bottom + moveYSum + childHeight);
                            } else if (style == STYLE_TOP) {
                                if (moveYSum >= 0) {
                                    moveYSum = Math.min(moveYSum, childHeight);
                                } else {
                                    moveYSum = 0;
                                }
                                childView.layout(0, moveYSum + top - childHeight, childWidth, moveYSum + top);
                            }
                            shouldMoveLength = (int) (200*deltaY/(currentMoveTime-lastMoveTime));
                        }
                    } else {
                        if (Math.abs(deltaX) > Math.abs(deltaY)) {
                            moveXSum += deltaX;
                            if (style == STYLE_LEFT) {
                                if (moveXSum > 0) {
                                    moveXSum = Math.min(moveXSum, childWidth);
                                } else {
                                    moveXSum = 0;
                                }
                                childView.layout(moveXSum - childWidth, 0, moveXSum, childHeight);
                            } else if (style == STYLE_RIGHT) {
                                if (moveXSum < 0) {
                                    moveXSum = Math.max(moveXSum, -childWidth);
                                } else {
                                    moveXSum = 0;
                                }
                                childView.layout(right + moveXSum, 0, right + moveXSum + childWidth, childHeight);
//                            childView.layout(-moveXSum,0, -moveXSum+childWidth, childHeight);
                            }
                            shouldMoveLength = (int) (200*deltaX/(currentMoveTime-lastMoveTime));
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if(isShouldDoTouch)onTouchEnd(event);
                break;
            case MotionEvent.ACTION_CANCEL:
                if(isShouldDoTouch)onTouchEnd(event);
                break;
        }
        return true;
    }

    private long lastMoveTime,currentMoveTime;
    private int shouldMoveLength;

    private void onTouchEnd(MotionEvent event) {
        UIHelper.showLog("shouldMoveLength:"+shouldMoveLength);
        float moveY = event.getRawY() - finalDownY;
        float moveX = event.getRawX() - finalDownX;
        if(style==STYLE_TOP||style==STYLE_BOTTOM){
            if(Math.abs(moveY)>Math.abs(moveX)){
                startScroll(moveYSum,moveYSum+shouldMoveLength);
//                if(moveY>0){
//                    //向下滑动
//                    if(style == STYLE_BOTTOM){
//                        close();
////                    startScroll(moveYSum,0);
//                    }else{
//                        open();
////                    startScroll(moveYSum,childHeight);
//                    }
//                }else{
//                    if(style == STYLE_BOTTOM){
//                        open();
////                    startScroll(moveYSum,-childHeight);
//                    }else{
//                        close();
////                    startScroll(moveYSum,0);
//                    }
//                }
            }
        }else{
            if(Math.abs(moveX)>Math.abs(moveY)){
                startScroll(moveXSum,moveXSum+shouldMoveLength);
//                if(moveX>0){
//                    //向右滑动
//                    if(style == STYLE_LEFT){
//                        open();
////                    startScroll(moveXSum,childWidth);
//                    }else{
//                        close();
////                    startScroll(moveYSum,0);
//                    }
//                }else{
//                    if(style == STYLE_LEFT){
//                        close();
////                    startScroll(moveYSum,0);
//                    }else{
//                        open();
////                    startScroll(moveYSum,-childWidth);
//                    }
//                }
            }
        }
    }

    public void open(){
        if(style == STYLE_BOTTOM){
            startScroll(moveYSum,-childHeight);
        }else if(style == STYLE_TOP){
            startScroll(moveYSum,childHeight);
        }else if(style == STYLE_LEFT){
            startScroll(moveXSum,childWidth);
        }else if(style == STYLE_RIGHT){
            startScroll(moveXSum,-childWidth);
        }
        isOpen = true;
        if(onTouchListen!=null)onTouchListen.onOpen();
    }

    public void close(){
        if(style == STYLE_BOTTOM){
            startScroll(moveYSum,0);
        }else if(style == STYLE_TOP){
            startScroll(moveYSum,0);
        }else if(style == STYLE_LEFT){
            startScroll(moveXSum,0);
        }else if(style == STYLE_RIGHT){
            startScroll(moveXSum,0);
        }
        isOpen = false;
        if(onTouchListen!=null)onTouchListen.onClose();
    }

    public boolean isOpen() {
        return isOpen;
    }

    private void startScroll(float startValue, float endValue){
        if(endValue>childHeight){
            endValue = childHeight;
        }
        if(endValue<-childHeight){
            endValue = -childHeight;
        }
        if(scrollAnimator!=null&&scrollAnimator.isRunning()){
            scrollAnimator.cancel();
        }
        scrollAnimator = ValueAnimator.ofFloat(startValue, endValue);
//        scrollAnimator.setInterpolator(new TimeInterpolator() {
//            @Override
//            public float getInterpolation(float input) {
//                return 0;
//            }
//        });
        final float allTime = 200;
        float time;
        if(style==STYLE_TOP||style==STYLE_BOTTOM){
            time = allTime*Math.abs(endValue-startValue)/childHeight;
        }else{
            time = allTime*Math.abs(endValue-startValue)/childWidth;
        }
        scrollAnimator.setDuration((long) time);
        scrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float floatValue = (float) animation.getAnimatedValue();
                int value = (int) floatValue;
                if(style == STYLE_BOTTOM){
                    childView.layout(0,bottom+value,childWidth,bottom+value+childHeight);
                    moveYSum = value;
                }else if(style == STYLE_TOP){
                    childView.layout(0,top+ value - childHeight,childWidth,top+value);
                    moveYSum = value;
                }else if(style == STYLE_LEFT){
                    childView.layout(left+value-childWidth,0,left+value, childHeight);
                    moveXSum = value;
                }else if(style == STYLE_RIGHT){
                    childView.layout(right+value,0, right+value+childWidth, childHeight);
                    moveXSum = value;
                }
            }
        });
        scrollAnimator.start();
    }

    int left;
    int top;
    int right;
    int bottom;
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        if(getChildCount()>index){
            childView = getChildAt(index);
            childWidth = childView.getMeasuredWidth();
            childHeight = childView.getMeasuredHeight();
            if(style == STYLE_BOTTOM){
                childView.layout(0,bottom+moveYSum,childWidth,bottom+moveYSum+childHeight);
//                childView.layout(0,bottom, childWidth,bottom+ childHeight);
            }else if(style == STYLE_LEFT){
                childView.layout(left+moveXSum-childWidth,0,left+moveXSum, childHeight);
//                childView.layout(left-childWidth,0,left, childHeight);
            }else if(style == STYLE_TOP){
                childView.layout(0,top+ moveYSum - childHeight,childWidth,top+moveYSum);
//                childView.layout(0,top- childHeight, childWidth,top);
            }else if(style == STYLE_RIGHT){
                childView.layout(right+moveXSum,0, right+moveXSum+childWidth, childHeight);
//                childView.layout(right,0, right+childWidth, childHeight);
            }
        }
    }
}
