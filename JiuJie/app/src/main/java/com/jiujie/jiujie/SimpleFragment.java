package com.jiujie.jiujie;

import android.view.View;
import android.widget.LinearLayout;

import com.jiujie.base.adapter.BaseRecyclerViewAdapter;
import com.jiujie.base.fragment.BaseListFragment;
import com.jiujie.base.util.UIHelper;

/**
 * Created by ChenJiaLiang on 2018/1/11.
 * Email:576507648@qq.com
 */

public class SimpleFragment extends BaseListFragment{

    @Override
    protected void initUI() {
        setLoadingEnd();

        final LinearLayout tagLayout = getTagLayout();
        tagLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                showTabLayout(true);

                UIHelper.showLog("tagLayout:"+tagLayout);
                if(tagLayout!=null){
                    UIHelper.showLog("tagLayout可见？:"+(tagLayout.getVisibility()== View.VISIBLE));
                    UIHelper.showLog("tagLayout getChildCount:"+tagLayout.getChildCount());
                    UIHelper.showLog("tagLayout getHeight:"+tagLayout.getHeight());
                    tagLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            UIHelper.showLog("tagLayout getHeight:"+tagLayout.getHeight());
                        }
                    },1000);
                }
            }
        },2000);
    }

    @Override
    public int getTagLayoutId() {
        return R.layout.layout_tag;
    }

//    @Override
//    public int getLayoutId() {
//        return R.layout.frag_simple;
//    }

    @Override
    public BaseRecyclerViewAdapter getAdapter() {
        return new SimpleListAdapter();
    }

    @Override
    public void initData() {

    }
}
