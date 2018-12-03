package com.longriver.netpro.webview.controller;

import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.alibaba.fastjson.JSON;
import com.longriver.netpro.util.DriverGet;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.CodeImgUtils;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 中国新闻网旅游引导
 * @author lilei
 * @2017-11-23
 * @version v1.0
 */
public class TrChinanewsComment {

	private static int insender = 0;//保证结果只返回一次
	
	public static void main(String[] args) {
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://finance.chinanews.com/life/2016/07-20/7945458.shtml");
		task.setAddress("http://finance.chinanews.com/life/2016/07-19/7943973.shtml");
		task.setCorpus("我就看看...");
		task.setNick("lilei1929@163.com");
		task.setPassword("lilei419688..");
		
		toComment(task);
	}

	/**
	 * 调用firefoxDriver
	 * @param task
	 */
	public static void toComment(TaskGuideBean task) {
		insender = 0;
		WebDriver driver = null;
		String mainHandle = null;
		try{
			
			String address = URLDecoder.decode(task.getAddress(),"utf-8");
			
			java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config\\properties\\paramsConfig");
			String firefoxUrl = bundle.getString("firefoxurl");
			//不加载图片
			FirefoxProfile profile = new FirefoxProfile();
//			profile.setPreference("permissions.default.image", 2);
			//禁用css
//			profile.setPreference("permissions.default.stylesheet", 2);
			//##禁用Flash 
			profile.setPreference("dom.ipc.plugins.enabled.libflashplayer.so","false");
			
			System.setProperty("webdriver.firefox.bin",firefoxUrl);
			driver = new FirefoxDriver(profile); 
			driver.get(address);  
			mainHandle = driver.getWindowHandle();
			
			WebElement loginButton = driver.findElement(By.className("header-login"));
			loginButton.click();
			Thread.sleep(1000);
			WebElement loginWeibo = driver.findElement(By.className("login-group"));
			WebElement l2 = loginWeibo.findElement(By.xpath("li[1]/span"));
			l2.click();
			Thread.sleep(1000);
			// 单击iTask之后，会打开一个新的窗口，获取所有窗口的句柄  
			Set<String> allHandles = driver.getWindowHandles();  
			// 对获取的所有句柄进行循环判断，把当前句柄从所有句柄中移除，剩下的就是想要获得的新窗口的句柄  
			Iterator<String> iter = allHandles.iterator();  
			String handle = null;  
			WebDriver driverNew = null;  
			while(iter.hasNext()) {  
			    handle = iter.next();  
			    driverNew = driver.switchTo().window(handle);
			}
			Thread.sleep(1000);
			
			WebElement username = driverNew.findElement(By.id("userId"));
			username.clear();
			username.sendKeys(task.getNick());
			WebElement passwd = driverNew.findElement(By.id("passwd"));
			passwd.clear();
			passwd.sendKeys(task.getPassword());
			WebElement llweibo = driverNew.findElement(By.xpath("/html/body/div[1]/div/div[2]/form/div/div[2]/div/p/a[1]"));
			WebElement codeShow = driverNew.findElement(By.className("oauth_code"));
			llweibo.click();
			Thread.sleep(1000);
			//出现验证码
			if(codeShow.isDisplayed()){
				WebElement imgcode = driverNew.findElement(By.className("code_img"));
				String code = CodeImgUtils.getCode(driverNew, imgcode, "chinanews.png", "3050");
				System.out.println("验证码:"+code);
				WebElement vcode = driverNew.findElement(By.xpath("/html/body/div[1]/div/div[2]/form/div/div[1]/div[1]/p[3]/input"));
				vcode.clear();
				vcode.sendKeys(code);
			}
			llweibo.click();
			Thread.sleep(1000);
			boolean success = false;
			//未成功输入第二次
			try {
				WebElement imgcode = driverNew.findElement(By.className("code_img"));
				String code = CodeImgUtils.getCode(driverNew, imgcode, "chinanews.png", "3050");
				System.out.println("验证码:"+code);
				WebElement vcode = driverNew.findElement(By.xpath("/html/body/div[1]/div/div[2]/form/div/div[1]/div[1]/p[3]/input"));
				vcode.clear();
				vcode.sendKeys(code);
			} catch (Exception e1) {
				success = true;//第一次验证码成功
			}
			if(!success){
				llweibo.click();
				try {
					driverNew.findElement(By.className("code_img"));
					driverNew.findElement(By.xpath("/html/body/div[1]/div/div[2]/form/div/div[1]/div[1]/p[3]/input"));
				} catch (Exception e) {
					success = true;//判断第二次
				}
			}
			if(!success){
				System.out.println("登录失败!");
				isSuccess(task,"登录失败");
				DriverGet.quit(driver);
			}else{
				driver.switchTo().window(mainHandle);
				Thread.sleep(1000);
				WebElement contentText = driver.findElement(By.xpath("/html/body/div[7]/div[1]/div[1]/div[2]/div[13]/div[2]/div/div/div[2]/div[3]/div/div[2]/div[1]/div/div[3]/div[2]/div/textarea"));
				contentText.clear();
				contentText.sendKeys(task.getCorpus());
				Thread.sleep(1000);
				WebElement submitBTN = driver.findElement(By.xpath("/html/body/div[7]/div[1]/div[1]/div[2]/div[13]/div[2]/div/div/div[2]/div[3]/div/div[2]/div[1]/div/div[4]/div[2]/div/a/button"));
				submitBTN.click();
				
				isSuccess(task, "");
				
				DriverGet.quit(driver);
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("start firefox exception...");
			isSuccess(task, "异常失败!");
			DriverGet.quit(driver);
		}
	}
	
	/**
	 * 判断是否成功
	 */
	public static void isSuccess(TaskGuideBean task,String msg){
		if(insender==0){
			MQSender.toMQ(task,msg);
		}
		insender = 1;
	}
	
}
