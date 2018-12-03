package com.longriver.netpro.webview.carcontroller;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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

import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 猫眼引导
 * @author rhy
 * @date 2018-3-13 上午11:36:24
 * @version V1.0
 */
public class MaoyanComment {

	public static void main(String[] args) {
		
		//手机号账号,前面需加86
		TaskGuideBean task = new TaskGuideBean();
		task.setNick("17173425504");
		task.setPassword("chwx123456");
//		task.setNick("17173425505");
//		task.setPassword("chwx123456");
		task.setAddress("http://maoyan.com/films/341138");
		task.setAddress("http://maoyan.com/films/1182552");
		task.setCorpus("没意思了@@@");
		task.setNick("17173425455");
		task.setPassword("chwx123456");
		toComment(task);
	}
	/**
	 * 猫眼评论
	 * @param task
	 */
	public static void toComment(TaskGuideBean task) {
		Map<String, String> paramMap = getCookie(task);
		
		try {
			if(paramMap.get("cookie")==null){
				
				isSuccess(task, paramMap.get("msg"));
				return;
			}
			
			String cookie = paramMap.get("cookie");
			String address = URLDecoder.decode(task.getAddress(),"utf-8");
			String movieId = null;
			try {
				movieId = getMovieId(address);
			} catch (Exception e1) {
				isSuccess(task, "无法获取到电影id");
			}
			
			URL url = new URL("http://maoyan.com/ajax/proxy/mmdb/comments/movie/"+movieId+".json?utm_medium=organic&uuid="+UUID.randomUUID().toString().replaceAll("-", "").toUpperCase()+"&ci=1");
			HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();

			Map<String,String> systemMap = null;
			try {
				
//				systemMap = getSystemParam(paramMap.get("pageSource"));
				systemMap = getSystemParam(paramMap.get("cookie"));
			} catch (Exception e) {
				
				isSuccess(task, "获取参数错误");
			}
			String param = "business=1" +
					"&clientType=web" +
					"&userId="+systemMap.get("id")+""+
					"&token="+systemMap.get("token")+"" +
					"&nick="+systemMap.get("nick")+"" +
					"&score=5" +
					"&content="+URLEncoder.encode(task.getCorpus(),"utf-8");
			
			openConnection.addRequestProperty("Host", "maoyan.com");
			openConnection.addRequestProperty("Connection", "keep-alive");
			openConnection.addRequestProperty("Content-Length", String.valueOf(param.length()));
			openConnection.addRequestProperty("Accept", "*/*");
			openConnection.addRequestProperty("Origin", "http://maoyan.com");
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
			
			BufferedReader br = new BufferedReader(new InputStreamReader(openConnection.getInputStream(),"utf-8"));
			
			String line;
			StringBuffer sb = new StringBuffer();
			while((line = br.readLine()) != null){
				
				sb.append(line);
			}
		
			isSuccess(task, "");
		} catch (Exception e) {
			
			isSuccess(task, "请联系开发人员^^^");
			e.printStackTrace();
		}
		
	}
	/**
	 * 获取电影id
	 * @param address
	 * @return
	 */
	private static String getMovieId(String address) {
		
		String movieId = "";
		try {
			
			movieId = address.substring(address.lastIndexOf("/")+1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return movieId;
	}
	/**
	 * 获取评论参数
	 * @param pageSource
	 * @return
	 */
	private static Map<String, String> getSystemParam(String pageSource) {
		
		try {
			
			Map<String,String> systemMap = new HashMap<String,String>();
//			pageSource = pageSource.substring(pageSource.indexOf("window.system"));
//			
//			String id = pageSource.substring(pageSource.indexOf("id")+4,pageSource.indexOf("token")-2).trim();
//			
//			String token = pageSource.substring(pageSource.indexOf("token")+8,pageSource.indexOf("username")-3).trim();
//			
//			String nick = pageSource.substring(pageSource.indexOf("username")+11,pageSource.indexOf("profile")-3).trim();
			
			String id = pageSource.substring(pageSource.indexOf("u=")+2,pageSource.indexOf("LREF=")-2).trim();
			
			String token = pageSource.substring(pageSource.indexOf("lt=")+3,pageSource.indexOf("SERV=")-2).trim();
			
			String nick = pageSource.substring(pageSource.indexOf("n=")+2,pageSource.indexOf("mtcdn=")-2).trim();
			
			systemMap.put("id", id);
			systemMap.put("token", token);
			systemMap.put("nick", nick);
			
			return systemMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取cookie
	 * @param task
	 */
	private static Map<String,String> getCookie(TaskGuideBean task) {
		
		Map<String,String> paramMap = new HashMap<String, String>();
		WebDriver driver = null;
		try {
			driver = getDriver();
			
			if(driver == null){
				
				paramMap.put("msg","driver打开失败" );
				return paramMap;
			}
			
			driver.get("https://passport.meituan.com/account/unitivelogin");
			
			driver.findElement(By.id("login-email")).sendKeys(task.getNick());
			
			driver.findElement(By.id("login-password")).sendKeys(task.getPassword());
			
			driver.findElement(By.xpath("//*[@id='J-normal-form']/div[6]/input[4]")).click();
			
			Thread.sleep(5000);
			try {
				if(StringUtils.isNotBlank(driver.findElement(By.xpath("//*[@id='J-normal-form']/div[1]")).getText())){
					
					paramMap.put("msg", driver.findElement(By.xpath("//*[@id='J-normal-form']/div[1]")).getText());
					return paramMap;
				}
			} catch (Exception e) {
			}
			
			paramMap.put("pageSource", driver.getPageSource());
			
			Set<Cookie> cookies = driver.manage().getCookies();
			
			String skeyCookie = "";
			for (Cookie cookie : cookies) {
				
				skeyCookie += cookie.toString().substring(0,cookie.toString().indexOf(";")) + "; ";
			}
			paramMap.put("cookie", skeyCookie);
			
			return paramMap;
		} catch (Exception e) {
			
			return paramMap;
		}finally{
			
			driver.quit();
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
		
		File file = new File("d:\\maoyan.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("3040", "d:\\maoyan.png");
		
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
		 * 判断是否成功
		 */
		public static void isSuccess(TaskGuideBean task,String msg){
				MQSender.toMQ(task,msg);
		}
	
}
