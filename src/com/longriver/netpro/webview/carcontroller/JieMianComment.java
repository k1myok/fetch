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
 * 界面评论
 * @author rhy
 * @date 2018-3-20 上午11:05:46
 * @version V1.0
 */
public class JieMianComment {

	private static int codetimes = 0;
	public static void main(String[] args) {
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://www.jiemian.com/article/1994606.html");
		task.setAddress("http://www.jiemian.com/article/2001213.html");
		task.setCorpus("相信技术的力量");
		task.setCorpus("太危险了这世界^^^");
		task.setNick("17080621284");
		task.setPassword("chwx123456");
		toComment(task);
	}
	
	/**
	 * 评论
	 * @param task
	 */
	public static void toComment(TaskGuideBean task) {
		
		if(task.getCorpus().length()<5){
			
			isSuccess(task, "请输入5字以上评论");
			return;
		}
		WebDriver driver = null;
		try{
		driver = getDriver();
		
		if(driver == null){
			isSuccess(task, "driver打开失败");
			return;
		}
		driver.get("https://passport.jiemian.com/");
		
		driver.findElement(By.id("email")).sendKeys(task.getNick());
		driver.findElement(By.name("password")).sendKeys(task.getPassword());
		
		driver.findElement(By.className("passport-btn")).click();
		Thread.sleep(2000);
		
		try {
			WebElement verifyImg = driver.findElement(By.id("verify_img"));
			String picCode = getPicCode(driver, verifyImg);
			
			driver.findElement(By.name("verify_code")).sendKeys(picCode);
			
			driver.findElement(By.className("passport-btn")).click();
			
			Thread.sleep(2000);
			String errorMsg = driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div/div[2]/form/ul/li[1]/p")).getText();
			
			if(codetimes <= 3){
				if(StringUtils.isNotBlank(errorMsg) && errorMsg.contains("验证码错误")){
					
					isSuccess(task, errorMsg);
					return;
					}
			}else{
				
				if(StringUtils.isNotBlank(errorMsg)){
					
					isSuccess(task, errorMsg);
					return;
				}
			}
			
		} catch (Exception e1) {
		}
		try {
			String errorMsg = driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div/div[2]/form/ul/li[1]/p")).getText();
			
			if(StringUtils.isNotBlank(errorMsg)){
				
			isSuccess(task, errorMsg);
			return;
			}
		} catch (Exception e) {
		}
		
		driver.get(URLDecoder.decode(task.getAddress(),"utf-8"));
		
		driver.findElement(By.id("content")).sendKeys(task.getCorpus());
		
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id='pl']/div[2]/button")).click();
		
		Thread.sleep(1000);
		
		isSuccess(task, "");
		
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
		
		File file = new File("d:\\jiemian.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("2050", "d:\\jiemian.png");
		
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
