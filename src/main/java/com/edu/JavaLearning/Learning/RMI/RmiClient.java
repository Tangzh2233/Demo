package com.edu.JavaLearning.Learning.RMI;

import com.edu.service.impl.mContext;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * @author Tangzhihao
 * @date 2018/2/27
 */

public class RmiClient {


    /*public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        PigeonServerInterface pigeon = (PigeonServerInterface) Naming.lookup("User");
        User user = pigeon.getUser();
        System.out.println("Result"+user.toString());
    }*/

    public void start(mContext ctx) throws RemoteException, MalformedURLException, NotBoundException {
        PigeonServerInterface pigeon = (PigeonServerInterface) Naming.lookup("//127.0.0.1:8888/PigeonServer");
        pigeon.login(ctx);
        System.out.println("remote success");
    }

}
