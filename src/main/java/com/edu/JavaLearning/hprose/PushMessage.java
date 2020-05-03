package com.edu.JavaLearning.hprose;

import lombok.Data;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/4/30 5:33 PM
 **/
@Data
public class PushMessage {
    private String id;
    private String data;

    public PushMessage(String id, String data) {
        this.id = id;
        this.data = data;
    }
}
