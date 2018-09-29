package com.edu.JavaLearning.jdktest;


/**
 * @author Tangzhihao
 * @date 2017/12/1
 */

public class test {


    private static final StringBuffer init = new StringBuffer("TANG");
    static class Str{
        String str;
        Str next;
        Str(String str){
            this.str = str;
        }
        public void add(Str next){
            this.next = next;
        }

    }

    /*public static void main(String[] args) {

    }*/
    public static void main(String[] st) {

        String str = "http://127.0.0.1:8848/page/main.html";
        System.out.println(str.toLowerCase());
        str = str.substring(7);
        System.out.println(str);
        /*init.append("zhi");
        System.out.println(init);
        Str tang = new Str("tang");
        Str zhi = new Str("zhi");
        Str hao = new Str("hao");
        tang.add(zhi);
        zhi.add(hao);
        for(Str e = tang;e!=null;e=e.next){
            System.out.println(e.str);
        }
        Str c = tang;
        do{
            System.out.println(c.str);
            c = c.next;
        }while (c!=null);*/





        /*String[] split = "127.0.0.1:2333".split(":");
        System.out.println("ip:"+split[0]+"   port"+split[1]);
        System.out.println(System.currentTimeMillis());
        System.out.println(System.nanoTime());
        boolean b = 2 > 1 ? false : true;
        StringBuffer buffer = new StringBuffer();
        buffer.append("hahah");
        buffer.append("\r\n");
        buffer.append("heihei");
        HashMap<Object, Object> map = new HashMap<>();
        System.out.println(buffer.toString());*/
        //String format = new DecimalFormat("_0000").format(1);
        //System.out.println(format);
    }

    public String str(int a){
        try{
            if(1==a){
                return "true";
            }else{
                return "false";
            }

        }catch (Exception e){
            return "false";
        }finally {

        }
    }
    /*public static void main(String[] args) {

        int[] a = new int[]{10,51,32,72,3,9,26,90,87,60};

        System.out.println("数组最大值:"+getMax(a));
        System.out.println("数组平均值:"+getMod(a));
        System.out.println("数组最小值:"+getMin(a));
        System.out.println(comp("a","hh"));

    }*/

    public static int getMax(int[] a){
        int max = -999999;
        for(int i=0;i<a.length;i++){
            if(a[i]>max){
                max = a[i];
            }else{
                continue;
            }
        }
        return max;
    }

    public static int getMod(int[] a){
        int sum = 0;
        for(int i=0;i<a.length;i++){
            sum = sum + a[i];
        }
        return sum/a.length;
    }

    public static int getMin(int[] a){
        int min = 999999;
        for(int i=0;i<a.length;i++){
            if(a[i]<min){
                min = a[i];
            }else{
                continue;
            }
        }
        return min;
    }

    public static String comp(String a,String b){
        int a1 = 0;
        int b1 = 0;
        try {
            a1 = Integer.valueOf(a);
            b1 = Integer.valueOf(b);
        } catch (NumberFormatException e) {
            return "请输入两个字符串数字！";
        }
        if(a1-b1>0){
            return "1";
        }else if(a1-b1<0){
            return "-1";
        }else{
            return "0";
        }
    }

}
