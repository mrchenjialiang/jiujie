package com.jiujie.jiujie.ui.activity;

import android.media.MediaPlayer;
import android.view.View;
import android.view.ViewParent;

import com.jiujie.jiujie.R;
import com.jiujie.jiujie.video.FixedTextureVideoView;
import com.jiujie.jiujie.video.VideoCacheUtil;
import com.jiujie.jiujie.video.VideoUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoActivity extends MyBaseActivity {

//    @Bind(R.id.v_videoView)
//    MyVideoView mVideoView;

    @Bind(R.id.v_FixedTextureVideoView)
    FixedTextureVideoView mVideoView;
//    private String videoPath = Environment.getExternalStorageDirectory()+"/Download/wallpaperfairy/liveWallpaper/111.mp4";
//    private String videoPath = Environment.getExternalStorageDirectory()+"/Download/wallpaperfairy/liveWallpaper/222.mp4";//问题火影
//    private String videoPath = "http://s.3987.com/uploadfile/userload/vedio/2018/0507/20180507121253269.mp4";//竖屏视频
    private String videoPath = "http://s.3987.com/uploadfile/video/2018/0428/20180428093338723.mp4";//横屏视频
//    private VideoUtil videoUtil;


//    private String videoPath = Environment.getExternalStorageDirectory()+"/横.mp4";//问题火影

    @Override
    public void initUI() {
        ButterKnife.bind(this);
        mTitle.setLeftButtonBack();
        mTitle.setTitleText("视频播放");

//        videoUtil = new VideoUtil(mVideoView,true,true,(ViewGroup) mVideoView.getParent());
//        videoUtil.setOnVideoPrepareListener(new OnVideoPrepareListener() {
//            @Override
//            public void onPrepare(int videoWidth, int videoHeight) {
//                setLoadingEnd();
//            }
//        });


        setLoading();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                setLoadingEnd();
                View parent = (View) mVideoView.getParent();
                mVideoView.setFixedSize(parent.getWidth(),parent.getHeight());
            }
        });
        mVideoView.setVideoPath(VideoCacheUtil.instance().getVideoPath(videoPath));
        mVideoView.start();
    }

    @Override
    public void initData() {
//        setLoading();
//        videoUtil.doPrepare(VideoCacheUtil.instance().getVideoPath(videoPath));
    }

    @OnClick(R.id.v_video_controller)
    public void onVideoControllerClick(){
        if(mVideoView.isPlaying()){
            mVideoView.pause();
        }else{
            mVideoView.start();
        }
//        if(videoUtil.isPlaying()){
//            videoUtil.doPause();
//        }else{
//            videoUtil.doStart();
//        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_video;
    }
}
