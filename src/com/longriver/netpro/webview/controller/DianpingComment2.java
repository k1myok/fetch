package com.longriver.netpro.webview.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 大众点评--账号、密码登录
 * @author rhy
 * @date 2018-4-27 下午4:05:51
 * @version V1.0
 */
public class DianpingComment2 {

	public static void main(String[] args) {
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("http://www.dianping.com/shop/93242709");
//		task.setAddress("http://www.dianping.com/shop/99250776");
		task.setAddress("http://www.dianping.com/shop/99250776");
		task.setCorpus("");
		task.setCorpus("相信教育的力量，海风教育棒棒的，为你打call\r");
		task.setCorpus("课堂的知识和游戏的穿插让教学变得更顺畅");
		task.setCorpus("首先要了解学生，首先要努力使自己成为学生的知心朋友，让他们主动地与自己交往，在交往中了解他们");
		task.setCorpus("应该先把课文要讲的内容，一一排列好，先讲什么，再讲什么，这样有条理，又清晰，可以在黑板写下你的笔录。 ");
		task.setNick("17080625988");
		task.setNick("17080626027");
		task.setNick("17194510858");
		task.setNick("17194513403");
		task.setNick("17194510866");
		task.setNick("17194510891");
		task.setPassword("chwx123456");
		toComment(task);
//		toComment1(task);
//		getCookie(task);
	}

