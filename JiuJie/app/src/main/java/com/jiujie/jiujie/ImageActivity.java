package com.jiujie.jiujie;

import android.widget.ImageView;

import com.jiujie.base.util.WallpaperUtil;
import com.jiujie.base.util.glide.GlideUtil;

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
}
