/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jiujie.base.util.video;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.jiujie.base.jk.OnSimpleListener;
import com.jiujie.base.util.UIHelper;

public class MyTextureView extends TextureView implements VideoController{
    private final String TAG = "TextureVideoView";
    private Surface mSurface = null;
    private MediaPlayer mMediaPlayer = null;
    private int         mAudioSession;
    private int         mVideoWidth;
    private int         mVideoHeight;
    private int         mCurrentBufferPercentage;
    private int fixedWidth;
    private int fixedHeight;
    private Matrix matrix;
    private OnVideoStatusListener onVideoStatusListener;
    private OnVideoPrepareListener onVideoPrepareListener;
    private String videoPath;
    private boolean isLoop;
    private boolean isAutoStart;
    private ViewGroup containerView;
    private String thumbUrl;
    private boolean isLoadingEnd;
    private OnSimpleListener onFirstSurfaceUpdateListener;
    private SurfaceTextureListener outsideSurfaceTextureListener;
    private float volume = -1;
    private boolean isPrepared;

    public MyTextureView(Context context) {
        super(context);
        init();
    }

    public MyTextureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public MyTextureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setFixedSize(int width, int height) {
        fixedHeight = height;
        fixedWidth = width;
        requestLayout();
    }

    public int getVideoHeight() {
        return mVideoHeight;
    }

    public int getVideoWidth() {
        return mVideoWidth;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (fixedWidth == 0 || fixedHeight == 0) {
            defaultMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            setMeasuredDimension(fixedWidth, fixedHeight);
        }
    }

