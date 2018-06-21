package com.jiujie.base.widget.viewgroup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ChenJiaLiang on 2018/6/6.
 * Email:576507648@qq.com
 */

public abstract class BaseScrollViewGroupAdapter<H, D> {

    private List<D> dataList;
    private Map<Integer, Cache> cacheMap = new HashMap<>();

    public BaseScrollViewGroupAdapter(List<D> dataList) {
        this.dataList = dataList;
    }

    class Cache {
        H h;
        D d;
        ViewGroup viewGroup;
        int position;

        public Cache(H h, D d, ViewGroup viewGroup, int position) {
            this.h = h;
            this.d = d;
            this.viewGroup = viewGroup;
            this.position = position;
        }
    }

    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public final void prepare(ViewGroup viewGroup, int position) {
        for (int pos : cacheMap.keySet()) {
            Cache cache = cacheMap.get(pos);
            if (cache.viewGroup == viewGroup) {
                //do destroy
                destroy(cache.h, cache.d, cache.position);
                cacheMap.remove(pos);
                break;
            }
        }
        D d = dataList.get(position);
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(getItemLayoutId(position), viewGroup, false);
        viewGroup.removeAllViews();
        viewGroup.addView(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        H h = getViewHolder(itemView, position);
        prepare(h, d, position);

        cacheMap.put(position, new Cache(h, d, viewGroup, position));
    }

    public final void show(ViewGroup viewGroup, int position) {
        if (!cacheMap.containsKey(position)) {
            prepare(viewGroup, position);
        }
        Cache cache = cacheMap.get(position);
        show(cache.h, cache.d, cache.position);
    }

    public final void hide(int position) {
        if (cacheMap.containsKey(position)) {
            Cache cache = cacheMap.get(position);
            hide(cache.h, cache.d, cache.position);
        }
    }

    public final void doRelease() {
        for (int pos : cacheMap.keySet()) {
            if (cacheMap.containsKey(pos)) {
                Cache cache = cacheMap.get(pos);
                if (cache != null && cache.viewGroup != null) {
                    destroy(cache.h, cache.d, cache.position);
                }
            }
        }
        cacheMap.clear();
    }

    protected abstract int getItemLayoutId(int position);

    protected abstract H getViewHolder(View view, int position);

    protected abstract void prepare(H h, D d, int position);

    protected abstract void show(H h, D d, int position);

    protected abstract void hide(H h, D d, int position);

    protected abstract void destroy(H h, D d, int position);

}
