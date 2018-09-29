package com.edu.util;

import com.alibaba.fastjson.JSONObject;

/**
 * @author linzhihao
 * @date 2018/5/8
 */
public class JsonUtil {

    /**
     * 获取json字符串中的属性，如果该属性是嵌套的属性那么
     * 通过递归的方式获取json中的属性
     * @param jsonObject
     * @param key
     * @return  如果找不到就返回null
     */
    public static Object getValue(JSONObject jsonObject, Object key) {
        Object result = jsonObject.get(key);
        if (result != null) {
            return result;
        }
        for (String k : jsonObject.keySet()) {
            Object value = jsonObject.get(k);
            if (value instanceof JSONObject) {
                result = getValue((JSONObject) value,key);
            }
        }
        return  result;
    }
}
