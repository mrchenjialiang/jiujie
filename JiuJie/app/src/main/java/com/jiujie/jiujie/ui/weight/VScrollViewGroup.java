package com.jiujie.jiujie.ui.weight;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;

import com.jiujie.base.jk.OnItemClickListen;
import com.jiujie.base.jk.OnMyPageChangeListener;
import com.jiujie.base.jk.OnTouchListen;
import com.jiujie.base.util.UIHelper;
import com.jiujie.jiujie.R;


public class VScrollViewGroup extends ViewGroup{
    private ViewGroup viewGroup1;
    private ViewGroup viewGroup2;
    private float downX,downY;
    private int moveX,moveY;
    private int mCurrentPosition;
    private Activity activity;
    private ValueAnimator valueAnimator;
    private MyScrollViewGroupAdapter myScrollViewGroupAdapter;
    private Orientation orientation = Orientation.vertical;
    private VelocityTracker mVelocityTracker = null;

    public enum Orientation{
        vertical,horizontal
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public VScrollViewGroup(Context context) {
        super(context);
        init(context, null);
    }

    public VScrollViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public VScrollViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.VScrollViewGroup);
            int orientationValue = a.getInt(R.styleable.VScrollViewGroup_orientation,0);
            if(orientationValue == 0){
                orientation = Orientation.horizontal;
            }else if(orientationValue == 1){
                orientation = Orientation.vertical;
            }
            a.recycle();
        }
        
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setClickable(true);
        viewGroup1 = new FrameLayout(context);
        viewGroup2 = new FrameLayout(context);
        addView(viewGroup1,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        addView(viewGroup2,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
    }

    private ViewGroup mCurrentViewGroup;
    private ViewGroup mOtherViewGroup;
    private OnItemClickListen onItemClickListen;
    private OnMyPageChangeListener onMyPageChangeListener;
    public void setOnItemClickListen(OnItemClickListen onItemClickListen) {
        this.onItemClickListen = onItemClickListen;
    }

    public void setOnMyPageChangeListener(OnMyPageChangeListener onMyPageChangeListener) {
        this.onMyPageChangeListener = onMyPageChangeListener;
    }

    private ViewGroup getCurrentViewGroup(){
        if(mCurrentViewGroup ==null){
            mCurrentViewGroup = viewGroup1;
        }
        return mCurrentViewGroup;
    }

    private ViewGroup getOtherViewGroup(){
        if(mOtherViewGroup ==null){
            mOtherViewGroup = viewGroup2;
        }
        return mOtherViewGroup;
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public void setCurrentPosition(int mCurrentPosition) {
        this.mCurrentPosition = mCurrentPosition;
        moveX = 0;
        setAdapter(myScrollViewGroupAdapter);
    }

    public void notifyDataSetChanged(){
        moveX = 0;
        setAdapter(myScrollViewGroupAdapter);
    }

    public boolean isMoving(){
        return moveX!=0;
    }

    public void recycler(){
        viewGroup1 = null;
        viewGroup2 = null;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(myScrollViewGroupAdapter==null||myScrollViewGroupAdapter.getCount()==0){
            getCurrentViewGroup().layout(0,0,getWidth(),getHeight());
            return;
        }
        int left = -moveX,right = getWidth() - moveX,top = 0,bottom = getHeight();
        getCurrentViewGroup().layout(left,top,right,bottom);

        if(moveX>0){
            left = getWidth() - moveX;
            right = getWidth()*2 - moveX;
            getOtherViewGroup().layout(left,top,right,bottom);
        }else if(moveX<0){
            left = - moveX - getWidth();
            right = - moveX;
            getOtherViewGroup().layout(left,top,right,bottom);
        }else{
            getOtherViewGroup().layout(0,0,0,0);
        }
    }

    public void setAdapter(MyScrollViewGroupAdapter myScrollViewGroupAdapter){
        if(this.myScrollViewGroupAdapter==null||this.myScrollViewGroupAdapter!=myScrollViewGroupAdapter){
            this.myScrollViewGroupAdapter = myScrollViewGroupAdapter;
        }
        if(myScrollViewGroupAdapter==null||myScrollViewGroupAdapter.getCount()==0){
            return;
        }
        if(myScrollViewGroupAdapter.getCount()==1){
            mCurrentPosition = 0;
            myScrollViewGroupAdapter.init(this,mCurrentPosition);
            myScrollViewGroupAdapter.show(this,mCurrentPosition);
            requestLayout();
        }else{
            if(mCurrentPosition<0){
                mCurrentPosition = 0;
            }
            if(mCurrentPosition>myScrollViewGroupAdapter.getCount()-1){
                mCurrentPosition = myScrollViewGroupAdapter.getCount()-1;
            }
            myScrollViewGroupAdapter.init(this,mCurrentPosition);
            myScrollViewGroupAdapter.show(this,mCurrentPosition);
            prepareForLastBitmap();
            prepareForNextBitmap();
            moveX = 0;
            requestLayout();
        }
        if(onMyPageChangeListener!=null){
            onMyPageChangeListener.onPageSelected(mCurrentPosition);
        }
    }

    private void prepareForLastBitmap(){
        if(myScrollViewGroupAdapter==null)return;
        int index = mCurrentPosition-1;
        if(index<0){
            index = myScrollViewGroupAdapter.getCount()-1;
        }
        myScrollViewGroupAdapter.init(this,index);
    }

    private void prepareForNextBitmap(){
        if(myScrollViewGroupAdapter==null)return;
        int index = mCurrentPosition+1;
        if(index>myScrollViewGroupAdapter.getCount()-1){
            index = 0;
        }
        myScrollViewGroupAdapter.init(this,index);
    }

    public void initActivity(Activity activity){
        this.activity = activity;
    }

    private enum AnimType{
        Restore,toNext,toLast
    }

    /**
     * @param animType 归位，next,last
     */
    private void startAnim(int fromValue, final int toValue, final AnimType animType){
        if(valueAnimator!=null&&!valueAnimator.isRunning()){
            valueAnimator.cancel();
        }
        valueAnimator = ValueAnimator.ofInt(fromValue,toValue);
        long allTime = Math.min(400,Math.abs(toValue - fromValue));
        long needTime;
        if(getWidth()==0){
            needTime = allTime;
        }else{
            needTime = allTime*Math.abs(toValue-fromValue)/getWidth();
        }
        valueAnimator.setDuration(needTime);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                if(isTouching){
                    valueAnimator.cancel();
                }else{
                    if(orientation== Orientation.horizontal){
                        // 不断重新计算上下左右位置
                        moveX = (int) animator.getAnimatedValue();
                        // 重绘
                        if(moveX==toValue){
                            onAnimEnd(animType);
                        }else{
                            requestLayout();
                        }
                    }else if(orientation== Orientation.vertical){
                        // 不断重新计算上下左右位置
                        moveY = (int) animator.getAnimatedValue();
                        // 重绘
                        if(moveY==toValue){
                            onAnimEnd(animType);
                        }else{
                            requestLayout();
                        }
                    }
                }
            }
        });
        valueAnimator.start();
    }

    private void setNext() {
        if(myScrollViewGroupAdapter==null)return;
        int index = mCurrentPosition + 1;
        if(index>myScrollViewGroupAdapter.getCount()-1){
            index = 0;
        }
        if(activity==null||activity.isFinishing()){
            return;
        }
        ViewGroup otherViewGroup = getOtherViewGroup();
        myScrollViewGroupAdapter.show(otherViewGroup,index);
    }

    private void setLast() {
        if(myScrollViewGroupAdapter==null)return;
        int index = mCurrentPosition - 1;
        if(index<0){
            index = myScrollViewGroupAdapter.getCount()-1;
        }
        ViewGroup otherViewGroup = getOtherViewGroup();
        myScrollViewGroupAdapter.show(otherViewGroup,index);
    }

    /**
     * @param animType 归位，next,last
     */
    private void onAnimEnd(AnimType animType){
        if(myScrollViewGroupAdapter==null)return;
        if(animType== AnimType.Restore){
            moveX = 0;
        }else if(animType== AnimType.toNext){
            moveX = 0;
            mCurrentPosition++;
            if(mCurrentPosition>myScrollViewGroupAdapter.getCount()-1){
                mCurrentPosition = 0;
            }
        }else if(animType== AnimType.toLast){
            moveX = 0;
            mCurrentPosition--;
            if(mCurrentPosition<0){
                mCurrentPosition = myScrollViewGroupAdapter.getCount()-1;
            }
        }
        if(animType== AnimType.toNext||animType== AnimType.toLast){
            ViewGroup linShi = getOtherViewGroup();
            mOtherViewGroup = getCurrentViewGroup();
            mCurrentViewGroup = linShi;
            if(onMyPageChangeListener!=null){
                onMyPageChangeListener.onPageSelected(mCurrentPosition);
            }
        }
        requestLayout();
    }

    private boolean isTouching;
    private float finalDownX,finalDownY;
    private int mTouchSlop;
    private OnTouchListen onTouchListen;

    public void setOnTouchListen(OnTouchListen onTouchListen) {
        this.onTouchListen = onTouchListen;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(myScrollViewGroupAdapter==null||myScrollViewGroupAdapter.getCount()<=1||orientation==null)return super.onInterceptTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getRawX();
                downY = (int) ev.getRawY();
                finalDownX = downX;
                finalDownY = downY;
                moveX = 0;
                moveY = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                float currentX = ev.getRawX();
                float currentY = ev.getRawY();
                // 满足此条件屏蔽子类的touch事件
                if(orientation== Orientation.horizontal){
                    if (Math.abs(currentX - downX) > mTouchSlop&& Math.abs(currentY - downY) <= Math.abs(currentX - downX)) {
                        return true;
                    }
                }else if(orientation== Orientation.vertical){
                    if (Math.abs(currentY - downY) > mTouchSlop&& Math.abs(currentX - downX) <= Math.abs(currentY - downY)) {
                        return true;
                    }
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(myScrollViewGroupAdapter==null||myScrollViewGroupAdapter.getCount()<=1){
            return super.onTouchEvent(event);
        }
        if(mVelocityTracker==null){
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(onTouchListen!=null)onTouchListen.onTouch(true);
                isTouching = true;
                break;
            case MotionEvent.ACTION_MOVE:
                onEventMove(event);
                break;
            case MotionEvent.ACTION_UP:
                onEventUp(event);
                break;
            case MotionEvent.ACTION_CANCEL:
                onEventUp(event);
                break;
        }
        return true;
    }

    private void onEventMove(MotionEvent event) {
        if(myScrollViewGroupAdapter.getCount()==1){
            return;
        }
        boolean isShowLast;
        boolean isShowNext;
        if(orientation== Orientation.horizontal){
            float currentX = event.getX();
            isShowLast = moveX<0;
            isShowNext = moveX>0;
            moveX -= (currentX-downX);
            downX = currentX;
            if(!isShowNext&&!isShowLast){
                if(moveX<0){
                    setLast();
                }else if(moveX>0){
                    setNext();
                }
            }else{
                if(isShowNext&&moveX<0){
                    setLast();
                }else if(isShowLast&&moveX>0){
                    setNext();
                }
            }
            if(Math.abs(moveX)<=getWidth()){
                requestLayout();
            }
        }else if(orientation== Orientation.vertical){
            float currentY = event.getY();
            isShowLast = moveY<0;
            isShowNext = moveY>0;
            moveY -= (currentY-downY);
            downY = currentY;
            if(!isShowNext&&!isShowLast){
                if(moveY<0){
                    setLast();
                }else if(moveY>0){
                    setNext();
                }
            }else{
                if(isShowNext&&moveY<0){
                    setLast();
                }else if(isShowLast&&moveY>0){
                    setNext();
                }
            }
            if(Math.abs(moveY)<=getHeight()){
                requestLayout();
            }
        }
    }

    private void onEventUp(MotionEvent event) {
        if(onTouchListen!=null)onTouchListen.onTouch(false);
        if(Math.abs(event.getX()-finalDownX)<mTouchSlop&&Math.abs(event.getY()-finalDownY)<mTouchSlop){
            if(onItemClickListen!=null){
                onItemClickListen.click(mCurrentPosition);
            }
        }
        isTouching = false;
        if(orientation== Orientation.horizontal){
            if(moveX!=0){
                mVelocityTracker.computeCurrentVelocity(1000);
                UIHelper.showLog(this, "mVelocityTracker getXVelocity "+mVelocityTracker.getXVelocity(0));
                UIHelper.showLog(this, "mVelocityTracker getYVelocity "+mVelocityTracker.getYVelocity(0));
                
                if(Math.abs(moveX) >= getWidth()/15){
                    if(moveX>0){
                        startAnim(moveX,getWidth(), AnimType.toNext);
                    }else{
                        startAnim(moveX,-getWidth(), AnimType.toLast);
                    }
                }else{
                    startAnim(moveX,0, AnimType.Restore);
                }
            }
        }else if(orientation== Orientation.vertical){
            if(moveY!=0){
                mVelocityTracker.computeCurrentVelocity(1000);
                UIHelper.showLog(this, "mVelocityTracker getXVelocity "+mVelocityTracker.getXVelocity(0));
                UIHelper.showLog(this, "mVelocityTracker getYVelocity "+mVelocityTracker.getYVelocity(0));
                if(Math.abs(moveY) >=getWidth()/15){
                    if(moveY>0){
                        startAnim(moveY,getHeight(), AnimType.toNext);
                    }else{
                        startAnim(moveY,-getHeight(), AnimType.toLast);
                    }
                }else{
                    startAnim(moveY,0, AnimType.Restore);
                }
            }
        }
    }
}
