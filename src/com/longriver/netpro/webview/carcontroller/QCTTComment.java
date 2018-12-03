package com.longriver.netpro.webview.carcontroller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

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
 * 汽车头条
 * @author rhy
 * @date 2018-3-26 上午11:11:34
 * @version V1.0
 */
public class QCTTComment {
	private static int codetimes = 0;
	public static void main(String[] args) {
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("https://www.qctt.cn/news/264519");
		task.setCorpus("相信技术的力量");
//		task.setCorpus("太危险了这世界^^^");
		task.setNick("17080621284");
		task.setPassword("chwx123456");
		toComment(task);
	}
	
	/**
	 * 评论
	 * @param task
	 */
	public static void toComment(TaskGuideBean task) {
		
		WebDriver driver = null;
		try{
		driver = getDriver();
		
		if(driver == null){
			
			isSuccess(task, "driver打开失败");
			return;
		}
		driver.get("https://www.qctt.cn/login");
		
		driver.findElement(By.id("user_username")).sendKeys(task.getNick());
		driver.findElement(By.id("user_password")).sendKeys(task.getPassword());
		
		driver.findElement(By.className("user_login")).click();
		Thread.sleep(6000);
		
		
		try {
			
			try {
				String userPrompt = driver.findElement(By.className("user_prompt_text")).getText();
				
				if(StringUtils.isNotBlank(userPrompt)){
					
					isSuccess(task, userPrompt);
					return;
					}
			} catch (Exception e) {
			}
			
			String errorMsg = driver.findElement(By.className("password_prompt_text")).getText();
			
			if(StringUtils.isNotBlank(errorMsg)){
				
			isSuccess(task, errorMsg);
			return;
			}
		} catch (Exception e) {
		}
		
		driver.get(URLDecoder.decode(task.getAddress(),"utf-8"));
		
		driver.findElement(By.xpath("/html/body/div[3]/div[3]/div[1]/a/div/div[2]/textarea")).sendKeys(task.getCorpus());
		
		try {
			WebElement imgCode = driver.findElement(By.xpath("/html/body/div[3]/div[3]/div[1]/div[5]/div/span[1]/img"));
			String picCode = getPicCode(driver, imgCode);
			
			driver.findElement(By.className("test_input")).sendKeys(picCode);
			
			driver.findElement(By.xpath("/html/body/div[3]/div[3]/div[1]/div[5]/div/span[3]")).click();
			
			Thread.sleep(800);
			String confirm = driver.switchTo().alert().getText();
			System.out.println(confirm);
			
			if(StringUtils.isNotBlank(confirm) && confirm.contains("请输入正确的验证码")){
				
				if(codetimes <= 4){
					
					codetimes++;
					driver.quit();
					toComment(task);
				}else{
					
					isSuccess(task, confirm);
					return;
				}
			}else{
				
				isSuccess(task, "");
			}
			
		} catch (Exception e) {
		}
		
		
		}catch(Exception e){
			
			isSuccess(task, "评论异常了");
		}finally{
			
			driver.quit();
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
		
		File file = new File("d:\\qctt.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("3040", "d:\\qctt.png");
		
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
			
			//	System.setProperty("phantomjs.binary.path", phantomjsUrl);
			//	driver = new PhantomJSDriver();
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
