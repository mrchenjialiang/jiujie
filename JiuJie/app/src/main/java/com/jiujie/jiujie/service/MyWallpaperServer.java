package com.jiujie.jiujie.service;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
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
//    private final Handler handler = new Handler();

    public void start(Activity activity) {
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
            if (!UIHelper.isIntentExisting(activity, intent)) {
                UIHelper.showToastShort(activity, "您的机型不支持设置动态壁纸");
                return;
            }
        }
        activity.startActivity(intent);
    }

    @Override
    public Engine onCreateEngine() {
        return new MyEngine();
    }

    class MyEngine extends Engine {
        private final Paint mPaint = new Paint();
        Bitmap wallpaperBitmap;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            setTouchEventsEnabled(true);
            wallpaperBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cs);
            super.onCreate(surfaceHolder);
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            UIHelper.showLog(this,"onTouchEvent");
            draw();
            super.onTouchEvent(event);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            UIHelper.showLog(this,"onVisibilityChanged visible "+visible);
            if (visible) {
                draw();
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format,
                                     int width, int height) {
            UIHelper.showLog(this,"onSurfaceChanged");
            draw();
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            UIHelper.showLog(this,"onDestroy");
        }

        private void draw() {

            UIHelper.showLog(this,"draw");
            final SurfaceHolder holder = getSurfaceHolder();
            Canvas c = holder.lockCanvas();

            c.drawBitmap(wallpaperBitmap,0,0,mPaint);

            holder.unlockCanvasAndPost(c);
        }
    }


}