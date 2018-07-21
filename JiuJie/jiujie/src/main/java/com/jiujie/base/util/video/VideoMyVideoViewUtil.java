//package com.jiujie.base.util.video;
//
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.os.Build;
//import android.text.TextUtils;
//import android.view.ViewGroup;
//
//import com.jiujie.base.util.UIHelper;
//
///**
// * Created by ChenJiaLiang on 2018/5/8.
// * Email:576507648@qq.com
// */
//
//public class VideoMyVideoViewUtil implements VideoController{
//    private final String TAG = "VideoMyVideoViewUtil";
//    private MyVideoView mVideoView;
//    private boolean isLoop;
//    private OnVideoStatusListener onVideoStatusListener;
//    private MediaPlayer mMediaPlayer;//要准备好了才不为空，注意判断空
//    private float volume = -1;
//    private boolean isAutoStart;
//    private String videoPath;
//    private OnVideoPrepareListener onVideoPrepareListener;
//    private String thumbUrl;
//    private boolean isPrepared;
//
//    public VideoMyVideoViewUtil(MyVideoView mVideoView, boolean isLoop, boolean isAutoStart, ViewGroup containerView) {
//        this.mVideoView = mVideoView;
//        this.isLoop = isLoop;
//        this.isAutoStart = isAutoStart;
//
//        mVideoView.setFullScreen(containerView);
//        init();
//    }
//
//    private void init() {
//        if(mVideoView==null)return;
//        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
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
//        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                UIHelper.showLog(TAG,"onPrepared");
//                mMediaPlayer = mp;
//                if(mMediaPlayer==null)return;
//                if(mVideoView==null)return;
//                isPrepared = true;
//                if(isAutoStart)doStart();
//                if(volume>=0)mp.setVolume(volume,volume);
//                int videoWidth = mp.getVideoWidth();
//                int videoHeight = mp.getVideoHeight();
//                mVideoView.setVideoSize(videoWidth,videoHeight);
//
//                if(onVideoStatusListener !=null){
//                    onVideoStatusListener.onPrepare(videoWidth,videoHeight);
//                }
//                if(onVideoPrepareListener !=null){
//                    onVideoPrepareListener.onPrepare(videoWidth,videoHeight);
//                }
//
//                //mVideoView.setOnInfoListener api >= 17
//                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
//                    @Override
//                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
//                        switch (what) {
//                            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
//                                if(onVideoStatusListener !=null){
//                                    onVideoStatusListener.onBufferListen(true);
//                                }
//                                break;
//                            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
//                                if(onVideoStatusListener !=null){
//                                    onVideoStatusListener.onBufferListen(false);
//                                }
//                                break;
//                            default:
//                                break;
//                        }
//                        return false;
//                    }
//                });
//            }
//        });
//        if(Build.VERSION.SDK_INT>=17){
//            mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
//                @Override
//                public boolean onInfo(MediaPlayer mp, int what, int extra) {
//                    switch (what) {
//                        case MediaPlayer.MEDIA_INFO_BUFFERING_START:
//                            UIHelper.showLog(VideoMyVideoViewUtil.this,"VideoView.setOnInfoListener buffering true");
//                            if(onVideoStatusListener !=null){
//                                onVideoStatusListener.onBufferListen(true);
//                            }
//                            break;
//                        case MediaPlayer.MEDIA_INFO_BUFFERING_END:
//                            UIHelper.showLog(VideoMyVideoViewUtil.this,"VideoView.setOnInfoListener buffering false");
//                            if(onVideoStatusListener !=null){
//                                onVideoStatusListener.onBufferListen(false);
//                            }
//                            break;
//                        default:
//                            break;
//                    }
//                    return false;
//                }
//            });
//        }
//        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//            @Override
//            public boolean onError(MediaPlayer mp, int what, int extra) {
//                if(mMediaPlayer==null)return true;
//                if(mVideoView==null)return true;
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
//        return isPrepared;
//    }
//
//    @Override
//    public void doPrepare(String videoPath, String thumbUrl) {
//        if(mVideoView==null)return;
//        this.videoPath = videoPath;
//        this.thumbUrl = thumbUrl;
//        isPrepared = false;
//        if(TextUtils.isEmpty(this.videoPath)){
//            if(onVideoStatusListener !=null){
//                onVideoStatusListener.onError(mMediaPlayer,"不存在该文件");
//            }
//        }else{
//            //设置视频路径
//            mVideoView.setVideoURI(Uri.parse(this.videoPath));
//        }
//    }
//
//    @Override
//    public void doStart(){
//        if(mVideoView==null)return;
//        try {
//            mVideoView.start();
//            mVideoView.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if(mVideoView==null)return;
//                    if (mVideoView.getCurrentPosition() > 30) {
//                        if(onVideoStatusListener !=null){
//                            onVideoStatusListener.onStart();
//                        }
//                    } else {
//                        mVideoView.postDelayed(this, 50);
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
//        if(mVideoView==null)return;
//        if(!mVideoView.isPlaying())return;
//        mVideoView.pause();
//        if(onVideoStatusListener !=null){
//            onVideoStatusListener.onPause();
//        }
//    }
//
//    @Override
//    public boolean isPlaying(){
//        if(mVideoView==null)return false;
//        return mVideoView.isPlaying();
//    }
//
//    @Override
//    public void setVoice(float volume){
//        if(mVideoView==null)return;
//        if(mMediaPlayer!=null){
//            mMediaPlayer.setVolume(volume,volume);
//        }else{
//            this.volume = volume;
//        }
//    }
//
//    @Override
//    public MediaPlayer getMediaPlayer() {
//        return mMediaPlayer;
//    }
//
//    @Override
//    public void doSeekTo(int position) {
//        if(mVideoView==null)return;
//        mVideoView.seekTo(position);
//    }
//
//    @Override
//    public void doReStart() {
//        doPrepare(videoPath,thumbUrl);
//    }
//
//    @Override
//    public void doRelease() {
//        if(mMediaPlayer!=null){
//            if(mMediaPlayer.isPlaying())mMediaPlayer.stop();
//            mMediaPlayer.release();
//            mMediaPlayer = null;
//        }
//        if(mVideoView!=null){
//            mVideoView = null;
//        }
//    }
//}
