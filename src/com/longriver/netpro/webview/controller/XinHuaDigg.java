package com.longriver.netpro.webview.controller;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Scanner;

import org.apache.log4j.Logger;


import com.alibaba.fastjson.JSON;
import com.longriver.netpro.common.sina.SData;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

public class XinHuaDigg {
	private static Logger logger = Logger.getLogger(XinHuaDigg.class);
	
	@SuppressWarnings("null")
	public static void xinhua(TaskGuideBean taskdo) throws Exception{
		
//		String sUrl = "http://news.xinhuanet.com/world/2016-04/21/c_1118692581.htm";
//		String commentId = "1237685";
		SData data = new SData();
		
		String commentId = taskdo.getPraiseWho();
		String sUrl = taskdo.getAddress();
		
		data.setString("mission_addr",sUrl);
		//评论转发内容
		data.setString("mission_commend_id",commentId);
		
		String threadId = sUrl.substring(sUrl.lastIndexOf("/")).replace("/", "").replace(".html", "").replace(".shtml", "").replace(".htm", "");
		
		threadId = threadId.substring(threadId.indexOf("_")+1);
		String cookie="";
	    URL u1 = new URL("http://comment.home.news.cn/a/commentUp.do?_ksTS=1461221256581_87&callback=jsonp88&commentId="+commentId+"&newsId=1-"+threadId);
		HttpURLConnection c1 = (HttpURLConnection) u1.openConnection();
		//_FakeX509TrustManager.allowAllSSL();
		c1.addRequestProperty("Host", "comment.home.news.cn");
		c1.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
		c1.addRequestProperty("Accept", "*/*");
		c1.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		c1.addRequestProperty("Referer", sUrl);
		c1.connect();
		InputStream fi = c1.getInputStream();
		Scanner fs = new Scanner(fi);
		while(fs.hasNext()){
			String scsc2 = fs.nextLine();
			System.out.println(scsc2);
			
		}
		MQSender.toMQ(taskdo,"");
		logger.info("新华点赞1次...");
		
	}
	
	public static void main(String arsg[]){
		try{
			 xinhua(null);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
