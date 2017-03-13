package com.jiujie.base.activity;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jiujie.base.R;
import com.jiujie.base.util.UIHelper;


//public abstract class BaseNoTitleActivity extends FragmentActivity {
public abstract class BaseNoTitleBeforeActivity extends BaseToolBarActivity {

	public Activity mActivity;
	private View mLoadingLine,mLoadingFail;
	private AnimationDrawable mLoadingAnimation;
	public int statusBarHeight;
	private LinearLayout contentLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		mActivity = this;
		initView();

		if(statusBarHeight<=0){
			statusBarHeight = UIHelper.getStatusBarHeightByReadR(mActivity);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
			//低版本有默认ToolBar高度
			setToolBarHeight(statusBarHeight);
		}else{
			setToolBarHeight(0);
		}

	}

	public abstract int getLayoutId();
	public abstract View getContentView();
	
	@SuppressWarnings("deprecation")
	private void initView() {
		contentLayout = (LinearLayout) findViewById(R.id.base_content);
		if (getLayoutId() == 0&&getContentView()==null) {
			throw new NullPointerException(this+" getLayoutId() should not be 0 or getContentView() should not be null");
		}
		View contentView;
		if (getLayoutId() == 0){
			contentView = getContentView();
		}else{
			contentView = LayoutInflater.from(this).inflate(getLayoutId(),null);
		}
		LinearLayout.LayoutParams lpContent = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		lpContent.weight = 1;
		contentView.setLayoutParams(lpContent);
		contentLayout.addView(contentView);

		initLoading();
	}

	public void setContentLayoutBackGroundColor(int color){
		if(contentLayout!=null)
			contentLayout.setBackgroundColor(color);
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
