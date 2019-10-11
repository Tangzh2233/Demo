package com.edu.JavaLearning.spring;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author tangzh
 * @version 1.0
 * @date 2019/10/11 9:52 AM
 * Spring事务学习
 **/
public class SpringTransaction {

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateDataMethodA(){
        //todo execute sql
        //todo execute sql
    }

    @Transactional
    public void updateDataMethodB(){
        //todo execute sql
    }

    @Transactional
    public void updateDataMethodC(){
        //todo execute sql
    }
}
