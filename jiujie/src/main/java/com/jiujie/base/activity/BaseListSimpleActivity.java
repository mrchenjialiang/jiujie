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
        }

        @Override
        public void onSucceed(T result) {
            if(type==0||type==1){
                dataList.clear();
            }
            List<V> list = analysisData(result);
            if(list!=null){
                dataList.addAll(list);
                isEnd = list.size()<size;
            }else{
                isEnd = true;
            }
            setLoadDataEnd(type);
        }

        @Override
        public void onFail(String error) {
            if(type==0){
                setLoadingFail();
            }
            if(type==2){
                page--;
            }
            UIHelper.showToastShort(mActivity,error);
        }
    }

    protected abstract List<V> analysisData(T result);
}
