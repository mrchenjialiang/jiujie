package com.jiujie.base.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jiujie.base.R;
import com.jiujie.base.jk.OnItemClickListen;
import com.jiujie.base.util.UIHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CenterListDialog extends BaseDialog{

    private LinearLayout listLine;
    private OnItemClickListen onItemClickListen;
    private List<String> dataList = new ArrayList<>();
    private Activity activity;

    public CenterListDialog(Activity context) {
        super(context);
        activity = context;
    }
    public void create(String[]data,OnItemClickListen onItemClickListen){
        this.onItemClickListen = onItemClickListen;
        dataList = Arrays.asList(data);
        create(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER, 0);
    }
    public void create(List<String> dataList,OnItemClickListen onItemClickListen){
        this.onItemClickListen = onItemClickListen;
        this.dataList = dataList;
        create(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER, 0);
    }

    @Override
    protected void initUI(View layout) {
        ScrollView scrollView = (ScrollView) layout.findViewById(R.id.dcl_scrollView);
        listLine = (LinearLayout) layout.findViewById(R.id.dcl_list_line);

        listLine.removeAllViews();
        for (int i=0;i<dataList.size();i++){
            listLine.addView(getItemView(i));
        }

        if(dataList.size()* UIHelper.dip2px(getContext(),45)>UIHelper.getScreenHeight(activity)*3/5){
            ViewGroup.LayoutParams lp = scrollView.getLayoutParams();
            lp.height = UIHelper.getScreenWidth(activity)*3/5;
            scrollView.setLayoutParams(lp);
        }
    }

    private View getItemView(final int position) {
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_center_list_item, listLine, false);
        TextView text = (TextView) itemView.findViewById(R.id.dcli_text);
        View line = itemView.findViewById(R.id.dcli_line);
        line.setVisibility(position>=dataList.size()-1?View.GONE:View.VISIBLE);
        text.setText(dataList.get(position));
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListen!=null)onItemClickListen.click(position);
                dismiss();
            }
        });
        return itemView;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_center_list;
    }
}