    protected void defaultMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
        int height = getDefaultSize(mVideoHeight, heightMeasureSpec);
        if (mVideoWidth > 0 && mVideoHeight > 0) {

            int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

            if (widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode == MeasureSpec.EXACTLY) {
                width = widthSpecSize;
                height = heightSpecSize;

                if ( mVideoWidth * height  < width * mVideoHeight ) {
                    width = height * mVideoWidth / mVideoHeight;
                } else if ( mVideoWidth * height  > width * mVideoHeight ) {
                    height = width * mVideoHeight / mVideoWidth;
                }
            } else if (widthSpecMode == MeasureSpec.EXACTLY) {
                width = widthSpecSize;
                height = width * mVideoHeight / mVideoWidth;
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    height = heightSpecSize;
                }
            } else if (heightSpecMode == MeasureSpec.EXACTLY) {
                height = heightSpecSize;
                width = height * mVideoWidth / mVideoHeight;
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    width = widthSpecSize;
                }
            } else {
                width = mVideoWidth;
                height = mVideoHeight;
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    height = heightSpecSize;
                    width = height * mVideoWidth / mVideoHeight;
                }
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    width = widthSpecSize;
                    height = width * mVideoHeight / mVideoWidth;
                }
            }
        } else {
            // no size yet, just adopt the given spec sizes
        }
        setMeasuredDimension(width, height);
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(MyTextureView.class.getName());
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(MyTextureView.class.getName());
    }

    public int resolveAdjustedSize(int desiredSize, int measureSpec) {
        return getDefaultSize(desiredSize, measureSpec);
    }

    private void init() {
        mVideoWidth = 0;
        mVideoHeight = 0;
        super.setSurfaceTextureListener(mSurfaceTextureListener);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
    }

    @Override
    public void setSurfaceTextureListener(SurfaceTextureListener listener) {
        this.outsideSurfaceTextureListener = listener;
    }

    //需求:视频等比例放大,直至一边铺满View的某一边,另一边超出View的另一边,再移动到View的正中央,这样长边两边会被裁剪掉同样大小的区域,视频看起来不会变形
    //也即是:先把视频区(实际的大小显示区)与View(定义的大小)区的两个中心点重合, 然后等比例放大或缩小视频区,直至一条边与View的一条边相等,另一条边超过
    //View的另一条边,这时再裁剪掉超出的边, 使视频区与View区大小一样. 这样在不同尺寸的手机上,视频看起来不会变形,只是水平或竖直方向的两端被裁剪了一些.
    private void transformVideo(int videoWidth, int videoHeight) {
        if (getResizedHeight() == 0 || getResizedWidth() == 0) {
            return;
        }
        float sx = (float) getResizedWidth() / (float) videoWidth;
        float sy = (float) getResizedHeight() / (float) videoHeight;

        float maxScale = Math.max(sx, sy);
        if (this.matrix == null) {
            matrix = new Matrix();
        } else {
            matrix.reset();
        }

        //第2步:把视频区移动到View区,使两者中心点重合.
        matrix.preTranslate((getResizedWidth() - videoWidth) / 2, (getResizedHeight() - videoHeight) / 2);

        //第1步:因为默认视频是fitXY的形式显示的,所以首先要缩放还原回来.
        matrix.preScale(videoWidth / (float) getResizedWidth(), videoHeight / (float) getResizedHeight());

        //第3步,等比例放大或缩小,直到视频区的一边超过View一边, 另一边与View的另一边相等. 因为超过的部分超出了View的范围,所以是不会显示的,相当于裁剪了.
        matrix.postScale(maxScale, maxScale, getResizedWidth() / 2, getResizedHeight() / 2);//后两个参数坐标是以整个View的坐标系以参考的

        setTransform(matrix);
        postInvalidate();
    }

    public int getResizedWidth() {
        if (fixedWidth == 0) {
            return getWidth();
        } else {
            return fixedWidth;
        }
    }

    public int getResizedHeight() {
        if (fixedHeight== 0) {
            return getHeight();
        } else {
            return fixedHeight;
        }
    }

    MediaPlayer.OnVideoSizeChangedListener mSizeChangedListener =
            new MediaPlayer.OnVideoSizeChangedListener() {
                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                    mVideoWidth = mp.getVideoWidth();
                    mVideoHeight = mp.getVideoHeight();
                    if (mVideoWidth != 0 && mVideoHeight != 0) {
                        getSurfaceTexture().setDefaultBufferSize(mVideoWidth, mVideoHeight);
                        requestLayout();
                        transformVideo(mVideoWidth, mVideoHeight);
                    }
                }
            };


    private boolean isFirstPrepared = true;
    private MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
        public void onPrepared(MediaPlayer mp) {
            if(mMediaPlayer==null)return;
            isPrepared = true;

            if(volume>=0)mMediaPlayer.setVolume(volume,volume);

            if(isFirstPrepared){
                isFirstPrepared = false;
                if(isAutoStart){
                    doStart();
                }
            }else if(isLoop){
                doStart();
            }

            mVideoWidth = mp.getVideoWidth();
            mVideoHeight = mp.getVideoHeight();

            if (mVideoWidth != 0 && mVideoHeight != 0) {
                getSurfaceTexture().setDefaultBufferSize(mVideoWidth, mVideoHeight);
            }
            if(containerView!=null){
                setFixedSize(containerView.getWidth(),containerView.getHeight());
            }

            if(onVideoPrepareListener !=null){
                onVideoPrepareListener.onPrepare(mVideoWidth,mVideoHeight);
            }

            if(onVideoStatusListener!=null) {
                onVideoStatusListener.onPrepare(mVideoWidth, mVideoHeight);
            }
        }
    };

    private OnCompletionListener mCompletionListener = new OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    if(onVideoStatusListener!=null)onVideoStatusListener.onCompleted(mp);
                    if(isLoop){
                        doReStart();
                    }else{
                        if(onVideoStatusListener!=null)onVideoStatusListener.onPause();
                    }
                }
            };

    private OnInfoListener mInfoListener =
            new OnInfoListener() {
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    switch (what) {
                        case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                            //第一次播放刚渲染完
//                            if(mMediaPlayer!=null&&mSurface!=null)
//                            mMediaPlayer.setSurface(mSurface);
                            break;
                        case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                            if(onVideoStatusListener !=null){
                                onVideoStatusListener.onBufferListen(true);
                            }
                            break;
                        case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                            if(onVideoStatusListener !=null){
                                onVideoStatusListener.onBufferListen(false);
                            }
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            };

    private OnErrorListener mErrorListener =
            new OnErrorListener() {
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    UIHelper.showLog(TAG,"OnErrorListener what:"+what+",extra:"+extra);
                    if(mMediaPlayer==null)return true;
                    if(isLoop){
                        doReStart();
                    }
                    if(onVideoStatusListener !=null){
                        onVideoStatusListener.onError(mp, "文件损坏或解码异常");
                    }
                    return true;//播放异常，则停止播放，防止弹窗使界面阻塞
                }
            };

    private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener =
            new MediaPlayer.OnBufferingUpdateListener() {
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    UIHelper.showLog(TAG,"OnBufferingUpdateListener percent:"+percent);
                    mCurrentBufferPercentage = percent;
                }
            };

    private SurfaceTextureListener mSurfaceTextureListener = new SurfaceTextureListener(){
        @Override
        public void onSurfaceTextureSizeChanged(final SurfaceTexture surface, final int width, final int height) {
            if(outsideSurfaceTextureListener!=null){
                outsideSurfaceTextureListener.onSurfaceTextureSizeChanged(surface, width, height);
            }
        }

        @Override
        public void onSurfaceTextureAvailable(final SurfaceTexture surface, final int width, final int height) {
            mSurface = new Surface(surface);
            doPrepare(videoPath,thumbUrl);
            if(outsideSurfaceTextureListener!=null){
                outsideSurfaceTextureListener.onSurfaceTextureAvailable(surface, width, height);
            }
        }

        @Override
        public boolean onSurfaceTextureDestroyed(final SurfaceTexture surface) {
            if(outsideSurfaceTextureListener!=null){
                outsideSurfaceTextureListener.onSurfaceTextureDestroyed(surface);
            }

            // after we return from this we can't use the surface any more
            if (mSurface != null) {
                mSurface.release();
                mSurface = null;
            }
            doRelease();
            return true;
        }
        @Override
        public void onSurfaceTextureUpdated(final SurfaceTexture surface) {
            if(outsideSurfaceTextureListener!=null){
                outsideSurfaceTextureListener.onSurfaceTextureUpdated(surface);
            }
            // do nothing
            if(!isLoadingEnd){
                isLoadingEnd = true;
                if(onFirstSurfaceUpdateListener!=null)onFirstSurfaceUpdateListener.onListen();
            }
        }
    };

    public void setOnFirstSurfaceUpdateListener(OnSimpleListener onFirstSurfaceUpdateListener) {
        this.onFirstSurfaceUpdateListener = onFirstSurfaceUpdateListener;
    }

    @Override
    public void setOnVideoStatusListener(OnVideoStatusListener onVideoStatusListener) {
        this.onVideoStatusListener = onVideoStatusListener;
    }

    @Override
    public void setOnVideoPrepareListener(OnVideoPrepareListener onVideoPrepareListener) {
        this.onVideoPrepareListener = onVideoPrepareListener;
    }

    @Override
    public boolean isPrepared() {
        return isPrepared;
    }

    @Override
    public void doPrepare(String videoPath, String thumbUrl) {
        this.videoPath = videoPath;
        this.thumbUrl = thumbUrl;
        Uri uri = Uri.parse(this.videoPath);
        if (uri == null || mSurface == null) {
            return;
        }
        isPrepared = false;

        boolean isFirst = mMediaPlayer==null;
        if(isFirst){
            AudioManager am = (AudioManager) getContext().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            if(am!=null){
                am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
                am.abandonAudioFocus(new AudioManager.OnAudioFocusChangeListener() {
                    @Override
                    public void onAudioFocusChange(int focusChange) {
                        UIHelper.showLog(TAG,"onAudioFocusChange focusChange:"+focusChange);
                    }
                });
            }
            mMediaPlayer = new MediaPlayer();
        }else{
            mMediaPlayer.reset();
        }
        try {
            if (mAudioSession != 0) {
                mMediaPlayer.setAudioSessionId(mAudioSession);
            } else {
                mAudioSession = mMediaPlayer.getAudioSessionId();
            }
            mMediaPlayer.setOnPreparedListener(mPreparedListener);
            mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
            mMediaPlayer.setOnCompletionListener(mCompletionListener);
            mMediaPlayer.setOnErrorListener(mErrorListener);
            mMediaPlayer.setOnInfoListener(mInfoListener);
            mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            mCurrentBufferPercentage = 0;
            mMediaPlayer.setDataSource(getContext().getApplicationContext(), uri);
            mMediaPlayer.setSurface(mSurface);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.prepareAsync();
            if(isFirst){
                requestLayout();
                invalidate();
            }
        }catch (Exception e){
            e.printStackTrace();
            UIHelper.showLog("Exception on doPrepare :"+e);
            if(onVideoStatusListener!=null)onVideoStatusListener.onError(mMediaPlayer,"文件异常");
        }
    }

    @Override
    public void doStart() {
        if(mMediaPlayer==null)return;
        try {
            mMediaPlayer.start();
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(mMediaPlayer==null)return;
                    if (mMediaPlayer.getCurrentPosition() > 30) {
                        if(onVideoStatusListener !=null){
                            onVideoStatusListener.onStart();
                        }
                    } else {
                        postDelayed(this, 50);
                    }
                }
            }, 50);
        }catch (Exception e){
            UIHelper.showLog(TAG,"Exception doStart "+e);
            e.printStackTrace();
            doReStart();
        }
    }

    @Override
    public void doPause() {
        if(mMediaPlayer==null)return;
        if(!mMediaPlayer.isPlaying())return;
        mMediaPlayer.pause();
        if(onVideoStatusListener !=null){
            onVideoStatusListener.onPause();
        }
    }

    @Override
    public void doSeekTo(int position) {
        if(mMediaPlayer==null)return;
        mMediaPlayer.seekTo(position);
    }

    @Override
    public void doReStart() {
        doPrepare(videoPath,thumbUrl);
    }

    @Override
    public void doRelease() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            AudioManager am = (AudioManager) getContext().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            if(am==null)return;
            am.abandonAudioFocus(null);
        }
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer!=null && mMediaPlayer.isPlaying();
    }

    @Override
    public void setVoice(float volume) {
        this.volume = volume;
        if(mMediaPlayer==null)return;
        mMediaPlayer.setVolume(volume,volume);
    }

    @Override
    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    /**
     * 已加载读取数？
     */
    public int getBufferPercentage() {
        if (mMediaPlayer != null) {
            return mCurrentBufferPercentage;
        }
        return 0;
    }

    public int getAudioSessionId() {
        if (mAudioSession == 0) {
            MediaPlayer foo = new MediaPlayer();
            mAudioSession = foo.getAudioSessionId();
            foo.release();
        }
        return mAudioSession;
    }

    public void setSome(boolean isLoop, boolean isAutoStart, ViewGroup containerView) {
        this.isLoop = isLoop;
        this.isAutoStart = isAutoStart;
        this.containerView = containerView;
    }
}
