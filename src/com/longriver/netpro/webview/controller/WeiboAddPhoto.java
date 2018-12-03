package com.longriver.netpro.webview.controller;

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
 * sina微博添加头像
 * @author lilei
 * @2014-4-10 下午16:10:04
 * @version v1.0
 */
public class WeiboAddPhoto {
	public static boolean process = false;
	public static boolean isSend = false;
	public static void main(String[] args) {
		process = true;
		TaskGuideBean task = new TaskGuideBean();
//		task.setNick("15558480767");
//		task.setPassword("a123456a");
		task.setNick("17194510856");
		task.setPassword("chwx123456");
		toComment(task);
	}

	/**
	 * 新浪新闻评论
	 * @param task
	 */
	public static void toComment(TaskGuideBean task) {
		isSend = true;
		String url = GetProprities.PaginationConfig.getProperty("touxiangUri")+task.getCorpus();
		String dir = "C:\\jietu\\touxiang.jpg";
		FileUtil.dirExists("C:\\jietu");
		boolean issuc = FileUtil.downloadPicture(url,dir);
		if(issuc){
			System.out.println("头像下载成功");
			torun(task);
		}
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
				task.setCode(10);
				isSuccess(task, suc);
				return ;
			}
			String address = "https://account.weibo.com/set/photo";
			driver.get(address);
			//低版本需注释
			try {
				WebElement element0 = driver.findElement(By.linkText("普通方式上传"));
				element0.click();  
				Thread.sleep(1000);
			} catch (Exception e) {
				System.out.println("切换为普通模式");
			}
			
			WebElement element1 = driver.findElement(By.xpath("//*[@id='pl_headimage_headimage']/div[2]/div/div[1]/div/form/div/div[2]/a/q/input"));
			element1.sendKeys("C:\\jietu\\touxiang.jpg");
			Thread.sleep(1000);
			WebElement element2 = driver.findElement(By.xpath("//*[@id='pl_headimage_headimage']/div[2]/div/div[1]/div/form/div/div[3]/a/span"));
			element2.click();
			Thread.sleep(5000);
		}catch(Exception e){
			task.setCode(10);
			isSuccess(task, "报错失败");
			System.out.println("评论异常");
			e.printStackTrace();
		}finally{
			task.setCode(100);
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
