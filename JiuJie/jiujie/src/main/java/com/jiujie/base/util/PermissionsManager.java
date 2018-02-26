package com.jiujie.base.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.jiujie.base.APP;
import com.jiujie.base.jk.ICallBackNoParam;
import com.jiujie.base.jk.OnListener;
import com.jiujie.base.jk.PermissionRequest;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.functions.Action1;

/**
 * author : Created by ChenJiaLiang on 2016/7/29.
 * Email : 576507648@qq.com
 */
public class PermissionsManager {


    public static void getPermission(final OnListener<Boolean[]> onListener, final String... permissions){
        if(permissions==null||permissions.length==0){
            onListener.onListen(null);
            return;
        }
        Boolean[] permissionResults = new Boolean[permissions.length];
        for (int i = 0;i<permissionResults.length;i++){
            permissionResults[i] = false;
        }
        RxPermissions.getInstance(APP.getContext()).requestEach(permissions)
                .subscribe(new Action1<Permission>() {
                    List<Boolean> resultList = new ArrayList<>();
                    @Override
                    public void call(Permission permission) {
                        UIHelper.showLog("getPermission callback "+permission.name+","+permission.granted);
                        resultList.add(permission.granted);
                        if(resultList.size()==permissions.length){
                            onListener.onListen(UIHelper.list2Array(resultList,new Boolean[resultList.size()]));
                        }
                    }
                });
    }

    public static void getPermissionSimple(final OnListener<Boolean> onListener,String... permissions){
        getPermission(new OnListener<Boolean[]>() {
            @Override
            public void onListen(Boolean[] booleans) {
                if(booleans==null||booleans.length==0){
                    onListener.onListen(false);
                }else{
                    for (boolean isHas:booleans){
                        if(!isHas){
                            onListener.onListen(false);
                            return;
                        }
                    }
                    onListener.onListen(true);
                }
            }
        },permissions);
    }
}
