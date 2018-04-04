package com.jiujie.base.util;

import com.jiujie.base.APP;
import com.jiujie.base.jk.OnListener;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

/**
 * author : Created by ChenJiaLiang on 2016/7/29.
 * Email : 576507648@qq.com
 */
public class PermissionsManager {


    public static void getPermission(final OnListener<Boolean[]> onListener, final String... permissions){
        if(permissions==null||permissions.length==0){
            throw new NullPointerException("permissions==null or length==0 when getPermissionSimple");
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
        if(permissions==null||permissions.length==0){
            throw new NullPointerException("permissions==null or length==0 when getPermissionSimple");
        }
        RxPermissions.getInstance(APP.getContext()).request(permissions)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean isHas) {
                        if (isHas) {
                            onListener.onListen(true);
                        } else {
                            onListener.onListen(false);
                        }
                    }
                });
    }
}
