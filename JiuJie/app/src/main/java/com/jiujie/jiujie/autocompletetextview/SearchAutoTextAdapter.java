package com.jiujie.jiujie.autocompletetextview;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.jiujie.jiujie.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by long on 2016/6/20.
 * 搜索词适配器
 */
public class SearchAutoTextAdapter extends BaseAdapter implements Filterable {

    private Activity mActivity;
    private List<String> dataList;
    private MyFilter myFilter;

    public SearchAutoTextAdapter(Activity mActivity, List<String> dataList) {
        this.mActivity = mActivity;
        this.dataList = dataList;
    }

    private void updateItems(List<String> dataList){
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList==null?0:dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder h;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_list, null);
            h = new ViewHolder(convertView);
            convertView.setTag(h);
        } else {
            h = (ViewHolder) convertView.getTag();
        }
        String text = dataList.get(position);
        h.mTvSearchWord.setText(text);
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (myFilter == null) {
            myFilter = new MyFilter(dataList);
        }
        return myFilter;
    }

    static class ViewHolder {
        @Bind(R.id.iml_text)
        TextView mTvSearchWord;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class MyFilter extends Filter {
        List<String> list;
        MyFilter(List<String> list) {
            this.list = list;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            results.values = list;
            results.count = list.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            updateItems((List<String>) results.values);
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
