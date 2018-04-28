package com.jiujie.base.util.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.jiujie.base.APP;
import com.jiujie.base.R;
import com.jiujie.base.dialog.BaseDialog;
import com.jiujie.base.dialog.EnsureDialog;
import com.jiujie.base.jk.OnBaseDialogClickListener;
import com.jiujie.base.util.UIHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionsActivity extends Activity {

    private static PermissionListener onListener;
    private final int REQUEST_CODE_PERMISSIONS = 1211;
    private final int REQUEST_CODE_GET_INSTALL_PERMISSION = 1212;
    private Map<String,Boolean> permissionResultMap = new HashMap<>();
    private Activity mActivity;

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
        mActivity = this;
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

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O&&getApplicationInfo().targetSdkVersion > Build.VERSION_CODES.O){
                    //对于这个权限来说，并不能动态申请，动态申请
                    String installPermissionInAndroid8 = Manifest.permission.REQUEST_INSTALL_PACKAGES;
                    if(permissionResultMap.containsKey(installPermissionInAndroid8)){
                        boolean canRequestPackageInstalls = getPackageManager().canRequestPackageInstalls();
                        permissionResultMap.put(installPermissionInAndroid8,canRequestPackageInstalls);
                        if(!canRequestPackageInstalls){
                            showRequestInstallPermissionDialog();
                            return;
                        }
                    }
                }
                checkResultPermissions();
                break;
        }
    }

    private void showRequestInstallPermissionDialog() {
        String appName = APP.getContext().getResources().getString(R.string.app_name);
        EnsureDialog ensureDialog = new EnsureDialog(mActivity);
        ensureDialog.create();
        UIHelper.setDialogNoDismissByTouchAndBackPress(ensureDialog.getDialog());
        ensureDialog.setText("\""+appName+"\"正在请求安装应用权限，是否去设置？")
                .setBtnLeft("下次再说", new OnBaseDialogClickListener() {
                    @Override
                    public void onClick(BaseDialog dialog, View v) {
                        dialog.dismiss();
                        checkResultPermissions();
                    }
                })
                .setBtnRight("去设置", new OnBaseDialogClickListener() {
                    @Override
                    public void onClick(BaseDialog dialog, View v) {
                        dialog.dismiss();
                        toOpenInstallPermission();
                    }
                }).show();
    }

    private void toOpenInstallPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
            intent.setData(Uri.parse("package:" + APP.getContext().getPackageName()));
//          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//如果加了这句，onActivityResult的回调就会在startActivityForResult时调用，而非返回了再回调
            if (UIHelper.isIntentExisting(mActivity, intent)) {
                startActivityForResult(intent, REQUEST_CODE_GET_INSTALL_PERMISSION);
                //符合这个条件的就先不检查权限结果，等 onActivityResult 再检查
            }else{
                checkResultPermissions();
            }
        }else{
            checkResultPermissions();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O&&requestCode == REQUEST_CODE_GET_INSTALL_PERMISSION){
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
