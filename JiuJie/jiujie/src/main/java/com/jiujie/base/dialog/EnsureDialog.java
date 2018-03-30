package com.jiujie.base.dialog;

import android.app.Activity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiujie.base.R;
import com.jiujie.base.jk.OnBaseDialogClickListener;
import com.jiujie.base.util.UIHelper;

/**
 * @author : Created by ChenJiaLiang on 2016/8/24.
 * Email : 576507648@qq.com
 */
public class EnsureDialog extends BaseDialog {

    private final Activity activity;
    private TextView mTitleTv;
    private TextView mTextTv;
    private TextView mBtnLeft;
    private TextView mBtnRight;
    private View titleLayout;
    private ViewGroup mContentLayout;

    public EnsureDialog(Activity context) {
        super(context);
        this.activity = context;
    }

    @Override
    protected void initUI(View layout) {
        titleLayout = layout.findViewById(R.id.de_title_layout);
        mTitleTv = layout.findViewById(R.id.de_title);
        mTextTv = layout.findViewById(R.id.de_text);
        mBtnLeft = layout.findViewById(R.id.de_btn_left);
        mBtnRight = layout.findViewById(R.id.de_btn_right);
        mContentLayout = layout.findViewById(R.id.de_content_layout);

        titleLayout.setVisibility(View.GONE);
    }

    public EnsureDialog create(){
        create(UIHelper.getScreenWidth(activity)*3/4, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER, 0);
        return this;
    }

    public EnsureDialog setLeftTextColor(int color){
        if(mBtnLeft==null)
            throw new NullPointerException("EnsureDialog please create() first");
        mBtnLeft.setTextColor(color);
        return this;
    }

    public EnsureDialog setRightTextColor(int color){
        if(mBtnRight==null)
            throw new NullPointerException("EnsureDialog please create() first");
        mBtnRight.setTextColor(color);
        return this;
    }

    public EnsureDialog setContentLayout(View contentLayout){
        if(mContentLayout==null)
            throw new NullPointerException("EnsureDialog please create() first");
        mContentLayout.removeAllViews();
        mContentLayout.addView(contentLayout);
        return this;
    }

    public EnsureDialog setText(String text){
        if(mTextTv==null)
            throw new NullPointerException("EnsureDialog please create() first; or when used setContentLayout() should not use this method");
        mTextTv.setText(text);
        if(text!=null&&text.length()>20){
            mTextTv.setGravity(Gravity.START);
        }else{
            mTextTv.setGravity(Gravity.CENTER);
        }
        return this;
    }

    public EnsureDialog setTitle(String text){
        if(mTitleTv==null)
            throw new NullPointerException("EnsureDialog please create() first");
        mTitleTv.setText(text);

        titleLayout.setVisibility(TextUtils.isEmpty(text)?View.GONE:View.VISIBLE);
        return this;
    }

    public EnsureDialog setBtnLeft(String text, final OnBaseDialogClickListener onClickListener){
        if(mBtnLeft==null)
            throw new NullPointerException("EnsureDialog please create() first");
        mBtnLeft.setText(text);
        mBtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener!=null)onClickListener.onClick(EnsureDialog.this,v);
            }
        });
        return this;
    }

    public EnsureDialog setBtnRight(String text, final OnBaseDialogClickListener onClickListener){
        if(mBtnRight==null)
            throw new NullPointerException("EnsureDialog please create() first");
        mBtnRight.setText(text);
        mBtnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener!=null)onClickListener.onClick(EnsureDialog.this,v);
            }
        });
        return this;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_ensure;
    }
}
