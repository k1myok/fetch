package com.longriver.netpro.webview.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.longriver.netpro.common.sina.SData;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

public class ThepaperDigg{
	private static Logger logger = Logger.getLogger(ThepaperDigg.class);
	
	@SuppressWarnings("null")
	public static void ifeng(TaskGuideBean taskdo) {
		logger.info("澎湃新闻顶");
		String commentId = taskdo.getPraiseWho();
//		String sUrl = StringUtil.formatString(URLDecoder.decode(taskdo.getAddress(),"utf-8"));
		
		try {
			String params = "commentId="+commentId;
				
			URL u3 = new URL("http://www.thepaper.cn/www/commentPraise.msp");
			//System.out.println(u3);
			HttpURLConnection c3 = (HttpURLConnection) u3.openConnection();
			c3.addRequestProperty("Host", "www.thepaper.cn");
			c3.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			c3.addRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
			c3.addRequestProperty("Accept-Language", "zh-cn");
			c3.addRequestProperty("Connection", "Keep-Alive");
			c3.addRequestProperty("Cache-Control", "no-cache");
			c3.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			c3.addRequestProperty("x-requested-with", "XMLHttpRequest");
			c3.setDoInput(true);
			c3.setDoOutput(true);
			PrintWriter out3 = new PrintWriter(c3.getOutputStream());
			out3.print(params);
			out3.flush();
			InputStream i3 = c3.getInputStream();
			Scanner s3 = new Scanner(i3, "utf-8");
			boolean b = false;
			while(s3.hasNext()){
				String scsc = s3.nextLine();
				System.out.println(scsc);
				
			}
			
			String statusCd = "0";
			String link = "error";
			if(b){
				statusCd = "1";
				link = "发送成功";
				System.out.println(link);
			}
			MQSender.toMQ(taskdo,"");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	public static void main(String arsg[]){
//		data.setString("commentId","4955312");
		TaskGuideBean b = new TaskGuideBean();
		b.setPraiseWho("4955312");
		new ThepaperDigg().ifeng(b);
	}
}
