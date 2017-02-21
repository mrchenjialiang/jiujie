package com.umeng.util;

import android.app.Activity;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

public class ShareUtil {

    public static final int WEIXIN = 0;//微信好友
    public static final int WEIXIN_FAVORITE = 1;//微信收藏
    public static final int WEIXIN_CIRCLE = 2;//微信朋友圈
    public static void share(Activity activity,int type,String url,String title,String content,String imageUrl,UMShareListener shareListener){
        SHARE_MEDIA share_media = null;
        switch (type){
            case WEIXIN:
                share_media = SHARE_MEDIA.WEIXIN;
                break;
            case WEIXIN_FAVORITE:
                share_media = SHARE_MEDIA.WEIXIN_FAVORITE;
                break;
            case WEIXIN_CIRCLE:
                share_media = SHARE_MEDIA.WEIXIN_CIRCLE;
                break;
        }

        UMImage image = new UMImage(activity, imageUrl);//网络图片
//        UMImage image = new UMImage(ShareActivity.this, file);//本地文件
//        UMImage image = new UMImage(ShareActivity.this, R.drawable.xxx);//资源文件
//        UMImage image = new UMImage(ShareActivity.this, bitmap);//bitmap文件
//        UMImage image = new UMImage(ShareActivity.this, byte[]);//字节流

        UMWeb  web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(content);//描述


        new ShareAction(activity).setPlatform(share_media)
                .withMedia(web)
                .setCallback(shareListener)
                .share();
    }
}
