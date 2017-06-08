package com.jiujie.base.fragment;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jiujie.base.R;


public abstract class BaseScrollKeepHeaderFragment extends BaseFragment implements AppBarLayout.OnOffsetChangedListener {
    private LinearLayout mMissLine,mKeepTopLine,mScrollLine;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = super.onCreateView(inflater, container, savedInstanceState);
        if(isShouldReLoad){
            if(mView==null)return null;
            AppBarLayout appBarLayout = (AppBarLayout) mView.findViewById(R.id.base_appBar);
            appBarLayout.addOnOffsetChangedListener(this);

            mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.base_swipeRefreshLayout);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    BaseScrollKeepHeaderFragment.this.onRefresh();
                }
            });


            mMissLine = (LinearLayout) mView.findViewById(R.id.base_dissmiss_line);
            mKeepTopLine = (LinearLayout) mView.findViewById(R.id.base_keep_top_line);
            mScrollLine = (LinearLayout) mView.findViewById(R.id.base_scroll_line);

            if(getDisMissHeaderLayoutId()>0) mMissLine.addView(inflater.inflate(getDisMissHeaderLayoutId(),null), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            else mMissLine.setVisibility(View.GONE);

            if(getKeepTopHeaderLayoutId()>0)mKeepTopLine.addView(inflater.inflate(getKeepTopHeaderLayoutId(),null), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            else mKeepTopLine.setVisibility(View.GONE);

            if(getScrollLayoutId()>0)mScrollLine.addView(inflater.inflate(getScrollLayoutId(),null), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            else mScrollLine.setVisibility(View.GONE);
        }
        return mView;
    }

    protected void setRefreshEnable(boolean isEnable){
        if(mSwipeRefreshLayout!=null)mSwipeRefreshLayout.setEnabled(isEnable);
    }

    protected abstract void onRefresh();

    protected void setRefreshing(boolean isRefreshing){
        if(mSwipeRefreshLayout!=null)mSwipeRefreshLayout.setRefreshing(isRefreshing);
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

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        setRefreshEnable(verticalOffset==0);
    }
}
