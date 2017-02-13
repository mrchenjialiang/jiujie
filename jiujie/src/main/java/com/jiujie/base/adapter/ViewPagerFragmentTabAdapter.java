package com.jiujie.base.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Use to Viewpager,with Fragment Item and TabLayout,Of course can use for Fragment Item ViewPager that not use TabLayout
 */
public class ViewPagerFragmentTabAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    private List<String> listTitle;

    public ViewPagerFragmentTabAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> listTitle) {
        super(fm);
        this.fragmentList=fragmentList;
        this.listTitle=listTitle;
    }

    @Override
    public Fragment getItem(int position) {
        if(fragmentList==null||fragmentList.size()<=position)return null;
        return fragmentList.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public int getCount() {
        return fragmentList==null?0:fragmentList.size();
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
