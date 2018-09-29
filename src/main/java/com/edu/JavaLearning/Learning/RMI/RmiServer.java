package com.edu.JavaLearning.Learning.RMI;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * @author Tangzhihao
 * @date 2018/2/27
 * 给本机port:8888绑定服务
 */

public class RmiServer {

    public static void main(String[] args) throws RemoteException, MalformedURLException, AlreadyBoundException {
        PigeonServerInterface service = new PigeonServerImpl();
        LocateRegistry.createRegistry(8888);
        Naming.bind("//127.0.0.1:8888/PigeonServer", service);
        System.out.println("rmi注册成功");
    }

    /*public static void main(String[] args) throws RemoteException, AlreadyBoundException, MalformedURLException {
        PigeonServerInterface user = new PigeonServerImpl();
        Naming.bind("User",user);
        System.out.println("RMI SERVER IS RUNNING");
    }*/

}
