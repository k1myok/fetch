package com.longriver.netpro.webview.controller;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
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
 * 凤凰视频评论
 * @author rhy
 * @2017-9-6 下午6:04:21
 * @version v1.0
 */
public class IFengVideoComment {

	private static Logger logger = Logger.getLogger(IFengVideoComment.class);
	
	public static void main(String[] args) {
		
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://v.ifeng.com/video_8487472.shtml");
//		task.setAddress("http://v.ifeng.com/video_8184468.shtml");
		task.setCorpus("我去看看，ya ");
		task.setCorpus("终极可以吗");
		task.setCorpus("hai可以吧");
		task.setNick("13611313453");
		task.setPassword("hailele333");
		task.setNick("只看热闹不买");
		task.setPassword("jixnyu19700");
		toComment(task);
	}

	/**
	 * 凤凰登录
	 * @param task
	 */
	public static void toComment(TaskGuideBean task) {
		WebDriver driver = null;
		try{
		
		java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config\\properties\\paramsConfig");
		String firefoxUrl = bundle.getString("firefoxurl");
		
		System.setProperty("webdriver.firefox.bin",firefoxUrl);
		driver = new FirefoxDriver(); 
		driver.get("http://id.ifeng.com/muser/login");  
		
		WebElement username = driver.findElement(By.id("username"));
		username.clear();
		username.sendKeys(task.getNick());
		
		WebElement password = driver.findElement(By.id("password"));
		password.clear();
		password.sendKeys(task.getPassword());
		
		WebElement loginButton = driver.findElement(By.xpath("//form[@target='LoginFrame']/input[7]"));
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
			
			e.printStackTrace();
			logger.info("start firefox exception...");
			if(driver != null){
				
			driver.quit();
			}
		}
	}

	/**
	 * 凤凰评论
	 * @param cookie  cookie信息
	 * @param task 引导信息
	 * @return
	 */
	private static String toGenTie(String cookie, TaskGuideBean task) {

		try{
			
		Map<String,String> paramMap = getParam(task);
		
		String preUrl = "http://comment.ifeng.com/postv.php?content="+URLEncoder.encode(task.getCorpus(),"utf-8")+
				"&docName="+URLEncoder.encode(paramMap.get("name"),"utf-8")+"" +
				"&docUrl="+paramMap.get("vid")+"" +
				"&skey="+paramMap.get("skey")+"" +
				"&format=js" +
				"&postto=" +
				"&callback=publicComment";
		
		URL url = new URL(preUrl);
		
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
				
		openConnection.addRequestProperty("Host", "comment.ifeng.com");
		openConnection.addRequestProperty("Connection", "keep-alive");
		openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		openConnection.addRequestProperty("Accept", "*/*");
		openConnection.addRequestProperty("Referer", "http://v.ifeng.com/video_8487472.shtml");
		openConnection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
		openConnection.addRequestProperty("Cookie", cookie);
		
		openConnection.connect();
		
		Scanner sc = new Scanner(openConnection.getInputStream(),"utf-8");
		
		String result = "";
		while(sc.hasNext()){
			
			result += sc.nextLine();
		}
		
		logger.info(result);
		
		String code = result.substring(result.indexOf("code")+4);
		code = code.substring(0,code.indexOf(",")).replaceAll("\"", "").replaceAll(":", "").trim();
		
		if(code!=null && "1".equals(code)){
			
			logger.info("ifeng video comment success...");
			isSuccess(task,"");
			return "end";
		}else{
			
			logger.info("ifeng video comment fail...");
			isSuccess(task,"失败");
			return "end";
		}
		}catch(Exception e){
			
			e.printStackTrace();
			logger.info("ifeng video comment exception...");
			isSuccess(task,"失败");
			return "end";
		}
	}

	/**
	 * 获取评论参数
	 * @param task
	 * @return
	 */
	private static Map<String, String> getParam(TaskGuideBean task) {
		
		try{
			
		Map<String,String> paramMap = new HashMap<String,String>();
			
		URL url = new URL(task.getAddress());
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		openConnection.connect();
		
		Scanner sc = new Scanner(openConnection.getInputStream(),"utf-8");
		String result = "";
		while(sc.hasNext()){
			
			result += sc.nextLine();
		}
		
		result = result.substring(result.indexOf("videoinfo")+9,result.indexOf("}")+1).replaceAll("=", "").trim();
		
		JSONObject parseResult = JSON.parseObject(result);
		String vid = parseResult.getString("vid");
		String name = parseResult.getString("name");
		String skey = parseResult.getString("skey");
		
		paramMap.put("vid", vid);
		paramMap.put("name", name);
		paramMap.put("skey", skey);
		
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
}
