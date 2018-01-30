package com.jiujie.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.jiujie.base.R;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by ChenJiaLiang on 2017/9/14.
 * Email:576507648@qq.com
 */

public class RotateImageView extends ImageView{

    private int singleTime;
    private int num;
    private boolean isShun;
    private float singleRotate;
    private boolean isEnable = true;

//    private Timer timer;
//    private TimerTask timerTask;

    public RotateImageView(Context context) {
        super(context);
        init(context,null);
    }

    public RotateImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public RotateImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public void setNum(int num) {
        this.num = num;
        singleRotate = 360*1.0f/ num;
    }

    public void setShun(boolean shun) {
        isShun = shun;
    }

    public void setSingleTime(int singleTime) {
        this.singleTime = singleTime;
    }

    public void start(){
        isEnable = true;
//        stop();
//        timer = new Timer(true);
//        timerTask = new TimerTask() {
//            float currentRotate = 0;
//            @Override
//            public void run() {
//                if(isShun){
//                    currentRotate+= singleRotate;
//                    if(currentRotate>=360){
//                        currentRotate = 0;
//                    }
//                }else{
//                    currentRotate-= singleRotate;
//                    if(currentRotate<=-360){
//                        currentRotate = 0;
//                    }
//                }
//                setRotation(currentRotate);
//            }
//        };
//        timer.schedule(timerTask,singleTime,singleTime);

        postDelayed(new Runnable() {
            float currentRotate = 0;
            @Override
            public void run() {
                if(!isEnable)return;
                if(isShun){
                    currentRotate+= singleRotate;
                    if(currentRotate>=360){
                        currentRotate = 0;
                    }
                }else{
                    currentRotate-= singleRotate;
                    if(currentRotate<=-360){
                        currentRotate = 0;
                    }
                }
                setRotation(currentRotate);
                postDelayed(this, singleTime);
            }
        }, singleTime);
    }

    public void stop(){
        isEnable = false;
//        if(timerTask!=null){
//            timerTask.cancel();
//            timerTask = null;
//        }
//        if(timer!=null){
//            timer.cancel();
//            timer = null;
//        }
    }

    private void init(Context context, AttributeSet attrs){
        if(attrs!=null){
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RotateImageView);
            singleTime = a.getInt(R.styleable.RotateImageView_r_singleTime, 0);
            num = a.getInt(R.styleable.RotateImageView_r_num, 0);
            isShun = a.getBoolean(R.styleable.RotateImageView_r_isShun, true);

            a.recycle();

            if(singleTime >0&& num >0){
                singleRotate = 360*1.0f/ num;
                start();
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setPivotX(w/2);
        setPivotY(h/2);//支点在图片中心
    }
}
