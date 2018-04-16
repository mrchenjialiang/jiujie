package com.jiujie.jiujie.ui.activity;

import android.os.Environment;
import android.widget.VideoView;

import com.jiujie.jiujie.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VideoActivity extends MyBaseActivity {

    @Bind(R.id.v_videoView)
    VideoView mVideoView;
//    private String videoPath = Environment.getExternalStorageDirectory()+"/Download/wallpaperfairy/liveWallpaper/111.mp4";
    private String videoPath = Environment.getExternalStorageDirectory()+"/Download/wallpaperfairy/liveWallpaper/222.mp4";

    @Override
    public void initUI() {
        ButterKnife.bind(this);
        mTitle.setLeftButtonBack();
        mTitle.setTitleText("视频播放");
    }

    @Override
    public void initData() {
//设置视频路径
        mVideoView.setVideoPath(videoPath);
//        mVideoView.setVideoURI(Uri.parse(VideoCacheUtil.getPlayUrl(mVideoUrl)));
        //开始播放视频
        mVideoView.start();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_video;
    }
}
