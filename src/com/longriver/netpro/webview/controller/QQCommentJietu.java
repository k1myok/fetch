package com.longriver.netpro.webview.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.longriver.netpro.util.Configur;
import com.longriver.netpro.util.DriverGet;
import com.longriver.netpro.util.FileUtil;
import com.longriver.netpro.util.GetProprities;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.PngErjinzhi;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 腾讯新闻评论
 * @author rhy
 * @2017-11-29 下午3:04:02
 * @version v1.0
 */
public class QQCommentJietu {

	private static Logger logger = Logger.getLogger(QQCommentJietu.class);
	public static boolean isSend = false;
	
	public static void main(String[] args) {
		
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("https://new.qq.com/cmsn/20180928/NEW2018092800910000");
		task.setAddress("https://new.qq.com/omn/20181113/20181113A0IV3Y.html");
		task.setAddress("http://bj.jjj.qq.com/a/20181114/002675.htm");

		task.setNick("2598532239");
		task.setPassword("4211432a");
		task.setCorpus("果然厉害!!!");
		task.setNick("502023904");
		task.setPassword("lilei516688");
//		task.setNick("2598532239");
//		task.setPassword("4211432a");
		task.setStatus2(1);//要截图
		toComment(task);
		
	}

	/**
	 * 新浪新闻评论
	 * @param task
	 */
	public static void toComment(TaskGuideBean task) {
		System.out.println("toComment");
		//先清空存放截图的文件夹
		FileUtil.deleteDirectoryFiles();
		//保证只返回一次结果
		isSend = true;
		WebDriver driver = DriverGet.getDriver();
		try{
			System.out.println("2222222222");
			String address = task.getAddress();
//			
//			driver.get("http://ui.ptlogin2.qq.com/cgi-bin/login?hide_title_bar=0&low_login=0&qlogin_auto_login=1&no_verifyimg=1&link_target=blank&appid=636014201&target=self&s_url=http%3A//www.qq.com/qq2012/loginSuccess.htm");  
//			
//			String commentUrl = "";
//			try {
//				commentUrl = getCommentUrl(address);
//			} catch (Exception e2) { 
//				address = "https://xw.qq.com/cmsid/"+address.substring(address.lastIndexOf("/")+1);
//				commentUrl =  getCommentUrl(address);
//			}
			
			try {
				//加载时间最多为: 6s
				driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
				driver.get(address);
				Thread.sleep(3000);
			} catch (Exception e1) {}
			((JavascriptExecutor)driver).executeScript("var q=document.documentElement.scrollTop=1000");
			Thread.sleep(1000);
			((JavascriptExecutor)driver).executeScript("document.documentElement.scrollTop=2500");
			Thread.sleep(3000);
			
			driver = driver.switchTo().frame(driver.findElement(By.id("commentIframe")));
			//登录
			driver.findElement(By.xpath("//*[@id='J_Post']/div[2]/span")).click();
			driver.switchTo().activeElement();
			boolean succ = Login(driver,task);
			if(!succ) return;
			Thread.sleep(3000);
			((JavascriptExecutor)driver).executeScript("var q=document.documentElement.scrollTop=1000");
			Thread.sleep(1000);
			((JavascriptExecutor)driver).executeScript("document.documentElement.scrollTop=2500");
			Thread.sleep(3000);
			
			driver = driver.switchTo().frame(driver.findElement(By.id("commentIframe")));
			
			WebElement textArea = driver.findElement(By.id("J_Textarea"));
			textArea.clear();
			textArea.sendKeys(task.getCorpus());
			Thread.sleep(1000);
			driver.findElement(By.id("J_PostBtn")).click();
			
			Thread.sleep(3000);
//			driver.manage().window().maximize();
//			try {
//				//加载时间最多为: 6s
//				driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
//				driver.get(commentUrl);
//			} catch (Exception e1) {}
////			driver.navigate().refresh();//刷新一次
//			WebDriver commentDriver = driver.switchTo().frame(driver.findElement(By.id("commentIframe")));
//			WebElement textArea = commentDriver.findElement(By.id("J_Textarea"));
//			textArea.clear();
//			textArea.sendKeys(task.getCorpus());
//			
//			commentDriver.findElement(By.id("J_PostBtn")).click();
//			
//			Thread.sleep(3000);
//			driver.manage().window().maximize();
//			if(task.getStatus2()==1){
//				WebElement content = commentDriver.findElement(By.xpath("//*[@id='J_ShortComment']/div[1]"));
//				//根据语料 判断是否成功
//				String tt = task.getCorpus().length()>1?task.getCorpus().substring(1):task.getCorpus();
//				String cor = tt.length()>6?tt.substring(0, 6):tt;
//				System.out.println("text=="+content.getText());
//				System.out.println("corpus=="+cor);
//				System.out.println("contains=="+content.getText().contains(cor));
//				if(content.getText().contains(cor)){
//					System.out.println("截图发帖成功!");
//					if(task.getStatus2()==1){
//						try {
//							String picUri = getPic(driver,content);
//							task.setPng(PngErjinzhi.getImageBinary(picUri));
//						} catch (Exception e) {
//							System.out.println("发帖成功截图失败!");
//						}
//					}
//					task.setCode(100);
//					isSuccess(task, "");
//				}else{
//					System.out.println("截图发帖失败!");
//					task.setCode(100);
//					isSuccess(task, "");
//				}
//				
//			}
		}catch(Exception e){
			logger.info("评论异常");
			task.setCode(102);
			isSuccess(task, "评论异常");
			e.printStackTrace();
		}finally{
			isSuccess(task, "");
			DriverGet.quit(driver);
		}
	}
	public static boolean Login(WebDriver driver,TaskGuideBean task){
		try {
			Thread.sleep(5000);
			{
				//动态设置iframe的高度
				JavascriptExecutor jj= (JavascriptExecutor)driver;
				jj.executeScript("document.getElementById('ptlogin_iframe').height='322px'");
			}
			driver = driver.switchTo().frame(driver.findElement(By.name("ptlogin_iframe")));
			
			WebElement login = driver.findElement(By.xpath("//*[@id='switcher_plogin']"));
			login.click();
			//由于隐藏看不到,用js操作
			JavascriptExecutor web= (JavascriptExecutor)driver;
			web.executeScript("document.getElementById('u').value='"+task.getNick()+"'");
			Thread.sleep(1000);
			web.executeScript("document.getElementById('p').value='"+task.getPassword()+"'");
			Thread.sleep(1000);
			web.executeScript("document.getElementById('login_button').click()");
			Thread.sleep(2000);
			if(driver.getCurrentUrl().contains("graph.qq.com")){
				driver.switchTo().parentFrame();
				JavascriptExecutor jj= (JavascriptExecutor)driver;
				jj.executeScript("document.getElementById('ptlogin_iframe').height='322px'");
				Thread.sleep(3000);
				driver = driver.switchTo().frame(driver.findElement(By.name("ptlogin_iframe")));
				WebElement loginButton = driver.findElement(By.id("login_button"));
				Thread.sleep(500);
				loginButton.click();
			}
//			WebElement username = driver.findElement(By.id("u"));
//			username.clear();
//			username.sendKeys(task.getNick());
			
//			WebElement password = driver.findElement(By.id("p"));
//			password.clear();
//			password.sendKeys(task.getPassword());
			
//			Thread.sleep(1000);
//			WebElement loginButton = driver.findElement(By.id("login_button"));
//			Thread.sleep(500);
//			loginButton.click();
			
			Thread.sleep(3500);
			driver.navigate().refresh();
			String error = "";
			try {
				error = driver.findElement(By.id("err_m")).getText();
			} catch (Exception e2) {}
			Thread.sleep(3000);
			String currentUrl = driver.getCurrentUrl();

			if(currentUrl.contains("cgi-bin/login?")){
				if(error.contains("密码不正确")){
					task.setCode(101);
					isSuccess(task, error);
				}else if(error.contains("网络繁忙")){
					task.setCode(102);
					isSuccess(task, error);
				}else{
					task.setCode(102);
					isSuccess(task, error+"错误");
				}
				return false;
			}
			
			if(currentUrl != null && currentUrl.contains("graph.qq.com")){
				
				isSuccess(task, "登录失败");
				driver.close();
				driver.quit();
				return false;
			}
			if(currentUrl.contains("no_verifyimg")){
				task.setCode(101);
				isSuccess(task, "账号需认证");
				return false;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 获取评论id
	 * @param pageSource
	 * @return
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @throws FailingHttpStatusCodeException 
	 */
	private static String getCommentUrl(String address) 
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		
		Document doc2 =Jsoup.connect(address).get();

		String result = doc2.toString();
		String targetId = "";
		if(address.contains("xw.qq.com")){
			
			result = result.substring(result.indexOf("cid")+3);
			targetId = result.substring(0,result.indexOf(",")).replaceAll(":", "").replaceAll("\"", "").trim();
		}else{
			int i1 = result.indexOf("cmt_id");
			int i2 = result.indexOf("comment_id");
			if(i1 != -1){
				result = result.substring(result.indexOf("cmt_id"));
				targetId = result.substring(result.indexOf("cmt_id")+6,result.indexOf(";")).replace("=", "").replaceAll("\"", "").trim();
			}else{
				result = result.substring(result.indexOf("comment_id"));
				targetId = result.substring(result.indexOf("comment_id")+10,result.indexOf(",")).replace(":", "").replaceAll("\"", "").trim();
			}
		}
		String commentId = "http://coral.qq.com/"+targetId;
		System.out.println(commentId);
		return commentId;
	}


	/**
	 * 获取评论图片
	 * @param driver
	 * @throws IOException 
	 */
	private static String getPic(WebDriver driver,WebElement comment) throws IOException {
		String picName = null;
		File screenshotAs = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		BufferedImage bufferedImage = ImageIO.read(screenshotAs);
		
		Point point = comment.getLocation();
		
		int width = comment.getSize().getWidth();
		int height = comment.getSize().getHeight() + 100;
		if(width<=0) width=300;
		
		BufferedImage subimage = bufferedImage.getSubimage(point.getX(), point.getY()+50, width, height);
		
		ImageIO.write(subimage, "png", screenshotAs);
		
		picName = getPicName();
		File file = new File(picName);
		FileUtils.copyFile(screenshotAs, file);
		return picName;
	}

	/**
	 * 图片名称
	 * @return
	 */
	private static String getPicName() {
		String picName = "c:\\jietu\\";
		File file =new File(picName);
		if(!file.exists() && !file.isDirectory()){
		    file.mkdirs();  
		}
		String picUri = "c:\\jietu\\qq_"+System.currentTimeMillis()+".png";
		return picUri;
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