package com.longriver.netpro.webview.controller;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.longriver.netpro.util.Configur;
import com.longriver.netpro.util.GetProprities;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 西祠发帖
 * @author rhy
 * @date 2018-4-23 上午11:15:34
 * @version V1.0
 */
public class XiciPost {
	private static int codetimes = 0;
	public static void main(String[] args) {
		TaskGuideBean task = new TaskGuideBean();
		task.setNick("劝百讽一");
		task.setPassword("MjfJvLSl2u");
		task.setAddress("http://www.xici.net/b151693/");

		task.setNick("山水都梁");
		task.setPassword("a19870621");

		task.setCorpus("干就完了");
		toComment(task);
	}
	public static void toComment(TaskGuideBean task) {
		WebDriver driver = null;
		
		Configur config = GetProprities.paramsConfig;
		String firefoxUrl = config.getProperty("firefoxurl");
		
		FirefoxProfile profile = new FirefoxProfile();
		//禁用css
		//profile.setPreference("permissions.default.stylesheet", 2);
		//不加载图片
		//profile.setPreference("permissions.default.image", 2);
		//##禁用Flash 
		profile.setPreference("dom.ipc.plugins.enabled.libflashplayer.so","false");
		System.setProperty("webdriver.firefox.bin",firefoxUrl); 
		driver = new FirefoxDriver(profile); 
		driver.get("http://account.xici.net/login");    
		
		try {
//			WebElement goLogin = driver.findElement(By.xpath("//*[@id='msg']/div[2]/a[1]"));
//			goLogin.click();
//			Thread.sleep(1000 * 2);
			
//			WebElement frame = driver.findElement(By.xpath("/html/body/div[3]/div/div[1]/form"));
//			driver.switchTo().frame(frame);
			WebElement name = driver.findElement(By.name("username"));
			name.sendKeys(task.getNick());
			Thread.sleep(1000);
			WebElement password = driver.findElement(By.name("password"));
			password.sendKeys(task.getPassword());
			Thread.sleep(1000);
			WebElement imageCode = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div[2]/form/div[4]/a[1]/div/img"));
			yzmInput(driver, imageCode, 1);
			WebElement login = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div[2]/form/div[7]/button"));
			login.click();
			try {
				String error = driver.findElement(By.className("tip_info")).getText();
				
				if(StringUtils.isNotBlank(error) ){
					
				if(codetimes <= 3){
					codetimes++;
					if(error.contains("验证码不正确")){
						
						driver.quit();
						toComment(task);
						return;
					}else{
						
						isSuccess(task, error);
						return;
					}
					
				}else{
					
					isSuccess(task, error);
					return;
				}
				
				}
			} catch (Exception e1) {
				
			}
			driver.get("http://www.xici.net/b151693/");
			driver.get(task.getAddress());
			
			Thread.sleep(2000);
			try {
				driver.findElement(By.xpath("//*[@id='warp']/div[12]/div/a[1]")).click();
			} catch (Exception e1) {
			}
			
			driver.findElement(By.id("doc_title")).sendKeys(task.getCorpus());
			
			
			driver.switchTo().frame("ueditor_0");
			driver.findElement(By.xpath("/html/body")).sendKeys(task.getCorpus());
			
			driver.switchTo().defaultContent();
			
			driver.findElement(By.id("verifyimg")).click();
			
			Thread.sleep(800);
			WebElement verifyimg = driver.findElement(By.id("showverifyimg"));
			
			String verifyCode = getVerifyimg(driver, verifyimg);
			driver.findElement(By.id("verifyimg")).sendKeys(verifyCode);
			
			driver.findElement(By.id("bSendDoc")).click();
			Thread.sleep(1000);
			String text = "";
			try {
				Alert alert = driver.switchTo().alert();
				text = alert.getText();
			} catch (Exception e) {
			}
			if(StringUtils.isNotBlank(text) && text.contains("验证码")){
				
				isSuccess(task, "验证码错误");
			}else{
				
				isSuccess(task, "");
			}
		} catch (Exception e) {

			String text = "";
			try {
				text = driver.findElement(By.xpath("//*[@id='msg']/div/div[2]/a")).getText();
				
			} catch (Exception e1) {
			}
			if(StringUtils.isNotBlank(text) && text.contains("点此去验证")){
				
				isSuccess(task, "账号需认证");
				return;
			}else{
				
				isSuccess(task, "评论异常");
				e.printStackTrace();
			}
		}finally{
			
			driver.quit();
		}
	}
	public static void yzmInput(WebDriver driver,WebElement imgCode,int yzmcode){
		String code = getPic(driver, imgCode);
		driver.findElement(By.xpath("/html/body/div[3]/div[2]/div[2]/form/div[5]/input")).clear();
		driver.findElement(By.xpath("/html/body/div[3]/div[2]/div[2]/form/div[5]/input")).sendKeys(code);
		
	}
	
	/**
	 * 获取验证码
	 * @param driver
	 */
	private static String getVerifyimg(WebDriver driver,WebElement comment) {
		
		try{
		File screenshotAs = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		BufferedImage bufferedImage = ImageIO.read(screenshotAs); 
		
		Point point = comment.getLocation();
		
		int width = comment.getSize().getWidth();
		int height = comment.getSize().getHeight();
		if(width<=0) width=300;
		if(height<=0) height=100;
		BufferedImage subimage = bufferedImage.getSubimage(point.getX(), point.getY(), width, height);
		
		ImageIO.write(subimage, "png", screenshotAs);
		
//		String picName = getPicName();

		File file = new File("c:\\verify.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("3040", "c:\\verify.png");

		
		return code;
		}catch(Exception e){
			
		}
		return null;
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
		if(width<=0) width=300;
		if(height<=0) height=100;
		BufferedImage subimage = bufferedImage.getSubimage(point.getX(), point.getY(), width, height);
		
		ImageIO.write(subimage, "png", screenshotAs);
		
//		String picName = getPicName();

		File file = new File("c:\\xici.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("3050", "c:\\xici.png");

		
		return code;
		}catch(Exception e){
			
		}
		return null;
	}
	/**
	 * 判断是否成功 
	 */
	public static void isSuccess(TaskGuideBean task,String msg){
		
		MQSender.toMQ(task,msg);
	}

}
