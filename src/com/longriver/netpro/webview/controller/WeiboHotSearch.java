package com.longriver.netpro.webview.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.longriver.netpro.common.sina.WeiboLoginJietu;
import com.longriver.netpro.util.Configur;
import com.longriver.netpro.util.DriverGet;
import com.longriver.netpro.util.FileUtil;
import com.longriver.netpro.util.GetProprities;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 微博热搜
 * @author lilei
 * @2018-1-20 下午3:10:04
 * @version v1.0
 */
public class WeiboHotSearch {
	public static boolean process = false;
	public static boolean isSend = false;
	public static void main(String[] args) {
		process = true;
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("https://weibo.com/2243807243/FFeFicaYx?ref=home&rid=8_0_8_3080945990415973881");
		task.setCorpus("美国联邦政府20日起正式");
		task.setAddress("https://weibo.com/2891173407/FF4n4A2Rc?refer_flag=1001030103_");
		task.setCorpus("比亚迪");
		task.setNick("lilei1929@163.com");
		task.setPassword("lilei419688..");
		toComment(task);
	}

	/**
	 * 新浪新闻评论
	 * @param task
	 */
	public static void toComment(TaskGuideBean task) {
		System.out.println("toComment");
		//先清空存放截图的文件夹
		FileUtil.deleteDirectoryFiles();
		//保证只返回一次结果
		isSend = true;
		torun(task,1);
	}
	public static void torun(TaskGuideBean task,int number) {
		WebDriver driver = null;
		String address = task.getAddress();
		if(address.contains("?")){
			address = address.substring(0, address.indexOf("?"));
		}
		address = address.replaceAll("https://weibo.com", "");
		try{
	//		java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config\\properties\\paramsConfig");
	//		String firefoxUrl = bundle.getString("firefoxurl");
	//		String phantomjsUrl = bundle.getString("phantomjsurl");
			Configur config = GetProprities.paramsConfig;
			String firefoxUrl = config.getProperty("firefoxurl");
	//		String phantomjsUrl = config.getProperty("phantomjsurl");
		
			FirefoxProfile profile = new FirefoxProfile();
			//禁用css
	//		profile.setPreference("permissions.default.stylesheet", 2);
			//不加载图片
//			profile.setPreference("permissions.default.image", 2);
			//##禁用Flash 
			profile.setPreference("dom.ipc.plugins.enabled.libflashplayer.so","false");
		
			System.setProperty("webdriver.firefox.bin",firefoxUrl);
			driver = new FirefoxDriver(profile); 
//			String suc = WeiboLoginJietu.toLogin(driver, task, 0);
//			if(!suc.equals("suc")){
//				isSuccess(task, suc);
//				return ;
//			}
			Thread.sleep(2000);
			//设置超时时间为60S
            driver.manage().timeouts().pageLoadTimeout(60,TimeUnit.SECONDS);
            driver.manage().window().maximize();
			driver.get("http://s.weibo.com/?topnav=1&wvr=6");
			
//				WebElement wenzhangBtn = driver.findElement(By.xpath("//*[@id='pl_searchHead']/div[1]/ul/li/a[4]"));
//				wenzhangBtn.click();
			
			WebElement corpus = driver.findElement(By.xpath("//*[@id='pl_searchHead']/div[1]/div/div/div[2]/div/input"));
			corpus.clear();
			corpus.sendKeys(task.getCorpus());
			
			WebElement sBtn = driver.findElement(By.xpath("//*[@id='pl_searchHead']/div[1]/div/div/div[1]/a"));
			sBtn.click();
			
			Thread.sleep(1000*3);
//			try {
//				String ls = "//a[contains(@href,'"+address+"')]";
//				System.out.println(ls);
//				List<WebElement> aboutLinks = driver.findElements(By.xpath(ls));
//				if(aboutLinks.size()>0){
//					aboutLinks.get(0).click();
//					System.out.println("点击成功");
//					Thread.sleep(1000*3);
//				}else{
//					task.setCode(10);
//					isSuccess(task, "登录超时");
//				}
//			} catch (Exception e) {
//				task.setCode(10);
//				isSuccess(task, "登录超时");
//				e.printStackTrace();
//			}
		}catch(Exception e){
			task.setCode(100);
			isSuccess(task, "");
			e.printStackTrace();
		}finally{
			task.setCode(100);
			isSuccess(task, "");
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
