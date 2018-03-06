package com.jiujie.jiujie;


import com.jiujie.base.util.ImageUtil;
import com.jiujie.base.util.UIHelper;

public class SimpleText {

    public static void main(String[] args) {
        int screenWidth = 1080;
        int screenHeight = 2160;
        int bitmapWidth = 1080;
        int bitmapHeight = 1920;
        System.out.println("doSetWallpaper screenWidth:" + screenWidth);
        System.out.println("doSetWallpaper screenHeight:" + screenHeight);
        System.out.println("doSetWallpaper bitmapWidth:" + bitmapWidth);
        System.out.println("doSetWallpaper bitmapHeight:" + bitmapHeight);

        float scaleWidth = bitmapWidth * 1.0f / screenWidth;
        float scaleHeight = bitmapHeight * 1.0f / screenHeight;
        int scaleResultRequestWidth = screenWidth;
        int scaleResultRequestHeight = screenHeight;
        if(screenWidth!=bitmapWidth||scaleHeight!=bitmapHeight){
            if (scaleWidth <= scaleHeight) {
                scaleResultRequestWidth = screenWidth;
                scaleResultRequestHeight = (int) (bitmapHeight / scaleWidth);
            } else {
                scaleResultRequestWidth = (int) (bitmapWidth/scaleHeight);
                scaleResultRequestHeight = screenHeight;
            }
        }

        System.out.println("doSetWallpaper scale bitmapWidth:" + scaleResultRequestWidth);
        System.out.println("doSetWallpaper scale bitmapHeight:" + scaleResultRequestHeight);

//        if(scaleWidth<1||scaleHeight<1){
//            if (scaleWidth <= scaleHeight) {
//                scaleResultRequestWidth = screenWidth;
//                scaleResultRequestHeight = (int) (bitmapHeight / scaleWidth);
//            } else {
//                scaleResultRequestWidth = (int) (bitmapWidth/scaleHeight);
//                scaleResultRequestHeight = screenHeight;
//            }
//        }else if(scaleWidth>1&&scaleHeight>1){
//            if (scaleWidth <= scaleHeight) {
//                scaleResultRequestWidth = screenWidth;
//                scaleResultRequestHeight = (int) (bitmapHeight / scaleWidth);
//            } else {
//                scaleResultRequestWidth = (int) (bitmapWidth/scaleHeight);
//                scaleResultRequestHeight = screenHeight;
//            }
//        }

//        System.out.println("doSetWallpaper result bitmapWidth:" + bitmapWidth);
//        System.out.println("doSetWallpaper result bitmapHeight:" + bitmapHeight);
    }
}
