package com.longriver.netpro.webview.controller;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.util.AllowAllSSL;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;
/**
 * 环球旅游评论
 */
public class TrHuanQiuComment2 {
	
	public static void main(String arsg[]){
		try{
			TaskGuideBean b = new TaskGuideBean();
			b.setNick("15652240394");
			b.setPassword("lilei419688");
			b.setAddress("http://go.huanqiu.com/story/2017-09/11369721.html");
			b.setCorpus("不错哦...");
			toRun(b);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void toRun(TaskGuideBean taskdo){
		Map<String,String> map = getCookies(taskdo);
		String cookie= map.get("cookie");
		System.out.println("cookie=="+cookie);
//		sentContent(cookie,map,taskdo.getCorpus());
	}
	public static void sentContent(String cookie,Map<String,String> map,String content){
		try{
			String params = "a=comment" +
					"&m=addnew" +
					"&os=pc&isshare=0" +
					"&uid="+map.get("uid") +
					"&sid=5a0459391d484e72458b5a4b" +
					"&cid=&pid=" +
					"&content="+content +
					"&appid=e8fcff106c8f" +
					"&_ja95dsnc";
			String url = "http://commentn.huanqiu.com/api/v2";
			URL fu11 = new URL(url);
			HttpURLConnection fc11 = (HttpURLConnection) fu11.openConnection();
			fc11.addRequestProperty("Host", "commentn.huanqiu.com");
			fc11.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
			fc11.addRequestProperty("Accept", "*/*");
			fc11.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			fc11.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			fc11.addRequestProperty("Connection", "Keep-Alive");
			fc11.addRequestProperty("If-Modified-Since", "0");
			fc11.addRequestProperty("Referer", "http://commentn.huanqiu.com/assets/HQ_dataPedal.v2.html");
			fc11.addRequestProperty("Cookie", cookie);
			fc11.addRequestProperty("Content-Length", params.length()+"");
			fc11.addRequestProperty("Pragma", "no-cache");
			fc11.addRequestProperty("Cache-Control", "no-cache");
			fc11.setDoInput(true);
			fc11.setDoOutput(true);
			PrintWriter out = new PrintWriter(fc11.getOutputStream());
			out.print(params);
			out.flush();
			InputStream f= fc11.getInputStream();
			Scanner fs = new Scanner(f);
			while(fs.hasNext()){//获得登录链接
				String scsc11 = fs.nextLine();
				System.out.println("scsc11=="+scsc11);
			}			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static Map<String,String> getCookies(TaskGuideBean taskdo){
		Map<String,String> map = new HashMap<String,String>();
		String cookies = "";
		try{
			String url = "http://interface.huanqiu.com/api.php?" +
					"t=txz&m=login" +
					"&email=" +taskdo.getNick()+
					"&password=" +taskdo.getPassword()+
					"&sign=4be0555c4e33c78d46faa99e24ab253c" +
					"&callback=ssoLogin";
			URL fu11 = new URL(url);
			HttpURLConnection fc11 = (HttpURLConnection) fu11.openConnection();
			fc11.addRequestProperty("Host", "interface.huanqiu.com");
			fc11.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
			fc11.addRequestProperty("Accept", "*/*");
			fc11.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			fc11.addRequestProperty("Connection", "Keep-Alive");
			fc11.addRequestProperty("Referer", taskdo.getAddress());
			fc11.addRequestProperty("Pragma", "no-cache");
			fc11.addRequestProperty("Cache-Control", "no-cache");
			fc11.connect();
			Map<String, List<String>> m1 = fc11.getHeaderFields();
			for(Map.Entry<String,List<String>> entry : m1.entrySet()){
				System.out.println(entry.getKey()+": "+entry.getValue());
				if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
					for(String value : entry.getValue()){
						cookies = cookies + value.substring(0, value.indexOf(";")) + ";";
					}
				}
			}
			InputStream f= fc11.getInputStream();
			Scanner fs = new Scanner(f);
			String scsc11 = "";
			while(fs.hasNext()){//获得登录链接
				scsc11 = fs.nextLine();
			}
			int f1 = scsc11.indexOf("{");
			int f11 = scsc11.lastIndexOf("}")+1;
			scsc11 = scsc11.substring(f1, f11);
			System.out.println("scsc1=="+scsc11);
			JSONObject jb = JSONObject.parseObject(scsc11);
			//_HUANQIU_AUTH_ID
			String url2 = "http://commentn.huanqiu.com/api/v2?" +
					"a=userinfo&m=addAloneUsers" +
					"&openid="+jb.getString("uid") +
					"&avatar="+jb.getString("avatar") +
					"&nickname=" +StringUtil.decode(jb.getString("nickname"))+
					"&auth_token="+jb.getString("auth_token") +
					"&source=huanqiu" +
					"&appid=e8fcff106c8f" +
					"&callback=addAloneUsers";
			URL fu12 = new URL(url2);
			HttpURLConnection fc2 = (HttpURLConnection) fu12.openConnection();
			fc2.addRequestProperty("Host", "commentn.huanqiu.com");
			fc2.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
			fc2.addRequestProperty("Accept", "*/*");
			fc2.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			fc2.addRequestProperty("Connection", "Keep-Alive");
			fc2.addRequestProperty("Referer", taskdo.getAddress());
			fc2.addRequestProperty("Cookie", cookies);
			fc2.addRequestProperty("Pragma", "no-cache");
			fc2.addRequestProperty("Cache-Control", "no-cache");
			fc2.connect();
			Map<String, List<String>> m2 = fc2.getHeaderFields();
			String _HUANQIU_AUTH_ID = "";
			String uid = "";
			for(Map.Entry<String,List<String>> entry : m2.entrySet()){
				if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
					for(String value : entry.getValue()){
						System.out.println("value=="+value);
						if(value.contains("_HUANQIU_AUTH_ID")){
							value = value.substring(value.indexOf("_HUANQIU_AUTH_ID"));
							int tt = value.indexOf(";");
							_HUANQIU_AUTH_ID = value.substring(0, tt+1);
						}
						if(value.contains("uid")){
							value = value.substring(value.indexOf("uid"));
							int tt = value.indexOf(";");
							uid = value.substring(0, tt+1);
							map.put("uid", uid);
						}
						
					}
				}
			}
			System.out.println("_HUANQIU_AUTH_ID=="+_HUANQIU_AUTH_ID);
			cookies += _HUANQIU_AUTH_ID;
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("cookie", cookies);
		return map;
	}
	
}
