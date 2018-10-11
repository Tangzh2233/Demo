package com.edu.service.impl;

import com.alibaba.fastjson.JSON;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Event;
import com.dianping.cat.message.ForkedTransaction;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import com.edu.JavaLearning.aop.aop.annotation.LogEvent;
import com.edu.JavaLearning.aop.aop.annotation.LogMetricCount;
import com.edu.JavaLearning.aop.aop.annotation.LogMetricSum;
import com.edu.JavaLearning.aop.aop.annotation.LogTransaction;
import com.edu.common.Assert;
import com.edu.common.UUIDUtil;
import com.edu.common.result.ResultData;
import com.edu.dao.domain.Dlog;
import com.edu.dao.domain.User;
import com.edu.dao.mapper.ideaDemo.DlogMapper;
import com.edu.dao.mapper.ideaDemo.UserMapper;
import com.edu.service.ILoginService;
import com.edu.util.CatUtil;
import com.edu.util.CookieUtils;
import com.edu.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/9/14.
 */
@Service
public class LoginServiceImpl implements ILoginService{

    private final static ThreadLocal<mContext> myContext = new ThreadLocal<>();
    private final static Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private UserMapper userMapper;
    @Resource
    private DlogMapper dlogMapper;

    @Value("${redis_key_expire_time}")
    private Integer key_expire_time;
    @Value("${redis_session_key}")
    private String user_session_key;


    @Override
    @LogTransaction(type = "SpringDemo",name = "Recharge")
    @LogEvent(type="recharge",expectedCodes = "pw000",codeProperty = "code",messageProperty = "message")
    @LogMetricCount(metricKey = "name")
    @LogMetricSum(metricKey = "登录",metric = "name")
    public User login(String name,double amount) {
        createCat();
        Transaction t = CatUtil.logTransaction("recharge","busicode");
        double account = amount;
        User user = new User();
        try {
            /*if("liu".equals(name)){
                t = Cat.getProducer().newTransaction("PigeonService", "method1");
                t.setStatus(Transaction.SUCCESS);
                Cat.logEvent("login事件","失败", "1","11111");
                t.setStatus("1");
            }else if("yu".equals(name)){
                mContext context = this.getContext();
                t = Cat.getProducer().newTransaction("PigeonCall", "method1");
                Cat.logEvent("PigeonCall.app","local");
                Cat.logEvent("PigeonCall.server","client");
                Cat.logEvent("PigeonCall.port","8848");
                Cat.logRemoteCallClient(context);
                RmiClient client = new RmiClient();
                //调用
                client.start(context);

            }else {
               // t = Cat.getProducer().newTransaction("loginTime", "ABC");
                t = Cat.getProducer().newTransaction("Cache.Redis","缓存test");
                t.setStatus(Transaction.SUCCESS);
                Event event = Cat.newEvent("Cache.Server", "127.0.0.1");
                event.setStatus("1");
                t.addChild(event);
            }*/
            Cat.logMetricForCount(name);
            Cat.logMetricForDuration("11", 78L);
            Cat.logMetricForSum("登录金额",account);
            if("exception".equals(name)){
                throw new NullPointerException();
            }
            user = userMapper.getUserByName(name);
        }catch (Exception e){
            t.setStatus(e);
        }finally {
            t.setStatus(Transaction.SUCCESS);
            t.complete();
        }
        logger.info("返回参数: "+user.toString());
        return user;
    }


    @Override
    public <User> User select(User user) {
        return user;
    }

