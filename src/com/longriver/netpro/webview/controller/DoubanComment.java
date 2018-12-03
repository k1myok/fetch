package com.longriver.netpro.webview.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 豆瓣电影评论
 * @author rhy
 * @2017-11-16 下午1:14:06
 * @version v1.0
 */
public class DoubanComment {

	private static Logger logger = LoggerFactory.getLogger(DoubanComment.class);
	
	private static int codetimes = 0;
	public static void main(String[] args) {
		
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("https://movie.douban.com/subject/2158490/?from=showing");
		task.setAddress("https://movie.douban.com/review/8926432/");
		task.setCorpus("哈哈");
		task.setNick("647594216@qq.com");
		task.setPassword("q1w2e3r4");
		
		task.setNick("15008074267");
		task.setPassword("3432635721");
//		task.setAddress("https://movie.douban.com/subject/26752852/");
		task.setCorpus("哈哈,还不错呢");
		task.setNick("647594216@qq.com");
		task.setPassword("q1w2e3r4");
		
		//微博账号登录
//		task.setNick("17173425504");
//		task.setPassword("chwx090674542");
//		task.setNick("17173425505");
//		task.setPassword("chwx123456");
		task.setNick("17173425256");
		task.setPassword("chwx123456");
		toComment(task);
	}

	/**
	 * 第一种类型短评
	 * 豆瓣评论
	 * @param task
	 */
	public static void toComment(TaskGuideBean task) {
		
		try{
			
		String cookie = toLogin(task);
			
		if(cookie == null){
			return;
		}
			
//		Map<String, String> paramMap = getCookieByWeibo(task);
//
//		String cookie = paramMap.get("cookie");
//		if(StringUtils.isBlank(paramMap.get("cookie"))){
//			
//			isSuccess(task, paramMap.get("msg"));
//			return;
//		}
		String address = URLDecoder.decode(task.getAddress(),"utf-8");
		
		String movieid = "";
		if(address != null && address.contains("subject")){
			
			movieid = address.substring(address.indexOf("subject")+7);
			if(movieid.indexOf("?")>=0){
			movieid = movieid.substring(0,movieid.indexOf("?")).replaceAll("/", "").trim();
			}else{
				movieid = movieid.substring(0).replaceAll("/", "").trim();
			}
		}else{
			
			movieid = address.substring(address.indexOf("review")+6).replaceAll("/", "").trim();
			executeComment(task,movieid,cookie);
			return;
		}
		
		URL url = new URL("https://movie.douban.com/j/subject/"+movieid+"/interest");
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		
		String sk = cookie.substring(cookie.indexOf("ck=")+3);
		sk = sk.substring(0,sk.indexOf(";"));
		String param = "ck="+sk+"" +
				"&interest=wish" +
				"&rating=" +
				"&foldcollect=F" +
				"&tags=" +
				"&comment="+URLEncoder.encode(task.getCorpus(),"utf-8")+"" +
				"&share-shuo=douban";
		
		if(address != null && address.indexOf("?")>=0){
			address = address.substring(0,address.indexOf("?"));
		}
		
		openConnection.addRequestProperty("Host", "movie.douban.com");
		openConnection.addRequestProperty("Connection", "keep-alive");
		openConnection.addRequestProperty("Content-Length", String.valueOf(param.length()));
		openConnection.addRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		openConnection.addRequestProperty("Origin", "https://movie.douban.com");
		openConnection.addRequestProperty("X-Requested-With", "XMLHttpRequest");
		openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.75 Safari/537.36");
		openConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		openConnection.addRequestProperty("Referer", address);
		openConnection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
		openConnection.addRequestProperty("Cookie", cookie);
		
		openConnection.setDoInput(true);
		openConnection.setDoOutput(true);
		openConnection.setRequestMethod("POST");
		openConnection.setUseCaches(false);
		
		
		PrintWriter pw = new PrintWriter(openConnection.getOutputStream());
		pw.print(param);
		pw.flush();
		
		int responseCode = openConnection.getResponseCode();
		if(responseCode != HttpURLConnection.HTTP_OK){
			
			logger.info("状态码错误");
			isSuccess(task, "状态码错误");
			return;
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(openConnection.getInputStream(),"utf-8"));
		String line;
		StringBuffer sb = new StringBuffer();
		while((line = br.readLine())!=null){
			sb.append(line);
		}
		
		System.out.println(sb.toString());
		JSONObject parseObject = JSON.parseObject(sb.toString());
		String result = parseObject.getString("r");
		if(result!=null && "0".equals(result)){
			
			logger.info("评论成功");
			isSuccess(task, "");
		}else{
			
			logger.info("评论失败");
			isSuccess(task, "评论失败");
		}
		}catch(Exception e){
			
			logger.info("评论异常");
			e.printStackTrace();
		}
	}
	
