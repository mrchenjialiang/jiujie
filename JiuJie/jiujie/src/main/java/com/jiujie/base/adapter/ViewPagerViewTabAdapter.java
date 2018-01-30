package com.jiujie.base.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

/**
 * Use to Viewpager,with Fragment Item and TabLayout,Of course can use for Fragment Item ViewPager that not use TabLayout
 */
public class ViewPagerViewTabAdapter extends PagerAdapter {
    private List<View> viewList;
    private List<String> listTitle;
    private LinkedList<View> viewCaches = new LinkedList<>();

    public ViewPagerViewTabAdapter(List<View> viewList, List<String> listTitle) {
        this.viewList=viewList;
        this.listTitle=listTitle;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view;
        if(viewCaches.size()==0){
            view = viewList.get(position);
        }else{
            view = viewCaches.removeFirst();
        }
        container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);//这句别漏了!!!!
        return view;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        viewCaches.add((View) object);
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
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
