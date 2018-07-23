package com.jiujie.jiujie.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.view.View;

import com.jiujie.base.WaitingDialog;
import com.jiujie.base.jk.OnListener;
import com.jiujie.base.jk.OnSimpleListener;
import com.jiujie.base.util.UIHelper;
import com.jiujie.base.util.permission.PermissionsManager;
import com.jiujie.base.util.photo.GetPhotoUtil;
import com.jiujie.jiujie.R;
import com.jiujie.jiujie.autocompletetextview.AutoCompleteTextViewActivity;
import com.jiujie.jiujie.grouplist.GroupListActivity;
import com.jiujie.jiujie.service.MyWallpaperServer;
import com.jiujie.jiujie.service.TransWallpaperServer;

import java.io.File;
import java.util.List;

public class MainActivity extends MyBaseActivity {

    private String filePath = "/storage/emulated/0/shoujiduoduo/Wallpaper/壁纸多多图片缓存/1516478.jpg";
    private GetPhotoUtil getPhotoUtil;


    @Override
    public void initUI() {
        mTitle.setTitleText("JiuJie首页");
        int screenWidth = UIHelper.getScreenWidth(mActivity);
        int screenHeight = UIHelper.getScreenHeight(mActivity);
        UIHelper.showLog("screenWidth:" + screenWidth);
        UIHelper.showLog("screenHeight:" + screenHeight);

        File externalCacheDir = getExternalCacheDir();
        File cacheDir = getCacheDir();
//        externalCacheDir:/storage/emulated/0/Android/data/com.jiujie.demo/cache
//        cacheDir:/data/user/0/com.jiujie.demo/cache
        UIHelper.showLog("externalCacheDir:" + externalCacheDir);
        UIHelper.showLog("cacheDir:" + cacheDir);

        PermissionsManager.requestWriteReadPermissions(new OnListener<Boolean>() {
            @Override
            public void onListen(Boolean isHasWriteReadPermission) {
                UIHelper.showLog("isHasWriteReadPermission:" + isHasWriteReadPermission);
            }
        });

        String model = Build.MODEL;
        UIHelper.showLog(this, "model:" + model);

        UIHelper.showLog(this, "externalStoragePublicDirectory DIRECTORY_DCIM:" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath());
        UIHelper.showLog(this, "externalStoragePublicDirectory DIRECTORY_MOVIES:" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath());

        UIHelper.showToastShort("create");
        final WaitingDialog waitingDialog = new WaitingDialog(mActivity);
        waitingDialog.create();
        waitingDialog.show();
        addOnDestroyListener(new OnSimpleListener() {
            @Override
            public void onListen() {
                UIHelper.showToastShort("onDestroy");
                if (waitingDialog.isShowing()) {
                    waitingDialog.dismiss();
                }
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    UIHelper.showToastShort("Thread.sleep 3000");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        UIHelper.showToastShort("onPause");
    }

    //    /storage/emulated/0/shoujiduoduo/Wallpaper/壁纸多多图片缓存/1516478.jpg

    @Override
    public void initData() {
//        getSupportFragmentManager().beginTransaction().add(R.id.main_frameLayout,new SimpleListFragment()).commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    public void getPhoto(View view) {
        if (getPhotoUtil == null) {
            getPhotoUtil = new GetPhotoUtil(mActivity) {
                @Override
                public void onGetPhotoEnd(boolean isFromCamera, List<String> imagePathList) {
                    UIHelper.showToastShort("isFromCamera " + isFromCamera + ",imagePathList size " + imagePathList.size());
                }

                @Override
                public boolean isShouldCrop() {
                    return true;
                }

                @Override
                public float getCropScaleHeight() {
                    return 1;
                }
            };
        }
        getPhotoUtil.showChooseImageDialog();
    }

    public void doCropImage(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean canRequestPackageInstalls = getPackageManager().canRequestPackageInstalls();
            UIHelper.showLog("8.0安装权限 canRequestPackageInstalls：" + canRequestPackageInstalls);
            PermissionsManager.getPermissionSimple(new OnListener<Boolean>() {
                @Override
                public void onListen(Boolean isHas) {
                    UIHelper.showLog("8.0安装权限：" + isHas);
                    boolean canRequestPackageInstalls = getPackageManager().canRequestPackageInstalls();
                    UIHelper.showLog("8.0安装权限 canRequestPackageInstalls1：" + canRequestPackageInstalls);
                }
            }, Manifest.permission.REQUEST_INSTALL_PACKAGES);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getPhotoUtil != null)
            getPhotoUtil.onActivityResult(mActivity, requestCode, resultCode, data);
        if (requestCode == 11111) {

        }
    }

    public void toGroupList(View view) {
        startActivity(new Intent(mActivity, GroupListActivity.class));
    }

    public void doDownload(View view) {
        String url = "http://www.5857.com/small/wallpaper_bizhi.apk";
        String name = "ty.apk";
//        String url = "http://d.5857.com/eeeo_180122/001.jpg";
//                String name = "1.jpg";
//        new SystemDownloadUtil(url,ImageUtil.instance().getCacheSDDic(mActivity),name,new SimpleDownloadFileListen(){
//            @Override
//            public void onFail(String error) {
//                UIHelper.showLog(UIHelper.isRunInUIThread());
//                UIHelper.showToastShort(error);
//            }
//
//            @Override
//            public void onFinish(String filePath) {
//                UIHelper.showLog(UIHelper.isRunInUIThread());
//                UIHelper.showToastShort(filePath);
//            }
//
//            @Override
//            public void onLoading(long loadedLength, int progress) {
//                UIHelper.showLog(UIHelper.isRunInUIThread());
//                UIHelper.showLog("onLoading loadedLength:"+loadedLength+",progress:"+progress);
//            }
//        }).start();
    }

    public void toFrame(View view) {
        startActivity(new Intent(mActivity, FragmentActivity.class));
    }

    int count;

    public void doWallpaper(View view) {
        count++;
        UIHelper.showToastShort("doWallpaper click " + count);
//        startActivity(new Intent(mActivity,ImageActivity.class));
    }

    public void toScrollKeepTop(View view) {
        startActivity(new Intent(mActivity, ScrollKeepTopSimpleActivity.class));
    }

    public void toScrollKeepTopGame(View view) {
        startActivity(new Intent(mActivity, ScrollKeepTopGameActivity.class));
    }

    public void toAutoCompleteTextView(View view) {
        startActivity(new Intent(mActivity, AutoCompleteTextViewActivity.class));
    }

    public void startLiveWallpaper(View view) {
        MyWallpaperServer myWallpaperServer = new MyWallpaperServer();
        myWallpaperServer.start(mActivity);
    }

    public void startTransLiveWallpaper(View view) {
        PermissionsManager.getPermissionSimple(new OnListener<Boolean>() {
            @Override
            public void onListen(Boolean aBoolean) {
                TransWallpaperServer myWallpaperServer = new TransWallpaperServer();
                myWallpaperServer.start(mActivity);
            }
        }, Manifest.permission.CAMERA, "android.hardware.camera", "android.hardware.camera.autofocus", "android.permission.FLASHLIGHT");
    }

    public void toVideo(View view) {
        startActivity(new Intent(mActivity, VideoActivity.class));
    }

    public void toVideoExo(View view) {
        startActivity(new Intent(mActivity, VideoExoActivity.class));
    }

    public void toVideoMediaPlayer(View view) {
//        startActivity(new Intent(mActivity,VideoMediaPlayerActivity.class));
    }

    public void toAdCs(View view) {
//        startActivity(new Intent(mActivity,AdCsActivity.class));
    }

    public void toVideoThumb(View view) {
        startActivity(new Intent(mActivity, VideoThumbActivity.class));
    }

    public void toVideoCrop(View view) {

    }

    public void toTouchMove(View view) {
        startActivity(new Intent(mActivity, TouchMoveActivity.class));
    }

    public void toCs(View view) {
        startActivity(new Intent(mActivity, CsActivity.class));
    }
}
