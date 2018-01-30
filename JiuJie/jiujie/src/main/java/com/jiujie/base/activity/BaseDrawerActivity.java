//package com.jiujie.base.activity;
//
//import android.content.Intent;
//import android.graphics.drawable.AnimationDrawable;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBar;
//import android.support.v7.widget.Toolbar;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//
//import com.jiujie.base.R;
//import com.jiujie.base.Title;
//import com.jiujie.base.jk.LoadStatus;
//import com.jiujie.base.util.UIHelper;
//import com.jiujie.base.widget.DrawerLayoutNoScroll;
//
///**
// * Created by ChenJiaLiang on 2016/6/3.
// * Email : 576507648@qq.com
// */
//public abstract class BaseDrawerActivity extends BaseMostActivity  implements LoadStatus {
//    public Title mTitle;
//    private View mLoadingLine,mLoadingFail;
//    private AnimationDrawable mLoadingAnimation;
//    public LinearLayout contentLayout;
//    private View customTitleView;
//    private LinearLayout mTagLayout;
//    private LinearLayout drawerContent;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_base_slide_content);
//
//        initDrawer();
//
//        initView();
//        initBaseTitle();
//
//    }
//
//    public abstract boolean isShowTitle();
//
//    private DrawerLayoutNoScroll drawerLayout;
//
//    public DrawerLayoutNoScroll getDrawerLayout() {
//        return drawerLayout;
//    }
//
//    public void setDrawerScrollEnable(boolean isEnable){
//        if(drawerLayout!=null)
//            drawerLayout.setIsCanScroll(isEnable);
//    }
//
//    protected int getDrawerInsideLayoutId(){
//        return 0;
//    }
//
//    protected void openDrawer(){
//        if(drawerLayout!=null&&drawerContent!=null&&drawerContent.getChildCount()>0){
//            drawerLayout.openDrawer(drawerContent);
//        }
//    }
//
//    protected void closeDrawer(){
//        if(drawerLayout!=null&&drawerContent!=null&&drawerContent.getChildCount()>0){
//            drawerLayout.closeDrawer(drawerContent);
//        }
//    }
//
//    /**
//     * 设置菜单位置，如Gravity.END右侧菜单，Gravity.START 左侧菜单
//     */
//    protected void setDrawerDirection(int gravity){
//        DrawerLayout.LayoutParams layoutParams = (DrawerLayout.LayoutParams) drawerContent.getLayoutParams();
//        layoutParams.gravity = gravity;
//        drawerContent.setLayoutParams(layoutParams);
//
//    }
//
//    private void initDrawer() {
//        drawerLayout = (DrawerLayoutNoScroll) findViewById(R.id.base_drawer);
//        drawerContent = (LinearLayout) findViewById(R.id.base_drawer_content);
//        if(getDrawerInsideLayoutId()!=0){
//            View drawerInsideContent = getLayoutInflater().inflate(getDrawerInsideLayoutId(), drawerContent, false);
//            drawerContent.addView(drawerInsideContent);
//        }
//    }
//
//    @Override
//    public void startActivity(Intent intent) {
//        super.startActivity(intent);
//        UIHelper.hidePan(mActivity);
//        overridePendingTransition(R.anim.slide_right_in, R.anim.no_anim);
//    }
//
//    @Override
//    public void finish() {
//        super.finish();
//        UIHelper.hidePan(mActivity);
//        overridePendingTransition(0, R.anim.slide_right_out);
//    }
//
//    /**
//     * 如果重写了，返回不是0，则不能使用mTitle
//     * 如果另外定义了标题，需注意标题栏颜色，如改，则需setToolBarBackGround
//     */
//    public int getCustomTitleLayoutId(){
//        return 0;
//    }
//
//    public View getBaseTitleLayout() {
//        return customTitleView;
//    }
//
//    private void initBaseTitle() {
//
//        if(getCustomTitleLayoutId()==0){
//            actionBar.setCustomView(R.layout.base_title); //设置自定义布局界面
//            customTitleView = actionBar.getCustomView();  //获取自定义View进行设置
//            mTitle = new Title(this,customTitleView);
//        }else{
//            actionBar.setCustomView(getCustomTitleLayoutId()); //设置自定义布局界面
//            customTitleView = actionBar.getCustomView();  //获取自定义View进行设置
//        }
//    }
//
//    public abstract int getLayoutId();
//
//    @SuppressWarnings("deprecation")
//    private void initView() {
//        contentLayout = (LinearLayout) findViewById(R.id.base_content);
//        if (getLayoutId() == 0) {
//            throw new NullPointerException(this+" getLayoutId() should not be 0");
//        }
//        View contentView = LayoutInflater.from(this).inflate(getLayoutId(),contentLayout,false);
//        LinearLayout.LayoutParams lpContent = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.FILL_PARENT,
//                LinearLayout.LayoutParams.FILL_PARENT);
//        contentView.setLayoutParams(lpContent);
//        lpContent.weight = 1;
//        contentLayout.addView(contentView);
//
//        contentLayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                setDrawerScrollEnable(true);
//                return false;
//            }
//        });
//        initLoading();
//    }
//
//    public LinearLayout getContentLayout() {
//        return contentLayout;
//    }
//
//    public void setContentLayoutBackGroundColor(int color){
//        if(contentLayout!=null)
//            contentLayout.setBackgroundColor(color);
//    }
//
//    private void initLoading() {
//        mTagLayout = (LinearLayout) findViewById(R.id.base_tag_layout);
//        mLoadingLine = findViewById(R.id.base_loading_line);
//        mLoadingFail = findViewById(R.id.base_loading_fail);
//        mTagLayout.setVisibility(View.GONE);
//        mLoadingLine.setVisibility(View.GONE);
//        mLoadingFail.setVisibility(View.GONE);
//        mLoadingLine.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("mLoadingLine-click");
//            }
//        });
//        findViewById(R.id.base_hide_pan).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                UIHelper.hidePan(mActivity);
//            }
//        });
//        mLoadingFail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                initData();
//            }
//        });
//
//        if(getTagLayoutId()!=0){
//            mTagLayout.addView(getLayoutInflater().inflate(getTagLayoutId(),mTagLayout,false), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        }
//    }
//
//    public void showTabLayout(boolean isShow){
//        mTagLayout.setVisibility(isShow?View.VISIBLE:View.GONE);
//    }
//
//    public int getTagLayoutId(){
//        return 0;
//    }
//
//    public abstract void initData();
//
//    public void setLoading(){
//        if(mLoadingAnimation==null){
//            ImageView image = (ImageView) findViewById(R.id.base_loading_image);
//            image.setImageResource(R.drawable.loading);
//            mLoadingAnimation = (AnimationDrawable) image.getDrawable();
//        }
//        mLoadingLine.setVisibility(View.VISIBLE);
//        mLoadingFail.setVisibility(View.GONE);
//        mLoadingAnimation.start();
//    }
//
//    public void setLoadingFail(){
//        setLoadingEnd();
//        mLoadingFail.setVisibility(View.VISIBLE);
//    }
//
//    public void setLoadingEnd(){
//        if(mLoadingAnimation!=null&&mLoadingAnimation.isRunning()){
//            mLoadingAnimation.stop();
//            mLoadingLine.setVisibility(View.GONE);
//        }
//        mLoadingFail.setVisibility(View.GONE);
//    }
//}
