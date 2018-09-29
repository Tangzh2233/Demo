package com.edu.JavaLearning.jdktest;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Administrator on 2017/8/31.
 */
public class ListTest implements Serializable{
    static final int MAXIMUM_CAPACITY = 1 << 30;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    public static void main(String[] args) {

        removeNode();


        //链表和数组
        ArrayList<String> a1 = new ArrayList<>();
        LinkedList<String> a = new LinkedList<>();
        List<String> b = new LinkedList<>();
        a.addFirst("H");
        a.getLast();
        a.add("A");
        a.add("C");
        a.add("E");
        b.add("B");
        b.add("D");
        b.add("F");
        b.add("G");
        System.out.println(a.toString());
        ListIterator<String> aIter = a.listIterator();
        ListIterator<String> bIter = b.listIterator();
        //ab合并
        while(aIter.hasNext()){
            if(bIter.hasNext()){
                bIter.next();
                bIter.add(aIter.next());
            }
        }
        System.out.println(b.toString()+"hahahha");
        //
        bIter = b.listIterator(); //没有这一步的话，bIter指向依旧停留在上一个wile的操作的位置
        while(bIter.hasNext()){
            bIter.next();
            if(bIter.hasNext()){
                bIter.next();
                bIter.remove();
            }
        }
        System.out.println(b.toString());

        //散列集
        HashSet<String> aset = new HashSet<>();
        HashMap<String,String> hashMap = new HashMap<>();
        aset.add("tang");
        aset.add("zhi");
        aset.add("hao");

        hashMap.put(null,"wari");
        hashMap.put("tang","22");
        hashMap.put("liu","21");
        hashMap.put("zhang","23");
        hashMap.put("tang","30");
        hashMap.put(null,"wari2");
        hashMap.put("Eric", "20");
        hashMap.put("John", "21");
        hashMap.put("LiLei", "19");

        System.out.println(hashMap.toString());

        System.out.println(aset.hashCode());
        Iterator<String> iterator = aset.iterator();
        while(iterator.hasNext()){
            String next = iterator.next();
            System.out.println(next);
        }
        int caipital = 1;

        System.out.println(MAXIMUM_CAPACITY+""+DEFAULT_LOAD_FACTOR);

        class entry<T,G>{
            T t;
            entry<T,G> next;

        }

    }
    public static void removeNode(){
        List<String> names = new ArrayList<>();
        /*names.add("tang");
        names.add("zhang");
        names.add("yu");
        names.add("liu");*/
        Iterator<String> iterator1 = names.iterator();
        while (iterator1.hasNext()){

        }
        System.out.println(names.size());
        for (int i=0;i<names.size();i++){
            if("tang".equals(names.get(i))){
                names.remove(i);
            }
        }



        Iterator<String> iterator = names.iterator();
        while (iterator.hasNext()){
            String next = iterator.next();
            if("tang".equals(next)){
                iterator.remove();
            }
        }
    }
}
