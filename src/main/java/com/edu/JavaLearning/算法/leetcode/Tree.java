package com.edu.JavaLearning.算法.leetcode;

import lombok.Data;

import java.util.*;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/8 2:01 PM
 **/
public class Tree {


    public static void main(String[] args) {
        dfs(TreeNode.initData);
        System.out.println("\n=========前序遍历=======");
        dfs2(TreeNode.initData);
        System.out.println("\n=========中序遍历=======");
        inOrder(TreeNode.initData);
        System.out.println("\n=========后序遍历=======");
        postOrder(TreeNode.initData);

        bfs(TreeNode.initData);
        System.out.println();

        System.out.println(maxDeep(TreeNode.initData) + "====" + minDeep(TreeNode.initData));

        bfsForLevelNode(TreeNode.initData);

        dfsForLevelNode(TreeNode.initData, 0);
        Level_Node.forEach(System.out::println);
    }

    //===================树的深度|广度优先搜索===================//

    /**
     * 树的深度优先搜索 栈实现
     *
     * @param root
     */
    private static void dfs(TreeNode root) {
        if (root == null) {
            return;
        }
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        System.out.println("=========深度优先搜索========");
        while (!stack.isEmpty()) {
            TreeNode pop = stack.pop();
            System.out.print(pop.val + "->");
            if (pop.right != null) {
                stack.push(pop.right);
            }
            if (pop.left != null) {
                stack.push(pop.left);
            }
        }
    }

    /**
     * 树的深度优先搜索 递归实现
     * 树的前序遍历
     *
     * @param root
     */
    private static void dfs2(TreeNode root) {
        if (root == null) {
            return;
        }
        System.out.print(root.val + "->");
        dfs2(root.left);
        dfs2(root.right);
    }

    /**
     * 树的中序遍历
     * 头节点左边为左子树右边为右子树
     * @param root
     */
    private static void inOrder(TreeNode root) {
        if (root == null) {
            return;
        }
        inOrder(root.left);
        System.out.print(root.val + "->");
        inOrder(root.right);
    }

    /**
     * 树的后序遍历
     * @param root
     */
    private static void postOrder(TreeNode root) {
        if (root == null) {
            return;
        }
        postOrder(root.left);
        postOrder(root.right);
        System.out.print(root.val + "->");
    }

    /**
     * 树的最大深度.分左树和右树
     * 每次递归深度+1;
     *
     * @param root
     * @return
     */
    private static int maxDeep(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int leftDeep = maxDeep(root.left) + 1;
        int rightDeep = maxDeep(root.right) + 1;
        return Math.max(leftDeep, rightDeep);
    }

    /**
     * 树的最小深度.分左树和右树
     * 每次递归深度+1;
     *
     * @param root
     * @return
     */
    private static int minDeep(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int leftDeep = minDeep(root.left) + 1;
        int rightDeep = minDeep(root.right) + 1;
        return Math.min(leftDeep, rightDeep);
    }

    /**
     * 树的广度优先搜索
     *
     * @param root
     */
    private static void bfs(TreeNode root) {
        if (root == null) {
            return;
        }
        System.out.println();
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        System.out.println("=========广度优先搜索========");
        while (!queue.isEmpty()) {
            TreeNode poll = queue.poll();
            System.out.print(poll.val + "->");
            if (poll.left != null) {
                queue.add(poll.left);
            }
            if (poll.right != null) {
                queue.add(poll.right);
            }
        }
    }

    /**
     * 给你一个二叉树，请你返回其按层序遍历得到的节点值
     * bfs实现
     */
    private static void bfsForLevelNode(TreeNode<Integer> root) {
        if (root == null) {
            return;
        }

        Queue<TreeNode<Integer>> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            //当前层取出后,queue中剩余的就是下一层的元素
            int levelNum = queue.size();
            List<Integer> levelNode = new ArrayList<>();
            //遍历当前层元素,弹出
            for (int i = 0; i < levelNum; i++) {
                TreeNode<Integer> poll = queue.poll();
                if (poll.left != null) {
                    queue.add(poll.left);
                }
                if (poll.right != null) {
                    queue.add(poll.right);
                }
                levelNode.add(poll.val);
            }
            System.out.println(levelNode.toString());
        }
    }

    /**
     * 给你一个二叉树，请你返回其按层序遍历得到的节点值
     * dfs实现
     */
    private static List<List<Integer>> Level_Node = new ArrayList<>();

    private static void dfsForLevelNode(TreeNode<Integer> root, int level) {
        if (root == null) {
            return;
        }
        //初始化每一层
        if (Level_Node.size() <= level) {
            Level_Node.add(new ArrayList<>());
        }
        List<Integer> integers = Level_Node.get(level);
        integers.add(root.val);

        dfsForLevelNode(root.left, level + 1);
        dfsForLevelNode(root.right, level + 1);
    }

    @Data
    public static class TreeNode<E> {
        public E val;
        public TreeNode<E> left;
        public TreeNode<E> right;

        public TreeNode(E val) {
            this.val = val;
        }

        public TreeNode(E val, TreeNode<E> left, TreeNode<E> right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }

        public static TreeNode<Integer> initData = initTreeNode();

        private static TreeNode<Integer> initTreeNode() {
            TreeNode<Integer> root = new TreeNode<>(5);
            root.left = new TreeNode<>(4);
            root.right = new TreeNode<>(3);
            root.left.left = new TreeNode<>(1);
            root.left.right = new TreeNode<>(6);
            root.left.left.left = new TreeNode<>(7);
            root.left.left.right = new TreeNode<>(8);
            root.left.right.left = new TreeNode<>(9);
            root.left.right.right = new TreeNode<>(10);
            root.right.left = new TreeNode<>(12);
            root.right.left.left = new TreeNode<>(11);
            root.right.left.right = new TreeNode<>(22);
            return root;
        }
    }
}
