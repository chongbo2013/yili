package com.lots.travel.network;

import android.support.annotation.Nullable;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;

/**
 * Created by nalanzi on 2017/9/16.
 */

public class JsonBody extends RequestBody{
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");
    private ByteString content;

    public JsonBody(ByteString content){
        this.content = content;
        JsonBody.create(MEDIA_TYPE,content);
    }

    public String getContent(){
        return content.string(Charset.forName("UTF-8"));
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return MEDIA_TYPE;
    }

    @Override
    public long contentLength() {
        return content.size();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
       sink.buffer().write(content);
    }
}
