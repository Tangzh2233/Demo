package com.edu.Java基础重温.集合;

import com.edu.util.DateUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * [HashMap,LinkedHashMap,WeakHashMap,HashTable,ConcurrentMap,HashSet]
 *
 * HashMap【JDK1.8】:底层实现Node<K,V>[] 数组+链表(单向)+红黑树即 Object[Node<K,V>]。
 *  Node<K,V>{
 *     int hash;
 *     K key;
 *     V value;
 *     Node<K,V> next;
 *  }
 *  1.第一次put时进行map初始化，默认Node<K,V>[16]。
 *  2.【JDK1.8】HashMap的Node[]大小总为2的幂次方。new HashMap(7)结果为Node<K,V>[8]||new HashMap(12)==Node<K,V>[16]
 *  3.KeySet并不是一个HashMap的key的Set集合,而是使用一系列方法对Node<K,V>[]进行有限的操作。keyset表现形式上是只能操作key,
 *    我们通常说，keySet()返回所有的键，values()返回所有的值，其实是不太对的，因为无论是keySet()和values()，其实都没有实质的内容。
 *    他们前者返回了一个Set，后者返回了一个Collection，
 *    但是Set和Collection都只是接口，既然是接口，那就大有文章可以做。
 *    很重要的一点就是，接口可以不是new someClass()的来的，
 *    也就是说，它可以不对应与一个类，而只提供一些方法。实际上，
 *    HashMap中所有的数据都是放在一个Node<E,V>[]的数组中的，
 *    而返回的Set接口也好，Collection也罢，都是直接针对这个Node<E,V>[]数组的，
 *    所以，当使用返回的Set接口或者Collection接口进行操作是，实际上操作的还是那个Node<E,V>[]数组。
 *    但是，返回的Collection只能做有限的操作，限定哪些呢？一句话总结就是：只能读，不能写，但能删能清"
 *  4.put的基本流程
 *      4.1:判断是否初始化，否?初始化
 *      4.2:i = hash(key)&(table.length-1);Node[i]==null?putData:4.3【Node[i]==null直接put】
 *      4.3:Node[i]!=null;(p.hash==hash&&(p.key==key||p.key.eq(key)))==true?update{key相同更新value}:4.4
 *      4.4:(Node[i] instanceof TreeNode)==true?putTreeNode:4.5 {判断节点是否为二叉树节点}
 *      4.5:for循环进行链表插入,若key相同更新value。如果循环次数>=8,将此链表转为红黑树
 *  5.Node链表的数量>=8 && table.length>=64时转化为红黑树(具体见treeifyBin方法)
 *    if(binCount >= TREEIFY_THRESHOLD{8} - 1) treeifyBin(tab, hash);
 *  6.Node个数>Node[].length*loadFactor数组进行x2扩容。
 *  7.getNode(hash,key)这个方法是map取值的核心:
 *          {判断key对应的table下表处是否有值}
 *      7.1:(table!=null && table.length>0 && table[(length-1)&hash]!=null)==true?7.2:null
 *          {如果有值则判断key及hash是否一致,一致则返回当前节点，否则链表循环对比7.2的条件}
 *      7.2:(table[i].hash==hash && (table[i].key==key||key.eq(table[i].key)))
 *  8.resize()的基本流程
 *      8.1:
 * @Author: tangzh
 * @Date: 2018/11/16$ 下午5:18$
 **/
public class MapLearning {
    private static HashMap hashMap = new HashMap(4) ;
    private static WeakHashMap weakHashMap;
    private static LinkedHashMap linkedHashMap;
    private static Hashtable hashtable;
    private static ConcurrentHashMap concurrentHashMap;
    private static HashSet hashSet;

    public static void main(String[] args) {
        hashMap.put("id","5201314");
        hashMap.put("di","ah2345");
        hashMap.put("name","hahahah");
        hashMap.put("pwd","1234");
        hashMap.put("ordNo","a132123");
        hashMap.put("amt","32.12");
        Set set = hashMap.keySet();
        set.forEach(o -> System.out.println(o));
        Iterator iterator = set.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
        System.out.println(DateUtil.getPreOrNextDateStamp(new Date(),Calendar.MINUTE,2).getTime());

    }
}
