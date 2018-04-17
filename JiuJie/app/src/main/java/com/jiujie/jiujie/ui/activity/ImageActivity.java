package com.jiujie.jiujie.ui.activity;

import android.widget.ImageView;

import com.jiujie.base.util.UIHelper;
import com.jiujie.base.util.WallpaperUtil;
import com.jiujie.glide.GlideUtil;
import com.jiujie.jiujie.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageActivity extends MyBaseActivity {

    @Bind(R.id.i_image)
    ImageView image;

    String imageUrl = "http://s.3987.com/uploadfile/2018/0301/5a97bb46b443a.jpg";

    @Override
    public void initUI() {
        ButterKnife.bind(this);
    }

    @Override
    public boolean isShowTitle() {
        return false;
    }

    @Override
    public void initData() {
        GlideUtil.instance().setDefaultImage(mActivity,imageUrl,image);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_image;
    }

    @OnClick(R.id.i_btn_set_wallpaper)
    public void onSetWallpaper(){
        WallpaperUtil.setWallPaper(mActivity,imageUrl);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    UIHelper.showToastShort("线程 getApplicationContext");
//                Toast.makeText(getApplicationContext(),"线程 Activity",Toast.LENGTH_SHORT).show();
                    UIHelper.showLog("线程 getApplicationContext");
                    Thread.sleep(1000);
                    UIHelper.showToastShort("线程 Activity");
//                Toast.makeText(MainActivity.this,"线程 Activity",Toast.LENGTH_SHORT).show();
                    UIHelper.showLog("线程 Activity");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
