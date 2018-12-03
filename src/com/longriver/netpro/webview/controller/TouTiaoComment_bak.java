package com.longriver.netpro.webview.controller;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.common.sohu._FakeX509TrustManager;
import com.longriver.netpro.fetchScript.ToutiaoNewFetch;
import com.longriver.netpro.util.Base64ToImage;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;


public class TouTiaoComment_bak {
	private static String lpath = "C:\\vcode\\toutiao.gif";
	public static void main(String args[]){
		try{
			TaskGuideBean taskdo = new TaskGuideBean();
//			taskdo.setNick("18854579641");
//			taskdo.setPassword("Mopao7077");
//			taskdo.setNick("15999726521");
//			taskdo.setPassword("Kaoshun91");
			taskdo.setNick("wljiaj492711725@163.com");
			taskdo.setPassword("Jufei607");
//			taskdo.setAddress("http://www.toutiao.com/a6466594468135961102/");
			taskdo.setAddress("http://www.toutiao.com/a6470035854919205389/" +
					"?tt_from=weixin&utm_campaign=client_share&article_category=automobile" +
					"&app=automobile&utm_source=weixin&iid=14157799151&utm_medium=toutiao_android&wxshare_count=1");
//			taskdo.setAddress("http://www.toutiao.com/a6375828888539840770/#comment_area");
			taskdo.setPraiseWho("6826042570");
			taskdo.setCorpus("这个???");
			
//			System.out.println(getCode(cookie));
//			toutiaoComment(taskdo);
//			taskdo.setPraiseWho("1578931047962637");
//			toutiaoDigg(taskdo);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public static void toutiaoComment(TaskGuideBean taskdo){
		//此cookie后面没有用到
		String cookie = toLogin(taskdo,0);
		System.out.println("cookie=="+cookie);
		if(!cookie.equals("")){
			boolean flag = toComment(taskdo,cookie);
			if(flag){
				MQSender.toMQ(taskdo,"");
			}else{
				MQSender.toMQ(taskdo,"失败!");
			}
		}else{
			System.out.println("未获得验证码!");
			MQSender.toMQ(taskdo,"验证码失败!");
		}
	}
	public static void toutiaoDigg(TaskGuideBean taskdo){
		//此cookie后面没有用到
		String cookie = toLogin(taskdo,0);
		System.out.println("cookie=="+cookie);
		if(!cookie.equals("")){
			boolean flag = toDigg(taskdo,cookie);
			if(flag){
				MQSender.toMQ(taskdo,"");
			}else{
				MQSender.toMQ(taskdo,"失败!");
			}
		}else{
			System.out.println("未获得验证码!");
			MQSender.toMQ(taskdo,"未获得验证码!");
		}
	}
	public static boolean toComment(TaskGuideBean taskdo,String cookie){
		try {
			Map<String,String> map = ToutiaoNewFetch.getGroupIdandItemsId(taskdo.getAddress());
			String corpus = taskdo.getCorpus();
			String params = "status=" +StringUtil.encode(corpus)+
					"&content=" +StringUtil.encode(corpus)+
					"&group_id=" +map.get("group_id")+
					"&item_id=" +map.get("item_id")+
					"&id=0" +
					"&format=json" +
					"&aid=24";
			System.out.println(params);
			URL u1 = new URL("http://www.toutiao.com/api/comment/post_comment/");
			HttpURLConnection c1 = (HttpURLConnection) u1.openConnection();
			c1.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			c1.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			c1.addRequestProperty("Content-Length", String.valueOf(params.length()));
			c1.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			c1.addRequestProperty("Connection", "keep-alive");
			c1.addRequestProperty("Cookie", cookie);
			c1.addRequestProperty("Host", "www.toutiao.com");
			c1.addRequestProperty("Referer", taskdo.getUrl());
			c1.addRequestProperty("X-Requested-With", "XMLHttpRequest");
			c1.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			c1.setDoInput(true);
			c1.setDoOutput(true);
			PrintWriter out = new PrintWriter(c1.getOutputStream());
			out.print(params);
			out.flush();
			InputStream i45 = c1.getInputStream();
	        Scanner s45 = new Scanner(i45, "utf-8");
	        StringBuffer sb = new StringBuffer();
	        while(s45.hasNext()){
	        	String scsc = s45.nextLine();
	        	System.out.println(scsc);
	        	sb.append(scsc);
	        }
	        String resp = sb.toString();
	        JSONObject jsStr = JSONObject.parseObject(resp);
	        String desc = StringUtil.decode(jsStr.getString("message"));
	        if(desc.equals("success")){
	        	System.out.println("发布成功!");
	        	return true;
	        }else{
	        	System.out.println("发布失败!");
	        	return false;
	        }
		} catch (Exception e) {
			System.out.println("发布失败!");
			e.printStackTrace();
			return false;
		}
	}
	public static String toLogin(TaskGuideBean taskdo,int num){
		String cookie = "";
		Map<String,String> map = getCode();
		String code = map.get("vcode");
		String cookieTmp = map.get("cookie");
		try {
			String userName =taskdo.getNick();
			String userPwd = taskdo.getPassword();
			String params = "mobile=" +
					"&code=" +
					"&account=" +StringUtil.encode(userName)+
					"&password=" +StringUtil.encode(userPwd)+
					"&captcha=" +code+
					"&is_30_days_no_login=false" +
					"&service=http%3A%2F%2Fwww.toutiao.com%2F";
			System.out.println(params);
			URL u1 = new URL("https://sso.toutiao.com/account_login/");
			HttpURLConnection c1 = (HttpURLConnection) u1.openConnection();
			c1.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			c1.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			c1.addRequestProperty("Content-Length", String.valueOf(params.length()));
			c1.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			c1.addRequestProperty("Connection", "keep-alive");
			c1.addRequestProperty("Cookie", cookieTmp);
			c1.addRequestProperty("Host", "sso.toutiao.com");
			c1.addRequestProperty("Referer", "https://sso.toutiao.com/login/");
			c1.addRequestProperty("X-Requested-With", "XMLHttpRequest");
			c1.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			c1.setDoInput(true);
			c1.setDoOutput(true);
			PrintWriter out = new PrintWriter(c1.getOutputStream());
			out.print(params);
			out.flush();
			//判断验证码
			InputStream i45 = c1.getInputStream();
	        Scanner s45 = new Scanner(i45, "utf-8");
	        StringBuffer sb = new StringBuffer();
	        while(s45.hasNext()){
	        	String scsc = s45.nextLine();
	        	System.out.println(scsc);
	        	sb.append(scsc);
	        }
	        String resp = sb.toString();
	        JSONObject jsStr = JSONObject.parseObject(resp);
	        String desc = StringUtil.decode(jsStr.getString("description"));
	        if(desc.contains("验证码为空") || desc.contains("错误")){
	        	System.out.println("验证码错误!");
	        	num++;
	        	if(num<3) return toLogin(taskdo,num);
	        	else{
	        		System.out.println("三次验证码输入错误!");
	        		return "";
	        	}
	        }else if(desc.contains("成功")){
	        	System.out.println("登陆成功!");
	        	//登陆成功后返回必须链接,获得所需cookie
	        	String redirect_url = jsStr.getString("redirect_url");
	        	URL u3 = new URL(redirect_url);
				HttpURLConnection fc11 = (HttpURLConnection) u3.openConnection();
				fc11.addRequestProperty("Accept", "text/javascript, text/html, application/xml, text/xml, */*");
				fc11.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
				fc11.addRequestProperty("Connection", "Keep-Alive");
				fc11.addRequestProperty("Cookie", cookieTmp);
				fc11.addRequestProperty("Host", "www.toutiao.com");
				fc11.addRequestProperty("Referer", "https://sso.toutiao.com/login/");
				fc11.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:55.0) Gecko/20100101 Firefox/55.0");
				fc11.connect();
	        	Map<String, List<String>> m1 = fc11.getHeaderFields();
				for(Map.Entry<String,List<String>> entry : m1.entrySet()){
					System.out.println(entry.getKey());
					if(entry.getKey() != null && entry.getKey().equals("Set-Cookie")){
						for(String value : entry.getValue()){
							cookie = cookie + value.substring(0, value.indexOf(";")) + ";";
						}
					}
				}
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cookie;
	}
	//参数如果不好用,可以用下面生成的cookie-->ck
	public static Map<String,String> getCode(){
		String vcode = "";
		Map<String,String> map = new HashMap<String,String>();
		try {
			_FakeX509TrustManager.allowAllSSL();
			String codeAddr = "https://sso.toutiao.com/login/";
			URL u1 = new URL(codeAddr);
			HttpURLConnection fc11 = (HttpURLConnection) u1.openConnection();
			fc11.addRequestProperty("Accept", "text/javascript, text/html, application/xml, text/xml, */*");
			fc11.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			fc11.addRequestProperty("Connection", "Keep-Alive");
//			fc11.addRequestProperty("Cookie", cookie);
			fc11.addRequestProperty("Host", "sso.toutiao.com");
			fc11.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:55.0) Gecko/20100101 Firefox/55.0");
			fc11.connect();
			String ck = "";
			Map<String, List<String>> m1 = fc11.getHeaderFields();
			for(Map.Entry<String,List<String>> entry : m1.entrySet()){
				System.out.println(entry.getKey());
				if(entry.getKey() != null && entry.getKey().equals("Set-Cookie")){
					for(String value : entry.getValue()){
						ck = ck + value.substring(0, value.indexOf(";")) + ";";
					}
				}
			}
			System.out.println("ck=="+ck);
			map.put("cookie", ck);
			InputStream i45 = fc11.getInputStream();
	        Scanner s45 = new Scanner(i45, "utf-8");
	        StringBuffer sb = new StringBuffer();
	        while(s45.hasNext()){
	        	String scsc = s45.nextLine();
//	        	System.out.println("scsc=="+scsc);
	        	sb.append(scsc);
	        }
	        String resp = sb.toString();
	        System.out.println("resp="+resp);
	        int dd1 = resp.indexOf("captcha:");
	        resp = resp.substring(dd1);
	        int tow = resp.indexOf("'", resp.indexOf("'")+1);
	        resp = resp.substring(8, tow).replaceAll("'", "").trim();
	        if(resp!=null && !resp.equals("")){
	        	Base64ToImage.ToImage(resp,lpath);
	        	vcode = RuoKuai.createByPostNew("3040",lpath);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("vcode", vcode);
		System.out.println("vcode=="+vcode);
		return map;
	}
	//点赞
	public static boolean toDigg(TaskGuideBean taskdo,String cookie){
		try {
			Map<String,String> map = ToutiaoNewFetch.getGroupIdandItemsId(taskdo.getAddress());
			String params = "comment_id=" +taskdo.getPraiseWho()+
					"&dongtai_id=" +taskdo.getPraiseWho()+
					"&group_id=" +map.get("group_id")+
					"&item_id=" +map.get("item_id")+
					"&action=digg";
			System.out.println(params);
			URL u1 = new URL("http://www.toutiao.com/api/comment/digg/");
			HttpURLConnection c1 = (HttpURLConnection) u1.openConnection();
			c1.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			c1.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			c1.addRequestProperty("Content-Length", String.valueOf(params.length()));
			c1.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			c1.addRequestProperty("Connection", "keep-alive");
			c1.addRequestProperty("Cookie", cookie);
			c1.addRequestProperty("Host", "www.toutiao.com");
			c1.addRequestProperty("Referer", taskdo.getUrl());
			c1.addRequestProperty("X-Requested-With", "XMLHttpRequest");
			c1.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			c1.setDoInput(true);
			c1.setDoOutput(true);
			PrintWriter out = new PrintWriter(c1.getOutputStream());
			out.print(params);
			out.flush();
			//判断验证码
			InputStream i45 = c1.getInputStream();
			Scanner s45 = new Scanner(i45, "utf-8");
			StringBuffer sb = new StringBuffer();
			while(s45.hasNext()){
				String scsc = s45.nextLine();
				sb.append(scsc);
			}
			String resp = sb.toString();
			JSONObject jsStr = JSONObject.parseObject(resp);
	        String message = StringUtil.decode(jsStr.getString("message"));
	        if(message.equals("success")){
	        	System.out.println("头条点赞success!");
	        	return true;
	        }else{
	        	System.out.println("头条点赞faile!");
	        	return false;
	        }
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("头条点赞faile!");
			return false;
		}

	}

}
