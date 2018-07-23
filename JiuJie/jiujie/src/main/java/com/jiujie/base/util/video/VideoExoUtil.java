package com.jiujie.base.util.video;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.jiujie.base.util.UIHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenJiaLiang on 2018/5/8.
 * Email:576507648@qq.com
 */

public class VideoExoUtil implements VideoController {
    private final String TAG = "VideoController VideoExoUtil";
    private final Context context;
    private final VideoScaleType videoScaleType;
    private Surface surface;
    private SurfaceView surfaceView;
    private TextureView textureView;
    private SurfaceHolder surfaceHolder;
    private boolean isLoop;
    private boolean isAutoStart;
    private SimpleExoPlayer simpleExoPlayer;
    private List<OnVideoStatusListener> videoStatusListenerList;
    private List<OnVideoPrepareListener> videoPrepareListenerList;
    private float volume = -1;
    private boolean isPrepared;
    private boolean isPause;
    private int videoWidth;
    private int videoHeight;

    public enum VideoScaleType {
        SCALE_FILL_XY, SCALE_CROP_CENTER
    }

    public VideoExoUtil(Context context, Surface surface, VideoScaleType videoScaleType) {
        this(context, surface, true, true, videoScaleType);
    }

    public VideoExoUtil(Context context, Surface surface, boolean isLoop, VideoScaleType videoScaleType) {
        this(context, surface, isLoop, true, videoScaleType);
    }

    public VideoExoUtil(Context context, Surface surface, boolean isLoop, boolean isAutoStart, VideoScaleType videoScaleType) {
        this.context = context;
        this.surface = surface;
        this.isLoop = isLoop;
        this.isAutoStart = isAutoStart;
        this.videoScaleType = videoScaleType;
        init();
    }

    public VideoExoUtil(Context context, SurfaceView surfaceView, boolean isLoop, boolean isAutoStart, VideoScaleType videoScaleType) {
        this.context = context;
        this.surfaceView = surfaceView;
        this.isLoop = isLoop;
        this.isAutoStart = isAutoStart;
        this.videoScaleType = videoScaleType;
        init();
    }

    public VideoExoUtil(Context context, TextureView textureView, boolean isLoop, boolean isAutoStart, VideoScaleType videoScaleType) {
        this.context = context;
        this.textureView = textureView;
        this.isLoop = isLoop;
        this.isAutoStart = isAutoStart;
        this.videoScaleType = videoScaleType;
        init();
    }

    public VideoExoUtil(Context context, SurfaceHolder surfaceHolder, VideoScaleType videoScaleType) {
        this(context, surfaceHolder, true, true, videoScaleType);
    }

    public VideoExoUtil(Context context, SurfaceHolder surfaceHolder, boolean isLoop, VideoScaleType videoScaleType) {
        this(context, surfaceHolder, isLoop, true, videoScaleType);
    }

    public VideoExoUtil(Context context, SurfaceHolder surfaceHolder, boolean isLoop, boolean isAutoStart, VideoScaleType videoScaleType) {
        this.context = context;
        this.surfaceHolder = surfaceHolder;
        this.isLoop = isLoop;
        this.isAutoStart = isAutoStart;
        this.videoScaleType = videoScaleType;
        init();
    }

    public void setSurfaceHolder(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        simpleExoPlayer.setVideoSurfaceHolder(surfaceHolder);
    }

    public void setSurfaceView(SurfaceView surfaceView) {
        this.surfaceView = surfaceView;
        simpleExoPlayer.setVideoSurfaceView(surfaceView);
    }

    public void setTextureView(TextureView textureView) {
        this.textureView = textureView;
        simpleExoPlayer.setVideoTextureView(textureView);
    }

    public void setSurface(Surface surface) {
        this.surface = surface;
        simpleExoPlayer.setVideoSurface(surface);
    }

