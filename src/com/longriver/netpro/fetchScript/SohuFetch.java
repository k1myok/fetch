package com.longriver.netpro.fetchScript;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.longriver.netpro.fetchScript.util.Jdbc2Mysql;
import com.longriver.netpro.fetchScript.util.Jdbc2MysqlBJWXB;
import com.longriver.netpro.fetchScript.util.Jdbc2MysqlNC;
import com.longriver.netpro.fetchScript.util.JdbcDistr;
import com.longriver.netpro.util.DateUtil;
import com.longriver.netpro.util.JsonHelper;
import com.longriver.netpro.webview.controller.InterfaceCJ;
import com.longriver.netpro.webview.entity.FetchTaskRiceverBean;

public class SohuFetch {
	
	public static void sohu(FetchTaskRiceverBean fetchTaskRicever) throws Exception{
//		String url = "http://quan.sohu.com/pinglun/cyqemw6s1/432274760";
		String url = fetchTaskRicever.getUrl();
		String name = "";
		String lastdomain = "";
		String pprdig = "";
		String ppinf = "";
		String sceroute = "";
		String ppmdig = "";
		String spinfo = "";
		String spsession = "";
		String client_id = "";
		String topic_id = "";
		String title = "";
		String oUrl = url;
	
		URL u = new URL(url);
		HttpURLConnection c = (HttpURLConnection) u.openConnection();
		c.addRequestProperty("Host", "quan.sohu.com");
		c.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0");
		c.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		c.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		c.addRequestProperty("Referer", "http://news.sohu.com/");
//		c.addRequestProperty("Cookie", name + lastdomain + pprdig + ppinf + sceroute + ppmdig);
		c.addRequestProperty("Connection", "keep-alive");
		c.addRequestProperty("Cache-Control", "max-age=0");
		c.connect();
		
		InputStream i = c.getInputStream();
		Scanner s = new Scanner(i, "gb2312");
		String tt = "";
		while(s.hasNext()){
			String scsc = s.nextLine();
			if(scsc.toLowerCase().indexOf("<title>") > -1){
				title = scsc.substring(scsc.toLowerCase().indexOf("<title>"));
				title = scsc.substring(0, scsc.toLowerCase().indexOf("</title>"));
				title = title.replace("<title>", "").replace("<TITLE>", "");
				title = title.replace("</title>", "").replace("</TITLE>", "");
			}
			if(scsc.indexOf("http://assets.changyan.sohu.com/upload/changyan.js") > -1){
				tt = scsc.substring(scsc.indexOf("http://assets.changyan.sohu.com/upload/changyan.js"));
				if(tt.indexOf("\"") > -1){
					tt = tt.substring(0, tt.indexOf("\""));
				}
				if(tt.indexOf("'") > -1){
					tt = tt.substring(0, tt.indexOf("'"));
				}
			}
			
		}

		String sid = url.substring(url.lastIndexOf("/") + 1);
		if(sid.indexOf(".shtml") > -1){
			sid = sid.substring(0, sid.indexOf(".shtml"));
		}
		if(sid.indexOf(".html") > -1){
			sid = sid.substring(0, sid.indexOf(".html"));
		}
		url = "http://pinglun.sohu.com/s" + sid + ".html";
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
		InputStream i2 = c2.getInputStream();
		Scanner s2 = new Scanner(i2, "gb2312");
		while(s2.hasNext()){
			String scsc = s2.nextLine();
			if(scsc.indexOf("http://quan.sohu.com") > -1){
				url = scsc.substring(scsc.indexOf("http://quan.sohu.com"));
				url = url.substring(0, url.indexOf("\""));
			}
		}
		String conf = "";
		String appid = "";
		//System.out.println(conf);
		conf = tt.substring(tt.indexOf("conf=")).replace("conf=", "").trim();
		if(conf.indexOf("&") > -1){
			conf = conf.substring(0, conf.indexOf("&"));
		}
		appid = tt.substring(tt.indexOf("appid=")).replace("appid=", "").trim();
		if(appid.indexOf("&") > -1){
			appid = appid.substring(0, appid.indexOf("&"));
		}
		client_id = appid;
		url = "http://changyan.sohu.com/node/html?t=" + new Date().getTime() + "&callback=fn&conf=" + conf + "&client_id=" + appid + "&title=" + URLEncoder.encode(URLEncoder.encode(title, "utf-8"), "utf-8") + "&topicurl=" + URLEncoder.encode(URLEncoder.encode(oUrl, "utf-8"), "utf-8") + "&topicsid=" + sid;
		///node/html?t=1442213602026&callback=fn&appid=cyqemw6s1&conf=prod_0266e33d3f546cb5436a10798e657d97&confstr=prod_0266e33d3f546cb5436a10798e657d97&client_id=cyqemw6s1&title=%25E6%2588%2591%25E6%259D%25A5%25E8%25AF%25B4%25E4%25B8%25A4%25E5%258F%25A5-%25E5%258D%25A1%25E6%2588%25B4%25E7%258F%258A%25E5%25B0%258F%25E5%25A6%25B9%25E6%2597%25B6%25E5%25B0%259A%25E5%25A4%25A7%25E7%2589%2587%25E6%259B%259D%25E5%2585%2589%2520%25E4%25BD%2593%25E6%2580%2581%25E4%25B8%25B0%25E8%2585%25B4%25E7%2583%25AD%25E8%25BE%25A3%25E6%2580%25A7%25E6%2584%259F-%25E5%25A8%25B1%25E4%25B9%2590%25E9%25A2%2591%25E9%2581%2593%25E5%259B%25BE%25E7%2589%2587%25E5%25BA%2593-%25E5%25A4%25A7%25E8%25A7%2586%25E9%2587%258E-%25E6%2590%259C%25E7%258B%2590&topicurl=http%253A%252F%252Fpinglun.sohu.com%252Fs9000682095.html&topicsid=9000682095&cbox=botBoxWrapper&customSohu=true&spSize=5&pageConf=%7B%22cbox%22%3A%22botBoxWrapper%22%2C%22customSohu%22%3Atrue%2C%22categoryId%22%3A%22%22%7D
		URL u4 = new URL(url);
		HttpURLConnection c4 = (HttpURLConnection) u4.openConnection();
		c4.connect();
		InputStream i4 = c4.getInputStream();
		Scanner s4 = new Scanner(i4);
		while(s4.hasNext()){
			String scsc = s4.nextLine();
			if(scsc.indexOf("topic_id") > -1){
				topic_id = scsc.substring(scsc.indexOf("topic_id")).replace("topic_id", "");
				topic_id = topic_id.substring(0, topic_id.indexOf(","));
				topic_id = topic_id.replace("\"", "").replace(":", "").trim();
				if(topic_id.indexOf("}") > -1){
					topic_id = topic_id.replace("}", "").trim();
				}
			}
		}
		
		URL uf1 = new URL("http://changyan.sohu.com/api/2/topic/comments?callback=fn&client_id=cyqemw6s1&topic_id="+topic_id+"&page_size=20&page_no=1&style=floor&inside_floor=3&outside_floor=2&_=" + new Date().getTime());
	    HttpURLConnection cf1 = (HttpURLConnection) uf1.openConnection();
	    cf1.addRequestProperty("Host", "changyan.sohu.com");
	    cf1.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:40.0) Gecko/20100101 Firefox/40.0");
	    cf1.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		//cf.addRequestProperty("Cookie", cookie);
	    cf1.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
	    cf1.addRequestProperty("Connection", "keep-alive");
	    cf1.addRequestProperty("X-Requested-With", "XMLHttpRequest");
	    cf1.connect();
		 
