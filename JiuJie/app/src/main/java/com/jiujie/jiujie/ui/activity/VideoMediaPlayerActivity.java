//package com.jiujie.jiujie.ui.activity;
//
//import android.os.Environment;
//import android.view.SurfaceView;
//import android.view.ViewGroup;
//
//import com.jiujie.base.util.UIHelper;
//import com.jiujie.base.util.video.VideoCacheUtil;
//import com.jiujie.base.util.video.VideoUtil;
//import com.jiujie.jiujie.R;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//public class VideoMediaPlayerActivity extends MyBaseActivity {
//
//    @Bind(R.id.vmp_surfaceView)
//    SurfaceView mSurfaceView;
////    private String videoPath = Environment.getExternalStorageDirectory()+"/Download/wallpaperfairy/liveWallpaper/111.mp4";
////    private String videoPath = Environment.getExternalStorageDirectory()+"/Download/wallpaperfairy/liveWallpaper/222.mp4";//问题火影
////    private String videoPath = "http://s.3987.com/uploadfile/userload/vedio/2018/0507/20180507121253269.mp4";//竖屏视频
////    private String videoPath = "http://s.3987.com/uploadfile/video/2018/0428/20180428093338723.mp4";//横屏视频
//    private VideoUtil videoUtil;
//
//
//    //    private String videoPath = Environment.getExternalStorageDirectory()+"/question_huoying.mp4";//问题火影
//    private String videoPath = Environment.getExternalStorageDirectory()+"/wanghong.mp4";//问题网红
////    private String videoPath = Environment.getExternalStorageDirectory()+"/video_heng.mp4";//横屏视频
//
//    @Override
//    public void initUI() {
//        ButterKnife.bind(this);
//        mTitle.setLeftButtonBack();
//        mTitle.setTitleText("视频播放");
//
//
//        UIHelper.showLog("videoPath:"+videoPath);
//        videoUtil = new VideoUtil(mSurfaceView,true,true,(ViewGroup) mSurfaceView.getParent());
//        videoUtil.doPrepare(VideoCacheUtil.instance().getVideoPath(videoPath),null);
//    }
//
//    @Override
//    public void initData() {
////        setLoading();
////        doPrepare();
//    }
//
//    @Override
//    protected void onDestroy() {
//        videoUtil.doRelease();
//        super.onDestroy();
//    }
//
//    @OnClick(R.id.vmp_controller)
//    public void onVideoControllerClick(){
//        if(videoUtil.isPlaying()){
//            videoUtil.doPause();
//        }else{
//            videoUtil.doStart();
//        }
//    }
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.activity_video_media_player;
//    }
//}
