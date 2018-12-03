package com.longriver.netpro.webview.controller;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.longriver.netpro.common.sina.SData;
import com.longriver.netpro.common.sina.SinaIdMidConverter;
import com.longriver.netpro.common.sina.WeiboSina;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

public class WeiboComment{
	private static Logger logger = Logger.getLogger(WeiboComment.class);
	
	public static void main(String arsg[]){
		TaskGuideBean b = new TaskGuideBean();
		b.setNick("lilei1929@163.com");
		b.setPassword("lilei419688..");
		b.setNick("13226172899"); //异常账号
		b.setPassword("lx1314");//		b.setAddress("http://weibo.com/2418724427/EnryFk1Yw?from=page_1001062418724427_profile&wvr=6&mod=weibotime&type=comment#_rnd1482479610672");
//		b.setAddress("https://weibo.com/2590506122/FzvJDgIWw?from=page_1001062590506122_profile&wvr=6&mod=weibotime&type=comment&sudaref=passport.weibo.com#_rnd1513219537731");
		b.setAddress("https://weibo.com/5044281310/FD04qwpsh");
		b.setCorpus("啥...");

		b.setCommentOrPost(0);//0评论 1评论+转发
		b.setCommentOrPost(1);//0评论 1评论+转发
		
		b.setNick("17080625988");
		b.setPassword("chwx9828875061");
		b.setCorpus("平遥牛肉");
		b.setAddress("https://weibo.com/5056645402/G3mKyjCnM?from=page_1005055056645402_profile&wvr=6&mod=weibotime&type=comment");
		sina(b);
	}
	
