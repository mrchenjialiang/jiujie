package com.jiujie.base.util;

import com.jiujie.base.APP;
import com.jiujie.base.jk.OnListener;
import com.jiujie.base.util.permission.PermissionListener;
import com.jiujie.base.util.permission.PermissionSimpleListener;
import com.jiujie.base.util.permission.PermissionsActivity;

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
}
