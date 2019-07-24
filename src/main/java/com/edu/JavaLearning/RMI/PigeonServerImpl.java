package com.edu.JavaLearning.RMI;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.edu.dao.domain.User;
import com.edu.service.impl.mContext;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Tangzhihao
 * @date 2018/2/27
 * 子接口的每个方法都必须声明抛出java.RMI.RemoteException异常，该异常是使用RMI时可能抛出的大多数异常的父类。
 * 子接口的实现类应该直接或者间接继承java.RMI.server.UnicastRemoteObject类，
 * 该类提供了很多支持RMI的方法，具体来说，这些方法可以通过JRMP协议导出一个远程对象的引用，并通过动态代理构建一个可以和远程对象交互的Stub对象。
 */

public class PigeonServerImpl extends UnicastRemoteObject implements PigeonServerInterface,Serializable{

    private static final long serialVersionUID = -3304127635087881348L;

    public PigeonServerImpl() throws RemoteException{

    }
    @Override
    public void login(mContext ctx) {
        Transaction transaction = null;
        try {
            transaction = Cat.getProducer().newTransaction("PigeonService", "method2");
            Cat.logEvent("PigeonService.app","remote");
            Cat.logEvent("PigeonService.client","server");
            Cat.logEvent("PigeonService.local","8888");
            Cat.logRemoteCallServer(ctx);
        }finally {
            transaction.setStatus("0");
            transaction.complete();
        }
        System.out.println("login");
    }

    @Override
    public User getUser() throws RemoteException {
        return new User(1,"tang","123");
    }
}
