package com.longriver.netpro.webview.carcontroller;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.longriver.netpro.util.Configur;
import com.longriver.netpro.util.FileUtil;
import com.longriver.netpro.util.GetProprities;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.TaskGuideBean;

public class PacificCarComment {
	public static boolean process = false;
	public static boolean isSend = false;
	public static void main(String[] args) {
		process = true;
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://cmt.pcauto.com.cn/topic/a0/r0/p1/ps30/t13404055.html");
		task.setCorpus("还行,看着感觉可以");
		task.setNick("17173425503");
		task.setPassword("chwx1234");
		toComment(task);
	}
	/**
	 * 评论
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
		WebDriver driver = null;
					try {
						String address = task.getAddress();
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
								
								driver.get(address);
						Thread.sleep(2000);
						String pageSource = driver.getPageSource();
//						String commentAddress = getCommentAddress(address,pageSource);
						driver.get("http://my.pcauto.com.cn/login.jsp?return=http%3A//www.pcauto.com.cn/");
						
						
						try {
							WebElement phoneNum = driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/div[1]/i[2]"));
							phoneNum.click();
							Thread.sleep(1000 * 2);
							WebElement userName = driver.findElement(By.xpath("//input[contains(@name,'username')]"));
							userName.clear();
							userName.click();
							userName.sendKeys(task.getNick()); 
							Thread.sleep(1000 * 2);
							WebElement passWord = driver.findElement(By.id("password"));
							passWord.clear();
							passWord.click();
							passWord.sendKeys(task.getPassword());
							Thread.sleep(1000 * 2);
							WebElement login = driver.findElement(By.xpath("//*[@id='loginform']/div[5]/input"));
							login.click();
							Thread.sleep(1000 * 2);
							try {
								WebElement error = driver.findElement(By.xpath("//*[@id='loginform']/div[4]/i"));
								if (error.getText().contains("用户名或密码错误，请重新输入")) {
									isSuccess(task, "用户名或密码错误");
									return;
								}
							} catch (Exception e) {
							}
							driver.get(address);
							Thread.sleep(1500);
							WebElement wantConment = driver.findElement(By.id("JtoggleCmt"));
							wantConment.click();
							Thread.sleep(1000 * 2);
							driver.switchTo().frame("cmtEditor1");
							WebElement content = driver.findElement(By.xpath("/html/body"));
//							content.clear();
							content.click();
							content.sendKeys(task.getCorpus()); 
							Thread.sleep(1500);
							
							driver.switchTo().defaultContent();
							WebElement commitCom = driver.findElement(By.id("Submit"));
							commitCom.click();
							
							isSuccess(task, "");
						} catch (InterruptedException e) {
						}
							
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block 
						e.printStackTrace();
						isSuccess(task, "评论异常");
					}finally{
						
						driver.quit();
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
}