    private void init() {
        // 1 create a default TrackSelector
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create the player
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        //循环播放配置
        simpleExoPlayer.setRepeatMode(isLoop ? Player.REPEAT_MODE_ALL : Player.REPEAT_MODE_OFF);
        //视频缩放配置
        if (videoScaleType == VideoScaleType.SCALE_FILL_XY) {
            simpleExoPlayer.setVideoScalingMode(1);//fill xy
        } else if (videoScaleType == VideoScaleType.SCALE_CROP_CENTER) {
            simpleExoPlayer.setVideoScalingMode(2);//crop center
        }

        simpleExoPlayer.setPlayWhenReady(isAutoStart);

//        bindPlayerToView();
        simpleExoPlayer.addVideoListener(new SimpleExoPlayer.VideoListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                videoWidth = width;
                videoHeight = height;
                UIHelper.showLog(TAG, "onVideoSizeChanged width:" + width + ",height:" + height + ",unappliedRotationDegrees:" + unappliedRotationDegrees + ",pixelWidthHeightRatio:" + pixelWidthHeightRatio);
            }

            @Override
            public void onRenderedFirstFrame() {
                UIHelper.showLog(TAG, "onRenderedFirstFrame");
            }
        });
        simpleExoPlayer.addListener(new Player.EventListener() {

            @Override
            public void onTimelineChanged(Timeline timeline, Object o, int i) {
                UIHelper.showLog(TAG, "addListener onTimelineChanged timeline:" + timeline.toString());
                UIHelper.showLog(TAG, "addListener onTimelineChanged getPeriodCount:" + timeline.getPeriodCount());
                UIHelper.showLog(TAG, "addListener onTimelineChanged o:" + o);
                UIHelper.showLog(TAG, "addListener onTimelineChanged i:" + i);
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroupArray, TrackSelectionArray trackSelectionArray) {
                UIHelper.showLog(TAG, "addListener onTracksChanged");
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                UIHelper.showLog(TAG, "addListener onLoadingChanged isLoading:" + isLoading);
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playWhenReady) {
                    if (volume >= 0) simpleExoPlayer.setVolume(volume);
                }
                UIHelper.showLog(TAG, "addListener onPlayerStateChanged playWhenReady:" + playWhenReady + ",playbackState:" + playbackState);
                switch (playbackState) {
                    case ExoPlayer.STATE_IDLE://1
                        UIHelper.showLog(TAG, "addListener onPlayerStateChanged idle!");
                        //一般不会进入这里，是发生错误了，取消播放了，才空闲进入这里
                        if (videoStatusListenerList != null) {
                            for (OnVideoStatusListener videoStatusListener : videoStatusListenerList) {
                                videoStatusListener.onError(null, "文件损坏或解码异常");
                            }
                        }
                        break;
                    case ExoPlayer.STATE_BUFFERING://2
                        UIHelper.showLog(TAG, "addListener onPlayerStateChanged buffering true");
                        if (videoStatusListenerList != null) {
                            for (OnVideoStatusListener videoStatusListener : videoStatusListenerList) {
                                videoStatusListener.onBufferListen(true);
                            }
                        }
                        break;
                    case ExoPlayer.STATE_READY://3
                        if (!isPrepared) {
                            isPrepared = true;
                            UIHelper.showLog(TAG, "addListener onPlayerStateChanged isPrepared true");

                            if (videoStatusListenerList != null) {
                                for (OnVideoStatusListener videoStatusListener : videoStatusListenerList) {
                                    videoStatusListener.onPrepare(videoWidth, videoHeight);
                                }
                            }

                            if (videoPrepareListenerList != null) {
                                for (OnVideoPrepareListener onPreparedListener : videoPrepareListenerList) {
                                    onPreparedListener.onPrepare(videoWidth, videoHeight);
                                }
                            }

                            if (videoStatusListenerList != null) {
                                for (OnVideoStatusListener videoStatusListener : videoStatusListenerList) {
                                    videoStatusListener.onStart();
                                }
                            }
                        }
                        UIHelper.showLog(TAG, "addListener onPlayerStateChanged buffering false");
                        if (videoStatusListenerList != null) {
                            for (OnVideoStatusListener videoStatusListener : videoStatusListenerList) {
                                videoStatusListener.onBufferListen(false);
                            }
                        }
                        break;
                    case ExoPlayer.STATE_ENDED://4
                        UIHelper.showLog(TAG, "Playback ended!");
                        if (isLoop) {
                            simpleExoPlayer.seekTo(0);
                        }
                        if (videoStatusListenerList != null) {
                            for (OnVideoStatusListener videoStatusListener : videoStatusListenerList) {
                                videoStatusListener.onCompleted(null);
                            }
                        }
                        break;
                }
            }

            @Override
            public void onRepeatModeChanged(int i) {
                UIHelper.showLog(TAG, "addListener onRepeatModeChanged");
            }

            @Override
            public void onShuffleModeEnabledChanged(boolean b) {
                UIHelper.showLog(TAG, "addListener onShuffleModeEnabledChanged b:" + b);
            }

            @Override
            public void onPlayerError(ExoPlaybackException e) {
                e.printStackTrace();
                UIHelper.showLog(TAG, "addListener onPlayerError " + e);
                if (videoStatusListenerList != null) {
                    for (OnVideoStatusListener videoStatusListener : videoStatusListenerList) {
                        videoStatusListener.onError(null, e.getMessage());
                    }
                }
            }

            @Override
            public void onPositionDiscontinuity(int i) {
                UIHelper.showLog(TAG, "addListener onPositionDiscontinuity " + i);
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                UIHelper.showLog(TAG, "addListener onPlaybackParametersChanged");
            }

            @Override
            public void onSeekProcessed() {
                UIHelper.showLog(TAG, "addListener onSeekProcessed");
            }
        });
    }

    private void bindPlayerToView() {
        // bind the player to the view
        if (surfaceView != null) {
            simpleExoPlayer.setVideoSurfaceView(surfaceView);
        } else if (surface != null) {
            simpleExoPlayer.setVideoSurface(surface);
        } else if (textureView != null) {
            simpleExoPlayer.setVideoTextureView(textureView);
        } else if (surfaceHolder != null) {
            simpleExoPlayer.setVideoSurfaceHolder(surfaceHolder);
        } else {
            throw new NullPointerException("VideoExoUtil init should have a surfaceView or surface or textureView or surfaceHolder");
        }
//        simpleExoPlayerView.setPlayer(simpleExoPlayer);
    }

    public SimpleExoPlayer getSimpleExoPlayer() {
        return simpleExoPlayer;
    }

    @Override
    public void addOnVideoStatusListener(OnVideoStatusListener onVideoStatusListener) {
        if (videoStatusListenerList == null) {
            videoStatusListenerList = new ArrayList<>();
        }
        videoStatusListenerList.add(onVideoStatusListener);
    }

    @Override
    public void addOnVideoPrepareListener(OnVideoPrepareListener onVideoPrepareListener) {
        if (videoPrepareListenerList == null) {
            videoPrepareListenerList = new ArrayList<>();
        }
        videoPrepareListenerList.add(onVideoPrepareListener);
    }

    @Override
    public boolean isPrepared() {
        return isPrepared;
    }

    @Override
    public boolean isPause() {
        return isPause;
    }

    @Override
    public void doPrepareUI(String videoPath, String thumbUrl) {

    }

    @Override
    public void doPrepare(String videoPath, String thumbUrl) {
        if (simpleExoPlayer == null) return;
        doPrepareUI(videoPath, thumbUrl);
        if (TextUtils.isEmpty(videoPath)) {
            return;
        }
        isPrepared = false;
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, context.getPackageName()), new DefaultBandwidthMeter());
        ExtractorMediaSource.Factory factory = new ExtractorMediaSource.Factory(dataSourceFactory)
                .setCustomCacheKey(videoPath)
