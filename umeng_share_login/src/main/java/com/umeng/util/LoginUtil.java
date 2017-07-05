package com.umeng.util;

import android.app.Activity;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * 友盟分享SDK 6.3.0
 */
public class LoginUtil {

    /**
     * 授权
     * 授权中只是能拿到uid，openid，token这些授权信息，想获取用户名和用户资料，需要调用获取用户信息的接口。
     * mShareAPI.getPlatformInfo(UserinfoActivity.this, SHARE_MEDIA.SINA, umAuthListener);---获取用户信息
     *
     * openid
     uid （6.2以前用unionid）用户id
     accessToken （6.2以前用access_token）
     RefreshToken（6.2以前用refresh_token）
     expiration （6.2以前用expires_in）过期时间
     */
    public static void loginWx(Activity activity,UMAuthListener umAuthListener){
        UMShareAPI mShareAPI = UMShareAPI.get(activity);
        mShareAPI.doOauthVerify(activity, SHARE_MEDIA.WEIXIN, umAuthListener);
    }

//    {
// unionid=同uid,
// screen_name=昵称,
// city=城市,
// accessToken=*****,
// refreshToken = *****,
// gender=性别,
// province=省份,
// openid=*****,
// profile_image_url=大头像,
// country=国家,
// access_token=*******,
// iconurl=头像,
// name=昵称,
// uid=uid,
// expiration=1231321321,
// language=语言,
// expires_in=123456465}
    public static void loginWxGetUserInfo(Activity activity,UMAuthListener umAuthListener){
        loginGetUserInfo(activity,SHARE_MEDIA.WEIXIN,umAuthListener);
    }


    //QQ:  {unionid=, is_yellow_vip=0, screen_name=昵称, msg=, vip=0, 
    // city=, accessToken=796DA480B4668504B7739A0A5810FC56, gender=男, province=, 
    // is_yellow_year_vip=0, openid=C28920E3DB700C643F384070995029ED, yellow_vip_level=0, 
    // profile_image_url=http://q.qlogo.cn/qqapp/1106103078/C28920E3DB700C643F384070995029ED/100, 
    // access_token=796DA480B4668504B7739A0A5810FC56, iconurl=http://q.qlogo.cn/qqapp/1106103078/C28920E3DB700C643F384070995029ED/100, 
    // name=昵称, uid=C28920E3DB700C643F384070995029ED, expiration=1505267914803, 
    // expires_in=1505267914803, ret=0, level=0}
    public static void loginQqGetUserInfo(Activity activity,UMAuthListener umAuthListener){
        loginGetUserInfo(activity,SHARE_MEDIA.QQ,umAuthListener);
    }

    public static void loginSinaGetUserInfo(Activity activity,UMAuthListener umAuthListener){
        loginGetUserInfo(activity,SHARE_MEDIA.SINA,umAuthListener);
    }


    private static void loginGetUserInfo(Activity activity,SHARE_MEDIA type,UMAuthListener umAuthListener){
        UMShareAPI mShareAPI = UMShareAPI.get(activity);
        mShareAPI.getPlatformInfo(activity, type, umAuthListener);
    }


}
