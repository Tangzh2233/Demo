package com.edu.JavaLearning.算法.leetcode;


/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/8 2:05 PM
 * 动态规划问题
 *
 **/
public class DP {

    public static void main(String[] args) {
        int[] data = new int[]{3, 6, 9, 1, 2, 4, 7, 5, 4, 7, 3};
        int[][] param = new int[][]{
                {}
        };
        System.out.println(param[0].length);
        System.out.println(maxProfits(data));
    }


    /**
     * 给定一个数组，它的第 i 个元素是一支给定股票第 i 天的价格。
     * 如果你最多只允许完成一笔交易（即买入和卖出一支股票一次），设计一个算法来计算你所能获取的最大利润。
     *
     * 这是最基础的动态规划问题。只需要用一个变量记录第i天之前的股票最低价格 min，就可以得到第 i 天卖出的收益prices[i]-min
     * @param data
     * @return
     */
    private static int maxProfit(int[] data) {
        int min = Integer.MAX_VALUE;
        int max = 0;
        for (int i = 0; i < data.length; i++) {
            min = Math.min(min, data[i]);
            //当天股票的最大收益为当前-之前最小数值
            max = Math.max(data[i] - min, max);
        }
        return max;
    }

    /**
     * 又是一道经典的股票问题，与上一题不同的地方在于没有限制股票的买入卖出次数，同样要求最大的利润；
     * @param data
     * @return
     * ....看不懂
     */
    private static int maxProfits(int[] data) {
        //持有
        int[] dp1 = new int[data.length];
        //不持有
        int[] dp2 = new int[data.length];
        dp1[0] = -data[0];
        dp2[0] = 0;
        for (int i = 1; i < data.length; i++) {
            dp1[i] = Math.max(dp1[i-1],dp2[i-1] - data[i]);
            dp2[i] = Math.max(dp2[i-1],dp1[i-1] + data[i]);
        }
        return dp2[dp2.length-1];
    }

    /**
     * 贪心算法,如果当前股票比前一天高则买入
     * 累加收益
     * @param data
     * @return
     */
    private static int maxProfit2(int[] data) {
        if (data.length <= 1) {
            return 0;
        }
        int profit = 0;
        for (int i = 1; i < data.length; i++) {
            if (data[i] > data[i - 1]) {
                profit += data[i] - data[i - 1];
            }
        }
        return profit;
    }
}
