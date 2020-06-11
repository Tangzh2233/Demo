package com.edu.JavaLearning.spring;

import com.edu.dao.domain.User;
import com.edu.dao.mapper.ideaDemo.DlogMapper;
import com.edu.dao.mapper.ideaDemo.UserMapper;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.beans.Introspector;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author tangzh
 * @version 1.0
 * @date 2019/10/11 9:52 AM
 * https://segmentfault.com/a/1190000019255721?utm_source=tag-newest
 *
 * Spring事务学习 + Mybatis
 * Mybatis-Spring即Mybatis接入Spring。主要配置文件"/xml/mybatis-tx.xml"
 * 重要组件
 * @see org.mybatis.spring.mapper.ClassPathMapperScanner  扫描加工构建DlogMapper.class
 * @see org.mybatis.spring.mapper.MapperFactoryBean       ClassPathMapperScanner就是将DlogMapper.class的beanType设置为此,autowire = byType
 * @see org.mybatis.spring.SqlSessionFactoryBean 负责创建SqlSessionFactory,而SqlSessionFactory是创建SqlSession的工厂类
 * @see org.apache.ibatis.session.Configuration  保存mybatis的基本配置及Mapper.xml的解析数据
 * @see org.apache.ibatis.binding.MapperProxyFactory  MapperProxy工厂类,负责创建DlogMapper的代理类
 *
 * 1.配置文件
 *  1-1.xsd文件定义xml中节点名称
 *  1-2.xml定义节点名称以后需要让Spring注册我们新增的节点 参数(节点名称,节点解析器)。
 *  1-3.extends NamespaceHandlerSupport 实现init方法。
 *      @see org.mybatis.spring.config.NamespaceHandler 对应<mybatis:scan /> 标签
 *      @see com.alibaba.dubbo.config.spring.schema.DubboNamespaceHandler
 *  1-4.同时注册我们节点的解析类 实现 BeanDefinitionParser
 *      @see org.mybatis.spring.config.MapperScannerBeanDefinitionParser
 *      @see com.alibaba.dubbo.config.spring.schema.DubboBeanDefinitionParser //直接创建节点对应类,返回BeanDefinition对象
 * 2.在Mybatis-Spring中,通过MapperScannerBeanDefinitionParser中ClassPathMapperScanner
 *   扫描<mybatis:scan />中base-package路径下的Mapper.class,构建BeanDefinition 并对这些Mapper.clss进行加工
 *   @see org.mybatis.spring.mapper.ClassPathMapperScanner#doScan(String...)           构建BeanDefinitionHolder
 *   @see org.mybatis.spring.mapper.ClassPathMapperScanner#processBeanDefinitions(Set) 加工BeanDefinition。
 *                                                       设置BeanDefinition的BeanClass为MapperFactoryBean,autowireMode为byType
 * 3.创建SqlSessionFactoryBean。初始化解析基本配置和 *.xml。全部封装在Configuration中
 *   @see SqlSessionFactoryBean#buildSqlSessionFactory() SqlSessionFactoryBean创建,
 *   解析mapperLocations属性中的Mapper.xml文件。
 *   @see XMLMapperBuilder#parse()
 *   @see XMLMapperBuilder#bindMapperForNamespace() 创建DlogMapper.class -> MapperProxyFactory<T> 的映射
 *   最终解析结果都保存在Configuration对象中。包含parameterMap、resultMap、MapperStatement等所有xml元素
 *
 * 4.DlogMapper.class代理对象创建。因为DlogMapper.class的beanClass都是MapperFactoryBean。Spring执行
 *   FactoryBean的getObject做类构建
 *   @see MapperFactoryBean#getObject()
 *   4-1.从Configuration#mapperRegistry获取映射关系。得到MapperProxyFactory<T>
 *   4-2.执行
 *       @see org.apache.ibatis.binding.MapperProxyFactory#newInstance(SqlSession)
 *       创建DlogMapper的动态代理类.其中InvocationHandler为MapperProxy
 *       @see org.apache.ibatis.binding.MapperProxy#invoke(Object, Method, Object[])
 *
 *
 **/
@Aspect
public class SpringTransaction {

    private final static Logger log = LoggerFactory.getLogger(SpringTransaction.class);

    private UserMapper userMapper;

    @Resource
    private TransactionTemplate transactionTemplate;

