package com.longriver.netpro.webview.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.longriver.netpro.util.DriverGet;
import com.longriver.netpro.util.FileUtil;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.PngErjinzhi;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * @author rhy
 * @2017-12-1 下午2:23:19
 * @version v2.0
 */
public class SohuCommentJietu {

	private static Logger logger = Logger.getLogger(SohuCommentJietu.class);
	public static boolean isSend = false;
	
	public static void main(String[] args) {
		
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://www.sohu.com/a/208302901_115376?_f=index_chan08pol_socnews_2");
		task.setAddress("http://www.sohu.com/a/208707178_119562?_f=index_news_12");
		task.setAddress("http://www.sohu.com/a/209576789_162758?code=9dc6ca9c6dc6c2cf206004ead89856d6&_f=index_cpc_4_0");
		task.setAddress("http://www.sohu.com/a/209567570_120702?g=0?code=5a8a3fcbe909b6b56f427b189cadecb3&_f=index_cpc_5_0");
		task.setAddress("http://www.sohu.com/a/209718703_260616?_f=index_news_2");
		task.setAddress("https://www.sohu.com/a/211340335_463728");
		task.setAddress("http://www.sohu.com/a/211317915_123753?_f=index_chan15news_2");
		task.setCorpus("念念不忘");
//		task.setAddress("http://www.sohu.com/a/208302901_115376?_f=index_chan08pol_socnews_2");
//		task.setAddress("http://www.sohu.com/a/208707178_119562?_f=index_news_12");
//		task.setAddress("http://www.sohu.com/a/209576789_162758?code=9dc6ca9c6dc6c2cf206004ead89856d6&_f=index_cpc_4_0");
//		task.setAddress("http://www.sohu.com/a/209567570_120702?g=0?code=5a8a3fcbe909b6b56f427b189cadecb3&_f=index_cpc_5_0");
//		task.setAddress("http://www.sohu.com/a/209718703_260616?_f=index_news_2");
//		task.setAddress("https://www.sohu.com/a/211340335_463728");
//		task.setAddress("http://www.sohu.com/a/224609614_115423?g=0?code=5bdf468b2c5e2f9b6d8537945c85bb4a&_f=index_cpc_5_0");
//		task.setCorpus("念念不忘");
		task.setNick("shijiebei201814@sohu.com");
		task.setPassword("123ABC_1");
//		task.setNick("15626027805");
//		task.setPassword("mm949811");
		
		
		//新系统

		task.setMark("bj");
		task.setMark("newVersion");
		task.setAddress("http://www.sohu.com/a/211317915_123753?_f=index_chan15news_2");
		task.setNick("15707465197");
		task.setPassword("yxsgry590");
		task.setCorpus("不仅苟且，还有枸杞...");
		task.setMark("newVersion2");
//		task.setAddress("http://www.sohu.com/a/224609614_115423?g=0?code=5bdf468b2c5e2f9b6d8537945c85bb4a&_f=index_cpc_5_0");
		task.setAddress("http://www.sohu.com/a/234308533_123753");
//		task.setNick("15707465197");
//		task.setPassword("yxsgry590");
		task.setCorpus("需要这样干");
		
		toComment(task);
	}

