package com.longriver.netpro.webview.controller;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 腾讯视频评论
 * @author rhy
 * @2017-9-4 下午3:20:12
 * @version v1.0
 */
public class QQVideoComment {

	private static Logger logger = Logger.getLogger(QQVideoComment.class);
	
	public static void main(String[] args) {
		
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("https://v.qq.com/x/cover/st7ipfmu3152pwh.html");
		task.setAddress("https://v.qq.com/x/cover/ey4dq5m2nq186hi.html");
		task.setCorpus("主演都有谁呀");
		task.setCorpus("神奇女侠的首播还不错");
		task.setCorpus("还不错呀你，的");
		task.setNick("3567918550");
		task.setPassword("wxb123456");
		
		toComment(task);
	}
	
	/**
	 * 腾讯登录
	 * @param task 引导所需内容
	 */
	public static void toComment(TaskGuideBean task){
		
		WebDriver driver = null;
		try{
		
		java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config\\properties\\paramsConfig");
		String firefoxUrl = bundle.getString("firefoxurl");
		
		System.setProperty("webdriver.firefox.bin",firefoxUrl);
		driver = new FirefoxDriver(); 
		
		driver.get("http://ui.ptlogin2.qq.com/cgi-bin/login?hide_title_bar=0&low_login=0&qlogin_auto_login=1&no_verifyimg=1&link_target=blank&appid=636014201&target=self&s_url=http%3A//www.qq.com/qq2012/loginSuccess.htm");  
		
		WebElement login = driver.findElement(By.id("switcher_plogin"));
		login.click();
		
		WebElement username = driver.findElement(By.id("u"));
		username.clear();
		username.sendKeys(task.getNick());
		
		WebElement password = driver.findElement(By.id("p"));
		password.clear();
		password.sendKeys(task.getPassword());
		
		WebElement loginButton = driver.findElement(By.id("login_button"));
		loginButton.click();

		Set<Cookie> cookies = driver.manage().getCookies();
		
		String skeyCookie = "";
		for (Cookie cookie : cookies) {
			
			skeyCookie += cookie.toString().substring(0,cookie.toString().indexOf(";")) + "; ";
		}
		
		String result = toGenTie(skeyCookie,task);
		
		if(result != null && "end".equals(result)){
			
			driver.quit();
		}
		}catch(Exception e){
			
			e.printStackTrace();
			logger.info("start firefox exception...");
			if(driver != null){
				
			driver.quit();
			}
		}
		
	}

