package com.jiujie.base.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jiujie.base.R;
import com.jiujie.base.util.UIHelper;

/**
 * 基础Activity类
 */
public abstract class BaseActivity extends BaseTitleActivity {

	private LinearLayout mLoadingLine,mLoadingFail;
	private AnimationDrawable mLoadingAnimation;
	private LinearLayout contentLayout;
	private LinearLayout mTagLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	@Override
	public int getBaseContentLayoutId() {
		return R.layout.activity_base;
	}

	private void initView() {
		initContentLayout();
		initLoading();
	}

	@Override
	public LinearLayout getBaseContentLayout() {
		return contentLayout;
	}

	private void initContentLayout() {
		contentLayout = (LinearLayout) findViewById(R.id.base_content_layout);
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
		mLoadingLine = (LinearLayout) findViewById(R.id.base_loading_line);
		mLoadingFail = (LinearLayout) findViewById(R.id.base_loading_fail);
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

	public abstract void initData();
	public abstract int getLayoutId();
	public View getContentView(){
		return null;
	}

	@Override
	public void setLoading(){
		if(getLoadingLayoutId()==R.layout.jj_loading_layout){
			if(mLoadingAnimation==null){
				ImageView image = (ImageView) findViewById(R.id.base_loading_image);
				image.setImageResource(R.drawable.loading);
				mLoadingAnimation = (AnimationDrawable) image.getDrawable();
			}
			mLoadingAnimation.start();
		}
		mLoadingLine.setVisibility(View.VISIBLE);
		mLoadingFail.setVisibility(View.GONE);
		mTagLayout.setVisibility(View.GONE);
	}

	@Override
	public void setLoadingFail(){
		if(getLoadingLayoutId()==R.layout.jj_loading_fail_layout){
			setLoadingEnd();
		}
		mLoadingLine.setVisibility(View.GONE);
		mLoadingFail.setVisibility(View.VISIBLE);
		mTagLayout.setVisibility(View.GONE);
	}

	@Override
	public void setLoadingEnd(){
		if(mLoadingAnimation!=null&&mLoadingAnimation.isRunning()){
			mLoadingAnimation.stop();
		}
		mLoadingLine.setVisibility(View.GONE);
		mLoadingFail.setVisibility(View.GONE);
		mTagLayout.setVisibility(View.GONE);
	}

	public LinearLayout getLoadingLine() {
		return mLoadingLine;
	}
}
