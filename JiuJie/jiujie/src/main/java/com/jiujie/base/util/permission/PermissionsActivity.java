package com.jiujie.base.util.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.jiujie.base.APP;
import com.jiujie.base.R;
import com.jiujie.base.util.UIHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionsActivity extends Activity {

    private static PermissionListener onListener;
    private final int REQUEST_CODE_PERMISSIONS = 1211;
    private final int GET_UNKNOWN_APP_SOURCES = 1212;
    private Map<String,Boolean> permissionResultMap = new HashMap<>();

    public static void launch(Context context, PermissionListener onListener, String... permissions){
        if(permissions==null||permissions.length==0||onListener==null){
            return;
        }
        int hasSize = 0;
        for(String permission:permissions){
            if(ContextCompat.checkSelfPermission(context, permission)== PackageManager.PERMISSION_GRANTED){
                hasSize++;
            }
        }
        if(hasSize==permissions.length){
            onListener.onResult(true);
            return;
        }
        PermissionsActivity.onListener = onListener;
        Intent intent = new Intent(context, PermissionsActivity.class);
        intent.putExtra("permissions",permissions);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null){
            handleIntent(getIntent());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String[] permissions = intent.getStringArrayExtra("permissions");
        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSIONS:
                UIHelper.showLog(this,"permissions:"+ Arrays.toString(permissions));
                UIHelper.showLog(this,"grantResults:"+ Arrays.toString(grantResults));
                for (int i = 0;i<permissions.length;i++){
                    permissionResultMap.put(permissions[i],grantResults[i]==PackageManager.PERMISSION_GRANTED);
                }

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    String installPermissionInAndroid8 = Manifest.permission.REQUEST_INSTALL_PACKAGES;
                    if(permissionResultMap.containsKey(installPermissionInAndroid8)&&!permissionResultMap.get(installPermissionInAndroid8)){
                        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                        if (UIHelper.isIntentExisting(this, intent)) {
                            String appName = APP.getContext().getResources().getString(R.string.app_name);
                            UIHelper.showToastShort("请先给予"+ appName+"安装权限");
                            startActivityForResult(intent, GET_UNKNOWN_APP_SOURCES);
                            //符合这个条件的就先不检查权限结果，等 onActivityResult 再检查
                            return;
                        }
                    }
                }
                checkResultPermissions();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O&&requestCode == GET_UNKNOWN_APP_SOURCES){
            boolean canRequestPackageInstalls = getPackageManager().canRequestPackageInstalls();
            permissionResultMap.put(Manifest.permission.REQUEST_INSTALL_PACKAGES,canRequestPackageInstalls);
            checkResultPermissions();
        }
    }

    private void checkResultPermissions(){
        List<String> failPermission = new ArrayList<>();
        for (String permission:permissionResultMap.keySet()){
            if(!permissionResultMap.get(permission)){
                failPermission.add(permission);
            }
        }
        if(onListener!=null){
            onListener.onFailPermissions(failPermission);
            onListener.onResult(failPermission.size()==0);
        }
        finish();
        onListener = null;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.alpha_show,R.anim.alpha_hide);
    }
}
