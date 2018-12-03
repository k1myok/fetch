package com.longriver.netpro.webview.controller;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;


public class WYSupportVps {
	
	private static Logger logger = Logger.getLogger(WYSupportVps.class);
	
	public static void main(String ar[]){
		TaskGuideBean b = new TaskGuideBean();
//		b.setPraiseWho("B6EMJLFT00252G50_630930902");//评论后面的复制里 链接里面的内容
//		b.setAddress("http://comment.money.163.com/money_bbs/B6EMJLFT00252G50.html");
		b.setPraiseWho("DGU9BQEE0001875P_76797088");
		b.setAddress("http://comment.news.163.com/news_guonei8_bbs/CB7FTQ8V0001875N.html");
		b.setAddress("http://comment.news.163.com/news_shehui7_bbs/DGU9BQEE0001875P.html");
		
		for(int i=0;i<2;i++){
			wangyi(b);
			try {
				Thread.sleep(1000l * 2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void wangyi(TaskGuideBean taskdo) {
		System.out.println("WYSupportVps");
		String cookie = "";
		try{
			String commentId = taskdo.getPraiseWho();
			String sUrl = taskdo.getAddress();
			String k = sUrl.substring(sUrl.lastIndexOf("/"));
			String borad[] = commentId.split("_");
			if(borad.length<2) return;
			String host = sUrl.substring(0,sUrl.indexOf(".163.com/"));
			System.out.println("host="+host);
			String rhost = host.replace("http://", "") + ".163.com";
			System.out.println("rhost="+rhost);
			URL fu1 = new URL(host+".163.com/" +"/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads/"+borad[0]+"/comments/gentoken?ibc=newspc");
			System.out.println("fu1="+fu1);
			HttpURLConnection fc1 = (HttpURLConnection) fu1.openConnection();
			
			fc1.addRequestProperty("Host", rhost);
			fc1.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			fc1.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			fc1.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
			fc1.addRequestProperty("Referer", sUrl);
			fc1.addRequestProperty("Connection", "keep-alive");
			fc1.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			fc1.setInstanceFollowRedirects(false);
			fc1.setDoInput(true);
			fc1.setDoOutput(true);
			PrintWriter fo1= new PrintWriter(fc1.getOutputStream());
			fo1.print("");
			fo1.flush();
			InputStream fi1 = fc1.getInputStream();
			Scanner fs1 = new Scanner(fi1);
			String token = "";
			while(fs1.hasNext()){
				token = fs1.nextLine();
				System.out.println(token);
				
			}
			Map<String, List<String>> m2 = fc1.getHeaderFields();
			for(Map.Entry<String,List<String>> entry : m2.entrySet()){
				if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
					for(String value : entry.getValue()){
						cookie = cookie + value.substring(0, value.indexOf(";")) + ";";
					}
				}
			}
			System.out.println("cookie=="+cookie);
			System.out.println("=================================");
			System.out.println("=================================");
			
			token = token.substring(token.indexOf(":")+1,token.indexOf("}")).replace("\"", "");
			System.out.println("token=="+token);
			
			System.out.println("borad[0]=="+borad[0]);
			System.out.println("borad[1]=="+borad[1]);
			
			String url = host+".163.com/" +
							"api/v1/products/a2869674571f77b5a0867c3d71db5856/" +
							"threads/" +borad[0]+
							"/comments/" +borad[1]+
							"/action/upvote?ntoken="+token+"&ibc=newspc";
			System.out.println("url2=="+url);
			URL u2 = new URL(url);
			HttpURLConnection c2 = (HttpURLConnection) u2.openConnection();
			
			c2.addRequestProperty("Host",  rhost);
			c2.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			c2.addRequestProperty("Accept", "*/*");
			c2.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
			c2.addRequestProperty("X-Requested-With", "XMLHttpRequest");
			c2.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			c2.addRequestProperty("Referer", sUrl);
			c2.addRequestProperty("Cookie", cookie);
			c2.addRequestProperty("Connection", "keep-alive");
			c2.setDoInput(true);
			c2.setDoOutput(true);
			c2.setUseCaches(false);
			PrintWriter out = new PrintWriter(c2.getOutputStream());
			out.print("");
			out.flush();
			
			InputStream f= c2.getInputStream();
			Scanner fs = new Scanner(f);
			while(fs.hasNext()){
				String scsc11 = fs.nextLine();
				System.out.println(scsc11);
			}
			MQSender.toMQ(taskdo,"");
			logger.info("end ===================================================");
			logger.info("end ===================================================");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
}
