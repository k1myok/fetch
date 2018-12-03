package com.longriver.netpro.webview.carcontroller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;

import com.longriver.netpro.fetchScript.util.Jdbc2MysqlSpcard;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.MsgUtil;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 爱卡汽车注册
 * @author rhy
 * @date 2018-3-8 上午10:52:11
 * @version V1.0
 */
public class AutohomeRegister {

	private static int codetimes = 0;
	public static void main(String[] args) {
		try{
			//手机号账号,前面需加86
			TaskGuideBean task = new TaskGuideBean();
			task.setNick("17173425504");
			task.setPassword("chwx090674542");
			task.setNick("17173425505");
			task.setPassword("chwx123456");
			
			task.setAddress("https://m.huanqiu.com/r/MV8wXzExNjQ3MjIzXzIxMjJfMTUyMDM5MTYwMA==?__from=yidian&yidian_docid=0IUEB5oC&yidian_s=&yidian_appid=");
			task.setAddress("https://www.yidianzixun.com/article/0IUJ3bjb");
			task.setCorpus("心更难咋样");
//			toRun(task);//一点账号登录
			task.setHostPort("5001");
			
			toRegister(task);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 爱卡汽车注册
	 * @param task
	 */
	private static void toRegister(TaskGuideBean task) {
		
		WebDriver driver = null;
		try {
			driver = getDriver();
			
			if(driver == null){
				
				isSuccess(task, "driver打开失败");
				return;
			}
			
			driver.get("http://my.xcar.com.cn/logging.php?action=login&referer=%2F");
			
			driver.findElement(By.cssSelector(".sina")).click();
			
//			Thread.sleep(2000);
			
			if(driver.getCurrentUrl().contains("api.weibo.com/oauth2/authorize")){
				
				driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[2]/div/p/a[1]")).click();
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
			

//			driver.findElement(By.xpath("//*[@id='app']/div[2]/div[1]/div/div[3]/div/ul/li[3]/a")).click();
			
//			Thread.sleep(2000);
//			String[] password = task.getPassword().split("&");
//			driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div/div/ul/li[2]")).click();
			
//			Thread.sleep(1000);
			
			driver.findElement(By.id("userId")).sendKeys(task.getNick());
			
			driver.findElement(By.id("passwd")).sendKeys(task.getPassword());
			
			try {
				WebElement vcode = driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/span/img"));
				
				String picCode = getPicCode(driver, vcode);
				
				driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/input")).sendKeys(picCode);
				
				driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[2]/div/p/a[1]")).click();
				
				Thread.sleep(2000);
				
				try {
					if(StringUtils.isNotBlank(driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText())){
						
						
						if(codetimes<3){
						if(driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText().contains("输入的验证码不正确")){
							
							codetimes++;
							driver.quit();
							toRegister(task);
						}
						}else{
//						task.setCode(6);
						isSuccess(task, driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText());
						return;
						}
					};
				} catch (Exception e) {
				}
			} catch (Exception e3) {
			}
			try {
				driver.findElement(By.className("WB_btn_login")).click();
				Thread.sleep(2000);
				
				try {
					if(StringUtils.isNotBlank(driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText())){
						
						
						if(codetimes<3){
						if(driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText().contains("输入的验证码不正确")){
							
							codetimes++;
							driver.quit();
							toRegister(task);
						}
						}else{
							
						isSuccess(task, driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText());
						return;
						}
					}
				} catch (Exception e) {
				}
			} catch (Exception e3) {
			}
			
			try {
				WebElement vcode = driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/span/img"));
				
				String picCode = getPicCode(driver, vcode);
				
				driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/input")).sendKeys(picCode);
				
				driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[2]/div/p/a[1]")).click();
				
				Thread.sleep(2000);
				try {
					if(StringUtils.isNotBlank(driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText())){
						
						
						if(codetimes<3){
						if(driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText().contains("输入的验证码不正确")){
							
							codetimes++;
							driver.quit();
							toRegister(task);
						}
						}else{
//						task.setCode(6);
						isSuccess(task, driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText());
						return;
						}
					};
				} catch (Exception e) {
				}
				
			} catch (Exception e2) {
			}
			
			
			try {
				String errorcontent = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[1]/dl/dt[1]")).getText();
				if(StringUtils.isNotBlank(errorcontent)){
					
//					task.setCode(1);
					isSuccess(task, errorcontent + driver.findElement(By.xpath("/html/body/div/div[2]/div/div[1]/dl/dt[3]")).getText());
					return;
				}
			} catch (Exception e1) {
			}
			
			if(driver.getCurrentUrl().contains("reg.xcar.com.cn/bind.php")){
				
				driver.findElement(By.id("mobile_num")).sendKeys(task.getNick());
				driver.findElement(By.id("show_check_code")).click();
				
				WebElement imgcode = driver.findElement(By.id("code_img"));
				
				String xcarcode = getXcarPicCode(driver, imgcode);
				driver.findElement(By.id("check_code")).sendKeys(xcarcode);
				driver.findElement(By.id("sendcode")).click();
				
				try {
					String codeError = driver.findElement(By.id("code_error")).getText();
					
					if(StringUtils.isNotBlank(codeError)){
						 
						
						if(codeError.contains("验证码错误") && codetimes<3){
							
							codetimes++;
							driver.quit();
							toRegister(task);
							
						}else{
							
							isSuccess(task, codeError);
							return;
						}
					}
				} catch (Exception e) {
				}
				
				try {
					String errormsg = driver.findElement(By.id("error_msg")).getText();
					if(StringUtils.isNotBlank(errormsg)){
						
						isSuccess(task, errormsg);
						return;
					}
				} catch (Exception e) {
				}
				Thread.sleep(35000);
				String msg = Jdbc2MysqlSpcard.getResultHis(port);
				
				String messageCode = getMessageCode(msg);
				if(StringUtils.isBlank(messageCode)){
					
					isSuccess(task, "短信验证码错误");
					return;
				}
				driver.findElement(By.id("msgcode")).sendKeys(messageCode);
				
				driver.findElement(By.id("msgcode")).sendKeys(Keys.ENTER);
//				driver.findElement(By.id("bindbtn")).click();
			}
			
			
			if(driver.getCurrentUrl().contains("api.weibo.com/oauth2/authorize")){
				
				driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[2]/div/p/a[1]")).click();
				
				Thread.sleep(2000);
			}
		}catch(Exception e){
			
			isSuccess(task, "爱卡汽车注册异常");
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
		if(StringUtils.isNotBlank(msg)){
			code = msg.substring(msg.indexOf("验证码为")+5,msg.indexOf("此验证码用于验证手机")-1).trim();
		}
		return code;
	}

	private static String getXcarPicCode(WebDriver driver, WebElement comment) throws IOException {
		File screenshotAs = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		BufferedImage bufferedImage = ImageIO.read(screenshotAs);
		
		Point point = comment.getLocation();
		
		int width = comment.getSize().getWidth();
		int height = comment.getSize().getHeight();
		if(width<=0) width=300;
		if(height<=0) height=100;
		BufferedImage subimage = bufferedImage.getSubimage(point.getX(), point.getY(), width, height);
		
		ImageIO.write(subimage, "png", screenshotAs);
		
		File file = new File("d:\\xcar.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("1040", "d:\\xcar.png");
		
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
		
		File file = new File("d:\\weibo.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("3050", "d:\\weibo.png");
		
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
			
//		System.setProperty("phantomjs.binary.path", phantomjsUrl);
//		driver = new PhantomJSDriver();
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
	
	public static void toRegister2(TaskGuideBean task){
		WebDriver driver = null;
		try {
			driver = getDriver();
			
			if(driver == null){
				
				isSuccess(task, "driver打开失败");
				return;
			}
			
			driver.get("http://reg.xcar.com.cn/register.php");
			
			driver.findElement(By.id("mobile")).sendKeys(task.getNick());
			
			driver.findElement(By.cssSelector(".geetest_radar_tip")).click();
			
			Thread.sleep(1000);
			driver.findElement(By.cssSelector(".geetest_slider_button")).click();
			
			WebElement canvas = driver.findElement(By.cssSelector(".geetest_canvas_slice"));
			
			String canvasCode = getCanvas(driver,canvas);
			String[] split = canvasCode.split(",");
			
			Actions actions = new Actions(driver);
			actions.moveToElement(driver.findElement(By.cssSelector(".geetest_slider_track")), Integer.parseInt(split[0])+70, Integer.parseInt(split[1])).perform();
			
			System.out.println();
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getCanvas(WebDriver driver,WebElement comment) throws Exception {
		
		File screenshotAs = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		BufferedImage bufferedImage = ImageIO.read(screenshotAs);
		
		Point point = comment.getLocation();
		
		int width = comment.getSize().getWidth();
		int height = comment.getSize().getHeight();
		if(width<=0) width=300;
		if(height<=0) height=100;
		BufferedImage subimage = bufferedImage.getSubimage(point.getX()+70, point.getY(), width-70, height);
		
		ImageIO.write(subimage, "png", screenshotAs);
		
		File file = new File("d:\\xcar.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("6137", "d:\\xcar.png");
		
		return code;
	}
	
	@Test
	public void getCode(){
		try {
			TaskGuideBean task = new TaskGuideBean();
			task.setNick("17173425504");
			task.setPassword("chwx090674542");
			task.setNick("17173425505");
			task.setPassword("chwx123456");
			toRegister2(task);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
