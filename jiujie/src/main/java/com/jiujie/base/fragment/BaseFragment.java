package com.jiujie.base.fragment;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jiujie.base.R;
import com.jiujie.base.Title;
import com.jiujie.base.util.UIHelper;

public abstract class BaseFragment extends BaseMostFragment{

	public Title mTitle;
	public Activity mActivity;
	public View mView;
	private LinearLayout mLoadingLine,mLoadingFail;
	private AnimationDrawable mLoadingAnimation;
	protected boolean isShouldReLoad;
	private LinearLayout mBaseTitleTitleLayout;
	private LinearLayout mBaseTitleContentLayout;
	private LinearLayout mTagLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(mView==null){
			mView = inflater.inflate(R.layout.fragment_base, null);
			mActivity = getActivity();
			initView();
			isShouldReLoad = true;
		}else{
			isShouldReLoad = false;
		}
		return mView;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		((ViewGroup)mView.getParent()).removeView(mView);
	}

	private void initBaseTitle() {
		if(isShowTitle()){
			int customTitleLayoutId = getCustomTitleLayoutId();
			if(customTitleLayoutId==0){
				customTitleLayoutId = R.layout.base_title;
			}
			View customTitle = LayoutInflater.from(mActivity).inflate(customTitleLayoutId, mBaseTitleTitleLayout, false);
			mBaseTitleTitleLayout.addView(customTitle, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
			if(customTitleLayoutId==R.layout.base_title){
				mTitle = new Title(mActivity,customTitle);
			}
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
				int titleHeight = getResources().getDimensionPixelOffset(R.dimen.height_of_title);
				int statusBarHeight = UIHelper.getStatusBarHeightByReadR(mActivity);
				setViewHeight(customTitle, titleHeight+statusBarHeight);
//					customTitle.setPadding(0,statusBarHeight,0,0);
			}
		}else{
			mBaseTitleTitleLayout.setVisibility(View.GONE);
		}
	}
	
	public boolean isShowTitle(){
		return true;
	}

	public int getCustomTitleLayoutId() {
		return 0;
	}

	protected void setViewHeight(View view, int height) {
		ViewGroup.LayoutParams lp = view.getLayoutParams();
		if(lp!=null){
			lp.height = height;
			view.setLayoutParams(lp);
		}
	}

	public abstract int getLayoutId();
	
	@SuppressWarnings("deprecation")
	private void initView() {

		mBaseTitleTitleLayout = (LinearLayout) mView.findViewById(R.id.base_title_title_layout);
		mBaseTitleContentLayout = (LinearLayout) mView.findViewById(R.id.base_title_content_layout);
		
		initBaseTitle();
		initBaseContent();
		
		initLoading();
	}

	private void initBaseContent() {
		if (getLayoutId() == 0) {
			return;
		}
		View contentView = LayoutInflater.from(mActivity).inflate(getLayoutId(),null);
		LinearLayout.LayoutParams lpContent = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		contentView.setLayoutParams(lpContent);
		mBaseTitleContentLayout.addView(contentView);
	}

	private void initLoading() {
		mLoadingLine = (LinearLayout) mView.findViewById(R.id.base_loading_line);
		mLoadingFail = (LinearLayout) mView.findViewById(R.id.base_loading_fail);
		mTagLayout = (LinearLayout)mView.findViewById(R.id.base_tag_layout);

		if(getLoadingLayoutId()!=0){
			mLoadingLine.addView(LayoutInflater.from(mActivity).inflate(getLoadingLayoutId(),mLoadingLine,false), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		}
		if(getLoadingFailLayoutId()!=0){
			mLoadingFail.addView(LayoutInflater.from(mActivity).inflate(getLoadingFailLayoutId(),mLoadingFail,false), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		}

		if(getTagLayoutId()!=0){
			mTagLayout.addView(LayoutInflater.from(mActivity).inflate(getTagLayoutId(), mTagLayout,false), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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
		mView.findViewById(R.id.base_hide_pan).setOnClickListener(new View.OnClickListener() {
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

	@Override
	public void setLoading(){
		if(getLoadingLayoutId()==R.layout.jj_loading_layout){
			if(mLoadingAnimation==null){
				ImageView image = (ImageView) mView.findViewById(R.id.base_loading_image);
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

}
