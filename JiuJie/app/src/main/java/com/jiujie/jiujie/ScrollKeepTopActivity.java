package com.jiujie.jiujie;

import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.flyco.tablayout.SlidingTabLayout;
import com.jiujie.base.activity.BaseScrollKeepHeaderActivity;
import com.jiujie.base.adapter.ViewPagerFragmentTabAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ScrollKeepTopActivity extends BaseScrollKeepHeaderActivity {

    @Bind(R.id.hpc_tabLayout)
    SlidingTabLayout slidingTabLayout;
    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Override
    public void initUI() {
        ButterKnife.bind(this);

        setRefreshEnable(false);

        List<Fragment> fragmentList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();
        fragmentList.add(new SimpleListFragment());
        fragmentList.add(new SimpleListFragment());
        titleList.add("11111");
        titleList.add("22222");
        viewPager.setAdapter(new ViewPagerFragmentTabAdapter(getSupportFragmentManager(),fragmentList,titleList));
        slidingTabLayout.setViewPager(viewPager);
    }

    @Override
    public void initData() {

    }

    @Override
    public int getDisMissHeaderLayoutId() {
        return R.layout.scroll_keep_top_miss;
    }

    @Override
    public int getKeepTopHeaderLayoutId() {
        return R.layout.scroll_keep_top_keep;
    }

    @Override
    public int getScrollLayoutId() {
        return R.layout.viewpager;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

    }

    @Override
    public void onRefresh() {

    }
}
