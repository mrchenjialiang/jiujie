package com.jiujie.base.fragment;

import android.os.Bundle;

import com.jiujie.base.R;
import com.jiujie.base.adapter.BaseRecyclerViewAdapter;
import com.jiujie.base.jk.Refresh;
import com.jiujie.base.util.RecyclerViewUtil;


/**
 * @author ChenJiaLiang
 */
public abstract class BaseListFragment extends BaseFragment implements  Refresh {

	public boolean isEnd;
	public RecyclerViewUtil recyclerViewUtil;
	public int page;
	public int size = 20;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}

	@Override
	public int getLayoutId() {
		return R.layout.refresh_recyclerview;
	}

	public abstract BaseRecyclerViewAdapter getAdapter();

	private void initView() {
		recyclerViewUtil = new RecyclerViewUtil(mActivity,mView,R.id.rr_SwipeRefreshLayout,R.id.rr_recyclerView,getAdapter());
		recyclerViewUtil.setRefreshListen(this);
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
		if(isEnd)recyclerViewUtil.setReadEnd();
		else recyclerViewUtil.setReadMore();
		if(type==0){
			setLoadingEnd();
		}else if(type==1){
			recyclerViewUtil.setRefreshing(false);
		}else if(type==2){
			recyclerViewUtil.setReadEnd();
		}
		recyclerViewUtil.notifyDataSetChanged();
	}
}
