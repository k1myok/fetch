package com.longriver.netpro.webview.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

import org.openqa.selenium.By;

import org.apache.http.client.CookieStore;
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

import com.longriver.netpro.common.sina.SinaIdMidConverter;

import com.longriver.netpro.common.sina.WeiboLoginJietu;
import com.longriver.netpro.util.Configur;
import com.longriver.netpro.util.DriverGet;
import com.longriver.netpro.util.FileUtil;
import com.longriver.netpro.util.GetProprities;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.PngErjinzhi;
import com.longriver.netpro.webview.controller.WeiboPostJietu;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * sina微博转发或同时评论 截图
 * @author lilei
 * @2017-11-28 下午3:10:04
 * @version v1.0
 */
public class WeiboCommentJietu {
	public static boolean process = false;
	public static boolean isSend = false;
	public static void main(String[] args) {
		process = true;
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("https://weibo.com/76649/FCsGl3hV2?from=page_10050576649_profile&wvr=6&mod=weibotime&type=comment#_rnd1515139140906");
		task.setCorpus("干的漂亮!");
		task.setNick("lilei1929@163.com");
		task.setPassword("lilei419688..");
		task.setCommentOrPost(1);
		task.setCommentOrPost(2);
//		task.setNick("15558480767");
//		task.setPassword("a123456a");
//		task.setNick("13426195063");
//		task.setPassword("haotianwen1985");
		task.setCorpus("看好你o");
		
		task.setAddress("https://weibo.com/5056645402/G3mKyjCnM?from=page_1005055056645402_profile&wvr=6&mod=weibotime&type=comment");
		task.setNick("17194513527");
		task.setPassword("chwx1234567");
		toComment(task);
		
	}

	/**
	 * 新浪新闻评论
	 * @param task
	 */
	public static void toComment(TaskGuideBean task) {
		System.out.println("toComment");
		//先清空存放截图的文件夹
		FileUtil.deleteDirectoryFiles();
		//保证只返回一次结果
		isSend = true;
		torun(task);
	}
	public static void torun(TaskGuideBean task) {
		WebDriver driver = DriverGet.getDriver();
		try{
			String suc = WeiboLoginJietu.toLogin(driver, task, 0);
			
			String address = task.getAddress();
			if(address.contains("?")){
				address = address.substring(0, address.indexOf("?"));
			}
			address += "?type=comment";
			driver.get(address);
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			
			if(task.getCorpus().length()>0){
				//转发语料最大限制为140
				if(task.getCorpus().length()>140) task.setCorpus(task.getCorpus().substring(0,140));
				List<WebElement> writeCorpus = driver.findElements(By.xpath("//textarea[contains(@node-type,'textEl')]"));
				Thread.sleep(1000);
				writeCorpus.get(0).clear();
				Thread.sleep(1000);
				writeCorpus.get(0).sendKeys(task.getCorpus());
			}
			Thread.sleep(500);
			if(task.getCommentOrPost()>0){//同时评论
				WebElement postxiedaiBox = driver.findElement(By.xpath("//*[@id='ipt11']"));
				postxiedaiBox.click();
				Thread.sleep(300);
			}
			WebElement postSubmit = driver.findElement(By.linkText("评论"));
			postSubmit.click();
			//获得截图并判断程序
			WebElement commentDiv = null;
			try {
				Thread.sleep(4000);
				List<WebElement> list = driver.findElements(By.xpath("//div[contains(@class,'list_li S_line1 clearfix')]"));
				commentDiv = list.get(0);
			} catch (Exception e1) {
				Thread.sleep(4000);
				System.out.println("xpath未找到");
				commentDiv = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/div[1]/div/div/div/div/div[5]/div/div[3]/div[2]/div/div[1]"));
			}
			Thread.sleep(200);
			boolean flag = WeiboPostJietu.juiJietu(driver,commentDiv,task.getCorpus());
			if(flag){
				System.out.println("截图发帖成功!");
				try {
					String picName = getPic(task,driver,commentDiv);
					task.setPng(PngErjinzhi.getImageBinary(picName));
				} catch (Exception e) {
					System.out.println("发帖成功截图失败!");
				}
			}else{
				System.out.println("截图发帖失败!");
				isSuccess(task, "匹配文字失败");
			}
		}catch(Exception e){
			isSuccess(task, "报错失败");
			System.out.println("评论异常");
			e.printStackTrace();
		}finally{
			if(task.getCommentOrPost() == 2 || task.getCommentOrPost() == 3){
				toPraise(driver,task);
			}
			isSuccess(task, "");//成功
			DriverGet.quit(driver);
		}
		System.out.println("结束------------------");
		System.out.println("结束------------------");
	}
	/**
	 * 获取评论图片
	 * @param driver
	 * @throws IOException 
	 */
	private static String getPic(TaskGuideBean task,WebDriver driver,WebElement comment) throws IOException {
		
		//让整个页面截图
		File screenshotAs = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		BufferedImage bufferedImage = ImageIO.read(screenshotAs);
		
		//获取页面上元素的位置
		Point point = comment.getLocation();
		
		int width = comment.getSize().getWidth();
		int height = comment.getSize().getHeight();
		
		//裁剪整个页面的截图，以获得元素的屏幕截图
		BufferedImage subimage = bufferedImage.getSubimage(point.getX(), point.getY(), width, height);
		
		ImageIO.write(subimage, "png", screenshotAs);
		
		String picName = getPicName();
		//将元素截图复制到磁盘
		File file = new File(picName);
		FileUtils.copyFile(screenshotAs, file);
		
		return picName;
	}

