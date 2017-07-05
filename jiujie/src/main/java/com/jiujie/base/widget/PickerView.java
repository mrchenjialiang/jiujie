package com.jiujie.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jiujie.base.R;
import com.jiujie.base.jk.OnSelectListener;
import com.jiujie.base.util.UIHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Created by ChenJiaLiang on 2016/11/25.
 *         Email : 576507648@qq.com
 */
public class PickerView extends View {

    private Paint mPaint;
    private int mCenterTextColor = Color.RED;
    private int mOtherTextColor = Color.BLACK;
    private boolean isInfinite = false;//是否无限滚动
    private List<String> mDataList;
    private int currentPosition;
    private float moveY;
    private int mViewHeight;
    private int mViewWidth;
    private int mCenterTextTextSize = 40;
    private float JG = 8;
    private OnSelectListener mSelectListener;

    public PickerView(Context context) {
        super(context);
        init(context,null);
    }

    public PickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public PickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.PickerView);

            mCenterTextTextSize = UIHelper.sp2px(context,16);
            mCenterTextTextSize = a.getDimensionPixelSize(R.styleable.PickerView_textSize, mCenterTextTextSize);

            mCenterTextColor = a.getColor(R.styleable.PickerView_textColor_center, mCenterTextColor);
            mOtherTextColor = a.getColor(R.styleable.PickerView_textColor_other,mOtherTextColor);
            isInfinite = a.getBoolean(R.styleable.PickerView_isInfinite,isInfinite);

            a.recycle();
        }
        if(mDataList==null)mDataList = new ArrayList<>();
        for (int i=2000;i<2017;i++){
            mDataList.add(String.format("%d测试", i));
        }
    }

    public void setData(List<String> data, int currentPosition) {
        this.mDataList = data;
        this.currentPosition = currentPosition;
        invalidate();
        if (mSelectListener != null && selectedPosition != currentPosition) {
            mSelectListener.onSelect(currentPosition);
            selectedPosition = currentPosition;
        }
    }

    public void setData(List<String> data) {
        this.mDataList = data;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();
//        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDataList == null || mDataList.size() == 0) {
            return;
        }
        if (currentPosition < 0) {
            currentPosition = 0;
        }
        if (currentPosition > mDataList.size() - 1) {
            currentPosition = mDataList.size() - 1;
        }

        String currentText = mDataList.get(currentPosition);
        // text居中绘制，注意baseline的计算才能达到居中，y值是text中心坐标
        float x = (float) mViewWidth / 2;
        float centerTextY = (float) mViewHeight / 2 + moveY;
        Paint.FontMetricsInt fmi = mPaint.getFontMetricsInt();
        centerTextY = (float) (centerTextY - (fmi.bottom / 2.0 + fmi.top / 2.0));

        final float finalCenterY = (float) mViewHeight / 2 - (fmi.bottom / 2.0f + fmi.top / 2.0f);

        float textSize = mCenterTextTextSize * (1 - Math.abs(centerTextY - finalCenterY)/finalCenterY/2);
        mPaint.setTextSize(textSize);
        mPaint.setColor(mCenterTextColor);
        canvas.drawText(currentText, x, centerTextY, mPaint);

        float y;
        mPaint.setColor(mOtherTextColor);
        if (!isInfinite) {
            //不是循环的话
            //上
            y = centerTextY;
            if (currentPosition > 0) {
                int position = currentPosition;
                float alpha = 255;
                while (y > 0 && position > 0) {
                    y = y - mCenterTextTextSize - JG;
                    position--;
                    textSize = mCenterTextTextSize * (1 - Math.abs(y - finalCenterY)/finalCenterY/2);
                    alpha = alpha * (1 - Math.abs(y - finalCenterY)/finalCenterY/2);
                    mPaint.setTextSize(textSize);
                    mPaint.setAlpha((int) alpha);
                    String text = mDataList.get(position);
                    canvas.drawText(text, x, y, mPaint);
                }
            }
            //下
            y = centerTextY;
            if (currentPosition < mDataList.size() - 1) {
                int position = currentPosition;
                float alpha = 255;
                while (y < mViewHeight && position < mDataList.size() - 1) {
                    y = y + mCenterTextTextSize + JG;
                    position++;
                    textSize = mCenterTextTextSize * (1 - Math.abs(y - finalCenterY)/finalCenterY/2);
                    alpha = alpha * (1 - Math.abs(y - finalCenterY)/finalCenterY/2);
                    mPaint.setTextSize(textSize);
                    mPaint.setAlpha((int) alpha);
                    String text = mDataList.get(position);
                    canvas.drawText(text, x, y, mPaint);
                }
            }
        } else {
            //无限循环
            //上
            y = centerTextY;
            int position = currentPosition;
            float alpha = 255;
            while (y > 0) {
                y = y - mCenterTextTextSize - JG;
                position--;
                if (position < 0) {
                    position = mDataList.size() - 1;
                }
                textSize = mCenterTextTextSize * (1 - Math.abs(y - finalCenterY)/finalCenterY/2);
                alpha = alpha * (1 - Math.abs(y - finalCenterY)/finalCenterY/2);
                mPaint.setTextSize(textSize);
                mPaint.setAlpha((int) alpha);
                String text = mDataList.get(position);
                canvas.drawText(text, x, y, mPaint);
            }
            //下
            y = centerTextY;
            position = currentPosition;
            alpha = 255;
            while (y < mViewHeight) {
                y = y + mCenterTextTextSize + JG;
                position++;
                if (position > mDataList.size() - 1) {
                    position = 0;
                }
                textSize = mCenterTextTextSize * (1 - Math.abs(y - finalCenterY)/finalCenterY/2);
                alpha = alpha * (1 - Math.abs(y - finalCenterY)/finalCenterY/2);
                mPaint.setTextSize(textSize);
                mPaint.setAlpha((int) alpha);
                String text = mDataList.get(position);
                canvas.drawText(text, x, y, mPaint);
            }
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                doDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                doMove(event);
                break;
            case MotionEvent.ACTION_UP:
                doUp(event);
                break;
        }
        return true;
    }

    private float lastY;

    private void doDown(MotionEvent event) {
        lastY = event.getY();
    }

    private int selectedPosition;

    private void doMove(MotionEvent event) {
        //选中条目改变的时候，会闪一下不同的数据---BUG
        float currentY = event.getY();
        moveY = currentY - lastY;
        int passCount = (int) (moveY / (mCenterTextTextSize + JG));
        if (isInfinite) {
            if (passCount != 0) {
                currentPosition -= passCount;
                while (currentPosition < 0) currentPosition += mDataList.size();
                while (currentPosition > mDataList.size() - 1) currentPosition -= mDataList.size();
                lastY = currentY;
                moveY = moveY % (mCenterTextTextSize + JG);
                if (mSelectListener != null && selectedPosition != currentPosition) {
                    mSelectListener.onSelect(currentPosition);
                    selectedPosition = currentPosition;
                }
            }
        } else {
            if (moveY > 0 && currentPosition == 0) {
                invalidate();
                return;
            } else if (moveY < 0 && currentPosition == mDataList.size() - 1) {
                invalidate();
                return;
            } else {
                if (passCount != 0) {
                    currentPosition -= passCount;
                    if (currentPosition < 0) {
                        currentPosition = 0;
                    }
                    if (currentPosition > mDataList.size() - 1) {
                        currentPosition = mDataList.size() - 1;
                    }
                    lastY = currentY;
                    moveY = moveY % (mCenterTextTextSize + JG);
                    if (mSelectListener != null && selectedPosition != currentPosition) {
                        mSelectListener.onSelect(currentPosition);
                        selectedPosition = currentPosition;
                    }
                }
            }
        }
        invalidate();
    }

    private void doUp(MotionEvent event) {
        float currentY = event.getY();
        moveY = currentY - lastY;
        if (moveY != 0) {
            float a = Math.abs(moveY) / 20;//10间隔一次，a即为200ms内完成需要的每次移动距离
            if (a < 3) a = 3;
            final float singChangeY = a;
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Math.abs(moveY) < singChangeY) {
                        moveY = 0;
                        invalidate();
                        return;
                    }
                    if (moveY > 0) {
                        moveY -= singChangeY;
                    }
                    if (moveY < 0) {
                        moveY += singChangeY;
                    }
                    invalidate();
                    postDelayed(this, 10);
                }
            }, 0);
        }
    }

    public void setOnSelectListener(OnSelectListener listener) {
        mSelectListener = listener;
    }

}
