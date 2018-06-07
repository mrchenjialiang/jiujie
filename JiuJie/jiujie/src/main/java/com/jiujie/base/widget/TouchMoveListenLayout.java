package com.jiujie.base.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.jiujie.base.jk.OnSimpleListener;


/**
 * Created by ChenJiaLiang on 2017/6/16.
 * Email:576507648@qq.com
 */

public class TouchMoveListenLayout extends FrameLayout{

    private int mTouchSlop;
    private float downX,downY;
    private float tempX,tempY;
    private OnTouchMoveListener onTouchListen;
    private View mContentView;
    private boolean enableFromLeftToRight;
    private boolean enableFromRightToLeft;
    private boolean enableFromTopToBottom;
    private boolean enableFromBottomToTop;
    private boolean enableTouchMoveFromLeftToRight,enableTouchMoveFromRightToLeft,enableTouchMoveFromTopToBottom,enableTouchMoveFromBottomToTop;
    private boolean isSliding;
    private boolean isDoActionAfterScrollEnd;
    private float scaleOfToDoActionWhenScrollEnd;
    private Scroller mScroller;
    private OnSimpleListener onScrollEndListen;
    private int lengthOfToDoActionWhenScrollEnd;
    private boolean isListenTouchMoveButNotMoveAndScroll;
    private int maxScrollTime;

    public TouchMoveListenLayout(Context context) {
        super(context);
        init(context);
    }

    public TouchMoveListenLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TouchMoveListenLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        setClickable(true);
        mScroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public void setMaxScrollTime(int maxScrollTime) {
        this.maxScrollTime = maxScrollTime;
    }

    public void setListenTouchMoveButNotMoveAndScroll(boolean listenTouchMoveButNotMoveAndScroll) {
        isListenTouchMoveButNotMoveAndScroll = listenTouchMoveButNotMoveAndScroll;
    }

    public void setOnTouchMoveListener(OnTouchMoveListener onTouchListen) {
        this.onTouchListen = onTouchListen;
    }

    public interface OnTouchMoveListener{
        void onTouchMoveFromLeftToRight(float distance);
        void onTouchMoveFromRightToLeft(float distance);
        void onTouchMoveFromBottomToTop(float distance);
        void onTouchMoveFromTopToBottom(float distance);
    }

    public void setDoScrollEndActionData(float scaleOfToDoActionWhenScrollEnd,OnSimpleListener onScrollEndListen) {
        this.onScrollEndListen = onScrollEndListen;
        this.scaleOfToDoActionWhenScrollEnd = scaleOfToDoActionWhenScrollEnd;
    }

    public void setDoScrollEndActionData(int lengthOfToDoActionWhenScrollEnd,OnSimpleListener onScrollEndListen) {
        this.onScrollEndListen = onScrollEndListen;
        this.lengthOfToDoActionWhenScrollEnd = lengthOfToDoActionWhenScrollEnd;
    }

    public void setEnable(boolean enableFromLeftToRight, boolean enableFromRightToLeft, boolean enableFromTopToBottom, boolean enableFromBottomToTop){
        this.enableFromLeftToRight = enableFromLeftToRight;
        this.enableFromRightToLeft = enableFromRightToLeft;
        this.enableFromTopToBottom = enableFromTopToBottom;
        this.enableFromBottomToTop = enableFromBottomToTop;
    }

    public void setEnableFromLeftToRight(boolean enableFromLeftToRight) {
        this.enableFromLeftToRight = enableFromLeftToRight;
    }

    public void setEnableFromRightToLeft(boolean enableFromRightToLeft) {
        this.enableFromRightToLeft = enableFromRightToLeft;
    }

    public void setEnableFromBottomToTop(boolean enableFromBottomToTop) {
        this.enableFromBottomToTop = enableFromBottomToTop;
    }

    public void setEnableFromTopToBottom(boolean enableFromTopToBottom) {
        this.enableFromTopToBottom = enableFromTopToBottom;
    }

