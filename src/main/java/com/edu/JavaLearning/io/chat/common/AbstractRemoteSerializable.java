package com.edu.JavaLearning.io.chat.common;


import com.alibaba.fastjson.JSON;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;

import java.nio.charset.Charset;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/18 4:36 PM
 **/
public abstract class AbstractRemoteSerializable {

    private final static Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    public static byte[] encode(final Object object) {
        String json = toJson(object, false);
        if (json != null) {
            return json.getBytes(CHARSET_UTF8);
        }
        return null;
    }

    public static String toJson(final Object obj, boolean prettyFormat) {
        return JSON.toJSONString(obj, prettyFormat);
    }

    public static <T> T decode(final byte[] bytes,Class<T> tClass){
        String json = new String(bytes, CHARSET_UTF8);
        return fromJson(json,tClass);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return JSON.parseObject(json, classOfT);
    }
}
