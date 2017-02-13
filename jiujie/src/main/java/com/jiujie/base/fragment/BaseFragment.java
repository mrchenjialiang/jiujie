package com.jiujie.base.fragment;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jiujie.base.R;
import com.jiujie.base.Title;
import com.jiujie.base.util.PermissionsManager;
import com.jiujie.base.util.UIHelper;

/**
 * 有标题
 */
public abstract class BaseFragment extends Fragment {

	public Title mTitle;
	public Activity mActivity;
	public View mView;
	private View mLoadingLine,mLoadingFail;
	private AnimationDrawable mLoadingAnimation;
	private LinearLayout contentLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_base, null);
		mActivity = getActivity();
		initView();
		initBaseTitle();
		return mView;
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}

	private void initBaseTitle() {
		mTitle = new Title(mActivity,mView);
	}

	public abstract int getLayoutId();
	
	@SuppressWarnings("deprecation")
	private void initView() {

		contentLayout = (LinearLayout) mView.findViewById(R.id.base_content);
		if (getLayoutId() == 0) {
			return;
		}
		View contentView = LayoutInflater.from(mActivity).inflate(getLayoutId(),
				null);
		LinearLayout.LayoutParams lpContent = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		contentView.setLayoutParams(lpContent);
		contentLayout.addView(contentView);
		initLoading();
	}

	public void setContentLayoutBackGroundColor(int color){
		if(contentLayout!=null)
			contentLayout.setBackgroundColor(color);
	}

	private void initLoading() {
		mLoadingLine = mView.findViewById(R.id.base_loading_line);
		mLoadingFail = mView.findViewById(R.id.base_loading_fail);
		mLoadingLine.setVisibility(View.GONE);
		mLoadingFail.setVisibility(View.GONE);
		mLoadingLine.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("mLoadingLine-click");
			}
		});
		mView.findViewById(R.id.base_hide_pan).setOnClickListener(new OnClickListener() {
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
			ImageView image = (ImageView) mView.findViewById(R.id.base_loading_image);
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

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		PermissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}
}
