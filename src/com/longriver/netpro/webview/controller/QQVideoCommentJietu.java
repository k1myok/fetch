package com.longriver.netpro.webview.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.PngErjinzhi;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 腾讯视频评论
 * @author rhy
 * @2017-9-4 下午3:20:12
 * @version v1.0
 */
public class QQVideoCommentJietu {

	private static Logger logger = Logger.getLogger(QQVideoCommentJietu.class);
	
	public static void main(String[] args) {
		
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("https://v.qq.com/x/cover/fjh8aiyz4v1eb8h.html");
		task.setCorpus("粉丝特约版");
		task.setCorpus("李小璐陪甜馨看小鸡仔");
		task.setCorpus("出问题不是一个人的错");
		task.setCorpus("问题放大了");
		task.setNick("3567918550");
		task.setPassword("wxb123456");
		
		toComment(task);
	}
	
	/**
	 * 腾讯登录
	 * @param task 引导所需内容
	 */
	public static void toComment(TaskGuideBean task){
		
		WebDriver driver = null;
		try{
		
		String address = URLDecoder.decode(task.getAddress(), "utf-8");
		java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config\\properties\\paramsConfig");
		String firefoxUrl = bundle.getString("firefoxurl");
		
		System.setProperty("webdriver.firefox.bin",firefoxUrl);
		FirefoxProfile profile = new FirefoxProfile();
//		profile.setPreference("permissions.default.stylesheet", 2);//禁用css
//		profile.setPreference("permissions.default.image", 2);//禁用图片
		profile.setPreference("dom.ipc.plugins.enabled.libflashplayer.so","false");//禁用flash
		driver = new FirefoxDriver(profile); 
		driver.manage().window().maximize();
		System.setProperty("webdriver.chrome.driver","C:\\tools\\chromedriver.exe");
	     ChromeOptions options = new ChromeOptions();
	    options.addArguments("--disable-plugins","--disable-images","--start-maximized","--disable-javascript");//禁用插件
//		driver = new ChromeDriver(options);
		driver.get(address);
		
		driver.findElement(By.id("mod_head_notice_trigger")).click();

//		driver.findElement(By.xpath("//*[@id='mod_notlogin_pop']/div/div/div/div[2]/div[2]/a")).click();
//		driver.findElement(By.xpath("//*[@id='J_Post']/div[2]/span")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("login_win_type"));
//		driver.switchTo().frame("iframe_mask");
		driver.findElement(By.className("btn_qq")).click();
		driver.switchTo().frame("_login_frame_quick_");
		driver.findElement(By.id("switcher_plogin")).click();
		
		driver.findElement(By.id("u")).sendKeys(task.getNick());
		driver.findElement(By.id("p")).sendKeys(task.getPassword());
		
		driver.findElement(By.id("login_button")).click();
		Thread.sleep(1000);
		
		try {
			String errmsg = driver.findElement(By.id("err_m")).getText();
			
			if(StringUtils.isNotBlank(errmsg)){
				
				isSuccess(task, errmsg);
				return;
			}
		} catch (Exception e1) {
		}
		((JavascriptExecutor)driver).executeScript("document.documentElement.scrollTop=5000");
		Thread.sleep(3000);
		((JavascriptExecutor)driver).executeScript("document.documentElement.scrollTop=2500");
		
		driver.switchTo().frame("commentIframe");
		
		try {
			WebElement loginButton = driver.findElement(By.xpath("//*[@id='J_Post']/div[2]/span"));
			if(loginButton.isDisplayed()){
				
				isSuccess(task, loginButton.getText());
				return;
			}
		} catch (Exception e1) {
		}
		
		driver.findElement(By.id("J_Textarea")).sendKeys(task.getCorpus());
		driver.findElement(By.id("J_PostBtn")).click();
		
		Thread.sleep(1000);
		
		WebElement comment = driver.findElement(By.xpath("//*[@id='J_ShortComment']/div"));
		
		//根据语料 判断是否成功
		if(comment.getText().contains(task.getCorpus())){
			System.out.println("截图发帖成功!");
			try {
				String picUri = getCommentPic(driver, comment);
				task.setPng(PngErjinzhi.getImageBinary(picUri));
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
			
//			driver.quit();
		}
		
	}
	/**
	 * 获取评论图片
	 * @param driver
	 * @throws IOException 
	 */
	private static String getCommentPic(WebDriver driver,WebElement comment) throws IOException {
		String picName = null;
		File screenshotAs = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		BufferedImage bufferedImage = ImageIO.read(screenshotAs);
		
		Point point = comment.getLocation();
		
		int width = comment.getSize().getWidth();
		int height = comment.getSize().getHeight();
		
//		BufferedImage subimage = bufferedImage.getSubimage(point.getX(), point.getY()+190, width, height);
		BufferedImage subimage = bufferedImage.getSubimage(point.getX(), point.getY()+190, width, height);
		
		ImageIO.write(subimage, "png", screenshotAs);
		
		picName = getPicName();
		File file = new File(picName);
		FileUtils.copyFile(screenshotAs, file);
		return picName;
	}
	/**
	 * 图片名称
	 * @return
	 */
	private static String getPicName() {
		String picName = "c:\\jietu\\";
		File file =new File(picName);
		if(!file.exists() && !file.isDirectory()){
		    file.mkdirs();  
		}
		String picUri = "c:\\jietu\\qq_"+System.currentTimeMillis()+".png";
		return picUri;
	}
	/**
	 * 判断是否成功
	 */
	public static void isSuccess(TaskGuideBean task,String msg){
		MQSender.toMQ(task,msg);
	}
}
