package com.jiujie.base.util.html;

import android.content.Context;
import android.content.Intent;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

import com.jiujie.base.activity.WebActivity;
import com.jiujie.base.jk.URLSpanClickListen;


/**
 * @author Created by ChenJiaLiang on 2016/4/27.
 */
public class URLSpan extends ClickableSpan {

    private final String mURL;
    private URLSpanClickListen urlSpanClickListen;

    protected URLSpan(String url,URLSpanClickListen urlSpanClickListen) {
        mURL = url;
        this.urlSpanClickListen = urlSpanClickListen;
    }

    @Override
    public void onClick(View widget) {
        if(urlSpanClickListen!=null){
            urlSpanClickListen.click(mURL);
        }else{
            Context context = widget.getContext();
            Intent intent = new Intent(context,WebActivity.class);
            intent.putExtra("url", mURL);
            intent.putExtra("title", "链接");
            try {
                context.startActivity(intent);
            } catch (Exception e) {
                Log.w("URLSpan", "Exception:" + e);
            }
        }
//				UIHelper.setJumpAnimation(context, 1);
    }
}