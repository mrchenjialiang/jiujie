package com.jiujie.base.fragment;

import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jiujie.base.R;

/**
 * @author Created by ChenJiaLiang on 2016/4/21.
 */
public abstract class BaseScrollKeepHeaderFragment extends BaseFragment implements AppBarLayout.OnOffsetChangedListener, SwipeRefreshLayout.OnRefreshListener {
    private LinearLayout mMissLine,mKeepTopLine,mScrollLine;
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    public void setRefreshing(boolean isRefreshing){
        mSwipeRefreshLayout.setRefreshing(isRefreshing);
    }

    public void setRefreshEnable(boolean isEnable){
        mSwipeRefreshLayout.setEnabled(isEnable);
    }

    @Override
    protected void initView() {
        super.initView();
        AppBarLayout appBarLayout = (AppBarLayout) mView.findViewById(R.id.bskh_appBar);
        appBarLayout.addOnOffsetChangedListener(this);

        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.bskh_swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mActivity,R.color.title_bg));

        mMissLine = mView.findViewById(R.id.bskh_dissmiss_line);
        mKeepTopLine = mView.findViewById(R.id.bskh_keep_top_line);
        mScrollLine = mView.findViewById(R.id.bskh_scroll_line);

        if(getDisMissHeaderLayoutId()>0) mMissLine.addView(LayoutInflater.from(mActivity).inflate(getDisMissHeaderLayoutId(), null), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        else mMissLine.setVisibility(View.GONE);

        if(getKeepTopHeaderLayoutId()>0)mKeepTopLine.addView(LayoutInflater.from(mActivity).inflate(getKeepTopHeaderLayoutId(), null), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        else mKeepTopLine.setVisibility(View.GONE);

        if(getScrollLayoutId()>0)mScrollLine.addView(LayoutInflater.from(mActivity).inflate(getScrollLayoutId(), null), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        else mScrollLine.setVisibility(View.GONE);
    }

    public View getMissLine(){
        return mMissLine;
    }

    public View getKeepTopLine(){
        return mKeepTopLine;
    }

    public View getScrollLine(){
        return mScrollLine;
    }

    public abstract int getDisMissHeaderLayoutId();

    public abstract int getKeepTopHeaderLayoutId();

    public abstract int getScrollLayoutId();

    @Override
    public int getLayoutId() {
        return R.layout.activity_base_scroll_keep_header;
    }
}
