package com.jiujie.base.pop;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.jiujie.base.R;
import com.jiujie.base.adapter.ChoosePhotoDirAdapter;
import com.jiujie.base.jk.OnItemClickListen;
import com.jiujie.base.model.Image;
import com.jiujie.base.util.UIHelper;

import java.util.List;

/**
 * Created by ChenJiaLiang on 2017/8/10.
 * Email:576507648@qq.com
 */

public class ChoosePhotoDirPop extends BasePop{

    private RecyclerView mDirList;

    public ChoosePhotoDirPop(Activity activity,List<Image> mDataList,OnItemClickListen onItemClickListen) {
        super(activity,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        ChoosePhotoDirAdapter adapter = new ChoosePhotoDirAdapter(this, mDataList);
        adapter.setOnItemClickListen(onItemClickListen);
        mDirList.setAdapter(adapter);
    }

    @Override
    protected void initUI(View layout) {
        mDirList = (RecyclerView) layout.findViewById(R.id.pcpd_list);
        UIHelper.initRecyclerView(getActivity(), mDirList,0,0);
    }

    @Override
    public View getLayout(Context context) {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.pop_choose_photo_dir;
    }
}
