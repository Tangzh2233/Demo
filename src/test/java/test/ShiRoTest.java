package test;

import com.edu.Application;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by Administrator on 2017/8/6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = Application.class)
public class ShiRoTest {

    @Test
    public void testNormalRealm(){
        org.apache.shiro.subject.Subject subject = null;
        //获取一系列实例，最终绑定给SecurityUtil，用来获取subject
        subject = getSecurityFactory("classpath:shiro.ini");
        UsernamePasswordToken token = new UsernamePasswordToken("zhang","456");
        Security(subject,token);
    }

    @Test
    public void testSingleRealm(){
        org.apache.shiro.subject.Subject subject = null;
        subject = getSecurityFactory("classpath:shiro-single-realm.ini");
        UsernamePasswordToken token = new UsernamePasswordToken("唐志豪","123");
        UsernamePasswordToken token1 = new UsernamePasswordToken("zhang","456");
        Security(subject,token);
        Security(subject,token1);
    }

    @Test
    public void testJdbcRealm(){
        new JdbcRealm(); //仅仅是查看jdbcRealm的源码
        //初始化SecurityFactory工厂，获取SecurityManager实例，绑定SecurityUtil,返回subject
        org.apache.shiro.subject.Subject subject = null;
        subject = getSecurityFactory("classpath:shiro-jdbc-realm.ini");
        UsernamePasswordToken token = new UsernamePasswordToken("liu", "456");
        //验证
        Security(subject,token);
    }

    @Test
    public void testAllSuccessfulStrategy(){
        org.apache.shiro.subject.Subject subject = null;
        subject = getSecurityFactory("classpath:shiro-all-strategy.ini");
        UsernamePasswordToken token = new UsernamePasswordToken("zhang","456");
        subject.login(token);

        PrincipalCollection principals = subject.getPrincipals();
        Assert.assertEquals(2,principals.asList().size());
    }

    //获取Subject
    public org.apache.shiro.subject.Subject getSecurityFactory(String iniResourcePath){

        //1.获取SecurityManager工厂 以ini配置文件初始化SecurityManager
        Factory<SecurityManager> factory = new IniSecurityManagerFactory(iniResourcePath);
        //2.获取SecurityManager示例，并绑定给SecurityUtils
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        //3.得到Subject
        org.apache.shiro.subject.Subject subject = SecurityUtils.getSubject();
        return subject;
    }

    //身份验证
    public void Security(org.apache.shiro.subject.Subject subject,UsernamePasswordToken token){
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(true,subject.isAuthenticated());//断言用户已经登录
        //退出
        subject.logout();
        System.out.println("验证完成");
    }
}
