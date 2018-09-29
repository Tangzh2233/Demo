package com.edu.common;

import org.apache.commons.lang3.StringUtils;

public abstract class Assert {

    public static void isEmpty(String key,String msg){
        if(StringUtils.isEmpty(key)){
            throw new IllegalArgumentException(msg);
        }
    }
}
