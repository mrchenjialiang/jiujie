package com.jiujie.jiujie.java;


import com.jiujie.base.util.UIHelper;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class SimpleText {

    public static void main(String[] args) {

        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("1");
        list.remove("1");
        list.remove("0");
        System.out.println(list);


//        File file = new File("C:/Users/Administrator/Desktop/20180531153838972.mp4");
//        long startTime = System.currentTimeMillis();
//        String fileMD5 = getFileMD5(file);
//        long endTime = System.currentTimeMillis();
//        System.out.println("fileMD5:"+fileMD5);
//        System.out.println("fileMD5 耗时:"+(endTime - startTime));
//
//        startTime = System.currentTimeMillis();
//        System.out.println("fileMD5-1:"+ UIHelper.getFileMD5(file));
//        endTime = System.currentTimeMillis();
//        System.out.println("fileMD5-1 耗时:"+(endTime - startTime));



//        List<String> dataList = new ArrayList<>();
//        for (int i = 0;i<100;i++){
//            dataList.add("item");
//        }
//
//        int adJg = 5;
//        int startIndex = 50;
//        int endIndex = 99;
//        for (int i = startIndex;i<dataList.size();i++){
//            if(i%adJg==0){
//                dataList.add(i,"ad");
//            }
//        }
//        for (int i = 0;i<dataList.size();i++){
//            System.out.println("index="+i+"="+dataList.get(i));
//        }


//        List<String> dataList = new ArrayList<>();
//        List<String> list = new ArrayList<>();
//        for (int i = 0;i<100;i++){
//            list.add("item");
//        }
//        int adJg = 5;
//        for (int i = 0;i<list.size();i++){
//            int size = dataList.size();
//            int currentIndex = dataList.size() + 1;
//            boolean isAd;
//            if(size >0&&((dataList.size() + 1) %(adJg+1)==0)){
//                dataList.add("ad");
//                isAd = true;
//            }else{
//                isAd = false;
//                dataList.add(list.get(i));
//            }
//            System.out.println("size:"+size+",currentIndex="+currentIndex+",isAd:"+isAd);
//        }

    }

    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest;
        FileInputStream in;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bytesToHexString(digest.digest());
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}
