//package com.jiujie.base.activity;
//
//import android.os.Bundle;
//import android.support.v7.app.ActionBar;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//
//import com.jiujie.base.R;
//import com.jiujie.base.Title;
//
//public abstract class BaseNewActivity extends BaseSlideActivity {
//
//    private Toolbar toolbar;
//    private Title mTitle;
//    private View customTitleView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        // 经测试在代码里直接声明透明状态栏更有效
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
////            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
////            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
////        }
//
//        super.setContentView(R.layout.activity_title1);
//        initToolbar();
//        initBaseTitle();
//
//    }
//
//    private void initBaseTitle() {
//        LinearLayout mBaseTitleLayout = (LinearLayout) findViewById(R.id.base_title_layout);
//        if(getCustomTitleLayoutId()==0){
//            mBaseTitleLayout.setVisibility(View.GONE);
//        }else{
//            mBaseTitleLayout.setVisibility(View.VISIBLE);
//            customTitleView = getLayoutInflater().inflate(getCustomTitleLayoutId(), mBaseTitleLayout, false);
//            if(getCustomTitleLayoutId()==R.layout.base_title){
//                mTitle = new Title(mActivity);
//            }
//            mBaseTitleLayout.addView(customTitleView);
//        }
//    }
//
//    protected abstract int getCustomTitleLayoutId();
//
//    private void initToolbar() {
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        ActionBar actionBar = getSupportActionBar();
//        if(actionBar==null)return;
//        // 返回箭头（默认不显示）
//        actionBar.setDisplayHomeAsUpEnabled(false);
//        // 左侧图标点击事件使能
//        actionBar.setHomeButtonEnabled(false);
//        // 使左上角图标(系统)是否显示
//        actionBar.setDisplayShowHomeEnabled(false);
//        // 显示标题
//        actionBar.setDisplayShowTitleEnabled(false);
//        // 显示自定义视图
//        actionBar.setDisplayShowCustomEnabled(true);
//        actionBar.setCustomView(R.layout.base_title); //设置自定义布局界面
//        setToolBarHeight(0);
//    }
//
//    public void setToolBarHeight(int height){
//        if(toolbar==null) return;
//        ViewGroup.LayoutParams params = toolbar.getLayoutParams();
//        if(params!=null){
//            params.height = height;
//            toolbar.setLayoutParams(params);
//        }
//    }
//
//}
