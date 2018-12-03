package com.longriver.netpro.webview.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Scanner;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.util.DriverGet;
import com.longriver.netpro.util.FileUtil;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.PngErjinzhi;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;


/**
 * 头条评论--点赞
 * @author rhy
 * @2017-12-19 下午1:12:16
 * @version v1.1
 */
public class TouTiaoComment_tt {
	
	private static Logger logger = Logger.getLogger(TouTiaoComment_tt.class);
	
	public static void main(String args[]){
			TaskGuideBean taskdo = new TaskGuideBean();
			taskdo.setNick("13074710236");
			taskdo.setPassword("ctyccc1780");
			
			taskdo.setNick("13316867860");
			taskdo.setPassword("wxbyg8800");
			
			taskdo.setAddress("https://www.toutiao.com/a6504728245668151821/");
			taskdo.setPraiseWho("1587188734683150");
			taskdo.setPraiseWho("1587189723425805");
			
			taskdo.setPraiseWho("1587194459488270");
			taskdo.setCorpus("点IC问题");
			
			toutiaoComment(taskdo);
//			toutiaoDigg(taskdo);
			
	}
	/**
	 * 评论
	 * @param taskdo
	 */
	public static void toutiaoComment(TaskGuideBean task){
		
		
		WebDriver driver = null;
		try{
			
		//先清空存放截图的文件夹
		FileUtil.deleteDirectoryFiles();
		String address = URLDecoder.decode(task.getAddress(),"utf-8");
		
		java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config\\properties\\paramsConfig");
		String firefoxUrl = bundle.getString("firefoxurl");

		FirefoxProfile profile = new FirefoxProfile();
//		profile.setPreference("permissions.default.stylesheet", 2);
//		profile.setPreference("permissions.default.image", 2);
		profile.setPreference("dom.ipc.plugins.enabled.libflashplayer.so", "false");
		System.setProperty("webdriver.firefox.bin",firefoxUrl);
		driver = new FirefoxDriver(profile);
		
//		ChromeOptions chromeOptions = new ChromeOptions();
//		chromeOptions.addArguments("--start-maximized");
//		System.setProperty("webdriver.chrome.driver","C:\\tools\\chromedriver.exe");
//		driver = new ChromeDriver(chromeOptions);
 		driver.get("https://sso.toutiao.com/login/");  
		
		driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div/div/ul/li[2]")).click();
		
		driver.findElement(By.xpath("//*[@id='userId']")).sendKeys(task.getNick());
		driver.findElement(By.xpath("//*[@id='passwd']")).sendKeys(task.getPassword());
		Thread.sleep(1000);
		
		try {
			WebElement preimgCode = driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/span/img"));
			
			if(preimgCode.isDisplayed()){
			String precode = getPic(driver, preimgCode);
			driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/input")).sendKeys(precode);
			}
		} catch (Exception e1) {
		}
		
		driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[2]/div/p/a[1]")).click();
		
		Thread.sleep(2000);
		try {
			WebElement imgCode = driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/span/img"));
			
			String code = getPic(driver, imgCode);
			driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/input")).sendKeys(code);
			driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[2]/div/p/a[1]")).click();
			Thread.sleep(3000);
		} catch (Exception e2) {
		}
		
		String currentUrl = driver.getCurrentUrl();
		
		try {
			if(currentUrl != null && "https://api.weibo.com/oauth2/authorize".equals(currentUrl)){
				
				driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[2]/div/p/a[1]")).click();
				Thread.sleep(2000);
			}
		} catch (Exception e3) {
		}
//		 || currentUrl.contains("authorize")
		if(currentUrl.contains("unitivelogin")){
			
			isSuccess(task, "登录失败");
			return;
		}
		
		driver.get(address);
		
//		Thread.sleep(3000);
//		Actions actions = new Actions(driver);
//		WebElement textArea = driver.findElement(By.xpath("//*[@id='comment']/div[2]/div/div[2]/div[1]/textarea"));
//		actions.moveToElement(textArea);
//		textArea.sendKeys(task.getCorpus());
//		driver.findElement(By.xpath("//*[@id='comment']/div[2]/div/div[2]/div[2]/div")).click();
//		Thread.sleep(5000);
		//*[@id="comment"]/ul/li[2]
		WebElement content = driver.findElement(By.xpath("//*[@id='comment']"));
		//*[@id="comment"]/ul/li[1]/div
		//根据语料 判断是否成功
//		if(content.getText().contains(task.getCorpus())){
			
			System.out.println("截图发帖成功!");
			String picUri = getPicContent(driver,content);
			task.setPng(PngErjinzhi.getImageBinary(picUri));
			isSuccess(task, "");
			
//		}else{
//			
//			System.out.println("截图发帖失败!");
//			isSuccess(task, "匹配文字失败");
//		}
		
		}catch(Exception e4){
			
			System.out.println(e4);
			e4.printStackTrace();
			isSuccess(task, "评论错误");
		}finally{
			
//			driver.quit();
		}
		
	}
	/**
	 * 获取评论图片
	 * @param driver
	 * @throws IOException 
	 */
	private static String getPicContent(WebDriver driver,WebElement comment) throws IOException {
		try{
		File screenshotAs = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		BufferedImage bufferedImage = ImageIO.read(screenshotAs);
		
		Point point = comment.getLocation();
		
		int width = comment.getSize().getWidth();
		int height = comment.getSize().getHeight();
		
		BufferedImage subimage = bufferedImage.getSubimage(point.getX(), point.getY(), width, height);
		
		ImageIO.write(subimage, "png", screenshotAs);
		
		String picName = getPicName();
		File file = new File(picName);
		FileUtils.copyFile(screenshotAs, file);
		
		}catch(Exception e){
			
			e.printStackTrace();
			logger.info("获取图片异常"+e);
		}
		return null;
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
		String picUri = "c:\\jietu\\toutiao_"+System.currentTimeMillis()+".png";
		return picUri;
	}
	/**
	 * 获取item_id,group_id
	 * @param address
	 * @return
	 */
	private static String getItemId(String address) {
		
		try {
			String itemId = address.substring(address.indexOf("com")+5).replace("/", "").trim();
			return itemId;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取cookie
	 * @return
	 */
	private static String getCookie(TaskGuideBean task){
		WebDriver driver = null;
		try{
			
		String address = URLDecoder.decode(task.getAddress(),"utf-8");
		
		java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config\\properties\\paramsConfig");
		String firefoxUrl = bundle.getString("firefoxurl");

		FirefoxProfile profile = new FirefoxProfile();
//		profile.setPreference("permissions.default.stylesheet", 2);
//		profile.setPreference("permissions.default.image", 2);
		profile.setPreference("dom.ipc.plugins.enabled.libflashplayer.so", "false");
		System.setProperty("webdriver.firefox.bin",firefoxUrl);
		driver = new FirefoxDriver(profile);
		driver.get("https://sso.toutiao.com/login/");  
		
		driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div/div/ul/li[2]")).click();
		
		driver.findElement(By.xpath("//*[@id='userId']")).sendKeys(task.getNick());
		driver.findElement(By.xpath("//*[@id='passwd']")).sendKeys(task.getPassword());
		Thread.sleep(1000);
		
		try {
			WebElement preimgCode = driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/span/img"));
			
			if(preimgCode.isDisplayed()){
			String precode = getPic(driver, preimgCode);
			driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/input")).sendKeys(precode);
			}
		} catch (Exception e1) {
		}
		
		driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[2]/div/p/a[1]")).click();
		
		Thread.sleep(2000);
		try {
			WebElement imgCode = driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/span/img"));
			
			String code = getPic(driver, imgCode);
			driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/input")).sendKeys(code);
			driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[2]/div/p/a[1]")).click();
			Thread.sleep(3000);
		} catch (Exception e) {
		}
		
		
//		try{
//		driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[2]/div/p/a[1]")).click();
//		Thread.sleep(5000);
//		}catch(Exception e){
//			
//		}
		
		String currentUrl = driver.getCurrentUrl();
		
		try {
			if(currentUrl != null && "https://api.weibo.com/oauth2/authorize".equals(currentUrl)){
				
				driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[2]/div/p/a[1]")).click();
				Thread.sleep(2000);
			}
		} catch (Exception e) {
		}
//		 || currentUrl.contains("authorize")
		if(currentUrl.contains("unitivelogin")){
			DriverGet.quit(driver);
			return null;
		}
		
		driver.get(address);
		
		Set<Cookie> cookies = driver.manage().getCookies();
		
		String skeyCookie = "";
		for (Cookie cookie : cookies) {
			
			skeyCookie += cookie.toString().substring(0,cookie.toString().indexOf(";")) + "; ";
		}
		
		return skeyCookie;
		
		}catch(Exception e){
			
			logger.info("评论异常");
			e.printStackTrace();
		}finally{
			
			if(driver != null){
			driver.quit();
			}
		}
		return null;
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
		
		BufferedImage subimage = bufferedImage.getSubimage(point.getX(), point.getY(), width, height);
		
		ImageIO.write(subimage, "png", screenshotAs);
		
//		String picName = getPicName();
		File file = new File("c:\\toutiao.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("3050", "c:\\toutiao.png");
		
		return code;
		}catch(Exception e){
			
			logger.info("获取图片异常"+e);
		}
		return null;
	}
	/**
	 * 点赞
	 * @param taskdo
	 */
	public static void toutiaoDigg(TaskGuideBean task){
		
		try{
		String cookie = getCookie(task);
		
		if(StringUtils.isBlank(cookie)){

			isSuccess(task, "登录失败");
			return;
		}
		URL url = new URL("https://www.toutiao.com/api/comment/digg/");
		
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		
		String address = URLDecoder.decode(task.getAddress(),"utf-8");
		
		String itemId = getItemId(address);
		String param = "comment_id="+task.getPraiseWho()+"" +
				"&dongtai_id="+task.getPraiseWho()+"" +
				"&group_id="+itemId+"" +
				"&item_id="+itemId+"" +
				"&action=digg";

		openConnection.addRequestProperty("Host", "www.toutiao.com");
		openConnection.addRequestProperty("Connection", "keep-alive");
		openConnection.addRequestProperty("Content-Length", String.valueOf(param.length()));
		openConnection.addRequestProperty("Accept", "text/javascript, text/html, application/xml, text/xml, */*");
		openConnection.addRequestProperty("Origin", "https://www.toutiao.com");
		openConnection.addRequestProperty("X-CSRFToken", "undefined");
		openConnection.addRequestProperty("X-Requested-With", "XMLHttpRequest");
		openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.75 Safari/537.36");
		openConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		openConnection.addRequestProperty("Referer", address);
		openConnection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
		openConnection.addRequestProperty("Cookie", cookie);
		
		openConnection.setDoInput(true);
		openConnection.setDoOutput(true);
		openConnection.setRequestMethod("POST");
		openConnection.setUseCaches(false);
		
		PrintWriter pw = new PrintWriter(openConnection.getOutputStream());
		pw.write(param);
		pw.flush();
		
		Scanner sc = new Scanner(openConnection.getInputStream(),"utf-8");
		
		String result = "";
		while(sc.hasNext()){
			
			result += sc.nextLine();
		}
		
		JSONObject parseObject = JSON.parseObject(result);
		String message = parseObject.getString("message");
		
		if(message != null && "success".equals(message)){
			
			isSuccess(task, "");
		}else{
			
			isSuccess(task, message);
		}
		}catch(Exception e){
			
			isSuccess(task, "点赞错误");
			e.printStackTrace();
		}
	}

	/**
	 * 判断是否成功
	 */
	public static void isSuccess(TaskGuideBean task,String msg){
		
		MQSender.toMQ(task,msg);
	}
	
}
