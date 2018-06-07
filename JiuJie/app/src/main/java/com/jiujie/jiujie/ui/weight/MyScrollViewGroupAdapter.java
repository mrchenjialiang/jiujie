package com.jiujie.jiujie.ui.weight;

import android.view.ViewGroup;

/**
 * Created by ChenJiaLiang on 2018/6/6.
 * Email:576507648@qq.com
 */

public abstract class MyScrollViewGroupAdapter<T> {

    public abstract int getCount();

    public abstract void init(ViewGroup viewGroup, int position);
    public abstract void show(ViewGroup viewGroup, int position);
}
