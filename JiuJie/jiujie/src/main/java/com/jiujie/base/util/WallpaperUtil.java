package com.jiujie.base.util;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.jiujie.base.jk.OnListener;
import com.jiujie.base.util.glide.GlideUtil;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.IOException;

import rx.functions.Action1;

/**
 * Created by ChenJiaLiang on 2017/7/27.
 * Email:576507648@qq.com
 */
public class WallpaperUtil {

    public static void setWallPaper(final Activity mActivity, final String url){
        if(mActivity==null||mActivity.isFinishing()|| TextUtils.isEmpty(url)){
            return;
        }
        final Dialog waitingDialog = UIHelper.getWaitingDialog(mActivity);
        waitingDialog.setCanceledOnTouchOutside(false);
        waitingDialog.show();
        UIHelper.showToastLong(mActivity,"正在设置壁纸...");
        requestPermissions(mActivity, new OnListener<Boolean>() {
            @Override
            public void onListen(Boolean isHas) {
                if(isHas){
                    doSetWallpaper(mActivity, url,waitingDialog);
                }else{
                    if(waitingDialog.isShowing())waitingDialog.dismiss();
                    UIHelper.showToastShort(mActivity,"设置失败，无设置壁纸权限");
                }
            }
        });
    }

    private static void requestPermissions(Context context,final OnListener<Boolean> onListener){
        RxPermissions.getInstance(context).requestEach(
                Manifest.permission.SET_WALLPAPER,
                Manifest.permission.SET_WALLPAPER_HINTS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Action1<Permission>() {
                    boolean isCallback;
                    @Override
                    public void call(Permission permission) {
                        if(isCallback){
                            return;
                        }
                        if (permission.granted) {
                            onListener.onListen(true);
                        }else {
                            onListener.onListen(false);
                        }
                        isCallback = true;
                    }
                });
    }

    private static void doSetWallpaper(final Activity mActivity, final String url, final Dialog waitingDialog) {
        new TaskManager<Boolean>() {
            @Override
            public Boolean runOnBackgroundThread() {
                Bitmap bitmap = GlideUtil.instance().getImage(mActivity, url, true, true);
                if(bitmap==null){
                    return false;
                }
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                float requestScale = 0.5625f;
                int left = 0,top = 0;
                if(width*1.0f/height> requestScale){
                    int yuanWidth = width;
                    width = (int) (height* requestScale);
                    left = (yuanWidth - width)/2;
                }else{
                    int yuanHeight = height;
                    height = (int) (width/requestScale);
                    top = (yuanHeight-height)/2;
                }
                bitmap = ImageUtil.instance().cropImage(bitmap, width, height, left, top, left + width, top + height);
                return setWallpaper(mActivity,bitmap);
            }

            @Override
            public void runOnUIThread(Boolean isSuccess) {
                if(waitingDialog!=null&&waitingDialog.isShowing())waitingDialog.dismiss();
                if(isSuccess){
                    UIHelper.showToastShort(mActivity,"设置壁纸成功");
                }else{
                    UIHelper.showToastShort(mActivity,"设置壁纸失败");
                }
            }
        }.start();
    }

    private static boolean setWallpaper(final Activity mActivity, final Bitmap resource){
        try {
            WallpaperManager instance = WallpaperManager.getInstance(mActivity);
            DisplayMetrics dm = new DisplayMetrics();
            mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            int desiredMinimumWidth = dm.widthPixels;
            int desiredMinimumHeight = dm.heightPixels;
            instance.suggestDesiredDimensions(desiredMinimumWidth, desiredMinimumHeight);
            instance.setBitmap(resource);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
