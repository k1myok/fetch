package com.longriver.netpro.webview.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;


/**
 * 今日头条注册
 * @author rhy
 * @date 2018-3-5 上午9:56:57
 * @version V1.0
 */
public class ToutiaoRegister {
	
	public static void main(String[] args) {
		TaskGuideBean task = new TaskGuideBean();
		task.setNick("17080626097");
		task.setPassword("chwx123456&chwx6100523328");//&前面豆瓣账号,&后面微博账号
		task.setHostPort("1");
		
		toRegister(task);
	}

	/**
	 * 今日头条注册
	 * @param task
	 */
	public static void toRegister(TaskGuideBean task) {
	
		WebDriver driver = null;
		try {
			driver = getDriver();
			
			if(driver == null){
				
				isSuccess(task, "driver打开失败");
				return;
			}
			
			driver.get("https://sso.toutiao.com/");
			
			String[] password = task.getPassword().split("&");
			driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div/div/ul/li[2]")).click();
			
			Thread.sleep(1000);
			
			driver.findElement(By.id("userId")).sendKeys(task.getNick());
			
			driver.findElement(By.id("passwd")).sendKeys(password[1]);
			
			try {
				WebElement vcode = driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/span/img"));
				
				String picCode = getPicCode(driver, vcode);
				
				driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/input")).sendKeys(picCode);
				
				driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[2]/div/p/a[1]")).click();
				
				Thread.sleep(5000);
				
				try {
					if(StringUtils.isNotBlank(driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText())){
						
//						task.setCode(6);
						isSuccess(task, driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText());
						return;
					};
				} catch (Exception e) {
				}
			} catch (Exception e3) {
			}
			try {
				driver.findElement(By.className("WB_btn_login")).click();
				Thread.sleep(5000);
				
				try {
					if(StringUtils.isNotBlank(driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText())){
						
						isSuccess(task, driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText());
						return;
					}
				} catch (Exception e) {
				}
			} catch (Exception e3) {
			}
			
			try {
				WebElement vcode = driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/span/img"));
				
				String picCode = getPicCode(driver, vcode);
				
				driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/input")).sendKeys(picCode);
				
				driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[2]/div/p/a[1]")).click();
				
				Thread.sleep(5000);
				try {
					if(StringUtils.isNotBlank(driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText())){
						
//						task.setCode(6);
						isSuccess(task, driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText());
						return;
					};
				} catch (Exception e) {
				}
				
			} catch (Exception e2) {
			}
			
			
			try {
				String errorcontent = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[1]/dl/dt[1]")).getText();
				if(StringUtils.isNotBlank(errorcontent)){
					
//					task.setCode(1);
					isSuccess(task, errorcontent + driver.findElement(By.xpath("/html/body/div/div[2]/div/div[1]/dl/dt[3]")).getText());
					return;
				}
			} catch (Exception e1) {
			}
			
			
			if(driver.getCurrentUrl().contains("api.weibo.com/oauth2/authorize")){
				
				driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[2]/div/p/a[1]")).click();
				
				Thread.sleep(5000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	/**
	 * 获取验证码
	 * @param driver
	 * @throws IOException 
	 */
	private static String getPicCode(WebDriver driver,WebElement comment) throws IOException {
		
		File screenshotAs = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		BufferedImage bufferedImage = ImageIO.read(screenshotAs);
		
		Point point = comment.getLocation();
		
		int width = comment.getSize().getWidth();
		int height = comment.getSize().getHeight();
		if(width<=0) width=300;
		if(height<=0) height=100;
		BufferedImage subimage = bufferedImage.getSubimage(point.getX(), point.getY(), width, height);
		
		ImageIO.write(subimage, "png", screenshotAs);
		
		File file = new File("d:\\toutiao.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("3000", "d:\\toutiao.png");
		
		return code;
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
			
//		System.setProperty("phantomjs.binary.path", phantomjsUrl);
//		driver = new PhantomJSDriver();
			System.setProperty("webdriver.firefox.bin",firefoxUrl);
			driver = new FirefoxDriver();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return driver;
	}
	/**
	 * 判断是否成功
	 */
	public static void isSuccess(TaskGuideBean task,String msg){
			MQSender.toMQ(task,msg);
	}
	
}
