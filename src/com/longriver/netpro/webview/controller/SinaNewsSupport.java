package com.longriver.netpro.webview.controller;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Scanner;

import com.alibaba.fastjson.JSON;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

public class SinaNewsSupport {
	public static void main(String args[]){
		TaskGuideBean b = new TaskGuideBean();
		b.setPraiseWho("55EFF119-72F99777-9EAF28F9-850-908");
		b.setPraiseWho("59A7ADDB-DFFF7F06-156659ED3-850-7A2");
		b.setAddress("http://news.sina.com.cn/w/2015-01-12/135431389274.shtml");
		b.setAddress("http://comment5.news.sina.com.cn/comment/skin/default.html?channel=gj&newsid=1-1-31389274");
		try {
			sina(b);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void sina(TaskGuideBean taskdo) throws Exception{
//		String url = "http://comment5.news.sina.com.cn/comment/skin/default.html?channel=gj&newsid=1-1-31389274";
//		String commentId = "55EFD0DD-72F99777-1473A48C5-850-958";
		
		String commentId = taskdo.getPraiseWho();
		String url = SinaCommentJietu.getCommentAdd(taskdo);
		
		String key = "";
		URL u2 = new URL(url);
		HttpURLConnection c2 = (HttpURLConnection) u2.openConnection();
		c2.connect();
		InputStream i2 = c2.getInputStream();
		Scanner s2 = new Scanner(i2, "gb2312");
		String chanel = "";
		while(s2.hasNext()){
			String scsc = s2.nextLine();
			if(scsc.indexOf("moodcounter") > -1 && scsc.indexOf("key=") > -1){
				key = scsc.substring(scsc.indexOf("key=")).replace("key=", "").trim();
				key = key.substring(1);
				key = key.substring(0, key.indexOf("\""));
			}
			if(scsc.indexOf("meta") > -1 && scsc.indexOf("name=\"comment\"") > -1){
				key = scsc.substring(scsc.indexOf("content=")).replace("content=\"", "");
				key = key.substring(key.indexOf(":") + 1);
				key = key.substring(0, key.indexOf("\""));
			}
			if(scsc.indexOf("channel:'") > -1){
				chanel = scsc.substring(scsc.indexOf("channel:'")).replace("channel:'", "");
				chanel = chanel.substring(0, chanel.indexOf("'"));
						
			}
		}
		
		
		//http://comment5.news.sina.com.cn/comment/skin/default.html?channel=ty&newsid=6-12-7458297
		if(url.indexOf("http://comment5") > -1){
			key = url.substring(url.indexOf("newsid=")).replace("newsid=", "");
			if(key.indexOf("&") > -1){
				key = key.substring(0, key.indexOf("&"));
			}
			chanel = url.substring(url.indexOf("channel=")).replace("channel=", "");
			if(chanel.indexOf("&") > -1){
				chanel = chanel.substring(0, chanel.indexOf("&"));
			}
		}
		
		//System.out.println(key + "|" + chanel + "|" + commentId);
		URL u3 = new URL("http://comment5.news.sina.com.cn/cmnt/vote");
		HttpURLConnection c3 = (HttpURLConnection) u3.openConnection();
		String params = "channel=" + chanel
				+ "&newsid=" + key
				+ "&parent=" + commentId
				+ "&format=js"
				+ "&vote=1"
				+ "&callback=function+%28o%29%7B%7D"
				+ "&domain=sina.com.cn";
		c3.addRequestProperty("Host", "comment5.news.sina.com.cn");
		c3.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0");
		c3.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		c3.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		c3.addRequestProperty("Referer", url);
		c3.addRequestProperty("Connection", "keep-alive");
		c3.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		c3.addRequestProperty("Content-Length", String.valueOf(params.length()));
		c3.setDoInput(true);
		c3.setDoOutput(true);
		PrintWriter out3 = new PrintWriter(c3.getOutputStream());
		out3.print(params);
		out3.flush();
		
		InputStream i3 = c3.getInputStream();
		Scanner s3 = new Scanner(i3);
		//String f = "";
		while(s3.hasNext()){
			s3.nextLine();
		}
		MQSender.toMQ(taskdo,"");
	}
}
