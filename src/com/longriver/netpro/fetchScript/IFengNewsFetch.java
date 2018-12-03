package com.longriver.netpro.fetchScript;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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



public class IFengNewsFetch {
	public static void main(String agrs[]){
		FetchTaskRiceverBean b=new FetchTaskRiceverBean();
//		b.setUrl("http://comment.ifeng.com/view.php?docUrl=http%3A%2F%2Fnews.ifeng.com%2Fa%2F20151222%2F46789031_0.shtml&docName=%E8%94%A1%E8%8B%B1%E6%96%87%E4%BC%9A%E5%B7%A5%E5%95%86%E5%A4%A7%E4%BD%AC%E9%81%AD%E6%8A%97%E8%AE%AE%EF%BC%9A%E5%B0%B1%E5%83%8F%E5%93%88%E5%B7%B4%E7%8B%97(%E5%9B%BE)&skey=acb7f2");
		b.setUrl("http://gentie.ifeng.com/view.html?docUrl=http%3A%2F%2Fnews.ifeng.com%2Fa%2F20170517%2F51107803_0.shtml&docName=%E4%B9%A0%E8%BF%91%E5%B9%B3%E5%87%BA%E5%B8%AD%E2%80%9C%E4%B8%80%E5%B8%A6%E4%B8%80%E8%B7%AF%E2%80%9D%E5%9B%BD%E9%99%85%E5%90%88%E4%BD%9C%E9%AB%98%E5%B3%B0%E8%AE%BA%E5%9D%9B%E7%BA%AA%E5%AE%9E&skey=33e64a&pcUrl=http://news.ifeng.com/a/20170517/51107803_0.shtml");
		try {
			sina(b);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void sina(FetchTaskRiceverBean fetchTaskRicever) throws Exception{
		//String url = "http://comment.ifeng.com/view.php?docUrl=http%3A%2F%2Fnews.ifeng.com%2Fa%2F20151222%2F46789031_0.shtml&docName=%E8%94%A1%E8%8B%B1%E6%96%87%E4%BC%9A%E5%B7%A5%E5%95%86%E5%A4%A7%E4%BD%AC%E9%81%AD%E6%8A%97%E8%AE%AE%EF%BC%9A%E5%B0%B1%E5%83%8F%E5%93%88%E5%B7%B4%E7%8B%97(%E5%9B%BE)&skey=acb7f2";
		String url = fetchTaskRicever.getUrl();
		String docUrl = url.substring(url.lastIndexOf("?")+8,url.indexOf("&docName"));
		System.out.println(docUrl);
		
		
		URL uf1 = new URL("http://comment.ifeng.com/get?job=1&order=DESC&orderBy=create_time&format=json&pagesize=20&p=1&docurl="+docUrl);
	    HttpURLConnection cf1 = (HttpURLConnection) uf1.openConnection();
	   
	    cf1.connect();
		 
	    String pageNO = "";
		InputStream iff1 = cf1.getInputStream();
		Scanner sf1 = new Scanner(iff1);
		//String mid = "";
		
		while(sf1.hasNext()){
			String scsc = sf1.nextLine();
			if(scsc.indexOf("count") > -1){
				pageNO = scsc.substring(scsc.indexOf("count")).replace("count", "");
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
			URL uf = new URL("http://comment.ifeng.com/get?job=1&order=DESC&orderBy=create_time&format=json&pagesize=20&p="+j+"&docurl="+docUrl);
			 System.out.println("http://comment.ifeng.com/get?job=1&order=DESC&orderBy=create_time&format=json&pagesize=20&p="+j+"&docurl="+docUrl);  
			HttpURLConnection cf= (HttpURLConnection) uf.openConnection();
		   
		    cf.connect();
			 
			InputStream iff = cf.getInputStream();
			Scanner sf = new Scanner(iff);
			//String mid = "";
			
			while(sf.hasNext()){
				String scsc = sf.nextLine();
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
		String  test = testTemp;
		String  s = "comment_contents";
		 int count = 0;
	        for(int i=0; i<test.length();){  
	            int c = -1;  
	            c = test.indexOf(s);
	            if(c != -1){
	            	test = test.substring(c+1);
	            	count ++;
	            }else {
	                System.out.println("---");  
	                break;  
	            }  
	        }  
	        System.out.println("count====="+count);  
	        System.out.println(testTemp);
			String ta = testTemp.substring(testTemp.indexOf("\"uname\":"));
	        System.out.println(ta);
	        List<FetchTaskRiceverBean> list = new ArrayList<FetchTaskRiceverBean>();
	        List<Map> mapList = new ArrayList<Map>();
			for(int i=0;i<count;i++){
				if(i!=0){
					ta = ta.substring(ta.indexOf("\"uname\":"));
				}
				
				ta = ta.substring(ta.indexOf("\"uname\":"));
				String nickname = ta.substring(ta.indexOf("\"uname\":")+8, ta.indexOf(","));
				ta = ta.substring(ta.indexOf("create_time"));
				String create_time = ta.substring(ta.indexOf("create_time")+13, ta.indexOf(","));
				ta = ta.substring(ta.indexOf(s));
				String content = ta.substring(ta.indexOf(s)+18, ta.indexOf("\","));
				System.out.println("content==="+decodeUnicode(content)+" create_time=="+create_time+" nickname=="+decodeUnicode(nickname));
				FetchTaskRiceverBean d = new FetchTaskRiceverBean();
	    		d.setAn(decodeUnicode(nickname).replaceAll("\"", "").trim());
	    		d.setContent(decodeUnicode(content).replaceAll("\"", "").trim());
	    		d.setPt(DateUtil.long2Date(create_time.replaceAll("\"", "").trim())); 
	    		list.add(d);
	    		
	    		Map map = new HashMap();
	    		map.put("urlid", b.getId());
	    		map.put("content", decodeUnicode(content).replaceAll("\"", ""));
	    		map.put("postTime", DateUtil.long2Date(create_time.replaceAll("\"", "").trim()));
	    		mapList.add(map);
			}
			JdbcDistr.distr(b,mapList,list);
		    
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
