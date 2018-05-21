package com.jiujie.base.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jiujie.base.APP;
import com.jiujie.base.R;
import com.jiujie.base.Title;
import com.jiujie.base.jk.OnTitleClickMoveToTopListen;
import com.jiujie.base.util.UIHelper;
import com.jiujie.glide.GlideCacheUtil;

/**
 * 执行顺序 setUserVisibleHint false
 *          setUserVisibleHint true
 *          onCreate
 *          onCreateView
 *          	initView
 *          		getAdapter if list
 *          	initUI
 *          	initData
 */
public abstract class BaseFragment extends BaseMostFragment {

	public Title mTitle;
	public FragmentActivity mActivity;
	public View mView;
	private LinearLayout mLoadingLine,mLoadingFail;
	private LinearLayout mBaseTitleLayout;
	private LinearLayout mBaseContentLayout;
	private LinearLayout mTagLayout;
	private final int Status_Loaded = 0;
	private final int Status_Loading = 1;
	private final int Status_Load_Fail = 2;
	private final int Status_Tag = 3;
	private int Status = Status_Loaded;
	public boolean isVisibleToUser;
	public boolean isDoInitData;

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(!this.isVisibleToUser&&isVisibleToUser){
			//由不可见→可见
			if(mView!=null&&!isDoInitData){
				//已经执行过 onCreateView，但还没执行 initUIAndData
				doInitData();
			}
		}
		this.isVisibleToUser = isVisibleToUser;
	}

	/**
	 * 是否页面对用户可见时才执行 initUIAndData
	 * 特别注意，setUserVisibleHint 貌似仅在 类似viewpager pagerAdapter中的Fragment才会调用.....
	 */
	public boolean isInitDataAfterVisibleToUser(){
		return false;
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		if(mView==null){
			if(isTitleContentOverLap()){
				mView = inflater.inflate(R.layout.fragment_base_overlap, container,false);
			}else{
				mView = inflater.inflate(R.layout.fragment_base, container,false);
			}
			mActivity = getActivity();
			initView();
			initUI();
			if(isInitDataAfterVisibleToUser()){
				if(isVisibleToUser)doInitData();
			}else{
				doInitData();
			}
		}
		return mView;
	}

	public void doInitData() {
		isDoInitData = true;
		initData();
	}

	public abstract void initUI();
	public abstract void initData();
	public abstract int getLayoutId();

	/**
	 * 基础布局，标题栏和内容是否重叠--标题栏在内容之上
	 */
	public boolean isTitleContentOverLap(){
		return false;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		GlideCacheUtil.getInstance(mActivity).clearCacheMemory();
		if(mView!=null&&mView.getParent()!=null){
			((ViewGroup)mView.getParent()).removeView(mView);
			isDoInitData = false;
		}
	}

	private void initBaseTitle() {
		if(isShowTitle()){
			int customTitleLayoutId = getCustomTitleLayoutId();
			if(customTitleLayoutId==0){
				customTitleLayoutId = R.layout.base_title;
			}
			View customTitle = LayoutInflater.from(mActivity).inflate(customTitleLayoutId, mBaseTitleLayout, false);
			mBaseTitleLayout.addView(customTitle, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
			if(customTitleLayoutId== R.layout.base_title){
				mTitle = new Title(mActivity,customTitle);
			}

			if (APP.isTitleContainStatusBar()){
//				if (!isTitleFitsSystemWindows()&&APP.isTitleContainStatusBar()){
				setViewHeight(customTitle, APP.getTitleHeight()+APP.getStatusBarHeight());
			}
			if(this instanceof OnTitleClickMoveToTopListen){
				final OnTitleClickMoveToTopListen onTitleClickMoveToTopListen = (OnTitleClickMoveToTopListen) this;
				mBaseTitleLayout.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						onTitleClickMoveToTopListen.moveToTop();
					}
				});
			}
		}else{
			mBaseTitleLayout.setVisibility(View.GONE);
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

	public LinearLayout getBaseContentLayout() {
		return mBaseContentLayout;
	}

	public LinearLayout getBaseTitleLayout() {
		return mBaseTitleLayout;
	}

	public LinearLayout getLoadingLine() {
		return mLoadingLine;
	}

	public LinearLayout getTagLayout() {
		return mTagLayout;
	}

	protected void initView() {
		mBaseTitleLayout = mView.findViewById(R.id.base_title_title_layout);
		mBaseContentLayout = mView.findViewById(R.id.base_title_content_layout);
		
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
		mBaseContentLayout.addView(contentView);
	}

	private void initLoading() {
		mLoadingLine = mView.findViewById(R.id.base_loading_line);
		mLoadingFail = mView.findViewById(R.id.base_loading_fail);
		mTagLayout = mView.findViewById(R.id.base_tag_layout);

		if(getLoadingLayoutId()!=0){
			mLoadingLine.addView(LayoutInflater.from(mActivity).inflate(getLoadingLayoutId(),mLoadingLine,false), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		}
		if(getLoadingFailLayoutId()!=0){
			mLoadingFail.addView(LayoutInflater.from(mActivity).inflate(getLoadingFailLayoutId(),mLoadingFail,false), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		}

		if(getTagLayoutId()!=0){
			mTagLayout.addView(LayoutInflater.from(mActivity).inflate(getTagLayoutId(), mTagLayout,false), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		}
		mLoadingLine.setVisibility(Status==Status_Loading?View.VISIBLE:View.GONE);
		mLoadingFail.setVisibility(Status==Status_Load_Fail?View.VISIBLE:View.GONE);
		mTagLayout.setVisibility(Status==Status_Tag?View.VISIBLE:View.GONE);

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
		if(isShow){
			Status = Status_Tag;
		}
		mLoadingLine.setVisibility(View.GONE);
		mLoadingFail.setVisibility(View.GONE);
		mTagLayout.setVisibility(isShow?View.VISIBLE:View.GONE);
	}

	@Override
	public void setLoading(){
		Status = Status_Loading;
		mLoadingLine.setVisibility(View.VISIBLE);
		mLoadingFail.setVisibility(View.GONE);
		mTagLayout.setVisibility(View.GONE);
	}

	@Override
	public void setLoadingFail(){
		Status = Status_Load_Fail;
		mLoadingLine.setVisibility(View.GONE);
		mLoadingFail.setVisibility(View.VISIBLE);
		mTagLayout.setVisibility(View.GONE);
	}

	@Override
	public void setLoadingEnd(){
		Status = Status_Loaded;
		mLoadingLine.setVisibility(View.GONE);
		mLoadingFail.setVisibility(View.GONE);
		mTagLayout.setVisibility(View.GONE);
	}

}
