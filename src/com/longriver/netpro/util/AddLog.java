package com.longriver.netpro.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class AddLog {
	public static boolean writeTxtFile(String content,String fileName){
		String path = createDir();
		if(fileName==null || fileName.equals("")) fileName = DateUtil.getCurrentTimeMillis();
		String name = fileName+".txt";
		String file = path+"/"+name;
		createFile(path+"/"+name);
		RandomAccessFile mm=null;  
		boolean flag=false;  
		FileOutputStream o=null;
		content += "\n"; 
		try {  
			o = new FileOutputStream(file,true);  
			o.write(content.getBytes("utf-8"));
			o.close();  
			flag=true;
		} catch (Exception e) {
			e.printStackTrace();  
		}finally{ 
			try {
				if(mm!=null){  
					mm.close();
				}  
			} catch (IOException e) {
				e.printStackTrace();
			} 
	  }  
	  return flag;  
	}
	public static void createFile(String filePath){
		File f = new File(filePath);  
        if (!f.exists()) {  
        	try {
        		f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
	public static String createDir(){
		String path = AddLog.class.getResource("/").getFile();
		path +="mylog/"+DateUtil.getCurrentDate();
		File file =new File(path);
		if(!file.exists() && !file.isDirectory()){
		    file.mkdirs(); 
		}
//		F:/myeclipseworkspace/network/WebRoot/WEB-INF/classes/mylog
		System.out.println(path);
		return path;
	}
	public static void main(String[] args){
		try {
			writeTxtFile("123","170054");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
