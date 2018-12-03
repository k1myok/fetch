package com.longriver.netpro.webview.carcontroller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

import net.sourceforge.jtds.jdbc.Driver;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.longriver.netpro.fetchScript.util.Jdbc2MysqlSpcard;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.MsgUtil;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 大众点评--动态验证码登录
 * @author rhy
 * @date 2018-4-27 下午4:05:51
 * @version V1.0
 */
public class DianpingComment {

	public static void main(String[] args) {
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://s.dianping.com/event/1172123079");
		task.setCorpus("相信技术的力量");
//		task.setCorpus("太危险了这世界^^^");
		task.setNick("17173425268");
		task.setPassword("chwx123456");
		task.setHostPort("8029");
		toComment(task);
	}

	public static void toComment(TaskGuideBean task) {
		
		WebDriver driver = getDriver();
		
		try {
//			driver.get("https://www.dianping.com/login?redir=http%3A%2F%2Fwww.dianping.com%2F");
			driver.get("http://www.dianping.com/");
			driver.findElement(By.xpath("//*[@id='userinfo']/div/div[4]/div[1]/a[1]")).click();
			Thread.sleep(2000);
			driver.switchTo().frame(0);

			Thread.sleep(2000);
			int switchCard = MsgUtil.switchCard(task.getIsApp(),task.getHostPort().substring(2));
			if(switchCard!=1){
				
				isSuccess(task, "卡池换卡失败");
				return;
			}
			driver.findElement(By.cssSelector(".bottom-password-login")).click();
//			driver.findElement(By.xpath("//*[@id='tab-account']")).click();
			
			driver.findElement(By.id("mobile-number-textbox")).sendKeys(task.getNick());
			
			Thread.sleep(3000);
			driver.findElement(By.id("send-number-button")).click();
			
			Thread.sleep(3000);
			String alertText = "";
			for(int i=0;i<=4;i++){
				
				try {
					WebElement mobileCaptcha = driver.findElement(By.xpath("//*[@id='captcha-mobile-container']/div[2]/img"));
					Thread.sleep(10000);
					String picCode = getPicCode(driver, mobileCaptcha);
					
					driver.findElement(By.id("captcha-textbox")).clear();
					driver.findElement(By.id("captcha-textbox")).sendKeys(picCode);
					
					Thread.sleep(3000);
					alertText = driver.findElement(By.xpath("//*[@id='alert']/span")).getText();

					if((StringUtils.isNotBlank(alertText) && alertText.contains("输入的验证码错误")) || (StringUtils.isNotBlank(alertText) && alertText.contains("请输入图形验证码"))){
						
						driver.findElement(By.xpath("//*[@id='captcha-mobile-container']/div[2]/img")).click();
						
						continue;
					}else{
						if(StringUtils.isNotBlank(alertText) && alertText.contains("已发送")){
							
							break;
						}
					}
				} catch (Exception e) {
				}
			}
			
			if(StringUtils.isNotBlank(alertText) && !alertText.contains("动态码已发送，请查看手机")){
				
				isSuccess(task, alertText);
				return;
				
			}
			String port = "";
			if(task.getHostPort().length()==4){
				port = task.getHostPort().substring(0, 1);
			}else{
				port = task.getHostPort().substring(0, 2);
			}
			
			Thread.sleep(20000);
			int times = 0;
			String msgCode = "";
			while(true){
				
				String msg = Jdbc2MysqlSpcard.getResultHis(port);
				if(StringUtils.isNotBlank(msg)){
					
					msgCode = getMessageCode(msg);
					break;
				}else{
					
					Thread.sleep(10000);
					
					if(times >= 4){
						break;
					}
					times++;
				}
			}
			if(StringUtils.isBlank(msgCode)){
				
				isSuccess(task, "未收到短信验证码");
				return;
			}
			
			driver.findElement(By.id("number-textbox")).sendKeys(msgCode);
			
			driver.findElement(By.id("login-button-mobile")).click();
			
			Thread.sleep(2000);
			
			driver.get(task.getAddress());
			
			Thread.sleep(2000);
			driver.findElement(By.id("QuestionContent")).clear();
			driver.findElement(By.id("QuestionContent")).sendKeys(task.getCorpus());
			Thread.sleep(1000);
			driver.findElement(By.id("J_noteSubmit")).click();
			
			
			isSuccess(task, "");
			System.out.println();
		} catch (Exception e) {
			
			isSuccess(task, "评论错误");
			e.printStackTrace();
		}finally{
			
//			driver.quit();
		}
	}
	/**
	 * 解析短信
	 * @param msg
	 * @return
	 */
	private static String getMessageCode(String msg) {
		
		String code = "";
		try {
			code = msg.substring(msg.indexOf("【大众点评】")+6,msg.indexOf("大众点评网手机验证码")-1).trim();
		} catch (Exception e) {
		}
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
	 * 获取验证码
	 * @param driver
	 * @throws IOException 
	 */
	private static String getPicCode(WebDriver driver,WebElement comment){
		
		String code = "";
		try {
			File screenshotAs = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			
			BufferedImage bufferedImage = ImageIO.read(screenshotAs);
			
			Point point = comment.getLocation();
			
			int width = comment.getSize().getWidth();
			int height = comment.getSize().getHeight();
//		BufferedImage subimage = bufferedImage.getSubimage(point.getX(), point.getY(), width, height);
			BufferedImage subimage = bufferedImage.getSubimage(1105, 393, width, height);
			ImageIO.write(subimage, "png", screenshotAs);
			
			File file = new File("d:\\dianping.png");
			FileUtils.copyFile(screenshotAs, file);
			
			code = RuoKuai.createByPostNew("3040", "d:\\dianping.png");
		} catch (WebDriverException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return code;
	}
	/**
	 * 判断是否成功
	 */
	public static void isSuccess(TaskGuideBean task,String msg){
			MQSender.toMQ(task,msg);
	}
}
