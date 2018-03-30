package com.jiujie.jiujie;


public class SimpleText {

    public static void main(String[] args) {
        System.out.println(get(1.23f));
        System.out.println(get(1.56f));
        System.out.println(get(1.67f));
        System.out.println(get(1.11f));
    }

    private static int get(float a){
        return Math.round(a);
    }
}
