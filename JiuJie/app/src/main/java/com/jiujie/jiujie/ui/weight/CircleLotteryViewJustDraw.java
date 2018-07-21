package com.jiujie.jiujie.ui.weight;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.jiujie.base.jk.OnListener;
import com.jiujie.base.util.UIHelper;

/**
 * Created by ChenJiaLiang on 2018/6/13.
 * Email:576507648@qq.com
 */

public class CircleLotteryViewJustDraw extends View {

    /**
     * 圆的直径
     */
    private int diameter;
    private RectF mRectF;
    private Paint mPaint;
    private String[] texts = {"时时彩", "11选5", "快3", "低频彩", "快乐8", "PK拾"};
    private int[] values = {1, 2, 3, 4, 5, 6};
    private int[] bgColors = {
            Color.parseColor("#FE961A"),//1
            Color.parseColor("#00B6EC"),//2
            Color.parseColor("#0095DB"),//3
            Color.parseColor("#00B6EC"),//2
            Color.parseColor("#FE961A"),//1
            Color.parseColor("#00B6EC"),//2
            Color.parseColor("#0095DB"),//3
            Color.parseColor("#00B6EC"),//2
            Color.parseColor("#FE961A"),//1
            Color.parseColor("#00B6EC"),//2
            Color.parseColor("#0095DB"),//3
            Color.parseColor("#00B6EC")//2
    };
    private int currentIndex = 0;
    private int currentValue = values[currentIndex];
    private OnListener<Integer> onItemChangeListener;
    private int itemHeight = 100;
    private int circleAllItemSize = 12;
    private int radius;
    private float singleAngle;
    private float singleAngleForArc;
    private int jgAngle = 2;
    private ValueAnimator scrollAnimator;
    private int mTouchSlop;
    private float tempDownAngle;
    private int currentPosition;
    private float downX;
    private float downY;
    private float downTotalMoveAngle;
    private float rotateAngle;
    private float finalDownAngle;

    public CircleLotteryViewJustDraw(@NonNull Context context) {
        super(context);
        init();
    }

