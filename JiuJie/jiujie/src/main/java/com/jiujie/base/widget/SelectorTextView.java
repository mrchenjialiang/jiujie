package com.jiujie.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.jiujie.base.R;


//                    常用的字体风格名称还有：
//                    * Typeface.BOLD //粗体
//                            * Typeface.BOLD_ITALIC //粗斜体
//                            * Typeface.ITALIC //斜体
//                            * Typeface.NORMAL //常规
//
//                    但是有时上面那些设置在绘图过程中是不起作用的，所以还有如下设置方式：
//                    Paint mp = new Paint();
//                    mp.setFakeBoldText(true); //true为粗体，false为非粗体
//                    mp.setTextSkewX(-0.5f); //float类型参数，负数表示右斜，整数左斜
//                    mp.setUnderlineText(true); //true为下划线，false为非下划线
//                    mp.setStrikeThruText(true); //true为删除线，false为非删除线

/**
 * author : Created by ChenJiaLiang on 2016/8/18.
 * Email : 576507648@qq.com
 */
public class SelectorTextView extends TextView{

    int textStyle_n = -1;
    int textStyle_p = -1;
    int textSize_n = 15;
    int textSize_p = 15;
    int textColor_n = 15;
    int textColor_p = 15;

    public SelectorTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    public SelectorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }
    public SelectorTextView(Context context) {
        super(context);
    }

    private StateListDrawable background_drawable = new MyStateListDrawable();

    class MyStateListDrawable extends StateListDrawable{
        @Override
        protected boolean onStateChange(int[] stateSet) {
            boolean isPress = false;
            for (int state : stateSet){
                if(state==android.R.attr.state_pressed||state==android.R.attr.state_selected){
                    isPress = true;
                    break;
                }
            }
            if(isPress){
                setTextSize(textSize_p);
                setTextColor(textColor_p);
                TextPaint paint = getPaint();
                if(textStyle_p==0){
                    paint.setFakeBoldText(false);
                    paint.setTextSkewX(0);
                }else if(textStyle_p==1)
                    paint.setFakeBoldText(true);
                else if(textStyle_p==2)
                    paint.setTextSkewX(-0.5f);
            }else{
                setTextSize(textSize_n);
                setTextColor(textColor_n);
                TextPaint paint = getPaint();
                if(textStyle_n==0){
                    paint.setFakeBoldText(false);
                    paint.setTextSkewX(0);
                }else if(textStyle_n==1)
                    paint.setFakeBoldText(true);
                else if(textStyle_n==2)
                    paint.setTextSkewX(-0.5f);
            }
            return super.onStateChange(stateSet);
        }
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.SelectorTextView);

            textStyle_n = a.getInt(R.styleable.SelectorTextView_textStyle_n,textStyle_n);
            textStyle_p = a.getInt(R.styleable.SelectorTextView_textStyle_p, textStyle_p);

            textSize_n = a.getDimensionPixelSize(R.styleable.SelectorTextView_textSize_n, textSize_n);
            textSize_p = a.getDimensionPixelSize(R.styleable.SelectorTextView_textSize_p, textSize_p);

            textColor_n = a.getColor(R.styleable.SelectorTextView_textColor_n, textColor_n);
            textColor_p = a.getColor(R.styleable.SelectorTextView_textColor_p,textColor_p);

            Drawable background_n = a.getDrawable(R.styleable.SelectorTextView_background_n);
            Drawable background_p = a.getDrawable(R.styleable.SelectorTextView_background_p);
            setBackGroundSelector(background_n,background_p);

            a.recycle();
        }
    }

    public void setBackGroundSelector(Drawable normalDrawer,Drawable pressDrawer) {
//        if(normalDrawer==null&&pressDrawer==null){
//            return;
//        }
        background_drawable.addState(new int[]{-android.R.attr.state_focused, -android.R.attr.state_selected, -android.R.attr.state_pressed},
                normalDrawer);
        background_drawable.addState(new int[]{android.R.attr.state_focused,},
                pressDrawer);
        background_drawable.addState(new int[]{android.R.attr.state_selected},
                pressDrawer);
        background_drawable.addState(new int[]{android.R.attr.state_pressed},
                pressDrawer);
        setBackground(background_drawable);
    }

    public void setBackGroundSelector(int normalId, int pressId) {
        if(normalId==0&&pressId==0){
            return;
        }
        background_drawable.addState(new int[]{-android.R.attr.state_focused, -android.R.attr.state_selected, -android.R.attr.state_pressed},
                getResources().getDrawable(normalId));
        background_drawable.addState(new int[]{android.R.attr.state_focused,},
                getResources().getDrawable(pressId));
        background_drawable.addState(new int[]{android.R.attr.state_selected},
                getResources().getDrawable(pressId));
        background_drawable.addState(new int[]{android.R.attr.state_pressed},
                getResources().getDrawable(pressId));
        setBackground(background_drawable);
    }
}
