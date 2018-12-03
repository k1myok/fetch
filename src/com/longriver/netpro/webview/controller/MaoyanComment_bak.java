package com.longriver.netpro.webview.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
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
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.util.DriverGet;
import com.longriver.netpro.util.KillProcess;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 猫眼引导
 * @author rhy
 * @2017-12-13 上午9:31:21
 * @version v1.0
 */
public class MaoyanComment_bak {

	private static Logger logger = Logger.getLogger(MaoyanComment_bak.class);
	
	public static void main(String[] args) {
		
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://maoyan.com/films/930966");
		task.setCorpus("鬼吹灯挺好听的");
		task.setCorpus("心牵着心吧世界走遍");
		task.setNick("15269114052");
		task.setPassword("owdltz2471g");
		
		task.setNick("18311546774");
		task.setPassword("lx1314");
		
		toComment(task);
	}

	/**
	 * 评论
	 * @param task
	 */
	public static void toComment(TaskGuideBean task) {
		
		try{
			
		String address = URLDecoder.decode(task.getAddress(),"utf-8");
		Map<String,String> paramMap = getCookie(task);
		
		URL url = new URL("http://maoyan.com/ajax/proxy/mmdb/comments/movie/"+paramMap.get("movieId")+".json?utm_medium=organic&uuid="+paramMap.get("uuid")+"&ci=1");
		
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		
		String param = "business=1" +
				"&clientType=web" +
				"&userId="+paramMap.get("userid")+"" +
				"&token="+paramMap.get("token")+"" +
				"&nick="+URLEncoder.encode(paramMap.get("nick"),"utf-8")+"" +
				"&score=5" +
				"&content="+URLEncoder.encode(task.getCorpus(), "utf-8");

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
		openConnection.addRequestProperty("Cookie", paramMap.get("cookie"));
		
		
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
		if(sb.toString().indexOf("登录失败")>-1){
			
			isSuccess(task, "登录失败");
			return;
		}
		JSONObject parseObject = JSON.parseObject(sb.toString());
		String parseData = parseObject.getString("data");
		JSONObject parseId = JSON.parseObject(parseData);
		String id = parseId.getString("id");
		if(StringUtils.isNotBlank(id)){
			
			isSuccess(task, "");
		}else{
			
			isSuccess(task, "评论错误");
		}
		}catch(Exception e){
			
			isSuccess(task, "发生错误");
		}
	}

