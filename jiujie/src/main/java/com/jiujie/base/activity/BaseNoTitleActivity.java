package com.jiujie.base.activity;

import android.view.View;


public abstract class BaseNoTitleActivity extends BaseNoTitleBeforeActivity {

	@Override
	public int getLayoutId() {
		return 0;
	}

	@Override
	public View getContentView() {
		return null;
	}

	@Override
	public void initData() {

	}

}