    public CircleLotteryViewJustDraw(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleLotteryViewJustDraw(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setClickable(true);
        mRectF = new RectF();
        mPaint = new Paint();
        // 设置画笔相关属性
        mPaint.setAntiAlias(true);
        itemHeight = UIHelper.dip2px(getContext(), 50);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (radius == 0) {
            diameter = Math.min(getWidth(), getHeight());
            radius = diameter / 2;
            singleAngle = 360 / circleAllItemSize;
            singleAngleForArc = (360 - jgAngle * circleAllItemSize) * 1.0f / circleAllItemSize;


            // 设置位置
            mRectF.left = itemHeight / 2; // 左上角x
            mRectF.top = itemHeight / 2; // 左上角y
            mRectF.right = radius * 2 - itemHeight / 2; // 左下角x
            mRectF.bottom = radius * 2 - itemHeight / 2; // 右下角y
        }

        canvas.save();
        canvas.rotate(rotateAngle, getWidth() / 2, getHeight() / 2);
        canvas.restore();

        //设置圆环宽度
        mPaint.setStrokeWidth(itemHeight);
        //设置为空心圆
        mPaint.setStyle(Paint.Style.STROKE);

        float startAngle = -90 - singleAngleForArc / 2 + rotateAngle;
        for (int i = 0; i < circleAllItemSize; i++) {
            mPaint.setColor(bgColors[i % bgColors.length]);
            canvas.drawArc(mRectF, startAngle, singleAngleForArc, false, mPaint);
            startAngle += singleAngleForArc;
            startAngle += jgAngle;
        }

        //设置圆环宽度
        mPaint.setStrokeWidth(0);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(28);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextAlign(Paint.Align.CENTER);

        for (int i = 0; i < circleAllItemSize; i++) {
            canvas.save();
            canvas.rotate(singleAngle * i + rotateAngle, getWidth() / 2, getHeight() / 2);
            float x = radius;
            float y = itemHeight / 2 + 14;
            canvas.drawText(texts[i % texts.length], x, y, mPaint);
            canvas.restore();
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
                finalDownAngle = tempDownAngle = getAbsAngle(x, y);

                UIHelper.showLog(this, "tempDownAngle:" + tempDownAngle);

                if (Math.pow(x - getWidth() / 2, 2) + Math.pow(y - getHeight() / 2, 2) > Math.pow(radius, 2)) {
                    return false;
                } else {
                    // 如果当前已经在快速滚动
                    if (scrollAnimator != null && scrollAnimator.isRunning()) {
                        scrollAnimator.cancel();
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                onEventMove(x, y);
                break;
            case MotionEvent.ACTION_UP:
                onEventUp(x, y);
                break;
            case MotionEvent.ACTION_CANCEL:
                onEventUp(x, y);
                break;
        }
        return true;
    }

    private void onEventMove(float x, float y) {
        float currentAngle = getAbsAngle(x, y);
        float disAngle = currentAngle - tempDownAngle;
        tempDownAngle = currentAngle;
        rotateAngle += disAngle;
        rotateAngle = rotateAngle % 360;
        invalidate();
    }

    private void onEventUp(float x, float y) {
        float upAngle = getAbsAngle(x, y);
        UIHelper.showLog(this, "upAngle:" + upAngle);
        if (Math.abs(upAngle - finalDownAngle) < 0.1) {
            if (Math.pow(x - getWidth() / 2, 2) + Math.pow(y - getHeight() / 2, 2) < Math.pow(radius - itemHeight, 2)) {
                return;
            }
            doClick(upAngle);
        } else {
            doFlying();
        }
    }

    private void doClick(float upAngle) {
        UIHelper.showLog(this, "doClick");
        float resultAngle = rotateAngle + 270 - getResultAngle(upAngle);
        if (rotateAngle == resultAngle) {
            return;
        }
        startAnim(rotateAngle, resultAngle);
    }

    private void doFlying() {
        UIHelper.showLog(this, "doFlying rotateAngle:" + rotateAngle);

        if (rotateAngle % singleAngle == 0) {
            return;
        }
        float resultAngle = rotateAngle;
        resultAngle = getResultAngle(resultAngle);
        if (rotateAngle == resultAngle) {
            return;
        }
        startAnim(rotateAngle, resultAngle);
    }

    private float getResultAngle(float originalAngle) {
        float resultAngle;
        int size = (int) (originalAngle / singleAngle);
        float left = originalAngle - singleAngle * size;
        if (Math.abs(left) >= singleAngle / 2) {
            if (left > 0) {
                resultAngle = singleAngle * (size + 1);
            } else {
                resultAngle = singleAngle * (size - 1);
            }
        } else {
            resultAngle = singleAngle * size;
        }
        resultAngle = resultAngle % 360;
        return resultAngle;
    }

    /**
     * 获取绝对角度
     * 0-360
     */
    private float getAbsAngle(float xTouch, float yTouch) {
        float x = xTouch - radius;
        float y = yTouch - radius;
        float angle = (float) (Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
        int quadrant = getQuadrant(xTouch, yTouch);
        if (quadrant == 1) {
            return 360 - Math.abs(angle);
        } else if (quadrant == 2) {
            return 180 + Math.abs(angle);
        } else if (quadrant == 3) {
            return 180 - Math.abs(angle);
        } else if (quadrant == 4) {
            return Math.abs(angle);
        }
        return angle;
    }

    /**
     * 根据当前位置计算象限
     */
    private int getQuadrant(float x, float y) {
        int tmpX = (int) (x - radius);
        int tmpY = (int) (y - radius);
        if (tmpX >= 0) {
            return tmpY >= 0 ? 4 : 1;
        } else {
            return tmpY >= 0 ? 3 : 2;
        }
    }

    private void startAnim(float startValue, float endValue) {
        UIHelper.showLog(this, "startAnim startValue:" + startValue + ",endValue:" + endValue);
        startValue = startValue % 360;
        endValue = endValue % 360;
        if (Math.abs(Math.abs(endValue) - Math.abs(startValue)) > 180) {
            if (startValue > 180) {
                startValue = 180 - startValue;
            } else if (startValue < -180) {
                startValue = 180 + startValue;
            }
            if (endValue > 180) {
                endValue = 180 - endValue;
            } else if (endValue < -180) {
                endValue = 180 + endValue;
            }
        }
        scrollAnimator = ValueAnimator.ofFloat(startValue, endValue);
        scrollAnimator.setDuration(200);
        final float finalEndValue = endValue;
        scrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rotateAngle = (float) animation.getAnimatedValue();
                // 重新布局
                invalidate();
                if (rotateAngle == finalEndValue) {
                    int position = (int) (rotateAngle / singleAngle);
                    if (position < 0) {
                        position = -position;
                    } else if (position > 0) {
                        position = circleAllItemSize - position;
                    }
                    if (currentPosition != position) {
                        currentPosition = position;
                        if (onItemChangeListener != null) {
                            onItemChangeListener.onListen(position);
                        }
                    }
                    UIHelper.showLog("CircleLotteryView1", "position:" + position + ",rotateAngle:" + rotateAngle);
                }
            }
        });
        scrollAnimator.start();
    }

//    /**
//     * 惯性滚动
//     */
//    private class AngleRunnable implements Runnable {
//
//        private float angelPerSecond;
//
//        public AngleRunnable(float velocity) {
//            this.angelPerSecond = velocity;
//        }
//
//        public void run() {
//            //小于20停止
//            if ((int) Math.abs(angelPerSecond) < 20) {
//                isMove = false;
//                return;
//            }
//            isMove = true;
//            // 滚动时候不断修改滚动角度大小
//            mStartAngle += (angelPerSecond / 30);
//            //逐渐减小这个值
//            angelPerSecond /= 1.0666F;
//            postDelayed(this, 30);
//            // 重新布局
//            getCheck();
//        }
//    }
}
