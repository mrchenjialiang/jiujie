package com.jiujie.base.util;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by ChenJiaLiang on 2018/5/7.
 * Email:576507648@qq.com
 */
class LoggerUtil {
    private static final String defaultKey = "LOG";
    private static List<String> logList = new ArrayList<>();
    private static boolean isLogging;

    static void addLog(Object object) {
        addLog(defaultKey,object);
    }

    static void addLog(Object... objects) {
        if(objects==null){
            addLog("null");
        }else{
            List<String> list = new ArrayList<>();
            for (Object o:objects){
                list.add(o.toString());
            }
            addLog(list);
        }
    }

    /**
     * object 不使用数组
     */
    static void addLog(Object key,Object object) {
        String keyStr;
        if(key==null){
            keyStr = defaultKey;
        }else{
            if(key instanceof String){
                keyStr = (String) key;
            }else{
                keyStr = key.getClass().getSimpleName();
            }
        }
        if(object==null){
            addLog(keyStr,"unKnowText");
        }else{
            if(object instanceof List){
                List list = (List) object;
                for (Object o:list){
                    addLog(keyStr,o.toString());
                }
            }else if(object instanceof String){
                addLog(keyStr,(String) object);
            }else if(object.getClass().isArray()){
                if(object instanceof int[]){
                    int[] array = (int[]) object;
                    addLog(keyStr,Arrays.toString(array));
                }else if(object instanceof boolean[]){
                    boolean[] array = (boolean[]) object;
                    addLog(keyStr,Arrays.toString(array));
                }else if(object instanceof byte[]){
                    byte[] array = (byte[]) object;
                    addLog(keyStr,Arrays.toString(array));
                }else if(object instanceof char[]){
                    char[] array = (char[]) object;
                    addLog(keyStr,Arrays.toString(array));
                }else if(object instanceof double[]){
                    double[] array = (double[]) object;
                    addLog(keyStr,Arrays.toString(array));
                }else if(object instanceof float[]){
                    float[] array = (float[]) object;
                    addLog(keyStr,Arrays.toString(array));
                }else if(object instanceof long[]){
                    long[] array = (long[]) object;
                    addLog(keyStr,Arrays.toString(array));
                }else if(object instanceof short[]){
                    short[] array = (short[]) object;
                    addLog(keyStr,Arrays.toString(array));
                }else if(object instanceof Object[]){
                    Object[] array = (Object[]) object;
                    addLog(keyStr,Arrays.toString(array));
                }else{
                    addLog(keyStr,object.toString());
                }
            }else{
                addLog(keyStr,object.toString());
            }
        }
        showLog();
    }

    private static void addLog(String key, String text){
        if(TextUtils.isEmpty(key)){
            key = "unKnowKey";
        }
        if(TextUtils.isEmpty(text)){
            text = "unKnowText";
        }
        logList.add(key+","+text);
    }

    private static void showLog(){
        if(logList==null||logList.size()==0){
            return;
        }
        if(isLogging){
            return;
        }
        while (logList.size()!=0){
            isLogging = true;
            String text = logList.get(0);
            if(text.contains(",")){
                int index = text.indexOf(",");
                String key = text.substring(0, index);
                String value = text.substring(index+1);
                Log.e(key,value);
            }
            logList.remove(0);
        }
        isLogging = false;
    }
}
