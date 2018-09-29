package com.edu.JavaLearning.jdktest.ENUM;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tangzhihao
 * @date 2018/1/9
 */

public enum EChannel {
    ZHANG(2,"zhang"),
    TANG(1,"tang"),
    LIU(3,"liu"),
    WANG(4,"wang"),
    QIAN(5,"qian"),
    ;
    private int seq;
    private String desc;
    public final static Map<Integer,EChannel> EChannel_Map = new HashMap<>();

    static {
        for (EChannel eChannel: EChannel.values()) {
            EChannel_Map.put(eChannel.getSeq(),eChannel);
        }
    }
     EChannel(int seq,String desc){
        this.seq = seq;
        this.desc = desc;
    }

    public int getSeq() {
        return seq;
    }
    public String getDesc() {
        return desc;
    }
    public EChannel getChannel(Integer a){
         return EChannel_Map.get(a);
    }

}
