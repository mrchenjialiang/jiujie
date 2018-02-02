package com.jiujie.base.dialog;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.jiujie.base.R;
import com.jiujie.base.util.GetPictureUtil;

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
//            GetPictureUtil.getPhotoFromAlbum();
        } else if (i == R.id.dialog_bottom_btn_camera) {
//            GetPictureUtil.getPhotoFromCamera();
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        GetPictureUtil.onActivityResult(mActivity,requestCode,resultCode,data);
    }
}
