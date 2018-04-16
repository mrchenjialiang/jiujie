package com.jiujie.jiujie.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.view.View;

import com.jiujie.base.jk.OnListener;
import com.jiujie.base.jk.SimpleDownloadFileListen;
import com.jiujie.base.util.ImageUtil;
import com.jiujie.base.util.PermissionsManager;
import com.jiujie.base.util.UIHelper;
import com.jiujie.base.util.file.SystemDownloadUtil;
import com.jiujie.base.util.photo.GetPhotoUtil;
import com.jiujie.jiujie.R;
import com.jiujie.jiujie.autocompletetextview.AutoCompleteTextViewActivity;
import com.jiujie.jiujie.grouplist.GroupListActivity;
import com.jiujie.jiujie.service.MyWallpaperServer;
import com.jiujie.jiujie.service.TransWallpaperServer;

import java.util.List;

public class MainActivity extends MyBaseActivity {

    private String filePath = "/storage/emulated/0/shoujiduoduo/Wallpaper/壁纸多多图片缓存/1516478.jpg";
    private GetPhotoUtil getPhotoUtil;


    @Override
    public void initUI() {
        mTitle.setTitleText("JiuJie首页");
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

    public void getPhoto(View view) {
        if(getPhotoUtil==null){
            getPhotoUtil = new GetPhotoUtil(mActivity) {
                @Override
                public void onGetPhotoEnd(boolean isFromCamera, List<String> imagePathList) {
                    UIHelper.showToastShort(mActivity,"isFromCamera "+isFromCamera+",imagePathList size "+imagePathList.size());
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
        getPhotoUtil.onActivityResult(mActivity,requestCode,resultCode,data);
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

    public void toScrollKeepTop(View view) {
        startActivity(new Intent(mActivity,ScrollKeepTopSimpleActivity.class));
    }

    public void toScrollKeepTopGame(View view) {
        startActivity(new Intent(mActivity,ScrollKeepTopGameActivity.class));
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
