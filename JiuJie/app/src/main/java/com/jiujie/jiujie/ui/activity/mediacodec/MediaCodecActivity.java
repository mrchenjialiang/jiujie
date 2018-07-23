package com.jiujie.jiujie.ui.activity.mediacodec;

import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.jiujie.jiujie.R;
import com.jiujie.jiujie.ui.activity.MyBaseActivity;

public class MediaCodecActivity extends MyBaseActivity implements SurfaceHolder.Callback {
    //        private String SAMPLE = Environment.getExternalStorageDirectory()+"/question_huoying.mp4";//问题火影
//    private String SAMPLE = Environment.getExternalStorageDirectory() + "/wanghong.mp4";//问题网红
//        private String SAMPLE = Environment.getExternalStorageDirectory() + "/video_heng.mp4";//横屏视频
//    private String SAMPLE = Environment.getExternalStorageDirectory() + "/111111.mp4";//伪横屏视频
    //    private String SAMPLE = Environment.getExternalStorageDirectory()+"/normal.mp4";//普通视频
    String SAMPLE = Environment.getExternalStorageDirectory() + "/zipai1.mp4";//自拍

    //    private VideoThread videoThread;
//    private SurfaceHolder holder;
    private VideoDecoderHelper videoDecoderHelper;

    protected void onDestroy() {
        super.onDestroy();
//        if(videoThread!=null)videoThread.doRelease();
    }

    @Override
    public void initUI() {
        SurfaceView surfaceView = findViewById(R.id.lsv_surface_view);
        surfaceView.getHolder().addCallback(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_surface_view;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
//        this.holder = holder;
        videoDecoderHelper = new VideoDecoderHelper(holder.getSurface(), SAMPLE);
    }

    @Override
    public void surfaceChanged(final SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        videoDecoderHelper.release();

//        if (videoThread != null) {
//            videoThread.interrupt();
//        }
    }

    //    VideoImageThread videoImageThread;
    public void doStart(View view) {
        videoDecoderHelper.start();


//        if (holder != null) {
////            if(videoImageThread==null)videoImageThread = new VideoImageThread(holder.getSurface(),SAMPLE,0,"video/avc");
//            if(videoImageThread==null)videoImageThread = new VideoImageThread(holder.getSurface(),SAMPLE,0,"video/avc");
//            videoImageThread.doStart();
//
////            if (videoThread == null) videoThread = new VideoThread(holder.getSurface(), SAMPLE);
////            videoThread.start();
//        }
    }

    public void doPause(View view) {
        videoDecoderHelper.stop();


//        UIHelper.showLog("doPause 点击时间"+UIHelper.timeLongHaoMiaoToString(System.currentTimeMillis(),"HH:ss"));
//        if(videoImageThread!=null)videoImageThread.doPause();


//        videoThread.doStop();
    }
}