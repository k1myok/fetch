package com.longriver.netpro.webview.carcontroller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;

import com.longriver.netpro.fetchScript.util.Jdbc2MysqlSpcard;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.MsgUtil;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 百度注册
 * @author rhy
 * @date 2018-3-9 下午3:39:37
 * @version V1.0
 */
public class BaiduRegister {

	public static void main(String[] args) {
		
		//手机号账号,前面需加86
		TaskGuideBean task = new TaskGuideBean();
//		task.setNick("17173425504");
//		task.setPassword("chwx090674542");
		task.setNick("17173425505");
		task.setPassword("chwx123456");
		task.setHostPort("5001");
		toRegister(task);
	}


	public static void toRegister(TaskGuideBean task) {
		
//		WebDriver driver = null;
		try {
			
			int switchCard = MsgUtil.switchCard(task.getIsApp(),task.getHostPort().substring(2));
			if(switchCard!=1){
				
				isSuccess(task, "卡池换卡失败");
				return;
			}
			
			int id = Jdbc2MysqlSpcard.sendMsg(task.getPassword(), "10690691036590", Integer.parseInt(task.getHostPort())/1000);
			
			int i = 0;
			while(true){
				
				int result = Jdbc2MysqlSpcard.getResultFsong(id);
				Thread.sleep(5000);
				i++;
				if(result==2){
					
					isSuccess(task, "");
					break;
				}else{
					if(i>10){
						isSuccess(task, "短信发送失败");
						break;
					}
					
				}
			}
			
//			driver = getDriver();
//			
//			if(driver == null){
//				
//				isSuccess(task, "driver打开失败");
//				return;
//			}
//			
//			driver.get("https://passport.baidu.com/v2/?reg&tpl=holmes_maa&u=https://mtj.baidu.com/web/welcome/psp");
//			
//			driver.findElement(By.id("TANGRAM__PSP_3__userName")).sendKeys("b"+System.currentTimeMillis());
//			
//			if(StringUtils.isNotBlank(driver.findElement(By.id("TANGRAM__PSP_3__userNameError")).getText())){
//				
//				isSuccess(task, driver.findElement(By.id("TANGRAM__PSP_3__userNameError")).getText());
//				return;
//			}
//			driver.findElement(By.id("TANGRAM__PSP_3__phone")).sendKeys(task.getNick());
//			
//			driver.findElement(By.id("TANGRAM__PSP_3__password")).sendKeys(task.getPassword());
//			
//			driver.findElement(By.id("TANGRAM__PSP_3__verifyCodeSend")).click();
//			
//			int switchCard = MsgUtil.switchCard(task.getHostPort().substring(2));
//			if(switchCard!=1){
//				
//				isSuccess(task, "卡池换卡失败");
//				return;
//			}
//			String port = "";
//			if(task.getHostPort().length()==4){
//				port = task.getHostPort().substring(0, 1);
//			}else{
//				port = task.getHostPort().substring(0, 2);
//			}
//		
//			Thread.sleep(35000);
//			String msg = Jdbc2MysqlSpcard.getResultHis(port);
//			
//			
//			driver.findElement(By.id("TANGRAM__PSP_3__verifyCode")).sendKeys("");
//			
//			driver.findElement(By.id("TANGRAM__PSP_3__isAgree")).click();
//			
//			driver.findElement(By.id("TANGRAM__PSP_3__submit")).submit();
//			
//			System.out.println("");
//			System.out.println("");
//			System.out.println("");
//			
//			System.out.println("");
//			System.out.println("");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
//			driver.quit();
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
	
	File file = new File("d:\\weibo.png");
	FileUtils.copyFile(screenshotAs, file);
	
	String code = RuoKuai.createByPostNew("3050", "d:\\weibo.png");
	
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
	@Test
	public void getCode(){
		
		System.out.println(System.currentTimeMillis());
	}
}