	/**
	 * 影评
	 * @param task
	 * @param cookie
	 */
//	private static void toMovieComment(TaskGuideBean task, String cookie) {
//
//		try{
//		String address = URLDecoder.decode(task.getCorpus(),"utf-8");
//		
//		String movieid = address.substring(address.indexOf("review")+6).replaceAll("/", "").trim();
//		URL url = new URL("https://movie.douban.com/subject/"+movieid+"/new_review");
//		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
//		
//		String param = "ck=mBZK" +
//				"&review%5Bsubject_id%5D=26935251" +
//				"&review%5Btitle%5D=%E9%9C%80%E8%A6%81%E8%BF%99%E6%A0%B7%E7%9A%84" +
//				"&review%5Brating%5D=5" +
//				"&review%5Btext%5D=%E9%82%A3%E5%B0%B1%E8%BF%99%E6%A0%B7%E5%90%A7%E5%8E%9F%E6%9D%A5%EF%BC%8C%E6%98%AF%E5%9B%A0%E4%B8%BA%E6%9C%89%E4%B8%80%E5%A4%A9%E6%BC%AB%E5%A8%81%E5%8F%AC%E9%9B%86%E5%A4%8D%E8%81%94%E5%85%A8%E4%BD%93%E6%88%90%E5%91%98%E6%8B%8D%E9%9B%86%E4%BD%93%E7%85%A7%E7%9A%84%E6%97%B6%E5%80%99%EF%BC%8C%E6%B5%A9%E5%85%8B%E5%AE%9D%E5%AE%9D%E6%8C%89%E6%8D%BA%E4%B8%8D%E4%BD%8F%E8%87%AA%E5%B7%B1%E5%96%9C%E6%82%A6%E7%9A%84%E5%BF%83%E6%83%85%EF%BC%8C%E7%9B%B4%E6%8E%A5%E5%9C%A8facebook%E4%B8%8A%E5%81%9A%E8%B5%B7%E4%BA%86%E7%8E%B0%E5%9C%BA%E7%9B%B4%E6%92%AD%EF%BC%8C%E8%BF%98%E9%82%80%E8%AF%B7%E4%B8%80%E4%BC%97%E5%A4%A7%E5%92%96%E4%BB%AC%E5%90%91%E8%87%AA%E5%B7%B1%E7%9A%84%E7%B2%89%E4%B8%9D%E6%89%93%E6%8B%9B%E5%91%BC%E3%80%82%E8%BF%99%E7%A7%8D%E5%85%A8%E5%91%98%E5%87%BA%E5%B8%AD%E7%9A%84%E5%A4%A7%E5%9E%8B%E6%B4%BB%E5%8A%A8%E4%B8%80%E4%B8%8D%E7%95%99%E7%A5%9E%E5%B0%B1%E5%8F%AF%E8%83%BD%E6%9A%B4%E9%9C%B2%E5%87%BA%E6%B6%89%E5%8F%8A%E5%89%A7%E6%83%85%E7%9A%84%E9%87%8D%E8%A6%81%E7%BB%86%E8%8A%82%EF%BC%8C%E9%9A%BE%E6%80%AA%E6%BC%AB%E5%A8%81%E5%BF%8D%E4%B8%8D%E4%BA%86%E3%80%82%E5%B9%B6%E7%A7%B0%E8%87%AA%E5%B7%B1%E7%9F%A5%E9%81%93%E9%94%99%E4%BA%86%E3%80%82%E4%B8%8D%E7%94%A8%E7%8C%9C%E5%B0%B1%E7%9F%A5%E9%81%93%EF%BC%8C%E8%BF%99%E4%B8%AA%E5%A4%A7%E5%98%B4%E5%B7%B4%E8%82%AF%E5%AE%9A%E5%8F%88%E5%BF%8D%E4%B8%8D%E4%BD%8F%E5%9C%A8%E7%BD%91%E4%B8%8A%E6%8F%90%E5%89%8D%E5%89%A7%E9%80%8F%E4%BA%86%EF%BC%8C%E8%BF%9E%E6%89%8B%E6%9C%BA%E9%83%BD%E8%83%BD%E8%A2%AB%E6%B2%A1%E6%94%B6%EF%BC%8C%E4%BC%B0%E8%AE%A1%E6%AF%94%E4%B9%8B%E5%89%8D%E8%BF%98%E8%A6%81%E8%BF%87%E5%88%86%E3%80%82" +
//				"&review%5Bspoiler%5D=" +
//				"&review%5Boriginal%5D=";
//				
//		openConnection.addRequestProperty("Host", "movie.douban.com");
//		openConnection.addRequestProperty("Connection", "keep-alive");
//		openConnection.addRequestProperty("Content-Length", String.valueOf(param.length()));
//		openConnection.addRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
//		openConnection.addRequestProperty("Origin", "https://movie.douban.com");
//		openConnection.addRequestProperty("X-Requested-With", "XMLHttpRequest");
//		openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.75 Safari/537.36");
//		openConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//		openConnection.addRequestProperty("Referer", address);
//		openConnection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
//		openConnection.addRequestProperty("Cookie", cookie);
//		
//		openConnection.setDoInput(true);
//		openConnection.setDoOutput(true);
//		openConnection.setRequestMethod("POST");
//		openConnection.setUseCaches(false);
//		
//		
//		PrintWriter pw = new PrintWriter(openConnection.getOutputStream());
//		pw.print(param);
//		pw.flush();
//		
//		int responseCode = openConnection.getResponseCode();
//		if(responseCode != HttpURLConnection.HTTP_OK){
//			
//			logger.info("状态码错误");
//			isSuccess(task, "状态码错误");
//			return;
//		}
//		
//		BufferedReader br = new BufferedReader(new InputStreamReader(openConnection.getInputStream(),"utf-8"));
//		String line;
//		StringBuffer sb = new StringBuffer();
//		while((line = br.readLine())!=null){
//			sb.append(line);
//		}
//		
//		System.out.println(sb.toString());
//		JSONObject parseObject = JSON.parseObject(sb.toString());
//		String result = parseObject.getString("r");
//		}catch(Exception e){
//			
//			e.printStackTrace();
//		}
//		
//		
//	}

