package com.jiujie.jiujie.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jiujie.base.adapter.BaseRecyclerViewAdapter;
import com.jiujie.base.util.UIHelper;
import com.jiujie.jiujie.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ChenJiaLiang on 2018/1/11.
 * Email:576507648@qq.com
 */

public class SimpleListAdapter extends BaseRecyclerViewAdapter{

    private final List<String> dataList;

    public SimpleListAdapter(List<String> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList==null?0:dataList.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view, int viewType) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        UIHelper.showLog("onBindItemViewHolder:"+position);
        if(holder instanceof ItemViewHolder){
            ItemViewHolder h = (ItemViewHolder) holder;
            h.text.setText(dataList.get(position));
        }
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_simple_list;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.iml_text)
        TextView text;
        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
