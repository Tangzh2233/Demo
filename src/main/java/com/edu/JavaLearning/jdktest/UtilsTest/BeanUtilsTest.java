package com.edu.JavaLearning.jdktest.UtilsTest;


import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

/**
 * @author Tangzhihao
 * @date 2017/12/12
 * BeanUtils + ModelMapper
 */

public class BeanUtilsTest {
    public static void main(String[] args) {
        Student stu = new Student();
        stu.setId(1);
        stu.setName("tang");
        stu.setPwd("123");
        stu.setClassId(12);

        ModelMapper modelMapper = new ModelMapper();
        Student2 stu2 = modelMapper.map(stu, Student2.class);

//        Student2 stu2 = new Student2();
//        BeanUtils.copyProperties(stu,stu2);

        System.out.println(stu2.toString());
        UUID uid = UUID.randomUUID();
        System.out.println(uid.toString());
        String replace = uid.toString().replaceAll("-", "*");
        System.out.println(replace);
    }
}