	/**
	 * 搜狐新闻评论
	 * @param task
	 */
	public static void toComment(TaskGuideBean task) {
		//先清空存放截图的文件夹
		FileUtil.deleteDirectoryFiles();
		//保证只返回一次结果
		isSend = true;
		if(task.getMark()!=null && "newVersion".equals(task.getMark())){
		    toWeiboRun(task,1);//新系统脚本--用微博登录发帖
		}else{
		    toRun(task,1);//三次验证码
		}
	}
	/**
	 * 北京系统
	 * @param task
	 * @param yzmcode
	 */
	public static void toRun(TaskGuideBean task,int yzmcode) {
		
		WebDriver driver = DriverGet.getDriver();
		try{
			//https登录不能成功
			String address = task.getAddress().replace("https:", "http:");
			
			driver.get(address);
			
			Thread.sleep(3000);
			driver.findElement(By.xpath("//*[@id='head-login']/div/a")).click();
			driver.findElement(By.xpath("/html/body/div[5]/div[1]/ul/li[1]")).click();
			driver.findElement(By.xpath("/html/body/div[5]/div[3]/ul/li[1]/input")).sendKeys(task.getNick());
			driver.findElement(By.xpath("/html/body/div[5]/div[3]/ul/li[2]/input")).sendKeys(task.getPassword());
			
			driver.findElement(By.xpath("/html/body/div[5]/div[3]/div[2]/input")).click();
			Thread.sleep(2000);
			try{
				String error = driver.findElement(By.className("err-info")).getText();
				if(StringUtils.isNotBlank(error)){
					if(error.contains("验证码")){
						WebElement imgCode = driver.findElement(By.xpath("/html/body/div[5]/div[3]/ul/li[3]/img"));
						String code = getPic(driver, imgCode);
						driver.findElement(By.xpath("/html/body/div[5]/div[3]/ul/li[3]/input")).sendKeys(code);
						driver.findElement(By.xpath("/html/body/div[5]/div[3]/div[2]/input")).click();
					}else if(error.contains("密码错误")){
						task.setCode(101);
						isSuccess(task, error);
						DriverGet.quit(driver);
						return;
					}
				}
				
//				driver.findElement(By.xpath("/html/body/div[5]/div[3]/div[2]/input")).click();
				Thread.sleep(2000);
				String error2 = driver.findElement(By.className("err-info")).getText();
				if(StringUtils.isNotBlank(error2)){
					System.out.println("text:"+error2);
					if(error2.contains("验证码") && yzmcode<4){
						yzmcode = yzmcode+1;
						DriverGet.quit(driver);
						toRun(task,yzmcode);
						return ;
					}else if(error.contains("密码错误")){
						task.setCode(101);
						isSuccess(task, error);
						DriverGet.quit(driver);
						return;
					}
					task.setCode(102);
					isSuccess(task, error2);
					DriverGet.quit(driver);
					return;
				}
			}catch(Exception e){
				System.out.println("可能没有验证码.获得不到");
			}
			
			WebElement loginWin = driver.findElement(By.xpath("/html/body/div[4]"));
			if(loginWin.isDisplayed()){
				task.setCode(101);
				isSuccess(task, "需要手机动态码登录");
				return;
			}
			
			driver.manage().window().maximize();
			driver.navigate().refresh();//刷新一次
			try {
				driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/div[7]")).click();
			} catch (Exception e1) {
				System.out.println("走另一个链接");
			}
			driver.findElement(By.xpath("//*[@id='mpbox']/div[2]/div/div[1]/textarea")).click();
			driver.findElement(By.xpath("//*[@id='mpbox']/div[2]/div/div[1]/textarea")).sendKeys(task.getCorpus());
			Thread.sleep(700);
			driver.findElement(By.xpath("//*[@id='mpbox']/div[2]/div/div[3]")).click();
			
			Thread.sleep(2000);
			//截图部分暂时注释
			WebElement content = null;
			try{
				content = driver.findElement(By.xpath("//*[@id='mpbox']/div[4]/div/div[4]/div[1]"));
			}catch(Exception e){
				content = driver.findElement(By.xpath("//*[@id='mpbox']/div[4]/div/div[2]/div[1]/div[2]"));
			}
			//根据语料 判断是否成功
			String tt = task.getCorpus().length()>1?task.getCorpus().substring(1):task.getCorpus();
			String cor = tt.length()>6?tt.substring(0, 6):tt;
			System.out.println("text=="+content.getText());
			System.out.println("corpus=="+cor);
			System.out.println("contains=="+content.getText().contains(cor));
			System.out.println(content.getText());
			if(content.getText().contains(cor)){
				System.out.println("截图发帖成功!");
				try {
					String picUri = getPicContent(task,driver,content);
					task.setPng(PngErjinzhi.getImageBinary(picUri));
				} catch (Exception e) {
					System.out.println("发帖成功截图失败!");
				}
			}else{
				System.out.println("截图发帖失败!");
				isSuccess(task, "");
			}
		}catch(Exception e){
			logger.info("评论异常");
			task.setCode(102);
			isSuccess(task, "评论错误");
			e.printStackTrace();
		}finally{
			task.setCode(100);
			isSuccess(task, "");
			DriverGet.quit(driver);
		}
	}
	/**
	 * 新系统
	 * @param task
	 */
	private static void toWeiboRun(TaskGuideBean task,int yzmcode) {
		
		WebDriver driver = DriverGet.getDriver();
		try{
		String address = task.getAddress().replace("https:", "http:");
		
		driver.get(address);
		
		driver.findElement(By.xpath("//*[@id='head-login']/div/a")).click();
		driver.findElement(By.xpath("/html/body/div[5]/div[4]/ul/li[2]/a")).click();
		
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
		
		try {
			WebElement preimgCode = driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/span/img"));
			
			if(preimgCode.isDisplayed()){
			String precode = getPicNew(driver, preimgCode);
			driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/input")).clear();
			driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/input")).sendKeys(precode);
			}
		} catch (Exception e1) {
		}
		
		driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[2]/div/p/a[1]")).click();
		
		Thread.sleep(2000);
		try {
			WebElement imgCode = driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/span/img"));
			
			String code = getPicNew(driver, imgCode);
			driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/input")).clear();
			driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/input")).sendKeys(code);
			driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[2]/div/p/a[1]")).click();
			Thread.sleep(3000);
		} catch (Exception e) {
		}
		
		String currentUrl = driver.getCurrentUrl();
		
		try {
			if(currentUrl != null && "https://api.weibo.com/oauth2/authorize".equals(currentUrl)){
				
				driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[2]/div/p/a[1]")).click();
				Thread.sleep(2000);
				String curUrl = driver.getCurrentUrl();
				if(curUrl!=null && curUrl.contains("http://passport.sohu.com/openlogin/callback/sina")){
					
					driver.findElement(By.id("agree_btn")).click();
					Thread.sleep(2000);
				}
			}
		} catch (Exception e) {
		}
		
		try {
			String errormsg = driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText();
			if(StringUtils.isNotBlank(errormsg)){
				System.out.println("text:"+errormsg);
				if(errormsg.contains("验证码") && yzmcode<2){
					yzmcode = yzmcode+1;
					DriverGet.quit(driver);
					toWeiboRun(task,yzmcode);
					return ;
				}
				isSuccess(task, errormsg);
				DriverGet.quit(driver);
				return;
			}
		} catch (Exception e) {
		}
		
		if(currentUrl.contains("unitivelogin")){
			
			isSuccess(task, "登录失败");
			return;
		}
		
		driver.findElement(By.xpath("//*[@id='mpbox']/div[2]/div/div[1]/textarea")).click();
		Thread.sleep(100);
		driver.findElement(By.xpath("//*[@id='mpbox']/div[2]/div/div[1]/textarea")).sendKeys(task.getCorpus());
		Thread.sleep(500);
		driver.findElement(By.xpath("//*[@id='mpbox']/div[2]/div/div[3]")).click();
		
		Thread.sleep(3000);
		WebElement content = null;
		try{
			content = driver.findElement(By.xpath("//*[@id='mpbox']/div[4]/div/div[4]/div[1]"));
		}catch(Exception e){
			content = driver.findElement(By.xpath("//*[@id='mpbox']/div[4]/div/div[2]/div[1]/div[2]"));
		}
		//根据语料 判断是否成功
		//根据语料 判断是否成功
		String tt = task.getCorpus().length()>1?task.getCorpus().substring(1):task.getCorpus();
		String cor = tt.length()>6?tt.substring(0, 6):tt;
		System.out.println("text=="+content.getText());
		System.out.println("corpus=="+cor);
		System.out.println("contains=="+content.getText().contains(cor));
		if(content.getText().contains(cor)){
			System.out.println("截图发帖成功!");
			try {
				String picUri = getPicContent(task,driver,content);
				task.setPng(PngErjinzhi.getImageBinary(picUri));
				isSuccess(task, "");
			} catch (Exception e) {
				System.out.println("发帖成功截图失败!");
			}
		}else{
			System.out.println("截图发帖失败!");
			isSuccess(task, "");
		}
		}catch(Exception e){
			
			isSuccess(task, "引导错误");
			e.printStackTrace();
			
		}finally{
			
			if(driver != null){
				driver.quit();
			}
		}
	}

