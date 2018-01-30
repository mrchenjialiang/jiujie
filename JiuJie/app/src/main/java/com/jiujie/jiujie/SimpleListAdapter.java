package com.jiujie.jiujie;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jiujie.base.adapter.BaseRecyclerViewAdapter;

/**
 * Created by ChenJiaLiang on 2018/1/11.
 * Email:576507648@qq.com
 */

public class SimpleListAdapter extends BaseRecyclerViewAdapter{
    @Override
    public int getCount() {
        return 20;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view, int viewType) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_simple_list;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        public ItemViewHolder(View itemView) {
            super(itemView);
        }
    }
}
