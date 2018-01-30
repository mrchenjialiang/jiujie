package com.jiujie.base.util.html;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.jiujie.base.jk.OnTextCallbackListen;
import com.jiujie.base.jk.URLSpanClickListen;
import com.jiujie.base.util.MyLinkedHashMap;

import java.util.ArrayList;

/**
 * Created by ChenJiaLiang on 2016/6/1.
 * Email : 576507648@qq.com
 */
public class HtmlTextUtil {

    private static MyLinkedHashMap<String, SpannableStringBuilder> dataMap = new MyLinkedHashMap<>();
    private static final int minImageWidth = 30;
    private static final int bigMoreToScreenWidth = 360;
    public static final int ScreenWidth = 0;//确定要撑满
    public static final int AutoWidth = 1;//原图多大就多大
    public static final int AiAutoWidth = 2;//如果原图大于某个尺寸就撑满，如果小于某个尺寸就固定30，其他情况原图多大就多大。

    public static void clearCache(){
        if(dataMap!=null){
            dataMap.clear();
        }
    }

    public static void setText(final Activity activity,final String text,final TextView textView, final boolean isSetScreenWidth,String key,final URLSpanClickListen urlClickListen) {
        setText(activity, text, textView, isSetScreenWidth,key,urlClickListen,null);
    }

    public static void setText(final Activity activity,final String text,final TextView textView, int imageWidthType,String key,final URLSpanClickListen urlClickListen) {
        setText(activity, text, textView, imageWidthType,key,urlClickListen,null);
    }

    public static void setText(final Activity activity,final String text,final TextView textView, int imageWidthType,final URLSpanClickListen urlClickListen) {
        setText(activity, text, textView, imageWidthType,null,urlClickListen,null);
    }

    public static void setText(final Activity activity,final String text,final TextView textView, int imageWidthType) {
        setText(activity, text, textView, imageWidthType,null,null,null);
    }

    public static void setText(final Activity activity,final String text,final TextView textView, final boolean isSetScreenWidth,String key,final URLSpanClickListen urlClickListen, final OnTextCallbackListen onTextCallbackListen) {
        if(isSetScreenWidth){
            setText(activity, text, textView, ScreenWidth,key,urlClickListen,onTextCallbackListen);
        }else{
            setText(activity, text, textView, AiAutoWidth,key,urlClickListen,onTextCallbackListen);
        }
    }

    public static void setText(final Activity activity,final String text,final TextView textView, int imageWidthType,String key,final URLSpanClickListen urlClickListen, final OnTextCallbackListen onTextCallbackListen) {
        int imageMaxWidth = textView.getWidth()-textView.getPaddingLeft()-textView.getPaddingRight();
        textView.setText(Html.fromHtml(text));
        textView.setMovementMethod(LinkMovementMethod.getInstance());//设置Span可点击
        getText(activity, text, key, imageMaxWidth,imageWidthType, urlClickListen, new OnTextCallbackListen() {
            @Override
            public void callback(CharSequence text) {
                textView.setText(text);
                textView.requestLayout();
                if (onTextCallbackListen != null) {
                    onTextCallbackListen.callback(text);
                }
            }
        });
    }

    /**
     * textView.setMovementMethod(LinkMovementMethod.getInstance());//设置Span可点击
     */
    public static void getText(final Activity activity,final String text,String key, final int imageMaxWidth,final URLSpanClickListen urlClickListen, final OnTextCallbackListen onTextCallbackListen) {
        getText(activity, text, key, imageMaxWidth, AutoWidth, urlClickListen, onTextCallbackListen);
    }

    /**
     * textView.setMovementMethod(LinkMovementMethod.getInstance());//设置Span可点击
     */
    public static void getText(final Activity activity,final String text,String key, final int imageMaxWidth,final int imageWidthType,final URLSpanClickListen urlClickListen, final OnTextCallbackListen onTextCallbackListen) {
        if(TextUtils.isEmpty(text))return;
        dataMap.setMaxSize(50);

        if(TextUtils.isEmpty(key)){
            int length = text.length();
            if(length <10){
                key = text;
            }else{
                key = text.substring(0,2)+
                        text.substring(length/5,length/5+2)+
                        text.substring(length*2/5,length*2/5+2)+
                        text.substring(length*3/5,length*3/5+2)+
                        text.substring(length*4/5,length*4/5+2);
            }
        }
        if (dataMap.containsKey(key)) {
            if(onTextCallbackListen!=null){
                onTextCallbackListen.callback(dataMap.get(key));
            }
            return;
        }

        final ArrayList<String> picList = new ArrayList<>();

        new AsyncTask<String, Void, Spanned>() {
            @Override
            protected Spanned doInBackground(String... params) {
                return Html.fromHtml(text, new URLImageGetter(activity, picList), null);
            }
            @Override
            protected void onPostExecute(Spanned spanned) {
                super.onPostExecute(spanned);
                CharSequence charSequence = setSpanAction(activity, spanned, picList, imageMaxWidth,imageWidthType, urlClickListen);
                if(onTextCallbackListen!=null){
                    onTextCallbackListen.callback(charSequence);
                }
            }
        }.execute("");
    }

