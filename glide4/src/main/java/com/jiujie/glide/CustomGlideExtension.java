//package com.jiujie.glide;
//
//import android.content.Context;
//
//import com.bumptech.glide.annotation.GlideExtension;
//import com.bumptech.glide.annotation.GlideOption;
//import com.bumptech.glide.load.resource.bitmap.CenterCrop;
//import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
//import com.bumptech.glide.request.RequestOptions;
//
///**
// * 使用GlideApp时才需要这个，为了简写api，把RequestOptions options的配置写到这里，再使用GlideApp.***的方式调用
// * Created by ChenJiaLiang on 2018/4/8.
// * Email:576507648@qq.com
// */
//@GlideExtension
//public class CustomGlideExtension {
//
//    private static int normalDefaultId = R.drawable.glide4_default;
//    private static int circleDefaultId = R.drawable.glide4_default;
//    private static int connerDefaultId = R.drawable.glide4_default;
//
//    public static void setNormalDefaultId(int normalDefaultId){
//        CustomGlideExtension.normalDefaultId = normalDefaultId;
//    }
//
//    public void setCircleDefaultId(int circleDefaultId) {
//        CustomGlideExtension.circleDefaultId = circleDefaultId;
//    }
//
//    public void setConnerDefaultId(int connerDefaultId) {
//        CustomGlideExtension.connerDefaultId = connerDefaultId;
//    }
//
//    /**
//     * 将构造方法设为私有，作为工具类使用
//     */
//    private CustomGlideExtension() {
//    }
//
//    /**
//     * 1.自己新增的方法的第一个参数必须是 RequestOptions options
//     * 2.方法必须是静态的
//     */
//    @GlideOption
//    public static void asDefault(RequestOptions options) {
//        options
//                .placeholder(normalDefaultId)
//                .centerCrop();
////                .transition(DrawableTransitionOptions.withCrossFade())
////                .override(MINI_THUMB_SIZE);
//    }
//
//    public static void asCircle(RequestOptions options, Context context){
//        options
//                .placeholder(circleDefaultId)
//                .centerCrop()
//                .transform(new GlideCircleTransform(context));
//    }
//
//    public static void asConner(RequestOptions options, Context context,int dp){
//        options
//                .placeholder(circleDefaultId)
//                .centerCrop()
//                .transform(new GlideRoundTransform(context, dp));
//    }
//
//
////    Glide对ImageView的width和height属性是这样解析的：
////    如果width和height都大于0，则使用layout中的尺寸。
////    如果width和height都是WRAP_CONTENT，则使用屏幕尺寸。
////    如果width和height中至少有一个值<=0并且不是WRAP_CONTENT，那么就会在布局的时候添加一个OnPreDrawListener监听ImageView的尺寸
////    Glide对WRAP_CONTENT的支持并不好，所以尽量不要用。
//}
