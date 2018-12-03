package com.longriver.netpro.webview.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.util.DriverGet;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 天涯评论
 * @author rhy
 * @2017-11-16 下午2:53:05
 * @version v1.0
 */
public class TianyaComment {

	private static Logger logger = LoggerFactory.getLogger(TianyaComment.class);
	
	public static void main(String[] args) {
		
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://bbs.tianya.cn/post-funinfo-7580679-1.shtml");
		task.setAddress("http://bbs.tianya.cn/post-worldlook-1811660-1-1.shtml");
		task.setAddress("http://bbs.tianya.cn/post-1095-648345-1-1.shtml");
		task.setCorpus("这个版本还行吗");
		task.setCorpus("未来的预言家");
		task.setCorpus("看怎么说呢");
		task.setNick("15561863090");
		task.setPassword("chwx1234");
		toComment(task);
	}

	/**
	 * 评论
	 * @param task
	 */
	public static void toComment(TaskGuideBean task) {
		
		try{
		String address = URLDecoder.decode(task.getAddress(),"utf-8");
		String cookie = getCookie(task);
		Map<String,String> paramMap = getParam(address);
		
		URL url = new URL("http://bbs.tianya.cn/api?method=bbs.ice.reply");
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		
//		String param = "params.artId=1773829" +
//				"&params.item=45" +
//				"&params.appId=bbs" +
//				"&params.appBlock=45" +
//				"&params.postId=1773829" +
//				"&params.preUrl=http%3A%2F%2Fbbs.tianya.cn%2Fpost-45-1773829-1-1.shtml" +
//				"&params.preTitle=%E4%BB%8A%E5%A4%A9%E5%B1%85%E7%84%B6%E6%9C%89%E5%90%8C%E5%AD%A6%E8%AF%B4%E9%87%8D%E5%BA%86%E5%B8%82%E6%98%AF%E4%B8%80%E7%BA%BF%E5%9F%8E%E5%B8%82%E2%80%A6%E2%80%A6" +
//				"&params.preUserId=13976075" +
//				"&params.preUserName=%E4%BC%AA%E8%A3%85di%E5%9D%9A%E5%BC%BA" +
//				"&params.prePostTime=1510765123000" +
//				"&params.action=f0.1510815039782.0%2C%2Cu19.70.6%2Cu20.85.34%2Cd21.229.135%2Cu22.76.89%2Cd23.229.34%2Cu24.69.101%2Cd25.229.216%2Cu26.32.96%2Cb27.11.2031%2Cd10.229.97%2Cu11.72.38%2Cd12.229.27%2Cu13.83.61%2Cu14.73.88%2Cd15.229.128%2Cu16.32.96%2Cd17.229.242%2Cd18.229.135%7Cc1099d216c355b573ce3ccfb8a56e18d%7C61faca1caedf304161bcae999ade2f7b%7CMozilla%2F5.0+(Windows+NT+10.0%3B+WOW64)+AppleWebKit%2F537.36+(KHTML%2C+like+Gecko)+Chrome%2F62.0.3202.75+Safari%2F537.36%7Cv2.2" +
//				"&params.sourceName=%E5%A4%A9%E6%B6%AF%E8%AE%BA%E5%9D%9B" +
//				"&params.type=3" +
//				"&params.bScore=true" +
//				"&params.content="+URLEncoder.encode("哈哈哈","utf-8")+"" +
//				"&params.title="+URLEncoder.encode("hahhahah","utf-8")+"" +
//				"&params.bWeiBo=false";
		String param = "params.artId="+paramMap.get("artId")+"" +
				"&params.item="+paramMap.get("item")+"" +
				"&params.appId="+paramMap.get("appId")+"" +
				"&params.appBlock="+paramMap.get("appBlock")+"" +
				"&params.postId="+paramMap.get("postId")+"" +
				"&params.preUrl="+URLEncoder.encode(paramMap.get("preUrl"),"utf-8")+"" +
				"&params.preTitle="+URLEncoder.encode(paramMap.get("preTitle"),"utf-8")+"" +
				"&params.preUserId="+paramMap.get("preUserId")+"" +
				"&params.preUserName="+URLEncoder.encode(paramMap.get("preUserName"),"utf-8")+"" +
				"&params.prePostTime="+paramMap.get("posttime")+"" +
				"&params.action="+
				"&params.sourceName=%E5%A4%A9%E6%B6%AF%E8%AE%BA%E5%9D%9B" +
				"&params.type=3" +
				"&params.bScore=true" +
				"&params.content="+URLEncoder.encode(task.getCorpus(),"utf-8")+"" +
				"&params.title="+URLEncoder.encode(task.getCorpus(),"utf-8")+"" +
				"&params.bWeiBo=false";

		openConnection.addRequestProperty("Host", "bbs.tianya.cn");
		openConnection.addRequestProperty("Connection", "keep-alive");
		openConnection.addRequestProperty("Content-Length", String.valueOf(param.length()));
		openConnection.addRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		openConnection.addRequestProperty("Origin", "http://bbs.tianya.cn");
		openConnection.addRequestProperty("X-Requested-With", "XMLHttpRequest");
		openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.75 Safari/537.36");
		openConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		openConnection.addRequestProperty("Referer", address);
		openConnection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
//		openConnection.addRequestProperty("Cookie", "__cid=1; __guid=230433294; __guid2=230433294; ADVS=35ab2827ea7e16; __cfduid=d4dc2965b841e5dceac2e8b7cb70d76dc1510636277; vk=b75963b8e43d81e6; sso=r=1636415444&sid=&wsid=CC2C273BBCFF2E861182D9ACFCFD9583; user=w=%u9006%u6d4102&id=134217895&f=1; temp=k=108809066&s=&t=1510814477&b=2536846dfba2d9c6ac8babf4aba206a9&ct=1510814477&et=1513406477; right=web4=n&portal=n; temp4=rm=889bb32948107c599c1c11817996252c; wl_visit=u=8385323482; Hm_lvt_80579b57bf1b16bdf88364b13221a8bd=1510814181,1510814417,1510814478,1510814550; Hm_lpvt_80579b57bf1b16bdf88364b13221a8bd=1510814550; ASL=17486,000no,7a469153747587f1; bc_ids_w=5c; bc_exp=0.2; bbs_msg=1510815022072_134217895_0_0_0_0; ty_msg=1510815022122_134217895_2_0_0_0_0_0_2_0_0_0_0_0; vip=108809066%3D0; ADVC=35ab2827ea7e16; tianya1=118768,1510814973,1,86400; time=ct=1510815026.151; __asc=c766a2e015fc38ae80b3451b615; __auc=c766a2e015fc38ae80b3451b615; u_tip=; __ptime=1510815026564; Hm_lvt_bc5755e0609123f78d0e816bf7dee255=1510636378,1510636409,1510636418,1510814485; Hm_lpvt_bc5755e0609123f78d0e816bf7dee255=1510815027; __u_a=v2.2.1");
		openConnection.addRequestProperty("Cookie", cookie);
		
		openConnection.setDoInput(true);
		openConnection.setDoOutput(true);
		openConnection.setRequestMethod("POST");
		openConnection.setUseCaches(false);
		
		
		PrintWriter pw = new PrintWriter(openConnection.getOutputStream());
		pw.print(param);
		pw.flush();
		
		if(openConnection.getResponseCode()!=HttpURLConnection.HTTP_OK){
			
			logger.info("状态码异常");
			isSuccess(task, "状态码异常");
			return;
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(openConnection.getInputStream(),"utf-8"));
		String line;
		StringBuffer sb = new StringBuffer();
		while((line=br.readLine())!=null){
			sb.append(line);
		}
		
		JSONObject parseObject = JSON.parseObject(sb.toString());
		String msg = parseObject.getString("success");
		
		if(msg != null && "1".equals(msg)){
			
			logger.info("评论成功");
			isSuccess(task, "");
		}else if(msg != null && "0".equals(msg)){
			
			String message = parseObject.getString("message");
			logger.info(message);
			isSuccess(task, message);
		}else{
			
			isSuccess(task, "未知错误");
		}
		}catch(Exception e){
			
			logger.info("评论异常");
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取评论参数
	 * @param address
	 * @return
	 */
	private static Map<String, String> getParam(String address) {

		try{
		Map<String,String> paramMap = new HashMap<String, String>();
		
		URL url = new URL(address);
		URLConnection openConnection = url.openConnection();
		openConnection.connect();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(openConnection.getInputStream(),"utf-8"));
		
		String line;
		StringBuffer sb = new StringBuffer();
		while((line = br.readLine())!=null){
			
			sb.append(line).append("\n");
		}
		String html = sb.toString();
		String result = html.substring(html.indexOf("bbsGlobal")+11);
		result = result.substring(0,result.indexOf(";"));
		
		JSONObject parseObject = JSON.parseObject(result);
		String artId = parseObject.getString("artId");
		paramMap.put("artId",artId );
		
		String item = parseObject.getString("item");
		paramMap.put("item",item );
		
		String appId = html.substring(html.indexOf("js_appid")+9,html.indexOf("js_blockid")).replaceAll("\"", "").trim();
		paramMap.put("appId",appId );
		
		String appBlock = html.substring(html.indexOf("js_blockid")+11,html.indexOf("js_postid")).replaceAll("\"", "").trim();
		paramMap.put("appBlock",appBlock );
		
		String postId = html.substring(html.indexOf("js_postid")+10,html.indexOf("js_posttime")).replaceAll("\"", "").trim();
		paramMap.put("postId", postId);
		
		String preUrl = html.substring(html.indexOf("js_activityurl")+15,html.indexOf("js_blockname")).replaceAll("\"", "").trim();
		paramMap.put("preUrl",preUrl );
		
		String preTitle = html.substring(html.indexOf("js_title")+9,html.indexOf("js_title_gbk")).replaceAll("\"", "").trim();
		paramMap.put("preTitle",preTitle );
		
		String preUserId = html.substring(html.indexOf("js_activityuserid")+18,html.indexOf("js_replytime")).replaceAll("\"", "").trim();
		paramMap.put("preUserId", preUserId);
		
		String preUserName = html.substring(html.indexOf("js_activityusername")+20,html.indexOf("js_activityusername_gbk")).replaceAll("\"", "").trim();
		paramMap.put("preUserName", preUserName);
		
		String posttime = html.substring(html.indexOf("js_posttime")+12,html.indexOf("js_title")).replaceAll("\"", "").trim();
		paramMap.put("posttime", posttime);
		
		return paramMap;
		}catch(Exception e){
			
			logger.info("获取参数异常");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 登录
	 */
	@Test
	public void toLogin(){
		java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config\\properties\\paramsConfig");
		String firefoxUrl = bundle.getString("firefoxurl");
		
		System.setProperty("webdriver.firefox.bin",firefoxUrl);
//		WebDriver driver = new FirefoxDriver(); 
		WebDriver driver = new HtmlUnitDriver(true); 
		
		driver.get("http://bbs.tianya.cn/");
		
		WebElement preLoginButton = driver.findElement(By.id("js_login"));
		preLoginButton.click();
		
		WebElement username = driver.findElement(By.id("vwriter"));
		username.clear();
		username.sendKeys("15561863090");
		
		WebElement password = driver.findElement(By.id("vpassword"));
		password.clear();
		password.sendKeys("chwx1234");
		
		WebElement loginButton = driver.findElement(By.xpath("//*[@id='topguideloginform']/div[4]/button"));
		loginButton.click();
		
		Set<Cookie> cookies = driver.manage().getCookies();
		
		String skeyCookie = "";
		for (Cookie cookie : cookies) {
			
			skeyCookie += cookie.toString().substring(0,cookie.toString().indexOf(";")) + "; ";
		}
		
		System.err.println("----------------------------");
		System.out.println(skeyCookie);
	}
	
	/**
	 * 获取cookie
	 * @param task
	 * @return
	 */
	public static String getCookie(TaskGuideBean task){
		
		WebDriver driver = null;
		try{
		java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config\\properties\\paramsConfig");
		String firefoxUrl = bundle.getString("firefoxurl");
		
		System.setProperty("webdriver.firefox.bin",firefoxUrl);
		driver = new FirefoxDriver(); 
		
		driver.manage().window().setPosition(new Point(1000, 0));
		driver.manage().window().setSize(new Dimension(0, 0));
		driver.get("https://passport.tianya.cn/m/login.jsp");
		
		WebElement username = driver.findElement(By.id("user_name"));
		username.clear();
		username.sendKeys(task.getNick());
		
		WebElement password = driver.findElement(By.id("password"));
		password.clear();
		password.sendKeys(task.getPassword());
		
		WebElement loginButton = driver.findElement(By.xpath("//*[@id='page_login']/form/div[1]/button"));
		loginButton.click();
		
		Set<Cookie> cookies = driver.manage().getCookies();
		
		String skeyCookie = "";
		for (Cookie cookie : cookies) {
			
			skeyCookie += cookie.toString().substring(0,cookie.toString().indexOf(";")) + "; ";
		}
		
		DriverGet.quit(driver);
		return skeyCookie;
		}catch(Exception e){
			
			logger.info("获取cookie异常");
			DriverGet.quit(driver);
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
