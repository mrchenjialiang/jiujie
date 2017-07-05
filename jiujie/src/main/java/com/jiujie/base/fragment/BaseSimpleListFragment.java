package com.jiujie.base.fragment;

import com.jiujie.base.jk.ICallback;
import com.jiujie.base.util.UIHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenJiaLiang on 2017/6/12.
 * Email:576507648@qq.com
 */

public abstract class BaseSimpleListFragment<T,V> extends BaseListFragment{

    protected List<V> dataList = new ArrayList<>();
    class MyCallback implements ICallback<T>{
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
            setLoadDataEnd(type);

            if(type==2){
                page--;
            }
            UIHelper.showToastShort(mActivity,error);
        }
    }

    protected abstract List<V> analysisData(T result);
}
