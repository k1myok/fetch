package com.longriver.netpro.fetchScript;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import com.longriver.netpro.fetchScript.util.Jdbc2Mysql;
import com.longriver.netpro.fetchScript.util.Jdbc2MysqlBJWXB;
import com.longriver.netpro.fetchScript.util.Jdbc2MysqlNC;
import com.longriver.netpro.fetchScript.util.JdbcDistr;
import com.longriver.netpro.webview.controller.InterfaceCJ;
import com.longriver.netpro.webview.entity.FetchTaskRiceverBean;



public class SinaNewsFetch {
	
	
	public static void sina(FetchTaskRiceverBean fetchTaskRicever) throws Exception{
//		String url = "http://comment5.news.sina.com.cn/comment/skin/default.html?channel=gn&newsid=comos-fxnkkuv4404956";
		String url = fetchTaskRicever.getUrl();
		if(url.indexOf("#")>-1){
			url = url.substring(0,url.indexOf("#"));
		}
		String channel ="";
		String key = "";
		String newsid = "";
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
					channel = scsc.substring(scsc.indexOf("channel:'")).replace("channel:'", "");
					channel = channel.substring(0, channel.indexOf("'"));
							
				}
			}
		}else{
			//http://comment5.news.sina.com.cn/comment/skin/default.html?channel=gn&newsid=1-1-31309107&style=0 

			key = url.substring(url.indexOf("newsid=")).replace("newsid=", "");
			if(key.indexOf("&") > -1){
				key = key.substring(0, key.indexOf("&")).replace("&", "");
			}
			channel = url.substring(url.indexOf("channel=")).replace("channel=", "");
			if(channel.indexOf("&") > -1){
				channel = channel.substring(0, channel.indexOf("&")).replace("&", "");
			}
		}
		newsid = key;
		URL uf1 = new URL("http://comment5.news.sina.com.cn/page/info?format=js&channel="+channel+"&newsid="+newsid+"&group=&compress=0&ie=utf-8&oe=utf-8&page=1&page_size=20");
	    HttpURLConnection cf1 = (HttpURLConnection) uf1.openConnection();
	   
	    cf1.connect();
		 
	    String pageNO = "";
		InputStream iff1 = cf1.getInputStream();
		Scanner sf1 = new Scanner(iff1);
		//String mid = "";
		
		while(sf1.hasNext()){
			String scsc = sf1.nextLine();
			if(scsc.indexOf("show") > -1){
				pageNO = scsc.substring(scsc.indexOf("show")).replace("show", "");
				pageNO = pageNO.substring(0, pageNO.indexOf(","));
				pageNO = pageNO.replace("\"", "").replace(":", "").trim();
				if(pageNO.indexOf("}") > -1){
					pageNO = pageNO.replace("}", "").trim();
				}
			}
		}
		System.out.println(pageNO);
		int pagesize = 20;
		int pageNum = Integer.parseInt(pageNO)/20+1;
		StringBuffer ct = new StringBuffer();
		for(int j=1;j<=pageNum;j++){
			URL uf = new URL("http://comment5.news.sina.com.cn/page/info?format=js&channel="+channel+"&newsid="+newsid+"&group=&compress=0&ie=utf-8&oe=utf-8&page="+j+"&page_size="+pagesize);
			 System.out.println("http://comment5.news.sina.com.cn/page/info?format=js&channel="+channel+"&newsid="+newsid+"&group=&compress=0&ie=utf-8&oe=utf-8&page="+j+"&page_size="+pagesize);  
			HttpURLConnection cf= (HttpURLConnection) uf.openConnection();
		   
		    cf.connect();
			
			InputStream iff = cf.getInputStream();
			Scanner sf = new Scanner(iff);
			//String mid = "";
			String cmntList = "";
			while(sf.hasNext()){
				String scsc = sf.nextLine();
				ct.append(scsc);

			}
			cmntList = ct.toString().substring(ct.toString().indexOf("\"cmntlist\":"));
			cmntList = cmntList.substring(0,cmntList.indexOf("}]"));

			if(j==1){
				JdbcDistr.hasInfo(fetchTaskRicever);
			}
			toObj(cmntList,fetchTaskRicever);
			ct = ct.delete(0,ct.length());;
		}
		
		
	}
	public static void toObj(String testTemp,FetchTaskRiceverBean b){
		try{
			String  test = testTemp;
			String  s = "status";
			 int count = 0; 
		        for(int i=0; i<test.length();){  
		            int c = -1;  
		            c = test.indexOf(s);   
		            if(c != -1){
		            	test = test.substring(c+1);  
		                count ++;
		            }  
		            else {
		                System.out.println("---------------");  
		                break;  
		            }  
		        }  
		        String tt = testTemp;
				//String cookie = WeiboSina.getCookie(userId,pwd);
		        List<FetchTaskRiceverBean> list = new ArrayList<FetchTaskRiceverBean>();
		        List<Map> mapList = new ArrayList<Map>();
				for(int i=0;i<count;i++){
					String ta_nick = tt;
					String publishName = "";
					ta_nick = ta_nick.substring(ta_nick.indexOf("nick")+7);
					String nickname =  ta_nick.substring(0,ta_nick.indexOf("\","));
					String ta_content = tt;
					ta_content = ta_content.substring(ta_content.indexOf("content")+10);
					String content = ta_content.substring(0, ta_content.indexOf("\","));
					String ta_teime = tt;
					ta_teime = ta_teime.substring(ta_teime.indexOf("time")+5);
					String create_time = ta_teime.substring(3, ta_teime.indexOf("\","));
					String ta_config = tt;
					ta_config = ta_config.substring(ta_config.indexOf("config")+8);
					String config = ta_config.substring(0, ta_config.indexOf("\","));
					config = config.replace("\"", "").trim();
					
					if(config.contains("wb_screen_name")){
						config = config.substring(config.indexOf("wb_screen_name")+15);
						config = config.substring(0,config.indexOf("&"));
						publishName = config;
						System.out.println("内容==="+decodeUnicode(content)+" 发布时间==="+decodeUnicode(create_time)+" 真实名称==="+decodeUnicode(config));
					}else{
						publishName = nickname;
						System.out.println("内容==="+decodeUnicode(content)+" 发布时间==="+decodeUnicode(create_time)+"真实名称==="+decodeUnicode(nickname));
					}
					
					FetchTaskRiceverBean d = new FetchTaskRiceverBean();
		    		d.setAn(decodeUnicode(decodeUnicode(publishName)).replaceAll("\"", "").trim());
		    		d.setContent(decodeUnicode(content).replaceAll("\"", "").trim());
		    		d.setPt(create_time.replaceAll("\"", "").trim());
		    		list.add(d);
		    		
		    		Map map = new HashMap();
		    		map.put("urlid", b.getId());
		    		map.put("content", decodeUnicode(content).replaceAll("\"", "").trim());
		    		map.put("postTime", create_time.replaceAll("\"", "").trim());
		    		mapList.add(map);
		    		System.out.println("page=="+i);
					
					if(tt.indexOf("}")>-1){
						tt = tt.substring(tt.indexOf("}")+1);
					}else{
						break;
					}
				}
				
				JdbcDistr.distr(b,mapList,list);
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public static void main(String agrs[]){
		FetchTaskRiceverBean b = new FetchTaskRiceverBean();
		b.setUrl("http://comment5.news.sina.com.cn/comment/skin/default.html?channel=gn&newsid=comos-fxnqrkc6385122");
		try {
			sina(b);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static String decodeUnicode(String theString) {      
	    char aChar;      
	     int len = theString.length();      
	    StringBuffer outBuffer = new StringBuffer(len);      
	    for (int x = 0; x < len;) {      
	     aChar = theString.charAt(x++);      
	     if (aChar == '\\') {      
	      aChar = theString.charAt(x++);      
	      if (aChar == 'u') {      
	       // Read the xxxx      
	       int value = 0;      
	       for (int i = 0; i < 4; i++) {      
	        aChar = theString.charAt(x++);      
	        switch (aChar) {      
	        case '0':      
	        case '1':      
	        case '2':      
	        case '3':      
	       case '4':      
	        case '5':      
	         case '6':      
	          case '7':      
	          case '8':      
	          case '9':      
	           value = (value << 4) + aChar - '0';      
	           break;      
	          case 'a':      
	          case 'b':      
	          case 'c':      
	          case 'd':      
	          case 'e':      
	          case 'f':      
	           value = (value << 4) + 10 + aChar - 'a';      
	          break;      
	          case 'A':      
	          case 'B':      
	          case 'C':      
	          case 'D':      
	          case 'E':      
	          case 'F':      
	           value = (value << 4) + 10 + aChar - 'A';      
	           break;      
	          default:      
	           throw new IllegalArgumentException(      
	             "Malformed   \\uxxxx   encoding.");      
	          }      
	   
	        }      
	         outBuffer.append((char) value);      
	        } else {      
	         if (aChar == 't')      
	          aChar = '\t';      
	         else if (aChar == 'r')      
	          aChar = '\r';      
	   
	         else if (aChar == 'n')      
	   
	          aChar = '\n';      
	   
	         else if (aChar == 'f')      
	   
	          aChar = '\f';      
	   
	         outBuffer.append(aChar);      
	   
	        }      
	   
	       } else     
	   
	       outBuffer.append(aChar);      
	   
	      }      
	   
	      return outBuffer.toString();      
	     }     
}
