package com.longriver.netpro.webview.controller;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.alibaba.fastjson.JSON;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
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
import com.longriver.netpro.fetchScript.util.Jdbc2MysqlSpcard;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.MsgUtil;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 一点资讯
 */
public class YDZXComment {
	
	private static int codetimes = 0;
	public static void main(String arsg[]){
		try{
			//手机号账号,前面需加86
			TaskGuideBean task = new TaskGuideBean();
			task.setNick("17173425504");
			task.setPassword("chwx090674542");
			task.setNick("17173425505");
			task.setPassword("chwx123456");
			
			task.setAddress("https://m.huanqiu.com/r/MV8wXzExNjQ3MjIzXzIxMjJfMTUyMDM5MTYwMA==?__from=yidian&yidian_docid=0IUEB5oC&yidian_s=&yidian_appid=");
			task.setAddress("https://www.yidianzixun.com/article/0IUJ3bjb");
			task.setCorpus("心更难咋样");
//			toRun(task);//一点账号登录
			
			toComment(task);//微博登录
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 一点资讯评论--微博登录
	 * @param task
	 */
	public static Map<String,String> getCookie(TaskGuideBean task) {
	
		WebDriver driver = null;
		
		Map<String,String> paramMap = new HashMap<String,String>();
		try {
			driver = getDriver();
			
			if(driver == null){
				
//				isSuccess(task, "driver打开失败");
				paramMap.put("msg", "driver打开失败");
				return paramMap;
			}
			
			driver.get("https://mp.yidianzixun.com/");
			
			driver.findElement(By.xpath("//*[@id='app']/div[2]/div[1]/div/div[2]/form/a[1]")).click();
			
//			Thread.sleep(2000);
			
			driver.findElement(By.xpath("//*[@id='app']/div[2]/div[1]/div/div[3]/div/ul/li[3]/a")).click();
			
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
						if(driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText().contains("输入的验证码不正确")){
							
							codetimes++;
							driver.quit();
							getCookie(task);
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
						if(driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText().contains("输入的验证码不正确")){
							
							codetimes++;
							driver.quit();
							getCookie(task);
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
						if(driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText().contains("输入的验证码不正确")){
							
							codetimes++;
							driver.quit();
							getCookie(task);
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
			
			
			if(driver.getCurrentUrl().contains("api.weibo.com/oauth2/authorize")){
				
				driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[2]/div/p/a[1]")).click();
				
				Thread.sleep(2000);
			}
			String yidianUrl = driver.getCurrentUrl();
			
			if(yidianUrl.contains("www.yidianzixun.com")){
				
				Set<Cookie> cookies = driver.manage().getCookies();
				
				String skeyCookie = "";
				for (Cookie cookie : cookies) {
					
					skeyCookie += cookie.toString().substring(0,cookie.toString().indexOf(";")) + "; ";
				}
				
				paramMap.put("cookie", skeyCookie);
				return paramMap;
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}finally{
			
			driver.quit();
		}
		return null;
		
	}
	
	/**
	 * 一点资讯评论
	 * @param task
	 */
	@SuppressWarnings("null")
	public static void toComment(TaskGuideBean task){
		try {
			Map<String,String> paramMap = getCookie(task);
			
			if(StringUtils.isBlank(paramMap.get("cookie"))){
				
				isSuccess(task, paramMap.get("msg"));
				return;
			}
			
			String address = URLDecoder.decode(task.getAddress(),"utf-8");
			URL url = new URL("http://www.yidianzixun.com/home/q/addcomment");
			HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
			
			String docid = getDocid(address);
			
			String param = "docid="+docid+"" +
					"&comment="+URLEncoder.encode(task.getCorpus(),"utf-8")+"" +
					"&reply=" +
					"&s=";
			
			openConnection.addRequestProperty("Host", "www.yidianzixun.com");
			openConnection.addRequestProperty("Connection", "keep-alive");
			openConnection.addRequestProperty("Content-Length", String.valueOf(param.length()));
			openConnection.addRequestProperty("Accept", "*/*");
			openConnection.addRequestProperty("Origin", "http://www.yidianzixun.com");
			openConnection.addRequestProperty("X-Requested-With", "XMLHttpRequest");
			openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.75 Safari/537.36");
			openConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			openConnection.addRequestProperty("Referer", address);
			openConnection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
			openConnection.addRequestProperty("Cookie",paramMap.get("cookie"));
			
			openConnection.setDoInput(true);
			openConnection.setDoOutput(true);
			openConnection.setRequestMethod("POST");
			openConnection.setUseCaches(false);
			
			
			PrintWriter pw = new PrintWriter(openConnection.getOutputStream());
			pw.print(param);
			pw.flush();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(openConnection.getInputStream(),"utf-8"));
			
			String line;
			StringBuffer sb = new StringBuffer();
			
			while((line = br.readLine())!=null){
				sb.append(line);
			}
			
			System.out.println(sb.toString());
			
			JSONObject parseObject = JSON.parseObject(sb.toString());
			String status = parseObject.getString("status").trim();
			
			if(StringUtils.isNotBlank(status) && "success".equals(status)){
				
				isSuccess(task, "");
			}else{
				
				isSuccess(task, "发帖失败");
			}
		} catch (Exception e) {
			isSuccess(task, "发帖异常了");
			e.printStackTrace();
			
		}
		
	}
	/**
	 * 获取文章id
	 * @param address
	 * @return
	 */
	private static String getDocid(String address) {
		
		String docid = "";
		try {
			
			if(address.indexOf("article")>-1){
				docid = address.substring(address.indexOf("article")+8);
			}else{
				docid = address.substring(address.indexOf("yidian_docid")+13,address.indexOf("&yidian_s"));
			}
			return docid;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return docid;
	}
	/**
	 * 获取验证码
	 * @param driver
	 * @throws IOException 
	 */
	private static String getPicYidian(WebDriver driver,WebElement comment) throws IOException {
		
		File screenshotAs = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		BufferedImage bufferedImage = ImageIO.read(screenshotAs);
		
		Point point = comment.getLocation();
		
		int width = comment.getSize().getWidth();
		int height = comment.getSize().getHeight();
		if(width<=0) width=300;
		if(height<=0) height=100;
		BufferedImage subimage = bufferedImage.getSubimage(point.getX(), point.getY(), width, height);
		
		ImageIO.write(subimage, "png", screenshotAs);
		
		File file = new File("d:\\yidian.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("3040", "d:\\yidian.png");
		
		return code;
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
		
		File file = new File("d:\\toutiao.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("3000", "d:\\toutiao.png");
		
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
			
//		System.setProperty("phantomjs.binary.path", phantomjsUrl);
//		driver = new PhantomJSDriver();
			System.setProperty("webdriver.firefox.bin",firefoxUrl);
			driver = new FirefoxDriver();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return driver;
	}
	/**
	 * 判断是否成功
	 */
	public static void isSuccess(TaskGuideBean task,String msg){
			MQSender.toMQ(task,msg);
	}
	
	
	@SuppressWarnings("null")
	public static void toRun(TaskGuideBean taskdo){
		try {
			String userId =URLEncoder.encode(taskdo.getNick(),"utf-8");
			String pwd = taskdo.getPassword();
			String contents = taskdo.getCorpus();
			String sUrl =taskdo.getAddress();
			int a=sUrl.lastIndexOf("&up");
			String id=sUrl.substring(a-8,a);
	        System.out.println(id);
			String cookie="";
			
		    URL u1 = new URL("http://www.yidianzixun.com/mp_sign_in");
			String fa = "password="+pwd+"&username="+userId;
			HttpURLConnection c1 = (HttpURLConnection) u1.openConnection();
			c1.addRequestProperty("Host", "www.yidianzixun.com");
			c1.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0");
			c1.addRequestProperty("Accept","*/*");
			c1.addRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
			c1.addRequestProperty("Referer", "http://www.yidianzixun.com/home");
			c1.addRequestProperty("X-Requested-With", "XMLHttpRequest");
			c1.addRequestProperty("Content-Length", String.valueOf(fa.length()));
			c1.setInstanceFollowRedirects(true);
			c1.setDoInput(true);
			c1.setDoOutput(true);
			PrintWriter fo = new PrintWriter(c1.getOutputStream());
			fo.print(fa);
			fo.flush();
			InputStream fi1 = c1.getInputStream();
			Scanner fs1 = new Scanner(fi1,"utf-8");
			while(fs1.hasNext()){
				String scsc2 = fs1.nextLine();
				System.out.println(scsc2);
				if(scsc2.contains("failed")){
					MQSender.toMQ(taskdo,"失败");
					return ;
				}
			}
			Map<String, List<String>> m1 = c1.getHeaderFields();
			for(Map.Entry<String,List<String>> entry : m1.entrySet()){
				if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
					for(String value : entry.getValue()){
						if(value.substring(0, value.indexOf(";")).equals("JSESSIONID=")){
							continue;
						}else{
							cookie = cookie + value.substring(0, value.indexOf(";")) + ";";
						}
					}
				}
			}
			        URL fu = new URL("http://www.yidianzixun.com/home/q?"
			        		+ "type=addcomment"
			        		+ "&docid="+id
			        		+ "&comment="+URLEncoder.encode(contents, "utf-8"));

					HttpURLConnection fc = (HttpURLConnection) fu.openConnection();
					fc.addRequestProperty("Host", "www.yidianzixun.com");
					fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
					fc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
					fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
					fc.addRequestProperty("Referer", sUrl);
					fc.addRequestProperty("Cookie", cookie);
					fc.addRequestProperty("Connection", "keep-alive");
					fc.setInstanceFollowRedirects(false);
					fc.setDoInput(true);
					fc.setDoOutput(true);
					PrintWriter fo1 = new PrintWriter(fc.getOutputStream());
					fo1.flush();
					InputStream fi = fc.getInputStream();
					Scanner fs = new Scanner(fi,"utf-8");
					String content = "失败";
					while(fs.hasNext()){
						String scsc2 = fs.nextLine();
						System.out.println("result::"+scsc2);
						if(scsc2.contains("success")) content = "";
					}
					MQSender.toMQ(taskdo,"");
		}catch (Exception e) {
			MQSender.toMQ(taskdo,"失败2");
		}
		
	}
	
	
	@Test
	public void getParam(){
		
		String ss = "https://www.yidianzixun.com/article/0IUJ3bjb";
		
		String docid = ss.substring(ss.indexOf("article")+8);
		System.out.println(docid);
	}
}
