package com.longriver.netpro.common.sohu;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

public class SohuDriverLogin {
	
	public static String toLogin(WebDriver driver,TaskGuideBean task){
		if(task.getMark()!=null && "newVersion".equals(task.getMark())){
			return weiboLogin(driver,task);//微博账号登录
		}else{
			return sohuLogin(driver,task);//搜狐账号登录
		}
	}
	public static String sohuLogin(WebDriver driver,TaskGuideBean task){
		try {
			driver.get("https://m.sohu.com/i/login/?_once_=000023_login_page&biz_name=comment&from="+task.getAddress());
			
			WebElement username = driver.findElement(By.xpath("//*[@id='container']/div/form/div[1]/div[2]/input"));
			username.clear();
			username.sendKeys(task.getNick());
			
			WebElement password = driver.findElement(By.xpath("//*[@id='container']/div/form/div[2]/div[2]/input"));
			password.clear();
			password.sendKeys(task.getPassword());
			
			WebElement loginButtons = driver.findElement(By.xpath("//*[@id='container']/div/form/div[3]/button"));
			loginButtons.click();
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(driver.getCurrentUrl().contains("m.sohu.com/i/login")){
			return "登录失败!";
		}else{
			return "suc";
		}
		
	}
	public static String weiboLogin(WebDriver driver,TaskGuideBean task){
		try {
			driver.get("https://passport.sohu.com/user/tologin");
			WebElement loginDiv = driver.findElements(By.xpath("//i[contains(@class,'img-sina')]")).get(0);
			loginDiv.click();
			Thread.sleep(1000);
			
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
			driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[2]/div/p/a[1]")).click();
			String errormsg = driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText();
			if(StringUtils.isNotBlank(errormsg)){
				if(errormsg.contains("验证码")){
					try {//有验证码
						WebElement preimgCode = driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/span/img"));
						if(preimgCode.isDisplayed()){
							String precode = getPicNew(driver, preimgCode);
							driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/input")).clear();
							driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/input")).sendKeys(precode);
						}
					} catch (Exception e1) {}
				}else if(errormsg.contains("密码错误")){
					return "用户名或密码错误";
				}
				driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[2]/div/p/a[1]")).click();
				//第二次输入验证码
				Thread.sleep(2000);
				try {
					WebElement imgCode = driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/span/img"));
					String code = getPicNew(driver, imgCode);
					driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/input")).clear();
					driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[1]/p[3]/input")).sendKeys(code);
					driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[2]/div/p/a[1]")).click();
					Thread.sleep(3000);
				} catch (Exception e) {}
			}
			//确认登录
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
			} catch (Exception e) {}
			//错误信息
			try {
				String errormsg2 = driver.findElement(By.xpath("//*[@id='outer']/div/div[2]/form/div/div[1]/div[2]/span[2]")).getText();
				if(StringUtils.isNotBlank(errormsg2)){
					return errormsg2;
				}
			} catch (Exception e) {
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return "登录报错";
		}
		return "suc";
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
	
}
