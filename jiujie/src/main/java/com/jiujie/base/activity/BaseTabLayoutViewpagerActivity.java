package com.jiujie.base.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.jiujie.base.R;
import com.jiujie.base.adapter.ViewPagerFragmentTabAdapter;

import java.util.List;

/**
 * author : Created by ChenJiaLiang on 2016/10/9.
 * Email : 576507648@qq.com
 */
public abstract class BaseTabLayoutViewpagerActivity extends BaseSlideContentActivity{
    protected ViewPager mViewPager;
    protected TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    private void initUI() {
        mTabLayout = (TabLayout) findViewById(R.id.base_tv_tabLayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        List<String> titleList = getTitleList();
        mViewPager.setAdapter(new ViewPagerFragmentTabAdapter(getSupportFragmentManager(), getFragmentList(), titleList));
        mTabLayout.setupWithViewPager(mViewPager);//must set after viewpager.setAdapter
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setDrawerScrollEnable(position==0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    protected abstract List<Fragment> getFragmentList();

    protected abstract List<String> getTitleList();

    @Override
    public int getLayoutId() {
        return R.layout.activity_base_tablayout_viewpager;
    }

    @Override
    public void initData() {

    }
}
