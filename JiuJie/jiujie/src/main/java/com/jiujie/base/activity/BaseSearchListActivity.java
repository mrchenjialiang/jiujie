package com.jiujie.base.activity;


import com.jiujie.base.R;
import com.jiujie.base.SearchTitle;


/**
 * @author ChenJiaLiang
 */
public abstract class BaseSearchListActivity extends BaseListActivity {

    public SearchTitle mTitle;

    @Override
    protected void initView() {
        super.initView();
        mTitle = new SearchTitle(this);
    }

    @Override
    public int getCustomTitleLayoutId() {
        return R.layout.jiujie_title_search;
    }

    @Override
    protected String getPageName() {
        return "搜索";
    }
}
