package com.jiujie.base.util.html;

import android.app.Activity;
import android.content.Intent;
import android.text.style.ClickableSpan;
import android.view.View;

import com.jiujie.base.activity.ImageViewPagerActivity;
import com.jiujie.base.util.UIHelper;

import java.util.ArrayList;

/**
 * Created by ChenJiaLiang on 2016/6/1.
 * Email : 576507648@qq.com
 */
public class URLImageSpan extends ClickableSpan {
    private final ArrayList<String> picList;
    private String url;
    private Activity mActivity;
    protected URLImageSpan(Activity mActivity,String url,ArrayList<String> picList) {
        this.mActivity = mActivity;
        this.url =url;
        this.picList = picList;
    }

    @Override
    public void onClick(View widget) {
        Intent intent = new Intent(mActivity,ImageViewPagerActivity.class);
        intent.putExtra("url", url);
        intent.putStringArrayListExtra("urlList", picList);
        mActivity.startActivity(intent);
        UIHelper.setJumpAnimation(mActivity, 1);
    }
}
