package com.longriver.netpro.webview.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.common.sohu.SohuScript;
import com.longriver.netpro.common.sohu._FakeX509TrustManager;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

public class SohuCommentVps2 {
	private static Logger logger = Logger.getLogger(SohuCommentVps2.class);
	
	public static void main(String args[]){
		TaskGuideBean b = new TaskGuideBean();
		b.setNick("lilei1929@163.com");
		b.setPassword("lilei419688");
//		b.setNick("Dongyuqi333@sohu.com"); //白名单账号
//		b.setPassword("dongyuqi123123");
		b.setCorpus("不可信");
		b.setAddress("http://www.sohu.com/a/205544693_596922?_f=index_chan29news_2");
//		b.setCorpus("新时代来临了,哈哈");
//		b.setPraiseWho("1373623765");
//		b.setPraiseContent("求嫖客的心理阴影面积");
//		b.setAddress("http://www.sohu.com/a/143903092_119562");
		b.setCorpus("不错不错");
//		b.setAddress("http://www.sohu.com/a/224609614_115423?g=0?code=5bdf468b2c5e2f9b6d8537945c85bb4a&_f=index_cpc_5_0");
		b.setAddress("http://news.sohu.com/20170807/n505713698.shtml");
//		b.setCorpus("新时代来临了,哈哈");
//		b.setPraiseWho("1373623765");
//		b.setPraiseContent("求嫖客的心理阴影面积");
//		b.setAddress("http://www.sohu.com/a/143903092_119562");
		sendContent(b);//评论
//		sendPraise(b);//点赞
	}
	public static void sendContent(TaskGuideBean taskdo){
		String dissurl = taskdo.getAddress();
		Map<String,String> map = loginsohu(taskdo,"",1);
		System.out.println("cookie=="+map.get("cookie"));
		Map<String,String> mapparams = getCommentParams(taskdo);
		if(map.get("cookie")==null || map.get("cookie").equals("")){
			MQSender.toMQ(taskdo,"登录失败");
			return ;
		}
		String contents = taskdo.getCorpus();
		try {
			String media_id = mapparams.get("media_id");
			String topic_id = mapparams.get("topic_id");
			String topic_title = mapparams.get("title");
			String cookie = map.get("cookie");
			
			URL u5 = new URL("http://apiv2.sohu.com/api/comment/submit");
			HttpURLConnection c5 = (HttpURLConnection) u5.openConnection();
			String params = "media_id=" + media_id
					+ "&topic_id="+topic_id 
					+ "&content=" + URLEncoder.encode(contents, "utf-8")
					+ "&reply_id=0"
					+ "&topic_title="+URLEncoder.encode(topic_title, "utf-8")
					+ "&topic_url="+URLEncoder.encode(dissurl, "utf-8")
					+ "&attachment_urls=";
			System.out.println("params:"+params);
			c5.setRequestProperty("Accept","*/*");
			c5.setRequestProperty("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
			c5.setRequestProperty("Connection","keep-alive");
			c5.setRequestProperty("Content-Length", String.valueOf(params.length()));
			c5.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
			c5.setRequestProperty("Cookie",cookie);
			c5.setRequestProperty("Host","apiv2.sohu.com");
			c5.setRequestProperty("Origin","http://www.sohu.com");
			c5.setRequestProperty("Referer",dissurl);
			c5.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:50.0) Gecko/20100101 Firefox/50.0");
			
			c5.setDoOutput(true);
			c5.setDoInput(true);
			
			PrintWriter out = new PrintWriter(c5.getOutputStream());
			out.print(params);
			out.flush();
			
			InputStream i5 = c5.getInputStream();
			Scanner s5 = new Scanner(i5);
			String f = "";
			while(s5.hasNext()){
				f = s5.nextLine();
			}
			System.out.println("发布返回信息:"+f);
			JSONObject jb = JSONObject.parseObject(f);
			JSONObject jbb = jb.getJSONObject("jsonObject");
			Integer error_code = 0;
			if(jbb!=null) error_code = jbb.getIntValue("error_code");
			Integer code = jb.getInteger("code");
			String content = "发表失败";
			if(code.equals(200) && !error_code.equals(10225)){
				content = "";
				System.out.println("发表成功!");
			}else if(code.equals(200) && error_code.equals(10225)){
				System.out.println("需要手机号验证!");
				content = "手机号验证";
			}
			MQSender.toMQ(taskdo,content);
		} catch (Exception e) {
			MQSender.toMQ(taskdo,"报错失败!");
			e.printStackTrace();
		}
		
	}
	public static void sendPraise(TaskGuideBean taskdo){
		String dissurl = taskdo.getAddress();
		Map<String,String> map = loginsohu(taskdo,"",1);
		Map<String,String> mapparams = getCommentParams(taskdo);
		System.out.println("cookie=="+map.get("cookie"));
		if(map.get("cookie")==null || map.get("cookie").equals("")){
			MQSender.toMQ(taskdo,"登录失败!");
			return ;
		}
		try {
			String topic_id = mapparams.get("topic_id");
			String cookie = map.get("cookie");
			//http://apiv2.sohu.com/api/comment/support?callback=jQuery1124007000594696286899_1495084830338&topic_id=3098725251&comment_id=1367298305&_=1495084830388
			String url = "http://apiv2.sohu.com/api/comment/support?callback=jQuery1124007000594696286899_"+new Date().getTime()+
						"&topic_id="+topic_id+
						"&comment_id="+taskdo.getPraiseWho()+
						"&_="+new Date().getTime();
			System.out.println("url=="+url);
			URL u5 = new URL(url);
			HttpURLConnection c5 = (HttpURLConnection) u5.openConnection();
			c5.setRequestProperty("Accept","*/*");
			c5.setRequestProperty("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
			c5.setRequestProperty("Connection","keep-alive");
			c5.setRequestProperty("Cookie",cookie);
			c5.setRequestProperty("Host","apiv2.sohu.com");
			c5.setRequestProperty("Referer",dissurl);
			c5.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:50.0) Gecko/20100101 Firefox/50.0");
			c5.connect();
			
			InputStream i5 = c5.getInputStream();
			Scanner s5 = new Scanner(i5);
			String f = "";
			while(s5.hasNext()){
				f = s5.nextLine();
			}
			System.out.println("发布返回信息:"+f);
			String content = "发表失败";
			if(f.contains("SUCC")){
				content = "";
				System.out.println("发表成功!");
			}
			MQSender.toMQ(taskdo,content);
		} catch (Exception e) {
			MQSender.toMQ(taskdo,"报错失败!");
			e.printStackTrace();
		}
		
	}
	//发帖需要topic_id,media_id,topic_title 获得topic_id需要 topic_source_id
	public static Map<String,String> getCommentParams(TaskGuideBean taskdo){
		String url = taskdo.getAddress();
		Map<String,String> map = new HashMap<String,String>();
		try {
			Document doc =Jsoup.connect(url).get();
			String tmp = doc.toString();
			tmp = tmp.substring(tmp.indexOf("window.sohu_mp.article("));
			int first = tmp.indexOf("({");
			int last = tmp.indexOf("});");
			tmp = tmp.substring(first+1, last+1);
			System.out.println("tmp=="+tmp);
			JSONObject jb = JSONObject.parseObject(tmp);
			map.put("news_id", jb.getString("news_id"));
			map.put("cms_id", jb.getString("cms_id"));
			map.put("media_id", jb.getString("media_id"));
			map.put("title", jb.getString("title"));
			
			//获得topic_id
			String url_topic = "http://changyan.sohu.com/api/2/topic/count?client_id=cyqemw6s1&topic_source_id="+jb.getString("cms_id")+"&callback=jQuery112407337340160700102_"+new Date().getTime()+"&_="+new Date().getTime();
			URL up33 = new URL(url_topic);
			HttpURLConnection c31 = (HttpURLConnection) up33.openConnection();
			c31.addRequestProperty("Host", "changyan.sohu.com");
			c31.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0");
			c31.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			c31.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
			c31.addRequestProperty("Referer", url);
			c31.connect();
			InputStream i11 = c31.getInputStream();
			Scanner s11 = new Scanner(i11, "gb2312");
			String scsc = "";
			while(s11.hasNext()){
				scsc += s11.nextLine();
				System.out.println("scsc=="+scsc);
			}
			int jsons = scsc.lastIndexOf("{");
			int jsone = scsc.indexOf("}");
			scsc = scsc.substring(jsons, jsone+1);
			JSONObject jb_topic = JSONObject.parseObject(scsc);
			map.put("topic_id", jb_topic.getString("id"));
			System.out.println("topic_id=="+jb_topic.getString("id"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	//账号登录
	public static Map<String,String> loginsohu(TaskGuideBean taskdo,String code,int number){
		Map<String,String> map = new HashMap<String,String>();
		try {
			String userId =taskdo.getNick();
			String pwd = taskdo.getPassword();
			
			SohuScript script = new SohuScript();
			String e = script.getHexMd5(pwd);
//			_FakeX509TrustManager.allowAllSSL();
			String params = "domain=sohu.com&passport200005491380987193628_cb"+new Date().getTime()+
				"&appid=1019&userid="+URLEncoder.encode(userId, "utf-8")+"&password="+e+"&persistentcookie=0";
			if(!code.equals(""))params += "&captcha="+code;
			System.out.println("params::"+params);
			URL upT = new URL("http://passport.sohu.com/apiv2/login");
			HttpURLConnection cpT = (HttpURLConnection) upT.openConnection();
			cpT.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			cpT.addRequestProperty("Accept-Encoding", "gzip, deflate");
			cpT.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			cpT.addRequestProperty("Connection", "keep-alive");
			cpT.addRequestProperty("Host", "passport.sohu.com");
			cpT.addRequestProperty("Referer", taskdo.getAddress());
			cpT.addRequestProperty("Cookie", "SUV=1505120953269963; vjuids=ab4fea5.14d556a3ec6.0.46b3e27278e9d8; vjlast=1431657136.1495070164.11; sohutag=8HsmeSc5NSwmcyc5NSwmYjc5NzcsJ2EmOiN0LCdnJzowLCdmJzowLCduJzo4OSwmaSc5Njwmdyc5NywmaCc5NSwmYyc5NCwmZSc5NCwmbSc5NywmdCc5NH0; scrnSize=1600*900; ppnewsinfo=1019|MjY2MjI3OTQxN0BzaW5hLnNvaHUuY29t||http://img3.pp.sohu.com/ppp/blog/images/common/nobody.gif; __utma=1.1743741362.1434682073.1434682073.1434682073.1; __utmv=1.lilei1929%40163.com; gn12=w:1; sci12=w:1; shenhui12=w:1; gj12=w:1; IPLOC=CN1100; networkmp_del=check:1; mut=zz.go.hsts.smuid; hide_ad=0; _smuid_type=2; v=3; _smuid=JJET5Mi0Vp7hB0I9aaf0Z8; nickname=%E9%9F%B5%E8%8C%B9; beans_dmp_done=1; lastdomain=1496647780|Z2k3OTA5N0Bzb2h1LmNvbXw|sohu.com|1");
			cpT.addRequestProperty("Upgrade-Insecure-Requests", "1");
			cpT.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:50.0) Gecko/20100101 Firefox/50.0");
			cpT.setDoOutput(true);
			cpT.setDoInput(true);
			
			PrintWriter out = new PrintWriter(cpT.getOutputStream());
			out.print(params);
			out.flush();
			
			InputStream i5 = cpT.getInputStream();
			Scanner s5 = new Scanner(i5,"utf-8");
			String f = "";
			while(s5.hasNext()){
				f = s5.nextLine();
				System.out.println("f::"+f);
				if(number<4){
					if(f.contains("code\":6") || f.contains("code\":7")){//需要验证码
						String randRom = getCode(userId);
						number++;
						Map<String,String> mm = loginsohu(taskdo,randRom,number);
						return mm;
					}
				}
			}
			Map<String, List<String>> m1 = cpT.getHeaderFields();
			System.out.println("m1.size()::"+m1.size());
			for(Map.Entry<String,List<String>> entry : m1.entrySet()){
				System.out.println("entry.getKey()::"+entry.getKey());
				if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
					String cookie = "";
					for(String value : entry.getValue()){
						System.out.println("value::"+value);
						if(value.indexOf("spsession=") > -1){
							cookie += value.substring(0, value.indexOf(";") + 1);
						}
						if(value.indexOf("lastdomain=") > -1){
							cookie += value.substring(0, value.indexOf(";") + 1);
						}
						if(value.indexOf("pprdig=") > -1){
							cookie += value.substring(0, value.indexOf(";") + 1);
						}
						if(value.indexOf("ppinf=") > -1){
							cookie += value.substring(0, value.indexOf(";") + 1);
						}
						if(value.indexOf("spinfo=") > -1){
							cookie += value.substring(0, value.indexOf(";") + 1);
						}
					}
					System.out.println("cookie::"+cookie);
					map.put("cookie", cookie);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static String getCode(String userid){
		String randRom="";
		try {
			URL u211 = new URL("http://passport.sohu.com/apiv2/picture_captcha?userid="+userid+"&t="+new Date().getTime());
			HttpURLConnection c211 = (HttpURLConnection) u211.openConnection();
			c211.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			c211.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			c211.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			c211.connect();
			InputStream i211 = c211.getInputStream();
			byte[] bs = new byte[1024];  
			int len;  
			String imgPath = "c:\\sohucode.jpg";
			String fPath = "c:\\";
			File sf=new File(fPath);  
			if(!sf.exists()){  
				sf.mkdirs();  
			}  
			OutputStream os = new FileOutputStream(imgPath);  
			while ((len = i211.read(bs)) != -1) {  
				os.write(bs, 0, len);  
			}  
			os.close();  
			i211.close();
			randRom = RuoKuai.createByPostNew("3040",imgPath);
			System.out.println("result========================"+randRom);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return randRom;
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
