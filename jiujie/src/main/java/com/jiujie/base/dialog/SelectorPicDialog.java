package com.jiujie.base.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.jiujie.base.R;
import com.jiujie.base.util.GetPictureUtil;
import com.jiujie.base.util.ImageUtil;
import com.jiujie.base.util.UIHelper;

import java.io.File;

/**
 * author : Created by ChenJiaLiang on 2016/8/24.
 * Email : 576507648@qq.com
 */
public class SelectorPicDialog extends BaseDialog implements View.OnClickListener {

    private final Activity mActivity;
    private OnImageSelected onImageSelected;
    private String imageLocalName;
    private String imageLocalDic;
    private boolean isShouldCrop;

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
            imageLocalName = UIHelper.timeLongHaoMiaoToString(System.currentTimeMillis(), "yyyy_MM_dd_HH_mm_ss") + ".png";
            imageLocalDic = ImageUtil.instance().getImageLocalDic(mActivity);
            GetPictureUtil.getPicFromCamera(mActivity, imageLocalDic, imageLocalName, GetPictureUtil.GetPictureFromCamera);
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

//        final Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setClassName("com.android.camera", "com.android.camera.CropImage");
//        intent.setData(Uri.fromFile(file));
//        intent.putExtra("outputX", width);
//        intent.putExtra("outputY", height);
//        intent.putExtra("aspectX", width);
//        intent.putExtra("aspectY", height);
//        intent.putExtra("scale", true);
//        intent.putExtra("noFaceDetection", true);
//        intent.putExtra("output", Uri.parse("file:/" + file.getAbsolutePath()));
//        activity.startActivityForResult(intent, GetPictureUtil.GetPictureToCrop);

//        String imageJpgName = ImageUtil.instance().getImageJpgName();
//        String IMAGE_FILE_LOCATION = ImageUtil.instance().getImageLocalDic(mActivity)+imageJpgName;
//        Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);//The Uri to store the big bitmap
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
//        intent.setType("image/*");
//        intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", 1);//宽高比例
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", width);
//        intent.putExtra("outputY", height);
//        intent.putExtra("scale", true);
//        intent.putExtra("return-data", false);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//        intent.putExtra("noFaceDetection", true); // no face detection
//        activity.startActivityForResult(intent, GetPictureUtil.GetPictureToCrop);
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
            String cameraPicPath = imageLocalDic + imageLocalName;
            File file = new File(cameraPicPath);
            if(file.exists()){
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
