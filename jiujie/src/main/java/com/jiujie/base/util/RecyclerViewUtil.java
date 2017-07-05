package com.jiujie.base.util;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.jiujie.base.R;
import com.jiujie.base.adapter.BaseRecyclerViewAdapter;
import com.jiujie.base.jk.OnScrolledListen;
import com.jiujie.base.jk.Refresh;


/**
 * author : Created by ChenJiaLiang on 2016/4/14.
 * Email : 576507648@qq.com
 * 初始化的时候，千万千万注意，是Activity的，还是Fragment的，否则，可能造成多个fragment中的fragment不显示
 */
public class RecyclerViewUtil {
    private final int type;
    private final int num;
    private final View header;
    private Refresh refresh;
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private Activity mActivity;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter adapter;
    private OnScrolledListen onScrolledListen;
    private boolean isLoadingData;
    private boolean isEnd;
    private boolean isFooterEnable = true;

    public RecyclerViewUtil(Activity mActivity, SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView, RecyclerView.Adapter adapter,int type,int num,View header){
        this.mActivity = mActivity;
        this.adapter = adapter;
        this.mSwipeRefreshWidget = swipeRefreshLayout;
        this.mRecyclerView = recyclerView;
        this.type = type;
        this.num = num;
        this.header = header;
        init();
    }

    public RecyclerViewUtil(Activity mActivity, SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView, RecyclerView.Adapter adapter,int type,int num){
        this(mActivity,swipeRefreshLayout,recyclerView,adapter,type,num,null);
    }

    public RecyclerViewUtil(Activity mActivity, SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView, RecyclerView.Adapter adapter){
        this(mActivity,swipeRefreshLayout,recyclerView,adapter,0,0,null);
    }

    /**
     * use in Activity
     */
    public RecyclerViewUtil(Activity mActivity, int swipeRefreshLayoutId, int recyclerViewId, RecyclerView.Adapter adapter,int type,int num,View header){
        this.mActivity = mActivity;
        this.adapter = adapter;
        this.type = type;
        this.num = num;
        this.header = header;
        if(swipeRefreshLayoutId>0)mSwipeRefreshWidget = (SwipeRefreshLayout) mActivity.findViewById(swipeRefreshLayoutId);
        mRecyclerView = (RecyclerView) mActivity.findViewById(recyclerViewId);
        init();
    }

    /**
     * use in Activity
     */
    public RecyclerViewUtil(Activity mActivity, int swipeRefreshLayoutId, int recyclerViewId, RecyclerView.Adapter adapter,int type,int num){
        this(mActivity,swipeRefreshLayoutId,recyclerViewId,adapter,type,num,null);
    }
    /**
     * use in Activity
     */
    public RecyclerViewUtil(Activity mActivity, int swipeRefreshLayoutId, int recyclerViewId, RecyclerView.Adapter adapter){
        this(mActivity,swipeRefreshLayoutId,recyclerViewId,adapter,0,0,null);
    }
    /**
     * use in Fragment
     */
    public RecyclerViewUtil(Activity mActivity, View rootView, int swipeRefreshLayoutId, int recyclerViewId, RecyclerView.Adapter adapter,int type,int num,View header){
        this.mActivity = mActivity;
        this.adapter = adapter;
        this.type = type;
        this.num = num;
        this.header = header;
        if(swipeRefreshLayoutId>0)mSwipeRefreshWidget = (SwipeRefreshLayout) rootView.findViewById(swipeRefreshLayoutId);
        mRecyclerView = (RecyclerView) rootView.findViewById(recyclerViewId);
        init();
    }
    /**
     * use in Fragment
     */
    public RecyclerViewUtil(Activity mActivity, View rootView, int swipeRefreshLayoutId, int recyclerViewId, RecyclerView.Adapter adapter,int type,int num){
        this(mActivity,rootView,swipeRefreshLayoutId,recyclerViewId,adapter,type,num,null);
    }
    /**
     * use in Fragment
     */
    public RecyclerViewUtil(Activity mActivity, View rootView, int swipeRefreshLayoutId, int recyclerViewId, RecyclerView.Adapter adapter){
        this(mActivity,rootView,swipeRefreshLayoutId,recyclerViewId,adapter,0,0,null);
    }

