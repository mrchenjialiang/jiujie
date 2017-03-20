package com.jiujie.base.activity;

import android.view.View;

/**
 * 为了几个抽象方法在后续不需全部实现
 */
public abstract class BaseNoSlideActivity extends BaseBeforeNoSlideActivity {

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
