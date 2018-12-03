package com.longriver.netpro.webview.carcontroller;

import java.net.URLDecoder;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 环球评论
 * @author rhy
 * @date 2018-3-20 上午10:47:12
 * @version V1.0
 */
public class HuanQiuComment {
	
	public static void main(String[] args) {
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://mil.huanqiu.com/world/2018-03/11677809.html");
		task.setCorpus("这个厉害了");
		task.setNick("17080621284");
		task.setPassword("chwx123456");
		toComment(task);
	}
	
	/**
	 * 评论
	 * @param task
	 */
	public static void toComment(TaskGuideBean task) {
		
		WebDriver driver = null;
		try{
		driver = getDriver();
		
		if(driver == null){
			isSuccess(task, "driver打开失败");
			return;
		}
		driver.get("https://i.huanqiu.com/");
		
		driver.findElement(By.name("email")).sendKeys(task.getNick());
		driver.findElement(By.name("password")).sendKeys(task.getPassword());
		
		driver.findElement(By.name("submit")).click();
		
		try {
			String errorMsg = driver.findElement(By.xpath("/html/body/div[2]/div/div/form/em")).getText();
			
			if(StringUtils.isNotBlank(errorMsg)){
				isSuccess(task, errorMsg);
				return;
			}
		} catch (Exception e) {
		}
		
		driver.get(URLDecoder.decode(task.getAddress(),"utf-8"));
		
		driver.findElement(By.id("tieTextareaMax")).sendKeys(task.getCorpus());
		
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id='huanqiu-tiePost']/div[2]/div[2]/div[2]/a")).click();
		
		Thread.sleep(1000);
		
		isSuccess(task, "");
		
		}catch(Exception e){
			
			isSuccess(task, "评论异常了");
		}finally{
			
			driver.quit();
		}
		
	}
	/**
	 * 获取driver
	 * @return
	 */
	public static WebDriver getDriver(){
		
		WebDriver driver = null;
		try {
			java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config\\properties\\paramsConfig");
			String firefoxUrl = bundle.getString("firefoxurl");
			String phantomjsUrl = bundle.getString("phantomjsurl");
			
			FirefoxProfile profile = new FirefoxProfile();
			//禁用css
			//profile.setPreference("permissions.default.stylesheet", 2);
			//不加载图片
			//profile.setPreference("permissions.default.image", 2);
			//##禁用Flash 
			profile.setPreference("dom.ipc.plugins.enabled.libflashplayer.so","false");
			
			//	System.setProperty("phantomjs.binary.path", phantomjsUrl);
			//	driver = new PhantomJSDriver();
			System.setProperty("webdriver.firefox.bin",firefoxUrl);
			driver = new FirefoxDriver();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return driver;
	}
		/**
		 * 判断是否成功
		 */
		public static void isSuccess(TaskGuideBean task,String msg){
				MQSender.toMQ(task,msg);
		}
}