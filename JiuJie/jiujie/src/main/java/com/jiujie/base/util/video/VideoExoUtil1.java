//package com.jiujie.base.util.video;
//
//import android.content.Context;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.text.TextUtils;
//import android.view.Surface;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.TextureView;
//
//import com.google.android.exoplayer2.ExoPlaybackException;
//import com.google.android.exoplayer2.ExoPlayer;
//import com.google.android.exoplayer2.ExoPlayerFactory;
//import com.google.android.exoplayer2.PlaybackParameters;
//import com.google.android.exoplayer2.Player;
//import com.google.android.exoplayer2.SimpleExoPlayer;
//import com.google.android.exoplayer2.Timeline;
//import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
//import com.google.android.exoplayer2.extractor.ExtractorsFactory;
//import com.google.android.exoplayer2.source.ExtractorMediaSource;
//import com.google.android.exoplayer2.source.MediaSource;
//import com.google.android.exoplayer2.source.TrackGroupArray;
//import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
//import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
//import com.google.android.exoplayer2.trackselection.TrackSelection;
//import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
//import com.google.android.exoplayer2.upstream.BandwidthMeter;
//import com.google.android.exoplayer2.upstream.DataSource;
//import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
//import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
//import com.google.android.exoplayer2.util.Util;
//import com.jiujie.base.util.UIHelper;
//
//import java.io.IOException;
//
///**
// * 2.5.1 在部分机型上，循环播放会有问题
// * Created by ChenJiaLiang on 2018/5/8.
// * Email:576507648@qq.com
// */
//
//public class VideoExoUtil1 implements VideoController {
//    private final String TAG = "VideoController VideoExoUtil";
//    private final Context context;
//    private final VideoScaleType videoScaleType;
//    private Surface surface;
//    private SurfaceView surfaceView;
//    private TextureView textureView;
//    private SurfaceHolder surfaceHolder;
//    private boolean isLoop;
//    private String videoPath;
//    private boolean isAutoStart;
//    private SimpleExoPlayer simpleExoPlayer;
//    private String thumbUrl;
//    private OnVideoStatusListener onVideoStatusListener;
//    private OnVideoPrepareListener onVideoPrepareListener;
//    private float volume = -1;
//
//    public enum VideoScaleType {
//        SCALE_FILL_XY, SCALE_CROP_CENTER
//    }
//
//    public VideoExoUtil1(Context context, Surface surface, boolean isLoop, boolean isAutoStart, VideoScaleType videoScaleType) {
//        this.context = context;
//        this.surface = surface;
//        this.isLoop = isLoop;
//        this.isAutoStart = isAutoStart;
//        this.videoScaleType = videoScaleType;
//        init();
//    }
//
//    public VideoExoUtil1(Context context, SurfaceView surfaceView, boolean isLoop, boolean isAutoStart, VideoScaleType videoScaleType) {
//        this.context = context;
//        this.surfaceView = surfaceView;
//        this.isLoop = isLoop;
//        this.isAutoStart = isAutoStart;
//        this.videoScaleType = videoScaleType;
//        init();
//    }
//
//    public VideoExoUtil1(Context context, TextureView textureView, boolean isLoop, boolean isAutoStart, VideoScaleType videoScaleType) {
//        this.context = context;
//        this.textureView = textureView;
//        this.isLoop = isLoop;
//        this.isAutoStart = isAutoStart;
//        this.videoScaleType = videoScaleType;
//        init();
//    }
//
//    public VideoExoUtil1(Context context, SurfaceHolder surfaceHolder, boolean isLoop, boolean isAutoStart, VideoScaleType videoScaleType) {
//        this.context = context;
//        this.surfaceHolder = surfaceHolder;
//        this.isLoop = isLoop;
//        this.isAutoStart = isAutoStart;
//        this.videoScaleType = videoScaleType;
//        init();
//    }
//
//    public void setSurfaceHolder(SurfaceHolder surfaceHolder) {
//        this.surfaceHolder = surfaceHolder;
//        simpleExoPlayer.setVideoSurfaceHolder(surfaceHolder);
//    }
//
//    public void setSurfaceView(SurfaceView surfaceView) {
//        this.surfaceView = surfaceView;
//        simpleExoPlayer.setVideoSurfaceView(surfaceView);
//    }
//
//    public void setTextureView(TextureView textureView) {
//        this.textureView = textureView;
//        simpleExoPlayer.setVideoTextureView(textureView);
//    }
//
//    public void setSurface(Surface surface) {
//        this.surface = surface;
//        simpleExoPlayer.setVideoSurface(surface);
//    }
//
//    private void init() {
//        // 1 create a default TrackSelector
//        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
//        TrackSelection.Factory videoTrackSelectionFactory =
//                new AdaptiveTrackSelection.Factory(bandwidthMeter);
//        DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
//
//        // 2. Create the player
//        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
//        // 自动播放?  开始/暂停 方法
//        simpleExoPlayer.setPlayWhenReady(isAutoStart);
//        //循环播放配置
//        simpleExoPlayer.setRepeatMode(isLoop ? Player.REPEAT_MODE_ALL : Player.REPEAT_MODE_OFF);
//        //视频缩放配置
//        if (videoScaleType == VideoScaleType.SCALE_FILL_XY) {
//            simpleExoPlayer.setVideoScalingMode(1);//fill xy
//        } else if (videoScaleType == VideoScaleType.SCALE_CROP_CENTER) {
//            simpleExoPlayer.setVideoScalingMode(2);//crop center
//        }
//
//        bindPlayerToView();
//        simpleExoPlayer.setVideoListener(new SimpleExoPlayer.VideoListener() {
//            @Override
//            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
//                if (onVideoStatusListener != null) {
//                    onVideoStatusListener.onPrepare(width, height);
//                }
//                if (onVideoPrepareListener != null) onVideoPrepareListener.onPrepare(width, height);
//            }
//
//            @Override
//            public void onRenderedFirstFrame() {
//
//            }
//        });
//        simpleExoPlayer.addListener(new Player.EventListener() {
//            @Override
//            public void onTimelineChanged(Timeline timeline, Object o) {
//                UIHelper.showLog(TAG, "addListener onTimelineChanged");
//            }
//
//            @Override
//            public void onTracksChanged(TrackGroupArray trackGroupArray, TrackSelectionArray trackSelectionArray) {
//                UIHelper.showLog(TAG, "addListener onTracksChanged");
//            }
//
//            @Override
//            public void onLoadingChanged(boolean isLoading) {
//                UIHelper.showLog(TAG, "addListener onLoadingChanged isLoading:" + isLoading);
//            }
//
//            @Override
//            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//                UIHelper.showLog(TAG, "addListener onPlayerStateChanged playWhenReady:" + playWhenReady + ",playbackState:" + playbackState + ",isPlaying:" + isPlaying());
//                switch (playbackState) {
//                    case ExoPlayer.STATE_IDLE://1
//                        UIHelper.showLog(TAG, "ExoPlayer idle!");
//                        if (onVideoStatusListener != null) {
//                            onVideoStatusListener.onCompleted(null);
//                        }
//                        break;
//                    case ExoPlayer.STATE_BUFFERING://2
//                        UIHelper.showLog(TAG, "ExoPlayer buffering true");
//                        if (onVideoStatusListener != null) {
//                            onVideoStatusListener.onBufferListen(true);
//                        }
//                        break;
//                    case ExoPlayer.STATE_READY://3
//                        UIHelper.showLog(TAG, "ExoPlayer buffering false");
//                        if (onVideoStatusListener != null) {
//                            onVideoStatusListener.onBufferListen(false);
//                        }
//                        break;
//                    case ExoPlayer.STATE_ENDED://4
//                        UIHelper.showLog(TAG, "Playback ended!");
//                        if (isLoop) {
//                            simpleExoPlayer.seekTo(0);
//                        }
//                        if (onVideoStatusListener != null) {
//                            onVideoStatusListener.onCompleted(null);
//                        }
//                        break;
//                }
//            }
//
//            @Override
//            public void onRepeatModeChanged(int i) {
//                UIHelper.showLog(TAG, "addListener onRepeatModeChanged");
//            }
//
//            @Override
//            public void onPlayerError(ExoPlaybackException e) {
//                e.printStackTrace();
//                UIHelper.showLog(TAG, "addListener onPlayerError " + e);
//
//            }
//
//            @Override
//            public void onPositionDiscontinuity() {
//                UIHelper.showLog(TAG, "addListener onPositionDiscontinuity");
//
//            }
//
//            @Override
//            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
//                UIHelper.showLog(TAG, "addListener onPlaybackParametersChanged");
//
//            }
//        });
//    }
//
//    private void bindPlayerToView() {
//        // bind the player to the view
//        if (surfaceView != null) {
//            simpleExoPlayer.setVideoSurfaceView(surfaceView);
//        } else if (surface != null) {
//            simpleExoPlayer.setVideoSurface(surface);
//        } else if (textureView != null) {
//            simpleExoPlayer.setVideoTextureView(textureView);
//        } else if (surfaceHolder != null) {
//            simpleExoPlayer.setVideoSurfaceHolder(surfaceHolder);
//        } else {
//            throw new NullPointerException("VideoExoUtil init should have a surfaceView or surface or textureView or surfaceHolder");
//        }
////        simpleExoPlayerView.setPlayer(simpleExoPlayer);
//    }
//
//    public SimpleExoPlayer getSimpleExoPlayer() {
//        return simpleExoPlayer;
//    }
//
//    @Override
//    public void addOnVideoStatusListener(OnVideoStatusListener onVideoStatusListener) {
//        this.onVideoStatusListener = onVideoStatusListener;
//    }
//
//    @Override
//    public void addOnVideoPrepareListener(OnVideoPrepareListener onVideoPrepareListener) {
//        this.onVideoPrepareListener = onVideoPrepareListener;
//    }
//
//    @Override
//    public boolean isPrepared() {
//        return false;
//    }
//
//    @Override
//    public boolean isPause() {
//        return false;
//    }
//
//    @Override
//    public void doPrepareUI(String videoPath, String thumbUrl) {
//
//    }
//
//    @Override
//    public void doPrepare(String videoPath, String thumbUrl) {
//        if (simpleExoPlayer == null) return;
//        this.videoPath = videoPath;
//        this.thumbUrl = thumbUrl;
//        doPrepareUI(videoPath, thumbUrl);
//        if (TextUtils.isEmpty(videoPath)) {
//            return;
//        }
//        // 默认带宽测量
//        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
//        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
//                Util.getUserAgent(context, context.getPackageName()), defaultBandwidthMeter);
//// Produces Extractor instances for parsing the media data.
//        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
//// This is the MediaSource representing the media to be played.
//        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoPath),
//                dataSourceFactory, extractorsFactory, null, new ExtractorMediaSource.EventListener() {
//            @Override
//            public void onLoadError(IOException e) {
//                e.printStackTrace();
//                UIHelper.showLog(TAG, "onLoadError " + e);
//            }
//        });
//        // 准备播放
//        simpleExoPlayer.prepare(mediaSource);
//
//        bindPlayerToView();
//    }
//
//    @Override
//    public void doStart() {
//        if (simpleExoPlayer == null) return;
//        simpleExoPlayer.setPlayWhenReady(true);
//    }
//
//    @Override
//    public void doPause() {
//        if (simpleExoPlayer == null) return;
//        simpleExoPlayer.setPlayWhenReady(false);
//    }
//
//    @Override
//    public boolean isPlaying() {
//        return simpleExoPlayer.getPlayWhenReady();
//    }
//
//    @Override
//    public void setVoice(float volume) {
//        this.volume = volume;
//        if (simpleExoPlayer == null) return;
//        simpleExoPlayer.setVolume(volume);
//    }
//
//    @Override
//    public MediaPlayer getMediaPlayer() {
//        return null;
//    }
//
//    @Override
//    public void doSeekTo(int timeMillis) {
//        if (simpleExoPlayer == null) return;
//        simpleExoPlayer.seekTo(timeMillis);
//    }
//
//    public int getDuration() {
//        if (simpleExoPlayer == null) return 0;
//        return (int) simpleExoPlayer.getDuration();
//    }
//
//    @Override
//    public void doReStart() {
//        if (simpleExoPlayer != null) simpleExoPlayer.seekTo(0);
////        doPrepare(videoPath, thumbUrl);
//    }
//
//    @Override
//    public void doRelease() {
//        if (simpleExoPlayer == null) return;
//        simpleExoPlayer.release();
//        simpleExoPlayer = null;
//    }
//
//}