	/**
	 * 第二种类型短评
	 * 豆瓣评论
	 * @param task
	 * @param movieid
	 */
	private static void executeComment(TaskGuideBean task, String movieid,String cookie) {
		
		try{
		String address = URLDecoder.decode(task.getAddress(),"utf-8");
		
		URL url = new URL("https://movie.douban.com/j/review/"+movieid+"/add_comment");
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		
		String sk = cookie.substring(cookie.indexOf("ck=")+3);
		sk = sk.substring(0,sk.indexOf(";"));
		
		String param = "ck="+sk+"" +
				"&start=0" +
				"&text="+URLEncoder.encode(task.getCorpus(),"utf-8")+"" +
				"&ref_cid=";
		
		if(address != null && address.indexOf("?")>=0){
			address = address.substring(0,address.indexOf("?"));
		}
		
		openConnection.addRequestProperty("Host", "movie.douban.com");
		openConnection.addRequestProperty("Connection", "keep-alive");
		openConnection.addRequestProperty("Content-Length", String.valueOf(param.length()));
		openConnection.addRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		openConnection.addRequestProperty("Origin", "https://movie.douban.com");
		openConnection.addRequestProperty("X-Requested-With", "XMLHttpRequest");
		openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.75 Safari/537.36");
		openConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		openConnection.addRequestProperty("Referer", address);
		openConnection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
		openConnection.addRequestProperty("Cookie", cookie);
		
		openConnection.setDoInput(true);
		openConnection.setDoOutput(true);
		openConnection.setRequestMethod("POST");
		openConnection.setUseCaches(false);
		
		
		PrintWriter pw = new PrintWriter(openConnection.getOutputStream());
		pw.print(param);
		pw.flush();
		
		int responseCode = openConnection.getResponseCode();
		if(responseCode != HttpURLConnection.HTTP_OK){
			
			logger.info("第二类型状态码错误");
			isSuccess(task, "状态码错误");
			return;
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(openConnection.getInputStream(),"utf-8"));
		String line;
		StringBuffer sb = new StringBuffer();
		while((line = br.readLine())!=null){
			sb.append(line);
		}
		
		System.out.println(sb.toString());
		JSONObject parseObject = JSON.parseObject(sb.toString());
		String result = parseObject.getString("r");
		if(result!=null && "0".equals(result)){
			
			logger.info("第二类型评论成功");
			isSuccess(task, "");
			return;
		}else{
			
			logger.info("第二类型评论失败");
			isSuccess(task, "评论失败");
			return;
		}
		}catch(Exception e){
			
			logger.info("第二类型评论异常");
			e.printStackTrace();
			return;
		}
		
	}