	public static void toComment1(TaskGuideBean task) {
		
		if(task.getCorpus().length()<15){
			
			isSuccess(task, "请输入15字以上的点评");
			return;
		}
		try {
//			POST /ajax/json/review/reviewAction HTTP/1.1
//			Host: www.dianping.com
//			Connection: keep-alive
//			Content-Length: 795
//			Accept: application/json, */*
//			Origin: http://www.dianping.com
//			X-Requested-With: XMLHttpRequest
//			X-Request: JSON
//			User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36
//			Content-type: application/x-www-form-urlencoded; charset=UTF-8
//			Referer: http://www.dianping.com/shop/93242709/review
//			Accept-Encoding: gzip, deflate
//			Accept-Language: zh-CN,zh;q=0.9
//			Cookie: cy=2; cye=beijing; _lxsdk_cuid=16306145e73c8-0ef427f01c81f5-3a614f0b-1fa400-16306145e73c8; _lxsdk=16306145e73c8-0ef427f01c81f5-3a614f0b-1fa400-16306145e73c8; _hc.v=746a9775-0462-3f31-b15b-75f2b814ca87.1524815388; _dp.ac.v=93084dda-8dc1-4a66-86d7-b5368cdbad99; ctu=841968358589f5e27c81030674d0a758492094a3ba8cca73285cb6d22a0bd779; __utmc=1; __utmz=1.1525680023.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); cityid=1; citypinyin=shanghai; cityname=5LiK5rW3; pvhistory="6L+U5ZuePjo8L3N0YXRpY3Rlc3QvbG9nZXZlbnQ/bmFtZT1XaGVyZUFtSUZhaWwmaW5mbz1odG1sLSU1QiU3QiUyMmNvZGUlMjIlM0ExJTJDJTIybWVzc2FnZSUyMiUzQSUyMk9ubHklMjBzZWN1cmUlMjBvcmlnaW5zJTIwYXJlJTIwYWxsb3dlZCUyMChzZWUlM0ElMjBodHRwcyUzQSUyRiUyRmdvby5nbCUyRlkwWmtOVikuJTIyJTdEJTVEJmNhbGxiYWNrPVdoZXJlQW1JMTE1MjU3NDQxMjIxMjY+OjwxNTI1NzQ0MTIyNTUzXV9b"; m_flash2=1; ctu=1d4fedfc139e2cf9496c5651abf2ec3eebcd0bf5525cde322c83d1f4be2d42cfdc5891f6f2a6adc7f1a3965ed486e411; lgtoken=0f0e44ac1-e745-45cc-b0c1-2d390f190e8c; dper=fe23bf78c2250f5d2dc612c2aae68a1819e66be3ce24fdeb364db0444376773b72dbed5ca1097d38027ddb3bc5a021b8ba520191ae6ba6f79c0fc37e5392bb506397feb87f8d508ef609c0130c95015fbf9e826d3f15a0514688c8302db890c5; ll=7fd06e815b796be3df069dec7836c3df; ua=17080626027; _lx_utm=utm_source%3DBaidu%26utm_medium%3Dorganic; _lxsdk_s=16343920e9e-23e-f03-05%7C%7C42; __utma=1.1348944814.1525680023.1525759191.1525847077.5; __utmb=1.1.10.1525847077

			String cookie = getCookie(task);
//			String cookie = "_hc.v=c6bc7f7f-b51e-6eb1-8600-fdb695de9f05.1525758111; lgtoken=0e43620cc-7c57-4a21-bf92-50e50c467308;_lxsdk_s=1633e452e93-24d-cfc-cfc%7C%7C41; ll=7fd06e815b796be3df069dec7836c3df; _lxsdk_cuid=1633e452e92c8-08562126f7d772-40524336-1fa400-1633e452e92c8; cye=beijing; cy=2; ua=17080626027; ctu=08f7bfb50b3e38becbbb6d2b9e1f83a1dd39d53e313dd234a5979a429ab02fed; dper=fe23bf78c2250f5d2dc612c2aae68a1817747480b8261fe9ca7ca2bbb17141ba329832dd3322acdb56267412e46cff9d908b6434e5cdbaca62eab92798aae6481cfc9d5c34a54006fec0ae1bba6f706cb3833ba2080e3294e69d8baadb239a2b; _lxsdk=1633e452e92c8-08562126f7d772-40524336-1fa400-1633e452e92c8; ";
//			cookie = "cy=2; cye=beijing; _lxsdk_cuid=16306145e73c8-0ef427f01c81f5-3a614f0b-1fa400-16306145e73c8; _lxsdk=16306145e73c8-0ef427f01c81f5-3a614f0b-1fa400-16306145e73c8; _hc.v=746a9775-0462-3f31-b15b-75f2b814ca87.1524815388; _dp.ac.v=93084dda-8dc1-4a66-86d7-b5368cdbad99; ctu=841968358589f5e27c81030674d0a758492094a3ba8cca73285cb6d22a0bd779; __utmc=1; __utmz=1.1525680023.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); cityid=1; citypinyin=shanghai; cityname=5LiK5rW3; pvhistory=\"6L+U5ZuePjo8L3N0YXRpY3Rlc3QvbG9nZXZlbnQ/bmFtZT1XaGVyZUFtSUZhaWwmaW5mbz1odG1sLSU1QiU3QiUyMmNvZGUlMjIlM0ExJTJDJTIybWVzc2FnZSUyMiUzQSUyMk9ubHklMjBzZWN1cmUlMjBvcmlnaW5zJTIwYXJlJTIwYWxsb3dlZCUyMChzZWUlM0ElMjBodHRwcyUzQSUyRiUyRmdvby5nbCUyRlkwWmtOVikuJTIyJTdEJTVEJmNhbGxiYWNrPVdoZXJlQW1JMTE1MjU3NDQxMjIxMjY+OjwxNTI1NzQ0MTIyNTUzXV9b\"; m_flash2=1; ctu=1d4fedfc139e2cf9496c5651abf2ec3eebcd0bf5525cde322c83d1f4be2d42cfdc5891f6f2a6adc7f1a3965ed486e411; lgtoken=0f0e44ac1-e745-45cc-b0c1-2d390f190e8c; dper=fe23bf78c2250f5d2dc612c2aae68a1819e66be3ce24fdeb364db0444376773b72dbed5ca1097d38027ddb3bc5a021b8ba520191ae6ba6f79c0fc37e5392bb506397feb87f8d508ef609c0130c95015fbf9e826d3f15a0514688c8302db890c5; ll=7fd06e815b796be3df069dec7836c3df; ua=17080626027; _lx_utm=utm_source%3DBaidu%26utm_medium%3Dorganic; _lxsdk_s=16343920e9e-23e-f03-05%7C%7C42; __utma=1.1348944814.1525680023.1525759191.1525847077.5; __utmb=1.1.10.1525847077";
			if(StringUtils.isBlank(cookie)){
				
				isSuccess(task, "获取cookie失败");
				return;
			}
			URL url = new URL("http://www.dianping.com/ajax/json/review/reviewAction");
			HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
			
			JSONObject paramMap = getParam(task,cookie);
			
			String shopId = task.getAddress().substring(task.getAddress().lastIndexOf("/")+1);
//			String param = "run=a" +
//					"&mode=pro" +
//					"&info="+URLEncoder.encode("{\"shopId\":"+shopId+",\"shopType\":"+paramMap.getString("channelId")+",\"cityId\":"+paramMap.getString("cityId")+",\"star\":{\"title\":\"总体评价\",\"value\":50,\"desc\":\"超赞\"},\"scoreList\":[{\"title\":\"效果\",\"value\":4,\"desc\":\"超棒\"},{\"title\":\"师资\",\"value\":4,\"desc\":\"超棒\"},{\"title\":\"环境\",\"value\":4,\"desc\":\"超棒\"}],\"reviewBody\":"+task.getCorpus()+",\"reviewPics\":[]}","utf-8")+""+
//					"&reviewId=-1" +
//					"&referPage="+URLEncoder.encode(task.getAddress(),"utf-8");
			System.out.println(shopId);
			System.out.println(paramMap.getString("channelId"));
			System.out.println(paramMap.getString("cityId"));
			String comment = task.getCorpus().replace("\r", "").trim();
//			param = "run=a&mode=pro&info=%7B%22shopId%22%3A93242709%2C%22shopType%22%3A75%2C%22cityId%22%3A1%2C%22star%22%3A%7B%22title%22%3A%22%E6%80%BB%E4%BD%93%E8%AF%84%E4%BB%B7%22%2C%22value%22%3A50%2C%22desc%22%3A%22%E8%B6%85%E8%B5%9E%22%7D%2C%22scoreList%22%3A%5B%7B%22title%22%3A%22%E6%95%88%E6%9E%9C%22%2C%22value%22%3A4%2C%22desc%22%3A%22%E8%B6%85%E6%A3%92%22%7D%2C%7B%22title%22%3A%22%E5%B8%88%E8%B5%84%22%2C%22value%22%3A4%2C%22desc%22%3A%22%E8%B6%85%E6%A3%92%22%7D%2C%7B%22title%22%3A%22%E7%8E%AF%E5%A2%83%22%2C%22value%22%3A4%2C%22desc%22%3A%22%E8%B6%85%E6%A3%92%22%7D%5D%2C%22reviewBody%22%3A%22%E6%95%99%E8%82%B2%E6%88%91%E9%80%89%E6%8B%A9%E6%B5%B7%E9%A3%8E%EF%BC%8C%E4%B8%BA%E4%BD%A0%E6%89%93call%22%2C%22reviewPics%22%3A%5B%5D%7D&reviewId=-1&referPage=http%3A%2F%2Fwww.dianping.com%2Fshop%2F93242709";
			String param = "run=a&mode=pro&info={\"shopId\":"+shopId+",\"shopType\":"+paramMap.getString("channelId")+",\"cityId\":"+paramMap.getString("cityId")+",\"star\":{\"title\":\"总体评价\",\"value\":50,\"desc\":\"超赞\"},\"scoreList\":[{\"title\":\"效果\",\"value\":4,\"desc\":\"超棒\"},{\"title\":\"师资\",\"value\":4,\"desc\":\"超棒\"},{\"title\":\"环境\",\"value\":4,\"desc\":\"超棒\"}],\"reviewBody\":\""+comment+"\",\"reviewPics\":[]}&reviewId=-1&referPage="+task.getAddress();
			openConnection.addRequestProperty("Host", "www.dianping.com");
			openConnection.addRequestProperty("Connection", "keep-alive");
			openConnection.addRequestProperty("Content-Length", String.valueOf(param.length()));
			openConnection.addRequestProperty("Accept", "application/json, */*");
			openConnection.addRequestProperty("Origin", "http://www.dianping.com");
			openConnection.addRequestProperty("X-Requested-With", "XMLHttpRequest");
			openConnection.addRequestProperty("X-Request", "JSON");
			openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");
			openConnection.addRequestProperty("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
			openConnection.addRequestProperty("Referer", task.getAddress()+"/review");
			openConnection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
			openConnection.addRequestProperty("Cookie", cookie);
			
			openConnection.setDoInput(true);
			openConnection.setDoOutput(true);
			openConnection.setUseCaches(false);
			openConnection.setRequestMethod("POST");
			
			PrintWriter pw = new PrintWriter(openConnection.getOutputStream());
//			pw.print(URLEncoder.encode(param,"utf-8"));
			pw.print(param);
			pw.flush();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(openConnection.getInputStream(),"utf-8"));
			
			StringBuffer sb = new StringBuffer();
			String line = null;
			while((line = br.readLine())!=null){
				
				sb.append(line);
			}
			
			System.out.println(sb.toString());
			
			JSONObject parseObject = JSON.parseObject(sb.toString());
			String code = parseObject.getString("code");
			
			if(StringUtils.isNotBlank(code) && "200".equals(code)){
				
				isSuccess(task, "");
				return;
			}else{
				
				isSuccess(task, "失败");
				return;
			}
			
		} catch (Exception e) {
			
			isSuccess(task, "点评异常");
			e.printStackTrace();
		}
	}

