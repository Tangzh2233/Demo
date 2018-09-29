package com.edu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.edu.Api.DubboProviderApi;

/**
 * @author Tangzhihao
 * @date 2018/4/28
 */
/*@Service(version = "1.0.0")*/
public class DubboProviderImpl implements DubboProviderApi{
    @Override
    public void myProvider() {
        System.out.println("服务连接成功！");
    }
}
