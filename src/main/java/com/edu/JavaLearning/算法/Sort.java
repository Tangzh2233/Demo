package com.edu.JavaLearning.算法;

import java.util.Stack;

/**
 * @author: tangzh
 * @date: 2019/8/19$ 9:58 AM$
 * @version: 1.0
 * 常用简单排序算法
 **/
public class Sort {


    public static void main(String[] args) {
        int[] data = new int[]{3, 4, 1, 9, 6, 4, 7, 2, 8};
        mergeSort(data);
    }


    /**
     * 冒泡排序
     * 依次遍历,相邻元素两两比较,将比较大的逐步后移
     */
    public static void bubbleSort(int[] data) {
        for (int i = 1; i <= data.length; i++) {
            for (int j = 0; j < data.length - i; j++) {
                if (data[j + 1] < data[j]) {
                    int temp = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = temp;
                }
            }
        }
    }

    /**
     * 插入排序
     * 原理: 依次保证数组的第1,2,3,4....length位有序
     * 将元素依次插入到已知有序列表中
     */

    public static void insertSort(int[] data) {
        for (int i = 0; i < data.length; i++) {
            //第i个元素之前的已知有序队列
            int temp = data[i];
            int j = i;
            for (; j > 0; j--) {
                //需要进行插入的值
                if (data[j - 1] > temp) {
                    int tem = data[j - 1];
                    data[j - 1] = data[j];
                    data[j] = tem;
                }
            }
        }
    }

    //也是依次插入有序序列中
    public static void insertSort2(int[] data, int i) {
        //记录当前排序到的index，及i之前的数组是有序的
        int j = i;
        if (i < data.length) {
            //选取最小值
            int min = data[i];
            for (; i > 0; i--) {
                if (data[i - 1] > min) {
                    data[i] = data[i - 1];
                    data[i - 1] = min;
                }
            }
            insertSort2(data, j + 1);
        }
    }

    /**
     * 选择排序
     * 每次选择最小的值,添加到数组后一位
     */
    public static void selectSort(int[] data, int i) {
        //记录当前排序到的index，及i之前的数组是有序的
        int j = i;
        if (i < data.length) {
            //选取最小值
            int min = data[i];
            for (; i < data.length; i++) {
                if (data[i] < min) {
                    min = data[i];
                    data[i] = data[j];
                    data[j] = min;
                }
            }
            selectSort(data, j + 1);
        }
    }

    /**
     * 快速排序
     * 原理：把基准数大的都放在基准数的左边,把比基准数小的放在基准数的右边,这样就找到了该数据在数组中的正确位置.
     * 以后采用递归的方式分别对前半部分和后半部分排序，当前半部分和后半部分均有序时该数组就自然有序了。
     * 挖坑：找个基准值,留下一个坑，把小于坑的high值填过去,这时候坑为high,再找个比基准值大的填过去，依次类推
     *
     * @return
     */
    public static void quickSort(int[] data, int low, int high) {
        if (low < high) {
            int temp = data[low], begin = low, end = high;
            while (low < high) {
                while (low < high && data[high] >= temp) {
                    high--;
                }
                data[low] = data[high];
                while (low < high && data[low] <= temp) {
                    low++;
                }
                data[high] = data[low];
            }
            data[low] = temp;
            quickSort(data, begin, low - 1);
            quickSort(data, low + 1, end);
        }
    }

    /**
     * 使用两个栈实现排序
     * 思路：入栈,判断元素大于栈顶元素，则栈顶元素出栈保存在栈B中，找到元素在栈A的位置后，栈B元素依次再入栈
     *
     * @param data
     */
    public static void stackSort(int[] data) {
        Stack<Integer> A = new Stack<>();
        Stack<Integer> B = new Stack<>();
        for (int i = 0; i < data.length; i++) {
            if (A.empty()) {
                A.push(data[i]);
                continue;
            }
            int sizeA = A.size();
            for (int j = 0; j <= sizeA; j++) {
                if (A.empty()) {
                    A.push(data[i]);
                    break;
                }
                Integer temp = A.peek();
                if (data[i] > temp) {
                    B.push(A.pop());
                } else {
                    A.push(data[i]);
                    break;
                }
            }
            while (!B.empty()) {
                A.push(B.pop());
            }
        }
        while (!A.empty()) {
            System.out.println(A.pop());
        }
    }

    /**
     * 归并排序
     */
    public static void mergeSort(int[] arr) {
        sort(arr, 0, arr.length - 1);
    }

    public static void sort(int[] arr, int L, int R) {
        if(L == R) {
            return;
        }
        int mid = L + ((R - L) >> 1);
        sort(arr, L, mid);
        sort(arr, mid + 1, R);
        merge(arr, L, mid, R);
    }

    public static void merge(int[] arr, int L, int mid, int R) {
        int[] temp = new int[R - L + 1];
        int i = 0;
        int p1 = L;
        int p2 = mid + 1;
        // 比较左右两部分的元素，哪个小，把那个元素填入temp中
        while(p1 <= mid && p2 <= R) {
            temp[i++] = arr[p1] < arr[p2] ? arr[p1++] : arr[p2++];
        }
        // 上面的循环退出后，把剩余的元素依次填入到temp中
        // 以下两个while只有一个会执行
        while(p1 <= mid) {
            temp[i++] = arr[p1++];
        }
        while(p2 <= R) {
            temp[i++] = arr[p2++];
        }
        // 把最终的排序的结果复制给原数组
        for(i = 0; i < temp.length; i++) {
            arr[L + i] = temp[i];
        }
    }
}
