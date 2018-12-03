package com.longriver.netpro.webview;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Set;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 新浪邮箱信息读取
 * @author rhy
 * @date 2018-3-8 下午4:45:03
 * @version V1.0
 */
public class SinaMail {

	public static void main(String[] args) {
		//手机号账号,前面需加86
		TaskGuideBean task = new TaskGuideBean();
		task.setNick("17173425504@sina.cn");
		task.setPassword("chwx090674542");
		
//		toRun(task);//一点账号登录
		
		getMessage(task);
	}
	
	private static void getMessage(TaskGuideBean task) {

		try {
			
			String cookie = getCookie(task);
//			String cookie = "UOR=mail.sina.com.cn,mail.sina.com.cn,; SINAGLOBAL=117.100.167.217_"+new Date().getTime()+";  Apache=117.100.167.217_"+"+new Date().getTime()+"+"; ULV=1"+new Date().getTime()+":2:2:2:117.100.167.217_"+new Date().getTime()+":"+new Date().getTime()+";  SCF=AtqvClNf0R130piz8wp881L5K_0KA3MzK7da9mI6IP44yYpIkYAR7l9Tu3m0ibrdMlaizqzT98rdRJy67wpG_ig.; SUB=_2A253pXVKDeRhGeBK4lcS9i3NyDmIHXVU0-GCrDV_PUNbm9BeLVH6kW9NR3p-sXIwLPyeOOsl43ZsLNca89TklKM-; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WFqA-flUKxEP4OPGXVE52lN5NHD95QcSh.fe0q0eKefWs4Dqcj_i--Ri-88i-zci--4iKnEi-2ci--ciKnRi-isi--Ni-88iK.Ni--RiKnci-z0; ALF=1552038043; sso_info=v02m6alo5qztKWRk6ClkJSUpZCjmKWRk6SljoOIpZCTmKWRk5iljoOgpZCkmKWRk5ylkJSUpY6TnKWRk6CljoOYpZCjjKadlqWkj5OYtI6TlLONo4yxjLOUwA==;vjlast="+new Date().getTime()+";";
			URL url = new URL("http://m0.mail.sina.com.cn/wa.php?a=list_mail");
			
			HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
			
			String param = "fid=new&order=htime&sorttype=desc&type=0&pageno=1&tag=-1&webmail=1";

			openConnection.addRequestProperty("Host", "m0.mail.sina.com.cn");
			openConnection.addRequestProperty("Connection", "keep-alive");
			openConnection.addRequestProperty("Content-Length", String.valueOf(param.length()));
			openConnection.addRequestProperty("Origin", "http://m0.mail.sina.com.cn");
			openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.75 Safari/537.36");
			openConnection.addRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
			openConnection.addRequestProperty("Accept", "*/*");
			openConnection.addRequestProperty("Referer", "http://m0.mail.sina.com.cn/classic/index.php?fl");
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
			System.out.println(sb.toString());
			
			JSONObject parseObject = JSON.parseObject(sb.toString());
			String data = parseObject.getString("data");
			
			JSONObject parseData = JSON.parseObject(data);
			String maillist = parseData.getString("maillist");
			JSONArray parseArray = JSON.parseArray(maillist);
			for(int i=0;i<parseArray.size();i++){
				
				String mailcontent = parseArray.get(i).toString();
				System.out.println(mailcontent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getCookie(TaskGuideBean task) {
		
		WebDriver driver = null;
		try {
			driver = getDriver();
			
			driver.get("http://mail.sina.com.cn/?from=mail");
			
			driver.findElement(By.id("freename")).sendKeys(task.getNick());
			
			driver.findElement(By.id("freepassword")).sendKeys(task.getPassword());
			
			driver.findElement(By.cssSelector("div a.loginBtn")).click();
			
			try {
				WebElement piccode = driver.findElement(By.className("checkcode"));
				
				String code = getPicCode(driver, piccode);
				driver.findElement(By.id("freecheckcode")).sendKeys(code);
				
				driver.findElement(By.cssSelector("div a.loginBtn")).click();
			} catch (Exception e) {
			}
			
			Thread.sleep(20000);
			Set<Cookie> cookies = driver.manage().getCookies();
			
			String skeyCookie = "";
			for (Cookie cookie : cookies) {
				
				skeyCookie += cookie.toString().substring(0,cookie.toString().indexOf(";")) + "; ";
			}
			
			return skeyCookie;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
//			driver.quit();
		}
		return null;
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
		
		String code = RuoKuai.createByPostNew("3050", "d:\\toutiao.png");
		
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
}
