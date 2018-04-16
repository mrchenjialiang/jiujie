package com.jiujie.jiujie.autocompletetextview;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.jiujie.jiujie.ui.activity.MyBaseActivity;
import com.jiujie.jiujie.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ChenJiaLiang on 2018/3/23.
 * Email:576507648@qq.com
 */

public class AutoCompleteTextViewActivity extends MyBaseActivity {

    @Bind(R.id.ts_ed_text)
    AutoCompleteTextView edText;
    @Bind(R.id.a_layout)
    View edLayout;

    @Override
    public void initUI() {
        ButterKnife.bind(this);

        edText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setDownList();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setDownList() {

        List<String> dataList = new ArrayList<>();
        for (int i = 0;i<20;i++){
            dataList.add("1"+i);
        }

//        SearchHelpPop searchHelpPop = new SearchHelpPop(mActivity, UIHelper.getScreenWidth(mActivity),500);
//        searchHelpPop.setData(dataList);
//        searchHelpPop.showAsDropDown(edLayout);


//        edText.get
        edText.setAdapter(new SearchAutoTextAdapter(mActivity,dataList));
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_autocompletetextview;
    }
}