	/**
	 * 评论
	 * @param skey task
	 * @return
	 */
	private static String toGenTie(String skey,TaskGuideBean task) {
		
		
		String tkey = "";
		if(skey!=null && skey.indexOf("skey")>0){
		tkey = skey.substring(skey.indexOf("skey=")+5);
		tkey = tkey.substring(0,tkey.indexOf(";"));
		
		}else{
			
			logger.info("login fail...");
			isSuccess(task,"失败");
			return "end";
		}
		String vid = getVid(task);
		
		String commentId = null;
		if(vid != null){
			
			commentId = getCommentId(vid);
		}
		
		try{
			
			URL url = new URL("https://w.coral.qq.com/article/comment/");
			HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
			
			String comment = URLDecoder.decode(task.getCorpus(),"gb2312");
			
			String param = "targetid="+commentId+"" +
					"&type=1&" +
					"format=SCRIPT" +
					"&callback=parent.topCallback" +
					"&content="+URLEncoder.encode(comment,"gb2312")+"" +
					"&_method=put" +
					"&g_tk="+GetG_TK(tkey)+"" +
					"&code=0" +
					"&source=9" +
					"&subsource=0" +
					"&picture=";

			openConnection.addRequestProperty("Host", "w.coral.qq.com");
			openConnection.addRequestProperty("Connection", "keep-alive");
			openConnection.addRequestProperty("Content-Length", String.valueOf(param.length()));
			openConnection.addRequestProperty("Cache-Control", "max-age=0");
			openConnection.addRequestProperty("Origin", "https://v.qq.com");
			openConnection.addRequestProperty("Upgrade-Insecure-Requests", "1");
			openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			openConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			openConnection.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			openConnection.addRequestProperty("Referer", "https://v.qq.com/txyp/coralComment_yp_1.0.htm");
			openConnection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			openConnection.addRequestProperty("Cookie", skey);
			
			openConnection.setDoInput(true);
			openConnection.setDoOutput(true);
			openConnection.setRequestMethod("POST");
			openConnection.setUseCaches(false);
			
			PrintWriter pw = new PrintWriter(openConnection.getOutputStream());
			pw.print(param);
			pw.flush();
			
			Scanner sc = new Scanner(openConnection.getInputStream(),"utf-8");
			
			String result = "";
			while(sc.hasNext()){
				
				result += sc.nextLine();
						
			}
			String errorCode = result.substring(result.indexOf("errCode"));
			errorCode = errorCode.substring(errorCode.indexOf(":")+1,errorCode.indexOf(","));
			
			if(errorCode!=null && "0".equals(errorCode)){
				
				isSuccess(task, "");
				return "end";
			}else{
				
				logger.info("return data fail...");
				isSuccess(task, "失败");
				return "end";
			}
		
		}catch(Exception e){
			
			e.printStackTrace();
			logger.error("qq video comment exception...");
			isSuccess(task, "失败");
			return "end";
		}
	}

	/**
	 * 获取评论id
	 * @return
	 */
	private static String getCommentId(String vid) {
		
		try{
			String preUrl = "https://ncgi.video.qq.com/fcgi-bin/video_comment_id?otype=json" +
					"&callback=jQuery19103120702532333608_1504505925757" +
					"&op=3" +
					"&vid="+vid+"" +
					"&_="+System.currentTimeMillis();
			
		URL url = new URL(preUrl);
		
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		
		openConnection.connect();
		
		Scanner sc = new Scanner(openConnection.getInputStream(),"utf-8");
		
		String result = "";
		
		while(sc.hasNext()){
			
			result += sc.nextLine();
		}
		
		result = result.substring(result.indexOf("(")+1,result.indexOf(")"));
		JSONObject parseResult = JSON.parseObject(result);
		String commentId = parseResult.getString("comment_id");
		
		return commentId;
		}catch(Exception e){
			
			e.printStackTrace();
			logger.error("qq video get commentId exception...");
		}
		return null;
	}

	/**
	 * 获取vid
	 * @return
	 */
	public static String getVid(TaskGuideBean task){
		
		try{
		URL url = new URL(task.getAddress());
		
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		
		openConnection.connect();
		
		Scanner sc = new Scanner(openConnection.getInputStream(),"utf-8");
		
		String result = "";
		
		while(sc.hasNext()){
			
			result += sc.nextLine();
		}
		
		String vid = result.substring(result.indexOf("VIDEO_INFO")+10);
		
		vid = vid.substring(0,vid.indexOf("}")+1).replaceAll("=", "").trim();
		
		JSONObject parseVid = JSON.parseObject(vid);
		vid = parseVid.getString("vid");

		return vid;
		}catch(Exception e){
			
			e.printStackTrace();
			logger.error("qq video get vid exception...");
		}
		return null;
	}
	
	/**
	 * 获取tk的值
	 * @param str skey的值
	 * @return
	 */
	public static String GetG_TK(String str){  
		 int hash = 2013;  
		 for(int i = 0, len = str.length(); i < len; ++i){  
		 hash += (hash << 5) + (int)(char)str.charAt(i);  
		 }  
		 return (hash & 0x7fffffff)+"";  
	 } 
	
	/**
	 * 判断是否成功
	 */
	public static void isSuccess(TaskGuideBean task,String msg){
		MQSender.toMQ(task,msg);
	}
}
