package com.jiujie.jiujie;

import com.jiujie.base.adapter.BaseRecyclerViewAdapter;
import com.jiujie.base.fragment.BaseListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenJiaLiang on 2018/1/11.
 * Email:576507648@qq.com
 */

public class SimpleListFragment extends BaseListFragment {

    private List<String> dataList = new ArrayList<>();

    @Override
    public void initUI() {
        mTitle.setLeftButtonBack();
        mTitle.setTitleText("哇哈哈");
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_simple;
    }

    @Override
    public BaseRecyclerViewAdapter getAdapter() {
        return new SimpleListAdapter(dataList);
    }

    @Override
    public void initData() {
//        for (int i = 0;i<)
    }
}
