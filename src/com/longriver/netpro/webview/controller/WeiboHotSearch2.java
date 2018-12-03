package com.longriver.netpro.webview.controller;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import com.longriver.netpro.util.FileUtil;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 微博热搜
 * @author lilei
 * @2018-1-22 下午9:30:04
 * @version v1.0
 */
public class WeiboHotSearch2 {
	public static boolean process = false;
	public static boolean isSend = false;
	public static void main(String[] args) {
		process = true;
		TaskGuideBean task = new TaskGuideBean();
		task.setCorpus("小迪快报");
		toComment(task);
	}

	/**
	 * 新浪新闻评论
	 * @param task
	 */
	public static void toComment(TaskGuideBean task) {
		System.out.println("toComment");
		//先清空存放截图的文件夹
		FileUtil.deleteDirectoryFiles();
		//保证只返回一次结果
		isSend = true;
		torun(task);
	}
	public static void torun(TaskGuideBean task) {
		try{
			String key = URLEncoder.encode(task.getCorpus(), "UTF-8");
			key = URLEncoder.encode(key, "UTF-8");
			String addr = "http://s.weibo.com/weibo/"+key+"&Refer=STopic_box";
			System.out.println("热搜词:"+task.getCorpus());
			System.out.println(addr);
			URL u45 = new URL(addr);
	        HttpURLConnection c45 = (HttpURLConnection) u45.openConnection();
	        c45.addRequestProperty("Host", "s.weibo.com");
	        c45.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
	        c45.addRequestProperty("Connection", "keep-alive");
	        c45.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		    c45.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3141.7 Safari/537.36");
		    c45.addRequestProperty("Referer", addr);
	        c45.setConnectTimeout(1000 * 20);
	        c45.setReadTimeout(1000 * 20);
	        c45.connect();
	        InputStream i45 = c45.getInputStream();
	        Scanner s45 = new Scanner(i45, "utf-8");
	        while(s45.hasNext()){
	         	String scsc = s45.nextLine();
	         	System.out.println(scsc);
	         }
		}catch(Exception e){
			task.setCode(100);
			isSuccess(task, "");
			e.printStackTrace();
		}finally{
			task.setCode(100);
			isSuccess(task, "");
		}
		System.out.println("结束------------------");
		System.out.println("结束------------------");
	}

	/**
	 * 判断是否成功
	 */
	public static void isSuccess(TaskGuideBean task,String msg){
		if(isSend){
			isSend = false;
			MQSender.toMQ(task,msg);
		}
	}
	
}
