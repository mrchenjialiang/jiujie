package com.jiujie.base.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.jiujie.base.jk.OnItemClickListen;
import com.jiujie.base.jk.OnMyPageChangeListener;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 在触摸时广告设置成不自动跳转
 * should onDestroy when onDestroy if use in Activity or onDestroyView if use in fragment
 * @author ChenJiaLiang
 */
public class AdvertViewpager extends ViewPager {
    private String TAG = "AdvertViewpager";
    private final int reAutoScrollTime = 4;//循环播放时间间隔--秒
    private int actionTime = 0;
    private Timer timer;
    private TimerTask task;
    private OnPageChangeListener onPageChangeListener;
    private boolean isThisDoTouch;//是否拦截触摸事件由该控件负责
    private boolean isJudge;//是否判断

    public AdvertViewpager(Context context) {
        super(context);
        init();
    }

    public AdvertViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private boolean isPause;
    public void onPause(){
        isPause = true;
//        if(timer!=null){
////            timer.cancel();//不能取消或者中断，否则就得重新构造一个，不能恢复，不然就崩了
//            timer.purge();
//        }
    }

    public void onResume(){
        if(isPause){
//            if(timer!=null&&task!=null)
//                timer.schedule(task,0,1000);
//            else
//                init();
            isPause = false;
        }
    }

    public void onDestroy(){
        onMyPageChangeListener = null;
        setOnPageChangeListener(null);
        clearOnPageChangeListeners();
        if(timer!=null){
            timer.cancel();
            timer = null;
            task = null;
        }
    }

    private void init(){
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        if(handler==null)handler = new MyHandler(this);
        if(timer==null)timer = new Timer(true);
        if(task==null){
            task = new TimerTask() {
                @Override
                public void run() {
                    if(isPause||getAdapter()==null||getAdapter().getCount()<=1){
                        return;
                    }
                    actionTime++;
                    if (actionTime >= reAutoScrollTime) {
                        int currentItem = getCurrentItem();
                        if (currentItem == getAdapter().getCount() - 1) {
                            handler.sendEmptyMessage(0);
                        } else {
                            handler.sendEmptyMessage(currentItem + 1);
                        }
                        actionTime = 0;
                    }
                }
            };
            timer.schedule(task,0,1000);
        }
    }

    private float downX;
    private float downY;
    private MyHandler handler;

    private static class MyHandler extends Handler{
        WeakReference<AdvertViewpager> weakReference;
        public MyHandler(AdvertViewpager advertViewpager){
            weakReference = new WeakReference<>(advertViewpager);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            AdvertViewpager advertViewpager = weakReference.get();
            if(advertViewpager!=null){
                PagerAdapter adapter = advertViewpager.getAdapter();
                if(adapter==null)return;
                if(msg.what<0||msg.what> adapter.getCount()-1){
                    advertViewpager.onDestroy();
                }else{
                    if (msg.what == 0) {
                        if(adapter.getCount()==2){
                            advertViewpager.setCurrentItem(msg.what, true);
                        }else{
                            advertViewpager.setCurrentItem(msg.what, false);
                        }
                    }else{
                        advertViewpager.setCurrentItem(msg.what);
                    }
                }
            }
        }
    }

    OnMyPageChangeListener onMyPageChangeListener;

    public void setOnMyPageChangeListener(final OnMyPageChangeListener onMyPageChangeListener) {
        this.onMyPageChangeListener = onMyPageChangeListener;
        if(onPageChangeListener==null){
            onPageChangeListener = new OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    actionTime = 0;
                    if(onMyPageChangeListener!=null)onMyPageChangeListener.onPageSelected(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            };
            addOnPageChangeListener(onPageChangeListener);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        actionTime = 0;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                finalDownX = downX;
                finalDownY = ev.getY();
                isJudge = false;
                isThisDoTouch = false;
                if (getChildCount() > 1) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = ev.getX();
                float moveY = ev.getY();
                if (!isJudge) {
                    isJudge = true;
                    isThisDoTouch = Math.abs(moveX - downX) > Math.abs(moveY - downY);
                    if(isThisDoTouch&&getAdapter()!=null){
                        if(getCurrentItem()==0&&moveX>downX){
                            //当前是第一项，还在往前翻
                            isThisDoTouch = false;
                        }else if(getCurrentItem()==getAdapter().getCount()-1&&moveX<downX){
                            //当前是最后一项，还在往后翻
                            isThisDoTouch = false;
                        }
                    }
                }
                if(getChildCount() > 1){
                    getParent().requestDisallowInterceptTouchEvent(isThisDoTouch);
                }
                break;
            case MotionEvent.ACTION_UP:
                if(Math.abs(ev.getX()-finalDownX)<mTouchSlop&&Math.abs(ev.getY()-finalDownY)<mTouchSlop){
                    if(onItemClickListen!=null){
                        onItemClickListen.click(getCurrentItem());
                    }
                }
                isJudge = false;
                isThisDoTouch = false;
                break;
        }
        return super.onTouchEvent(ev);
    }

    private float finalDownX,finalDownY;
    private int mTouchSlop;
    private OnItemClickListen onItemClickListen;
    public void setOnItemClickListen(OnItemClickListen onItemClickListen) {
        this.onItemClickListen = onItemClickListen;
    }
}
