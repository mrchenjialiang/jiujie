package com.jiujie.base.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jiujie.base.R;
import com.jiujie.base.SearchTitle;
import com.jiujie.base.util.UIHelper;


public abstract class BaseSearchActivity extends BaseToolBarActivity{

	public SearchTitle mTitle;
	public Activity mActivity;
	private View mLoadingLine,mLoadingFail;
	private AnimationDrawable mLoadingAnimation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base_search);
		mActivity = this;
		initView();
		initBaseTitle();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
			//低版本有默认ToolBar高度
			setToolBarHeight(UIHelper.getStatusBarHeightByReadR(mActivity));
			setToolBarBackGround(Color.parseColor("#efeff4"));
		}else{
			setToolBarHeight(0);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		UIHelper.hidePan(mActivity);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	public void finish() {
		super.finish();
		UIHelper.hidePan(mActivity);
	}

	private void initBaseTitle() {
		mTitle = new SearchTitle(this);
	}

	public abstract int getLayoutId();
	
	@SuppressWarnings("deprecation")
	private void initView() {
		LinearLayout base_content = (LinearLayout) findViewById(R.id.base_content);
		if (getLayoutId() == 0) {
			return;
		}
		View contentView = LayoutInflater.from(this).inflate(getLayoutId(),
				null);
		LinearLayout.LayoutParams lpContent = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		contentView.setLayoutParams(lpContent);
		base_content.addView(contentView);
		
		initLoading();
	}

	private void initLoading() {
		mLoadingLine = findViewById(R.id.base_loading_line);
		mLoadingFail = findViewById(R.id.base_loading_fail);
		mLoadingLine.setVisibility(View.GONE);
		mLoadingFail.setVisibility(View.GONE);
		mLoadingLine.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("mLoadingLine-click");
			}
		});
		findViewById(R.id.base_hide_pan).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UIHelper.hidePan(mActivity);
			}
		});
		mLoadingFail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				initData();
			}
		});
	}
	
	public abstract void initData();
	
	public void setLoading(){
		if(mLoadingAnimation==null){
			ImageView image = (ImageView) findViewById(R.id.base_loading_image);
			image.setImageResource(R.drawable.loading);
			mLoadingAnimation = (AnimationDrawable) image.getDrawable();
		}
		mLoadingLine.setVisibility(View.VISIBLE);
		mLoadingFail.setVisibility(View.GONE);
		mLoadingAnimation.start();
	}
	
	public void setLoadingFail(){
		setLoadingEnd();
		mLoadingFail.setVisibility(View.VISIBLE);
	}
	
	public void setLoadingEnd(){
		if(mLoadingAnimation!=null&&mLoadingAnimation.isRunning()){
			mLoadingAnimation.stop();
			mLoadingLine.setVisibility(View.GONE);
		}
		mLoadingFail.setVisibility(View.GONE);
	}
}
