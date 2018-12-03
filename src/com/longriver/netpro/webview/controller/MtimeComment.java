package com.longriver.netpro.webview.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.util.DriverGet;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 时光电影评论
 * @author rhy
 * @2017-11-16 下午1:48:37
 * @version v1.0
 */
public class MtimeComment {

	private static Logger logger = LoggerFactory.getLogger(MtimeComment.class);
	
	public static void main(String[] args) {
		
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://news.mtime.com/2017/11/14/1575511.html");
		task.setCorpus("hahaha");
		task.setNick("18435160035");
		task.setPassword("q1w2e3r4");
		
		toComment(task);
	}

	/**
	 * 时光网评论
	 * @param task
	 */
	public static void toComment(TaskGuideBean task) {
	
		try{
		String address = URLDecoder.decode(task.getAddress(),"utf-8");
		String articelId = address.substring(address.lastIndexOf("/")+1,address.indexOf(".html")).trim();
		
		String cookie = toLogin(task);
		
		URL url = new URL("http://service.library.mtime.com/CMS.api?Ajax_CallBack=true" +
				"&Ajax_CallBackType=Mtime.Library.Services" +
				"&Ajax_CallBackMethod=AddOrEditCmsNewsComment" +
				"&Ajax_CrossDomain=1" +
				"&Ajax_RequestUrl="+address+"" +
				"&t=" +
				"&Ajax_CallBackArgument0="+articelId+"" +
				"&Ajax_CallBackArgument1=0" +
				"&Ajax_CallBackArgument2=-1" +
				"&Ajax_CallBackArgument3="+URLEncoder.encode(task.getCorpus(),"utf-8")+"" +
				"&Ajax_CallBackArgument4=%E7%82%B9%E5%87%BB%E8%8E%B7%E5%8F%96%E9%AA%8C%E8%AF%81%E7%A0%81" +
				"&Ajax_CallBackArgument5=" +
				"&Ajax_CallBackArgument6=0" +
				"&Ajax_CallBackArgument7=0");
		
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();

		openConnection.addRequestProperty("Host", "service.library.mtime.com");
		openConnection.addRequestProperty("Connection", "keep-alive");
		openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.75 Safari/537.36");
		openConnection.addRequestProperty("Accept", "*/*");
		openConnection.addRequestProperty("Referer", address);
		openConnection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
		openConnection.addRequestProperty("Cookie", cookie);
		openConnection.connect();
		
		if(openConnection.getResponseCode() != HttpURLConnection.HTTP_OK){
			
			logger.info("状态码错误");
			isSuccess(task, "状态码错误");
			return;
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(openConnection.getInputStream(),"utf-8"));
		String line;
		StringBuffer sb = new StringBuffer();
		while((line = br.readLine())!=null){
			sb.append(line);
		}
		
		System.out.println(sb.toString());
		String result = sb.toString();
		
		String error = result.substring(result.indexOf("{"),result.lastIndexOf("}")+1);
		JSONObject parseObject = JSON.parseObject(error);
		error = parseObject.getString("error");
		
		if(error == null || "null".equals(error)){
			
			logger.info("评论成功");
			isSuccess(task, "");
		}else{
			
			logger.info("评论失败");
			isSuccess(task, "评论失败");
		}
		}catch(Exception e){
			
			logger.info("评论异常");
			isSuccess(task, "评论异常");
			e.printStackTrace();
		}
	}
	
	/**
	 * 登录
	 * @param task
	 * @return
	 */
	public static String toLogin(TaskGuideBean task){
		
		WebDriver driver = null;
		try{
		java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config\\properties\\paramsConfig");
		String firefoxUrl = bundle.getString("firefoxurl");
		
		System.setProperty("webdriver.firefox.bin",firefoxUrl);
		driver = new FirefoxDriver(); 
		
		driver.get("https://passport.mtime.com/member/signin/");
		
		WebElement username = driver.findElement(By.id("loginEmailText"));
		username.clear();
		username.sendKeys(task.getNick());
		
		WebElement password = driver.findElement(By.id("loginPasswordText"));
		password.clear();
		password.sendKeys(task.getPassword());
		
		WebElement loginButton = driver.findElement(By.id("loginButton"));
		loginButton.click();
		
		Thread.sleep(3000);
		Set<Cookie> cookies = driver.manage().getCookies();
		
		String skeyCookie = "";
		for (Cookie cookie : cookies) {
			
			skeyCookie += cookie.toString().substring(0,cookie.toString().indexOf(";")) + "; ";
		}
		
		DriverGet.quit(driver);;
		return skeyCookie;
		}catch(Exception e){
			
			logger.info("登录失败");
			DriverGet.quit(driver);
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
}
