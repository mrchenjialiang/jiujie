package com.jiujie.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.StateSet;
import android.widget.ImageView;

import com.jiujie.base.R;

/**
 * author : Created by ChenJiaLiang on 2016/8/18.
 * Email : 576507648@qq.com
 */
public class SelectorImage extends ImageView{
    public SelectorImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    public SelectorImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }
    public SelectorImage(Context context) {
        super(context);
    }

    private StateListDrawable src_drawable;
    private StateListDrawable background_drawable;
    
    class MyStateListDrawable extends StateListDrawable{
        @Override
        protected boolean onStateChange(int[] stateSet) {
            boolean stateChange = super.onStateChange(stateSet);
            if(stateChange){
                boolean isPress = false;
                for (int state : stateSet){
                    if(state==android.R.attr.state_pressed){
                        isPress = true;
                        break;
                    }
                }
//                if(isPress){
//                    setTextS
//                }
            }
            return stateChange;
        }
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.SelectorImage);
            Drawable background_n = a.getDrawable(R.styleable.SelectorImage_background_n);
            Drawable background_p = a.getDrawable(R.styleable.SelectorImage_background_p);
            Drawable src_n = a.getDrawable(R.styleable.SelectorImage_src_n);
            Drawable src_p = a.getDrawable(R.styleable.SelectorImage_src_p);
            setImageSelector(src_n, src_p);
            setBackGroundSelector(background_n, background_p);
            a.recycle();
        }

    }

    public void setBackGroundSelector(Drawable normalDrawer,Drawable pressDrawer) {
        if(normalDrawer==null&&pressDrawer==null){
            return;
        }
        if(background_drawable==null)background_drawable = new MyStateListDrawable();
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

    public void setImageSelector(Drawable normalDrawer,Drawable pressDrawer){
        if(normalDrawer==null&&pressDrawer==null){
            return;
        }
        if(src_drawable==null)src_drawable = new MyStateListDrawable();
        src_drawable.addState(new int[]{-android.R.attr.state_focused, -android.R.attr.state_selected, -android.R.attr.state_pressed},
                normalDrawer);
        src_drawable.addState(new int[]{android.R.attr.state_focused},
                pressDrawer);
        src_drawable.addState(new int[]{android.R.attr.state_selected},
                pressDrawer);
        src_drawable.addState(new int[]{android.R.attr.state_pressed},
                pressDrawer);
        setImageDrawable(src_drawable);
    }

    public void setImageSelector(int normalId, int pressId) {
        if(normalId==0&&pressId==0){
            return;
        }
        if(src_drawable==null)src_drawable = new MyStateListDrawable();
        src_drawable.addState(new int[]{-android.R.attr.state_focused, -android.R.attr.state_selected, -android.R.attr.state_pressed},
                getResources().getDrawable(normalId));
        src_drawable.addState(new int[]{android.R.attr.state_focused},
                getResources().getDrawable(pressId));
        src_drawable.addState(new int[]{android.R.attr.state_selected},
                getResources().getDrawable(pressId));
        src_drawable.addState(new int[]{android.R.attr.state_pressed},
                getResources().getDrawable(pressId));
        setImageDrawable(src_drawable);
    }

    public void setBackGroundSelector(int normalId, int pressId) {
        if(normalId==0&&pressId==0){
            return;
        }
        if(background_drawable==null)background_drawable = new MyStateListDrawable();
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
