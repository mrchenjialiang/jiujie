package com.jiujie.glide;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

/**
 * Created by ChenJiaLiang on 2018/4/11.
 * Email:576507648@qq.com
 */
public class GlideRequestM {
    private boolean isShouldReturn;
    private Object contextObject;
    private ImageView imageView;
    private RequestManager requestManager;
    private Context context;

    public GlideRequestM(Object contextObject, ImageView imageView) {
        this.contextObject = contextObject;
        this.imageView = imageView;
    }

    boolean isShouldReturn() {
        return isShouldReturn;
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }

    public Context getContext() {
        return context;
    }

    public GlideRequestM init() {
        if(contextObject!=null&&contextObject instanceof Activity){
            Activity activity = (Activity) contextObject;
            if(activity.isFinishing()){
                isShouldReturn = true;
                return this;
            }
            requestManager = Glide.with(activity);
            context = activity;
        }else if(contextObject!=null&&contextObject instanceof Fragment){
            Fragment fragment = (Fragment) contextObject;
            requestManager = Glide.with(fragment);
            context = fragment.getContext();
        }else if(imageView!=null){
            requestManager = Glide.with(imageView);
            context = imageView.getContext();
        }else{
            isShouldReturn = true;
            return this;
        }
        isShouldReturn = false;
        return this;
    }
}
