package com.jiujie.base.util.glide;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * @author : Created by ChenJiaLiang on 2017/1/12.
 *         Email : 576507648@qq.com
 */
public class GlideRoundTransform extends BitmapTransformation {

    private static float radius = 0f;
    private static float scaleHeight = 0;

    public GlideRoundTransform(Context context) {
        this(context, 4);
    }

    public GlideRoundTransform(Context context, int dp) {
        super(context);
        radius = Resources.getSystem().getDisplayMetrics().density * dp;
    }

    public GlideRoundTransform(Context context, int dp,float scaleHeight) {
        super(context);
        radius = Resources.getSystem().getDisplayMetrics().density * dp;
        GlideRoundTransform.scaleHeight = scaleHeight;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, toTransform);
    }

    private static Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        Bitmap result;
        if(scaleHeight!=0){
            result = pool.get(source.getWidth(), (int) (source.getWidth()*scaleHeight), Bitmap.Config.RGB_565);
        }else{
            result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.RGB_565);
        }
        if (result == null) {
            if(scaleHeight!=0){
                result = Bitmap.createBitmap(source.getWidth(), (int) (source.getWidth()*scaleHeight), Bitmap.Config.RGB_565);
            }else{
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.RGB_565);
            }
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF;
        if(scaleHeight!=0){
            rectF = new RectF(0f, 0f, source.getWidth(), source.getWidth()*scaleHeight);
        }else{
            rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
        }
        canvas.drawRoundRect(rectF, radius, radius, paint);
        return result;
    }

    @Override
    public String getId() {
        return getClass().getName() + Math.round(radius);
    }
}
