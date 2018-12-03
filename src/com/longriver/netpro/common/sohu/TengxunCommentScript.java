package com.longriver.netpro.common.sohu;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import javax.script.*;  

public class TengxunCommentScript {
	
	public static void main(String args[]){
		TengxunCommentScript st02 = new TengxunCommentScript();
		String str = "";
		//347E941672AC4007F4CADA3AD44F4835
		try {
			st02.getCookie("123123","");
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(str);
	}
	
	
	public static String getCookie(String useid,String pwd){
		String cookie = "";
		try {  
		    Runtime rt = Runtime.getRuntime();  
		    Process proc = rt.exec("c:\\qqGetCookie\\qqGetCookie.exe "+useid+" "+pwd); 
		    
		    InputStream stderr = proc.getErrorStream();  
		    InputStreamReader isr = new InputStreamReader(stderr);  
		    BufferedReader br = new BufferedReader(isr);  
		    StringBuffer a = new StringBuffer();
		    
		    try 
	        {
	            while(proc != null) 
	            {
	                int _ch = proc.getInputStream().read();
	                 if(_ch != -1){
	                	 a.append((char)_ch);
	                 }
	                	
	                 else break;
	             }
	         } 
	         catch (Exception e) 
	         {
	            e.printStackTrace();
	        } finally  
            {  
                try   
                {  
                	isr.close();  
                }   
                catch (IOException e)   
                {  
                    e.printStackTrace();  
                }  
            } 
		    
		    String testTemp = a.toString();
		    String  b = testTemp;
		    System.out.print("b=="+b);
		    String  s = "Cookie";
			 int count = 0;  
		        //һ����str�ĳ��ȵ�ѭ������  
		        for(int i=0; i<b.length();){  
		            int c = -1;  
		            c = b.indexOf(s);  
		            //�����S������Ӵ�����C��ֵ����-1.  
		            if(c != -1){  
		                //�����c+1 ���� c+ s.length();������Ϊ�����str���ַ��ǡ�aaaa���� s = ��aa����������2�����ʵ������3�����ַ�  
		                //��ʣ�µ��ַ��ϴȡ��ŵ�str��  
		            	b = b.substring(c+1);  
		                count ++;  
		            }  
		            else {  
		                //i++;  
		                System.out.println("--------------");  
		                break;  
		            }  
		        }  
				String tt = testTemp;
				//String cookie = WeiboSina.getCookie(userId,pwd);
				for(int i=0;i<count;i++){
					String nickname =  "";
					tt = tt.substring(tt.indexOf("Cookie")+7);
					if(tt.indexOf("for")>-1)
						nickname =  tt.substring(0,tt.indexOf("for"));
					nickname = nickname.trim();
					cookie = cookie+nickname+";";
					
				}
		    System.out.println(cookie);
		    
		    int exitVal = proc.waitFor();  
		    System.out.println("0表示正常终止  exitVal=="+exitVal);
		    return cookie;
		} catch (Throwable t) { 
			t.printStackTrace();  
			return cookie;
		    
		}  
		
		
	}
	
}
