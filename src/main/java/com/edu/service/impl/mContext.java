package com.edu.service.impl;

import com.dianping.cat.Cat;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tangzhihao
 * @date 2018/2/27
 */

public class mContext implements Cat.Context,Serializable {

    private Map<String,String> properties = new HashMap<>();

    public void addProperty(String key,String value){
        this.properties.put(key,value);
    }

    public String getProperty(String key){
        return this.properties.get(key);
    }

}
