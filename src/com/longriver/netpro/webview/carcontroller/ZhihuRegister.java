package com.longriver.netpro.webview.carcontroller;

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
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import com.longriver.netpro.fetchScript.util.Jdbc2MysqlSpcard;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.MsgUtil;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 知乎注册
 * @author rhy
 * @date 2018-3-22 下午4:13:32
 * @version V1.0
 */
public class ZhihuRegister {

	private static int codetimes = 0;
	public static void main(String[] args) {
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://www.jiemian.com/article/1994606.html");
		task.setAddress("http://www.jiemian.com/article/2001213.html");
		task.setCorpus("相信技术的力量");
		task.setCorpus("太危险了这世界^^^");
		task.setNick("17173425504");
		task.setPassword("chwx123456");
		task.setHostPort("4001");
		toRegister(task);
	}
	
	/**
	 * 注册
	 * @param task
	 */
	public static void toRegister(TaskGuideBean task) {
		
		WebDriver driver = null;
		try{
		driver = getDriver();
		
		if(driver == null){
			isSuccess(task, "driver打开失败");
			return;
		}
		driver.get("https://www.zhihu.com/signup");
		
		driver.findElement(By.xpath("//*[@id='root']/div/main/div/div/div/div[2]/div[1]/div/div/form/div[1]/div[2]/div[2]")).sendKeys(task.getNick());
		driver.findElement(By.xpath("//*[@id='root']/div/main/div/div/div/div[2]/div[1]/div/div/form/div[3]/div[1]/button")).click();
		
		Thread.sleep(2000);
		try {
			String errorMask = driver.findElement(By.xpath("//*[@id='root']/div/main/div/div/div/div[2]/div[1]/div/div/form/div[1]/div[2]/div[2]/span")).getText();
			if(StringUtils.isNotBlank(errorMask) && errorMask.contains("该手机号已注册")){
				
				task.setCode(100);
				isSuccess(task, "");
				return;
			}
		
		} catch (Exception e2) {
		}
		
		int switchCard = MsgUtil.switchCard(task.getIsApp(),task.getHostPort().substring(2));
		if(switchCard!=1){
			
			isSuccess(task, "卡池换卡失败");
			return;
		}
		String port = "";
		if(task.getHostPort().length()==4){
			port = task.getHostPort().substring(0, 1);
		}else{
			port = task.getHostPort().substring(0, 2);
		}
		
		Thread.sleep(20000);
		
		int times = 0;
		String msg = "";
		while(true){
			
			if(times < 4){
				msg = Jdbc2MysqlSpcard.getResultHis(port);
				if(StringUtils.isNotBlank(msg)){
					break;
				}else{
					Thread.sleep(10000);
				}
			}else{
				
				break;
			}
		}
		
			String code = getMsgCode(msg);
			
			driver.findElement(By.xpath("//*[@id='root']/div/main/div/div/div/div[2]/div[1]/div/div/form/div[3]/div[1]/div/div[2]")).sendKeys(code);
			
			driver.findElement(By.xpath("//*[@id='root']/div/main/div/div/div/div[2]/div[1]/div/div/form/button")).click();
			
			Thread.sleep(2000);
			try {
				String lastError = driver.findElement(By.xpath("//*[@id='root']/div/main/div/div/div/div[2]/div[1]/div/div/form/div[3]/div[1]/div/div[2]")).getText();
				
				if(StringUtils.isNotBlank(lastError)){
					
					isSuccess(task, lastError);
					return;
				}
			} catch (Exception e) {
			}
		
			driver.findElement(By.xpath("//*[@id='root']/div/main/div/div/div/div/div[1]/div/form/div[1]/div[1]/input")).sendKeys(task.getNick());
			driver.findElement(By.xpath("//*[@id='root']/div/main/div/div/div/div/div[1]/div/form/div[2]/div/div[1]/input")).sendKeys(task.getPassword());
			
			driver.findElement(By.xpath("//*[@id='root']/div/main/div/div/div/div/div[1]/div/form/button")).click();
			
			Thread.sleep(2000);
			
			driver.findElement(By.xpath("//*[@id='root']/div/main/div/div[2]/div/div[2]/div[1]/button")).click();
			Thread.sleep(2000);
			
			driver.findElement(By.xpath("//*[@id='root']/div/main/div/div[2]/div/div[2]/button")).click();
			Thread.sleep(1000);
			
			task.setCode(100);
			isSuccess(task, "");
		}catch(Exception e){
			
			isSuccess(task, "评论异常了");
			e.printStackTrace();
		}finally{
			
//			driver.quit();
		}
	}
	
	/**
	 * 解析短信
	 * @param msg
	 * @return
	 */
	private static String getMsgCode(String msg) {
		
		String code = null;
		try {
			code = msg.substring(msg.indexOf("验证码是")+4,msg.indexOf("10 分钟内有效")-1).trim();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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
			
//			System.setProperty("phantomjs.binary.path", "C:\\tools\\phantomjs.exe");
//			driver = new PhantomJSDriver();
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