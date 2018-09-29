package com.edu.JavaLearning.jdktest.datastructure;

public class StackHeapTest {
	
	public static void main(String[] args) {
		MyStackByArray msba = new MyStackByArray();
		MyStackByLink mhbl = new MyStackByLink();
		MyQueueByArray mqba = new MyQueueByArray();


		for(int i=0;i<5;i++){
			mqba.enQueue(i);
		}
		for(int j=0;j<5;j++){
			System.out.println(mqba.deQueue()+" ");
		}
		
	/*	for(int i=0;i<31;i++){
			msba.push(i);
		}
		System.out.println(msba.toString());
		
		for(int j=0;j<15;j++){
			System.out.print(msba.pop()+" ");
		}

        System.out.println();
        for(int i1=0;i1<10;i1++){
			mhbl.push(i1);
		}
		for(int j1=0;j1<10;j1++){
			System.out.print(mhbl.pop()+" ");
		}*/
	
	}

}
