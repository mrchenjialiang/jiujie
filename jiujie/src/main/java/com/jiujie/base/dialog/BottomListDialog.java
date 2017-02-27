package com.jiujie.base.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiujie.base.R;
import com.jiujie.base.jk.OnItemClickListen;
import com.jiujie.base.util.UIHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author : Created by ChenJiaLiang on 2016/8/24.
 * Email : 576507648@qq.com
 */
public class BottomListDialog extends BaseDialog {

    private final Activity mActivity;
    private List<String> dataList;
    LinearLayout list;
    View scrollView;
    private OnItemClickListen onItemClickListen;

    public BottomListDialog(Activity activity) {
        super(activity);
        this.mActivity = activity;
    }

    @Override
    protected void initUI(View layout) {
        list = (LinearLayout) layout.findViewById(R.id.dbl_list);
        scrollView = layout.findViewById(R.id.dbl_scrollView);
        layout.findViewById(R.id.dbl_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        list.removeAllViews();
        if(dataList.size()==1){
            list.addView(getListItemView(dataList.get(0),ItemType.Single, 0), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }else{
            for (int i = 0;i<dataList.size();i++){
                View itemView;
                if(i==0){
                    itemView = getListItemView(dataList.get(i), ItemType.Top , i);
                    list.addView(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    list.addView(getLine(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }else if(i==dataList.size()-1){
                    itemView = getListItemView(dataList.get(i), ItemType.Bottom, i);
                    list.addView(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }else{
                    itemView = getListItemView(dataList.get(i), ItemType.Center, i);
                    list.addView(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    list.addView(getLine(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                final int finalI = i;
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        if(onItemClickListen!=null){
                            onItemClickListen.click(finalI);
                        }
                    }
                });
            }

            int height = UIHelper.dip2px(getContext(), 50)*dataList.size();
            int maxHeight = UIHelper.getScreenHeight(mActivity)*3/5;
            if(height>maxHeight){
                ViewGroup.LayoutParams lp = scrollView.getLayoutParams();
                lp.height = maxHeight;
                scrollView.setLayoutParams(lp);
            }
        }
    }

    public void setOnItemClickListen(OnItemClickListen onItemClickListen) {
        this.onItemClickListen = onItemClickListen;
    }
    private Map<Integer,Integer> positionColorMap;

    /**
     * 必须在create前设置
     */
    public void setItemTextColor(int position, int positionColor) {
        if(positionColorMap==null){
            positionColorMap = new HashMap<>();
        }
        positionColorMap.put(position, positionColor);
    }

    private enum ItemType{
        Top,Bottom,Center,Single
    }

    private View getLine(){
        TextView textView = new TextView(getContext());
        textView.setHeight(1);
        textView.setBackgroundColor(Color.parseColor("#c1c1c1"));
        return textView;
    }

    private View getListItemView(String text, ItemType type, int position){
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(20);
        if(positionColorMap!=null&&positionColorMap.containsKey(position)){
            textView.setTextColor(positionColorMap.get(position));
        }else{
            textView.setTextColor(Color.parseColor("#2b84fd"));
        }
        textView.setHeight(UIHelper.dip2px(getContext(), 50));
        switch (type){
            case Top:
                textView.setBackgroundResource(R.drawable.selector_bottom_dialog_item_top);
                break;
            case Bottom:
                textView.setBackgroundResource(R.drawable.selector_bottom_dialog_item_bottom);
                break;
            case Center:
                textView.setBackgroundResource(R.drawable.selector_bottom_dialog_item_center);
                break;
            case Single:
                textView.setBackgroundResource(R.drawable.selector_bottom_dialog_item_single);
                break;
        }
        return textView;
    }

    public void create(List<String> dataList){
        if(dataList==null||dataList.size()==0){
            throw new NullPointerException("BottomListDialog create dataList should not be null or size==0");
        }
        this.dataList = dataList;
        create(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM, R.style.BottomAlertAni);
    }

    public void create(String[] dataList){
        if(dataList==null||dataList.length==0){
            throw new NullPointerException("BottomListDialog create dataList should not be null or size==0");
        }
        this.dataList = Arrays.asList(dataList);
        create(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM, R.style.BottomAlertAni);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_bottom_list;
    }
}
