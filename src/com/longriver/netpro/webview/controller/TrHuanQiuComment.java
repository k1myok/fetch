package com.longriver.netpro.webview.controller;

import java.net.URLDecoder;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.longriver.netpro.util.DriverGet;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 环球旅游引导
 * @author rhy
 * @2017-11-22 下午3:46:37
 * @version v1.0
 */
public class TrHuanQiuComment {

	private static Logger logger = LoggerFactory.getLogger(TrHuanQiuComment.class);
	
	public static void main(String[] args) {
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://go.huanqiu.com/news/tourism/2017-11/11392529.html");
		task.setAddress("http://go.huanqiu.com/news/tourism/2017-11/11392652.html");
		task.setCorpus("我就看看...");
		task.setNick("15652240394");
		task.setPassword("lilei419688");
		
		toComment(task);
	}

	/**
	 * 调用firefoxDriver
	 * @param task
	 */
	public static void toComment(TaskGuideBean task) {
		
		WebDriver driver = null;
		try{
			
		String address = URLDecoder.decode(task.getAddress(),"utf-8");
		
		java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config\\properties\\paramsConfig");
		String firefoxUrl = bundle.getString("firefoxurl");
		//不加载图片
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("permissions.default.image", 2);
		//禁用css
//		profile.setPreference("permissions.default.stylesheet", 2);
		//##禁用Flash 
		profile.setPreference("dom.ipc.plugins.enabled.libflashplayer.so","false");
		
		System.setProperty("webdriver.firefox.bin",firefoxUrl);
		driver = new FirefoxDriver(profile); 
		driver.get("https://i.huanqiu.com/");  
		
		WebElement username = driver.findElement(By.xpath("/html/body/div[2]/div/div/form/span[2]/input"));
		username.clear();
		username.sendKeys(task.getNick());
		
		WebElement password = driver.findElement(By.xpath("/html/body/div[2]/div/div/form/span[3]/input"));
		password.clear();
		password.sendKeys(task.getPassword());
		
		WebElement loginButton = driver.findElement(By.xpath("/html/body/div[2]/div/div/form/span[5]/input"));
		loginButton.click();
		Thread.sleep(3000);
		String url = driver.getCurrentUrl();
		if(url.contains("i.huanqiu.com/login")){
			System.out.println("登录失败!");
			isSuccess(task,"登录失败");
			DriverGet.quit(driver);
			return ;
		}
//		Set cookie = driver.manage().getCookies();
//		System.out.println("url="+url);
//		System.out.println("cookie="+cookie.toString());
		driver.get(address);
		
		WebElement contentText = driver.findElement(By.id("tieTextareaMax"));
		contentText.clear();
		contentText.sendKeys(task.getCorpus());
		
		WebElement contentButton = driver.findElement(By.xpath("//*[@id='huanqiu-tiePost']/div[2]/div[2]/div[2]/a"));
		contentButton.click();
		
		isSuccess(task, "");
		
		}catch(Exception e){
			
			e.printStackTrace();
			logger.info("start firefox exception...");
		}finally{
			DriverGet.quit(driver);
		}
	}
	
	/**
	 * 判断是否成功
	 */
	public static void isSuccess(TaskGuideBean task,String msg){
		MQSender.toMQ(task,msg);
	}
	
}
