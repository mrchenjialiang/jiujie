package com.jiujie.base.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.Looper;
import android.os.StatFs;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.text.ClipboardManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiujie.base.APP;
import com.jiujie.base.R;
import com.jiujie.base.WaitingDialog;
import com.jiujie.base.util.appupdate.NotificationUtil;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("deprecation")
@SuppressLint("SimpleDateFormat")
public class UIHelper {

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

    public static void showToastShort(final Activity context, final String text) {
        if(TextUtils.isEmpty(text)||TextUtils.isEmpty(text.trim())){
            return;
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
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
        if(TextUtils.isEmpty(text)||TextUtils.isEmpty(text.trim())){
            return;
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
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

    public static void showLog(String text) {
        if (APP.isDeBug) {
            Log.e("LOG", text);
        }
    }
    public static void showLogInFile(String text) {
//        writeStringToFile("E:/AndroidLog/","log.txt",text);
        writeStringToFile(Environment.getExternalStorageDirectory().getPath(), "log.txt", text);
    }

    public static void writeStringToFile(String fileDic,String fileName,String text){
        if(TextUtils.isEmpty(text)){
            return;
        }
        if(TextUtils.isEmpty(fileDic)){
            return;
        }
        if(TextUtils.isEmpty(fileName)){
            return;
        }
        File file = new File(fileDic);
        if(!file.exists()){
            boolean mkdirs = file.mkdirs();
            if(!mkdirs){
                UIHelper.showLog("writeStringToFile mkdirs File " + file.getPath() + " fail");
                return;
            }
        }
        file = new File(fileDic,fileName);

        if(file.exists()) {
            boolean delete = file.delete();
            if(!delete){
                UIHelper.showLog("writeStringToFile delete File " + file.getPath() + " fail");
                return;
            }
        }
        if(!file.exists()){
            try {
                boolean newFile = file.createNewFile();
                if(!newFile){
                    UIHelper.showLog("writeStringToFile newFile File " + file.getPath() + " fail");
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file,false);
            fos.write(text.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
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
//        ConnectivityManager cwjManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        cwjManager.getActiveNetworkInfo();
//        if (cwjManager.getActiveNetworkInfo() != null) {
//            netStatus = cwjManager.getActiveNetworkInfo().isAvailable();
//        }
//        return netStatus;
        //6.0 之后得使用 getApplicationContext()..getSystemService(...)
        //否则会内存泄漏
        ConnectivityManager manager = (ConnectivityManager)context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        return activeNetworkInfo.isConnected();
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        return display.getWidth();
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        return display.getHeight();
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
                        ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
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
            if (activity != null)
                ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(view, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param type     1:into center 0→max, 2,out center max →0
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
            date = new SimpleDateFormat(format).parse(timeStr);
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
            date = new SimpleDateFormat(oldFormat).parse(timeStr);
        } catch (Exception e) {
            date = new Date();
        }
        return new SimpleDateFormat(newFormat).format(date);
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
        return sdf.format(new Date(timestamp * 1000L));
    }

    /**
     * 毫秒 时间戳转换为String
     */
    public static String timeLongHaoMiaoToString(long millis, String timeFromat) {
        SimpleDateFormat sdf = new SimpleDateFormat(timeFromat);
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
    public static Dialog getWaitingDialog(Context context) {
        return new WaitingDialog.Builder(context).create();
    }

    /**
     * 拷贝进剪切板
     */
    public static void copyText(Activity activity, CharSequence text) {
        ClipboardManager clipboard = (ClipboardManager) activity
                .getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setText(text);
        showToastShort(activity, "复制成功");
    }


    public static void waitDevelopment(Activity context) {
        showToastShort(context, "相关功能正在研发中，敬请期待！");
    }

    public static void waitForOpen(Activity context) {
        showToastShort(context,"尚未开放，敬请期待！");
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
            showLog("Exception startRing:"+e);
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
        long[] pattern = {50, 300, 50, 200};
        vibrator.vibrate(pattern, -1);
    }

    public static int getStatusBarHeightByReadR(Context context) {
        Class<?> c ;
        Object obj ;
        Field field ;
        int x ;
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
     * @param newImageFile 新存储到本地的图片文件
     */
    public static void refreshSystemImage(Context context, File newImageFile) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(newImageFile);
            intent.setData(uri);
            context.sendBroadcast(intent);
        } else {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
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
    public static boolean isActivityExistInTask(Context context,Class<?> cls){
        Intent intent = new Intent(context, cls);
        ComponentName cmpName = intent.resolveActivity(context.getPackageManager());
        boolean flag = false;
        if (cmpName != null) { // 说明系统中存在这个activity
            ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
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
        if(content==null){
            content = "";
        }
        content = content.replaceAll("<div><br \\/><\\/div>","");
        while(content.startsWith("<br />")||content.startsWith("<br >")||content.startsWith("<br/>")||content.startsWith("<br>")||content.startsWith("\\n")){
            if(content.startsWith("<br />")){
                content = content.substring("<br />".length(),content.length()).trim();
            }
            if(content.startsWith("<br >")){
                content = content.substring("<br >".length(),content.length()).trim();
            }
            if(content.startsWith("<br/>")){
                content = content.substring("<br/>".length(),content.length()).trim();
            }
            if(content.startsWith("<br>")){
                content = content.substring("<br>".length(),content.length()).trim();
            }
            if(content.startsWith("\\n")){
                content = content.substring("\\n".length(),content.length()).trim();
            }
        }
        while(content.endsWith("<br />")||content.endsWith("<br >")||content.endsWith("<br/>")||content.endsWith("<br>")||content.endsWith("\\n")){
            if(content.endsWith("<br />")){
                content = content.substring(0,content.length()-"<br />".length()).trim();
            }
            if(content.endsWith("<br >")){
                content = content.substring(0,content.length()-"<br >".length()).trim();
            }
            if(content.endsWith("<br/>")){
                content = content.substring(0,content.length()-"<br/>".length()).trim();
            }
            if(content.endsWith("<br>")){
                content = content.substring(0,content.length()-"<br>".length()).trim();
            }
            if(content.endsWith("\\n")){
                content = content.substring(0,content.length()-"\\n".length()).trim();
            }
        }
        return content;
    }

//    public static boolean isMobilePhone(String mobiles){
//        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//        Matcher m = p.matcher(mobiles);
//        return m.matches();
//    }
    public static boolean isMobilePhone(String mobiles){
        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static boolean isEmail(String email){
        String str="^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
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
    private static char getBankCardCheckCode(String nonCheckCodeCardId){
        if(nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for(int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if(j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');
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
//        return Formatter.formatFileSize(context, blockSize * totalBlocks);
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
//        return Formatter.formatFileSize(context, blockSize * availableBlocks);
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
//        return Formatter.formatFileSize(context, blockSize * totalBlocks);
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
//        return Formatter.formatFileSize(context, blockSize * availableBlocks);
    }

    public static long getFileSize(File file){
        long size = 0;
        if(file!=null&&file.exists()){
            if(file.isDirectory()){
                for (File fileItem : file.listFiles()) {
                    size += getFileSize(fileItem);
                }
            }else{
                return file.length();
            }
        }else{
            return 0;
        }
        return size;
    }

    public static Object doMethod(Class<?> classObject,String methodName){
        try {
            Method classMethod = classObject.getDeclaredMethod(methodName);
            classMethod.setAccessible(true);
            return classMethod.invoke(classObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
