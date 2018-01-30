package com.jiujie.base.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by Administrator on 2017/5/31.
 */

public class MyAdViewPager extends View{
    private List<String> dataList;

    public MyAdViewPager(Context context) {
        super(context);
    }

    public MyAdViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyAdViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setDataList(List<String> dataList){
        this.dataList = dataList;
    }
}
