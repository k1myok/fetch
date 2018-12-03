package com.longriver.netpro.util;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtil {
	
	public static String getIFCommentUrl(String url){
		String commentUrl = "";
		String docName = "";
		String docUrl = "";
		String skey = "";
		
		 URL u1;
			try {
				u1 = new URL(url);
				HttpURLConnection fc = (HttpURLConnection) u1.openConnection();
				fc.addRequestProperty("Host", "news.ifeng.com");
				fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
				fc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
				fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
				fc.addRequestProperty("Referer", url);
				fc.addRequestProperty("Connection", "keep-alive");
				fc.setInstanceFollowRedirects(false);
				fc.connect();
				InputStream fi = fc.getInputStream();
				Scanner fs = new Scanner(fi);
				while(fs.hasNext()){
					String scsc2 = fs.nextLine();
					if(scsc2.indexOf("\"docName\":") > -1){
						docName = scsc2.substring(scsc2.indexOf("\"docName\":")).replace("\"docName\":", "");
						docName = docName.substring(0, docName.indexOf(",")).replace("\"", "").trim();
					}
					if(scsc2.indexOf("\"docUrl\":") > -1){
						docUrl = scsc2.substring(scsc2.indexOf("\"docUrl\":")).replace("\"docUrl\":", "");
						docUrl = docUrl.substring(0, docUrl.indexOf(",")).replace("\"", "").trim();
					}
					if(scsc2.indexOf("\"skey\":") > -1){
						skey = scsc2.substring(scsc2.indexOf("\"skey\":")).replace("\"skey\":", "");
						skey = skey.substring(0, skey.indexOf(",")).replace("\"", "").trim();
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				commentUrl = "http://gentie.ifeng.com/view.html?docName="+URLEncoder.encode(docName, "utf-8")
						+"&docUrl="+URLEncoder.encode(docUrl, "utf-8")
						+"&skey="+skey;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return commentUrl;
	}
	public static String getQQCommentUrl(String url){
		String commentUrl = "";
		String tbs = "";
		 URL u1;
		 String host = null;
//		 if(url.contains("auto.qq.com")){
//		    	host = "auto.qq.com";
//		    }else{
//		    	host = "news.qq.com";
//		    }
			try {
				u1 = new URL(url);
				HttpURLConnection fc = (HttpURLConnection) u1.openConnection();
//				fc.addRequestProperty("Host", host);
				fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
				fc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
				fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
				fc.addRequestProperty("Referer", url);
				fc.addRequestProperty("Connection", "keep-alive");
				fc.setInstanceFollowRedirects(false);
				fc.connect();
				InputStream fi = fc.getInputStream();
				Scanner fs = new Scanner(fi);
				while(fs.hasNext()){
					String scsc2 = fs.nextLine();
					if(scsc2.indexOf("cmt_id =") > -1){
						tbs = scsc2.substring(scsc2.indexOf("cmt_id =")).replace("cmt_id =", "");
						tbs = tbs.substring(0, tbs.indexOf(";")).trim();
						break;
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			commentUrl = "http://coral.qq.com/"+tbs;
		return commentUrl;
	}

	/**
	 * 根据新闻链接获得搜狐评论链接
	 */
	public static String getSohuCommentUrl(String url){
		String commentUrl = url;
		String threadId = url.substring(url.lastIndexOf("/")+1);
		threadId  = threadId.substring(0,threadId.indexOf(".shtml")).replace("n", "s");
		commentUrl = "http://pinglun.sohu.com/"+threadId+".html";
		String tbs = "";
		 URL u1;
			try {
				u1 = new URL(commentUrl);
				HttpURLConnection fc = (HttpURLConnection) u1.openConnection();
				fc.addRequestProperty("Host", "pinglun.sohu.com");
				fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
				fc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
				fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
				fc.addRequestProperty("Referer", url);
				fc.addRequestProperty("Connection", "keep-alive");
				fc.setInstanceFollowRedirects(false);
				fc.connect();
				InputStream fi = fc.getInputStream();
				Scanner fs = new Scanner(fi);
				while(fs.hasNext()){
					String scsc2 = fs.nextLine();
					if(scsc2.indexOf("<a href=\"") > -1){
						tbs = scsc2.substring(scsc2.indexOf("<a href=\"")).replace("<a href=\"", "");
						tbs = tbs.substring(0, tbs.indexOf("\">")).trim();
						break;
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			commentUrl = tbs;
		return commentUrl;
	}
	/**
	 * 根据新闻链接获得新浪评论链接
	 */
	public static String getSinaCommentUrl(String url){
		String tbs = "";
		String commentUrl ="";
	    URL u1;
	    String host = "";
	    boolean flag = true;
	    
	    if(url.contains("auto.sina.com.cn")){
	    	host = "auto.sina.com.cn";
	    }else{
	    	host = "news.sina.com.cn";
	    	flag = false;
	    }
		try {
			u1 = new URL(url);
			HttpURLConnection fc = (HttpURLConnection) u1.openConnection();
			fc.addRequestProperty("Host", host);
			fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
			fc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			fc.addRequestProperty("Referer", url);
			fc.addRequestProperty("Connection", "keep-alive");
			fc.connect();
			InputStream fi = fc.getInputStream();
			Scanner fs = new Scanner(fi);
			while(fs.hasNext()){
				String scsc2 = fs.nextLine();
				if(scsc2.indexOf("<meta name=\"comment\" content=\"") > -1&&!flag){
					tbs = scsc2.substring(scsc2.indexOf("<meta name=\"comment\" content=\"")).replace("<meta name=\"comment\" content=\"", "");
					tbs = tbs.substring(0, tbs.indexOf("/>")).replace("\"", "").trim();
					break;
				}
				if(scsc2.indexOf("<meta name=\"comment\" content=\"") > -1&&flag){
					tbs = scsc2.substring(scsc2.indexOf("<meta name=\"comment\" content=\"")).replace("<meta name=\"comment\" content=\"", "");
					tbs = tbs.substring(0, tbs.indexOf(">")).replace("\"", "").trim();
					break;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!flag){
			String tt[] = tbs.split(":");
			commentUrl = "http://comment5.news.sina.com.cn/comment/skin/default.html?"
					+ "channel="+tt[0]+"&newsid="+tt[1];
		}else{
			String tt[] = tbs.split(";");
			commentUrl = "http://comment5.news.sina.com.cn/comment/skin/default.html?"
				+ "channel="+tt[0].replace("comment_channel:", "")+"&newsid="+tt[1].replace("comment_id:", "");
		}
		
		return commentUrl;
	}

	/**
	 * 根据新闻链接获得163评论链接
	 */
	public static String get163CommentUrl(String url){
		
		if(url.contains("photonew") || url.contains("photoview")){
			return get163PHCommentUrl(url);
		}
		
		String threadId = url.substring(url.lastIndexOf("/")+1);
		threadId  = threadId.substring(0,threadId.indexOf(".html"));
		String strBaiduUrl = "http://sdk.comment.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads/"
				+ threadId+"?ibc=jssdk"
						+ "&callback=tool1008155684180092067_"+new Date().getTime()+"&_="+new Date().getTime();
		String tbs = "";
		String commentUrl ="";
	    URL u1;
		try {
			u1 = new URL(strBaiduUrl);
			HttpURLConnection fc = (HttpURLConnection) u1.openConnection();
			fc.addRequestProperty("Host", "sdk.comment.163.com");
			fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
			fc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			fc.addRequestProperty("Referer", url);
			fc.addRequestProperty("Connection", "keep-alive");
			fc.connect();
			InputStream fi = fc.getInputStream();
			Scanner fs = new Scanner(fi);
			while(fs.hasNext()){
				String scsc2 = fs.nextLine();
				if(scsc2.indexOf("boardId\":") > -1){
					tbs = scsc2.substring(scsc2.indexOf("boardId\":")).replace("boardId\":", "");
					tbs = tbs.substring(0, tbs.indexOf(",")).replace("\"", "").trim();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 String regex1 = "http://([\\d\\D]*?)\\.";
		 String urlT = getContent(regex1,url);
		commentUrl = "http://comment."+urlT+".163.com/"+tbs+"/"+threadId+".html";
		return commentUrl;
	}
	public static String get163PHCommentUrl(String url){
		
		String strBaiduUrl = url;
		String regex1 = "<div class=\"parting-line\"></div>                <a href=\"([\\d\\D]*?)\" hidefocus=\"true\" ";
		String commentUrl = "";
	    URL u1;
	    StringBuffer a = new StringBuffer();
		try {
			u1 = new URL(strBaiduUrl);
			HttpURLConnection fc = (HttpURLConnection) u1.openConnection();
		
			fc.connect();
			InputStream fi = fc.getInputStream();
			Scanner fs = new Scanner(fi,"GBK");
			
			while(fs.hasNext()){
				String scsc2 = fs.nextLine();
				a.append(scsc2);
				System.out.println(scsc2);
		        
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		commentUrl = getContent(regex1,a.toString());
		return commentUrl;
	}
	private static String getContent(String regex,String text) {  
        String content = "";  
        Pattern pattern = Pattern.compile(regex);  
        Matcher matcher = pattern.matcher(text);  
        while(matcher.find()) {  
            content = matcher.group(1).toString();  
            System.out.println(content);
        }  
       
        return content;  
    }  

}