    /**
     * @see Introspector#getBeanInfo()
     * @see Introspector#getTargetPropertyInfo()
     * springBean.xml
     * SpringBean 依赖注入分为byName和byType两种方式。
     * 但是Spring是如何知道一个Bean中哪些属性需要注入。就是备注中getTargetPropertyInfo()
     * 通过分割public get set方法。来确定类中有哪些属性
     *
     * @see org.mybatis.spring.mapper.MapperFactoryBean
     * 此类只是在
     * @see org.mybatis.spring.mapper.ClassPathMapperScanner
     * 中设置了 GenericBeanDefinition的setAutowireMode为byType
     * 最终在Spring会依据以下两个方法
     * @see org.mybatis.spring.mapper.MapperFactoryBean#setSqlSessionFactory(SqlSessionFactory)
     * @see org.mybatis.spring.mapper.MapperFactoryBean#setSqlSessionTemplate(SqlSessionTemplate)
     * 认为 MapperFactoryBean 存在属性 SqlSessionFactory、SqlSessionTemplate 然后在容器中通过
     * byType 的方式找到相应的Bean执行这两个方法。最终达到MapperFactoryBean的sqlSession属性注入。
     *
     * 当前方法也是一样。在xml中设置了autowire。最终Spring在初始化MybatisSpring时会调用
     * 当前方法,进行属性userMapper的赋值
     * @param userMapper
     */
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateDataMethodA(){
        //todo execute sql
        //todo execute sql
    }

    public void updateDataMethodB(){
        updateDataMethodC();
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateDataMethodC(){
        List<User> dataBaseData = new ArrayList<>();
        dataBaseData.add(new User("糖糖","1q2w3e4r","100000001"));
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    User user = userMapper.queryUserByUserNo("1001");
                    System.out.println(user.toString());
                    userMapper.insertUserList(dataBaseData);
                    int i = 1 / 0;
                } catch (Exception e) {
                    status.setRollbackOnly();
                    System.out.println("异常了");
                }
            }
        });
        System.out.println("事务回滚？？");
    }

    public static void main(String[] args) {
//        executeJdbcTx();
        executeSpringTx();
    }

    public static void executeSpringTx(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/xml/mybatis-tx.xml","/xml/springBean.xml");
        SpringTransaction instance = context.getBean(SpringTransaction.class);

        //内部方法调用事务不会生效
        instance.updateDataMethodB();

        List<User> tang = instance.userMapper.getUserByName2("tangzh");
        System.out.println(tang);
        List<User> dataBaseData = new ArrayList<>();
        List<User> dataBaseData1 = new ArrayList<>();
        dataBaseData.add(new User("糖糖","1q2w3e4r"));
        dataBaseData1.add(new User("糖糖","1qaz","007"));
        TransactionTemplate transactionTemplate = context.getBean(TransactionTemplate.class);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    List<User> tang = instance.userMapper.getUserByName2("tangzh");
                    int i = instance.userMapper.updateUserByName("tangzh",11);
                    System.out.println(tang);
                    System.out.println(i);

                    instance.userMapper.insertUserList(dataBaseData1);
                    instance.userMapper.insertUserList(dataBaseData);
                } catch (Exception e) {
                    log.error("数据入库失败",e);
                    status.setRollbackOnly();
                    throw new IllegalArgumentException();
                }
            }
        });
    }

    public static void executeJdbcTx() {
        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statement1 = null;
        try {
            String sql = "insert into users(username,password,usr_no) values ('糖糖','123456','012')";
            String sql2 = "insert into users(username,password) values ('糖糖','123456')";
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ideademo", "root", "12345678");
            connection.setAutoCommit(false);
            System.out.println(connection.getTransactionIsolation());
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            statement = connection.prepareStatement(sql);
            statement1 = connection.prepareStatement(sql2);
            statement.executeUpdate();
            statement1.executeUpdate();
//            while (resultSet.next()){
//                int id = resultSet.getInt(1);
//                String userName = resultSet.getString(2);
//                String password = resultSet.getString(3);
//                String flag = resultSet.getString(4);
//                String userNo = resultSet.getString(5);
//                System.out.println("id: "+ id + "userName: "+ userName + "password: "+ password + "flag: "+ flag + "userNo: "+ userNo);
//            }
            connection.commit();
        } catch (SQLException e) {
            log.error("数据入库失败",e);
            try {
                if(connection != null){
                    connection.rollback();
                }
            } catch (SQLException e1) {
                log.error("回滚失败",e1);
            } finally {
                try {
                    if(statement != null ){
                        statement.close();
                        statement = null;
                    }
                    if(statement1 != null ){
                        statement1.close();
                        statement = null;
                    }
                    if(connection != null){
                        connection.close();
                        connection = null;
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
