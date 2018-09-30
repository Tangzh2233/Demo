package com.edu.JavaLearning.jdktest.datastructure;


public class MyStackByArray {
	
	private Object[] obj = new Object[10];
	//实际节点个数
	private int size = 0;
	private int top;
	
	public MyStackByArray(){
		top = -1;
	}
	
	public int size(){
		return size;
	}
	
	public void resize(){
		Object[] old = obj;
		obj = new Object[obj.length*2];
		for(int i=0;i<size;i++){
			obj[i]=old[i];
			old[i]=null;
		}	
	}
	
	public void push(Object o){
		if(size==obj.length){
			resize();
			obj[++top] = o;
		}else{
			obj[++top] = o;
		}
		size++;
	}
	
	public Object pop(){
		size--;
		return obj[top--];
	}
	
	@Override
    public String toString(){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<size;i++){
			sb.append(obj[i]+" ");
		}
		return sb.toString();
	}

}
