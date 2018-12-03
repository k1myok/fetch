package com.longriver.netpro.webview.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;

import com.longriver.netpro.common.sina.WeiboSina;
import com.longriver.netpro.util.Configur;
import com.longriver.netpro.util.DateUtil;
import com.longriver.netpro.util.DriverGet;
import com.longriver.netpro.util.GetProprities;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.MsgUtil;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 网易注册
 * @author lilei
 * @2018-01-24 10:10:04
 * @version v1.0
 */
public class WyRegister {
	public static boolean process = false;
	public static boolean isSend = false;
	public static boolean towTimes = true;
	public static void main(String[] args) {
		process = true;
		TaskGuideBean task = new TaskGuideBean();
		task.setPassword("chwx123456");
//		task.setTestAccount("");
//		task.setNick("17173425505");//m17173425505@163.com
//		task.setHostPort("5001");
		task.setNick("17173425501");//"nick":"m17173425501@163.com注册成功！"
		task.setHostPort("8001");
//		task.setNick("17173425503");//m17173425503@163.com
//		task.setHostPort("6001");
		torun(task);
	}

	public static void torun(TaskGuideBean task){
		System.out.println("切换端口中...");
		int suc = MsgUtil.switchCard(task.getIsApp(),task.getHostPort());
		if(suc==1) toBackrun(task);
		else{
			System.out.println("注册切换卡池失败...");
			System.out.println("注册切换卡池失败...");
		}
	}
	public static void toBackrun(TaskGuideBean task){
		isSend = true;
		towTimes = true;
		toRegister(task);
	}
	public static void toRegister(TaskGuideBean task) {
		WebDriver driver = null;
		String vcode = null;
		task.setCode(10);
		try{
			Configur config = GetProprities.paramsConfig;
			String firefoxUrl = config.getProperty("firefoxurl");
			
			FirefoxProfile profile = new FirefoxProfile();
			//禁用css
//			profile.setPreference("permissions.default.stylesheet", 2);
			//不加载图片
//			profile.setPreference("permissions.default.image", 2);
			//##禁用Flash 
//			profile.setPreference("dom.ipc.plugins.enabled.libflashplayer.so","false");
			
			System.setProperty("webdriver.firefox.bin",firefoxUrl);
			driver = new FirefoxDriver(profile); 
			driver.get("http://reg.email.163.com/unireg/call.do?cmd=register.entrance&from=163navi&regPage=163");
			driver.manage().timeouts().implicitlyWait(180, TimeUnit.SECONDS);
			//注册手机邮箱
			WebElement telBtn = driver.findElement(By.xpath("//*[@id='tabsUl']/li[2]/a"));
			Thread.sleep(1000 * 1);
			telBtn.click();
			//输入手机号
			WebElement telInput = driver.findElement(By.xpath("//*[@id='mobileIpt']"));
			telInput.clear();
			telInput.sendKeys(task.getNick());
			Thread.sleep(1000 * 1);
			//验证码
			WebElement imgCode = driver.findElement(By.xpath("//*[@id='mVcodeImg']"));
			String code = getPicCode(driver, imgCode);
			System.out.println("验证码:"+code);
			WebElement codeInput = driver.findElement(By.xpath("//*[@id='mVcodeIpt']"));
			codeInput.clear();
			codeInput.sendKeys(code);
			Thread.sleep(1000 * 1);
			//获得短信
			WebElement getTelBtn = driver.findElement(By.xpath("//*[@id='sendAcodeStg']"));
			getTelBtn.click();
			Thread.sleep(1000 * 2);
			//判断前面是否已经注册
			WebElement sendMsgIsShow = driver.findElement(By.xpath("//*[@id='acodeSentSpan']"));
			if(!sendMsgIsShow.isDisplayed()){
				try {
					WebElement errTip = driver.findElement(By.xpath("//*[@id='m_mobile']/span"));
					System.out.println(errTip.getText());
					if(errTip.getText().contains("已被激活")){
						System.out.println("已经注册");
						task.setCode(100);
						isSuccess(task, "");
						return ;
					};
				} catch (Exception e) {}
			}
			vcode = MsgUtil.getWyCodeMsg(task.getIsApp(),task.getHostPort());
			if(vcode==null){
				task.setCode(22);
				isSuccess(task, "收不到验证码");
				return;
			}
			//短信验证码code
			WebElement telCodeInput = driver.findElement(By.xpath("//*[@id='acodeIpt']"));
			telCodeInput.clear();
			telCodeInput.sendKeys(vcode);
			Thread.sleep(1000 * 1);
			//输入密码
			WebElement pwdInput = driver.findElement(By.xpath("//*[@id='mobilePwdIpt']"));
			pwdInput.clear();
			pwdInput.sendKeys(task.getPassword());
			Thread.sleep(1000 * 1);
			WebElement pwdInput2 = driver.findElement(By.xpath("//*[@id='mobileCfmPwdIpt']"));
			pwdInput2.clear();
			pwdInput2.sendKeys(task.getPassword());
			Thread.sleep(1000 * 1);
			//点击注册
			WebElement registerBtn = driver.findElement(By.xpath("//*[@id='mobileRegA']"));
			registerBtn.click();
			Thread.sleep(1000 * 10);
			WebElement nickText = driver.findElements(By.xpath("//span[contains(@class,'u-uid')]")).get(0);
			System.out.println("content=="+nickText.getText());//m17173425501@163.com注册成功！
			String nick = nickText.getText().substring(0, nickText.getText().indexOf(".com")+4);
			task.setNick(nick);
			task.setCode(100);
			isSuccess(task, "");//成功
		}catch(Exception e){
			System.out.println("注册异常");
			task.setCode(10);
			isSuccess(task, "注册异常");
			e.printStackTrace();
		}finally{
			DriverGet.quit(driver);
		}
		System.out.println("结束------------------");
		System.out.println("结束------------------");
	}
	public static String getMsgContent(String content,int num){
		//编辑短信内容 794914 至 1069 0090 166 即可成功注册微博
		try {
			int ind1 = content.indexOf("至");
			if(num==1)
				return content.substring(7, ind1-1).trim();
			else
				return content.substring(ind1+2,content.length()-9).replaceAll(" ", "");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
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
		
		File file = new File("c:\\wyReg.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("3000", "c:\\wyReg.png");
		
		return code;
	}
	/**
	 * 判断是否成功
	 */
	public static void isSuccess(TaskGuideBean task,String msg){
		if(isSend){
			isSend = false;
			MQSender.toMQ(task,msg);
		}
	}
	
}
