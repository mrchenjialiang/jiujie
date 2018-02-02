package com.jiujie.base.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.jiujie.base.jk.OnSimpleListener;
import com.jiujie.base.util.UIHelper;

/**
 * Created by ChenJiaLiang on 2018/2/2.
 * Email:576507648@qq.com
 */

public class GuideView extends View{

    private Paint mPaint;
    private Bitmap bitmap;
    private int resultLeft;
    private int resultTop;
    private int backgroundColor = Color.parseColor("#80000000");

    public GuideView(Context context) {
        super(context);
        init();
    }

    public GuideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GuideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint = new Paint();
//        mPaint.setColor(Color.parseColor());
    }

    /**
     *
     * @param type 0:左上角，1：右上角，2：左下角，3：右下角
     */
    public void setData(final View resultView, final int drawId, final int type, final int offX, final int offY){
        UIHelper.getViewDrawListen(resultView, new OnSimpleListener() {
            @Override
            public void onListen() {
                analysis(resultView, drawId, type, offX, offY);
            }
        });
    }

    private void analysis(View resultView, int drawId, int type, int offX, int offY) {
        int[] location = new int[2];
        resultView.getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];

        int resultViewWidth = resultView.getWidth();
        int resultViewHeight = resultView.getHeight();

        bitmap = BitmapFactory.decodeResource(getResources(), drawId);
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        if(type==0){
            resultLeft = viewX;
            resultTop = viewY;
        }else if(type==1){
            resultLeft = viewX + resultViewWidth - bitmapWidth;
            resultTop = viewY;
        }else if(type==2){
            resultLeft = viewX;
            resultTop = viewY + resultViewHeight - bitmapHeight;
        }else if(type==3){
            resultLeft = viewX + resultViewWidth - bitmapWidth;
            resultTop = viewY + resultViewHeight - bitmapHeight;
        }
        resultLeft +=offX;
        resultTop +=offY;
        UIHelper.showLog("resultLeft:"+resultLeft);
        UIHelper.showLog("resultTop:"+resultTop);
        invalidate();
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(backgroundColor);
        if(bitmap!=null){
            canvas.drawBitmap(bitmap,resultLeft,resultTop,mPaint);
        }
    }
}
