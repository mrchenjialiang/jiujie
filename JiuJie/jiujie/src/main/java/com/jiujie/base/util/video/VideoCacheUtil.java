package com.jiujie.base.util.video;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.file.Md5FileNameGenerator;
import com.jiujie.base.util.UIHelper;

import java.io.File;

/**
 * Created by 3987 on 2018/4/4.
 * //获取代理地址的工具，用户获取是否有缓存的地址
 */
public class VideoCacheUtil {
    private static VideoCacheUtil videoCacheUtil;
    //视频缓存
    private HttpProxyCacheServer proxy;
    private Context context;
    private String cacheDir;
    private long cacheSize;
    private String saveDir;

    private VideoCacheUtil() {
    }

    public static VideoCacheUtil instance(){
        if(videoCacheUtil==null){
            videoCacheUtil = new VideoCacheUtil();
        }
        return videoCacheUtil;
    }

    public void init(Context context,String saveDir,String cacheDir,long cacheSize){
        this.context = context.getApplicationContext();
        this.saveDir = saveDir;
        this.cacheDir = cacheDir;
        this.cacheSize = cacheSize;
        if(TextUtils.isEmpty(this.saveDir)){
            this.saveDir = getDefaultSaveDir();
        }
        if(TextUtils.isEmpty(this.cacheDir)){
            this.cacheDir = getDefaultCacheDir();
        }
        if(this.cacheSize==0){
            this.cacheSize = 500*1024*1024;
        }
    }

    private String getDefaultCacheDir() {
        File cacheDir = context.getExternalCacheDir();
        if(cacheDir==null){
            cacheDir = context.getCacheDir();
        }
        return cacheDir.getAbsolutePath()+"/video/";
    }

    private String getDefaultSaveDir(){
        String packageName = context.getPackageName();
        String[] split = packageName.split("\\.");
        StringBuilder dirSb = new StringBuilder(Environment.getExternalStorageDirectory() + "/");
        if (split.length > 1) {
            for (String str : split) {
                if (!str.equals("com")) {
                    dirSb.append(str).append("/");
                }
            }
        } else {
            dirSb.append("jiujie/");
        }
        dirSb.append("video/");
        return dirSb.toString();
    }

    public String getVideoSavePath(String url){
        if(TextUtils.isEmpty(saveDir)){
            return null;
        }
        if(TextUtils.isEmpty(url)){
            return null;
        }
        return saveDir + new Md5FileNameGenerator().generate(url);
    }

    public String getVideoPath(String url) {
        UIHelper.showLog(this,"getVideoPath url:"+url);
        if(!TextUtils.isEmpty(url)&&url.startsWith("http")){
            String videoPath = getVideoSavePath(url);
            if (!TextUtils.isEmpty(videoPath)&&new File(videoPath).exists()) {
                UIHelper.showLog(this,"getVideoPath return videoPath:"+videoPath);
                return videoPath;
            } else {
                //仅能操作网络视频
                HttpProxyCacheServer proxyService = getProxyService();
                proxyService.registerCacheListener(new CacheListener() {
                    @Override
                    public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {

                    }
                },url);
                String proxyUrl = proxyService.getProxyUrl(url);
                UIHelper.showLog(this,"getVideoPath return proxyUrl:"+proxyUrl);
                return proxyUrl;
            }
        }else{
            return url;
        }
    }

    public String getCacheDir(){
        return cacheDir;
    }

    private HttpProxyCacheServer getProxyService() {
        return proxy == null ? (proxy = newProxy()) : proxy;
    }

    private HttpProxyCacheServer newProxy() {
        if(context==null||TextUtils.isEmpty(cacheDir))return null;
        return new HttpProxyCacheServer.Builder(context)
                .cacheDirectory(new File(cacheDir))
                .maxCacheSize(cacheSize)
                .build();
    }

}
