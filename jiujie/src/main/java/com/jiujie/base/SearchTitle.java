package com.jiujie.base;

import android.app.Activity;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.jiujie.base.jk.InputAction;
import com.jiujie.base.util.UIHelper;

public class SearchTitle {

	private Activity activity;
	private EditText mSearchText;
	private View mTitleView;
	private View mLeftBtn;
	private View mSearchBtn;

	public SearchTitle(Activity activity) {
		this.activity = activity;
		initUI();
	}

	private void initUI() {
		mTitleView = activity.findViewById(R.id.ts_base_title);
		mLeftBtn = activity.findViewById(R.id.ts_title_back);
		mSearchBtn = activity.findViewById(R.id.ts_title_search);
		mSearchText = (EditText) activity.findViewById(R.id.ts_title_search_text);
	}

	public EditText getSearchText() {
		return mSearchText;
	}

	public void setTitleHide(){
		mTitleView.setVisibility(View.GONE);
	}
	public void setTitleShow(){
		mTitleView.setVisibility(View.VISIBLE);
	}
	
	public void setLeftButtonBack(){
		if(mLeftBtn!=null)mLeftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(activity!=null) activity.finish();
			}
		});
	}
	
	public void setSearchAction(final InputAction inputAction){
		if(mSearchBtn!=null)mSearchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(activity!=null) UIHelper.hidePan(activity);
				if(inputAction!=null) {
					Editable text = mSearchText.getText();
					if(TextUtils.isEmpty(text)){
						inputAction.send("");
					}else{
						inputAction.send(mSearchText.getText().toString());
					}
				}
			}
		});
		
		mSearchText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
    				if(activity!=null) UIHelper.hidePan(activity);
                	if(inputAction!=null) {
    					Editable text = mSearchText.getText();
    					if(TextUtils.isEmpty(text)){
    						inputAction.send("");
    					}else{
    						inputAction.send(mSearchText.getText().toString());
    					}
    				}
                }
                return false;
            }

        });
	}

	public void setHintText(String hint) {
		mSearchText.setHint(hint);
	}

}
