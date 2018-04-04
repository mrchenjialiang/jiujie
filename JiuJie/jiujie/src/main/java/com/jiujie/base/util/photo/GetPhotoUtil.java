package com.jiujie.base.util.photo;

import android.app.Activity;
import android.content.Intent;

import com.jiujie.base.dialog.BottomListDialog;
import com.jiujie.base.jk.OnItemClickListen;
import com.jiujie.base.jk.OnListener;
import com.jiujie.base.util.ImageUtil;
import com.jiujie.base.util.UIHelper;

import java.util.List;

/**
 * 注：onActivityResult需要在Activity的onActivityResult中调用
 * Created by ChenJiaLiang on 2018/3/21.
 * Email:576507648@qq.com
 */
public abstract class GetPhotoUtil implements GetPhotoRequest{
    private final Activity mActivity;
    private BottomListDialog bottomListDialog;

    public GetPhotoUtil(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public boolean isShouldCrop() {
        return false;
    }

    @Override
    public int getPhotoMaxSize() {
        return 1;
    }

    @Override
    public float getCropScaleHeight() {
        return 1;
    }

    @Override
    public List<String> getCheckedPhotoList() {
        return null;
    }

    @Override
    public String getCameraOutPutDir() {
        return ImageUtil.instance().getCacheSDDic(mActivity)+ "res/image/camera";
    }

    @Override
    public String getOutPutFileName() {
        return UIHelper.getTimeFileName(".jpg");
    }

    @Override
    public int getCropFileMaxLength() {
        return 0;
    }

    @Override
    public void showChooseImageDialog() {
        if(bottomListDialog==null){
            bottomListDialog = new BottomListDialog(mActivity);
            bottomListDialog.create(new String[]{"拍照","从相册选取"});
            bottomListDialog.setOnItemClickListen(new OnItemClickListen() {
                @Override
                public void click(int i) {
                    if(i==0){
                        takePhotoFromCamera();
                    }else{
                        takePhotoFromAlbum();
                    }
                }
            });
        }
        bottomListDialog.show();
    }

    public void takePhotoFromCamera() {
        GetPictureUtil.getPhotoFromCamera(mActivity,
                getCameraOutPutDir(),
                getOutPutFileName(),
                isShouldCrop(),
                getCropScaleHeight(),
                getCropFileMaxLength(),
                new OnListener<List<String>>() {
                    @Override
                    public void onListen(List<String> strings) {
                        onGetPhotoEnd(true,strings);
                    }
                }
        );
    }

    public void takePhotoFromAlbum() {
        GetPictureUtil.getPhotoFromAlbum(mActivity,
                getPhotoMaxSize(),
                getCheckedPhotoList(),
                isShouldCrop(),
                getCropScaleHeight(),
                getCropFileMaxLength(),
                new OnListener<List<String>>() {
                    @Override
                    public void onListen(List<String> strings) {
                        onGetPhotoEnd(false,strings);
                    }
                }
        );
    }

    public void onActivityResult(Activity mActivity, int requestCode, int resultCode, Intent data) {
        GetPictureUtil.onActivityResult(mActivity,requestCode,resultCode,data);
    }
}