	/**
	 * 豆瓣通过微博登录
	 * @param task
	 */
	public static Map<String,String> getCookieByWeibo(TaskGuideBean task) {
	
		WebDriver driver = null;
		
		Map<String,String> paramMap = new HashMap<String,String>();
		try {
			driver = getDriver();
			
			if(driver == null){
				
//				isSuccess(task, "driver打开失败");
				paramMap.put("msg", "driver打开失败");
				return paramMap;
			}
			
			driver.get("https://accounts.douban.com/login?source=movie");
			
			driver.findElement(By.xpath("//*[@id='lzform']/div[7]/a[2]")).click();
			
//			Thread.sleep(2000);
			
//			driver.findElement(By.xpath("//*[@id='app']/div[2]/div[1]/div/div[3]/div/ul/li[3]/a")).click();
			
//			Thread.sleep(2000);
//			String[] password = task.getPassword().split("&");
//			driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div/div/ul/li[2]")).click();
			
//			Thread.sleep(1000);
			
			driver.findElement(By.id("userId")).sendKeys(task.getNick());
			
			driver.findElement(By.id("passwd")).sendKeys(task.getPassword());
			
			try {
				WebElement vcode = driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/span/img"));
				
				String picCode = getPicCode(driver, vcode);
				
				driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/input")).sendKeys(picCode);
				
				driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[2]/div/p/a[1]")).click();
				
				Thread.sleep(2000);
				
				try {
					if(StringUtils.isNotBlank(driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText())){
						
						
						if(codetimes<3){
						if(driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText().contains("输入的验证码不正确") || driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText().contains("请输入验证码")){
							
							codetimes++;
							driver.quit();
							getCookieByWeibo(task);
						}
						}else{
//						task.setCode(6);
//						isSuccess(task, driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText());
						paramMap.put("msg", driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText());
						return paramMap;
						}
					};
				} catch (Exception e) {
				}
			} catch (Exception e3) {
			}
			try {
				driver.findElement(By.className("WB_btn_login")).click();
				Thread.sleep(2000);
				
				try {
					if(StringUtils.isNotBlank(driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText())){
						
						
						if(codetimes<3){
						if(driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText().contains("输入的验证码不正确") ||  driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText().contains("请输入验证码")){
							
							codetimes++;
							driver.quit();
							getCookieByWeibo(task);
						}
						}else{
//						isSuccess(task, driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText());
							
							paramMap.put("msg", driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText());
						return paramMap;
						}
					}
				} catch (Exception e) {
				}
			} catch (Exception e3) {
			}
			
			try {
				WebElement vcode = driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/span/img"));
				
				String picCode = getPicCode(driver, vcode);
				
				driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/input")).sendKeys(picCode);
				
				driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[2]/div/p/a[1]")).click();
				
				Thread.sleep(2000);
				try {
					if(StringUtils.isNotBlank(driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText())){
						
						
						if(codetimes<3){
						if(driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText().contains("输入的验证码不正确") ||  driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText().contains("请输入验证码")){
							
							codetimes++;
							driver.quit();
							getCookieByWeibo(task);
						}
						}else{
//						task.setCode(6);
//						isSuccess(task, driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText());
							paramMap.put("msg", driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText());
						return paramMap;
						}
					};
				} catch (Exception e) {
				}
				
			} catch (Exception e2) {
			}
			
			
			try {
				String errorcontent = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[1]/dl/dt[1]")).getText();
				if(StringUtils.isNotBlank(errorcontent)){
					
//					task.setCode(1);
//					isSuccess(task, errorcontent + driver.findElement(By.xpath("/html/body/div/div[2]/div/div[1]/dl/dt[3]")).getText());
					paramMap.put("msg", errorcontent + driver.findElement(By.xpath("/html/body/div/div[2]/div/div[1]/dl/dt[3]")).getText());
					return paramMap;
				}
			} catch (Exception e1) {
			}
			
			
			try {
				if(driver.getCurrentUrl().contains("api.weibo.com/oauth2/authorize")){
					
					driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[2]/div/p/a[1]")).click();
					
					Thread.sleep(2000);
				}
			} catch (Exception e) {
			}
			String doubanUrl = driver.getCurrentUrl();
			
			if(doubanUrl.contains("accounts.douban.com/phone/bind")){
				
				paramMap.put("msg", "请您绑定手机号");
				return paramMap;
			}
			
			if(doubanUrl.contains("accounts.douban.com/accounts/safety/locked")){
				paramMap.put("msg", "账号被锁");
				return paramMap;
			}
			if(doubanUrl.contains("www.douban.com")){
				
				Set<Cookie> cookies = driver.manage().getCookies();
				
				String skeyCookie = "";
				for (Cookie cookie : cookies) {
					
					skeyCookie += cookie.toString().substring(0,cookie.toString().indexOf(";")) + "; ";
				}
				
				paramMap.put("cookie", skeyCookie);
				return paramMap;
			}
		} catch (Exception e) {
			
			isSuccess(task, "发生异常了");
			e.printStackTrace();
		}finally{
			
			driver.quit();
		}
		return paramMap;
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
		
		File file = new File("c:\\weibo.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("3050", "c:\\weibo.png");
		
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
	 * 豆瓣登录
	 * @param task
	 * @return
	 */
	public static String toLogin(TaskGuideBean task){
		
		String skeyCookie = "";
		WebDriver driver = null;
		try{
		java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config\\properties\\paramsConfig");
		String firefoxUrl = bundle.getString("firefoxurl");
		
		System.setProperty("webdriver.firefox.bin",firefoxUrl);
		driver = new FirefoxDriver();
		driver.get("https://accounts.douban.com/login?source=movie");
		
		WebElement username = driver.findElement(By.id("email"));
		username.clear();
		username.sendKeys(task.getNick());
		
		WebElement password = driver.findElement(By.id("password"));
		password.clear();
		password.sendKeys(task.getPassword());
		
		String pageSource = driver.getPageSource();
		
		Document doc = Jsoup.parse(pageSource);
		
		Element form = doc.getElementById("lzform");
		String text = form.text();
		if(text.contains("请输入上图中的单词")){
			
			try {
				WebElement imagecode = driver.findElement(By.id("captcha_image"));
				
				
				WebElement imageText = driver.findElement(By.id("captcha_field"));
				imageText.clear();
				imageText.sendKeys(getImageCode(driver,imagecode));
				
				WebElement loginButtons = driver.findElement(By.xpath("//*[@id='lzform']/div[7]/input"));
				loginButtons.click();
			} catch (Exception e) {
			}
			
		}else{
		
			try {
				WebElement loginButton = driver.findElement(By.xpath("//*[@id='lzform']/div[6]/input"));
				loginButton.click();
			} catch (Exception e) {
			}
		}
		
		Thread.sleep(8000);
		
		String currentUrl = driver.getCurrentUrl();
		if(currentUrl != null && currentUrl.contains("locked")){
			
			isSuccess(task, "账号被锁");
			
			return null;
		}
		if(currentUrl != null && currentUrl.contains("login")){
			
			String msg = getErrorMessage(driver);
			
			if((msg != null && "需要输入验证码".equals(msg)) || (msg != null && "验证码不正确".equals(msg))){
				
				if(codetimes<4){
					
					driver.quit();
					codetimes++;
					toLogin(task);
					
				}
				
//				password.clear();
//				password.sendKeys(task.getPassword());
//				
//				WebElement imagecode = driver.findElement(By.id("captcha_image"));
//				
//				WebElement imageText = driver.findElement(By.id("captcha_field"));
//				imageText.clear();
//				imageText.sendKeys(getImageCode(driver,imagecode));
//				
//				WebElement loginButtons = driver.findElement(By.xpath("//*[@id='lzform']/div[8]/input"));
//				loginButtons.click();
//				
//				Thread.sleep(8000);
//				currentUrl = driver.getCurrentUrl();
//				if(currentUrl != null && currentUrl.contains("locked")){
//					
//					isSuccess(task, "账号被锁");
//					
//					DriverGet.quit(driver);
//					return null;
//				}
//				
//				if(currentUrl != null && currentUrl.contains("login")){
//					
//					msg = getErrorMessage(driver);
//					if(StringUtils.isBlank(msg)){
//						msg = "登录失败";
//					}
//					isSuccess(task, msg);
//					DriverGet.quit(driver);
//					return null;
//				}
				
			}else{
				isSuccess(task, msg);
				return null;
				
			}
//			if(StringUtils.isBlank(msg)){
//				
//				msg = "登录失败";
//			}
			
		}
		Set<Cookie> cookies = driver.manage().getCookies();
		
		for (Cookie cookie : cookies) {
			
			skeyCookie += cookie.toString().substring(0,cookie.toString().indexOf(";")) + "; ";
		}
		return skeyCookie;
		}catch(Exception e){
			
			logger.info("登录异常");
			isSuccess(task, "登录异常");
			return null;
		}finally{
			
			driver.quit();
		}
	}
	
	/**
	 * 获取失败信息
	 * @param driver
	 * @return
	 */
	private static String getErrorMessage(WebDriver driver) {
		
		String msg = null;
		try{
		String pageSource = driver.getPageSource();
		Document document = Jsoup.parse(pageSource);
		Element message = document.getElementById("item-error");
		msg = message.text();
		}catch(Exception e){
			
		}
		return msg;
	}

	/**
	 * 获取验证码
	 * @param driver
	 * @param imgcode
	 * @return
	 */
	private static String getImageCode(WebDriver driver,WebElement imgcode) {
		try{
		//让整个页面截图
		File screenshotAs = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		BufferedImage bufferedImage = ImageIO.read(screenshotAs);
		
		//获取页面上元素的位置
		Point point = imgcode.getLocation();
		
		int width = imgcode.getSize().getWidth();
		int height = imgcode.getSize().getHeight();
		
		//裁剪整个页面的截图，以获得元素的屏幕截图
		BufferedImage subimage = bufferedImage.getSubimage(point.getX(), point.getY(), width, height);
		
		ImageIO.write(subimage, "png", screenshotAs);
		
		//将元素截图复制到磁盘
		File file = new File("c:\\douban.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("2000", "c:\\douban.png");
		
		return code;
		
		}catch(Exception e){
			
			e.printStackTrace();
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
