package com.longriver.netpro.webview.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
 * sina微博转发或同时评论 截图
 * @author lilei
 * @2017-11-28 下午3:10:04
 * @version v1.0
 */
public class WeiboPostJietu {
	public static boolean process = false;
	public static boolean isSend = false;
	public static void main(String[] args) {
		process = true;
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("https://weibo.com/1974576991/Gkg3qlIk5?type=repost");
		task.setCorpus("[哈哈]");
		task.setNick("lilei1929@163.com");
		task.setPassword("lilei419688..");
//		task.setCommentOrPost(1);
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
		WebDriver driver = DriverGet.getDriver();
		try{
			String suc = WeiboLoginJietu.toLogin(driver, task, 0);
			if(!suc.equals("suc")){
				isSuccess(task, suc);
				return ;
			}
			String address = task.getAddress();
			if(address.contains("?")){
				address = address.substring(0, address.indexOf("?"));
			}
			address += "?type=repost";
			driver.get(address);
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			
			if(task.getCorpus().length()>0){
				//转发语料最大限制为140
				List<WebElement> writeCorpus = driver.findElements(By.xpath("//textarea[contains(@title,'转发微博内容')]"));
				if(writeCorpus.size()>0) writeCorpus.get(0).clear();
				else{
					driver.navigate().refresh();
					writeCorpus = driver.findElements(By.xpath("//textarea[contains(@title,'转发微博内容')]"));
					writeCorpus.get(0).clear();
				}
				if(task.getCorpus().length()>140) task.setCorpus(task.getCorpus().substring(0,140));
				Thread.sleep(1000);
				writeCorpus.get(0).sendKeys(task.getCorpus());
			}
			Thread.sleep(1000);
			WebElement postSubmit = driver.findElement(By.linkText("转发"));
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
			//根据语料 判断是否成功
			boolean flag = juiJietu(driver,commentDiv,task.getCorpus());
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
			WeiboPublishJietu.loginout(driver);//退出登录
			isSuccess(task, "");//成功
			DriverGet.quit(driver);
		}
		System.out.println("结束------------------");
		System.out.println("结束------------------");
	}
	public static boolean juiJietu(WebDriver driver,WebElement commentDiv,String corpus){
		WebElement nickE = driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div/div/div[3]/div[1]/ul/li[5]/a/em[2]"));
		String nick = nickE.getText().trim();
		if(commentDiv.getText().trim().contains(nick)){
			return true;
		}
		String tt = corpus.length()>1?corpus.substring(1):corpus;
		String cor = tt.length()>6?tt.substring(0, 6):tt;
		System.out.println("text=="+commentDiv.getText());
		System.out.println("corpus=="+cor);
		System.out.println("contains=="+commentDiv.getText().contains(cor));
		if(commentDiv.getText().trim().contains(cor)) return true;
		return false;
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
