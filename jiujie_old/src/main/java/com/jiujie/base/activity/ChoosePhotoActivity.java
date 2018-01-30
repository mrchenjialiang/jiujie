package com.jiujie.base.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiujie.base.R;
import com.jiujie.base.adapter.ChoosePhotoAdapter;
import com.jiujie.base.jk.ICallbackSimple;
import com.jiujie.base.jk.OnItemClickListen;
import com.jiujie.base.jk.OnListCheckedChangeListener;
import com.jiujie.base.model.Image;
import com.jiujie.base.pop.ChoosePhotoDirPop;
import com.jiujie.base.util.ImageUtil;
import com.jiujie.base.util.MyItemDecoration;
import com.jiujie.base.util.UIHelper;
import com.jiujie.base.widget.JJSimpleButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ChenJiaLiang on 2017/8/8.
 * Email:576507648@qq.com
 */

public class ChoosePhotoActivity extends BaseActivity{

    private static ICallbackSimple<List<String>> callbackSimple;
    private List<Image> mDataList = new ArrayList<>();
    private List<Image> mMainDataList = new ArrayList<>();
    private List<String> mCheckedList = new ArrayList<>();
    private ChoosePhotoAdapter mMainAdapter;
    private boolean isCountLimit;
    private int maxCheckCount;
    private JJSimpleButton mBtnEnsure;

    public static void launch(Activity activity, boolean isCountLimit, int maxCheckCount, List<String> checkedList, ICallbackSimple<List<String>> callbackSimple){
        ChoosePhotoActivity.callbackSimple = callbackSimple;
        Intent intent = new Intent(activity, ChoosePhotoActivity.class);
        intent.putExtra("isCountLimit",isCountLimit);
        intent.putExtra("maxCheckCount",maxCheckCount);
        if(checkedList!=null&&checkedList.size()>0){
            ArrayList<String> checkedArrayList = new ArrayList<>(checkedList);
            intent.putStringArrayListExtra("checkedList",checkedArrayList);
        }
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        initUI();
        initTitle();
        initData();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        maxCheckCount = intent.getIntExtra("maxCheckCount", 0);
        isCountLimit = intent.getBooleanExtra("isCountLimit", false);
        ArrayList<String> checkedList = intent.getStringArrayListExtra("checkedList");
        if(checkedList!=null){
            mCheckedList.addAll(checkedList);
        }
    }

    @Override
    public int getCustomTitleLayoutId() {
        return R.layout.title_choose_photo;
    }

    private void initTitle() {
        LinearLayout baseTitleLayout = getBaseTitleLayout();
        baseTitleLayout.findViewById(R.id.tcp_btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        baseTitleLayout.findViewById(R.id.tcp_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBtnEnsure = (JJSimpleButton) baseTitleLayout.findViewById(R.id.tcp_btn_ensure);
        mBtnEnsure.setSelected(mCheckedList.size()==0);
        if(maxCheckCount>0){
            mBtnEnsure.setText("完成("+mCheckedList.size()+"/"+maxCheckCount+")").refresh();
        }else{
            mBtnEnsure.setText("完成").refresh();
        }
        mBtnEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBtnEnsure.isSelected()){
                    return;
                }
                if(callbackSimple!=null){
                    callbackSimple.onSucceed(mCheckedList);
                }
                finish();
            }
        });
    }

    private void initUI() {
        RecyclerView mMainList = (RecyclerView) findViewById(R.id.cp_main_list);
        findViewById(R.id.cp_btn_show_dir).setOnClickListener(new View.OnClickListener() {
            ChoosePhotoDirPop choosePhotoDirPop;
            @Override
            public void onClick(View v) {
                if(choosePhotoDirPop==null){
                    choosePhotoDirPop = new ChoosePhotoDirPop(mActivity, mDataList,new OnItemClickListen() {
                        int checkPosition;
                        @Override
                        public void click(int position) {
                            if(checkPosition==position){
                                return;
                            }
                            checkPosition = position;
                            mMainDataList.clear();
                            mMainDataList.addAll(mDataList.get(position).getImageList());
                            mMainAdapter.notifyDataSetChanged();
                        }
                    });
                }
                choosePhotoDirPop.showOnAboutView(v);
            }
        });
        UIHelper.initRecyclerView(mActivity, mMainList,1,3);
        mMainList.addItemDecoration(new MyItemDecoration(UIHelper.dip2px(mActivity,2),3,false));
        mMainAdapter = new ChoosePhotoAdapter(mActivity,mMainDataList);
        TextView textView = new TextView(mActivity.getApplicationContext());
        textView.setHeight(UIHelper.dip2px(mActivity,50));
        mMainAdapter.addFooter(textView);
        mMainAdapter.setOnListCheckedChangeListener(new OnListCheckedChangeListener<String>() {
            @Override
            public void onCheckedChanged(List<String> checkList) {
                mCheckedList.clear();
                if(checkList!=null&&checkList.size()>0){
                    mCheckedList.addAll(checkList);
                }
                mBtnEnsure.setSelected(mCheckedList.size()==0);
                if(maxCheckCount>0){
                    mBtnEnsure.setText("完成("+mCheckedList.size()+"/"+maxCheckCount+")").refresh();
                }else{
                    mBtnEnsure.setText("完成").refresh();
                }
            }
        });
        mMainAdapter.setCountLimit(isCountLimit);
        mMainAdapter.setMaxCheckCount(maxCheckCount);
        mMainAdapter.setCheckedList(mCheckedList);
        mMainList.setAdapter(mMainAdapter);
    }

    @Override
    public void initData() {
        ImageUtil.instance().getAllImageFromLocal(this, new ICallbackSimple<Map<String, Image>>() {
            @Override
            public void onSucceed(Map<String, Image> result) {
                if(result!=null&&result.size()>0){
                    mDataList.clear();
                    for (String path:result.keySet()){
                        Image image = result.get(path);
                        if(image.isDir()&&image.getImageList()!=null&&image.getImageList().size()>0)mDataList.add(image);
                    }
                    if(mDataList.size()>0){
                        mMainDataList.addAll(mDataList.get(0).getImageList());
                        mMainAdapter.notifyDataSetChanged();
                        return;
                    }
                }
                UIHelper.showToastShort(mActivity,"手机里没有相关图片");
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_photo;
    }

    @Override
    protected String getPageName() {
        return "选择图片";
    }
}
