package com.edu.JavaLearning.设计模式.constructpattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/15.
 */
public class Body {
    List<String> body = new ArrayList<>();

    public void add(String str) {
        body.add(str);
    }

    public List<String> getResult() {
        return body;
    }
}
