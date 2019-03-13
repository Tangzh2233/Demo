package com.edu.spring;

import com.edu.common.DemoContext;
import com.edu.util.PPSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContextEvent;
/**
 * @Author: tangzh
 * @Date: 2019/1/11$ 4:58 PM$
 * 自定义ContextLoaderLister。
 * 添加常用字段读取功能
 **/
public class DemoContextLoaderListener extends ContextLoaderListener {

    private static final Logger logger = LoggerFactory.getLogger(DemoContextLoaderListener.class);

    private String[] resourcesFileName = {"context"};

    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {
            DemoContext.initDemoContext(new PPSUtil(resourcesFileName));
        } catch (Exception e) {
            logger.error("DemoContext加载失败",e);
            throw new RuntimeException(e);
        }
        super.contextInitialized(event);
    }
}
