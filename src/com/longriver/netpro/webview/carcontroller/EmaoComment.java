package com.longriver.netpro.webview.carcontroller;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.longriver.netpro.util.Configur;
import com.longriver.netpro.util.FileUtil;
import com.longriver.netpro.util.GetProprities;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.TaskGuideBean;
/**
 * 一貓
 * @author wyanegao
 *
 */
public class EmaoComment {

	public static boolean process = false;
	public static boolean isSend = false;
	public static void main(String[] args) {
		process = true;
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://news.emao.com/news/201803/27646.html");
		task.setCorpus("哈弗H4更为全面的配置,挺好的");
		task.setNick("17173425387");
		task.setPassword("chwx1234");
		toComment(task);
	}
	/**
	 * 评论
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
		if (task.getCorpus().length() < 10 || task.getCorpus().length() > 300) {
			isSuccess(task, "評論內容需要10 - 100");
			return;
		}
		WebDriver driver = null;
						String address = task.getAddress();
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
								driver.get("http://passport.emao.com/login");
								try {

									WebElement name = driver.findElement(By.id("mobile")); 
									name.clear(); 
									name.click();
									name.sendKeys(task.getNick());
									Thread.sleep(1000 * 2);
									WebElement passWord = driver.findElement(By.id("pwd"));
									passWord.clear();
									passWord.click();
									passWord.sendKeys(task.getPassword());
									
									Thread.sleep(1000 * 2);
									try {
										WebElement pwdError = driver.findElement(By.id("pwdError"));
										if (pwdError.getText().contains("您输入的密码有误")) {
											isSuccess(task,"密码有误");
											return;
										}
										
									} catch (Exception e1) {
										
									}
									Thread.sleep(1000 * 2);
									WebElement dologin = driver.findElement(By.id("loginButton"));
									dologin.click();
									try {
										WebElement mobileError = driver.findElement(By.id("mobileError"));
										if (mobileError.getText().contains("您输入的用户名有误")) {
											isSuccess(task,"您输入的用户名有误");
											return;
										}
										WebElement pwdError = driver.findElement(By.id("pwdError"));
										if (pwdError.getText().contains("您输入的密码有误")) {
											isSuccess(task,"密码有误");
											return;
										}
										
									} catch (Exception e1) {
										
									}
									Thread.sleep(1000 * 2);
									driver.get(task.getAddress());
									WebElement content = driver.findElement(By.id("comment_textarea_0"));
									content.clear();
									content.click();
									content.sendKeys(task.getCorpus());
									Thread.sleep(1000 * 2);
									WebElement commit = driver.findElement(By.xpath("//*[@id='comment_place']/div/dl/dd/div[3]/button"));
									commit.click();
									
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