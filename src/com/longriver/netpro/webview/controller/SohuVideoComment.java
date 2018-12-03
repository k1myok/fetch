package com.longriver.netpro.webview.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.longriver.netpro.common.sohu.SohuDriverLogin;
import com.longriver.netpro.util.FileUtil;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.PngErjinzhi;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 搜狐视频评论
 * @author rhy
 * @2017-9-5 上午10:01:07
 * @version v1.0
 */
public class SohuVideoComment {

	private static Logger logger = Logger.getLogger(SohuVideoComment.class);
	public static boolean isSend = false;
	
	public static void main(String[] args) {
		
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://tv.sohu.com/20170906/n600143906.shtml");
		task.setAddress("http://my.tv.sohu.com/us/312813498/92426999.shtml");
		task.setAddress("http://my.tv.sohu.com/us/202088812/92527677.shtml");
		task.setCorpus("这个有点意思呀adgff");
		task.setCorpus("人呢?走了走了");
		task.setNick("18611400343");
		task.setPassword("xyxhcx00");
		task.setMark("newVersion");
//		task.setNick("px40789@sohu.com");
//		task.setPassword("9672023788");
		//新浪账号
//		task.setAddress("http://my.tv.sohu.com/us/202088812/92527677.shtml");
//		task.setNick("15707465197");
//		task.setPassword("yxsgry590");
//		task.setCorpus("不仅苟且，还有枸杞...");
		
		toComment(task);
	}

	/**
	 * 搜狐登录
	 * @param task 登录信息
	 */
	public static void toComment(TaskGuideBean task) {
		//先清空存放截图的文件夹
		FileUtil.deleteDirectoryFiles();
		//保证只返回一次结果
		isSend = true;
		
		WebDriver driver = null;
		try{
		
		java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config\\properties\\paramsConfig");
		String firefoxUrl = bundle.getString("firefoxurl");
		FirefoxProfile profile = new FirefoxProfile();
		//禁用css
//		profile.setPreference("permissions.default.stylesheet", 2);
		//不加载图片
//		profile.setPreference("permissions.default.image", 2);
		//##禁用Flash 
		profile.setPreference("dom.ipc.plugins.enabled.libflashplayer.so","false");
		System.setProperty("webdriver.firefox.bin",firefoxUrl);
		driver = new FirefoxDriver(profile); 
		
		String suc = SohuDriverLogin.sohuLogin(driver,task);
		if(!suc.equals("suc")){
			isSuccess(task,suc);
			return ;
		}
		Thread.sleep(1000);
		driver.get(task.getAddress());
		
		WebElement commentTextArea = driver.findElement(By.id("commentTextarea"));
		commentTextArea.clear();
		commentTextArea.sendKeys(task.getCorpus());
		
		Thread.sleep(1000);
		WebElement commentSubmit = driver.findElements(By.xpath("//input[contains(@value,'发表评论')]")).get(0);
		commentSubmit.click();
		
		driver.manage().window().maximize();
		Thread.sleep(4000);
		WebElement commentDiv = driver.findElements(By.xpath("//div[contains(@class,'J_new')]")).get(0);
		//根据语料 判断是否成功
		System.out.println("getCorpus=="+task.getCorpus().trim());
		System.out.println("getText=="+commentDiv.getText().trim());
		if(task.getCorpus().length()>60) task.setCorpus(task.getCorpus().substring(0, 60));
		if(commentDiv.getText().trim().contains(task.getCorpus().trim())){
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
			
			e.printStackTrace();
			logger.info("start firefox exception...");
			isSuccess(task,"报错失败");
		}finally{
			driver.quit();
			isSuccess(task,"");
		}
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
		String picUri = "c:\\jietu\\souhuvideo_"+System.currentTimeMillis()+".png";
		return picUri;
	}
}
