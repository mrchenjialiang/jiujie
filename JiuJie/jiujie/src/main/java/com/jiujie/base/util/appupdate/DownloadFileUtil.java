package com.jiujie.base.util.appupdate;

import android.Manifest;
import android.text.TextUtils;

import com.jiujie.base.jk.DownloadFileListen;
import com.jiujie.base.jk.OnListener;
import com.jiujie.base.util.FileUtil;
import com.jiujie.base.util.PermissionsManager;
import com.jiujie.base.util.UIHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author : Created by ChenJiaLiang on 2017/2/24.
 *         Email : 576507648@qq.com
 */
public class DownloadFileUtil {

    private final String url;
    private final String saveDir;
    private final String saveName;
    private final DownloadFileListen downloadListen;

    public DownloadFileUtil(String url,String saveDir,String saveName,DownloadFileListen downloadListen) {
        this.url = url;
        this.saveDir = saveDir;
        this.saveName = saveName;
        this.downloadListen = downloadListen;
    }

    public void start(){
        PermissionsManager.getPermissionSimple(new OnListener<Boolean>() {
            @Override
            public void onListen(Boolean isHasPermission) {
                if(isHasPermission){
                    realStart();
                }else{
                    if (downloadListen != null) downloadListen.onFail("没有读写权限，请在权限管理中给予");
                }
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void realStart(){
        if(TextUtils.isEmpty(url)||TextUtils.isEmpty(saveDir)||TextUtils.isEmpty(saveName)){
            if(downloadListen!=null)downloadListen.onFail("无相关下载资源");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(downloadListen!=null)downloadListen.onPrepare();
                Request request = new Request.Builder().url(url).build();
                // 创建okHttpClient对象
//                OkHttpClient mOkHttpClient = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS).readTimeout(10,TimeUnit.MINUTES).build();
                new OkHttpClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        UIHelper.showLog("DownFileUtil Exception " + e.getMessage());
                        if(downloadListen!=null)downloadListen.onFail("下载失败");
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        FileUtil.requestPermission(new OnListener<Boolean>() {
                            @Override
                            public void onListen(Boolean isHas) {
                                if(isHas){
                                    InputStream is = null;
                                    byte[] buf = new byte[1024];
                                    int len;
                                    FileOutputStream fos = null;
                                    try {
                                        File file = FileUtil.createFile(saveDir,saveName);
                                        if(file==null){
                                            if(downloadListen!=null)downloadListen.onFail("文件创建失败");
                                            return;
                                        }

                                        is = response.body().byteStream();
                                        long total = response.body().contentLength();
                                        fos = new FileOutputStream(file);
                                        long sum = 0;
                                        if(downloadListen!=null)downloadListen.onStart(total);
                                        int oldProgress;
                                        int newProgress = 0;
                                        while ((len = is.read(buf)) != -1) {
                                            fos.write(buf, 0, len);
                                            sum += len;
                                            oldProgress = newProgress;
                                            newProgress = (int) (sum * 1.0f / total * 100);
                                            if(oldProgress!=newProgress){
                                                if(downloadListen!=null)downloadListen.onLoading(sum,newProgress);
                                            }
                                        }
                                        fos.flush();
                                        if(downloadListen!=null)downloadListen.onFinish(new File(saveDir,saveName).getAbsolutePath());
                                    } catch (Exception e) {
                                        UIHelper.showLog("DownFileUtil Exception " + e.getMessage());
                                        e.printStackTrace();
                                        if(downloadListen!=null)downloadListen.onFail("下载失败");
                                    } finally {
                                        try {
                                            if (is != null) is.close();
                                            if (fos != null) fos.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }else{
                                    if(downloadListen!=null)downloadListen.onFail("缺少读写权限");
                                }
                            }
                        });
                    }
                });
            }
        }).start();
    }
}
