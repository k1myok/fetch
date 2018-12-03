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
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.longriver.netpro.util.Configur;
import com.longriver.netpro.util.GetProprities;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.PngErjinzhi;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 腾讯新闻评论新版
 * firefox,selenium有兼容性问题，版本需匹配
 * 目前使用的firefox版本为firefox33，selenium的版本为2.44
 * @author rhy
 * @2017-8-29 下午2:50:51
 * @version v1.0
 */
public class QQNewsComment2 {

	static Logger logger = Logger.getLogger(QQNewsComment2.class);
	
	public static void main(String[] args) {
//		http://view.news.qq.com/original/intouchtoday/n3998.html
//		http://view.qq.com/a/20170829/019480.htm
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://view.qq.com/a/20170829/019480.htm");
		task.setAddress("http://view.news.qq.com/original/intouchtoday/n3998.html");
		task.setAddress("http://cq.qq.com/a/20180326/002714.htm");
		task.setCorpus("这个版本还行吗");
		task.setNick("3567918550");
		task.setPassword("wxb123456");
		task.setNick("2183571245");
		task.setPassword("hjscs244");
		
		task.setNick("2598532239");
		task.setPassword("4211432a");
		
		toComment(task);
	}

	/**
	 * 调用firefoxDriver
	 * @param task
	 */
	public static void toComment(TaskGuideBean task) {
		System.out.println("toComment");
//		task.setPng(PngErjinzhi.getImageBinary("1"));
		try{
			
		Configur p = GetProprities.paramsConfig;
		String firefoxUrl = p.getProperty("firefoxurl");
		
		System.setProperty("webdriver.firefox.bin",firefoxUrl);
		WebDriver driver = new FirefoxDriver(); 
		driver.get("http://ui.ptlogin2.qq.com/cgi-bin/login?hide_title_bar=0&low_login=0&qlogin_auto_login=1&no_verifyimg=1&link_target=blank&appid=636014201&target=self&s_url=http%3A//www.qq.com/qq2012/loginSuccess.htm");  
		
		WebElement login = driver.findElement(By.id("switcher_plogin"));
		login.click();
		System.out.println("22222");
		WebElement username = driver.findElement(By.id("u"));
		username.clear();
		username.sendKeys(task.getNick());
		
		WebElement password = driver.findElement(By.id("p"));
		password.clear();
		password.sendKeys(task.getPassword());
		
		WebElement loginButton = driver.findElement(By.id("login_button"));
		loginButton.click();
		System.out.println("333");
		Set<Cookie> cookies = driver.manage().getCookies();
		
		String skeyCookie = "";
		for (Cookie cookie : cookies) {
			
			skeyCookie += cookie.toString().substring(0,cookie.toString().indexOf(";")) + "; ";
		}
		System.out.println("444");
		String result = toGenTie(skeyCookie,task);
		
		if(result != null && "end".equals(result)){
			
			driver.quit();
		}
		}catch(Exception e){
			
			e.printStackTrace();
			logger.info("start firefox exception...");
		}
	}
	
	/**
	 * 跟帖
	 * @param skey 评论cookie
	 */ 
	public static String toGenTie(String skey,TaskGuideBean task) {
		System.out.println("444");
		try{
			String tkey = "";
			if(skey!=null && skey.indexOf("skey")>0){
			tkey = skey.substring(skey.indexOf("skey=")+5);
			tkey = tkey.substring(0,tkey.indexOf(";"));
			
			}else{
				
				isSuccess(task, "失败");
				logger.info("login fail...");
				return "end";
			}
		URL url = new URL("http://w.coral.qq.com/article/comment/");
		
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		
		String targetid = getTargetid(task.getAddress());
		
		String comment = URLDecoder.decode(task.getCorpus(),"gb2312");
		String param = "targetid="+targetid+"" +
				"&type=1" +
				"&format=SCRIPT" +
				"&callback=parent.topCallback" +
				"&content="+URLEncoder.encode(comment,"gb2312")+"" +
				"&_method=put" +
				"&g_tk="+GetG_TK(tkey)+"" +
				"&code=1" +
				"&source=1" +
				"&subsource=0" +
				"&picture=";

		openConnection.addRequestProperty("Host", "w.coral.qq.com");
		openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0");
		openConnection.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		openConnection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		openConnection.addRequestProperty("Referer", "http://www.qq.com/coral/coralBeta3/coralMainDom3.0.htm");
		openConnection.addRequestProperty("Cookie", skey);
		openConnection.addRequestProperty("Connection", "keep-alive");
		openConnection.addRequestProperty("Content-Length", String.valueOf(param.length()));
		
		openConnection.setDoInput(true);
		openConnection.setDoOutput(true);
		openConnection.setRequestMethod("POST");
		openConnection.setUseCaches(false);
		
		PrintWriter pw = new PrintWriter(openConnection.getOutputStream());
		pw.write(param);
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
			logger.info("has exception fail...");
			isSuccess(task, "失败");
			return "end";
		}
	}
	
	
	/**
	 * 获取targetid
	 * @param address 文章地址
	 * @return
	 */
	private static String getTargetid(String address) {
		
		try{
		WebClient webClient = new WebClient();

		webClient.getOptions().setUseInsecureSSL(true);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(
				false);
		webClient.getOptions().setTimeout(30000);

		HtmlPage htmlPage = webClient.getPage(address);

		String result = htmlPage.asXml();
		
		result = result.substring(result.indexOf("cmt_id"));
		String targetId = result.substring(result.indexOf("cmt_id")+6,result.indexOf(";")).replace("=", "").replaceAll("\"", "").trim();
		
		return targetId;
		}catch(Exception e){
			e.printStackTrace();
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
