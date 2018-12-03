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
/**
 * 易车网
 * @author wyanegao
 *
 */
public class YiCheComment {
	public static boolean process = false;
	public static boolean isSend = false;
	public static void main(String[] args) {
		process = true;
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://www.autoreport.cn/znCar/20180306/1507287027.html");
		task.setCorpus("厉害了，其实都挺好的");
		task.setNick("17080621281");
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
								driver.get("http://i.yiche.com/authenservice/login.html?returnurl=http%3A%2F%2Fbeijing.bitauto.com%2F%3FWT.mc_id%3Dbdcyt__yichewang%26referrer%3Dhttps%3A%2F%2Fwww.baidu.com%2Fbaidu.php%3Fsc.K000000fJeHuq9k18PzDEoS22b0ERM_hMhnWuf_utNLmOFZuvVLyvbs0ZRuE_95dWmYnO9LBwZURzSG5QdiYFvzJEtq6n2bMhLRXaCNd8Cr5XAxjoWZ6eHQbK4hoRhuS4lgQQ9m19G4QbpkDmLuVoFiyQzrBUF4Tly_F15Atjfxz45mQG0.7D_aqqKMKfTBjESLS94pmXgDkxIjCCTEQCxjRM6u12N9h9mer1x_tN0.U1Yk0ZDqYVX1VTL30ZKGm1Yk0ZfqYVX1VTL30A-V5HcsP0KM5gKzm6KdpHdBmy-bIfKspyfqnfKWpyfqn1T30AdY5HDsnHIxnH0krNtknjD1g1DsnWPxn1czr7t1PW0k0AVG5H00TMfqrHn40ANGujYkPjmzg1cknjbdg1D3PHR4g1DLnj0kg1csrHc30AFG5HcsP7tkPHR0UynqP1cLPjR3n161g1TzP1fdrjn3rNtknj6kPWRLPHcYg17xnH0zg100TgKGujYs0Z7Wpyfqn0KzuLw9u1Ys0A7B5HKxn0K-ThTqn0KsTjYs0A4vTjYsQW0snj0snj0s0AdYTjYs0AwbUL0qn0KzpWYk0Aw-IWdsmsKhIjYs0ZKC5H00ULnqn0KBI1Ykn0K8IjYs0ZPl5fKYIgnqPHR4PjnYPHmknWndPjTsrjTzP6Kzug7Y5HDdnWDkPWn3rH0vrHn0Tv-b5yfknvcdny7hnj0snjFbnWc0mLPV5RcLwWmzPDR4rj9Arj0vPHD0mynqnfKsUWYs0Z7VIjYs0Z7VT1Ys0ZGY5H00UyPxuMFEUHYsg1Kxn7tsg1Kxn0Kbmy4dmhNxTAk9Uh-bT1Ysg1Kxn7tsg1DYnWc4rH7xn0Ksmgwxuhk9u1Ys0AwWpyfqn0K-IA-b5iYk0A71TAPW5H00IgKGUhPW5H00Tydh5H00uhPdIjYs0AulpjYs0Au9IjYs0ZGsUZN15H00mywhUA7M5HD0UAuW5H00mLFW5HDvP1D1%26ck%3D4555.2.104.440.155.590.219.367%26shh%3Dwww.baidu.com%26sht%3Dbaidu%26us%3D1.0.1.0.1.301.0%26ie%3Dutf-8%26f%3D8%26tn%3Dbaidu%26wd%3D%25E6%2598%2593%25E8%25BD%25A6%25E7%25BD%2591%26rqlang%3Dcn%26inputT%3D1946%26bc%3D110101");
								try {
									WebElement name = driver.findElement(By.id("txt_LoginName"));
									name.clear();
									name.click();
									name.sendKeys(task.getNick());
									Thread.sleep(1000 * 2);
									WebElement passWord = driver.findElement(By.id("txt_Password"));
									passWord.clear();
									passWord.click();
									passWord.sendKeys(task.getPassword());
									Thread.sleep(1000 * 2);
									WebElement login = driver.findElement(By.id("btn_Login"));
									login.click();
									try {
										WebElement error = driver.findElement(By.id("//*[@id='form1']/ul/li[2]/div/em"));
										if (error.getText().contains("请输入正确的密码") || error.getText().contains("帐号或密码错误")) {
											isSuccess(task, "帐号或密码错误");
											return;
										}
									} catch (Exception e) {
										
									}
									Thread.sleep(1000 * 2);
									driver.get(task.getAddress());
									WebElement content = driver.findElement(By.xpath("//*[@id='CreateCommentReply_txtContent']"));
									content.clear();
									content.click();
									content.sendKeys(task.getCorpus());
									Thread.sleep(1000 * 2);
									WebElement commit = driver.findElement(By.xpath("//*[@id='CreateCommentReply_aSubmit']"));
									commit.click();
									
									isSuccess(task, "");
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
		
		MQSender.toMQ(task,msg);
	}
}