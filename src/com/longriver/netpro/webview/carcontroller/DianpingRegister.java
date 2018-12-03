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

import com.longriver.netpro.fetchScript.util.Jdbc2MysqlSpcard;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.MsgUtil;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 大众点评注册
 * @author rhy
 * @date 2018-3-23 上午10:36:55
 * @version V1.0
 */
public class DianpingRegister {

	private static int codetimes = 0;
	public static void main(String[] args) {
		TaskGuideBean task = new TaskGuideBean();
		task.setCorpus("相信技术的力量");
		task.setCorpus("太危险了这世界^^^");
		task.setNick("17173425486");
		task.setPassword("chwx123456");
		task.setHostPort("6003");
		toRegister(task);
	}
	
	/**
	 * 评论
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
		driver.get("https://www.dianping.com/reg");
//		driver.get("https://www.dianping.com/account/iframeRegister?callback=EasyLogin_frame_callback0&wide=false&protocol=https:&redir=https%3A%2F%2Fwww.dianping.com");
		driver.switchTo().frame(0);
		driver.findElement(By.id("mobile-number-textbox")).sendKeys(task.getNick());
		Thread.sleep(800);
		driver.findElement(By.id("send-number-button")).click();
		
		Thread.sleep(3000);
		try {
			String alertContent = driver.findElement(By.xpath("//*[@id='register-form']/div[2]/div")).getText();
			
			if(StringUtils.isNotBlank(alertContent)){
				
				if(alertContent.contains("该手机号已经注册")){
					task.setCode(100);
					isSuccess(task, "");
					return;
				}else if(!alertContent.contains("动态码已发送") && !alertContent.contains("请输入图形验证码")){
					
					isSuccess(task, alertContent);
					return;
				}
			}
		} catch (Exception e2) {
		}
		
		try {
			WebElement captcha = driver.findElement(By.xpath("//*[@id='captcha-container']/div[2]"));
			String picCode = getPicCode(driver, captcha);
			
			driver.findElement(By.id("captcha-textbox")).sendKeys(picCode);
			
			Thread.sleep(2000);
			try {
				String itemContent = driver.findElement(By.xpath("//*[@id='register-form']/div[2]/div")).getText();
				
				if(StringUtils.isNotBlank(itemContent)){
					
					if(itemContent.contains("该手机号已经注册")){
						
						task.setCode(100);
						isSuccess(task, "");
						return;
					}else if(codetimes <= 3 && itemContent.contains("输入的验证码错误")){
						
						codetimes++;
						driver.quit();
						toRegister(task);
						
					}else if(!itemContent.contains("动态码已发送") && !itemContent.contains("动态码不能为空")){
						
						isSuccess(task, itemContent);
						return;
					}
				}
			} catch (Exception e) {
			}
		} catch (Exception e2) {
		}
		
		Thread.sleep(900);
		driver.findElement(By.id("register-button")).click();
		
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
		String msgCode = "";
		while(true){
			
			String msg = Jdbc2MysqlSpcard.getResultHis(port);
			if(StringUtils.isNotBlank(msg)){
				
				msgCode = getMessageCode(msg);
				break;
			}else{
				
				Thread.sleep(10000);
				
				if(times >= 4){
					break;
				}
				times++;
			}
		}
		driver.findElement(By.id("number-textbox")).sendKeys(msgCode);
		Thread.sleep(700);
		driver.findElement(By.id("password-textbox")).sendKeys(task.getPassword());
		Thread.sleep(1200);
		driver.findElement(By.id("register-button")).click();
		Thread.sleep(1000);
		
		try {
			String errorMsg = driver.findElement(By.xpath("//*[@id='register-form']/div[2]/div")).getText();
			
			if(StringUtils.isNotBlank(errorMsg)){
				
			isSuccess(task, errorMsg);
			return;
			}
		} catch (Exception e) {
		}
		
		String currentUrl = driver.getCurrentUrl();//
		
		System.out.println(currentUrl);
		
		if(currentUrl.equals("https://www.dianping.com/")){
			
			task.setCode(100);
			isSuccess(task, "");
			return;
		}else{
			
			isSuccess(task, "注册失败");
			return;
		}
		
		}catch(Exception e){
			
			isSuccess(task, "评论异常了");
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
	private static String getMessageCode(String msg) {
		
		String code = "";
		try {
			code = msg.substring(msg.indexOf("手机注册验证码")+8,msg.indexOf("如非本人操作")-1).trim();
		} catch (Exception e) {
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
//		BufferedImage subimage = bufferedImage.getSubimage(point.getX(), point.getY(), width, height);
		BufferedImage subimage = bufferedImage.getSubimage(1100, 430, width, height);
		ImageIO.write(subimage, "png", screenshotAs);
		
		File file = new File("c:\\dianping.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("3040", "c:\\dianping.png");
		
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
//			System.setProperty("webdriver.firefox.bin",firefoxUrl);
//			driver = new FirefoxDriver();
			ChromeOptions chromeOptions = new ChromeOptions();
//			chromeOptions.addArguments("--start-maximized");
			System.setProperty("webdriver.chrome.driver","C:\\vcode\\chromedriver.exe");
			driver = new ChromeDriver(chromeOptions);
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
