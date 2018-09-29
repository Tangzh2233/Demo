package com.edu.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Tangzhihao
 * @date 2018/4/12
 * 自定义配置文件加载类
 */

public class OpenConfig {

    private static final Logger logger = LoggerFactory.getLogger(OpenConfig.class);
    public static final String FILE_NAME = "config";
    public static Properties properties;

    static {
        try {
            properties = new Properties();
            InputStream in = OpenConfig.class.getClassLoader().getResourceAsStream("config.properties");
            properties.load(in);
        } catch (IOException e) {
            logger.info("配置文件加载失败",e);
        }
    }

    public static String getConfig(String key){
        try {
            String property = properties.getProperty(key);
            if(StringUtils.isNotBlank(property)){
                return property;
            }
            throw new Exception("配置文件获取失败");
        } catch (Exception e) {
            String message = "不能在"+FILE_NAME+"中发现参数:"+key;
            throw new RuntimeException(message);
        }
    }
}
