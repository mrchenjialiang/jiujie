package com.jiujie.base.dialog;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.jiujie.base.R;
import com.jiujie.base.util.GetPictureUtil;
import com.jiujie.base.util.ImageUtil;

import java.io.File;

/**
 * author : Created by ChenJiaLiang on 2016/8/24.
 * Email : 576507648@qq.com
 */
public class SelectorPicDialog extends BaseDialog implements View.OnClickListener {

    private final Activity mActivity;
    private OnImageSelected onImageSelected;
    private boolean isShouldCrop;
    private String cameraPicPath;

    public SelectorPicDialog(Activity activity) {
        super(activity);
        this.mActivity = activity;
    }

    @Override
    protected void initUI(View layout) {
        layout.findViewById(R.id.dialog_bottom_btn_alum).setOnClickListener(this);
        layout.findViewById(R.id.dialog_bottom_btn_camera).setOnClickListener(this);
        layout.findViewById(R.id.dialog_bottom_btn_cancel).setOnClickListener(this);
    }

    public void create(){
        create(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM, R.style.BottomAlertAni);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_selector_pic;
    }

    public void onClick(View v){
        dismiss();
        int i = v.getId();
        if (i == R.id.dialog_bottom_btn_alum) {
            GetPictureUtil.getPicFromAlbum(mActivity, GetPictureUtil.GetPictureFromAlbum);
        } else if (i == R.id.dialog_bottom_btn_camera) {
            cameraPicPath = GetPictureUtil.getPicFromCamera(mActivity, GetPictureUtil.GetPictureFromCamera);
        } else if (i == R.id.dialog_bottom_btn_cancel) {
        }
    }

    public interface OnImageSelected{
        void onImageSelected(String imageLocalPath);
    }

    public void setOnImageSelected(OnImageSelected onImageSelected){
        this.onImageSelected = onImageSelected;
    }

    public void setShouldCrop(boolean isShouldCrop){
        this.isShouldCrop = isShouldCrop;
    }

    private void toCrop(Activity activity,File file, int width, int height){
        String dic = ImageUtil.instance().getImageLocalDic(mActivity);
        GetPictureUtil.cropImage(activity,file.getAbsolutePath(),GetPictureUtil.GetPictureToCrop,width,height,dic,"userIcon");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==GetPictureUtil.GetPictureFromAlbum&&resultCode==Activity.RESULT_OK&&data!=null){
            String albumPicPath = GetPictureUtil.getAlbumPicFromResult(data, mActivity);
            if(!TextUtils.isEmpty(albumPicPath)){
                File file = new File(albumPicPath);
                if(file.exists()){
                    if(isShouldCrop){
                        toCrop(mActivity,file,150,150);
                    }else{
                        if(onImageSelected!=null)onImageSelected.onImageSelected(albumPicPath);
                    }
                }
            }
        }else if(requestCode==GetPictureUtil.GetPictureFromCamera&&resultCode==Activity.RESULT_OK){
            File file = new File(cameraPicPath);
            if(file.exists()&&file.length()>0){
                if(isShouldCrop){
                    toCrop(mActivity,file,150,150);
                }else{
                    if(onImageSelected!=null)onImageSelected.onImageSelected(cameraPicPath);
                }
            }
        }else if(requestCode==GetPictureUtil.GetPictureToCrop&&resultCode==Activity.RESULT_OK){
            if(data!=null){
                String path = data.getStringExtra("path");
                File file = new File(path);
                if(file.exists()){
                    if(onImageSelected!=null)onImageSelected.onImageSelected(path);
                }
            }
        }
    }
}