//                .setContinueLoadingCheckIntervalBytes(500 * 1024)
                .setTag(videoPath);
        ExtractorMediaSource mediaSource = factory.createMediaSource(Uri.parse(videoPath));
        // 准备播放
        simpleExoPlayer.prepare(mediaSource);

        bindPlayerToView();
    }

    @Override
    public void doStart() {
        if (simpleExoPlayer == null) return;
        simpleExoPlayer.setPlayWhenReady(true);
        isPause = false;

        if (videoStatusListenerList != null) {
            for (OnVideoStatusListener videoStatusListener : videoStatusListenerList) {
                videoStatusListener.onStart();
            }
        }
    }

    @Override
    public void doPause() {
        if (simpleExoPlayer == null) return;
        simpleExoPlayer.setPlayWhenReady(false);
        isPause = true;

        if (videoStatusListenerList != null) {
            for (OnVideoStatusListener videoStatusListener : videoStatusListenerList) {
                videoStatusListener.onPause();
            }
        }
    }

    @Override
    public boolean isPlaying() {
        return simpleExoPlayer.getPlayWhenReady();
    }

    @Override
    public void setVoice(float volume) {
        this.volume = volume;
        if (simpleExoPlayer == null) return;
        simpleExoPlayer.setVolume(volume);
    }

    @Override
    public MediaPlayer getMediaPlayer() {
        return null;
    }

    @Override
    public void doSeekTo(int timeMillis) {
        if (simpleExoPlayer == null) return;
        simpleExoPlayer.seekTo(timeMillis);
    }

    public int getDuration() {
        if (simpleExoPlayer == null) return 0;
        return (int) simpleExoPlayer.getDuration();
    }

    @Override
    public void doReStart() {
        if (simpleExoPlayer != null) simpleExoPlayer.seekTo(0);
//        doPrepare(videoPath, thumbUrl);
    }

    @Override
    public void doRelease() {
        if (simpleExoPlayer == null) return;

        if (surfaceView != null) {
            simpleExoPlayer.clearVideoSurfaceView(surfaceView);
            surfaceView = null;
        } else if (surface != null) {
            simpleExoPlayer.clearVideoSurface(surface);
            surface = null;
        } else if (textureView != null) {
            simpleExoPlayer.clearVideoTextureView(textureView);
            textureView = null;
        } else if (surfaceHolder != null) {
            simpleExoPlayer.clearVideoSurfaceHolder(surfaceHolder);
            surfaceHolder = null;
        }

        simpleExoPlayer.release();
        simpleExoPlayer = null;

        if (videoStatusListenerList != null) {
            videoStatusListenerList.clear();
            videoStatusListenerList = null;
        }
        if (videoPrepareListenerList != null) {
            videoPrepareListenerList.clear();
            videoPrepareListenerList = null;
        }
    }

}
