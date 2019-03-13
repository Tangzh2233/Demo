package com.edu.util.zookeeper;

import com.edu.util.ReflectionUtils;
import com.edu.util.zookeeper.listener.DrmConnectStateListener;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Author: tangzh
 * @Date: 2019/1/21$ 7:02 PM$
 * 这样写会导致new出一堆AppDrmNode，很麻烦！！
 * 解决方式:使用注解，实现SpringContext启动时自动向zookeeper注册节点
 *
 * 几个接口的作用:
 * ApplicationListener<ApplicationContextEvent>:实现事件监听,本类中的用处是监听ContextRefreshedEvent(SpringContext初始化完成)事件
 * 执行onApplicationEvent方法,完成注解的扫描注入及zookeeper节点的创建
 *
 * ApplicationContextAware:获取SpringContext实例。Spring初始化完成后会调用实现此接口的bean,进行Context注入。
 *
 * 实现InitializingBean的类，在类加载后会调用afterPropertiesSet()方法，开发人员可以重写afterPropertiesSet()完成自己的工作。
 *
 * 实现DisposableBean接口的类，在类销毁时，会调用destroy()方法，开发人员可以重新该方法完成自己的工作。
 **/
public class DrmZookeeperClient implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware, InitializingBean, DisposableBean {
    private CuratorClient client;
    private final static String nameSpace = "DRM";
    private String zkAdress;
    private String appName;
    private ConnectionStateListener listener;
    //保存Spring的上下文
    private ApplicationContext applicationContext;
    //保存已注册的节点,当session超时时,据此数据进行重新注册
    private Set<AppDrmNode> appDrmNodes = new CopyOnWriteArraySet<>();
    //保证单例
    private static DrmZookeeperClient drmZookeeperClient;
    private volatile boolean inited = false;

    public void init() throws InterruptedException {
        this.client = new CuratorClient();
        listener = new DrmConnectStateListener(this.client);
        this.client.init(this.zkAdress,nameSpace,5000,listener);
        drmZookeeperClient = this;
    }

    public void configRegist(){
        if(appDrmNodes.size()>0){
            for(AppDrmNode node:appDrmNodes){
                configRegist(node,true);
            }
        }
    }

    public boolean configRegist(AppDrmNode node,boolean flag) {
        String configKey = node.getClassName()+ "." + node.getParamName();
        String path = "/" + this.appName + "/" + configKey;
        try {
            if(!this.client.isPathExist(path)){
                this.client.createPath(path, CreateMode.PERSISTENT,node.getValue());
            }
            //添加节点监视器
            this.client.watch(path,new DrmNodeWatcher(this.client,node));
            if(flag){
                this.appDrmNodes.add(node);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void reInit(){
        try {
            client.removeConnectStateListener(listener);
            listener = null;
            client.destroy();
            client = null;
            init();
            configRegist();
        }catch (Exception e){

        }
    }

    public CuratorClient getClient() {
        return client;
    }

    public void setClient(CuratorClient client) {
        this.client = client;
    }

    public String getZkAdress() {
        return zkAdress;
    }

    public void setZkAdress(String zkAdress) {
        this.zkAdress = zkAdress;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public static DrmZookeeperClient getDrmZookeeperClient() {
        return drmZookeeperClient;
    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(!inited){
            synchronized (this){
                if (!inited){
                    Map<String, DynamicResource> beans = this.applicationContext.getBeansOfType(DynamicResource.class);
                    for(DynamicResource next : beans.values()){
                        Field[] fields = next.getClass().getDeclaredFields();
                        for(Field field:fields){
                            DRM drm = field.getAnnotation(DRM.class);
                            if(drm!=null){
                                field.setAccessible(true);
                                String fieldName = field.getName();
                                String value = (String)ReflectionUtils.getFieldValue(next, fieldName);
                                if(StringUtils.isBlank(value)){
                                    value = drm.value();
                                }
                                ReflectionUtils.writeFieldWithSet(fieldName,next,value);
                                AppDrmNode node = new AppDrmNode(next, fieldName, value);
                                this.appDrmNodes.add(node);
                            }
                        }
                    }
                    configRegist();
                    inited = true;
                }
            }
        }

    }
}
