package com.longriver.netpro.webview.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

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

import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.longriver.netpro.common.sina.WeiboLoginJietu;
import com.longriver.netpro.util.Configur;
import com.longriver.netpro.util.DriverGet;
import com.longriver.netpro.util.FileUtil;
import com.longriver.netpro.util.GetProprities;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.PngErjinzhi;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 新浪新闻引导及截图
 * @author rhy
 * @2017-11-28 下午3:10:04
 * @version v1.0
 * 网易,凤凰,搜狐都能用新浪微博登录
 */
public class SinaCommentJietu {
	public static boolean process = false;
	public static boolean isSend = false;
	public static void main(String[] args) {
		process = true;
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://news.sina.com.cn/c/nd/2017-12-04/doc-ifyphxwa7859073.shtml");
		task.setAddress("http://news.sina.com.cn/c/nd/2017-12-07/doc-ifypnqvn0746272.shtml");
		task.setAddress("http://news.sina.com.cn/o/2018-05-25/doc-ihcaqueu0706189.shtml");
		//app链接,做不了
//		task.setAddress("https://news.sina.cn/gn/2018-06-15/detail-ihcyszrz7327016.d.html?sinawapsharesource=newsapp&wm=3200_0001");
		task.setCorpus("恩恩,说的对");
		task.setNick("lilei1929@163.com");
		task.setPassword("lilei419688..0");

		task.setCorpus("还有这事.....");
//		task.setAddress("http://news.sina.com.cn/c/nd/2017-12-04/doc-ifyphxwa7859073.shtml");
//		task.setAddress("http://news.sina.com.cn/c/nd/2017-12-07/doc-ifypnqvn0746272.shtml");
//		task.setAddress("http://news.sina.com.cn/c/nd/2017-12-12/doc-ifypnyqi3907301.shtml");
//		task.setAddress("http://sports.sina.com.cn/g/pl/2017-12-21/doc-ifypvuqe4650551.shtml");
//		task.setAddress("http://hebei.sina.com.cn/news/s/2017-12-26/detail-ifypyuvc3560160.shtml");
//		task.setAddress("http://sports.sina.com.cn/china/national/2017-12-18/doc-ifyptkyk5095847.shtml");
//		task.setAddress("http://video.sina.com.cn/p/ent/doc/2018-01-02/111667739943.html");
		task.setAddress("http://news.sina.com.cn/c/2018-05-10/doc-ihaichqz2541939.shtml");
		task.setCorpus("中国牛逼");
		task.setNick("lilei1929@163.com");
		task.setPassword("lilei419688..");
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
		torun(task,1);
	}
	public static void torun(TaskGuideBean task,int number) {
		WebDriver driver = DriverGet.getDriver();
		try{
			String address = task.getAddress();
			 
			String commentAddress = "";
			commentAddress = getCommentAdd(task);
			if(commentAddress.equals("")){
				driver.get(address);
				Thread.sleep(2000);
				String pageSource = driver.getPageSource();
				commentAddress = getCommentAddress(address,pageSource);
			}
			String suc = WeiboLoginJietu.toLogin(driver, task, 0);
			if(!suc.equals("suc")){
				isSuccess(task, suc);
				return ;
			}
			
			driver.get(commentAddress);
			Thread.sleep(1500);
			WebElement commentArea = driver.findElement(By.xpath("//*[@id='SI_Wrap']/div[1]/div[1]/div/div[1]/div[2]/div[1]/div/textarea"));
			commentArea.clear();
			commentArea.sendKeys(task.getCorpus());
			Thread.sleep(2000);
			WebElement publicComment=null;
			try {
				publicComment = driver.findElement(By.tagName("发布"));
			} catch (Exception e) {
				publicComment = driver.findElement(By.xpath("//*[@id='bottom_sina_comment']/div[1]/div[3]/div[2]/a[1]"));
			}
			publicComment.click();
			//获得截图并判断程序
			Thread.sleep(3000);
//			WebElement commentDiv = driver.findElement(By.xpath("//*[@id='SI_Wrap']/div[1]/div[1]/div/div[2]/div[2]/div[3]/div[1]"));
//			Thread.sleep(1000);
//			//根据语料 判断是否成功
//			String tt = task.getCorpus().length()>1?task.getCorpus().substring(1):task.getCorpus();
//			String cor = tt.length()>6?tt.substring(0, 6):tt;
//			System.out.println("text=="+commentDiv.getText());
//			System.out.println("corpus=="+cor);
//			System.out.println("contains=="+commentDiv.getText().contains(cor));
//			if(commentDiv.getText().contains(cor)){
//				System.out.println("截图发帖成功!");
//				try {
//					String picName = getPic(task,driver,commentDiv);
//					task.setPng(PngErjinzhi.getImageBinary(picName));
//				} catch (Exception e) {
//					System.out.println("发帖成功截图失败!");
//				}
//			}else{
//				System.out.println("截图失败!");
//				isSuccess(task, "");
//			}
		}catch(Exception e){
			task.setCode(102);
			isSuccess(task, "报错失败");
			System.out.println("评论异常");
			e.printStackTrace();
		}finally{
			task.setCode(100);
			isSuccess(task, "");//成功
			DriverGet.quit(driver);
		}
		System.out.println("结束------------------");
		System.out.println("结束------------------");
	}
	public static String getCommentAdd(TaskGuideBean taskdo){
		WebClient webClient = new WebClient();

		webClient.getOptions().setUseInsecureSSL(true);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(
				false);
		webClient.getOptions().setTimeout(30000);

		HtmlPage htmlPage = null;
		try {
			htmlPage = webClient.getPage(taskdo.getAddress());
		}  catch (IOException e1) {
			e1.printStackTrace();
		}
		String result = htmlPage.asXml();
		System.out.println(result);
		String address = "";
		if(taskdo.getAddress().contains("comment5.news.sina")) return taskdo.getAddress();
		if(taskdo.getAddress().contains("sports")){
			result = result.substring(result.indexOf("PAGEDATA")).replace("=", "");
			result = result.substring(0, result.indexOf("</script>"));
			String channel = result.substring(result.indexOf("channel")+8,result.indexOf("newsid")).replace("'", "").replace(",", "").trim();
			String newidPre = result.substring(result.indexOf("newsid"));
			String newsIds = newidPre.substring(newidPre.indexOf("conmos-")+7,newidPre.indexOf(",")).replace("'", "").replace(",", "").trim();
			
			address = "http://comment5.news.sina.com.cn/comment/skin/default.html?channel="+channel+"&newsid=comos-"+newsIds;
		}else if(result.indexOf("SINA_TEXT_PAGE_INFO")>-1){
			result = result.substring(result.indexOf("SINA_TEXT_PAGE_INFO")).replace("=", "");
			result = result.substring(0, result.indexOf("</script>"));
			String channel = result.substring(result.indexOf("channel")+8,result.indexOf("newsid")).replace("'", "").replace(",", "").trim();
			String newidPre = result.substring(result.indexOf("newsid"));
			
			String newsIds = newidPre.substring(newidPre.indexOf("newsid")+7,newidPre.indexOf(",")).replace("'", "").replace(",", "").trim();
			address = "http://comment5.news.sina.com.cn/comment/skin/default.html?channel="+channel+"&newsid="+newsIds;
		}else if(result.indexOf("travel")>-1){
			result = result.substring(result.indexOf("ARTICLE_DATA")).replace("=", "");
			result = result.substring(0, result.indexOf("</script>"));
			String tt = result.substring(result.indexOf("channel"));
			String channel = tt.substring(8,tt.indexOf("',")).replace("'", "").trim();
			String newidPre = result.substring(result.indexOf("newsid"));
			
			String newsIds = newidPre.substring(newidPre.indexOf("newsid")+7,newidPre.indexOf("',")).replace("'", "").replace(",", "").trim();
			address = "http://comment5.news.sina.com.cn/comment/skin/default.html?channel="+channel+"&newsid="+newsIds;
		}else{
			result = result.substring(result.indexOf("DFZ.CFG"));
			result = result.substring(result.indexOf("{"), result.indexOf("}")+1);
			JSONObject jsStr = JSONObject.parseObject(result);
			String channel = jsStr.getString("channel");
			String newsIds = taskdo.getAddress().substring(0, taskdo.getAddress().indexOf("htm"));
			newsIds = newsIds.substring(newsIds.lastIndexOf("/")+1,newsIds.lastIndexOf("."));
			address = "http://comment5.news.sina.com.cn/comment/skin/default.html?channel="+channel+"&newsid="+newsIds;
		}
		return address;
	}
	/**
	 * 获取评论地址
	 * @param pageSource
	 * @return
	 */
	private static String getCommentAddress(String address,String pageSource) throws Exception{
		String param = null;
//		if(address.contains("sports")){
//			if(pageSource.indexOf("PAGEDATA")>-1){
//				param = pageSource.substring(pageSource.indexOf("PAGEDATA"));
//			}else{
//				param = pageSource.substring(pageSource.indexOf("SINA_TEXT_PAGE_INFO"));
//			}
//		}else{
//			if(pageSource.indexOf("SINA_TEXT_PAGE_INFO")>-1){
//				param = pageSource.substring(pageSource.indexOf("SINA_TEXT_PAGE_INFO"));
//			}else{
//				param = pageSource.substring(pageSource.indexOf("SINA_TEXT_PAGE_INFO"));
//			}
//		}
		int cnindex = pageSource.indexOf("channel: '");
		String t1 = pageSource.substring(cnindex);
		int cnindex_1 = t1.indexOf(",");
		String t11 = t1.substring(10, cnindex_1);
		String channel = t11.replaceAll("'", "").trim();
		
		int cnindex2 = pageSource.indexOf("newsid: '");
		String t2 = pageSource.substring(cnindex2);
		int cnindex_2 = t2.indexOf(",");
		String t21 = t2.substring(9, cnindex_2);
		String newsid = t21.replaceAll("'", "").trim();
		if(address.contains("sports")){
			newsid = newsid.replace("conmos", "comos");
		}
		address = "http://comment5.news.sina.com.cn/comment/skin/default.html?channel="+channel+"&newsid="+newsid;
	
		return address;
	}
	/**
	 * 获取验证码
	 * @param driver
	 * @throws IOException 
	 */
	private static String getPicCode(WebDriver driver,WebElement comment) throws IOException {
		
		File screenshotAs = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		BufferedImage bufferedImage = ImageIO.read(screenshotAs);
		
		Point point = comment.getLocation();
		
		int width = comment.getSize().getWidth();
		int height = comment.getSize().getHeight();
		if(width<=0) width=300;
		if(height<=0) height=100;
		BufferedImage subimage = bufferedImage.getSubimage(point.getX(), point.getY(), width, height);
		
		ImageIO.write(subimage, "png", screenshotAs);
		
		File file = new File("c:\\sina.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("3000", "c:\\sina.png");
		
		return code;
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
		String picUri = "c:\\jietu\\sina_"+System.currentTimeMillis()+".png";
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
