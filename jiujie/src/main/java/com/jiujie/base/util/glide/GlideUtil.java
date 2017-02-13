package com.jiujie.base.util.glide;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jiujie.base.R;
import com.jiujie.base.util.UIHelper;

import java.io.File;

/**
 * @author : Created by ChenJiaLiang on 2017/1/12.
 *         Email : 576507648@qq.com
 */
public class GlideUtil {

    private static GlideUtil glideUtil;
    private boolean isKeepInMemory;

    private GlideUtil() {
        long romAvailableSize = UIHelper.getRomAvailableSize();
        isKeepInMemory = romAvailableSize > 100 * 1024 * 1024;
    }

    public static GlideUtil instance() {
        if (glideUtil == null) {
            glideUtil = new GlideUtil();
        }
        return glideUtil;
    }

    public boolean isKeepInMemory() {
        return isKeepInMemory;
    }

    public void setSimpleImage(Context context, String url, ImageView imageView) {
        DrawableRequestBuilder<String> builder = Glide.with(context)
                .load(url)
                .crossFade();
        if (!isKeepInMemory) {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        builder.into(imageView);
    }

    public void setSimpleImage(FragmentActivity activity, String url, ImageView imageView) {
        DrawableRequestBuilder<String> builder = Glide.with(activity)
                .load(url)
                .crossFade();
        if (!isKeepInMemory) {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        builder.into(imageView);
    }

    public void setSimpleImage(Fragment fragment, String url, ImageView imageView) {
        DrawableRequestBuilder<String> builder = Glide.with(fragment)
                .load(url)
                .crossFade();
        if (!isKeepInMemory) {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        builder.into(imageView);
    }

    public void setDefaultNoCenterCropImage(Context context, String url, ImageView imageView) {
        DrawableRequestBuilder<String> builder = Glide.with(context)
                .load(url)
                .placeholder(R.drawable.logo_gray)
                .crossFade();
        if (!isKeepInMemory) {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        builder.into(imageView);
    }

    public void setDefaultImage(Context context, String url, ImageView imageView) {
        DrawableRequestBuilder<String> builder = Glide.with(context)
                .load(url)
                .placeholder(R.drawable.logo_gray)
                .crossFade()
                .centerCrop();
        if (!isKeepInMemory) {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        builder.into(imageView);
    }

    public void setDefaultImage(FragmentActivity activity, String url, ImageView imageView) {
        DrawableRequestBuilder<String> builder = Glide.with(activity)
                .load(url)
                .placeholder(R.drawable.logo_gray)
                .crossFade()
                .centerCrop();
        if (!isKeepInMemory) {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        builder
                .into(imageView);
    }

    public void setDefaultImage(Fragment fragment, String url, ImageView imageView) {
        DrawableRequestBuilder<String> builder = Glide.with(fragment)
                .load(url)
                .placeholder(R.drawable.logo_gray)
                .crossFade()
                .centerCrop();
        if (!isKeepInMemory) {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        builder
                .into(imageView);
    }

    public void setStockKLineImage(Context context, String url, ImageView imageView) {
        DrawableRequestBuilder<String> builder = Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用磁盘缓存
                .skipMemoryCache(true)//跳过内存缓存
                .dontAnimate()
                .centerCrop();
        if (!isKeepInMemory) {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        builder
                .into(imageView);
    }

    public void setDefaultNoCacheImage(Context context, String url, ImageView imageView) {
        DrawableRequestBuilder<String> builder = Glide.with(context)
                .load(url)
                .placeholder(R.drawable.logo_gray)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用磁盘缓存
                .skipMemoryCache(true)//跳过内存缓存
                .crossFade()
                .centerCrop();
        if (!isKeepInMemory) {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        builder
                .into(imageView);
    }

    public void setDefaultNoCacheImage(Context context, int id, ImageView imageView) {
        DrawableRequestBuilder<Integer> builder = Glide.with(context)
                .load(id)
                .placeholder(R.drawable.logo_gray)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用磁盘缓存
                .skipMemoryCache(true)//跳过内存缓存
                .crossFade()
                .centerCrop();
        if (!isKeepInMemory) {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        builder
                .into(imageView);
    }

    public void setDefaultNoCacheImage(Context context, File file, ImageView imageView) {
        DrawableRequestBuilder<File> builder = Glide.with(context)
                .load(file)
                .placeholder(R.drawable.logo_gray)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用磁盘缓存
                .skipMemoryCache(true)//跳过内存缓存
                .crossFade()
                .centerCrop();
        if (!isKeepInMemory) {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        builder
                .into(imageView);
    }

    public void setDefaultNoCacheImage(FragmentActivity activity, String url, ImageView imageView) {
        DrawableRequestBuilder<String> builder = Glide.with(activity)
                .load(url)
                .placeholder(R.drawable.logo_gray)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用磁盘缓存
                .skipMemoryCache(true)//跳过内存缓存
                .crossFade()
                .centerCrop();
        if (!isKeepInMemory) {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        builder
                .into(imageView);
    }

    public void setDefaultNoCacheImage(Fragment fragment, String url, ImageView imageView) {
        DrawableRequestBuilder<String> builder = Glide.with(fragment)
                .load(url)
                .placeholder(R.drawable.logo_gray)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用磁盘缓存
                .skipMemoryCache(true)//跳过内存缓存
                .crossFade()
                .centerCrop();
        if (!isKeepInMemory) {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        builder
                .into(imageView);
    }

    public void setCircleImage(Context context, String url, ImageView imageView) {
        DrawableRequestBuilder<String> builder = Glide.with(context)
                .load(url)
                .placeholder(R.drawable.circle_bg_gray)
                .transform(new GlideCircleTransform(context.getApplicationContext()))
                .crossFade();
        if (!isKeepInMemory) {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        builder
//                .centerCrop()//不能这样，否则会变成方形
                .into(imageView);
    }

    public void setCircleImage(FragmentActivity activity, String url, ImageView imageView) {
        DrawableRequestBuilder<String> builder = Glide.with(activity)
                .load(url)
                .placeholder(R.drawable.circle_bg_gray)
                .transform(new GlideCircleTransform(activity.getApplicationContext()))
                .crossFade();
        if (!isKeepInMemory) {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        builder
//                .centerCrop()//不能这样，否则会变成方形
                .into(imageView);
    }

    public void setCircleImage(Fragment fragment, String url, ImageView imageView) {
        DrawableRequestBuilder<String> builder = Glide.with(fragment)
                .load(url)
                .placeholder(R.drawable.circle_bg_gray)
                .transform(new GlideCircleTransform(fragment.getActivity().getApplicationContext()))
                .crossFade();
        if (!isKeepInMemory) {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        builder
//                .centerCrop()//不能这样，否则会变成方形
                .into(imageView);
    }

    public void setConnerImage(Context context, String url, ImageView imageView, int dp) {
        DrawableRequestBuilder<String> builder = Glide.with(context)
                .load(url)
                .placeholder(R.drawable.logo_gray_conner)
                .transform(new GlideRoundTransform(context.getApplicationContext(), dp))
                .crossFade();
        if (!isKeepInMemory) {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        builder
//                .centerCrop()//不能这样，否则会变成方形
                .into(imageView);
    }

    public void setConnerImage(FragmentActivity activity, String url, ImageView imageView, int dp) {
        DrawableRequestBuilder<String> builder = Glide.with(activity)
                .load(url)
                .placeholder(R.drawable.logo_gray_conner)
                .transform(new GlideRoundTransform(activity.getApplicationContext(), dp))
                .crossFade();
        if (!isKeepInMemory) {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        builder
//                .centerCrop()//不能这样，否则会变成方形
                .into(imageView);
    }

    public void setConnerImage(Fragment fragment, String url, ImageView imageView, int dp) {
        DrawableRequestBuilder<String> builder = Glide.with(fragment)
                .load(url)
                .placeholder(R.drawable.logo_gray_conner)
                .transform(new GlideRoundTransform(fragment.getActivity().getApplicationContext(), dp))
                .crossFade();
        if (!isKeepInMemory) {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        builder
//                .centerCrop()//不能这样，否则会变成方形
                .into(imageView);
    }

}
