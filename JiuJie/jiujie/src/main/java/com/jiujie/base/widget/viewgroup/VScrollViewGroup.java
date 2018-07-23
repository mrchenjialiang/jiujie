package com.jiujie.base.widget.viewgroup;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.jiujie.base.R;
import com.jiujie.base.jk.OnItemClickListen;
import com.jiujie.base.jk.OnMyPageChangeListener;
import com.jiujie.base.jk.OnTouchListen;
import com.jiujie.base.util.UIHelper;


public class VScrollViewGroup extends FrameLayout {
    private ViewGroup[] viewGroups;
    private ViewGroup currentViewGroup, lastViewGroup, nextViewGroup;
    private float downX, downY;
    private int moveX, moveY;
    private int mCurrentPosition;
    private ValueAnimator valueAnimator;
    private BaseScrollViewGroupAdapter myScrollViewGroupAdapter;
    private Orientation orientation = Orientation.vertical;
    private VelocityTracker mVelocityTracker = null;

    private boolean isTouching;
    private float tempX, tempY;
    private int mTouchSlop;
    private OnTouchListen onTouchListen;
    private boolean isLoop = true;

    public VScrollViewGroup(Context context) {
        super(context);
        init(context, null);
    }

    public VScrollViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public VScrollViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public enum Orientation {
        vertical, horizontal
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.VScrollViewGroup);
            int orientationValue = a.getInt(R.styleable.VScrollViewGroup_orientation, 0);
            if (orientationValue == 0) {
                orientation = Orientation.horizontal;
            } else if (orientationValue == 1) {
                orientation = Orientation.vertical;
            }
            a.recycle();
        }

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setClickable(true);
        viewGroups = new ViewGroup[3];
        currentViewGroup = viewGroups[0] = new FrameLayout(context);
        lastViewGroup = viewGroups[1] = new FrameLayout(context);
        nextViewGroup = viewGroups[2] = new FrameLayout(context);

        for (ViewGroup viewGroup : viewGroups) {
            addView(viewGroup, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }
    }

    private OnItemClickListen onItemClickListen;
    private OnMyPageChangeListener onMyPageChangeListener;

    public void setOnItemClickListen(OnItemClickListen onItemClickListen) {
        this.onItemClickListen = onItemClickListen;
    }

    public void setOnMyPageChangeListener(OnMyPageChangeListener onMyPageChangeListener) {
        this.onMyPageChangeListener = onMyPageChangeListener;
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public boolean isMoving() {
        if (orientation == Orientation.horizontal) {
            return moveX != 0;
        } else if (orientation == Orientation.vertical) {
            return moveY != 0;
        }
        return false;
    }

    public void recycler() {
        if (viewGroups != null) {
            for (ViewGroup viewGroup : viewGroups) {
                if (viewGroup != null && viewGroup.getChildCount() > 0) {
                    viewGroup.removeAllViews();
                }
            }
        }
        viewGroups = null;
        myScrollViewGroupAdapter = null;
        removeAllViews();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (myScrollViewGroupAdapter == null || myScrollViewGroupAdapter.getCount() == 0) {
            currentViewGroup.layout(0, 0, getWidth(), getHeight());
            lastViewGroup.layout(0, 0, 0, 0);
            nextViewGroup.layout(0, 0, 0, 0);
            return;
        }
        if (orientation == Orientation.horizontal) {
            int left = -moveX, right = getWidth() - moveX, top = 0, bottom = getHeight();
            currentViewGroup.layout(left, top, right, bottom);

            if (moveX > 0) {
                left = getWidth() - moveX;
                right = getWidth() * 2 - moveX;
                nextViewGroup.layout(left, top, right, bottom);
                lastViewGroup.layout(0, 0, 0, 0);
            } else if (moveX < 0) {
                //从左到右滑动
                left = -moveX - getWidth();
                right = -moveX;
                lastViewGroup.layout(left, top, right, bottom);
                nextViewGroup.layout(0, 0, 0, 0);
            } else {
                lastViewGroup.layout(0, 0, 0, 0);
                nextViewGroup.layout(0, 0, 0, 0);
            }
        } else if (orientation == Orientation.vertical) {
            int left = 0, right = getWidth(), top = -moveY, bottom = getHeight() - moveY;
            currentViewGroup.layout(left, top, right, bottom);

            UIHelper.showLog("onLayout top:" + top);
            if (moveY > 0) {
                top = getHeight() - moveY;
                bottom = getHeight() * 2 - moveY;
                nextViewGroup.layout(left, top, right, bottom);
                lastViewGroup.layout(0, 0, 0, 0);
            } else if (moveY < 0) {
                top = -moveY - getHeight();
                bottom = -moveY;
                lastViewGroup.layout(left, top, right, bottom);
                nextViewGroup.layout(0, 0, 0, 0);
            } else {
                lastViewGroup.layout(0, 0, 0, 0);
                nextViewGroup.layout(0, 0, 0, 0);
            }
        }
    }

    public void setAdapter(BaseScrollViewGroupAdapter myScrollViewGroupAdapter) {
        setAdapter(myScrollViewGroupAdapter, 0);
    }

    public void setAdapter(BaseScrollViewGroupAdapter myScrollViewGroupAdapter, int currentPosition) {
        if (this.myScrollViewGroupAdapter == null || this.myScrollViewGroupAdapter != myScrollViewGroupAdapter) {
            this.myScrollViewGroupAdapter = myScrollViewGroupAdapter;
        }
        if (myScrollViewGroupAdapter == null || myScrollViewGroupAdapter.getCount() == 0) {
            return;
        }
        moveX = 0;
        moveY = 0;
        mCurrentPosition = currentPosition;
        if (myScrollViewGroupAdapter.getCount() == 1) {
            mCurrentPosition = 0;
            myScrollViewGroupAdapter.prepare(currentViewGroup, mCurrentPosition);
            onItemShow(false,false);
        } else {
            if (mCurrentPosition < 0) {
                mCurrentPosition = 0;
            }
            if (mCurrentPosition > myScrollViewGroupAdapter.getCount() - 1) {
                mCurrentPosition = myScrollViewGroupAdapter.getCount() - 1;
            }
            myScrollViewGroupAdapter.prepare(currentViewGroup, mCurrentPosition);
            onItemShow(true, true);
        }
    }

    private void onItemShow(boolean isShouldPrepareLast, boolean isShouldPrepareNext) {
        if (myScrollViewGroupAdapter == null) return;
        if(isShouldPrepareLast)prepareLast();
        if(isShouldPrepareNext)prepareNext();
        myScrollViewGroupAdapter.show(currentViewGroup, mCurrentPosition);
        requestLayout();
        if (onMyPageChangeListener != null) {
            onMyPageChangeListener.onPageSelected(mCurrentPosition);
        }
    }

    private void prepareLast() {
        if (myScrollViewGroupAdapter == null) return;
        int index = mCurrentPosition - 1;
        if (index < 0) {
            index = myScrollViewGroupAdapter.getCount() - 1;
        }
        myScrollViewGroupAdapter.prepare(lastViewGroup, index);
    }

    private void prepareNext() {
        if (myScrollViewGroupAdapter == null) return;
        int index = mCurrentPosition + 1;
        if (index > myScrollViewGroupAdapter.getCount() - 1) {
            index = 0;
        }
        myScrollViewGroupAdapter.prepare(nextViewGroup, index);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (myScrollViewGroupAdapter == null) return;
        myScrollViewGroupAdapter.doRelease();
        myScrollViewGroupAdapter = null;
    }

    private enum AnimType {
        toRestore, toNext, toLast
    }

    /**
     * @param animType 归位，next,last
     */
    private void startAnim(int fromValue, final int toValue, final AnimType animType) {
        UIHelper.showLog("startAnim fromValue:" + fromValue + ",toValue:" + toValue);
        if (fromValue == toValue) return;
        int needTime = Math.max(100, Math.min(400, Math.abs(toValue - fromValue)/10));
//        needTime = Math.abs(toValue - fromValue)/10;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.cancel();
        }
        valueAnimator = ValueAnimator.ofInt(fromValue, toValue);
        UIHelper.showLog("startAnim needTime:" + needTime);
        valueAnimator.setDuration(needTime);
//        AccelerateDecelerateInterpolator 在动画开始与结束的地方速率改变比较慢，在中间的时候加速
//        AccelerateInterpolator  在动画开始的地方速率改变比较慢，然后开始加速
//        AnticipateInterpolator 开始的时候向后然后向前甩
//        AnticipateOvershootInterpolator 开始的时候向后然后向前甩一定值后返回最后的值
//        BounceInterpolator   动画结束的时候弹起
//        CycleInterpolator 动画循环播放特定的次数，速率改变沿着正弦曲线
//        DecelerateInterpolator 在动画开始的地方快然后慢
//        LinearInterpolator   以常量速率改变
//        OvershootInterpolator    向前甩一定值后再回到原来位置
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                if (isTouching) {
                    valueAnimator.cancel();
                } else {
                    if (orientation == Orientation.horizontal) {
                        // 不断重新计算上下左右位置
                        moveX = (int) animator.getAnimatedValue();
                        // 重绘
                        requestLayout();
                        if (moveX == toValue) {
                            onAnimEnd(animType);
                        }
                    } else if (orientation == Orientation.vertical) {
                        // 不断重新计算上下左右位置
                        moveY = (int) animator.getAnimatedValue();
                        // 重绘
                        requestLayout();
                        if (moveY == toValue) {
                            onAnimEnd(animType);
                        }
                    }
                }
            }
        });
        valueAnimator.start();
    }

    /**
     * @param animType 归位，next,last
     */
    private void onAnimEnd(AnimType animType) {
        if (myScrollViewGroupAdapter == null) return;
        if (animType == AnimType.toNext) {
            myScrollViewGroupAdapter.hide(mCurrentPosition);
            mCurrentPosition++;
            if (mCurrentPosition > myScrollViewGroupAdapter.getCount() - 1) {
                mCurrentPosition = 0;
            }
            ViewGroup linShi = currentViewGroup;
            currentViewGroup = nextViewGroup;
            nextViewGroup = lastViewGroup;
            lastViewGroup = linShi;
            moveX = 0;
            moveY = 0;
            onItemShow(true, true);
        } else if (animType == AnimType.toLast) {
            myScrollViewGroupAdapter.hide(mCurrentPosition);
            mCurrentPosition--;
            if (mCurrentPosition < 0) {
                mCurrentPosition = myScrollViewGroupAdapter.getCount() - 1;
            }
            ViewGroup linShi = currentViewGroup;
            currentViewGroup = lastViewGroup;
            lastViewGroup = nextViewGroup;
            nextViewGroup = linShi;
            moveX = 0;
            moveY = 0;
            onItemShow(true, true);
        }
    }

    public void setOnTouchListen(OnTouchListen onTouchListen) {
        this.onTouchListen = onTouchListen;
    }

    private boolean isTouchMoveEnable;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        isTouchMoveEnable = false;
        if (myScrollViewGroupAdapter == null || myScrollViewGroupAdapter.getCount() <= 1 || orientation == null)
            return false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = tempX = (int) ev.getRawX();
                downY = tempY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float currentX = ev.getRawX();
                float currentY = ev.getRawY();
                // 满足此条件屏蔽子类的touch事件
                if (orientation == Orientation.horizontal) {
                    if (Math.abs(currentX - downX) > mTouchSlop && Math.abs(currentY - downY) <= Math.abs(currentX - downX)) {
                        UIHelper.showLog("VScrollViewGroup", "水平拦截");
                        isTouchMoveEnable = true;
                        return true;
                    }
                } else if (orientation == Orientation.vertical) {
                    if (Math.abs(currentY - downY) > mTouchSlop && Math.abs(currentX - downX) <= Math.abs(currentY - downY)) {
                        UIHelper.showLog("VScrollViewGroup", "垂直拦截");
                        isTouchMoveEnable = true;
                        return true;
                    }
                }
                break;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isTouchMoveEnable) return false;
        if (myScrollViewGroupAdapter == null || myScrollViewGroupAdapter.getCount() <= 1) {
            return false;
        }
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (onTouchListen != null) onTouchListen.onTouch(true);
                isTouching = true;
                break;
            case MotionEvent.ACTION_MOVE:
                onEventMove(event);
                break;
            case MotionEvent.ACTION_UP:
                onEventUp(event);
                break;
            case MotionEvent.ACTION_CANCEL:
                onEventUp(event);
                break;
        }
        return true;
    }

    private void onEventMove(MotionEvent event) {
        if (myScrollViewGroupAdapter.getCount() <= 1) {
            return;
        }
        if (valueAnimator != null && valueAnimator.isRunning()) {
            return;
//            valueAnimator.cancel();
        }
        float currentX = event.getRawX();
        float currentY = event.getRawY();
        int deltaX = (int) (tempX - currentX);
        int deltaY = (int) (tempY - currentY);
        tempX = currentX;
        tempY = currentY;
        moveX += deltaX;
        moveY += deltaY;

        if (moveX < -getWidth()) {
            moveX = -getWidth();
        }
        if (moveX > getWidth()) {
            moveX = getWidth();
        }
        if (moveY < -getHeight()) {
            moveY = -getHeight();
        }
        if (moveY > getHeight()) {
            moveY = getHeight();
        }
        requestLayout();
    }

    private void onEventUp(MotionEvent event) {
        if (onTouchListen != null) onTouchListen.onTouch(false);
        if (Math.abs(event.getRawX() - tempX) < mTouchSlop && Math.abs(event.getRawY() - tempY) < mTouchSlop) {
            if (onItemClickListen != null) {
                onItemClickListen.click(mCurrentPosition);
            }
        }
        isTouching = false;
        if (orientation == Orientation.horizontal) {
            if (moveX != 0) {
                mVelocityTracker.computeCurrentVelocity(1000);
                float velocity = mVelocityTracker.getXVelocity(0);
                UIHelper.showLog(this,"velocity:"+velocity);
                if (Math.abs(moveX) >= getWidth() / 2 || Math.abs(velocity) > 1000) {
                    if (moveX > 0) {
                        startAnim(moveX, getWidth(), AnimType.toNext);
                    } else {
                        startAnim(moveX, -getWidth(), AnimType.toLast);
                    }
                } else {
                    startAnim(moveX, 0, AnimType.toRestore);
                }
            }
        } else if (orientation == Orientation.vertical) {
            if (moveY != 0) {
                mVelocityTracker.computeCurrentVelocity(1000);
                float velocity = mVelocityTracker.getYVelocity(0);
                UIHelper.showLog(this,"velocity:"+velocity);
                if (Math.abs(moveY) >= getHeight() / 2 || Math.abs(velocity) > 1000) {
                    if (moveY > 0) {
                        startAnim(moveY, getHeight(), AnimType.toNext);
                    } else {
                        startAnim(moveY, -getHeight(), AnimType.toLast);
                    }
                } else {
                    startAnim(moveY, 0, AnimType.toRestore);
                }
            }
        }
    }
}
