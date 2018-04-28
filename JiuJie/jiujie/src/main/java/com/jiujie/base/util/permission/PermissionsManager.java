package com.jiujie.base.util.permission;

import android.Manifest;
import android.os.Build;

import com.jiujie.base.APP;
import com.jiujie.base.jk.OnListener;

/**
 * author : Created by ChenJiaLiang on 2016/7/29.
 * Email : 576507648@qq.com
 */
public class PermissionsManager {

    public static void getPermission(final PermissionListener onListener, final String... permissions){
        PermissionsActivity.launch(APP.getContext(), onListener ,permissions);
    }

    public static void getPermissionSimple(final OnListener<Boolean> onListener,String... permissions){
        PermissionsActivity.launch(APP.getContext(), new PermissionSimpleListener() {
            @Override
            public void onResult(Boolean isAllHas) {
                onListener.onListen(isAllHas);
            }
        },permissions);
    }

    public static void requestPermissions(PermissionSimpleListener onListener,String... permissions){
        PermissionsActivity.launch(APP.getContext(), onListener,permissions);
    }

    public static void requestWriteReadPermissions(OnListener<Boolean> onListener){
        getPermissionSimple(onListener, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    public static void requestInstallPermissions(OnListener<Boolean> onListener){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O&&APP.getContext().getApplicationInfo().targetSdkVersion > 26) {
            boolean canRequestPackageInstalls = APP.getContext().getPackageManager().canRequestPackageInstalls();
            if(canRequestPackageInstalls){
                onListener.onListen(true);
            }else{
                getPermissionSimple(onListener, Manifest.permission.REQUEST_INSTALL_PACKAGES);
            }
        }else{
            onListener.onListen(true);
        }
    }

    public static void requestSetWallpaperPermissions(OnListener<Boolean> onListener){
        PermissionsManager.getPermissionSimple(onListener,
                Manifest.permission.SET_WALLPAPER,
                Manifest.permission.SET_WALLPAPER_HINTS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public static void requestCameraPermissions(OnListener<Boolean> onListener){
        PermissionsManager.getPermissionSimple(onListener,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
                ,Manifest.permission.CAMERA);
    }
}