	public static void sina(TaskGuideBean taskdo){
		
		try {
			SData data =new SData();
			
			///String addr="http://weibo.com/5534959891/CzBGjzsDz?from=page_1005055534959891_profile&wvr=6&mod=weibotime&type=comment";
			String userId =taskdo.getNick();
			String pwd = taskdo.getPassword();
			String contents = taskdo.getCorpus();
			String addr = taskdo.getAddress();
			String transmitFlag = taskdo.getCommentOrPost()+"";//0评论 1评论+转发
			
			if(transmitFlag==null || transmitFlag.equals("")){
				transmitFlag = "2";
			}
			if(contents==null || contents.trim().equals("")){
				contents = "转发";
			}
			logger.info(transmitFlag+"------0评论 1评论+转发-----"+transmitFlag);
			data.put("user_account_id", userId);
			data.put("user_account_pw", pwd);
			data.put("contents", contents);
			data.put("addr", addr);
			data.put("transmitFlag", transmitFlag);
			String midEncode = addr.substring(addr.lastIndexOf("/")).replace("/", "");
			if(midEncode.indexOf("?") > -1){
				midEncode = midEncode.substring(0, midEncode.indexOf("?"));
			}
			if(midEncode.indexOf("#") > -1){
				midEncode = midEncode.substring(0, midEncode.indexOf("#"));
			}
			
				String cookie = "";
				String uid = "";
				String content = "未定义错误";
				taskdo.setCode(9);
				String link = "";
		      
		 	    
		        cookie = WeiboSina.getCookie(data,0);
		        String m = "";
		 		if(cookie.length()>10){
		 			if(uid==null||uid.equals("")){
			     		uid = cookie.substring(cookie.indexOf("uid%3D")+6);
			     		uid = uid.substring(0,uid.indexOf("%26"));
			     	}
			         URL u45 = new URL(addr);
			         HttpURLConnection c45 = (HttpURLConnection) u45.openConnection();
			         c45.addRequestProperty("Host", "weibo.com");
//			         c45.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			         c45.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			         c45.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
			         c45.addRequestProperty("Cookie", cookie);
			         c45.addRequestProperty("Connection", "keep-alive");
			         c45.setConnectTimeout(1000 * 30);
			         c45.setReadTimeout(1000 * 20);
			         c45.connect();
			         InputStream i45 = c45.getInputStream();
			         Scanner s45 = new Scanner(i45, "utf-8");
			         String pdetail = "";
			         String location = "";
			         while(s45.hasNext()){
			         	String scsc = s45.nextLine();
			         	//System.out.println(scsc);
			         	if(scsc.indexOf("$CONFIG['page_id']") > -1){
			         		pdetail = scsc.substring(scsc.indexOf("$CONFIG['page_id']")).replace("$CONFIG['page_id']", "");
			         		pdetail = pdetail.substring(pdetail.indexOf("'") + 1);
			         		pdetail = pdetail.substring(0, pdetail.indexOf("'"));
			         	}
			         	if(scsc.indexOf("$CONFIG['location']") > -1){
			         		location = scsc.substring(scsc.indexOf("$CONFIG['location']")).replace("$CONFIG['location']", "");
			         		location = location.substring(pdetail.indexOf("'") + 1);
			         		location = location.substring(0, location.indexOf("'"));
			         	}
			         }
			         //System.out.println(pdetail);
			         
//			         URL u451 = new URL("http://api.t.sina.com.cn/queryid.json?mid=" + midEncode + "&isBase62=1&type=1");
//			         HttpURLConnection c451 = (HttpURLConnection) u451.openConnection();
//			         c451.connect();
//			         InputStream i451 = c451.getInputStream();
//			         Scanner s451 = new Scanner(i451);
//			         while(s451.hasNext()){
//			         	String scsc = s451.nextLine();
//			         	if(scsc.indexOf("id") > -1){
//			         		m = scsc.substring(scsc.indexOf("{\"id\":\"")).replace("{\"id\":\"", "");
//			         		m = m.substring(0, m.indexOf("\""));
//			         	}
//			         }
			         m = SinaIdMidConverter.midToId(midEncode);
			         System.out.println("------------------------------------WeiboTransmit Sina-----------------------------------------");
			         URL u5 = new URL("https://weibo.com/aj/comment/add?_wv=5&__rnd=" + new Date().getTime());
			         HttpURLConnection c5 = (HttpURLConnection) u5.openConnection();
			         String paramss = "act=post"
			         		+ "&mid=" + m
			         		+ "&uid=" + uid
			         		+ "&isroot=0"
			         		+ "&content=" + URLEncoder.encode(contents, "utf-8")
			         		+ "&type=big"
			         		+ "&location=" + location
			         		+ "&module=bcommlist"
			         		+ "&pdetail=" + pdetail
			         		+ "&_t=0";
			         if(transmitFlag!=null && !transmitFlag.equals("") && !transmitFlag.equals("0") && !transmitFlag.equals("3")){
							logger.info(transmitFlag+"------1评论+转发-----"+transmitFlag);
							paramss = paramss + "&forward=1";
						}else{
							logger.info(transmitFlag+"------0评论-----"+transmitFlag);
							paramss = paramss + "&forward=0";
						}
			         System.out.println("cookie==="+cookie);
			         c5.addRequestProperty("Host", "weibo.com");
//			         c5.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			         c5.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			         c5.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
			         c5.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			         c5.addRequestProperty("X-Requested-With", "XMLHttpRequest");
			         c5.addRequestProperty("Referer", "http://weibo.com/" + uid + "/" + midEncode);
			         c5.addRequestProperty("Content-Length", String.valueOf(paramss.length()));
			         c5.addRequestProperty("Cookie", cookie);
			         c5.addRequestProperty("Connection", "keep-alive");
			         c5.addRequestProperty("Pragma", "no-cache");
			         c5.addRequestProperty("Cache-Control", "no-cache");
			         c5.setDoInput(true);
			         c5.setDoOutput(true);
			         c5.setConnectTimeout(1000 * 30);
			         c5.setReadTimeout(1000 * 20);
			         PrintWriter o5 = new PrintWriter(c5.getOutputStream());
			 		o5.print(paramss);
			 		o5.flush();
			 		
			 		Map<String, List<String>> headerFields = c5.getHeaderFields();
			 		List<String> list = headerFields.get("Set-Cookie");
			 		for (String string : list) {
						System.out.println(string);
						System.out.println("==========");
					}
			 		for(Map.Entry<String, List<String>> header:headerFields.entrySet()){
			 			System.out.println(header);
			 			System.out.println("----------");
			 		}
			 		InputStream i5 = null;
			 		try{
			 			i5 = c5.getInputStream();
			 		}catch(Exception e){
			 			e.printStackTrace();
			 		}
			 		//返回状态码和跳转地址判断
			 		if(c5.getResponseCode()==302 || 
							(c5.getHeaderField("Location")!=null && c5.getHeaderField("Location").contains("security.weibo.com"))){
						content = "帐号异常";
						taskdo.setCode(2);
					}
			 		Scanner s5 = new Scanner(i5);
			 		String mid = "";
			 		String mid2 = "";
			 		while(s5.hasNext()){
			 			String scsc = StringUtil.decodeUnicode(s5.nextLine());
			 			System.out.println(scsc);
			 			if(scsc.indexOf("action-data=\\\"mid=") > -1){
							mid = scsc.substring(scsc.indexOf("action-data=\\\"mid=")).replace("action-data=\\\"mid=", "");
							mid = mid.substring(0, mid.indexOf("\\"));
						}
						if(scsc.indexOf("action-data=\\\"cid=") > -1){
							mid2 = scsc.substring(scsc.indexOf("action-data=\\\"cid=")).replace("action-data=\\\"cid=", "");
							mid2 = mid2.substring(0, mid2.indexOf("&"));
						}
						content = WeiboSina.getResultContent(scsc,content,taskdo);
						link = "http://weibo.com/" + uid + "/";
			 		}
			 		taskdo.setUrl(link);
		 		}else{
		 			content = WeiboSina.getDengluResult(cookie,content,taskdo);
		 		}
		 		if(StringUtils.isBlank(content)){//成功时保存cookie信息
		        	taskdo.setCookieData(cookie);
		        }
		 		
		 		if("2".equals(transmitFlag) || "3".equals(transmitFlag)){
		 			toPraise(taskdo,m);//微博点赞
		 		}
			MQSender.toMQ(taskdo,content);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			taskdo.setCode(8);
			MQSender.toMQ(taskdo,"url链接错误!");
		} catch (Exception e) {
			taskdo.setCode(8);
			e.printStackTrace();
			MQSender.toMQ(taskdo,"发帖异常报错失败!");
			
		}	
			
	}
	public static String  getSpJs(String publickey,String servertime,
			String nonce,String pw) throws ScriptException, NoSuchMethodException, IOException{
		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine engine = sem.getEngineByName("javascript");
		//String path = InterfaceWeiboComment.class.getResource("/").getFile();
		
		
		String jsFileName = "c:\\sina.js";   // 璇诲彇js鏂囦欢   
		Object result  ;
		FileReader reader = new FileReader(jsFileName);   // 鎵ц鎸囧畾鑴氭湰   
		engine.eval(reader); 
		if(engine instanceof Invocable) {    
			Invocable invoke = (Invocable)engine;    // 璋冪敤merge鏂规硶锛屽苟浼犲叆涓や釜鍙傛暟    

			// c = merge(2, 3);    
			result = ((Invocable)engine).invokeFunction("sinaRsa", publickey.trim(),
					servertime.trim(), nonce.trim(), pw.trim());

			}else{
				result = new String("");
			}

			reader.close();  
						
		return (String)result;
		
	}
	
