package com.jiujie.base.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

import java.util.ArrayList;

/**
 * 有弹性的ScrollView,已改良，可多点触摸滑动
 * @author Created by ChenJiaLiang on 2015/02/05.
 * Email :576507648@qq.com
 */
@SuppressWarnings("deprecation")
public class ScrollViewElastic extends ScrollView {
     
    //移动因子, 是一个百分比, 比如手指移动了100px, 那么View就只移动50px
    //目的是达到一个延迟的效果
    private static final float MOVE_FACTOR = 0.5f;
     
    //松开手指后, 界面回到正常位置需要的动画时间
    private static final int ANIM_TIME = 300;
     
    //ScrollView的子View， 也是ScrollView的唯一一个子View
    private View contentView; 
     
    //用于记录正常的布局位置
    private Rect originalRect = new Rect();
     
    //手指按下时记录是否可以继续下拉
    private boolean canPullDown = false;
     
    //手指按下时记录是否可以继续上拉
    private boolean canPullUp = false;
     
    //在手指滑动的过程中记录是否移动了布局
    private boolean isMoved = false;
 
	public ScrollViewElastic(Context context) {
        super(context);
		// 判断SDK版本 2.3以上禁止listView滚动时的颜色
		if (Integer.valueOf(android.os.Build.VERSION.SDK) > 9) {
			setOverScrollMode(View.OVER_SCROLL_NEVER);
		}
    }
     
    public ScrollViewElastic(Context context, AttributeSet attrs) {
        super(context, attrs);
		// 判断SDK版本 2.3以上禁止listView滚动时的颜色
		if (Integer.valueOf(android.os.Build.VERSION.SDK) > 9) {
			setOverScrollMode(View.OVER_SCROLL_NEVER);
		}
    }
 
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            contentView = getChildAt(0);
        }
		// 判断SDK版本 2.3以上禁止listView滚动时的颜色
		if (Integer.valueOf(android.os.Build.VERSION.SDK) > 9) {
			setOverScrollMode(View.OVER_SCROLL_NEVER);
		}
    }
     
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(contentView == null) return;
 
        //ScrollView中的唯一子控件的位置信息, 这个位置信息在整个控件的生命周期中保持不变
        originalRect.set(contentView.getLeft(), contentView.getTop(), contentView
                .getRight(), contentView.getBottom());
    }
 
	private int moveY;
	private int actionPointCount;
	private ArrayList<Point> pointList = new ArrayList<>();
	private boolean isShouldRefreshPoints = false;
    private float downX,downY;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
    	
        if (contentView == null) {
            return super.dispatchTouchEvent(ev);
        }
        switch (ev.getAction()&MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN:
        	downAction(ev);
            if(canPullDown||canPullUp){
                getParent().requestDisallowInterceptTouchEvent(true);
            }
            downX = ev.getX();
            downY = ev.getY();
            break;
        case MotionEvent.ACTION_POINTER_DOWN:
        	downAction(ev);
        	break;
        case MotionEvent.ACTION_UP:
        	upAction(ev);
            break;
        case MotionEvent.ACTION_POINTER_UP:
        	isShouldRefreshPoints = true;
        	upAction(ev);
        	break;
        case MotionEvent.ACTION_MOVE:
            float checkX = ev.getX();
            float checkY = ev.getY();
            if(Math.abs(checkY-downY)<Math.abs(checkX-downX)){
                //X移动距离大于Y移动距离
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
            }
            //在移动的过程中， 既没有滚动到可以上拉的程度， 也没有滚动到可以下拉的程度
            if(!canPullDown && !canPullUp) {
                canPullDown = isCanPullDown();
                canPullUp = isCanPullUp();
                break;
            }
            int pointerCount = ev.getPointerCount();
			if(actionPointCount!=pointerCount||isShouldRefreshPoints){
            	refreshPoints(ev);
            	isShouldRefreshPoints = false;
            }
			float maxY = 0;
			for (int i = 0; i < pointerCount; i++) {
				Point point = pointList.get(i);
				float currentY = ev.getY(i);
				float oldY = point.getY();
				float moveY = currentY - oldY;
				point.setY(currentY);
				if(Math.abs(moveY)>maxY){
					maxY = moveY;
				}
			}
			moveY += maxY;
            //是否应该移动布局
            boolean shouldMove = 
                    (canPullDown && moveY > 0)    //可以下拉， 并且手指向下移动
                    || (canPullUp && moveY< 0)    //可以上拉， 并且手指向上移动
                    || (canPullUp && canPullDown); //既可以上拉也可以下拉（这种情况出现在ScrollView包裹的控件比ScrollView还小）

            if(shouldMove){
                //计算偏移量
                int offset = (int)(moveY * MOVE_FACTOR);
                 
                //随着手指的移动而移动布局
                contentView.layout(originalRect.left, originalRect.top + offset,
                        originalRect.right, originalRect.bottom + offset);
                 
                isMoved = true;  //记录移动了布局
            }else{
            	moveY = 0;
            }
             
            break;
        default:
            break;
        }
 
        return super.dispatchTouchEvent(ev);
    }

	private void downAction(MotionEvent ev) {
		actionPointCount++;
		//判断是否可以上拉和下拉
        canPullDown = isCanPullDown();
        canPullUp = isCanPullUp();
        refreshPoints(ev);
	}
	private void refreshPoints(MotionEvent ev){
		pointList.clear();
		for (int i = 0; i < ev.getPointerCount(); i++) {
			Point point = new Point(ev.getX(i), ev.getY(i));
			pointList.add(point);
		}
	}
    class Point{
    	float X;
    	float Y;
		@Override
		public String toString() {
			return "Point [X=" + X + ", Y=" + Y + "]";
		}
		public Point(float x, float y) {
			super();
			X = x;
			Y = y;
		}
		public float getX() {
			return X;
		}
		public float getY() {
			return Y;
		}
		public void setX(float x) {
			X = x;
		}
		public void setY(float y) {
			Y = y;
		}
    }
    private void upAction(MotionEvent ev){
    	int pointerCount = ev.getPointerCount();
    	actionPointCount--;
    	refreshPoints(ev);
//    	if(actionPointCount!=0) return;
    	if(pointerCount!=1) return;
    	if(!isMoved) return;  //如果没有移动布局， 则跳过执行
        // 开启动画
        TranslateAnimation anim = new TranslateAnimation(0, 0, contentView.getTop(),
                originalRect.top);
        anim.setDuration(ANIM_TIME);
         
        contentView.startAnimation(anim);
        
        // 设置回到正常的布局位置
        contentView.layout(originalRect.left, originalRect.top, 
                originalRect.right, originalRect.bottom);
        
        pointList.clear();
        //将标志位设回false
        canPullDown = false;
        canPullUp = false;
        isMoved = false;
        moveY = 0;
    }
 
    /**
     * 判断是否滚动到顶部
     */
    private boolean isCanPullDown() {
        return getScrollY() == 0 || 
                contentView.getHeight() < getHeight() + getScrollY();
    }
     
    /**
     * 判断是否滚动到底部
     */
    private boolean isCanPullUp() {
        return  contentView.getHeight() <= getHeight() + getScrollY();
    }

    private int top;
    public int getScrollTop() {
        return top;
    }
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        top = t;
        super.onScrollChanged(l, t, oldl, oldt);
    }
     
}
