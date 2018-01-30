package com.jiujie.base.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.TextView;

import com.jiujie.base.jk.OnItemClickListen;


/**
 * Created by ChenJiaLiang on 2017/6/21.
 * Email:576507648@qq.com
 */

public class JJColorTextView extends TextView{

    private int height;
    private int width;
    private float downX;
    private float downY;
    private int mTouchSlop;
    private Paint.FontMetricsInt mFontMetricsInt;
    private TextPaint mPaint;

    public JJColorTextView(Context context) {
        super(context);
        init(context,null);
    }

    public JJColorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public JJColorTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
//        if(attrs!=null){
//            TypedArray a = activity.obtainStyledAttributes(attrs, R.styleable.JJSimpleButton);
//
//            //获取自定义属性和默认值
//            bgColor_n = a.getColor(R.styleable.JJSimpleButton_bgColor_n, getBgColor_n());
//
//            a.recycle();
//        }

        setClickable(true);
    }

    private String[] texts;
    private int[] colors;

    public void setData(String[] texts,int[] colors) {
        this.texts = texts;
        this.colors = colors;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(texts!=null&&colors!=null&&texts.length==colors.length){
            mPaint = getPaint();
            mFontMetricsInt = mPaint.getFontMetricsInt();
            float textSize = mPaint.getTextSize();
            mPaint.setTextAlign(Paint.Align.LEFT);
            int lineSpacing = mFontMetricsInt.leading;//行间距
            int canUseWidth = width - getPaddingLeft() - getPaddingRight();
            int x = getPaddingLeft();
            int y = (int) (getPaddingTop()+textSize/2);
            int leftWidth = canUseWidth;
            for (int i = 0;i<texts.length;i++){
                String text = texts[i];
                mPaint.setColor(colors[i]);

                float textWidth = mPaint.measureText(text);
                if(textWidth<leftWidth){
//                    canvas.drawText(text, x, y, mPaint);
                    drawText(canvas,text,x,y,mPaint);
                    x+=textWidth;
                    leftWidth-=textWidth;
                }else if(textWidth==leftWidth){
//                    canvas.drawText(text, x, y, mPaint);
                    drawText(canvas,text,x,y,mPaint);
                    x = getPaddingLeft();
                    y+=(textSize+lineSpacing);
                    leftWidth = canUseWidth;
                }else{
                    while (text.length()>0){
                        int textLength = mPaint.breakText(text, 0, text.length(), true, leftWidth, null);
                        String drawText = text.substring(0, textLength);
                        text = text.substring(textLength);
//                        canvas.drawText(drawText, x, y, mPaint);
                        drawText(canvas,drawText,x,y,mPaint);
                        x = getPaddingLeft();
                        y+=(textSize+lineSpacing);
                    }
                }
            }
        }else{
            super.onDraw(canvas);
        }
    }

    /**
     * x,y
     */
    private void drawText(Canvas canvas, String text, float x, float y, Paint mPaint) {
        y = y - (mFontMetricsInt.ascent + mFontMetricsInt.descent) / 2;
        canvas.drawText(text, x, y, mPaint);
    }

    private OnItemClickListen onItemClickListen;
    public void setOnItemClickListen(OnItemClickListen onItemClickListen) {
        this.onItemClickListen = onItemClickListen;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                downX = touchX;
                downY = touchY;
                break;
            case MotionEvent.ACTION_UP:
                if(Math.abs(event.getX()-downX)<mTouchSlop&&Math.abs(event.getY()-downY)<mTouchSlop){
                    if(onItemClickListen!=null){
                        int textIndex = getTextIndex(event.getX(), event.getY());
                        onItemClickListen.click(textIndex);
                    }
                }
                return false;
        }
        return true;
    }

    private int getTextIndex(float x, float y){
        String text = null;
        int canUseWidth = width - getPaddingLeft() - getPaddingRight();
        int lineSpacing = mFontMetricsInt.leading;//行间距

        int lineCount = (int) ((y-getPaddingTop())/(mPaint.getTextSize()+lineSpacing));
        x += (lineCount*canUseWidth);

        for (int i = 0;i<texts.length;i++){
            text+=texts[i];
            float textWidth = mPaint.measureText(text);
            if(textWidth>=x){
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        if(!TextUtils.isEmpty(mText)){
//            int height = getPaddingTop()+getPaddingBottom();
//            String text = mText;
//            while (text.length()>0) {
//                int textLength = mPaint.breakText(text, 0, text.length(), true, leftWidth, null);
//            }
//        }
//        if(resetHeight!=0){
//            setMeasuredDimension(widthMeasureSpec,resetHeight);
//        }
//    }
}
