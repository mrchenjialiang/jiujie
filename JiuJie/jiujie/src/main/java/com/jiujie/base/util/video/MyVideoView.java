//package com.jiujie.base.util.video;
//
//import android.content.Context;
//import android.media.MediaPlayer;
//import android.util.AttributeSet;
//import android.view.ViewGroup;
//import android.widget.VideoView;
//
///**
// * Created by ChenJiaLiang on 2018/5/8.
// * Email:576507648@qq.com
// */
//public class MyVideoView extends VideoView implements VideoController{
//
//    private VideoUtil videoUtil;
//    private int videoWidth;
//    private int videoHeight;
//    private ViewGroup containerView;
//
//    public MyVideoView(Context context) {
//        super(context);
//    }
//
//    public MyVideoView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public MyVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
//    public void setVideoUtil(VideoUtil videoUtil){
//        this.videoUtil = videoUtil;
//    }
//
//    @Override
//    public void addOnVideoStatusListener(final OnVideoStatusListener onVideoStatusListener) {
//        if(videoUtil!=null)videoUtil.addOnVideoStatusListener(onVideoStatusListener);
//    }
//
//    @Override
//    public void addOnVideoPrepareListener(OnVideoPrepareListener onVideoPrepareListener) {
//        if(videoUtil!=null)videoUtil.addOnVideoPrepareListener(onVideoPrepareListener);
//    }
//
//    @Override
//    public boolean isPrepared() {
//        return videoUtil!=null&&videoUtil.isPrepared();
//    }
//
//    @Override
//    public void doPrepare(String videoPath, String thumbUrl) {
//        if(videoUtil!=null)videoUtil.doPrepare(videoPath,thumbUrl);
//    }
//
//    @Override
//    public void doStart() {
//        if(videoUtil!=null)videoUtil.doStart();
//    }
//
//    @Override
//    public void doPause() {
//        if(videoUtil!=null)videoUtil.doPause();
//    }
//
//    @Override
//    public void doSeekTo(int position) {
//        if(videoUtil!=null)videoUtil.doSeekTo(position);
//    }
//
//    @Override
//    public void doReStart() {
//        if(videoUtil!=null)videoUtil.doReStart();
//    }
//
//    @Override
//    public void doRelease() {
//        if(videoUtil!=null)videoUtil.doRelease();
//    }
//
//    @Override
//    public void setVoice(float volume) {
//        if(videoUtil!=null)videoUtil.setVoice(volume);
//    }
//
//    public void setFullScreen(ViewGroup containerView) {
//        this.containerView = containerView;
//        requestLayout();
//    }
//
//    @Override
//    public MediaPlayer getMediaPlayer() {
//        if(videoUtil!=null) return videoUtil.getMediaPlayer();
//        else return null;
//    }
//
//    public void setVideoSize(int videoWidth, int videoHeight){
//        if(this.videoWidth==videoWidth&&this.videoHeight==videoHeight){
//            return;
//        }
//        this.videoWidth = videoWidth;
//        this.videoHeight = videoHeight;
//        requestLayout();
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//        if(containerView!=null&&containerView.getHeight()!=0){
//            setMeasuredDimension(containerView.getWidth(),containerView.getHeight());
//        }
//    }
//}
