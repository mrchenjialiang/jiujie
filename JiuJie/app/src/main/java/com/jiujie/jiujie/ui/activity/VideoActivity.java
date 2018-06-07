package com.jiujie.jiujie.ui.activity;

import android.os.Environment;
import android.view.ViewGroup;

import com.jiujie.base.util.UIHelper;
import com.jiujie.base.util.video.OnVideoPrepareListener;
import com.jiujie.base.util.video.TextureVideoView;
import com.jiujie.base.util.video.VideoCacheUtil;
import com.jiujie.base.util.video.VideoUtil;
import com.jiujie.jiujie.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoActivity extends MyBaseActivity {

//    @Bind(R.id.v_videoView)
//    MyVideoView surfaceView;

//    @Bind(R.id.v_OriginalTextureVideoView)
//    OriginalTextureVideoView surfaceView;

    @Bind(R.id.v_TextureVideoView)
    TextureVideoView mVideoView;

//    private String videoPath = Environment.getExternalStorageDirectory()+"/Download/wallpaperfairy/liveWallpaper/111.mp4";
//    private String videoPath = Environment.getExternalStorageDirectory()+"/Download/wallpaperfairy/liveWallpaper/222.mp4";//问题火影
//    private String videoPath = "http://s.3987.com/uploadfile/userload/vedio/2018/0507/20180507121253269.mp4";//竖屏视频
//    private String videoPath = "http://s.3987.com/uploadfile/video/2018/0428/20180428093338723.mp4";//横屏视频
    private VideoUtil videoUtil;


//    private String videoPath = Environment.getExternalStorageDirectory()+"/question_huoying.mp4";//问题火影
    private String videoPath = Environment.getExternalStorageDirectory()+"/wanghong.mp4";//问题网红
//    private String videoPath = Environment.getExternalStorageDirectory()+"/video_heng.mp4";//横屏视频
//    private String videoPath = Environment.getExternalStorageDirectory()+"/normal.mp4";//普通视频

    @Override
    public void initUI() {
        ButterKnife.bind(this);
        mTitle.setLeftButtonBack();
        mTitle.setTitleText("视频播放");

//        videoUtil = new VideoUtil(surfaceView,true,true,(ViewGroup) surfaceView.getParent());
//        videoUtil.setOnVideoPrepareListener(new OnVideoPrepareListener() {
//            @Override
//            public void onPrepare(int videoWidth, int videoHeight) {
//                setLoadingEnd();
//            }
//        });


//        setLoading();
//        surfaceView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                setLoadingEnd();
//                View parent = (View) surfaceView.getParent();
//                surfaceView.setFixedSize(parent.getWidth(),parent.getHeight());
//            }
//        });
//        surfaceView.setVideoPath(VideoCacheUtil.instance().getVideoPath(videoPath));
//        surfaceView.start();

        UIHelper.showLog("videoPath:"+videoPath);

        setLoading();
        videoUtil = new VideoUtil(mVideoView,true,true,(ViewGroup) mVideoView.getParent());
        videoUtil.setOnVideoPrepareListener(new OnVideoPrepareListener() {
            @Override
            public void onPrepare(int videoWidth, int videoHeight) {
                setLoadingEnd();
            }
        });
        videoUtil.doPrepare(VideoCacheUtil.instance().getVideoPath(videoPath),null);
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
