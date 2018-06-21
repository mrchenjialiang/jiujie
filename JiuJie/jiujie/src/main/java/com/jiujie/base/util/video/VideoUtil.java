//package com.jiujie.base.util.video;
//
//import android.media.MediaPlayer;
//import android.view.SurfaceView;
//import android.view.ViewGroup;
//
///**
// * Created by ChenJiaLiang on 2018/5/8.
// * Email:576507648@qq.com
// */
//public class VideoUtil implements VideoController{
//    private VideoController videoController;
//
//    public VideoUtil(MyVideoView mVideoView,boolean isLoop,boolean isAutoStart,ViewGroup containerView) {
//        videoController = new VideoMyVideoViewUtil(mVideoView,isLoop,isAutoStart,containerView);
//        if(mVideoView!=null)mVideoView.setVideoUtil(this);
//    }
//
//    public VideoUtil(SurfaceView surfaceView,boolean isLoop,boolean isAutoStart,ViewGroup containerView) {
//        videoController = new VideoSurfaceViewUtil(surfaceView,isLoop,isAutoStart,containerView);
//    }
//
//    public VideoUtil(TextureVideoView textureVideoView,boolean isLoop,boolean isAutoStart,ViewGroup containerView) {
//        videoController = new VideoTextureViewUtil(textureVideoView,isLoop,isAutoStart,containerView);
//    }
//
//    public VideoUtil(VideoController videoController) {
//        this.videoController = videoController;
//    }
//
//    @Override
//    public void addOnVideoStatusListener(OnVideoStatusListener onVideoStatusListener) {
//        videoController.addOnVideoStatusListener(onVideoStatusListener);
//    }
//
//    @Override
//    public void addOnVideoPrepareListener(OnVideoPrepareListener onVideoPrepareListener) {
//        videoController.addOnVideoPrepareListener(onVideoPrepareListener);
//    }
//
//    @Override
//    public boolean isPrepared() {
//        return videoController.isPrepared();
//    }
//
//    @Override
//    public void doPrepare(String videoPath, String thumbUrl) {
//        videoController.doPrepare(videoPath,thumbUrl);
//    }
//
//    @Override
//    public void doStart(){
//        videoController.doStart();
//    }
//
//    @Override
//    public void doPause(){
//        videoController.doPause();
//    }
//
//    @Override
//    public boolean isPlaying(){
//        return videoController.isPlaying();
//    }
//
//    @Override
//    public void setVoice(float volume){
//        videoController.setVoice(volume);
//    }
//
//    @Override
//    public MediaPlayer getMediaPlayer() {
//        return videoController.getMediaPlayer();
//    }
//
//    @Override
//    public void doSeekTo(int position) {
//        videoController.doSeekTo(position);
//    }
//
//    @Override
//    public void doReStart() {
//        videoController.doReStart();
//    }
//
//    @Override
//    public void doRelease() {
//        videoController.doRelease();
//    }
//
//}
