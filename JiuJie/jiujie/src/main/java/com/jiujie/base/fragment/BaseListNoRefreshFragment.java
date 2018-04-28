package com.jiujie.base.fragment;

import com.jiujie.base.R;
import com.jiujie.base.util.recycler.RecyclerViewUtil;


/**
 * @author ChenJiaLiang
 */
public abstract class BaseListNoRefreshFragment extends BaseListFragment{

	@Override
	public int getLayoutId() {
		return R.layout.recyclerview;
	}

	@Override
	public RecyclerViewUtil getRecyclerViewUtil() {
		return new RecyclerViewUtil(this,mActivity,mView,0, getRecyclerViewId(),getAdapter(),getRecycleViewType(),getRecycleViewGridNum());
	}

	protected int getRecyclerViewId() {
		return R.id.recyclerView;
	}
}
