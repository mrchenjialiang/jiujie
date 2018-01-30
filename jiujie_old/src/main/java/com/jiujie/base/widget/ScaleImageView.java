package com.jiujie.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.jiujie.base.R;

public class ScaleImageView extends ImageView {

    private float scaleHeight,scaleWidth;

    public ScaleImageView(Context context) {
        super(context);
    }

    public ScaleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ScaleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.ScaleImageView);

            scaleHeight = a.getFloat(R.styleable.ScaleImageView_scaleHeight, 0);
            scaleWidth = a.getFloat(R.styleable.ScaleImageView_scaleWidth, 0);

            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        super.onMeasure((int)(sizeWidth*scaleWidth), (int)(sizeWidth*scaleHeight));
    }
}
