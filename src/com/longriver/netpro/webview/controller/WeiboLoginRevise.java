package com.longriver.netpro.webview.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.longriver.netpro.util.DriverGet;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.MsgUtil;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 微博解封账号登录
 * @author CHWX
 */
public class WeiboLoginRevise {
	public static void main(String[] args) {
		TaskGuideBean task = new TaskGuideBean();
		task.setNick("17173425386");
		registerAccount(null,task);
	}
	/**
	 * 登录
	 * @param driver
	 * @param task
	 * @param number
	 */
	public static void toLogin(WebDriver driver,TaskGuideBean task,int number){
		number++;
		try {
			driver.get("https://login.sina.com.cn/signup/signin.php");  
			
			WebElement username = driver.findElement(By.id("username"));
			username.clear();
			username.sendKeys(task.getNick());
			
			WebElement password = driver.findElement(By.id("password"));
			password.clear();
			password.sendKeys(task.getPassword());
			
			WebElement loginButton = driver.findElement(By.xpath("//*[@id='vForm']/div[2]/div/ul/li[7]/div[1]/input"));
			loginButton.click();
			
			Thread.sleep(4000);
			try{
				WebElement imgCode = driver.findElement(By.id("check_img"));
				System.out.println(imgCode.getSize());
				if(!(imgCode.isDisplayed())){
					System.out.println("无验证码 Element is not displayed!");
					return ;
				};
				String code = getPicCode(driver, imgCode);
				System.out.println("验证码:"+code);
				WebElement codePut = driver.findElement(By.id("door"));
				codePut.clear();
				codePut.sendKeys(code);
				
				Thread.sleep(500);
				WebElement loginButton2 = driver.findElement(By.xpath("//*[@id='vForm']/div[2]/div/ul/li[7]/div[1]/input"));
				loginButton2.click();
				Thread.sleep(2000);
				if(driver.getCurrentUrl().contains("login.sina.com.cn")){
					//用户名密码错误
					WebElement erroruser = driver.findElement(By.xpath("//*[@id='login_err']/span/i[2]"));
					System.out.println("content=="+erroruser.getText());
					if(erroruser.getText().contains("验证码")){
						System.out.println("输入的验证码不正确");
						if(number<4) toLogin(driver,task,number);
					}
				}
			}catch(Exception e){
				System.out.println("无验证码");
				System.out.println("无验证码");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
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
	/**
	 * 密码错误解决方案
	 * @param driver
	 * @param task
	 * @return
	 */
	public static void wrongPwd(WebDriver driver,TaskGuideBean task){
		try {
			if(fateitest(driver)){
				task.setCode(100);
				MQSender.toMQ(task,"");//成功
				System.out.println("测试发帖成功,直接返回");
				return;
			}//发帖不成功再修改密码
			
			driver.get("https://security.weibo.com/iforgot/loginname?entry=sso");
			WebElement username = driver.findElement(By.xpath("//*[@id='loginname']"));
			username.clear();
			username.sendKeys(task.getNick());
			try {
				WebElement vcode = driver.findElement(By.xpath("//*[@id='codeimg']"));
				String code = getPicCode(driver,vcode);
				WebElement codeInput = driver.findElement(By.xpath("//*[@id='code']"));
				codeInput.clear();
				Thread.sleep(1000*2);
				codeInput.sendKeys(code);
			} catch (Exception e) {
				System.out.println("找回密码没有验证码");
			}
			//点击立即验证
			Thread.sleep(1000*1);
			WebElement nowV = driver.findElement(By.xpath("//*[@id='btn']"));
			nowV.click();
			try {
				WebElement error = driver.findElement(By.xpath("//*[@id='codemsg']"));
				if(error.isDisplayed()){
					WebElement vcode2 = driver.findElement(By.xpath("//*[@id='codeimg']"));
					try {
						String code2 = getPicCode(driver,vcode2);
						WebElement codeInput = driver.findElement(By.xpath("//*[@id='code']"));
						codeInput.clear();
						codeInput.sendKeys(code2);
						nowV.click();
					} catch (Exception e) {
						System.out.println("找回密码没有验证码");
					}
				}
			} catch (Exception e1) {
				System.out.println("找回密码没有验证码");
			}
			Thread.sleep(1000*2);
			//下一步
			WebElement next1 = driver.findElement(By.xpath("//*[@id='nextStep']"));
			next1.click();
			Thread.sleep(1000*2);
			
			try {
				WebElement inputtel = driver.findElement(By.xpath("//*[@id='tel']"));
				inputtel.click();
				inputtel.clear();
				inputtel.sendKeys(task.getNick());
				
				WebElement next3 = driver.findElement(By.xpath("//*[@id='btn-submit']"));
				next3.click();
				Thread.sleep(1000*2);
			} catch (Exception e1) {}
			
			WebElement msgcontent = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div/ul/li[1]/p/i[2]"));
			WebElement totel = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div/ul/li[1]/p/i[3]"));
			System.out.println("短信内容:"+msgcontent.getText());
			System.out.println("发送到:"+totel.getText());
			
			int suc = 0;
			suc = MsgUtil.sendMsg(task.getIsApp(),task.getNick(), msgcontent.getText(), totel.getText(),task.getHostPort());
			
			if(suc != 1){
				task.setCode(23);
				MQSender.toMQ(task,"发送短信失败");
				return;
			}
			Thread.sleep(1000*15);
			WebElement next2 = driver.findElement(By.xpath("//*[@id='nextbtn']"));
			next2.click();
			//请先发送短信
			try {
				WebElement error2 = driver.findElement(By.xpath("//*[@id='err_tip']"));
				if(error2.isDisplayed()){
					Thread.sleep(1000*15);
					next2.click();
				}
			} catch (Exception e) {}
			String pwd = "chwx"+StringUtil.randomQq();
			System.out.println("newPassword:"+pwd);
			task.setPassword(pwd);
			Thread.sleep(1000*2);
			WebElement pwd1 = driver.findElement(By.id("pwd1"));
			pwd1.clear();pwd1.sendKeys(pwd);
			WebElement pwd2 = driver.findElement(By.id("pwd2"));
			pwd2.clear();pwd2.sendKeys(pwd);
			Thread.sleep(1000*1);
			WebElement submitbtn = driver.findElement(By.id("submit-btn"));
			submitbtn.click();
			Thread.sleep(1000*5);
			
			task.setCode(100);
			MQSender.toMQ(task,"");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DriverGet.quit(driver);
		}
	}
	public static boolean fateitest(WebDriver driver){
		try {
			WebElement publicComment = driver.findElement(By.xpath("//*[@id='v6_pl_content_publishertop']/div/div[2]/textarea"));
			publicComment.clear();
			publicComment.sendKeys("今天天气很不错啊");
			Thread.sleep(200);
			WebElement commit = driver.findElement(By.xpath("//*[@id='v6_pl_content_publishertop']/div/div[3]/div[1]/a"));
			commit.click();
			Thread.sleep(1000);
			WebElement myy = driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div/div/div[3]/div[1]/ul/li[5]/a/em[2]"));
			myy.click();
			Thread.sleep(1000);
			//获得截图并判断程序
			WebElement commentDiv = null;
			try {
				Thread.sleep(4000);
				commentDiv = driver.findElement(By.xpath("//*[@id='Pl_Official_MyProfileFeed__21']/div/div[2]/div[1]"));
			} catch (Exception e1) {
				Thread.sleep(4000);
				System.out.println("xpath未找到");
				commentDiv = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/div[2]/div[2]/div/div[2]/div[1]"));
			}
			Thread.sleep(200);
			//根据语料 判断是否成功
			if(commentDiv.getText().trim().contains("天气很")){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 检查是否注册
	 * @param driver
	 * @param task
	 * @return 0未注册 1已注册
	 */
	public static String registerAccount(WebDriver driver,TaskGuideBean task){
		try {
			String addr = "https://weibo.com/signup/v5/formcheck?type=mobilesea&zone=0086&value="+task.getNick()+"&from=&__rnd="+new Date().getTime();
			URL u45 = new URL(addr);
	         HttpURLConnection c45 = (HttpURLConnection) u45.openConnection();
	         c45.addRequestProperty("Host", "weibo.com");
	         c45.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
	         c45.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	         c45.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
//	         c45.addRequestProperty("Cookie", "SINAGLOBAL=1177591405104.9563.1524733397439; YF-Ugrow-G0=ad83bc19c1269e709f753b172bddb094; SCF=As11a6MXCMY6nnlnx6cLKGFgjhsZ9P2J6s6dSCZvLewHofVTrbXiufZqtUj4gkeEXrimpg57FlRQhKMlze-VvJw.; SUHB=0k1Co5yIdZ3ZwA; YF-Page-G0=3d55e26bde550ac7b0d32a2ad7d6fa53; YF-V5-G0=cd5d86283b86b0d506628aedd6f8896e; SUB=_2AkMsYKqldcPxrAVVn_ASyWPjbohH-jyftcNTAn7uJhMyAxgv7npQqSVutBF-XDctzjKro41e_tNIT5dCQZgdJ2hI; SUBP=0033WrSXqPxfM72wWs9jqgMF55529P9D9WhVigonbQRGUgmrKzF9ZYpi5JpVF02NeK.XSK20ShB0; login_sid_t=3ed06c1f39c091cf469795364dc422b6; cross_origin_proto=SSL; WBStorage=5548c0baa42e6f3d|undefined; wb_view_log=1600*9001; _s_tentry=login.sina.com.cn; UOR=,,login.sina.com.cn; Apache=6535604454294.049.1530668436567; ULV=1530668436591:10:1:2:6535604454294.049.1530668436567:1530076817939; appkey=");
	         c45.addRequestProperty("Connection", "keep-alive");
	         c45.addRequestProperty("Referer", "https://weibo.com/signup/signup.php");
	         c45.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	         c45.setConnectTimeout(1000 * 30);
	         c45.setReadTimeout(1000 * 20);
	         c45.connect();
	         InputStream i45 = c45.getInputStream();
	         Scanner s45 = new Scanner(i45, "utf-8");
	         // 100000 未注册  600001已经注册
	         while(s45.hasNext()){
	        	 String scsc = s45.nextLine();
	        	 System.out.println(scsc);
	        	 if(scsc.contains("100000")) return "0";
	        	 else return "1";
		     }
	         return "0";
			
//			driver.get("https://weibo.com/signup/signup.php");
//			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
//			
//			WebElement username = driver.findElement(By.xpath("//input[contains(@name,'username')]"));
//			Thread.sleep(1000 * 1);
//			username.click();
//			
//			Thread.sleep(1000 * 1);
//			username.clear();
//			username.sendKeys(task.getNick());
//			username.click();
//			Thread.sleep(1000 * 1);
//			WebElement password = driver.findElement(By.xpath("//input[contains(@name,'passwd')]"));
//			Thread.sleep(1000 * 1);
//			password.click();
//			Thread.sleep(1000*2);
//			WebElement isReg = driver.findElement(By.xpath("//div[contains(@node-type,'mobilesea_tip')]"));
//			if(isReg.getText().contains("已注册")){
//				System.out.println("该手机号已注册");
//				task.setCode(1);
//				return "1";
//			}else{
//				return "0";
//			}
		} catch (Exception e) {
			//没注册
			return "0";
		}
	}
}