	    String pageNO = "";
		InputStream iff1 = cf1.getInputStream();
		Scanner sf1 = new Scanner(iff1);
		//String mid = "";
		
		while(sf1.hasNext()){
			String scsc = sf1.nextLine();
			if(scsc.indexOf("cmt_sum") > -1){
				pageNO = scsc.substring(scsc.indexOf("cmt_sum")).replace("cmt_sum", "");
				pageNO = pageNO.substring(0, pageNO.indexOf(","));
				pageNO = pageNO.replace("\"", "").replace(":", "").trim();
				if(pageNO.indexOf("}") > -1){
					pageNO = pageNO.replace("}", "").trim();
				}
			}
		}
		
		
		int pagesize = 20;
		int pageNum = Integer.parseInt(pageNO)/20;
		if(pageNum==0) pageNum = 1;
		StringBuffer ct = new StringBuffer();
		for(int j=1;j<=pageNum;j++){
			
	    URL uf = new URL("http://changyan.sohu.com/api/2/topic/comments?callback=fn&client_id=cyqemw6s1&topic_id="+topic_id+"&page_size="+pagesize+"&page_no="+j+"&style=floor&inside_floor=3&outside_floor=2&_=" + new Date().getTime());
	    HttpURLConnection cf = (HttpURLConnection) uf.openConnection();
		
        cf.addRequestProperty("Host", "changyan.sohu.com");
        cf.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:40.0) Gecko/20100101 Firefox/40.0");
		cf.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		//cf.addRequestProperty("Cookie", cookie);
		cf.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		cf.addRequestProperty("Connection", "keep-alive");
		cf.addRequestProperty("X-Requested-With", "XMLHttpRequest");
		cf.connect();
		 
