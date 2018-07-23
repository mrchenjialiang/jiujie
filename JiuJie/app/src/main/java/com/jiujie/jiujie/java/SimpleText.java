package com.jiujie.jiujie.java;


import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class SimpleText {

    public static void main(String[] args) {

        List<Float> list = new ArrayList<>();
        list.add(0.41f);//1
        list.add(0.3f);//2
        list.add(0.25f);//3
        list.add(0.22f);//4
        list.add(0.20f);//5
        list.add(0.18f);//6
        list.add(0.16f);//7
        list.add(0.14f);//8
        list.add(0.12f);//9
        list.add(0.11f);//10
        list.add(0.11f);//11
        list.add(0.10f);//12
        list.add(0.10f);//13
        list.add(0.09f);//14
        list.add(0.085f);//15
        list.add(0.083f);//16
        list.add(0.081f);//17
        list.add(0.078f);//18
        list.add(0.075f);//19
        list.add(0.074f);//20
        list.add(0.073f);//21
        list.add(0.071f);//22
        list.add(0.070f);//23
        list.add(0.068f);//24
        list.add(0.067f);//25
        list.add(0.066f);//26
        list.add(0.063f);//27
        list.add(0.060f);//28
        list.add(0.056f);//29
        list.add(0.055f);//30

        int addNum = 21000;
        int resultNum = 0;
        int passDay = 365*10;
        List<Integer> leftNumList = new ArrayList<>();//每天的存活用户
        for (int i = 0; i < passDay; i++) {
            leftNumList.add(0);
        }
        for (int i = 0; i < passDay; i++) {
            if (i > list.size() - 1) {
                Float liuCun = list.get(list.size() - 1);
                //超过一个月后，留存大于0.01时，每3天减少千分之一的留存
                if (liuCun > 0.01) {
                    if(i % 3 == 0){
                        liuCun = liuCun - 0.001f;
                    }
                }else if (liuCun > 0.001) {
                    //超过一个月后，留存大于0.001时，每3天减少万分之一的留存
                    if(i % 10 == 0){
                        liuCun = liuCun - 0.0001f;
                    }
                }
                list.add(liuCun);
            }
            if(i==150){
                System.out.println("5个月后留存:" + list.get(i));
            }
            leftNumList.set(i, (int) (addNum * list.get(i)));
        }
        for (int i = 0; i < passDay; i++) {
            resultNum += leftNumList.get(i);
        }
        System.out.println("resultNum:" + resultNum);


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
