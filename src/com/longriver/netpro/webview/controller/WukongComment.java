package com.longriver.netpro.webview.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
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
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.common.sina.WeiboSina;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.MsgUtil;
import com.longriver.netpro.webview.cookieController.WeiboPraiseCookie;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 悟空问答
 * @author rhy
 * @date 2018-7-11 上午10:51:27
 * @version V1.0
 */
public class WukongComment{

	/**
	 * 17173425524   3
	 * 17173425525   5
	 * 17173425525   4
	 * @param args
	 */
	public static void main(String[] args) {
		TaskGuideBean task = new TaskGuideBean();
		task.setNick("17194510840");
		task.setPassword("CHwx12345");
		task.setCorpus("每个男生都想拥有一本属于自己的恋爱秘籍，然后希望有大波大波的妹子像你招手，下面是恋爱高手给出的恋爱秘籍，超经典的，男生千万不要错过，错过可能后悔一辈子。"+
						"1、如果你想成长，就必须记住一点，想办法让女人去买单吧。"+
						"2、如果你不打算跟她结婚，你就别在她身上花那么多的时间和金钱，不要因为一个树 而失去整个森林。"+
						"3、现在的女人都走两个极端:要么极端自恋要么极端自卑。"+
						"4、互动的最高境界，就是瞎扯淡，看到什么物件，就乱编什么故事。"+
						"5、太正经的老实男人100%泡不到美女，正如人们常说“男人不坏，女人不爱”。"+
						"6、女人永远都觉得男人穿西装最帅了。"+
						"7、泡妞最高理想是泡一个“美女淑女”三合为一的女人做老婆，但是现实的情况证明这是一个“永远无法实现的共产主义社会”，许多男人能泡到有其中一项的女人都算是高手中的高手了。"+
						"8、不要去迎合妞的时间，让妞主动迎合你，因为不忙的男人，都是低价值的。" +
						"8、不要去迎合妞的时间，让妞主动迎合你，因为不忙的男人，都是低价值的。" +
						"9、别天天研究理论，拿出勇气投入实际上去 要知道一般能写出很牛逼理论的人，都泡不到妞。" +
						"10、除了手表鞋子要贵一点，其他的，干净得体就行了。" +
						"11、学习PUA只是能够让你从感性泡妞升级到理性泡妞，减少失败，但是不代表，你就能比平常人牛逼很多。" +
						"12、灰色与黑色是男人永不过时的颜色，你要有男人形象就别穿得花花绿绿象一只“鸭”！" +
						"13、严重提醒务必遵守，别乱搞，任何时候记得做好措施，要不然你会死的很难看。" +
						"14、女人要多用心去体会，而不能太用脑去思考。");
//		task.setCorpus("严重提醒务必遵守，别乱搞，任何时候记得做好措施，要不然你会死的很难看。");
		task.setAddress("https://www.wukong.com/answer/6577144960720568584/");
		task.setIsApp(6);
//		task.setNick("17194510840");
//		task.setPassword("CHwx12345");
		task.setNick("17173425524");
		task.setHostPort("3001");
		
//		task.setNick("17173425525");
//		task.setHostPort("5001");
		
		toComment(task);
	}

