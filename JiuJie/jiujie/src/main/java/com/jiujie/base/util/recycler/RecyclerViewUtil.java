package com.jiujie.base.util.recycler;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.jiujie.base.R;
import com.jiujie.base.adapter.BaseRecyclerViewAdapter;
import com.jiujie.base.jk.OnScrollPositionListener;
import com.jiujie.base.jk.OnScrolledListen;
import com.jiujie.base.jk.Refresh;
import com.jiujie.base.util.UIHelper;


/**
 * author : Created by ChenJiaLiang on 2016/4/14.
 * Email : 576507648@qq.com
 * 初始化的时候，千万千万注意，是Activity的，还是Fragment的，否则，可能造成多个fragment中的fragment不显示
 */
public class RecyclerViewUtil {
    private int type;
    private int num;
    private View header;
    private Refresh refresh;
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private Activity mActivity;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter adapter;
    private OnScrolledListen onScrolledListen;
    private boolean isLoadingData;
    private boolean isEnd;
    private boolean isFooterEnable = true;
    private int advanceSize;

    private RecyclerViewUtil(){
    }

    public RecyclerViewUtil(Refresh refresh,Activity mActivity, SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView, RecyclerView.Adapter adapter,int type,int num,View header){
        this.refresh = refresh;
        this.mActivity = mActivity;
        this.adapter = adapter;
        this.mSwipeRefreshWidget = swipeRefreshLayout;
        this.mRecyclerView = recyclerView;
        this.type = type;
        this.num = num;
        this.header = header;
        init();
    }

    public RecyclerViewUtil(Refresh refresh,Activity mActivity, SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView, RecyclerView.Adapter adapter,int type,int num){
        this(refresh,mActivity,swipeRefreshLayout,recyclerView,adapter,type,num,null);
    }

    public RecyclerViewUtil(Refresh refresh,Activity mActivity, SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView, RecyclerView.Adapter adapter){
        this(refresh,mActivity,swipeRefreshLayout,recyclerView,adapter,0,0,null);
    }

    /**
     * use in Activity
     */
    public RecyclerViewUtil(Refresh refresh,Activity mActivity, int swipeRefreshLayoutId, int recyclerViewId, RecyclerView.Adapter adapter,int type,int num,View header){
        this.refresh = refresh;
        this.mActivity = mActivity;
        this.adapter = adapter;
        this.type = type;
        this.num = num;
        this.header = header;
        if(swipeRefreshLayoutId>0)mSwipeRefreshWidget = (SwipeRefreshLayout) mActivity.findViewById(swipeRefreshLayoutId);
        mRecyclerView = mActivity.findViewById(recyclerViewId);
        init();
    }

    /**
     * use in Activity
     */
    public RecyclerViewUtil(Refresh refresh,Activity mActivity, int swipeRefreshLayoutId, int recyclerViewId, RecyclerView.Adapter adapter,int type,int num){
        this(refresh,mActivity,swipeRefreshLayoutId,recyclerViewId,adapter,type,num,null);
    }
    /**
     * use in Activity
     */
    public RecyclerViewUtil(Refresh refresh,Activity mActivity, int swipeRefreshLayoutId, int recyclerViewId, RecyclerView.Adapter adapter){
        this(refresh,mActivity,swipeRefreshLayoutId,recyclerViewId,adapter,0,0,null);
    }
    /**
     * use in Fragment
     */
    public RecyclerViewUtil(Refresh refresh,Activity mActivity, View rootView, int swipeRefreshLayoutId, int recyclerViewId, RecyclerView.Adapter adapter,int type,int num,View header){
        this.refresh = refresh;
        this.mActivity = mActivity;
        this.adapter = adapter;
        this.type = type;
        this.num = num;
        this.header = header;
        if(swipeRefreshLayoutId>0)mSwipeRefreshWidget = (SwipeRefreshLayout) rootView.findViewById(swipeRefreshLayoutId);
        mRecyclerView = rootView.findViewById(recyclerViewId);
        init();
    }
    /**
     * use in Fragment
     */
    public RecyclerViewUtil(Refresh refresh,Activity mActivity, View rootView, int swipeRefreshLayoutId, int recyclerViewId, RecyclerView.Adapter adapter,int type,int num){
        this(refresh,mActivity,rootView,swipeRefreshLayoutId,recyclerViewId,adapter,type,num,null);
    }
    /**
     * use in Fragment
     */
    public RecyclerViewUtil(Refresh refresh,Activity mActivity, View rootView, int swipeRefreshLayoutId, int recyclerViewId, RecyclerView.Adapter adapter){
        this(refresh,mActivity,rootView,swipeRefreshLayoutId,recyclerViewId,adapter,0,0,null);
    }

    public void setRefreshEnable(boolean isEnable){
        if(mSwipeRefreshWidget!=null) mSwipeRefreshWidget.setEnabled(isEnable);
    }

    public void setAdvanceSize(int advanceSize){
        this.advanceSize = advanceSize;
    }

