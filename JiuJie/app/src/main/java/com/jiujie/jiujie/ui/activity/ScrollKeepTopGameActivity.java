package com.jiujie.jiujie.ui.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.flyco.tablayout.SlidingTabLayout;
import com.jiujie.base.activity.BaseScrollKeepHeaderActivity;
import com.jiujie.base.adapter.ViewPagerFragmentTabAdapter;
import com.jiujie.base.util.ImageUtil;
import com.jiujie.base.util.TaskManager;
import com.jiujie.base.util.UIHelper;
import com.jiujie.glide.GlideUtil;
import com.jiujie.jiujie.R;
import com.jiujie.jiujie.ui.fragment.SimpleListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ScrollKeepTopGameActivity extends BaseScrollKeepHeaderActivity {

    @Bind(R.id.hpc_tabLayout)
    SlidingTabLayout slidingTabLayout;
    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Bind(R.id.gd_top_image_bg)
    ImageView topImageBg;

    @Bind(R.id.bskh_appBar)
    View missAndTopBgView;
    private View missLine;
    private View keepTopLine;
    private Bitmap imageBitmap;

    @Override
    public void initUI() {
        ButterKnife.bind(this);
        missLine = getMissLine();
        keepTopLine = getKeepTopLine();

        setRefreshEnable(false);

        List<Fragment> fragmentList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();
        fragmentList.add(new SimpleListFragment());
        fragmentList.add(new SimpleListFragment());
        titleList.add("11111");
        titleList.add("22222");
        viewPager.setAdapter(new ViewPagerFragmentTabAdapter(getSupportFragmentManager(),fragmentList,titleList));
        slidingTabLayout.setViewPager(viewPager);

        new Thread(new Runnable() {
            @Override
            public void run() {
                imageBitmap = GlideUtil.instance().getImageBitmap(mActivity, R.drawable.game_cs1);
            }
        }).start();
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_game_detail;
    }

    @Override
    public int getDisMissHeaderLayoutId() {
        return R.layout.game_miss;
    }

    @Override
    public int getKeepTopHeaderLayoutId() {
        return R.layout.game_keep;
    }

    @Override
    public int getScrollLayoutId() {
        return R.layout.viewpager;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        verticalOffset = Math.abs(verticalOffset);
        if(verticalOffset==0){
//            missAndTopBgView.setBackgroundColor(Color.parseColor("#00ffffff"));
            missAndTopBgView.setBackgroundResource(R.drawable.game_cs_2_top);
        }else if(verticalOffset >= missLine.getHeight() + keepTopLine.getHeight()){
            missAndTopBgView.setBackgroundColor(Color.parseColor("#"+Integer.toHexString(255/100*10)+"ffffff"));
        }else{
            int heightB = verticalOffset * 100 / (missLine.getHeight() + keepTopLine.getHeight());
            int minB = 1;
            int maxB = 10;
            heightB = minB + heightB*(maxB - minB) / 100;
            UIHelper.showLog("heightB:"+heightB);
//            GlideUtil.instance().setSimpleBlur(mActivity,R.drawable.game_cs1,topImageBg,R.drawable.trans,heightB,1,false);
            String heightB16 = Integer.toHexString(255/100*heightB);
            if(heightB16.length()==1){
                heightB16 = "0"+heightB16;
            }
            UIHelper.showLog("heightB16:"+heightB16);
            missAndTopBgView.setBackgroundColor(Color.parseColor("#"+heightB16+"ffffff"));
        }
    }

//    private Bitmap getImageBitmap(Bitmap bitmap){
//        ImageUtil.instance().
//        return bitmap;
//    }

    @Override
    public void onRefresh() {

    }
}
