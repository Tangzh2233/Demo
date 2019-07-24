package com.edu.JavaLearning.JVMandReflect.reflect;
import com.alibaba.fastjson.JSON;
import com.edu.dao.domain.Alert;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * @Author: tangzh
 * @Date: 2019/3/13$ 10:50 AM$
 **/
public class ReflectApply {
    public static void main(String[] args) {
        String json = null;
        try {
            json = JSON.toJSONString(toJson(Alert.class));
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        System.out.println(json);

    }
    private static Object toJson(Class aClass) throws IllegalAccessException, InstantiationException {
        Object params = aClass.newInstance();
        Field[] fields = aClass.getDeclaredFields();
        for (Field field:fields){
            field.setAccessible(true);
            try {
                Object data;
                Class<?> aClass1;
                Class<?> type = field.getType();
                BaseDataType baseDataType = BaseDataType.formName(type.getName());
                if(baseDataType==null){
                    aClass1 = Class.forName(type.getName());
                    if(aClass1==Integer.class){
                        data = 0;
                    }else if(aClass1 == Long.class){
                        data = 0L;
                    }else if(aClass1 == Date.class){
                        data = new Date();
                    }else if(aClass1 != String.class && aClass1 instanceof Object){
                        data = toJson(aClass1);
                    }else{
                        data = aClass1.newInstance();
                    }
                }else {
                    data = baseDataType.getDefaultData();
                }
                field.set(params,data);
            } catch (IllegalAccessException | ClassNotFoundException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        return params;
    }

}

enum BaseDataType{
    BYTE("byte",byte.class,0),
    SHORT("short",short.class,0),
    INT("int",int.class,0),
    LONG("long",long.class,0L),
    FLOAT("float",float.class,0.0),
    DOUBLE("double",double.class,0.00),
    BOOLEAN("boolean",boolean.class,false)
    ;
    private String name;
    private Class type;
    private Object defaultData;

    public Object getDefaultData() {
        return defaultData;
    }

    public void setDefaultData(Object defaultData) {
        this.defaultData = defaultData;
    }

    BaseDataType(String name, Class type, Object defaultData) {
        this.name = name;
        this.type = type;
        this.defaultData = defaultData;
    }

    public static BaseDataType formName(String name){
        for(BaseDataType item:BaseDataType.values()){
            if(name.equals(item.getName())){
                return item;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }
}
