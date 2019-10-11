package com.edu.JavaLearning.collection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author tangzh
 * @version 1.0
 * @date 2019/9/25 5:29 PM
 **/
public class Jdk1_8 {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        List<Order> orders = initData();


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

}

class Order implements Comparable<Order>{
    private Integer seq;
    private String date;
    private String name;
    private String price;
    private String status;

    public Order(String date, String name, String price, String status,Integer seq) {
        this.date = date;
        this.name = name;
        this.price = price;
        this.status = status;
        this.seq = seq;
    }

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

    public String getPrice() {
        return price;
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

    @Override
    public int compareTo(Order o) {
        return Integer.valueOf(o.getPrice()) - Integer.valueOf(this.getPrice());
    }
}