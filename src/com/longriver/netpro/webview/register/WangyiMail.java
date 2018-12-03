package com.longriver.netpro.webview.register;

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

import com.longriver.netpro.fetchScript.util.Jdbc2MysqlNewVersion;
import com.longriver.netpro.fetchScript.util.Jdbc2MysqlSpcard;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.MsgUtil;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 网易邮箱注册
 * @author rhy
 * @date 2018-3-30 下午5:05:58
 * @version V1.0
 */
public class WangyiMail {

	public static void main(String[] args) {
		TaskGuideBean task = new TaskGuideBean();
		task.setCorpus("相信技术的力量");
		task.setCorpus("太危险了这世界^^^");
		task.setNick("17173425240");
		task.setPassword("chwx123456");
		task.setHostPort("11032");
		task.setId("97126");
		toComment(task);
	}
	
	/**
	 * 评论
	 * @param task
	 */
	public static void toComment(TaskGuideBean task) {
		
		WebDriver driver = null;
		try{
		driver = getDriver();
		
		if(driver == null){

			Jdbc2MysqlNewVersion.updateAccount(task);
			return;
		}
		driver.get("http://reg.email.163.com/unireg/call.do?cmd=register.entrance&from=163mail_right");
		
		Thread.sleep(3600);
		driver.findElement(By.xpath("//*[@id='tabsUl']/li[2]/a")).click();
		Thread.sleep(2400);
		driver.findElement(By.id("mobileIpt")).sendKeys(task.getNick());
		Thread.sleep(3300);
		try {
			driver.findElement(By.id("mobileRegA")).click();
			
			Thread.sleep(2000);
			String txtErr = driver.findElement(By.xpath("//*[@id='m_mobile']/span")).getText();
			Thread.sleep(1000);
			if(StringUtils.isNotBlank(txtErr) && txtErr.contains("重新激活")){
				
				Jdbc2MysqlNewVersion.updateAccountSuccess(task);
				return;
			}
		} catch (Exception e2) {
		}
		Thread.sleep(4100);
		driver.findElement(By.id("sendAcodeBtn")).click();
		Thread.sleep(2000);
		
		//换卡
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
		
		Thread.sleep(20000);
		
		int times = 0;
		String msg = "";
		while(true){
			
			if(times < 8){
				msg = Jdbc2MysqlSpcard.getResultHis(port);
				if(StringUtils.isNotBlank(msg)){
					break;
				}else{
					Thread.sleep(10000);
				}
				times++;
			}else{
				
				break;
			}
		}
		
		
		try {
			WebElement vcode = driver.findElement(By.id("mVcodeImg"));
			String picCode = getPicCode(driver, vcode);
			Thread.sleep(1000);
			driver.findElement(By.id("mVcodeIpt")).sendKeys(picCode);
			
					
		} catch (Exception e1) {
		}
		
		if(StringUtils.isBlank(msg)){//没有收到短信
			
			Jdbc2MysqlNewVersion.updateAccount(task);
			return;
		}
		String mcode = getMessageCode(msg);
		
		driver.findElement(By.id("acodeIpt")).sendKeys(mcode);//短信验证码
		driver.findElement(By.id("mobilePwdIpt")).sendKeys(task.getPassword());
		driver.findElement(By.id("mobileCfmPwdIpt")).sendKeys(task.getPassword());
		
		driver.findElement(By.id("mobileRegA")).click();
		
		Thread.sleep(5000);
		
		String currentUrl = driver.getCurrentUrl();
		if(currentUrl.indexOf("activity/reg/ok.do")>-1){
			
			Jdbc2MysqlNewVersion.updateAccountSuccess(task);
		}
		
		}catch(Exception e){
			
			Jdbc2MysqlNewVersion.updateAccount(task);
		}finally{
			
			driver.quit();
		}
		
	}
	
	/**
	 * 解析短信
	 * @param msg
	 * @return
	 */
	private static String getMessageCode(String msg) {
		
		String mcode = "";
		try {
			mcode = msg.substring(msg.indexOf("验证码")+3,msg.indexOf("网易自营电商")-1);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mcode;
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
		
		File file = new File("d:\\wangyi.png");
		FileUtils.copyFile(screenshotAs, file);
		
		String code = RuoKuai.createByPostNew("3000", "d:\\wangyi.png");
		
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
