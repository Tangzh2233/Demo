package com.edu.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: tangzh
 * @Date: 2019/7/4$ 2:39 PM$
 **/
public class OOMTest {

    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<>();
        while (true){
            list.add(new OOMObject());
        }
    }

    static class OOMObject{
    }
}
