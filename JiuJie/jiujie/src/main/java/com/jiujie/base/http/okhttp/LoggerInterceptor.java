package com.jiujie.base.http.okhttp;

import android.text.TextUtils;
import android.util.Log;

import com.jiujie.base.util.UIHelper;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class LoggerInterceptor implements Interceptor {
    public static final String TAG = "OkHttpUtils";
    private String tag;
    private boolean showResponse;

    public LoggerInterceptor(String tag, boolean showResponse) {
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        this.showResponse = showResponse;
        this.tag = tag;
    }

    public LoggerInterceptor(String tag) {
        this(tag, false);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        return logForResponse(response);
    }

    private Response logForResponse(Response response) {
        try {
            //===>response log
            Log.e(tag, "========Http Log Start=======");
            Response.Builder builder = response.newBuilder();
            Response clone = builder.build();
            Log.e(tag, "url : " + clone.request().url());
            Log.e(tag, "method : " + clone.request().method());
            Log.e(tag, "code : " + clone.code());
            Log.e(tag, "protocol : " + clone.protocol());
            if (!TextUtils.isEmpty(clone.message()))
                Log.e(tag, "message : " + clone.message());

            if (showResponse) {
                ResponseBody body = clone.body();
                if (body != null) {
                    MediaType mediaType = body.contentType();
                    if (mediaType != null) {
                        Log.e(tag, "responseBody's contentType : " + mediaType.toString());
                        if (isText(mediaType)) {
                            String resp = UIHelper.decode(body.string());
                            if(!TextUtils.isEmpty(resp)){
                                if(resp.length()>2048){
                                    String text = resp;
                                    while (text.length()>2048){
                                        String show = text.substring(0, 2048);
                                        text = text.substring(2048);
                                        Log.e(tag, "responseBody's content : " + show);
                                    }
                                    Log.e(tag, "responseBody's content : " + text);
                                }else{
                                    Log.e(tag, "responseBody's content : " + resp);
                                }

                            }else{
                                Log.e(tag, "responseBody's content : " + resp);
                            }
                            Log.e(tag, "========Http Log End=======");

                            body = ResponseBody.create(mediaType, resp);
                            return response.newBuilder().body(body).build();
                        } else {
                            Log.e(tag, "responseBody's content : " + " maybe [file part] , too large too print , ignored!");
                        }
                    }
                }
            }

            Log.e(tag, "========Http Log End=======");
        } catch (Exception e) {
//            e.printStackTrace();
        }

        return response;
    }

    private boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        String subtype = mediaType.subtype();
        if (subtype != null) {
            if (subtype.equals("json") ||
                    subtype.equals("xml") ||
                    subtype.equals("html") ||
                    subtype.equals("javascript") ||
                    subtype.equals("x-javascript") ||
                    subtype.equals("webviewhtml")
                    )
                return true;
        }
        return false;
    }

    private String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "something error when show requestBody.";
        }
    }
}
