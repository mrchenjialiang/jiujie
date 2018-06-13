package com.jiujie.jiujie.ui.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jiujie.base.jk.OnListener;
import com.jiujie.base.util.UIHelper;

/**
 * Created by ChenJiaLiang on 2018/6/13.
 * Email:576507648@qq.com
 */

public class CircleLotteryView extends ViewGroup {

    /**
     * 圆的直径
     */
    private int diameter;
    /**
     * 判断是否正在自动滚动
     */
    private boolean isMove;
    /**
     * 布局滚动角度
     */
    private int mStartAngle = 0;
    /**
     * 每秒最大移动角度
     */
    private int mMax_Speed = 300;
    /**
     * 如果移动角度达到该值，则屏蔽点击
     */
    private int mMin_Speed = 3;
    private float mLastX;
    private float mLastY;
    private long mDownTime;
    private float mTmpAngle;
    private AngleRunnable mAngleRunnable;
    private float singleAngle;
    private RectF mRectF;
    private Paint mPaint;
    private String[] texts = {"时时彩", "11选5", "快3", "低频彩", "快乐8", "PK拾"};
    private int[] values = {1, 2, 3, 4, 5, 6};
    private int[] bgColors = {
            Color.parseColor("#FE961A"),
            Color.parseColor("#00B6EC"),
            Color.parseColor("#0095DB"),
            Color.parseColor("#00B6EC"),
            Color.parseColor("#FE961A"),
            Color.parseColor("#00B6EC"),
            Color.parseColor("#0095DB"),
            Color.parseColor("#00B6EC")
    };
    private int currentIndex = 0;
    private int currentValue = values[currentIndex];
    private OnListener<Integer> onItemChangeListener;
    private int itemHeight = 100;
    private int circleAllItemSize = 8;
    private int radius;
    private float y;
    private boolean isInitLayout;

    public CircleLotteryView(@NonNull Context context) {
        super(context);
        init();
    }

