package com.edu.util;


import org.apache.commons.lang3.StringUtils;

/**
 * @author tangzh
 * @version 1.0
 * @date 2019/11/8 10:50 AM
 **/
public class CommonUtil {

    public static boolean afterVersion(String version1, String version2){
        if (StringUtils.isEmpty(version1) || StringUtils.isEmpty(version2)) {
            return false;
        }

        String[] versionArray1 = version1.split("\\.");
        String[] versionArray2 = version2.split("\\.");
        int idx = 0;

        //取最小长度值
        int minLength = Math.min(versionArray1.length, versionArray2.length);
        int diff = 0;
        while (idx < minLength
                && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度
                && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符
            ++idx;
        }

        //如果已经分出大小,则直接返回,如果未分出大小,则再比较位数，有子版本的为大;
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        return diff>0;
    }

    public static void main(String[] args) {
        for(int i=0;i<10;i++){
            int item = (int)(4 * Math.random());
            System.out.println(item);
        }
//        System.out.println(afterVersion("2.9.4","3.1.5.0"));
    }

    public static void compareVersion(String v1,String v2){
        //切分版本字符串
        String[] v1s = v1.split("\\.");
        String[] v2s = v2.split("\\.");

        //针对结果进行版本号大小判断
        int result = compareString(v1s,v2s);

        if(result>0){
            System.out.println("v2 version is smaller");
        }else if(result<0){
            System.out.println("v1 version is smaller");
        }else if(result == 0){
            System.out.println("v1 equals v2");
        }else {

        }
    }

    public static int compareString(String[] v1s,String[] v2s){
        int len = 0;
        //得到两个数组长度的最大值，长度相等返回第二个
        if(v1s.length>v2s.length){
            len = v1s.length;
        }else{
            len = v2s.length;
        }
        //循环逐一比较两个数组相应位置的大小
        for(int i=0;i<len;i++){
            try {
                int com = compareChars(v1s[i], v2s[i]);
                if (com != 0) {
                    return com;
                }
            }catch (ArrayIndexOutOfBoundsException e){
                if(v1s.length>v2s.length){
                    return 1;
                }else{
                    return -1;
                }
            }
        }
        return 0;
    }

    //把两个数组中相对应位置的元素拆分转成char类型进行比较
    public static int compareChars(String v1s,String v2s){
        char[] c1 = v1s.toCharArray();
        char[] c2 = v2s.toCharArray();

        //当两个数组长度一样时，1代表前者大，-1代表后者大
        if(c1.length==c2.length){
            for (int i = 0; i < c1.length; i++) {
                if(getHashCode(c1[i])>getHashCode(c2[i])){
                    return 1;
                }else if(getHashCode(c1[i])<getHashCode(c2[i])){
                    return -1;
                }
            }
        }

        //当后者长度大时，1代表前者大，-1代表后者大
        if(c1.length<c2.length){
            for (int i = 0; i < c1.length; i++) {
                if(getHashCode(c1[i])>getHashCode(c2[i])){
                    return 1;
                }else if(getHashCode(c1[i])<getHashCode(c2[i])){
                    return -1;
                }
            }
            return 1;
        }

        //当前者长度大时，1代表前者大，-1代表后者大
        if(c1.length>c2.length){
            for (int i = 0; i < c2.length; i++) {
                if(getHashCode(c1[i])>getHashCode(c2[i])){
                    return 1;
                }else if(getHashCode(c1[i])<getHashCode(c2[i])){
                    return 1;
                }
            }
            return -1;
        }

        return 0;
    }

    //根据char计算出hash值进行比较
    public static int getHashCode(char c){
        return String.valueOf(c).toLowerCase().hashCode();
    }

    public static int compareVersion2(String version1, String version2) {
        if (version1.equals(version2)) {
            return 0;
        }
        String[] version1Array = version1.split("//.");
        String[] version2Array = version2.split("//.");
        int index = 0;
        //获取最小长度值
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        //循环判断每位的大小
        while (index < minLen && (diff = Integer.parseInt(version1Array[index]) - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            //如果位数不一致，比较多余位数
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }
}
