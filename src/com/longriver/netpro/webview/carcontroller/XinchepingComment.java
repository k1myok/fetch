package com.longriver.netpro.webview.carcontroller;

import java.net.URLDecoder;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 新车评网
 * @author rhy
 * @date 2018-3-28 下午5:47:01
 * @version V1.0
 */
public class XinchepingComment {
	
	public static void main(String[] args) {
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://www.xincheping.com/news/114472.html");
		task.setCorpus("相信技术的力量");
		task.setCorpus("太危险了这世界^^^");
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
		driver.get("http://www.xincheping.com/uc/login.html");
		
		driver.findElement(By.id("username")).sendKeys(task.getNick());
		driver.findElement(By.id("password")).sendKeys(task.getPassword());
		
		driver.findElement(By.id("loginSubmitBtn")).click();
		Thread.sleep(5000);
		

		String currentUrl = driver.getCurrentUrl();
		
		if(currentUrl.contains("login")){
			
			isSuccess(task, "登录失败");
			return;
		}
		
		driver.get(URLDecoder.decode(task.getAddress(),"utf-8"));
		
		driver.findElement(By.id("xcp_cmt_content")).sendKeys(task.getCorpus());
		
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id='cmtEntry']/div[1]/div[1]/p[5]/span/input")).click();
		
		Thread.sleep(1000);
		
		isSuccess(task, "");
		
		}catch(Exception e){
			
			isSuccess(task, "评论异常了");
			e.printStackTrace();
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
