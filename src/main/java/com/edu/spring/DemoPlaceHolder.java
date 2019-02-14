package com.edu.spring;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

/**
 * @Author: tangzh
 * @Date: 2019/1/11$ 10:36 AM$
 * 自定义 PropertyPlaceholderConfigurer
 * 增加系统外部文件读取功能
 **/
public class DemoPlaceHolder extends PropertyPlaceholderConfigurer {

    private final Logger logger = LoggerFactory.getLogger(DemoPlaceHolder.class);
    private String externalConfig;

    public DemoPlaceHolder(){
        //忽略外部资源不存在的情况
        this.setIgnoreResourceNotFound(true);
        this.setIgnoreUnresolvablePlaceholders(true);
    }

    //PropertyPlaceholderConfigurer通过调用此方法进行配置加载,此处重写此方法添加自定义功能
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
            throws BeansException {
        //todo 添加外部文件的配置到properties中
        Properties properties = this.doExternalConfig(props);
        //todo
        super.processProperties(beanFactoryToProcess,properties);
    }

    //1.从Properties中获取externalConfig的实际路径
    //2.加载文件并返回
    private Properties doExternalConfig(Properties properties){
        Properties externalProps = new Properties();
        if(StringUtils.isNotBlank(this.externalConfig)){
            try {
                if(this.externalConfig.startsWith("${")&&this.externalConfig.endsWith("}")){
                    String key = this.externalConfig.substring(2,this.externalConfig.length()-1);
                    this.externalConfig = (String)properties.get(key);
                }
                if(this.externalConfig == null){
                    return  externalProps;
                }
                FileInputStream inputStream = new FileInputStream(this.externalConfig);
                externalProps.load(inputStream);
                this.logger.info("外部文件{}加载成功",this.externalConfig);
            } catch (FileNotFoundException var1) {
                //文件加载失败,用classLoader加载一次
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                InputStream inputStream = classLoader.getResourceAsStream(this.externalConfig);
                try {
                    externalProps.load(inputStream);
                } catch (IOException var3) {
                    this.logger.error(var3.getMessage(),var3);
                    throw new RuntimeException("加载外部文件失败");
                }
            } catch (IOException var2) {
                this.logger.error(var2.getMessage(),var2);
            }
        }

        Iterator it$ = externalProps.keySet().iterator();
        //增加外部文件配置
        while (it$.hasNext()){
            Object key = it$.next();
            Object value = externalProps.get(key);
            this.logger.debug("[key={},value={}]",key,value);
            if(value!=null){
                properties.put(key,value);
            }
        }
        return properties;
    }

    public void setExternalConfig(String externalConfig) {
        this.externalConfig = externalConfig;
    }

}
