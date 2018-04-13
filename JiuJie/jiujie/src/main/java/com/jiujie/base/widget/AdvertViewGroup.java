package com.jiujie.base.widget;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.jiujie.base.R;
import com.jiujie.base.jk.MyHandlerInterface;
import com.jiujie.base.jk.OnItemClickListen;
import com.jiujie.base.jk.OnMyPageChangeListener;
import com.jiujie.base.jk.OnTouchListen;
import com.jiujie.base.util.MyHandler;
import com.jiujie.base.util.TaskManager;
import com.jiujie.base.util.UIHelper;
import com.jiujie.glide.GlideUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class AdvertViewGroup extends ViewGroup implements MyHandlerInterface {
    private List<String> dataList;
    private ImageView imageView1;
    private ImageView imageView2;
    private int moveX;
    private int mCurrentPosition;
    private Timer timer;
    private MyHandler myHandler;
    private TimerTask timerTask;
    private float downX;
    private Activity activity;
    private ValueAnimator valueAnimator;
    private int imageType;
    private boolean isAutoScroll = true;


    public AdvertViewGroup(Context context) {
        super(context);
        init(context);
    }

    public AdvertViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AdvertViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void init(Context context){
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setClickable(true);
        imageView1 = new ImageView(context);
        imageView2 = new ImageView(context);
        imageView1.setBackgroundColor(ContextCompat.getColor(context,R.color.gray));
        imageView2.setBackgroundColor(ContextCompat.getColor(context,R.color.gray));
        imageView1.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView2.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(imageView1,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        addView(imageView2,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
    }

    private ImageView mCurrentImageView;
    private ImageView mOtherImageView;
    private OnItemClickListen onItemClickListen;
    private OnMyPageChangeListener onMyPageChangeListener;
    public void setOnItemClickListen(OnItemClickListen onItemClickListen) {
        this.onItemClickListen = onItemClickListen;
    }

    public void setOnMyPageChangeListener(OnMyPageChangeListener onMyPageChangeListener) {
        this.onMyPageChangeListener = onMyPageChangeListener;
    }

    private ImageView getCurrentImageView(){
        if(mCurrentImageView==null){
            mCurrentImageView = imageView1;
        }
        return mCurrentImageView;
    }

    private ImageView getOtherImageView(){
        if(mOtherImageView==null){
            mOtherImageView = imageView2;
        }
        return mOtherImageView;
    }

    public void setAutoScroll(boolean autoScroll) {
        if(isAutoScroll!=autoScroll){
            isAutoScroll = autoScroll;
        }
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public void setCurrentPosition(int mCurrentPosition) {
        this.mCurrentPosition = mCurrentPosition;
        moveX = 0;
        setDataList(dataList);
//        ImageView linShi = getOtherImageView();
//        mOtherImageView = getCurrentImageView();
//        mCurrentImageView = linShi;
//        if(onMyPageChangeListener!=null){
//            onMyPageChangeListener.onPageSelected(mCurrentPosition);
//        }
//        requestLayout();
    }

    public void notifyDataSetChanged(){
        moveX = 0;
        setDataList(dataList);
        UIHelper.showLog("notifyDataSetChanged "+dataList.size());
    }

    public boolean isMoving(){
        return moveX!=0;
    }

    public void recycler(){
        imageView1 = null;
        imageView2 = null;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(dataList==null||dataList.size()==0){
            getCurrentImageView().layout(0,0,getWidth(),getHeight());
            return;
        }
        int left = -moveX,right = getWidth() - moveX,top = 0,bottom = getHeight();
        getCurrentImageView().layout(left,top,right,bottom);

        if(moveX>0){
            left = getWidth() - moveX;
            right = getWidth()*2 - moveX;
            getOtherImageView().layout(left,top,right,bottom);
        }else if(moveX<0){
            left = - moveX - getWidth();
            right = - moveX;
            getOtherImageView().layout(left,top,right,bottom);
        }else{
            getOtherImageView().layout(0,0,0,0);
        }
    }

    /**
     * @param imageType 0:centerCrop,1:fitXY
     */
    public void setImageType(int imageType){
        this.imageType = imageType;
//        imageView1.setScaleType(imageType==0?ImageView.ScaleType.CENTER_CROP: ImageView.ScaleType.FIT_XY);
//        imageView2.setScaleType(imageType==0?ImageView.ScaleType.CENTER_CROP: ImageView.ScaleType.FIT_XY);
    }

    public void setDataList(List<String> dataList){
        if(this.dataList==null||this.dataList!=dataList){
            this.dataList = dataList;
        }
        UIHelper.showLog(""+this.dataList);
        if(dataList==null||dataList.size()==0){
            return;
        }
        UIHelper.showLog(""+this.dataList.size());
        stopTime();
        if(dataList.size()==1){
            mCurrentPosition = 0;
            GlideUtil.instance().setDefaultImage(getContext(),dataList.get(mCurrentPosition),  getCurrentImageView(),R.drawable.trans,getWidth(),getHeight());
            requestLayout();
        }else{
            if(mCurrentPosition<0){
                mCurrentPosition = 0;
            }
            if(mCurrentPosition>dataList.size()-1){
                mCurrentPosition = dataList.size()-1;
            }
            GlideUtil.instance().setDefaultImage(getContext(),dataList.get(mCurrentPosition), getCurrentImageView(),R.drawable.trans,getWidth(),getHeight());
            prepareForLastBitmap();
            prepareForNextBitmap();
            moveX = 0;
            requestLayout();
            if(isAutoScroll){
                startTime();
            }
        }
        if(onMyPageChangeListener!=null){
            onMyPageChangeListener.onPageSelected(mCurrentPosition);
        }
    }

    private void prepareForLastBitmap(){
        new TaskManager<Drawable>() {
            @Override
            public Drawable runOnBackgroundThread() {
                int index = mCurrentPosition-1;
                if(index<0){
                    index = dataList.size()-1;
                }
                return GlideUtil.instance().getImageDrawable(getContext(),dataList.get(index),getWidth(),getHeight());
            }

            @Override
            public void runOnUIThread(Drawable bitmap) {

            }
        }.start();
    }

    private void prepareForNextBitmap(){
        new TaskManager<Drawable>() {
            @Override
            public Drawable runOnBackgroundThread() {
                int index = mCurrentPosition+1;
                if(index>dataList.size()-1){
                    index = 0;
                }
                return GlideUtil.instance().getImageDrawable(getContext(),dataList.get(index),getWidth(),getHeight());
            }

            @Override
            public void runOnUIThread(Drawable bitmap) {

            }
        }.start();
    }

    protected void stopTime() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        myHandler = null;
    }

    private int TIME = 0;
    private boolean isPause;
    private void startTime(){
        stopTime();
        timer = new Timer("AdViewPager", true);
        myHandler = new MyHandler(this);
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if(activity==null||activity.isFinishing()){
                    stopTime();
                    return;
                }
                if(!isPause&&!isTouching){
                    TIME++;
                    if(TIME%4==0){
                        myHandler.sendEmptyMessage(0);
                    }
                    if(TIME>=3000){
                        TIME = 0;
                    }
                }
            }
        };
        timer.schedule(timerTask, 1000, 1000);
    }

    public void onPause(){
        isPause = true;
    }

    public void onResume(){
        isPause = false;
    }

    public void initActivity(Activity activity){
        this.activity = activity;
    }


    @Override
    public void handleMessage(Message msg) {
        setNext();
        startAnim(0,getWidth(),1);
    }

    /**
     * @param type 0:item复位，1：翻下一页，2：翻上一页
     */
    private void startAnim(int fromX, final int toX, final int type){
        if(valueAnimator!=null&&!valueAnimator.isRunning()){
            valueAnimator.cancel();
        }
        valueAnimator = ValueAnimator.ofInt(fromX,toX);
        long allTime = 200;
        long needTime;
        if(getWidth()==0){
            needTime = allTime;
        }else{
            needTime = allTime*Math.abs(toX-fromX)/getWidth();
        }
        valueAnimator.setDuration(needTime);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                if(isTouching){
                    valueAnimator.cancel();
                }else{
                    TIME = 0;
                    // 不断重新计算上下左右位置
                    moveX = (int) animator.getAnimatedValue();
                    // 重绘
                    if(moveX==toX){
                        onAnimEnd(type);
                    }else{
                        requestLayout();
                    }
                }
            }
        });
        valueAnimator.start();
    }

    private void setNext() {
        int index = mCurrentPosition + 1;
        if(index>dataList.size()-1){
            index = 0;
        }
        if(activity==null||activity.isFinishing()){
            stopTime();
            return;
        }
        ImageView otherImageView = getOtherImageView();
        GlideUtil.instance().setDefaultImage(otherImageView,dataList.get(index), otherImageView,imageType==0);
    }

    private void setLast() {
        int index = mCurrentPosition - 1;
        if(index<0){
            index = dataList.size()-1;
        }
        if(activity==null||activity.isFinishing()){
            stopTime();
            return;
        }
        ImageView otherImageView = getOtherImageView();
        GlideUtil.instance().setDefaultImage(otherImageView,dataList.get(index), otherImageView,imageType==0);
    }

    /**
     * @param type 0:归位，1：next,2:last
     */
    private void onAnimEnd(int type){
        TIME = 0;
        if(type==0){
            moveX = 0;
        }else if(type==1){
            moveX = 0;
            mCurrentPosition++;
            if(mCurrentPosition>dataList.size()-1){
                mCurrentPosition = 0;
            }
        }else if(type==2){
            moveX = 0;
            mCurrentPosition--;
            if(mCurrentPosition<0){
                mCurrentPosition = dataList.size()-1;
            }
        }
        if(type==1||type==2){
            ImageView linShi = getOtherImageView();
            mOtherImageView = getCurrentImageView();
            mCurrentImageView = linShi;
            if(onMyPageChangeListener!=null){
                onMyPageChangeListener.onPageSelected(mCurrentPosition);
            }
        }
        requestLayout();
    }

    private boolean isTouching;
    private float finalDownX,finalDownY;
    private int mTouchSlop;
    private OnTouchListen onTouchListen;

    public void setOnTouchListen(OnTouchListen onTouchListen) {
        this.onTouchListen = onTouchListen;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        TIME = 0;
        if(dataList==null||dataList.size()==0){
            return false;
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(onTouchListen!=null)onTouchListen.onTouch(true);
                isTouching = true;
                downX = event.getX();
                finalDownX = downX;
                finalDownY = event.getY();
                moveX = 0;
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                if(dataList.size()==1){
                    break;
                }
                float currentX = event.getX();
                boolean isShowLast = moveX<0;
                boolean isShowNext = moveX>0;

                moveX -= (currentX-downX);
                downX = currentX;
                if(!isShowNext&&!isShowLast){
                    if(moveX<0){
                        setLast();
                    }else if(moveX>0){
                        setNext();
                    }
                }else{
                    if(isShowNext&&moveX<0){
                        setLast();
                    }else if(isShowLast&&moveX>0){
                        setNext();
                    }
                }
                if(Math.abs(moveX)<=getWidth()){
                    requestLayout();
                }
                break;
            case MotionEvent.ACTION_UP:
                if(onTouchListen!=null)onTouchListen.onTouch(false);
                if(Math.abs(event.getX()-finalDownX)<mTouchSlop&&Math.abs(event.getY()-finalDownY)<mTouchSlop){
                    if(onItemClickListen!=null){
                        onItemClickListen.click(mCurrentPosition);
                    }
                }
                isTouching = false;
                if(moveX!=0){
                    Log.e("ACTION_UP", "onTouchEvent: getWidth/5="+getWidth()/15 );
                    if(Math.abs(moveX) >=getWidth()/15){
                        if(moveX>0){
                            startAnim(moveX,getWidth(),1);
                        }else{
                            startAnim(moveX,-getWidth(),2);
                        }
                    }else{
                        startAnim(moveX,0,0);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if(onTouchListen!=null)onTouchListen.onTouch(false);
                if(Math.abs(event.getX()-finalDownX)<mTouchSlop&&Math.abs(event.getY()-finalDownY)<mTouchSlop){
                    if(onItemClickListen!=null){
                        onItemClickListen.click(mCurrentPosition);
                    }
                }
                isTouching = false;
                if(moveX!=0){
                    if(Math.abs(moveX) >=getWidth()/3){
                        if(moveX>0){
                            startAnim(moveX,getWidth(),1);
                        }else{
                            startAnim(moveX,-getWidth(),2);
                        }
                    }else{
                        startAnim(moveX,0,0);
                    }
                }
                break;
        }
        return true;
    }
}
