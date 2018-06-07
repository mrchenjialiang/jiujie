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
public class MessageDialog extends BaseDialog {

    private final Activity activity;
    private TextView mTitleTv;
    private TextView mTextTv;
    private TextView mBtnEnsure;
    private View titleLayout;
    private ViewGroup mContentLayout;

    public MessageDialog(Activity context) {
        super(context);
        this.activity = context;
    }

    @Override
    protected void initUI(View layout) {
        titleLayout = layout.findViewById(R.id.dm_title_layout);
        mTitleTv = layout.findViewById(R.id.dm_title);
        mTextTv = layout.findViewById(R.id.dm_text);
        mBtnEnsure = layout.findViewById(R.id.dm_btn_ensure);
        mContentLayout = layout.findViewById(R.id.dm_content_layout);

        titleLayout.setVisibility(View.GONE);
    }

    public MessageDialog create(){
        create(UIHelper.getScreenWidth(activity)*3/4, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER, 0);
        return this;
    }

    public MessageDialog setBtnTextColor(int color){
        if(mBtnEnsure==null)
            throw new NullPointerException("MessageDialog please create() first");
        mBtnEnsure.setTextColor(color);
        return this;
    }

    public MessageDialog setContentLayout(View contentLayout){
        if(mContentLayout==null)
            throw new NullPointerException("MessageDialog please create() first");
        mContentLayout.removeAllViews();
        mContentLayout.addView(contentLayout);
        return this;
    }

    public MessageDialog setText(String text){
        if(mTextTv==null)
            throw new NullPointerException("MessageDialog please create() first; or when used setContentLayout() should not use this method");
        mTextTv.setText(text);
        if(text!=null&&text.length()>20){
            mTextTv.setGravity(Gravity.START);
        }else{
            mTextTv.setGravity(Gravity.CENTER);
        }
        return this;
    }

    public MessageDialog setTitle(String text){
        if(mTitleTv==null)
            throw new NullPointerException("MessageDialog please create() first");
        mTitleTv.setText(text);

        titleLayout.setVisibility(TextUtils.isEmpty(text)?View.GONE:View.VISIBLE);
        return this;
    }

    public MessageDialog setBtnEnsure(String text, final OnBaseDialogClickListener onClickListener){
        if(mBtnEnsure==null)
            throw new NullPointerException("MessageDialog please create() first");
        mBtnEnsure.setText(text);
        mBtnEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener!=null)onClickListener.onClick(MessageDialog.this,v);
            }
        });
        return this;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_message;
    }
}
