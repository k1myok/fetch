package com.longriver.netpro.util;

import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

public class DriverGet {
	private static WebDriver driver = null;
	
	private DriverGet(){}
	
	public static void main(String[] args) {
		
	}
	public static WebDriver getDriver(){
		System.out.println(driver);
		if(driver==null || driver.toString().contains("null")){
			produceDriver();
		}
		return driver;
	}
	public static void produceDriver(){
		Configur config = GetProprities.paramsConfig;
		String firefoxUrl = config.getProperty("firefoxurl");
		
		FirefoxProfile profile = new FirefoxProfile();
		//禁用css
//		profile.setPreference("permissions.default.stylesheet", 2);
		//不加载图片
//		profile.setPreference("permissions.default.image", 2);
		//##禁用Flash 
		profile.setPreference("dom.ipc.plugins.enabled.libflashplayer.so","false");
		
		System.setProperty("webdriver.firefox.bin",firefoxUrl);
		driver = new FirefoxDriver(profile); 
	}
	public static void quit(WebDriver driver){
		try {
			System.out.println("关闭driver");
			if(driver != null){
				driver.quit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
