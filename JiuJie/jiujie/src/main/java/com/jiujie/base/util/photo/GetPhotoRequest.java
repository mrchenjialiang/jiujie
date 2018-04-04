package com.jiujie.base.util.photo;

import java.util.List;

/**
 * Created by ChenJiaLiang on 2018/3/21.
 * Email:576507648@qq.com
 */

public interface GetPhotoRequest {
    void onGetPhotoEnd(boolean isFromCamera, List<String> imagePathList);
    boolean isShouldCrop();
    int getPhotoMaxSize();
    float getCropScaleHeight();
    List<String> getCheckedPhotoList();
    String getCameraOutPutDir();
    String getOutPutFileName();
    int getCropFileMaxLength();
    void showChooseImageDialog();
}
