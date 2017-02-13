package com.jiujie.base.util;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jiujie.base.R;
import com.jiujie.base.adapter.BaseRecyclerViewAdapter;
import com.jiujie.base.fragment.BaseListNoTitleFragment;
import com.jiujie.base.jk.LoadStatus;
import com.jiujie.base.jk.OnScrolledListen;
import com.jiujie.base.jk.Refresh;


/**
 * author : Created by ChenJiaLiang on 2016/4/14.
 * Email : 576507648@qq.com
 */
public class RecyclerViewUtil {
    private Refresh refresh;
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private Activity mActivity;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter adapter;
    private OnScrolledListen onScrolledListen;
    private boolean isLoadingData;
    private boolean isEnd;

    /**
     * use in Activity
     */
    public RecyclerViewUtil(Activity mActivity, int swipeRefreshLayoutId, int recyclerViewId, RecyclerView.Adapter adapter){
        this.mActivity = mActivity;
        this.adapter = adapter;
        mSwipeRefreshWidget = (SwipeRefreshLayout) mActivity.findViewById(swipeRefreshLayoutId);
        mRecyclerView = (RecyclerView) mActivity.findViewById(recyclerViewId);
        init();
    }
    /**
     * use in Fragment
     */
    public RecyclerViewUtil(Activity mActivity, View rootView, int swipeRefreshLayoutId, int recyclerViewId, RecyclerView.Adapter adapter){
        this.mActivity = mActivity;
        this.adapter = adapter;
        mSwipeRefreshWidget = (SwipeRefreshLayout) rootView.findViewById(swipeRefreshLayoutId);
        mRecyclerView = (RecyclerView) rootView.findViewById(recyclerViewId);
        init();
    }

    public RecyclerViewUtil(Activity mActivity, SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView, RecyclerView.Adapter adapter){
        this.mActivity = mActivity;
        this.adapter = adapter;
        this.mSwipeRefreshWidget = swipeRefreshLayout;
        this.mRecyclerView = recyclerView;
        init();
    }

    public void setRefreshEnable(boolean isEnable){
        if(mSwipeRefreshWidget!=null) mSwipeRefreshWidget.setEnabled(isEnable);
    }

    private void init() {
        initRecyclerView();
        if(adapter!=null)
        mRecyclerView.setAdapter(adapter);
        setReadEnd();

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

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == adapter.getItemCount()) {
                    if(!isEnd){
                        if (refresh != null&&!isLoadingData) {
                            refresh.loadMore();
                        }else{
                            if(isEnd){
                                setReadEnd();
                            }else{
                                setReadMore();
                            }
                        }
                    }else{
                        setReadEnd();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                if(onScrolledListen!=null)onScrolledListen.onScrolled(recyclerView,dx,dy);
            }
        });
    }

    public void isLoadingData(boolean isLoadingData) {
        this.isLoadingData = isLoadingData;
    }

    public void isEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }

//    public void setLoadDataStart(Class<? instanceof  LoadStatus> loadStatus, int type) {
//        isLoadingData(true);
//        if(type==0){
//            loadStatus.
//            setLoading();
//        }else if(type==1){
//            setRefreshing(true);
//        }else if(type==2){
//            setReadMore();
//        }
//    }

    public void setOnScrolledListen(OnScrolledListen onScrolledListen) {
        this.onScrolledListen = onScrolledListen;
    }

    public void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        if(mLayoutManager==null)mLayoutManager = new LinearLayoutManager(mActivity.getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
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

    public void hideFooter(){
        if(adapter!=null&&adapter instanceof BaseRecyclerViewAdapter){
            ((BaseRecyclerViewAdapter) adapter).hideFooter();
        }
    }

    public void showFooter(){
        if(adapter!=null&&adapter instanceof BaseRecyclerViewAdapter){
            ((BaseRecyclerViewAdapter) adapter).showFooter();
        }
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
        mRecyclerView.setLayoutManager(mLayoutManager==null?(mLayoutManager = new LinearLayoutManager(mActivity.getApplicationContext())):mLayoutManager );
        if(adapter!=null)
            adapter.notifyDataSetChanged();
    }
}
