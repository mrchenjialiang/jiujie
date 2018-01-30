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
            if(type==0||type==1){
                dataList.clear();
            }
            List<V> list = analysisData(result);
            if(list!=null){
                dataList.addAll(list);
                if(isEndFromSize()){
                    setEnd(list.size()<size);
                }else{
                    setEnd(list.size()<1);
                }
            }else{
                setEnd(true);
            }
            setLoadDataEnd(type);
        }

        @Override
        public void onFail(String error) {
            if(type==0){
                if(dataList.size()==0){
                    setLoadingFail();
                }else{
                    setLoadingEnd();
                }
            }else if(type==2){
                page--;
                recyclerViewUtil.setReadFail();
                notifyDataSetChanged();
                setLoadDataEnd(type);
            }else if(type==1){
                recyclerViewUtil.setRefreshing(false);
                UIHelper.showToastShort(mActivity,error);
            }
        }
    }

    protected abstract List<V> analysisData(T result);
}
