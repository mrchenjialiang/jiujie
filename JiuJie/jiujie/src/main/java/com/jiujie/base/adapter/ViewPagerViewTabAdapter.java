package com.jiujie.base.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.jiujie.base.util.ObjectCacheUtil;

import java.util.List;

/**
 * Use to Viewpager,with Fragment Item and TabLayout,Of course can use for Fragment Item ViewPager that not use TabLayout
 */
public class ViewPagerViewTabAdapter extends PagerAdapter {
    private List<View> viewList;
    private List<String> listTitle;
    private final ObjectCacheUtil<View> viewCacheUtil;

    public ViewPagerViewTabAdapter(List<View> viewList, List<String> listTitle) {
        this.viewList=viewList;
        this.listTitle=listTitle;
        viewCacheUtil = new ObjectCacheUtil<>();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = viewCacheUtil.getCacheObject();
        if(view==null){
            view = viewList.get(position);
        }
        container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);//这句别漏了!!!!
        return view;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
        viewCacheUtil.addCacheObject(view);
    }

    @Override
    public int getCount() {
        return viewList==null?0:viewList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(listTitle==null||listTitle.size()<=position)return null;
        return listTitle.get(position);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
