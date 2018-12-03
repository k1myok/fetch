package com.longriver.netpro.webview.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.alibaba.fastjson.JSON;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.longriver.netpro.common.sohu.TengxunCommentScript;
import com.longriver.netpro.util.Configur;
import com.longriver.netpro.util.GetProprities;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.PngErjinzhi;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 点赞需要账号---地址为评论页
 * @author rhy
 * @date 2018-7-5 下午5:59:27
 * @version V1.0
 */
public class QQNewsComment{
	public static void main(String agrs[]){
		TaskGuideBean s = new TaskGuideBean();
		s.setPraiseWho("6415412322777693179");
		s.setAddress("http://coral.qq.com/2789214407");
		//http://news.qq.com/a/20160428/004219.htm
		//http://coral.qq.com/1384258410
		s.setNick("502023904");
		s.setPassword("lilei516688");
//		s.setNick("2861180886");
//		s.setPassword("Tennisdream1");
//		s.setNick("2051106923");
//		s.setPassword("wxb123456");
		s.setAddress("http://news.qq.com/a/20170522/016242.htm");
//		s.setAddress("http://news.qq.com/a/20170801/028934.htm");
		
		s.setAddress("http://coral.qq.com/2840283046");
		s.setPraiseWho("2840283046");
		
		s.setNick("3227576451");
		s.setPassword("cq0819010425");
		s.setCorpus("挺可怕的");
		digg(s);
//		sina(s);
	}
	
	public static void sina(TaskGuideBean taskdo){
		
			String content = "";
			try {
				String address = formatUrl(taskdo);
				String sUrl = address;
				System.out.println("sUrl=="+sUrl);
				String userid =taskdo.getNick();
				String pwd = taskdo.getPassword();
				String contents = taskdo.getCorpus();
				
				
				String threadId = sUrl.substring(sUrl.lastIndexOf("/")).replace("/", "");
				System.out.println("pwd=="+pwd);
				System.out.println("threadId=="+threadId);

				String cookie = TengxunCommentScript.getCookie(userid,pwd);
				if(cookie!=null && cookie.length()<12){//登录不成功再试一次
					cookie = TengxunCommentScript.getCookie(userid,pwd);
				}
				System.out.println("cookie.length=="+cookie.length());
				System.out.println("cookie=="+cookie);
				if(cookie!=null && cookie.length()>12){
					String skey = cookie.substring(cookie.indexOf("skey")).replace("skey", "");
					skey =skey.substring(skey.indexOf("=")+1,skey.indexOf(";"));
					String params = "targetid="+threadId
							+ "&type=1&format=SCRIPT&callback=parent.topCallback" 
							+ "&content=" + URLEncoder.encode(contents, "gb2312")
							+ "&_method=put&g_tk="+GetG_TK(skey)+"&thrdes=&code=1&source=1&subsource=0&picture=";
							
					URL u3 = new URL("http://w.coral.qq.com/article/comment/?v="+new Date().getTime());
					HttpURLConnection c3 = (HttpURLConnection) u3.openConnection();
					c3.addRequestProperty("Host", "w.coral.qq.com");
					c3.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0");
					c3.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
					c3.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
					c3.addRequestProperty("Referer", "http://www.qq.com/coral/coralBeta3/coralMainDom3.2.htm");
					c3.addRequestProperty("Cookie", cookie);
					c3.addRequestProperty("Connection", "keep-alive");
					c3.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					c3.addRequestProperty("Content-Length", String.valueOf(params.length()));
					
					c3.setDoOutput(true);
					c3.setDoInput(true);
					PrintWriter out3 = new PrintWriter(c3.getOutputStream());
					out3.print(params);
					out3.flush();
					InputStream i3 = c3.getInputStream();
					Scanner s3 = new Scanner(i3, "gb2312");
					String f = "";
					boolean b = false;
					while(s3.hasNext()){
						String scsc = s3.nextLine();
						System.out.println(scsc);
						if(scsc.contains("\"errCode\":0")){
							content = "";
						}
					}
				}else{
					content = "登录失败";
				}
				MQSender.toMQ(taskdo,content);
			} catch (Exception e) {
				MQSender.toMQ(taskdo,"报错失败");
				e.printStackTrace();
			}
			
			
		}
	
//	public  static void digg(TaskGuideBean taskdo){
////		String userid =taskdo.getNick();
////		String pwd = taskdo.getPassword();
////		String cookie = TengxunCommentScript.getCookie(userid,pwd);
////		if(cookie!=null||!cookie.equals("")){
//			URL u11;
//			String content = "";
//			try {
//				String commentId = taskdo.getPraiseWho();
//				String sUrl = taskdo.getAddress();
//				String threadId = sUrl.substring(sUrl.lastIndexOf("/")).replace("/", "");
//				u11 = new URL("http://w.coral.qq.com/article/comment/up/to/"+commentId+"?targetid="+threadId+"&callback=ding");
//				HttpURLConnection c3 = (HttpURLConnection) u11.openConnection();
//				c3.addRequestProperty("Host", "w.coral.qq.com");
//				c3.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0");
//				c3.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//				c3.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
//				c3.addRequestProperty("Referer", "http://www.qq.com/coral/coralBeta3/coralMainDom3.2.htm");
////				c3.addRequestProperty("Cookie", cookie);
//				c3.addRequestProperty("Connection", "keep-alive");
//				c3.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//				HttpURLConnection c11 = (HttpURLConnection) u11.openConnection();
//				c11.connect();
//				InputStream i11 = c11.getInputStream();
//				Scanner s11 = new Scanner(i11);
//				
//				while(s11.hasNext()){
//					String scsc = s11.nextLine();
//					System.out.println(scsc);
//				}
//				MQSender.toMQ(taskdo,"");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
////		}
//		
//	
//	}
	public static String GetG_TK(String str){  
		 int hash = 2013;  
		 for(int i = 0, len = str.length(); i < len; ++i){  
		 hash += (hash << 5) + (int)(char)str.charAt(i);  
		 }  
		 return (hash & 0x7fffffff)+"";  
	 }  
	public static String formatUrl(TaskGuideBean taskdo){
		String address = taskdo.getAddress();
		if(!address.contains(".htm")) return address;
		
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
			htmlPage = webClient.getPage(address);
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String result = htmlPage.asXml();
		result = result.substring(result.indexOf("cmt_id"));
		String cmt_id = result.substring(result.indexOf("cmt_id")+6,result.indexOf(";")).replace("=", "").trim();
		
		address = "http://coral.qq.com/"+cmt_id;
		
		return address;
	}
	
