package com.edu.JavaLearning.jdktest.LockAndCollection;

import java.io.Serializable;
import java.lang.ref.PhantomReference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @author Tangzhihao
 * @date 2018/3/28
 */

public class LockTest {

    private Vector vector;
    private ArrayList arrayList;
    private LinkedList linkedList;
    private CopyOnWriteArrayList copyOnWriteArrayList;
    private HashSet hashSet;
    private TreeSet treeSet;
    private CopyOnWriteArraySet copyOnWriteArraySet;
    private HashMap hashMap;
    private Hashtable hashtable;
    private TreeMap treeMap;
    private ConcurrentHashMap concurrentHashMap;
    private Stack stack;
    private Deque deque;
    private Queue queue;
    private AbstractQueuedSynchronizer abstractQueuedSynchronizer;
    private Collections collections;
    private AtomicInteger atomicInteger;//以原子的方式更新int值
    private ArrayBlockingQueue arrayBlockingQueue;
    private LinkedBlockingQueue linkedBlockingQueue;

    public static void main(String[] args) throws UnknownHostException {

        TheThread thred = new TheThread();
        Thread thread = new Thread(thred,"A");
        Thread thread2 = new Thread(thread,"B");
        Thread thread3 = new Thread(thread,"C");
        new WeakReference(thread);
        new SoftReference(thread);
        Object object;
    //    new PhantomReference();
        thread.start();
        thread2.start();
        thread3.start();
    /*    ArrayList<String> list = new ArrayList();
        list.add("tang");
        list.add("zhi");
        list.add("hao");
        Collections.sort(list, Comparator.naturalOrder());*/
    //    LockTest.getLocalIp();
    //    System.setProperty("host.ip","111.111.111.111");
    //    String ip = System.getProperty("host.ip");

    //    System.out.println(ip);
    /*  HashMap<Object, Object> map = new HashMap<>();
        String str = "tang";
        int h;
        int i = (h = str.hashCode()) ^ (h >>> 16);
        System.out.println(3552486 >> 16);
        System.out.println(3552486^54);
        System.out.println(5/2);
        System.out.println(5%2);

        int a = 0;
        int[] b = new int[]{1,2,3,4};
        String t = "tang";
        LockTest.changeStr(t);
        System.out.println(t);
*/
    }
    public static void getLocalIp() throws UnknownHostException {
        InetAddress hostName = InetAddress.getLocalHost();
        InetAddress loopbackAddress = InetAddress.getLoopbackAddress();
        InetAddress localHost = Inet4Address.getLocalHost();
        System.out.println(hostName);
        System.out.println(loopbackAddress);
        System.out.println(localHost);
        System.out.println(InetAddress.getByName(""));
    }
    public static void changeNum(int a){
        a = 3;
        System.out.println(a);
    }
    public static void changeInt(int[] a){
        for(int i=0;i<a.length;i++){
            a[i] = 66;
        }
        System.out.println(a.toString());
    }
    public static void changeStr(String str){
        str = "hahahah";
    }

}
