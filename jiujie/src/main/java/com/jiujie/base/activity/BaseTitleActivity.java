package com.jiujie.base.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jiujie.base.R;
import com.jiujie.base.Title;
import com.jiujie.base.util.UIHelper;

public abstract class BaseTitleActivity extends BaseSlideActivity{

    public int statusBarHeight;
    public Title mTitle;
    private Toolbar toolbar;
    private View customTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 这句很关键，注意是调用父类的方法，因为这里的setContentView重写了
        super.setContentView(R.layout.activity_base_toolbar);
        initToolbar();
        initTitle();
    }

    /**
     * 设置自定义标题栏
     * 重写则不能使用mTitle
     */
    public int getCustomTitleLayoutId(){
        return 0;
    }

    public View getCustomTitleView() {
        return customTitleView;
    }

    private void initTitle() {
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

        if(getCustomTitleLayoutId()==0){
            actionBar.setCustomView(R.layout.base_title); //设置自定义布局界面
            customTitleView = actionBar.getCustomView();  //获取自定义View进行设置
            mTitle = new Title(this,customTitleView);
        }else{
            actionBar.setCustomView(getCustomTitleLayoutId()); //设置自定义布局界面
            customTitleView = actionBar.getCustomView();  //获取自定义View进行设置
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        int titleHeight = isShowTitle()?getResources().getDimensionPixelOffset(R.dimen.height_of_title):0;
//		getDimension 获取绝对尺寸（"像素"单位，只是float）,
//      getDimensionPixelSize是将getDimension获取的小数部分四舍五入，
//      getDimensionPixelOffset是强制转换成int，即舍去小数部分

        //需设置ToolBar高度，否则系统将默认为actionBar高度，如有差值，效果不佳
        statusBarHeight = UIHelper.getStatusBarHeightByReadR(mActivity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            //低版本有默认ToolBar高度
            setToolBarHeight(statusBarHeight + titleHeight);
        }else{
            setToolBarHeight(titleHeight);
        }
    }

    public abstract boolean isShowTitle();

    public void setToolBarHeight(int height){
        if(toolbar==null) return;
        ViewGroup.LayoutParams params = toolbar.getLayoutParams();
        if(params!=null){
            params.height = height;
            toolbar.setLayoutParams(params);
        }
    }

    public void setToolBarBackGround(int color){
        if(toolbar==null) return;
        toolbar.setBackgroundColor(color);
    }

    @Override
    public final void setContentView(int layoutId) {
        setContentView(View.inflate(this, layoutId, null));
    }

    @Override
    public final void setContentView(View view) {
        LinearLayout rootLayout = (LinearLayout) findViewById(R.id.root_layout);
        if (rootLayout == null) return;
        rootLayout.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }
}
