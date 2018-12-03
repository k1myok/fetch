package com.longriver.netpro.webview.controller;

import java.io.InputStream;
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

/**
 * 58车网
 */
public class CarQCDPComment {
	public static void main(String arsg[]){
		try{
			TaskGuideBean b = new TaskGuideBean();
			b.setNick("hawkfirm");
			b.setPassword("123123");
			b.setAddress("http://news.58che.com/news/1514733.html");
			b.setCorpus("纸老虎????");
			toRun(b);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void toRun(TaskGuideBean taskdo){
		
		try {
			String userId = taskdo.getNick();
			String pwd = taskdo.getPassword();
			String contents = taskdo.getCorpus();
			String sUrl = taskdo.getAddress();
			
			String articleid = sUrl.substring(sUrl.lastIndexOf("/")).replace("/", "").replace(".html", "");
			String cookie=getCookie(userId,pwd,sUrl);
			
			System.out.println("============="+cookie);
			sendConent(contents,cookie,articleid);
			MQSender.toMQ(taskdo,"");
		}catch (Exception e) {
			MQSender.toMQ(taskdo,"报错失败");
			e.printStackTrace();
		}
		
	}
	public static String getCookie(String username,String pwd,String url){
		String cookie = "";
		URL fu;
		try {
					fu = new URL("http://bbs.58che.com/api.php?m=user&c=login&a=user_login");
				
				HttpURLConnection fc = (HttpURLConnection) fu.openConnection();
				String fp = "username="+username
						+ "&password="+pwd
						+ "&type=23"
						+ "&backUrl="+ URLEncoder.encode(url, "utf-8")
						+ "&rand=1468389864696";
				System.out.println(fp);
				fc.addRequestProperty("Host", "bbs.58che.com");
				fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
				fc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
				fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
				fc.addRequestProperty("Referer", "http://bbs.58che.com/logging.php?backurl="+url);
				fc.addRequestProperty("Connection", "keep-alive");
				fc.addRequestProperty("Content-Length", String.valueOf(fp.length()));
				fc.setInstanceFollowRedirects(false);
				fc.setDoInput(true);
				fc.setDoOutput(true);
				PrintWriter fo = new PrintWriter(fc.getOutputStream());
				fo.print(fp);
				fo.flush();
				Map<String, List<String>> m1 = fc.getHeaderFields();
				for(Map.Entry<String,List<String>> entry : m1.entrySet()){
					if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
						for(String value : entry.getValue()){
							cookie = cookie + value.substring(0, value.indexOf(";")) + ";";
						}
					}
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cookie;
	}
	
	
	public static void sendConent(String contents,String cookie,String articleid){
		URL fu;
		try {
			  fu = new URL("http://comments.58che.com/index.php?cf=a");
				
				HttpURLConnection fc = (HttpURLConnection) fu.openConnection();
				String fp = "kindid=2"
						+ "&articleid="+articleid
						+ "&comment_id=0"
						+ "&content="+ URLEncoder.encode(contents, "utf-8");
				System.out.println(fp);
				fc.addRequestProperty("Host", "comments.58che.com");
				fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
				fc.addRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
				fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
				fc.addRequestProperty("Referer", "https://service.cheshi.com/user/login.php");
				fc.addRequestProperty("Connection", "keep-alive");
				fc.addRequestProperty("Content-Length", String.valueOf(fp.length()));
				fc.addRequestProperty("Cookie", cookie);
				fc.setInstanceFollowRedirects(false);
				fc.setDoInput(true);
				fc.setDoOutput(true);
				PrintWriter fo = new PrintWriter(fc.getOutputStream());
				fo.print(fp);
				fo.flush();
				InputStream fi = fc.getInputStream();
				Scanner fs = new Scanner(fi);
				while(fs.hasNext()){
					String scsc2 = fs.nextLine();
					System.out.println(scsc2);
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
