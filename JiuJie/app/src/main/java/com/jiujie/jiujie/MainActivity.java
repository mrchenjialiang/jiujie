package com.jiujie.jiujie;

import android.Manifest;
import android.content.Intent;
import android.view.View;

import com.jiujie.base.jk.OnListener;
import com.jiujie.base.jk.SimpleDownloadFileListen;
import com.jiujie.base.util.GetPictureUtil;
import com.jiujie.base.util.ImageUtil;
import com.jiujie.base.util.PermissionsManager;
import com.jiujie.base.util.UIHelper;
import com.jiujie.base.util.file.SystemDownloadUtil;
import com.jiujie.jiujie.autocompletetextview.AutoCompleteTextViewActivity;
import com.jiujie.jiujie.grouplist.GroupListActivity;
import com.jiujie.jiujie.service.JiuJieKeepService;
import com.jiujie.jiujie.service.MyWallpaperServer;
import com.jiujie.jiujie.service.TransWallpaperServer;

public class MainActivity extends MyBaseActivity {

    private String filePath = "/storage/emulated/0/shoujiduoduo/Wallpaper/壁纸多多图片缓存/1516478.jpg";


    @Override
    public void initUI() {
        mTitle.setTitleText("JiuJie首页");

        startService(new Intent(mActivity, JiuJieKeepService.class));
    }

    //    /storage/emulated/0/shoujiduoduo/Wallpaper/壁纸多多图片缓存/1516478.jpg

    @Override
    public void initData() {
//        getSupportFragmentManager().beginTransaction().add(R.id.main_frameLayout,new SimpleListFragment()).commit();

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    private String getCropOutPutDir() {
        return ImageUtil.instance().getCacheSDDic(mActivity) + "res/image/camera";
    }

    public void toSd(View view) {
        startActivity(new Intent(mActivity, VideoActivity.class));
    }

    public void doAction1(View view) {
        PermissionsManager.getPermissionSimple(new OnListener<Boolean>() {
            @Override
            public void onListen(Boolean isHasPermission) {
//                UIHelper
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        GetPictureUtil.onActivityResult(mActivity, requestCode, resultCode, data);
    }

    public void toGroupList(View view) {
        startActivity(new Intent(mActivity, GroupListActivity.class));
    }

    public void doDownload(View view) {
        String url = "http://www.5857.com/small/wallpaper_bizhi.apk";
        String name = "ty.apk";
//        String url = "http://d.5857.com/eeeo_180122/001.jpg";
//                String name = "1.jpg";
        new SystemDownloadUtil(url,ImageUtil.instance().getCacheSDDic(mActivity),name,new SimpleDownloadFileListen(){
            @Override
            public void onFail(String error) {
                UIHelper.showLog(UIHelper.isRunInUIThread());
                UIHelper.showToastShort(mActivity,error);
            }

            @Override
            public void onFinish(String filePath) {
                UIHelper.showLog(UIHelper.isRunInUIThread());
                UIHelper.showToastShort(mActivity,filePath);
            }

            @Override
            public void onLoading(long loadedLength, int progress) {
                UIHelper.showLog(UIHelper.isRunInUIThread());
                UIHelper.showLog("onLoading loadedLength:"+loadedLength+",progress:"+progress);
            }
        }).start();
    }

    public void toFrame(View view) {
        startActivity(new Intent(mActivity,FragmentActivity.class));
    }

    public void doWallpaper(View view) {
        startActivity(new Intent(mActivity,ImageActivity.class));
    }

    public void toScrollKeyTop(View view) {
        startActivity(new Intent(mActivity,ScrollKeepTopActivity.class));
    }

    public void toAutoCompleteTextView(View view) {
        startActivity(new Intent(mActivity,AutoCompleteTextViewActivity.class));
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
        },Manifest.permission.CAMERA,"android.hardware.camera","android.hardware.camera.autofocus","android.permission.FLASHLIGHT");
    }
}