    private void init() {
        initRecyclerView();
        if(adapter!=null)
            mRecyclerView.setAdapter(adapter);
        setReadMore();

        if(mSwipeRefreshWidget!=null){
            mSwipeRefreshWidget.setColorSchemeColors(ContextCompat.getColor(mActivity,R.color.refresh_color));
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
            int lastVisiblePosition;
            long time = 0;
            int firstVisibleItemPosition;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                checkLoadMore();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
                if(layoutManager instanceof LinearLayoutManager){
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                    lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();
                    firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                }else if(layoutManager instanceof StaggeredGridLayoutManager){
                    StaggeredGridLayoutManager layoutManager1 = (StaggeredGridLayoutManager) layoutManager;
                    int[] lastPositions = new int[layoutManager1.getSpanCount()];
                    layoutManager1.findLastCompletelyVisibleItemPositions(lastPositions);
                    lastVisiblePosition = findMax(lastPositions);
                    int[] firstPositions = new int[layoutManager1.getSpanCount()];
                    layoutManager1.findFirstVisibleItemPositions(firstPositions);
                    firstVisibleItemPosition = findMin(firstPositions);
                }
                if(onScrolledListen!=null)onScrolledListen.onScrolled(recyclerView,dx,dy);
                if(onScrollPositionListener!=null)onScrollPositionListener.onScroll(firstVisibleItemPosition, lastVisiblePosition);

                checkLoadMore();
            }

            private void checkLoadMore() {
                if(adapter instanceof BaseRecyclerViewAdapter &&!isEnd){
                    BaseRecyclerViewAdapter mAdapter = (BaseRecyclerViewAdapter) adapter;
                    if(mAdapter.getCount()>=(refresh!=null&&refresh.isEndFromSize()?refresh.getSize():1)){
                        if(mAdapter.getLastPosition() + 1 >= adapter.getItemCount()- (isLoadMoreFail()?0:advanceSize)) {
                            if (!isFooterEnable) return;

                            long current = System.currentTimeMillis();
                            if (current - time < 10) {
                                time = current;
                                return;
                            }
                            time = current;

                            if (refresh != null && !isLoadingData) {
                                refresh.loadMore();
                                setReadMore();
                                isLoadingData = true;
                            }
                        }
                    }
                }else{
                    setReadEnd();
                }
            }
        });
    }

    private OnScrollPositionListener onScrollPositionListener;

    public void setOnScrollPositionListener(OnScrollPositionListener onScrollPositionListener) {
        this.onScrollPositionListener = onScrollPositionListener;
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
    private int findMin(int[] firstPositions) {
        int min = firstPositions[0];
        for (int value : firstPositions) {
            if (value < min) {
                min = value;
            }
        }
        return min;
    }

    public void isLoadingData(boolean isLoadingData) {
        this.isLoadingData = isLoadingData;
    }

    public void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }

    public boolean isEnd(){
        return isEnd;
    }

    public void setOnScrolledListen(OnScrolledListen onScrolledListen) {
        this.onScrolledListen = onScrolledListen;
    }

    private void initRecyclerView() {
        UIHelper.initRecyclerView(mActivity,mRecyclerView,type,num);
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

    public void setReadFail(){
        if(adapter!=null&&adapter instanceof BaseRecyclerViewAdapter){
            ((BaseRecyclerViewAdapter) adapter).setReadFail();
        }
    }

    public boolean isLoadMoreFail() {
        return adapter != null && adapter instanceof BaseRecyclerViewAdapter && ((BaseRecyclerViewAdapter) adapter).getFooterStatus() == BaseRecyclerViewAdapter.Footer_Status_Load_Fail;
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
        notifyDataSetChanged(false);
    }

    public void notifyDataSetChanged(boolean isForceNotifyAll){
        if(adapter!=null){
            if(isForceNotifyAll){
                adapter.notifyDataSetChanged();
            }else{
                if(adapter instanceof BaseRecyclerViewAdapter){
                    ((BaseRecyclerViewAdapter) adapter).notifyDataSetChanged1();
                }else{
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

//    public void setLoadDataStart(int type, LoadStatus loadStatus) {
//        isLoadingData(true);
//        if(type==0){
//            loadStatus.setLoading();
//            setEnd(false);
//        }else if(type==1){
//            setRefreshing(true);
//            setEnd(false);
//        }else if(type==2){
//            setReadMore();
//        }
//    }
//
//    public void setLoadDataEnd(int type, LoadStatus loadStatus) {
//        isLoadingData(false);
//        if(type==0){
//            loadStatus.setLoadingEnd();
//        }else if(type==1){
//            setRefreshing(false);
//        }
//        if(isEnd)setReadEnd();
//        else setReadMore();
//        notifyDataSetChanged(type==0||type==1);
//    }
//
//    public void setLoadDataFail(int type,LoadStatus loadStatus){
//        isLoadingData(false);
//        if(type==0){
//            loadStatus.setLoadingFail();
//        }else if(type==1){
//            setRefreshing(false);
//        }else if(type==2){
//            setReadFail();
//        }
//    }
}
