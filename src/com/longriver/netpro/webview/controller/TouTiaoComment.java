package com.longriver.netpro.webview.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
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
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.util.DriverGet;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;


/**
 * 头条评论--点赞
 * @author rhy
 * @2017-12-19 下午1:12:16
 * @version v1.1
 */
public class TouTiaoComment {
	
	private static Logger logger = Logger.getLogger(TouTiaoComment.class);
	public static boolean isSend = false;
	
	public static void main(String args[]){
			TaskGuideBean taskdo = new TaskGuideBean();
			
			taskdo.setNick("18311242559");
			taskdo.setPassword("chwx62115358");
			
			taskdo.setAddress("https://www.toutiao.com/a6618142184988213764/");
			taskdo.setAddress("https://www.toutiao.com/i6628186235317781000/?amp=&amp=&amp=&amp=&amp=&amp=&amp=&amp=&amp=&amp=&amp=&amp=&amp=&app=news_article&appinstall=0&article_category=stock&from=groupmessage&group_id=6628186235317781000&iid=52360059339&isappinstalled=0&pbid=6628336369461413389&timestamp=1543272756&tt_from=weixin&utm_campaign=client_share&utm_medium=toutiao_android&utm_source=weixin&wxshare_count=4#comment_area");
			taskdo.setPraiseWho("1618238015363079");
			taskdo.setCorpus("该换手机了");
//			System.out.println(getItemId(taskdo.getAddress()));;
//			toutiaoComment(taskdo);
			toutiaoDigg(taskdo);
			
			
	}
	/**
	 * 评论
	 * @param taskdo
	 */
	public static void toutiaoComment(TaskGuideBean task){
		isSend = true;
		WebDriver driver = DriverGet.getDriver();
		try{
//			String cookie = "sso_login_status=1; uid_tt=da23c3f1c5228443412209151743d428; toutiao_sso_user=c3aae27ef1c08e531dfb30a4e1e9b5bb; sid_tt=9dfc2b5edc2d199b2fb5463f56e5ffb3; sso_uid_tt=a60c86d929cef325b1ef53014ba99fb1; tt_webid=6620256762971735560; sessionid=9dfc2b5edc2d199b2fb5463f56e5ffb3; sid_guard='9dfc2b5edc2d199b2fb5463f56e5ffb3|1541398649|15552000|Sat\054 04-May-2019 06:17:29 GMT'; ccid=c1795fbd9a9e43cab10fc92803732657; login_flag=8c283eccc5f87307bfdaf6da4383a4ee; __tasessionId=cb7zsqrpa1541398650768; csrftoken=34cc037082c5cdfad3974603553760f9; CNZZDATA1259612802=459371704-1541393465-https%253A%252F%252Fsso.toutiao.com%252F%7C1541393465; uuid='w:ba6bab14387b473b9949d36857818196'; WEATHER_CITY=%E5%8C%97%E4%BA%AC; UM_distinctid=166e284c42c2f6-016d5343cf46e4-40524136-15f900-166e284c42d192; ";
			String cookie = getCookie(task,driver);
			if(cookie!=null && cookie.contains("验证码")) cookie = getCookie(task,driver);
			if(cookie!=null && cookie.contains("验证码")) cookie = getCookie(task,driver);
			System.out.println(cookie);
			if(StringUtils.isBlank(cookie)){
				isSuccess(task, "登录失败");
				return;
			}else if(cookie.equals("密码")){
				isSuccess(task, "密码错误");
				return;
			}else if(cookie.equals("验证码错误")){
				isSuccess(task, "验证码错误");
				return;
			}
			int suc = toDis(cookie,task);
			System.out.println("suc=="+suc);
			Thread.sleep(1000*2);
			if(suc==0) suc = toDis(cookie,task);
			System.out.println("suc=="+suc);
			if(suc==1) isSuccess(task, "");
			else isSuccess(task, "发帖失败!");
		}catch(Exception e){
			e.printStackTrace();
			isSuccess(task, "评论错误");
		}finally{
			DriverGet.quit(driver);
		}
	}
	public static int toDis(String cookie,TaskGuideBean task){
		int suc = 1;
		try {
			URL url = new URL("https://www.toutiao.com/api/comment/post_comment/");
			HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
			String itemId = getItemId(task.getAddress());
			System.out.println("itemId=="+itemId);
			String param = "status="+URLEncoder.encode(task.getCorpus(),"utf-8") +
					"&content="+URLEncoder.encode(task.getCorpus(),"utf-8") +
					"&group_id=" +itemId+
					"&item_id=" +itemId+
					"&id=0" +
					"&format=json" +
					"&aid=24";
			System.out.println("param=="+param);
			String csrftoken = null;
			{
				String pro = "csrftoken=";
				int k = cookie.indexOf(pro)+pro.length();
				String kk = cookie.substring(k);
				int t = kk.indexOf(";");
				csrftoken = kk.substring(0,t);
				System.out.println(csrftoken);
			}

			openConnection.addRequestProperty(":authority", "www.toutiao.com");
			openConnection.addRequestProperty(":method", "POST");
			openConnection.addRequestProperty(":path", "/api/comment/post_comment/");
			openConnection.addRequestProperty(":scheme", "https");
			openConnection.addRequestProperty("Accept", "text/javascript, text/html, application/xml, text/xml, */*");
			openConnection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
			openConnection.addRequestProperty("Content-Length", String.valueOf(param.length()));
			openConnection.addRequestProperty("content-type", "application/x-www-form-urlencoded");
			openConnection.addRequestProperty("Cookie", cookie);
			openConnection.addRequestProperty("origin", "https://www.toutiao.com");
			openConnection.addRequestProperty("referer", task.getAddress());
			openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36");
			openConnection.addRequestProperty("X-Requested-With", "XMLHttpRequest");
			//重要
			openConnection.addRequestProperty("x-csrftoken", csrftoken);
			
			openConnection.setDoInput(true);
			openConnection.setDoOutput(true);
			openConnection.setRequestMethod("POST");
			
			PrintWriter pw = new PrintWriter(openConnection.getOutputStream());
			pw.write(param);
			pw.flush();
			
			Scanner sc = new Scanner(openConnection.getInputStream(),"utf-8");
			
			String result = "";
			while(sc.hasNext()){
				result += sc.nextLine();
			}
		} catch (Exception e) {
			suc = 0;
			e.printStackTrace();
		}
		return suc;

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
	private static String getCookie(TaskGuideBean task,WebDriver driver){
		
		try{
			
			String address = task.getAddress();
			
			driver.get("https://sso.toutiao.com/login/");  
			
			driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div/div/ul/li[1]")).click();
			
			driver.findElement(By.id("account")).clear();
			driver.findElement(By.id("account")).sendKeys(task.getNick());
			driver.findElement(By.id("password")).clear();
			driver.findElement(By.id("password")).sendKeys(task.getPassword());
			Thread.sleep(1000);
			
			try {
				WebElement preimgCode = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div/div/form/div[3]/div/img"));
				if(preimgCode.isDisplayed()){
				String precode = getPic(driver, preimgCode);
				driver.findElement(By.id("captcha")).sendKeys(precode);
				}
			} catch (Exception e1) {
			}
			
			driver.findElement(By.name("submitBtn")).click();
			
			String currentUrl = driver.getCurrentUrl();
			
			if(currentUrl.contains("login")){
				String r = "";
				String msg = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div/div/div")).getText();
				if(msg.contains("验证码")){
					r = "验证码错误";
				}else if(msg.contains("密码")){
					r = "账号或密码不匹配";
				}
				return r;
			}
			driver.get(address);
			Thread.sleep(2*1000);
			Set<Cookie> cookies = driver.manage().getCookies();
			StringBuilder sb = new StringBuilder();
			for (Cookie cookie : cookies) {
				System.out.println(cookies.toString());
				sb.append(cookie.toString().substring(0,cookie.toString().indexOf(";")));
				sb.append("; ");
			}
			return sb.toString();
		
		}catch(Exception e){
			
			logger.info("评论异常");
			e.printStackTrace();
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
		File file = new File("c:\\vcode\\toutiao.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("3040", "c:\\vcode\\toutiao.png");
		
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
		WebDriver driver=null;
		try{
			driver = DriverGet.getDriver();;
			String cookie = getCookie(task,driver);
		
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
		String csrftoken = null;
		{
			String pro = "csrftoken=";
			int k = cookie.indexOf(pro)+pro.length();
			String kk = cookie.substring(k);
			int t = kk.indexOf(";");
			csrftoken = kk.substring(0,t);
			System.out.println(csrftoken);
		}

		openConnection.addRequestProperty("Host", "www.toutiao.com");
		openConnection.addRequestProperty("Connection", "keep-alive");
		openConnection.addRequestProperty("Content-Length", String.valueOf(param.length()));
		openConnection.addRequestProperty("Accept", "text/javascript, text/html, application/xml, text/xml, */*");
//		openConnection.addRequestProperty("Origin", "https://www.toutiao.com");
		openConnection.addRequestProperty("X-CSRFToken", csrftoken);
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
		System.out.println("result=="+result);
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
		}finally{
			DriverGet.quit(driver);
		}
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
