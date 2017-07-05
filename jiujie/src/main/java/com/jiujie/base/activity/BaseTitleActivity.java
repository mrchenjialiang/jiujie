package com.jiujie.base.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jiujie.base.R;
import com.jiujie.base.Title;
import com.jiujie.base.jk.LoadStatus;
import com.jiujie.base.util.UIHelper;

public abstract class BaseTitleActivity extends BaseSlideActivity implements LoadStatus {

    public Title mTitle;
    private LinearLayout mBaseTitleTitleLayout;
    private LinearLayout mBaseTitleContentLayout;
    private LinearLayout mBaseTitleToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 这句很关键，注意是调用父类的方法，因为这里的setContentView重写了
        super.setContentView(R.layout.activity_base_title);

        mBaseTitleToolbarLayout = (LinearLayout) findViewById(R.id.base_title_toolbar_layout);
        mBaseTitleTitleLayout = (LinearLayout) findViewById(R.id.base_title_title_layout);
        mBaseTitleContentLayout = (LinearLayout) findViewById(R.id.base_title_content_layout);

        initBaseToolbar();
        initBaseTitle();
        initBaseContent();
    }

    private void initBaseToolbar() {
        if(isUseToolbar()){
            initToolbar();
        }else{
            mBaseTitleToolbarLayout.setVisibility(View.GONE);
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if(actionBar==null)return;
            // 返回箭头（默认不显示）
            actionBar.setDisplayHomeAsUpEnabled(false);
            // 左侧图标点击事件使能
            actionBar.setHomeButtonEnabled(false);
            // 使左上角图标(系统)是否显示
            actionBar.setDisplayShowHomeEnabled(false);
            // 显示标题
            actionBar.setDisplayShowTitleEnabled(false);
            // 显示自定义视图
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(R.layout.fq_toolbar_title); //设置自定义布局界面--无用的标题栏，只是用来填充一下
//        setToolBarHeight(0);
            ViewGroup.LayoutParams params = toolbar.getLayoutParams();
            if(params!=null){
                params.height = 0;
                toolbar.setLayoutParams(params);
            }
        }
    }

    private void initBaseContent() {
        int baseContentLayoutId = getBaseContentLayoutId();
        if(baseContentLayoutId>0){
            mBaseTitleContentLayout.addView(getLayoutInflater().inflate(baseContentLayoutId,mBaseTitleContentLayout,false), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    /**
     * 当用到BaseScrollKeepHeaderFragment(用CoordinatorLayout)的时候，当不显示标题栏时，需要返回true，不然置顶会留空状态栏
     */
    protected boolean isUseToolbar(){
        return false;
    }

    /**
     * 设置自定义标题栏
     * 重写则不能使用mTitle
     */
    public int getCustomTitleLayoutId(){
        return 0;
    }

    public boolean isShowTitle(){
        return true;
    }

    public LinearLayout getBaseTitleLayout() {
        return mBaseTitleTitleLayout;
    }

    public LinearLayout getBaseContentLayout() {
        return mBaseTitleContentLayout;
    }

    public abstract int getBaseContentLayoutId();

    private void initBaseTitle() {
        if(isShowTitle()){
            mBaseTitleTitleLayout.setVisibility(View.VISIBLE);
            LayoutInflater inflater = getLayoutInflater();
            View customTitleView;
            if(getCustomTitleLayoutId()==0){
                customTitleView = inflater.inflate(R.layout.base_title, mBaseTitleTitleLayout,false);
                mTitle = new Title(this,customTitleView);
            }else{
                customTitleView = inflater.inflate(getCustomTitleLayoutId(), mBaseTitleTitleLayout,false);
            }
            mBaseTitleTitleLayout.addView(customTitleView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                int titleHeight = isShowTitle()?getResources().getDimensionPixelOffset(R.dimen.height_of_title):0;
//                getDimension 获取绝对尺寸（"像素"单位，只是float）,
//                getDimensionPixelSize是将getDimension获取的小数部分四舍五入，
//                getDimensionPixelOffset是强制转换成int，即舍去小数部分
                int statusBarHeight = UIHelper.getStatusBarHeightByReadR(mActivity);
                setViewHeight(customTitleView, titleHeight+statusBarHeight);
            }
        }else{
            mBaseTitleTitleLayout.setVisibility(View.GONE);
        }
    }

    protected void setViewHeight(View view, int height) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if(lp!=null){
            lp.height = height;
            view.setLayoutParams(lp);
        }
    }
    protected void setViewSize(View view, int width,int height) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if(lp!=null){
            lp.height = height;
            lp.width = width;
            view.setLayoutParams(lp);
        }
    }

    @Override
    public final void setContentView(@LayoutRes int layoutResID) {
        throw new IllegalStateException("setContentView can't use in BaseTitleActivity");
    }

    @Override
    public final void setContentView(View view) {
        throw new IllegalStateException("setContentView can't use in BaseTitleActivity");
    }

    @Override
    public final void setContentView(View view, ViewGroup.LayoutParams params) {
        throw new IllegalStateException("setContentView can't use in BaseTitleActivity");
    }

}
