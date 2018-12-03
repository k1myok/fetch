package com.longriver.netpro.webview.controller;

import java.io.PrintWriter;
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
 * 网易视频评论
 * @author rhy
 * @2017-9-6 上午10:06:20
 * @version v1.0
 */
public class WyVideoComment {

	private static Logger logger = Logger.getLogger(WyVideoComment.class);
	
	public static void main(String[] args) {
		
		//http://sdk.comment.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads/BR2NJQL3008535RB/comments?ibc=jssdk&_=1505095725325
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://v.163.com/paike/VBFMA0F1R/VBHDNJNS2.html");
		task.setAddress("http://v.163.com/zixun/V8GAM7JAP/VBR2NJQL3.html#from=rank");
		task.setAddress("http://v.163.com/zixun/V7M3CBCH5/V7PRCG7KA.html#from=rank");
		task.setAddress("http://v.163.com/zixun/VBMGGRV60/VCKQT0CN2.html#from=rank");
		task.setCorpus("海天蚝油是这个味吗");
		task.setCorpus("好像就是秋天多点呀");
		task.setCorpus("立秋后挺多的呀");
		task.setNick("bijielun@163.com");
		task.setPassword("bifeng110");
		
		toComment(task);
		
	}

	/**
	 * 网易视频登录
	 * @param task
	 */
	public static void toComment(TaskGuideBean task) {
		
		WebDriver driver = null;
		try{
		
		java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config\\properties\\paramsConfig");
		String firefoxUrl = bundle.getString("firefoxurl");
		
		System.setProperty("webdriver.firefox.bin",firefoxUrl);
		driver = new FirefoxDriver(); 
		driver.get("http://news.163.com/special/news_test212/");
		
		WebElement preLoginButton = driver.findElement(By.xpath("//*[@id='login-before']/a[1]"));
		preLoginButton.click();
		
		
		WebElement username = driver.findElement(By.id("email"));
		username.clear();
		username.sendKeys(task.getNick());
		
		
		WebElement password = driver.findElement(By.id("password"));
		password.clear();
		password.sendKeys(task.getPassword());
		
		WebElement loginButton = driver.findElement(By.xpath("//form/div[4]/input"));
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
	 * 去跟帖
	 * @param cookie cookie信息
	 * @param task 引导信息
	 * @return
	 */
	private static String toGenTie(String cookie,TaskGuideBean task) {
		
		try{
			String articelUrl = task.getAddress();
			Map<String,String> articleMap = getArticleId(articelUrl);
			
			String preUrl = "http://sdk.comment.163.com/api/v1/products/"+articleMap.get("productKey")+"/threads/"+articleMap.get("docId")+"/comments?ibc=jssdk&_="+System.currentTimeMillis();
			URL url = new URL(preUrl);
			HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
			
			String param = "content="+URLEncoder.encode(task.getCorpus(),"utf-8")+"" +
					"&parentId=";
			
			openConnection.addRequestProperty("Host", "sdk.comment.163.com");
			openConnection.addRequestProperty("Connection", "keep-alive");
			openConnection.addRequestProperty("Content-Length", String.valueOf(param.length()));
			openConnection.addRequestProperty("Origin", "http://v.163.com");
			openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			openConnection.addRequestProperty("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
			openConnection.addRequestProperty("Accept", "*/*");
			openConnection.addRequestProperty("Referer", task.getAddress());
			openConnection.addRequestProperty("Accept-Language", "sdk.comment.163.com");
			openConnection.addRequestProperty("Host", "zh-CN,zh;q=0.8");
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
			JSONObject parseResult = JSON.parseObject(result);
			String against = parseResult.getString("against");
			if(against != null && "0".equals(against)){
				
				logger.info("wy video gentie success...");
				isSuccess(task, "");
				return "end";
			}else{
				
				logger.info("wy video gentie fail...");
				isSuccess(task, "失败");
				return "end";
			}
			
			}catch(Exception e){
				
				logger.info("wy video gentie exception...");
				e.printStackTrace();
				isSuccess(task, "失败");
				return "end";
			}
	}

	/**
	 * 获取文章id
	 * @param articelUrl 文章链接
	 * @return
	 */
	private static Map<String,String> getArticleId(String articelUrl) {
		
		try {
			Map<String, String> articleMap = new HashMap<String, String>();
			
			URL url = new URL(articelUrl);
			HttpURLConnection openConnection = (HttpURLConnection) url
					.openConnection();

			openConnection.connect();

			Scanner sc = new Scanner(openConnection.getInputStream(), "utf-8");
			String result = "";
			while (sc.hasNext()) {

				result += sc.nextLine();
			}

			result = result.substring(result.indexOf("config")+6);
			result = result.substring(0,result.indexOf(";")).replaceAll("=", "").trim();
			String productKey = result.substring(result.indexOf("productKey")+10,result.indexOf("docId")).replaceAll("\"", "").replaceAll(":", "").replaceAll(",", "").trim();
			String docId = result.substring(result.indexOf("docId")+5,result.indexOf("target")).replaceAll("\"", "").replaceAll(":", "").replaceAll(",", "").trim();
			
			articleMap.put("productKey", productKey);
			articleMap.put("docId", docId);

			return articleMap;
		} catch (Exception e) {

			logger.error("wy video get articel param exception...");
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
