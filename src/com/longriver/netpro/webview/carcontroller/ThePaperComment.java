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
 * 澎湃新闻
 * @author rhy
 * @date 2018-3-22 上午9:31:08
 * @version V1.0
 */
public class ThePaperComment {
	
	private static int codetimes = 0;
	public static void main(String[] args) {
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://www.thepaper.cn/newsDetail_forward_2042579");
		task.setCorpus("这个厉害了");
		task.setNick("17080621284");
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
		driver.get(URLDecoder.decode(task.getAddress(),"utf-8"));
		
		driver.findElement(By.xpath("//*[@id='head']/div[1]/div/a[3]")).click();
		
		
		
		driver.findElement(By.id("lg_wds_name")).sendKeys(task.getNick());
		driver.findElement(By.id("lg_wds_pwd")).sendKeys(task.getPassword());
		
		WebElement dyncode = driver.findElement(By.id("annexCode2"));
		String picCode = getPicCode(driver, dyncode);
		
		driver.findElement(By.id("lg_wds_dynCode")).sendKeys(picCode);
		
		driver.findElement(By.xpath("/html/body/div[10]/div[2]/div[6]")).click();
		
		Thread.sleep(1000);
		try {
			String errorMsg = driver.findElement(By.id("login_msg")).getText();
			
			if(StringUtils.isNotBlank(errorMsg) && !errorMsg.equals("登录成功")){
				
				if(errorMsg.equals("图片中的字符输入错误") && codetimes <= 4){
					
					driver.quit();
					codetimes++;
					toComment(task);
				}else{
					isSuccess(task, errorMsg);
					return;
				}
			}
		} catch (Exception e) {
		}		
		driver.findElement(By.id("commText")).sendKeys(task.getCorpus());
		
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id='commtid']/div[1]/div[2]/button")).click();
		
		Thread.sleep(1000);
		
		isSuccess(task, "");
		
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
		
		File file = new File("d:\\paper.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("3040", "d:\\paper.png");
		
		return code;
	}
		/**
		 * 判断是否成功
		 */
		public static void isSuccess(TaskGuideBean task,String msg){
				MQSender.toMQ(task,msg);
		}

}
