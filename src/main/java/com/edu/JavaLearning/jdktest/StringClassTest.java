package com.edu.JavaLearning.jdktest;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/8/11.
 */
public class StringClassTest {

    private static final Long ONE_MINUTE = 60*1000L;

    private final Comparator<String> strComp = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return 0;
        }
    };
    public static void main(String[] args) {

    //    ArrayList<String> list = new ArrayList<>();
    //    list.subList(2,8);
        Set<String> demoSet = new LinkedHashSet<>();
        demoSet.add("1");
        demoSet.add("2");
        demoSet.add("3");
        demoSet.add("4");
        Map<String,String> map = new HashMap<>();
        Map<Integer,String> tree1 = new TreeMap<>(/*new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1-o2;
            }
        }*/);
        Map<Integer,String> tree = new TreeMap<>();

        map.put("zhang","bowen");
        map.put("liu","yuanju");
        map.put("tang","zhihao");


        tree.put(3,"hao");
        tree.put(2,"zhi");
        tree.put(1,"tang");
        Set<String> strings = map.keySet();

        //获取map的value集，放在Arraylist<String>中
        List<String> valueList = new ArrayList<>(map.values());
        //将list拼接成字符串，用‘|’连接
        String s = StringUtils.join(valueList, "|");
        System.out.println(s);

/*

        Set<Map.Entry<String, String>> entrySet = map.entrySet();
        Iterator<Map.Entry<String, String>> iterator = entrySet.iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String> next = iterator.next();
            System.out.println(next.getKey()+"->"+next.getValue());
        }
*/

        /*for (String name:strings) {
            System.out.println(map.get(name));
        }*/

        /*String a = "abcdefghijklmnopqrstuvwxyz";
        String a1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String b = new String();
        String c = new String("tang");
        char[] d = new char[]{'t','a','n','g'};
        String e = new String(d,0,2);
        int hashCode = "Lee".hashCode();
        System.out.println(hashCode);
        Object oj = new Object();
        System.gc();
        Set<String> set = new HashSet<>();*/

    /*    List<String> list = new ArrayList<>();
        list.add("tang");
        list.add("tang1");
        list.add("tang2");
        Iterator<String> iterator = list.iterator();
        ListIterator<String> iterator1 = list.listIterator();
        System.out.println(iterator.next());
        System.out.println(iterator.next());
        iterator.remove();
        System.out.println(list.toString());
    */
        /*Date date = new Date();
        String s = DateUtilDateToStr(date);
        try {
            Date toDate = DateUtilStrToDate(s);

            System.out.println(toDate.toString()+"11111");
            System.out.println(s+"22222");
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        /*int[] a = new int[]{10,51,32,72,3,9,26,90,87,60};

        System.out.println("数组最大值:"+getMax(a));
        System.out.println("数组平均值:"+getMod(a));
        System.out.println("数组最小值:"+getMin(a));
        System.out.println(comp("2","2"));*/

    }

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

    public static int comp(String a,String b){
        int a1 = Integer.valueOf(a);
        int b1 = Integer.valueOf(b);
        if(a1-b1>0){
            return 1;
        }else if(a1-b1<0){
            return -1;
        }else{
            return 0;
        }
    }




    //yyyymmdd -> Date
    public static Date DateUtilStrToDate(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date strDate = sdf.parse(str);
        return strDate;
    }
    //Date ->yyyymmdd
    public static String DateUtilDateToStr(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String format = sdf.format(date);
        return format;
    }


    public <E> boolean  contain(Collection<E> c,Object obj){
        for (E element:c) {
            if(element.equals(obj)){
                return true;
            }
        }
        return false;
    }

}
