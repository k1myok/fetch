package com.longriver.netpro.webview.controller;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.longriver.netpro.common.sina.WeiboLoginJietu;
import com.longriver.netpro.util.Configur;
import com.longriver.netpro.util.DriverGet;
import com.longriver.netpro.util.GetProprities;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 新浪微话题直发
 * @author lilei
 * @2015-5-18 下午3:10:04
 * @version v1.0
 */
public class WeiboWeiHuaTi {
	public static boolean process = false;
	public static boolean isSend = false;
	public static void main(String[] args) {
		process = true;
		TaskGuideBean task = new TaskGuideBean();
		task.setCorpus("过还是不过呢");
		task.setNick("lilei1929@163.com");
		task.setPassword("lilei419688..");
		task.setAddress("https://weibo.com/p/1008085fa17fa7ee856e9c0ef2014688ae103a?k=%E9%98%BF%E9%87%8C%E4%BA%91%E5%A4%A7%E5%AD%A6&from=501&_from_=huati_topic");
		task.setAddress("http://s.weibo.com/weibo/%23%E4%BD%A0%E8%A6%81%E8%BF%87%E5%85%AD%E4%B8%80%23");
		toComment(task);
	}

	/**
	 * 微话题直发
	 * @param task
	 */
	public static void toComment(TaskGuideBean task) {
		System.out.println("微话题");
		//保证只返回一次结果
		isSend = true;
		torun(task);
	}
	public static void torun(TaskGuideBean task) {
		WebDriver driver = null;
		try{
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
			String suc = WeiboLoginJietu.toLogin(driver, task, 0);
			if(!suc.equals("suc")){
				isSuccess(task, suc);
				return ;
			}
			String home = task.getAddress().replaceAll("#", "%23");
			driver.get(home);
			Thread.sleep(2000);
			WebElement textarea = null;								   
			try {
				textarea = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div[2]/div/div/div[1]/div[3]/div/div/div[2]/div[2]/div[1]/textarea"));
			} catch (Exception e1) {
				textarea = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/div[1]/div[2]/div/div/div/div/div[2]/div[2]/div[1]/textarea"));
			}
			textarea.click(); Thread.sleep(200);
			try {//发帖前需要关注
				WebElement guanzhu = driver.findElement(By.xpath("/html/body/div[13]/div[2]/div[4]/a[1]/span"));
				guanzhu.click();
				Thread.sleep(1000);
			} catch (Exception e) {
			}
			
			textarea.sendKeys(task.getCorpus());
			Thread.sleep(200);
			textarea.click(); Thread.sleep(200);
			WebElement commit = null;
			try {
				commit = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div[2]/div/div/div[1]/div[3]/div/div/div[2]/div[2]/div[2]/div[1]/a"));
			} catch (Exception e) {
				commit = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/div[1]/div[2]/div/div/div/div/div[2]/div[2]/div[2]/div[1]/a"));
			}
			Thread.sleep(200);
			commit.click();
			Thread.sleep(1000);
			String page = driver.getPageSource();
			if(page.contains("layer_152")){
				System.out.println("失败!");
				isSuccess(task, "弹窗失败");
			}
		}catch(Exception e){
			isSuccess(task, "报错失败");
			System.out.println("评论异常");
			e.printStackTrace();
		}finally{
			isSuccess(task, "");//成功
			DriverGet.quit(driver);
		}
		System.out.println("结束------------------");
		System.out.println("结束------------------");
	}

	/**
	 * 判断是否成功
	 */
	public static void isSuccess(TaskGuideBean task,String msg){
		if(isSend){
			isSend = false;
			MQSender.toMQ(task,msg);
		}
	}
	
}
