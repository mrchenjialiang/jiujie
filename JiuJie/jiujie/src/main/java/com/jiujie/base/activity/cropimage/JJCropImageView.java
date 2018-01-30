package com.jiujie.base.activity.cropimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jiujie.base.jk.OnListener;
import com.jiujie.base.util.ImageUtil;
import com.jiujie.base.util.TaskManager;
import com.jiujie.base.util.glide.GlideUtil;

import java.io.File;

/**
 * Created by ChenJiaLiang on 2017/12/29.
 * Email:576507648@qq.com
 */

public class JJCropImageView extends View{

    private Bitmap bitmap;
    private int width,height;
    private Rect mSrcRect;
    private Rect mDestRect;
    private Paint mPaint;
    private int bitmapLeft;
    private int bitmapTop;
    private int bitmapRight;
    private int bitmapBottom;
    private int cropLeft;
    private int cropTop;
    private int cropRight;
    private int cropBottom;
    private String savePath;
    private String saveName;
    private float scaleHeight;
    private Rect mCropRect;
    private int actionType;
    private float downX;
    private float downY;
    private final int radio = 10;
    private final int radioAction = 30;//响应范围要大点，不然按不到
    private final int minCropWidthHeight = 10;

    public JJCropImageView(Context context) {
        super(context);
        init();
    }

