package com.jiujie.base.http;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * 监听上传进度的RequestBody
 * Created by ChenJiaLiang on 2018/5/31.
 * Email:576507648@qq.com
 */

public abstract class ProgressRequestBody extends RequestBody {

    private final RequestBody requestBody;
    private BufferedSink bufferedSink;

    public ProgressRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
            bufferedSink = Okio.buffer(sink(sink));
        }
        requestBody.writeTo(bufferedSink);
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();
    }

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            private long current;
            private long total;
            private float lastProgress = 0;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (total == 0) {
                    total = contentLength();
                }
                current += byteCount;
                float progress = current * 1000 * 1.0f / total / 10;
                if (lastProgress < progress) {
                    onProgress(total, current, progress);
                    lastProgress = progress;
                }
            }
        };
    }

    public abstract void onProgress(long total,long current,float progress);
}
