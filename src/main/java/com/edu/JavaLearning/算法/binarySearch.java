package com.edu.JavaLearning.算法;

import java.util.Arrays;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/3/17 12:20 PM
 * 二分查找
 **/
public class binarySearch {

    private static int N = 0;
    private static int N2 = 0;

    public static void main(String[] args) {
        int[] data = new int[100];
        for (int i = 0; i < 100; i++) {
            data[i] = i + 2;
        }
        int[] dataA = new int[]{1, 2, 3, 4, 5};

//        System.out.println(binary(data,52));
//        System.out.println(getMaxMin(1680,640));
        System.out.println(feBo(6) + " N = " + N);
        System.out.println(feBo2(1, 1, 3) + " N2 = " + N2);
        System.out.println(recursiveSum(dataA));
    }

    /**
     * 二分查找,返回index
     *
     * @param sources
     * @param target
     * @return
     */
    private static int binary(int[] sources, int target) {
        int high = sources.length;
        int low = 0;
        int count = 0;

        while (low <= high) {
            System.out.println(++count);
            int middle = (high + low) / 2;
            if (sources[middle] == target) {
                System.out.println(sources[middle]);
                return middle;
            }
            if (sources[middle] > target) {
                high = middle - 1;
            } else {
                low = middle + 1;
            }
        }
        return 0;
    }

    /**
     * 输入长宽,将其均匀分成方块,确保最大的
     *
     * @return
     */
    private static int getMaxMin(int length, int wide) {
        if (length == wide) {
            return length;
        } else {
            int a = length - wide;
            if (a > wide) {
                length = a;
            } else {
                length = wide;
                wide = a;
            }
            return getMaxMin(length, wide);
        }
    }

    /**
     * 斐波拉切 - 正向递归
     *
     * @return f(n) = f(n-1) + f(n-2)
     * 时间复杂度 O(2^n) 树的叶子节点
     */
    private static long feBo(long num) {
        N++;
        if (num == 1 || num == 2) {
            return 1;
        }
        return feBo(num - 1) + feBo(num - 2);
    }

    /**
     * 斐波拉切 - 尾递归
     * f1 = 1 f2=1 f3=2 f4=3 f5=5 f6=8
     * @return f(n) = f(n-1) + f(n-2)
     * 时间复杂度 O(n)
     */
    private static long feBo2(long first, long second, long num) {
        N2++;
        if (num < 3) {
            return 1;
        } else if (num == 3) {
            return first + second;
        } else {
            return feBo2(second, second + first, num - 1);
        }
    }


    /**
     * 递归数组求和
     *
     * @return
     */
    private static int recursiveSum(int[] data) {
        int sum;
        if (data.length == 0) {
            return 0;
        } else if (data.length == 1) {
            sum = data[0];
            return sum;
        } else {
            int[] copyOf = Arrays.copyOf(data, data.length - 1);
            sum = data[data.length - 1];
            return sum + recursiveSum(copyOf);
        }
    }


    /**
     * 递归快速排序
     *
     * @param data
     * @return
     */
    private static int[] quickSort(int low, int high, int[] data) {
        int key = data[low];
        while (low < high) {
            while (low < high && data[high] >= key) {
                high--;
            }
            data[low] = data[high];
            while (low < high && data[low] < key) {
                low++;
            }
            data[high] = data[low];
        }
        return null;
    }

    /**
     * data 中是否存在 sum = data[i] + data[j] + data[k]
     *
     * @param data
     * @param sum
     * @return
     */
    private static int serarchSum(int[] data, int sum) {
        return 0;
    }

}
