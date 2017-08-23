package com.jiujie.base.adapter;

import android.animation.Animator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import java.util.List;

/**
 * had footer
 * Created by ChenJiaLiang on 2016/4/14.
 */
public abstract class BaseRecyclerViewSimpleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = -10;
    private static final int TYPE_HEADER = -11;
    private View footer;
    private View header;
    private int oldItemCount;
    private int mDuration = 300;
    private Interpolator mInterpolator = new LinearInterpolator();
    private int mLastPosition = -1;
    private FooterViewHolder footerViewHolder;
    private HeaderViewHolder headerViewHolder;

    public void notifyDataSetChanged1(){
        int itemCount = getItemCount();
        if(oldItemCount==0||oldItemCount >= itemCount){
            notifyDataSetChanged();
        }else{
            int startPosition = oldItemCount -1;
            for (int position = startPosition;position<itemCount;position++){
                notifyItemInserted(position++);
            }

//            notifyItemRangeChanged(startPosition, itemCount);
        }
        oldItemCount = itemCount;
    }

    @Override
    public final int getItemCount() {
        int count = getCount();
        if(header!=null){
            count++;
        }
        if(footer!=null){
            count++;
        }
        return count;
    }

    public boolean isFullLine(int position){
        return getItemViewType(position)<0;
    }

    public View getHeader() {
        return header;
    }

    public abstract int getCount();

    private RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType){
        int itemLayoutId = getItemLayoutId(viewType);
        if(itemLayoutId==0){
            throw new RuntimeException("getItemLayoutId should not return 0 in " + this.getClass().getName());
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayoutId, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        RecyclerView.ViewHolder itemViewHolder = getItemViewHolder(view, viewType);
        if(itemViewHolder==null){
            throw new RuntimeException("getItemViewHolder should not return null in " + this.getClass().getName());
        }
        return itemViewHolder;
    }

    public abstract RecyclerView.ViewHolder getItemViewHolder(View view, int viewType);

    public abstract void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public final void onBindViewHolder(final RecyclerView.ViewHolder holder, int position){
        final int itemViewType = getItemViewType(position);
        if(itemViewType !=TYPE_HEADER&&itemViewType!=TYPE_FOOTER){
            int p = position;
            if(header!=null){
                p--;
            }
            onBindItemViewHolder(holder, p);
        }

        int adapterPosition = holder.getAdapterPosition();
        if (adapterPosition > mLastPosition) {
            Animator[] itemViewAnim = getItemViewAnim(holder.itemView);
            if(itemViewAnim!=null){
                for (Animator anim : itemViewAnim) {
                    anim.setDuration(mDuration).start();
                    anim.setInterpolator(mInterpolator);
                }
            }
            mLastPosition = adapterPosition;
        }
    }

    public int getLastPosition(){
        return mLastPosition;
    }

    protected Animator[] getItemViewAnim(View view){
        return null;
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

    protected int getItemType(int position){
        return TYPE_ITEM;
    }

    @Override
    public final int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if(position==0&&header!=null){
            return TYPE_HEADER;
        }else if (footer!=null && position + 1 == getItemCount()) {
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
               footer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
               footerViewHolder = new FooterViewHolder(footer);
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

    public void addHeader(View header){
        this.header = header;
    }

    public void addFooter(View footer){
        this.footer = footer;
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        FooterViewHolder(View view) {
            super(view);
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        HeaderViewHolder(View view) {
            super(view);
        }
    }
}