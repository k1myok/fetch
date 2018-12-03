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

import com.alibaba.fastjson.JSON;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;


//车市网评论
public class CarCheShiComment {
	
	public static String dir = "c:\\";
	public static String path = "c:\\cheshiVcode.jpg";
	
	public static void main(String arsg[]){
		try{
			TaskGuideBean b = new TaskGuideBean();
			b.setNick("hawkfirm");
			b.setPassword("chwx1234");
			b.setAddress("http://news.cheshi.com/20160624/1868035.shtml");
			b.setCorpus("这年头不挣钱啥也买不了");
			toRun(b);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void toRun(TaskGuideBean taskdo){
		
		try {
			String userId =taskdo.getNick();
			String pwd = taskdo.getPassword();
			String contents = taskdo.getCorpus();
			if(contents.length()<5){
				contents += "...............";
			}
			String sUrl =taskdo.getAddress();
			int ind1 = sUrl.indexOf(".shtm");
			String urlt = sUrl.substring(0, ind1);
			int ind2 = urlt.lastIndexOf("/");
			String postid = urlt.substring(ind2+1);
			
			String cookie=getCookie(userId,pwd,URLEncoder.encode(sUrl, "utf-8"));
		
					URL fu = new URL("http://comments.cheshi.com/check_code.php?type=story&id="+postid+"&0.458426917437464");
					HttpURLConnection fc = (HttpURLConnection) fu.openConnection();
					fc.addRequestProperty("Host", "comments.cheshi.com");
					fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
					fc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
					fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
					fc.addRequestProperty("Referer", "http://comments.cheshi.com/story_comments_2014.php?story_id="+postid);
					fc.addRequestProperty("Cookie", cookie);
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
			        

					String randRom = RuoKuai.createByPostNew("3050",path);
					System.out.println("result========================"+randRom);
					
					sendConent(contents,randRom,cookie,taskdo,postid);
					
					
		}catch (Exception e) {
			MQSender.toMQ(taskdo,"失败");
			e.printStackTrace();
		}
		
	}
	public static String getCookie(String username,String pwd,String url){
		String cookie = "";
		URL fu;
		try {
					fu = new URL("https://service.cheshi.com/user/login.php");
				
				HttpURLConnection fc = (HttpURLConnection) fu.openConnection();
				String fp = "username="+username
						+ "&password="+pwd
						+ "&backurl="+url
						+ "&smb=%E7%99%BB%E5%BD%95";
				System.out.println(fp);
				fc.addRequestProperty("Host", "service.cheshi.com");
				fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
				fc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
				fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
				fc.addRequestProperty("Referer", "https://service.cheshi.com/user/login.php");
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
		return cookie;
	}
	
	
	public static void sendConent(String contents,String veryCode,String cookie,TaskGuideBean taskdo,String postid){
		URL fu;
		try {
					fu = new URL("http://comments.cheshi.com/story_comments_2014.php?story_id="+postid);
				
				HttpURLConnection fc = (HttpURLConnection) fu.openConnection();
				String fp = "pwd=&"
						+ "comment="+ URLEncoder.encode(contents, "utf-8")
						+ "&check_code="+veryCode
						+ "&name="+taskdo.getNick()
						+ "&ghost=false";
				System.out.println(fp);
				fc.addRequestProperty("Host", "comments.cheshi.com");
				fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
				fc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
				fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
				fc.addRequestProperty("Referer", "https://service.cheshi.com/user/login.php");
				fc.addRequestProperty("Connection", "keep-alive");
				fc.addRequestProperty("Content-Length", String.valueOf(fp.length()));
				fc.addRequestProperty("Cookie", cookie);
				fc.setInstanceFollowRedirects(false);
				fc.setDoInput(true);
				fc.setDoOutput(true);
				PrintWriter fo = new PrintWriter(fc.getOutputStream());
				fo.print(fp);
				fo.flush();
				InputStream fi = fc.getInputStream();
				Scanner fs = new Scanner(fi);
				while(fs.hasNext()){
					String scsc2 = fs.nextLine();
					System.out.println(scsc2);
					if(scsc2.contains("\"1\"")){
						MQSender.toMQ(taskdo,"");
					}else{
						MQSender.toMQ(taskdo,"失败");
					}
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
