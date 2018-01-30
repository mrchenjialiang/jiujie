package com.jiujie.jiujie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jiujie.base.util.ImageUtil;
import com.jiujie.base.util.UIHelper;

import java.io.File;

public class MainActivity extends MyBaseActivity {

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle.setTitleText("JiuJie首页");

        image = (ImageView) findViewById(R.id.main_image);
        initData();
    }

    @Override
    public void initData() {
//        getSupportFragmentManager().beginTransaction().add(R.id.main_frame,new SimpleFragment()).commit();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    public void toCrop(View view) {
    }



    private String getCropOutPutDir(){
        return ImageUtil.instance().getCacheSDDic(mActivity)+ "res/image/crop";
    }

    private String getOutPutFileName(){
        return "123.jpg";
    }

    public void toSd(View view) {
        try {
            //获取父目录
            File parentFile = new File(getCropOutPutDir());
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setDataAndType(Uri.fromFile(parentFile), "*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivity(intent);
        }catch (Exception e){
            UIHelper.showToastShort(mActivity,"没有文件管理器");
        }

//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("*/*");
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        try {
//            startActivity(Intent.createChooser(intent, "打开文件管理器"));
//        } catch (android.content.ActivityNotFoundException ex) {
//            UIHelper.showToastShort(mActivity,"没有文件管理器");
//        }
    }
}
