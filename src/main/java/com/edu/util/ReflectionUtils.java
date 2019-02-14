package com.edu.util;

import com.jiupai.cornerstone.util.SysoutLogUtil;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author: tangzh
 * @Date: 2019/1/21$ 8:12 PM$
 **/
public class ReflectionUtils {

    public static Method getDeclaredMethod(Object object, String methodName, Class... parameterTypes) {
        Method method = null;
        Class clazz = object.getClass();

        while(clazz != Object.class) {
            try {
                method = clazz.getDeclaredMethod(methodName, parameterTypes);
                return method;
            } catch (Throwable var6) {
                clazz = clazz.getSuperclass();
            }
        }

        return null;
    }

    public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes, Object[] parameters) {
        Method method = getDeclaredMethod(object, methodName, parameterTypes);

        try {
            if (null != method) {
                method.setAccessible(true);
                return method.invoke(object, parameters);
            } else {
                return null;
            }
        } catch (IllegalArgumentException var6) {
            throw new RuntimeException(var6);
        } catch (IllegalAccessException var7) {
            throw new RuntimeException(var7);
        } catch (InvocationTargetException var8) {
            throw new RuntimeException(var8);
        }
    }

    public static Field getDeclaredField(Object object, String fieldName) {
        Field field = null;
        Class clazz = object.getClass();

        while(clazz != Object.class) {
            try {
                field = clazz.getDeclaredField(fieldName);
                return field;
            } catch (Throwable var5) {
                clazz = clazz.getSuperclass();
            }
        }

        return null;
    }

    public static void setFieldValue(Object object, String fieldName, Object value) {
        Field field = getDeclaredField(object, fieldName);
        field.setAccessible(true);

        try {
            field.set(object, value);
        } catch (IllegalArgumentException var5) {
            throw new RuntimeException(var5);
        } catch (IllegalAccessException var6) {
            throw new RuntimeException(var6);
        }
    }

    public static Object getFieldValue(Object object, String fieldName) {
        Field field = getDeclaredField(object, fieldName);
        field.setAccessible(true);

        try {
            return field.get(object);
        } catch (Exception var4) {
            SysoutLogUtil.error(var4.getMessage(), var4);
            return null;
        }
    }

    public static void writeField(String fieldName, Object obj, Object value) {
        try {
            Class tClass = obj.getClass();
            Field field = tClass.getDeclaredField(fieldName);
            if (field != null) {
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), tClass);
                Method method = pd.getWriteMethod();
                method.invoke(obj, value);
                SysoutLogUtil.debug("field:" + field.getName() + "---getValue:" + value);
            }
        } catch (Exception var7) {
            SysoutLogUtil.error("set field[" + fieldName + "] error", var7);
        }

    }

    public static void writeFieldWithSet(String fieldName, Object obj, Object value) {
        try {
            Class tClass = obj.getClass();
            Field field = tClass.getDeclaredField(fieldName);
            if (field != null) {
                Method method = tClass.getDeclaredMethod("set" + uppercaseFirstCharacter(fieldName), String.class);
                if (method != null) {
                    method.invoke(obj, value);
                    SysoutLogUtil.debug("field:" + field.getName() + "---getValue:" + value);
                } else {
                    writeField(fieldName, obj, value);
                }
            }
        } catch (Exception var6) {
            SysoutLogUtil.error("set field[" + fieldName + "] error", var6);
        }

    }

    public static String uppercaseFirstCharacter(String name) {
        char[] cs = name.toCharArray();
        if (cs[0] >= 'a' && cs[0] <= 'z') {
            cs[0] = (char)(cs[0] - 32);
        }
        return String.valueOf(cs);
    }
}
