package com.longriver.netpro.webview.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

public class ThepaperComment{
	
	private static Logger logger = Logger.getLogger(ThepaperComment.class);
	private static String imagesdir = "c:\\";
	private static String imagesp = "c:\\4.jpg";
	
	public static void main(String arsg[]){
		TaskGuideBean bb = new TaskGuideBean();
		bb.setNick("18610647727");
		bb.setPassword("123123");
		bb.setCorpus("要自信...");
		bb.setAddress("http://www.thepaper.cn/newsDetail_forward_1456513");
		ifeng(bb);
	}
	
	public static void ifeng(TaskGuideBean taskdo){
		logger.info("澎湃新闻评论");
		
		try {
			String userId = taskdo.getNick();
			String pwd = taskdo.getPassword();
			String sUrl = taskdo.getAddress();
			String contents = taskdo.getCorpus();
			
			String cid = sUrl.substring(sUrl.lastIndexOf("_")+1);
			String cookie = "";
		//获得验证码
		URL u2 = new URL("http://www.thepaper.cn/www/RandomPicture?"+ new Date().getTime());
		HttpURLConnection c2 = (HttpURLConnection) u2.openConnection();
		c2.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36 SE 2.X MetaSr 1.0");
		c2.addRequestProperty("Accept", "image/webp,*/*;q=0.8");
		c2.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
		c2.addRequestProperty("Connection", "keep-alive");
		c2.addRequestProperty("Host", "www.thepaper.cn");
		c2.addRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
		c2.addRequestProperty("Referer", sUrl);
		
		c2.connect();
		Map<String, List<String>> m1 = c2.getHeaderFields();
		for(Map.Entry<String,List<String>> entry : m1.entrySet()){
			if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
				for(String value : entry.getValue()){
						cookie = value + ";";
				}
			}
		}
		InputStream i2 = c2.getInputStream();
		
		// 1K的数据缓冲  
        byte[] bs = new byte[1024];  
        // 读取到的数据长度  
        int len;  
        // 输出的文件流  
       File sf=new File(imagesdir);  
       if(!sf.exists()){  
           sf.mkdirs();  
       }  
       OutputStream os = new FileOutputStream(imagesp);  
        // 开始读取  
        while ((len = i2.read(bs)) != -1) {  
          os.write(bs, 0, len);  
        }  
        // 完毕，关闭所有链接  
        os.close();  
        i2.close();  
        

		String randRom = RuoKuai.createByPostNew("3040",imagesp);
		System.out.println("result========================"+randRom);
		//登录
		String param = "loginName="+userId+"&psw="+pwd+"&vcode="+randRom+"&remember_me=&isVerify=0";
		URL u1 = new URL("http://www.thepaper.cn/www/login.msp");
		HttpURLConnection c1 = (HttpURLConnection) u1.openConnection();
		c1.addRequestProperty("Host", "www.thepaper.cn");
		c1.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0)Gecko/20100101 Firefox/31.0");
		c1.addRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		c1.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		c1.addRequestProperty("Referer", sUrl);
		c1.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		c1.addRequestProperty("Content-Length", String.valueOf(param.length()));
		c1.addRequestProperty("Cookie", cookie);
		
		c1.setInstanceFollowRedirects(false);
		c1.setDoInput(true);
		c1.setDoOutput(true);
		PrintWriter out = new PrintWriter(c1.getOutputStream());
		out.print(param);
		out.flush();
		Map<String, List<String>> m11 = c1.getHeaderFields();
		for(Map.Entry<String,List<String>> entry : m11.entrySet()){
			if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
				for(String value : entry.getValue()){
						cookie = cookie + value.substring(0, value.indexOf(";")) + ";";
				}
			}
		}
		System.out.println("cookie==================="+cookie);
		
		String params = "c="+cid+"&commentType=1&content=" + URLEncoder.encode(contents, "utf-8");
		//发帖
		URL u3 = new URL("http://www.thepaper.cn/www/comment.msp");
		//System.out.println(u3);
		HttpURLConnection c3 = (HttpURLConnection) u3.openConnection();
		c3.addRequestProperty("Host", "www.thepaper.cn");
		c3.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
		c3.addRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		c3.addRequestProperty("Accept-Language", "zh-cn");
		c3.addRequestProperty("Referer", sUrl);
		c3.addRequestProperty("Cookie", cookie);
		c3.addRequestProperty("Connection", "Keep-Alive");
		c3.addRequestProperty("Cache-Control", "no-cache");
		c3.addRequestProperty("Content-Length", String.valueOf(params.length()));
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
			if(scsc.contains("登录")){
				if(taskdo.getCorpusid()<=2){
					taskdo.setCorpusid(taskdo.getCorpusid()+1);
					ifeng(taskdo);
					return ;
				}else{
					b = true;
				}
			}else{
				b=true;
			}
		}
		
		
		String content = "";
			String link = "error";
			if(b){
				link = "发送成功";
				System.out.println(link);
				
			}else{
				content = "失败";
			}
			MQSender.toMQ(taskdo,content);
		} catch (IOException e) {
			MQSender.toMQ(taskdo,"失败");
			e.printStackTrace();
		}	
	}

	
}
