package com.achpay.wallet.utils;

import com.achpay.wallet.network.UniteResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Gson类库的封装工具类，专门负责解析json数据</br>
 * 内部实现了Gson对象的单例
 * Created by Dawnton on 2016-08-03.
 */
public class GsonUtil {

    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    private GsonUtil() {

    }

    /**
     * 将对象转换成json格式
     *
     * @param ts
     * @return
     */
    public static String objectToJson(Object ts) {
        try {
            String jsonStr = null;
            if (gson != null) {
                jsonStr = gson.toJson(ts);
            } else {
                gson = new Gson();
                jsonStr = gson.toJson(ts);
            }
            return jsonStr;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 将对象转换成json格式(并自定义日期格式)
     *
     * @param ts
     * @return
     */
    public static String objectToJsonDateSerializer(Object ts,
                                                    final String dateformat) {
        String jsonStr = null;
        gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Date.class,
                        new JsonSerializer<Date>() {
                            public JsonElement serialize(Date src,
                                                         Type typeOfSrc,
                                                         JsonSerializationContext context) {
                                SimpleDateFormat format = new SimpleDateFormat(
                                        dateformat);
                                return new JsonPrimitive(format.format(src));
                            }
                        }).setDateFormat(dateformat).create();
        if (gson != null) {
            jsonStr = gson.toJson(ts);
        }
        return jsonStr;
    }

    /**
     * 将json格式转换成list对象
     *
     * @param jsonStr
     * @return
     */
    public static List<?> jsonToList(String jsonStr) {
        List<?> objList = null;
        if (gson != null) {
            Type type = new com.google.gson.reflect.TypeToken<List<?>>() {
            }.getType();
            objList = gson.fromJson(jsonStr, type);
        } else {
            gson = new Gson();
            Type type = new com.google.gson.reflect.TypeToken<List<?>>() {
            }.getType();
            objList = gson.fromJson(jsonStr, type);
        }
        return objList;
    }

    /**
     * 将json格式转换成list对象，并准确指定类型
     *
     * @param jsonStr
     * @param type    格式: Type type = new TypeToken<List<String>>(){}.getType();
     * @return
     */
    public static List<?> jsonToList(String jsonStr, Type type) {
        List<?> objList = null;
        if (gson != null) {
            objList = gson.fromJson(jsonStr, type);
        } else {
            gson = new Gson();
            objList = gson.fromJson(jsonStr, type);
        }
        return objList;
    }

    /**
     * 将json格式转换成map对象
     *
     * @param jsonStr
     * @return
     */
    public static Map<?, ?> jsonToMap(String jsonStr) {
        try {
            Map<?, ?> objMap = null;
            if (gson != null) {
                Type type = new com.google.gson.reflect.TypeToken<Map<?, ?>>() {
                }.getType();
                objMap = gson.fromJson(jsonStr, type);
            } else {
                gson = new Gson();
                Type type = new com.google.gson.reflect.TypeToken<Map<?, ?>>() {
                }.getType();
                objMap = gson.fromJson(jsonStr, type);
            }
            return objMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将json转换成bean对象
     *
     * @param jsonStr
     * @return
     */
    public static Object jsonToBean(String jsonStr, Class<?> cl) {
        try {
            Object obj = null;
            if (gson != null) {
                obj = gson.fromJson(jsonStr, cl);
            } else {
                gson = new Gson();
                obj = gson.fromJson(jsonStr, cl);
            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static UniteResponse jsonToResponse(String json, Class clazz) {
        Gson gson = new Gson();
        Type objectType = type(UniteResponse.class, clazz);
        return gson.fromJson(json, objectType);
    }

    static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }

    /**
     * 将json转换成bean对象
     *
     * @param jsonStr
     * @param cl
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T jsonToBeanDateSerializer(String jsonStr, Class<T> cl,
                                                 final String pattern) {
        Object obj = null;
        gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    public Date deserialize(JsonElement json, Type typeOfT,
                                            JsonDeserializationContext context)
                            throws JsonParseException {
                        SimpleDateFormat format = new SimpleDateFormat(pattern);
                        String dateStr = json.getAsString();
                        try {
                            return format.parse(dateStr);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }).setDateFormat(pattern).create();
        if (gson != null) {
            obj = gson.fromJson(jsonStr, cl);
        }
        return (T) obj;
    }

    /**
     * 根据
     *
     * @param jsonStr
     * @param key
     * @return
     */
    public static Object getJsonValue(String jsonStr, String key) {
        try {
            Object rulsObj = null;
            Map<?, ?> rulsMap = jsonToMap(jsonStr);
            if (rulsMap != null && rulsMap.size() > 0) {
                rulsObj = rulsMap.get(key);
            }
            return rulsObj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将包含unicode编码的json转换为正常的json
     *
     * @param json json字符串
     * @return utf-8 json字符串
     */
    public static String decodeUnicode(String json) {
        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;

        while ((i = json.indexOf("\\u", pos)) != -1) {
            sb.append(json.substring(pos, i));
            if (i + 5 < json.length()) {
                pos = i + 6;
                sb.append((char) Integer.parseInt(
                        json.substring(i + 2, i + 6), 16));
            }
        }

        if (pos > 0) {
            sb.append(json.substring(pos));
            return sb.toString();
        } else {
            return json;
        }
    }
}
