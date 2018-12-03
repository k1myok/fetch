package com.longriver.netpro.webview.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Test;
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
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 新浪视频评论
 * @author rhy
 * @2017-9-4 下午3:48:00
 * @version v1.0
 */
public class SinaVideoComment {

	private static Logger logger = Logger.getLogger(SinaVideoComment.class);
	
	public static void main(String[] args) {
		
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://video.sina.com.cn/p/news/o/doc/2017-05-05/102166193449.html");
		task.setAddress("http://video.sina.com.cn/p/news/s/doc/2017-08-29/161066979177.html");
		task.setCorpus("这个新闻足够励志");
		task.setCorpus("father");
		task.setNick("liuyang1980002@sina.com");
		task.setPassword("inter1908");
		task.setNick("tungu55249@163.com");
		task.setPassword("a123456");
		
		
		toComment(task);
	}

	/**
	 * 新浪登录
	 * @param task
	 */
	public static void toComment(TaskGuideBean task) {
		
		WebDriver driver = null;
		try{
		java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config\\properties\\paramsConfig");
		String firefoxUrl = bundle.getString("firefoxurl");
		
		System.setProperty("webdriver.firefox.bin",firefoxUrl);
		driver = new FirefoxDriver();
		driver.get("https://login.sina.com.cn/signup/signin.php");
		
		WebElement username = driver.findElement(By.id("username"));
		username.clear();
		username.sendKeys(task.getNick());
		
		WebElement password = driver.findElement(By.id("password"));
		password.clear();
		password.sendKeys(task.getPassword());
		
		WebElement loginButton = driver.findElement(By.xpath("//*[@id='vForm']/div[2]/div/ul/li[7]/div[1]/input"));
		loginButton.click();
		
		Thread.sleep(3000);
		
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
			
			logger.error("sina video login exception...");
			e.printStackTrace();
			if(driver != null){
				
				driver.quit();
			}
		}
	}

	/**
	 * 跟帖
	 * @param skeyCookie 评论需要的cookie
	 */
	private static String toGenTie(String cookie,TaskGuideBean task) {
	
		try{
			
		Map<String,String> paramMap = getParam(task.getAddress());
		
		String preUrl = "http://comment5.news.sina.com.cn/cmnt/submit";
		URL url = new URL(preUrl);
		
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		
		String param = "channel="+paramMap.get("channel")+"" +
				"&newsid="+paramMap.get("newsid")+"" +
				"&parent=" +
				"&content="+task.getCorpus()+"" +
				"&format=json" +
				"&ie=utf-8" +
				"&oe=utf-8" +
				"&ispost=" +
				"&share_url="+task.getAddress()+"" +
				"&video_url=" +
				"&img_url=" +
				"&iframe=1" +
				"&callback=iJax"+System.currentTimeMillis();

		openConnection.addRequestProperty("Host", "comment5.news.sina.com.cn");
		openConnection.addRequestProperty("Connection", "keep-alive");
		openConnection.addRequestProperty("Content-Length", String.valueOf(param.length()));
		openConnection.addRequestProperty("Cache-Control", "max-age=0");
		openConnection.addRequestProperty("Origin", "http://video.sina.com.cn");
		openConnection.addRequestProperty("Upgrade-Insecure-Requests", "1");
		openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		openConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		openConnection.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		openConnection.addRequestProperty("Referer", task.getAddress());
		openConnection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
		openConnection.addRequestProperty("Cookie", cookie);
		
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
		
		logger.info(result);
		String callBack = result.substring(result.indexOf("(")+1,result.indexOf(")"));
		
		JSONObject parseCallBack = JSON.parseObject(callBack);
		String parseResult = parseCallBack.getString("result");
		
		JSONObject parseRes = JSON.parseObject(parseResult);
		String code = parseRes.getString("filter_code");
		
		if("4002".equals(code) || "4000".equals(code)){
			
			isSuccess(task, "");
			return "end";
		}else{
			
			isSuccess(task, "失败");
//			toCommentAgain(task);
			logger.info("sina video comment fail...");
			return "end";
		}
		}catch(Exception e){
			
			e.printStackTrace();
			isSuccess(task, "失败");
			logger.error("sina video comment excepton...");
			return "end";
		}
	}

	/**
	 * 获取评论参数
	 * @param address 视频地址
	 * @return
	 */
	private static Map<String, String> getParam(String address) {
		
		try{
		Map<String,String> paramMap = new HashMap<String,String>();
			
		URL url = new URL(address);
		
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		
		openConnection.connect();
		
		Scanner sc = new Scanner(openConnection.getInputStream(),"utf-8");
		String result = "";
		while(sc.hasNext()){
			
			result += sc.nextLine();
		}
		
		String paramConfig = result.substring(result.indexOf("$SCOPE['shareconfig']"));
		
		String channel = paramConfig.substring(paramConfig.indexOf("channel")+8);
		channel = channel.substring(0,channel.indexOf("cid")).replaceAll("'", "").replaceAll(",", "").trim();
		
		String newsid = paramConfig.substring(paramConfig.indexOf("newsid")+7);
		newsid = newsid.substring(0,newsid.indexOf("ad_state")).replaceAll("'", "").replaceAll(",", "");
		
		paramMap.put("channel", channel);
		paramMap.put("newsid", newsid);
		
		return paramMap;
		}catch(Exception e){
			
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 判断是否成功
	 */
	public static void isSuccess(TaskGuideBean task,String msg){
		MQSender.toMQ(task,msg);
	}
	
	/**
	 * 新浪登录
	 * @param task
	 */
	public static void toCommentAgain(TaskGuideBean task) {
		
		WebDriver driver = null;
		try{
		java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config\\properties\\paramsConfig");
		String firefoxUrl = bundle.getString("firefoxurl");
		
		System.setProperty("webdriver.firefox.bin",firefoxUrl);
		driver = new FirefoxDriver();
		driver.get("https://login.sina.com.cn/signup/signin.php");
		
		WebElement username = driver.findElement(By.id("username"));
		username.clear();
		username.sendKeys(task.getNick());
		
		WebElement password = driver.findElement(By.id("password"));
		password.clear();
		password.sendKeys(task.getPassword());
		
		WebElement loginButton = driver.findElement(By.xpath("//*[@id='vForm']/div[2]/div/ul/li[7]/div[1]/input"));
		loginButton.click();
		
		WebElement vercode = driver.findElement(By.id("door"));
		vercode.clear();
		vercode.sendKeys(getVerCode());
		
		loginButton.click();
		Thread.sleep(3000);
		
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
			
			logger.error("sina video login exception...");
			e.printStackTrace();
			if(driver != null){
				
				driver.quit();
			}
		}
	}

	/**
	 * 获取验证码
	 * @return
	 */
	private static String getVerCode() {
		String randRom="";
		try {
			URL u211 = new URL("https://login.sina.com.cn/cgi/pin.php?r=85539242&s=0");
			HttpURLConnection c211 = (HttpURLConnection) u211.openConnection();
			c211.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			c211.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			c211.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			c211.connect();
			InputStream i211 = c211.getInputStream();
			byte[] bs = new byte[1024];  
			int len;  

			String imgPath = "d:\\vcode\\sinacode.jpg";

			OutputStream os = new FileOutputStream(imgPath);  
			while ((len = i211.read(bs)) != -1) {  
				os.write(bs, 0, len);  
			}  
			os.close();  
			i211.close();
			randRom = RuoKuai.createByPostNew("5000",imgPath);
			System.out.println("result========================"+randRom);
		} catch (IOException e) {

			e.printStackTrace();
		}
		return randRom;
	}
	
}
