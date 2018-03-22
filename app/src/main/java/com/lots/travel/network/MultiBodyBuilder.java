package com.lots.travel.network;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 *
 * @Multipart
 * @Part() List<MultipartBody.Part> parts
 */
public class MultiBodyBuilder {
    private List<MultipartBody.Part> parts;

    private MultiBodyBuilder(){
        parts = new ArrayList<>();
    }

    public static MultiBodyBuilder create(){
        return new MultiBodyBuilder();
    }

    public MultiBodyBuilder addText(String key,String value){
        MultipartBody.Part part = MultipartBody.Part.createFormData(key,value);
        parts.add(part);
        return this;
    }

    public MultiBodyBuilder addFile(String key, String filename, File file){
        RequestBody body = RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData(key,filename,body);
        parts.add(part);
        return this;
    }

    public List<MultipartBody.Part> build(){
        return parts;
    }

}
