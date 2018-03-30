package com.jiujie.jiujie.service;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.jiujie.base.util.UIHelper;
import com.jiujie.jiujie.R;

/**
 * Created by ChenJiaLiang on 2018/3/29.
 * Email:576507648@qq.com
 */
public class MyWallpaperServer extends WallpaperService {
    private final Handler handler=new Handler();

    public void start(Activity activity){
        ComponentName componentName = new ComponentName(activity, this.getClass().getName());

//        Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
//        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, componentName);
//        if(!UIHelper.isIntentExisting(activity,intent)){
//            UIHelper.showToastShort(activity,"您的机型不支持设置动态壁纸");
//            return;
//        }
//        activity.startActivity(intent);
//        activity.startActivityForResult(intent, Constant.REQUEST_SET_LIVE_WALLPAPER);

        Intent intent;
        if (android.os.Build.VERSION.SDK_INT < 16) {
            intent = new Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
        } else {
            intent = new Intent("android.service.wallpaper.CHANGE_LIVE_WALLPAPER");
            intent.putExtra("android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT", componentName);
            if(!UIHelper.isIntentExisting(activity,intent)){
                UIHelper.showToastShort(activity,"您的机型不支持设置动态壁纸");
                return;
            }
        }
        activity.startActivity(intent);
    }

    @Override
    public Engine onCreateEngine() {
        return new MyEngine();
    }
    class MyEngine extends Engine{
        private final Paint mPaint=new Paint();
        private float centerx;
        private float centery;
        private float mTouchx;
        private float mTouchy;
        private boolean mVisible;
        Bitmap bm1;
        Bitmap bm2;
        Bitmap bm3;
        Bitmap bm4;

        private final Runnable myDraw  =new Runnable() {

            @Override
            public void run() {
// TODO Auto-generated method stub
                draw();
            }
        };

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
// TODO Auto-generated method stub
            setTouchEventsEnabled(true);
            bm1= BitmapFactory.decodeResource(getResources(), R.drawable.load_fail);
            super.onCreate(surfaceHolder);
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
// TODO Auto-generated method stub
            if (event.getAction()==MotionEvent.ACTION_DOWN) {
                mTouchx=event.getX();
                mTouchy=event.getY();
                draw();
            } else if(event.getAction()==MotionEvent.ACTION_MOVE)  {
                mTouchx=event.getX();
                mTouchy=event.getY();
                draw();
            }
            else if(event.getAction()==MotionEvent.ACTION_UP){
                undraw();
            }
            super.onTouchEvent(event);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
// TODO Auto-generated method stub
            mVisible=visible;
            if (visible) {
                draw();
            }
            else {
                handler.removeCallbacks(myDraw);
            }
        }
        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format,
                                     int width, int height) {
            centerx=width/2.0f;
            centery=height/2.0f;
            draw();
            super.onSurfaceChanged(holder, format, width, height);
        }
        @Override
        public void onDestroy() {
// TODO Auto-generated method stub
            super.onDestroy();
            handler.removeCallbacks(myDraw);
        }
        public void undraw(){
            final SurfaceHolder holder=getSurfaceHolder();
            Canvas c=holder.lockCanvas();
            c.drawBitmap(bm1, mTouchx, mTouchy, mPaint);
            holder.unlockCanvasAndPost(c);
            if (mVisible) {
                handler.postDelayed(myDraw, 1000);
            }
        }
        public void draw(){

            final SurfaceHolder holder=getSurfaceHolder();
            Canvas c=holder.lockCanvas();

            c.drawCircle(mTouchx, mTouchy, centerx, mPaint);

            holder.unlockCanvasAndPost(c);

            handler.removeCallbacks(myDraw);
            if (mVisible) {
                handler.postDelayed(myDraw, 1000);
            }
        }
    }



}