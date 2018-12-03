package com.longriver.netpro.webview.carcontroller;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.longriver.netpro.util.Configur;
import com.longriver.netpro.util.GetProprities;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.TaskGuideBean;

public class ICarComment {
	public static boolean process = false;
	public static boolean isSend = false;
	public static void main(String[] args) {
		process = true;
		TaskGuideBean task = new TaskGuideBean();
		task.setNick("17173425505");
		task.setPassword("chwx1234");
		task.setCorpus("可以很酷");
		task.setAddress("http://info.xcar.com.cn/201803/news_2002383_1.html");
		torun(task);
	}
	public static void torun(TaskGuideBean task){
		 toBackrun(task);
	}
	public static void toBackrun(TaskGuideBean task){
		isSend = true;
		toComment(task);
	}  
	public static void toComment(TaskGuideBean task) {
		WebDriver driver = null;
		String content = "";
		String vcode = null;
		task.setCode(10);
		
		Configur config = GetProprities.paramsConfig;
		String firefoxUrl = config.getProperty("firefoxurl");
		
		FirefoxProfile profile = new FirefoxProfile();
		System.setProperty("webdriver.firefox.bin",firefoxUrl);
		driver = new FirefoxDriver(profile); 
		//禁用css
		//		profile.setPreference("permissions.default.stylesheet", 2);
				//不加载图片
		//		profile.setPreference("permissions.default.image", 2);
				//##禁用Flash 
				profile.setPreference("dom.ipc.plugins.enabled.libflashplayer.so","false");
				
				System.setProperty("webdriver.firefox.bin",firefoxUrl);
		driver.get("http://my.xcar.com.cn/logging.php?action=login&referer=%2F"); 
		try {
			WebElement goLogin = driver.findElement(By.id("namelogin"));
			goLogin.click();
			Thread.sleep(1000 * 2);
			WebElement name = driver.findElement(By.id("username")); 
			name.clear(); 
			name.click();
			name.sendKeys(task.getNick());
			Thread.sleep(1000 * 2);
			WebElement passWord = driver.findElement(By.id("userpwd"));
			passWord.clear();
			passWord.click();
			passWord.sendKeys(task.getPassword());
			
			Thread.sleep(1000 * 2);
			WebElement dologin = driver.findElement(By.id("dologin"));
			dologin.click();
			try {
				WebElement errormsg = driver.findElement(By.id("errormsg_2_text"));
				isSuccess(task, errormsg.getText());
				return;
			} catch (Exception e) {
				
			}
			Thread.sleep(1000 * 2);
			driver.get(task.getAddress());
			Thread.sleep(1000 * 2);
			WebElement click = driver.findElement(By.id("commentnumtwo"));
			click.click();
			Thread.sleep(1000 * 2);
			driver.switchTo().frame("comment");
			WebElement comContent = driver.findElement(By.xpath("//*[@id='feedbackForm']/div/div[1]/div[2]/textarea"));
			comContent.clear();
			comContent.click();
			comContent.sendKeys(task.getCorpus());
			Thread.sleep(1000 * 2);
			WebElement commit = driver.findElement(By.xpath("//*[@id='feedbackForm']/div/div[2]/button"));
			commit.click();
			
			isSuccess(task, "");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block 
			e.printStackTrace();
			isSuccess(task, "评论异常");
		}finally{
			
			driver.quit();
		}
	}
	/**
	 * 判断是否成功 
	 */
	public static void isSuccess(TaskGuideBean task,String msg){
		
		MQSender.toMQ(task,msg);
	}
}
