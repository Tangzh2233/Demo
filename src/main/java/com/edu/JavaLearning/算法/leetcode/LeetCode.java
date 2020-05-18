package com.edu.JavaLearning.算法.leetcode;

import lombok.Data;

import java.util.*;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/4/1 12:53 PM
 **/
public class LeetCode {


    public static void main(String[] args) {
//        int[] data = new int[]{1,3,5,7,9,44,51,78,87,89};
//        int[] param = new int[]{2,5,9,44,67,90,100};
//        System.out.println(JSON.toJSONString(mergeData(data,param)));
        System.out.println(getMaxLength("abcabcdefg"));
//        turnListNodeDiGui(initListNodeData())
        int[] data = new int[]{1, 8, 6, 2, 5, 4, 8, 3, 10};
        System.out.println(maxArea(data));
    }

    /**
     * 给定一个整数数组 nums 和一个目标值 target，
     * 请你在该数组中找出和为目标值的那两个整数，并返回他们的数组下标。
     *
     * @param data
     * @param target
     * @return
     */
    public static int[] sum(int[] data, int target) {
        int[] result = new int[2];
        for (int i = 0; i < data.length; i++) {
            int num1 = data[i];
            for (int j = i + 1; j < data.length; j++) {
                if (num1 + data[j] == target) {
                    result[0] = i;
                    result[1] = j;
                    return result;
                }
            }
        }
        return result;
    }

    public static int[] sumForMap(int[] data, int target) {
        int[] result = new int[2];
        Map<Integer, Integer> mapData = new HashMap<>();
        for (int i = 0; i < data.length; i++) {
            int temp = target - data[i];
            if (mapData.containsKey(temp)) {
                result[0] = i;
                result[1] = mapData.get(temp);
                return result;
            }
            mapData.put(data[i], i);
        }
        return result;
    }

    /**
     * data[i] + data[k] + data[j] + .... = target
     * @param data
     * @param target
     * @return
     */
    public static int[] sumForMaps(int[] data, int target) {
        int[] result = new int[2];
        Map<Integer, Integer> mapData = new HashMap<>();
        for (int i = 0; i < data.length; i++) {
            int temp = target - data[i];
            if (mapData.containsKey(temp)) {
                result[0] = i;
                result[1] = mapData.get(temp);
                return result;
            }
            mapData.put(data[i], i);
        }
        return result;
    }

    /**
     * 合并两个有序数组
     *
     * @param data
     * @param param
     * @return
     */
    public static int[] mergeData(int[] data, int[] param) {
        int[] returnData = new int[data.length + param.length];

        int i = 0, j = 0, k = 0;
        while (i < data.length && j < param.length) {
            if (data[i] > param[j]) {
                returnData[k++] = param[j++];
            } else {
                returnData[k++] = data[i++];
            }
        }
        if (i == data.length) {
            while (j < param.length) {
                returnData[k++] = param[j++];
            }
        }
        if (j == param.length) {
            while (i < data.length) {
                returnData[k++] = data[i++];
            }
        }
        return returnData;
    }

    /**
     * 给定一个字符串，请你找出其中不含有重复字符的 最长子串 的长度。
     * 输入: "abcabcbb"
     * 输出: 3 abc
     * 输入: "pwwkew"
     * 输出: 3 kew
     * <p>
     * 这道题主要用到思路是：滑动窗口
     * 用Map保存已经遍历过的值
     * 什么是滑动窗口？
     * 其实就是一个队列,比如例题中的 abcabcbb，进入这个队列（窗口）为 abc 满足题目要求，当再进入 a，队列变成了 abca，这时候不满足要求。所以，我们要移动这个队列！
     * 如何移动？
     *
     * @param data
     * @return
     */
    public static int getMaxLength(String data) {
        Map<Character, Integer> mapData = new HashMap<>();
        int left = 0, max = 0;
        for (int i = 0; i < data.length(); i++) {
            if (mapData.containsKey(data.charAt(i))) {
                //有相同的时候left才滑动
                left = Math.max(left, mapData.get(data.charAt(i)) + 1);
            }
            mapData.put(data.charAt(i), i);
            max = Math.max(max, i - left + 1);
        }
        System.out.println(data.substring(left, left + max));
        return max;
    }

    /**
     * 反转链表
     * 保留前置节点,将当前节点的next节点设置为pre节点
     * 1 -> 2 -> 3 -> 4 -> 5
     * =>
     * 5 -> 4 -> 4 -> 2 ->1
     *
     * @param node
     * @return
     */
    public static ListNode turnListNode(ListNode node) {
        if (node == null || node.next == null) {
            return node;
        }

        //保留前置节点和当前节点。改变当前和前置节点的值
        ListNode pre = null;
        ListNode current = node;
        while (current != null) {
            ListNode next = current.next;
            //当前节点的next设置为前置节点
            current.next = pre;
            //前置节点更新为当前节点,供下一次调用
            pre = current;
            //向后遍历
            current = next;
        }
        return pre;
    }

    public static ListNode turnListNodeDiGui(ListNode node) {
        if (node == null || node.next == null) {
            return node;
        }
        //p为最后一个点
        ListNode p = turnListNodeDiGui(node.next);
        //node.next.next就是设置p.next=node
        node.next.next = node;
        //将原node.next=p的链接删除,否则就是闭环了
        node.next = null;
        return p;
    }

    private static ListNode initListNodeData() {
        ListNode<Integer> one = new ListNode<>(1);
        ListNode<Integer> two = new ListNode<>(2, one);
        ListNode<Integer> three = new ListNode<>(3, two);
        ListNode<Integer> four = new ListNode<>(4, three);
        return new ListNode<>(5, four);
    }

    /**
     * 盛最多水的容器
     *
     * @param data
     * @return
     */
    private static int maxArea(int[] data) {
        int x = 0, y = data.length - 1;
        int area = 0;
        while (x < y) {
            int high = Math.min(data[x], data[y]);
            area = Math.max((y - x) * high, area);
            if (data[x] < data[y]) {
                x++;
            } else {
                y--;
            }
        }
        return area;
    }

    @Data
    static class ListNode<E> {
        private E value;
        private ListNode next;

        public ListNode(E value) {
            this.value = value;
        }

        public ListNode(E value, ListNode next) {
            this.value = value;
            this.next = next;
        }
    }
}
