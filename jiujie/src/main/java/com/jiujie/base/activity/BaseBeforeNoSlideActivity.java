package com.jiujie.base.activity;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jiujie.base.R;
import com.jiujie.base.Title;
import com.jiujie.base.util.UIHelper;

public abstract class BaseBeforeNoSlideActivity extends BaseToolBarNoSlideActivity{

	public Title mTitle;
	public Activity mActivity;
	private View mLoadingLine,mLoadingFail;
	private AnimationDrawable mLoadingAnimation;
	public LinearLayout contentLayout;
	private View customTitleView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_base);
		mActivity = this;
		initView();
		initBaseTitle();


		int titleHeight = getResources().getDimensionPixelOffset(R.dimen.height_of_title);
//		getDimension 获取绝对尺寸（"像素"单位，只是float）,
//      getDimensionPixelSize是将getDimension获取的小数部分四舍五入，
//      getDimensionPixelOffset是强制转换成int，即舍去小数部分

		//需设置ToolBar高度，否则系统将默认为actionBar高度，如有差值，效果不佳
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
			//低版本有默认ToolBar高度
			setToolBarHeight(UIHelper.getStatusBarHeightByReadR(mActivity)+ titleHeight);
		}else{
			setToolBarHeight(titleHeight);
		}
	}

	/**
	 * 如果重写了，返回不是0，则不能使用mTitle
	 * 如果另外定义了标题，需注意标题栏颜色，如改，则需setToolBarBackGround
	 */
	public int getCustomTitleLayoutId(){
		return 0;
	}

	public View getCustomTitleView() {
		return customTitleView;
	}

	private void initBaseTitle() {
		ActionBar actionBar = getSupportActionBar();
		if(actionBar==null)return;
		// 返回箭头（默认不显示）
		actionBar.setDisplayHomeAsUpEnabled(false);
		// 左侧图标点击事件使能
		actionBar.setHomeButtonEnabled(false);
		// 使左上角图标(系统)是否显示
		actionBar.setDisplayShowHomeEnabled(false);
		// 显示标题
		actionBar.setDisplayShowTitleEnabled(false);
		// 显示自定义视图
		actionBar.setDisplayShowCustomEnabled(true);

		if(getCustomTitleLayoutId()==0){
			actionBar.setCustomView(R.layout.base_title); //设置自定义布局界面
			customTitleView = actionBar.getCustomView();  //获取自定义View进行设置
			mTitle = new Title(this,customTitleView);
		}else{
			actionBar.setCustomView(getCustomTitleLayoutId()); //设置自定义布局界面
			customTitleView = actionBar.getCustomView();  //获取自定义View进行设置
		}




//		android:fitsSystemWindows="true" ToolBar属性设置了就不用下面设置padding了，设置了padding会有其他问题

//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
//			int statusBarHeight = UIHelper.getStatusBarHeightByReadR(mActivity);
//			actionBarView.setPadding(0,statusBarHeight,0,0);
//		}
	}

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
			contentView = LayoutInflater.from(this).inflate(getLayoutId(), null);
		}
		LinearLayout.LayoutParams lpContent = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
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
	public abstract int getLayoutId();
	public abstract View getContentView();

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
