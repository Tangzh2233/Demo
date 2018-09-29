package com.edu.JavaLearning.jdktest.datastructure;

/**
 * @author Tangzhihao
 * @date 2017/12/7
 */

public class SortTest {


    public static void main(String[] args) {
        int[] a = new int[]{9,3,2,5,1,2,7,3,0,9,6};
        int[] b = new int[]{9,7,2,1,4,15};
        bubbleSort(b);
    //  insertSort(a);
    //  selectionSort(a);
    //  quickSort(a,0,10);
        for (int a1:b) {
            System.out.print(a1+" ");
        }

    }
    
    /**
     * @author: Tangzhihao
     * @date: 2017/12/8 10:30
     * @params: 
     * @return: 快速排序
     * @description:递归调用挖坑排序的第一次
     */

    public static void quickSort(int[] a,int low,int high){

        if (low<high){
            int middle = quickSortOnce(a, low, high);
            quickSort(a,0,middle-1);
            quickSort(a,middle+1,high);
        }
    }
    
    /**
     * @description:快速排序（挖坑排序第一次）。实际使用递归调用一次
     */
    public static int quickSortOnce(int[] a,int low,int high){
        //挖个坑，把key拿出来
        int key = a[low];
        while(low<high){
            //找到<key的high坐标
            while(low<high && a[high]>=key){
                high--;
            }
            a[low] = a[high];
            //找到>key的low坐标
            while(low<high && a[low]<=key){
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
     * @params: 
     * @return: 
     * @description:选择排序 最小值放在i位，i->{0---a.length-1}
     */
    public static void selectionSort(int[] a){
        int i,j,min;
        for(i=0;i<a.length;i++){
            //选择第i个位置为最小值（i轮过后，第i个左边的是已排序的）
            min = a[i];
            for(j=i+1;j<a.length;j++){
                if(a[j]<min){
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
     * @params: 
     * @return:
     * @description:插入排序 将一个值插入到左边已知顺序的位置
     */
    public static void insertSort(int[] ints){
        int i,j,temp;
        for(i=1;i<ints.length;i++){
            //待插入元素
            temp = ints[i];
            //依次移动和左边已知的有序数列进行比较，知道插入位置
            for(j=i-1;j>=0;j--){
                if(ints[j]>ints[j+1]){
                    ints[j+1] = ints[j];
                    ints[j] = temp;
                }
            }
        }
    }

    /**
     * @author: Tangzhihao
     * @date: 2017/12/7 10:28
     * @params: 
     * @return:
     * @description:冒泡排序
     */
    public static void bubbleSort(int[] ints){
        //N个元素，需要执行N次
        for(int i=1;i<=ints.length;i++){
            //第x个元素,执行N-x次
            for(int j=0;j<ints.length-i;j++){
                if(ints[j]>ints[j+1]){
                    int temp = 0;
                    temp = ints[j];
                    ints[j] = ints[j+1];
                    ints[j+1] = temp;
                }
            }
        }
    }
}
