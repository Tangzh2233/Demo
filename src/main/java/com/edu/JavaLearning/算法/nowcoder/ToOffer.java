package com.edu.JavaLearning.算法.nowcoder;


import com.edu.JavaLearning.算法.leetcode.Tree;

import java.util.*;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/8 9:56 PM
 * 牛客网剑指offer
 **/
public class ToOffer {

    private static List<String> param = new ArrayList<>();

    public static void main(String[] args) {
        int[] data = new int[]{1,3,2,4,6};
        Arrays.sort(data);
        System.out.println(Arrays.toString(data));
        System.out.println(replaceSpace(new StringBuffer("We Are Happy")));
    }

    /**
     * 输入某二叉树的前序遍历和中序遍历的结果，请重建出该二叉树。假设输入的前序遍历和中序遍历的结果中都不含重复的数字。
     * 例如输入前序遍历序列{1,2,4,7,3,5,6,8}和中序遍历序列{4,7,2,1,5,3,8,6}，则重建二叉树并返回。
     * @param pre
     * @param in
     * @return
     *
     * 1.根据当前前序序列的第一个结点确定根结点，为 1
     * 2.找到 1 在中序遍历序列中的位置，为 in[3]
     * 3.切割左右子树，则 in[3] 前面的为左子树， in[3] 后面的为右子树
     * 4.则切割后的左子树前序序列为：{2,4,7}，切割后的左子树中序序列为：{4,7,2}；切割后的右子树前序序列为：{3,5,6,8}，切割后的右子树中序序列为：{5,3,8,6}
     * 5.对子树分别使用同样的方法分解
     */
    public Tree.TreeNode<Integer> reConstructBinaryTree(int[] pre, int[] in) {
        if (pre.length == 0 || in.length == 0) {
            return null;
        }
        Tree.TreeNode<Integer> root = new Tree.TreeNode<>(pre[0]);
        for (int i = 0; i < in.length; i++) {
            //在中序中找到前序的root节点,然后切割
            if (in[i] == pre[0]) {
                root.left = reConstructBinaryTree(Arrays.copyOfRange(pre, 1, i + 1), Arrays.copyOfRange(in, 0, i));
                root.right = reConstructBinaryTree(Arrays.copyOfRange(pre, i + 1, pre.length), Arrays.copyOfRange(in, i + 1, in.length));
                break;
            }
        }
        return root;
    }

    /**
     * 输入一个链表，按链表从尾到头的顺序返回一个ArrayList。
     * @param listNode
     * @return
     */
    public ArrayList<Integer> printListFromTailToHead(ListNode listNode) {
        ArrayList<Integer> result = new ArrayList<>();
        if(listNode == null){
            return result;
        }
        ListNode current = listNode;
        ListNode pre = null;
        while (current != null){
            ListNode next = current.next;
            current.next = pre;
            pre = current;
            current = next;
        }
        while (pre != null){
            result.add(pre.val);
            pre = pre.next;
        }
        return result;
    }

    /**
     * 请实现一个函数，将一个字符串中的每个空格替换成“%20”。
     * 例如，当字符串为We Are Happy.则经过替换之后的字符串为We%20Are%20Happy。
     * @param str
     * @return
     */
    public static String replaceSpace(StringBuffer str) {
        String string = str.toString();
        StringBuilder result = new StringBuilder();
        char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ' ') {
                result.append("%20");
            } else {
                result.append(chars[i]);
            }
        }
        return result.toString();
    }

    /**
     * 在一个二维数组中（每个一维数组的长度相同），每一行都按照从左到右递增的顺序排序，
     * 每一列都按照从上到下递增的顺序排序。
     * 请完成一个函数，输入这样的一个二维数组和一个整数，判断数组中是否含有该整数。
     * @param target
     * @param array
     * @return
     */
    public boolean find(int target, int[][] array) {
        if (array == null || array.length == 0 || array[0].length == 0) {
            return false;
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i][i] == target) {
                return true;
            }
            if (array[i][i] < target) {
                continue;
            }
            if (array[i][i] > target) {
                for (int j = i - 1; j >= 0; j--) {
                    if (array[i][j] == target) {
                        return true;
                    }
                }
                for (int j = i - 1; j >= 0; j--) {
                    if (array[j][i] == target) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void compare() {
        List<String> param = new ArrayList<>();
        Scanner in = new Scanner(System.in);
        int a = Integer.valueOf(in.nextLine());
        for (int i = 0; i < a; i++) {
            param.add(in.nextLine());
        }
        TreeMap<Long, String> dataMap = new TreeMap<>();
        param.forEach(item -> {
                    if (item.endsWith("G")) {
                        dataMap.put((Integer.valueOf(item.substring(0, item.length() - 1)) * 1000L), item);
                    }
                    if (item.endsWith("T")) {
                        dataMap.put((Integer.valueOf(item.substring(0, item.length() - 1)) * 1000000L), item);
                    }
                    if (item.endsWith("M")) {
                        dataMap.put(Long.valueOf(item.substring(0, item.length() - 1)), item);
                    }
                }
        );
        for (String value : dataMap.values()) {
            System.out.println(value);
        }
    }

    static class ListNode {
        int val;
        ListNode next = null;

        ListNode(int val) {
            this.val = val;
        }
    }

}
