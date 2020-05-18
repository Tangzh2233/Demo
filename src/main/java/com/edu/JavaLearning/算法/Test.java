package com.edu.JavaLearning.算法;

import java.util.*;
import java.util.concurrent.Executors;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/9 8:50 PM
 **/
public class Test {


    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String param = String.valueOf(in.nextLine());
        String[] strData = param.split(",");
        int[] data = new int[strData.length];
        for(int k = 0;k<strData.length;k++){
            data[k] = Integer.valueOf(strData[k]);
        }
        int length = data.length;
        if(length < 3){
            return;
        }
        for(int i = length/2-1;i>=0;i--){
            heap(data,i,length);
        }
        for(int j=length-1;j>0;j--){
            swap(data,0,j);
            heap(data,0,j);
        }
        System.out.println(data[2]);
    }

    public static void remove(HashMap<String, Object> hashMap, String preKey) {
        if (preKey == null) {
            return;
        }
        HashMap<String, Object> clone = (HashMap<String, Object>) hashMap.clone();

        for (String s : clone.keySet()) {
            if (s.startsWith(preKey)) {
                hashMap.remove(s);
            }
        }
        Executors.newCachedThreadPool();
    }

    public static String convertDecToHex(int n) {
        int mag = Integer.SIZE - Integer.numberOfLeadingZeros(n);
        int chars = Math.max(((mag + (4 - 1)) / 4), 1);
        char[] buf = new char[chars];

        formatInt(n, 4, buf, 0, chars);

        return new String(buf);

    }

    final static char[] digits = {
            '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B',
            'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    public static int formatInt(int val, int shift, char[] buf, int offset, int len) {
        int charPos = len;
        int radix = 1 << shift;
        int mask = radix - 1;
        do {
            buf[offset + --charPos] = digits[val & mask];
            val >>>= shift;
        } while (val != 0 && charPos > 0);

        return charPos;
    }

    public static void heap(int[] data, int index, int length) {
        int root = data[index];
        for (int i = 2 * index + 1; i < length; i = 2 * i + 1) {
            if (i + 1 < length && data[i + 1] > data[i]) {
                i++;
            }
            if (data[i] > root) {
                data[index] = data[i];
                index = i;
            } else {
                break;
            }
        }
        data[index] = root;
    }

    public static void swap(int[] data, int from, int to) {
        int temp = data[from];
        data[from] = data[to];
        data[to] = temp;
    }
}