	/**
	 * 点赞
	 * @param taskdo
	 * @param m
	 */
	private static void toPraise(TaskGuideBean task, String mid){
		
		try{
		URL url = new URL("https://weibo.com/aj/v6/like/add?ajwvr=6&__rnd="+System.currentTimeMillis());
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		
		String param = "location=page_100505_single_weibo" +
				"&version=mini" +
				"&qid=heart" +
				"&mid="+mid+"" +
				"&loc=profile" +
				"&cuslike=1" +
				"&_t=0";
		
		connection.addRequestProperty("Host", "weibo.com");
		connection.addRequestProperty("Connection", "keep-alive");
		connection.addRequestProperty("Content-Length", String.valueOf(param.length()));
		connection.addRequestProperty("Origin", "https://weibo.com");
		connection.addRequestProperty("X-Requested-With", "XMLHttpRequest");
		connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36 LBBROWSER");
		connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.addRequestProperty("Accept", "*/*");
		connection.addRequestProperty("Referer", task.getAddress());
		connection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
		connection.addRequestProperty("Cookie", task.getCookieData());
		
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		connection.setRequestMethod("POST");
		
		PrintWriter pw = new PrintWriter(connection.getOutputStream());
		pw.print(param);
		pw.flush();
		
		Scanner sc = new Scanner(connection.getInputStream(),"utf-8");
		String result = "";
		while(sc.hasNext()){
			
			result += sc.nextLine();
		}
		System.out.println(result);
		}catch(Exception e){
			
			e.printStackTrace();
		}
	}
	
}
