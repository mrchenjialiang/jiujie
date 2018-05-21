package com.jiujie.jiujie.video;

import android.media.MediaPlayer;
import android.view.SurfaceView;
import android.view.ViewGroup;

/**
 * Created by ChenJiaLiang on 2018/5/8.
 * Email:576507648@qq.com
 */
public class VideoUtil implements VideoController{
    private VideoController videoController;

    public VideoUtil(MyVideoView mVideoView,boolean isLoop,boolean isAutoStart,ViewGroup containerView) {
        videoController = new VideoViewUtil(mVideoView,isLoop,isAutoStart,containerView);
        if(mVideoView!=null)mVideoView.setVideoUtil(this);
    }

    public VideoUtil(SurfaceView surfaceView,boolean isLoop,boolean isAutoStart,ViewGroup containerView) {
        videoController = new SurfaceViewUtil(surfaceView,isLoop,isAutoStart,containerView);
    }

    @Override
    public void setOnVideoStatusListener(OnVideoStatusListener onVideoStatusListener) {
        videoController.setOnVideoStatusListener(onVideoStatusListener);
    }

    @Override
    public void setOnVideoPrepareListener(OnVideoPrepareListener onVideoPrepareListener) {
        videoController.setOnVideoPrepareListener(onVideoPrepareListener);
    }

    @Override
    public void doPrepare(String url) {
        videoController.doPrepare(url);
    }

    @Override
    public void doStart(){
        videoController.doStart();
    }

    @Override
    public void doPause(){
        videoController.doPause();
    }

    @Override
    public boolean isPlaying(){
        return videoController.isPlaying();
    }

    @Override
    public void setVoice(float volume){
        videoController.setVoice(volume);
    }

    @Override
    public MediaPlayer getMediaPlayer() {
        return videoController.getMediaPlayer();
    }

    @Override
    public void doSeekTo(int position) {
        videoController.doSeekTo(position);
    }

    @Override
    public void doReStart() {
        videoController.doReStart();
    }

    @Override
    public void doRelease() {
        videoController.doRelease();
    }

}
