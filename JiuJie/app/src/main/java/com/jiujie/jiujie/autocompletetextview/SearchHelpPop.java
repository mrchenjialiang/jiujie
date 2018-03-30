package com.jiujie.jiujie.autocompletetextview;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import com.jiujie.base.pop.BasePop;
import com.jiujie.jiujie.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ChenJiaLiang on 2018/3/23.
 * Email:576507648@qq.com
 */

public class SearchHelpPop extends BasePop {

    @Bind(R.id.listView)
    ListView listView;
    private List<String> dataList = new ArrayList<>();
    private SearchAutoTextAdapter mAdapter;

    public SearchHelpPop(Activity activity, int width, int height) {
        super(activity, width, height);
    }

    @Override
    protected void initUI(View view) {
        ButterKnife.bind(this,view);

        mAdapter = new SearchAutoTextAdapter(getActivity(), dataList);
        listView.setAdapter(mAdapter);

//        setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
//        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    public void setData(List<String> list){
        dataList.clear();
        if(list!=null){
            dataList.addAll(list);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public View getLayout(Context context) {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.listview;
    }
}