    /**
     * 整个页面布局滑动
     */
    public void attachToActivity(Activity activity) {
        TypedArray a = activity.getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.windowBackground });
        int background = a.getResourceId(0, 0);
        a.recycle();

        ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
        decorChild.setBackgroundResource(background);
        decor.removeView(decorChild);
        addView(decorChild);
        setContentView(decorChild);
        decor.addView(this);
    }

    /**
     * 只ContentView滑动
     */
    public void setContentView(View decorChild) {
        mContentView = (View) decorChild.getParent();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(mContentView==null)return super.onInterceptTouchEvent(ev);
        if(!enableFromLeftToRight&&!enableFromRightToLeft&&!enableFromTopToBottom&&!enableFromBottomToTop){
            return super.onInterceptTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = tempX = (int) ev.getRawX();
                downY = tempY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float currentX = ev.getRawX();
                float currentY = ev.getRawY();
                enableTouchMoveFromLeftToRight = false;
                enableTouchMoveFromRightToLeft = false;
                enableTouchMoveFromTopToBottom = false;
                enableTouchMoveFromBottomToTop = false;
                // 满足此条件屏蔽子类的touch事件
                if(enableFromLeftToRight){
                    if (currentX - downX > mTouchSlop&& Math.abs(currentY - downY) <= Math.abs(currentX - downX)) {
                        enableTouchMoveFromLeftToRight = true;
                        return true;
                    }
                }
                if(enableFromRightToLeft){
                    if (downX - currentX > mTouchSlop&& Math.abs(currentY - downY) <= Math.abs(currentX - downX)) {
                        enableTouchMoveFromRightToLeft= true;
                        return true;
                    }
                }
                if(enableFromTopToBottom){
                    if (currentY - downY > mTouchSlop&& Math.abs(currentX - downX) <= Math.abs(currentY - downY)) {
                        enableTouchMoveFromTopToBottom = true;
                        return true;
                    }
                }
                if(enableFromBottomToTop){
                    if (downY - currentY > mTouchSlop&& Math.abs(currentX - downX) <= Math.abs(currentY - downY)) {
                        enableTouchMoveFromBottomToTop = true;
                        return true;
                    }
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mContentView==null)return super.onTouchEvent(event);
        if(!enableFromLeftToRight&&!enableFromRightToLeft&&!enableFromTopToBottom&&!enableFromBottomToTop){
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float currentX = event.getRawX();
                float currentY = event.getRawY();
                int deltaX = (int) (tempX - currentX);
                int deltaY = (int) (tempY - currentY);
                tempX = currentX;
                tempY = currentY;
                if(isListenTouchMoveButNotMoveAndScroll){
                    break;
                }else{
                    if(enableTouchMoveFromLeftToRight){
                        if (currentX - downX > mTouchSlop&& Math.abs(currentY - downY) <= Math.abs(currentX - downX)) {
                            isSliding = true;
                        }
                        if (isSliding) {
                            if(mContentView.getScrollX()+deltaX<=-mContentView.getWidth()){
                                mContentView.scrollTo(-mContentView.getWidth(),0);
                            }else{
                                mContentView.scrollBy(deltaX, 0);
                            }
                        }
                    }else if(enableFromRightToLeft&&enableTouchMoveFromRightToLeft){
                        if (downX - currentX > mTouchSlop&& Math.abs(currentY - downY) <= Math.abs(currentX - downX)) {
                            isSliding = true;
                        }
                        if (isSliding) {
                            if(mContentView.getScrollX()+deltaX>=mContentView.getWidth()){
                                mContentView.scrollTo(mContentView.getWidth(),0);
                            }else{
                                mContentView.scrollBy(deltaX, 0);
                            }
                        }
                    }else if(enableFromTopToBottom&&enableTouchMoveFromTopToBottom){
                        if (currentY - downY > mTouchSlop&& Math.abs(currentX - downX) <= Math.abs(currentY - downY)) {
                            isSliding = true;
                        }
                        if (isSliding) {
                            if(mContentView.getScrollY()+deltaY<=-mContentView.getHeight()){
                                mContentView.scrollTo(0,-mContentView.getHeight());
                            }else{
                                mContentView.scrollBy(0, deltaY);
                            }
                        }
                    }else if(enableFromBottomToTop&&enableTouchMoveFromBottomToTop){
                        if (downY - currentY > mTouchSlop&& Math.abs(currentX - downX) <= Math.abs(currentY - downY)) {
                            isSliding = true;
                        }
                        if (isSliding) {
                            if(mContentView.getScrollY()+deltaY>=mContentView.getHeight()){
                                mContentView.scrollTo(0,mContentView.getHeight());
                            }else{
                                mContentView.scrollBy(0, deltaY);
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if(isListenTouchMoveButNotMoveAndScroll){
                    if(onTouchListen!=null) {
                        int moveXWhenUp = (int) (event.getRawX() - downX);
                        int moveYWhenUp = (int) (event.getRawY() - downY);
                        if(enableTouchMoveFromLeftToRight&&moveXWhenUp>0){
                            onTouchListen.onTouchMoveFromLeftToRight(moveXWhenUp);
                        }else if(enableTouchMoveFromRightToLeft&&moveXWhenUp<0){
                            onTouchListen.onTouchMoveFromRightToLeft(-moveXWhenUp);
                        }else if(enableTouchMoveFromTopToBottom&&moveYWhenUp>0){
                            onTouchListen.onTouchMoveFromTopToBottom(moveYWhenUp);
                        }else if(enableTouchMoveFromBottomToTop&&moveYWhenUp<0){
                            onTouchListen.onTouchMoveFromBottomToTop(-moveYWhenUp);
                        }
                    }
                    break;
                }
                boolean isDoAction = false;
                if(isSliding){
                    if(mContentView.getScrollX()<0){
                        int moveXD = Math.abs(mContentView.getScrollX());
                        if(onTouchListen!=null) {
                            onTouchListen.onTouchMoveFromLeftToRight(moveXD);
                        }
                        if ((scaleOfToDoActionWhenScrollEnd>0&&moveXD >= scaleOfToDoActionWhenScrollEnd*mContentView.getWidth())
                                ||(lengthOfToDoActionWhenScrollEnd>0&&moveXD>=lengthOfToDoActionWhenScrollEnd)) {
                            scrollToRight();
                            isDoAction = true;
                        }
                    }else if(mContentView.getScrollX()>0){
                        int moveXD = Math.abs(mContentView.getScrollX());
                        if(onTouchListen!=null) {
                            onTouchListen.onTouchMoveFromRightToLeft(moveXD);
                        }
                        if ((scaleOfToDoActionWhenScrollEnd>0&&moveXD >= scaleOfToDoActionWhenScrollEnd*mContentView.getWidth())
                                ||(lengthOfToDoActionWhenScrollEnd>0&&moveXD>=lengthOfToDoActionWhenScrollEnd)) {
                            scrollToLeft();
                            isDoAction = true;
                        }
                    }else if(mContentView.getScrollY()<0){
                        int moveYD = Math.abs(mContentView.getScrollY());
                        if(onTouchListen!=null) {
                            onTouchListen.onTouchMoveFromTopToBottom(moveYD);
                        }
                        if ((scaleOfToDoActionWhenScrollEnd>0&&moveYD >= scaleOfToDoActionWhenScrollEnd*mContentView.getHeight())
                                ||(lengthOfToDoActionWhenScrollEnd>0&&moveYD>=lengthOfToDoActionWhenScrollEnd)) {
                            scrollToBottom();
                            isDoAction = true;
                        }
                    }else if(mContentView.getScrollY()>0){
                        int moveYD = Math.abs(mContentView.getScrollY());
                        if(onTouchListen!=null) {
                            onTouchListen.onTouchMoveFromBottomToTop(moveYD);
                        }
                        if ((scaleOfToDoActionWhenScrollEnd>0&&moveYD >= scaleOfToDoActionWhenScrollEnd*mContentView.getHeight())
                                ||(lengthOfToDoActionWhenScrollEnd>0&&moveYD>=lengthOfToDoActionWhenScrollEnd)) {
                            scrollToTop();
                            isDoAction = true;
                        }
                    }
                }
                isSliding = false;
                isDoActionAfterScrollEnd = isDoAction;
                if(!isDoAction&&(mContentView.getScrollX()!=0||mContentView.getScrollY()!=0)){
                    scrollOrigin();
                }
                break;
        }

        return true;
    }

    /**
     * 滚动出界面
     */
    private void scrollToRight() {
        // 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
        int scrollX = mContentView.getScrollX();
        int leftDix = mContentView.getWidth() - Math.abs(scrollX);
        mScroller.startScroll(scrollX, 0, -leftDix, 0, getScrollTime(leftDix));
        postInvalidate();
    }

    /**
     * 滚动出界面
     */
    private void scrollToLeft() {
        // 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
        int scrollX = mContentView.getScrollX();
        int leftDix = mContentView.getWidth() - Math.abs(scrollX);
        mScroller.startScroll(scrollX, 0, leftDix, 0, getScrollTime(leftDix));
        postInvalidate();
    }

    /**
     * 滚动出界面
     */
    private void scrollToBottom() {
        // 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
        int scrollY = mContentView.getScrollY();
        int leftDix = mContentView.getHeight() - Math.abs(scrollY);
        mScroller.startScroll(0, scrollY, 0, -leftDix, getScrollTime(leftDix));
        postInvalidate();
    }

    /**
     * 滚动出界面
     */
    private void scrollToTop() {
        // 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
        int scrollY = mContentView.getScrollY();
        int leftDix = mContentView.getHeight() - Math.abs(scrollY);
        mScroller.startScroll(0, scrollY, 0, leftDix, getScrollTime(leftDix));
        postInvalidate();
    }

    private int getScrollTime(int leftDix){
        if(maxScrollTime>0){
            return Math.min(maxScrollTime,Math.abs(leftDix));
        }else{
            return Math.abs(leftDix);
        }
    }

    /**
     * 滚动到起始位置
     */
    private void scrollOrigin() {
        int scrollX = mContentView.getScrollX();
        int scrollY = mContentView.getScrollY();
        if(scrollX!=0){
            mScroller.startScroll(scrollX, 0, -scrollX, 0, getScrollTime(scrollX));
            postInvalidate();
        }else if(scrollY!=0){
            mScroller.startScroll(0, scrollY, 0, -scrollY, getScrollTime(scrollY));
            postInvalidate();
        }
    }

    /**
     * 滚动到起始位置
     * @param isSmoothScroll 是否要滑动过程
     */
    public void scrollOrigin(boolean isSmoothScroll) {
        if(isSmoothScroll){
            scrollOrigin();
        }else{
            mContentView.scrollTo(0,0);
        }
    }

    @Override
    public void computeScroll() {
        // 调用startScroll的时候scroller.computeScrollOffset()返回true，
        if (mScroller.computeScrollOffset()) {
            mContentView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();

            if (mScroller.isFinished() && isDoActionAfterScrollEnd) {
                if(onScrollEndListen!=null){
                    onScrollEndListen.onListen();
                }
                isDoActionAfterScrollEnd = false;
            }
        }
    }

}
