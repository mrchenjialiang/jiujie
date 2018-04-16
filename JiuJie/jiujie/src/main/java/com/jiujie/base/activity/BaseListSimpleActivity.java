package com.jiujie.base.activity;

import com.jiujie.base.jk.ICallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenJiaLiang on 2017/6/12.
 * Email:576507648@qq.com
 */

public abstract class BaseListSimpleActivity<T, V> extends BaseListActivity {

    protected List<V> dataList = new ArrayList<>();

    protected class MyCallback implements ICallback<T> {
        private final int type;

        public MyCallback(int type) {
            this.type = type;
            setLoadDataStart(type);
//            setCanReadCache(type==0);
        }

        @Override
        public void onSucceed(T result) {
            setLoadDataEnd(type, result);
        }

        @Override
        public void onFail(String error) {
            setLoadDataFail(type, error);
        }
    }

    protected void setLoadDataEnd(int type, T result) {
        if (type == 0 || type == 1) {
            dataList.clear();
        }
        List<V> list = analysisData(result);
        boolean isEnd = list == null || (isEndFromSize() ? list.size() < size : list.size() < 1);
        boolean isHasData = list != null && list.size() > 0;
        if (isHasData) {
            if (isAddDataWithQuChong()) {
                for (V d : list) {
                    if (!dataList.contains(d)) {
                        dataList.add(d);
                    }
                }
            } else {
                dataList.addAll(list);
            }
        }
        setEnd(isEnd);
        setLoadDataUIEnd(type);
    }

    protected boolean isAddDataWithQuChong() {
        return false;
    }

    protected abstract List<V> analysisData(T result);
}
