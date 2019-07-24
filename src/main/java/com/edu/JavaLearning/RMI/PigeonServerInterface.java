package com.edu.JavaLearning.RMI;

import com.edu.dao.domain.User;
import com.edu.service.impl.mContext;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Tangzhihao
 * @date 2018/2/27
 * Server端
 */
//继承Remote接口，标识该接口可以被远程访问
public interface PigeonServerInterface extends Remote{

    void login(mContext ctx) throws RemoteException;
    User getUser() throws RemoteException;
}
