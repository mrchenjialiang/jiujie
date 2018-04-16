package com.jiujie.base.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.Looper;
import android.os.StatFs;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiujie.base.APP;
import com.jiujie.base.R;
import com.jiujie.base.WaitingDialog;
import com.jiujie.base.jk.InputAction;
import com.jiujie.base.jk.OnListener;
import com.jiujie.base.jk.OnScrollListener;
import com.jiujie.base.jk.OnSimpleListener;
import com.jiujie.base.util.appupdate.NotificationUtil;
import com.jiujie.base.util.recycler.MyGridLayoutManager;
import com.jiujie.base.util.recycler.MyLinearLayoutManager;
import com.jiujie.base.util.recycler.MyStaggeredGridLayoutManager;
import com.jiujie.base.util.recycler.PagingScrollHelper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.provider.MediaStore.Video.Thumbnails.MINI_KIND;

@SuppressWarnings("deprecation")
@SuppressLint("SimpleDateFormat")
public class UIHelper {
    private static String TIME_ZONE_ID = "GMT+08";
    private static long currentAndServiceTimeJg;//当前时间-服务器时间

    public static void initTime() {
        long timeMillis = System.currentTimeMillis();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            String currentTimeZoneId = getCurrentTimeZoneId();
            setTimeZoneId(currentTimeZoneId);

            String serviceTime = timeLongHaoMiaoToString(timeMillis, "yyyy-MM-dd HH:mm:ss", "GMT+08");
            String currentTime = timeLongHaoMiaoToString(timeMillis, "yyyy-MM-dd HH:mm:ss", currentTimeZoneId);
            Date serviceDate = df.parse(serviceTime);
            Date currentDate = df.parse(currentTime);
            currentAndServiceTimeJg = currentDate.getTime() - serviceDate.getTime();
        } catch (Exception e) {
            currentAndServiceTimeJg = 0;
        }
    }

    public static void setTimeZoneId(String timeZoneId) {
        if (!TextUtils.isEmpty(timeZoneId)) {
            TIME_ZONE_ID = timeZoneId;
        }
    }

    /**
     * 获取当前时区
     */
    public static String getCurrentTimeZoneId() {
        TimeZone tz = TimeZone.getDefault();
        return tz.getID();
    }

    public static long getCurrentServiceTime() {
        long timeMillis = System.currentTimeMillis();
        return timeMillis - currentAndServiceTimeJg;
    }

    /**
     * 获取版本号
     *
     * @return 应用的版本号
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            return "1.0.0";
        }
    }

    /**
     * 获取版本号
     *
     * @return 应用的版本号
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            return 1;
        }
    }

    /**
     * 获取版本号
     *
     * @return 应用的版本号
     */
    public static String getVersion(Context context, String packageName) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(packageName, 0);
            return info.versionName;
        } catch (Exception e) {
            return "1.0.0";
        }
    }

    public static String getImei(Context context) {
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return "";
            }
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm == null) {
                return "";
            }
            return tm.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isRunInUIThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static void showToastShort(final Activity context, final String text) {
        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(text.trim())) {
            return;
        }
        if (isRunInUIThread()) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        } else {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static void showToastLong(final Activity context, final String text) {
        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(text.trim())) {
            return;
        }
        if (isRunInUIThread()) {
            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        } else {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, text, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public static void showLog(Object... text) {
        if (APP.isDeBug) {
            if (text != null) {
                for (Object o : text) {
                    Log.e("LOG", "" + o.toString());
                }
            }
        }
    }

    public static void showLog(Object key,String text) {
        if (APP.isDeBug) {
            if(key==null){
                return;
            }
            if(key instanceof String){
                Log.e(key.toString(), ""+text);
            }else{
                Log.e(key.getClass().getSimpleName(), ""+text);
            }
        }
    }

    public static void showLog(String text) {
        showLog("LOG", text);
    }

    public static void showLogInFile(String text) {
//        writeStringToFile("E:/AndroidLog/","log.txt",text);
        writeStringToFile(Environment.getExternalStorageDirectory().getPath(), "log.txt", text);
    }

    public static void writeStringToFile(final String fileDic, final String fileName, final String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (TextUtils.isEmpty(fileDic)) {
            return;
        }
        if (TextUtils.isEmpty(fileName)) {
            return;
        }
        FileUtil.requestPermission(new OnListener<Boolean>() {
            @Override
            public void onListen(Boolean isHas) {
                if(isHas){
                    File file = FileUtil.createFile(fileDic, fileName);
                    if (file == null) {
                        return;
                    }
                    FileOutputStream fos;
                    try {
                        fos = new FileOutputStream(file, false);
                        fos.write(text.getBytes());
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static String readStringFromFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String mimeTypeLine;
            while ((mimeTypeLine = br.readLine()) != null) {
                sb.append(mimeTypeLine);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5 * (dipValue >= 0 ? 1 : -1));
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取网络状态
     */
    public static boolean getNetWorkStatus(Context context) {
//        boolean netStatus = false;
//        ConnectivityManager cwjManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
//        cwjManager.getActiveNetworkInfo();
//        if (cwjManager.getActiveNetworkInfo() != null) {
//            netStatus = cwjManager.getActiveNetworkInfo().isAvailable();
//        }
//        return netStatus;
        //6.0 之后得使用 getApplicationContext()..getSystemService(...)
        //否则会内存泄漏
        if (context == null) return false;
        ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) return false;
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static int getNetworkSimpleType(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) return 0;
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
//                return "WIFI";
                return 1;
            } else {
//                return "GPRS";
                return 2;
            }
        }
        return 0;
    }

    /**
     * 获取网络状态
     */
    public static String getNetworkType(Context context) {
        String strNetworkType = "";
        ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) return "";
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();
                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }
                        break;
                }
            }
        }
        return strNetworkType;
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Activity mActivity) {
        ViewGroup rootView = (ViewGroup) mActivity.getWindow().getDecorView();
        Display display = mActivity.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int screenWidth = Math.max(width, rootView.getWidth());
        SharePHelper.instance(mActivity).saveObject("screenWidth",screenWidth);
        return screenWidth;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(Activity mActivity) {
        ViewGroup rootView = (ViewGroup) mActivity.getWindow().getDecorView();
        Display display = mActivity.getWindowManager().getDefaultDisplay();
        int height = display.getHeight();
        int screenHeight = Math.max(height, rootView.getHeight());
        SharePHelper.instance(mActivity).saveObject("screenHeight",screenHeight);
        return screenHeight;
    }

    public static int getScreenWidthByRead(Context context){
        Integer screenWidth = SharePHelper.instance(context).readObject("screenWidth");
        return screenWidth==null?0:screenWidth;
    }

    public static int getScreenHeightByRead(Context context){
        Integer screenHeight = SharePHelper.instance(context).readObject("screenHeight");
        return screenHeight==null?0:screenHeight;
    }

    /**
     * 隐藏键盘
     */
    public static void hidePan(Activity activity) {
        try {
            if (activity != null) {
                View currentFocus = activity.getCurrentFocus();
                if (currentFocus != null) {
                    IBinder windowToken = currentFocus.getWindowToken();
                    if (windowToken != null) {
                        InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (manager != null)
                            manager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示键盘
     */
    public static void showPan(Activity activity, View view) {
        try {
            if (activity != null) {
                InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (manager != null) manager.showSoftInput(view, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param type 1:into center 0→max, 2,out center max →0
     */
    public static void setJumpAnimation(Activity activity, int type) {
        if (type == 1) {
            activity.overridePendingTransition(R.anim.center_0_to_max, R.anim.alpha_null);
        } else if (type == 2) {
            activity.overridePendingTransition(R.anim.alpha_null, R.anim.center_max_to_0);
        }
    }

    //yyyy-MM-dd hh:mm:ss----12小时制
    //yyyy-MM-dd HH:mm:ss----24小时制

    /**
     * 时间格式转换
     */
    public static long timeStrToTimeLong(String timeStr, String format) {
        Date date;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            sdf.setTimeZone(TimeZone.getTimeZone(TIME_ZONE_ID));
            date = sdf.parse(timeStr);
        } catch (ParseException e) {
            date = new Date();
        }
        return date.getTime();
    }

    /**
     * 时间格式转换
     */
    public static String timeStrToTimeStr(String timeStr, String oldFormat, String newFormat) {
        Date date;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(oldFormat);
            sdf.setTimeZone(TimeZone.getTimeZone(TIME_ZONE_ID));
            date = sdf.parse(timeStr);
        } catch (Exception e) {
            date = new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat(newFormat);
        sdf.setTimeZone(TimeZone.getTimeZone(TIME_ZONE_ID));
        return sdf.format(date);
    }

    /**
     * 时间格式转换，固定从yyyy-MM-dd HH:mm:ss 转换为  yyyy-MM-dd
     */
    public static String timeStrToTimeStr1(String timeStr) {
        return timeStrToTimeStr(timeStr, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd");
    }

    /**
     * 时间格式转换，固定从yyyy-MM-dd HH:mm:ss 转换为  yyyy-MM-dd
     */
    public static String timeStrToTimeStr2(String timeStr) {
        return timeStrToTimeStr(timeStr, "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd");
    }

    /**
     * 将时间戳转换成类似yyyy-MM-dd HH:mm:ss
     *
     * @param timestamp  时间戳  秒为单位
     * @param timeFromat 时间格式
     */
    public static String timeDoubleMiaoToString(Double timestamp, String timeFromat) {
        SimpleDateFormat sdf = new SimpleDateFormat(timeFromat);
        sdf.setTimeZone(TimeZone.getTimeZone(TIME_ZONE_ID));
        return sdf.format(new Date((long) (timestamp * 1000L)));
    }

    /**
     * 将时间戳转换成类似yyyy-MM-dd HH:mm:ss
     *
     * @param timestamp  时间戳  秒为单位
     * @param timeFromat 时间格式
     */
    public static String timeLongMiaoToString(long timestamp, String timeFromat) {
        SimpleDateFormat sdf = new SimpleDateFormat(timeFromat);
        sdf.setTimeZone(TimeZone.getTimeZone(TIME_ZONE_ID));
        return sdf.format(new Date(timestamp * 1000L));
    }

    /**
     * 毫秒 时间戳转换为String
     */
    public static String timeLongHaoMiaoToString(long millis, String timeFromat) {
        SimpleDateFormat sdf = new SimpleDateFormat(timeFromat);
        sdf.setTimeZone(TimeZone.getTimeZone(TIME_ZONE_ID));
        return sdf.format(new Date(millis));
    }

    /**
     * 毫秒 时间戳转换为String
     */
    public static String timeLongHaoMiaoToString(long millis, String timeForm, String timeZoneId) {
        SimpleDateFormat sdf = new SimpleDateFormat(timeForm);
        sdf.setTimeZone(TimeZone.getTimeZone(timeZoneId));
        return sdf.format(new Date(millis));
    }

    /**
     * 生成随机的num位的字符串(数字+字母)
     */
    public static String getRandomStr(int num) {
        StringBuilder builder = new StringBuilder();
        char[] chars = {
                '1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D',
                'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
                'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
                'Y', 'Z'};
//        int[] cha = new int[num];
        for (int i = 0; i < num; i++) {
            int j = (int) (Math.random() * 62);
            builder.append(chars[j]);
        }
        return builder.toString();
    }

    /**
     * 只带一个参数的这个方法,适用于子listView的item的高度是固定不会变动的情况
     * listView嵌套listView,设置子listView的高度,子listView的item必须是LinearLayout,因为其他的没重写onMeasure()方法
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            int itemHeight = listItem.getMeasuredHeight();
//			System.out.println("item"+i+""+itemHeight);
            totalHeight += itemHeight;
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 设置ListView无默认间隔线和选中selector，无滚动条
     */
    public static void setListViewSome(ListView listView) {
        setListViewNoDividerAndSelector(listView);
        setListViewNoOverScroll(listView);
        setListViewNoScrollBar(listView);
    }

    /**
     * 设置ListView无默认间隔线和选中selector
     */
    public static void setListViewNoDividerAndSelector(ListView listView) {
        listView.setDivider(null);
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }

    /**
     * 设置ListView无过度滚动的颜色
     */
    public static void setListViewNoOverScroll(ListView listView) {
        listView.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
    }

    /**
     * 设置ListView无滚动条
     */
    public static void setListViewNoScrollBar(ListView listView) {
        listView.setVerticalScrollBarEnabled(false);
    }

    /**
     * 为TextView设置内容，如果没有内容就Gone，有内容才可见---避免界面上出现没内容却占用空间的情况
     */
    public static void setText(TextView tv, String text) {
        if (!TextUtils.isEmpty(text)) {
            tv.setVisibility(View.VISIBLE);//避免复用时的Gone
            tv.setText(text);
        } else {
            tv.setVisibility(View.GONE);
        }
    }

    /**
     * 获取缓冲对话框
     * should not use application Context
     */
    public static Dialog getWaitingDialog(Activity activity) {
        WaitingDialog waitingDialog = new WaitingDialog(activity);
        waitingDialog.create();
        return waitingDialog.getDialog();
    }

    /**
     * 拷贝进剪切板
     */
    public static void copyText(Activity activity, CharSequence text, boolean isShowToast) {
        boolean isSuccess = copyText(activity, text);
        if (isShowToast) showToastShort(activity, isSuccess ? "复制成功" : "复制失败");
    }

    /**
     * 拷贝进剪切板
     */
    public static boolean copyText(Context context, CharSequence text) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard == null) {
            Log.e("Log", "复制失败，clipboard==null");
            return false;
        }
        if (!TextUtils.isEmpty(text)) {
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.HONEYCOMB) {
                clipboard.setPrimaryClip(ClipData.newPlainText("text", text));    //API 11之后使用该方法

                CharSequence getText = clipboard.getText();
                if (text.equals(getText)) {
                    Log.e("Log", "复制成功");
                    return true;
                } else {
                    Log.e("Log", "复制失败");
                    return false;
                }
            } else {
                clipboard.setText(text);//API 11 之前

                CharSequence getText = clipboard.getText();
                if (text.equals(getText)) {
                    Log.e("Log", "复制成功");
                    return true;
                } else {
                    Log.e("Log", "复制失败");
                    return false;
                }
            }
        } else {
            Log.e("Log", "复制内容为空，不复制");
            return false;
        }
    }


    public static void waitDevelopment(Activity context) {
        showToastShort(context, "相关功能正在研发中，敬请期待！");
    }

    public static void waitForOpen(Activity context) {
        showToastShort(context, "尚未开放，敬请期待！");
    }

    /**
     * 获取某年某个月的所有日
     */
    public static String[] getDaysOfMonth(int year, int month) {
        int size = 0;
        if (month == 2) {
            if (year % 4 == 0) {
                size = 29;
            } else {
                size = 28;
            }
        } else if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            size = 31;
        } else {
            size = 30;
        }
        String[] days = new String[size];
        for (int i = 0; i < size; i++) {
            days[i] = (i + 1) + "";
        }
        return days;
    }

    /**
     * 从assets中获取文件内容
     *
     * @param fileName 文件名
     * @return 文件内容
     */
    public static String getDataFromAssets(Context context, String fileName) {
        StringBuilder builder = new StringBuilder();
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            while ((line = bufReader.readLine()) != null)
                builder.append(line);
        } catch (Exception e) {
            Toast.makeText(context, "出异常了!", Toast.LENGTH_SHORT).show();
            return null;
        }
        return builder.toString();
    }

    /**
     * 拷贝出一个不被影响的List，需要List中的数据对象序列号才有效
     */
    @SuppressWarnings({"rawtypes"})
    public static List copyBySerialize(List src) {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out;
        List dest = null;
        try {
            out = new ObjectOutputStream(byteOut);
            out.writeObject(src);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            dest = (List) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dest;
    }

    /**
     * MD5加密
     */
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

//    /**
//     * 清除ImageLoader的某个缓存
//     * @param url
//     */
//    public static void clearImageLoaderCache(String url) {
//        if (!TextUtils.isEmpty(url)) {
//            DiscCacheAware discCacheAware = ImageLoader.getInstance()
//                    .getDiscCache();
//            File file = discCacheAware.get(url);
//            if (file != null && file.exists()) {
//                file.delete();
//            }
//        }
//    }

//    use for ImageLoader but never use,now,use Glide
//    public static File getImageLoaderCache(String url) {
//        if (!TextUtils.isEmpty(url)) {
//            DiscCacheAware discCacheAware = ImageLoader.getInstance()
//                    .getDiscCache();
//            File file = discCacheAware.get(url);
//            if (file != null && file.exists()) {
//                return file;
//            }
//        }
//        return null;
//    }

    /**
     * 格式化单位   B → KB，MB，GB，TB
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
//         return size + "Byte";
            return "0KB";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    /**
     * 保留1位小数→Str
     */
    public static String getOneDecimal(double num) {
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(num);
    }

    /**
     * 保留1位小数→Str
     */
    public static Double getOneDecimalDouble(double num) {
        DecimalFormat df = new DecimalFormat("0.0");
        return Double.valueOf(df.format(num));
    }

    /**
     * 保留1位小数→Str
     */
    public static Float getOneDecimalFloat(float num) {
        DecimalFormat df = new DecimalFormat("0.0");
        return Float.valueOf(df.format(num));
    }

    /**
     * 保留两位小数→Str
     */
    public static String getTwoDecimal(float num) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(num);
    }

    /**
     * 保留两位小数→Str
     */
    public static String getTwoDecimal(double num) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(num);
    }

    /**
     * 保留三位小数→Str
     */
    public static String getThreeDecimal(double num) {
        DecimalFormat df = new DecimalFormat("0.000");
        return df.format(num);
    }

    /**
     * 保留三位小数→double
     */
    public static double getThreeDecimal1(double num) {
        DecimalFormat df = new DecimalFormat("0.000");
        return Double.valueOf(df.format(num));
    }

    /**
     * 保留两位小数→float
     */
    public static float getTwoDecimal1(float num) {
        return (float) Math.round(num * 100) / 100;
    }

    /**
     * 保留两位小数→float
     */
    public static double getTwoDecimal1(double num) {
        return (double) Math.round(num * 100) / 100;
    }

    /**
     * 让TextView中的关键字不同的颜色
     */
    public static void setTextColorWhenKeyWord(TextView textView, String text, String keyWord, int color) {
        if (!TextUtils.isEmpty(keyWord)) {
            if (text.contains(keyWord)) {
                SpannableString sp = new SpannableString(text);
                int indexOf = text.indexOf(keyWord);
                sp.setSpan(new ForegroundColorSpan(color), indexOf, indexOf + keyWord.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                textView.setText(sp);
            } else {
                textView.setText(text);
            }
        } else {
            textView.setText(text);
        }
    }

    /**
     * 震动
     */
    public static void showVibrator(Context context) {
        // 震动提示
        Vibrator vibrator = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator == null) return;
        long[] pattern = {50, 300, 50, 200};
        vibrator.vibrate(pattern, -1);
    }

    /**
     * 系统提醒音
     */
    public static void startRing(Context context) {
        try {
            MediaPlayer mp = new MediaPlayer();
            mp.reset();
            mp.setDataSource(context, RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            showLog("Exception startRing:" + e);
        }
    }


    /**
     * 震动
     */
    public static void startVibrator(Context context) {
        // 震动提示
        @SuppressWarnings("static-access")
        Vibrator vibrator = (Vibrator) context
                .getSystemService(context.VIBRATOR_SERVICE);
        if (vibrator == null) return;
        long[] pattern = {50, 300, 50, 200};
        vibrator.vibrate(pattern, -1);
    }

    public static int getStatusBarHeightByReadR(Context context) {
        Class<?> c;
        Object obj;
        Field field;
        int x;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
            return 75;
        }
    }

    /**
     * 显示通知
     */
    public static void showNotification(Context context, int drawableId,
                                        String firstContent, Intent actionIntent, String showTitle,
                                        String showContent, int notifyId) {
        NotificationUtil.showNotification(context, drawableId, firstContent, actionIntent, showTitle, showContent, notifyId);
    }

    /**
     * Unicode编码转'中文'String
     */
    public static String decode(String text) {
        Pattern reUnicode = Pattern.compile("\\\\u([0-9a-zA-Z]{4})");
        Matcher m = reUnicode.matcher(text);
        StringBuffer sb = new StringBuffer(text.length());
        while (m.find()) {
            m.appendReplacement(sb,
                    Character.toString((char) Integer.parseInt(m.group(1), 16)));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * String→URLEncoder
     */
    public static String toURLEncoded(String paramString) {
        if (TextUtils.isEmpty(paramString)) {
            return paramString;
        }

        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (Exception localException) {
            return paramString;
        }
    }

    /**
     * 刷新手机图库
     *
     * @param file 新存储到本地的图片文件
     */
    public static void refreshSystemImage(Context context, File file) {
        if (file == null || !file.exists() || file.length() == 0) {
            return;
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), file.getName(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
    }

    public static boolean isIntentExisting(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfo =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.size() > 0;
    }

    /**
     * 判断某一个类是否存在任务栈里面
     */
    public static boolean isActivityExistInTask(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        ComponentName cmpName = intent.resolveActivity(context.getPackageManager());
        boolean flag = false;
        if (cmpName != null) { // 说明系统中存在这个activity
            ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
            if (am == null) return false;
            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
                    flag = true;
                    break;  //跳出循环，优化效率
                }
            }
        }
        return flag;
    }

    @NonNull
    public static String removeUselessLine(String content) {
        if (content == null) {
            content = "";
        }
        content = content.replaceAll("<div><br \\/><\\/div>", "");
        while (content.startsWith("<br />") || content.startsWith("<br >") || content.startsWith("<br/>") || content.startsWith("<br>") || content.startsWith("\\n")) {
            if (content.startsWith("<br />")) {
                content = content.substring("<br />".length(), content.length()).trim();
            }
            if (content.startsWith("<br >")) {
                content = content.substring("<br >".length(), content.length()).trim();
            }
            if (content.startsWith("<br/>")) {
                content = content.substring("<br/>".length(), content.length()).trim();
            }
            if (content.startsWith("<br>")) {
                content = content.substring("<br>".length(), content.length()).trim();
            }
            if (content.startsWith("\\n")) {
                content = content.substring("\\n".length(), content.length()).trim();
            }
        }
        while (content.endsWith("<br />") || content.endsWith("<br >") || content.endsWith("<br/>") || content.endsWith("<br>") || content.endsWith("\\n")) {
            if (content.endsWith("<br />")) {
                content = content.substring(0, content.length() - "<br />".length()).trim();
            }
            if (content.endsWith("<br >")) {
                content = content.substring(0, content.length() - "<br >".length()).trim();
            }
            if (content.endsWith("<br/>")) {
                content = content.substring(0, content.length() - "<br/>".length()).trim();
            }
            if (content.endsWith("<br>")) {
                content = content.substring(0, content.length() - "<br>".length()).trim();
            }
            if (content.endsWith("\\n")) {
                content = content.substring(0, content.length() - "\\n".length()).trim();
            }
        }
        return content;
    }

    //    public static boolean isMobilePhone(String mobiles){
//        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//        Matcher m = p.matcher(mobiles);
//        return m.matches();
//    }
    public static boolean isMobilePhone(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 校验是否是银行卡卡号
     */
    public static boolean isBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        return bit != 'N' && cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     */
    private static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 获得SD卡总大小
     */
    public static long getSDTotalSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return blockSize * totalBlocks;
//        return Formatter.formatFileSize(activity, blockSize * totalBlocks);
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     */
    public static long getSDAvailableSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return blockSize * availableBlocks;
//        return Formatter.formatFileSize(activity, blockSize * availableBlocks);
    }

    /**
     * 获得机身内存总大小
     */
    public static long getRomTotalSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return blockSize * totalBlocks;
//        return Formatter.formatFileSize(activity, blockSize * totalBlocks);
    }

    /**
     * 获得机身可用内存
     */
    public static long getRomAvailableSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return blockSize * availableBlocks;
//        return Formatter.formatFileSize(activity, blockSize * availableBlocks);
    }

    public static long getFileSize(File file) {
        long size = 0;
        if (file != null && file.exists()) {
            if (file.isDirectory()) {
                for (File fileItem : file.listFiles()) {
                    size += getFileSize(fileItem);
                }
            } else {
                return file.length();
            }
        } else {
            return 0;
        }
        return size;
    }

    public static Object doMethod(Class<?> classObject, String methodName) {
        try {
            Method classMethod = classObject.getDeclaredMethod(methodName);
            classMethod.setAccessible(true);
            return classMethod.invoke(classObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 判断微信是否可用
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断qq是否可用
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void initRecyclerViewHPage(Context context, RecyclerView mRecyclerView, OnListener<Integer> onPageChangeListener) {
        initRecyclerViewH(context, mRecyclerView);
        new PagingScrollHelper(mRecyclerView, onPageChangeListener);
    }

    public static void initRecyclerViewH(Context context, RecyclerView mRecyclerView) {
        initRecyclerView(context, mRecyclerView, 0, 0, 0);
    }

    public static void initRecyclerViewV(Context context, RecyclerView mRecyclerView) {
        initRecyclerView(context, mRecyclerView, 0, 0, 1);
    }

    public static void initRecyclerViewV(Context context, RecyclerView mRecyclerView, int type, int gNum) {
        initRecyclerView(context, mRecyclerView, type, gNum, 1);
    }

    public static void initRecyclerView(Context context, RecyclerView mRecyclerView, int type, int gNum) {
        initRecyclerView(context, mRecyclerView, type, gNum, 1);
    }

    /**
     * 初始化RecyclerView
     *
     * @param type   0:普通列表，1：普通GridView样式，2：瀑布流样式
     * @param gNum   如果type = 1 或 2，则该值表示列数
     * @param orient 0:水平，1垂直
     */
    public static void initRecyclerView(Context context, RecyclerView mRecyclerView, int type, int gNum, int orient) {
        mRecyclerView.setHasFixedSize(true);
        if (type == 0) {
            MyLinearLayoutManager mLayoutManager = new MyLinearLayoutManager(context.getApplicationContext(), orient == 0 ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(mLayoutManager);
        } else if (type == 1) {
            MyGridLayoutManager gridLayoutManager = new MyGridLayoutManager(context.getApplicationContext(), gNum, orient == 0 ? GridLayoutManager.HORIZONTAL : GridLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        } else {
            MyStaggeredGridLayoutManager staggeredGridLayoutManager =
                    new MyStaggeredGridLayoutManager(gNum, orient == 0 ? StaggeredGridLayoutManager.HORIZONTAL : StaggeredGridLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        }
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setSupportsChangeAnimations(false);
        mRecyclerView.setItemAnimator(animator);
    }

    /**
     * 获取应用的签名
     */
    public static String getAPPSHA1Value() {
        //获取包管理器
        PackageManager pm = APP.getContext().getPackageManager();

        //获取当前要获取 SHA1 值的包名，也可以用其他的包名，但需要注意，
        //在用其他包名的前提是，此方法传递的参数 Context 应该是对应包的上下文。
        String packageName = APP.getContext().getPackageName();

        //返回包括在包中的签名信息
        int flags = PackageManager.GET_SIGNATURES;

        try {
            //获得包的所有内容信息类
            PackageInfo packageInfo = pm.getPackageInfo(packageName, flags);
            //签名信息
            Signature[] signatures = packageInfo.signatures;
            byte[] cert = signatures[0].toByteArray();

            //将签名转换为字节数组流
            InputStream input = new ByteArrayInputStream(cert);

            //证书工厂类，这个类实现了出厂合格证算法的功能
            CertificateFactory cf = CertificateFactory.getInstance("X509");
            X509Certificate c = (X509Certificate) cf.generateCertificate(input);
            String hexString;
            //加密算法的类，这里的参数可以使 MD4,MD5 等加密算法
            MessageDigest md = MessageDigest.getInstance("SHA1");

            //获得公钥
            byte[] publicKey = md.digest(c.getEncoded());

            //字节到十六进制的格式转换
            hexString = byte2HexFormatted(publicKey);
            return hexString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //这里是将获取到得编码进行16 进制转换
    private static String byte2HexFormatted(byte[] arr) {
        StringBuilder str = new StringBuilder(arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();
            if (l == 1)
                h = "0" + h;
            if (l > 2)
                h = h.substring(l - 2, l);
            str.append(h.toUpperCase());
            if (i < (arr.length - 1))
                str.append(':');
        }
        return str.toString();
    }

    public static void setEditTextAction(final Activity activity, final EditText editText, final InputAction inputAction) {
        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_GO) {
                    if (activity != null) UIHelper.hidePan(activity);
                    if (inputAction != null) {
                        Editable text = editText.getText();
                        if (TextUtils.isEmpty(text.toString().trim())) {
                            inputAction.send("");
                        } else {
                            inputAction.send(editText.getText().toString().trim());
                            editText.setSelection(text.length());
                        }
                    }
                }
                return false;
            }
        });
    }

    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     *
     * @param videoPath 视频的路径
     *                  width 指定输出视频缩略图的宽度
     *                  height 指定输出视频缩略图的高度度
     *                  kind 参照MediaStore.Images(Video).Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *                  其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     *                  指定大小的视频缩略图
     */
    public static void setVideoThumbLocal(String videoPath, ImageView imageView, int width, int height) {
        Bitmap bitmap;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MINI_KIND);
        if (bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            imageView.setImageBitmap(bitmap);
        }
    }

    /**
     * 获取视频的预览图的方法要调用的方法
     */
    public static void setVideoThumbnail(final String url, final ImageView imageView) {
        if (TextUtils.isEmpty(url) || imageView == null) return;

        final long tagStart = System.currentTimeMillis();
        imageView.setTag(tagStart);
        new TaskManager<Bitmap>() {
            @Override
            public Bitmap runOnBackgroundThread() {
                return createVideoThumbnail(url);
            }

            @Override
            public void runOnUIThread(Bitmap bitmap) {
                long tag = (long) imageView.getTag();
                if (tag == tagStart && bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }.start();
    }

    /**
     * 获取视频预览图
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static Bitmap createVideoThumbnail(String url) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            return retriever.getFrameAtTime();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                retriever.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        if (bitmap != null) {
//            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
//                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
//        }
//        return bitmap;
    }

    public static void setDialogNoDismissByTouchAndBackPress(Dialog dialog) {
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });
    }

    public static boolean isSdCardExist() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static boolean installNormal(Context context, String filePath) {
        File file = new File(filePath);
        return installNormal(context, file);
    }

    public static boolean installNormal(Context context, File file) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        if (!file.exists() || !file.isFile() || file.length() <= 0) {
            return false;
        }
        i.setDataAndType(UriUtil.getUri(context, i, file), "application/vnd.android.package-archive");
        context.startActivity(i);
        return true;
    }

    /**
     * 从url中截取文件名
     */
    public static String getFileName(String url) {
        int index = url.lastIndexOf("/");
        if (index != -1) {
            return url.substring(index + 1);
        }
        return null;
    }

    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        if (am == null) return null;
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    public static void recycleImageView(ImageView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
            drawable.setCallback(null);
            imageView.setImageDrawable(null);
            System.gc();
        }
    }

    /**
     * 设置状态栏黑色字体图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @return 1:MIUUI 2:Flyme 3:android6.0
     */
    public static int setStatusBarLightMode(Activity activity) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (MIUISetStatusBarLightMode(activity.getWindow(), true)) {
                result = 1;
            } else if (FlymeSetStatusBarLightMode(activity.getWindow(), true)) {
                result = 2;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                result = 3;
            }
        }
        return result;
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    private static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    private static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 当前日期 年_月_日_时_分_秒
     *
     * @param fileType .jpg  .png   .txt  ...
     */
    public static String getTimeFileName(String fileType) {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        return sdf.format(new Date(currentTimeMillis)) + fileType;
    }

    public static void getViewDrawListen(final View view, final OnSimpleListener onSimpleListener) {
        final ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                if (onSimpleListener != null) {
                    onSimpleListener.onListen();
                }
                return true;
            }
        });
    }

    public static <T> T[] list2Array(List<T> list, T[] array) {
        return list.toArray(array);
    }

    public static <T> T[] list2Array(List<T> list) {
        T[] array = (T[]) new Object[list.size()];
        return list.toArray(array);
    }

    public static <T> List<T> array2List(T[] array) {
        return Arrays.asList(array);
    }

    public static void setOnListScrollListener(final RecyclerView mRecyclerView, final OnScrollListener onScrollListener) {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisiblePosition;
            int firstVisibleItemPosition;
            int scrolledX;
            int scrolledY;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrolledX += dx;
                scrolledY += dy;
                RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                    lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();
                    firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    StaggeredGridLayoutManager layoutManager1 = (StaggeredGridLayoutManager) layoutManager;
                    int[] lastPositions = new int[layoutManager1.getSpanCount()];
                    layoutManager1.findLastCompletelyVisibleItemPositions(lastPositions);
                    lastVisiblePosition = findMax(lastPositions);
                    int[] firstPositions = new int[layoutManager1.getSpanCount()];
                    layoutManager1.findFirstVisibleItemPositions(firstPositions);
                    firstVisibleItemPosition = findMin(firstPositions);
                }
                if (onScrollListener != null)
                    onScrollListener.onScroll(scrolledX, scrolledY, firstVisibleItemPosition, lastVisiblePosition);

            }

            //To find the maximum value in the array
            private int findMax(int[] lastPositions) {
                int max = lastPositions[0];
                for (int value : lastPositions) {
                    if (value > max) {
                        max = value;
                    }
                }
                return max;
            }

            private int findMin(int[] firstPositions) {
                int min = firstPositions[0];
                for (int value : firstPositions) {
                    if (value < min) {
                        min = value;
                    }
                }
                return min;
            }

        });
    }

    public static String getRandomNumString(int size) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            int num = new Random().nextInt(10);
            if (i == 0) {
                while (num == 0) {
                    num = new Random().nextInt(10);
                }
            }
            sb.append(num);
        }
        return sb.toString();
    }

    public static String getDeviceId() {
        try {
            String tmDevice = "";
            if (ActivityCompat.checkSelfPermission(APP.getContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager tm = (TelephonyManager) APP.getContext().getSystemService(Context.TELEPHONY_SERVICE);
                tmDevice = tm.getDeviceId();
            }

            final String androidId = Settings.Secure.getString(APP.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);

            UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32));

            return deviceUuid.toString();
        } catch (Exception e) {
            showLog("Exception when getDeviceId " + e);
        }
        return "";
    }
}
