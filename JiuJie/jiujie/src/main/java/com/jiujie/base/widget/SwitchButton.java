package com.jiujie.base.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.Checkable;

import com.jiujie.base.R;
import com.jiujie.base.jk.OnCheckedChangeListener;
import com.jiujie.base.util.UIHelper;

/**
 * @author : Created by ChenJiaLiang on 2017/3/29.
 *         Email : 576507648@qq.com
 */
public class SwitchButton extends View implements Checkable {

    private int colorCheck = Color.RED;
    private int colorUnCheck = Color.parseColor("#e5e5e5");
    private int circleColor = Color.WHITE;
    private int mainHeight;
    private int circleHeight;
    private boolean checked = false;
    private float moveX;
    private int height, width;
    private Paint mPaint;
    private RectF leftRectF;
    private RectF rightRectF;
    private float downX;
    private float rectFR;
    private float disparity;
    private int maxMoveX;
    private int minMoveX;
    private float finalDownX;
    private int mTouchSlop;
    private float finalDownY;

    public SwitchButton(Context context) {
        super(context);
        init(context, null);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setClickable(true);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.SwitchButton);
            mainHeight = a.getDimensionPixelSize(R.styleable.SwitchButton_mainHeight, mainHeight);
            circleHeight = a.getDimensionPixelSize(R.styleable.SwitchButton_circleHeight, circleHeight);
            circleColor = a.getColor(R.styleable.SwitchButton_circleColor, circleColor);
            colorCheck = a.getColor(R.styleable.SwitchButton_colorCheck, colorCheck);
            colorUnCheck = a.getColor(R.styleable.SwitchButton_colorUnCheck, colorUnCheck);
            checked = a.getBoolean(R.styleable.SwitchButton_checked, checked);

            width = a.getLayoutDimension(R.styleable.SwitchButton_android_layout_width, -2);
            height = a.getLayoutDimension(R.styleable.SwitchButton_android_layout_height, -2);

            if (width < 0) {
                width = UIHelper.dip2px(context,48);
            }
            if (height < 0) {
                height = UIHelper.dip2px(context,30);
            }
            if(mainHeight<=0){
                mainHeight = height;
            }
            if(circleHeight<=0){
                circleHeight = mainHeight;
            }

            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int paddingLeft = 0;
        int paddingRight = 0;
        int paddingTop = 0;
//        int paddingLeft = getPaddingLeft();
//        int paddingRight = getPaddingRight();
//        int paddingTop = getPaddingTop();
        if (height == 0 || mPaint == null) {
            height = getHeight();
            width = getWidth();
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            if (mainHeight > height) {
                mainHeight = height;
            }
            if (circleHeight > height) {
                circleHeight = height;
            }
            int dp1 = UIHelper.dip2px(getContext(), 1);//留1dp来显示点间距
            maxMoveX = width / 2 - circleHeight / 2 - dp1;
            minMoveX = circleHeight / 2 - width / 2 + dp1;

            if (checked) {
                moveX = maxMoveX;
            } else {
                moveX = minMoveX;
            }

            rectFR = (float) mainHeight / 2;//左右半圆的半径
            disparity = (float) (height - mainHeight) / 2;
            //左右半圆+1，-1是为了消除可能性计算误差导致有的分辨率上半圆和矩形之间有条空白间隔像素
            leftRectF = new RectF(paddingLeft, paddingTop + disparity, paddingLeft + mainHeight + 1, paddingTop + disparity + mainHeight);
            rightRectF = new RectF(width - paddingRight - mainHeight - 1, paddingTop + disparity, width - paddingRight, paddingTop + disparity + mainHeight);
        }
        mPaint.setStyle(Paint.Style.FILL);

        if (moveX > maxMoveX) {
            moveX = maxMoveX;
        } else if (moveX < minMoveX) {
            moveX = minMoveX;
        }
        if (moveX == maxMoveX) {
            mPaint.setColor(colorCheck);
            canvas.drawArc(leftRectF, 90, 180, false, mPaint);
            canvas.drawArc(rightRectF, -90, 180, false, mPaint);
            canvas.drawRect(paddingLeft + rectFR, paddingTop + disparity, width - paddingRight - rectFR, paddingTop + disparity + mainHeight, mPaint);
        } else if (moveX == minMoveX) {
            mPaint.setColor(colorUnCheck);
            canvas.drawArc(leftRectF, 90, 180, false, mPaint);
            canvas.drawArc(rightRectF, -90, 180, false, mPaint);
            canvas.drawRect(paddingLeft + rectFR, paddingTop + disparity, width - paddingRight - rectFR, paddingTop + disparity + mainHeight, mPaint);
        } else {
            mPaint.setColor(colorCheck);
            canvas.drawArc(leftRectF, 90, 180, false, mPaint);
            mPaint.setColor(colorUnCheck);
            canvas.drawArc(rightRectF, -90, 180, false, mPaint);

            mPaint.setColor(colorCheck);
            canvas.drawRect(paddingLeft + rectFR, paddingTop + disparity, width / 2 + moveX, paddingTop + disparity + mainHeight, mPaint);
            mPaint.setColor(colorUnCheck);
            canvas.drawRect(width / 2 + moveX, paddingTop + disparity, width - paddingRight - rectFR, paddingTop + disparity + mainHeight, mPaint);
        }

        mPaint.setColor(circleColor);
        canvas.drawCircle(width / 2 + moveX, height / 2, circleHeight / 2, mPaint);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                finalDownX = downX;
                finalDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                doMove(event);
                break;
            case MotionEvent.ACTION_UP:
                float upX = event.getX();
                if (upX == finalDownX && event.getY() - finalDownY < mTouchSlop) {
                    doClick();
                } else {
                    if (moveX >= 0) {
                        if (!checked) {
                            changeCheck();
                        }
                        doAnim((int) moveX, maxMoveX);
                    } else {
                        if (checked) {
                            changeCheck();
                        }
                        doAnim((int) moveX, minMoveX);
                    }
                }
                break;
        }
        return true;
//        return isEnabled();
//        return super.onTouchEvent(event);
    }

    private boolean isShouldReDraw = true;
    private void doMove(MotionEvent event) {
        float currentX = event.getX();
        moveX = (checked?maxMoveX:minMoveX) + currentX - downX;
        if(moveX>minMoveX&&moveX<maxMoveX){
            isShouldReDraw = true;
        }else{
            if(isShouldReDraw){
                if(moveX<=minMoveX){
                    isShouldReDraw = false;
                    invalidate();
                }else if(moveX>=maxMoveX){
                    isShouldReDraw = false;
                    invalidate();
                }
            }
        }
        if(isShouldReDraw)invalidate();
    }

    public void setChecked(boolean checked) {
        if (this.checked == checked) {
            return;
        }
        this.checked = checked;
        if (checked) {
            moveX = maxMoveX;
        } else {
            moveX = minMoveX;
        }
        invalidate();
    }

    public void toggle() {
        setChecked(!checked);
    }

    public boolean isChecked() {
        return checked;
    }

    private OnCheckedChangeListener onCheckChangeListen;

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckChangeListen) {
        this.onCheckChangeListen = onCheckChangeListen;
    }

    private void doClick() {
        if (checked) {
            doAnim((int) moveX, minMoveX);
        } else {
            doAnim((int) moveX, maxMoveX);
        }
        changeCheck();
    }

    private void changeCheck() {
        checked = !checked;
        if (onCheckChangeListen != null) onCheckChangeListen.onCheckedChanged(this,this, checked);
    }

    private void doAnim(int from, int to) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(from, to);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                moveX = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setDuration(200);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }
}