	/**
	 * 获取cookie及参数
	 * @param task
	 */
	public static Map<String,String> getCookie(TaskGuideBean task) {
		
		WebDriver driver = null;
		try{
		
//			if(task.getCorpus().length()<6){
//				
//				isSuccess(task, "请输入六字以上的短评");
//				return null;
//			}
		String address = URLDecoder.decode(task.getAddress(),"utf-8");
		
		java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config\\properties\\paramsConfig");
		String firefoxUrl = bundle.getString("firefoxurl");
		System.setProperty("webdriver.firefox.bin",firefoxUrl);
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("dom.ipc.plugins.enabled.libflashplayer.so", "false");
//		driver = new FirefoxDriver(profile);
		
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("--start-maximized");
		System.setProperty("webdriver.chrome.driver","C:\\tools\\chromedriver.exe");
		driver = new ChromeDriver(chromeOptions);
		
		driver.get("https://passport.meituan.com/account/unitivelogin?service=www&continue=http%3A%2F%2Fwww.meituan.com%2Faccount%2Fsettoken%3Fcontinue%3Dhttp%253A%252F%252Fbj.meituan.com%252F");
		
		Thread.sleep(2000);
		driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div/a[2]")).click();
		
		Thread.sleep(2000);
		String currentHandle = driver.getWindowHandle();
		Set<String> windowHandles = driver.getWindowHandles();
		for (String handle : windowHandles) {
			
			if(currentHandle.equals(handle)){
				
				continue;
			}else{
				
				driver.switchTo().window(handle);
			}
		}
		driver.findElement(By.xpath("//*[@id='userId']")).sendKeys(task.getNick());
		driver.findElement(By.xpath("//*[@id='passwd']")).sendKeys(task.getPassword());
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[2]/div/p/a[1]")).click();
		
		Thread.sleep(2000);
		try {
			WebElement imgCode = driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/span/img"));
			
			String code = getPic(driver, imgCode);
			driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/input")).sendKeys(code);
			driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[2]/div/p/a[1]")).click();
			
		} catch (Exception e) {
		}
		
		Thread.sleep(3000);
//		try{
//		driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[2]/div/p/a[1]")).click();
//		Thread.sleep(5000);
//		}catch(Exception e){
//			
//		}
		
		String currentUrl = driver.getCurrentUrl();
		if(currentUrl.contains("unitivelogin") || currentUrl.contains("authorize")){
			
			isSuccess(task, "登录失败");
			return null;
		}
//		driver.switchTo().window(currentHandle);
//		driver.switchTo().defaultContent();
		driver.get(address);
		
		Actions action = new Actions(driver);
		
		action.dragAndDrop(driver.findElement(By.xpath("/html/body/div[1]/div/div[3]")), driver.findElement(By.xpath("/html/body/div[1]/div/div[3]/div/ul/li/a"))).perform();
		driver.findElement(By.xpath("/html/body/div[1]/div/div[3]/div/ul/li/a")).click();
		Thread.sleep(5000);
		
		
		String text = driver.findElement(By.xpath("//*[@id='app']/div/div[1]/div/div[2]/div[1]/div[4]/div[2]/a")).getText();
		if("我的短评".equals(text)){
			
			isSuccess(task, "您已经点评过了");
			DriverGet.quit(driver);
			return null;
		}
//		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", By.xpath("//*[@id='app']/div/div[1]/div/div[2]/div[1]/div[4]/div[2]/a")); 
//		driver.findElement(By.xpath("//*[@id='app']/div/div[1]/div/div[2]/div[1]/div[4]/div[2]/a")).click();
//		
//		driver.findElement(By.xpath("//*[@id='comment-form']/div[2]/ul/li[5]/i[2]")).click();
//		
//		driver.findElement(By.xpath("//*[@id='comment-form']/div[3]/textarea")).sendKeys(task.getCorpus());
//		
//		driver.findElement(By.xpath("//*[@id='comment-form']/input[2]")).click();
		
		Map<String,String> paramMap = new HashMap<String,String>();
		String pageSource = driver.getPageSource();
		
		pageSource = pageSource.substring(pageSource.indexOf("window.system"));
		
		String userid = pageSource.substring(pageSource.indexOf("id")+2);
		userid = userid.substring(0,userid.indexOf(",")).replaceAll("\"", "").replaceAll(":", "").trim();
		paramMap.put("userid", userid);
		
		String nick = pageSource.substring(pageSource.indexOf("username")+8);
		nick = nick.substring(0, nick.indexOf(",")).replaceAll("\"", "").replaceAll(":", "").trim();
		paramMap.put("nick", nick);
		
		Set<Cookie> cookies = driver.manage().getCookies();
		String skeyCookie = "";
		for (Cookie cookie : cookies) {
			
			skeyCookie += cookie.toString().substring(0,cookie.toString().indexOf(";")) + "; ";
		}
		paramMap.put("cookie", skeyCookie);
		
		String uuid = skeyCookie.substring(skeyCookie.indexOf("uuid=")+5);
		uuid = uuid.substring(0, uuid.indexOf(";")).trim();
		paramMap.put("uuid", uuid);
		
		String token = skeyCookie.substring(skeyCookie.indexOf("lt=")+3);
		if(token.indexOf(";")>-1){
		token = token.substring(0, token.indexOf(";")).trim();
		}else{
			token = token.substring(0).trim();
		}
		paramMap.put("token", token);
		
		String movieId = address.substring(address.lastIndexOf("/")+1).trim();
		paramMap.put("movieId", movieId);
		
		return paramMap;
		}catch(Exception e){
			
			logger.info("评论异常");
			isSuccess(task, "评论错误");
			e.printStackTrace();
		}finally{
			
		driver.quit();
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
		File file = new File("c:\\maoyan.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("3050", "c:\\maoyan.png");
		
		return code;
		}catch(Exception e){
			
			DriverGet.quit(driver);
			logger.info("获取图片异常"+e);
		}
		return null;
	}
	/**
	 * 判断是否成功
	 */
	public static void isSuccess(TaskGuideBean task,String msg){
		KillProcess.kill();
		MQSender.toMQ(task,msg);
	}
	
	
}
