package com.longriver.netpro.webview.carcontroller;

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

import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 微博评论
 * @author rhy
 * @date 2018-3-29 下午5:13:21
 * @version V1.0
 */
public class WeiboPicComment {

	private static int codetimes = 0;
	public static void main(String[] args) {
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://www.jiemian.com/article/1994606.html");
		task.setAddress("http://www.jiemian.com/article/2001213.html");
		task.setCorpus("相信技术的力量");
		task.setCorpus("太危险了这世界^^^");
		task.setNick("17019995457");
		task.setPassword("g32bwie");
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
		driver.get("https://login.sina.com.cn/signup/signin.php");
		
		driver.findElement(By.id("username")).sendKeys(task.getNick());
		driver.findElement(By.id("password")).sendKeys(task.getPassword());
		
		driver.findElement(By.xpath("//*[@id='vForm']/div[2]/div/ul/li[7]/div[1]/input")).click();
		
		try {
			WebElement checkImg = driver.findElement(By.id("check_img"));
			
			String picCode = getPicCode(driver, checkImg);
			
			driver.findElement(By.id("door")).sendKeys(picCode);
			
			driver.findElement(By.xpath("//*[@id='vForm']/div[2]/div/ul/li[7]/div[1]/input")).click();
			
		} catch (Exception e1) {
		}
		
		try {
			String loginError = driver.findElement(By.xpath("//*[@id='login_err']/span/i[2]")).getText();
			
			if(StringUtils.isNotBlank(loginError)){
				
				if(loginError.contains("验证码")){
					
					codetimes++;
					if(codetimes <= 4){
						
						driver.quit();
						toComment(task);
					}
				}else{
					
					isSuccess(task, loginError);
					return;
				}
			}
		} catch (Exception e1) {
		}
		Thread.sleep(3000);
		driver.get("https://weibo.com");
		
		Thread.sleep(5000);
		driver.findElement(By.xpath("//*[@id='v6_pl_content_publishertop']/div/div[2]/textarea")).sendKeys(task.getCorpus());
//		driver.findElement(By.xpath("//*[@id='pic_upload']/input")).sendKeys("C:\\Users\\chwx\\Pictures\\QQ浏览器截图\\3.jpg");
//		driver.findElement(By.xpath("//*[@id='pic_upload']/input")).sendKeys("C:\\Users\\chwx\\Pictures\\QQ浏览器截图\\4.jpg");
//		Thread.sleep(5000);
		
		//视频引导
		driver.findElement(By.xpath("//*[@id='v6_pl_content_publishertop']/div/div[3]/div[2]/a[3]/div/form/input[1]")).sendKeys("D:\\wangyi.mp4");
		Thread.sleep(5000);
		try {
			driver.findElement(By.cssSelector("p.btn:nth-child(6) > a:nth-child(1)")).click();
			driver.findElement(By.cssSelector("div.W_layer:nth-child(3) > div:nth-child(1) > div:nth-child(3) > a:nth-child(1)")).click();
		} catch (Exception e) {
		}
		
		driver.findElement(By.xpath("//*[@id='v6_pl_content_publishertop']/div/div[3]/div[1]/a")).click();

		Thread.sleep(3000);
		
		isSuccess(task, "");
		
		}catch(Exception e){
			
			isSuccess(task, "评论异常了");
		}finally{
			
			driver.quit();
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
		
		File file = new File("d:\\weibo.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("2050", "d:\\weibo.png");
		
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