    public JJCropImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public JJCropImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#4BCC60"));
        mPaint.setAntiAlias(true);
        setClickable(true);
    }

    public void setImagePath(final String path, String savePath, String saveName, float scaleHeight){
        if(TextUtils.isEmpty(path)){
            return;
        }
        if(scaleHeight<=0){
            scaleHeight = 1;
        }
        this.savePath = savePath;
        this.saveName = saveName;
        this.scaleHeight = scaleHeight;
        final float finalScaleHeight = scaleHeight;
        new TaskManager<Bitmap>() {
            @Override
            public Bitmap runOnBackgroundThread() {
                return GlideUtil.instance().getImage(getContext(),path);
            }

            @Override
            public void runOnUIThread(Bitmap bitmap) {
                int bitmapWidth = bitmap.getWidth();
                int bitmapHeight = bitmap.getHeight();
                while (bitmapWidth>width||bitmapHeight>height){
                    if(bitmapWidth>width){
                        float scale = bitmapWidth * 1.0f / width;
                        bitmapWidth = width;
                        bitmapHeight = (int) (bitmapHeight/scale);
                    }else if(bitmapHeight>height){
                        float scale = bitmapHeight * 1.0f / height;
                        bitmapHeight = height;
                        bitmapWidth = (int) (bitmapWidth/scale);
                    }
                }
                mSrcRect = new Rect(0, 0, bitmapWidth, bitmapHeight);
                bitmapLeft = width/2 - bitmapWidth/2;
                bitmapTop = height/2 - bitmapHeight/2;
                bitmapRight = bitmapLeft + bitmapWidth;
                bitmapBottom = bitmapTop + bitmapHeight;
                bitmap = ImageUtil.instance().scaleBitmap(bitmap,bitmapRight-bitmapLeft,bitmapBottom - bitmapTop);
                mDestRect = new Rect(bitmapLeft, bitmapTop, bitmapRight, bitmapBottom);

                int cropWidth,cropHeight;
                if(bitmapHeight>bitmapWidth){
                    cropWidth = bitmapWidth;
                    cropHeight = (int) (cropWidth *finalScaleHeight);
                    if(cropHeight >bitmapHeight){
                        float scale = cropHeight * 1.0f / bitmapHeight;
                        cropHeight = bitmapHeight;
                        cropWidth = (int) (cropWidth /scale);
                    }
                }else{
                    cropHeight = bitmapHeight;
                    cropWidth = (int) (cropHeight /finalScaleHeight);
                    if(cropWidth >bitmapWidth){
                        float scale = cropWidth * 1.0f / bitmapWidth;
                        cropWidth = bitmapWidth;
                        cropHeight = (int) (cropHeight /scale);
                    }
                }
                cropLeft = width/2 - cropWidth /2;
                cropTop = height/2 - cropHeight /2;
                cropRight = cropLeft + cropWidth;
                cropBottom = cropTop + cropHeight;
                mCropRect = new Rect(cropLeft, cropTop, cropRight, cropBottom);
                JJCropImageView.this.bitmap = bitmap;
                invalidate();
            }
        }.start();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(bitmap!=null){
            canvas.drawBitmap(bitmap, mSrcRect, mDestRect, mPaint);
            if(mCropRect!=null){
                //阴影块
                mPaint.setColor(Color.parseColor("#66000000"));
                canvas.drawRect(0,0,cropLeft,height,mPaint);
                canvas.drawRect(cropRight,0,width,height,mPaint);
                canvas.drawRect(cropLeft,0,cropRight,cropTop,mPaint);
                canvas.drawRect(cropLeft,cropBottom,cropRight,height,mPaint);

                //选中区域
                mPaint.setColor(Color.parseColor("#4BCC60"));
                canvas.drawCircle(cropLeft,cropTop,radio,mPaint);
                canvas.drawCircle(cropLeft,cropBottom,radio,mPaint);
                canvas.drawCircle(cropRight,cropTop,radio,mPaint);
                canvas.drawCircle(cropRight,cropBottom,radio,mPaint);
//                canvas.drawRect(mCropRect,mPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                actionType = checkActionType(downX, downY);
                break;
            case MotionEvent.ACTION_MOVE:
                if(actionType ==0){
                    break;
                }
                float currentX = event.getX();
                float currentY = event.getY();
                float moveX = currentX - downX;
                float moveY = currentY - downY;
                if(actionType==5){
                    if(cropLeft+moveX<bitmapLeft){
                        moveX = bitmapLeft - cropLeft;
                    }else if(cropRight+moveX>bitmapRight){
                        moveX = bitmapRight - cropRight;
                    }
                    cropLeft+=moveX;
                    cropRight+=moveX;

                    if(cropTop+moveY<bitmapTop){
                        moveY = bitmapTop-cropTop;
                    }else if(cropBottom+moveY>bitmapBottom){
                        moveY = bitmapBottom - cropBottom;
                    }
                    cropTop+=moveY;
                    cropBottom+=moveY;
                }else{
                    //以X为标准
                    if(actionType==1) {
                        //左上点
                        cropLeft+=moveX;
                        if(cropLeft<bitmapLeft){
                            cropLeft = bitmapLeft;
                        }
                        if(cropLeft>cropRight - minCropWidthHeight){
                            cropLeft = cropRight - minCropWidthHeight;
                        }
                        cropTop = (int) (cropBottom - (cropRight - cropLeft)*scaleHeight);
                        if(cropTop<bitmapTop){
                            cropTop = bitmapTop;
                            cropLeft = (int) (cropRight - (cropBottom - cropTop)/scaleHeight);
                        }
                    }else if(actionType==3) {
                        //左下点
                        cropLeft+=moveX;
                        if(cropLeft<bitmapLeft){
                            cropLeft = bitmapLeft;
                        }
                        if(cropLeft>cropRight - minCropWidthHeight){
                            cropLeft = cropRight - minCropWidthHeight;
                        }
                        cropBottom = (int) (cropTop + (cropRight - cropLeft)*scaleHeight);
                        if(cropBottom>bitmapBottom){
                            cropBottom = bitmapBottom;
                            cropLeft = (int) (cropRight - (cropBottom - cropTop)/scaleHeight);
                        }
                    }else if(actionType==2) {
                        //右上点
                        cropRight+=moveX;
                        if(cropRight>bitmapRight){
                            cropRight = bitmapRight;
                        }
                        if(cropRight<cropLeft + minCropWidthHeight){
                            cropRight = cropLeft + minCropWidthHeight;
                        }
                        cropTop = (int) (cropBottom - (cropRight - cropLeft)*scaleHeight);
                        if(cropTop<bitmapTop){
                            cropTop = bitmapTop;
                            cropRight = (int) (cropLeft + (cropBottom - cropTop)/scaleHeight);
                        }
                    }else if(actionType==4) {
                        //右下点
                        cropRight+=moveX;
                        if(cropRight>bitmapRight){
                            cropRight = bitmapRight;
                        }
                        if(cropRight<cropLeft + minCropWidthHeight){
                            cropRight = cropLeft + minCropWidthHeight;
                        }
                        cropBottom = (int) (cropTop + (cropRight - cropLeft)*scaleHeight);
                        if(cropBottom>bitmapBottom){
                            cropBottom = bitmapBottom;
                            cropRight = (int) (cropLeft + (cropBottom - cropTop)/scaleHeight);
                        }
                    }

                }
                postInvalidate();
                downX = currentX;
                downY = currentY;
                break;
        }
        return super.onTouchEvent(event);
    }

    private int checkActionType(float downX, float downY) {
        if(Math.abs(downX-cropLeft)<radioAction&&Math.abs(downY-cropTop)<radioAction){
            //左上点
            return 1;
        }else if(Math.abs(downX-cropRight)<radioAction&&Math.abs(downY-cropTop)<radioAction){
            //右上点
            return 2;
        }else if(Math.abs(downX-cropLeft)<radioAction&&Math.abs(downY-cropBottom)<radioAction){
            //左下点
            return 3;
        }else if(Math.abs(downX-cropRight)<radioAction&&Math.abs(downY-cropBottom)<radioAction){
            //右下点
            return 4;
        }else if(downX>cropLeft&&downX<cropRight&&downY>cropTop&&downY<cropBottom){
            //中间
            return 5;
        }
        return 0;
    }

    public Bitmap getCropBitmap(){
        int fromLeft = cropLeft - bitmapLeft;
        int fromTop = cropTop - bitmapTop;
        int fromRight = fromLeft + cropRight - cropLeft;
        int fromBottom = fromTop + cropBottom - cropTop;
        return ImageUtil.instance().cropImage(bitmap,cropRight-cropLeft,cropBottom-cropTop, fromLeft, fromTop, fromRight, fromBottom);
    }

    public void doSave(final OnListener<String> saveListener){
        new TaskManager<String>() {
            @Override
            public String runOnBackgroundThread() {
                Bitmap cropBitmap = getCropBitmap();
                if(cropBitmap!=null){
                    if(saveName.toLowerCase().contains(".jpg")){
                        ImageUtil.instance().saveImageToLocalAsJpg(savePath,saveName,cropBitmap);
                    }else{
                        ImageUtil.instance().saveImageToLocalAsPng(savePath,saveName,cropBitmap);
                    }
                }
                return new File(savePath,saveName).getAbsolutePath();
            }

            @Override
            public void runOnUIThread(String s) {
                if(saveListener!=null){
                    saveListener.onListen(s);
                }
            }
        }.start();
    }
}
