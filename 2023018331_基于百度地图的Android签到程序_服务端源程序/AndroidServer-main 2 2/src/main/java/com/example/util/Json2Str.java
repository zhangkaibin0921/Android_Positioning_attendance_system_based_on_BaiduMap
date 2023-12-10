package com.example.util;
import com.google.gson.Gson;
public class Json2Str {
    public static Gson gson = new Gson();

    public static String toJsonString(Object object, Class<?> objectClass) {
        return gson.toJson(object, objectClass);
    }
    public static <T> T fromJsonString(String jsonString, Class<T> objectClass) {
        return gson.fromJson(jsonString, objectClass);}
}