	/**
	 * 获取微博的验证码
	 * @param driver
	 * @param preimgCode
	 * @return
	 */
	private static String getPicNew(WebDriver driver, WebElement preimgCode) throws Exception{
		
		File screenshotAs = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		BufferedImage bufferedImage = ImageIO.read(screenshotAs);
		
		Point point = preimgCode.getLocation();
		
		int width = preimgCode.getSize().getWidth();
		int height = preimgCode.getSize().getHeight();
		
		BufferedImage subimage = bufferedImage.getSubimage(point.getX(), point.getY(), width, height);
		
		ImageIO.write(subimage, "png", screenshotAs);
		
//		String picName = getPicName();
		File file = new File("c:\\sohu.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("3050", "c:\\sohu.png");
		
		return code;
	}
	
	/**
	 * 获取验证码
	 * @param driver
	 * @throws IOException 
	 */
	private static String getPic(WebDriver driver,WebElement comment) throws IOException {
		
		File screenshotAs = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		BufferedImage bufferedImage = ImageIO.read(screenshotAs);
		
		Point point = comment.getLocation();
		
		int width = comment.getSize().getWidth();
		int height = comment.getSize().getHeight();
		
		BufferedImage subimage = bufferedImage.getSubimage(point.getX(), point.getY(), width, height);
		
		ImageIO.write(subimage, "png", screenshotAs);
		
//		String picName = getPicName();
		File file = new File("c:\\sohu.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("3040", "c:\\sohu.png");
		
		return code;
	}
	/**
	 * 获取评论图片
	 * @param driver
	 * @throws IOException 
	 */
	private static String getPicContent(TaskGuideBean task,WebDriver driver,WebElement comment) throws IOException {
		System.out.println("获取评论图片");
		File screenshotAs = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		BufferedImage bufferedImage = ImageIO.read(screenshotAs);
		
		Point point = comment.getLocation();
		
		int width = comment.getSize().getWidth();
		int height = comment.getSize().getHeight();
		if(width<=0) width=300;
		if(height<=0) height=100;
		BufferedImage subimage = bufferedImage.getSubimage(point.getX(), point.getY(), width, height);
		
		ImageIO.write(subimage, "png", screenshotAs);
		
		String picName = getPicName();
		File file = new File(picName);
		FileUtils.copyFile(screenshotAs, file);
		task.setPng(PngErjinzhi.getImageBinary(picName));
		
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
		String picUri = "c:\\jietu\\sohu_"+System.currentTimeMillis()+".png";
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
	
}
