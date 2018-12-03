package com.longriver.netpro.webview.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.util.encoders.Base64;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.longriver.netpro.common.sina.SData;
import com.longriver.netpro.common.sohu._FakeX509TrustManager;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.PngErjinzhi;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

public class SinaNewsComment {
	
	public static void main(String args[]){
//		String userId = "451711035@qq.com";
//		String pwd = "bifeng119";
//		String url = "http://comment5.news.sina.com.cn/comment/skin/default.html?channel=gn&newsid=comos-fxhtvkk5791768";
//		String contents = "陆丰汽车真的不错";
		TaskGuideBean info = new TaskGuideBean();
		info.setNick("lilei1929@163.com");
		info.setPassword("lilei419688..");
//		info.setNick("nofuny@sina.com");
//		info.setPassword("258099");
		info.setCorpus("值得拥有");
//		info.setAddress("http://news.sina.com.cn/c/nd/2017-10-09/doc-ifymrqmq1915585.shtml");
//		info.setAddress("http://edu.sina.com.cn/a/2017-09-28/doc-ifymfcih7227840.shtml");
//		info.setAddress("http://news.sina.com.cn/gov/2017-10-12/doc-ifymvuys7829286.shtml ");
		info.setAddress("http://k.sina.com.cn/article_1779884443_6a16e19b020007xmj.html?from=auto&subch=bauto");
		sina(info);
	}
	public static void sina(TaskGuideBean taskdo){
//		taskdo.setPng(PngErjinzhi.getImageBinary("1"));
		SData data =new SData();
		
		String address = SinaCommentJietu.getCommentAdd(taskdo);
		
		try {
			String userId =taskdo.getNick();
			String pwd = taskdo.getPassword();
			String contents = taskdo.getCorpus();
			String url = URLDecoder.decode(address,"utf-8");
			
			data.put("user_account_id", userId);
			data.put("user_account_pw", pwd);
			data.put("mission_addr", url);
			data.put("mission_contents", contents);
			
			String sso_info = "";
			String lt = "";
			String alf = "";
			String alc = "";
			String subp = "";
			String sub = "";
			String sup = "";
			String sue = "";
			String sus = "";
			String tgc = "";
			String u_trs2 = "";
			String u_trs1 = "";
			
			
			_FakeX509TrustManager.allowAllSSL();
			String param = "entry=sso"
					+ "&gateway=1"
					+ "&from=null"
					+ "&savestate=30"
					+ "&useticket=0"
					+ "&pagerefer=https%3A%2F%2Flogin.sina.com.cn%2Fsignup%2Fsignup%3Fentry%3Dnews&vsnf=1"
					+ "&su=" + new String(Base64.encode(URLEncoder.encode(userId, "gb2312").getBytes("gb2312")))
					+ "&service=news"
					+ "&sp=" + URLEncoder.encode(pwd, "gb2312")
					+ "&sr=1440*900"
					+ "&encoding=UTF-8"
					+ "&cdult=3"
					+ "&domain=sina.com.cn"
					+ "&prelt=0"
					+ "&returntype=TEXT";
			System.out.println("param====="+param);
			URL u1 = new URL("https://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.15)&_=" + new Date().getTime());
			HttpURLConnection c1 = (HttpURLConnection) u1.openConnection();
			c1.addRequestProperty("Host", "login.sina.com.cn");
			c1.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0");
			c1.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			c1.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
			c1.addRequestProperty("Connection", "keep-alive");
			c1.setDoOutput(true);
			c1.setDoInput(true);
			PrintWriter out = new PrintWriter(c1.getOutputStream());
			out.print(param);
			out.flush();
			Map<String, List<String>> m1 = c1.getHeaderFields();
			for(Map.Entry<String,List<String>> entry : m1.entrySet()){
				System.out.println("key=="+entry.getKey());
				System.out.println("value=="+entry.getValue());
				if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
					for(String value : entry.getValue()){
						System.out.println("value=="+value);
						if(value.indexOf("sso_info=") > -1){
							sso_info = value.substring(0, value.indexOf(";") + 1);
							System.out.println("1==");
						}
						if(value.indexOf("LT=") > -1){
							lt = value.substring(0, value.indexOf(";") + 1);
							System.out.println("2==");
						}
						if(value.indexOf("ALF=") > -1){
							alf = value.substring(0, value.indexOf(";") + 1);
							System.out.println("3==");
						}
						if(value.indexOf("ALC=") > -1){
							alc = value.substring(0, value.indexOf(";") + 1);
							System.out.println("4==");
						}
						if(value.indexOf("SUBP=") > -1){
							subp = value.substring(0, value.indexOf(";") + 1);
							System.out.println("5==");
						}
						if(value.indexOf("SUB=") > -1){
							sub = value.substring(0, value.indexOf(";") + 1);
							System.out.println("6==");
						}
						if(value.indexOf("SUP=") > -1){
							sup = value.substring(0, value.indexOf(";") + 1);
							System.out.println("7==");
						}
						if(value.indexOf("SUE=") > -1){
							sue = value.substring(0, value.indexOf(";") + 1);
							System.out.println("8==");
						}
						if(value.indexOf("SUS=") > -1){
							sus = value.substring(0, value.indexOf(";") + 1);
							System.out.println("9==");
						}
						if(value.indexOf("tgc=") > -1){
							tgc = value.substring(0, value.indexOf(";") + 1);
							System.out.println("10==");
						}
						if(value.indexOf("U_TRS2=") > -1){
							u_trs2 = value.substring(0, value.indexOf(";") + 1);
							System.out.println("11==");
						}
						if(value.indexOf("U_TRS1=") > -1){
							u_trs1 = value.substring(0, value.indexOf(";") + 1);
							System.out.println("12==");
						}
					}
				}
			}
			InputStream i1 = c1.getInputStream();
			Scanner s1 = new Scanner(i1, "gb2312");
			while(s1.hasNext()){
				String scsc = s1.nextLine();
				if(scsc.indexOf("\"retcode\":\"101\"") > -1){
					MQSender.toMQ(taskdo,"登录失败!");
					return;
				}
			}
			///以上登录
			
			String content = "";
			System.out.println("--------------------------------------11111111111111111111111111111111111--------------------------------------");
			String key = "";
			//http://news.sina.com.cn/c/2014-12-21/013931309107.shtml
			//http://comment5.news.sina.com.cn/comment/skin/default.html?channel=gn&newsid=1-1-31309107&style=0
			//http://comment5.news.sina.com.cn/hotnews/info?format=js&channel=gn&hotid=gn_day&jsvar=requestId_15660403
			String chanel = "";
			if(url.indexOf("http://comment5.") == -1){
				URL u2 = new URL(url);
				HttpURLConnection c2 = (HttpURLConnection) u2.openConnection();
				c2.connect();
				InputStream i2 = c2.getInputStream();
				Scanner s2 = new Scanner(i2, "gb2312");
				
				while(s2.hasNext()){
					String scsc = s2.nextLine();
					if(scsc.indexOf("moodcounter") > -1 && scsc.indexOf("key=") > -1){
						key = scsc.substring(scsc.indexOf("key=")).replace("key=", "").trim();
						key = key.substring(1);
						key = key.substring(0, key.indexOf("\""));
					}
					if(scsc.indexOf("meta") > -1 && scsc.indexOf("name=\"comment\"") > -1){
						key = scsc.substring(scsc.indexOf("content=")).replace("content=\"", "").replace("gn:", "");
						key = key.substring(0, key.indexOf("\""));
					}
					if(scsc.indexOf("channel:'") > -1){
						chanel = scsc.substring(scsc.indexOf("channel:'")).replace("channel:'", "");
						chanel = chanel.substring(0, chanel.indexOf("'"));
								
					}
				}
			}else{
				//http://comment5.news.sina.com.cn/comment/skin/default.html?channel=gn&newsid=1-1-31309107&style=0
				key = url.substring(url.indexOf("newsid=")).replace("newsid=", "");
				if(key.indexOf("&") > -1){
					key = key.substring(0, key.indexOf("&")).replace("&", "");
				}
				chanel = url.substring(url.indexOf("channel=")).replace("channel=", "");
				if(chanel.indexOf("&") > -1){
					chanel = chanel.substring(0, chanel.indexOf("&")).replace("&", "");
				}
			}
			System.out.println("--------------------------------------22222222222222222222222222222222222--------------------------------------");
			if(data.getString("mission_addr").indexOf("sky.news") == -1 && data.getString("mission_addr").indexOf("comment5.news") == -1){
				String params = "channel=" + chanel
						+ "&newsid=" + key
						+ "&parent=B"
						+ "&content=" + URLEncoder.encode(contents, "gb2312")
						+ "&format=js"
						+ "&ie=gbk"
						+ "&oe=gbk"
						+ "&ispost=0"
						+ "&share_url=" + data.getString("mission_addr")
						+ "&video_url="
						+ "&img_url=";
				URL u3 = new URL("http://comment5.news.sina.com.cn/cmnt/submit");
				HttpURLConnection c3 = (HttpURLConnection) u3.openConnection();
				c3.addRequestProperty("Host", "comment5.news.sina.com.cn");
				c3.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0");
				c3.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
				c3.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
				c3.addRequestProperty("Referer", data.getString("mission_addr"));
				c3.addRequestProperty("Cookie", sso_info + lt + alf + alc + subp + sub + sup + sue + sus + tgc + u_trs2 + u_trs1);
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
					f = scsc;
					String code = formatJson(f);
					if(code.equals("1005")){
						content = "需要手机验证";
					}else if(scsc.indexOf("Catch exception : getUserInfo: sup does not exist!") > -1){
						content = "sup does not exist!";
					}else if(code.equals("4000") || code.equals("4001")){
						content = "";
					}else if(scsc.indexOf("\"id\":") > -1){
						f = scsc.substring(scsc.lastIndexOf("\"id\":")).replace("\"id\":", "");
						f = f.substring(f.indexOf("\"") + 1);
						f = f.substring(0, f.indexOf("\""));
						b = true;
					}else if(scsc.indexOf("Catch exception : getUserInfo: sup does not exist!") > -1){
						content = "失败1";
					}else{
						f = "error";
						content = "失败2";
					}
					String missionId = data.getString("mission_id");
					String statusCd = "0";
					if(b){
						statusCd = "1";
					}
				}
			}else if(data.getString("mission_addr").indexOf("comment5.news") > -1){
				String newsId = data.getString("mission_addr").substring(data.getString("mission_addr").indexOf("newsid=")).replace("newsid=", "");
				if(newsId.indexOf("&") > -1){
					newsId = newsId.substring(0, newsId.indexOf("&"));
				}
				chanel = data.getString("mission_addr").substring(data.getString("mission_addr").indexOf("channel=")).replace("channel=", "");
				if(chanel.indexOf("&") > -1){
					chanel = chanel.substring(0, chanel.indexOf("&"));
				}
				String params = "channel=" + chanel
						+ "&newsid=" + newsId
						+ "&parent=B"
						+ "&content=" + URLEncoder.encode(contents, "gb2312")
						+ "&format=js"
						+ "&ie=gbk"
						+ "&oe=gbk"
						+ "&ispost=0"
						+ "&share_url=" + URLEncoder.encode(data.getString("mission_addr"), "gb2312")
						+ "&video_url="
						+ "&img_url=";
				System.out.println("params=="+params);
				URL u3 = new URL("http://comment5.news.sina.com.cn/cmnt/submit");
				HttpURLConnection c3 = (HttpURLConnection) u3.openConnection();
				c3.addRequestProperty("Host", "comment5.news.sina.com.cn");
				c3.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0");
				c3.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
				c3.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
				c3.addRequestProperty("Referer", data.getString("mission_addr"));
				c3.addRequestProperty("Cookie", sso_info + lt + alf + alc + subp + sub + sup + sue + sus + tgc + u_trs2 + u_trs1);
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
					f = scsc;
					String code = formatJson(f);
					if(code.equals("1005")){
						content = "需要手机验证";
					}else if(scsc.indexOf("Catch exception : getUserInfo: sup does not exist!") > -1){
						content = "sup does not exist!";
					}else if(code.equals("4000") || code.equals("4001")){
						content = "";
					}else if(scsc.indexOf("\"id\":") > -1){
						f = scsc.substring(scsc.lastIndexOf("\"id\":")).replace("\"id\":", "");
						f = f.substring(f.indexOf("\"") + 1);
						f = f.substring(0, f.indexOf("\""));
						b = true;
					}else if(scsc.indexOf("Catch exception : getUserInfo: sup does not exist!") > -1){
						content = "失败1";
					}else{
						f = "error";
						content = "失败2";
					}
					String missionId = data.getString("mission_id");
					String statusCd = "0";
					if(b){
						statusCd = "1";
					}
				}
			}else{
				URL u = new URL(data.getString("mission_addr"));
				HttpURLConnection c = (HttpURLConnection) u.openConnection();
				c.connect();
				InputStream i = c.getInputStream();
				Scanner s = new Scanner(i);
				String newsId = "";
				while(s.hasNext()){
					String scsc = s.nextLine();
					if(scsc.indexOf("newsid:") > -1){
						newsId = scsc.substring(scsc.indexOf("newsid:'")).replace("newsid:'", "");
						newsId = newsId.substring(0, newsId.indexOf("'"));
					}
				}
				
				String params = "channel=" + chanel
						+ "&newsid=" + newsId
						+ "&parent=B"
						+ "&content=" + URLEncoder.encode(contents, "gb2312")
						+ "&format=js"
						+ "&ie=gbk"
						+ "&oe=gbk"
						+ "&ispost=0"
						+ "&share_url=" + URLEncoder.encode(data.getString("mission_addr"), "gb2312")
						+ "&video_url="
						+ "&img_url=";
				URL u3 = new URL("http://comment5.news.sina.com.cn/cmnt/submit");
				HttpURLConnection c3 = (HttpURLConnection) u3.openConnection();
				c3.addRequestProperty("Host", "comment5.news.sina.com.cn");
				c3.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0");
				c3.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
				c3.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
				c3.addRequestProperty("Referer", data.getString("mission_addr"));
				c3.addRequestProperty("Cookie", sso_info + lt + alf + alc + subp + sub + sup + sue + sus + tgc + u_trs2 + u_trs1);
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
					f = scsc;
					String code = formatJson(f);
					if(code.equals("1005")){
						content = "需要手机验证";
					}else if(scsc.indexOf("Catch exception : getUserInfo: sup does not exist!") > -1){
						content = "sup does not exist!";
					}else if(code.equals("4000") || code.equals("4001")){
						content = "";
					}else if(scsc.indexOf("\"id\":") > -1){
						f = scsc.substring(scsc.lastIndexOf("\"id\":")).replace("\"id\":", "");
						f = f.substring(f.indexOf("\"") + 1);
						f = f.substring(0, f.indexOf("\""));
						b = true;
					}else if(scsc.indexOf("Catch exception : getUserInfo: sup does not exist!") > -1){
						content = "失败1";
					}else{
						f = "error";
						content = "失败2";
					}
					String missionId = data.getString("mission_id");
					String statusCd = "0";
					if(b){
						statusCd = "1";
					}
				}				
			}
			MQSender.toMQ(taskdo,content);
		}catch (IOException e) {
			MQSender.toMQ(taskdo,"报错失败!");
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
	public static String formatJson(String str) {
		System.out.println("str=="+str);
		if (str != null) {            
			int i1= str.indexOf("{");
			int i1_1= str.lastIndexOf("}")+1;
			str = str.substring(i1, i1_1);
			JSONObject jb = JSONObject.parseObject(str);
			JSONObject jb2 = jb.getJSONObject("result");
			String code = (String) jb2.get("filter_code");
			System.out.println("code=="+code);
			return code;
		}
		return str;    
	}
	
}
