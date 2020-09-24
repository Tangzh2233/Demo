package com.edu.JavaLearning.jdktest.UtilsTest;

import com.edu.dao.domain.User;
//import com.rrx.util.Md5Util;

import java.util.*;

/**
 * @author tangzh
 * @version 1.0
 * @date 2019/12/26 3:52 PM
 **/
public class MD5Util {


    public static void main(String[] args) {
        List<User> list_a = new ArrayList<>();
        User user = new User();
        user.setUsername("吴宝存");
        User user1 = new User();
        user1.setUsername("吴灿");
        User user6 = new User();
        user6.setUsername("吴长华");
        User user2 = new User();
        user2.setUsername("吴读林");
        User user3 = new User();
        user3.setUsername("吴飞");
        User user4 = new User();
        user4.setUsername("吴飞");
        User user5 = new User();
        user5.setUsername("吴广兴");
        list_a.add(user);list_a.add(user1);list_a.add(user2);list_a.add(user3);
        list_a.add(user4);list_a.add(user5);list_a.add(user6);

        list_a.sort(User::compareTo);
        list_a.forEach(item -> System.out.println(item.getUsername()));


        long time = new Date().getTime();
        String ts = String.valueOf(time / 1000);
        TreeMap<String, String> map = new TreeMap<>();
        map.put("memberID","506092517281130484");
        map.put("returnType","2");
        map.put("_ts",ts);

//        String md5 = Md5Util.md5(map, "jdbfriend20151224");
//        System.out.println("ts = "+ts + "MD5={}" + md5);
    }
}
