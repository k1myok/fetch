package com.longriver.netpro.webview.educontroller;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 鲸媒体注册
 * @author rhy
 * @date 2018-4-2 上午10:44:38
 * @version V1.0
 */
public class JingMeitiRegister {

	public static void main(String[] args) {
		TaskGuideBean task = new TaskGuideBean();
		task.setCorpus("相信技术的力量");
		task.setCorpus("太危险了这世界^^^");
		task.setNick("17080621276");
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
		driver.get("http://www.jingmeiti.com/wp-login.php?action=register");
		
		driver.findElement(By.xpath("/html/body/div/div[3]/div/div/div[1]/a[2]")).click();
		
		Thread.sleep(2000);
		driver.findElement(By.id("user_login")).sendKeys(task.getNick());
		driver.findElement(By.id("user_email")).sendKeys(task.getNick()+"@163.com");
		
		driver.findElement(By.id("user_pass")).sendKeys(task.getPassword());
		driver.findElement(By.id("confirm_pass")).sendKeys(task.getPassword());
		
		driver.findElement(By.id("regLinkBtn")).click();
		Thread.sleep(10000);
		
		driver.findElement(By.xpath("/html/body/div/div[3]/div/div/div[1]/a[1]")).click();
		
		driver.findElement(By.id("user_login")).sendKeys(task.getNick());
		
		driver.findElement(By.id("user_pass")).sendKeys(task.getPassword());
		
		driver.findElement(By.id("loginLinkBtn")).click();
		
		Thread.sleep(5000);
		String currentUrl = driver.getCurrentUrl();
		
		if(currentUrl.equals("http://www.jingmeiti.com/wp-admin/")){
			
			task.setCode(100);
			isSuccess(task, "");
			
		}
		
		}catch(Exception e){
			
			isSuccess(task, "评论异常了");
			e.printStackTrace();
		}finally{
			
			driver.quit();
		}
		
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
			
//			System.setProperty("phantomjs.binary.path", phantomjsUrl);
//			driver = new PhantomJSDriver();
			System.setProperty("webdriver.firefox.bin",firefoxUrl);
			driver = new FirefoxDriver();
			
//			ChromeOptions chromeOptions = new ChromeOptions();
//			chromeOptions.addArguments("--start-maximized");
//			System.setProperty("webdriver.chrome.driver","C:\\tools\\chromedriver.exe");
//			driver = new ChromeDriver(chromeOptions);
			
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
