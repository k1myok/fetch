package com.longriver.netpro.webview.controller;

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
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import com.longriver.netpro.fetchScript.util.Jdbc2MysqlMyMQ;
import com.longriver.netpro.fetchScript.util.Jdbc2MysqlSpcard;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.MsgUtil;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 豆瓣注册
 * @author rhy
 * @2018-2-24 下午4:18:43
 * @version v1.0
 */
public class DoubanRegister {


	private static int codetimes = 0;
	public static void main(String[] args) {
		TaskGuideBean task = new TaskGuideBean();
		task.setNick("17173425344");
		task.setPassword("chwx123456&chwx123456"); //&前面豆瓣账号，&后面微博账号
		task.setHostPort("4020");

		
		toRegister(task);
	}

	/**
	 * 豆瓣注册
	 * @param task
	 */
	public static void toRegister(TaskGuideBean task) {
		
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
			
			driver.get("https://accounts.douban.com/login");

			try {
				driver.findElement(By.xpath("//*[@id='lzform']/div[7]/a[2]/img")).click();
			} catch (Exception e4) {
				driver.findElement(By.xpath("//*[@id='lzform']/div[8]/a[2]/img")).click();
			}
			driver.findElement(By.xpath("//*[@id='lzform']/div[7]/a[2]/img")).click();
			
			String[] password = task.getPassword().split("&");
			
			task.getHostPort().substring(0,2);
			task.setPassword(password[0]);
			
			String port = "";
			if(task.getHostPort().length()==4){
				port = task.getHostPort().substring(0, 1);
			}else{
				port = task.getHostPort().substring(0, 2);
			}

			int switchCard = MsgUtil.switchCard(task.getIsApp(),task.getHostPort().substring(2));
			
			if(switchCard==0){
				
				isSuccess(task, "卡池换卡失败");
				return;
			}

//			Thread.sleep(2000);
			int times = 0;
			if(driver.getCurrentUrl().contains("accounts.douban.com/phone/bind")){//绑定手机号
				
				driver.findElement(By.id("mobile-num")).sendKeys(task.getNick());
				
				driver.findElement(By.id("get-code")).click();
				Thread.sleep(5000);

				driver.findElement(By.id("password")).sendKeys(password[0]);
				
				driver.findElement(By.xpath("//*[@id='post-code']/div[3]/input")).sendKeys(password[0]);
				
//				int id = Jdbc2MysqlMyMQ.addTelMsg(task.getNick(),task.getHostPort());
				Thread.sleep(20000);
				while(true){
					
//					String msg = Jdbc2MysqlMyMQ.getResult(id);
					
					String msg = Jdbc2MysqlSpcard.getResultHis(port);
					Thread.sleep(5000);
					times++;
					if(times>=12){
						break;
					}
					if(StringUtils.isNotBlank(msg)){
						
						String code = getDoubanCode(msg);
						
						if(StringUtils.isNotBlank(code) && code.length()==4){
							driver.findElement(By.xpath("//*[@id='post-code']/div[4]/input")).sendKeys(code);
							driver.findElement(By.xpath("//*[@id='post-code']/div[5]/input[1]")).click();
							Thread.sleep(10000);
							
							try {
								String tipsError = driver.findElement(By.xpath("//*[@id='tips-error']/em")).getText();
								
								if(StringUtils.isNotBlank(tipsError)){
									
//									task.setCode(1);
									isSuccess(task, tipsError);
									return;
								}
							} catch (Exception e) {
							}
							
							String text = driver.findElement(By.xpath("/html/body/div[2]/h1")).getText();
							
							if(StringUtils.isNotBlank(text)){
								
								task.setCode(100);
								isSuccess(task,"");
								return;
							}else{
								isSuccess(task,text);
								return;
							}
						}
						
					}
					
				}
			}else{
				
				driver.findElement(By.id("userId")).sendKeys(task.getNick());
				driver.findElement(By.id("passwd")).sendKeys(password[1]);
				
				try {
					WebElement vcode = driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/span/img"));
					
					String picCode = getPicCode(driver, vcode);
					
					driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/input")).sendKeys(picCode);
					
					driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[2]/div/p/a[1]")).click();
					
					Thread.sleep(5000);
					
					try {
						if(StringUtils.isNotBlank(driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText())){
							

							if(driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText().contains("输入的验证码不正确")){
								
								
								if(codetimes < 4){
									
									driver.quit();
									codetimes++;
									toRegister(task);
								}
								
							}else{
								
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
					Thread.sleep(5000);

					
					try {
						if(StringUtils.isNotBlank(driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText())){
							

							if(driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText().contains("输入的验证码不正确")){
								
								
								if(codetimes < 4){
									
									driver.quit();
									codetimes++;
									toRegister(task);
								}
//									task.setCode(6);
							}else{
								
							isSuccess(task, driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText());
							return;
							}

						}
					} catch (Exception e) {
					}
				} catch (Exception e3) {
				}
//				driver.findElement(By.className("WB_btn_login")).sendKeys(Keys.ENTER);
//				
//				WebElement tipArrow = driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]"));
//				if(StringUtils.isNotBlank(tipArrow.getText())){
//					
//					isSuccess(task, "微博"+tipArrow.getText());
//					return;
//				}
				
				
				String currentUrl = driver.getCurrentUrl();

				if(currentUrl.contains("www.douban.com") || currentUrl.contains("accounts.douban.com/accounts/safety/locked")){//https://www.douban.com/

					
					task.setCode(100);
					isSuccess(task, "");
					return;
				}
				try {
					WebElement vcode = driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/span/img"));
					
					String picCode = getPicCode(driver, vcode);
					
					driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/input")).sendKeys(picCode);
					
					driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[2]/div/p/a[1]")).click();
					
					Thread.sleep(10000);
					try {
						if(StringUtils.isNotBlank(driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText())){

							if(driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText().contains("输入的验证码不正确")){
								
								if(codetimes < 4){
									
									driver.quit();
									codetimes++;
									toRegister(task);
								}
								
							}else{
								
								isSuccess(task, driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText());
								return;
								}
//							task.setCode(6);
							

							
//							task.setCode(6);
							isSuccess(task, driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText());
							return;

						};
					} catch (Exception e) {
					}
					
				} catch (Exception e2) {
				}
				
				
				try {
					String errorcontent = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[1]/dl/dt[1]")).getText();
					if(StringUtils.isNotBlank(errorcontent)){
						
//						task.setCode(1);
						isSuccess(task, errorcontent + driver.findElement(By.xpath("/html/body/div/div[2]/div/div[1]/dl/dt[3]")).getText());
						return;
					}
				} catch (Exception e1) {
				}
				
				if(driver.getCurrentUrl().contains("api.weibo.com/oauth2/authorize")){
					
					if(driver.findElement(By.className("WB_btn_link")).isEnabled()){
					driver.findElement(By.className("WB_btn_link")).click();
					Thread.sleep(10000);
					}
				}
				
				driver.findElement(By.id("mobile-num")).sendKeys(task.getNick());
				
				driver.findElement(By.id("get-code")).click();
				
				Thread.sleep(3000);
				driver.findElement(By.id("password")).sendKeys(password[0]);
				
				driver.findElement(By.xpath("//*[@id='post-code']/div[3]/input")).sendKeys(password[0]);
				
//				int id = Jdbc2MysqlMyMQ.addTelMsg(task.getNick(),task.getHostPort());
				
				Thread.sleep(20000);
				while(true){
					
					String msg = Jdbc2MysqlSpcard.getResultHis(port);
					Thread.sleep(5000);
					times++;
					if(times>=12){
						break;
					}
					if(StringUtils.isNotBlank(msg)){
						
						String code = getDoubanCode(msg);
						
						if(StringUtils.isNotBlank(code) && code.length()==4){
							driver.findElement(By.xpath("//*[@id='post-code']/div[4]/input")).sendKeys(code);
							driver.findElement(By.xpath("//*[@id='post-code']/div[5]/input[1]")).click();
							Thread.sleep(5000);
							
							try {
								String tipsError = driver.findElement(By.xpath("//*[@id='tips-error']/em")).getText();
								
								if(StringUtils.isNotBlank(tipsError)){
									
									isSuccess(task, tipsError);
									return;
								}
							} catch (Exception e) {
							}
							String text = driver.findElement(By.xpath("/html/body/div[2]/h1")).getText();
							
							if(StringUtils.isNotBlank(text)){
								
								task.setCode(100);
								isSuccess(task,"");
								return;
							}else{
								isSuccess(task,text);
								return;
							}
						}
						
					}
					
				}
			}
		} catch (Exception e) {
			
//			task.setCode(8);
			isSuccess(task, "注册有异常");
			e.printStackTrace();
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
		
		File file = new File("c:\\sina.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("3000", "c:\\sina.png");
		
		return code;
	}
	private static String getDoubanCode(String msg) {
		
		String code = "";
		if(msg.indexOf("豆瓣绑定手机验证码")>-1){
			code = msg.substring(msg.indexOf("手机验证码")+5,msg.indexOf("该验证码")).replace("：", "").replace("，", "").trim();
		}
		return code;
	}
	


	/**
	 * 判断是否成功
	 */
	public static void isSuccess(TaskGuideBean task,String msg){
			MQSender.toMQ(task,msg);
	}
	
}
