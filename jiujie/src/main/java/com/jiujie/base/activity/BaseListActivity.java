package com.jiujie.base.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.jiujie.base.R;
import com.jiujie.base.jk.Refresh;
import com.jiujie.base.util.RecyclerViewUtil;


/**
 * @author ChenJiaLiang
 */
public abstract class BaseListActivity extends BaseActivity implements Refresh {

	public boolean isEnd = true;
	public RecyclerViewUtil recyclerViewUtil;
	public int page;
	public int size = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}
	
	@Override
	public int getLayoutId() {
		return R.layout.refresh_recyclerview;
	}

	public abstract RecyclerView.Adapter getAdapter();

	private void initView() {
		recyclerViewUtil = new RecyclerViewUtil(mActivity,R.id.rr_SwipeRefreshLayout,R.id.rr_recyclerView,getAdapter(),getRecycleViewType(),getRecycleViewGridNum());
		recyclerViewUtil.setRefreshListen(this);
	}

	public int getRecycleViewType(){
		return 0;
	}

	public int getRecycleViewGridNum(){
		return 2;
	}

	/**
	 * @param type 0:first,1:refresh,2:loadNextPage
	 */
	public void setLoadDataStart(int type){
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
		else recyclerViewUtil.hideFooter();
		recyclerViewUtil.notifyDataSetChanged();
	}

	public void notifyDataSetChanged(){
		recyclerViewUtil.notifyDataSetChanged();
	}
}