	/**
	 * 图片名称
	 * @return
	 */
	private static String getPicName() {
		String picName = "c:\\jietu\\";
		File file =new File(picName);
		if(!file.exists() && !file.isDirectory()){
		    file.mkdirs();  
		}
		String picUri = "c:\\jietu\\weibo_"+System.currentTimeMillis()+".png";
		return picUri;
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
	
	/**
	 * 获取mid
	 * @param task
	 * @return
	 */
	public static String getMid(TaskGuideBean task) {
		String midEncode = task.getAddress().substring(task.getAddress().lastIndexOf("/")).replace("/", "");
		if(midEncode.indexOf("?") > -1){
			midEncode = midEncode.substring(0, midEncode.indexOf("?"));
		}
		if(midEncode.indexOf("#") > -1){
			midEncode = midEncode.substring(0, midEncode.indexOf("#"));
		}
		String mid = SinaIdMidConverter.midToId(midEncode);
		System.out.println(mid);
		return mid;
	}
	/**
	 * 点赞
	 * @param taskdo
	 * @param m
	 */
	private static void toPraise(WebDriver driver,TaskGuideBean task){
		
		try{
			
		Set<Cookie> cookies = driver.manage().getCookies();
		String skeyCookie = "";
		for (Cookie cookie : cookies) {
			
			skeyCookie += cookie.toString().substring(0,cookie.toString().indexOf(";")) + "; ";
		}
		URL url = new URL("https://weibo.com/aj/v6/like/add?ajwvr=6&__rnd="+System.currentTimeMillis());
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		
		String param = "location=page_100505_single_weibo" +
				"&version=mini" +
				"&qid=heart" +
				"&mid="+getMid(task)+"" +
				"&loc=profile" +
				"&cuslike=1" +
				"&_t=0";
		
		connection.addRequestProperty("Host", "weibo.com");
		connection.addRequestProperty("Connection", "keep-alive");
		connection.addRequestProperty("Content-Length", String.valueOf(param.length()));
		connection.addRequestProperty("Origin", "https://weibo.com");
		connection.addRequestProperty("X-Requested-With", "XMLHttpRequest");
		connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36 LBBROWSER");
		connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.addRequestProperty("Accept", "*/*");
		connection.addRequestProperty("Referer", task.getAddress());
		connection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
		connection.addRequestProperty("Cookie", skeyCookie);
		
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
		}
	}
}
