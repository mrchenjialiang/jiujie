package com.jiujie.base.activity;

import com.jiujie.base.jk.ICallback;
import com.jiujie.base.util.UIHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenJiaLiang on 2017/6/12.
 * Email:576507648@qq.com
 */

public abstract class BaseListSimpleActivity<T,V> extends BaseListActivity{

    protected List<V> dataList = new ArrayList<>();
    protected class MyCallback implements ICallback<T>{
        private final int type;
        public MyCallback(int type) {
            this.type = type;
            setLoadDataStart(type);
//            setCanReadCache(type==0);
        }

        @Override
        public void onSucceed(T result) {
            setLoadDataEnd(type,result);
        }

        @Override
        public void onFail(String error) {
            setLoadDataFail(type,error);
        }
    }

    /**
     * @param type 0:first,1:refresh,2:loadNextPage
     */
    protected void setLoadDataStart(int type){
        recyclerViewUtil.isLoadingData(true);
        if(type==0){
            setLoading();
            setEnd(false);
        }else if(type==1){
            recyclerViewUtil.setRefreshing(true);
            setEnd(false);
        }else if(type==2){
            recyclerViewUtil.setReadMore();
        }
    }

    protected void setLoadDataEnd(int type, T result) {
        recyclerViewUtil.isLoadingData(false);
        if(type==0||type==1){
            dataList.clear();
        }
        List<V> list = analysisData(result);
        boolean isEnd = list==null||(isEndFromSize()?list.size()<size:list.size()<1);
        boolean isHasData = list != null && list.size() > 0;
        if(isHasData){
            if(isAddDataWithQuChong()){
                for (V d:list){
                    if(!dataList.contains(d)){
                        dataList.add(d);
                    }
                }
            }else{
                dataList.addAll(list);
            }
        }
        setEnd(isEnd);
        setLoadDataUIEnd(type);
    }

    protected boolean isAddDataWithQuChong(){
        return false;
    }

    protected void setLoadDataUIEnd(int type) {
        if(type==0){
            setLoadingEnd();
        }else if(type==1){
            recyclerViewUtil.setRefreshing(false);
        }
        if(isEnd()){
            recyclerViewUtil.setReadEnd();
        }
        recyclerViewUtil.notifyDataSetChanged(type==0||type==1);
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
            UIHelper.showToastShort(mActivity,error);
        }else if(type==2){
            page--;
            UIHelper.showToastShort(mActivity,error);
        }
    }

    protected abstract List<V> analysisData(T result);
}
