package com.edu.JavaLearning.io;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/14 4:32 PM
 * 大文件排序
 * 假如:内存100M  文件1G 要求对1G的文件排序
 * 思路
 * 1.文件拆分,拆成一个50M,拆了20个
 * 2.对单个文件读入内存排序。使用堆排
 * 3.文件合并
 *  3-1:假若一次读A文件10000行、假若一次读B文件10000行 到内存中
 *  3-2:对于这两个已经有序的序列做归并排序。
 *      3-2-1:当A先排完则,将当前组合的序列写入合并文件A+B
 *      3-2-2:再从A中读取10000行,和上次B 10000行剩余的归并。
 *      3-2-3:谁先读完谁再从文件取值。直至文件读完。没排完的文件直接合并添加到A+B合并文件中。
 *      3-2-4:以此内推
 *
 **/
public class SortBigFile {
}
