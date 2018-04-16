package com.jiujie.jiujie.java;


public class SimpleText {

    public static void main(String[] args) {


        String text = ImageUrl.text;
        String[] split = text.split("==");
        for (String str : split){
            if(str.contains("!!")){
                String substring = str.substring(str.indexOf("!!") + 2);
                System.out.println("\""+substring+"\",");
            }
        }
    }
}
