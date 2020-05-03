package com.edu.JavaLearning.juc.aqs;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

/**
 * @author Tangzhihao
 * @date 2018/3/30
 */

public class Client {
    public static void main(String[] args) {
        System.out.println(new BigDecimal("1.2"));
        BlockingQueue<Integer> deque = new LinkedBlockingQueue<>();
        Produer produer = new Produer(deque);
        Consumer consumer = new Consumer(deque);
        Consumer consumer1 = new Consumer(deque);
        new Thread(produer, "Producer").start();
        new Thread(consumer, "Consumer").start();
        new Thread(consumer1, "Consumer2").start();

        Client client = new Client();
        client.getResult("1,8,6,2,5,4,8,3,7");

    }

    private void getResult(String input) {
        String[] split = input.split(",");
        List<Item> nodes = new ArrayList<>();
        List<Integer> result = new ArrayList<>();
        for (int i = 1; i <= split.length; i++) {
            nodes.add(new Item(i, Integer.valueOf(split[i - 1])));
        }
        for (Item item : nodes) {
            result.add(getMax(item, nodes));
        }
        List<Integer> collect = result.stream().sorted((o1, o2) -> o2 - o1).collect(Collectors.toList());
        System.out.println(collect.get(0));
    }

    private int getMax(Item source, List<Item> target) {
        int max = 0;
        for (Item item : target) {
            if (item.equals(source)) {
                continue;
            }
            int x = returnX(item.getX(), source.getX());
            int y = returnY(item.getY(), source.getY());
            if (x * y > max) {
                max = x * y;
            }
        }
        return max;
    }

    private int returnX(int x, int y) {
        int max = x >= y ? x : y;
        if (max == x) {
            return x - y;
        } else {
            return y - x;
        }
    }

    private int returnY(int x, int y) {
        return x >= y ? y : x;
    }

    static class Item {
        private int x;
        private int y;

        public Item(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public boolean equals(Item obj) {
            return this.getX() == obj.getX() && this.getY() == obj.getY();
        }
    }


}
