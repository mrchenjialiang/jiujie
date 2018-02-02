package com.jiujie.jiujie;

import com.jiujie.base.fragment.BaseFragment;

/**
 * Created by ChenJiaLiang on 2018/1/11.
 * Email:576507648@qq.com
 */

public class SimpleFragment extends BaseFragment{

    @Override
    protected void initUI() {
//        mTitle.setTitleText("哇哈哈");
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_simple;
    }

    @Override
    public int getCustomTitleLayoutId() {
        return R.layout.title_frag;
    }

    @Override
    public void initData() {

    }
}
