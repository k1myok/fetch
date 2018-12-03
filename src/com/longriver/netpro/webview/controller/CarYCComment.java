package com.longriver.netpro.webview.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import com.alibaba.fastjson.JSON;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;
/**
 * 易车网
 */
public class CarYCComment {
	
	public static void main(String arsg[]){
		try{
			TaskGuideBean b = new TaskGuideBean();
			b.setNick("hawkfirm");
			b.setPassword("123123");
			b.setAddress("http://news.bitauto.com/drive/20161122/1906755613.html");
			b.setCorpus("没钱买车啊....");
			yc(b);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void yc(TaskGuideBean taskdo){
		try {
				String userId =taskdo.getNick();
				String pwd = taskdo.getPassword();
				String contents = taskdo.getCorpus();
				String sUrl = taskdo.getAddress();
				
				String newId = sUrl.substring(sUrl.lastIndexOf("/")+1,sUrl.indexOf(".html"));
				newId = newId.substring(3).split("\\-")[0];
				System.out.println("newId=="+newId);
				String cookie = "";
				
				
				
				String guid="b3d558a6-d30a-d581-e7b8-103fe17b9122";
				String code = getRom(guid);
				
				String param = "txt_LoginName=" + URLEncoder.encode(userId, "utf-8")
						+ "&txt_Password=" + URLEncoder.encode(pwd, "utf-8")
						
						+ "&&returnurl=&Gamut=true&guid="+guid+"&txt_Code="+code;
				URL u1 = new URL("http://i.bitauto.com/ajax/Authenservice/login.ashx");
				HttpURLConnection c1 = (HttpURLConnection) u1.openConnection();
				//_FakeX509TrustManager.allowAllSSL();
				c1.addRequestProperty("Host", "i.bitauto.com");
				c1.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
				c1.addRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
				c1.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
				c1.addRequestProperty("Referer", "http://i.bitauto.com/AuthenService/Frame/login.aspx?ra=0.4879317351151258&regtype=simple");
				c1.addRequestProperty("X-Requested-With", "XMLHttpRequest");
				c1.addRequestProperty("Content-Length", String.valueOf(param.length()));
				c1.setDoInput(true);
				c1.setDoOutput(true);
				PrintWriter out = new PrintWriter(c1.getOutputStream());
				out.print(param);
				out.flush();
				InputStream f= c1.getInputStream();
				Scanner fs = new Scanner(f);
				String vc = "";
				while(fs.hasNext()){
					String scsc11 = fs.nextLine();
					//System.out.println(scsc11);
					if(scsc11.indexOf("hideImg") > -1){
						vc = scsc11.substring(scsc11.indexOf("hideImg")).replace("hideImg", "");
						vc = vc.substring(vc.indexOf("["), vc.indexOf("]"));
						vc =vc.replace("\"", "").replace("[", "");
					}
				}			
				System.out.println("vc=============="+vc);
				
				String authUrls[] =vc.split(",");
				for(int i=0; i<authUrls.length;i++){
					if(authUrls[i].contains("i.bitauto.com")){
						cookie = getCookies(authUrls[i]);
					}
				}
				System.out.println("cookie=="+cookie);	
				if(cookie!=null && !cookie.equals("")){
					sentContent(cookie,contents,newId,sUrl,taskdo);
				}else{
					MQSender.toMQ(taskdo,"失败");
				}
				//diggComment(newId,"8944498");
		} catch (Exception e) {
			MQSender.toMQ(taskdo,"报错失败");
		}
	}
	public static String getCookies(String url){
		String cookies = "";
		try{
			
			URL fu11 = new URL(url);
			HttpURLConnection fc11 = (HttpURLConnection) fu11.openConnection();
			fc11.addRequestProperty("Accept", "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01");
			fc11.addRequestProperty("Accept-Language", "zh-cn");
			fc11.addRequestProperty("Connection", "Keep-Alive");
			fc11.addRequestProperty("X-Requested-With", "XMLHttpRequest");
			fc11.addRequestProperty("Host", "i.bitauto.com");
			fc11.addRequestProperty("Referer", "http://i.bitauto.com/AuthenService/Frame/login.aspx?ra=0.4879317351151258&regtype=simple");
			fc11.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			fc11.connect();
			InputStream fi11 = fc11.getInputStream();
			Scanner fs11 = new Scanner(fi11);
			Map<String, List<String>> m1 = fc11.getHeaderFields();
			for(Map.Entry<String,List<String>> entry : m1.entrySet()){
				if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
					for(String value : entry.getValue()){
						cookies = cookies + value.substring(0, value.indexOf(";")) + ";";
					}
				}
			}	
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return cookies;
	}
	
	public static void sentContent(String cookie,String content,String newID,String sUrl,TaskGuideBean taskdo){
		try{
			URL fu11 = new URL("http://news.bitauto.com/comment/NewsCommentHandlerN.aspx?type=0&id="+newID+"&seed=0.15721384179778397");
			String param = "__VIEWSTATE=%2FwEPDwUKLTg3OTgwMjMzOWQYAgUYQ3JlYXRlQ29tbWVudFJlcGx5JGx2UmVnDw9kAgFkBRpDcmVhdGVDb21tZW50UmVwbHkkbHZMb2dpbg8PZAIBZC%2BWRgul8c6uQlQDzQzq8rWhoPm0&CreateCommentReply%24txtContent="+(URLEncoder.encode(content, "utf-8"))+"&__VIEWSTATEGENERATOR=E5C998C1&__EVENTVALIDATION=%2FwEdAAKQTAUfHHszSGczEcX5pMR3RV1kLwhU8dhP8Pex6rNj%2BiaBYGJSXSqgOXUyq3k0cwLObEie";
			HttpURLConnection fc11 = (HttpURLConnection) fu11.openConnection();
			fc11.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			fc11.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			fc11.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.93 Safari/537.36");
			
			fc11.addRequestProperty("Cookie", cookie);
			fc11.addRequestProperty("Referer", sUrl);
			
			fc11.addRequestProperty("Content-Length", String.valueOf(param.length()));
			fc11.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			fc11.addRequestProperty("Connection", "Keep-Alive");
			fc11.addRequestProperty("Host", "news.bitauto.com");
			
			fc11.setDoInput(true);
			fc11.setDoOutput(true);
			PrintWriter out = new PrintWriter(fc11.getOutputStream());
			out.print(param);
			out.flush();
			InputStream f= fc11.getInputStream();
			Scanner fs = new Scanner(f);
			String cc = "失败";
			while(fs.hasNext()){
				String scsc11 = fs.nextLine();
				if(scsc11.contains("ok")) cc = "";
			}
			MQSender.toMQ(taskdo,cc);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	public void diggComment(String newId,String commentID){
			try{
				
				String cookie = "";

					URL fu = new URL("http://ip.bitauto.com/iplocation/setcookie.ashx");
					HttpURLConnection fc = (HttpURLConnection) fu.openConnection();
					fc.addRequestProperty("Accept", "*/*");
					fc.addRequestProperty("Accept-Language", "zh-cn");
					fc.addRequestProperty("Connection", "Keep-Alive");
					fc.addRequestProperty("Host", "news.bitauto.com");
					fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
					fc.connect();
					Map<String, List<String>> m1 = fc.getHeaderFields();
					for(Map.Entry<String,List<String>> entry : m1.entrySet()){
						if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
							for(String value : entry.getValue()){
								cookie = cookie + value.substring(0, value.indexOf(";")) + ";";
							}
						}
					}	
				
					URL fu2 = new URL("http://news.bitauto.com/comment/NewsnewStyleCommentHandler.aspx?type=0&id="+newId+"&seed=0.7788538129534572");
					HttpURLConnection fc2 = (HttpURLConnection) fu2.openConnection();
					fc2.addRequestProperty("Accept", "*/*");
					fc2.addRequestProperty("Accept-Language", "zh-cn");
					fc2.addRequestProperty("Connection", "Keep-Alive");
					fc2.addRequestProperty("Host", "news.bitauto.com");
					fc2.addRequestProperty("Cookie", cookie);
					fc2.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
					fc2.connect();
					Map<String, List<String>> m2 = fc2.getHeaderFields();
					for(Map.Entry<String,List<String>> entry : m2.entrySet()){
						if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
							for(String value : entry.getValue()){
								cookie = cookie + value.substring(0, value.indexOf(";")) + ";";
							}
						}
					}	
					
				URL fu11 = new URL("http://news.bitauto.com/comment/Ajax/UpdateStandPoint.aspx?"
						+ "type=0&newsid="+newId
						+ "&id="+commentID
						+ "&isagree=1"
						+ "&callback=SupportCallBack"
						+ "");
				HttpURLConnection fc11 = (HttpURLConnection) fu11.openConnection();
				fc11.addRequestProperty("Accept", "*/*");
				fc11.addRequestProperty("Accept-Language", "zh-cn");
				fc11.addRequestProperty("Connection", "Keep-Alive");
				fc11.addRequestProperty("Host", "news.bitauto.com");
				fc11.addRequestProperty("Cookie", cookie);
				fc11.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
				fc11.connect();
				
				InputStream f= fc11.getInputStream();
				Scanner fs = new Scanner(f);
				while(fs.hasNext()){
					String scsc11 = fs.nextLine();
					System.out.println(scsc11);
				}	
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	public static String getRom(String guid){
		String cookie = "";
		URL u2;
		try {
			u2 = new URL("http://i.bitauto.com/authenservice/common/CheckCode.aspx?guid="+guid);
			HttpURLConnection c2 = (HttpURLConnection) u2.openConnection();
			c2.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36 SE 2.X MetaSr 1.0");
			c2.addRequestProperty("Accept", "image/webp,*/*;q=0.8");
			c2.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			c2.addRequestProperty("Connection", "keep-alive");
			c2.addRequestProperty("Host", "i.bitauto.com");
			c2.addRequestProperty("Referer", "http://i.bitauto.com/AuthenService/Frame/login.aspx?ra=0.5751392936799675&regtype=simple");
			c2.addRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
			
			c2.connect();
			
			InputStream i2 = c2.getInputStream();
			
			// 1K的数据缓冲  
	        byte[] bs = new byte[1024];  
	        // 读取到的数据长度  
	        int len;  
	        // 输出的文件流  
		       File sf=new File("c:\\");  
		       if(!sf.exists()){  
		           sf.mkdirs();  
		       }  
		       String uu = "c:\\yiche.gif";
		       OutputStream os = new FileOutputStream(uu);  
		        // 开始读取  
		        while ((len = i2.read(bs)) != -1) {  
		          os.write(bs, 0, len);  
		        }  
		        // 完毕，关闭所有链接  
		        os.close();  
		        i2.close();  
		        

				String randRom = RuoKuai.createByPostNew("1050",uu);
				if(randRom.length()>0){
					System.out.println("result========================"+randRom);
					cookie = randRom;
				}else{
					cookie = "";
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cookie;
	}
	
}
