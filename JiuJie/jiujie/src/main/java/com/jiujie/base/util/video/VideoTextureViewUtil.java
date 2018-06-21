package com.jiujie.base.util.video;

import android.media.MediaPlayer;
import android.view.ViewGroup;

import com.jiujie.base.jk.OnSimpleListener;

/**
 * Created by ChenJiaLiang on 2018/5/8.
 * Email:576507648@qq.com
 */

public class VideoTextureViewUtil implements VideoController {
    private VideoController videoController;

    public VideoTextureViewUtil(MyTextureView myTextureView, boolean isLoop, boolean isAutoStart, ViewGroup containerView) {
        if (myTextureView == null)
            throw new NullPointerException("MyTextureView should not be null in VideoTextureViewUtil ");
        this.videoController = myTextureView;
        myTextureView.setSome(isLoop, isAutoStart, containerView);
    }

    public void setOnFirstSurfaceUpdateListener(OnSimpleListener onFirstSurfaceUpdateListener) {
        if (videoController != null && videoController instanceof MyTextureView)
            ((MyTextureView) videoController).setOnFirstSurfaceUpdateListener(onFirstSurfaceUpdateListener);
    }

    @Override
    public void addOnVideoStatusListener(OnVideoStatusListener onVideoStatusListener) {
        if (videoController != null)
            videoController.addOnVideoStatusListener(onVideoStatusListener);
    }

    @Override
    public void addOnVideoPrepareListener(OnVideoPrepareListener onVideoPrepareListener) {
        if (videoController != null)
            videoController.addOnVideoPrepareListener(onVideoPrepareListener);
    }

    @Override
    public boolean isPrepared() {
        return videoController != null && videoController.isPrepared();
    }

    @Override
    public boolean isPause() {
        return videoController != null && videoController.isPause();
    }

    @Override
    public void doPrepareUI(String videoPath, String thumbUrl) {
        if (videoController != null) videoController.doPrepareUI(videoPath, thumbUrl);
    }

    @Override
    public void doPrepare(String videoPath, String thumbUrl) {
        if (videoController != null) videoController.doPrepare(videoPath, thumbUrl);
    }

    @Override
    public void doStart() {
        if (videoController != null) videoController.doStart();
    }

    @Override
    public void doPause() {
        if (videoController != null) videoController.doPause();
    }

    @Override
    public boolean isPlaying() {
        return videoController != null && videoController.isPlaying();
    }

    @Override
    public void setVoice(float volume) {
        if (videoController != null) videoController.setVoice(volume);
    }

    @Override
    public MediaPlayer getMediaPlayer() {
        if (videoController == null) return null;
        return videoController.getMediaPlayer();
    }

    @Override
    public void doSeekTo(int position) {
        if (videoController != null) videoController.doSeekTo(position);
    }

    @Override
    public void doReStart() {
        if (videoController != null) videoController.doReStart();
    }

    @Override
    public void doRelease() {
        if (videoController != null) videoController.doRelease();
    }
}
