package com.jiujie.base.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jiujie.base.R;
import com.jiujie.base.util.UIHelper;

/**
 * 基础Activity类
 */
public abstract class BaseActivity extends BaseTitleActivity {

	private View mLoadingLine,mLoadingFail;
	private AnimationDrawable mLoadingAnimation;
	public LinearLayout contentLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		initView();
	}

	private void initView() {
		contentLayout = (LinearLayout) findViewById(R.id.base_content);
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

		initLoading();
	}

	protected void setContentLayoutBackGroundColor(int color){
		if(contentLayout!=null){
			contentLayout.setBackgroundColor(color);
		}
	}

	private void initLoading() {
		mLoadingLine = findViewById(R.id.base_loading_line);
		mLoadingFail = findViewById(R.id.base_loading_fail);
		mLoadingLine.setVisibility(View.GONE);
		mLoadingFail.setVisibility(View.GONE);
		mLoadingLine.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("mLoadingLine-click");
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

	public abstract void initData();
	public abstract int getLayoutId();
	public View getContentView(){
		return null;
	}

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
