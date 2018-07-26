package com.jiujie.jiujie.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.jiujie.base.activity.BaseListSimpleActivity;
import com.jiujie.base.jk.OnListener;
import com.jiujie.base.util.UIHelper;
import com.jiujie.base.util.permission.PermissionsManager;
import com.jiujie.jiujie.service.MyWallpaperServer;
import com.jiujie.jiujie.service.TransWallpaperServer;
import com.jiujie.jiujie.ui.activity.mediacodec.MediaCodecActivity;
import com.jiujie.jiujie.ui.activity.mediacodec.VideoDecoderDemo1Activity;
import com.jiujie.jiujie.ui.activity.mediacodec.decoder.VideoDecoderActivity;
import com.jiujie.jiujie.ui.adapter.MainAdapter;

import java.util.List;

public class MainActivity extends BaseListSimpleActivity<String, String> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        finish();
        startActivity(intent);
    }

    @Override
    public void initUI() {
        mTitle.setTitleText("JiuJie首页");

        PermissionsManager.requestWriteReadPermissions(new OnListener<Boolean>() {
            @Override
            public void onListen(Boolean isHasWriteReadPermission) {
                UIHelper.showLog("isHasWriteReadPermission:" + isHasWriteReadPermission);
            }
        });

        String model = Build.MODEL;
        UIHelper.showLog(this, "model:" + model);

        recyclerViewUtil.setRefreshEnable(false);
        recyclerViewUtil.setFooterEnable(false);
    }

    @Override
    public void initData() {
        dataList.add("视频播放");
        dataList.add("EXO视频播放");
        dataList.add("动态壁纸桌面");
        dataList.add("透明壁纸桌面");
        dataList.add("触摸滑动页面");
        dataList.add("Cs页面");
        dataList.add("音视频解码1");
        dataList.add("音视频解码2");
        dataList.add("音视频解码3");
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        MainAdapter mainAdapter = new MainAdapter(dataList);
        mainAdapter.setOnItemClickListener(new OnListener<Integer>() {
            @Override
            public void onListen(Integer position) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(mActivity, VideoActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(mActivity, VideoExoActivity.class));
                        break;
                    case 2:
                        MyWallpaperServer myWallpaperServer = new MyWallpaperServer();
                        myWallpaperServer.start(mActivity);
                        break;
                    case 3:
                        PermissionsManager.getPermissionSimple(new OnListener<Boolean>() {
                            @Override
                            public void onListen(Boolean aBoolean) {
                                TransWallpaperServer myWallpaperServer = new TransWallpaperServer();
                                myWallpaperServer.start(mActivity);
                            }
                        }, Manifest.permission.CAMERA, "android.hardware.camera", "android.hardware.camera.autofocus", "android.permission.FLASHLIGHT");
                        break;
                    case 4:
                        startActivity(new Intent(mActivity, TouchMoveActivity.class));
                        break;
                    case 5:
                        startActivity(new Intent(mActivity, CsActivity.class));
                        break;
                    case 6:
                        startActivity(new Intent(mActivity, MediaCodecActivity.class));
                        break;
                    case 7:
                        startActivity(new Intent(mActivity, VideoDecoderActivity.class));
                        break;
                    case 8:
                        startActivity(new Intent(mActivity, VideoDecoderDemo1Activity.class));
                        break;
                }
            }
        });
        return mainAdapter;
    }

    @Override
    public void refresh() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    protected List<String> analysisData(String result) {
        return null;
    }
}
