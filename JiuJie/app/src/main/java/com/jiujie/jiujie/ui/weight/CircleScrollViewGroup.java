package com.jiujie.jiujie.ui.weight;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.jiujie.base.util.UIHelper;

/**
 * Created by ChenJiaLiang on 2018/6/13.
 * Email:576507648@qq.com
 */

public class CircleScrollViewGroup extends FrameLayout {

    /**
     * 圆的直径
     */
    private int mRadius;
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
    private boolean isResetLayout;
    private float mLastX;
    private float mLastY;
    private long mDownTime;
    private float mTmpAngle;
    private AngleRunnable mAngleRunnable;
    private float singleAngle;

    public CircleScrollViewGroup(@NonNull Context context) {
        super(context);
        init();
    }

    public CircleScrollViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleScrollViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setClickable(true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (!isResetLayout && getChildCount() > 0) {
            initLayout();
            mRadius = getWidth();
            isResetLayout = true;
        }
    }

    private void initLayout() {
        int width = this.getWidth();
        int height = this.getHeight();
        if (width != height) {
            int min = Math.min(width, height);
            width = min;
            height = min;
        }

        int childCount = getChildCount();
        singleAngle = 360 * 1.0f / childCount;
        float currentAngle = 0;
        int padding = getPaddingTop();
        int centerX = width / 2;
        int centerY = height / 2;
        int R = width / 2 - padding;

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            int x = (int) (centerX - R * Math.sin(Math.PI * (currentAngle - 90) / 180));
            int y = (int) (centerY - R - 10 + R * Math.cos(Math.PI * (currentAngle - 90) / 180));
            child.setRotation(currentAngle);
            child.layout(x, y, x + child.getMeasuredWidth(), y + child.getMeasuredHeight());
            currentAngle += singleAngle;

            UIHelper.showLog("initLayout:x" + x + ",y:" + y);
        }

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
        double x = xTouch - (mRadius / 2d);
        double y = yTouch - (mRadius / 2d);
        return (float) (Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
    }

    /**
     * 根据当前位置计算象限
     */
    private int getQuadrant(float x, float y) {
        int tmpX = (int) (x - mRadius / 2);
        int tmpY = (int) (y - mRadius / 2);
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
