package com.jiujie.base.util.video;

import android.media.MediaPlayer;
import android.view.ViewGroup;

import com.jiujie.base.jk.OnSimpleListener;

/**
 * Created by ChenJiaLiang on 2018/5/8.
 * Email:576507648@qq.com
 */

public class VideoTextureViewUtil implements VideoController{
    private MyTextureView myTextureView;

    public VideoTextureViewUtil(MyTextureView myTextureView, boolean isLoop, boolean isAutoStart, ViewGroup containerView) {
        if(myTextureView==null)throw new NullPointerException("MyTextureView should not be null in VideoTextureViewUtil ");
        this.myTextureView = myTextureView;
        myTextureView.setSome(isLoop,isAutoStart,containerView);
    }

    public void setOnFirstSurfaceUpdateListener(OnSimpleListener onFirstSurfaceUpdateListener) {
        if(myTextureView !=null) myTextureView.setOnFirstSurfaceUpdateListener(onFirstSurfaceUpdateListener);
    }

    @Override
    public void setOnVideoStatusListener(OnVideoStatusListener onVideoStatusListener) {
        if(myTextureView !=null) myTextureView.setOnVideoStatusListener(onVideoStatusListener);
    }

    @Override
    public void setOnVideoPrepareListener(OnVideoPrepareListener onVideoPrepareListener) {
        if(myTextureView !=null) myTextureView.setOnVideoPrepareListener(onVideoPrepareListener);
    }

    @Override
    public boolean isPrepared() {
        return myTextureView != null && myTextureView.isPrepared();
    }

    @Override
    public void doPrepare(String videoPath, String thumbUrl) {
        if(myTextureView !=null) myTextureView.doPrepare(videoPath,thumbUrl);
    }

    @Override
    public void doStart(){
        if(myTextureView !=null) myTextureView.doStart();
    }

    @Override
    public void doPause(){
        if(myTextureView !=null) myTextureView.doPause();
    }

    @Override
    public boolean isPlaying() {
        return myTextureView != null && myTextureView.isPlaying();
    }

    @Override
    public void setVoice(float volume){
        if(myTextureView !=null) myTextureView.setVoice(volume);
    }

    @Override
    public MediaPlayer getMediaPlayer() {
        if(myTextureView ==null)return null;
        return myTextureView.getMediaPlayer();
    }

    @Override
    public void doSeekTo(int position) {
        if(myTextureView !=null) myTextureView.doSeekTo(position);
    }

    @Override
    public void doReStart() {
        if(myTextureView !=null) myTextureView.doReStart();
    }

    @Override
    public void doRelease() {
        if(myTextureView !=null) myTextureView.doRelease();
    }
}
