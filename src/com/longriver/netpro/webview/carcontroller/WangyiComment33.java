package com.longriver.netpro.webview.carcontroller;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.util.DriverGet;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.PngErjinzhi;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 网易引导
 * @author rhy
 * @2017-11-30 下午2:14:14
 * @version v1.0
 */
public class WangyiComment33 {

	private static Logger logger = Logger.getLogger(WangyiComment33.class);
	public static void main(String[] args) {
		
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://news.163.com/17/1130/13/D4GASEUB0001899N.html");
//		task.setAddress("http://bj.news.163.com/18/0321/10/DDDR274504388CSB.html");
		task.setCorpus("就应该这样呢");
//		task.setNick("ri_z_ptt@163.com");
//		task.setPassword("123456eee");
		
//		task.setNick("13338858605");
//		task.setPassword("joy841002");
		
//		task.setNick("augy00@163.com");
//		task.setPassword("qg2048");
		task.setNick("augx50@163.com");
		task.setPassword("odz815");
//		task.setNick("654446543@qq.com");
//		task.setPassword("03197705713");
		toComment(task);
	}
	
	
	/**
	 * 
	 */
	public static void toComment(TaskGuideBean task){
		
		
		WebDriver driver = null;
		try{
			
			java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config\\properties\\paramsConfig");
			String firefoxUrl = bundle.getString("firefoxurl");
		
			
			String address = URLDecoder.decode(task.getAddress(),"utf-8");
			String commentAddress = getCommentAddress(address);
			
			System.setProperty("webdriver.firefox.bin",firefoxUrl);
			driver = new FirefoxDriver();
			
			driver.get("https://3g.163.com/touch/login");
			
			Thread.sleep(5000);
			driver.switchTo().frame(1);
			
			driver.findElement(By.xpath("//*[@id='account-box']/div[2]/input")).sendKeys(task.getNick());
			
			driver.findElement(By.xpath("//*[@id='login-form']/div[1]/div[3]/div[2]/input[2]")).sendKeys(task.getPassword());
			
			driver.findElement(By.id("dologin")).click();
			
			Thread.sleep(5000);
			boolean code = dragCode(driver);
			if(!code){
				task.setCode(10);
				isSuccess(task, "滑动验证码失败");
				return;
			}
			
			driver.findElement(By.id("dologin")).click();
			
			Thread.sleep(3000);
			
			try {
				driver.findElement(By.xpath("//div[@id='cnt-box-parent']/div[3]/div/div[2]/div[2]/a[1]")).click();
				Thread.sleep(3000);
			} catch (Exception e) {
			}
			
			try {
				String userMsg = driver.findElement(By.xpath("//div[@id='cnt-box2']/div/div[2]/div[2]")).getText();//继续登录
				if(StringUtils.isNotBlank(userMsg) && userMsg.contains("手机")){
					
					isSuccess(task, "建议你绑定手机");
					return;
				}
			} catch (Exception e1) {
			}
			
			String currentUrl = driver.getCurrentUrl();
			System.out.println(currentUrl);
			
//			if(!currentUrl.equals("http://reg.163.com/Main.jsp")){
//				
//				isSuccess(task, "登录失败");
//				return;
//			}
			if(!currentUrl.equals("http://3g.163.com/touch/#/")){
				
				isSuccess(task, "登录失败");
				return;
			}
			driver.get(commentAddress);
			Thread.sleep(3000);

			driver.findElement(By.xpath("//*[@id='replyForm']/textarea")).sendKeys(task.getCorpus());
//			driver.manage().window().maximize();
			Thread.sleep(3000);	
			driver.findElement(By.xpath("//*[@id='replyForm']/div/a")).click();
			
			Thread.sleep(4000);
			
			WebElement comment = driver.findElement(By.xpath("//*[@id='mainReplies']/div[2]/div[1]"));
			
//			//根据语料 判断是否成功
			String cc = task.getCorpus();
			if(task.getCorpus().length()>10) cc=task.getCorpus().substring(0, 10);
			System.out.println("comment.getText()=="+comment.getText());
			System.out.println("corpus=="+cc);
			if(comment.getText().contains(cc)){
				System.out.println("截图发帖成功!");
				try {
					//截图
					if(task.getStatus2()==1){
						String picUri = getCommentPic(driver, comment);
						task.setPng(PngErjinzhi.getImageBinary(picUri));
					}
					isSuccess(task, "");
				} catch (Exception e) {
					System.out.println("发帖成功截图失败!");
				}
			}else{
				System.out.println("截图发帖失败!");
				isSuccess(task, "匹配文字失败");
			}
			
			}catch(Exception e){
				
				isSuccess(task, "评论错误");
				e.printStackTrace();
			}finally{
				
				driver.quit();
			}
			
	}
	public static boolean dragCode(WebDriver driver){
		boolean flag = false;
		try {
			for(int m=0;m<6;m++){
				
				driver.findElement(By.xpath("//*[@id='NECaptcha']/div[1]/div[2]/div[1]")).click();
				Thread.sleep(3000);
				
				String pageSource = driver.getPageSource();
				
				Document document = Jsoup.parse(pageSource);
				
				Elements elements = document.select("img");
				
				int times = 1;
				String param = "";
				img:for (Element element : elements) {
					
					String attr = element.attr("src");
					if(StringUtils.isNotBlank(attr) && attr.contains("nisp-captcha")){
						
						if(times==2){
							param = getParam(attr);
							break img;
						}
						times++;
					}
				}
				
				Actions actions = new Actions(driver);
				
				WebElement button = driver.findElement(By.xpath("//*[@id='NECaptcha']/div[1]/div[2]/div[1]"));
				
				actions.dragAndDropBy(button, Integer.parseInt(param.split(",")[0])-45, Integer.parseInt(param.split(",")[1])).perform();//第一次
				
				Thread.sleep(1000);
				String errmsg = "";
				try {
					errmsg = driver.findElement(By.xpath("//*[@id='nerror']/div[2]")).getText();
				} catch (Exception e2) {
				}
				if(StringUtils.isNotBlank(errmsg) && errmsg.contains("请滑动验证码")){
					System.out.println("滑动失败,继续 "+(m+1));
				}else{
					flag = true;
					break;
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	/**
	 * 获取参数
	 * @param attr
	 * @return
	 */
	private static String getParam(String attr) {
	
		try{
		URL url = new URL(attr);
		
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		
		openConnection.connect();
		
		InputStream is = openConnection.getInputStream();
		
		byte[] b = new byte[1024];
		int len;
		
		OutputStream os = new FileOutputStream("d:\\slide.png");
		
		while((len = is.read(b)) != -1){
			
			os.write(b, 0, len);
		}
		
		os.close();
		is.close();
		
		String code = RuoKuai.createByPostNew("6137", "d:\\slide.png");
		System.out.println(code);
		return code;
		
		}catch(Exception e){
			
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取评论图片
	 * @param driver
	 */
	private static String getCommentPic(WebDriver driver,WebElement comment) {
		
		try{
		File screenshotAs = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		BufferedImage bufferedImage = ImageIO.read(screenshotAs);
		
		Point point = comment.getLocation();
		
		int width = comment.getSize().getWidth();
		int height = comment.getSize().getHeight();
		
		BufferedImage subimage = bufferedImage.getSubimage(point.getX(), point.getY(), width, height);
		
		ImageIO.write(subimage, "png", screenshotAs);
		
		String picName = getPicName();
		File file = new File(picName);
		FileUtils.copyFile(screenshotAs, file);
		
		return picName;
		}catch(Exception e){
			DriverGet.quit(driver);;
			logger.info("获取图片异常"+e);
		}
		return null;
	}

	/**
	 * 获取评论地址
	 * @param address
	 * @return
	 */
	private static String getCommentAddress(String address) {
		
		try{
			
		String docId = address.substring(address.lastIndexOf("/")+1,address.indexOf(".html"));
		address = "http://sdk.comment.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads/"+docId+"?ibc=jssdk&callback=tool10042492721813033474_"+System.currentTimeMillis()+"&_="+System.currentTimeMillis();
		URL url = new URL(address);
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		openConnection.connect();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(openConnection.getInputStream(),"utf-8"));
		StringBuffer sb = new StringBuffer();
		
		String line;
		while((line = br.readLine())!=null){
			
			sb.append(line);
		}
		
		JSONObject parseObject = JSON.parseObject(sb.toString().substring(sb.toString().indexOf("(")+1,sb.toString().indexOf(")")));
		String boardId = parseObject.getString("boardId");
		docId = parseObject.getString("docId");
		
		address = "http://comment.news.163.com/"+boardId+"/"+docId+".html";
		
		return address;
		}catch(Exception e){
			
			logger.info("获取评论地址错误");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取验证码
	 * @param driver
	 */
	@SuppressWarnings("unused")
	private static String getPic(WebDriver driver,WebElement comment) {
		
		try{
		File screenshotAs = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		BufferedImage bufferedImage = ImageIO.read(screenshotAs);
		
		Point point = comment.getLocation();
		
		int width = comment.getSize().getWidth();
		int height = comment.getSize().getHeight();
		
		BufferedImage subimage = bufferedImage.getSubimage(point.getX()+223, point.getY()+240, width-265, height-400);
		
		ImageIO.write(subimage, "png", screenshotAs);
		
//		String picName = getPicName();
		File file = new File("d:\\wangyi.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("3040", "d:\\wangyi.png");
		
		return code;
		}catch(Exception e){
			
			logger.info("获取图片异常"+e);
		}
		return null;
	}

	/**
	 * 图片名称
	 * @return
	 */
	private static String getPicName() {
		String picName = "d:\\jietu\\";
		File file =new File(picName);
		if(!file.exists() && !file.isDirectory()){
		    file.mkdirs();  
		}
		String picUri = "d:\\jietu\\wy_11.png";
		return picUri;
	}

	/**
	 * 判断是否成功
	 */
	public static void isSuccess(TaskGuideBean task,String msg){
			MQSender.toMQ(task,msg);
	}
	
	
}