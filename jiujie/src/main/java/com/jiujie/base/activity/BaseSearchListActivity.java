package com.jiujie.base.activity;

import android.graphics.Color;
import android.os.Bundle;

import com.jiujie.base.R;
import com.jiujie.base.SearchTitle;


/**
 * @author ChenJiaLiang
 */
public abstract class BaseSearchListActivity extends BaseListActivity{

	public SearchTitle mTitle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mTitle = new SearchTitle(this);
		setToolBarBackGround(Color.parseColor("#efeff4"));
	}

	@Override
	public int getCustomTitleLayoutId() {
		return R.layout.jiujie_title_search;
	}
}
