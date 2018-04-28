package com.jiujie.jiujie.java;


import java.util.ArrayList;
import java.util.List;

public class SimpleText {

    public static void main(String[] args) {

        List<String> dataList = new ArrayList<>();
        List<String> list = new ArrayList<>();
        for (int i = 0;i<100;i++){
            list.add("item");
        }
        int adJg = 6;
        for (int i = 0;i<list.size();i++){
            int size = dataList.size();
            int adSize = dataList.size()/adJg;
            int currentIndex = dataList.size() + 1;
            boolean isAd;
            if(size >0&&(currentIndex %adJg==0)){
                dataList.add("ad");
                isAd = true;
            }else{
                isAd = false;
                dataList.add(list.get(i));
            }
            System.out.println("size:"+size+",currentIndex="+currentIndex+",isAd:"+isAd);
        }

//        for (int i = 0;i<dataList.size();i++){
//            System.out.println(i+"-" + dataList.get(i));
//        }

    }
}
