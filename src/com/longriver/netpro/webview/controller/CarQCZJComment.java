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
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.common.sohu.QCZJScript;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 *	汽车之家 
 */
public class CarQCZJComment {
	
	public static void main(String arsg[]){
		try{
			TaskGuideBean b = new TaskGuideBean();
			b.setNick("李陵少");
			b.setPassword("lilei419688");
//			b.setAddress("http://www.autohome.com.cn/tuning/201607/890162-7.html#pvareaid=2023114");
			b.setAddress("http://www.autohome.com.cn/drive/201609/889749.html");
			b.setCorpus("其他车企还扛得住吗?");
//			b.setNick("kevinc0211");
//			b.setPassword("25204680hx");
//			b.setAddress("http://www.autohome.com.cn/news/201606/890019.html");
//			b.setCorpus("唐颜值颜值颜值颜值!");
			yc(b);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void yc(TaskGuideBean taskdo){
		
		try {
			String userId = taskdo.getNick();
			String pwd = taskdo.getPassword();
			String sUrl = taskdo.getAddress();
			String contents = taskdo.getCorpus();
			
			String cookie = "";
			String newId = sUrl.substring(sUrl.lastIndexOf("/")+1,sUrl.indexOf(".html"));
			newId = newId.split("-")[0];
			//登录未获得cookie
			String param = "name="+(URLEncoder.encode(userId, "utf-8"))+"&pwd="+new QCZJScript().getUid(pwd)+"&validcode=&isauto=true&type=json&backurl=http%253a%252f%252fwww.autohome.com.cn%252fdrive%252f201606%252f889749.html&url=http%253a%252f%252fwww.autohome.com.cn%252fdrive%252f201606%252f889749.html&";
			URL u1 = new URL("http://account.autohome.com.cn/Login/ValidIndex");
			HttpURLConnection fc11 = (HttpURLConnection) u1.openConnection();
			fc11.addRequestProperty("Accept", "*/*");
			fc11.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			fc11.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.93 Safari/537.36");
			fc11.addRequestProperty("Referer", sUrl);
			fc11.addRequestProperty("Content-Length", String.valueOf(param.length()));
			fc11.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			fc11.addRequestProperty("Connection", "Keep-Alive");
			fc11.addRequestProperty("Host", "account.autohome.com.cn");
			fc11.setDoInput(true);
			fc11.setDoOutput(true);
			PrintWriter out = new PrintWriter(fc11.getOutputStream());
			out.print(param);
			out.flush();
			InputStream f= fc11.getInputStream();
			Scanner fs = new Scanner(f);
			String loginurl = "http:";
			while(fs.hasNext()){//获得登录链接
				String scsc11 = fs.nextLine();
				JSONObject jsonobject = JSONObject.parseObject(scsc11);
				System.out.println("LoginUrl=="+jsonobject.get("LoginUrl"));
				loginurl += jsonobject.get("LoginUrl");
			}			
			//获得cookie
			URL u2 = new URL(loginurl);
			HttpURLConnection fc2 = (HttpURLConnection) u2.openConnection();
			fc2.addRequestProperty("Accept", "*/*");
			fc2.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			fc2.addRequestProperty("Host", "account.che168.com");
			fc2.addRequestProperty("Referer",sUrl);
			fc2.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.93 Safari/537.36");
			fc2.setInstanceFollowRedirects(false);
			fc2.connect();
			Map<String, List<String>> m11 = fc2.getHeaderFields();
			for(Map.Entry<String,List<String>> entry : m11.entrySet()){
				if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
					for(String value : entry.getValue()){
						cookie = cookie + value.substring(0, value.indexOf(";")) + ";";
					}
				}
			}
			try {
				System.out.println("cookie=="+cookie);
				if(cookie!=null && !cookie.equals(""))
					sentContent(cookie,contents,newId,sUrl,taskdo);
			} catch (Exception e) {
				MQSender.toMQ(taskdo,"报错失败");
				e.printStackTrace();
			}
		} catch (Exception e) {
			MQSender.toMQ(taskdo,"报错失败2");
			e.printStackTrace();
		}
				
	}
	
	public static void sentContent(String cookie,String content,String newID,String sUrl,TaskGuideBean taskdo){
		try{
			URL fu11 = new URL("http://www.autohome.com.cn/ashx/article/AjaxSubmitReply.ashx");
			String param = "appid=1&_appid=cms&objid="+newID+"&txtcontent="+URLEncoder.encode(content, "utf-8")+"&dataType=json";
			HttpURLConnection fc11 = (HttpURLConnection) fu11.openConnection();
			fc11.addRequestProperty("Accept", "*/*");
			fc11.addRequestProperty("Accept-Language", "zh-cn");
			fc11.addRequestProperty("Connection", "Keep-Alive");
			fc11.addRequestProperty("X-Requested-With", "XMLHttpRequest");
			fc11.addRequestProperty("Host", "www.autohome.com.cn");
			fc11.addRequestProperty("Cookie", cookie);
			fc11.addRequestProperty("Referer", sUrl);
			fc11.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			
			fc11.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
			fc11.addRequestProperty("Content-Length", String.valueOf(param.length()));
			fc11.setDoInput(true);
			fc11.setDoOutput(true);
			PrintWriter out = new PrintWriter(fc11.getOutputStream());
			out.print(param);
			out.flush();
			InputStream f= fc11.getInputStream();
			Scanner fs = new Scanner(f);
			while(fs.hasNext()){
				String scsc11 = fs.nextLine();
				System.out.println(new String(scsc11.getBytes(),"UTF-8"));
			}
			MQSender.toMQ(taskdo,"");
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
}
