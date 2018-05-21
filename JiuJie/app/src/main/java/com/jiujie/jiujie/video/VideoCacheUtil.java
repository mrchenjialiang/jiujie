package com.jiujie.jiujie.video;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.file.Md5FileNameGenerator;

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
    }

    public String getVideoSavePath(String url){
        return saveDir + new Md5FileNameGenerator().generate(url);
    }

    public String getVideoPath(String url) {
        String videoPath = getVideoSavePath(url);
        Log.e("VideoCacheUtil","videoPath:"+videoPath);
        if (new File(videoPath).exists()) {
            return videoPath;
        } else {
            if(!TextUtils.isEmpty(url)&&url.startsWith("http")){
                //仅能操作网络视频
                return getProxyService().getProxyUrl(url);
            }else{
                return url;
            }
        }
    }

    public String getCacheDir(){
        return cacheDir;
    }

    private HttpProxyCacheServer getProxyService() {
        return proxy == null ? (proxy = newProxy()) : proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(context)
                .cacheDirectory(new File(cacheDir))
                .maxCacheSize(cacheSize)
                .build();
    }

}
