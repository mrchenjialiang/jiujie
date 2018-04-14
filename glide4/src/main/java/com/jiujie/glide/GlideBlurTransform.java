package com.jiujie.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.renderscript.RSRuntimeException;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.util.Util;

import java.security.MessageDigest;

import jp.wasabeef.glide.transformations.internal.FastBlur;
import jp.wasabeef.glide.transformations.internal.RSBlur;

/**
 * Created by ChenJiaLiang on 2018/4/9.
 * Email:576507648@qq.com
 */

public class GlideBlurTransform extends BitmapTransformation {
    private Context mContext;
    private int mRadius;
    private int mSampling;
    public GlideBlurTransform(Context mContext,int radius, int sampling) {
        this.mContext = mContext;
        mRadius = radius;
        mSampling = sampling;
    }
    @Override
    protected Bitmap transform(@NonNull BitmapPool mBitmapPool, @NonNull Bitmap source, int outWidth, int outHeight) {
        int width = source.getWidth();
        int height = source.getHeight();
        int scaledWidth = width / mSampling;
        int scaledHeight = height / mSampling;

        Bitmap bitmap = mBitmapPool.get(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        canvas.scale(1 / (float) mSampling, 1 / (float) mSampling);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(source, 0, 0, paint);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            try {
                bitmap = RSBlur.blur(mContext, bitmap, mRadius);
            } catch (RSRuntimeException e) {
                bitmap = FastBlur.blur(bitmap, mRadius, true);
            }
        } else {
            bitmap = FastBlur.blur(bitmap, mRadius, true);
        }

        return bitmap;
//        return BitmapResource.obtain(bitmap, mBitmapPool);
//        return BlurBitmapUtil.instance().blurBitmap(context, toTransform, 20,outWidth,outHeight);
    }
    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GlideBlurTransform that = (GlideBlurTransform) o;
        return mRadius == that.mRadius && mSampling == that.mSampling;
    }

    @Override
    public int hashCode() {
        int result = mRadius;
        result = 31 * result + mSampling;
        return result;
    }
}