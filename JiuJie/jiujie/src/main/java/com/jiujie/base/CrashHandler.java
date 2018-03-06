package com.jiujie.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.jiujie.base.jk.OnListener;
import com.jiujie.base.util.FileUtil;
import com.jiujie.base.util.UIHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/** 
* UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告. 
*  
* @author user 
*  
*/  
@SuppressLint("SimpleDateFormat")
public class CrashHandler implements UncaughtExceptionHandler {  
    
  public static final String TAG = "CrashHandler";  
    
  //系统默认的UncaughtException处理类   
  private UncaughtExceptionHandler mDefaultHandler;
  //CrashHandler实例  
  private static CrashHandler INSTANCE = new CrashHandler();  
  //程序的Context对象  
  private Context mContext;  
  //用来存储设备信息和异常信息  
  private Map<String, String> infos = new HashMap<>();

  //用于格式化日期,作为日志文件名的一部分  
  private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

  /** 保证只有一个CrashHandler实例 */  
  private CrashHandler() {
  }  

  /** 获取CrashHandler实例 ,单例模式 */  
  public static CrashHandler getInstance() {  
      return INSTANCE;  
  }  

  /** 
   * 初始化 
   */
  public void init(Context context) {  
      mContext = context;  
      //获取系统默认的UncaughtException处理器  
      mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();  
      //设置该CrashHandler为程序的默认处理器  
      Thread.setDefaultUncaughtExceptionHandler(this);  
  }  

  /** 
   * 当UncaughtException发生时会转入该函数来处理 
   */  
  @Override  
  public void uncaughtException(Thread thread, Throwable ex) {  
      if (!handleException(ex) && mDefaultHandler != null) {  
          //如果用户没有处理则让系统默认的异常处理器来处理  
          mDefaultHandler.uncaughtException(thread, ex);  
      } else {  
          try {  
              Thread.sleep(3000);  
          } catch (InterruptedException e) {  
              Log.e(TAG, "error : ", e);  
          }  
          //退出程序  
          android.os.Process.killProcess(android.os.Process.myPid());  
          System.exit(1);  
      }  
  }  

  /** 
   * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 
   * @return true:如果处理了该异常信息;否则返回false.
   */  
  private boolean handleException(final Throwable ex) {  
      if (ex == null) {  
          return false;  
      }  
      //使用Toast来显示异常信息  
      new Thread() {  
          @Override  
          public void run() {  
              Looper.prepare();  
              Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();  
              UIHelper.showLog("msg:" + ex.getMessage() + "\n原因:" + ex.getCause());
              Looper.loop();  
          }  
      }.start();  
      //收集设备参数信息   
      collectDeviceInfo(mContext);  
      //保存日志文件   
      saveCrashInfo2File(ex);  
      return true;  
  }  
    
  /** 
   * 收集设备参数信息 
   */
  public void collectDeviceInfo(Context ctx) {  
      try {  
          PackageManager pm = ctx.getPackageManager();  
          PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);  
          if (pi != null) {  
              String versionName = pi.versionName == null ? "null" : pi.versionName;  
              String versionCode = pi.versionCode + "";  
              infos.put("versionName", versionName);  
              infos.put("versionCode", versionCode);  
          }  
      } catch (NameNotFoundException e) {
          Log.e(TAG, "an error occured when collect package info", e);  
      }  
      Field[] fields = Build.class.getDeclaredFields();  
      for (Field field : fields) {
          try {
              field.setAccessible(true);  
              infos.put(field.getName(), field.get(null).toString());  
//              Log.d(TAG, field.getName() + " : " + field.get(null));  //日志里不显示设备信息
          } catch (Exception e) {  
              Log.e(TAG, "an error occured when collect crash info", e);  
          }
      }  
  }  

  /** 
   * 保存错误信息到文件中 
   */
  private void saveCrashInfo2File(Throwable ex) {
      if (Build.VERSION.SDK_INT >= 23) {
          int checkCallPhonePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
          if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
             return;
          }
      }
      final StringBuilder sb = new StringBuilder();
      for (Map.Entry<String, String> entry : infos.entrySet()) {  
          String key = entry.getKey();  
          String value = entry.getValue();  
          sb.append(key).append("=").append(value).append("\n");
      }
        
      Writer writer = new StringWriter();  
      PrintWriter printWriter = new PrintWriter(writer);  
      ex.printStackTrace(printWriter);
      Throwable cause = ex.getCause();  
      while (cause != null) {
          cause.printStackTrace(printWriter);  
          cause = cause.getCause();  
      }  
      printWriter.close();  
      String result = writer.toString();  
      sb.append(result);
      UIHelper.showLog(result);
      if (UIHelper.isSdCardExist()) {
          FileUtil.requestPermission(new OnListener<Boolean>() {
              @Override
              public void onListen(Boolean isHas) {
                  if(isHas){
                      try {
                          String time = formatter.format(new Date());
                          String fileName = "crash_" + time + ".txt";

                          File file = FileUtil.createLogFile(mContext, fileName);
                          if(file==null||!file.exists()){
                              return;
                          }
                          FileOutputStream fos = new FileOutputStream(file,true);
                          sb.append(UIHelper.timeLongHaoMiaoToString(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss")).append("\n===END===\n");
                          fos.write(sb.toString().getBytes());
                          fos.close();
                      } catch (Exception e) {
                          Log.e(TAG, "an error happened while writing file", e);
                      }
                  }
              }
          });
      }
  }
}  