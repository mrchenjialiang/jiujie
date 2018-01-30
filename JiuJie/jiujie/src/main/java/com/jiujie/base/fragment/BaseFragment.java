package com.jiujie.base.fragment;

import android.os.Bundle;
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(mView==null){
			if(isTitleContentOverLap()){
				mView = inflater.inflate(R.layout.fragment_base_overlap, null);
			}else{
				mView = inflater.inflate(R.layout.fragment_base, null);
			}
			mActivity = getActivity();
			initView();
			initUI();
			initData();
		}
		return mView;
	}

	/**
	 * 基础布局，标题栏和内容是否重叠--标题栏在内容之上
	 */
	public boolean isTitleContentOverLap(){
		return false;
	}

	protected abstract void initUI();

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if(mView!=null&&mView.getParent()!=null){
			((ViewGroup)mView.getParent()).removeView(mView);
		}
	}

	//当标题栏纯色时，重写返回true比较合适...返回true时，标题栏UI需android:fitsSystemWindows="true"
//	public boolean isTitleFitsSystemWindows(){
//		return false;
//	}

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

	public abstract int getLayoutId();

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
		mBaseTitleLayout = (LinearLayout) mView.findViewById(R.id.base_title_title_layout);
		mBaseContentLayout = (LinearLayout) mView.findViewById(R.id.base_title_content_layout);
		
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
	
	public abstract void initData();

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