	/**
	 * 悟空回答
	 * @param task
	 */
	public static void toComment(TaskGuideBean task) {
		
		WebDriver driver = null;
		
		try{
			
//		int suc = MsgUtil.switchCard(task.getIsApp(),task.getHostPort());
		int suc = 1;
		if(suc==1){
			
			driver = getDriver();
			driver.get("https://sso.toutiao.com/login/?service=https%3A%2F%2Fwww.wukong.com%2Fwenda%2Fwelcome%2F#type=0");//悟空问答登录页
//			driver.findElement(By.cssSelector(".phone-mail > span:nth-child(1)")).click();//点击手机验证码登录
			driver.findElement(By.id("mobile")).sendKeys(task.getNick());//输入手机号
			
			WebElement picCode = driver.findElement(By.xpath("/html/body/div/div/div/div[2]/div/div/form/div[2]/div/img"));
			String imgCode = getPicCode(driver, picCode);
			driver.findElement(By.id("captcha1")).sendKeys(imgCode);//输入图片验证码
			
			driver.findElement(By.xpath("/html/body/div/div/div/div[2]/div/div/form/div[3]/span")).click();//点击获取短信验证码
			
			Thread.sleep(5000);
			String msg = MsgUtil.getMsg(task.getIsApp(),task.getHostPort());
			if(StringUtils.isNotBlank(msg)){
				
				String msgCode = getMsgCode(msg.trim());
				driver.findElement(By.id("code")).sendKeys(msgCode);//短信验证码
			}else{
				
				isSuccess(task, "未能收到短信");
				return;
			}
			
			driver.findElement(By.xpath("/html/body/div/div/div/div[2]/div/div/form/input")).click();
			
			Thread.sleep(2000);
			try {
				String errorMsg = driver.findElement(By.xpath("/html/body/div/div/div/div[2]/div/div/div[1]")).getText();
				if(StringUtils.isNotBlank(errorMsg)){
					
					isSuccess(task, errorMsg);
					return;
				}
			} catch (Exception e) {
			}
			
			driver.get("https://www.wukong.com/");
			driver.get("https://www.wukong.com/question/6540784924968878339/");
			
			String qid = getQid(driver.getPageSource());
			
			Set<Cookie> cookies = driver.manage().getCookies();
			
			String skeyCookie = "";
			for (Cookie cookie : cookies) {
				
				skeyCookie += cookie.toString().substring(0,cookie.toString().indexOf(";")) + "; ";
			}
//			uid_tt=179b254303c80577f6086f457b813fed; answer_enterFrom=; wendacsrftoken=4a62bb6d2e3a1d3454369b7cf52866d6; answer_finalFrom=; _gat=1; cookie_tt_page=3d2d99269c2988057b639d4aca81d7e6; _ga=GA1.2.332062695.1531365500; tt_webid=6577164727536748046; wenda_login_status=1; sessionid=42ce401b918bff3e367e2845fdb9252f; _gid=GA1.2.1616210691.1531365500; 
			
			String tt_webid = skeyCookie.substring(skeyCookie.indexOf("tt_webid")+9);
			tt_webid = tt_webid.substring(0,tt_webid.indexOf(";"));
			String sessionid = skeyCookie.substring(skeyCookie.indexOf("sessionid")+10);
			sessionid = sessionid.substring(0,sessionid.indexOf(";"));
			String uid_tt = skeyCookie.substring(skeyCookie.indexOf("uid_tt")+7);
			uid_tt = uid_tt.substring(0,uid_tt.indexOf(";"));
			
//			skeyCookie += tt_webid + "; "+sessionid +"; "+uid_tt +";";
			System.out.println(skeyCookie);
			postDraft(task,skeyCookie,qid);
			slardarMain(task);
			postAnswer(task,skeyCookie,qid);
//			answerRedirect(task);
			
//			toAnswer(task,skeyCookie,qid);//
//			toDigg(task,skeyCookie,qid);
		}else{
			isSuccess(task, "卡池换卡失败");
			return;
		}


		}catch(Exception e){
			
			isSuccess(task, "回答错误了");
			e.printStackTrace();
		}finally{
			
			if(driver != null){
//				driver.quit();
			}
		}
	}
	
