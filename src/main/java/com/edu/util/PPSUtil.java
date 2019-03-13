package com.edu.util;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @Author: tangzh
 * @Date: 2019/1/11$ 6:58 PM$
 * 读取本地配置文件 功能类似OpenConfig
 **/
public class PPSUtil {
    private Map<String,Evalue> propertiesMap = new HashMap<>();

    public PPSUtil(String...resourcesName){
        for (String fileName:resourcesName){
            PPUtil ppUtil = new PPUtil(fileName);
            for(String key:ppUtil.getAllKeys()){
                if(StringUtils.isNotBlank(key)){
                    propertiesMap.put(key,new Evalue(ppUtil.getValue(key)));
                }
            }
        }
    }

    public Evalue getValue(String key){
        return propertiesMap.get(key);
    }

    public class Evalue{
        private String value;
        Evalue(String value){
            this.value = value;
        }
        public String getValue(){
            return value;
        }
    }
}
class PPUtil{
    private ResourceBundle resourceBundle;

    public PPUtil(String fileName){
        this.resourceBundle = ResourceBundle.getBundle(fileName, Locale.getDefault());
    }

    public String getValue(String key){
        return resourceBundle.getString(key);
    }

    public String[] getValues(String key){
        return  resourceBundle.getStringArray(key);
    }

    public String[] getAllKeys(){
        ArrayList<String> keys = new ArrayList<>();
        Enumeration<String> enums = resourceBundle.getKeys();
        while (enums.hasMoreElements()){
            keys.add(enums.nextElement());
        }
        return keys.toArray(new String[0]);
    }
}
