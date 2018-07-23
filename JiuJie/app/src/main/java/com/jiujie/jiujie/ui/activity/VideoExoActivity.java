package com.jiujie.jiujie.ui.activity;

import android.os.Environment;
import android.view.SurfaceView;
import android.view.TextureView;

import com.google.android.exoplayer2.ExoPlayer;
import com.jiujie.base.util.UIHelper;
import com.jiujie.base.util.video.VideoExoUtil;
import com.jiujie.jiujie.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoExoActivity extends MyBaseActivity {

    //    @Bind(R.id.ve_surfaceView)
//    SurfaceView surfaceView;
    @Bind(R.id.ve_textureView)
    TextureView textureView;
    private VideoExoUtil videoUtil;

    private String[] videoPaths = {
//            "/storage/emulated/0/Download/videowallpaper/liveWallpaper/dad153c5620a2c83546849dc3204f107.mp4",//竖屏视频--桌面上播放两遍就停了
            Environment.getExternalStorageDirectory() + "/zipai1.mp4",//手机自拍
            "http://s.3987.com/uploadfile/userload/vedio/2018/0609/20180609075856324.mp4",//十几M视频
            "http://s.3987.com/uploadfile/userload/vedio/2018/0507/20180507121253269.mp4",//竖屏视频
            "http://s.3987.com/uploadfile/video/2018/0428/20180428093338723.mp4",//横屏视频
            Environment.getExternalStorageDirectory() + "/wanghong.mp4",//问题网红
            Environment.getExternalStorageDirectory() + "/question_huoying.mp4",//问题火影----exo播放这个会失败
            Environment.getExternalStorageDirectory() + "/video_heng.mp4"//问题网红
    };
    private int index = -1;

    @Override
    public void initUI() {
        ButterKnife.bind(this);
        mTitle.setLeftButtonBack();
        mTitle.setTitleText("视频播放");

        reset();
    }

    private void reset() {
        if (videoUtil != null) {
            videoUtil.doRelease();
        }
//        使用textureView，ExoPlayer 会在5.0+识别视频角度；如果使用surfaceView ，不会自动识别角度
//        videoUtil = new VideoExoUtil(this, surfaceView, true, true, VideoExoUtil.VideoScaleType.SCALE_CROP_CENTER);
        videoUtil = new VideoExoUtil(this, textureView, true, true, VideoExoUtil.VideoScaleType.SCALE_CROP_CENTER);


        index++;
        if (index > videoPaths.length - 1) {
            index = 0;
        }

//        index = 0;
        UIHelper.showLog("videoPath:" + videoPaths[index]);
//        videoUtil.doPrepare(VideoCacheUtil.instance().getVideoPath(videoPaths[index]),null);
        videoUtil.doPrepare(videoPaths[index], null);


    }

    @Override
    public void initData() {
    }

    @OnClick(R.id.ve_video_controller)
    public void onVideoControllerClick() {
        if (videoUtil.isPlaying()) {
            videoUtil.doPause();
        } else {
//            videoUtil.doStart();
            reset();
        }
    }

    @OnClick(R.id.ve_btn_set)
    public void onBtnSetClick() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_video_exo;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoUtil.doRelease();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!videoUtil.isPlaying()) {
            videoUtil.doStart();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoUtil.doPause();
    }
}
