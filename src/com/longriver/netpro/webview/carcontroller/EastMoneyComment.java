package com.longriver.netpro.webview.carcontroller;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.longriver.netpro.util.Configur;
import com.longriver.netpro.util.FileUtil;
import com.longriver.netpro.util.GetProprities;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.MsgUtil;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

public class EastMoneyComment {
	public static boolean process = false;
	public static boolean isSend = false;
	public static void main(String[] args) {
		process = true;
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://stock.eastmoney.com/news/1411,20180329850671912.html");
		task.setCorpus("加油的");
		task.setNick("17173425387");
		task.setPassword("chwx1234");
		task.setHostPort("5015");
		toComment(task);
	}
	/**
	 * 评论
	 * @param task
	 */
	public static void toComment(TaskGuideBean task) {
		
		System.out.println("toComment");
		//先清空存放截图的文件夹
		FileUtil.deleteDirectoryFiles();
		//保证只返回一次结果
		isSend = true;
		toBackrun(task);
	}
//	public static void torun(TaskGuideBean task){
//		System.out.println("切换端口中...");
//		int suc = MsgUtil.switchCard(task.getHostPort());
//		if(suc==1) toBackrun(task);
//		else{
//			System.out.println("注册切换卡池失败...");
//		}
//	}
	public static void toBackrun(TaskGuideBean task) {
		String vcode = null;
		WebDriver driver = null;
						String address = task.getAddress();
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
								driver.get(task.getAddress());
								
								try {
									WebElement content = driver.findElement(By.xpath("//*[@id='newsComment']/div[1]/div[2]/div[1]/div[2]/textarea"));
									content.clear();
									content.click();
									content.sendKeys(task.getCorpus());
									Thread.sleep(1000 * 2);//*[@id="sign_in_modal"]/div[1]/div/iframe
									WebElement gologin = driver.findElement(By.xpath("//*[@id='sign_in_modal']/div[1]/div/iframe"));
									driver.switchTo().frame(gologin);
									WebElement name = driver.findElement(By.id("txt_account")); 
									name.clear(); 
									name.click();
									name.sendKeys(task.getNick());
									Thread.sleep(1000 * 2);
									WebElement passWord = driver.findElement(By.id("txt_pwd"));
									passWord.clear();
									passWord.click();
									passWord.sendKeys(task.getPassword());
									Thread.sleep(1000 * 2);
									WebElement dologin = driver.findElement(By.id("btn_login"));
									dologin.click();
									try {
//										WebElement ogin = driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/iframe"));
//										driver.switchTo().frame(gologin);
										WebElement image = driver.findElement(By.id("img_vcode"));
						 				yzmInput(driver, image, 1);
										dologin.click();
										try {
											WebElement mobileError = driver.findElement(By.id("msg_account"));
											while (mobileError.getText().contains("请输入正确的验证码")) {
												yzmInput(driver, image, 1);
												dologin.click();
												Thread.sleep(1000 * 2);
												if (mobileError.getText().contains("请稍后重试")) {
													isSuccess(task,mobileError.getText());
													return;
												}
											}
												
											
										} catch (Exception e1) {
											
										}
									} catch (Exception e) {
										
									}
									try {
						        				WebElement mobileError = driver.findElement(By.id("msg_account"));
											isSuccess(task,mobileError.getText());
											return;
										
									} catch (Exception e1) {
										
									}
									
									
									
									isSuccess(task, "");
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									isSuccess(task, "评论异常");
									e.printStackTrace();
								}finally{
									
									driver.quit();
								}
	}
	public static void yzmInput(WebDriver driver,WebElement imgCode,int yzmcode){
		String code = getPic(driver, imgCode);//*[@id="SOHU_MAIN"]/div[7]/div/div[3]/input
		driver.findElement(By.id("txt_vcode")).clear();
		driver.findElement(By.id("txt_vcode")).sendKeys(code);
		
	}
	/**
	 * 获取验证码
	 * @param driver
	 */
	private static String getPic(WebDriver driver,WebElement comment) {
		
		try{
		File screenshotAs = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		BufferedImage bufferedImage = ImageIO.read(screenshotAs); 
		
		Point point = comment.getLocation();
		
		int width = comment.getSize().getWidth();
		int height = comment.getSize().getHeight();
		if(width<=0) width=300;
		if(height<=0) height=100;
		BufferedImage subimage = bufferedImage.getSubimage(point.getX(), point.getY(), width, height);
		
		ImageIO.write(subimage, "png", screenshotAs);
		
//		String picName = getPicName();
		File file = new File("d:\\ifeng.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("3050", "d:\\ifeng.png");
		
		return code;
		}catch(Exception e){
			
		}
		return null;
	}
	/**
	 * 判断是否成功
	 */
	public static void isSuccess(TaskGuideBean task,String msg){
		
		MQSender.toMQ(task,msg);
	}
}