package com.jiujie.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Checkable;

import com.jiujie.base.R;
import com.jiujie.base.jk.OnCheckedChangeListener;


/**
 * Created by ChenJiaLiang on 2017/6/21.
 * Email:576507648@qq.com
 */

public class JJSimpleButton extends View implements Checkable {


    private int bgColor_n;
    private int bgColor_p;
    private int lineColor_n;
    private int lineColor_p;
    private int textColor_n;
    private int textColor_p;
    private float conner;
    private float lineWidth = 2;

    private Paint mPaint;
    private int height;
    private int width;
    private RectF rectf;
    private boolean isPress;
    private boolean checkEnable;
    private boolean isChecked;
    private float downX;
    private float downY;
    private int mTouchSlop;
    private OnClickListener onClickListener;
    private String text;
    private float textSize;
    private float textWidth;
    private Paint.FontMetricsInt mFontMetricsInt;
    private int resetHeight;

    public JJSimpleButton(Context context) {
        super(context);
        init(context,null);
    }

    public JJSimpleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public JJSimpleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        if(attrs!=null){
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.JJSimpleButton);

            //获取自定义属性和默认值
            bgColor_n = a.getColor(R.styleable.JJSimpleButton_bgColor_n, getBgColor_n());
            bgColor_p = a.getColor(R.styleable.JJSimpleButton_bgColor_p, getBgColor_p());
            lineColor_n = a.getColor(R.styleable.JJSimpleButton_lineColor_n, getLineColor_n());
            lineColor_p = a.getColor(R.styleable.JJSimpleButton_lineColor_p, getLineColor_p());
            textColor_n = a.getColor(R.styleable.JJSimpleButton_textColor_n, getTextColor_n());
            textColor_p = a.getColor(R.styleable.JJSimpleButton_textColor_p, getTextColor_p());
            conner = a.getDimension(R.styleable.JJSimpleButton_conner, getConner());
            lineWidth = a.getDimension(R.styleable.JJSimpleButton_lineWidth, getLineWidth());
            checkEnable = a.getBoolean(R.styleable.JJSimpleButton_checkEnable,getCheckEnable());
            if(checkEnable){
                isChecked = a.getBoolean(R.styleable.JJSimpleButton_isChecked,isChecked());
            }
            text = a.getString(R.styleable.JJSimpleButton_text);
            textSize = a.getDimension(R.styleable.JJSimpleButton_textSize, getTextSize());
            textWidth = a.getDimension(R.styleable.JJSimpleButton_textWidth, getTextWidth());

