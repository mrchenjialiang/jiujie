package com.jiujie.base.fragment;

import com.jiujie.base.R;
import com.jiujie.base.adapter.BaseRecyclerViewAdapter;
import com.jiujie.base.jk.OnTitleClickMoveToTopListen;
import com.jiujie.base.jk.Refresh;
import com.jiujie.base.util.RecyclerViewUtil;


/**
 * @author ChenJiaLiang
 */
public abstract class BaseListFragment extends BaseFragment implements Refresh,OnTitleClickMoveToTopListen {

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
		if(recycleViewType==0){
			size = 20;
		}else{
			size = getRecycleViewGridNum()*10;
			if(size<20){
				size = 20;
			}
		}
	}

	protected boolean isEndFromSize(){
		return false;
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

	public boolean isEnd() {
		return recyclerViewUtil != null && recyclerViewUtil.isEnd();
	}

	public void setEnd(boolean isEnd){
		if(recyclerViewUtil!=null)recyclerViewUtil.setEnd(isEnd);
	}

	/**
	 * @param type 0:first,1:refresh,2:loadNextPage
	 */
	public void setLoadDataStart(int type){
		if(recyclerViewUtil==null)return;
		recyclerViewUtil.setLoadDataStart(type,this);
	}

	/**
	 * @param type 0:first,1:refresh,2:loadNextPage
	 */
	public void setLoadDataEnd(int type){
		if(recyclerViewUtil==null)return;
		recyclerViewUtil.setLoadDataEnd(type,this);
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
