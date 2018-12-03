package com.longriver.netpro.webview.carcontroller;

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

import com.longriver.netpro.fetchScript.util.Jdbc2MysqlSpcard;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.MsgUtil;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 新浪邮箱注册
 * @author rhy
 * @date 2018-3-15 上午10:11:28
 * @version V1.0
 */
public class SinaMailRegister {

	public static void main(String[] args) {
		//手机号账号,前面需加86
		TaskGuideBean task = new TaskGuideBean();
		task.setNick("17173425504");
		task.setPassword("chwx123456");
//		task.setNick("17173425505");
//		task.setPassword("chwx123456");
		task.setNick("17173425487");
		task.setPassword("chwx123456");
		task.setHostPort("4003");
		toRegister(task);
	}
	
	/**
	 * 新浪邮箱注册
	 * @param task
	 */
	public static void toRegister(TaskGuideBean task) {
		
		WebDriver driver = null;
		try {
			driver = getDriver();
			
			if(driver == null){
				
				isSuccess(task, "driver打开失败");
				return;
			}
			
			driver.get("https://mail.sina.com.cn/register/regmail.php");
			
			int switchCard = MsgUtil.switchCard(task.getIsApp(),task.getHostPort().substring(2));
			if(switchCard!=1){
				
				isSuccess(task, "卡池换卡失败");
				return;
			}
			String port = "";
			if(task.getHostPort().length()==4){
				port = task.getHostPort().substring(0, 1);
			}else{
				port = task.getHostPort().substring(0, 2);
			}
			
			driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div/div/ul/li[2]/a")).click();
			
			
			driver.findElement(By.name("email")).sendKeys(task.getNick());
			
			driver.findElement(By.className("freeTelCode")).click();
			
			int times = 0;
			String msgCode = null;
//			Thread.sleep(30000);
			while(true){
				
				Thread.sleep(5000);
				times++;
				if(times<8){
				
				String msg = Jdbc2MysqlSpcard.getResultHis(port);
				
				msgCode = getMsgCode(msg);
				if(StringUtils.isNotBlank(msgCode)){
					break;
				}
				}else{
						
						break;
				}
			}
			driver.findElement(By.name("msgvcode")).sendKeys("");
			
			driver.findElement(By.name("psw")).sendKeys(task.getPassword());
			
			driver.findElement(By.className("subIcoTel")).click();
			
			
		} catch (Exception e) {
			
			isSuccess(task, "注册失败");
			e.printStackTrace();
		}finally{
			
			driver.quit();
		}
		
		
	}
	
	/**
	 * 解析短信
	 * @param msg
	 * @return
	 */
	private static String getMsgCode(String msg) {
		
		try {
			String msgCode = msg.substring(msg.indexOf("【美团网】")+5,msg.indexOf("注册验证码")-1).trim();
			return msgCode;
		} catch (Exception e) {

			return null;
		}
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
		
		File file = new File("d:\\maoyan.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("3040", "d:\\maoyan.png");
		
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
