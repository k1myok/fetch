package com.longriver.netpro.webview.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import com.alibaba.fastjson.JSON;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

public class PepoleDigg {
	
	public static void main(String arsg[]){
//		data.setString("mission_addr","http://sports.people.com.cn/n1/2016/0420/c149182-28292072.html");
//		data.setString("commend_id","155702868");
		TaskGuideBean taskdo = new TaskGuideBean();
		taskdo.setAddress("http://sports.people.com.cn/n1/2016/0420/c149182-28292072.html");
		taskdo.setPraiseWho("155702868");
		 xinhua(taskdo);
	}
	@SuppressWarnings("null")
	public static void xinhua(TaskGuideBean taskdo) {
		try {
			String commend_id = taskdo.getPraiseWho();
			String sUrl = taskdo.getAddress();
			
			String cookie="";
			URL u1 = new URL("http://bbs.people.com.cn/api/news.do?action=doScore&postId="+commend_id+"&score=1&callback=jsonp1461307953426");
			HttpURLConnection c1 = (HttpURLConnection) u1.openConnection();
			//_FakeX509TrustManager.allowAllSSL();
			c1.addRequestProperty("Host", "bbs.people.com.cn");
			c1.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
			c1.addRequestProperty("Accept", "*/*");
			c1.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
			c1.addRequestProperty("Referer", sUrl);
			c1.connect();
			InputStream i2 = c1.getInputStream();
			Scanner s2 = new Scanner(i2, "gb2312");
			String token = "";
			String s = "";
			String c = "";
			while(s2.hasNext()){
				String scsc = s2.nextLine();
				System.out.println(scsc);
			}
			MQSender.toMQ(taskdo,"");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
