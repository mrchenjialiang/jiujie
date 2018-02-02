package com.jiujie.base.activity.cropimage;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiujie.base.R;
import com.jiujie.base.activity.BaseActivity;
import com.jiujie.base.jk.OnListener;
import com.jiujie.base.util.UIHelper;
import com.jiujie.base.widget.JJSimpleButton;

import java.io.File;

public class JJCropImageActivity extends BaseActivity {

    private static OnListener<String> onCropListener;
    private Dialog waitingDialog;
    private JJCropImageView cropImageView;

    /**
     * @param scaleHeight 裁剪的宽高比例
     */
    public static void launch(Activity activity, String path, String savePath, String saveName, float scaleHeight, OnListener<String> onCropListener){
        launch(activity, path, savePath, saveName, scaleHeight,0, onCropListener);
    }

    /**
     * @param scaleHeight 裁剪的宽高比例
     */
    public static void launch(Activity activity, String path, String savePath, String saveName, float scaleHeight,int cropFileMaxLength, OnListener<String> onCropListener){
        JJCropImageActivity.onCropListener = onCropListener;
        activity.startActivity(new Intent(activity,JJCropImageActivity.class)
                .putExtra("path",path)
                .putExtra("savePath",savePath)
                .putExtra("saveName",saveName)
                .putExtra("scaleHeight",scaleHeight)
                .putExtra("cropFileMaxLength",cropFileMaxLength)
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onCropListener = null;
        if(waitingDialog!=null&&waitingDialog.isShowing()){
            waitingDialog.dismiss();
        }
    }
    private void initTitle() {
        LinearLayout baseTitleLayout = getBaseTitleLayout();
        baseTitleLayout.findViewById(R.id.tcp_btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView titleTextTv = (TextView) baseTitleLayout.findViewById(R.id.tcp_title);
        titleTextTv.setText("裁剪图片");
        titleTextTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        JJSimpleButton mBtnEnsure = (JJSimpleButton) baseTitleLayout.findViewById(R.id.tcp_btn_ensure);
        mBtnEnsure.setText("确定").refresh();
        mBtnEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(waitingDialog==null) waitingDialog = UIHelper.getWaitingDialog(mActivity);
                waitingDialog.show();
                cropImageView.doSave(new OnListener<String>() {
                    @Override
                    public void onListen(String s) {
                        waitingDialog.dismiss();
                        if(onCropListener!=null){
                            onCropListener.onListen(s);
                        }
                        finish();
                    }
                });
            }
        });
    }

    @Override
    public int getCustomTitleLayoutId() {
        return R.layout.title_choose_photo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String mPath = intent.getStringExtra("path");
        if(TextUtils.isEmpty(mPath)){
            UIHelper.showToastShort(mActivity,"图片不存在");
            finish();
            return;
        }
        if(!new File(mPath).exists()){
            UIHelper.showToastShort(mActivity,"图片不存在");
            finish();
            return;
        }
        String savePath = intent.getStringExtra("savePath");
        String saveName = intent.getStringExtra("saveName");
        float scaleHeight = intent.getFloatExtra("scaleHeight", 1);
        int cropFileMaxLength = intent.getIntExtra("cropFileMaxLength", 0);

        initTitle();

        cropImageView = (JJCropImageView) findViewById(R.id.ci_cropImageView);
        cropImageView.setCropData(mPath, savePath, saveName, scaleHeight,cropFileMaxLength);

        //自带的，返回键不好整，可能颜色和背景对不上，不好定死
//        mTitle.setLeftButtonBack();
//        mTitle.setTitleText("裁剪图片");
//        mTitle.setRightTextButtonText("确定");
//        mTitle.setRightTextButtonClick(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    @Override
    protected boolean isOpenSlideBack() {
        return false;
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.jiujie_activity_crop_image;
    }
}
