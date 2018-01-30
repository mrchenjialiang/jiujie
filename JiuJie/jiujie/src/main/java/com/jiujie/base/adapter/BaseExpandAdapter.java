package com.jiujie.base.adapter;

import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;


/**
 * author : Created by ChenJiaLiang on 2016/8/31.
 * Email : 576507648@qq.com
 */
public abstract class BaseExpandAdapter extends BaseExpandableListAdapter{

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public abstract void bindKeepTopHeaderView(ExpandableListView expandableListView,
                                               View header, int groupPosition,
                                               int childPosition, int alpha);

    public abstract View getKeepTopHeaderView(ExpandableListView expandableListView);
}