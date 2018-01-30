package com.jiujie.base.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.jiujie.base.R;
import com.jiujie.base.jk.OnChooseEnsure;
import com.jiujie.base.jk.OnSelectListener;
import com.jiujie.base.util.DateUtil;
import com.jiujie.base.widget.PickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * author : Created by ChenJiaLiang on 2016/10/12.
 * Email : 576507648@qq.com
 */
public class ChooseCalendarDialog extends BaseDialog implements View.OnClickListener {
    private PickerView mPickerDay;
    private int mSelectYear;
    private int mSelectMonth;
    private int mSelectDay;
    private List<String> mDataListDay;
    private boolean isShowYearFuture;
    private List<String> mDataListYear = new ArrayList<>();

    public ChooseCalendarDialog(Context context) {
        super(context);
    }

    public void create() {
        create(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM, R.style.BottomAlertAni);
    }

    public void create(boolean isShowYearFuture) {
        this.isShowYearFuture = isShowYearFuture;
        create(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM, R.style.BottomAlertAni);
    }

    @Override
    protected void initUI(View layout) {
        PickerView mPickerYear = (PickerView) layout.findViewById(R.id.dcc_year);
        PickerView mPickerMonth = (PickerView) layout.findViewById(R.id.dcc_month);
        mPickerDay = (PickerView) layout.findViewById(R.id.dcc_day);
        layout.findViewById(R.id.dcc_cancel).setOnClickListener(this);
        layout.findViewById(R.id.dcc_ensure).setOnClickListener(this);

        Calendar currentCalendar = Calendar.getInstance();
        mSelectYear = currentCalendar.get(Calendar.YEAR);
        mSelectMonth = currentCalendar.get(Calendar.MONTH) + 1;
        mSelectDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        mDataListYear.clear();
        if (isShowYearFuture) {
            int currentPosition = 0;
            for (int i = 1; i < 50; i++) {
                currentPosition++;
                mDataListYear.add(0, (mSelectYear + i) + "");
            }
            mDataListYear.add(mSelectYear + "");
            for (int i = 1; i < 50; i++) {
                mDataListYear.add((mSelectYear - i) + "");
            }
            mPickerYear.setData(mDataListYear, currentPosition);
        } else {
            for (int i = 0; i < 100; i++) {
                mDataListYear.add((mSelectYear - i) + "");
            }
            mPickerYear.setData(mDataListYear, 0);
        }

        final List<String> mDataListMonth = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            mDataListMonth.add(i + "");
        }
        mPickerMonth.setData(mDataListMonth, mSelectMonth - 1);

        mDataListDay = new ArrayList<>();
        for (int i = 1; i <= DateUtil.getDaySizeOfMonth(mSelectYear, mSelectMonth - 1); i++) {
            mDataListDay.add(i + "");
        }
        mPickerDay.setData(mDataListDay, mSelectDay - 1);

        mPickerYear.setOnSelectListener(new OnSelectListener() {
            @Override
            public void onSelect(int position) {
//                mSelectYear = currentCalendar.get(Calendar.YEAR) - position;
//                if(showForTextView!=null)showForTextView.setText(mSelectYear + "-" + mSelectMonth + "-" + mSelectDay);
                mSelectYear = Integer.valueOf(mDataListYear.get(position));

                mDataListDay.clear();
                int daySizeOfMonth = DateUtil.getDaySizeOfMonth(mSelectYear, mSelectMonth - 1);
                if(mSelectDay>daySizeOfMonth){
                    mSelectDay = daySizeOfMonth;
                }
                for (int i = 1; i <= daySizeOfMonth; i++) {
                    mDataListDay.add(i + "");
                }
                mPickerDay.setData(mDataListDay, mSelectDay - 1);
            }
        });
        mPickerMonth.setOnSelectListener(new OnSelectListener() {
            @Override
            public void onSelect(int position) {
//                mSelectMonth = position + 1;
                mSelectMonth = Integer.valueOf(mDataListMonth.get(position));
//                if(showForTextView!=null)showForTextView.setText(mSelectYear + "-" + mSelectMonth + "-" + mSelectDay);

                mDataListDay.clear();
                int daySizeOfMonth = DateUtil.getDaySizeOfMonth(mSelectYear, mSelectMonth - 1);
                if(mSelectDay>daySizeOfMonth){
                    mSelectDay = daySizeOfMonth;
                }
                for (int i = 1; i <= daySizeOfMonth; i++) {
                    mDataListDay.add(i + "");
                }
                mPickerDay.setData(mDataListDay, mSelectDay - 1);
            }
        });
        mPickerDay.setOnSelectListener(new OnSelectListener() {
            @Override
            public void onSelect(int position) {
                mSelectDay = position + 1;
//                if(showForTextView!=null)showForTextView.setText(mSelectYear + "-" + mSelectMonth + "-" + mSelectDay);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_choose_calendar;
    }

    private OnChooseEnsure<Calendar> onChooseEnsure;

    public void setOnChooseEnsure(OnChooseEnsure<Calendar> onChooseEnsure) {
        this.onChooseEnsure = onChooseEnsure;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.dcc_ensure) {
            if (onChooseEnsure != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, mSelectYear);
                calendar.set(Calendar.MONTH, mSelectMonth - 1);
                calendar.set(Calendar.DAY_OF_MONTH, mSelectDay);
                onChooseEnsure.onEnsure(calendar);
            }
            dismiss();
        } else if (i == R.id.dcc_cancel) {
            dismiss();
        }
    }
}
