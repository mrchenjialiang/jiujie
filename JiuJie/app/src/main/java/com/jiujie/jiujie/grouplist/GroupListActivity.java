package com.jiujie.jiujie.grouplist;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.jiujie.base.activity.BaseActivity;
import com.jiujie.base.widget.ExpandableListViewOneHeaderKeepTop;
import com.jiujie.jiujie.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GroupListActivity extends BaseActivity {

    @Bind(R.id.gh_ExpandableListView)
    ExpandableListViewOneHeaderKeepTop expandableListView;
    private GroupListAdapter adapter;
    private Map<Integer,List<String>> childDataMap = new HashMap<>();
    private List<String> groupList = new ArrayList<>();

    public void initUI() {
        ButterKnife.bind(this);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;//返回true,表示不可点击
            }
        });
    }

    @Override
    public void initData() {
        for (int i=0;i<10;i++){
            groupList.add("group"+i);
        }
        for (int i = 0;i<groupList.size();i++){
            List<String> childList = new ArrayList<>();
            for (int j=0;j<10;j++){
                childList.add("child"+i);
            }
            childDataMap.put(i,childList);
        }
        setData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_group_list;
    }



    private void setData(){
        if(adapter ==null){
            adapter = new GroupListAdapter(mActivity, childDataMap,groupList);
            expandableListView.setAdapter(adapter);
            for(int i = 0; i < adapter.getGroupCount(); i++){
                expandableListView.expandGroup(i);
            }
        }else{
            adapter.notifyDataSetChanged();
        }
    }
}
