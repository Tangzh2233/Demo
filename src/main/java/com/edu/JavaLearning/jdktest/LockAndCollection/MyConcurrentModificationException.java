package com.edu.JavaLearning.jdktest.LockAndCollection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author Tangzhihao
 * @date 2018/5/21
 */

public class MyConcurrentModificationException {

    public static void main(String[] args) {
        ArrayList<String> vector = new ArrayList<>();
        vector.add("tang");
        vector.add("zhi");
        vector.add("hao");
        vector.add("yu");
        vector.add("jia");
        vector.add("chun");
        Iterator<String> iterator = vector.iterator();
        /*while (iterator.hasNext()){
            String next = iterator.next();
            if("zhi".equals(next)){
                vector.remove(next);
            }
        }*/
        for(int i=0;i<vector.size();i++){
            if(vector.get(i).equals("zhi")){
                vector.remove(i);
            }
        }
        for (String s:vector) {
            if("zhi".equals(s)){
                vector.remove(s);
            }
        }
        for (String s:vector) {
            System.out.println(s);
        }
    }
}