	/**
	 * 获取参数
	 * @param task
	 * @return
	 */
	private static JSONObject getParam(TaskGuideBean task,String cookie) throws Exception{
		
		URL url = new URL(task.getAddress());
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();

		openConnection.addRequestProperty("Host", "www.dianping.com");
		openConnection.addRequestProperty("Connection", "keep-alive");
		openConnection.addRequestProperty("Cache-Control", "max-age=0");
		openConnection.addRequestProperty("Upgrade-Insecure-Requests", "1");
		openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");
		openConnection.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		openConnection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
		openConnection.addRequestProperty("Cookie", cookie);
		
		openConnection.connect();
		
		Scanner sc = new Scanner(openConnection.getInputStream(),"utf-8");
		
		String result = "";
		while(sc.hasNext()){
			
			result += sc.nextLine() + "\n";
		}
		
		result = result.substring(result.indexOf("_DP_HeaderData"));
		result = result.substring(result.indexOf("=")+1,result.indexOf("}")+1).trim();
		
		JSONObject parseObject = JSON.parseObject(result);
		return parseObject;
	}
	/**
	 * 获取cookie
	 * @param task
	 * @return
	 */
	public static String getCookie(TaskGuideBean task) {
		
		WebDriver driver = getDriver();
		
		if(driver == null){
			
			return null;
		}
		try {
//			driver.get("https://www.dianping.com/login?redir=http%3A%2F%2Fwww.dianping.com%2F");
			driver.get("http://www.dianping.com/");
			driver.findElement(By.xpath("//*[@id='userinfo']/div/div[4]/div[1]/a[1]")).click();
			Thread.sleep(2000);
			driver.switchTo().frame(0);

			Thread.sleep(2000);
	
//			
//			driver.findElement(By.cssSelector(".bottom-password-login")).click();
			driver.findElement(By.xpath("/html/body/div/div[2]/div[5]/span")).click();
			Thread.sleep(1000);
			driver.findElement(By.id("tab-account")).click();
			driver.findElement(By.id("account-textbox")).sendKeys(task.getNick());
			
			driver.findElement(By.id("password-textbox")).sendKeys(task.getPassword());
			
			driver.findElement(By.id("login-button-account")).click();
			
			Thread.sleep(2000);
			String alertText = "";
			for(int i=0;i<=4;i++){
				
				try {
					WebElement mobileCaptcha = driver.findElement(By.xpath("//*[@id='captcha-account-container']/div[2]/img"));
					Thread.sleep(10000);
					String picCode = getPicCode(driver, mobileCaptcha);
					
					driver.findElement(By.id("captcha-textbox-account")).clear();
					driver.findElement(By.id("captcha-textbox-account")).sendKeys(picCode);
					driver.findElement(By.id("login-button-account")).click();
					Thread.sleep(3000);
					alertText = driver.findElement(By.xpath("//*[@id='alert']/span")).getText();

					if((StringUtils.isNotBlank(alertText) && alertText.contains("验证码错误")) || (StringUtils.isNotBlank(alertText) && alertText.contains("请输入图形验证码"))){
						
						driver.findElement(By.xpath("//*[@id='captcha-account-container']/div[2]/img")).click();
						Thread.sleep(1000);
						continue;
					}else{
							
						break;
					}
				} catch (Exception e) {
				}
			}
			try {
				String alertt = driver.findElement(By.xpath("//*[@id='alert']/span")).getText();
				
				if(StringUtils.isNotBlank(alertt) && !alertt.contains("成功")){
					
					isSuccess(task, alertt);
					return null;
				}
			} catch (Exception e2) {
			}
			try {
				driver.findElement(By.id("login-button-account")).click();
				Thread.sleep(3000);
			} catch (Exception e) {
			}
			
//			driver.get(task.getAddress());
			
//			Thread.sleep(3000);
//			driver.get(task.getAddress()+"/review");
			Thread.sleep(5000);
			
			try {
				driver.findElement(By.id("confirmOk")).click();
			} catch (Exception e1) {
			}
			if(driver.getCurrentUrl().contains("verify.meituan.com")){
				
			try {
				
					for(int j=0;j<4;j++){
						
						WebElement imgcode = driver.findElement(By.id("yodaImgCode"));
						
						String piccode = getPicCode2(driver,imgcode);
						driver.findElement(By.id("yodaImgCodeInput")).sendKeys(piccode);
						
						driver.findElement(By.id("yodaImgCodeSure")).click();
						
						Thread.sleep(4000);
						
						if(driver.getCurrentUrl().contains("verify.meituan.com")){
							
							continue;
						}else{
							
							break;
						}
					}
			} catch (Exception e) {
			}
			}
			
			if(driver.getCurrentUrl().contains("verify.meituan.com")){
				
				return null;
			}
			Set<Cookie> cookies = driver.manage().getCookies();
			
			String skeyCookie = "";
			for (Cookie cookie : cookies) {
				
				skeyCookie += cookie.toString().substring(0,cookie.toString().indexOf(";")) + "; ";
			}
			
			return skeyCookie;
//			Thread.sleep(1000);
//			driver.findElement(By.cssSelector("#J_shop-rating > div > ul > li:nth-child(5) > a")).click();
//			Thread.sleep(1000);
//			driver.findElement(By.cssSelector("#J_review-s1 > div > ul > li:nth-child(5) > a")).click();
//			Thread.sleep(1000);
//			driver.findElement(By.cssSelector("#J_review-s2 > div > ul > li:nth-child(5) > a")).click();
//			Thread.sleep(1000);
//			driver.findElement(By.cssSelector("#J_review-s3 > div > ul > li:nth-child(5) > a")).click();
//			Thread.sleep(1000);
//			
//			driver.findElement(By.id("J_review-body")).clear();
//			driver.findElement(By.id("J_review-body")).sendKeys(task.getCorpus());
//			
//			driver.findElement(By.id("J_review-submit")).click();
//			
//			Thread.sleep(3000);
//			
//			if(driver.getCurrentUrl().contains("http://www.dianping.com/addreview/success")){
//				
//				isSuccess(task, "");
//				return;
//			}else{
//				
//				return;
//			}
		} catch (Exception e) {
			
			isSuccess(task, "点评错误");
			e.printStackTrace();
		}finally{
			
			driver.quit();
		}
		return null;
	}
	/**
	 * 完全driver方式
	 * @param task
	 * @return
	 */
	public static void toComment(TaskGuideBean task) {
		
		WebDriver driver = getDriver();
		
		if(driver == null){
			
			return ;
		}
		try {
//			driver.get("https://www.dianping.com/login?redir=http%3A%2F%2Fwww.dianping.com%2F");
			driver.get("http://www.dianping.com/");
			driver.findElement(By.xpath("//*[@id='userinfo']/div/div[4]/div[1]/a[1]")).click();
			Thread.sleep(2000);
			driver.switchTo().frame(0);

			Thread.sleep(2000);
	
//			
//			driver.findElement(By.cssSelector(".bottom-password-login")).click();
			driver.findElement(By.xpath("/html/body/div/div[2]/div[5]/span")).click();
			Thread.sleep(1000);
			driver.findElement(By.id("tab-account")).click();
			driver.findElement(By.id("account-textbox")).sendKeys(task.getNick());
			
			driver.findElement(By.id("password-textbox")).sendKeys(task.getPassword());
			
			driver.findElement(By.id("login-button-account")).click();
			
			Thread.sleep(2000);
			String alertText = "";
			for(int i=0;i<=4;i++){
				
				try {
					WebElement mobileCaptcha = driver.findElement(By.xpath("//*[@id='captcha-account-container']/div[2]/img"));
					Thread.sleep(10000);
					String picCode = getPicCode(driver, mobileCaptcha);
					
					driver.findElement(By.id("captcha-textbox-account")).clear();
					driver.findElement(By.id("captcha-textbox-account")).sendKeys(picCode);
					driver.findElement(By.id("login-button-account")).click();
					Thread.sleep(3000);
					alertText = driver.findElement(By.xpath("//*[@id='alert']/span")).getText();

					if((StringUtils.isNotBlank(alertText) && alertText.contains("验证码错误")) || (StringUtils.isNotBlank(alertText) && alertText.contains("请输入图形验证码"))){
						
						driver.findElement(By.xpath("//*[@id='captcha-account-container']/div[2]/img")).click();
						Thread.sleep(1000);
						continue;
					}else{
							
						break;
					}
				} catch (Exception e) {
				}
			}
			try {
				String alertt = driver.findElement(By.xpath("//*[@id='alert']/span")).getText();
				
				if(StringUtils.isNotBlank(alertt) && !alertt.contains("成功")){
					
					isSuccess(task, alertt);
					return;
				}
			} catch (Exception e2) {
			}
			try {
				driver.findElement(By.id("login-button-account")).click();
				Thread.sleep(3000);
			} catch (Exception e) {
			}
			
//			driver.get(task.getAddress());
			
//			Thread.sleep(3000);
			driver.get(task.getAddress()+"/review");
			Thread.sleep(2000);
			
			try {
				driver.findElement(By.id("confirmOk")).click();
			} catch (Exception e1) {
			}
			if(driver.getCurrentUrl().contains("verify.meituan.com")){
				
			try {
				
					for(int j=0;j<4;j++){
						
						WebElement imgcode = driver.findElement(By.id("yodaImgCode"));
						
						String piccode = getPicCode2(driver,imgcode);
						driver.findElement(By.id("yodaImgCodeInput")).sendKeys(piccode);
						
						driver.findElement(By.id("yodaImgCodeSure")).click();
						
						Thread.sleep(4000);
						
						if(driver.getCurrentUrl().contains("verify.meituan.com")){
							
							continue;
						}else{
							
							break;
						}
					}
			} catch (Exception e) {
			}
			}
			
			if(driver.getCurrentUrl().contains("verify.meituan.com")){
				
				return ;
			}
//			Set<Cookie> cookies = driver.manage().getCookies();
//			
//			String skeyCookie = "";
//			for (Cookie cookie : cookies) {
//				
//				skeyCookie += cookie.toString().substring(0,cookie.toString().indexOf(";")) + "; ";
//			}
			Thread.sleep(1000);
			driver.findElement(By.cssSelector("#J_shop-rating > div > ul > li:nth-child(5) > a")).click();
			Thread.sleep(1000);
			driver.findElement(By.cssSelector("#J_review-s1 > div > ul > li:nth-child(5) > a")).click();
			Thread.sleep(1000);
			driver.findElement(By.cssSelector("#J_review-s2 > div > ul > li:nth-child(5) > a")).click();
			Thread.sleep(1000);
			driver.findElement(By.cssSelector("#J_review-s3 > div > ul > li:nth-child(5) > a")).click();
			Thread.sleep(1000);
			
			driver.findElement(By.id("J_review-body")).clear();
			driver.findElement(By.id("J_review-body")).sendKeys(task.getCorpus());
			
			driver.findElement(By.id("J_review-submit")).click();
			
			Thread.sleep(3000);
			
//			if(driver.getCurrentUrl().contains("http://www.dianping.com/addreview/success")){
				
				isSuccess(task, "");
				return;
//			}else{
//				
//				isSuccess(task, "失败");
//				return;
//			}
		} catch (Exception e) {
			
			isSuccess(task, "点评错误");
			e.printStackTrace();
		}finally{
			
			driver.quit();
		}
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
	private static String getPicCode2(WebDriver driver,WebElement comment){
		
		String code = "";
		try {
			File screenshotAs = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			
			BufferedImage bufferedImage = ImageIO.read(screenshotAs);
			
			Point point = comment.getLocation();
			
			int width = comment.getSize().getWidth();
			int height = comment.getSize().getHeight();
			BufferedImage subimage = bufferedImage.getSubimage(point.getX(), point.getY(), width, height);
			ImageIO.write(subimage, "png", screenshotAs);
			
			File file = new File("c:\\dianping.png");
			FileUtils.copyFile(screenshotAs, file);
			
			code = RuoKuai.createByPostNew("3040", "c:\\dianping.png");
		} catch (WebDriverException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return code;
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
			
			File file = new File("c:\\dianping.png");
			FileUtils.copyFile(screenshotAs, file);
			
			code = RuoKuai.createByPostNew("3040", "c:\\dianping.png");
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
