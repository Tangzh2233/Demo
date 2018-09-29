package com.edu.JavaLearning.Learning.编程式事务管理TransactionTemplate;

import com.edu.dao.domain.Dlog;
import com.edu.dao.domain.User;
import com.edu.dao.mapper.ideaDemo.DlogMapper;
import com.edu.dao.mapper.ideaDemo.UserMapper;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Tangzhihao
 * @date 2017/11/16
 * 用于一个方法里对数据库进行多次操作的情况
 */

public class MyTransactionTemplate {

    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private DlogMapper dlogMapper;
    @Resource
    private UserMapper userMapper;

    /*@Resource
    private TransactionAspectSupport transactionAspectSupport;*/

    public User insertUser(final User user, final Dlog dlog){
        try {
            transactionTemplate.execute(new TransactionCallback<User>(){

                @Override
                public User doInTransaction(TransactionStatus transactionStatus) {
                    try {
                        userMapper.insertUserList(new ArrayList<User>(){
                            {
                                add(user);
                            }
                        });
                        dlogMapper.addDlog(dlog);
                    } catch (Exception e) {
                        transactionStatus.setRollbackOnly();//小代码块回滚
                    }
                    return null;
                }
            });
        } catch (TransactionException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//主事务回滚
        }

        return user;
    }

    public User updateUser(User user){
        User user1 = new User();
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                try {
                    int a = 123;
                } catch (Exception e) {
                    transactionStatus.setRollbackOnly();
                }
            }
        });
        return  user1;
    }
}
