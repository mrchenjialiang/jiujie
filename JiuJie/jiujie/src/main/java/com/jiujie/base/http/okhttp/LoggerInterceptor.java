package com.jiujie.base.http.okhttp;

import android.text.TextUtils;

import com.jiujie.base.util.UIHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class LoggerInterceptor implements Interceptor {
    private boolean showResponse;
    private Map<String,List<String>> logMap = new HashMap<>();

    public LoggerInterceptor(boolean showResponse) {
        this.showResponse = showResponse;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        return logForResponse(response);
    }

    private void addLog(String url,String text){
        if(TextUtils.isEmpty(text))return;
        List<String> logStrList;
        if(logMap.containsKey(url)){
            logStrList = logMap.get(url);
        }else{
            logStrList = new ArrayList<>();
            logMap.put(url,logStrList);
        }
        logStrList.add(text);
    }

    private void showLog(String url){
        if(logMap.containsKey(url)){
            List<String> logStrList = logMap.get(url);
            if(logStrList!=null&&logStrList.size()>0){
                UIHelper.showLog("┌─────────────────Http Log Start───────────────────");
//                for (int i = 0;i<logStrList.size();i++){
                for (String text : logStrList){
                    text = "│    " + text;
                    UIHelper.showLog(text);
                }
                UIHelper.showLog("└─────────────────Http Log end  ───────────────────");
                logMap.remove(url);
            }
        }
    }

    private Response logForResponse(Response response) {
        try {
            Response.Builder builder = response.newBuilder();
            Response clone = builder.build();
            String url = clone.request().url().toString();
            addLog(url,"url : " + url);
            addLog(url,"method : " + clone.request().method());
            addLog(url,"code : " + clone.code());
            addLog(url,"protocol : " + clone.protocol());
            if (!TextUtils.isEmpty(clone.message()))
                addLog(url,"message : " + clone.message());

            if (showResponse) {
                ResponseBody body = clone.body();
                if (body != null) {
                    MediaType mediaType = body.contentType();
                    if (mediaType != null) {
                        addLog(url,"responseBody's contentType : " + mediaType.toString());
                        if (isText(mediaType)) {
                            String resp = UIHelper.decode(body.string());
                            if(!TextUtils.isEmpty(resp)){
                                if(resp.length()>2048){
                                    String text = resp;
                                    while (text.length()>2048){
                                        String show = text.substring(0, 2048);
                                        text = text.substring(2048);
                                        addLog(url,"responseBody's content : " + show);
                                    }
                                    addLog(url,"responseBody's content : " + text);
                                }else{
                                    addLog(url,"responseBody's content : " + resp);
                                }

                            }else{
                                addLog(url,"responseBody's content : " + resp);
                            }

                            showLog(url);

                            body = ResponseBody.create(mediaType, resp);
                            return response.newBuilder().body(body).build();
                        } else {
                            addLog(url,"responseBody's content : " + " maybe [file part] , too large too print , ignored!");
                        }
                    }
                }
            }

            showLog(url);
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
        } catch (Exception e) {
            return "something error when show requestBody.";
        }
    }
}
