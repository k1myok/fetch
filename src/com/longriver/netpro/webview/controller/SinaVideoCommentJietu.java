package com.longriver.netpro.webview.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.longriver.netpro.common.sina.WeiboLoginJietu;
import com.longriver.netpro.util.Configur;
import com.longriver.netpro.util.DriverGet;
import com.longriver.netpro.util.FileUtil;
import com.longriver.netpro.util.GetProprities;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.PngErjinzhi;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 新浪新闻引导及截图
 * @author lilei
 * @2018-1-8 下午3:10:04
 * @version v1.0
 */
public class SinaVideoCommentJietu {
	public static boolean process = false;
	public static boolean isSend = false;
	public static void main(String[] args) {
		process = true;
		TaskGuideBean task = new TaskGuideBean();
		task.setCorpus("支持支持");
		task.setCorpus("念念不忘！");
		task.setNick("lilei1929@163.com");
		task.setPassword("lilei419688..");
		task.setAddress("http://video.sina.com.cn/p/news/s/doc/2017-08-29/161066979177.html");
		task.setAddress("http://video.sina.com.cn/p/ent/doc/2018-01-02/110267739873.html");
		task.setAddress("http://video.sina.com.cn/p/ent/doc/2018-01-02/111667739943.html");
		task.setAddress("http://video.sina.com.cn/p/sports/o/v/v/doc/2017-08-29/131766978481.html");
//		task.setNick("15558480767");
//		task.setPassword("a123456a");
//		task.setNick("13426195063");
//		task.setPassword("haotianwen1985");
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
		WebDriver driver = null;
		try{
			Configur config = GetProprities.paramsConfig;
			String firefoxUrl = config.getProperty("firefoxurl");
			
			FirefoxProfile profile = new FirefoxProfile();
			//禁用css
	//		profile.setPreference("permissions.default.stylesheet", 2);
			//不加载图片
	//		profile.setPreference("permissions.default.image", 2);
			//##禁用Flash 
			profile.setPreference("dom.ipc.plugins.enabled.libflashplayer.so","false");
			
			System.setProperty("webdriver.firefox.bin",firefoxUrl);
			driver = new FirefoxDriver(profile); 
			String suc = WeiboLoginJietu.toLogin(driver, task, 0);
			if(!suc.equals("suc")){
				isSuccess(task, suc);
				return ;
			}
			String address = task.getAddress();
			driver.get(address);
			Thread.sleep(200);
			WebElement comment5 = driver.findElement(By.xpath("//a[contains(@href,'comment5.news.sina.com.cn')]"));
			comment5.click();
			Thread.sleep(200);
			WebElement publicComment = driver.findElement(By.xpath("//textarea[contains(@placeholder,'请输入评论')]"));
			publicComment.clear();
			publicComment.sendKeys(task.getCorpus());
			Thread.sleep(200);
			WebElement commit = driver.findElements(By.xpath("//a[contains(@comment-type,'submit')]")).get(0);
			commit.click();
			
			if(task.getStatus2()==1){
				
			}
			Thread.sleep(4000);
			//获得截图并判断程序
			WebElement commentDiv = null;
			try {
				commentDiv = driver.findElements(By.xpath("//div[contains(@class,'item item-hack clearfix')]")).get(0);
			} catch (Exception e1) {
				System.out.println("xpath未找到");
				e1.printStackTrace();
			}
			Thread.sleep(200);
			//根据语料 判断是否成功
			System.out.println("getCorpus=="+task.getCorpus().trim());
			System.out.println("getText=="+commentDiv.getText().trim());
			if(task.getCorpus().length()>91) task.setCorpus(task.getCorpus().substring(0, 90));
			if(commentDiv.getText().trim().contains(task.getCorpus().trim())){
				System.out.println("截图发帖成功!");
				//需要截图走下面
				if(task.getStatus2()==1){
					try {
						String picName = getPic(task,driver,commentDiv);
						task.setPng(PngErjinzhi.getImageBinary(picName));
					} catch (Exception e) {
						System.out.println("发帖成功截图失败!");
					}
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
	
}
