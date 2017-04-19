package org.kangbiao.weiboReporter.util;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by bradykang on 4/18/2017.
 *
 */
public class JsonUtil {
    private JsonUtil(){}

    private static final Gson gson=new Gson();

    public static <T> T fromJson(String json, Type type){
        return gson.fromJson(json,type);
    }

}
