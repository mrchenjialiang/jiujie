package com.jiujie.base.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.Checkable;

import com.jiujie.base.R;
import com.jiujie.base.jk.OnCheckedChangeListener;

/**
 * author : Created by ChenJiaLiang on 2016/10/12.
 * Email : 576507648@qq.com
 */
public class SwitchImageButton extends View implements Checkable {

    private Context context;

    private Bitmap bitmapCenter;
    private Bitmap bitmapBottom;
    private Paint paint;
    private PorterDuffXfermode pdfMode;

    private int viewWidth;
    private int viewHeight;
    private Bitmap bitmapTop;
    private float moveX;
    private int bitmapCenterWidth;
//    private int bitmapCenterHeight;
    private int bitmapTopWidth;
    private int bitmapTopHeight;
    private float downX;
    private int maxMoveX;
    private int mTouchSlop;
    private float downY;

    public SwitchImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public SwitchImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SwitchImageButton(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.context = context;

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        bitmapCenter = decodeBitmap(R.drawable.switch_center);
        bitmapBottom = decodeBitmap(R.drawable.switch_bottom);
        bitmapTop = decodeBitmap(R.drawable.switch_top);

        viewWidth = bitmapBottom.getWidth();
        viewHeight = bitmapBottom.getHeight();

        bitmapCenterWidth = bitmapCenter.getWidth();
//        bitmapCenterHeight = bitmapCenter.getHeight();

        bitmapTopWidth = bitmapTop.getWidth();
        bitmapTopHeight = bitmapTop.getHeight();

        paint = new Paint();
        pdfMode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);

        maxMoveX = viewWidth - jG - bitmapTopWidth;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(viewWidth, viewHeight);
    }

    private Bitmap decodeBitmap(int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

    private final int jG = 2;
    @Override
    protected void onDraw(Canvas canvas) {

//        canvas.drawColor(Color.WHITE);

        int saveFlags = Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG;
        canvas.saveLayer(0, 0, viewWidth, viewHeight, null, saveFlags);

        canvas.drawBitmap(bitmapBottom, 0, 0, paint);
        paint.setXfermode(pdfMode);

        float useMoveX = moveX;
        if(useMoveX<jG){
            useMoveX = jG;
        }else if(useMoveX> maxMoveX){
            useMoveX = maxMoveX;
        }
        float centerBitmapX;
        if(useMoveX==jG){
            centerBitmapX = -bitmapCenterWidth/2;
        }else if(useMoveX== maxMoveX){
            centerBitmapX = 0;
        }else{
            centerBitmapX = -bitmapCenterWidth/2 + useMoveX + bitmapTopWidth/2;
        }
        canvas.drawBitmap(bitmapCenter, centerBitmapX, 0, paint);
        paint.setXfermode(null);

        canvas.drawBitmap(bitmapTop, useMoveX, (viewHeight - bitmapTopHeight) / 2, paint);
        canvas.restore();
    }

    public void setChecked(boolean isCheck){
        if(this.isCheck==isCheck){
            return;
        }
        this.isCheck = isCheck;
        if(isCheck){
            moveX = maxMoveX;
        }else{
            moveX = 0;
        }
        invalidate();
    }

    public void toggle() {
        setChecked(!isCheck);
    }

    public boolean isChecked(){
        return isCheck;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float upX = event.getX();
                if(upX ==downX&&event.getY()-downY<mTouchSlop){
                    doClick();
                }else{
                    if(moveX<=maxMoveX/2){
                        if(isCheck){
                            changeCheck();
                        }
                        doAnim((int)moveX,0);
                    }else{
                        if(!isCheck){
                            changeCheck();
                        }
                        doAnim((int)moveX,maxMoveX);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                doMove(event);
                break;
        }
        return isEnabled();
//        return true;
    }

    private boolean isCheck;
    private void doClick() {
        if(isCheck){
            doAnim(maxMoveX,0);
        }else{
            doAnim(0,maxMoveX);
        }
        changeCheck();
    }

    private void changeCheck() {
        isCheck = !isCheck;
        if(onCheckChangeListen!=null)onCheckChangeListen.onCheckedChanged(this,this, isCheck);
    }

    private void doAnim(int from,int to){
        if(from<0){
            from = 0;
        }
        if(from>maxMoveX){
            from = maxMoveX;
        }
        if(to<0){
            to = 0;
        }
        if(to>maxMoveX){
            to = maxMoveX;
        }
        ValueAnimator valueAnimator = ValueAnimator.ofInt(from, to);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                moveX = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setDuration(200);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }

    private boolean isShouldReDraw = true;
    private void doMove(MotionEvent event) {
        float currentX = event.getX();
        moveX = (isCheck?maxMoveX:0) + currentX - downX;
        if(moveX>0&&moveX<maxMoveX){
            isShouldReDraw = true;
        }else{
            if(isShouldReDraw){
                if(moveX<=0){
                    isShouldReDraw = false;
                    invalidate();
                }else if(moveX>=maxMoveX){
                    isShouldReDraw = false;
                    invalidate();
                }
            }
        }
        if(isShouldReDraw)invalidate();
    }

    private OnCheckedChangeListener onCheckChangeListen;

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckChangeListen) {
        this.onCheckChangeListen = onCheckChangeListen;
    }
}
