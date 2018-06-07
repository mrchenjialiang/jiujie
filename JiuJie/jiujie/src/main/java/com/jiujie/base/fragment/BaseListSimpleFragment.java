package com.jiujie.base.fragment;

import com.jiujie.base.jk.ICallback;
import com.jiujie.base.util.UIHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * D--接口直接获取数据类型 Data
 * ID--Item使用数据类型 ItemData
 * Created by ChenJiaLiang on 2017/6/12.
 * Email:576507648@qq.com
 */
public abstract class BaseListSimpleFragment<D,ID> extends BaseListFragment{

    protected class MyCallback implements ICallback<D>{
        private final int type;
        public MyCallback(int type) {
            this.type = type;
            setLoadDataStart(type);
        }

        @Override
        public void onSucceed(D result) {
            setLoadDataEnd(type,result);
        }

        @Override
        public void onFail(String error) {
            setLoadDataFail(type,error);
        }
    }
    
    
    protected List<ID> dataList = new ArrayList<>();
    private boolean isDataChange;

    protected void setLoadDataEnd(int type, D result) {
        int oldSize = dataList.size();
        if (type == 0 || type == 1) {
            dataList.clear();
        }
        List<ID> list = analysisData(result);
        boolean isEnd = list == null || (isEndFromSize() ? list.size() < this.size : list.size() < 1);
        boolean isHasData = list != null && list.size() > 0;
        if (isHasData) {
            int firstAddIndex;
            int lastAddIndex;
            if(dataList.size()==0){
                firstAddIndex = 0;
            }else{
                firstAddIndex = dataList.size() - 1;
            }
            if (isAddDataWithQuChong()) {
                for (ID d : list) {
                    if (!dataList.contains(d)) {
                        dataList.add(d);
                    }
                }
            } else {
                dataList.addAll(list);
            }
            lastAddIndex = dataList.size() - 1;
            doAfterAddDataBeforeSetUI(firstAddIndex,lastAddIndex);
        }
        int newSize = dataList.size();
        isDataChange = type != 2 || oldSize != newSize;
        setEnd(isEnd);
        setLoadDataUIEnd(type);
    }

    protected void doAfterAddDataBeforeSetUI(int firstAddIndex,int lastAddIndex){

    }

    protected boolean isAddDataWithQuChong(){
        return false;
    }

    protected void setLoadDataUIEnd(int type) {
        recyclerViewUtil.isLoadingData(false);
        if(type==0){
            setLoadingEnd();
        }else if(type==1){
            recyclerViewUtil.setRefreshing(false);
        }
        if(isEnd()){
            recyclerViewUtil.setReadEnd();
        }
        if(type==0||type==1){
            recyclerViewUtil.notifyDataSetChanged(true);
        }else{
            if(isDataChange){
                recyclerViewUtil.notifyDataSetChanged(false);
            }
        }
    }

    @Override
    public void setLoadingFail() {
        super.setLoadingFail();
        recyclerViewUtil.isLoadingData(false);
    }

    protected void setLoadDataFail(int type, String error) {
        recyclerViewUtil.isLoadingData(false);
        if(type==0){
            setLoadingFail();
        }else if(type==1){
            recyclerViewUtil.setRefreshing(false);
        }else if(type==2){
            recyclerViewUtil.setReadFail();
        }
        if(type==1){
            UIHelper.showToastShort(error);
        }else if(type==2){
            page--;
            UIHelper.showToastShort(error);
        }
    }

    protected abstract List<ID> analysisData(D result);
}
