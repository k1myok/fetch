package com.longriver.netpro.webview.carcontroller;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
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

public class PeopleComment {
	public static boolean process = false;
	public static boolean isSend = false;
	public static void main(String[] args) {
		process = true;
		TaskGuideBean task = new TaskGuideBean();
		task.setNick("17080621281");
		task.setPassword("chwx1234");
		task.setCorpus("撸起袖子加油干");
		task.setAddress("http://bbs1.people.com.cn/post/129/1/2/166775846.html");
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
		System.setProperty("webdriver.firefox.bin",firefoxUrl);
		driver = new FirefoxDriver(profile); 
		driver.get("http://sso.people.com.cn/login");
		
		try {
			WebElement name = driver.findElement(By.id("username"));
			name.clear(); 
			name.click();
			name.sendKeys(task.getNick());
			Thread.sleep(2000);
			WebElement page = driver.findElement(By.xpath("/html/body/div[2]"));
			page.click();
			Thread.sleep(2000);
			WebElement password = driver.findElement(By.id("password"));
			password.clear(); 
			password.click();
			Thread.sleep(2000);
			password.sendKeys(task.getPassword());
			try {
				WebElement imgCode = driver.findElement(By.id("vercodeimg"));
				yzmInput(driver,imgCode,1);
				WebElement submit = driver.findElement(By.id("submit"));
				submit.click();
				try {
					WebElement error = driver.findElement(By.xpath("//*[@id='message']/label"));
					while (error.getText().contains("验证码")) {
						yzmInput(driver,imgCode,1);
						Thread.sleep(2000);
						submit.click();
					}
				} catch (Exception e) {
					
				}
			} catch (Exception e) {
				WebElement submit = driver.findElement(By.id("submit"));
				submit.click();
			}
			Thread.sleep(2000);
			driver.get(task.getAddress());
			WebElement comContent = driver.findElement(By.xpath("/html/body/div[2]/div[3]/ul/li[3]/form/textarea"));
			comContent.clear();
			comContent.click();
			comContent.sendKeys(task.getCorpus());
			Thread.sleep(1000 * 2);
			WebElement commit = driver.findElement(By.xpath("/html/body/div[2]/div[3]/ul/li[3]/form/div[1]/p/span[1]"));
			commit.click();
			
			isSuccess(task, "");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isSuccess(task, "评论异常");
		}finally{
			
			driver.quit();
		}
		
		
	}
	public static void yzmInput(WebDriver driver,WebElement imgCode,int yzmcode){
		String code = getPic(driver, imgCode);
		driver.findElement(By.id("randCode")).clear();;
		driver.findElement(By.id("randCode")).sendKeys(code);
		
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
		File file = new File("c:\\ifeng.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("3060", "c:\\ifeng.png");
		
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