    @Override
    public int addDlog(Dlog dlog) {
        int tableName = (int)Integer.valueOf(dlog.getId());
        System.out.println("表名: "+tableName%2);
        return dlogMapper.addDlog(dlog,String.valueOf(tableName%2));
    }

//    public static void main(String[] args) {
//        Transaction t = Cat.getProducer().newTransaction("Cross", "ABC");
//        Cat.logEvent("Cross","main","0","");
//        User user = new User();
//        try {
//            t.setStatus(Transaction.SUCCESS);
//        }catch (Exception e){
//            t.setStatus(e);
//        }finally {
//            t.complete();
//        }
//    }
    /*private void test(){
        LoginServiceImpl loginService = new LoginServiceImpl();
    }*/
    private mContext getContext(){
        mContext context = myContext.get();
        if(context==null){
            context = new mContext();
            myContext.set(context);
        }
        return context;
    }

    private void createCat(){
        Transaction t = Cat.getProducer().newTransaction("Tang", "a");
        Cat.logEvent("Tangevent","b");
        ForkedTransaction ft = Cat.getProducer().newForkedTransaction("Forke", "a");
        MyThread myThread = new MyThread(ft);
        new Thread(myThread).start();
        t.setStatus(Message.SUCCESS);
        t.complete();

    }
    class MyThread implements Runnable{
        private ForkedTransaction forkedTransaction;

        public MyThread(ForkedTransaction f){
            this.forkedTransaction = f;
        }
        @Override
        public void run() {
            forkedTransaction.fork();
            Cat.logEvent("forkevent","b");
            forkedTransaction.setStatus(Message.SUCCESS);
            forkedTransaction.complete();
        }
    }

    @Override
    public ResultData userLogin(String name, String pwd, HttpServletRequest request, HttpServletResponse response){
        User user = userMapper.getUserByName(name);

        Transaction t = Cat.getProducer().newTransaction("Demo","登录");
        try {
            if(user==null){
                CatUtil.buildEvent(t,name+"|用户登录","用户不存在","1");
                return ResultData.isFail("01","用户信息有误!");
            }
            if(!DigestUtils.md5DigestAsHex(pwd.getBytes()).equals(user.getPassword())){
                CatUtil.buildEvent(t,name+"|用户登录","密码错误","1");
                return ResultData.isFail("01","用户信息有误!");
            }

            String token = UUIDUtil.getUUID().substring(10);
            user.setPassword(null);
            RedisUtil.set(user_session_key+":"+token, JSON.toJSONString(user));
            RedisUtil.expire(user_session_key+":"+token,key_expire_time);
            CookieUtils.setCookie(request,response,"USER_TOKEN",token);
            CatUtil.buildEvent(t,name+"|用户登录","登录成功","0");
            Cat.logMetricForCount("用户登录");
            return ResultData.defaultSuccess();
        } finally {
            t.setStatus(Transaction.SUCCESS);
            t.complete();
        }
    }

    @Override
    public ResultData userRegister(User user) {
        Assert.isEmpty(user.getUsername(),"参数不能为空");
        Assert.isEmpty(user.getPassword(),"参数不能为空");
        final User usr = new User();
        usr.setUsername(user.getUsername());
        usr.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        int i = userMapper.insertUserList(new ArrayList<User>() {{
            add(usr);
        }});
        if(i>0){
            return ResultData.isSuccess("注册成功!",null);
        }else {
            return ResultData.isFail("01","注册失败!");
        }
    }

    @Override
    public ResultData loginOut(HttpServletRequest request, HttpServletResponse response, String token) {
        try {
            String json = RedisUtil.get(user_session_key + ":" + token);
            if(StringUtils.isBlank(json)){
                return ResultData.isFail("01","session已过期");
            }
            RedisUtil.del(user_session_key + ":" + token);
            return ResultData.defaultSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.defaultSuccess();
        }
    }

    @Override
    public ResultData checkToken(String token){
        String json = null;
        try {
            json = RedisUtil.get(user_session_key+":"+token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(StringUtils.isBlank(json)){
            return ResultData.isFail("01","身份已过期");
        }
        //更新redis key 时间
        RedisUtil.expire(user_session_key+":"+token,key_expire_time);
        return ResultData.isSuccess("",JSON.parseObject(json,User.class));
    }

}
