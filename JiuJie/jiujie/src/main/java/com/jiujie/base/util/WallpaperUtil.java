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

import java.io.File;
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
                try {
                    Bitmap bitmap = GlideUtil.instance().getImage(mActivity, url, true, true);
                    if (bitmap == null) {
                        return false;
                    }
                    int screenWidth = UIHelper.getScreenWidth(mActivity);
                    int screenHeight = UIHelper.getScreenHeight(mActivity);
                    int bitmapWidth = bitmap.getWidth();
                    int bitmapHeight = bitmap.getHeight();
                    boolean isChange = false;
                    if (bitmapWidth > screenWidth) {
                        bitmapWidth = screenWidth;
                        float scale = bitmapWidth * 1f / screenWidth;
                        bitmapHeight = (int) (bitmapHeight / scale);
                        isChange = true;
                    }
                    if (bitmapHeight > screenHeight) {
                        bitmapHeight = screenHeight;
                        float scale = bitmapHeight * 1f / screenHeight;
                        bitmapWidth = (int) (bitmapWidth / scale);
                        isChange = true;
                    }
                    if (isChange)
                        bitmap = ImageUtil.instance().scaleBitmap(bitmap, bitmapWidth, bitmapHeight);

                    float requestScale = 0.5625f;
                    int left = 0, top = 0;
                    if (bitmapWidth * 1.0f / bitmapHeight > requestScale) {
                        int yuanWidth = bitmapWidth;
                        bitmapWidth = (int) (bitmapHeight * requestScale);
                        left = (yuanWidth - bitmapWidth) / 2;
                    } else {
                        int yuanHeight = bitmapHeight;
                        bitmapHeight = (int) (bitmapWidth / requestScale);
                        top = (yuanHeight - bitmapHeight) / 2;
                    }
                    bitmap = ImageUtil.instance().cropImage(bitmap, bitmapWidth, bitmapHeight, left, top, left + bitmapWidth, top + bitmapHeight);
                    return setWallpaper(mActivity, bitmap);
                }catch (Exception e){
                    String message = e.getMessage();
                    File logFile = FileUtil.createLogFile(mActivity, "setWallpaperFailLog.txt");
                    UIHelper.writeStringToFile(logFile.getParentFile().getAbsolutePath(),logFile.getName(),message);
                    UIHelper.showToastShort(mActivity, "图片处理失败");
                }
                return false;
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
