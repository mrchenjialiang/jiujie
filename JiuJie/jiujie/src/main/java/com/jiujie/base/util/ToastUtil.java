package com.jiujie.base.util;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jiujie.base.APP;
import com.jiujie.base.R;

/**
 * Created by ChenJiaLiang on 2018/4/17.
 * Email:576507648@qq.com
 */

public class ToastUtil {

    private static ToastHolder toastHolder;
//    private static Toast toast;

    public static void clear() {
        if (toastHolder != null) {
            toastHolder.doRelease();
            toastHolder = null;
        }
//        if (toast != null) {
//            toast.cancel();
//            toast = null;
//        }
    }

    public static void showToastShort(final String text) {
        showToast(text, false);
//        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(text.trim())) {
//            return;
//        }
//        if (APP.getContext() == null) {
//            UIHelper.showLog("showToast fail because APP.getContext==null,should do APP.init() when application create");
//            return;
//        }
//        if (UIHelper.isRunInUIThread()) {
//            if (toast == null) {
//                toast = Toast.makeText(APP.getContext(), "", Toast.LENGTH_SHORT);
//            }
//            toast.setText(text);
//            toast.setDuration(Toast.LENGTH_SHORT);
//            toast.show();
//        } else {
//            new TaskManager<Boolean>() {
//                @Override
//                public Boolean runOnBackgroundThread() {
//                    return null;
//                }
//
//                @Override
//                public void runOnUIThread(Boolean aBoolean) {
//                    if (toast == null) {
//                        toast = Toast.makeText(APP.getContext(), "", Toast.LENGTH_SHORT);
//                    }
//                    toast.setText(text);
//                    toast.setDuration(Toast.LENGTH_SHORT);
//                    toast.show();
//                }
//            }.start();
//        }
    }

    public static void showToastLong(final String text) {
        showToast(text, true);
//        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(text.trim())) {
//            return;
//        }
//        if (APP.getContext() == null) {
//            UIHelper.showLog("showToast fail because APP.getContext==null,should do APP.init() when application create");
//            return;
//        }
//        if (UIHelper.isRunInUIThread()) {
//            if (toast == null) {
////                toast = Toast.makeText(APP.getContext(), "", Toast.LENGTH_LONG);
//                toast = new Toast(APP.getContext());
//            }
//            toast.setText(text);
//            toast.setDuration(Toast.LENGTH_LONG);
//            toast.show();
//        } else {
//            new TaskManager<Boolean>() {
//                @Override
//                public Boolean runOnBackgroundThread() {
//                    return null;
//                }
//
//                @Override
//                public void runOnUIThread(Boolean aBoolean) {
//                    if (toast == null) {
//                        toast = Toast.makeText(APP.getContext(), "", Toast.LENGTH_LONG);
//                    }
//                    toast.setText(text);
//                    toast.setDuration(Toast.LENGTH_LONG);
//                    toast.show();
//                }
//            }.start();
//        }
    }


    private static void showToast(final String text, final boolean isLong) {
        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(text.trim())) {
            return;
        }
        if (APP.getContext() == null) {
            UIHelper.showLog("showToast fail because APP.getContext==null,should do APP.init() when application create");
            return;
        }
        if (UIHelper.isRunInUIThread()) {
            getToastHolder().show(text, isLong);
        } else {
            new TaskManager<Boolean>() {
                @Override
                public Boolean runOnBackgroundThread() {
                    return null;
                }

                @Override
                public void runOnUIThread(Boolean aBoolean) {
                    getToastHolder().show(text, isLong);
                }
            }.start();
        }
    }

    private static ToastHolder getToastHolder() {
        if (toastHolder == null) {
            Toast toast = new Toast(APP.getContext());
            View toastLayout = LayoutInflater.from(APP.getContext()).inflate(R.layout.layout_toast, null);
            TextView toastTv = toastLayout.findViewById(R.id.lt_text);
            toast.setView(toastLayout);
            toastHolder = new ToastHolder(toast, toastTv);
        }
        return toastHolder;
    }

    private static class ToastHolder {
        Toast toast;
        TextView toastTv;

        public ToastHolder(Toast toast, TextView toastTv) {
            this.toast = toast;
            this.toastTv = toastTv;
        }

        public void show(String text, boolean isLong) {
            try {
                toastTv.setText(text);
                toast.setDuration(isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
                toast.show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        public void doRelease() {
            toast.cancel();
            toast = null;
        }
    }
}
