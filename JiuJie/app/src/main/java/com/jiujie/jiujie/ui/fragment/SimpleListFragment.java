package com.jiujie.jiujie.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.jiujie.base.adapter.BaseRecyclerViewAdapter;
import com.jiujie.base.fragment.BaseListSimpleFragment;
import com.jiujie.base.util.UIHelper;
import com.jiujie.base.util.recycler.RecyclerViewUtil;
import com.jiujie.jiujie.R;
import com.jiujie.jiujie.ui.adapter.SimpleListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenJiaLiang on 2018/1/11.
 * Email:576507648@qq.com
 */

public class SimpleListFragment extends BaseListSimpleFragment<String,String> {

    private int type;

    @Override
    public void initUI() {
        UIHelper.showLog(this,type+" initUI");
//        mTitle.setLeftButtonBack();
//        mTitle.setTitleText("哇哈哈");
    }

    @Override
    public boolean isInitDataAfterVisibleToUser() {
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIHelper.showLog(this,type+" onCreate");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        UIHelper.showLog(this,type+" onHiddenChanged hidden:"+hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        UIHelper.showLog(this,type+" setUserVisibleHint isVisibleToUser:"+isVisibleToUser);
    }

    @Override
    public BaseRecyclerViewAdapter getAdapter() {
        UIHelper.showLog(this,type+" getAdapter");
        return new SimpleListAdapter(dataList);
    }

    @Override
    public void initData() {
        UIHelper.showLog(this,type+" initData");
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
    public boolean isEndFromSize() {
        return false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.recyclerview;
    }

    @Override
    public RecyclerViewUtil getRecyclerViewUtil() {
        return new RecyclerViewUtil(this,mActivity,mView,0, getRecyclerViewId(),getAdapter(),getRecycleViewType(),getRecycleViewGridNum());
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

    public void setType(int type) {
        this.type = type;
    }
}
