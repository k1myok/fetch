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

/**
 * 315汽车消费网
 * @author wyanegao
 *
 */
public class CheXiaofeiComment {
	public static boolean process = false;
	public static boolean isSend = false;
	public static void main(String[] args) {
		process = true;
		TaskGuideBean task = new TaskGuideBean();
		task.setNick("17173425504@sina.cn");
		task.setPassword("chwx1234");
		task.setAddress("http://inf.315che.com/n/2018_04/997269/");
		task.setCorpus("基本都会影响的");
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
		//禁用css
		//		profile.setPreference("permissions.default.stylesheet", 2);
				//不加载图片
		//		profile.setPreference("permissions.default.image", 2);
				//##禁用Flash  
				profile.setPreference("dom.ipc.plugins.enabled.libflashplayer.so","false");
		System.setProperty("webdriver.firefox.bin",firefoxUrl); 
		driver = new FirefoxDriver(profile); 
		driver.get(task.getAddress()); 
		
		try {
			WebElement concent = driver.findElement(By.id("commentcontent"));
			concent.clear();
			concent.click();
			concent.sendKeys(task.getCorpus());
			Thread.sleep(1000 * 2);
			WebElement commit = driver.findElement(By.xpath("/html/body/div[4]/div[1]/div[6]/div[2]/div[2]/a"));
			commit.click();
			
			try {
				if (!driver.switchTo().alert().getText().isEmpty()) {
					isSuccess(task, driver.switchTo().alert().getText());
					return;
				}
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
			isSuccess(task, "");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			isSuccess(task, "评论异常");
			e.printStackTrace();
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