package com.jiujie.jiujie;

import android.text.TextUtils;

import com.jiujie.base.adapter.BaseRecyclerViewAdapter;
import com.jiujie.base.fragment.BaseListSimpleFragment;
import com.jiujie.base.util.recycler.RecyclerViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenJiaLiang on 2018/1/11.
 * Email:576507648@qq.com
 */

public class SimpleListFragment extends BaseListSimpleFragment<String,String> {

    @Override
    public void initUI() {
//        mTitle.setLeftButtonBack();
//        mTitle.setTitleText("哇哈哈");
    }

    @Override
    public BaseRecyclerViewAdapter getAdapter() {
        return new SimpleListAdapter(dataList);
    }

    @Override
    public void initData() {
        for (int i = 0;i<100;i++){
            dataList.add(""+i);
        }
        notifyDataSetChanged();
    }

    @Override
    public void loadMore() {

    }

    @Override
    public boolean isShowTitle() {
        return false;
    }

    @Override
    protected boolean isEndFromSize() {
        return false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.recyclerview;
    }

    @Override
    public RecyclerViewUtil getRecyclerViewUtil() {
        return new RecyclerViewUtil(mActivity,mView,0, getRecyclerViewId(),getAdapter(),getRecycleViewType(),getRecycleViewGridNum());
    }

    protected int getRecyclerViewId() {
        return R.id.recyclerView;
    }

    @Override
    protected List<String> analysisData(String result) {
        if(!TextUtils.isEmpty(result)){
            List<String> list = new ArrayList<>();
            for (int  i =0;i<20;i++){
                list.add("i:"+(dataList.size()+i));
            }
            return list;
        }
        return null;
    }
}
