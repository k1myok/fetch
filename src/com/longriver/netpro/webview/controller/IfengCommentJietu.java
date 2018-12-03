package com.longriver.netpro.webview.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import com.longriver.netpro.util.DriverGet;
import com.longriver.netpro.util.FileUtil;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.MyWebDriverListener;
import com.longriver.netpro.util.PngErjinzhi;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 凤凰新闻
 * @author rhy
 * @2017-12-5 下午4:30:03
 * @version v1.0
 */
public class IfengCommentJietu {

	private static Logger logger = Logger.getLogger(IfengCommentJietu.class);
	public static boolean isSend = false;
	
	public static void main(String[] args) {
		
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://news.ifeng.com/a/20171205/53876619_0.shtml");
		task.setAddress("http://news.ifeng.com/a/20171026/52794364_0.shtml");
		task.setAddress("http://sd.ifeng.com/a/20180711/6716951_0.shtml");

		task.setCorpus("恩恩,什么个情况?");
		task.setNick("13042079465");
		task.setPassword("qqqwww");
//		task.setNick("江山前方");
//		task.setPassword("lilei419688");
//		try {
//			System.out.println("address=="+getCommentAddress(task.getAddress()));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		toComment(task);
	}

	/**
	 * 凤凰新闻评论
	 * @param task
	 */
	public static void toComment(TaskGuideBean task) {
		//先清空存放截图的文件夹
		FileUtil.deleteDirectoryFiles();
		//保证只返回一次结果
		isSend = true;
		toRun(task);//三次验证码
	}
	public static void toRun(TaskGuideBean task) {
		
		WebDriver driver = DriverGet.getDriver();
		try{
			String address = URLDecoder.decode(task.getAddress(),"utf-8");
			
			driver.get("https://id.ifeng.com/user/login");
			
			Thread.sleep(1000);
			driver.findElement(By.id("userLogin_name")).sendKeys(task.getNick());
			driver.findElement(By.id("userLogin_pwd")).sendKeys(task.getPassword());
			WebElement imgCode = driver.findElement(By.id("code_img"));
			//验证码
			yzmInput(driver,imgCode,1);
			
			Thread.sleep(3000);
			System.out.println(driver.getCurrentUrl());
			if(driver.getCurrentUrl().contains("user/login")){
				String error = driver.findElement(By.className("warn_text")).getText();
				if(error.contains("密码")) task.setCode(101);//需要改变账号状态
				if(error.contains("验证码")) task.setCode(102);//不需要改变账号状态
				isSuccess(task, error+"错");
				return;
			}
			if(driver.getPageSource().contains("尚未绑定手")){
				task.setCode(101);
				isSuccess(task, "需要手机核验");
				return ;
			}
			
			driver.get(address);
			Thread.sleep(1000);
			driver.findElement(By.className("js_cmtNum")).click();
//			String commentAddress = getCommentAddress(address);
//			System.out.println("commentAddress="+commentAddress);
//			driver.get(commentAddress);
			switchToNewWindow(driver);
			Thread.sleep(2000);
			
			driver.findElement(By.xpath("//*[@id='js_cmtContainer']/div[6]/div[2]/form/div[1]/textarea")).sendKeys(task.getCorpus());
			driver.findElement(By.xpath("//*[@id='js_cmtContainer']/div[6]/div[2]/form/div[2]/a")).click();
			
			Thread.sleep(1500);
			if(driver.getPageSource().contains("allsite/loginmob")){
				task.setCode(101);
				isSuccess(task, "需要手机核验");
				return ;
			}
			Thread.sleep(2000);
			WebElement content = driver.findElement(By.xpath("//*[@id='js_cmtContainer']/div[5]/div[2]/div[1]/div"));
			//根据语料 判断是否成功
			//根据语料 判断是否成功
			String tt = task.getCorpus().length()>1?task.getCorpus().substring(1):task.getCorpus();
			String cor = tt.length()>6?tt.substring(0, 6):tt;
			System.out.println("text=="+content.getText());
			System.out.println("corpus=="+cor);
			System.out.println("contains=="+content.getText().contains(cor));
			if(content.getText().contains(cor)){
				System.out.println("截图发帖成功!");
				try {
					String picUri = getPicContent(task,driver,content);
					task.setPng(PngErjinzhi.getImageBinary(picUri));
				} catch (Exception e) {
					System.out.println("发帖成功截图失败!");
				}
			}else{
				System.out.println("截图发帖失败!");
				task.setCode(102);
				isSuccess(task, "");
			}
			
		}catch(Exception e){
			logger.info("评论异常");
			task.setCode(102);
			isSuccess(task, "评论错误");
			e.printStackTrace();
		}finally{
			task.setCode(100);
			isSuccess(task, "");
			DriverGet.quit(driver);
		}
	}
	//切换到新窗口
	public static void switchToNewWindow(WebDriver driver){
		String currentWindow = driver.getWindowHandle();
	    //得到所有窗口的句柄
	    Set<String> handles = driver.getWindowHandles();
	    //排除当前窗口的句柄，则剩下是新窗口
	    Iterator<String> it = handles.iterator();
	    while(it.hasNext()){
	        if(currentWindow == it.next()) continue;
	        driver.switchTo().window(it.next());      
	    }
	     
	}
	public static void yzmInput(WebDriver driver,WebElement imgCode,int yzmcode){
		String code = getPic(driver, imgCode);
		driver.findElement(By.id("userLogin_securityCode")).clear();;
		driver.findElement(By.id("userLogin_securityCode")).sendKeys(code);
		driver.findElement(By.id("userLogin_btn")).click();
		
		try{
			String text = driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/div/div[1]/div[6]")).getText();
			
			if(StringUtils.isNotBlank(text)){
				if(text.contains("验证码") && yzmcode<5){
					yzmcode = yzmcode+1;
					yzmInput(driver,imgCode,yzmcode);
					return ;
				}
			}
		}catch(Exception e){}
	}
	/**
	 * 获取文章id
	 * @param address
	 * @return
	 * @throws IOException 
	 */
	private static String getCommentAddress(String address) throws IOException {
		URL url = new URL(address);
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		openConnection.connect();
		
		Scanner sc = new Scanner(openConnection.getInputStream(),"utf-8");
		String result = "";
	    while(sc.hasNext()){
	    	result += sc.nextLine() + "\n";
	    }
		String title = result.substring(result.indexOf("<title>")+7, result.lastIndexOf("</title>"));
		if(title.lastIndexOf("_")>0) title = title.substring(0, title.lastIndexOf("_"));
		System.out.println("title=="+title);
		String docId = result.substring(result.indexOf("commentUrl")+10);
		docId = docId.substring(0, docId.indexOf(",")).replaceAll(":\"", "").replaceAll("\"", "").trim();
		
		docId = "http://gentie.ifeng.com/view.html?docUrl="+docId+"&docName="+title+"&pcUrl="+address;
		
		return docId;
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
		
		String code = RuoKuai.createByPostNew("3040", "c:\\ifeng.png");
		
		return code;
		}catch(Exception e){
			
			logger.info("获取图片异常"+e);
		}
		return null;
	}
	/**
	 * 获取评论图片
	 * @param driver
	 * @throws IOException 
	 */
	private static String getPicContent(TaskGuideBean task,WebDriver driver,WebElement comment) throws IOException {
		File screenshotAs = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		
		BufferedImage bufferedImage = ImageIO.read(screenshotAs);
		
		Point point = comment.getLocation();
		
		int width = comment.getSize().getWidth();
		int height = comment.getSize().getHeight();
		
		BufferedImage subimage = bufferedImage.getSubimage(point.getX(), point.getY(), width, height);
		
		ImageIO.write(subimage, "png", screenshotAs);
		
		String picUri = getPicName();
		File file = new File(picUri);
		FileUtils.copyFile(screenshotAs, file);
		
		return picUri;
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
		String picUri = "c:\\jietu\\ifeng_"+System.currentTimeMillis()+".png";
		return picUri;
	}
	/**
	 * 判断是否成功
	 */
	public static void isSuccess(TaskGuideBean task,String msg){
		if(isSend){
			isSend = false;
//			KillProcess.kill();
			MQSender.toMQ(task,msg);
		}
	}
	
}
