package com.jiujie.base.activity;

import android.support.v7.widget.RecyclerView;

import com.jiujie.base.R;
import com.jiujie.base.jk.OnTitleClickMoveToTopListen;
import com.jiujie.base.jk.Refresh;
import com.jiujie.base.util.recycler.RecyclerViewUtil;


/**
 * @author ChenJiaLiang
 */
public abstract class BaseListActivity extends BaseActivity implements Refresh,OnTitleClickMoveToTopListen {

	public RecyclerViewUtil recyclerViewUtil;
	public int page;
	public int size = 20;

	@Override
	public int getLayoutId() {
		return R.layout.refresh_recyclerview;
	}

	public abstract RecyclerView.Adapter getAdapter();

	@Override
	protected void initView() {
		super.initView();
		recyclerViewUtil = new RecyclerViewUtil(mActivity,R.id.rr_SwipeRefreshLayout,R.id.rr_recyclerView,getAdapter(),getRecycleViewType(),getRecycleViewGridNum());
		recyclerViewUtil.setRefreshListen(this);
	}

	protected boolean isEndFromSize(){
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
}
