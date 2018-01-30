package com.jiujie.base.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.jiujie.base.R;
import com.jiujie.base.activity.ImageViewPagerActivity;
import com.jiujie.base.jk.OnListCheckedChangeListener;
import com.jiujie.base.model.Image;
import com.jiujie.base.util.UIHelper;
import com.jiujie.base.util.glide.GlideUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenJiaLiang on 2017/8/10.
 * Email:576507648@qq.com
 */

public class ChoosePhotoAdapter extends BaseRecyclerViewSimpleAdapter{

    private final List<Image> dataList;
    private final Activity mActivity;
    private List<String> checkedList = new ArrayList<>();
    private OnListCheckedChangeListener<String> onListCheckedChangeListener;
    private int maxCheckCount;
    private boolean isCountLimit;

    public ChoosePhotoAdapter(Activity activity,List<Image> dataList) {
        this.mActivity = activity;
        this.dataList = dataList;
    }

    public void setCountLimit(boolean countLimit) {
        isCountLimit = countLimit;
    }

    public void setMaxCheckCount(int maxCheckCount) {
        this.maxCheckCount = maxCheckCount;
    }

    public void setCheckedList(List<String> checkedList) {
        this.checkedList.clear();
        if(checkedList!=null){
            this.checkedList.addAll(checkedList);
        }
    }

    public List<String> getCheckedList() {
        return checkedList;
    }

    public void setOnListCheckedChangeListener(OnListCheckedChangeListener<String> onListCheckedChangeListener) {
        this.onListCheckedChangeListener = onListCheckedChangeListener;
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
            GlideUtil.instance().setDefaultImage(mActivity,d.getPath(),h.image);
            h.btnCheck.setSelected(checkedList.contains(d.getPath()));
            h.trans.setVisibility(checkedList.contains(d.getPath())?View.VISIBLE:View.GONE);
            h.btnCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isChange;
                    if(checkedList.contains(d.getPath())){
                        checkedList.remove(d.getPath());
                        h.btnCheck.setSelected(false);
                        h.trans.setVisibility(View.GONE);
                        isChange = true;
                    }else{
                        if(isCountLimit&& checkedList.size()>maxCheckCount){
                            UIHelper.showToastShort(mActivity,"最多选"+maxCheckCount+"张");
                            isChange = false;
                        }else{
                            checkedList.add(d.getPath());
                            h.btnCheck.setSelected(true);
                            h.trans.setVisibility(View.VISIBLE);
                            isChange = true;
                        }
                    }
                    if(isChange&&onListCheckedChangeListener!=null){
                        onListCheckedChangeListener.onCheckedChanged(checkedList);
                    }
                }
            });
            h.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> urlList = new ArrayList<>();
                    for (Image d:dataList){
                        urlList.add(d.getPath());
                    }
                    ImageViewPagerActivity.launch(mActivity,position,urlList);
                }
            });
        }
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_choose_photo;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        View btnCheck;
        View trans;
        public ItemViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.icp_image);
            btnCheck = itemView.findViewById(R.id.icp_btn_check);
            trans = itemView.findViewById(R.id.icp_trans);
        }
    }
}
