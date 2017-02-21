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
        UMShareAPI mShareAPI = UMShareAPI.get(activity);
        mShareAPI.getPlatformInfo(activity, SHARE_MEDIA.WEIXIN, umAuthListener);
    }


}