    public CircleLotteryView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleLotteryView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setClickable(true);
        mRectF = new RectF();
        mPaint = new Paint();
        itemHeight = UIHelper.dip2px(getContext(), 50);
    }

    public void setOnItemChangeListener(OnListener<Integer> onItemChangeListener) {
        this.onItemChangeListener = onItemChangeListener;
    }

    public void setData(String[] texts, int[] values, int[] bgColors, int currentIndex) {
        this.texts = texts;
        this.values = values;
        this.bgColors = bgColors;
        this.currentIndex = currentIndex;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        UIHelper.showLog(this,"onFinishInflate");
        removeAllViews();
        for (int i = 0;i<circleAllItemSize;i++){
            TextView textView = new TextView(getContext());
            textView.setTextSize(14);
            textView.setTextColor(Color.WHITE);
            textView.setText(texts[i%texts.length]);
            addView(textView);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        UIHelper.showLog(this,"onLayout isInitLayout:"+isInitLayout);
        if (!isInitLayout) {
            initLayout();
            diameter = Math.min(getWidth(),getHeight());
            radius = diameter / 2;
            isInitLayout = true;
        }
    }

    /**
     * 测量
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        UIHelper.showLog(this,"onMeasure getChildCount():"+getChildCount());
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = this.getChildAt(i);
            this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
            child.getMeasuredWidth();
        }
    }

    /**
     * 排版布局
     */
    private void initLayout() {
        //图片摆放的圆弧半径
        //计算图片圆的半径
        final int mContent = diameter / 2 - itemHeight / 2;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            //计算每个图片摆放的角度
            int mAnGle = 360 / circleAllItemSize * (i + 1);
            //获取每个图片摆放的左上角的x和y坐标
            float left = (float) (diameter / 2 + mContent * Math.cos(mAnGle * Math.PI / 180)) - child.getMeasuredWidth() / 2;
            float top = (float) (diameter / 2 + mContent * Math.sin(mAnGle * Math.PI / 180)) - child.getMeasuredHeight() / 2;
            /**
             * 一四象限
             */
            if (getQuadrantByAngle(mAnGle) == 1 || getQuadrantByAngle(mAnGle) == 4) {
                child.setRotation(mAnGle - 270);
                /**
                 * 二三象限
                 */
            } else {
                child.setRotation(mAnGle + 90);
            }
            child.layout((int) left, (int) top, (int) left + child.getMeasuredWidth(), (int) top + child.getMeasuredHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        UIHelper.showLog(this,"onDraw radius:"+radius);

        if (radius == 0) {
            diameter = Math.min(getWidth(), getHeight());
            radius = diameter /2;
        }
        // 设置画笔相关属性
        mPaint.setAntiAlias(true);
        //设置画布颜色
        canvas.drawColor(Color.TRANSPARENT);
        //设置圆环宽度
        mPaint.setStrokeWidth(itemHeight);
        //设置为空心圆
        mPaint.setStyle(Paint.Style.STROKE);


        // 设置位置
        mRectF.left = itemHeight / 2; // 左上角x
        mRectF.top = itemHeight / 2; // 左上角y
        mRectF.right = radius * 2 - itemHeight / 2; // 左下角x
        mRectF.bottom = radius * 2 - itemHeight / 2; // 右下角y

        int jgAngle = 2;

        singleAngle = (360 - jgAngle * circleAllItemSize) * 1.0f / circleAllItemSize;
        float startAngle = -90 - singleAngle / 2;
        for (int i = 0; i < circleAllItemSize; i++) {
            mPaint.setColor(bgColors[i % bgColors.length]);
            canvas.drawArc(mRectF, startAngle, singleAngle, false, mPaint);

//            invalidate();
//            postInvalidate();

            startAngle += singleAngle;
            startAngle += jgAngle;
        }

        //设置圆环宽度
//        mPaint.setStrokeWidth(0);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setTextSize(28);
//        mPaint.setColor(Color.WHITE);
//        mPaint.setTextAlign(Paint.Align.CENTER);

//        float x = radius;
//        float y = itemHeight / 2 + 14;
//        canvas.drawText("时时彩", x, y, mPaint);

//        startAngle = -90 - singleAngle / 2;
//        for (int i = 0; i < circleAllItemSize; i++) {
//            String text = texts[i % texts.length];
//
////            float x = (float) (radius + (radius - itemHeight / 2) * Math.cos(startAngle * Math.PI / 180));
////            float y = (float) (radius + (radius - itemHeight / 2) * Math.sin(startAngle * Math.PI / 180));
//            float x;
//            float y;
//            startAngle = startAngle%360;
//            if((startAngle<=0&&startAngle>=-90)||(startAngle>=270&&startAngle<=360)){
////                右上
//                x = (float) (radius + (radius - itemHeight / 2) * Math.cos(Math.abs(startAngle%90)));
//                y = (float) (radius - (radius - itemHeight / 2) * Math.sin(Math.abs(startAngle%90)));
//            }else if((startAngle<=-90&&startAngle>=-180)||(startAngle>=180&&startAngle<=270)){
////                左上
//                x = (float) (radius - (radius - itemHeight / 2) * Math.cos(Math.abs(startAngle%90)));
//                y = (float) (radius - (radius - itemHeight / 2) * Math.sin(Math.abs(startAngle%90)));
//            }else if((startAngle<=-180&&startAngle>=-270)||(startAngle>=90&&startAngle<=180)){
////                左下
//                x = (float) (radius - (radius - itemHeight / 2) * Math.cos(Math.abs(startAngle%90)));
//                y = (float) (radius + (radius - itemHeight / 2) * Math.sin(Math.abs(startAngle%90)));
//            }else{
////                右下
//                x = (float) (radius + (radius - itemHeight / 2) * Math.cos(Math.abs(startAngle%90)));
//                y = (float) (radius + (radius - itemHeight / 2) * Math.sin(Math.abs(startAngle%90)));
//            }
//
//            canvas.drawText(text, x, y, mPaint);
//
//
//            startAngle += singleAngle;
//            startAngle += jgAngle;
//        }
    }

    /**
     * 触摸监听
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                mDownTime = System.currentTimeMillis();
                mTmpAngle = 0;

                // 如果当前已经在快速滚动
                if (isMove) {
                    // 移除快速滚动的回调
                    removeCallbacks(mAngleRunnable);
                    isMove = false;
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                /**
                 * 获得开始的角度
                 */
                float start = getAngle(mLastX, mLastY);
                /**
                 * 获得当前的角度
                 */
                float end = getAngle(x, y);
                Log.e("TAG", "start = " + start + " , end =" + end);
                // 一四象限
                if (getQuadrant(x, y) == 1 || getQuadrant(x, y) == 4) {
                    mStartAngle += end - start;
                    mTmpAngle += end - start;
                    //二三象限
                } else {
                    mStartAngle += start - end;
                    mTmpAngle += start - end;
                }
                // 重新布局
                getCheck();
                break;
            case MotionEvent.ACTION_UP:
                // 获取每秒移动的角度
                float anglePerSecond = mTmpAngle * 1000
                        / (System.currentTimeMillis() - mDownTime);
                // 如果达到最大速度
                if (Math.abs(anglePerSecond) > mMax_Speed && !isMove) {
                    // 惯性滚动
                    post(mAngleRunnable = new AngleRunnable(anglePerSecond));
                    return true;
                }
                // 如果当前旋转角度超过minSpeed屏蔽点击
                if (Math.abs(mTmpAngle) > mMin_Speed) {
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 旋转圆盘
     */
    private void getCheck() {
        mStartAngle %= 360;
        setRotation(mStartAngle);
    }

    /**
     * 获取移动的角度
     */
    private float getAngle(float xTouch, float yTouch) {
        double x = xTouch - (diameter / 2d);
        double y = yTouch - (diameter / 2d);
        return (float) (Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
    }

    /**
     * 根据当前位置计算象限
     */
    private int getQuadrant(float x, float y) {
        int tmpX = (int) (x - diameter / 2);
        int tmpY = (int) (y - diameter / 2);
        if (tmpX >= 0) {
            return tmpY >= 0 ? 4 : 1;
        } else {
            return tmpY >= 0 ? 3 : 2;
        }

    }


    /**
     * 通过角度判断象限
     */
    private int getQuadrantByAngle(int angle) {
        if (angle <= 90) {
            return 4;
        } else if (angle <= 180) {
            return 3;
        } else if (angle <= 270) {
            return 2;
        } else {
            return 1;
        }
    }

    /**
     * 惯性滚动
     */
    private class AngleRunnable implements Runnable {

        private float angelPerSecond;

        public AngleRunnable(float velocity) {
            this.angelPerSecond = velocity;
        }

        public void run() {
            //小于20停止
            if ((int) Math.abs(angelPerSecond) < 20) {
                isMove = false;
                return;
            }
            isMove = true;
            // 滚动时候不断修改滚动角度大小
            mStartAngle += (angelPerSecond / 30);
            //逐渐减小这个值
            angelPerSecond /= 1.0666F;
            postDelayed(this, 30);
            // 重新布局
            getCheck();
        }
    }
}
