package com.jiujie.jiujie;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.jiujie.base.APP;
import com.jiujie.base.jk.OnListener;
import com.jiujie.base.jk.SimpleDownloadFileListen;
import com.jiujie.base.util.GetPictureUtil;
import com.jiujie.base.util.ImageUtil;
import com.jiujie.base.util.PermissionsManager;
import com.jiujie.base.util.TaskManager;
import com.jiujie.base.util.UIHelper;
import com.jiujie.base.util.file.SystemDownloadUtil;
import com.jiujie.jiujie.grouplist.GroupListActivity;

public class MainActivity extends MyBaseActivity {

    private String filePath = "/storage/emulated/0/shoujiduoduo/Wallpaper/壁纸多多图片缓存/1516478.jpg";


    @Override
    public void initUI() {
        mTitle.setTitleText("JiuJie首页");


        new TaskManager<Boolean>() {
            @Override
            public Boolean runOnBackgroundThread() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void runOnUIThread(Boolean aBoolean) {
                UIHelper.showToastShort(mActivity,"哇哈哈");
                Toast.makeText(getApplicationContext(), "呵呵", Toast.LENGTH_SHORT).show();
            }
        }.start();

    }

    //    /storage/emulated/0/shoujiduoduo/Wallpaper/壁纸多多图片缓存/1516478.jpg

    @Override
    public void initData() {
//        getSupportFragmentManager().beginTransaction().add(R.id.main_frameLayout,new SimpleListFragment()).commit();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    private String getCropOutPutDir() {
        return ImageUtil.instance().getCacheSDDic(mActivity) + "res/image/camera";
    }

    public void toSd(View view) {
//        try {
//            //获取父目录
//            File parentFile = new File(getCropOutPutDir());
//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.setDataAndType(Uri.fromFile(parentFile), "*/*");
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            startActivity(intent);
//        }catch (Exception e){
//            UIHelper.showToastShort(mActivity,"没有文件管理器");
//        }

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
        PermissionsManager.getPermissionSimple(new OnListener<Boolean>() {
            @Override
            public void onListen(Boolean isHasPermission) {
//                UIHelper
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA);

//        UIHelper.installNormal(mActivity, "/storage/emulated/0/tencent/QQfile_recv/jingling_oppo.apk");//oppo测试机

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
        GetPictureUtil.onActivityResult(mActivity, requestCode, resultCode, data);
    }

    public void toGroupList(View view) {
        startActivity(new Intent(mActivity, GroupListActivity.class));
    }

    public void doDownload(View view) {
        String url = "http://www.5857.com/small/wallpaper_bizhi.apk";
        String name = "ty.apk";
//        String url = "http://d.5857.com/eeeo_180122/001.jpg";
//                String name = "1.jpg";
        new SystemDownloadUtil(url,ImageUtil.instance().getCacheSDDic(mActivity),name,new SimpleDownloadFileListen(){
            @Override
            public void onFail(String error) {
                UIHelper.showLog(UIHelper.isRunInUIThread());
                UIHelper.showToastShort(mActivity,error);
            }

            @Override
            public void onFinish(String filePath) {
                UIHelper.showLog(UIHelper.isRunInUIThread());
                UIHelper.showToastShort(mActivity,filePath);
            }

            @Override
            public void onLoading(long loadedLength, int progress) {
                UIHelper.showLog(UIHelper.isRunInUIThread());
                UIHelper.showLog("onLoading loadedLength:"+loadedLength+",progress:"+progress);
            }
        }).start();
    }

    public void toFrame(View view) {
        startActivity(new Intent(mActivity,FragmentActivity.class));
    }
}
