package com.longriver.netpro.webview.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.longriver.netpro.util.DriverGet;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 劲旅
 * @author rhy
 * @2017-11-23 上午9:21:31
 * @version v1.0
 */
public class JinlvComment {

	private static Logger logger = LoggerFactory.getLogger(JinlvComment.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://ctcnn.com/html/2017-11-21/19045499.html#PPN=info");
		task.setCorpus("这个版本还行吗a");
		task.setCorpus("可以的");
		task.setNick("18435160035");
		task.setPassword("q1w2e3r4");
		
		toComment(task);
	}

	/**
	 * 调用firefoxDriver
	 * @param task
	 */
	public static void toComment(TaskGuideBean task) {
		
		WebDriver driver = null;
		try{
			
		String address = URLDecoder.decode(task.getAddress(),"utf-8");
		
		java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config\\properties\\paramsConfig");
		String firefoxUrl = bundle.getString("firefoxurl");
		
		FirefoxProfile profile = new FirefoxProfile();
//		profile.setPreference("permissions.default.stylesheet", 2);//禁用css
//		profile.setPreference("permissions.default.image", 2);//禁用图像
//		profile.setPreference("dom.ipc.plugins.enabled.libflashplayer.so", "false");//禁用flash
		
		System.setProperty("webdriver.firefox.bin",firefoxUrl);
		driver = new FirefoxDriver(profile); 
		driver.get(address);  
		
		driver.navigate().refresh();
		WebElement textArea = driver.findElement(By.xpath("//*[@id='comment-txt']"));
		textArea.clear();
		textArea.sendKeys(task.getCorpus());
		
		WebElement username = driver.findElement(By.id("message_name"));
		username.clear();
		username.sendKeys(task.getNick());
		
		WebElement captchaImage = driver.findElement(By.id("captchaImage"));
		String imgcode = getImgCode(driver,captchaImage);
		
		WebElement captcha = driver.findElement(By.id("captcha"));
		captcha.clear();
		captcha.sendKeys(imgcode);
		
		WebElement contentButton = driver.findElement(By.xpath("//*[@id='commentBox']/div[1]/div[2]/div/div/input"));
		contentButton.click();
		
		Alert alert = driver.switchTo().alert();
		String text = alert.getText();
		alert.accept();
		if(text != null && text.contains("验证码不正确")){
			
			logger.info("验证码错误");
			isSuccess(task, "验证码错误");
		}else if(text != null && text.contains("成功")){
			
			logger.info("评论成功");
			isSuccess(task, "");
		}
		
		DriverGet.quit(driver);;
		}catch(Exception e){
			
			isSuccess(task, "评论异常");
			DriverGet.quit(driver);
			e.printStackTrace();
			logger.info("start firefox exception...");
		}
	}
	
	/**
	 * 获取验证码
	 * @param driver
	 * @param imgcode
	 * @return
	 */
	private static String getImgCode(WebDriver driver, WebElement imgcode) {
		
		try{
		//让整个页面截图
		File screenshotAs = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		BufferedImage bufferedImage = ImageIO.read(screenshotAs);
		
		//获取页面上元素的位置
		Point point = imgcode.getLocation();
		
		int width = imgcode.getSize().getWidth();
		int height = imgcode.getSize().getHeight();
		
		//裁剪整个页面的截图，以获得元素的屏幕截图
		BufferedImage subimage = bufferedImage.getSubimage(point.getX(), point.getY(), width, height);
		
		ImageIO.write(subimage, "png", screenshotAs);
		
		//将元素截图复制到磁盘
		File file = new File("c:\\jin.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("1040", "c:\\jin.png");
		
		return code;
		}catch(Exception e){
			
			logger.info("获取验证码异常");
			e.printStackTrace();
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