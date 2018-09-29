package com.edu.JavaLearning.jdktest.datastructure;

/**
 * @author Tangzhihao
 * @date 2017/12/11
 */

public class MyQueueByArray {

    private Object[]  Q = new Object[10];
    private int size;
    private int buttom;

    public MyQueueByArray(){
        this.size = 0;
        this.buttom = 0;
    }

    public void resize(){
        Object[] old = Q;
        Q = new Object[Q.length*2];
        for(int i=0;i<size;i++){
            Q[i] = old[i];
            old[i] = null;
        }
    }

    public void enQueue(Object o){
        if(size>=Q.length){
            resize();
            Q[size++] = o;
        }else{
            Q[size++] = o;
        }
    }
    public Object deQueue(){
        if(buttom>size){
            return null;
        }else{
            Object rt = Q[buttom++];
            Q[buttom-1]=null;
            return  rt;
        }

    }

}
