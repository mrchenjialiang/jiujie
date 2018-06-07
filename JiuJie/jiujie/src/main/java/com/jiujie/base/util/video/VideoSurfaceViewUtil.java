//package com.jiujie.base.util.video;
//
//import android.graphics.Rect;
//import android.media.MediaPlayer;
//import android.os.Build;
//import android.text.TextUtils;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.ViewGroup;
//
//import com.jiujie.base.util.UIHelper;
//
///**
// * Created by ChenJiaLiang on 2018/5/8.
// * Email:576507648@qq.com
// */
//
//public class VideoSurfaceViewUtil implements VideoController{
//    private final String TAG = "VideoSurfaceViewUtil";
//    private final SurfaceView mSurfaceView;
//    private OnVideoStatusListener onVideoStatusListener;
//    private OnVideoPrepareListener onVideoPrepareListener;
//    private MediaPlayer mMediaPlayer;
//    private boolean isLoop;
//    private ViewGroup containerView;
//    private String videoPath;
//    private boolean isAutoStart;
//    private String thumbUrl;
//    private boolean isPrepared;
//
//    /**
//     * @param isLoop 是否循环播放
//     * @param isAutoStart 是否在准备好后自动播放
//     * @param containerView 如不为空，则表示要全屏
//     */
//    public VideoSurfaceViewUtil(SurfaceView surfaceView, boolean isLoop, boolean isAutoStart, ViewGroup containerView) {
//        if(surfaceView==null)throw new NullPointerException("surfaceView should not be null");
//        this.mSurfaceView = surfaceView;
//        this.isLoop = isLoop;
//        this.isAutoStart = isAutoStart;
//        this.containerView = containerView;
//        init();
//    }
//
//    private void init() {
//        mMediaPlayer = new MediaPlayer();
//        //设置播放时打开屏幕
//        mSurfaceView.getHolder().setKeepScreenOn(true);
//        mSurfaceView.getHolder().addCallback(new SurfaceViewLis());
//        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                if(onVideoStatusListener!=null)onVideoStatusListener.onCompleted(mp);
//                if(isLoop){
//                    doReStart();
//                }else{
//                    if(onVideoStatusListener!=null)onVideoStatusListener.onPause();
//                }
//            }
//        });
//        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                if(mMediaPlayer==null)return;
//                UIHelper.showLog("setOnPreparedListener onPrepared");
//                isPrepared = true;
//
//                if(isAutoStart){
//                    doStart();
//                }
//
//                int videoWidth = mp.getVideoWidth();
//                int videoHeight = mp.getVideoHeight();
//
//                UIHelper.showLog("setOnPreparedListener videoWidth:"+videoWidth);
//                UIHelper.showLog("setOnPreparedListener videoHeight:"+videoHeight);
//
//                if(onVideoPrepareListener !=null){
//                    onVideoPrepareListener.onPrepare(videoWidth,videoHeight);
//                }
//
//                if(onVideoStatusListener!=null) {
//                    onVideoStatusListener.onPrepare(videoWidth, videoHeight);
//                }
//                if(containerView!=null){
//                    ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();
//                    if(lp!=null){
//                        int containerViewWidth = containerView.getWidth();
//                        int containerViewHeight = containerView.getHeight();
//
//                        UIHelper.showLog("setOnPreparedListener containerViewWidth:"+containerViewWidth);
//                        UIHelper.showLog("setOnPreparedListener containerViewHeight:"+containerViewHeight);
//
//                        if(videoWidth==0||videoHeight==0||containerViewHeight==0||containerViewWidth==0){
//                            return;
//                        }
//                        float scaleWidth = videoWidth * 1.0f / containerViewWidth;
//                        float scaleHeight = videoHeight * 1.0f / containerViewHeight;
//                        if (containerViewWidth != videoWidth || scaleHeight != videoHeight) {
//                            int scaleResultRequestWidth;
//                            int scaleResultRequestHeight;
//                            if (scaleWidth <= scaleHeight) {
//                                scaleResultRequestWidth = containerViewWidth;
//                                scaleResultRequestHeight = (int) (videoHeight / scaleWidth);
//                            } else {
//                                scaleResultRequestWidth = (int) (videoWidth / scaleHeight);
//                                scaleResultRequestHeight = containerViewHeight;
//                            }
//
//                            UIHelper.showLog("setOnPreparedListener scaleResultRequestWidth:"+scaleResultRequestWidth);
//                            UIHelper.showLog("setOnPreparedListener scaleResultRequestHeight:"+scaleResultRequestHeight);
//
//
//                            if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.JELLY_BEAN_MR2){
//                                int left = scaleResultRequestWidth / 2 - containerViewWidth / 2;
//                                int top = scaleResultRequestHeight / 2 - containerViewHeight / 2;
//                                int right = scaleResultRequestWidth / 2 + containerViewWidth / 2;
//                                int bottom = scaleResultRequestHeight / 2 + containerViewHeight / 2;
//                                UIHelper.showLog("setOnPreparedListener left:"+left);
//                                UIHelper.showLog("setOnPreparedListener top:"+top);
//                                UIHelper.showLog("setOnPreparedListener right:"+right);
//                                UIHelper.showLog("setOnPreparedListener bottom:"+bottom);
//                                mSurfaceView.setClipBounds(new Rect( left, top, right, bottom));
//
//
//                                //videoWidth:1024
//                                //videoHeight:576
//                                //containerViewWidth:1080
//                                //containerViewHeight:1734
//                                //scaleResultRequestWidth:3082
//                                //scaleResultRequestHeight:1734
////                                mSurfaceView.setClipBounds(new Rect( 500, 0, 1200, 1734));
//                            }
//
////                            lp.width = containerViewWidth;
////                            lp.height = containerViewHeight;
////                            mSurfaceView.setLayoutParams(lp);
//
//                            lp.width = scaleResultRequestWidth;
//                            lp.height = scaleResultRequestHeight;
//                            mSurfaceView.setLayoutParams(lp);
//                        }
//                    }
//                }
//            }
//        });
//        mMediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
//            @Override
//            public boolean onInfo(MediaPlayer mp, int what, int extra) {
//                switch (what) {
//                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
//                        if(onVideoStatusListener !=null){
//                            onVideoStatusListener.onBufferListen(true);
//                        }
//                        break;
//                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
//                        if(onVideoStatusListener !=null){
//                            onVideoStatusListener.onBufferListen(false);
//                        }
//                        break;
//                    default:
//                        break;
//                }
//                return false;
//            }
//        });
//        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//            @Override
//            public boolean onError(MediaPlayer mp, int what, int extra) {
//                if(mMediaPlayer==null)return true;
//                if(isLoop){
//                    doReStart();
//                }
//                if(onVideoStatusListener !=null){
//                    onVideoStatusListener.onError(mp, "文件损坏或解码异常");
//                }
//                return true;//播放异常，则停止播放，防止弹窗使界面阻塞
//            }
//        });
//    }
//
//    @Override
//    public void setOnVideoStatusListener(OnVideoStatusListener onVideoStatusListener) {
//        this.onVideoStatusListener = onVideoStatusListener;
//    }
//
//    @Override
//    public void setOnVideoPrepareListener(OnVideoPrepareListener onVideoPrepareListener) {
//        this.onVideoPrepareListener = onVideoPrepareListener;
//    }
//
//    @Override
//    public boolean isPrepared() {
//        return isPrepared;
//    }
//
//    @Override
//    public void doPrepare(String videoPath, String thumbUrl) {
//        if(mMediaPlayer==null)return;
//        this.videoPath = videoPath;
//        this.thumbUrl = thumbUrl;
//        isPrepared = false;
//        if(TextUtils.isEmpty(this.videoPath)){
//            if(onVideoStatusListener !=null){
//                onVideoStatusListener.onError(mMediaPlayer,"不存在该文件");
//            }
//        }else{
//            //设置视频路径
//            try {
//                if(mMediaPlayer!=null&&isPlaying()){
//                    mMediaPlayer.stop();
//                }
//                mMediaPlayer.reset();
////        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                mMediaPlayer.setDataSource(this.videoPath);
//                if(this.videoPath.startsWith("http")){
//                    mMediaPlayer.prepareAsync();
//                }else{
//                    mMediaPlayer.prepare();
//                }
//            } catch (Exception e) {
//                UIHelper.showLog(TAG,"Exception doPrepare "+e);
//                e.printStackTrace();
//                if(onVideoStatusListener !=null){
//                    onVideoStatusListener.onError(mMediaPlayer,e.getMessage());
//                }
//            }
//        }
//    }
//
//    @Override
//    public void doStart(){
//        if(mMediaPlayer==null)return;
//        try {
//            mMediaPlayer.start();
//            mSurfaceView.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if(mMediaPlayer==null)return;
//                    if (mMediaPlayer.getCurrentPosition() > 30) {
//                        if(onVideoStatusListener !=null){
//                            onVideoStatusListener.onStart();
//                        }
//                    } else {
//                        mSurfaceView.postDelayed(this, 50);
//                    }
//                }
//            }, 50);
//        }catch (Exception e){
//            UIHelper.showLog(TAG,"Exception doStart "+e);
//            e.printStackTrace();
//            doReStart();
//        }
//    }
//
//    @Override
//    public void doPause(){
//        if(mMediaPlayer==null)return;
//        if(!mMediaPlayer.isPlaying())return;
//        mMediaPlayer.pause();
//        if(onVideoStatusListener !=null){
//            onVideoStatusListener.onPause();
//        }
//    }
//
//    @Override
//    public boolean isPlaying(){
//        if(mMediaPlayer==null)return false;
//        return mMediaPlayer.isPlaying();
//    }
//
//    @Override
//    public void setVoice(float volume){
//        if(mMediaPlayer==null)return;
//        mMediaPlayer.setVolume(volume,volume);
//    }
//
//    @Override
//    public MediaPlayer getMediaPlayer() {
//        return mMediaPlayer;
//    }
//
//    @Override
//    public void doSeekTo(int position) {
//        if(mMediaPlayer==null)return;
//        mMediaPlayer.seekTo(position);
//    }
//
//    @Override
//    public void doReStart() {
//        doPrepare(videoPath,thumbUrl);
//    }
//
//    @Override
//    public void doRelease() {
//        if(mMediaPlayer==null)return;
//        if(mMediaPlayer.isPlaying())mMediaPlayer.stop();
//        mMediaPlayer.release();
//        mMediaPlayer = null;
//    }
//
//    private class SurfaceViewLis implements SurfaceHolder.Callback {
//        @Override
//        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//        }
//        @Override
//        public void surfaceCreated(SurfaceHolder holder) {
//            UIHelper.showLog("SurfaceViewLis surfaceCreated videoPath "+ videoPath);
////            mMediaPlayer.setDisplay(mSurfaceView.getHolder());
//            mMediaPlayer.setDisplay(holder);
//            doPrepare(videoPath,thumbUrl);
//        }
//
//        @Override
//        public void surfaceDestroyed(SurfaceHolder holder) {
//
//        }
//    }
//
//}
