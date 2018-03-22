package com.lots.travel.network;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SosonaGsonConverterFactory extends Converter.Factory {
    private final Gson gson;

    private SosonaGsonConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    public static SosonaGsonConverterFactory create() {
        return create(new Gson());
    }

    public static SosonaGsonConverterFactory create(Gson gson) {
        return new SosonaGsonConverterFactory(gson);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new SosonaGsonResponseBodyConverter<>(gson, adapter);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new SosonaGsonRequestBodyConverter<>(gson, adapter);
    }

    private class SosonaGsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
        private final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
        private final Charset UTF_8 = Charset.forName("UTF-8");

        private final Gson gson;
        private final TypeAdapter<T> adapter;

        SosonaGsonRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
            this.gson = gson;
            this.adapter = adapter;
        }

        @Override public RequestBody convert(T value) throws IOException {
            Buffer buffer = new Buffer();
            Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
            JsonWriter jsonWriter = gson.newJsonWriter(writer);
            adapter.write(jsonWriter, value);
            jsonWriter.close();
            return new JsonBody(buffer.readByteString());
        }
    }

    private class SosonaGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private final Charset UTF_8 = Charset.forName("UTF-8");
        private final Gson gson;
        private final TypeAdapter<T> adapter;

        SosonaGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
            this.gson = gson;
            this.adapter = adapter;
        }

        @Override
        public T convert(ResponseBody value) throws IOException {
            String response = value.string();
            HttpResult result = gson.fromJson(response, HttpResult.class);
            if (result.getCode()!=0) {
                value.close();
                throw new ApiException(result.getCode(),result.getErrorMsg());
            }

            MediaType mediaType = value.contentType();
            Charset charset = mediaType == null ? UTF_8:mediaType.charset(UTF_8);
            ByteArrayInputStream bis = new ByteArrayInputStream(response.getBytes());
            InputStreamReader reader = new InputStreamReader(bis,charset);
            JsonReader jsonReader = gson.newJsonReader(reader);
            try {
                return adapter.read(jsonReader);
            } finally {
                value.close();
            }
        }
    }

}
