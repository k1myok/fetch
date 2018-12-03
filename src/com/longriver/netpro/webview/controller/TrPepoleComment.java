package com.longriver.netpro.webview.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.common.sina.SData;
import com.longriver.netpro.common.sohu.PepoleScript;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

public class TrPepoleComment {
	
	public static void main(String arsg[]){
		try{
			TaskGuideBean b = new TaskGuideBean();
			b.setNick("15652240394");
			b.setPassword("lilei419688");
//			b.setNick("wolffirm@163.com");
//			b.setPassword("wangmeng121");
			b.setAddress("http://travel.people.com.cn/n1/2017/1122/c41570-29660633.html");
			b.setAddress("http://bbs1.people.com.cn/post/129/1/2/165323312.html");
			b.setAddress("http://travel.people.com.cn/n1/2017/1122/c41570-29660571.html");
			b.setCorpus("不错不错哦");
			toRun(b);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public static void toRun(TaskGuideBean taskdo){
		String cookie = login(taskdo);
		String content = "";
		if(cookie.equals("")){
			content = "登录失败";
		}else{
			Map<String,String> map = getParams(taskdo);
			String result = sendContent(cookie,taskdo,map);
			if(!result.equals("success")){
				content = "发帖失败";
			}else{
				System.out.println("发帖成功");
			}
		}
		MQSender.toMQ(taskdo,content);
	}
	public static String sendContent(String cookie,TaskGuideBean b,Map<String,String> map){
		String r = "";
		try {
			URL fu = new URL("http://bbs1.people.com.cn/postAction.do?callback=jQuery171013593349940217592_"+new Date().getTime());
			HttpURLConnection fc = (HttpURLConnection) fu.openConnection();
			String fp = "parentId="+map.get("parentId")+"&bid="+map.get("bid")+
				"&titleText="+URLEncoder.encode(b.getCorpus(), "utf-8")+
				"&pageNo=0&view=true" +
				"&message="+URLEncoder.encode(b.getCorpus(), "utf-8");
			System.out.println(fp);
			fc.addRequestProperty("Host", "bbs1.people.com.cn");
			fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; W…) Gecko/20100101 Firefox/57.0");
			fc.addRequestProperty("Accept", "text/javascript, application/j…ion/x-ecmascript, */*; q=0.01");
			fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
			fc.addRequestProperty("Referer", "http://bbs1.people.com.cn/post/129/1/2/165323309.html");
			fc.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			fc.addRequestProperty("Content-Length", String.valueOf(fp.length()));
			fc.addRequestProperty("Cookie", cookie);
			
			fc.setInstanceFollowRedirects(false);
			fc.setDoInput(true);
			fc.setDoOutput(true);
			PrintWriter fo = new PrintWriter(fc.getOutputStream());
			fo.print(fp);
			fo.flush();
			InputStream fci = fc.getInputStream();
			Scanner fsc = new Scanner(fci);
			while(fsc.hasNext()){
				String scsc2 = fsc.nextLine();
				System.out.println("scsc3="+scsc2);
				if(scsc2.contains("true")) r = "success";
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return r;
	}
	public static Map<String,String> getParams(TaskGuideBean taskdo){
		String bid = "";
		String parentId = "";
		Map<String,String> map = new HashMap<String,String>();
		try {
			String address = taskdo.getAddress();
			if(address.contains("bbs1")){
				int ll = address.lastIndexOf(".htm");
				address = address.substring(0, ll);
				String ss[] = address.split("/");
				bid = ss[2];
				parentId = ss[ss.length-1];
			}else{
				int li = address.lastIndexOf("-")+1;
				int ll = address.lastIndexOf(".htm");
				String nid = address.substring(li, ll);
				System.out.println("nid="+nid);
				URL u1 = new URL("http://bbs1.people.com.cn/postLink.do?nid="+nid);
				HttpURLConnection c1 = (HttpURLConnection) u1.openConnection();
				c1.addRequestProperty("Host", "bbs1.people.com.cn");
				c1.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
				c1.addRequestProperty("Accept", "*/*");
				c1.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
				c1.setRequestMethod("GET");
				c1.connect();
				InputStream fci = c1.getInputStream();
				Scanner fsc = new Scanner(fci);
				while(fsc.hasNext()){
					String scsc2 = fsc.nextLine();
//					System.out.println(scsc2);
					if(scsc2.contains("catalogs")){
						int f1 = scsc2.indexOf("_")+1;
						int f2 = scsc2.lastIndexOf("\"");
						bid = scsc2.substring(f1, f2);
					}
					if(scsc2.contains("contentid")){
						int f1 = scsc2.indexOf("content=")+9;
						int f2 = scsc2.lastIndexOf("\"");
						parentId = scsc2.substring(f1, f2);
					}
					if(!bid.equals("") && !parentId.equals("")){
						break;
					}
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("bid="+bid);
		System.out.println("parentId="+parentId);
		map.put("bid", bid);
		map.put("parentId", parentId);
		return map;
	}
	public static String login(TaskGuideBean taskdo){
		String cookie = "";
		SData data = new SData();
		try {
			String userId =taskdo.getNick();
			String pwd = taskdo.getPassword();
			String contents = taskdo.getCorpus();
			String sUrl = taskdo.getAddress();
			
			data.setString("user_account_id",userId);
			data.setString("user_account_pw",pwd);
			data.setString("mission_addr",sUrl);
			data.setString("mission_contents",contents);
			 //获得token
			URL u1 = new URL("http://sso.people.com.cn/getRandomCode?userName="+URLEncoder.encode(userId, "utf-8")+"&_r=0.20134093472734094&callback=rmwsso_getscript_callback_338654");
			HttpURLConnection c1 = (HttpURLConnection) u1.openConnection();
			c1.addRequestProperty("Host", "sso.people.com.cn");
			c1.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
			c1.addRequestProperty("Accept", "*/*");
			c1.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
			c1.addRequestProperty("Referer", sUrl);
			c1.connect();
			InputStream i2 = c1.getInputStream();
			Scanner s2 = new Scanner(i2, "gb2312");
			String token = "";
			String s = "";
			String c = "";
			while(s2.hasNext()){
				String scsc = s2.nextLine();
				System.out.println("scsc=="+scsc);
				if(scsc.indexOf("\"t\":") > -1){
					token = scsc.substring(scsc.indexOf("\"t\":")).replace("\"t\":", "").trim();
					token = token.substring(1, token.indexOf(",")).replace("\"", "");
				}
				if(scsc.indexOf("\"s\":") > -1){
					s = scsc.substring(scsc.indexOf("\"s\":")).replace("\"s\":", "").trim();
					s = s.substring(1, s.indexOf(",")).replace("\"", "");
				}
				if(scsc.indexOf("\"c\":") > -1){
					c = scsc.substring(scsc.indexOf("\"c\":")).replace("\"c\":", "").trim();
					c = c.substring(1, c.indexOf("}")).replace("\"", "");
				}
			}
			System.out.println("token=="+token);
			System.out.println("c=="+c);
			System.out.println("s=="+s);
			//登录
			String threadId = sUrl.substring(sUrl.lastIndexOf("/")).replace("/", "").replace(".html", "").replace(".shtml", "").replace(".htm", "");
			threadId = threadId.substring(threadId.indexOf("-")+1);
			URL fu = new URL("http://sso.people.com.cn/login");
			HttpURLConnection fc = (HttpURLConnection) fu.openConnection();
			String fp = "loginName="+ URLEncoder.encode(userId, "utf-8")+"&password="+PepoleScript.getHexMd5(PepoleScript.getHexMd5(PepoleScript.getHexMd5(pwd)+s)+c)+"&token="+token+"&appCode=&remember=1";
					
			fc.addRequestProperty("Host", "sso.people.com.cn");
			fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
			fc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			fc.addRequestProperty("Referer", sUrl);
			fc.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
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
			System.out.println("cookie1="+cookie);
			InputStream fci = fc.getInputStream();
			Scanner fsc = new Scanner(fci);
			while(fsc.hasNext()){
				String scsc2 = fsc.nextLine();
				int ff = scsc2.indexOf("value = '");
				if(ff>-1){
					scsc2 = scsc2.substring(ff+9, scsc2.length()-2);
					System.out.println(scsc2);
					JSONObject jb = JSONObject.parseObject(scsc2);
					if(jb.getString("result").equals("success")){
						JSONObject jb2 = jb.getJSONObject("responseObj");
						cookie += "sid="+jb2.getString("sid")+";";
						cookie += "sso_u="+jb2.getString("sso_u")+";";
						cookie += "sso_l="+jb2.getString("sso_l")+";";
						cookie += "sso_s="+jb2.getString("sso_s")+";";
					}else{
						cookie = "";
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("cookie="+cookie);
		return cookie;
	}
}
