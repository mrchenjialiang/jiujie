package com.jiujie.base.dialog;

import android.app.Activity;
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
    private TextView mTextTv;
    private TextView mBtnLeft;
    private TextView mBtnRight;

    public EnsureDialog(Activity context) {
        super(context);
        this.activity = context;
    }

    @Override
    protected void initUI(View layout) {
        mTextTv = (TextView) layout.findViewById(R.id.de_text);
        mBtnLeft = (TextView) layout.findViewById(R.id.de_btn_left);
        mBtnRight = (TextView) layout.findViewById(R.id.de_btn_right);
    }

    public EnsureDialog create(){
        create(UIHelper.getScreenWidth(activity)*3/4, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER, 0);
        return this;
    }

    public EnsureDialog setText(String text){
        if(mTextTv==null)
            throw new NullPointerException("EnsureDialog please create() first");
        mTextTv.setText(text);
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
