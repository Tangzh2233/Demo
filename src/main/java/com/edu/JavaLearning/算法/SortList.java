package com.edu.JavaLearning.算法;

import com.alibaba.fastjson.JSON;
import com.edu.dao.domain.User;
import com.edu.util.ParamsCheckUtil;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/3/27 11:52 AM
 * 排序算法再探
 * https://blog.csdn.net/MLcongcongAI/article/details/88081244
 **/
public class SortList {

    public static void main(String[] args) {
        int[] data = new int[]{100, 3, 7, 88, 2, 9, 102, 5, 4, 10, 66, 99, 45, 23};
        fastSort(data);
        System.out.println(JSON.toJSONString(data));
    }

    /**
     * 冒泡排序 比较交换
     * 第一次循环需要比较N次,此时再比较所需次数为N-1
     * 双层for循环
     * 最差|平均:时间复杂度O(n^2)
     * 最好:本就是有序。时间复杂度O(n) 不需要做比较交换
     *
     * @param data
     */
    public static void maoPao(int[] data) {
        if (data.length <= 1) {
            return;
        }
        //外层循环控制一个需要重复N次
        for (int i = 1; i <= data.length; i++) {
            //遍历一次后最后一个一个为最大值,此时排序最后一个不计入
            for (int j = 0; j < data.length - i; j++) {
                if (data[j] > data[j + 1]) {
                    int temp = data[j + 1];
                    data[j + 1] = data[j];
                    data[j] = temp;
                }
            }
        }
    }

    /**
     * 选择排序,每次循环找出当前位置最小的,然后交换值
     * 时间复杂度: O(n^2)
     *
     * @param data
     */
    public static void select(int[] data) {
        if (data.length <= 1) {
            return;
        }
        //[1-N][2-N][3-N]
        //寻找第i个位置最小的数。eg i = 0在[0,N]找最小; i = 1在[1-N]找最小;
        for (int i = 0; i < data.length; i++) {
            for (int j = i + 1; j < data.length; j++) {
                //记录当前最小值,有比他还小的交换位置
                int min = data[i];
                if (data[j] <= min) {
                    //data[j]比data[i]小,交换保证data[i]是最小的
                    data[i] = data[j];
                    data[j] = min;
                }
            }
        }
    }

    /**
     * 插入排序。将当前值往有序队列里插入
     * 时间复杂度: O(n^2)
     *
     * @param data
     */
    public static void insert(int[] data) {
        if (data.length <= 1) {
            return;
        }
        //默认data[0]为有序初始序列
        for (int i = 1; i < data.length; i++) {
            //内层为子序列
            for (int j = i; j > 0; j--) {
                //往前找到你在这个有序队列的位置
                if (data[j] < data[j - 1]) {
                    int temp = data[j];
                    data[j] = data[j - 1];
                    data[j - 1] = temp;
                }
                //因为子序列已经是有序的,所以当data[j] < data[j-1] 不满足时不需要再往前遍历查询
                else {
                    break;
                }
            }
        }
    }

    /**
     * 希尔排序,插入排序的优化版本。先宏观调控,具体参考
     *
     * @param data
     * @see https://blog.csdn.net/MLcongcongAI/article/details/88081244
     * 时间复杂度: 最好O(n) 平均O(n^1.3) 最坏O(n^2)
     * 空间复杂度 O(1)
     */
    public static void xiEr(int[] data) {
        if (data.length <= 1) {
            return;
        }
        int length = data.length;
        int gap = length;
        while (gap > 1) {
            gap = gap / 2;
            //控制 k=0,1,2..gap
            for (int j = 0; j < gap; j++) {
                //外层确定分组 k,k+gap,k+2gap,k+3gap
                for (int k = j; k < length; k = k + gap) {
                    //根据分组进行子序列插入
                    for (int i = k; i - gap >= 0; i -= gap) {
                        if (data[i] < data[i - gap]) {
                            int temp = data[i];
                            data[i] = data[i - gap];
                            data[i - gap] = temp;
                        }
                        //因为子序列已经是有序的,所以当data[j] < data[j-1] 不满足时不需要再往前遍历查询
                        else {
                            break;
                        }
                    }
                }
            }

        }
    }

