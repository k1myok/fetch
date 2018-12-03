package com.longriver.netpro.webview.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
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
 * 新车评网评论
 */
public class CarXCPComment {
	
	public static void main(String arsg[]){
		try{
			TaskGuideBean b = new TaskGuideBean();
			b.setNick("hawkfirm");
			b.setPassword("chwx1234");
			b.setAddress("http://www.xincheping.com/news/98732.html");
			b.setCorpus("看看就行了..");
			toRun(b);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void toRun(TaskGuideBean taskdo){
		
		try {
			String userId = taskdo.getNick();
			String pwd = taskdo.getPassword();
			String contents = taskdo.getCorpus();
			String sUrl = taskdo.getAddress();
			
			String cookie = "";
			cookie=getCookie(userId,pwd,URLEncoder.encode(sUrl, "utf-8"));
			if(cookie.equals("")){
				MQSender.toMQ(taskdo,"失败");
			}else{
				sendConent(contents,cookie,taskdo);
			}
		}catch (Exception e) {
			MQSender.toMQ(taskdo,"失败");
			e.printStackTrace();
		}
		
	}
	public static String getCookie(String username,String pwd,String url){
		URL fu;
		String cookie = "";
		try {
				fu = new URL("http://www.xincheping.com/uc/login.do");
				HttpURLConnection fc = (HttpURLConnection) fu.openConnection();
				String fp = "username="+username
						+ "&password="+pwd
						+ "&platform=1";
				System.out.println(fp);
				fc.addRequestProperty("Host", "www.xincheping.com");
				fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
				fc.addRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
				fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
				fc.addRequestProperty("Connection", "keep-alive");
				fc.addRequestProperty("Content-Length", String.valueOf(fp.length()));
				fc.setInstanceFollowRedirects(false);
				fc.setDoInput(true);
				fc.setDoOutput(true);
				PrintWriter fo = new PrintWriter(fc.getOutputStream());
				fo.print(fp);
				fo.flush();
				Map<String, List<String>> m1 = fc.getHeaderFields();
				for(Map.Entry<String,List<String>> entry : m1.entrySet()){
					System.out.println("key=="+entry.getKey()+"  value=="+entry.getValue());
					if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
						for(String value : entry.getValue()){
							cookie = cookie + value.substring(0, value.indexOf(";")) + ";";
						}
					}
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("cookie=="+cookie);
		return cookie;
	}
	
	
	public static void sendConent(String contents,String cookie,TaskGuideBean taskdo){
		URL fu;
		try {
					fu = new URL("http://www.xincheping.com/cmt/submit.do");
				
				HttpURLConnection fc = (HttpURLConnection) fu.openConnection();
				String fp = "content=%3Cspan+style%3D%22color%3A+rgb(51%2C+51%2C+51)%3B+line-height%3A+30px%3B%22%3E"+URLEncoder.encode(contents,"utf-8")
						+ "%3C%2Fspan%3E"
						+ "&targetType=1"
						+ "&targetId=94966"
						+ "&area=%E6%96%B0%E7%96%86%E4%B9%8C%E9%B2%81%E6%9C%A8%E9%BD%90";
				fc.addRequestProperty("Host", "www.xincheping.com");
				fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
				fc.addRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
				fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
				fc.addRequestProperty("Connection", "keep-alive");
				fc.addRequestProperty("Cookie", cookie);
				fc.setInstanceFollowRedirects(false);
				fc.setDoInput(true);
				fc.setDoOutput(true);
				PrintWriter fo = new PrintWriter(fc.getOutputStream());
				fo.print(fp);
				fo.flush();
				InputStream fi = fc.getInputStream();
				Scanner fs = new Scanner(fi,"utf-8");
				while(fs.hasNext()){
					String scsc2 = fs.nextLine();
					System.out.println(scsc2);
				}
				MQSender.toMQ(taskdo,"");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void digg(String cookie){
		
			URL fu;
		try {
					fu = new URL("http://www.xincheping.com/data/post.do");
				
				HttpURLConnection fc = (HttpURLConnection) fu.openConnection();
				String fp = "targetId=1488346&targetType=16&submitType=praise";
				fc.addRequestProperty("Host", "www.xincheping.com");
				fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
				fc.addRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
				fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
				fc.addRequestProperty("Connection", "keep-alive");
				fc.addRequestProperty("Cookie", cookie);
				fc.setInstanceFollowRedirects(false);
				fc.setDoInput(true);
				fc.setDoOutput(true);
				PrintWriter fo = new PrintWriter(fc.getOutputStream());
				fo.print(fp);
				fo.flush();
				InputStream fi = fc.getInputStream();
				Scanner fs = new Scanner(fi,"utf-8");
				while(fs.hasNext()){
					String scsc2 = fs.nextLine();
					System.out.println(scsc2);
				}
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	/** 
     * ����url����url��html���� 
     */  
    public static String openUrl(String currentUrl,String charset) {  
        InputStream is = null;  
        BufferedReader br = null;  
        URL url;  
        StringBuffer html = new StringBuffer();  
        try {  
            url = new URL(currentUrl);  
            URLConnection conn = url.openConnection();  
            conn.setReadTimeout(5000);  
            conn.connect();  
            is = conn.getInputStream();  
            br = new BufferedReader(new InputStreamReader(is,charset));  
            String str;  
            while (null != (str = br.readLine())) {  
                html.append(str).append("\n");  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if (br != null) {  
                try {  
                    br.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
            if (is != null) {  
                try {  
                    is.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return html.toString();  
    }  
    
}
