package com.longriver.netpro.webview.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.util.DriverGet;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.PngErjinzhi;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 凤凰视频引导
 * @author rhy
 * @2018-1-5 下午3:03:58
 * @version v1.0
 */
public class IFengVideoCommentJietu {

	private static Logger logger = Logger.getLogger(IFengVideoCommentJietu.class);
	
	private static Integer times = 1;
	public static void main(String[] args) {
		
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://v.ifeng.com/video_8487472.shtml");
//		task.setAddress("http://v.ifeng.com/video_8184468.shtml");
		task.setAddress("http://v.ifeng.com/video_10924435.shtml");
		task.setCorpus("我去看看，ya ");
		task.setCorpus("终极可以吗");
//		task.setCorpus("hai可以吧");
		task.setNick("13611313453");
		task.setPassword("hailele333");
//		task.setNick("只看热闹不买");
//		task.setPassword("jixnyu19700");
		toComment(task);
	}

	/**
	 * 凤凰登录
	 * @param task
	 */
	public static void toComment(TaskGuideBean task) {
		WebDriver driver = null;
		try{

		String address = URLDecoder.decode(task.getAddress(), "utf-8");
		java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config\\properties\\paramsConfig");
		String firefoxUrl = bundle.getString("firefoxurl");
		
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("dom.ipc.plugins.enabled.libflashplayer.so","false");
		System.setProperty("webdriver.firefox.bin",firefoxUrl);
		driver = new FirefoxDriver(profile); 
		driver.get("https://id.ifeng.com/user/login");
		
		Thread.sleep(3000);
		driver.findElement(By.id("userLogin_name")).sendKeys(task.getNick());
		driver.findElement(By.id("userLogin_pwd")).sendKeys(task.getPassword());
		WebElement imgCode = driver.findElement(By.id("code_img"));
		
		String code = getPic(driver, imgCode);
		driver.findElement(By.id("userLogin_securityCode")).clear();;
		driver.findElement(By.id("userLogin_securityCode")).sendKeys(code);
		Thread.sleep(1000);
		driver.findElement(By.id("userLogin_btn")).click();
		
		try{
			String text = driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/div/div[1]/div[6]")).getText();
			
			if(StringUtils.isNotBlank(text)){
				if(text.contains("验证码") && times<=3){
					
					times++;
					DriverGet.quit(driver);
					toComment(task);
					return ;
				}else{
					
					isSuccess(task, text);
					return;
				}
			}
		}catch(Exception e){}
		
		Thread.sleep(3000);
		if(driver.getCurrentUrl().contains("user/login")){
			isSuccess(task, "登录失败");
			return;
		}
		
		driver.get(address);
		
//		Thread.sleep(7000);
		((JavascriptExecutor)driver).executeScript("var q=document.documentElement.scrollTop=500");   
		driver.findElement(By.id("comment_input")).sendKeys(task.getCorpus());
		driver.findElement(By.xpath("//*[@id='js_com_btnico']/a")).click();
		Thread.sleep(3000);
		
		try {
			String alertText = driver.switchTo().alert().getText();
			
			if(StringUtils.isNotBlank(alertText)){
				
				isSuccess(task, "请绑定手机");
				return;
			}
		} catch (Exception e1) {
		}
		WebElement content = driver.findElement(By.xpath("//*[@id='vcom_list_ul']/li[1]"));
		
		//根据语料 判断是否成功
		System.out.println("content.getText()=="+content.getText());
		if(content.getText().contains(task.getCorpus())){
			System.out.println("截图发帖成功!");
			try {
				
				String picUri = getPicContent(driver,content);
				task.setPng(PngErjinzhi.getImageBinary(picUri));
				isSuccess(task, "");
			} catch (Exception e) {
				System.out.println("发帖成功截图失败!");
			}
		}else{
			System.out.println("截图发帖失败!");
			isSuccess(task, "匹配文字失败");
		}
		
		}catch(Exception e){
			
			isSuccess(task, "评论错误");
			e.printStackTrace();
		}finally{
			
			if(driver != null){
			driver.quit();
			}
		}
	}
	/**
	 * 获取验证码
	 * @param driver
	 */
	private static String getPic(WebDriver driver,WebElement comment) {
		
		try{
		File screenshotAs = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		BufferedImage bufferedImage = ImageIO.read(screenshotAs);
		
		Point point = comment.getLocation();
		
		int width = comment.getSize().getWidth();
		int height = comment.getSize().getHeight();
		
		BufferedImage subimage = bufferedImage.getSubimage(point.getX(), point.getY(), width, height);
		
		ImageIO.write(subimage, "png", screenshotAs);
		
//		String picName = getPicName();
		File file = new File("d:\\ifeng.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("3040", "d:\\ifeng.png");
		
		return code;
		}catch(Exception e){
			
			logger.info("获取图片异常"+e);
		}
		return null;
	}

	/**
	 * 获取评论图片
	 * @param driver
	 * @throws IOException 
	 */
	private static String getPicContent(WebDriver driver,WebElement comment) throws IOException {
		File screenshotAs = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		
		BufferedImage bufferedImage = ImageIO.read(screenshotAs);
		
		Point point = comment.getLocation();
		
		int width = comment.getSize().getWidth();
		int height = comment.getSize().getHeight();
		
		BufferedImage subimage = bufferedImage.getSubimage(point.getX(), point.getY(), width, height);
		
		ImageIO.write(subimage, "png", screenshotAs);
		
		String picUri = getPicName();
		File file = new File(picUri);
		FileUtils.copyFile(screenshotAs, file);
		
		return picUri;
	}

	/**
	 * 图片名称
	 * @return
	 */
	private static String getPicName() {
		String picName = "d:\\jietu\\";
		File file =new File(picName);
		if(!file.exists() && !file.isDirectory()){
		    file.mkdirs();  
		}
		String picUri = "d:\\jietu\\ifeng_"+System.currentTimeMillis()+".png";
		return picUri;
	}	
	/**
	 * 判断是否成功
	 */
	public static void isSuccess(TaskGuideBean task,String msg){
		MQSender.toMQ(task,msg);
	}
}