    /**
     * 归并排序:该算法是采用分治法（Divide and Conquer）的一个非常典型的应用。
     * 将已有序的子序列合并，得到完全有序的序列；
     * 即先使每个子序列有序，再使子序列段间有序。若将两个有序表合并成一个有序表，称为2-路归并。
     * 时间复杂度:O(n log n)
     * 空间复杂度:O(n)
     *
     * @param data
     */
    public static int[] merge(int[] data) {
        if (data.length < 2) {
            return data;
        }
        int middle = data.length / 2;
        int[] left = Arrays.copyOfRange(data, 0, middle);
        int[] right = Arrays.copyOfRange(data, middle, data.length);
        return merge(merge(left), merge(right));
    }

    /**
     * 两个有序子序列合并成一个有序序列
     *
     * @param left
     * @param right
     * @return
     */
    private static int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];
        int i = 0, j = 0, k = 0;
        while (i < left.length && j < right.length) {
            if (left[i] > right[j]) {
                result[k++] = right[j++];
            } else {
                result[k++] = left[i++];
            }
        }
        //跳出while代表,比较小的子序列循环完毕
        //left较小,将right剩余部分加入主序列
        if (i == left.length) {
            while (j < right.length) {
                result[k++] = right[j++];
            }
        }
        //right较小,将left剩余部分加入主序列
        if (j == right.length) {
            while (i < left.length) {
                result[k++] = left[i++];
            }
        }
        return result;
    }

    /**
     * 快速排序
     * 最佳|平均时间复杂度: O(n log n)
     * 最差:O(n^2)
     * 空间复杂度: 最佳O(log n) 最差O(n)
     *
     * @param data
     */
    public static void fastSort(int[] data) {
        fast(data, 0, data.length - 1);
    }

    private static void fast(int[] data, int low, int high) {
        if (low < high) {
            //获取基准值
            int key = data[low], begin = low, end = high;
            while (low < high) {
                //从右至左找到第一个小于基准值的index
                while (low < high && data[high] >= key) {
                    high--;
                }
                //将值赋值至低位
                data[low] = data[high];

                //从左至右找到第一个大于基准值赋值给刚才空出的位置
                while (low < high && data[low] < key) {
                    low++;
                }
                data[high] = data[low];
            }
            data[low] = key;
            fast(data, begin, low - 1);
            fast(data, low + 1, end);
        }
    }

    /**
     * 堆排序
     * 最佳|平均|最差时间复杂度: O(n log n)
     * 空间复杂度: O(1)
     *
     * @param data
     */
    public static void heap(int[] data) {
        //1.构建大顶堆
        for (int i = data.length / 2 - 1; i >= 0; i--) {
            //从第一个非叶子结点从下至上，从右至左调整结构
            adjust(data, i, data.length);
        }
        //2.调整堆结构+交换堆顶元素与末尾元素
        for (int j = data.length - 1; j > 0; j--) {
            swap(data, 0, j);//将堆顶元素与末尾元素进行交换
            adjust(data, 0, j);//从栈顶开始调整
        }
    }

    /**
     * 数组表示堆
     * 父节点parent; 左孩子left=2*parent+1; 右孩子:right=2*parent+2
     *
     * @param data
     * @param length 当前数组长度
     * @param index  当前要操作的父节点
     *               自下而上,从左至右
     */
    private static void adjust(int[] data, int index, int length) {
        //取出当前元素
        int temp = data[index];
        //k 为index的左孩子。                     //交换一次以后判断index左孩子的左孩子是否还满足大顶堆
        for (int k = 2 * index + 1; k < length; k = 2 * k + 1) {
            //左孩子 < 右孩子.k++为当前右孩子
            //计算左右孩子最大,后与父节点比较
            if (k + 1 < length && data[k] < data[k + 1]) {
                //右孩子大,k++此时为右孩子
                k++;
            }
            //孩子大于父节点,将子节点的值赋给父节点
            if (data[k] > temp) {
                data[index] = data[k];
                index = k;
            } else {
                break;
            }
        }
        data[index] = temp;
    }

    private static void swap(int[] data, int from, int to) {
        int temp = data[from];
        data[from] = data[to];
        data[to] = temp;
    }

}