		BufferedReader sf = new BufferedReader(new InputStreamReader(cf.getInputStream(),"utf-8"));
		
		//String mid = "";
		String temp = "";
		while(( temp = sf.readLine())!=null){
			String scsc =temp;
			ct.append(scsc);
		}
		if(j==1){
			JdbcDistr.hasInfo(fetchTaskRicever);
		}
		toObj(ct.toString(),fetchTaskRicever);
		ct = ct.delete(0,ct.length());;
	}
		
	}
	public static void toObj(String testTemp,FetchTaskRiceverBean b){
		try{
			String  test = testTemp;
			String  s = "content";
			 int count = 0;
		        for(int i=0; i<test.length();){  
		            int c = -1;  
		            c = test.indexOf(s);
		            if(c != -1){
		            	test = test.substring(c+1);  
		                count ++;
		            }else {  
		                //i++;  
		                System.out.println("----");  
		                break;  
		            }  
		        }  
		        System.out.println("count====="+count);  
		        
				String ta = testTemp.substring(testTemp.indexOf(s));
				List<FetchTaskRiceverBean> list = new ArrayList<FetchTaskRiceverBean>();
				List<Map> mapList = new ArrayList<Map>();
				for(int i=0;i<count;i++){
					ta = ta.substring(ta.indexOf(s));
					String content = ta.substring(ta.indexOf(s)+10, ta.indexOf("\","));
					ta = ta.substring(ta.indexOf("create_time"));
					String create_time = DateUtil.long2Date2(ta.substring(ta.indexOf("create_time")+13, ta.indexOf(",")));
					ta = ta.substring(ta.indexOf("nickname"));
					String nickname = ta.substring(ta.indexOf("nickname")+10, ta.indexOf(","));
					
					System.out.println("content=="+content+" create_time=="+create_time+" nickname=="+nickname);
					
					FetchTaskRiceverBean d = new FetchTaskRiceverBean();
		    		d.setAn(nickname.replaceAll("\"", "").trim());
		    		d.setContent(content.replaceAll("\"", "").trim());
		    		d.setPt(create_time.trim());
		    		list.add(d);
		    		
		    		Map map = new HashMap();
		    		map.put("urlid", b.getId());
		    		map.put("content", content.replaceAll("\"", "").trim());
		    		map.put("postTime", create_time.trim());
		    		mapList.add(map);
		    		
				}
				JdbcDistr.distr(b,mapList,list);
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String agrs[]){
		FetchTaskRiceverBean b=new FetchTaskRiceverBean();
		b.setUrl("http://quan.sohu.com/pinglun/cyqemw6s1/434468658");
		try {
			sohu(b);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
