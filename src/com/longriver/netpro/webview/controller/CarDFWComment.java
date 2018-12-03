package com.longriver.netpro.webview.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.RandomStringUtils;

import com.alibaba.fastjson.JSON;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

//东方网
public class CarDFWComment {
	
	public static String dir = "c:\\";
	public static String path = "c:\\dongfangwang.jpg";
	public static void main(String arsg[]){
		TaskGuideBean b = new TaskGuideBean();
		b.setAddress("http://news.eastday.com/c/20160713/u1a9522779.html");
		b.setAddress("http://sh.eastday.com/m/20170110/u1a10239114.html");
		b.setCorpus("emeenen");
//		b.setNick("lilei1929");
//		b.setPassword("lilei419688");
		toRun(b);
	}
	
	public static void toRun(TaskGuideBean taskdo){
		
		try {
			String userId = taskdo.getNick();
			String contents = taskdo.getCorpus();
			String sUrl = taskdo.getAddress();
			String cookie = "";
			//获得验证码
			URL fu = new URL("http://lyb.eastday.com/public/code?0.5438278787137835");
			HttpURLConnection fc = (HttpURLConnection) fu.openConnection();
			fc.addRequestProperty("Accept","*/*");
			fc.addRequestProperty("Accept-Language	","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			fc.addRequestProperty("Connection","keep-alive");
			fc.addRequestProperty("Host", "lyb.eastday.com");
			fc.addRequestProperty("Referer", sUrl);
			fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:49.0) Gecko/20100101 Firefox/49.0");
			fc.addRequestProperty("X-Requested-With", "XMLHttpRequest");
			fc.addRequestProperty("Connection", "keep-alive");
			fc.connect();
			InputStream i2 = fc.getInputStream();
	        byte[] bs = new byte[1024];  
	        int len;  
	       File sf=new File(dir);  
	       if(!sf.exists()){  
	           sf.mkdirs();  
	       }  
	       OutputStream os = new FileOutputStream(path);  
	        while ((len = i2.read(bs)) != -1) {  
	          os.write(bs, 0, len);  
	        }  
	        os.close();  
	        i2.close();  
			String randRom = RuoKuai.createByPostNew("2040",path);
			System.out.println("result========================"+randRom);
			Map<String, List<String>> m133 = fc.getHeaderFields();
			for(Map.Entry<String,List<String>> entry : m133.entrySet()){
				if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
					for(String value : entry.getValue()){
						cookie = cookie + value.substring(0, value.indexOf(";")) + ";";
					}
				}
			}
			//确认访客
			 String filename=RandomStringUtils.randomAlphanumeric(10);
			 String tem = cookie;
			 String sess = ";";
			 if(tem.contains("sess")){
				int k1 = tem.indexOf("sess=");
				tem = tem.substring(k1);
				int k2 = tem.indexOf(";");
				sess = tem.substring(5, k2);
			 }	
			 System.out.println("sess::"+sess);
			 System.out.println("nick::"+filename);
			 System.out.println("cookie=="+cookie);
			 URL u12 = new URL("http://lyb.eastday.com/public/checkGuest?from=guest&sess="+sess+"&nick="+filename+"&code="+randRom+"&callback=SetGuest");
			HttpURLConnection c12 = (HttpURLConnection) u12.openConnection();
			c12.addRequestProperty("Accept","*/*");
			c12.addRequestProperty("Accept-Language	","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			c12.addRequestProperty("Connection","keep-alive");
			c12.addRequestProperty("Host", "lyb.eastday.com");
			c12.addRequestProperty("Referer", sUrl);
			c12.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:49.0) Gecko/20100101 Firefox/49.0");
			c12.addRequestProperty("X-Requested-With", "XMLHttpRequest");
			c12.addRequestProperty("Connection", "keep-alive");
			c12.connect();
			Map<String, List<String>> m12 = c12.getHeaderFields();
			String uid = "";
			for(Map.Entry<String,List<String>> entry : m12.entrySet()){
				if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
					System.out.println("key=="+entry.getKey());
					System.out.println("value=="+entry.getValue());
					for(String value : entry.getValue()){
						if(value.contains("EastUid")){
							int k1 = value.indexOf("EastUid");
							int k2 = value.indexOf(";");
							uid = value.substring(k1+8, k2);
						}
					}
				}
			}
			System.out.println("cookie2=="+cookie);
			System.out.println("uid=="+uid);

			
			
			//发帖
			String contentu = URLEncoder.encode(contents, "utf-8");
			String url = URLEncoder.encode(sUrl,"utf-8");
			int a=sUrl.lastIndexOf(".html");
			String id=sUrl.substring(a-7,a);
	        System.out.println("articleId=="+id);
	        URL fu3 = new URL("http://lyb.eastday.com/index/addTie");
	    	HttpURLConnection fc3 = (HttpURLConnection) fu3.openConnection();
	    	String fp = "&UserID="+uid
							+"&NeiRong="+contentu
							+"&articleId="+id
							+"&UserName="+userId
							+"&pinlun=0";
	    	
			fc3.addRequestProperty("Host", "lyb.eastday.com");
			fc3.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:49.0) Gecko/20100101 Firefox/49.0");
			fc3.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			fc3.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			fc3.addRequestProperty("Referer", sUrl);
			fc3.addRequestProperty("Connection", "keep-alive");
			fc3.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			fc3.addRequestProperty("Content-Length", String.valueOf(fp.length()));
			fc3.setInstanceFollowRedirects(false);
			fc3.setDoInput(true);
			fc3.setDoOutput(true);
			PrintWriter fo3 = new PrintWriter(fc3.getOutputStream());
			fo3.print(fp);
			fo3.flush();
			InputStream fi = fc3.getInputStream();
			Scanner fs = new Scanner(fi);
			while(fs.hasNext()){
				String scsc2 = fs.nextLine();
				System.out.println("scsc2=="+scsc2);
			}
			MQSender.toMQ(taskdo,"");
		}catch (Exception e) {
			MQSender.toMQ(taskdo,"报错失败");
			e.printStackTrace();
		}
		
	}
	
	
}
