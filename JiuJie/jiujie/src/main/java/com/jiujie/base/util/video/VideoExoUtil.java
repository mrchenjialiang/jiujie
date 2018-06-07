package com.jiujie.base.util.video;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.jiujie.base.util.UIHelper;

import java.io.IOException;

/**
 * Created by ChenJiaLiang on 2018/5/8.
 * Email:576507648@qq.com
 */

public class VideoExoUtil implements VideoController{
    private final String TAG = "VideoExoUtil";
    private final Context context;
    private final VideoScaleType videoScaleType;
    private Surface surface;
    private SurfaceView surfaceView;
    private TextureView textureView;
    private SurfaceHolder surfaceHolder;
    private boolean isLoop;
    private String videoPath;
    private boolean isAutoStart;
    private SimpleExoPlayer simpleExoPlayer;
    private String thumbUrl;

    public enum VideoScaleType{
        SCALE_FILL_XY,SCALE_CROP_CENTER
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
        // 自动播放?  开始/暂停 方法
        simpleExoPlayer.setPlayWhenReady(isAutoStart);
        //循环播放配置
        simpleExoPlayer.setRepeatMode(isLoop?Player.REPEAT_MODE_ALL:Player.REPEAT_MODE_OFF);
        //视频缩放配置
        if(videoScaleType==VideoScaleType.SCALE_FILL_XY){
            simpleExoPlayer.setVideoScalingMode(1);//fill xy
        }else if(videoScaleType==VideoScaleType.SCALE_CROP_CENTER){
            simpleExoPlayer.setVideoScalingMode(2);//crop center
        }

        bindPlayerToView();

        simpleExoPlayer.setVideoListener(new SimpleExoPlayer.VideoListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int i2, float v) {
                UIHelper.showLog(TAG,"onVideoSizeChanged "+width+","+height+","+i2+","+v);
            }

            @Override
            public void onRenderedFirstFrame() {
                UIHelper.showLog(TAG,"onRenderedFirstFrame");
            }
        });
    }

    private void bindPlayerToView() {
        // bind the player to the view
        if(surfaceView!=null){
            simpleExoPlayer.setVideoSurfaceView(surfaceView);
        }else if(surface!=null){
            simpleExoPlayer.setVideoSurface(surface);
        }else if(textureView!=null){
            simpleExoPlayer.setVideoTextureView(textureView);
        }else if(surfaceHolder!=null){
            simpleExoPlayer.setVideoSurfaceHolder(surfaceHolder);
        }else{
            throw new NullPointerException("VideoExoUtil init should have a surfaceView or surface or textureView or surfaceHolder");
        }
//        simpleExoPlayerView.setPlayer(simpleExoPlayer);
    }

    public SimpleExoPlayer getSimpleExoPlayer() {
        return simpleExoPlayer;
    }

    @Override
    public void setOnVideoStatusListener(OnVideoStatusListener onVideoStatusListener) {
//        this.onVideoStatusListener = onVideoStatusListener;
    }

    @Override
    public void setOnVideoPrepareListener(OnVideoPrepareListener onVideoPrepareListener) {
//        this.onVideoPrepareListener = onVideoPrepareListener;
    }

    @Override
    public boolean isPrepared() {
        return false;
    }

    @Override
    public void doPrepare(String videoPath, String thumbUrl) {
        if(simpleExoPlayer==null)return;
        this.videoPath = videoPath;
        this.thumbUrl = thumbUrl;
        // 默认带宽测量
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, context.getPackageName()), defaultBandwidthMeter);
// Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
// This is the MediaSource representing the media to be played.
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(this.videoPath),
                dataSourceFactory, extractorsFactory, null, new ExtractorMediaSource.EventListener() {
            @Override
            public void onLoadError(IOException e) {
                e.printStackTrace();
                UIHelper.showLog(TAG,"onLoadError " +e);
            }
        });
        // 准备播放
        simpleExoPlayer.prepare(mediaSource);

        bindPlayerToView();
    }

    @Override
    public void doStart(){
        if(simpleExoPlayer==null)return;
        simpleExoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void doPause(){
        if(simpleExoPlayer==null)return;
        simpleExoPlayer.setPlayWhenReady(false);
    }

    @Override
    public boolean isPlaying(){
        return simpleExoPlayer.getPlayWhenReady();
    }

    @Override
    public void setVoice(float volume){
        if(simpleExoPlayer==null)return;
        simpleExoPlayer.setVolume(volume);
    }

    @Override
    public MediaPlayer getMediaPlayer() {
        return null;
    }

    @Override
    public void doSeekTo(int timeMillis) {
        if(simpleExoPlayer==null)return;
        simpleExoPlayer.seekTo(timeMillis);
    }

    public int getDuration() {
        if(simpleExoPlayer==null)return 0;
        return (int) simpleExoPlayer.getDuration();
    }

    @Override
    public void doReStart() {
        doPrepare(videoPath,thumbUrl);
    }

    @Override
    public void doRelease() {
        if(simpleExoPlayer==null)return;
        simpleExoPlayer.release();
        simpleExoPlayer = null;
    }

}
