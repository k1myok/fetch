package com.longriver.netpro.webview.fincontroller;

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

import com.longriver.netpro.util.Configur;
import com.longriver.netpro.util.DriverGet;
import com.longriver.netpro.util.GetProprities;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.PngErjinzhi;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 新浪股吧
 * @author rhy
 * @date 2018-4-26 上午11:39:32
 * @version V1.0
 */
public class SinagubaConnent {

	private static int codetimes = 0;
	public static void main(String[] args) {
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://guba.sina.com.cn/?s=thread&bid=14247&tid=1691437&pid=1&dpc=1");
		task.setCorpus("相信技术的力量");
		task.setCorpus("太危险了这世界^^^");
		task.setNick("13602024474");
		task.setPassword("2crgkeuyu");
		toComment(task);
	}
	
	/**
	 * 评论
	 * @param task
	 */
	public static void toComment(TaskGuideBean task) {
		WebDriver driver = null;
		try{
			String address = task.getAddress();
			
	//		java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config\\properties\\paramsConfig");
	//		String firefoxUrl = bundle.getString("firefoxurl");
			Configur config = GetProprities.paramsConfig;
			String firefoxUrl = config.getProperty("firefoxurl");
			
			FirefoxProfile profile = new FirefoxProfile();
			//禁用css
	//		profile.setPreference("permissions.default.stylesheet", 2);
			//不加载图片
	//		profile.setPreference("permissions.default.image", 2);
			//##禁用Flash 
			profile.setPreference("dom.ipc.plugins.enabled.libflashplayer.so","false");
			
			System.setProperty("webdriver.firefox.bin",firefoxUrl);
			driver = new FirefoxDriver(profile); 
			
			driver.get("https://login.sina.com.cn/signup/signin.php");  
			
			WebElement username = driver.findElement(By.id("username"));
			username.clear();
			username.sendKeys(task.getNick());
			
			WebElement password = driver.findElement(By.id("password"));
			password.clear();
			password.sendKeys(task.getPassword());
			
			WebElement loginButton = driver.findElement(By.xpath("//*[@id='vForm']/div[2]/div/ul/li[7]/div[1]/input"));
			loginButton.click();
			
			Thread.sleep(4000);
			try{
				WebElement imgCode = driver.findElement(By.id("check_img"));
				
				String code = getPicCode(driver, imgCode);
				System.out.println("验证码:"+code);
				WebElement codePut = driver.findElement(By.id("door"));
				codePut.clear();
				codePut.sendKeys(code);
				
				WebElement loginButton2 = driver.findElement(By.xpath("//*[@id='vForm']/div[2]/div/ul/li[7]/div[1]/input"));
				loginButton2.click();
				
				Thread.sleep(2000);
				}catch(Exception e){
					System.out.println("验证码失败");
					System.out.println("验证码失败");
				}
			String currentUrl = driver.getCurrentUrl();
			Thread.sleep(200);
			if(StringUtils.isNotBlank(currentUrl) && currentUrl.contains("login.sina.com")){
				if(codetimes<4){
					codetimes++;
					DriverGet.quit(driver);
					toComment(task);
					return ;
				}
				isSuccess(task, "登录失败");
				DriverGet.quit(driver);
				return;
			}else if(StringUtils.isNotBlank(currentUrl) && currentUrl.contains("security.weibo.com")){
				
				isSuccess(task, "账号被锁");
				DriverGet.quit(driver);
				return;
			}
			
			driver.get(task.getAddress());
			Thread.sleep(1500);
			driver.findElement(By.xpath("//*[@id='thread']/div[2]/div[6]/div[1]/textarea")).sendKeys(task.getCorpus());
			Thread.sleep(2000);
			
			
			WebElement imgcode = driver.findElement(By.xpath("//*[@id='thread']/div[2]/div[6]/div[1]/div[1]/a[1]/img"));
			
			
			driver.findElement(By.xpath("//*[@id='thread']/div[2]/div[6]/div[1]/div[1]/label/input")).sendKeys("");
			
			

			
		}catch(Exception e){
			isSuccess(task, "报错失败");
			System.out.println("评论异常");
			e.printStackTrace();
		}finally{
			isSuccess(task, "");//成功
			DriverGet.quit(driver);
			driver.quit();
		}
		System.out.println("结束------------------");
		System.out.println("结束------------------");
	}
	/**
	 * 获取验证码
	 * @param driver
	 * @throws IOException 
	 */
	private static String getImgCode(WebDriver driver,WebElement comment) throws IOException {
		
		File screenshotAs = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		BufferedImage bufferedImage = ImageIO.read(screenshotAs);
		
		Point point = comment.getLocation();
		
		int width = comment.getSize().getWidth();
		int height = comment.getSize().getHeight();
		if(width<=0) width=300;
		if(height<=0) height=100;
		BufferedImage subimage = bufferedImage.getSubimage(point.getX(), point.getY(), width, height);
		
		ImageIO.write(subimage, "png", screenshotAs);
		
		File file = new File("d:\\sina.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("3040", "d:\\sina.png");
		
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
		
		File file = new File("d:\\sina.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("3000", "d:\\sina.png");
		
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
