package com.longriver.netpro.webview.appcontroller;

public class AppTest {

	public static void main(String[] args) {
		
		AppTest app = new AppTest();
		app.getValue();
		app.getValue();
	}
	
	public   void getValue(){
		
		synchronized(this){
			
			for(int i=0;i<10;i++){
				
				System.out.println(i);
			}
		}
	}
}
