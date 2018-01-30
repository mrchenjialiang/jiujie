package com.jiujie.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.jiujie.base.R;
import com.jiujie.base.util.ImageUtil;

/**
 * @author : Created by ChenJiaLiang on 2017/2/5.
 *         Email : 576507648@qq.com
 */
public class RatingBarCustom extends View{

    private int max = 5,num = 2,spacing = 8;
    private Paint mPaint;
    private Bitmap bitmapN;
    private Bitmap bitmapP;

    public RatingBarCustom(Context context) {
        super(context);
    }

    public RatingBarCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RatingBarCustom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setMax(int max) {
        this.max = max;
        requestLayout();
    }

    public void setNum(int num) {
        this.num = num;
        if(num>max){
            throw new NullPointerException("RatingBarCustom num should bigger than max");
        }
        invalidate();
    }

    public void setStarN(Bitmap bitmapN) {
        this.bitmapN = bitmapN;
        requestLayout();
    }

    public void setStarP(Bitmap bitmapP) {
        this.bitmapP = bitmapP;
        requestLayout();
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
        requestLayout();
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.RatingBarCustom);

            num = a.getInt(R.styleable.RatingBarCustom_rbc_num, num);
            max = a.getInt(R.styleable.RatingBarCustom_rbc_max, max);
            Drawable star_n = a.getDrawable(R.styleable.RatingBarCustom_rbc_star_n);
            Drawable star_p = a.getDrawable(R.styleable.RatingBarCustom_rbc_star_p);
            spacing = (int)a.getDimension(R.styleable.RatingBarCustom_rbc_spacing, spacing);

            if(star_n ==null|| star_p ==null){
                throw new NullPointerException("RatingBarCustom star_n and star_p should not be null");
            }
            if(num>max){
                throw new NullPointerException("RatingBarCustom num should bigger than max");
            }

            bitmapN = ImageUtil.instance().drawable2Bitmap(star_n);
            bitmapP = ImageUtil.instance().drawable2Bitmap(star_p);

            a.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float left = getPaddingLeft();
        float top = getPaddingTop();
        if(mPaint==null){
            mPaint = new Paint();
        }
        for (int i=1;i<=num;i++){
            canvas.drawBitmap(bitmapP, left,top, mPaint);
            left += (bitmapP.getWidth() + spacing);
        }
        for (int i=1;i<=max-num;i++){
            canvas.drawBitmap(bitmapN,left,top, mPaint);
            left += (bitmapN.getWidth() + spacing);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int starNWidth = bitmapN.getWidth();
        int starPWidth = bitmapP.getWidth();
        int width = starNWidth * (max - num) + starPWidth * num + spacing * (max - 1) + getPaddingLeft() + getPaddingRight();

        setMeasuredDimension(width, Math.max(bitmapN.getHeight(),bitmapP.getHeight())+getPaddingTop()+getPaddingBottom());
    }
}
