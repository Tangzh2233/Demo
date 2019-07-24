package com.edu.JavaLearning.collection;

import sun.misc.Unsafe;
import java.lang.ref.PhantomReference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * [HashMap,HashTable,LinkedHashMap,WeakHashMap,ConcurrentHashMap,HashSet,TreeMap]
 *
 * 【HashMap、LinkedHashMap、TreeMap的使用场景】https://www.jianshu.com/p/dd746074f390
 *
 * ================HashMap extends AbstractMap<K,V>
 *                         implements Map<K,V>, Cloneable, Serializable=============
 *(Mark:对红黑树部分理解的很浅)
 *
 * HashMap【JDK1.8】:底层实现Node<K,V>[] 数组+链表(单向)+红黑树即 Object[Node<K,V>]。
 *  Node<K,V> implement Map.Entry<K,V>{
 *     int hash;
 *     K key;
 *     V value;
 *     Node<K,V> next;
 *  }
 *  1.第一次put时进行map初始化，默认Node<K,V>[16]。key,value可以为null;线程不安全。
 *  2.【JDK1.8】HashMap的Node[]大小总为2的n次方。new HashMap(7)结果为Node<K,V>[8]||new HashMap(12)==Node<K,V>[16]
 *  3.KeySet并不是一个实际存在的HashMap的key的Set集合,而是使用一系列方法对Node<K,V>[]进行有限的操作。keyset表现形式上是只能操作key,
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
 *  6.Node个数>Node[].length*loadFactor时,数组进行x2扩容。
 *  7.getNode(hash,key)这个方法是map取值的核心:
 *          {判断key对应的table下表处是否有值}
 *      7.1:(table!=null && table.length>0 && table[(length-1)&hash]!=null)==true?7.2:null
 *          {如果有值则判断key及hash是否一致,一致则返回当前节点，否则链表循环对比7.2的条件}
 *      7.2:(table[i].hash==hash && (table[i].key==key||key.eq(table[i].key)))
 *  8.resize()的基本流程[threshold属性两个作用1、初始化时map的大小 2、map扩容的size临界值]
 *      8.1:保存老的oldTab=table、oldCap=length、oldThr=threshold
 *      8.2:oldCap>0判断是否为首次实例化?yes[oldthr>0?yes[newCap=oldThr]:no[newCap=default(16),newThr=16*0.75]]
 *      8.3:不是初始化newCap=oldCap<<1,newThr=newCap*扩容因子
 *      8.4:以上为table扩容,扩容以后开始数据迁移
 *      8.5:for循环oldTab. e=table[j]!=null&&e.next==null 则newTab[e.hash&(newCap-1)]=e;
 *      8.6:else if(e instanceof TreeNode):红黑树的环节
 *      8.7:if(e.hash&oldCap==0)newTab[j] else: newTab[j+oldCap]
 * ============树的左旋右旋============
 * 对X节点左旋-->>将X的右孩子Y置为X的父节点,同时保证树的特性
 * 对X节点右旋-->>将X的左孩子Z置为X的父节点,同时保证树的特性
 * https://www.cnblogs.com/skywang12345/p/3245399.html#a3
 *
 * ===============HashTable extends Dictionary<K,V>
 *                          implements Map<K,V>, Cloneable, java.io.Serializable ==============
 * 【JDK1.8】HashTable:数组+单向链表。并没有转化为红黑树的操作。具体实现没啥特别的操作。
 * Entry<K,V> implement Map.Entry<K,V>{
 *      final int hash;
 *      final K key;
 *      V value;
 *      Entry<K,V> next;
 * }
 *  1.默认初始化Entry<K,V>[11]大小为11,加载因子0.75f。Key,Value不能为null。使用synchronized保证线程安全
 *  2.put操作与HashMap的区别(应该是HashMap1.7和1.8版本的差别)
 *      2.1:key,value不能为null;
 *      2.2:没有红黑树结构;
 *      2.3:若table[index]!=null,为链表形式,newEntry添加在链表头节点，而非HashMap添加在尾节点;
 *
 * ================LinkedHashMap extends HashMap<K,V>
 *                               implements Map<K,V> ===================
 *【JDK1.8】LinkedHashMap
 * Entry<K,V> extends HashMap.Node<K,V>{
 *     final int hash;
 *     final K key;
 *     V value;
 *     Entry<K,V> next;
 *     Entry<K,V> before;
 *     Entry<K,V> after;
 * }
 *  1.LinkedHashMap出现的作用:保存数据的插入顺序.就是在HashMap.Entry<K,V>的基础上添加两个befor和after节点,类变量保存tail和head节点。
 *  2.LinkedHashMap因为继承HashMap,所以初始化的和HashMap流程基本一致;
 *  3.继承HashMap重写部分方法，达到想要的效果。put操作中重写了newNode(hash,k,v,entry)+afterNodeAccess(entry)+afterNodeInsertion(boolean)
 *  4.put基本流程
 *      4.1:@1-key不存在.重写LinkedHashMap.Entry<K,V> p = newNode(...)
 *             链表尾部添加新节点last=tail;tail=p;p.before=last;last.after=p;
 *          @2-key已经存在,更新value+afterNodeAccess(entry).
 *      4.2:afterNodeAccess(entry),将entry设置为尾节点,用于保证修改顺序。
 *  5.网络资料:https://www.cnblogs.com/leesf456/p/5248868.html
 *
 * =================HashSet extends AbstractSet<E>
 *                          implements Set<E>, Cloneable, java.io.Serializable===============
 * 【JDK1.8】HashSet<E>内部就是一个value为new Object()的HashMap。
 *  private transient HashMap<E,Object> map;
 *  private static final Object PRESENT = new Object();
 *    1.add(E e){
 *        return map.put(e,PRESENT);
 *    }
 *
 * ==================WeakHashMap extends AbstractMap<K,V>
 *                               implements Map<K,V>==========================
 *  1.特点:HashMap.put("id","001"); WeakHashMap.put("id","001");在Sys.gc()后,HashMap不变,
 *        WeakHashMap.size()==0,其中元素将被gc。若想让元素不被gc,key即"id"需被显示引用。eg:
 *        String key="id",WeakHashMap(key,"001")。如此不会被gc。
 *
 *  2.实现原理:"WeakHashMap的key为弱引用,当key被gc后，将放进队列queue中(垃圾回收的一种通知机制),
 *            下次操作WeakHashMao之前都会遍历一下queue,若存在相关的key,则poll,然后删除Map中的相关key-value"。
 *  3.HashIterator 迭代器的实现可以看下，涉及遍历过程中key被gc的一个情况。
 *
 * =======================ThreadLocal<T>.ThreadLocalMap========================
 *
 * [JDK1.8]
 * static class ThreadLocalMap{
 *   static class Entry extends WeakReference<ThreadLocal<?>> {
 *        Object value;
 *        Entry(ThreadLocal<?> k, Object v) {
 *            super(k);
 *            value = v;
 *       }
 *   }
 * }实际存储容器为Entry[16]默认大小为16的Entry数组。Entry中的key为ThreadLocal本身，value为输入数据
 *  1.public void set(T value){
 *      Thread t = Thread.currentThread();
 *      ThreadLocalMap map = getMap(t);
 *      if (map != null)
 *         map.set(this, value);
 *      else
 *      createMap(t, value);
 *  }注:为数组，不存在链表结构，出现table[i]!=null的时后i+1以此类推。具体参看ThreadLocal.ThreadMap.set(..)+nextIndex(..)方法。
 *  每一个线程拥有一个类型为ThreadLocal.ThreadLocalMap的threadLocals变量。
 *  ThreadLocal.set(V v)就是往当前线程的threadLocals变量中添加key=this,value=v的数据。
 *
 * *********使用ThreadLocal时要注意内存溢出问题**********
 *
 * =========================TreeMap extends AbstractMap<K,V>
 *                          implements NavigableMap<K,V>, Cloneable=======================
 * 树节点结构
 * !!!!!!!!=====树的左旋右旋=====!!!!!!!
 *  对X节点左旋:
 *      -->>将X的右孩子Y置为X的父节点;
 *      -->>将Y的左孩子置为X的右孩子;
 *      -->>将X的父节点置为Y的父节点;
 *      同时保证树的特性
 *  对X节点右旋:
 *      -->>将X的左孩子Z置为X的父节点;
 *      -->>将Z的右孩子置为X的左孩子;
 *      -->>将X的父节点置为Z的的父节点
 *      同时保证树的特性
 *https://www.cnblogs.com/skywang12345/p/3245399.html#a3
 *
 * static final class Entry<K,V> implements Map.Entry<K,V> {
 *         K key;
 *         V value;
 *         Entry<K,V> left;
 *         Entry<K,V> right;
 *         Entry<K,V> parent;
 *         boolean color = BLACK;
 * }就是一个典型的红黑树结构,对于红黑树的学习非常有帮助。
 * !!!========put是正常操作。重要的是新节点添加以后红黑书树的修正操作======!!!
 * 1.fixAfterInsertion(e) 默认新节点color=red
 *   case1:(e==null||e==root||e.parent.color!=red)-->> 直接添加,root.color==black;
 *   case2:!(case1)
 *      -->>(parentof(e)==leftof(parentof(parentof(e)))):当前节点的父节点为祖父节点的左孩子
 *          -->>2.1获取当前节点的叔叔节点 Entry<K,V> y = rightOf(parentOf(parentOf(e)));
 *              -->> 类似如下:只是左右操作是相反的
 *      -->>parentof(e)==rightof(parentof(parentof(e))):当前节点的父节点为祖父节点的右孩子
 *          -->> 获取当前节点的叔叔节点  Entry<K,V> y = leftOf(parentOf(parentOf(e)));
 *              -->>if(color(y)==red) 叔叔是红节点
 *                  -->>
 *                      setColor(parentOf(e), BLACK);       设置当前节点父节点为黑节点
 *                      setColor(y, BLACK);                 设置当前节点叔叔节点为黑节点
 *                      setColor(parentOf(parentOf(e)),RED);设置当前节点祖父节点为红节点
 *                      e = parentOf(parentOf(e));          将当前节点设置为原当前节点的祖父节点
 *              -->>else 叔叔是黑节点
 *                  -->>if(e == leftOf(parentOf(e))){       当前节点是左孩子[多做如下操作,当前节点变更为父节点，并进行右旋]
 *                          e = parentOf(e);                将当前节点设置为原当前节点的父节点
 *                          rotateRight(e);                 右旋当前当前节点
 *                       }
 *                      setColor(parentOf(e), BLACK);       设置当前节点为黑节点
 *                      setColor(parentOf(parentOf(e)),RED);设置当前节点的祖父节点为红节点
 *                      rotateLeft(parentOf(parentOf(e)));  左旋当前节点的祖父节点
 * ==============这一坨操作为啥要这样做,是需要考虑的事情=================
 *
 * 【JDK1.7】=====================ConcurrentHashMap<K,V> extends AbstractMap<K,V>
 *                      implements ConcurrentMap<K,V>, Serializable=======================
 * 与HashTable的锁住这个表相比，1.7的ConcurrentHashMap采用分段锁设计，分段数量即Segment[]对象数组的大小
 * 默认设置简化版数据结构：
 *   class ConcurrentHashMap ...{
 *      Segment[16] segments;
 *      class Segment extends  ReentrantLock ...{
 *          transient int count;
 *          transient int modcount;
 *          transient int threshold;
 *          final float loadFactor;
 *          HashEntry<K,V>[2] table;
 *      }
 *      class HashEntry<K,V>{
 *          final int hash;
 *          final K k;
 *          volatile V v;
 *          volatile HashEntry<K,V> next;
 *
 *      }
 *   }
 *   1.默认初始化Segments.length=16;table.length=2;
 *     注：默认初始化时只会实例化一个segment和与之对应的HashEntry,即延迟初始化。
 *     put,get基本流程,先确认位于哪个Segment[?],再在segment中确定table[?]进行再table的链表put,get操作
 *   2.put操作简析
 *      2.1:依据key的hashcode计算出hash,然后计算出key应该在哪个segment[?]。
 *      2.2:使用unsafe根据偏移量直接判断segment[?]是否为null。
 *        2.2.1:不为null,则在此segment上进行put的相关操作
 *        2.2.2:为null则ensureSegment(?)进行对应segment初始化,初始化完成以后再进行put操作。
 *   3.ensureSegment(?)初始化指定位置"?"segment简析
 *      3.1:计算下标为?的segment偏移量,获取segments实例。long u = (k << SSHIFT) + SBASE;
 *          SSHIFT=(一通操作没看懂);SBASE=UNSAFE.arrayBaseOffset(tc)返回数组类型的第一个元素的偏移地址;
 *      3.2:使用unsafe(ss,u)判断此segment是否为空(即是否已经被其他线程实例化)
 *        3.2.1:为null。从segment[0](segment[0]是在map初始化的时候实例化的)中获取初始化new segment所需的参数-->>new HashEntry[2]
 *        3.2.2:recheck Segment[?]是否为空。new Segment();while(seg[?]==null){
 *              丧心病狂的走一步判断一次                        if (UNSAFE.compareAndSwapObject(ss, u, null, seg = s))
 *                                                          break;
 *                                                      }
 *   4.Segment实例化完成之后,进行实际的put操作。
 *      4.1:tryLock()->>获取segment中table,index=(table.length-1)&hash
 *           ->>通过unsafe获取table[index]的首元素-->>进行类似HashMap的链表put操作
 *           ->>若存在相同的key则进行value替换,不存在则new HashEntry(hash,k,v,first);
 *           -->>setEntryAt(tab, index, node)通过unsafe设置table[index]。
 *   5.rehash(HashEntry<K,V> node) 是对segment中的table进行*2扩容。因为在rehash是在segment中，已经保证了线程安全。
 *   6.remove(Object k,int hash,Object value)。类似HashMap,使用证线程安全使用tryLock()来保证。
 *   7.size()
 *      7.1:尝试两次将每个segment中的count进行累加,如果两次size没有改变则返回sum(count)。否则则锁住全表ensureSegment(j).lock()再进行sum(count)
 *      7.2:通过sum(modCount)来判断两次sum(count)有没有进行put,remove,clean操作。
 *
 *   ===================HashIterator 迭代器的实现[类似实现不难,看看源码debug一下就明白了]=================
 *   abstract class HashIterator {
 *         int nextSegmentIndex;
 *         int nextTableIndex;
 *         HashEntry<K,V>[] currentTable;
 *         HashEntry<K, V> nextEntry;
 *         HashEntry<K, V> lastReturned;
 *
 *         HashIterator() {
 *             nextSegmentIndex = segments.length - 1;
 *             nextTableIndex = -1;
 *             advance();
 *         }
 *     //advanced从segment从后向前查询到第一个不为空到元素,类变量保存相关的值
    *      final void advance() {
*             for (;;) {
*                 if (nextTableIndex >= 0) {
*                     if ((nextEntry = entryAt(currentTable,nextTableIndex--)) != null)
*                         break;
*                 }
*                 else if (nextSegmentIndex >= 0) {
*                     Segment<K,V> seg = segmentAt(segments, nextSegmentIndex--);
*                     if (seg != null && (currentTable = seg.table) != null)
*                         nextTableIndex = currentTable.length - 1;
*                 }
*                 else
*                     break;
*             }
    *      }
 *  【jdk1.8】===================ConcurrentHashMap<K,V> extends AbstractMap<K,V>
 *                         implements ConcurrentMap<K,V>====================================
 *      }
 *   }
 *   1.默认初始化Segments.length=16;table.length=2;
 *     注：默认初始化时只会实例化一个segment和与之对应的HashEntry,即延迟初始化。
 *     put,get基本流程,先确认位于哪个Segment[?],再在segment中确定table[?]进行再table的链表put,get操作
 *   2.put操作简析
 *      2.1:依据key的hashcode计算出hash,然后计算出key应该在哪个segment[?]。
 *      2.2:使用unsafe根据偏移量直接判断segment[?]是否为null。
 *        2.2.1:不为null,则在此segment上进行put的相关操作
 *        2.2.2:为null则ensureSegment(?)进行对应segment初始化,初始化完成以后再进行put操作。
 *   3.ensureSegment(?)初始化指定位置"?"segment简析
 *      3.1:计算下标为?的segment偏移量,获取segments实例。long u = (k << SSHIFT) + SBASE;
 *          SSHIFT=(一通操作没看懂);SBASE=UNSAFE.arrayBaseOffset(tc)返回数组类型的第一个元素的偏移地址;
 *      3.2:使用unsafe(ss,u)判断此segment是否为空(即是否已经被其他线程实例化)
 *        3.2.1:为null。从segment[0](segment[0]是在map初始化的时候实例化的)中获取初始化new segment所需的参数-->>new HashEntry[2]
 *        3.2.2:recheck Segment[?]是否为空。new Segment();while(seg[?]==null){
 *              丧心病狂的走一步判断一次                        if (UNSAFE.compareAndSwapObject(ss, u, null, seg = s))
 *                                                          break;
 *                                                      }
 *   4.Segment实例化完成之后,进行实际的put操作。
 *      4.1:tryLock()->>获取segment中table,index=(table.length-1)&hash
 *           ->>通过unsafe获取table[index]的首元素-->>进行类似HashMap的链表put操作
 *           ->>若存在相同的key则进行value替换,不存在则new HashEntry(hash,k,v,first);
 *           -->>setEntryAt(tab, index, node)通过unsafe设置table[index]。
 *   5.rehash(HashEntry<K,V> node) 是对segment中的table进行*2扩容。因为在rehash是在segment中，已经保证了线程安全。
 *   6.remove(Object k,int hash,Object value)。类似HashMap,使用证线程安全使用tryLock()来保证。
 *   7.size()
 *      7.1:尝试两次将每个segment中的count进行累加,如果两次size没有改变则返回sum(count)。否则则锁住全表ensureSegment(j).lock()再进行sum(count)
 *      7.2:通过sum(modCount)来判断两次sum(count)有没有进行put,remove,clean操作。
 *
 *   ===================HashIterator 迭代器的实现[类似实现不难,看看源码debug一下就明白了]=================
 *   abstract class HashIterator {
 *         int nextSegmentIndex;
 *         int nextTableIndex;
 *         HashEntry<K,V>[] currentTable;
 *         HashEntry<K, V> nextEntry;
 *         HashEntry<K, V> lastReturned;
 *
 *         HashIterator() {
 *             nextSegmentIndex = segments.length - 1;
 *             nextTableIndex = -1;
 *             advance();
 *         }
 *     //advanced从segment从后向前查询到第一个不为空到元素,类变量保存相关的值
    *      final void advance() {
*             for (;;) {
*                 if (nextTableIndex >= 0) {
*                     if ((nextEntry = entryAt(currentTable,nextTableIndex--)) != null)
*                         break;
*                 }
*                 else if (nextSegmentIndex >= 0) {
*                     Segment<K,V> seg = segmentAt(segments, nextSegmentIndex--);
*                     if (seg != null && (currentTable = seg.table) != null)
*                         nextTableIndex = currentTable.length - 1;
*                 }
*                 else
*                     break;
*             }
    *      }
 *  【jdk1.8】===================ConcurrentHashMap<K,V> extends AbstractMap<K,V>
 *                         implements ConcurrentMap<K,V>====================================
 *
 *
 * @Author: tangzh
 * @Date: 2018/11/16$ 下午5:18$
 **/
public class MapLearning {
    private static HashMap hashMap = new HashMap(19) ;
    private HashMap unsafeMap = new HashMap();
    private static Hashtable hashtable = new Hashtable();
    private static LinkedHashMap linkedHashMap = new LinkedHashMap();
    private static HashSet hashSet = new HashSet();
    private static WeakHashMap weakHashMap = new WeakHashMap();
    private static TreeMap treeMap = new TreeMap();
    private static ThreadLocal<Map> context = new ThreadLocal<>();
    private static ThreadLocal<String> context1 = new ThreadLocal<>();
    private static ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap(8);

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws NoSuchFieldException {
        hashMap.put(null,null);hashMap.put("id","5201314");
        hashMap.put("di","ah2345");hashMap.put("name","hahahah");
        hashMap.put("pwd","1234");hashMap.put("ordNo","a132123");
        hashMap.put("amt","32.12");
//      ==================================
        hashtable.put("id","001");hashtable.put("name","tang");
        hashtable.put("pwd","qwer1234");hashtable.put("age",18);
//      ===================================
        linkedHashMap.put("id",002);linkedHashMap.put("name","zhi");
        linkedHashMap.put("pwd","qwer1234");linkedHashMap.put("age",18);
//      ===================================
        weakHashMap.put("id",003);weakHashMap.put("name","1234");
//      ===================================
        context.set(hashMap);context1.set("ThreadLocal");
//      ==============WeakHashMap测试区===================
        LocalContextCache<String,String> cache = new LocalContextCache<>(100);
        cache.put(new String("key1"),"value1");
        cache.put("key2","value2");
        cache.put("key3","value3");
        System.out.println(cache.size());
        System.gc();
        System.out.println(cache.size());
//      ===================TreeMap===================
        ValueCompator<String, String> valueCompator = new ValueCompator<String, String>(treeMap);
        treeMap.put("1","9");treeMap.put("2","8");
        treeMap.put("3","7");
        treeMap.put("4","6");treeMap.put("5","5");treeMap.put("6","4");
        treeMap.put("7","3");treeMap.put("8","2");treeMap.put("9","1");
//      ================ConcurrentHashMap===================
        concurrentHashMap.put("id","001");

//      ====================================================
        System.out.println(context.get());
        Iterator iterator2 = linkedHashMap.keySet().iterator();
        while (iterator2.hasNext()){
            System.out.println(iterator2.next()+",");
        }

        Iterator iterator1 = hashtable.entrySet().iterator();
        while (iterator1.hasNext()){
            System.out.print(hashtable.get(iterator1.next())+",");
        }
        System.out.println();
        Set set = hashMap.keySet();
        set.forEach(o -> System.out.print(o+","));
        System.out.println();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()){
            System.out.print(hashMap.get(iterator.next())+",");
        }

        //Unsafe测试
        MapLearning mapInstance = new MapLearning();
        mapInstance.UnsafeTest();
    }

    @SuppressWarnings("unchecked")
    private void UnsafeTest(){
        Map map = (Map)unsafe.getObject(this, HASHMAP_OFFSET);
        map.put("unsafe","unsafe");
        System.out.println(map.toString());
    }
    private static Unsafe unsafe;
    private static final long HASHMAP_OFFSET;
    //初始化unsafeMap在MapLearning.class的偏移量
    static {
        try {
            unsafe = getUnsafeInstance();
            Class a = MapLearning.class;
            HASHMAP_OFFSET = unsafe.objectFieldOffset(a.getDeclaredField("unsafeMap"));
        } catch (NoSuchFieldException e) {
            throw new Error(e);
            //此处throw的原因：
            //final a = 2;此时final变量在类加载的准备阶段就已经完成赋值;
            //但是若 final b;这种情况下b的赋值必须在Construct或静态块中完成;
            //否则就会报错。当static块中使用try{}catch{}时,若不抛出异常则程序
            //可以继续运行,这是不被允许的,因为它不能确定final一定被赋值了。
        }

    }

    /**
      * @description:获取Unsafe实例
    **/
    private static Unsafe getUnsafeInstance(){
        Unsafe unsafe = null;
        try {
            Class<?> aClass = Class.forName("sun.misc.Unsafe");
            Constructor<?> constructor = aClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            unsafe = (Unsafe)constructor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return unsafe;
    }

}
/**
  * @description:强引用,软引用,弱引用，虚引用
  * @author:   tangzh
  * @date 2018/12/3 3:46 PM
**/
class LocalContextCache<K,V>{
    private int size;
    private SoftReference<String> soft;
    private WeakReference<String> weak;
    private PhantomReference<String> phantom;
    private WeakHashMap<K,V> context;

    LocalContextCache(int size){
        this.size = size;
        context = new WeakHashMap<>(size);
    }

    public void put(K k,V v){
        if(k==null||v==null){
            throw new NullPointerException();
        }
        this.context.put(k,v);
    }

    public V get(K k){
        if(k==null){
            throw new NullPointerException();
        }
        return this.context.get(k);
    }
    public int size(){
        return this.context.size();
    }

}
class ValueCompator<K,V> implements Comparator<K>{

    private Map<K,V> map;

    ValueCompator(Map<K,V> map){
        this.map = map;
    }

    @Override
    public int compare(K o1, K o2) {
        if(map==null){
            return 0;
        }
        V value1 = map.get(o1);
        V value2 = map.get(o2);
        Comparable<? super V> v = (Comparable<? super V>) value1;
        return v.compareTo(value2);
    }
}

