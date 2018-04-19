package com.jiujie.jiujie.java;


public class SimpleText {

    public static void main(String[] args) {

        int recommendSize = 10;
        int recommendPageSize = recommendSize / 8 + (recommendSize % 8 == 0 ? 0 : 1);

        System.out.println("recommendPageSize:" + recommendPageSize);
        int pageSize = 0;
        for (int i = 0; i < recommendPageSize; i++) {
            int pageInsideSize = 0;
            for (int j = 0; j < 8; j++) {
                int size = i * 8 + j;
                System.out.println("size:" + size);
                if (size <= recommendSize - 1) {
                    pageInsideSize++;
                } else {
                    break;
                }
            }
            System.out.println(i + "pageInsideSize:" + pageInsideSize);
            pageSize++;
        }
        System.out.println("pageSize:" + pageSize);

    }
}
