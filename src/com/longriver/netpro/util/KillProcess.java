package com.longriver.netpro.util;

import java.util.Scanner;

public class KillProcess {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stu


		kill();

	}
	//杀死启动的进程---暂时注释
	public static void kill(){
		try{
			Process process = Runtime.getRuntime().exec("taskList");
            Scanner in = new Scanner(process.getInputStream());
            while (in.hasNextLine()) {
                String temp = in.nextLine();

                if (temp.contains("phantomjs.exe")) {
                	temp = temp.replaceAll(" ", "");
                    // 获得pid
                    String pid = temp.substring("phantomjs.exe".length(), temp.indexOf("Console"));
                    System.out.println(pid+"-----------------");
                    Runtime.getRuntime().exec("tskill " + pid);
                }
//                if (temp.contains("firefox.exe")) {
//                	temp = temp.replaceAll(" ", "");
//                	// 获得pid
//                	String pid = temp.substring("firefox.exe".length(), temp.indexOf("Console"));
//                	System.out.println(pid+"-----------------");
//                	Runtime.getRuntime().exec("tskill " + pid);
//                }

//                if (temp.contains("phantomjs.exe")) {
//                	temp = temp.replaceAll(" ", "");
//                    // 获得pid
//                    String pid = temp.substring("phantomjs.exe".length(), temp.indexOf("Console"));
//                    System.out.println(pid+"-----------------");
//                    Runtime.getRuntime().exec("tskill " + pid);
//                }
                if (temp.contains("firefox.exe")) {
                	temp = temp.replaceAll(" +"," ");
                	System.out.println(temp);
                	temp = temp.replaceAll("firefox.exe ","");
                	int t1 = temp.indexOf(" ");
                	temp = temp.substring(0, t1);
                	// 获得pid
                	String pid = temp;
                	System.out.println(pid+"-----------------");
                	Runtime.getRuntime().exec("tskill " + pid);
                }

            }
		}catch (Exception e){
        	e.printStackTrace();
            //结束进程
         }
	}

}
