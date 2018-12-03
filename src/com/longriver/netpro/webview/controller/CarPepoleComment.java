package com.longriver.netpro.webview.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.Base64;

import com.alibaba.fastjson.JSON;
import com.longriver.netpro.common.sina.SData;
import com.longriver.netpro.common.sohu.PepoleScript;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

public class CarPepoleComment {
	
	private static Logger logger = Logger.getLogger(CarPepoleComment.class);
	
	public static void main(String arsg[]){
		TaskGuideBean b = new TaskGuideBean();
		b.setNick("wolffirm@163.com");
		b.setPassword("wangmeng121");
		b.setAddress("http://sports.people.com.cn/n1/2016/0420/c149182-28292072.html");
		b.setCorpus("等等等等");
		b.setCorpus("好久没来了");
		xinhua(b);
	}
	
	public static void xinhua(TaskGuideBean taskdo){
		logger.info("执行人民网新闻评论");
		SData data = new SData();
		
		try {
			String userId =taskdo.getNick();
			String pwd = taskdo.getPassword();
			String contents = taskdo.getCorpus();
			String sUrl = taskdo.getAddress();
			
			//微博登陆用户名
			 data.setString("user_account_id",userId);
			//微博登陆密码
			 data.setString("user_account_pw",pwd);
			//目标地址
			 data.setString("mission_addr",sUrl);
			//评论转发内容
			 data.setString("mission_contents",contents);
			 
			String cookie="";
			URL u1 = new URL("http://sso.people.com.cn/getRandomCode?userName="+URLEncoder.encode(userId, "utf-8")+"&_r=0.20134093472734094&callback=rmwsso_getscript_callback_338654");
			HttpURLConnection c1 = (HttpURLConnection) u1.openConnection();
			//_FakeX509TrustManager.allowAllSSL();
			c1.addRequestProperty("Host", "sso.people.com.cn");
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
				if(scsc.indexOf("\"t\":") > -1){
					token = scsc.substring(scsc.indexOf("\"t\":")).replace("\"t\":", "").trim();
					token = token.substring(1, token.indexOf(",")).replace("\"", "");
				}
				if(scsc.indexOf("\"s\":") > -1){
					s = scsc.substring(scsc.indexOf("\"s\":")).replace("\"s\":", "").trim();
					s = s.substring(1, s.indexOf(",")).replace("\"", "");
				}
				if(scsc.indexOf("\"c\":") > -1){
					c = scsc.substring(scsc.indexOf("\"c\":")).replace("\"c\":", "").trim();
					c = c.substring(1, c.indexOf("}")).replace("\"", "");
				}
			}
			
			String threadId = sUrl.substring(sUrl.lastIndexOf("/")).replace("/", "").replace(".html", "").replace(".shtml", "").replace(".htm", "");
			logger.info("11111111");
			threadId = threadId.substring(threadId.indexOf("-")+1);
					URL fu = new URL("http://sso.people.com.cn/login");
					HttpURLConnection fc = (HttpURLConnection) fu.openConnection();
					String fp = "loginName="+ URLEncoder.encode(userId, "utf-8")+"&password="+PepoleScript.getHexMd5(PepoleScript.getHexMd5(PepoleScript.getHexMd5(pwd)+s)+c)+"&token="+token+"&appCode=&remember=1";
							
					fc.addRequestProperty("Host", "sso.people.com.cn");
					fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
					fc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
					fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
					fc.addRequestProperty("Referer", sUrl);
					fc.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					fc.addRequestProperty("Content-Length", String.valueOf(fp.length()));
					fc.setInstanceFollowRedirects(false);
					fc.setDoInput(true);
					fc.setDoOutput(true);
					PrintWriter fo = new PrintWriter(fc.getOutputStream());
					fo.print(fp);
					fo.flush();
					Map<String, List<String>> m1 = fc.getHeaderFields();
					for(Map.Entry<String,List<String>> entry : m1.entrySet()){
						if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
							for(String value : entry.getValue()){
								cookie = cookie + value.substring(0, value.indexOf(";")) + ";";
							}
						}
					}
					URL fu11 = new URL("http://bbs1.people.com.cn/postRecieveFromNews.do?callback=jsonp1461231951914&nid="+threadId+"&isAjax=true&messageContent="+URLEncoder.encode(contents, "utf-8"));
					HttpURLConnection fc1 = (HttpURLConnection) fu11.openConnection();
							
					fc1.addRequestProperty("Host", "bbs1.people.com.cn");
					fc1.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
					fc1.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
					fc1.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
					fc1.addRequestProperty("Referer", sUrl);
					fc1.addRequestProperty("Cookie", cookie);
					fc1.addRequestProperty("Connection", "keep-alive");
					fc1.addRequestProperty("Content-Length", String.valueOf(fp.length()));
					fc1.setInstanceFollowRedirects(false);
					fc1.setDoInput(true);
					fc1.setDoOutput(true);
					fo.print(fp);
					fo.flush();
					InputStream fi = fc1.getInputStream();
					Scanner fs = new Scanner(fi);
					while(fs.hasNext()){
						String scsc2 = fs.nextLine();
						logger.info(scsc2);
					}
					MQSender.toMQ(taskdo,"");
					
		}catch (Exception e) {
			MQSender.toMQ(taskdo,"报错失败");
			e.printStackTrace();
		}
	}
	
	
}
