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
    private LinearLayout list;
    private View scrollView;
    private OnItemClickListen onItemClickListen;

    public BottomListDialog(Activity activity) {
        super(activity);
        this.mActivity = activity;
    }

    @Override
    protected void initUI(View layout) {
        list = layout.findViewById(R.id.dbl_list);
        scrollView = layout.findViewById(R.id.dbl_scrollView);
        View cancelTopView = layout.findViewById(R.id.dbl_cancel_top_view);
        layout.findViewById(R.id.dbl_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setOrRefreshDataList(dataList);

        ViewGroup.LayoutParams lp = cancelTopView.getLayoutParams();
        lp.height = getCancelMarginTop();
        cancelTopView.setLayoutParams(lp);
    }

    public int getCancelMarginTop(){
        return UIHelper.dip2px(mActivity,12);
    }

    public void setOrRefreshDataList(List<String> dataList) {
        list.removeAllViews();
        if(dataList.size()==1){
            scrollView.setPadding(0,0,0,0);
            View itemView = getListItemView(dataList.get(0), ItemType.Single, 0);
            list.addView(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if (onItemClickListen != null) {
                        onItemClickListen.click(0);
                    }
                }
            });
        }else{

            boolean isShouldSetPadding = false;
            int height = UIHelper.dip2px(getContext(), 50)*dataList.size();
            int maxHeight = UIHelper.getScreenHeight(mActivity)*3/5;
            if(height>maxHeight){
                isShouldSetPadding = true;
                int dp4 = UIHelper.dip2px(getContext(), 4);
                scrollView.setPadding(0,dp4,0,dp4);

                ViewGroup.LayoutParams lp = scrollView.getLayoutParams();
                lp.height = maxHeight;
                scrollView.setLayoutParams(lp);
            }else{
                scrollView.setPadding(0,0,0,0);
            }
            for (int i = 0;i<dataList.size();i++){
                View itemView;
                if(isShouldSetPadding){
                    itemView = getListItemView(dataList.get(i), ItemType.Center, i);
                    list.addView(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    list.addView(getLine(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }else{
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
            throw new NullPointerException("BottomListDialog create imageList should not be null or size==0");
        }
        this.dataList = dataList;
        create(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM, R.style.BottomAlertAni);
    }

    public void create(String[] dataList){
        if(dataList==null||dataList.length==0){
            throw new NullPointerException("BottomListDialog create imageList should not be null or size==0");
        }
        this.dataList = Arrays.asList(dataList);
        create(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM, R.style.BottomAlertAni);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_bottom_list;
    }
}