            a.recycle();
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(textSize);
        mPaint.setStrokeWidth(textWidth);
        mFontMetricsInt = mPaint.getFontMetricsInt();
        setClickable(true);
    }

    @Override
    public void setChecked(boolean checked) {
        if(isChecked==checked){
            return;
        }
        onCheckChange();
        invalidate();
    }

    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        onCheckChange();
    }

    public float getTextWidth() {
        return textWidth;
    }

    public String getText() {
        return text;
    }

    public float getTextSize() {
        return textSize;
    }

    public boolean getCheckEnable() {
        return checkEnable;
    }

    public int getBgColor_n() {
        return bgColor_n;
    }

    public int getBgColor_p() {
        return bgColor_p;
    }

    public int getLineColor_n() {
        return lineColor_n;
    }

    public int getLineColor_p() {
        return lineColor_p;
    }

    public int getTextColor_n() {
        return textColor_n;
    }

    public int getTextColor_p() {
        return textColor_p;
    }

    public float getConner() {
        return conner;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public JJSimpleButton setTextWidth(float textWidth) {
        this.textWidth = textWidth;
        return this;
    }

    public JJSimpleButton setTextSize(float textSize) {
        this.textSize = textSize;
        return this;
    }

    public JJSimpleButton setText(String text) {
        this.text = text;
        return this;
    }

    public JJSimpleButton setCheckEnable(boolean checkEnable) {
        this.checkEnable = checkEnable;
        return this;
    }

    public JJSimpleButton setBgColor_n(int bgColor_n) {
        this.bgColor_n = bgColor_n;
        return this;
    }

    public JJSimpleButton setBgColor_p(int bgColor_p) {
        this.bgColor_p = bgColor_p;
        return this;
    }

    public JJSimpleButton setLineColor_n(int lineColor_n) {
        this.lineColor_n = lineColor_n;
        return this;
    }

    public JJSimpleButton setLineColor_p(int lineColor_p) {
        this.lineColor_p = lineColor_p;
        return this;
    }

    public JJSimpleButton setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    public JJSimpleButton setConner(float conner) {
        this.conner = conner;
        return this;
    }

    public JJSimpleButton setTextColor_p(int textColor_p) {
        this.textColor_p = textColor_p;
        return this;
    }

    public JJSimpleButton setTextColor_n(int textColor_n) {
        this.textColor_n = textColor_n;
        return this;
    }

    public void refresh(){
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        boolean isStatusP = isChecked||isPress||isSelected();
        mPaint.setStyle(Paint.Style.FILL);//充满
        mPaint.setColor(getBgColor(isStatusP));
        if(conner>0){
            canvas.drawRoundRect(rectf, conner, conner, mPaint);//第二个参数是x半径，第三个参数是y半径

            int lineColor = getLineColor(isStatusP);
            if(lineColor!=0){
                mPaint.setColor(lineColor);
                mPaint.setStrokeWidth(lineWidth);
                mPaint.setStyle(Paint.Style.STROKE);//不充满
                canvas.drawRoundRect(rectf, conner, conner, mPaint);//第二个参数是x半径，第三个参数是y半径
            }
        }else{
//            canvas.drawRect(0, 0, width, height, mPaint);// 长方形
            canvas.drawRect(1, 1, width-1, height-1, mPaint);// 长方形

            int lineColor = getLineColor(isStatusP);
            if(lineColor!=0){
                mPaint.setColor(lineColor);
                mPaint.setStrokeWidth(lineWidth);
                mPaint.setStyle(Paint.Style.STROKE);//不充满
//                canvas.drawRect(0, 0, width, height, mPaint);// 长方形
                canvas.drawRect(1, 1, width-1, height-1, mPaint);// 长方形
            }
        }
        if(!TextUtils.isEmpty(text)){
            int textColor = getTextColor(isStatusP);
            mPaint.setStyle(Paint.Style.FILL);//充满
            mPaint.setColor(textColor);
            mPaint.setStrokeWidth(textWidth);
            mPaint.setTextAlign(Paint.Align.CENTER);

            drawText(canvas,text,width/2,height/2,mPaint);
        }

//        super.onDraw(canvas);
    }

    /**
     * x,y
     */
    private void drawText(Canvas canvas, String text, float x, float y, Paint mPaint) {
        y = y - (mFontMetricsInt.ascent + mFontMetricsInt.descent) / 2;
        canvas.drawText(text, x, y, mPaint);
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private OnCheckedChangeListener onCheckedChangeListener;

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    private void onCheckChange(){
        if(checkEnable){
            isChecked = !isChecked;
            if(onCheckedChangeListener!=null)onCheckedChangeListener.onCheckedChanged(this,this,isChecked);
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        int action = event.getAction();
        boolean thisIsPress = false;
        if(action == MotionEvent.ACTION_UP||action == MotionEvent.ACTION_CANCEL||action == MotionEvent.ACTION_MOVE){
            thisIsPress = false;
        }else{
            if(touchX>=0&&touchX<=width&&touchY>=0&&touchY<=height){
                thisIsPress = true;
            }
        }
        if(isPress!=thisIsPress){
            isPress = thisIsPress;
            invalidate();
        }
        switch (action){
            case MotionEvent.ACTION_DOWN:
                downX = touchX;
                downY = touchY;
                break;
            case MotionEvent.ACTION_UP:
                if(Math.abs(event.getX()-downX)<mTouchSlop&&Math.abs(event.getY()-downY)<mTouchSlop){
                    if(onClickListener!=null){
                        onClickListener.onClick(this);
                    }
                    if(checkEnable){
                        onCheckChange();
                    }
                }
                break;
        }
        return true;
    }

    private int getBgColor(boolean isStatusP) {
        if(isStatusP){
            if(bgColor_p==0){
                if(bgColor_n==0){
                    return Color.WHITE;
                }
                return bgColor_n;
            }
            return bgColor_p;
        }else{
            if(bgColor_n==0){
                return Color.WHITE;
            }
            return bgColor_n;
        }
    }

    private int getTextColor(boolean isStatusP) {
        if(isStatusP){
            if(textColor_p==0){
                if(textColor_n==0){
                    return Color.BLACK;
                }
                return textColor_n;
            }
            return textColor_p;
        }else{
            if(textColor_n==0){
                return Color.BLACK;
            }
            return textColor_n;
        }
    }

    private int getLineColor(boolean isStatusP) {
        if(isStatusP){
            if(lineColor_p==0){
                if(lineColor_n==0){
                    return 0;
                }
                return lineColor_n;
            }
            return lineColor_p;
        }else{
            return lineColor_n;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
//        rectf = new RectF(0, 0, w, h);
        rectf = new RectF(1, 1, w-1, h-1);
    }

    public void setHeight(int height){
        this.resetHeight = height;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(resetHeight!=0){
            setMeasuredDimension(widthMeasureSpec,resetHeight);
        }
    }
}
