package com.jiujie.base.fragment;

import com.jiujie.base.R;
import com.jiujie.base.adapter.BaseRecyclerViewAdapter;
import com.jiujie.base.jk.Refresh;
import com.jiujie.base.util.RecyclerViewUtil;


/**
 * @author ChenJiaLiang
 */
public abstract class BaseListFragment extends BaseFragment implements Refresh {

	public boolean isEnd;
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
	}

	protected RecyclerViewUtil getRecyclerViewUtil(){
		return new RecyclerViewUtil(mActivity,mView, getSwipeRefreshLayoutId(), getRecyclerViewId(),getAdapter(),getRecycleViewType(),getRecycleViewGridNum());
	}

	protected int getRecyclerViewId() {
		return R.id.rr_recyclerView;
	}

	protected int getSwipeRefreshLayoutId() {
		return R.id.rr_SwipeRefreshLayout;
	}

	public int getRecycleViewType(){
		return 0;
	}

	public int getRecycleViewGridNum(){
		return 2;
	}

	public void setRefreshEnable(boolean isEnable){
		if(recyclerViewUtil!=null) recyclerViewUtil.setRefreshEnable(isEnable);
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

	/**
	 * @param type 0:first,1:refresh,2:loadNextPage
	 */
	public void setLoadDataStart(int type){
		if(recyclerViewUtil==null)return;
		recyclerViewUtil.isLoadingData(true);
		if(type==0){
			setLoading();
			isEnd = false;
		}else if(type==1){
			recyclerViewUtil.setRefreshing(true);
			isEnd = false;
		}else if(type==2){
			recyclerViewUtil.setReadMore();
		}
	}
	/**
	 * @param type 0:first,1:refresh,2:loadNextPage
	 */
	public void setLoadDataEnd(int type){
		recyclerViewUtil.isLoadingData(false);
		recyclerViewUtil.isEnd(isEnd);
		if(type==0){
			setLoadingEnd();
		}else if(type==1){
			recyclerViewUtil.setRefreshing(false);
		}
		if(isEnd)recyclerViewUtil.setReadEnd();
		else recyclerViewUtil.setReadMore();
		recyclerViewUtil.notifyDataSetChanged();
	}
}
