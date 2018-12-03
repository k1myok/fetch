package com.longriver.netpro.webview.controller;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 一点资讯点赞app
 * @author rhy
 * @2017-7-10 下午1:58:16
 * @version v1.0
 */
public class YDZXPraiseApp {
	
	static Logger logger = Logger.getLogger(YDZXPraiseApp.class);
	
	public static void main(String[] args) {
		
		TaskGuideBean task = new TaskGuideBean();
		
		task.setAddress("https://www.yidianzixun.com/article/0GvIHsgn?title_sn=0&utk=81nw2y8z&appid=yidian&ver=4.4.6.6&f=ios");
		task.setPraiseWho("otopxp-l7r4ikb");
		
//		task.setAddress("https://www.yidianzixun.com/article/0GvEt70b?title_sn/0");
//		task.setAddress("https://www.yidianzixun.com/article/0GvIY6Nf?title_sn/0");
//		task.setPraiseWho("otoqph2as84n");
//		task.setPraiseWho("otomowcredckdkt");
		
		task.setAppInfo("GET https://a1.go2yd.com/Website/interact/add-comment?appid=yidian" +
							"&comment=%E5%A5%BD%E8%AF%84%E4%B8%80%E4%B8%AA%E5%90%A7" +
							"&cv=4.4.6.6" +
							"&distribution=com.apple.appstore" +
							"&docid=0GvIHsgn" +
							"&net=wifi" +
							"&platform=0" +
							"&show_media_name=0" +
							"&title_sn=0" +
							"&version=020124 HTTP/1.1"+
							"Host: a1.go2yd.com"+
							"Accept: */*"+
							"Proxy-Connection: keep-alive"+
							"Cookie: JSESSIONID=PCFhmJC2hRNknuBftoYAaw"+
							"User-Agent: yidian/4.4.6.6 (iPhone; iOS 9.2; Scale/2.00)"+
							"Accept-Language: zh-Hans;q=1, zh-Hant;q=0.9, zh-Hans-CN;q=0.8, en-CN;q=0.7"+
							"Accept-Encoding: gzip, deflate"+
							"Connection: keep-alive"		
							
				
				);
		
//		task.setAppHeaders("GET https://a1.go2yd.com/Website/interact/add-comment?platform=1" +
//							"&docid=0GotfESH" +
//							"&appid=yidian" +
//							"&itemid=0GotfESH" +
//							"&impid=486523690_1499744333505_3600" +
//							"&title_sn=0" +
//							"&cv=4.1.0.0" +
//							"&comment=%E5%A4%9A%E4%BC%9A%E7%9A%84%E6%96%B0%E9%97%BB%E4%BA%86" +
//							"&distribution=app.qq.com" +
//							"&meta=486523690_1499744333505_3600" +
//							"&version=020124" +
//							"&net=wifi HTTP/1.1"+
//							"Cookie: JSESSIONID=PCFhmJC2hRNknuBftoYAaw"+
//							"Host: a1.go2yd.com"+
//							"Accept-Encoding: gzip, deflate"+
//							"X-Tingyun-Processed: true"+
//							"X-Tingyun-Id: C8q8DudRQsY;c=2;r=1721647153"+
//							"Connection: Keep-Alive"+
//							"User-Agent: Dalvik/1.6.0 (Linux; U; Android 4.2.2; Coolpad 8297 Build/JDQ39)"
//
//							);
		
		try {
			ydzxPriseIos(task);
//			ydzxPraiseAndroid(task);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 一点资讯点赞ios
	 * @return
	 * @throws Exception
	 */
	public static void ydzxPriseIos(TaskGuideBean task){
		
		try{
			
		Map<String,String> commentMap =YDZXCommentApp.getCommentParam(task);
		
		String preUrl = "https://a1.go2yd.com/Website/interact/like-comment?appid="+commentMap.get("appid")+"" +
						"&comment_id="+task.getPraiseWho()+"" +
						"&cv="+commentMap.get("cv")+"" +
						"&distribution="+commentMap.get("distribution")+"" +
						"&net="+commentMap.get("net")+"" +
						"&platform="+commentMap.get("platform")+"" +
						"&version="+commentMap.get("version")+"";
		
		URL url = new URL(preUrl);
		
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		
		openConnection.addRequestProperty("Host", "a1.go2yd.com");
		openConnection.addRequestProperty("Accept", "*/*");
		openConnection.addRequestProperty("Proxy-Connection", "keep-alive");
		openConnection.addRequestProperty("Cookie", commentMap.get("cookie"));
		openConnection.addRequestProperty("User-Agent", commentMap.get("userAgent"));
		openConnection.addRequestProperty("Accept-Language", "zh-Hans;q=1, zh-Hant;q=0.9, zh-Hans-CN;q=0.8, en-CN;q=0.7");
		openConnection.addRequestProperty("Accept-Encoding", "gzip, deflate");
		openConnection.addRequestProperty("Connection", "keep-alive");
		
		openConnection.connect();
		
		Scanner sc = new Scanner(openConnection.getInputStream(),"utf-8");
		
		String result = "";
		
		while(sc.hasNext()){
			
			result += sc.nextLine();
		}
		
		logger.info("ydzx prise ios result:::"+result);
		
		JSONObject parseResult = JSON.parseObject(result);
		String status = parseResult.getString("status");
		
		if(StringUtils.isNotBlank(status) && "success".equals(status)){
			
			logger.info("ydzx praise ios success...");
		}else{
			
			logger.info("ydzx praise ios fail...");
		}
		}catch(Exception e){
			
			e.printStackTrace();
			logger.info("ydzx praise ios exception...");
		}
	}
	
	/**
	 * 一点资讯android点赞
	 * @return
	 */
	public static void ydzxPraiseAndroid(TaskGuideBean task){
		
		logger.info("ydzx praise android start...");
		
		try{
			
		Map<String,String> commentMap = YDZXCommentApp.getCommentAdroid(task);
		
		String preUrl = "https://a1.go2yd.com/Website/interact/like-comment?platform="+commentMap.get("platform")+"" +
						"&appid="+commentMap.get("appid")+"" +
						"&comment_id="+task.getPraiseWho()+"" +
						"&cv="+commentMap.get("cv")+"" +
						"&distribution="+commentMap.get("distribution")+"" +
						"&version="+commentMap.get("version")+"" +
						"&net="+commentMap.get("net")+"";
		
		URL url = new URL(preUrl);
		
		URLConnection openConnection = url.openConnection();
		
		openConnection.addRequestProperty("Cookie", commentMap.get("cookie"));
		openConnection.addRequestProperty("Accept-Encoding", "gzip, deflate");
		openConnection.addRequestProperty("X-Tingyun-Processed", "true");
		openConnection.addRequestProperty("X-Tingyun-Id", commentMap.get("xtingyunId"));
		openConnection.addRequestProperty("Host", "a1.go2yd.com");
		openConnection.addRequestProperty("Connection", "Keep-Alive");
		openConnection.addRequestProperty("User-Agent", commentMap.get("userAgent"));
		
		openConnection.connect();
		
		Scanner sc = new Scanner(openConnection.getInputStream(),"utf-8");
		
		String result = "";
		
		while(sc.hasNext()){
			
			result += sc.nextLine();
		}
		
		logger.info("ydzx praise android result :::"+result);
		
		JSONObject parseResult = JSON.parseObject(result);
		String status = parseResult.getString("status");
		
		if(status != null && "success".equals(status)){
			
			logger.info("ydzx praise android success...");
		}else{
			
			logger.info("ydzx praise android fail...");
		}
		}catch(Exception e){
			
			e.printStackTrace();
			logger.info("ydzx praise android exception...");
		}
	}
}
