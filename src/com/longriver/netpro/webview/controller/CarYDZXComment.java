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
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 一点资讯
 */
public class CarYDZXComment {
	
	public static void main(String arsg[]){
		try{
			TaskGuideBean b = new TaskGuideBean();
			b.setNick("13366550854@163.com");
			b.setPassword("shikai");
			b.setAddress("http://www.yidianzixun.com/home?page=article&id=0DriRhSv&up=1327");
			b.setCorpus("应该还是可以的");
			toRun(b);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("null")
	public static void toRun(TaskGuideBean taskdo){
		try {
			String userId =URLEncoder.encode(taskdo.getNick(),"utf-8");
			String pwd = taskdo.getPassword();
			String contents = taskdo.getCorpus();
			String sUrl =taskdo.getAddress();
			int a=sUrl.lastIndexOf("&up");
			String id=sUrl.substring(a-8,a);
	        System.out.println(id);
			String cookie="";
			
		    URL u1 = new URL("http://www.yidianzixun.com/mp_sign_in");
			String fa = "password="+pwd+"&username="+userId;
			HttpURLConnection c1 = (HttpURLConnection) u1.openConnection();
			c1.addRequestProperty("Host", "www.yidianzixun.com");
			c1.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0");
			c1.addRequestProperty("Accept","*/*");
			c1.addRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
			c1.addRequestProperty("Referer", "http://www.yidianzixun.com/home");
			c1.addRequestProperty("X-Requested-With", "XMLHttpRequest");
			c1.addRequestProperty("Content-Length", String.valueOf(fa.length()));
			c1.setInstanceFollowRedirects(true);
			c1.setDoInput(true);
			c1.setDoOutput(true);
			PrintWriter fo = new PrintWriter(c1.getOutputStream());
			fo.print(fa);
			fo.flush();
			InputStream fi1 = c1.getInputStream();
			Scanner fs1 = new Scanner(fi1,"utf-8");
			while(fs1.hasNext()){
				String scsc2 = fs1.nextLine();
				System.out.println(scsc2);
				if(scsc2.contains("failed")){
					MQSender.toMQ(taskdo,"失败");
					return ;
				}
			}
			Map<String, List<String>> m1 = c1.getHeaderFields();
			for(Map.Entry<String,List<String>> entry : m1.entrySet()){
				if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
					for(String value : entry.getValue()){
						if(value.substring(0, value.indexOf(";")).equals("JSESSIONID=")){
							continue;
						}else{
							cookie = cookie + value.substring(0, value.indexOf(";")) + ";";
						}
					}
				}
			}
			        URL fu = new URL("http://www.yidianzixun.com/home/q?"
			        		+ "type=addcomment"
			        		+ "&docid="+id
			        		+ "&comment="+URLEncoder.encode(contents, "utf-8"));

					HttpURLConnection fc = (HttpURLConnection) fu.openConnection();
					fc.addRequestProperty("Host", "www.yidianzixun.com");
					fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
					fc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
					fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
					fc.addRequestProperty("Referer", sUrl);
					fc.addRequestProperty("Cookie", cookie);
					fc.addRequestProperty("Connection", "keep-alive");
					fc.setInstanceFollowRedirects(false);
					fc.setDoInput(true);
					fc.setDoOutput(true);
					PrintWriter fo1 = new PrintWriter(fc.getOutputStream());
					fo1.flush();
					InputStream fi = fc.getInputStream();
					Scanner fs = new Scanner(fi,"utf-8");
					String content = "失败";
					while(fs.hasNext()){
						String scsc2 = fs.nextLine();
						System.out.println("result::"+scsc2);
						if(scsc2.contains("success")) content = "";
					}
					MQSender.toMQ(taskdo,content);
		}catch (Exception e) {
			MQSender.toMQ(taskdo,"报错失败");
		}
		
	}
	
	
}
