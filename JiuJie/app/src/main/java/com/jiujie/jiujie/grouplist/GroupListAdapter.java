package com.jiujie.jiujie.grouplist;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.jiujie.base.adapter.BaseExpandAdapter;
import com.jiujie.base.util.UIHelper;
import com.jiujie.jiujie.R;

import java.util.List;
import java.util.Map;

public class GroupListAdapter extends BaseExpandAdapter {

    private final Activity activity;
    private final LayoutInflater mInflater;
    private final List<String> groupList;
    private final Map<Integer, List<String>> childMap;

    public GroupListAdapter(Activity activity, Map<Integer, List<String>> dataList, List<String> groupList){
        this.activity = activity;
        this.childMap = dataList;
        this.groupList = groupList;
        mInflater = LayoutInflater.from(activity);
    }

    @Override
    public void bindKeepTopHeaderView(ExpandableListView expandableListView, View header, int groupPosition, int childPosition, int alpha) {
        TextView textView = header.findViewById(R.id.ig_text);
        textView.setText(groupList.get(groupPosition));
    }

    @Override
    public View getKeepTopHeaderView(ExpandableListView expandableListView) {
        return mInflater.inflate(R.layout.item_group, null);
    }

    @Override
    public int getGroupCount() {
        return groupList==null?0:groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(childMap==null||childMap.size()==0){
            return 0;
        }else{
            List<String> childDataList = childMap.get(groupPosition);
            return childDataList==null?0:childDataList.size();
        }
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        GroupViewHolder h;
        if(view==null){
            view = mInflater.inflate(R.layout.item_group, null);
            h = getGroupViewHolder(view);
            view.setTag(h);
        }else{
            h = (GroupViewHolder) view.getTag();
        }
        UIHelper.showLog("Group:"+groupPosition);
        h.text.setText(groupList.get(groupPosition));
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        ChildViewHolder h;
        if(view==null){
            view = mInflater.inflate(R.layout.item_group_child, null);
            h = getChildViewHolder(view);
            view.setTag(h);
        }else{
            h = (ChildViewHolder) view.getTag();
        }
        final String d = childMap.get(groupPosition).get(childPosition);
        UIHelper.showLog("Child groupPosition:"+groupPosition+",childPosition:"+childPosition);
        h.text.setText(d);
        return view;
    }

    private GroupViewHolder getGroupViewHolder(View header) {
        GroupViewHolder h = new GroupViewHolder();
        h.text = header.findViewById(R.id.ig_text);
        return h;
    }

    private ChildViewHolder getChildViewHolder(View view) {
        ChildViewHolder h = new ChildViewHolder();
        h.text = view.findViewById(R.id.igc_text);
        return h;
    }

    class GroupViewHolder{
        TextView text;
    }

    class ChildViewHolder{
        TextView text;
    }
}
