package com.edu.JavaLearning.Learning.CloneTest;

/**
 * Created by Administrator on 2017/8/15.
 */
public class UserCloneTest {
    public static void main(String[] args) {
        User user = new User();
        User user1 = null;
        user.setId(1);
        user.setName(new StringBuffer("tang"));
        user.setPassword(new int[]{1,2,3});
        try {
            user1 = (User) user.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        String a = "tang2";
        String b = new String("tang2");
        System.out.println(a==b);
        System.out.println(user);
        System.out.println(user1);
        System.out.println(System.currentTimeMillis()/1000);
    }
}
