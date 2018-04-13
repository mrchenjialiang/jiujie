package com.jiujie.base.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.jiujie.base.R;
import com.jiujie.base.util.UIHelper;
import com.jiujie.glide.GlideCacheUtil;

/**
 * 基础Activity类
 */
public abstract class BaseActivity extends BaseTitleActivity {

	private LinearLayout mLoadingLine,mLoadingFail;
	private LinearLayout contentLayout;
	private LinearLayout mTagLayout;
	private ImmersionBar immersionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		doBeforeCreate();
		super.onCreate(savedInstanceState);
		initView();
		initUI();
		initData();
	}

	protected void doBeforeCreate() {

	}

	public boolean isTitleBarTextDark(){
		return false;
	}

	@Override
	public int getBaseContentLayoutId() {
		return R.layout.activity_base;
	}

	protected void initView() {
		initContentLayout();
		initLoading();

		if(isTitleBarTextDark()){
			immersionBar = ImmersionBar.with(this);
			immersionBar.fitsSystemWindows(false).statusBarDarkFont(true).init();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		GlideCacheUtil.getInstance(mActivity).clearCacheMemory();
		if (immersionBar != null) {
			immersionBar.destroy(); //必须调用该方法，防止内存泄漏
		}
	}

	@Override
	public LinearLayout getBaseContentLayout() {
		return contentLayout;
	}

	private void initContentLayout() {
		contentLayout = findViewById(R.id.base_content_layout);
		View contentView;
		if (getLayoutId() == 0){
			contentView = getContentView();
		}else{
			contentView = LayoutInflater.from(this).inflate(getLayoutId(), null);
		}
		if(contentView==null){
			throw new NullPointerException(this+" getLayoutId() should not be 0 or getContentView() should not be null");
		}
		LinearLayout.LayoutParams lpContent = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		lpContent.weight = 1;
		contentView.setLayoutParams(lpContent);
		contentLayout.addView(contentView);
	}

	private void initLoading() {
		mLoadingLine = findViewById(R.id.base_loading_line);
		mLoadingFail = findViewById(R.id.base_loading_fail);
		mTagLayout = (LinearLayout)findViewById(R.id.base_tag_layout);

		if(getLoadingLayoutId()!=0){
			mLoadingLine.addView(getLayoutInflater().inflate(getLoadingLayoutId(),mLoadingLine,false), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		}
		if(getLoadingFailLayoutId()!=0){
			mLoadingFail.addView(getLayoutInflater().inflate(getLoadingFailLayoutId(),mLoadingFail,false), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		}

		if(getTagLayoutId()!=0){
			mTagLayout.addView(getLayoutInflater().inflate(getTagLayoutId(),mTagLayout,false), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		}
		mLoadingLine.setVisibility(View.GONE);
		mLoadingFail.setVisibility(View.GONE);
		mTagLayout.setVisibility(View.GONE);

		mLoadingLine.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("mLoadingLine-click");
			}
		});
		mTagLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("mTagLayout-click");
			}
		});
		findViewById(R.id.base_hide_pan).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				UIHelper.hidePan(mActivity);
			}
		});
		mLoadingFail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initData();
			}
		});
	}

	public int getLoadingLayoutId(){
		return R.layout.jj_loading_layout;
	}

	public int getLoadingFailLayoutId(){
		return R.layout.jj_loading_fail_layout;
	}

	public int getTagLayoutId(){
		return 0;
	}

	public void showTabLayout(boolean isShow){
		mLoadingLine.setVisibility(View.GONE);
		mLoadingFail.setVisibility(View.GONE);
		mTagLayout.setVisibility(isShow?View.VISIBLE:View.GONE);
	}

	public LinearLayout getTagLayout() {
		return mTagLayout;
	}


	public abstract void initUI();
	public abstract void initData();
	public abstract int getLayoutId();
	public View getContentView(){
		return null;
	}

	@Override
	public void setLoading(){
		mLoadingLine.setVisibility(View.VISIBLE);
		mLoadingFail.setVisibility(View.GONE);
		mTagLayout.setVisibility(View.GONE);
	}

	@Override
	public void setLoadingFail(){
		mLoadingLine.setVisibility(View.GONE);
		mLoadingFail.setVisibility(View.VISIBLE);
		mTagLayout.setVisibility(View.GONE);
	}

	@Override
	public void setLoadingEnd(){
		mLoadingLine.setVisibility(View.GONE);
		mLoadingFail.setVisibility(View.GONE);
		mTagLayout.setVisibility(View.GONE);
	}

	public LinearLayout getLoadingLine() {
		return mLoadingLine;
	}
}
