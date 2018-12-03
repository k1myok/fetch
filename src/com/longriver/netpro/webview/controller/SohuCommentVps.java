package com.longriver.netpro.webview.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
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

import com.alibaba.fastjson.JSON;
import com.longriver.netpro.common.sohu.SohuScript;
import com.longriver.netpro.common.sohu._FakeX509TrustManager;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

public class SohuCommentVps {
	private static Logger logger = Logger.getLogger(SohuCommentVps.class);
	
	public static void main(String args[]){
		TaskGuideBean b = new TaskGuideBean();
		b.setNick("lilei1929@163.com");
		b.setPassword("lilei419688");
//		b.setNick("Dongyuqi333@sohu.com"); //白名单账号
//		b.setPassword("dongyuqi123123");
		b.setCorpus("赞!!!");
		b.setPraiseWho("1356696689");
		b.setPraiseContent("又是“论文搞事情”");
		b.setAddress("http://news.sohu.com/20170807/n505713698.shtml");
		sendContent(b);//评论
//		sendPraise(b);//点赞
	}
	public static void sendContent(TaskGuideBean taskdo){
		Map<String,String> map = loginsohu(taskdo,"");
		String contents = taskdo.getCorpus();
		System.out.println("contents=="+contents);
		String dissurl = taskdo.getAddress();
		try {
			String content = "";
			String client_id = map.get("client_id");
			String topic_id = map.get("topic_id");
			String cookie = map.get("cookie");
			String newId = map.get("newId");
			String title = map.get("title");
			URL u5 = new URL("http://changyan.sohu.com/api/2/comment/submit");
			HttpURLConnection c5 = (HttpURLConnection) u5.openConnection();
			String params = "client_id=cyqemw6s1"
					+ "&topic_id="+topic_id 
					+ "&content=" + URLEncoder.encode(contents, "utf-8")
					+ "&reply_id=0"
					+ "&topic_title="+URLEncoder.encode(title, "utf-8")
					+ "&topic_url="+URLEncoder.encode(taskdo.getAddress(), "utf-8")
					+ "&attachment_urls=";
			//http://changyan.sohu.com/node/html?t=1442214163197&callback=fn&appid=cyqemw6s1&conf=prod_0266e33d3f546cb5436a10798e657d97&confstr=prod_0266e33d3f546cb5436a10798e657d97&client_id=cyqemw6s1&title=%25EF%25BF%25BD%25EF%25BF%25BD%25EF%25BF%25BD%25E3%2583%25A8%25EF%25BF%25BD%25E7%25BF%25A0%25E8%25A2%25B1%25EF%25BF%25BD%25EF%25BF%25BD-%25EF%25BF%25BD%25E2%2584%2583%25EF%25BF%25BD%25E5%25AF%25B8%25EF%25BF%25BD%25EF%25BF%25BD%25E7%2581%258F%25EF%25BF%25BD%25E6%25BF%25A1%25E8%25A7%2584%25EF%25BF%25BD%25E8%25B7%25BA%25EF%25BF%25BD%25E6%25BE%25B6%25D1%2585%25EF%25BF%25BD%25EF%25BF%25BD%25EF%25BF%25BD%25EF%25BF%25BD%25EF%25BF%25BD%25EF%25BF%25BD%2B%25E6%25B5%25A3%25EF%25BF%25BD%25EF%25BF%25BD%25EF%25BF%25BD%25E6%25B6%2593%25E6%258B%258C%25EF%25BF%25BD%25E5%25AF%25B8%25EF%25BF%25BD%25EF%25BF%25BD%25E6%259D%2588%25EF%25BD%2586%25EF%25BF%25BD%25D1%2584%25EF%25BF%25BD%25EF%25BF%25BD-%25E6%25BF%259E%25E5%258F%2598%25EF%25BF%25BD%25E6%25A3%25B0%25EF%25BF%25BD%25EF%25BF%25BD%25EF%25BF%25BD%25EF%25BF%25BD%25E5%2589%25A7%25EF%25BF%25BD%25EF%25BF%25BD%25E6%2590%25B4%25EF%25BF%25BD-%25E6%25BE%25B6%25D1%2586%25EF%25BF%25BD%25EF%25BF%25BD%25EF%25BF%25BD-%25EF%25BF%25BD%25EF%25BF%25BD%25EF%25BF%25BD%25EF%25BF%25BD&topicurl=http%253A%252F%252Fpinglun.sohu.com%252Fs9000682095.html&topicsid=9000682095.html&cbox=botBoxWrapper&customSohu=true&spSize=5&pageConf=%7B%22cbox%22%3A%22botBoxWrapper%22%2C%22customSohu%22%3Atrue%2C%22categoryId%22%3A%22%22%7D
			System.out.println("params=="+params);
//			//node/html?t=1442213602026&callback=fn&appid=cyqemw6s1&conf=prod_0266e33d3f546cb5436a10798e657d97&confstr=prod_0266e33d3f546cb5436a10798e657d97&client_id=cyqemw6s1&title=%25E6%2588%2591%25E6%259D%25A5%25E8%25AF%25B4%25E4%25B8%25A4%25E5%258F%25A5-%25E5%258D%25A1%25E6%2588%25B4%25E7%258F%258A%25E5%25B0%258F%25E5%25A6%25B9%25E6%2597%25B6%25E5%25B0%259A%25E5%25A4%25A7%25E7%2589%2587%25E6%259B%259D%25E5%2585%2589%2520%25E4%25BD%2593%25E6%2580%2581%25E4%25B8%25B0%25E8%2585%25B4%25E7%2583%25AD%25E8%25BE%25A3%25E6%2580%25A7%25E6%2584%259F-%25E5%25A8%25B1%25E4%25B9%2590%25E9%25A2%2591%25E9%2581%2593%25E5%259B%25BE%25E7%2589%2587%25E5%25BA%2593-%25E5%25A4%25A7%25E8%25A7%2586%25E9%2587%258E-%25E6%2590%259C%25E7%258B%2590&topicurl=http%253A%252F%252Fpinglun.sohu.com%252Fs9000682095.html&topicsid=9000682095&cbox=botBoxWrapper&customSohu=true&spSize=5&pageConf=%7B%22cbox%22%3A%22botBoxWrapper%22%2C%22customSohu%22%3Atrue%2C%22categoryId%22%3A%22%22%7D
			c5.setRequestProperty("Host","changyan.sohu.com");
			c5.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36");
			c5.setRequestProperty("Accept","application/json, text/javascript, */*; q=0.01");
			c5.setRequestProperty("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			c5.setRequestProperty("Accept-Encoding","gzip, deflate");
			c5.setRequestProperty("Cookie",cookie);
			//System.out.println(name + lastdomain + pprdig + ppinf + sceroute + ppmdig + spinfo + spsession);
			c5.setRequestProperty("Connection","keep-alive");
			c5.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			c5.setRequestProperty("Content-Length", String.valueOf(params.length()));
			if(dissurl.contains("auto")){
				c5.setRequestProperty("Referer","http://pinglun.auto.sohu.com/pinglun/"+client_id+"/"+newId);
				c5.setRequestProperty("Origin","http://pinglun.auto.sohu.com");
			}else{
				c5.setRequestProperty("Referer",taskdo.getAddress());
				c5.setRequestProperty("Origin","http://news.sohu.com");
			}
			
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
				System.out.println("f::"+f);
			}
			
			if(f.indexOf("\"error_code\":") > -1){
				content = "发表失败,请检查是否账号有问题";
				System.out.println("发表失败,请检查是否账号有问题");
			}else{
				content = "";
				System.out.println("发表成功!");
			}
			MQSender.toMQ(taskdo,content);
		} catch (Exception e) {
			e.printStackTrace();
			MQSender.toMQ(taskdo,"失败2");
		}
		
	}
	public static void sendPraise(TaskGuideBean taskdo){
		Map<String,String> map = loginsohu(taskdo,"");
		System.out.println("==============99999999999999999999999999==");
		String dissurl = taskdo.getAddress();
		try {
			String client_id = map.get("client_id");
			String topic_id = map.get("topic_id");
			String cookie = map.get("cookie");
			String commentId = taskdo.getPraiseWho();
			String uu = "http://changyan.sohu.com/api/2/comment/action?callback=jQuery1706646573783708611_"+new Date().getTime()+
					"&client_id="+client_id+
					"&topic_id="+topic_id+
					"&action_type=1"+
					"&comment_id="+commentId+
					"&_=1484100920851";
			//http://changyan.sohu.com/api/2/comment/action?callback=jQuery1708948873105788234_1488865142886&
			//client_id=cyqemw6s1&topic_id=398169975&action_type=1&comment_id=518893293&_=1488865173660
			System.out.println("uu=="+uu);
			URL fu = new URL(uu);
			HttpURLConnection fc = (HttpURLConnection) fu.openConnection();
			fc.addRequestProperty("Accept","*/*");
			fc.addRequestProperty("Accept-Language	","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			fc.addRequestProperty("Connection","keep-alive");
			fc.addRequestProperty("Host", "changyan.sohu.com");
			fc.addRequestProperty("Referer", dissurl);
			fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:49.0) Gecko/20100101 Firefox/49.0");
			fc.addRequestProperty("X-Requested-With", "XMLHttpRequest");
			fc.addRequestProperty("Cookie", cookie);
			fc.addRequestProperty("Connection", "keep-alive");
			fc.connect();
			
			InputStream i3 = fc.getInputStream();
			Scanner s3 = new Scanner(i3, "utf-8");
			while(s3.hasNext()){
				String scsc = s3.nextLine();
				System.out.println("scsc=="+scsc);
			}
			MQSender.toMQ(taskdo,"");
		} catch (Exception e) {
			e.printStackTrace();
			MQSender.toMQ(taskdo,"失败2");
		}
		
	}
	public static Map<String,String> loginsohu(TaskGuideBean taskdo,String code){
		Map<String,String> map = new HashMap<String,String>();
		try {
			String url = taskdo.getAddress();
			String newId = url.substring(url.lastIndexOf("/")+2,url.indexOf(".shtml"));
			Map<String,String> mapCookie = SohuCommentVps2.loginsohu(taskdo,"",1);
		String name = "";
		String lastdomain = "";
		String pprdig = "";
		String ppinf = "";
		String sceroute = "";
		String ppmdig = "";
		String client_id = "";
		String topic_id = "";
		String title = "";
		String oUrl = url;
		
		System.out.println("==============333333333333333333333==");
		
		if(url.indexOf("http://quan.") > -1){
			URL u = new URL(url);
			HttpURLConnection c = (HttpURLConnection)u.openConnection();
			c.connect();
			Scanner s = new Scanner(c.getInputStream(), "utf-8");
			while(s.hasNext()){
				String line = s.nextLine();
				if(line != null && line.indexOf("data-origin_url=\"") > -1){
					url = line.substring(line.indexOf("data-origin_url=\"")).replace("data-origin_url=\"", "");
					url = url.substring(0, url.indexOf("\""));
				}
			}
		}
		
		System.out.println("==============444444444444444444444==");
		URL u = new URL(url);
		int index = url.indexOf("://");
		int index2 = url.indexOf("com/");
		String host = url.substring(index+3, index2+3);
		System.out.println("host==="+host);
		HttpURLConnection c = (HttpURLConnection) u.openConnection();
		c.addRequestProperty("Host", host);
		c.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0");
		c.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		c.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		c.addRequestProperty("Referer", url);
		c.addRequestProperty("Connection", "keep-alive");
		c.addRequestProperty("Cache-Control", "max-age=0");
		c.connect();
		
		System.out.println("==============5555555555555555555555==");
		InputStream i = c.getInputStream();
		Scanner s = new Scanner(i, "gb2312");
		String tt = "";
		String appidTmp = "";
		while(s.hasNext()){
			String scsc = s.nextLine();
			if(scsc.toLowerCase().indexOf("<title>") > -1){
				title = scsc.substring(scsc.toLowerCase().indexOf("<title>"));
				title = scsc.substring(0, scsc.toLowerCase().indexOf("</title>"));
				title = title.replace("<title>", "").replace("<TITLE>", "");
				title = title.replace("</title>", "").replace("</TITLE>", "");
			}
			if(scsc.replaceAll(" ", "").contains("varappid=")){
				System.out.println("appid=="+scsc.trim());
				appidTmp = scsc.replaceAll(" ", "").replaceAll("varappid='", "").replaceAll("',", "");
			}
			if(scsc.indexOf("http://assets.changyan.sohu.com/upload/changyan.js") > -1){
				System.out.println(scsc);
				tt = scsc.substring(scsc.indexOf("http://assets.changyan.sohu.com/upload/changyan.js"));
				if(tt.indexOf("\"") > -1){
					tt = tt.substring(0, tt.indexOf("\""));
				}
				if(tt.indexOf("'") > -1){
					tt = tt.substring(0, tt.indexOf("'"));
				}
			}
			
		}
		System.out.println("==============666666666666666666666666==");
		String sid = newId;;
		url = "http://pinglun.sohu.com/s" + sid + ".html";
		System.out.println("url==="+url);
		URL u2 = new URL(url);
		HttpURLConnection c2 = (HttpURLConnection) u2.openConnection();
		c2.addRequestProperty("Host", "pinglun.sohu.com");
		c2.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0");
		c2.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		c2.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		c2.addRequestProperty("Referer", "http://news.sohu.com/");
		c2.addRequestProperty("Cookie", name + lastdomain + pprdig + ppinf + sceroute);
		c2.addRequestProperty("Connection", "keep-alive");
		c2.connect();
		c2.setInstanceFollowRedirects(false);
		Map<String, List<String>> m2 = c2.getHeaderFields();
		for(Map.Entry<String,List<String>> entry : m2.entrySet()){
			if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
				for(String value : entry.getValue()){
					if(value.indexOf("ppmdig=") > -1){
						ppmdig = value.substring(0, value.indexOf(";") + 1);
					}
				}
			}
		}
		System.out.println("==============77777777777777777777777777==");
		InputStream i2 = c2.getInputStream();
		Scanner s2 = new Scanner(i2, "gb2312");
		while(s2.hasNext()){
			String scsc = s2.nextLine();
			if(scsc.indexOf("http://quan.sohu.com") > -1){
				url = scsc.substring(scsc.indexOf("http://quan.sohu.com"));
				url = url.substring(0, url.indexOf("\""));
			}
		}
		System.out.println("url=="+url);
		URL u3 = new URL(url);
		HttpURLConnection c3 = (HttpURLConnection) u3.openConnection();
		c3.addRequestProperty("Host", "quan.sohu.com");
		c3.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0");
		c3.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		c3.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		c3.addRequestProperty("Referer", "http://news.sohu.com/");
		c3.addRequestProperty("Cookie", name + lastdomain + pprdig + ppinf + sceroute + ppmdig);
		c3.connect();

		InputStream i3 = c3.getInputStream();
		Scanner s3 = new Scanner(i3, "UTF-8");
		while(s3.hasNext()){
			String scsc = s3.nextLine();
			if(scsc.indexOf("http://assets.changyan.sohu.com/upload/changyan.js") > -1){
				url = scsc.substring(scsc.indexOf("http://assets.changyan.sohu.com/upload/changyan.js"));
				url = url.substring(0, url.indexOf("'"));
			}
			//System.out.println(scsc);
		}
		System.out.println("==============88888888888888888888888==");
		String conf = "";
		String appid = "";
		//System.out.println(conf);
		conf = tt.substring(tt.indexOf("conf=")).replace("conf=", "").trim();
		if(conf.indexOf("&") > -1){
			conf = conf.substring(0, conf.indexOf("&"));
		}
		System.out.println("tt=="+tt);
		if(tt.indexOf("appid=")>-1) appid = tt.substring(tt.indexOf("appid=")).replace("appid=", "").trim();
		else appid = appidTmp;
		if(appid.indexOf("&") > -1){
			appid = appid.substring(0, appid.indexOf("&"));
		}
		client_id = appid;
		//url = "http://changyan.sohu.com/node/html?t=" + new Date().getTime() + "&callback=fn&appid=" + appid + "&conf=" + conf + "&confstr=" + conf + "&client_id=" + appid + "&title=" + URLEncoder.encode(URLEncoder.encode(title, "utf-8"), "utf-8") + "&topicurl=" + URLEncoder.encode(URLEncoder.encode(oUrl, "utf-8"), "utf-8") + "&topicsid=" + sid;
		url = "http://changyan.sohu.com/api/3/topic/liteload?callback=jQuery17010014260769821703_"+ new Date().getTime()
		+"&client_id="+ appid+"&topic_url=" + URLEncoder.encode(oUrl, "utf-8")
		+"&topic_title=%E5%9C%88%E5%AD%90+-+" + URLEncoder.encode(title, "utf-8")
		+ "&page_size=20"
		+ "&hot_size=5"
		+ "&topic_source_id="+sid;
		//System.out.println(url);
		///node/html?t=1442213602026&callback=fn&appid=cyqemw6s1&conf=prod_0266e33d3f546cb5436a10798e657d97&confstr=prod_0266e33d3f546cb5436a10798e657d97&client_id=cyqemw6s1&title=%25E6%2588%2591%25E6%259D%25A5%25E8%25AF%25B4%25E4%25B8%25A4%25E5%258F%25A5-%25E5%258D%25A1%25E6%2588%25B4%25E7%258F%258A%25E5%25B0%258F%25E5%25A6%25B9%25E6%2597%25B6%25E5%25B0%259A%25E5%25A4%25A7%25E7%2589%2587%25E6%259B%259D%25E5%2585%2589%2520%25E4%25BD%2593%25E6%2580%2581%25E4%25B8%25B0%25E8%2585%25B4%25E7%2583%25AD%25E8%25BE%25A3%25E6%2580%25A7%25E6%2584%259F-%25E5%25A8%25B1%25E4%25B9%2590%25E9%25A2%2591%25E9%2581%2593%25E5%259B%25BE%25E7%2589%2587%25E5%25BA%2593-%25E5%25A4%25A7%25E8%25A7%2586%25E9%2587%258E-%25E6%2590%259C%25E7%258B%2590&topicurl=http%253A%252F%252Fpinglun.sohu.com%252Fs9000682095.html&topicsid=9000682095&cbox=botBoxWrapper&customSohu=true&spSize=5&pageConf=%7B%22cbox%22%3A%22botBoxWrapper%22%2C%22customSohu%22%3Atrue%2C%22categoryId%22%3A%22%22%7D
		URL u4 = new URL(url);
		HttpURLConnection c4 = (HttpURLConnection) u4.openConnection();
		c4.connect();
		InputStream i4 = c4.getInputStream();
		Scanner s4 = new Scanner(i4);
		while(s4.hasNext()){
			String scsc = s4.nextLine();
			//System.out.println(scsc);
			if(scsc.indexOf("topic_id") > -1){
				topic_id = scsc.substring(scsc.indexOf("topic_id")).replace("topic_id", "");
				if(topic_id.contains(",")){
					topic_id = topic_id.substring(0, topic_id.indexOf(","));
				}else
					topic_id = topic_id.substring(0, topic_id.indexOf("}"));
				
				topic_id = topic_id.replace("\"", "").replace(":", "").trim();
				if(topic_id.indexOf("}") > -1){
					topic_id = topic_id.replace("}", "").trim();
				}
			}
		}
		map.put("client_id", client_id);
		map.put("topic_id", topic_id);
		map.put("cookie", mapCookie.get("cookie"));
		map.put("newId", newId);
		map.put("title", title);
		System.out.println("map.get(cookie)=="+map.get("cookie"));
		
		} catch (Exception e) {
			MQSender.toMQ(taskdo,"失败");
			e.printStackTrace();
		}
		return map;
	}
	
	public String formatString(String str) {
        if (str != null) {            
        	try {                
        		str = new String(str.getBytes("ISO-8859-1"), "UTF-8");            
        		} catch (Exception e) {}        
        		}
        return str;    
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
}
