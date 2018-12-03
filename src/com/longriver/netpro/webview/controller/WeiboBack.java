package com.longriver.netpro.webview.controller;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 微博账号找回
 * @author rhy
 * @date 2018-7-9 下午3:09:57
 * @version V1.0
 */
public class WeiboBack {

	public static void main(String[] args) {
		TaskGuideBean task = new TaskGuideBean();
		task.setNick("17173425386");
		task.setPassword("chwx6854068850");
		task.setHostPort("10005");
		task.setNick("17194513452");
		task.setPassword("chwx1817790093");
		
		task.setNick("17194510840");
		task.setPassword("chwx2862807");
		task.setHostPort("6001");
		task.setIsApp(5);
		toComment(task);
	}
	
	public static void toComment(TaskGuideBean task){
		
		
		
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
