package com.jiujie.base.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiujie.base.BaseRecyclerViewFooter;
import com.jiujie.base.R;

import java.util.List;

/**
 * had footer
 * Created by ChenJiaLiang on 2016/4/14.
 */
public abstract class BaseRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = -1;
    private static final int TYPE_HEADER = -2;
    private BaseRecyclerViewFooter footer;
    private View header;
    private FooterViewHolder footerViewHolder;
    private HeaderViewHolder headerViewHolder;

    private final int Footer_Status_Loading = 0;
    private final int Footer_Status_Load_End = 1;
    private final int Footer_Status_Hide = 2;
    private final int Footer_Status_Show = 3;
    private final int Footer_Status_Prepare = 4;
    private int footerStatus;

    @Override
    public final int getItemCount() {
        if(header!=null)
            return getCount() + 2;
        else
            return getCount() + 1;
    }

    public abstract int getCount();

    public final RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(getItemLayoutId(viewType), null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        return getItemViewHolder(view,viewType);
    }

    public abstract RecyclerView.ViewHolder getItemViewHolder(View view, int viewType);

    public abstract void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        int itemViewType = getItemViewType(position);
        if(itemViewType !=TYPE_HEADER&&itemViewType!=TYPE_FOOTER){
            if(header!=null)
                position--;
            onBindItemViewHolder(holder, position);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int itemViewType = getItemViewType(position);
                    return itemViewType<0 ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        int layoutPosition = holder.getLayoutPosition();
        int itemViewType = getItemViewType(layoutPosition);
        if(itemViewType<0 && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    public abstract int getItemLayoutId(int viewType);

    public int getItemType(int position){
        return TYPE_ITEM;
    }

    @Override
    public final int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if(position==0&&header!=null){
            return TYPE_HEADER;
        }else if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            if(header!=null)position--;
            return getItemType(position);
        }
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       if (viewType == TYPE_FOOTER) {
           if(footerViewHolder==null){
               View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.list_footer, parent,false);
               view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
               footerViewHolder = new FooterViewHolder(view);
               switch (footerStatus){
                   case Footer_Status_Loading:
                       footer.setReadMore();
                       break;
                   case Footer_Status_Load_End:
                       footer.setReadEnd();
                       break;
                   case Footer_Status_Hide:
                       footer.hide();
                       break;
                   case Footer_Status_Show:
                       footer.show();
                       break;
               }
           }
           return footerViewHolder;
        }else if(viewType == TYPE_HEADER){
           if(headerViewHolder==null){
               header.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
               headerViewHolder = new HeaderViewHolder(header);
           }
           return headerViewHolder;
       }else{
           return onCreateItemViewHolder(parent,viewType);
       }
    }

    public final BaseRecyclerViewFooter getFooter(){
        return footer;
    }

    public final void setReadEnd(){
        footerStatus = Footer_Status_Load_End;
        if(footer!=null){
            footer.setReadEnd();
        }
    }
    public final void setPrepare(){
        footerStatus = Footer_Status_Prepare;
        if(footer!=null){
            footer.setPrepare();
        }
    }
    public final void setReadMore(){
        footerStatus = Footer_Status_Loading;
        if(footer!=null){
            footer.setReadMore();
        }
    }

    public void addHeaderView(View header){
        this.header = header;
    }

    public void hideFooter() {
        footerStatus = Footer_Status_Hide;
        if(footer!=null)
            footer.hide();
    }

    public void showFooter() {
        footerStatus = Footer_Status_Show;
        if(footer!=null)
            footer.show();
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        FooterViewHolder(View view) {
            super(view);
            footer = new BaseRecyclerViewFooter(view);
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        HeaderViewHolder(View view) {
            super(view);
        }
    }
}