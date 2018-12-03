package com.longriver.netpro.webview.carcontroller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.longriver.netpro.fetchScript.util.Jdbc2MysqlSpcard;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.MsgUtil;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 猫眼注册
 * @author rhy
 * @date 2018-3-12 下午5:42:03
 * @version V1.0
 */
public class MaoyanRegister {

	private static int codetimes = 0;
	public static void main(String[] args) {
		
		//手机号账号,前面需加86
		TaskGuideBean task = new TaskGuideBean();
		task.setNick("17173425504");
		task.setPassword("chwx123456");
//		task.setNick("17173425505");
//		task.setPassword("chwx123456");
		task.setNick("17173425487");
		task.setPassword("chwx123456");
		task.setHostPort("4003");
		toRegister(task);
	}

	/**
	 * 猫眼注册
	 * @param task
	 */
	public static void toRegister(TaskGuideBean task) {
		
		WebDriver driver = null;
		try {
			driver = getDriver();
			
			if(driver == null){
				
				isSuccess(task, "driver打开失败");
				return;
			}
			
			driver.get("http://bj.meituan.com/");
//			driver.get("https://passport.meituan.com/account/unitivesignup");
			driver.findElement(By.xpath("//*[@id='main']/header/div[1]/div/div/div[2]/a[2]")).click();
			
			Thread.sleep(5000);
			driver.quit();
			
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
			
			driver.findElement(By.name("mobile")).sendKeys(task.getNick());
			Thread.sleep(500);
			try {
				if(StringUtils.isNotBlank(driver.findElement(By.xpath("/html/body/div/div[1]/div/form/div[1]/span[2]")).getText())){
					
					isSuccess(task, driver.findElement(By.xpath("/html/body/div/div[1]/div/form/div[1]/span[2]")).getText());
					return;
				}
			} catch (Exception e1) {
			}
			driver.findElement(By.xpath("/html/body/div/div[1]/div/form/div[2]/div[2]/input")).click();
			Thread.sleep(500);
			driver.findElement(By.xpath("/html/body/div/div[1]/div/form/div[2]/div[2]/input")).click();
			Thread.sleep(500);
			
			try {
				if(StringUtils.isNotBlank(driver.findElement(By.xpath("/html/body/div/div[1]/div/form/div[1]/span[2]")).getText()) && driver.findElement(By.xpath("/html/body/div/div[1]/div/form/div[1]/span[2]")).getText().contains("该手机号已被绑定")){
					
					task.setCode(100);
					isSuccess(task, "");
					return;
				}
			} catch (Exception e2) {
			}
			try {
				if(StringUtils.isNotBlank(driver.findElement(By.id("J-verify-tip")).getText()) && driver.findElement(By.id("J-verify-tip")).getText().contains("动态码获取次数过多")){
					
					isSuccess(task, driver.findElement(By.id("J-verify-tip")).getText());
					return;
				}
			} catch (Exception e1) {
			}
			try {
				WebElement captchaImg = driver.findElement(By.id("signup-captcha-mobile-img"));
				String picCode = getPicCode(driver, captchaImg);
				driver.findElement(By.id("captcha-mobile")).sendKeys(picCode);
				
				driver.findElement(By.xpath("/html/body/div/div[1]/div/form/div[2]/div[2]/input")).click();
				Thread.sleep(500);
				try {
					if(StringUtils.isNotBlank(driver.findElement(By.id("J-verify-tip")).getText()) && "验证码错误".equals(driver.findElement(By.id("J-verify-tip")).getText())){
						
						if(codetimes<4){
							
							codetimes++;
							driver.quit();
							toRegister(task);
						}else{
							
							isSuccess(task, "验证码错误");
							return;
						}
					}else{
						
						if(!driver.findElement(By.id("J-verify-tip")).getText().contains("已发送")){
							isSuccess(task, driver.findElement(By.id("J-verify-tip")).getText());
							return;
						}
					}
				} catch (Exception e) {
				}
			} catch (Exception e) {
			}
			
			
			int times = 0;
			String msgCode = null;
//			Thread.sleep(30000);
			while(true){
				
				Thread.sleep(5000);
				times++;
				if(times<8){
				
				String msg = Jdbc2MysqlSpcard.getResultHis(port);
				
				msgCode = getMsgCode(msg);
				if(StringUtils.isNotBlank(msgCode)){
					break;
				}
				}else{
						
						break;
					
//					try {
//						if(StringUtils.isBlank(msgCode)){
//							
//							driver.findElement(By.xpath("/html/body/div/div[1]/div/form/div[2]/div[2]/input")).click();
//							WebElement captchaImg = driver.findElement(By.id("signup-captcha-mobile-img"));
//							String picCode = getPicCode(driver, captchaImg);
//							driver.findElement(By.id("captcha-mobile")).sendKeys(picCode);
//							
//							driver.findElement(By.xpath("/html/body/div/div[1]/div/form/div[2]/div[2]/input")).click();
//							
//							try {
//								if(StringUtils.isNotBlank(driver.findElement(By.id("J-verify-tip")).getText()) && "验证码错误".equals(driver.findElement(By.id("J-verify-tip")).getText())){
//									
//									if(codetimes<4){
//										
//										codetimes++;
//										driver.quit();
//										toRegister(task);
//									}else{
//										
//										isSuccess(task, "验证码错误");
//										return;
//									}
//								}else{
//									
//									if(!driver.findElement(By.id("J-verify-tip")).getText().contains("已发送")){
//										isSuccess(task, driver.findElement(By.id("J-verify-tip")).getText());
//										return;
//									}
//								}
//							} catch (Exception e) {
//							}
//							Thread.sleep(30000);
//							String msg = Jdbc2MysqlSpcard.getResultHis(port);
//							
//							msgCode = getMsgCode(msg);
//							if(StringUtils.isNotBlank(msgCode)){
//								break;
//							}
//						}else{
							
//							break;
//						}
//					} catch (Exception e) {
//					}
				}
			}
			
			if(StringUtils.isBlank(msgCode)){
				
				isSuccess(task, "没有收到短信");
				return;
			}
			driver.findElement(By.name("verifycode")).sendKeys(msgCode);
			Thread.sleep(500);
			try {
				if(StringUtils.isNotBlank(driver.findElement(By.xpath("/html/body/div/div[1]/div/form/div[3]/span")).getText())){
					
					isSuccess(task, driver.findElement(By.xpath("/html/body/div/div[1]/div/form/div[3]/span")).getText());
					return;
				}
			} catch (Exception e) {
			}
			driver.findElement(By.name("password")).sendKeys(task.getPassword());
			Thread.sleep(500);
			try {
				if(StringUtils.isNotBlank(driver.findElement(By.xpath("/html/body/div/div[1]/div/form/div[4]/span")).getText())){
					
					isSuccess(task, driver.findElement(By.xpath("/html/body/div/div[1]/div/form/div[4]/span")).getText());
					return;
				}
			} catch (Exception e) {
			}
		
			driver.findElement(By.name("password2")).sendKeys(task.getPassword());
			Thread.sleep(500);
			try {
				if(StringUtils.isNotBlank(driver.findElement(By.xpath("/html/body/div/div[1]/div/form/div[5]/span")).getText())){
					
					isSuccess(task, driver.findElement(By.xpath("/html/body/div/div[1]/div/form/div[5]/span")).getText());
					return;
				}
			} catch (Exception e) {
			}
			
			driver.findElement(By.name("commit")).click();
//			http://bj.meituan.com/
			isSuccess(task, "");
		} catch (Exception e) {
			
			isSuccess(task, "注册失败");
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
	private static String getMsgCode(String msg) {
		
		try {
			String msgCode = msg.substring(msg.indexOf("【美团网】")+5,msg.indexOf("注册验证码")-1).trim();
			return msgCode;
		} catch (Exception e) {

			return null;
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
