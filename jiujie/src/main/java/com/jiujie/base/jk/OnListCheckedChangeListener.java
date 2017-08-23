package com.jiujie.base.jk;

import android.view.View;
import android.widget.Checkable;

import java.util.List;

/**
 * @author : Created by ChenJiaLiang on 2016/12/15.
 *         Email : 576507648@qq.com
 */

public interface OnListCheckedChangeListener<T> {
    void onCheckedChanged(List<T> checkList);
}
