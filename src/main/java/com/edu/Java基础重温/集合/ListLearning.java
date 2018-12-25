package com.edu.Java基础重温.集合;

import javax.management.relation.RoleList;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * =====【ArrayList+Vector+CopyOnWriteArrayList+LinkedList+Stack+RoleList】
 * ====================ArrayList、Vector:源码实现基本一致extends AbstractList<E>
 *                     implements List<E>, RandomAccess, Cloneable======================
 * Object[] elementData;对象数组
 * 底层默认为Object[10] | Itr迭代器 | ListItr迭代器增强版，可以向前迭代.
 * SubList返回Object[]的子集 | ArrayListSpliterator平分迭代器，多线程同时迭代一个Object[]
 * 不同：1 后者通过synchronized保证线程安全同时效率降低
 *      2 前者默认初始化为{},第一次add时进行初始内存分配。后者默认初始化为Object[10]
 *      3 Vector多了一个setSize(),手动设置对象数组大小。
 *      4 Vector可以初始化时手动设置每次扩容的大小，默认为Object[].length()x2。而ArrayList为Object[].length()+(Object[].length()>>1)
 *==================CopyOnWriteArrayList==================================
 * CopyOnWriteArrayList:底层为volatile Object[] + ReentrantLock 实现。
 * CopyOnWrite容器即写时复制的容器。
 * 通俗的理解是当我们往一个容器添加元素的时候，不直接往当前容器添加，
 * 而是先将当前容器进行Copy，复制出一个新的容器，然后新的容器里添加元素，添加完元素之后，
 * 再将原容器的引用指向新的容器。这样做的好处是我们可以对CopyOnWrite容器进行并发的读，
 * 而不需要加锁，因为当前容器不会添加任何元素。所以CopyOnWrite容器也是一种读写分离的思想，读和写不同的容器。
 *
 * 功能:读写分离，写入，删除时加ReentrantLock锁，读取时不加锁，共享读。读写使用的不是一个容器，适用读多写少的场景，线程安全。
 * 和Vector、ArrayList的比较。Vector读写都加锁，不能共享读。ArrayList无锁，线程不安全。
 *
 * ========================LinkedList===============================
 * LinkedList:双向链表  Node(first) <-> Node <-> Node <-> Node <->Node(last)
 *    Node<E>{
 *      E item;
 *      Node<E> prev;前驱节点
 *      NOde<E> next;后继节点
 *    }
 * ========================Stack<E> extends Vector<E>========================
 * 在Vector的基础上封装的几个方法
 *     1.1 push(E e){
 *           //就是向Vector的Object[]添加元素
 *         }
 *     1.2 peek(){
 *           return elementData[size()-1];
 *           //即返回Object[]数组最后一个元素
 *         }
 *     1.3 pop(){
 *            Object obj = elementData[size()-1];
 *            removeElementAt[size()-1];
 *            return obj;
 *            //返回Object[]数组最后一个元素,并从Object[]中删除此元素;
 *         }
 * ========================RoleList extends ArrayList<Object>===================
 *
 * @Author: tangzh
 * @Date: 2018/11/14$ 下午3:44$
 * 1.迭代器
 **/
public class ListLearning {
    private static List datasA,datasB,datasC,datasD,datasE,datasF;
    private ArrayList arrayList = new ArrayList();
    private Vector vector = new Vector();
    private CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
    private LinkedList linkedList = new LinkedList();
    private RoleList roleList = new RoleList();
    private Stack stack = new Stack();

    @SuppressWarnings("uncheck")
    public static void main(String[] args) {
        datasC = new Vector(2){{
            add(1);add(2);add(3);add(4);
        }};
        datasD = new CopyOnWriteArrayList(){{
            add("1");add("2");add("3");
        }};
        datasD.remove("2");
        datasE = new LinkedList();
        datasA = new ArrayList();
        datasF = new ArrayList(100);
        datasB = new ArrayList(10){{
            add("蒙多");add(":");
        }};
        datasA.add("老");
        datasA.add("飘");
        datasA.add("是");
        datasA.add("个");
        datasA.add("大");
        datasA.add("俩");
        datasA.add("们");
        datasA.sort(Comparator.comparingInt(Object::hashCode));

        datasB.addAll(1,datasA);
        datasA.add(1,"insert");
        Iterator iterator = datasA.iterator();
        Spliterator spliterator = datasB.spliterator();
        spliterator.forEachRemaining(o -> System.out.println(o));
        ListIterator listIterator = datasA.listIterator();

        //此处为一个死循环
//        while (listIterator.hasNext()){
//            Object next = listIterator.next();
//            System.out.println(next);
//            if("俩".equals(next)){
//                for (int i=0;i<3;i++){
//                    System.out.println(listIterator.previous());
//                }
//            }
//        }

        System.out.println("============================");
        for(int i=0;i<100;i++){
            datasF.add(i);
        }
        Spliterator spliterator1 = datasF.spliterator();//100
        Spliterator spliterator2 = spliterator1.trySplit(); //50 | 50
        Spliterator spliterator3 = spliterator2.trySplit(); //25 | 25
        Spliterator spliterator4 = spliterator1.trySplit(); //25 | 25
        MyThread myThread1 = new MyThread(spliterator1);myThread1.setName("001:");
        MyThread myThread2 = new MyThread(spliterator2);myThread2.setName("002:");
        MyThread myThread3 = new MyThread(spliterator3);myThread3.setName("003:");
        MyThread myThread4 = new MyThread(spliterator4);myThread4.setName("004:");
        myThread1.start();
        myThread2.start();
        myThread3.start();
        myThread4.start();
    }
    static class MyThread extends Thread{
        Spliterator spliterator;
        MyThread(Spliterator spliterator){
            this.spliterator = spliterator;
        }
        @Override
        public void run() {
            spliterator.forEachRemaining(o-> System.out.println(Thread.currentThread().getName()+"遍历："+o));
        }
    }
}


