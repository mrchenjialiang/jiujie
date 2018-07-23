package com.jiujie.jiujie.ui.activity;

import android.os.Environment;
import android.view.ViewGroup;

import com.jiujie.base.util.UIHelper;
import com.jiujie.base.util.video.MyTextureView;
import com.jiujie.base.util.video.OnVideoPrepareListener;
import com.jiujie.base.util.video.VideoCacheUtil;
import com.jiujie.base.util.video.VideoTextureViewUtil;
import com.jiujie.jiujie.R;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoActivity extends MyBaseActivity {

    @Bind(R.id.v_MyTextureView)
    MyTextureView myTextureView;

//    private String videoPath = Environment.getExternalStorageDirectory()+"/Download/wallpaperfairy/liveWallpaper/111.mp4";
//    private String videoPath = Environment.getExternalStorageDirectory()+"/Download/wallpaperfairy/liveWallpaper/222.mp4";//问题火影
//    private String videoPath = "http://s.3987.com/uploadfile/userload/vedio/2018/0507/20180507121253269.mp4";//竖屏视频
//    private String videoPath = "http://s.3987.com/uploadfile/video/2018/0428/20180428093338723.mp4";//横屏视频
    private VideoTextureViewUtil videoUtil;
    private boolean isPause;


//    private String videoPath = Environment.getExternalStorageDirectory()+"/question_huoying.mp4";//问题火影
//    private String videoPath = Environment.getExternalStorageDirectory()+"/wanghong.mp4";//问题网红
//    private String videoPath = Environment.getExternalStorageDirectory()+"/video_heng.mp4";//横屏视频
//    private String videoPath = Environment.getExternalStorageDirectory()+"/normal.mp4";//普通视频
    private String videoPath = Environment.getExternalStorageDirectory()+"/111111.mp4";//伪横屏视频
//    private String videoPath = "/storage/emulated/0/DCIM/Camera/00b25e14a5ed3af4059e8341cb54f497.mp4";//R11本机视频

    @Override
    public void initUI() {
        ButterKnife.bind(this);
        mTitle.setLeftButtonBack();
        mTitle.setTitleText("视频播放");

        UIHelper.showLog("videoPath:"+videoPath);
        String name = new File(videoPath).getName();
        UIHelper.showLog("name:"+name);

        setLoading();
        videoUtil = new VideoTextureViewUtil(myTextureView,true,true,(ViewGroup) myTextureView.getParent());
        videoUtil.addOnVideoPrepareListener(new OnVideoPrepareListener() {
            @Override
            public void onPrepare(int videoWidth, int videoHeight) {
                UIHelper.showLog("videoWidth:"+videoWidth);
                UIHelper.showLog("videoHeight:"+videoHeight);
                setLoadingEnd();
            }
        });
        videoUtil.doPrepare(VideoCacheUtil.instance().getVideoPath(videoPath),null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isPause){
            isPause = false;
            if(videoUtil!=null){
                videoUtil.doStart();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
        if(videoUtil!=null){
            videoUtil.doPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(videoUtil!=null){
            videoUtil.doRelease();
        }
    }

    @Override
    public void initData() {
//        setLoading();
//        videoUtil.doPrepare(VideoCacheUtil.instance().getVideoPath(videoPath));
    }

    @OnClick(R.id.v_video_controller)
    public void onVideoControllerClick(){
//        if(surfaceView.isPlaying()){
//            surfaceView.pause();
//        }else{
//            surfaceView.start();
//        }
        if(videoUtil.isPlaying()){
            videoUtil.doPause();
        }else{
            videoUtil.doStart();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_video;
    }
}
