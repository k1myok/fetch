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

public class ChinanewsComment {
	public static boolean process = false;
	public static boolean isSend = false;
	public static void main(String[] args) {
		process = true;
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://www.chinanews.com/ty/2018/03-27/8477050.shtml");
		task.setCorpus("加油挺好的");
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
		torun(task);
	}
	public static void torun(TaskGuideBean task){
		System.out.println("切换端口中...");
		int suc = MsgUtil.switchCard(task.getIsApp(),task.getHostPort());
		if(suc==1) toBackrun(task);
		else{
			System.out.println("注册切换卡池失败...");
		}
	}
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
									WebElement content = driver.findElement(By.xpath("//*[@id='SOHU_MAIN']/div[2]/div[3]/div/div[2]/div[1]/div/div[3]/div[2]/div/textarea"));
									content.clear();
									content.click();
									content.sendKeys(task.getCorpus());
									Thread.sleep(1000 * 2);
									WebElement commit = driver.findElement(By.xpath("//*[@id='SOHU_MAIN']/div[2]/div[3]/div/div[2]/div[1]/div/div[4]/div[2]/div/a/button"));
									commit.click();
									Thread.sleep(1000 * 2);
									WebElement name = driver.findElement(By.xpath("//*[@id='SOHU_MAIN']/div[6]/div/div[2]/div[3]/div[3]/input"));
									name.clear();
									name.click();
									name.sendKeys(task.getNick());
									Thread.sleep(1000 * 2);
									try {
										WebElement error = driver.findElement(By.xpath("//*[@id='SOHU_MAIN']/div[6]/div/div[2]/div[3]/div[5]/span[1]"));
										if (error.getText().contains("手机号不正确")) {
											isSuccess(task, error.getText());
											return;
										}
									} catch (Exception e) {
										
									}
									WebElement getWord = driver.findElement(By.xpath("//*[@id='SOHU_MAIN']/div[6]/div/div[2]/div[3]/div[4]/span"));
									getWord.click();
									Thread.sleep(1000 * 2);
									WebElement imgCode = driver.findElement(By.xpath("//*[@id='SOHU_MAIN']/div[7]/div/img"));
									yzmInput(driver,imgCode,1);
									Thread.sleep(1000 * 2);//*[@id="SOHU_MAIN"]/div[7]/div/span[2]
									WebElement sure = driver.findElement(By.xpath("//*[@id='SOHU_MAIN']/div[7]/div/span[2]"));
									sure.click();
									Thread.sleep(1000 * 2);
									try {
										WebElement errorMsg = driver.findElement(By.xpath("//*[@id='SOHU_MAIN']/div[7]/div/span[1]"));
										while (errorMsg.getText().contains("验证码错误")) {
											yzmInput(driver,imgCode,1);
											Thread.sleep(1000 * 2);
											sure.click();
										}
									} catch (Exception e) {
										
									}
									WebElement password = driver.findElement(By.xpath("//*[@id='SOHU_MAIN']/div[6]/div/div[2]/div[3]/div[4]/div/input"));
									password.clear();
									password.click();
									vcode = getChinaNewsCodeMsg(task.getIsApp(),task.getHostPort());
									
									if(vcode==null){
										task.setCode(22);
										isSuccess(task, "收不到验证码");
										return;
									}
									password.sendKeys(vcode);//验证码
									Thread.sleep(1000 * 2);
									WebElement login = driver.findElement(By.xpath("//*[@id='SOHU_MAIN']/div[6]/div/div[2]/div[3]/span"));
									login.click();
									try {
										WebElement error = driver.findElement(By.xpath("//*[@id='SOHU_MAIN']/div[6]/div/div[2]/div[3]/div[5]/span[2]"));
										isSuccess(task, error.getText());
										return;
									} catch (Exception e) {
										
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
		driver.findElement(By.xpath("//*[@id='SOHU_MAIN']/div[7]/div/div[3]/input")).clear();;
		driver.findElement(By.xpath("//*[@id='SOHU_MAIN']/div[7]/div/div[3]/input")).sendKeys(code);
		
	}
	public static String getChinaNewsCodeMsg(int shebei,String port){
//		String ss = "验证码密码为698283";
		int portNumber = Integer.parseInt(port)/1000;
		String msg = MsgUtil.getMsg(shebei,portNumber+"");
		if(msg==null || msg.equals("")) return null;
		int index1 = msg.indexOf("为")+1;
		int index2 = msg.indexOf("，");
		return msg.substring(index1, index2);
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
		File file = new File("c:\\ifeng.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("1040", "c:\\ifeng.png");
		
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