    private static CharSequence setSpanAction(Activity activity, CharSequence text, ArrayList<String> picList, int imageMaxWidth, int imageWidthType, URLSpanClickListen urlClickListen){
        if (text instanceof Spannable) {
            int end = text.length();
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            ImageSpan[] images = style.getSpans(0, end, ImageSpan.class);
            android.text.style.URLSpan[] urls = style.getSpans(0, end, android.text.style.URLSpan.class);
            for (ImageSpan image : images) {
                setImageSpanAction(activity,style,image,picList,imageMaxWidth,imageWidthType);
            }
            for (android.text.style.URLSpan url : urls) {
                URLSpan urlSpan = new URLSpan(url.getURL(),urlClickListen);
                style.setSpan(urlSpan, style.getSpanStart(url), style.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                style.removeSpan(url);
            }
            return style;
        }
        return text;
    }

    private static void setImageSpanAction(Activity activity, SpannableStringBuilder style, ImageSpan image, ArrayList<String> picList, int imageMaxWidth, int imageWidthType){
        if(image==null)return;
        Drawable drawable = image.getDrawable();
        if(drawable==null)return;

        int width = drawable.getIntrinsicWidth();
        int height;
        if(imageWidthType==AiAutoWidth){
            if(width<minImageWidth){
                width = minImageWidth;
            }else if(width>=bigMoreToScreenWidth){
                width = imageMaxWidth;
            }
        }else if(imageWidthType==ScreenWidth){
            width = imageMaxWidth;
        }
        if(width==drawable.getIntrinsicWidth()){
            height = drawable.getIntrinsicHeight();
        }else{
            height = (int)((float)width*drawable.getIntrinsicHeight()/ drawable.getIntrinsicWidth());
        }
        drawable.setBounds(0, 0, width, height);

//            if(isSetScreenWidth){
//                int imageMaxWidth = textView.getWidth()-textView.getPaddingLeft()-textView.getPaddingRight();
//                int height = (int)((float)imageMaxWidth*drawable.getIntrinsicHeight()/drawable.getIntrinsicWidth());
//                drawable.setBounds(0, 0, imageMaxWidth, height);
//            }else{
//                if(drawable.getIntrinsicWidth()<30){
//                    int height = (int)((float)30*drawable.getIntrinsicHeight()/drawable.getIntrinsicWidth());
//                    drawable.setBounds(0, 0, 30, height);
//                }else{
//                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//                }
//            }

        ImageSpan imageSpan = new ImageSpan(drawable);
        style.setSpan(imageSpan, style.getSpanStart(image), style.getSpanEnd(image), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        URLImageSpan myImageSpan = new URLImageSpan(activity,image.getSource(),picList);
        style.setSpan(myImageSpan, style.getSpanStart(image), style.getSpanEnd(image), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        style.removeSpan(image);
    }

    public static void setText(final Activity activity,final String text,final TextView textView,String key,URLSpanClickListen urlClickListen){
        setText(activity, text, textView, false, key, urlClickListen);
    }
    public static void setText(final Activity activity,final String text,final TextView textView,String key){
        setText(activity, text, textView, false, key, null);
    }

    /**
     * @param textView must not setTag outsize
     */
    public static void setText(final Activity activity,final String text,final TextView textView,URLSpanClickListen urlClickListen){
        if(TextUtils.isEmpty(text)){
            if(textView!=null)textView.setText(text);
            return;
        }
        setText(activity, text, textView, false, null,urlClickListen);
    }
    public static void setText(final Activity activity,final String text,final TextView textView){
        if(TextUtils.isEmpty(text)){
            if(textView!=null)textView.setText(text);
            return;
        }
        setText(activity, text, textView, false, null,null);
    }

    public static void setText(final Activity activity,final String text,TextView textView ,OnTextCallbackListen onTextCallbackListen){
        if(textView==null)textView = new TextView(activity);
        if(TextUtils.isEmpty(text)){
            textView.setText(text);
            return;
        }
        setText(activity, text, textView, false, null,null,onTextCallbackListen);
    }

}
