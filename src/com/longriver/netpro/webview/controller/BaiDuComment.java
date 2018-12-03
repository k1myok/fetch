package com.longriver.netpro.webview.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringEscapeUtils;

import com.alibaba.fastjson.JSON;
import com.longriver.netpro.common.sohu.BaiduScript;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

public class BaiDuComment {
	
	public static void main(String arsg[]){
		TaskGuideBean taskdo = new TaskGuideBean();
//		taskdo.setNick("lilei1929@163.com");
//    	taskdo.setPassword("lilei419688");
    	taskdo.setNick("18610647727");
    	taskdo.setPassword("wangmeng121");
    	taskdo.setCorpus("没看过啊!!");
    	taskdo.setAddress("http://tieba.baidu.com/p/4563096316");
//    	taskdo.setAddress("http://tieba.baidu.com/p/4817991706");
    	baidu(taskdo);
	}
	public static void baidu(TaskGuideBean taskdo){
		String userId =taskdo.getNick();
    	String pwd = taskdo.getPassword();
    	String content = taskdo.getCorpus();
    	String sUrl = "";
    	
//		String userId = "lilei1929@163.com";
//		String pwd = "lilei419688";
		String cookie="";
		
		try {
			sUrl = taskdo.getAddress();
			String strBaiduUrl = "http://www.baidu.com/cache/user/html/login-1.2.html";
			
			URL u1 = new URL(strBaiduUrl);
			HttpURLConnection c1 = (HttpURLConnection) u1.openConnection();
			c1.connect();
			Map<String, List<String>> m1 = c1.getHeaderFields();
			for(Map.Entry<String,List<String>> entry : m1.entrySet()){
				if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
					for(String value : entry.getValue()){
						cookie = cookie + value.substring(0, value.indexOf(";")) + ";";
					}
				}
			}
			String token = getToken(cookie);
			String codeString = checkLogin( token, userId, cookie);
			String verCode = "";
			String no ="0";
			if(codeString!=null&&!codeString.equals("")){
				verCode = getVerycode( codeString, cookie);
				 no = getYanZheng( codeString, token, verCode, cookie );
				
			}
			if(no.equals("0")){
				Map<String,String> a = getPublicKey(cookie,token);
				System.out.println(a.get("pubkey"));
				System.out.println(a.get("rsakey"));
				String erro = getLogin(userId, pwd, token, cookie, a.get("pubkey"), a.get("rsakey"),verCode,codeString);
				if(erro.indexOf("false;")>-1){
					codeString = erro.replace("false;", "");
					verCode = getVerycode( codeString, cookie);	
					 no = getYanZheng( codeString, token, verCode, cookie );
					 if(no.equals("0")){
							cookie = getLogin(userId, pwd, token, cookie, a.get("pubkey"), a.get("rsakey"),verCode,codeString);
					 }
				}else{
					cookie = erro;
				}
				System.out.println("cookie-============="+cookie);
				//addTieZi( cookie, content,title);
				genTieZi(sUrl, cookie, content,taskdo);
				
			}else{
				MQSender.toMQ(taskdo,"登录失败,验证码可能错误");
			}
		} catch (Exception e) {
			MQSender.toMQ(taskdo,"失败");
			e.printStackTrace();
		}
		
	}
	public static String getToken(String cookie){
		String token = "";
		String strBaiduUrl = "https://passport.baidu.com/v2/api/?getapi&tpl=mn&apiver=v3&tt="+new Date().getTime()+"&class=login&logintype=basicLogin&callback=bd__cbs__1z1bwi&gid=7DDFA2B-0496-4783-97C3-FA84625ADC0C";
	    URL u1;
		try {
			u1 = new URL(strBaiduUrl);
			HttpURLConnection fc = (HttpURLConnection) u1.openConnection();
			fc.addRequestProperty("Host", "passport.baidu.com");
			fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
			fc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			fc.addRequestProperty("Referer", "https://www.baidu.com/");
			fc.addRequestProperty("Cookie", cookie);
			fc.addRequestProperty("Connection", "keep-alive");
			fc.connect();
			InputStream fi = fc.getInputStream();
			Scanner fs = new Scanner(fi);
			while(fs.hasNext()){
				String scsc2 = fs.nextLine();
				if(scsc2.indexOf("token\" :") > -1){
					token = scsc2.substring(scsc2.indexOf("token\" :")).replace("token\" :", "");
					token = token.substring(0, token.indexOf(",")).replace("\"", "").trim();
				}
				System.out.println(scsc2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return token;
	}
	
	public static String checkLogin(String token,String userId,String cookie){
		String codeString ="";
		String strBaiduUrl="";
		try {
			strBaiduUrl = "https://passport.baidu.com/v2/api/?logincheck&token="+token+"&tpl=mn"
					+ "&apiver=v3&tt="+new Date().getTime()+"&sub_source=leadsetpwd&username="+URLEncoder.encode(userId, "utf-8")+"&isphone=false&callback=bd__cbs__lyvi3s";
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		 URL u1;
			try {
				u1 = new URL(strBaiduUrl);
				HttpURLConnection fc = (HttpURLConnection) u1.openConnection();
				fc.addRequestProperty("Host", "passport.baidu.com");
				fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
				fc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
				fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
				fc.addRequestProperty("Referer", "https://www.baidu.com/");
				fc.addRequestProperty("Cookie", cookie);
				fc.addRequestProperty("Connection", "keep-alive");
				fc.connect();
				InputStream fi = fc.getInputStream();
				Scanner fs = new Scanner(fi);
				while(fs.hasNext()){
					String scsc2 = fs.nextLine();
					if(scsc2.indexOf("codeString\" : ") > -1){
						codeString = scsc2.substring(scsc2.indexOf("codeString\" : ")).replace("codeString\" : ", "");
						codeString = codeString.substring(0, codeString.indexOf(",")).replace("\"", "").trim();
						
					}
					System.out.println(scsc2);
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return codeString;
	}
	public static String getYanZheng(String codeString,String token,String verifycode,String cookie ){
	String strBaiduUrl = "https://passport.baidu.com/v2/?checkvcode&token="+token+"&tpl=mn&apiver=v3&tt="+new Date().getTime()+"&verifycode="+verifycode+"&codestring="+codeString+"&callback=bd__cbs__owb2bw";
	String no = ""; 
	URL u1;
		try {
			u1 = new URL(strBaiduUrl);
			HttpURLConnection fc = (HttpURLConnection) u1.openConnection();
			fc.addRequestProperty("Host", "passport.baidu.com");
			fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
			fc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			fc.addRequestProperty("Referer", "https://www.baidu.com/");
			fc.addRequestProperty("Cookie", cookie);
			fc.addRequestProperty("Connection", "keep-alive");
			fc.connect();
			InputStream fi = fc.getInputStream();
			Scanner fs = new Scanner(fi);
			while(fs.hasNext()){
				String scsc2 = fs.nextLine();
				if(scsc2.indexOf("no\":") > -1){
					no = scsc2.substring(scsc2.indexOf("no\":")).replace("no\":", "");
					no = no.substring(0, no.indexOf(",")).replace("\"", "").replace("\"", "").trim();
					
				}
				System.out.println(scsc2);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return no;
	}
	
	public static String testWx(){
		String randVery = "";
		String strBaiduUrl = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=LArnk71H4Z-1XEt7AZGVK1LgRlqmQD1v7fRFp0Uc7r1kdOgHhJ9aX8viwtHxFeywzGOSwmbBEklZJpoonm8uWHsrfCGaIYDV_xhsC1IKUmZUnfumW9Ur4-Og4FJhYpcVAPUeAAAJYD&media_id=xIlZRSHBFYia_NeoC1LE0nS2fXh6rgvjP-ps1_kwzvJeqp2uCR09s5sS8LY709YS";
		 URL u1;
			try {
				u1 = new URL(strBaiduUrl);
				HttpURLConnection fc = (HttpURLConnection) u1.openConnection();
				fc.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
				fc.setDoOutput(true);
				fc.setDoInput(true);
		         System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
		         System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
				fc.connect();
				Map<String, List<String>> m1 = fc.getHeaderFields();
				for(Map.Entry<String,List<String>> entry : m1.entrySet()){
					if(entry.getKey() != null ){
						System.out.println(entry.getKey());
						for(String value : entry.getValue()){
							System.out.println(value);
						}
					}
				}

				System.out.println(fc.getHeaderField("Content-disposition"));
				InputStream i2 = fc.getInputStream();
				
		        byte[] bs = new byte[1024];  
		        int len;  

		       File sf=new File("c:\\");  
		       if(!sf.exists()){  
		           sf.mkdirs();  
		       }  
		       OutputStream os = new FileOutputStream("c:\\baidu.jpg");  

		        while ((len = i2.read(bs)) != -1) {  
		          os.write(bs, 0, len);  
		        }  
		        os.close();  
		        i2.close();  
			} catch (Exception e) {

				e.printStackTrace();
			}
			return randVery;
	}
	
	public static String getVerycode(String codeString,String cookie){
		String randVery = "";
		String strBaiduUrl = "https://passport.baidu.com/cgi-bin/genimage?"+codeString;
		 URL u1;
			try {
				u1 = new URL(strBaiduUrl);
				HttpURLConnection fc = (HttpURLConnection) u1.openConnection();
				fc.addRequestProperty("Host", "passport.baidu.com");
				fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
				fc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
				fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
				fc.addRequestProperty("Referer", "https://www.baidu.com/");
				fc.addRequestProperty("Cookie", cookie);
				fc.addRequestProperty("Connection", "keep-alive");
				fc.connect();
				
				InputStream i2 = fc.getInputStream();
				
		        byte[] bs = new byte[1024];  
		        int len;  
		       File sf=new File("c:\\");  
		       if(!sf.exists()){  
		           sf.mkdirs();  
		       }  
		       OutputStream os = new FileOutputStream("c:\\311111.jpg");  
		        while ((len = i2.read(bs)) != -1) {  
		          os.write(bs, 0, len);  
		        }  
		        os.close();  
		        i2.close();  

				randVery = RuoKuai.createByPostNew("4020","c:\\311111.jpg");
				System.out.println("result========================"+randVery);
			} catch (Exception e) {

				e.printStackTrace();
			}
			return randVery;
	}
	public static Map<String,String> getPublicKey(String cookie,String token){
		String strBaiduUrl = "https://passport.baidu.com/v2/getpublickey?token="+token+"&tpl=wise&apiver=v3&tt="+new Date().getTime()+"&callback=bd__cbs__1z1bwi&gid=7DDFA2B-0496-4783-97C3-FA84625ADC0C";
		String pubkey = "";
		String rsakey = "";
		Map<String,String> a = new HashMap<String,String>();
	    URL u1;
		try {
			u1 = new URL(strBaiduUrl);
			HttpURLConnection fc = (HttpURLConnection) u1.openConnection();
			fc.addRequestProperty("Host", "passport.baidu.com");
			fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
			fc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			fc.addRequestProperty("Referer", "https://www.baidu.com/");
			fc.addRequestProperty("Cookie", cookie);
			fc.addRequestProperty("Connection", "keep-alive");
			fc.connect();
			InputStream fi = fc.getInputStream();
			Scanner fs = new Scanner(fi);
			while(fs.hasNext()){
				String scsc2 = fs.nextLine();
				if(scsc2.indexOf("pubkey\":") > -1){
					pubkey = scsc2.substring(scsc2.indexOf("pubkey\":")).replace("pubkey\":", "");
					pubkey = pubkey.substring(0, pubkey.indexOf(",")).replace("\"", "").trim();
					pubkey = pubkey.replace("-----BEGIN PUBLIC KEY-----", "");
					pubkey = pubkey.replace("-----END PUBLIC KEY-----", "").replace("'", "");
				
					pubkey = StringEscapeUtils.unescapeJava(pubkey);
					a.put("pubkey", pubkey);
				}
				if(scsc2.indexOf("\"key\":") > -1){
					rsakey = scsc2.substring(scsc2.indexOf("\"key\":")).replace("\"key\":", "");
					rsakey = rsakey.substring(0, rsakey.indexOf("}")).replace("\'", "").trim();
					
					
					a.put("rsakey", rsakey);
				}
				
				System.out.println(scsc2);
				
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return a;
	}
	
	
	public static String getLogin(String username,String pwd,String token,String cookie,String pubkey,String rsakey,String veryCode,String codeString){
		String strBaiduUrl = "https://passport.baidu.com/v2/api/?login";
		String parma = "";
		String vcodetype="";
		try {
			parma = "staticpage=http%3A%2F%2Fapp.baidu.com%2Fsfile%2Fv3Jump.html&charset=UTF-8"
			+"&token="+token+"&tpl=mn&subpro=&apiver=v3&tt="+new Date().getTime()
			+"&codestring="+codeString+"&safeflg=0&u=%0D%0A%0D%0Ahttp%3A%2F%2Fapp.baidu.com%2Findex%3Fregdev%3D1&isPhone=false"
			+ "&detect=1&quick_user=0&logintype=basicLogin&logLoginType=pc_loginDialog&idc=&loginmerge=true"
			+ "&username="+URLEncoder.encode(username, "utf-8")+"&password="+BaiduScript.getUid(pubkey, pwd)+"&verifycode="+veryCode+"&countrycode="
			+ "&rsakey="+rsakey+"&crypttype=12&ppui_logintime=426406"
			+ "&callback=parent.bd__pcbs__mwrr8d&gid=7DDFA2B-0496-4783-97C3-FA84625ADC0C";
		} catch (Exception e1) {

			e1.printStackTrace();
		}
	    URL u1;
		try {
			u1 = new URL(strBaiduUrl);
			HttpURLConnection fc = (HttpURLConnection) u1.openConnection();
			fc.addRequestProperty("Host", "passport.baidu.com");
			fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
			fc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			fc.addRequestProperty("Referer", "https://www.baidu.com/");
			fc.addRequestProperty("Cookie", cookie);
			fc.addRequestProperty("Content-Length", String.valueOf(parma.length()));
			fc.addRequestProperty("Connection", "keep-alive");
			fc.setInstanceFollowRedirects(false);
			fc.setDoInput(true);
			fc.setDoOutput(true);
			PrintWriter fo = new PrintWriter(fc.getOutputStream());
			fo.print(parma);
			fo.flush();
			InputStream fi = fc.getInputStream();
			Scanner fs = new Scanner(fi);
			String err_no="";
			while(fs.hasNext()){
				String scsc2 = fs.nextLine();
				if(scsc2.indexOf("err_no=") > -1){
					err_no = scsc2.substring(scsc2.indexOf("err_no=")).replace("err_no=", "");
					err_no = err_no.substring(0, err_no.indexOf("&")).trim();
				}
				if(scsc2.indexOf("codeString=") > -1){
					codeString = scsc2.substring(scsc2.indexOf("codeString=")).replace("codeString=", "");
					codeString = codeString.substring(0, codeString.indexOf("&")).trim();
				}
				if(scsc2.indexOf("vcodetype=") > -1){
					vcodetype = scsc2.substring(scsc2.indexOf("vcodetype=")).replace("vcodetype=", "");
					vcodetype = vcodetype.substring(0, vcodetype.indexOf("&")).trim();
				}
				
				System.out.println(scsc2);
				
			}
			if(err_no.equals("257")){
				cookie = "false;"+codeString;
			}else{
				Map<String, List<String>> m1 = fc.getHeaderFields();
				for(Map.Entry<String,List<String>> entry : m1.entrySet()){
					if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
						for(String value : entry.getValue()){
							System.out.println(value);
							cookie = cookie + value + ";";
						}
					}
				}
			}
			
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		
		return cookie;
	}
	public static void addTieZi(String cookie,String content,String title){
		String tbs = getTbs(cookie);
		String strBaiduUrl = "http://tieba.baidu.com/f/commit/thread/add";
		String parma = "";
		try {
			parma = "ie=utf-8"
					+ "&kw=%E5%B9%BF%E5%9C%BA%E8%88%9E"
					+ "&fid=2254269&tid=0&vcode_md5="
					+ "&floor_num=0&rich_text=1"
					+ "&tbs="+tbs
					+ "&content="+URLEncoder.encode(content, "utf-8")
					+ "&title="+URLEncoder.encode(title, "utf-8")
					+ "&prefix=&files=%5B%5D"
					+ "&mouse_pwd=116%2C113%2C116%2C106%2C113%2C112%2C118%2C115%2C79%2C119%2C106%2C118%2C106%2C119%2C106%2C118%2C106%2C119%2C106%2C118%2C106%2C119%2C106%2C118%2C106%2C119%2C106%2C118%2C79%2C113%2C115%2C127%2C112%2C113%2C79%2C119%2C117%2C112%2C112%2C106%2C113%2C112%2C126%2C14618287723700"
					+ "&mouse_pwd_t=1461828772370"
					+ "&mouse_pwd_isclick=0&__type__=thread";
			
		} catch (Exception e1) {

			e1.printStackTrace();
		}
	    URL u1;
		try {
			u1 = new URL(strBaiduUrl);
			HttpURLConnection fc = (HttpURLConnection) u1.openConnection();
			fc.addRequestProperty("Host", "tieba.baidu.com");
			fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
			fc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			fc.addRequestProperty("Referer", "https://www.baidu.com/");
			fc.addRequestProperty("Cookie", cookie);
			fc.addRequestProperty("Content-Length", String.valueOf(parma.length()));
			fc.addRequestProperty("Connection", "keep-alive");
			fc.setInstanceFollowRedirects(false);
			fc.setDoInput(true);
			fc.setDoOutput(true);
			PrintWriter fo = new PrintWriter(fc.getOutputStream());
			fo.print(parma);
			fo.flush();
			InputStream fi = fc.getInputStream();
			Scanner fs = new Scanner(fi);
			while(fs.hasNext()){
				String scsc2 = fs.nextLine();
				
				System.out.println(scsc2);
				
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		
	}
	
	public static String getTbs(String cookie){
		String strBaiduUrl = "http://tieba.baidu.com/dc/common/tbs";
		String tbs = "";
	    URL u1;
		try {
			u1 = new URL(strBaiduUrl);
			HttpURLConnection fc = (HttpURLConnection) u1.openConnection();
			fc.addRequestProperty("Host", "tieba.baidu.com");
			fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
			fc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			fc.addRequestProperty("Referer", "https://www.baidu.com/");
			fc.addRequestProperty("Cookie", cookie);
			fc.addRequestProperty("Connection", "keep-alive");
			fc.connect();
			InputStream fi = fc.getInputStream();
			Scanner fs = new Scanner(fi);
			while(fs.hasNext()){
				String scsc2 = fs.nextLine();
				if(scsc2.indexOf("tbs\":") > -1){
					tbs = scsc2.substring(scsc2.indexOf("tbs\":")).replace("tbs\":", "");
					tbs = tbs.substring(0, tbs.indexOf(",")).replace("\"", "").trim();
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return tbs;
	}
	
	
	public static Map<String,String> getGenTieParams(String url){
		Map<String,String> a = new HashMap<String,String>();
		String strBaiduUrl = url;
		String pubkey = "";
		String rsakey = "";
		String tid="";
	    URL u1;
		try {
			u1 = new URL(strBaiduUrl);
			HttpURLConnection fc = (HttpURLConnection) u1.openConnection();
			fc.addRequestProperty("Host", "tieba.baidu.com");
			fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
			fc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			fc.addRequestProperty("Referer", "https://www.baidu.com/");
			fc.addRequestProperty("Connection", "keep-alive");
			fc.connect();
			InputStream fi = fc.getInputStream();
			Scanner fs = new Scanner(fi,"utf-8");
			while(fs.hasNext()){
				String scsc2 = fs.nextLine();

				if(scsc2.indexOf("kw:") > -1&&pubkey.equals("")){
					pubkey = scsc2.substring(scsc2.indexOf("kw:")).replace("kw:", "");
					pubkey = pubkey.substring(0, pubkey.indexOf(",")).replace("\'", "").trim();
					
					a.put("kw", pubkey);
				}
				if(scsc2.indexOf("fid:") > -1){
					rsakey = scsc2.substring(scsc2.indexOf("fid:")).replace("fid:", "");
					rsakey = rsakey.substring(0, rsakey.indexOf(",")).replace("\'", "").trim();
					
					
					a.put("fid", rsakey);
				}
				if(scsc2.indexOf("tid:") > -1){
					tid = scsc2.substring(scsc2.indexOf("tid:")).replace("tid:", "");
					tid = tid.substring(0, tid.indexOf(",")).replace("\'", "").trim();
					
					
					a.put("tid", tid);
				}
				
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return a;
		
	}
	public static void genTieZi(String sUrl,String cookie,String content,TaskGuideBean taskdo){
			
		Map<String,String> a = getGenTieParams(sUrl);
		
		String tbs = getTbs(cookie);
		String strBaiduUrl = "http://tieba.baidu.com/f/commit/post/add";
		String parma = "";;
		try {
			parma = "ie=utf-8"
					+ "&kw="+URLEncoder.encode(a.get("kw"), "utf-8")
					+ "&fid="+a.get("fid")+"&tid="+a.get("tid")+"&vcode_md5="
					+ "&floor_num=14&rich_text=1"
					+ "&tbs="+tbs
					+ "&content="+URLEncoder.encode(content, "utf-8")
					+ "&files=%5B%5D"
					+ "&mouse_pwd=49%2C58%2C54%2C47%2C48%2C52%2C48%2C50%2C10%2C50%2C47%2C51%2C47%2C50%2C47%2C51%2C47%2C50%2C47%2C51%2C47%2C50%2C47%2C51%2C47%2C50%2C47%2C51%2C10%2C48%2C50%2C52%2C54%2C58%2C10%2C50%2C48%2C53%2C53%2C47%2C52%2C53%2C59%2C14627826168030"
					+ "&mouse_pwd_t="+new Date().getTime()
					+ "&mouse_pwd_isclick=0&__type__=reply";
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	    URL u1;
		try {
			u1 = new URL(strBaiduUrl);
			HttpURLConnection fc = (HttpURLConnection) u1.openConnection();
			fc.addRequestProperty("Host", "tieba.baidu.com");
			fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
			fc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			fc.addRequestProperty("Referer", "https://www.baidu.com/");
			fc.addRequestProperty("Cookie", cookie);
			fc.addRequestProperty("Content-Length", String.valueOf(parma.length()));
			fc.addRequestProperty("Connection", "keep-alive");
			fc.setInstanceFollowRedirects(false);
			fc.setDoInput(true);
			fc.setDoOutput(true);
			PrintWriter fo = new PrintWriter(fc.getOutputStream());
			fo.print(parma);
			fo.flush();
			InputStream fi = fc.getInputStream();
			Scanner fs = new Scanner(fi);
			while(fs.hasNext()){
				String scsc2 = fs.nextLine();
				System.out.println(scsc2);
				
			}
			MQSender.toMQ(taskdo,"");
		} catch (Exception e) {
			MQSender.toMQ(taskdo,"失败");
			e.printStackTrace();
		}
		
		
	}
	
	
	public static String replaceBlank(String str) {
		        String dest = "";
		        if (str!=null) {
		            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		            Matcher m = p.matcher(str);
		            dest = m.replaceAll("");
		        }
		        return dest;
		    }
		    
}
