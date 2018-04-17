package com.jiujie.base.fragment;

import com.jiujie.base.R;
import com.jiujie.base.adapter.BaseRecyclerViewAdapter;
import com.jiujie.base.jk.OnTitleClickMoveToTopListen;
import com.jiujie.base.jk.Refresh;
import com.jiujie.base.util.UIHelper;
import com.jiujie.base.util.recycler.RecyclerViewUtil;


/**
 * @author ChenJiaLiang
 */
public abstract class BaseListFragment extends BaseFragment implements Refresh, OnTitleClickMoveToTopListen {

    public RecyclerViewUtil recyclerViewUtil;
    public int page;
    public int size = 20;

    @Override
    public int getLayoutId() {
        return R.layout.refresh_recyclerview;
    }

    public abstract BaseRecyclerViewAdapter getAdapter();

    @Override
    protected void initView() {
        super.initView();
        recyclerViewUtil = getRecyclerViewUtil();
        recyclerViewUtil.setRefreshListen(this);
        int recycleViewType = getRecycleViewType();
        if (recycleViewType == 0) {
            size = 20;
        } else {
            size = getRecycleViewGridNum() * 10;
            if (size < 20) {
                size = 20;
            }
        }
    }

    protected boolean isEndFromSize() {
        return false;
    }

    protected RecyclerViewUtil getRecyclerViewUtil() {
        return new RecyclerViewUtil(mActivity, mView, getSwipeRefreshLayoutId(), getRecyclerViewId(), getAdapter(), getRecycleViewType(), getRecycleViewGridNum());
    }

    protected int getRecyclerViewId() {
        return R.id.rr_recyclerView;
    }

    protected int getSwipeRefreshLayoutId() {
        return R.id.rr_SwipeRefreshLayout;
    }

    public int getRecycleViewType() {
        return 0;
    }

    public int getRecycleViewGridNum() {
        return 2;
    }

    public void setRefreshEnable(boolean isEnable) {
        if (recyclerViewUtil != null) recyclerViewUtil.setRefreshEnable(isEnable);
    }

    @Override
    public void initData() {

    }

    @Override
    public void refresh() {

    }

    @Override
    public void loadMore() {

    }

    public boolean isEnd() {
        return recyclerViewUtil != null && recyclerViewUtil.isEnd();
    }

    public void setEnd(boolean isEnd) {
        if (recyclerViewUtil != null) recyclerViewUtil.setEnd(isEnd);
    }

    public void notifyDataSetChanged() {
        if (recyclerViewUtil == null) return;
        recyclerViewUtil.notifyDataSetChanged(true);
    }

    @Override
    public void moveToTop() {
        if (recyclerViewUtil == null) return;
        recyclerViewUtil.getRecyclerView().smoothScrollToPosition(0);
    }

    /**
     * @param type 0:first,1:refresh,2:loadNextPage
     */
    protected void setLoadDataStart(int type) {
        if (type == 0) {
            setLoading();
            setEnd(false);
        } else if (type == 1) {
            recyclerViewUtil.setRefreshing(true);
            setEnd(false);
        } else if (type == 2) {
            recyclerViewUtil.setReadMore();
        }
    }

    protected void setLoadDataUIEnd(int type) {
        recyclerViewUtil.isLoadingData(false);
        if (type == 0) {
            setLoadingEnd();
        } else if (type == 1) {
            recyclerViewUtil.setRefreshing(false);
        }
        if (isEnd()) {
            recyclerViewUtil.setReadEnd();
        }
        recyclerViewUtil.notifyDataSetChanged(type == 0 || type == 1);
    }

    @Override
    public void setLoadingFail() {
        super.setLoadingFail();
        recyclerViewUtil.isLoadingData(false);
    }

    protected void setLoadDataFail(int type, String error) {
        recyclerViewUtil.isLoadingData(false);
        if (type == 0) {
            setLoadingFail();
        } else if (type == 1) {
            recyclerViewUtil.setRefreshing(false);
        } else if (type == 2) {
            recyclerViewUtil.setReadFail();
        }
        if (type == 1) {
            UIHelper.showToastShort(error);
        } else if (type == 2) {
            page--;
            UIHelper.showToastShort(error);
        }
    }
}