	/**
	 * 回答评论
	 * @param task
	 * @param skeyCookie
	 * @param qid
	 */
	private static void toAnswer(TaskGuideBean task, String cookie,
			String qid) {
		try{
		URL url = new URL("https://www.wukong.com/wenda/web/commit/postcomment/");
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		
		String param = "text="+URLEncoder.encode(task.getCorpus(),"utf-8")+"" +
				"&ansid=6574406867013861635" +
				"&reply_to_comment_id=0" +
				"&t="+System.currentTimeMillis();

		String wendascrftoken = getWendacsrftoken(cookie);
//		:authority: www.wukong.com
//		:method: POST
//		:path: 
//		:scheme: https
//		accept: application/json, text/javascript, */*; q=0.01
//		accept-encoding: gzip, deflate, br
//		accept-language: zh-CN,zh;q=0.9
//		content-length: 96
//		content-type: application/x-www-form-urlencoded; charset=UTF-8
//		cookie: tt_webid=6576778023801243149; tt_webid=6576778023801243149; _ga=GA1.2.992063100.1531275461; _gid=GA1.2.987997961.1531275461; cookie_tt_page=3d2d99269c2988057b639d4aca81d7e6; sessionid=b2fdff75920a7f3e3b9c803ec588b5ef; sessionid=b2fdff75920a7f3e3b9c803ec588b5ef; uid_tt=7c8080b63278496a79311d16530d76f6; uid_tt=7c8080b63278496a79311d16530d76f6; wenda_login_status=1; wendacsrftoken=10f533b0766fcb85a7cb643b317e56ca; answer_finalFrom=; _gat=1; answer_enterFrom=
//		origin: https://www.wukong.com
//		referer: https://www.wukong.com/question/6540784924968878339/
//		user-agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36
//		wendacsrftoken: 10f533b0766fcb85a7cb643b317e56ca
//		x-requested-with: XMLHttpRequest
		connection.addRequestProperty(":authority", "www.wukong.com");
		connection.addRequestProperty(":method", "POST");
		connection.addRequestProperty(":path", "/wenda/web/commit/postcomment/");
		connection.addRequestProperty(":scheme", "https");
		connection.addRequestProperty("accept", "application/json, text/javascript, */*; q=0.01");
		connection.addRequestProperty("accept-language", "zh-CN,zh;q=0.9");
		connection.addRequestProperty("content-length", String.valueOf(param.length()));
		connection.addRequestProperty("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
		connection.addRequestProperty("cookie", cookie);
		connection.addRequestProperty("origin", "https://www.wukong.com");
		connection.addRequestProperty("referer", task.getAddress());
		connection.addRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
		connection.addRequestProperty("wendacsrftoken", wendascrftoken);
		connection.addRequestProperty("x-requested-with", "XMLHttpRequest");
		
		
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		connection.setRequestMethod("POST");
		
		PrintWriter pw = new PrintWriter(connection.getOutputStream());
		pw.print(param);
		pw.flush();
		
		Scanner sc = new Scanner(connection.getInputStream(),"utf-8");
		String result = "";
		while(sc.hasNext()){
			
			result += sc.nextLine();
		}
		System.out.println(result);
		
		}catch(Exception e){
			
			e.printStackTrace();
			isSuccess(task, "发生异常");
		}
	}
	private static void answerRedirect(TaskGuideBean task) {
		try{
		URL url = new URL("https://m.toutiao.com/log/sentry/v2/api/slardar/main/?ev_type=ajax&ax_status=200&ax_type=post&ax_duration=86&ax_size=43&ax_protocol=https&ax_domain=www.wukong.com&ax_path=%2Fwenda%2Fweb%2Fcommit%2Fpostdraft%2F&version=1.0.0&bid=wenda_pc&pid=question&hostname=www.wukong.com&protocol=https&timestamp="+System.currentTimeMillis());
		
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		
		openConnection.addRequestProperty("Referer", task.getAddress());
		openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
		
		openConnection.connect();
		
		Scanner sc = new Scanner(openConnection.getInputStream(),"utf-8");
		String result = "";
		while(sc.hasNext()){
			
			result += sc.nextLine();
		}
		System.out.println(result);
		
		}catch(Exception e){
			
		}
		
	}

	/**
	 * 安全认证
	 * @param task
	 */
	private static void slardarMain(TaskGuideBean task) {
		
		try{
		URL url = new URL("https://m.toutiao.com/log/sentry/v2/api/slardar/main/?ev_type=ajax&ax_status=200&ax_type=post&ax_duration=86&ax_size=43&ax_protocol=https&ax_domain=www.wukong.com&ax_path=%2Fwenda%2Fweb%2Fcommit%2Fpostdraft%2F&version=1.0.0&bid=wenda_pc&pid=question&hostname=www.wukong.com&protocol=https&timestamp="+System.currentTimeMillis());
		
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		
		openConnection.addRequestProperty("Referer", task.getAddress());
		openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
		
		openConnection.connect();
		
		Scanner sc = new Scanner(openConnection.getInputStream(),"utf-8");
		String result = "";
		while(sc.hasNext()){
			
			result += sc.nextLine();
		}
		System.out.println(result);
		
		}catch(Exception e){
			
		}
	}

	/**
	 * 回答方式
	 * @param skeyCookie
	 */
	private static void postAnswer(TaskGuideBean task,String cookie,String qid) {
		try{
		URL url = new URL("https://www.wukong.com/wenda/web/commit/postanswer/");
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		
		String param = "qid="+qid+"" +
				"&content="+URLEncoder.encode("<p>"+task.getCorpus()+"</p>", "utf-8")+"" +
				"&forward_pgc=0" +
				"&enter_from=other" +
				"&final_from=";

		String wendascrftoken = getWendacsrftoken(cookie);
		
		connection.addRequestProperty(":authority", "www.wukong.com");
		connection.addRequestProperty(":method", "POST");
		connection.addRequestProperty(":path", "/wenda/web/commit/postanswer/");
		connection.addRequestProperty(":scheme", "https");
		connection.addRequestProperty("accept", "application/json, text/javascript, */*; q=0.01");
		connection.addRequestProperty("accept-language", "zh-CN,zh;q=0.9");
		connection.addRequestProperty("content-length", String.valueOf(param.length()));
		connection.addRequestProperty("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
		connection.addRequestProperty("cookie", cookie);
		connection.addRequestProperty("origin", "https://www.wukong.com");
		connection.addRequestProperty("referer", task.getAddress());
		connection.addRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
		connection.addRequestProperty("wendacsrftoken", wendascrftoken);
		connection.addRequestProperty("x-requested-with", "XMLHttpRequest");
		
		
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		connection.setRequestMethod("POST");
		
		PrintWriter pw = new PrintWriter(connection.getOutputStream());
		pw.print(param);
		pw.flush();
		
		Scanner sc = new Scanner(connection.getInputStream(),"utf-8");
		String result = "";
		while(sc.hasNext()){
			
			result += sc.nextLine();
		}
//		JSONObject parseObject = JSON.parseObject(result);
//		String err_no = parseObject.getString("err_no");
//		String err_tips = parseObject.getString("err_tips");
//		System.out.println(err_no);
//		System.out.println(err_tips);
		System.out.println(result);
		}catch(Exception e){
			
			e.printStackTrace();
			isSuccess(task, "发生异常");
		}
	}

	/**
	 * 获取wendacsrftoken
	 * @param cookie
	 * @return
	 */
	private static String getWendacsrftoken(String cookie) {
		
		String wendacsrftoken = cookie.substring(cookie.indexOf("wendacsrftoken")+15,cookie.indexOf("answer_finalFrom")).trim().replaceAll(";", "");
		return wendacsrftoken;
	}

	/**
	 * 拖拽方式
	 * @param skeyCookie
	 */
	private static void postDraft(TaskGuideBean task,String cookie,String qid) {
		try{
		URL url = new URL("https://www.wukong.com/wenda/web/commit/postdraft/");
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		
		String param = "qid="+qid+"" +
				"&content="+URLEncoder.encode("<p>"+task.getCorpus()+"</p>","utf-8")+"";

		String wendascrftoken = getWendacsrftoken(cookie);
		
		connection.addRequestProperty(":authority", "www.wukong.com");
		connection.addRequestProperty(":method", "POST");
		connection.addRequestProperty(":path", "/wenda/web/commit/postdraft/");
		connection.addRequestProperty(":scheme", "https");
		connection.addRequestProperty("accept", "*/*");
		connection.addRequestProperty("accept-language", "zh-CN,zh;q=0.9");
		connection.addRequestProperty("content-length", String.valueOf(param.length()));
		connection.addRequestProperty("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
		connection.addRequestProperty("cookie", cookie);
		connection.addRequestProperty("origin", "https://www.wukong.com");
		connection.addRequestProperty("referer", task.getAddress());
		connection.addRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
		connection.addRequestProperty("wendacsrftoken", wendascrftoken);
		connection.addRequestProperty("x-requested-with", "XMLHttpRequest");
		
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		connection.setRequestMethod("POST");
		
		PrintWriter pw = new PrintWriter(connection.getOutputStream());
		pw.print(param);
		pw.flush();
		
		Scanner sc = new Scanner(connection.getInputStream(),"utf-8");
		String result = "";
		while(sc.hasNext()){
			
			result += sc.nextLine();
		}
		JSONObject parseObject = JSON.parseObject(result);
		String err_no = parseObject.getString("err_no");
		String err_tips = parseObject.getString("err_tips");
		System.out.println(err_no);
		System.out.println(err_tips);
		System.out.println(result);
		
		}catch(Exception e){
			
			e.printStackTrace();
			isSuccess(task, "发生异常");
		}
	}

	/**
	 * 获取qid
	 * @param pageSource
	 * @return
	 */
	private static String getQid(String pageSource) {
		String qid = pageSource.substring(pageSource.indexOf("data-qid")+9,pageSource.indexOf("data-init-offset"));
		qid = qid.trim().replaceAll("\"", "");
		return qid;
	}

	/**
	 * 解析短信验证码
	 * @param msg
	 * @return
	 */
	private static String getMsgCode(String msg) {
		System.out.println(msg);
		String msgCode = msg.substring(0,msg.indexOf("今日头条验证码")-1);
		System.out.println("短信验证码------>>>>"+msgCode);
		return msgCode;
	}

	/**
	 * 获取验证码
	 * @param driver
	 * @throws IOException 
	 */
	private static String getPicCode(WebDriver driver,WebElement comment) throws Exception {
		
		File screenshotAs = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		BufferedImage bufferedImage = ImageIO.read(screenshotAs);
		
		Point point = comment.getLocation();
		
		int width = comment.getSize().getWidth();
		int height = comment.getSize().getHeight();
		BufferedImage subimage = bufferedImage.getSubimage(point.getX(), point.getY(), width, height);
		
		ImageIO.write(subimage, "png", screenshotAs);
		
		File file = new File("d:\\wukong.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("3040", "d:\\wukong.png");
		
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
//			ChromeOptions chromeOptions = new ChromeOptions();
//			chromeOptions.addArguments("--start-maximized");
//			System.setProperty("webdriver.chrome.driver","C:\\tools\\chromedriver.exe");
//			driver = new ChromeDriver(chromeOptions);
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
		
		public static void toDigg(TaskGuideBean task,String cookie,String qid){
			try{
				URL url = new URL("https://www.wukong.com/wenda/web/commit/digg/");
				HttpURLConnection connection = (HttpURLConnection)url.openConnection();
				
				String param = "ansid=6574406867013861635";

				String wendascrftoken = getWendacsrftoken(cookie);
//				:authority: www.wukong.com
//				:method: POST
//				:path: 
//				:scheme: https
//				accept: 
//				accept-encoding: gzip, deflate, br
//				accept-language: zh-CN,zh;q=0.9
//				content-length: 25
//				content-type: 
//				cookie: tt_webid=6576778023801243149; tt_webid=6576778023801243149; _ga=GA1.2.992063100.1531275461; _gid=GA1.2.987997961.1531275461; cookie_tt_page=3d2d99269c2988057b639d4aca81d7e6; sessionid=b2fdff75920a7f3e3b9c803ec588b5ef; sessionid=b2fdff75920a7f3e3b9c803ec588b5ef; uid_tt=7c8080b63278496a79311d16530d76f6; uid_tt=7c8080b63278496a79311d16530d76f6; wenda_login_status=1; wendacsrftoken=10f533b0766fcb85a7cb643b317e56ca; answer_finalFrom=; _gat=1; answer_enterFrom=
//				origin: https://www.wukong.com
//				referer: https://www.wukong.com/question/6540784924968878339/
//				user-agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36
//				wendacsrftoken: 10f533b0766fcb85a7cb643b317e56ca
//				x-requested-with: XMLHttpRequest
				connection.addRequestProperty(":authority", "www.wukong.com");
				connection.addRequestProperty(":method", "POST");
				connection.addRequestProperty(":path", "/wenda/web/commit/digg/");
				connection.addRequestProperty(":scheme", "https");
				connection.addRequestProperty("accept", "application/json, text/javascript, */*; q=0.01");
				connection.addRequestProperty("accept-language", "zh-CN,zh;q=0.9");
				connection.addRequestProperty("content-length", String.valueOf(param.length()));
				connection.addRequestProperty("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
				connection.addRequestProperty("cookie", cookie);
				connection.addRequestProperty("origin", "https://www.wukong.com");
				connection.addRequestProperty("referer", task.getAddress());
				connection.addRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
				connection.addRequestProperty("wendacsrftoken", wendascrftoken);
				connection.addRequestProperty("x-requested-with", "XMLHttpRequest");
				
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setUseCaches(false);
				connection.setRequestMethod("POST");
				
				PrintWriter pw = new PrintWriter(connection.getOutputStream());
				pw.print(param);
				pw.flush();
				
				Scanner sc = new Scanner(connection.getInputStream(),"utf-8");
				String result = "";
				while(sc.hasNext()){
					
					result += sc.nextLine();
				}
//				JSONObject parseObject = JSON.parseObject(result);
//				String err_no = parseObject.getString("err_no");
//				String err_tips = parseObject.getString("err_tips");
//				System.out.println(err_no);
//				System.out.println(err_tips);
				System.out.println(result);
				
				}catch(Exception e){
					
					e.printStackTrace();
					isSuccess(task, "发生异常");
				}
		}
	@Test
	public void getValue(){
		getWendacsrftoken(null);
//		tt_webid=6576778023801243149; tt_webid=6576778023801243149; _ga=GA1.2.992063100.1531275461; _gid=GA1.2.987997961.1531275461; answer_finalFrom=; cookie_tt_page=3d2d99269c2988057b639d4aca81d7e6; sessionid=b2fdff75920a7f3e3b9c803ec588b5ef; sessionid=b2fdff75920a7f3e3b9c803ec588b5ef; uid_tt=7c8080b63278496a79311d16530d76f6; uid_tt=7c8080b63278496a79311d16530d76f6; wenda_login_status=1; wendacsrftoken=10f533b0766fcb85a7cb643b317e56ca; _gat=1; answer_enterFrom=
//		tt_webid=6576778023801243149; tt_webid=6576778023801243149; _ga=GA1.2.992063100.1531275461; _gid=GA1.2.987997961.1531275461; answer_finalFrom=; cookie_tt_page=3d2d99269c2988057b639d4aca81d7e6; sessionid=b2fdff75920a7f3e3b9c803ec588b5ef; sessionid=b2fdff75920a7f3e3b9c803ec588b5ef; uid_tt=7c8080b63278496a79311d16530d76f6; uid_tt=7c8080b63278496a79311d16530d76f6; wenda_login_status=1; wendacsrftoken=10f533b0766fcb85a7cb643b317e56ca; _gat=1; answer_enterFrom=
//		msg==2484（今日头条验证码），30分钟内有效。此验证码用于登录你的帐号或更换绑定，验证码提供给他人可能导致帐号被盗。请勿泄露。【今日头条】
	
	}
}
