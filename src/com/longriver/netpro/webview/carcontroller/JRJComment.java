package com.longriver.netpro.webview.carcontroller;

import java.net.URLDecoder;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 金融界评论
 * @author rhy
 * @date 2018-3-20 上午9:31:13
 * @version V1.0
 */
public class JRJComment {
	
	public static void main(String[] args) {
		//手机号账号,前面需加86
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://stock.jrj.com.cn/2018/03/19125124260509.shtml");
		task.setAddress("http://fund.jrj.com.cn/2018/03/18212824257248.shtml"); 
		task.setAddress("http://stock.jrj.com.cn/2018/03/19112524260159.shtml");
		task.setAddress("http://finance.jrj.com.cn/2018/03/19063324257993.shtml");
		task.setCorpus("首付呀，这个厉害了");
		task.setNick("17080621284");
		task.setPassword("chwx123456");
		toComment(task);
	}
//	/**
//	 * 评论
//	 * @param task
//	 */
//	public static void toComment(TaskGuideBean task) {
//		
//		try {
//			Map<String, String> paramMap = getCookie(task);
//			
//			if(paramMap.get("cookie")==null){
//				
//				isSuccess(task, paramMap.get("msg"));
//				return;
//			}
//			
//			String address = URLDecoder.decode(task.getAddress(),"utf-8");
//			Map<String, String> param = null;
//			String senderId = null;
//			try {
//				param = getCommentParam(paramMap.get("source"));
//				senderId = paramMap.get("cookie");
//				senderId = senderId.substring(senderId.indexOf("myjrj_userid")+13);
//				senderId = senderId.substring(0,senderId.indexOf(";")).trim();
//			} catch (Exception e) {
//			}
//			
//			URL url = new URL("http://news.comments.jrj.com.cn/index.php/comment?jsoncallback=jQuery1706197894157977528_"+System.currentTimeMillis()+"" +
//					"&appId="+param.get("appId")+"" +
//					"&appItemid="+param.get("appItemid")+"" +
//					"&itemUrl="+param.get("itemUrl")+"" +
//					"&senderId="+senderId+"" +
//					"&replyRootid=" +
//					"&replyToid=" +
//					"&content="+URLEncoder.encode(task.getCorpus(),"utf-8")+"" +
//					"&senderName=" +
//					"&itemTitle="+URLEncoder.encode(param.get("itemTitle"),"utf-8")+"" +
//					"&_="+System.currentTimeMillis());
//			
//			HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
//
//			openConnection.addRequestProperty("Host", "news.comments.jrj.com.cn");
//			openConnection.addRequestProperty("Connection", "keep-alive");
//			openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.75 Safari/537.36");
//			openConnection.addRequestProperty("Accept", "*/*");
//			openConnection.addRequestProperty("Referer", address);
//			openConnection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
//			openConnection.addRequestProperty("Cookie", paramMap.get("cookie"));
//			
//			openConnection.connect();
//			
//			BufferedReader br = new BufferedReader(new InputStreamReader(openConnection.getInputStream(),"utf-8"));
//			
//			String line;
//			StringBuffer sb = new StringBuffer();
//			
//			while((line = br.readLine()) != null){
//				
//				sb.append(line);
//			}
//			
//			System.out.println(sb.toString());
//			
//			JSONObject parseObject = JSON.parseObject(sb.toString());
//			String returnCode = parseObject.getString("returnCode");
//			
//			if(StringUtils.isNotBlank(returnCode) && "1".equals(returnCode)){
//				
//				isSuccess(task, "");
//			}else{
//				
//				isSuccess(task, "发布失败");
//			}
//		} catch (Exception e) {
//			
//			isSuccess(task, "评论异常");
//			e.printStackTrace();
//		}
//		
//	}
//	/**
//	 * 获取评论参数
//	 * @param address
//	 * @return
//	 */
//	private static Map<String, String> getCommentParam(String source) {
//		
//		Map<String,String> param = new HashMap<String,String>();
//		
//		try {
//			source = source.substring(source.indexOf("iiid=")+5);
//			
//			String appItemid = source.substring(source.indexOf("appItemid")+10);
//			appItemid = appItemid.substring(0,appItemid.indexOf(";")).trim();
//			param.put("appItemid", appItemid);
//			
//			String appId = source.substring(source.indexOf("appId")+6);
//			appId = appId.substring(0,appId.indexOf(";")).replaceAll("'", "").trim();
//			param.put("appId", appId);
//			
//			String itemTitle = source.substring(source.indexOf("itemTitle")+11);
//			itemTitle = itemTitle.substring(0,itemTitle.indexOf(";")-1);
//			param.put("itemTitle", itemTitle);
//			
//			String itemUrl = source.substring(source.indexOf("itemUrl")+9);
//			itemUrl = itemUrl.substring(0,itemUrl.indexOf(";")-1);
//			param.put("itemUrl", itemUrl);
//			
//			
//			System.out.println("appItemid==="+appItemid);
//			System.out.println("appId==="+appId);
//			System.out.println("itemTitle=="+itemTitle);
//			System.out.println("itemUrl=="+itemUrl);
//		} catch (Exception e) {
//			
//			
//		}
//		
//		return param;
//	}
	/**
	 * 获取cookie
	 * @param task
	 */
	public static void toComment(TaskGuideBean task) {
		
		WebDriver driver = null;
		try{
		driver = getDriver();
		
		if(driver == null){
			isSuccess(task, "driver打开失败");
			return;
		}
		driver.get("https://sso.jrj.com.cn/sso/ssopassportlogin");
		
		driver.findElement(By.id("txtUsername")).sendKeys(task.getNick());
		driver.findElement(By.id("txtPassword")).sendKeys(task.getPassword());
		
		driver.findElement(By.xpath("/html/body/div[2]/div/div/form/div[7]/a")).click();
		
		try {
			String errorMsg = driver.findElement(By.xpath("/html/body/div[2]/div/div/form/div[2]/span")).getText();
			
			if(StringUtils.isNotBlank(errorMsg)){
				isSuccess(task, errorMsg);
				return;
			}
			
		} catch (Exception e) {
		}
		
		driver.get(URLDecoder.decode(task.getAddress(),"utf-8"));
		
		driver.switchTo().frame("jrjIf_editorContaier_Top");
		
		driver.findElement(By.xpath("/html/body/p")).sendKeys(task.getCorpus());
		driver.switchTo().defaultContent();
		
		driver.findElement(By.xpath("//*[@id='jrje_reply_Top']/div[4]/div/div/button")).click();
	
		Thread.sleep(1000);
		isSuccess(task, "");
		}catch(Exception e){
			
			isSuccess(task, "评论异常了");
		}finally{
			
			driver.quit();
		}
		
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
