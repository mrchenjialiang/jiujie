package com.jiujie.base.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.jiujie.base.adapter.BaseExpandAdapter;


/**
 * 改自github 的 PinnedHeaderExpandableListView
 * author : Created by ChenJiaLiang on 2016/8/31.
 * Email 576507648@qq.com
 * android:groupIndicator="@null"---去掉默认箭头
 */
public class ExpandableListViewOneHeaderKeepTop extends ExpandableListView implements OnScrollListener,OnGroupClickListener {

    private final String TAG = "ExpandableListViewOneHeaderKeepTop";
    private static final int MAX_ALPHA = 255;

    private BaseExpandAdapter mAdapter;
    private View mKeepTopHeaderView;
    private int mHeaderViewWidth;
    private int mHeaderViewHeight;
    private float mDownX;
    private float mDownY;
    private KeepTopHeaderStatus mCurrentHeaderStatus = KeepTopHeaderStatus.GONE;

    public enum KeepTopHeaderStatus{
        //       NO_INIT,
        GONE,VISIBLE,PUSHING_UP
    }

    public ExpandableListViewOneHeaderKeepTop(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        registerListener();
    }

    public ExpandableListViewOneHeaderKeepTop(Context context, AttributeSet attrs) {
        super(context, attrs);
        registerListener();
    }

    public ExpandableListViewOneHeaderKeepTop(Context context) {
        super(context);
        registerListener();
    }

    private void setKeepTopHeaderView(View view) {
        mKeepTopHeaderView = view;
        LayoutParams lp = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);

        if (mKeepTopHeaderView != null) {
            setFadingEdgeLength(0);
        }

        requestLayout();
    }

    private void registerListener() {
        setOnScrollListener(this);
        setOnGroupClickListener(this);
    }

    /**
     * 点击 mKeepTopHeaderView 触发的事件
     */
    private void onKeepTopHeaderClick() {
        long packedPosition = getExpandableListPosition(this.getFirstVisiblePosition());

        int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);

        if(this.isGroupExpanded(groupPosition)){
            this.collapseGroup(groupPosition);
        }else{
            this.expandGroup(groupPosition);
        }

        this.setSelectedGroup(groupPosition);
    }

    /**
     * 如果 mKeepTopHeaderView 是可见的 , 此函数用于判断是否点击了 mKeepTopHeaderView, 并对做相应的处理 ,
     * 因为 mKeepTopHeaderView 是画上去的 , 所以设置事件监听是无效的 , 只有自行控制 .
     * 不能写于onTouch事件里，因为如果childView有点击事件，会被抓走...KeepTopHeaderView的触屏事件优先级要高些才行
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mCurrentHeaderStatus!=KeepTopHeaderStatus.GONE&& mKeepTopHeaderView != null) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mDownX = ev.getX();
                    mDownY = ev.getY();
                    if (mDownX <= mHeaderViewWidth && mDownY <= mHeaderViewHeight) {
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    float x = ev.getX();
                    float y = ev.getY();

                    float offsetX = Math.abs(x - mDownX);
                    float offsetY = Math.abs(y - mDownY);
                    // 如果 mKeepTopHeaderView 是可见的 , 点击在 mKeepTopHeaderView 内 , 那么触发 headerClick()
                    if (offsetX <= 5 && offsetY <= 5 && x <= mHeaderViewWidth && y <= mHeaderViewHeight) {
                        onKeepTopHeaderClick();
                        return true;
                    }
                    break;
                default:
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void setAdapter(ExpandableListAdapter adapter) {
        super.setAdapter(adapter);
        if(adapter instanceof BaseExpandAdapter){
            mAdapter = (BaseExpandAdapter) adapter;
            setKeepTopHeaderView(mAdapter.getKeepTopHeaderView(this));
        }
    }

    /**
     * 点击了 Group 触发的事件 , 要根据根据当前点击 Group 的状态来
     */
    @Override
    public boolean onGroupClick(ExpandableListView parent,View v,int groupPosition,long id) {
        if(parent.isGroupExpanded(groupPosition))
            parent.collapseGroup(groupPosition);
        else
            parent.expandGroup(groupPosition);
        // 返回 true 才可以弹回第一行 , 不知道为什么
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mKeepTopHeaderView != null) {
            measureChild(mKeepTopHeaderView, widthMeasureSpec, heightMeasureSpec);
            mHeaderViewWidth = mKeepTopHeaderView.getMeasuredWidth();
            mHeaderViewHeight = mKeepTopHeaderView.getMeasuredHeight();
        }
    }

    private KeepTopHeaderStatus getHeaderState(int groupPosition, int childPosition) {
        if(groupPosition<0&&childPosition<0){
            return KeepTopHeaderStatus.GONE;
        }
        if(mAdapter==null)return KeepTopHeaderStatus.GONE;
        final int childCount = mAdapter.getChildrenCount(groupPosition);
        if (childPosition == childCount - 1) {
            return KeepTopHeaderStatus.PUSHING_UP;
        } else if (childPosition == -1&& !isGroupExpanded(groupPosition)) {
            return KeepTopHeaderStatus.GONE;
        } else {
            return KeepTopHeaderStatus.VISIBLE;
        }
    }

    private void bindKeepTopHeaderView(int groupPosition, int childPosition) {
        if (mKeepTopHeaderView == null || mAdapter == null || mAdapter.getGroupCount() == 0) {
            return;
        }

        if(mCurrentHeaderStatus==KeepTopHeaderStatus.VISIBLE){
            mAdapter.bindKeepTopHeaderView(this, mKeepTopHeaderView, groupPosition, childPosition, MAX_ALPHA);
            mKeepTopHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
        }else if(mCurrentHeaderStatus==KeepTopHeaderStatus.PUSHING_UP){
            View firstView = getChildAt(0);
            int bottom = firstView.getBottom();
            int y;
            int alpha;
            if (bottom < mHeaderViewHeight) {
                y = (bottom - mHeaderViewHeight);
                alpha = MAX_ALPHA * (mHeaderViewHeight + y) / mHeaderViewHeight;
            } else {
                y = 0;
                alpha = MAX_ALPHA;
            }
            mAdapter.bindKeepTopHeaderView(this, mKeepTopHeaderView, groupPosition, childPosition, alpha);

            if (mKeepTopHeaderView.getTop() != y) {
                mKeepTopHeaderView.layout(0, y, mHeaderViewWidth, mHeaderViewHeight + y);
            }
        }
    }

    /**
     * 列表界面更新时调用该方法(如滚动时)
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (mCurrentHeaderStatus!=KeepTopHeaderStatus.GONE&&mKeepTopHeaderView!=null) {
            //分组栏是直接绘制到界面中，而不是加入到ViewGroup中
            drawChild(canvas, mKeepTopHeaderView, getDrawingTime());
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {

        final long flatPos = getExpandableListPosition(firstVisibleItem);
        int groupPosition = ExpandableListView.getPackedPositionGroup(flatPos);
        int childPosition = ExpandableListView.getPackedPositionChild(flatPos);
        mCurrentHeaderStatus = getHeaderState(groupPosition, childPosition);

        bindKeepTopHeaderView(groupPosition, childPosition);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }
}
