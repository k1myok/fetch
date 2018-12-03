package com.longriver.netpro.util;

import java.io.File;  

public class DeleteLog {  
  
 public static void deleteTomcat_temp(){
	 System.out.println("删除tomcat/temp缓存文件中...");
	 String dir = getTomcatDir();
	 delFolder(dir,0);
 }
 public static String getTomcatDir(){
	System.out.println(DeleteLog.class.getResource("/").getFile());
	String path = DeleteLog.class.getResource("/").getFile();
//	int ind = path.indexOf("fetch");
	int ind = path.indexOf("webapps");
	path = path.substring(0, ind);
	
	return path+"temp/";
 }
 
 
 public static void delFolder(String folderPath,int cuurent) {
     try {
        delAllFile(folderPath); //删除完里面所有内容
        if(cuurent==1){
        	String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //删除空文件夹
        }
     } catch (Exception e) {
       e.printStackTrace(); 
     }
}

//删除指定文件夹下所有文件
//param path 文件夹完整绝对路径
   public static boolean delAllFile(String path) {
       boolean flag = false;
       File file = new File(path);
       if (!file.exists()) {
         return flag;
       }
       if (!file.isDirectory()) {
         return flag;
       }
       String[] tempList = file.list();
       File temp = null;
       for (int i = 0; i < tempList.length; i++) {
          if (path.endsWith(File.separator)) {
             temp = new File(path + tempList[i]);
          } else {
              temp = new File(path + File.separator + tempList[i]);
          }
          if (temp.isFile()) {
             temp.delete();
          }
          if (temp.isDirectory()) {
             try {
            	 delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
				 delFolder(path + "/" + tempList[i],1);//再删除空文件夹
				 flag = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
          }
       }
       return flag;
 }
 public static void main(String[] args) {  
	 delFolder("E:\\bb",0);
 }  
  
} 
