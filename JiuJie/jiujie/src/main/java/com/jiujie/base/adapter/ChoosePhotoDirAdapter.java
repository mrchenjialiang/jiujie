package com.jiujie.base.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiujie.base.R;
import com.jiujie.base.jk.OnItemClickListen;
import com.jiujie.base.model.Image;
import com.jiujie.base.pop.BasePop;
import com.jiujie.glide.GlideUtil;

import java.util.List;

/**
 * Created by ChenJiaLiang on 2017/8/10.
 * Email:576507648@qq.com
 */

public class ChoosePhotoDirAdapter extends BaseRecyclerViewSimpleAdapter<Image,ChoosePhotoDirAdapter.ItemViewHolder>{

    private final BasePop basePop;
    private int checkPosition;
    private OnItemClickListen onItemClickListen;

    public ChoosePhotoDirAdapter(BasePop basePop, List<Image> dataList) {
        super(dataList);
        this.basePop = basePop;
    }

    public void setOnItemClickListen(OnItemClickListen onItemClickListen) {
        this.onItemClickListen = onItemClickListen;
    }

    @Override
    public ItemViewHolder getItemViewHolder(View view, int viewType) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(ItemViewHolder h, Image d, final int position) {
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
            image = itemView.findViewById(R.id.icpd_image);
            name = itemView.findViewById(R.id.icpd_name);
            path = itemView.findViewById(R.id.icpd_path);
            count = itemView.findViewById(R.id.icpd_count);
            check = itemView.findViewById(R.id.icpd_check);
        }
    }
}
