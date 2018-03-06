package com.jiujie.base.util;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.jiujie.base.jk.OnListener;
import com.jiujie.base.util.glide.GlideUtil;

import java.io.File;
import java.io.IOException;

/**
 * Created by ChenJiaLiang on 2017/7/27.
 * Email:576507648@qq.com
 */

public class WallpaperUtil {

    public static void setWallPaper(final Activity mActivity, final String url) {
        if (mActivity == null || mActivity.isFinishing() || TextUtils.isEmpty(url)) {
            return;
        }
        final Dialog waitingDialog = UIHelper.getWaitingDialog(mActivity);
        waitingDialog.setCanceledOnTouchOutside(false);
        waitingDialog.show();
        UIHelper.showToastShort(mActivity, "正在设置壁纸...");
        requestPermissions(new OnListener<Boolean>() {
            @Override
            public void onListen(Boolean isHas) {
                if (isHas) {
                    doSetWallpaper(mActivity, url, waitingDialog);
                } else {
                    if (waitingDialog.isShowing()) waitingDialog.dismiss();
                    UIHelper.showToastShort(mActivity, "设置失败，无设置壁纸权限");
                }
            }
        });
    }

    private static void requestPermissions(final OnListener<Boolean> onListener) {
        PermissionsManager.getPermissionSimple(new OnListener<Boolean>() {
            @Override
            public void onListen(Boolean isHasPermission) {
                onListener.onListen(isHasPermission);
            }
        }, Manifest.permission.SET_WALLPAPER,
                Manifest.permission.SET_WALLPAPER_HINTS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private static void doSetWallpaper(final Activity mActivity, final String url, final Dialog waitingDialog) {
        new TaskManager<Boolean>() {
            @Override
            public Boolean runOnBackgroundThread() {
                try {
                    UIHelper.showLog("doSetWallpaper url:" + url);
                    Bitmap bitmap = GlideUtil.instance().getImage(mActivity, url, true, true);
                    if (bitmap == null) {
                        return false;
                    }
                    int screenWidth = UIHelper.getScreenWidth(mActivity);
                    int screenHeight = UIHelper.getScreenHeight(mActivity);
                    int bitmapWidth = bitmap.getWidth();
                    int bitmapHeight = bitmap.getHeight();


                    UIHelper.showLog("doSetWallpaper screenWidth:" + screenWidth);
                    UIHelper.showLog("doSetWallpaper screenHeight:" + screenHeight);
                    UIHelper.showLog("doSetWallpaper bitmapWidth:" + bitmapWidth);
                    UIHelper.showLog("doSetWallpaper bitmapHeight:" + bitmapHeight);


                    //1,先把图片缩放成宽和屏幕一样宽或者高度和屏幕一样高，并且，另外一边要>=屏幕的
                    float scaleWidth = bitmapWidth * 1.0f / screenWidth;
                    float scaleHeight = bitmapHeight * 1.0f / screenHeight;
                    if (screenWidth != bitmapWidth || scaleHeight != bitmapHeight) {
                        int scaleResultRequestWidth;
                        int scaleResultRequestHeight;
                        if (scaleWidth <= scaleHeight) {
                            scaleResultRequestWidth = screenWidth;
                            scaleResultRequestHeight = (int) (bitmapHeight / scaleWidth);
                        } else {
                            scaleResultRequestWidth = (int) (bitmapWidth / scaleHeight);
                            scaleResultRequestHeight = screenHeight;
                        }
                        bitmap = ImageUtil.instance().scaleBitmap(bitmap, scaleResultRequestWidth, scaleResultRequestHeight);
                        bitmapWidth = bitmap.getWidth();
                        bitmapHeight = bitmap.getHeight();

                        UIHelper.showLog("doSetWallpaper after scaleBitmap bitmapWidth:" + bitmapWidth);
                        UIHelper.showLog("doSetWallpaper after scaleBitmap bitmapHeight:" + bitmapHeight);
                    }

                    //2,裁剪成屏幕尺寸
                    if (bitmapWidth != screenWidth || bitmapHeight != screenHeight) {
                        int top = Math.abs(bitmap.getHeight() - screenHeight) / 2;
                        int left = Math.abs(bitmap.getWidth() - screenWidth) / 2;
                        bitmap = ImageUtil.instance().cropImage(bitmap, screenWidth, screenHeight, left, top, left + screenWidth, top + screenHeight);
                        bitmapWidth = bitmap.getWidth();
                        bitmapHeight = bitmap.getHeight();

                        UIHelper.showLog("doSetWallpaper after crop bitmapWidth:" + bitmapWidth);
                        UIHelper.showLog("doSetWallpaper after crop bitmapHeight:" + bitmapHeight);
                    }

                    return setWallpaper(mActivity, bitmap);
                } catch (final Exception e) {
                    FileUtil.requestPermission(new OnListener<Boolean>() {
                        @Override
                        public void onListen(Boolean isHas) {
                            if(isHas){
                                String message = e.getMessage();
                                File logFile = FileUtil.createLogFile(mActivity, "setWallpaperFailLog.txt");
                                UIHelper.writeStringToFile(logFile.getParentFile().getAbsolutePath(), logFile.getName(), message);
                                UIHelper.showToastShort(mActivity, "图片处理失败");
                            }
                        }
                    });
                }
                return false;
            }

            @Override
            public void runOnUIThread(Boolean isSuccess) {
                if (waitingDialog != null && waitingDialog.isShowing()) waitingDialog.dismiss();
                if (isSuccess) {
                    UIHelper.showToastShort(mActivity, "设置壁纸成功");
                } else {
                    UIHelper.showToastShort(mActivity, "设置壁纸失败");
                }
            }
        }.start();
    }

    private static boolean setWallpaper(final Activity mActivity, final Bitmap resource) {
        try {
            WallpaperManager instance = WallpaperManager.getInstance(mActivity);
//            DisplayMetrics dm = new DisplayMetrics();
//            mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
//            int desiredMinimumWidth = dm.widthPixels;
//            int desiredMinimumHeight = dm.heightPixels;
//            UIHelper.showLog("doSetWallpaper setWallpaper Width:" + desiredMinimumWidth);
//            UIHelper.showLog("doSetWallpaper setWallpaper Height:" + desiredMinimumHeight);
//            instance.suggestDesiredDimensions(desiredMinimumWidth, desiredMinimumHeight);
            instance.setBitmap(resource);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
