package com.longriver.netpro.webview.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;


import com.alibaba.fastjson.JSON;
import com.longriver.netpro.common.sina.Base64Utils;
import com.longriver.netpro.common.sina.SData;
import com.longriver.netpro.common.sina.SinaIdMidConverter;
import com.longriver.netpro.common.sina.WeiboSina;
import com.longriver.netpro.util.Configur;
import com.longriver.netpro.util.DriverGet;
import com.longriver.netpro.util.GetProprities;
import com.longriver.netpro.util.IPUtil;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

public class WeiboReader {
	private static Logger logger = Logger.getLogger(WeiboReader.class);
	
	public static void main(String args[]){
//		String userId = "15250221843";
//		String pwd = "lx1314";
//		String addr = "http://weibo.com/2549964007/E03Agg8Ob?type=comment#_rnd1469417038214";
//		String contents = "赞一个";
//		TaskGuideBean s = new TaskGuideBean();
//		s.setAddress("https://weibo.com/6019107527/Gb7jphrg3?from=page_1005056019107527_profile&wvr=6&mod=weibotime&type=comment");
//		s.setAddress("https://weibo.com/2662279417/Gb6U0yicZ?from=page_1005052662279417_profile&wvr=6&mod=weibotime&type=repost#_rnd1523168578604");
//		try {
//			sinaReader("1");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
			TaskGuideBean b = new TaskGuideBean();
			b.setNick("13226172899"); //异常账号
			b.setPassword("lx1314");//		b.setAddress("http://weibo.com/2418724427/EnryFk1Yw?from=page_1001062418724427_profile&wvr=6&mod=weibotime&type=comment#_rnd1482479610672");
			b.setAddress("https://weibo.com/2662279417/GbpkYhR7S?from=page_1005052662279417_profile&wvr=6&mod=weibotime&type=comment");
			sinaReaderLogin(b);
	}
	public static void sinaReader(String uid){
		TaskGuideBean s = new TaskGuideBean();
		if(uid.contains("1")) s.setAddress("https://weibo.com/2662279417/GbpkYhR7S?from=page_1005052662279417_profile&wvr=6&mod=weibotime");// 冬奥会的排头兵
		else if(uid.contains("2")) s.setAddress("https://weibo.com/6019151807/Gbr7K78et");//本周全球金融市场
		else if(uid.contains("3")) s.setAddress("https://weibo.com/6020385696/Gbsn0DjAX");//起跑线》的哪个情
		else if(uid.contains("4")) s.setAddress("https://weibo.com/6495677950/Gbpmlnj53");//2019年春期待什么
		else s.setAddress("https://weibo.com/6476940202/GbpuOvJ9H");//古人言“腰缠十万贯,骑鹤下
		if(!uid.contains("w")){
			for(int i=0;i<300;i++){
				try {
					sina(s);
					System.out.println("已运行:"+(i+1));
					Thread.sleep(1000*3);
					if(i%10==8) IPUtil.qieIP();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else{
			System.out.println("webdriver..");
			for(int i=0;i<4;i++){
				try {
					sinaWeb(s);
					System.out.println("已运行:"+(i+1));
					Thread.sleep(1000*3);
					IPUtil.qieIP();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	public static void sinaWeb(TaskGuideBean taskdo) throws Exception{
		WebDriver driver = null;
		try{
			Configur p = GetProprities.paramsConfig;
			String firefoxUrl = p.getProperty("firefoxurl");
			//Firefox
			FirefoxProfile profile = new FirefoxProfile();
			//##禁用Flash 
			profile.setPreference("dom.ipc.plugins.enabled.libflashplayer.so","false");
			System.setProperty("webdriver.firefox.bin",firefoxUrl);
			driver = new FirefoxDriver(profile);
			for(int i=0;i<50;i++){
				driver.get(taskdo.getAddress());  
				Thread.sleep(1000*5);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			DriverGet.quit(driver);
		}
	}
	public static void sina(TaskGuideBean taskdo) throws Exception{
	
		//http://weibo.com/aj/v6/mblog/forward?ajwvr=6&domain=5234172433&__rnd=1417000316699
		URL u5 = new URL(taskdo.getAddress());
		HttpURLConnection c5 = (HttpURLConnection) u5.openConnection();

		c5.addRequestProperty("Host", "weibo.com");
//		c5.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
		c5.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		c5.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		c5.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		c5.addRequestProperty("X-Requested-With", "XMLHttpRequest");
		c5.addRequestProperty("Referer", taskdo.getAddress());
		c5.addRequestProperty("Cookie", getCookie());
		c5.addRequestProperty("Connection", "keep-alive");
		c5.addRequestProperty("Pragma", "no-cache");
		c5.addRequestProperty("Cache-Control", "no-cache");
		// 必须设置false，否则会自动redirect到Location的地址  
		c5.setInstanceFollowRedirects(false); 
		c5.setConnectTimeout(1000 * 30);
		c5.setReadTimeout(1000 * 20);
		c5.setDoInput(true);
		c5.setDoOutput(true);
		InputStream i5 = null;
		try{
			i5 = c5.getInputStream();
		}catch(Exception e){
			e.printStackTrace();
		}
		Scanner s5 = new Scanner(i5);
		while(s5.hasNext()){
			String scsc = StringUtil.decodeUnicode(s5.nextLine());
			System.out.println("scsc================"+scsc);
		}
	}

	
	public static String getCookie(){
		String cookie = "";
		try {
			URL uf2 = new URL("https://passport.weibo.com/visitor/visitor?a=incarnate&t=JVgfdUVWHpsfTtQUR3lG%2Br58v38cJiRC1oZDPysvp00%3D&w=2&c=095&gc=&cb=cross_domain&from=weibo&_rand=0.502839476801455");
			//_FakeX509TrustManager.allowAllSSL();
			HttpURLConnection cf2 = (HttpURLConnection) uf2.openConnection();
			cf2.addRequestProperty("Host", "passport.weibo.com");
			cf2.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36");
			cf2.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			cf2.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
			cf2.setConnectTimeout(1000*30);
		    cf2.setReadTimeout(1000*30);
			cf2.connect();
			Map<String, List<String>> headers2 = cf2.getHeaderFields(); 
			for(Map.Entry<String,List<String>> entry : headers2.entrySet()){
			    if(entry.getKey() != null && (entry.getKey().indexOf("Set-Cookie") > -1)){
			        for(String value : entry.getValue()){
			        	if(value.contains("SUB="))
			        		cookie += cookie + value.substring(0, value.indexOf(";") + 1);
			        }  
			    }
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		System.out.println(cookie);
		return cookie;
	}
	public static void sinaReaderLogin(TaskGuideBean taskdo){
		try {
			SData data =new SData();
			
			String userId =taskdo.getNick();
			String pwd = taskdo.getPassword();
			data.put("user_account_id", userId);
			data.put("user_account_pw", pwd);
			String cookie = WeiboSina.getCookie(data,0);
			if(cookie.length()>10)
			for(int i=0;i<5;i++){
				URL u5 = new URL(taskdo.getAddress());
				HttpURLConnection c5 = (HttpURLConnection) u5.openConnection();
				c5.addRequestProperty("Host", "weibo.com");
				c5.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
				c5.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
				c5.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
				c5.addRequestProperty("X-Requested-With", "XMLHttpRequest");
				c5.addRequestProperty("Cookie", cookie);
				c5.addRequestProperty("Connection", "keep-alive");
				c5.addRequestProperty("Pragma", "no-cache");
				c5.addRequestProperty("Cache-Control", "no-cache");
				c5.setConnectTimeout(1000 * 30);
				c5.setReadTimeout(1000 * 20);
				c5.connect();
				System.out.println("yunxing:"+i);
				Thread.sleep(1000*2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String formatString(String str) {
        if (str != null) {
        	try {                
        		str = new String(str.getBytes("ISO-8859-1"), "UTF-8");            
        		} catch (Exception e) {}        
        		}
        return str;    
    }
	
}
