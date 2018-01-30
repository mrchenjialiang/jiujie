package com.jiujie.base.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import com.jiujie.base.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * 需求透明主题，只能在清单里设置，Activity里设置无效！
 * @author ChenJiaLiang
 * 改自blog http://blog.csdn.net/xiaanming
 */
public class SlideLayout extends FrameLayout {
	private static final String TAG = SlideLayout.class.getSimpleName();
	private View mContentView;
	private int mTouchSlop;
	private int downX;
	private int downY;
	private int tempX;
	private Scroller mScroller;
	private int viewWidth;
	private boolean isSilding;
	private boolean isFinish;
	private Drawable mShadowDrawable;
	private Activity mActivity;
	private List<View> mChildViewList = new LinkedList<>();
	private boolean isEnable = true;

	public SlideLayout(Context context) {
		super(context);
		init(context);
	}

	public SlideLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SlideLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		mScroller = new Scroller(context);

		mShadowDrawable = getResources().getDrawable(R.drawable.shadow_left);
	}


	public void attachToActivity(Activity activity) {
		mActivity = activity;
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

	private void setContentView(View decorChild) {
		mContentView = (View) decorChild.getParent();
	}

	/**
	 * 事件拦截操作
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if(!isEnable){
			return super.onInterceptTouchEvent(ev);
		}

		//处理滑动冲突问题
		List<View> touchViewList = getTouchViewList(mChildViewList,ev);
//		Log.e(TAG, "mChildViewList:" + mChildViewList.size());
//		Log.e(TAG, "touchViewList:" + touchViewList);
//		if(touchViewList!=null)Log.e(TAG,"touchViewList size:"+touchViewList.size());
		if(touchViewList!=null&&touchViewList.size()>0){
			for (View touchView:touchViewList){
				//设置不拦截情况
				if(touchView instanceof ViewPager){
					ViewPager viewPager = (ViewPager) touchView;
					if(viewPager.getCurrentItem() != 0){
						return super.onInterceptTouchEvent(ev);
					}
				}else if(touchView instanceof HorizontalScrollView){
					HorizontalScrollView horizontalScrollView = (HorizontalScrollView) touchView;
					if(horizontalScrollView.getScrollX()>0){
						return super.onInterceptTouchEvent(ev);
					}
				}else if(touchView instanceof RecyclerView){
					//RecyclerView判断不出touchView是RecyclerView

//					RecyclerView recyclerView = (RecyclerView) touchView;
//					RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//					if(layoutManager instanceof LinearLayoutManager){
//						LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
//						int orientation = linearLayoutManager.getOrientation();
//						if(orientation==0){
//							return super.onInterceptTouchEvent(ev);
//						}
//					}else if(layoutManager instanceof StaggeredGridLayoutManager){
//						StaggeredGridLayoutManager linearLayoutManager = (StaggeredGridLayoutManager) layoutManager;
//						int orientation = linearLayoutManager.getOrientation();
//						if(orientation==0){
//							return super.onInterceptTouchEvent(ev);
//						}
//					}
				}else{
					if(touchView instanceof TextView||touchView instanceof ImageView){

					}else{
						if(touchView.onTouchEvent(ev)){
							return super.onInterceptTouchEvent(ev);
						}
					}
				}
			}
		}

		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				downX = tempX = (int) ev.getRawX();
				downY = (int) ev.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				int moveX = (int) ev.getRawX();
				// 满足此条件屏蔽SildingFinishLayout里面子类的touch事件
				if (moveX - downX > mTouchSlop
						&& Math.abs((int) ev.getRawY() - downY) < mTouchSlop) {
					return true;
				}
				break;
		}

		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(!isEnable){
			return super.onTouchEvent(event);
		}
		switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE:
				int moveX = (int) event.getRawX();
				int deltaX = tempX - moveX;
				tempX = moveX;
				if (moveX - downX > mTouchSlop
						&& Math.abs((int) event.getRawY() - downY) < mTouchSlop) {
					isSilding = true;
				}

				if (moveX - downX >= 0 && isSilding) {
					mContentView.scrollBy(deltaX, 0);
				}
				break;
			case MotionEvent.ACTION_UP:
				isSilding = false;
				if (mContentView.getScrollX() <= -viewWidth / 3) {
					isFinish = true;
					scrollRight();
				} else {
					scrollOrigin();
					isFinish = false;
				}
				break;
		}

		return true;
	}


	/**
	 * 获取需求不拦截的ChildView列表
	 */
	private void getChildShouldInterceptViewList(List<View> mChildViewList,ViewGroup parent){
		int childCount = parent.getChildCount();
		for(int i=0; i<childCount; i++){
			View child = parent.getChildAt(i);
			if(child instanceof ViewPager){
				mChildViewList.add(child);
			}else if(child instanceof ViewGroup){
				if(child instanceof HorizontalScrollView){
					mChildViewList.add(child);
					getChildShouldInterceptViewList(mChildViewList,(ViewGroup) child);
				}else if(child instanceof ScrollView){
					getChildShouldInterceptViewList(mChildViewList,(ViewGroup) child);
				}else{
					getChildShouldInterceptViewList(mChildViewList,(ViewGroup) child);
				}
			} else{
				mChildViewList.add(child);
			}
		}
	}

	/**
	 * 返回我们触摸到的View 列表
	 */
	private List<View> getTouchViewList(List<View> mChildViewList,MotionEvent ev){
		if(mChildViewList == null || mChildViewList.size() == 0){
			return null;
		}
		List<View> touchViewList = new ArrayList<>();
		Rect mRect = new Rect();
		for(View v : mChildViewList){
//			try {
//				Log.e(TAG,"v:"+v);
//				Log.e(TAG, "MotionEvent getX:" + (int) ev.getX() + "," + (int) ev.getY());
//				Log.e(TAG, "MotionEvent getRawX:" + (int) ev.getRawX() + "," + (int) ev.getRawY());
//				v.getLocalVisibleRect(mRect);
//				Log.e(TAG, "getLocalVisibleRect:" + mRect.left + "," + mRect.top + "," + mRect.right + "," + mRect.bottom);
//				v.getHitRect(mRect);
//				Log.e(TAG, "getHitRect:" + mRect.left + "," + mRect.top + "," + mRect.right + "," + mRect.bottom);
//				v.getDrawingRect(mRect);
//				Log.e(TAG, "getDrawingRect:" + mRect.left + "," + mRect.top + "," + mRect.right + "," + mRect.bottom);
//				v.getGlobalVisibleRect(mRect);
//				Log.e(TAG, "getGlobalVisibleRect:" + mRect.left + "," + mRect.top + "," + mRect.right + "," + mRect.bottom);
//			}catch (Exception e){
//				Log.e(TAG,"e:"+e);
//			}
			v.getGlobalVisibleRect(mRect);
			if(mRect.contains((int)ev.getRawX(), (int)ev.getRawY())){
				touchViewList.add(v);
			}
		}
		return touchViewList;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			viewWidth = this.getWidth();

			mChildViewList.clear();
			getChildShouldInterceptViewList(mChildViewList, this);
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (mShadowDrawable != null && mContentView != null) {

			int left = mContentView.getLeft()
					- mShadowDrawable.getIntrinsicWidth();
			int right = left + mShadowDrawable.getIntrinsicWidth();
			int top = mContentView.getTop();
			int bottom = mContentView.getBottom();

			mShadowDrawable.setBounds(left, top, right, bottom);
			mShadowDrawable.draw(canvas);
		}

	}


	/**
	 * 滚动出界面
	 */
	private void scrollRight() {
		final int delta = (viewWidth + mContentView.getScrollX());
		// 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
		mScroller.startScroll(mContentView.getScrollX(), 0, -delta + 1, 0,
				Math.abs(delta));
		postInvalidate();
	}

	/**
	 * 滚动到起始位置
	 */
	private void scrollOrigin() {
		int delta = mContentView.getScrollX();
		mScroller.startScroll(mContentView.getScrollX(), 0, -delta, 0,
				Math.abs(delta));
		postInvalidate();
	}

	@Override
	public void computeScroll() {
		// 调用startScroll的时候scroller.computeScrollOffset()返回true，
		if (mScroller.computeScrollOffset()) {
			mContentView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();

			if (mScroller.isFinished() && isFinish) {
				mActivity.finish();
			}
		}
	}


	public void setIsCanScroll(boolean isEnable) {
		this.isEnable = isEnable;
	}
}
