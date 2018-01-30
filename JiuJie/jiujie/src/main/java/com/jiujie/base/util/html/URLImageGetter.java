package com.jiujie.base.util.html;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.TextUtils;

import com.jiujie.base.APP;
import com.jiujie.base.util.ImageUtil;
import com.jiujie.base.util.UIHelper;

import java.util.List;

/**
 * @author Created by ChenJiaLiang on 2016/6/1.
 *         Email : 576507648@qq.com
 */
public class URLImageGetter implements Html.ImageGetter {
    private final List<String> picList;
    private final Activity activity;

    protected URLImageGetter(Activity activity, List<String> picList) {
        this.activity = activity;
        this.picList = picList;
    }

    @Override
    public Drawable getDrawable(String source) {
        if (TextUtils.isEmpty(source))
            return null;
        if (!TextUtils.isEmpty(source) && !source.startsWith("http")) {
            if (!TextUtils.isEmpty(APP.defaultDoMain)) {
                source = APP.defaultDoMain + source;
            }
        }
        if (picList != null && !picList.contains(source)) picList.add(source);
        Drawable drawable = null;
        try {
            drawable = ImageUtil.instance().getImageDrawableFromNet(activity, source);
        } catch (Exception e) {
            e.printStackTrace();
            if (APP.isDeBug) {
                UIHelper.showLog("Exception:" + e);
                UIHelper.showToastLong(activity, "Exception:" + e);
            } else {
                UIHelper.showLog("Exception:" + e);
            }
        }
        return drawable;
    }
}
