package com.longriver.netpro.webview.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.util.DriverGet;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 马蜂窝评论
 * @author rhy
 * @2017-11-21 上午10:08:41
 * @version v1.0
 */
public class MafengwoComment {

	private static Logger logger = LoggerFactory.getLogger(MafengwoComment.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//登录地址
		//https://passport.mafengwo.cn/
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://www.mafengwo.cn/i/7847488.html");
		task.setAddress("http://www.mafengwo.cn/sales/2262967.html");
		task.setCorpus("这个版本还行吗a");
		task.setCorpus("挺贵的");
		task.setNick("18435160035");
		task.setPassword("q1w2e3r4");
		
		toComment(task);
	}

	/**
	 * 评论
	 * @param task
	 */
	public static void toComment(TaskGuideBean task) {
	
		try{
			
		String address = URLDecoder.decode(task.getAddress(),"utf-8");
		String cookie = getCookie(task);
		
		URL url = new URL("http://www.mafengwo.cn/note/ajax_post.php");
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		
		String articleId = address.substring(address.lastIndexOf("/")+1,address.indexOf("html")-1);
		
		String param = "sAction=publishReply" +
				"&iId="+articleId+"" +
				"&iPropid=0" +
				"&sContent="+URLEncoder.encode(task.getCorpus(),"utf-8")+"";

		openConnection.addRequestProperty("Host", "www.mafengwo.cn");
		openConnection.addRequestProperty("Connection", "keep-alive");
		openConnection.addRequestProperty("Content-Length", String.valueOf(param.length()));
		openConnection.addRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		openConnection.addRequestProperty("Origin", "http://www.mafengwo.cn");
		openConnection.addRequestProperty("X-Requested-With", "XMLHttpRequest");
		openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.75 Safari/537.36");
		openConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		openConnection.addRequestProperty("Referer", address);
		openConnection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
		openConnection.addRequestProperty("Cookie", cookie);
		
		openConnection.setDoInput(true);
		openConnection.setDoOutput(true);
		openConnection.setRequestMethod("POST");
		openConnection.setUseCaches(false);
		
		
		PrintWriter pw = new PrintWriter(openConnection.getOutputStream());
		pw.print(param);
		pw.flush();
		
		if(openConnection.getResponseCode()!=HttpURLConnection.HTTP_OK){
			
			logger.info("状态码异常");
			isSuccess(task, "状态码异常");
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
		
		String ret = null;
		if(result.indexOf("payload")>-1){
			
		JSONObject parseObject = JSON.parseObject(result);
		String payload = parseObject.getString("payload");
		JSONObject parsePayload = JSON.parseObject(payload);
		ret = parsePayload.getString("ret");
		
		}else if(result.indexOf("errno")>0){
			
			JSONObject parseObj = JSON.parseObject(result);
			String error = parseObj.getString("error");
			JSONObject parseError = JSON.parseObject(error);
			String errno = parseError.getString("errno");
			if(errno != null && "30".equals(errno)){
				
				isSuccess(task, "游记已经删除");
				return;
			}else{
				
				isSuccess(task, "未知错误1");
				return;
			}
			
		}else{
			
			logger.info("未登录");
			isSuccess(task, "未登录");
			return;
		}
		
		if(ret != null && "1".equals(ret)){
			
			logger.info("评论成功");
			isSuccess(task, "");
		}else{
			
			logger.info("评论失败");
			isSuccess(task, "未知错误2");
		}
		}catch(Exception e){
			
			logger.info("评论异常");
			isSuccess(task, "评论异常");
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取cookie
	 * @param task
	 * @return
	 */
	private static String getCookie(TaskGuideBean task){
		
		WebDriver driver = null;
		try{
			
			java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config\\properties\\paramsConfig");
			String firefoxUrl = bundle.getString("firefoxurl");
			
			System.setProperty("webdriver.firefox.bin",firefoxUrl);
			driver = new FirefoxDriver(); 
			driver.get("https://passport.mafengwo.cn/");  
			
			WebElement username = driver.findElement(By.xpath("//*[@id='_j_login_form']/div[1]/input"));
			username.clear();
			username.sendKeys(task.getNick());
			
			WebElement password = driver.findElement(By.xpath("//*[@id='_j_login_form']/div[2]/input"));
			password.clear();
			password.sendKeys(task.getPassword());
			
			WebElement loginButton = driver.findElement(By.xpath("//*[@id='_j_login_form']/div[5]/button"));
			loginButton.click();
			
			Thread.sleep(5000);
			Set<Cookie> cookies = driver.manage().getCookies();
			
			String skeyCookie = "";
			for (Cookie cookie : cookies) {
				
				skeyCookie += cookie.toString().substring(0,cookie.toString().indexOf(";")) + "; ";
			}
			
			DriverGet.quit(driver);
			return skeyCookie;
			}catch(Exception e){
				
				DriverGet.quit(driver);
				e.printStackTrace();
				logger.info("start firefox exception...");
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
