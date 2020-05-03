package com.edu.JavaLearning.collection;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.unidal.helper.Files;
import org.unidal.helper.Urls;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.Collator;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;


/**
 * @author tangzh
 * @version 1.0
 * @date 2019/9/25 5:29 PM
 **/
public class Jdk1_8 implements Serializable {

    public static final long serialVersionUID = -3739457594546398735L;
    private static int maxSize = 128;
    private static ConcurrentHashMap<String,Class> classCache = new ConcurrentHashMap<>(16);


    @SuppressWarnings("unchecked")
    public static void main(String[] args) {

        ArrayList<Object> list = Lists.newArrayList();
            list.clear();
            list.forEach(item ->{
                System.out.println("hahha");
            });

        String memberId = "memberId";

        String member = "get"+memberId.substring(0,1).toUpperCase()+ memberId.substring(1);
        System.out.println(member);

        List<Order> orders = initData();
        ArrayList<String> fieldList = Lists.newArrayList("name", "price");

        List<Order> orderList = getFieldList("com.edu.JavaLearning.collection.Order", fieldList, orders);
        System.out.println(JSON.toJSONString(orderList));


        System.out.println((int) 2*0.75);

        String url = "http://cat.huidu.jiupaipay.com/cat/s/router?domain=personalServer&ip=100.109.79.170&op=json";
        try {
            InputStream inputStream = Urls.forIO().readTimeout(2000).connectTimeout(1000).openStream(url);
            String content = Files.forIO().readFrom(inputStream, "utf-8");
            System.out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> listData = new ArrayList(){
            {
                add("1");add("1");add("2");add("2");
                add("3");add("3");add("3");add("4");
            }
        };
        listData = Lists.newArrayList(Sets.newHashSet(listData));
        listData.forEach(System.out::println);
        Set<String> data = new HashSet(){
            {
                add("a");
                add("b");
                add("c");
            }
        };

        StringBuilder replaceSql = new StringBuilder();
        data.forEach(item -> replaceSql.append(item).append(","));
        System.out.println("012345".substring(0,4));



        List<Order> collect = orders.stream()
                .filter(order -> "F".equals(order.getStatus()))
                .collect(Collectors.toList());
        collect.forEach(System.out::println);

        System.out.println("====================");

        orders.removeAll(collect);
        orders.forEach(System.out::println);

        System.out.println("=====================");

        orders.stream().filter(order -> Integer.valueOf(order.getPrice())>200)
                .sorted() //实现Comparable,使用compareTo排序[实际compareTo还是调用Order内部方法]
                .sorted(Comparator.comparing(Order::getSeq)) //直接指定排序方法
                //peek 类似forEach,但是forEach是Terminal类型的操作
                .peek(order -> order.setStatus(order.getStatus().toLowerCase()))
                //去重
                .distinct()
                .map(order -> order.getSeq() + 100)
                //最多取4个元素
                .limit(4)
                //跳过前2个元素
                .skip(2);
//                .collect(Collectors.toMap(Order::getName,Order::getPrice));
//                .forEach(System.out::println);

        //Lambda表达式+函数式接口
        MyLambda myLambda = param -> {
            Integer integer = Integer.valueOf(param);
            System.out.println(integer+10);
        };
        myLambda.test("11");
        myLambda.test1();

        new Thread(() -> System.out.println("函数式接口")).start();

        //方法的引用
        //构造器引用
        System.out.println(MyLambda.create(MyLambdaImpl::new));
        //静态方法引用
        MyLambda lambda = MyLambda.create(MyLambdaImpl::new);
        List<MyLambda> lambdas = Arrays.asList(lambda);
        lambdas.forEach(MyLambda::staticMethod);
    }

    public static void collectors(){
        List<Order> orders = initData();
        Double collect = orders.stream().collect(Collectors.averagingDouble(Order::getSeq));
        System.out.println(collect);
    }


    public static List<Order> initData(){
        Order order = new Order("2017", "主板", "1333", "S",1);
        Order order1 = new Order("2018", "CPU", "1000", "F",2);
        Order order2 = new Order("2019", "显卡", "2345", "S",3);
        Order order3 = new Order("2020", "屏幕", "1433", "S",4);
        Order order4 = new Order("2016", "键盘", "699", "S",5);
        Order order5 = new Order("2015", "鼠标", "199", "S",6);
        Order order6 = new Order("2014", "机箱", "329", "F",7);
        Order order7 = new Order("2015", "数据线", "15", "F",8);
        Order order8 = new Order("2016", "散热器", "88", "S",9);
        List<Order> orderList = new ArrayList<>();
        orderList.add(order);
        orderList.add(order1);
        orderList.add(order2);
        orderList.add(order3);
        orderList.add(order4);
        orderList.add(order5);
        orderList.add(order6);
        orderList.add(order7);
        orderList.add(order8);
        orderList.add(order1);
        return orderList;
    }

    @SuppressWarnings("unchecked")
    public static  <T> List<T> getFieldList(String className, List<String> fieldList, List<T> data) {
        List<T> returnData = new ArrayList<>();
        try {
            Class<T> tClass = (Class<T>) classCache.get(className);
            if (tClass == null) {
                tClass = (Class<T>) Class.forName(className);
                if (classCache.size() <= maxSize) {
                    classCache.putIfAbsent(className, tClass);
                }
            }
            for (T t : data) {
                T t1 = tClass.newInstance();
                for (String field : fieldList) {
                    String getMethodName = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
                    String setMethodName = "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
                    Method getMethod = tClass.getMethod(getMethodName);
                    Method setMethod = tClass.getMethod(setMethodName, getMethod.getReturnType());
                    setMethod.invoke(t1, getMethod.invoke(t));
                }
                returnData.add(t1);
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
            return data;
        }
        return returnData;
    }

}

/**
 * 默认方法和静态方法不会破坏函数式接口的语义
 *
 * 接口正常只能定义变量和抽象方法
 * 但是1.8的Default关键字可以让接口添加具体的实现方法
 * 及静态方法,可以重写也可以不重写,但是抽象方法必须重写
 *
 * ️函数接口指的是 ⚠️只有一个函数的接口⚠️，这样的接口可以隐式转换为Lambda表达式。
 * java.lang.Runnable和java.util.concurrent.Callable是函数式接口的最佳例子。
 * 在实践中，函数式接口非常脆弱：只要某个开发者在该接口中添加一个函数，则该接口就不再是函数式接口进而导致编译失败。
 * 为了克服这种代码层面的脆弱性，并显式说明某个接口是函数式接口，Java 8 提供了一个特殊的注解@FunctionalInterface
 * （Java 库中的所有相关接口都已经带有这个注解了）
 */
@FunctionalInterface
interface MyLambda {

    void test(String param);

    default void test1(){
        System.out.println("I am Default Method");
    }

    static MyLambda create(final Supplier<MyLambda> supplier){
        return supplier.get();
    }

    static String staticMethod(final MyLambda lambda){
        return "I am StaticMethod";
    }

}

class MyLambdaImpl implements MyLambda{

    private Integer seq;

    @Override
    public void test(String param) {

    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }
}

class Order implements Comparable<Order>{
    private Integer seq;
    private String date;
    private String name;
    private String price;
    private String status;

    public String getPrice() {
        return price;
    }

    public Order(String date, String name, String price, String status, Integer seq) {
        this.date = date;
        this.name = name;
        this.price = price;
        this.status = status;
        this.seq = seq;
    }

    public Order(){}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    @Override
    public String toString() {
        return "Order{" +
                "seq=" + seq +
                ", date='" + date + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    /**
     * 依据汉语拼音排序
     * @param o
     * @return
     */
    @Override
    public int compareTo(@NotNull Order o) {
        return Collator.getInstance(Locale.CHINESE).compare(this.getName(),o.getName());
    }
}
