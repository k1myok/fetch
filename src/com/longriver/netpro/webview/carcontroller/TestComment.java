package com.longriver.netpro.webview.carcontroller;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
/**
 * https://www.zhihu.com/question/26702261
 * @author rhy
 * @date 2018-4-27 上午11:17:03
 * @version V1.0
 */
public class TestComment {
	
	public static void main(String[] args) {
		
		toComment();
	}
	private static void toComment() {
		// TODO Auto-generated method stub
		WebDriver driver = getDriver();
		
//		driver.get("https://sso.toutiao.com/login/");//今日头条
		driver.get("https://mp.yidianzixun.com/");//一点资讯
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
}
