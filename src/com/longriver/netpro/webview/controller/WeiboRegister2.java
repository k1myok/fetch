package com.longriver.netpro.webview.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import com.longriver.netpro.common.sina.WeiboSina;
import com.longriver.netpro.util.Configur;
import com.longriver.netpro.util.DateUtil;
import com.longriver.netpro.util.DriverGet;
import com.longriver.netpro.util.GetProprities;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.MsgUtil;
import com.longriver.netpro.util.MsgUtilBak;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * sina微博注册
 * @author lilei
 * @2018-01-24 10:10:04
 * @version v1.0
 */
public class WeiboRegister2 {
	public static boolean process = false;
	public static boolean isSend = false;
	public static void main(String[] args) {
		process = true;
		TaskGuideBean task = new TaskGuideBean();
		task.setNick("15652240394");
		task.setNick("17194517981");
		task.setNick("17194517993");
		task.setNick("17194513392");
		task.setPassword("chwx1234");
		task.setTestAccount("八圣吉祥颂");
		torun(task);
	}

	public static void torun(TaskGuideBean task){
		isSend = true;
		toRegister(task);
	}
	public static void toRegister(TaskGuideBean task) {
		WebDriver driver = null;
		String content = "";
		task.setCode(10);
		try{
			Configur config = GetProprities.paramsConfig;
			String firefoxUrl = config.getProperty("firefoxurl");
			
			FirefoxProfile profile = new FirefoxProfile();
			//禁用css
//			profile.setPreference("permissions.default.stylesheet", 2);
			//不加载图片
			profile.setPreference("permissions.default.image", 2);
			//##禁用Flash 
			profile.setPreference("dom.ipc.plugins.enabled.libflashplayer.so","false");
			
			System.setProperty("webdriver.firefox.bin",firefoxUrl);
			driver = new FirefoxDriver(profile); 
			driver.get("https://weibo.com/signup/signup.php");
			driver.manage().timeouts().implicitlyWait(180, TimeUnit.SECONDS);
			
			WebElement username = driver.findElements(By.xpath("//input[contains(@name,'username')]")).get(0);
			Thread.sleep(1000 * 2);
			username.click();
			username.clear();
			Thread.sleep(1000 * 2);
			username.sendKeys(task.getNick());
			driver.findElements(By.xpath("//input[contains(@name,'passwd')]")).get(0).click();
			
			WebElement password = driver.findElements(By.xpath("//input[contains(@name,'passwd')]")).get(0);
			password.clear();
			password.sendKeys(task.getPassword());
			password.click();
			Thread.sleep(1000 * 1);
			try {
				WebElement isReg = driver.findElements(By.xpath("//div[contains(@node-type,'mobilesea_tip')]")).get(0);
				if(isReg.getText().contains("已注册")){
					System.out.println("该手机号已注册");
					task.setCode(100);
					isSuccess(task, "");//成功
					return;
				}
			} catch (Exception e) {}
			WebElement getCode = driver.findElements(By.xpath("//a[contains(@action-data,'type=sendsms')]")).get(0);
			Set<Cookie> cookies = driver.manage().getCookies();
			String skeyCookie = "";
			for (Cookie cookie : cookies) {
				skeyCookie += cookie.toString().substring(0,cookie.toString().indexOf(";")) + "; ";
			}
			if(getCode.isDisplayed()){
				System.out.println("收短信激活码");
				shouMsg(skeyCookie,task);
				Thread.sleep(1000 * 1);
				String vcode = MsgUtilBak.getSinaCodeMsg(task.getNick(),task.getHostPort(),0);
				if(vcode==null){
					task.setCode(22);
					isSuccess(task, "收不到验证码");
					return;
				}
				skeyCookie = zhuce(skeyCookie,task,vcode);
				//点击立即注册
			}else{
				System.out.println("发短信激活码");
				//点击立即注册
				WebElement register = driver.findElements(By.xpath("//a[contains(@refake-type,'submit')]")).get(0);
				register.click();
				Thread.sleep(1000 * 3);
				WebElement msgInfo = driver.findElements(By.xpath("//dd[contains(@class,'msg_info')]")).get(0);
				System.out.println(msgInfo.getText());//编辑短信内容 794914 至 1069 0090 166 即可成功注册微博
				String cnt = getMsgContent(msgInfo.getText(),1);
				String totel = getMsgContent(msgInfo.getText(),2);
				if(!cnt.equals("") && !totel.equals("")){
					
					
					int suc = 0;
					suc = MsgUtil.sendMsg(task.getIsApp(),task.getNick(), cnt, totel,task.getHostPort());
					
					if(suc != 1){
						task.setCode(23);
						isSuccess(task, "发送短信失败");
						return;
					}
					Thread.sleep(1000 * 25);
				}
			}
			if(task.getTestAccount()==null || task.getTestAccount().equals("")) task.setTestAccount(StringUtil.randomName());
			try {
				wanshaninfo(content,skeyCookie,task);
				if(task.getCode()==22){
					task.setTestAccount(StringUtil.randomName());
					wanshaninfo(content,skeyCookie,task);
				}
				if(task.getCode()==22){
					task.setTestAccount(StringUtil.randomName()+StringUtil.randomString());
					wanshaninfo(content,skeyCookie,task);
				}
				try {
					if(task.getCode()==100){
						finashed(skeyCookie);
						//添加教育,公司,联系方式等信息
						WeiboRegPerData.wanshaninfo(task,skeyCookie);
					}
				} catch (Exception e) {}
				System.out.println("昵称:"+task.getTestAccount());
				weiboZf(skeyCookie);
			} catch (Exception e) {
				e.printStackTrace();
				task.setCode(21);
				content = "注册成功,昵称未填";
			}finally{
				isSuccess(task, "");//成功
				DriverGet.quit(driver);
			}
		}catch(Exception e){
			System.out.println("评论异常");
			e.printStackTrace();
		}finally{
			DriverGet.quit(driver);
		}
		System.out.println("结束------------------");
		System.out.println("结束------------------");
	}
	public static void shouMsg(String skeyCookie,TaskGuideBean task){
		try {
			URL u5 = new URL("https://weibo.com/signup/v5/formcheck?type=sendsms&value="+task.getNick()+"&zone=0086&__rnd=" + new Date().getTime());
			HttpURLConnection c5 = (HttpURLConnection) u5.openConnection();
			 c5.addRequestProperty("Host", "weibo.com");
			 c5.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			 c5.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			 c5.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
			 c5.addRequestProperty("Content-Type", "application/x-www-form-urlencoded;");
			 c5.addRequestProperty("X-Requested-With", "XMLHttpRequest");
			 c5.addRequestProperty("Referer", "https://weibo.com/signup/signup.php");
			 c5.addRequestProperty("Cookie", skeyCookie);
			 c5.addRequestProperty("Connection", "keep-alive");
			 c5.addRequestProperty("Pragma", "no-cache");
			 c5.addRequestProperty("Cache-Control", "no-cache");
			 c5.setDoInput(true);
			 c5.setDoOutput(true);
			 c5.setConnectTimeout(1000 * 30);
			 c5.setReadTimeout(1000 * 20);
			 c5.connect();
			 InputStream i5 = null;
	 		try{
	 			i5 = c5.getInputStream();
	 		}catch(Exception e){
	 			e.printStackTrace();
	 		}
	 		Scanner s5 = new Scanner(i5);
	 		while(s5.hasNext()){
	 			String scsc = StringUtil.decodeUnicode(s5.nextLine());
	 			System.out.println(scsc);
	 		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String zhuce(String skeyCookie,TaskGuideBean task,String code){
		try {
			String paramss = "zone=0086" +
					"&username=" +task.getNick()+
					"&passwd=" +WeiboSina.getPwd(task.getNick(), task.getPassword())+
					"&inviteCode=&from=&callback=&mcode=&mbk=" +
					"&regtime=" +new Date().getTime()+
					"&salttime=9edf42361707c0fc64aaead61b244b3f" +
					"&sinaid=0eb75a8485415317f9a069be257a17f7" +
					"&page=mobile&invitesource=0&lang=zh-cn&backurl=&appsrc=&showlogo=&c=" +
					"&pincode=" +code+
					"&rejectFake=clickCount%253D3%2526subBtnClick%253D0%2526keyPress%253D18%2526menuClick%253D0%2526mouseMove%253D343%2526checkcode%253D0%2526subBtnPosx%253D657%2526subBtnPosy%253D517%2526subBtnDelay%253D61%2526keycode%253D0%252C0%252C0%252C0%252C0%252C0%252C0%252C0%252C0%252C0%252C0%252C0%252C0%252C0%252C0%252C0%252C0%252C123%2526winWidth%253D1600%2526winHeight%253D763%2526userAgent%253DMozilla%252F5.0%2520%28Windows%2520NT%25206.1%253B%2520WOW64%253B%2520rv%253A58.0%29%2520Gecko%252F20100101%2520Firefox%252F58.0&entry=&replaceurl=%2F%2Fweibo.com%2Fsignup%2Fv5%2Fajaxreg";
			URL u5 = new URL("https://weibo.com/signup/v5/reg");
			HttpURLConnection c5 = (HttpURLConnection) u5.openConnection();
			c5.addRequestProperty("Host", "weibo.com");
			c5.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			c5.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			c5.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
			c5.addRequestProperty("Content-Type", "application/x-www-form-urlencoded;");
			c5.addRequestProperty("X-Requested-With", "XMLHttpRequest");
			c5.addRequestProperty("Referer", "https://weibo.com/signup/signup.php");
			c5.addRequestProperty("Content-Length", String.valueOf(paramss.length()));
			c5.addRequestProperty("Cookie", skeyCookie);
			c5.addRequestProperty("Connection", "keep-alive");
			c5.addRequestProperty("Pragma", "no-cache");
			c5.addRequestProperty("Cache-Control", "no-cache");
			c5.setDoInput(true);
			c5.setDoOutput(true);
			c5.setConnectTimeout(1000 * 30);
			c5.setReadTimeout(1000 * 20);
			c5.connect();
			PrintWriter o5 = new PrintWriter(c5.getOutputStream());
	 		o5.print(paramss);
	 		o5.flush();
	 		Map<String, List<String>> headers2 = c5.getHeaderFields(); 
		    for(Map.Entry<String,List<String>> entry : headers2.entrySet()){
		        if(entry.getKey() != null && (entry.getKey().indexOf("Set-Cookie") > -1)){
		            for(String value : entry.getValue()){  
		            	skeyCookie = skeyCookie + value.substring(0, value.indexOf(";") + 1);
		            }  
		        }
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return skeyCookie;
	}
	public static void toWanshan(String content,String skeyCookie,TaskGuideBean task){
		try {
			try {
				wanshaninfo(content,skeyCookie,task);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if(task.getCode()==22){
				task.setTestAccount(StringUtil.randomName());
				wanshaninfo(content,skeyCookie,task);
			}
			if(task.getCode()==22){
				task.setTestAccount(StringUtil.randomName()+StringUtil.randomString());
				wanshaninfo(content,skeyCookie,task);
			}
			try {
				if(task.getCode()==100){
					finashed(skeyCookie);
					//添加教育,公司,联系方式等信息
					WeiboRegPerData.wanshaninfo(task,skeyCookie);
				}
			} catch (Exception e) {}
			System.out.println("昵称:"+task.getTestAccount());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void getArrlowNick(WebDriver driver,TaskGuideBean task){
		try {
			WebElement nickname = driver.findElements(By.xpath("//input[contains(@node-type,'nickname')]")).get(0);
			Thread.sleep(1000 * 2);
			nickname.click();
			nickname.clear();
			nickname.sendKeys(task.getTestAccount());
			Thread.sleep(1000 * 1);
			WebElement yearsel = driver.findElement(By.xpath("//*[@id='pl_guide_front_recommend']/div[2]/div[2]/div[3]/select[1]"));
			yearsel.click();
			Thread.sleep(1000l * 2);
			WebElement msgInfo = driver.findElements(By.xpath("//span[contains(@class,'icon_succ')]")).get(0);
			if(!msgInfo.isDisplayed()){
				task.setTestAccount(task.getTestAccount()+StringUtil.randomString());
				getArrlowNick(driver,task);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public static String getMsgContent(String content,int num){
		//编辑短信内容 794914 至 1069 0090 166 即可成功注册微博
		try {
			int ind1 = content.indexOf("至");
			if(num==1)
				return content.substring(7, ind1-1).trim();
			else
				return content.substring(ind1+2,content.length()-9).replaceAll(" ", "");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	public static String wanshaninfo(String content,String skeyCookie,TaskGuideBean task) throws Exception{
		URL u5 = new URL("https://weibo.com/nguide/aj/register4?__rnd=" + new Date().getTime());
        HttpURLConnection c5 = (HttpURLConnection) u5.openConnection();
        int num = StringUtil.getRandom();
        String birthday = 1987+num+"-"+num+"-"+(num+7);
        String paramss = "_t=0"
         		+ "&birthday="+birthday
         		+ "&city=1"
         		+ "&gender=f"
         		+ "&nickname=" + URLEncoder.encode(task.getTestAccount(),"utf-8")
         		+ "&nickRecommend="
         		+ "&province=11";
         c5.addRequestProperty("Host", "weibo.com");
         c5.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
         c5.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
         c5.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
         c5.addRequestProperty("Content-Type", "application/x-www-form-urlencoded;");
         c5.addRequestProperty("X-Requested-With", "XMLHttpRequest");
         c5.addRequestProperty("Referer", "https://weibo.com/nguide/recommend?ugf=reg&lang=zh-cn&backurl=&sudare");
         c5.addRequestProperty("Content-Length", String.valueOf(paramss.length()));
         c5.addRequestProperty("Cookie", skeyCookie);
         c5.addRequestProperty("Connection", "keep-alive");
         c5.addRequestProperty("Pragma", "no-cache");
         c5.addRequestProperty("Cache-Control", "no-cache");
         c5.setDoInput(true);
         c5.setDoOutput(true);
         c5.setConnectTimeout(1000 * 30);
         c5.setReadTimeout(1000 * 20);
         PrintWriter o5 = new PrintWriter(c5.getOutputStream());
 		o5.print(paramss);
 		o5.flush();
 		InputStream i5 = null;
 		try{
 			i5 = c5.getInputStream();
 		}catch(Exception e){
 			e.printStackTrace();
 		}
 		Scanner s5 = new Scanner(i5);
 		while(s5.hasNext()){
 			String scsc = StringUtil.decodeUnicode(s5.nextLine());
 			System.out.println(scsc);
 			System.out.println("昵称:"+task.getTestAccount()+", 完善资料=="+scsc);
 			content = WeiboSina.getResultContent(scsc,content,task);
 		}
 		return content;
	}
	public static void finashed(String skeyCookie){
		try {
			URL u5 = new URL("https://weibo.com/nguide/aj/finish4");
			HttpURLConnection c5 = (HttpURLConnection) u5.openConnection();
			String paramss = "data={'1042015:tagCategory_026':['2817145687','2215805097','2784647491','2693420092','1702275522'],'1042015:tagCategory_027':['6060135632','2358420085','2027870265','1741666603','2132723453'],'1042015:tagCategory_034':['2019719255','5837513668','1897410357','2792195654','5863606615'],'1042015:tagCategory_050':['1392726625','6245651750','2738020975','2787899254','5961081364'],'1042015:tagCategory_999':['5674071345','1750344480','2994719140','5634740555','5445984988']}";
			c5.addRequestProperty("Host", "weibo.com");
			c5.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			c5.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			c5.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
			c5.addRequestProperty("Content-Type", "application/x-www-form-urlencoded;");
			c5.addRequestProperty("X-Requested-With", "XMLHttpRequest");
			c5.addRequestProperty("Referer", "https://weibo.com/nguide/interests?backurl=");
			c5.addRequestProperty("Content-Length", String.valueOf(paramss.length()));
			c5.addRequestProperty("Cookie", skeyCookie);
			c5.addRequestProperty("Connection", "keep-alive");
			c5.addRequestProperty("Pragma", "no-cache");
			c5.addRequestProperty("Cache-Control", "no-cache");
			c5.setDoInput(true);
			c5.setDoOutput(true);
			c5.setConnectTimeout(1000 * 30);
			c5.setReadTimeout(1000 * 20);
			PrintWriter o5 = new PrintWriter(c5.getOutputStream());
			o5.print(paramss);
			o5.flush();
			InputStream i5 = null;
			try{
				i5 = c5.getInputStream();
			}catch(Exception e){
				e.printStackTrace();
			}
			Scanner s5 = new Scanner(i5);
			while(s5.hasNext()){
				String scsc = StringUtil.decodeUnicode(s5.nextLine());
//				System.out.println("scsc=="+scsc);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	public static void weiboZf(String cookie){
		try {
			long rnd = new Date().getTime();
			URL u5 = new URL("https://weibo.com/p/aj/v6/mblog/add?domain=100505&ajwvr=6&__rnd=" + rnd);
			//http://weibo.com/p/aj/v6/mblog/add?domain=100505&ajwvr=6&__rnd=1416423644659
			String contents = DateUtil.getCurrentTime()+",我的微博生涯!";
			HttpURLConnection c5 = (HttpURLConnection) u5.openConnection();
			String paramss = "text=" + URLEncoder.encode(contents, "utf-8")
					+ "&pic_id="
					+ "&rank=0"
					+ "&rankid="
					+ "&_surl="
					+ "&hottopicid="
					+ "&location=home"
					+ "&module=stissue"
					+ "&_t=0";
			c5.addRequestProperty("Host", "weibo.com");
//		c5.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			c5.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			c5.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
			c5.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			c5.addRequestProperty("X-Requested-With", "XMLHttpRequest");
			c5.addRequestProperty("Referer", "http://weibo.com/u/2662279417/home?leftnav=1&wvr=5");
			c5.addRequestProperty("Content-Length", String.valueOf(paramss.length()));
			c5.addRequestProperty("Cookie", cookie);
			c5.addRequestProperty("Connection", "keep-alive");
			c5.addRequestProperty("Pragma", "no-cache");
			c5.addRequestProperty("Cache-Control", "no-cache");
			c5.setDoInput(true);
			c5.setDoOutput(true);
			c5.setConnectTimeout(1000 * 30);
			c5.setReadTimeout(1000 * 20);
			PrintWriter o5 = new PrintWriter(c5.getOutputStream());
			o5.print(paramss);
			o5.flush();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
