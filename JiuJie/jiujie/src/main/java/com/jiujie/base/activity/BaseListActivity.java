package com.jiujie.base.activity;

import android.support.v7.widget.RecyclerView;

import com.jiujie.base.R;
import com.jiujie.base.jk.OnTitleClickMoveToTopListen;
import com.jiujie.base.jk.Refresh;
import com.jiujie.base.util.UIHelper;
import com.jiujie.base.util.recycler.RecyclerViewUtil;


/**
 * @author ChenJiaLiang
 */
public abstract class BaseListActivity extends BaseActivity implements Refresh,OnTitleClickMoveToTopListen {

	public RecyclerViewUtil recyclerViewUtil;
	public int page;
	public int size = 20;

	@Override
	public int getPage() {
		return page;
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public int getLayoutId() {
		return R.layout.refresh_recyclerview;
	}

	public abstract RecyclerView.Adapter getAdapter();

	@Override
	protected void initView() {
		super.initView();
		recyclerViewUtil = new RecyclerViewUtil(this,mActivity,R.id.rr_SwipeRefreshLayout,R.id.rr_recyclerView,getAdapter(),getRecycleViewType(),getRecycleViewGridNum());
	}

	@Override
	public boolean isEndFromSize(){
		return false;
	}

	public int getRecycleViewType(){
		return 0;
	}

	public int getRecycleViewGridNum(){
		return 2;
	}

	public boolean isEnd() {
		return recyclerViewUtil != null && recyclerViewUtil.isEnd();
	}

	public void setEnd(boolean isEnd){
		if(recyclerViewUtil!=null)recyclerViewUtil.setEnd(isEnd);
	}

	public void notifyDataSetChanged(){
		if(recyclerViewUtil==null)return;
		recyclerViewUtil.notifyDataSetChanged(true);
	}

	@Override
	public void moveToTop() {
		if(recyclerViewUtil==null)return;
		recyclerViewUtil.getRecyclerView().smoothScrollToPosition(0);
	}

	/**
	 * @param type 0:first,1:refresh,2:loadNextPage
	 */
	protected void setLoadDataStart(int type){
		if(type==0){
			setLoading();
			setEnd(false);
		}else if(type==1){
			recyclerViewUtil.setRefreshing(true);
			setEnd(false);
		}else if(type==2){
			recyclerViewUtil.setReadMore();
		}
	}

	protected void setLoadDataUIEnd(int type) {
		recyclerViewUtil.isLoadingData(false);
		if(type==0){
			setLoadingEnd();
		}else if(type==1){
			recyclerViewUtil.setRefreshing(false);
		}
		if(isEnd()){
			recyclerViewUtil.setReadEnd();
		}
		recyclerViewUtil.notifyDataSetChanged(type==0||type==1);
	}

	@Override
	public void setLoadingFail() {
		super.setLoadingFail();
		recyclerViewUtil.isLoadingData(false);
	}

	protected void setLoadDataFail(int type, String error) {
		recyclerViewUtil.isLoadingData(false);
		if(type==0){
			setLoadingFail();
		}else if(type==1){
			recyclerViewUtil.setRefreshing(false);
		}else if(type==2){
			recyclerViewUtil.setReadFail();
		}
		if(type==1){
			UIHelper.showToastShort(error);
		}else if(type==2){
			page--;
			UIHelper.showToastShort(error);
		}
	}
}