    public void setRefreshEnable(boolean isEnable){
        if(mSwipeRefreshWidget!=null) mSwipeRefreshWidget.setEnabled(isEnable);
    }

    private void init() {
        initRecyclerView();
        if(adapter!=null)
            mRecyclerView.setAdapter(adapter);
        setReadMore();

        if(mSwipeRefreshWidget!=null){
            mSwipeRefreshWidget.setColorSchemeColors(mActivity.getResources().getColor(R.color.title_bg));
            mSwipeRefreshWidget.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (refresh != null&&!isLoadingData) {
                        refresh.refresh();
                    }else{
                        setRefreshing(false);
                    }
                }
            });
        }

        mRecyclerView.clearOnScrollListeners();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;
            long time = 0;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(lastVisibleItem + 1 == adapter.getItemCount()){
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if(!isFooterEnable)return;

                        long current = System.currentTimeMillis();
                        if(current-time<10){
                            time = current;
                            return;
                        }
                        time = current;

                        if(!isEnd){
                            if (refresh != null&&!isLoadingData) {
                                refresh.loadMore();
                            }
                            setReadMore();
                        }else{
                            setReadEnd();
                        }
                    }else{
                        if(isEnd){
                            setReadEnd();
                        }else{
                            setReadMore();
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
                if(layoutManager instanceof LinearLayoutManager){
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                }else if(layoutManager instanceof StaggeredGridLayoutManager){
                    StaggeredGridLayoutManager layoutManager1 = (StaggeredGridLayoutManager) layoutManager;
                    int[] lastPositions = new int[layoutManager1.getSpanCount()];
                    layoutManager1.findLastCompletelyVisibleItemPositions(lastPositions);
                    lastVisibleItem = findMax(lastPositions);
                }
                if(onScrolledListen!=null)onScrolledListen.onScrolled(recyclerView,dx,dy);
            }
        });
    }

    //To find the maximum value in the array
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public void isLoadingData(boolean isLoadingData) {
        this.isLoadingData = isLoadingData;
    }

    public void isEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }

    public void setOnScrolledListen(OnScrolledListen onScrolledListen) {
        this.onScrolledListen = onScrolledListen;
    }

    private void initRecyclerView() {
        UIHelper.initRecyclerView(mActivity,mRecyclerView,type,num);
    }

    public void setRefreshListen(Refresh refresh){
        this.refresh = refresh;
    }

    public void setRefreshing(boolean isRefreshing){
        if(mSwipeRefreshWidget!=null) mSwipeRefreshWidget.setRefreshing(isRefreshing);
    }

    public void setRefreshing(){
        setRefreshing(true);
    }

    public void onRefreshComplete(){
        setRefreshing(false);
    }

    public void setReadEnd(){
        if(adapter!=null&&adapter instanceof BaseRecyclerViewAdapter){
            ((BaseRecyclerViewAdapter) adapter).setReadEnd();
        }
    }

    public void setReadMore(){
        if(adapter!=null&&adapter instanceof BaseRecyclerViewAdapter){
            ((BaseRecyclerViewAdapter) adapter).setReadMore();
        }
    }

    public void setFooterEnable(boolean isFooterEnable){
        this.isFooterEnable = isFooterEnable;
    }

    public void addHeaderView(View header){
        if(adapter!=null&&adapter instanceof BaseRecyclerViewAdapter){
            ((BaseRecyclerViewAdapter) adapter).addHeaderView(header);
        }
    }

    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    public void notifyDataSetChanged(){
        if(adapter!=null){
            if(adapter instanceof BaseRecyclerViewAdapter){
                ((BaseRecyclerViewAdapter) adapter).notifyDataSetChanged1();
            }else{
                adapter.notifyDataSetChanged();
            }
        }
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }
}
