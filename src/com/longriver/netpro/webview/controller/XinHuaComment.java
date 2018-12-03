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

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.longriver.netpro.common.sina.SData;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

public class XinHuaComment {
	
	private static Logger logger = Logger.getLogger(XinHuaComment.class);
	public static void main(String args[]){
		TaskGuideBean info = new TaskGuideBean();
		info.setNick("18610647727");
		info.setPassword("wangmeng121");
		info.setCorpus("什么时候开");
		info.setAddress("http://news.xinhuanet.com/world/2016-04/21/c_1118692581.htm");
		xinhua(info);
	}	
	@SuppressWarnings("null")
	public static void xinhua(TaskGuideBean taskdo){
		System.out.println("执行新华网新闻评论");
		SData data = new SData();
		
//		String userId = "18610647727";
//		String pwd = "wangmeng121";
//		String sUrl = "http://news.xinhuanet.com/world/2016-04/21/c_1118692581.htm";
//		String contents = "印个奥巴马的";
		
		try {
			String userId =taskdo.getNick();
			String pwd = taskdo.getPassword();
			String contents = taskdo.getCorpus();
			String sUrl = taskdo.getAddress();
			
			//微博登陆用户名
			 data.setString("user_account_id",userId);
			//微博登陆密码
			 data.setString("user_account_pw",pwd);
			//目标地址
			 data.setString("mission_addr",sUrl);
			//评论转发内容
			 data.setString("mission_contents",contents);
	
	
			String cookie="";
		    URL u1 = new URL("http://comment.home.news.cn/a/ajaxLogin.do?_ksTS=1461220199607_105&callback=jsonp106&username="+userId+"&password="+pwd);
			HttpURLConnection c1 = (HttpURLConnection) u1.openConnection();
			//_FakeX509TrustManager.allowAllSSL();
			c1.addRequestProperty("Host", "comment.home.news.cn");
			c1.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
			c1.addRequestProperty("Accept", "*/*");
			c1.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
			c1.addRequestProperty("Referer", sUrl);
			c1.connect();
			Map<String, List<String>> m1 = c1.getHeaderFields();
			for(Map.Entry<String,List<String>> entry : m1.entrySet()){
				if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
					for(String value : entry.getValue()){
						cookie = cookie + value.substring(0, value.indexOf(";")) + ";";
					}
				}
			}
			
			String threadId = sUrl.substring(sUrl.lastIndexOf("/")).replace("/", "").replace(".html", "").replace(".shtml", "").replace(".htm", "");
			
			threadId = threadId.substring(threadId.indexOf("_")+1);
					URL fu = new URL("http://comment.home.news.cn/a/adComment.do");
					HttpURLConnection fc = (HttpURLConnection) fu.openConnection();
					String fp = "sourceId=1&type=1&rurl="+sUrl+"&parentId=&newsId=1-"+threadId+"&content="+ URLEncoder.encode(contents, "utf-8")+"&shareToWb=1" 
							+ "&parentId="
							+ "&content=" + URLEncoder.encode(contents, "utf-8");
					fc.addRequestProperty("Host", "comment.home.news.cn");
					fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
					fc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
					fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
					fc.addRequestProperty("Referer", sUrl);
					fc.addRequestProperty("Cookie", cookie);
					fc.addRequestProperty("Connection", "keep-alive");
					fc.addRequestProperty("Content-Length", String.valueOf(fp.length()));
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
						logger.debug(scsc2);
					}
			
					MQSender.toMQ(taskdo,"");
			
		}catch (Exception e) {
			MQSender.toMQ(taskdo,"失败");
		}
		
	}
	
}
