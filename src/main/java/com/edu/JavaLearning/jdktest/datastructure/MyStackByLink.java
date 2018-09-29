package com.edu.JavaLearning.jdktest.datastructure;

public class MyStackByLink {
	//栈顶元素
	private Note top;
	
	public MyStackByLink() {
		top = new Note(null,null);
	}
	
	public void push(Object o){
		top = new Note(o,top);
	}
	
	public Object pop(){
		Note now = top;
		if(top!=null){
			top = (Note) top.pre;
		}
		return now.data;
	}
	
	
	private final class Note {
		private Object data;//数据
		private Note pre;//前一个节点
		Note(Object data,Note pre){
			this.data = data;
			this.pre = pre;
		}
	}

}
