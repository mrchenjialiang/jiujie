package com.jiujie.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.jiujie.base.R;

import java.util.List;

/**
 * @author : Created by ChenJiaLiang on 2016/11/16.
 *         Email : 576507648@qq.com
 */
public class MarqueeTextView extends View{
    private List<CharSequence> dataList;
    private CharSequence currentText = "currentText";
    private int currentTextPosition;
//    private int mViewWidth;
    private int mViewHeight;
    private Paint mPaint;
    private int textColor = Color.BLACK;
    private float textSize = 14;

    public MarqueeTextView(Context context) {
        super(context);
        init();
    }
    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        init();
    }
    public MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        init();
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.MarqueeTextView);

            textColor = a.getColor(R.styleable.MarqueeTextView_textColorMarquee, textColor);
            textSize = a.getDimension(R.styleable.MarqueeTextView_textSizeMarquee, textSize);

            a.recycle();
        }
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(textColor);
        mPaint.setTextSize(textSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewHeight = getMeasuredHeight();
//        mViewWidth = getMeasuredWidth();
    }

    private boolean isStartMarquee;
    private boolean shouldStopMarquee;
    public void setDataList(List<CharSequence> dataList){
        this.dataList = dataList;
        if(dataList!=null){
            if(dataList.size()>0){
                currentText = dataList.get(0);
                invalidate();
            }
            if(dataList.size()>1&&!isStartMarquee){
                isStartMarquee = true;
                startMarquee();
            }else{
                shouldStopMarquee = true;
            }
        }else{
            if(isStartMarquee){
                shouldStopMarquee = true;
            }
        }
    }

    private void startMarquee(){
        final int delayMillis = 10;
        final float moveOneY = 3;
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if(shouldStopMarquee){
                    shouldStopMarquee = false;
                    isStartMarquee = false;
                    moveY = 0;
                    invalidate();
                    return;
                }
                moveY-= moveOneY;
                invalidate();
                if(moveY< -mViewHeight/2){
                    moveY = mViewHeight/2;
                    showNext();
                    postDelayed(this,delayMillis);
                }else if(Math.abs(moveY)< moveOneY){
                    moveY = 0;
                    invalidate();
                    postDelayed(this,3000);
                }else{
                    postDelayed(this,delayMillis);
                }
            }
        }, 3000);
    }

    private void showNext() {
        currentTextPosition++;
        if(currentTextPosition>dataList.size()-1){
            currentTextPosition = 0;
        }
        currentText = dataList.get(currentTextPosition);
    }

    private int moveY;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        float x = (float) (mViewWidth / 2.0);
//        float x = 0;

        float y = (float) (mViewHeight / 2.0 + moveY);
//        float textWidth = mPaint.measureText(currentText);
        Paint.FontMetricsInt fmi = mPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));
//        canvas.drawText(currentText, textWidth/2, baseline, mPaint);


//        canvas.drawText(currentText, getPaddingLeft(), y-textSize/2, mPaint);
//        CharSequence text = currentText;
//        canvas.drawText(text, getPaddingLeft(), y-textSize/2, mPaint);
        canvas.drawText(currentText,0,currentText.length(),getPaddingLeft(), baseline, mPaint);
//        canvas.drawText(currentText,0,currentText.length(),getPaddingLeft(), y-textSize/2, mPaint);

    }

    public int getCurrentPosition() {
        return currentTextPosition;
    }
}
