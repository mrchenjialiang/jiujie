package com.jiujie.jiujie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.jiujie.base.jk.OnListener;
import com.jiujie.base.util.GetPictureUtil;
import com.jiujie.base.util.ImageUtil;
import com.jiujie.base.util.UIHelper;

import java.io.File;
import java.util.List;

public class MainActivity extends MyBaseActivity {

    private String filePath = "/storage/emulated/0/shoujiduoduo/Wallpaper/壁纸多多图片缓存/1516478.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle.setTitleText("JiuJie首页");

        initData();
    }

    //    /storage/emulated/0/shoujiduoduo/Wallpaper/壁纸多多图片缓存/1516478.jpg

    @Override
    public void initData() {
//        getSupportFragmentManager().beginTransaction().add(R.id.main_frameLayout,new SimpleFragment()).commit();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    private String getCropOutPutDir(){
        return ImageUtil.instance().getCacheSDDic(mActivity)+ "res/image/camera";
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

    public void doAction1(View view) {
        UIHelper.installNormal(mActivity,"/storage/emulated/0/tencent/QQfile_recv/jingling_oppo.apk");//oppo测试机

//        UIHelper.installNormal(mActivity,"/storage/emulated/0/tencent/qqfile_recv/jingling.apk");//小米测试机
//        UIHelper.installNormal(mActivity,"/storage/emulated/0/Download/jingling_m_3987.apk");


//        GetPictureUtil.getPhotoFromCamera(mActivity, getCropOutPutDir(), UIHelper.getTimeFileName(".jpg"), true, 1, 100 * 1024, new OnListener<List<String>>() {
//            @Override
//            public void onListen(List<String> strings) {
//                UIHelper.showLog("strings:"+strings);
//                File file = new File(strings.get(0));
//                if(file.exists()){
//                    UIHelper.showToastShort(mActivity,strings.get(0));
//                    UIHelper.showLog("length:"+file.length());
//                }
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        GetPictureUtil.onActivityResult(mActivity,requestCode,resultCode,data);
    }
}
