package com.longriver.netpro.webview.carcontroller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 知乎评论
 * @author rhy
 * @date 2018-3-19 下午2:36:06
 * @version V1.0
 */
public class ZhihuComment {

	public static void main(String[] args) {
		//手机号账号,前面需加86
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("https://www.zhihu.com/question/28237063/answer/333286987");
		task.setCorpus("没意思了@@@");
		task.setNick("17080621284");
		task.setPassword("chwx123456");
		toComment(task);
	}
	
	private static void toComment(TaskGuideBean task) {
		
		WebDriver driver = null;
		try{
		driver = getDriver();
		driver.get("https://www.zhihu.com/signup");
		
		driver.findElement(By.xpath("//*[@id='root']/div/main/div/div/div/div[2]/div[2]/span")).click();
		
		driver.findElement(By.xpath("//*[@id='root']/div/main/div/div/div/div[2]/div[1]/form/div[1]/div[2]/div[1]/input")).sendKeys(task.getNick());
		
		driver.findElement(By.xpath("//*[@id='root']/div/main/div/div/div/div[2]/div[1]/form/div[2]/div/div[1]/input")).sendKeys(task.getPassword());
		
		driver.findElement(By.xpath("//*[@id='root']/div/main/div/div/div/div[2]/div[1]/form/button")).click();
		
		driver.get(task.getAddress());
		
		driver.findElement(By.xpath("//*[@id='root']/div/main/div/div[1]/div[2]/div[2]/div/div/div[1]/button[2]")).click();
		
		driver.findElement(By.xpath("//*[@id='root']/div/main/div/div[2]/div[1]/div/div[1]/div/div/form/div/div[1]/div/div/div[2]/div/div[2]/div/div/div/div/span/span")).sendKeys(task.getCorpus());

		driver.findElement(By.xpath("//*[@id='root']/div/main/div/div[2]/div[1]/div/div[1]/div/div/form/div/div[2]/div/div/div[2]/button")).click();
		Thread.sleep(1000);
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
//			driver.quit();
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
		
		File file = new File("d:\\maoyan.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("3040", "d:\\maoyan.png");
		
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
