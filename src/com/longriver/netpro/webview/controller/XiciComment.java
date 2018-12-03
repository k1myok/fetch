package com.longriver.netpro.webview.controller;

import java.awt.image.BufferedImage;
import java.io.File;

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

import com.longriver.netpro.util.Configur;
import com.longriver.netpro.util.GetProprities;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 西祠胡同跟帖
 * @author wyanegao
 *
 */
public class XiciComment {

	private static int codetimes = 0;
	public static void main(String[] args) {
		TaskGuideBean task = new TaskGuideBean();
		task.setNick("劝百讽一");
		task.setPassword("MjfJvLSl2u");
		task.setAddress("http://www.xici.net/d246679289.htm");
		task.setAddress("http://www.xici.net/d216832528.htm");
		task.setCorpus("一言不合");
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
				
				if(codetimes <= 3){
					codetimes++;
					if(StringUtils.isNotBlank(error) && error.contains("验证码不正确")){
						
						driver.quit();
						toComment(task);
						return;
					}
					
				}else{
					
					isSuccess(task, error);
					return;
				}
				
			} catch (Exception e1) {
				
			}
			driver.get("http://www.xici.net/b151693/");
			driver.get(task.getAddress());
			
			driver.switchTo().frame("ueditor_0");
//			WebElement comConcent = driver.findElement(By.xpath("//*[@id='xici-editor-container']/textarea"));
			WebElement comConcent = driver.findElement(By.xpath("/html/body"));
			comConcent.clear();
			comConcent.sendKeys(task.getCorpus());
			Thread.sleep(3000);
			
			driver.switchTo().defaultContent();
			WebElement submit = driver.findElement(By.id("bSendDoc"));
			submit.click();
			
			Thread.sleep(1000 * 5);
			
			isSuccess(task, "");
		} catch (Exception e) {
			
			isSuccess(task, "评论异常");
			e.printStackTrace();
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
