package com.jiujie.base.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiujie.base.R;
import com.jiujie.base.jk.OnItemClickListen;
import com.jiujie.base.model.Image;
import com.jiujie.base.pop.BasePop;
import com.jiujie.base.util.glide.GlideUtil;

import java.util.List;

/**
 * Created by ChenJiaLiang on 2017/8/10.
 * Email:576507648@qq.com
 */

public class ChoosePhotoDirAdapter extends BaseRecyclerViewSimpleAdapter{

    private final List<Image> dataList;
    private final BasePop basePop;
    private int checkPosition;
    private OnItemClickListen onItemClickListen;

    public ChoosePhotoDirAdapter(BasePop basePop, List<Image> dataList) {
        this.basePop = basePop;
        this.dataList = dataList;
    }

    public void setOnItemClickListen(OnItemClickListen onItemClickListen) {
        this.onItemClickListen = onItemClickListen;
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
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ItemViewHolder){
            final ItemViewHolder h = (ItemViewHolder) holder;
            final Image d = dataList.get(position);
            GlideUtil.instance().setDefaultImage(basePop.getActivity(),d.getImageList().get(0).getPath(),h.image,R.drawable.trans);
            h.name.setText(d.getName());
            h.path.setText(d.getPath());
            h.count.setText(d.getImageList()==null?"0":(d.getImageList().size()+""));
            h.check.setVisibility(checkPosition==position?View.VISIBLE:View.INVISIBLE);
            h.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(checkPosition==position){
                        return;
                    }
                    if(onItemClickListen!=null){
                        onItemClickListen.click(position);
                    }
                    checkPosition = position;
                    notifyDataSetChanged();
                    basePop.dismiss();
                }
            });
        }
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_choose_photo_dir;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        View check;
        TextView name,path,count;
        public ItemViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.icpd_image);
            name = (TextView) itemView.findViewById(R.id.icpd_name);
            path = (TextView) itemView.findViewById(R.id.icpd_path);
            count = (TextView) itemView.findViewById(R.id.icpd_count);
            check = itemView.findViewById(R.id.icpd_check);
        }
    }
}
