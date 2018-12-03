package com.longriver.netpro.webview.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

import org.apache.commons.lang3.StringUtils;

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
 * 新浪微博直发
 * @author rhy
 * @2017-11-28 下午3:10:04
 * @version v1.0
 */
public class WeiboPublishJietu {
	public static boolean process = false;
	public static boolean isSend = false;
	public static void main(String[] args) {
		process = true;
		TaskGuideBean task = new TaskGuideBean();
		task.setCorpus("歼-20已换装新型航空发动机, 发动机叶片技术已是世界一流!");
		task.setNick("lilei1929@163.com");
		task.setPassword("lilei419688..");
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
		
		if(StringUtils.isNotBlank(task.getPicData())){
			
			WeiboPicComment.toComment(task);
			return;
		}
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
			if(!suc.equals("suc")){
				isSuccess(task, suc);
				return ;
			}
			String s = driver.getPageSource();
			String uid = getUid(s);
			if(uid.equals("")){
				System.out.println("uid未找到,可能改版了");
				return;
			}
			String home = "https://weibo.com/u/"+uid+"/home";
			driver.get(home);
			Thread.sleep(200);
			
			WebElement publicComment = driver.findElement(By.xpath("//*[@id='v6_pl_content_publishertop']/div/div[2]/textarea"));
			publicComment.clear();
			publicComment.sendKeys(task.getCorpus());
			Thread.sleep(200);
			WebElement commit = driver.findElement(By.xpath("//*[@id='v6_pl_content_publishertop']/div/div[3]/div[1]/a"));
			commit.click();
			Thread.sleep(1000);
			String allfa = "https://weibo.com/"+uid+"/profile?profile_ftype=1&is_all=1";
			driver.get(allfa);
			//获得截图并判断程序
			WebElement commentDiv = null;
			try {
				Thread.sleep(4000);
				commentDiv = driver.findElement(By.xpath("//*[@id='Pl_Official_MyProfileFeed__21']/div/div[2]/div[1]"));
			} catch (Exception e1) {
				Thread.sleep(4000);
				System.out.println("xpath未找到");
				commentDiv = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/div[2]/div[2]/div/div[2]/div[1]"));
			}
			Thread.sleep(200);
			//根据语料 判断是否成功
			System.out.println("getCorpus=="+task.getCorpus().trim());
			System.out.println("getText=="+commentDiv.getText().trim());
			if(task.getCorpus().length()>20) task.setCorpus(task.getCorpus().substring(0, 20));
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
			isSuccess(task, "报错失败");
			System.out.println("评论异常");
			e.printStackTrace();
		}finally{
			loginout(driver);
			isSuccess(task, "");//成功
			DriverGet.quit(driver);
		}
		System.out.println("结束------------------");
		System.out.println("结束------------------");
	}
	
	public static String getUid(String source){
		try {
			int k = source.indexOf("uid:'");
			source = source.substring(k);
			int kk = source.indexOf("',");
			source = source.substring(5, kk);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("uid未找到");
			return "";
		}
		return source;
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
		int height = comment.getSize().getHeight()+40;
		
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
	public static void loginout(WebDriver driver){
//		try {
//			System.out.println("退出登录");
//			driver.get("https://weibo.com/logout.php");
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
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
