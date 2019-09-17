package com.edu.JavaLearning.jdktest.datastructure;



import com.google.common.util.concurrent.RateLimiter;

import java.util.Arrays;

/**
 * @author Tangzhihao
 * @date 2017/12/7
 */

public class SortTest {

    private static RateLimiter rateLimiter = RateLimiter.create(2.0);


    public static void main(String[] args) {
        int[] a = new int[]{9, 3, 2, 5, 1, 2, 7, 3, 0, 9, 6};
        int[] b = new int[]{9, 7, 2, 1, 4, 15};
        quickSort(a,0,10);
        rateLimiter.acquire();
    }

    /**
     * @author: Tangzhihao
     * @date: 2017/12/8 10:30
     * @return: 快速排序 第一次结果：low左边小于int[low]，low右边大于int[low]。递归分别对左右两部分执行相同操作
     * @description:递归调用挖坑排序的第一次
     */

    public static void quickSort(int[] a, int low, int high) {

        if (low < high) {
            int middle = quickSortOnce(a, low, high);
            quickSort(a, 0, middle - 1);
            quickSort(a, middle + 1, high);
        }
    }

    /**
     * @description:快速排序（挖坑排序第一次）。实际使用递归调用一次
     */
    public static int quickSortOnce(int[] a, int low, int high) {
        //挖个坑，把key拿出来
        int key = a[low];
        while (low < high) {
            //找到<key的high坐标
            while (low < high && a[high] >= key) {
                high--;
            }
            a[low] = a[high];
            //找到>key的low坐标
            while (low < high && a[low] <= key) {
                low++;
            }
            a[high] = a[low];
        }
        //多的坑，把key填回去
        a[low] = key;
        //返回子集的区间，递归调用
        return low;
    }

    /**
     * @author: Tangzhihao
     * @date: 2017/12/7 15:55
     * @description:选择排序 最小值放在i位，i->{0---a.length-1}
     */
    public static void selectionSort(int[] a) {
        int i, j, min;
        for (i = 0; i < a.length; i++) {
            //选择第i个位置为最小值（i轮过后，第i个左边的是已排序的）
            min = a[i];
            for (j = i + 1; j < a.length; j++) {
                if (a[j] < min) {
                    a[i] = a[j];
                    a[j] = min;
                    //交换以后，保证一次圆满的循环里，第i位为最小值
                    min = a[i];
                }
            }

        }
    }

    /**
     * @author: Tangzhihao
     * @date: 2017/12/7 14:33
     * @description:插入排序 将一个值插入到左边已知顺序的位置
     */
    public static void insertSort(int[] ints) {
        int i, j, temp;
        for (i = 1; i < ints.length; i++) {
            //待插入元素
            temp = ints[i];
            //依次移动和左边已知的有序数列进行比较，知道插入位置
            for (j = i - 1; j >= 0; j--) {
                if (ints[j] > ints[j + 1]) {
                    ints[j + 1] = ints[j];
                    ints[j] = temp;
                }
            }
        }
    }

    /**
     * @author: Tangzhihao
     * @date: 2017/12/7 10:28
     * @description:冒泡排序
     */
    public static void bubbleSort(int[] ints) {
        //N个元素，需要执行N次
        for (int i = 1; i <= ints.length; i++) {
            //第x个元素,执行N-x次
            for (int j = 0; j < ints.length - i; j++) {
                if (ints[j] > ints[j + 1]) {
                    int temp = 0;
                    temp = ints[j];
                    ints[j] = ints[j + 1];
                    ints[j + 1] = temp;
                }
            }
        }
    }
}

/**
 * 链表反转
 */
class turnList {

    static class Node<T> {
        Node next;
        T value;

        Node(T t) {
            this.value = t;
        }

        void setNext(Node next) {
            this.next = next;
        }

        Node getNext() {
            return next;
        }
    }

    public static void main(String[] args) {

        int[] datas = new int[]{10, 40, 30, 60, 90, 70, 20, 50, 80};
        turnHeap(datas);

        System.out.println(binarySearch(datas, 12));

        System.out.println(clear("818181666", "816"));

        Node<Integer> node = new Node<>(0);
        Node head = node;
        for (int i = 0; i < 5; i++) {
            Node next = new Node(i + 1);
            node.setNext(next);
            node = next;
        }
        Node node1 = turnNode(head);
        while (true) {
            if (node1.getNext() == null) {
                break;
            }
            System.out.println(node1.value);
            node1 = node1.next;
        }
    }


    /**
     * 链表反转
     */
    public static Node turnNode(Node node) {
        if (node == null || node.getNext() == null) {
            return node;
        }
        Node turnNode = turnNode(node.getNext());
        node.getNext().setNext(node);
        node.setNext(null);
        return turnNode;
    }

    /**
     * 类似消消乐
     */
    public static String clear(String target, String key) {
        if (target.length() < key.length()) {
            return target;
        }
        char[] chars = target.toCharArray();
        int i = key.length();
        for (int j = 0; j <= target.length() - i; j++) {
            char[] chars1 = new char[i];
            int a = j;
            for (int k = 0; k < i; k++) {
                chars1[k] = target.charAt(a++);
            }
            if (key.equals(new String(chars1))) {
                for (int k = 0; k < i; k++) {
                    chars[j++] = '$';
                }
            }
        }
        String s = new String(chars);
        String ss = s.replace("$", "");
        if (s.equals(ss)) {
            return s;
        }
        return clear(ss, key);
    }

    public static int binarySearch(int[] datas, int key) {
        int min = 0, max = datas.length;
        Arrays.sort(datas);
        int item = 0;
        while (min <= max) {
            if (max - min == 1) {
                item++;
            } else {
                item = item + (max - min) / 2;
            }
            if (item > datas.length - 1) {
                return -1;
            }
            if (datas[item] == key) {
                return item;
            }
            if (datas[item] < key) {
                min = item;
            }
            if (datas[item] > key) {
                max = item;
                item = 0;
            }
        }
        return -1;
    }

    public static void turnHeap(int[] target) {
        int[] heap = new int[0];
        for (int i = 0; i < target.length; i++) {
            heap = addToHeap(target[i], heap);
        }
        for (int j = 0; j < heap.length; j++) {
            System.out.print(heap[j]+",");
        }
    }


    public static int[] addToHeap(int i, int[] heap) {

        int index = heap.length;

        int[] heap1 = new int[index + 1];
        for (int j = 0; j < heap.length; j++) {
            heap1[j] = heap[j];
        }
        heap1[index] = i;
        while (true) {
            int f = (index - 1) / 2;
            int temp;
            if (heap1[f] < i) {
                temp = heap1[f];
                heap1[f] = i;
                heap1[index] = temp;

                index = f;
                if (index == 0) {
                    break;
                }
                continue;
            }
            break;
        }
        return heap1;

    }

}



