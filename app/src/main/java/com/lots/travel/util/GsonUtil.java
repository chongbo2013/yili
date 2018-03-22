package com.lots.travel.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by nalanzi on 2017/10/21.
 */

public class GsonUtil {

    public static <T> List<T> array(String json, Class<T[]> clazz) {
        Gson gson = new Gson();
        T[] array = gson.fromJson(json, clazz);
        return Arrays.asList(array);
    }

    public static <T> ArrayList<T> list(String json, Class<T> clazz) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<JsonObject>>(){}.getType();
        ArrayList<JsonObject> jsonObjects = gson.fromJson(json, type);

        ArrayList<T> arrayList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects) {
            arrayList.add(gson.fromJson(jsonObject, clazz));
        }
        return arrayList;
    }

}
