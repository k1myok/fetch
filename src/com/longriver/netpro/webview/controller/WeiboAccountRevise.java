package com.longriver.netpro.webview.controller;


import java.io.File;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.longriver.netpro.common.sina.WeiboLoginRevise;
import com.longriver.netpro.util.Configur;
import com.longriver.netpro.util.DriverGet;
import com.longriver.netpro.util.FileUtil;
import com.longriver.netpro.util.GetProprities;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.MsgUtil;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 微博账号激活
 * @author lilei
 * @2017-11-28 下午3:10:04
 * @version v1.0
 */
public class WeiboAccountRevise {
	public static boolean process = false;
	public static boolean isSend = false;
	public static void main(String[] args) {
		process = true;
		TaskGuideBean task = new TaskGuideBean();
		task.setNick("17173425386");
		task.setPassword("chwx6854068850");
		task.setHostPort("10005");
		task.setNick("17194513452");
		task.setPassword("chwx1817790093");
		
		task.setNick("17194510840");
		task.setPassword("chwx2862807");
		task.setHostPort("6001");
		task.setIsApp(5);
		toComment(task);
//		String ss = "1069 0090 10015";
//		System.out.println(ss.replaceAll(" ", ""));
		
	}
	/**
	 * 新浪微博登录入口
	 * @param task
	 */
	public synchronized static void toComment(TaskGuideBean task) {
		
		
		System.out.println("toComment");
		System.out.println("切换端口中...");
//		int suc = 1;
		int suc = MsgUtil.switchCard(task.getIsApp(),task.getHostPort());
		if(suc==1){
			//先清空存放截图的文件夹
//			FileUtil.deleteDirectoryFiles();
			//保证只返回一次结果
			isSend = true;
			torun(task);
		}else{
			isSuccess(task, "找回切换卡池失败直接退出...");
			System.out.println("找回切换卡池失败直接退出...");
			System.out.println("找回切换卡池失败直接退出...");
		}
		
	}
	public static void torun(TaskGuideBean task) {
		if(task.getPassword()==null || task.getPassword().equals("")) task.setPassword("chwx54321");
		WebDriver driver = null;
		try{
			driver = DriverGet.getDriver(); 
			
			WeiboLoginRevise.toLogin(driver, task, 0);
			Thread.sleep(1000 * 2);
			System.out.println("driver.getCurrentUrl()----->>>>"+driver.getCurrentUrl());
			//点击登录后如果还在当前页
			if(driver.getCurrentUrl().contains("login.sina.com.cn")){
				//用户名密码错误
				WebElement erroruser = driver.findElement(By.xpath("//*[@id='login_err']/span/i[2]"));
				System.out.println("content=="+erroruser.getText());
				if(erroruser.getText().contains("密码错误")){
					System.out.println("登录名或密码错误");
					//判断用户名是否存在--不存在直接注册
					String r = WeiboLoginRevise.registerAccount(driver,task);
					if(r.equals("0")){//未注册
						DriverGet.quit(driver);
						System.out.println("没注册去注册");
						WeiboRegister.toBackrun(task);//注册
						return;
					}else if(r.equals("1")){//已注册密码错误
						System.out.println("是密码错误");
						WeiboLoginRevise.wrongPwd(driver,task);
						return;
					}else{//报错
						
					}
				}else{//可能是验证码错误
					System.out.println("可能是验证码错误,不返回");
//					DriverGet.quit(driver);
					return ;
				}
				//登录成功
			}else if(driver.getCurrentUrl().contains("my.sina.com.cn")){
				driver.manage().timeouts().pageLoadTimeout(35,TimeUnit.SECONDS);
				
				driver.get("https://weibo.com/");
				System.out.println("driver.getCurrentUrl()=="+driver.getCurrentUrl());
				//进入微博时账号被封
				if(driver.getCurrentUrl().contains("unfreeze") || driver.getCurrentUrl().contains("security")){
					try {
						WebElement yanzhengBTN = driver.findElement(By.xpath("//*[@id='embed-captcha']/div/div[3]"));
						yanzhengBTN.click();
						
						Thread.sleep(5000);
						String exeMsg = "";
						try {
							exeMsg = driver.findElement(By.xpath("//*[@id='pl_unfreeze_phone']/div[1]/div[1]/span[2]")).getText();
						} catch (Exception e) {
						}
						if(exeMsg != null && exeMsg.contains("您当前使用的账号存在异常")){
							
						}else{
						Thread.sleep(1000*30);
						}
					} catch (Exception e1) {Thread.sleep(1000*30);}
					WebElement telInput = driver.findElements(By.xpath("//input[contains(@node-type,'phoneInput')]")).get(0);
					telInput.clear();telInput.sendKeys(task.getNick());
					Thread.sleep(1000 * 1);
					WebElement next1 = driver.findElements(By.xpath("//span[contains(@class,'btn_30px W_f14 W_fb')]")).get(0);
					next1.click();
					Thread.sleep(1000 * 1);
					try {
						WebElement errorInfo = driver.findElement(By.xpath("/html/body/div[3]/div/table/tbody/tr/td/div/div[2]/div/dl/dd/p[1]"));
						if(errorInfo.isDisplayed() && errorInfo.getText().contains("多个帐号")){//彻底失败
							System.out.println("您输入的手机号码已经验证过多个帐号，换个号码吧");
							DriverGet.quit(driver);
							task.setCode(21);
							isSuccess(task,"验证过多个帐号");
						}else{
							System.out.println("进入下一步");
							WeiboLoginRevise.wrongPwd(driver,task);
							return;
						}
					} catch (Exception e) {
						//直接编辑短信发送
						WebElement sendduanxin = driver.findElements(By.xpath("//span[contains(@class,'spc_txt')]")).get(1);
						System.out.println("content=="+sendduanxin.getText());
						Thread.sleep(1000 * 1);
						if(sendduanxin.isDisplayed()){
							String dxcontent = sendduanxin.getText();
							Thread.sleep(1000 * 1);
							WebElement sendcode =  driver.findElements(By.xpath("//span[contains(@class,'spc_txt')]")).get(2);
							String dxctx = sendcode.getText().replaceAll(" ", "");
							System.out.println("编辑短信:"+dxcontent+" 发送到:"+dxctx);
							//发送短信
							
							int suc = 0;
							suc = MsgUtil.sendMsg(task.getIsApp(),task.getNick(), dxcontent, dxctx,task.getHostPort());
							
							if(suc != 1){
								task.setCode(23);
								isSuccess(task, "发送短信失败");
								return;
							}
							Thread.sleep(1000*20);
							WebElement sureBnt =  driver.findElement(By.xpath("/html/body/div/div[2]/div[1]/div[2]/div[2]/a/span"));
							sureBnt.click();
							System.out.println(driver.getCurrentUrl());
							Thread.sleep(1000*5);
							task.setCode(100);
							isSuccess(task, "");
						}
					}
//					finally{
//						DriverGet.quit(driver);
//					}
				}else{
					if(task.getType()!=null && task.getType().equals("10")){
						Set<Cookie> cookies = driver.manage().getCookies();
						String skeyCookie = "";
						for (Cookie cookie : cookies) {
							skeyCookie += cookie.toString().substring(0,cookie.toString().indexOf(";")) + "; ";
						}
						WeiboRegister.toWanshan("",skeyCookie,task);
						task.setCode(100);
						isSuccess(task,"");
//						DriverGet.quit(driver);
					}else{
						WeiboLoginRevise.wrongPwd(driver,task);
					}
				}
			}else{
				try {
					if(driver.getCurrentUrl().contains("security")){
						try {
							WebElement yanzhengBTN = driver.findElement(By.xpath("//*[@id='embed-captcha']/div/div[3]"));
							yanzhengBTN.click();
							
							Thread.sleep(3000);
							String exeMsg = "";
							try {
								exeMsg = driver.findElement(By.cssSelector(".title")).getText();
							} catch (Exception e) {
							}
							if(StringUtils.isBlank(exeMsg) || !exeMsg.contains("解除帐号异常")){
								Thread.sleep(1000*30);
							}
						} catch (Exception e1) {
							Thread.sleep(1000*30);
							}
					}
					//点击下一步
					WebElement commit = driver.findElement(By.xpath("//*[@id='nextStep']"));
					commit.click();
					Thread.sleep(1000 * 2);
					
					//新逻辑
					driver.findElement(By.id("getcode")).click();//获取短信验证码
					
					Thread.sleep(5000);
					String msgCode = MsgUtil.getMsg(task.getIsApp(), task.getHostPort());
					
					if(StringUtils.isBlank(msgCode)){
						
						isSuccess(task, "未收到短信");
						return;
					}
					String code = getMsgCode(msgCode);
					
					driver.findElement(By.id("checkcode")).sendKeys(code);
					driver.findElement(By.id("nextStep")).click();
					
					try {
						String telTextError = driver.findElement(By.id("telTextError")).getText();//输入短信验证码标志信息
						
						if(StringUtils.isNotBlank(telTextError)){
							
							isSuccess(task, telTextError);
							return;
						}
					} catch (Exception e) {
					}
					Thread.sleep(3000);
					String removeResult = driver.findElement(By.cssSelector("p.page:nth-child(1)")).getText();
					
					if(StringUtils.isNotBlank(removeResult) && removeResult.contains("帐号异常解除成功")){
						
						driver.findElement(By.cssSelector("li.item:nth-child(3) > div:nth-child(1) > a:nth-child(1)")).click();//点击返回
						Thread.sleep(2000);
						isSuccess(task, "");
					}
					/*WebElement sendduanxin = driver.findElements(By.xpath("//p[contains(@class,'page page_two')]")).get(0);
					System.out.println("content=="+sendduanxin.getText());
					Thread.sleep(1000 * 1);
					if(sendduanxin.getText().contains("编辑短信")){
						WebElement dxcode = sendduanxin.findElements(By.xpath("//span[contains(@class,'S_link1 W_fb')]")).get(0);
						String dxcontent = dxcode.getText();
						Thread.sleep(1000 * 1);
						WebElement sendcode = sendduanxin.findElements(By.xpath("//span[contains(@class,'S_link1 W_fb')]")).get(1);
						String dxctx = sendcode.getText();
						System.out.println("编辑短信:"+dxcontent+" 发送到:"+dxctx);
						//发送短信
						
						int suc = 0;
						suc = MsgUtil.sendMsg(task.getIsApp(),task.getNick(), dxcontent, dxctx,task.getHostPort());
						
						if(suc != 1){
							task.setCode(23);
							isSuccess(task, "发送短信失败");
							return;
						}
						Thread.sleep(1000*20);
					}
					WebElement sended = driver.findElement(By.xpath("//*[@id='submit-btn']"));
					sended.click();
					Thread.sleep(1000*2);
					
					try {
						WebElement msgs01 = driver.findElements(By.xpath("//p[contains(@class,'form_prompt S_txt2 tmp-tips-error')]")).get(0);
						if(msgs01.getText().contains("请先发送")){
							System.out.println("请先发送短信");
							Thread.sleep(1000*15);
							sended.click();
						}
					} catch (Exception e) {}
					String pwd = "chwx"+StringUtil.randomQq();
					System.out.println("newPassword:"+pwd);
					task.setPassword(pwd);
					WebElement pwd1 = driver.findElement(By.id("pwd1"));
					pwd1.clear();pwd1.sendKeys(pwd);
					WebElement pwd2 = driver.findElement(By.id("pwd2"));
					pwd2.clear();pwd2.sendKeys(pwd);
					Thread.sleep(1000*1);
					WebElement submitbtn = driver.findElement(By.id("submit-btn"));
					submitbtn.click();
					Thread.sleep(1000*3);
					
					task.setCode(100);
					
					MQSender.toMQ(task,"");*/
				} catch (Exception e) {
					e.printStackTrace();
					MQSender.toMQ(task,"发生异常了1");
				}
//				finally{
//					DriverGet.quit(driver);
//				}
			}
			
			
		}catch(Exception e){
			System.out.println("评论异常");
			MQSender.toMQ(task,"发生异常了2");
			e.printStackTrace();
		}finally{
//			System.out.println("添加头像-------》》》》》");
//			try {
//				addPhote(driver);
//			} catch (Exception e) {
//			}
			DriverGet.quit(driver);
		}
		System.out.println("结束------------------");
		System.out.println("结束------------------");
	}
	/**
	 * 解析短信
	 * @param msgCode
	 * @return
	 */
	private static String getMsgCode(String msgCode) {
		
		System.out.println("解除短信为----->>>>"+msgCode);
		msgCode = msgCode.substring(msgCode.indexOf("您的验证码")+6,msgCode.indexOf("仅用于解除帐号异常")-1).trim();
		return msgCode;
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
	
	/**
	 * 添加头像
	 * @param driver
	 */
	public static void addPhote(WebDriver driver){
		try {
			String address = "https://account.weibo.com/set/photo";
			driver.get(address);
			//低版本需注释
			try {
				WebElement element0 = driver.findElement(By.linkText("普通方式上传"));
				element0.click();  
				Thread.sleep(1000);
			} catch (Exception e) {
				System.out.println("切换为普通模式");
			}
			
			WebElement element1 = driver.findElement(By.xpath("//*[@id='pl_headimage_headimage']/div[2]/div/div[1]/div/form/div/div[2]/a/q/input"));
			element1.sendKeys("C:\\jietu\\"+getNum()+".jpg");
			Thread.sleep(1000);
			WebElement element2 = driver.findElement(By.xpath("//*[@id='pl_headimage_headimage']/div[2]/div/div[1]/div/form/div/div[3]/a/span"));
			element2.click();
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			System.out.println("添加头像异常了");
		}
	}
	/**
	 * 获取随机数
	 * @return
	 */
	public static int getNum(){
		Random randow = new Random();
		int nextInt = randow.nextInt(100);
		return nextInt;
	}
}
