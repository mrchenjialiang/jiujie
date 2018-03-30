package com.jiujie.jiujie.service;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.jiujie.base.util.UIHelper;

/**
 * Created by ChenJiaLiang on 2018/3/29.
 * Email:576507648@qq.com
 */
public class TransWallpaperServer extends WallpaperService {
    private final Handler handler=new Handler();
    private Camera camera;

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
    class MyEngine extends Engine implements Camera.PreviewCallback{

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            startPreview();
            setTouchEventsEnabled(true);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            UIHelper.showLog(this,"onDestroy");
            stopPreview();
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            UIHelper.showLog(this,"onVisibilityChanged visible "+visible);
            if(visible){
                startPreview();
            }else{
                stopPreview();
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format,
                                     int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
        }

        private boolean isStarted;
        private void startPreview() {
            if(isStarted)return;
            isStarted = true;
//            camera = Camera.open();
            camera = Camera.open(Camera.getNumberOfCameras()-1);
            camera.setDisplayOrientation(90);
            try {
                camera.setPreviewDisplay(getSurfaceHolder());
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private void stopPreview() {
            if(camera!=null){
                try {
                    camera.stopPreview();
                    camera.setPreviewCallback(null);
                    camera.release();
                }catch (Exception e){
                    e.printStackTrace();
                }
                camera = null;
            }
            isStarted = false;
        }


        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            camera.addCallbackBuffer(data);
        }
    }


}