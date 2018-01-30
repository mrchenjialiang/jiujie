package com.jiujie.base.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.jiujie.base.R;
import com.jiujie.base.jk.OnChooseEnsure;
import com.jiujie.base.jk.OnSelectListener;
import com.jiujie.base.widget.PickerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * author : Created by ChenJiaLiang on 2016/10/12.
 * Email : 576507648@qq.com
 */
public class SinglePickerDialog extends BaseDialog implements View.OnClickListener {
    private int mCurrentPosition;
    private List<String> mDataList = new ArrayList<>();

    public SinglePickerDialog(Context context) {
        super(context);
    }

    public void create(String[] data) {
        create(Arrays.asList(data));
    }

    public void create(List<String> dataList) {
        if (dataList == null || dataList.size() == 0) {
            throw new NullPointerException("dataList should not be null or size == 0");
        }
        mDataList.addAll(dataList);
        create(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM, R.style.BottomAlertAni);
    }

    @Override
    protected void initUI(View layout) {
        PickerView mPickerView = (PickerView) layout.findViewById(R.id.dsp_picker);
        layout.findViewById(R.id.dsp_cancel).setOnClickListener(this);
        layout.findViewById(R.id.dsp_ensure).setOnClickListener(this);
        mPickerView.setData(mDataList);
        mPickerView.setOnSelectListener(new OnSelectListener() {
            @Override
            public void onSelect(int position) {
                mCurrentPosition = position;
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_single_picker;
    }

    private OnChooseEnsure<Integer> onChooseEnsure;

    public void setOnChooseEnsure(OnChooseEnsure<Integer> onChooseEnsure) {
        this.onChooseEnsure = onChooseEnsure;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.dsp_ensure) {
            if (onChooseEnsure != null) {
                onChooseEnsure.onEnsure(mCurrentPosition);
            }
            dismiss();
        } else if (i == R.id.dsp_cancel) {
            dismiss();
        }
    }
}
