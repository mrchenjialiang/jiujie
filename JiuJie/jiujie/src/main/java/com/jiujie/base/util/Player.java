package com.jiujie.base.util;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.SeekBar;

import com.jiujie.base.jk.MyHandlerInterface;

import java.util.Timer;
import java.util.TimerTask;

public class Player implements OnBufferingUpdateListener,
        OnCompletionListener, OnPreparedListener,
        SurfaceHolder.Callback, MyHandlerInterface {
    private int videoWidth;
    private int videoHeight;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;
    private SeekBar skbProgress;
    private View bufferView;
    private boolean hasPlayed = false;
    private Handler handleProgress;

    private OnPlayListen onPlayListen;
    private String videoUrl;

    public void setOnPlayListen(OnPlayListen onPlayListen) {
        this.onPlayListen = onPlayListen;
    }

    public interface OnPlayListen {
        void onPrepare(int width, int height);

        void onStart();

        void onPause();

        void onCompleted();

        void onStop();
    }

    public Player(SurfaceView surfaceView, final SeekBar skbProgress, View bufferView) {
        this.skbProgress = skbProgress;
        this.bufferView = bufferView;
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        handleProgress = new MyHandler(this);
        Timer mTimer = new Timer();
        /******************************************************
         通过定时器和Handler来更新进度条
         */
        TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (mediaPlayer == null || skbProgress == null)
                    return;
                if (mediaPlayer.isPlaying() && !skbProgress.isPressed()) {
                    handleProgress.sendEmptyMessage(0);
                }
            }
        };
        mTimer.schedule(mTimerTask, 0, 1000);
    }

    @Override
    public void handleMessage(Message msg) {
        int position = mediaPlayer.getCurrentPosition();
        int duration = mediaPlayer.getDuration();
        if (duration > 0) {
            long pos = skbProgress.getMax() * position / duration;
            skbProgress.setProgress((int) pos);
        }
    }

    public void play() {
        mediaPlayer.start();
        if (onPlayListen != null) onPlayListen.onStart();
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    /**
     * 播放网络视频
     */
    public void playUrl(String videoUrl) {
        playUrl(videoUrl, false);
    }

    public void playUrl(String videoUrl, boolean isLocal) {
        UIHelper.showLog("videoUrl:" + videoUrl);
        this.videoUrl = videoUrl;
        try {
            if (hasPlayed) {
                if (onPlayListen != null) onPlayListen.onStart();
                mediaPlayer.start();
                return;
            }
            mediaPlayer.reset();
            mediaPlayer.setDataSource(videoUrl);
            if (!isLocal) {
                mediaPlayer.prepareAsync();
            } else {
                mediaPlayer.prepare();
            }
            hasPlayed = true;
            bufferView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        if (TextUtils.isEmpty(videoUrl)) {
            return;
        }
        mediaPlayer.pause();
        if (onPlayListen != null) onPlayListen.onPause();
    }

    public void stop() {
        if (TextUtils.isEmpty(videoUrl)) {
            return;
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (onPlayListen != null) onPlayListen.onStop();
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    /**
     * 暂停后,又点击播放调用.
     */
    public void start() {
        if (TextUtils.isEmpty(videoUrl)) {
            return;
        }
        mediaPlayer.start();
        if (onPlayListen != null) onPlayListen.onStart();
    }

    /**
     * 释放MediaPlayer
     */
    public void release() {
        mediaPlayer.release();
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        Log.e("mediaPlayer", "surface changed");
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnInfoListener(new OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    switch (what) {
                        case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                            if (bufferView != null) bufferView.setVisibility(View.VISIBLE);
                            break;
                        case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                            if (bufferView != null) bufferView.setVisibility(View.GONE);
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            Log.e("mediaPlayer", "error", e);
        }
        Log.e("mediaPlayer", "surface created");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        Log.e("mediaPlayer", "surface destroyed");
    }


    /**
     * 通过onPrepared播放
     */
    @Override
    public void onPrepared(MediaPlayer arg0) {
        videoWidth = mediaPlayer.getVideoWidth();
        videoHeight = mediaPlayer.getVideoHeight();
        if (videoHeight != 0 && videoWidth != 0) {
            arg0.start();
            if (onPlayListen != null) onPlayListen.onPrepare(videoWidth, videoHeight);
        }
        bufferView.setVisibility(View.GONE);
        Log.e("mediaPlayer", "onPrepared");
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
        hasPlayed = false;
        if (onPlayListen != null) onPlayListen.onCompleted();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
//		Log.e("CJL", "已缓冲:"+bufferingProgress + "% buffer");
//		int playedProgress = mediaPlayer.getCurrentPosition()*100/mediaPlayer.getDuration();
//		Log.e("CJL", "已播放:"+playedProgress + "% play");
        if (skbProgress == null) return;
        skbProgress.setSecondaryProgress(bufferingProgress);
        int currentProgress = skbProgress.getMax() * mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();
        Log.e(currentProgress + "% play", bufferingProgress + "% buffer");

    }
}
