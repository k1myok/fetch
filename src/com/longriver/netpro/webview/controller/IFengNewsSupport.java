package com.longriver.netpro.webview.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.testng.log4testng.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 凤凰点赞
 * @author rhy
 * @2017-12-28 下午2:27:23
 * @version v1.0
 */
public class IFengNewsSupport {
	
	private static Logger logger = Logger.getLogger(IFengNewsSupport.class);
	
	public static void main(String agrs[]){
		
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://news.ifeng.com/a/20171228/54596196_0.shtml");
		
//		task.setAddress("http://sports.ifeng.com/a/20171228/54603271_0.shtml");
//		task.setAddress("http://news.ifeng.com/a/20171228/54612123_0.shtml");
		task.setNick("13040899405");
		task.setPassword("qqqwww");
		task.setPraiseWho("1017782679");//印度牛肉据说好吃，天然放养
		task.setPraiseWho("1017795893");//行就行
		task.setPraiseWho("1017799418");//天然气的供应没有跟上
		task.setPraiseWho("1018111417");

		ifeng(task);
	}
	
	/**
	 * 
	 * @param taskdo
	 * @throws Exception
	 */
	public static void ifeng(TaskGuideBean task){
		
		Map<String,String> paramMap = null;
		try{
		paramMap = getCookie(task);
		String errorText = paramMap.get("errorText");
		
		if(StringUtils.isNotBlank(errorText)){
			
			isSuccess(task, errorText);
			return;
		}
		
		String address = URLDecoder.decode(task.getAddress(), "utf-8");
		
		URL url = new URL("http://comment.ifeng.com/vote.php?callback=recmCallback" +
				"&cmtId="+task.getPraiseWho()+"" +
				"&job=up" +
				"&docUrl="+paramMap.get("docId")+"" +
				"&callback=recmCallback" +
				"&format=js" +
				"&_="+System.currentTimeMillis());
		
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		
		openConnection.addRequestProperty("Host", "comment.ifeng.com");
		openConnection.addRequestProperty("Connection", "keep-alive");
		openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.75 Safari/537.36");
		openConnection.addRequestProperty("Accept", "*/*");
		openConnection.addRequestProperty("Referer", address);
		openConnection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
		openConnection.addRequestProperty("Cookie", paramMap.get("cookie"));
		openConnection.connect();
	
		BufferedReader br = new BufferedReader(new InputStreamReader(openConnection.getInputStream(), "utf-8"));
		
		String line;
		StringBuffer sb = new StringBuffer();
		while((line = br.readLine()) != null){
			
			sb.append(line);
		}
		
		String result = sb.toString();
		if(result!=null && result.contains("已经点过了")){
			
			isSuccess(task, "已经点过了噢！");
			return;
		}else{
			
		String commentResult = result.substring(result.indexOf("commentJsonVarStr___")+21);
		commentResult = commentResult.substring(0, commentResult.indexOf(";"));
		JSONObject parseObject = JSON.parseObject(commentResult);
		String code = parseObject.getString("code");
		
		if(code != null && "1".equals(code)){
			
			isSuccess(task, "");
		}else{
			
			isSuccess(task, "发生错误");
		}
		}
		}catch(Exception e){
			
			isSuccess(task, "评论错误");
			e.printStackTrace();
		}
		
	}

	/**
	 * 获取cookie
	 * @return
	 */
	private static Map<String,String> getCookie(TaskGuideBean task) {
		
		WebDriver driver = null;
		
		try {
			
			Map<String,String> paramMap = new HashMap<String,String>();
			
			java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config\\properties\\paramsConfig");
			String firefoxUrl = bundle.getString("firefoxurl");
			System.setProperty("webdriver.firefox.bin",firefoxUrl);
			FirefoxProfile profile = new FirefoxProfile();
			profile.setPreference("dom.ipc.plugins.enabled.libflashplayer.so", "false");
			driver = new FirefoxDriver(profile);
			
			driver.get("https://id.ifeng.com/user/login");
			
			driver.findElement(By.id("userLogin_name")).sendKeys(task.getNick());
			driver.findElement(By.id("userLogin_pwd")).sendKeys(task.getPassword());
			WebElement imgcode = driver.findElement(By.id("code_img"));
			
			String code = getPic(driver, imgcode);
			
			driver.findElement(By.id("userLogin_securityCode")).sendKeys(code);
			
			driver.findElement(By.id("userLogin_btn")).click();
			
			Thread.sleep(2000);
			String errorText = "";
			try {
				errorText = driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/div/div[1]/div[6]")).getText();
				
				if(StringUtils.isNotBlank(errorText)){
					
					paramMap.put("errorText", errorText);
					return paramMap;
				}
			} catch (Exception e) {
			}
			
			paramMap.put("errorText", errorText);
			
			Set<Cookie> cookies = driver.manage().getCookies();
			
			String skeyCookie = "";
			for (Cookie cookie : cookies) {
				
				skeyCookie += cookie.toString().substring(0,cookie.toString().indexOf(";")) + "; ";
			}
			
			paramMap.put("cookie", skeyCookie);
			
			String docId = getDocId(task);
			paramMap.put("docId", docId);
			
			return paramMap;
		} catch (Exception e) {
			
			e.printStackTrace();
		}finally{
			
			if(driver != null){
			driver.quit();
			}
		}
		return null;
	}
	/**
	 * 获取文章id
	 * @param pageSource
	 * @return
	 */
	private static String getDocId(TaskGuideBean task) {
		
		String commentUrl = "";
		try {
			
			String address = URLDecoder.decode(task.getAddress(), "utf-8");
			URL url = new URL(address);
			
			HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
			openConnection.connect();
		
			BufferedReader br = new BufferedReader(new InputStreamReader(openConnection.getInputStream(), "utf-8"));
			
			String line;
			StringBuffer sb = new StringBuffer();
			while((line = br.readLine()) != null){
				
				sb.append(line);
			}
			
			String pageSource = sb.toString();
			commentUrl = pageSource.substring(pageSource.indexOf("commentUrl")+12);
			commentUrl = commentUrl.substring(0, commentUrl.indexOf(",")).replaceAll("\"", "").trim();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return commentUrl;
	}

	/**
	 * 获取验证码
	 * @param driver
	 */
	private static String getPic(WebDriver driver,WebElement imgcode) {
		
		try{
		File screenshotAs = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		BufferedImage bufferedImage = ImageIO.read(screenshotAs);
		
		Point point = imgcode.getLocation();
		
		int width = imgcode.getSize().getWidth();
		int height = imgcode.getSize().getHeight();
		
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
	 * 判断是否成功
	 */
	public static void isSuccess(TaskGuideBean task,String msg){
		
		MQSender.toMQ(task, msg);
	}
	
}
