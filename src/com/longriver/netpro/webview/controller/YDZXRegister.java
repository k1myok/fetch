package com.longriver.netpro.webview.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.longriver.netpro.fetchScript.util.Jdbc2MysqlSpcard;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.MsgUtil;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 一点资讯注册
 * @author rhy
 * @date 2018-3-6 下午3:23:24
 * @version V1.0
 */
public class YDZXRegister {

	private static int codetimes = 0;
	public static void main(String[] args) {
		TaskGuideBean task = new TaskGuideBean();
		task.setNick("17173425505");
		task.setPassword("chwx123456&chwx123456");////&前面豆瓣账号，&后面微博账号
		task.setAddress("http://www.yidianzixun.com/article/0GS9yJvG");
		task.setHostPort("5001");
		task.setCorpus("心更难咋样");
		
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
			
			driver.get("https://mp.yidianzixun.com/");
			
			driver.findElement(By.xpath("//*[@id='app']/div[2]/div[1]/div/div[2]/form/a[1]")).click();
			
//			Thread.sleep(2000);
			
			driver.findElement(By.xpath("//*[@id='app']/div[2]/div[1]/div/div[3]/div/ul/li[3]/a")).click();
			
//			Thread.sleep(2000);
			String[] password = task.getPassword().split("&");
//			driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div/div/ul/li[2]")).click();
			
//			Thread.sleep(1000);
			
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
						
						if(codetimes<3){
						if(driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText().contains("输入的验证码不正确")){
							
							codetimes++;
							driver.quit();
							toRegister(task);
						}
						}
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
						
						if(codetimes<3){
						if(driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText().contains("输入的验证码不正确")){
							
							driver.quit();
							toRegister(task);
						}
						}
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
				
//				Thread.sleep(5000);
				try {
					if(StringUtils.isNotBlank(driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText())){
						
//						task.setCode(6);
						if(codetimes<3){
						if(driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText().contains("输入的验证码不正确")){
							
							driver.quit();
							toRegister(task);
						}
						}
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
				
				Thread.sleep(2000);
			}
			
			driver.get("http://www.yidianzixun.com/profile/edit");
			

			try {
				String phoneText = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/div/div[4]/div")).getText();
				if(StringUtils.isNotBlank(phoneText) && phoneText.substring(3,7).equals("****")){
					
					isSuccess(task, "");
					return;
				}
			} catch (Exception e2) {
			}
			driver.findElement(By.name("phone")).sendKeys(task.getNick());
			WebElement imgyzm = driver.findElement(By.className("img-yzm"));
			
			String picYidian = getPicYidian(driver,imgyzm);
			driver.findElement(By.name("image-verification-code")).sendKeys(picYidian);
			Thread.sleep(3000);
			driver.findElement(By.className("btn-get-verification-code")).click();
			
			String port = "";
			if(task.getHostPort().length()==4){
				port = task.getHostPort().substring(0, 1);
			}else{
				port = task.getHostPort().substring(0, 2);
			}
			try {
				if(StringUtils.isNotBlank(driver.findElement(By.className("error-msg-inner")).getText())){
					
					if(codetimes<3){
					if(driver.findElement(By.className("error-msg-inner")).getText().contains("请输入正确的图片验证码")){
						
						driver.quit();
						toRegister(task);
					}
					}
					isSuccess(task, driver.findElement(By.className("error-msg-inner")).getText());
					return;
				}
			} catch (Exception e1) {
			}
			int switchCard = MsgUtil.switchCard(task.getIsApp(),task.getHostPort().substring(2));
			
			if(switchCard!=1){
				
				isSuccess(task, "卡池换卡失败");
				return;
			}
			Thread.sleep(35000);
			String msg = Jdbc2MysqlSpcard.getResultHis(port);
			
			String phoneCode = getPhoneCode(msg);
			
			if(StringUtils.isBlank(phoneCode)){
				
				Thread.sleep(30000);
				driver.findElement(By.className("btn-get-verification-code")).click();
				Thread.sleep(20000);
				msg = Jdbc2MysqlSpcard.getResultHis(port);
				
				phoneCode = getPhoneCode(msg);
				
				if(StringUtils.isBlank(phoneCode)){
					isSuccess(task, "没有收到短信验证码");
				
					driver.quit();
					return;
				}
				
			}
			driver.findElement(By.name("phone-verification-code")).sendKeys(phoneCode);
			
			driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/div/div[4]/form/div[3]/div/p[3]/button")).click();
			
			Thread.sleep(2000);
			try {
				if(StringUtils.isNotBlank(driver.findElement(By.className("error-msg-inner")).getText())){
					
					isSuccess(task, driver.findElement(By.className("error-msg-inner")).getText());
					return;
				}
			} catch (Exception e1) {
			}
			
			String text;
			try {
				text = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/div/div[4]/form/div/span")).getText();
				
				if(StringUtils.isBlank(text)){
					
					isSuccess(task, "手机验证码错误");
					return;
				}else{
					
					isSuccess(task, "");
					return;
				}
			} catch (Exception e) {
				
				isSuccess(task, "手机验证码错误");
			}
		} catch (Exception e) {
			
			isSuccess(task, "注册异常。。。");
			e.printStackTrace();
		}finally{
			
			driver.quit();
		}
		
		
	}
	/**
	 * 解析短信
	 * @param msg
	 * @return
	 */
	private static String getPhoneCode(String msg) {
		
		String phoneCode = "";
		if(StringUtils.isNotBlank(msg)){
			
			phoneCode = msg.substring(msg.indexOf("验证码")+4,msg.indexOf("请在30分钟")).trim();
		}
		
		return phoneCode;
	}
	
	/**
	 * 获取验证码
	 * @param driver
	 * @throws IOException 
	 */
	private static String getPicYidian(WebDriver driver,WebElement comment) throws IOException {
		
		File screenshotAs = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		BufferedImage bufferedImage = ImageIO.read(screenshotAs);
		
		Point point = comment.getLocation();
		
		int width = comment.getSize().getWidth();
		int height = comment.getSize().getHeight();
		if(width<=0) width=300;
		if(height<=0) height=100;
		BufferedImage subimage = bufferedImage.getSubimage(point.getX(), point.getY(), width, height);
		
		ImageIO.write(subimage, "png", screenshotAs);
		
		File file = new File("d:\\yidian.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("3040", "d:\\yidian.png");
		
		return code;
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
		
		File file = new File("d:\\weibo.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("3050", "d:\\weibo.png");
		
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
			
			//System.setProperty("phantomjs.binary.path", phantomjsUrl);
			//driver = new PhantomJSDriver();
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
	
	@Test
	public void getParam(){
		String ss = "170****6142";
		String substring = ss.substring(3,7);
		
		System.out.println(substring.equals("****"));
		System.out.println(substring);
	}
}