	/**
	 * 腾讯新闻点赞
	 */
	public static void digg(TaskGuideBean task){
		
		try{
			
		String targetid = getTargetid(task);
		URL url = new URL("http://w.coral.qq.com/article/comment/up/to/"+task.getPraiseWho()+"?callback=jQuery112404059075347326504_"+System.currentTimeMillis()+"&targetid="+targetid+"&_="+System.currentTimeMillis());
	
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();

		openConnection.addRequestProperty("Host", "w.coral.qq.com");
		openConnection.addRequestProperty("Connection", "keep-alive");
		openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
		openConnection.addRequestProperty("Accept", "*/*");
		openConnection.addRequestProperty("Referer", "http://page.coral.qq.com/coralpage/comment/news.html");
		openConnection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
		openConnection.addRequestProperty("Cookie", getCookie(task));
		
		openConnection.connect();
		
		Scanner sc = new Scanner(openConnection.getInputStream(),"utf-8");
		String result = "";
		while(sc.hasNextLine()){
			result += sc.nextLine();
		}
		System.out.println(result);
		
		String errCode = result.substring(result.indexOf("errCode")+9);
		errCode = errCode.substring(0,errCode.indexOf(","));
		
		if(StringUtils.isNotBlank(errCode) && "0".equals(errCode)){
			
			MQSender.toMQ(task,"");
		}else{
			
			MQSender.toMQ(task,"账号需认证");
		}
		}catch(Exception e){
			
			MQSender.toMQ(task,"点赞异常");
			e.printStackTrace();
		}
	}
	private static String getTargetid(TaskGuideBean task) {
		String address = task.getAddress();
		String targetId = address.substring(address.lastIndexOf("/")+1);
		return targetId;
	}

	/**
	 * @param task
	 */
	public static String getCookie(TaskGuideBean task) {
		
		WebDriver driver = null;
		try{
			
		Configur p = GetProprities.paramsConfig;
		String firefoxUrl = p.getProperty("firefoxurl");
		
		System.setProperty("webdriver.firefox.bin",firefoxUrl);
		driver = new FirefoxDriver(); 
		driver.get("http://ui.ptlogin2.qq.com/cgi-bin/login?hide_title_bar=0&low_login=0&qlogin_auto_login=1&no_verifyimg=1&link_target=blank&appid=636014201&target=self&s_url=http%3A//www.qq.com/qq2012/loginSuccess.htm");  
		
		WebElement login = driver.findElement(By.id("switcher_plogin"));
		login.click();
		WebElement username = driver.findElement(By.id("u"));
		username.clear();
		username.sendKeys(task.getNick());
		
		WebElement password = driver.findElement(By.id("p"));
		password.clear();
		password.sendKeys(task.getPassword());
		
		WebElement loginButton = driver.findElement(By.id("login_button"));
		loginButton.click();
		Set<Cookie> cookies = driver.manage().getCookies();
		
		String skeyCookie = "";
		for (Cookie cookie : cookies) {
			
			skeyCookie += cookie.toString().substring(0,cookie.toString().indexOf(";")) + "; ";
		}
		return skeyCookie;
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			driver.quit();
		}
		return null;
	}
	@Test
	public void getTask(){
		TaskGuideBean s = new TaskGuideBean();
		s.setPraiseWho("6420472517207709483");
		s.setAddress("http://coral.qq.com/2840283046");
		//http://news.qq.com/a/20160428/004219.htm
		//http://coral.qq.com/1384258410
		s.setNick("502023904");
		s.setPassword("lilei516688");
		digg(s);
	}
}
