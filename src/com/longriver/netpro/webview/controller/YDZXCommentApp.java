package com.longriver.netpro.webview.controller;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 一点资讯评论app
 * @author rhy
 * @2017-7-10 上午11:16:01
 * @version v1.0
 */
public class YDZXCommentApp {
	static Logger logger = Logger.getLogger(YDZXCommentApp.class);
	public static void main(String[] args) {
						 
		TaskGuideBean task = new TaskGuideBean();
		
		task.setAddress("https://www.yidianzixun.com/article/0Gv33IxG?title_sn=0&utk=81nw2y8z&appid=yidian&ver=4.4.6.6&f=ios");
		task.setAddress("https://www.yidianzixun.com/article/0GuemgID?title_sn=0&utk=81nw2y8z&appid=yidian&ver=4.4.6.6&f=ios");
		
		task.setAddress("https://www.yidianzixun.com/article/0GotfESH?title_sn/0");
		task.setAddress("https://www.yidianzixun.com/article/0GoyzeCq?title_sn/0");
		task.setAddress("https://www.yidianzixun.com/article/0H8mmBxs?title_sn/0");
		
		task.setCorpus("我们需要的是质量");
		task.setCorpus("16岁，没看错吧");
		
		task.setCorpus("这娱乐圈那么乱");
		task.setCorpus("怎么样了呀");
		task.setAppInfo("GET https://a1.go2yd.com/Website/interact/add-comment?appid=yidian" +
							"&comment=%E5%85%A8%E6%98%AF%E4%B8%80%E5%A0%86%E5%9E%83%E5%9C%BE" +
							"&cv=4.4.6.6" +
							"&distribution=com.apple.appstore" +
							"&docid=0Gv33IxG" +
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
		
		task.setAppInfo("GET https://a1.go2yd.com/Website/interact/add-comment?platform=1&docid=0HFXf8hw&appid=yidian&itemid=0HFXf8hw&impid=486523690_1505203188915_286_h&title_sn=0&cv=4.3.0.2&comment=%E4%BB%BB%E5%87%AD%E6%88%91%E5%92%8C%E4%BD%A0%E5%91%80&distribution=XSyybcpd&reqid=81nw2y8z_1505203226492_185&meta=486523690_1505203188915_286_h&version=020126&net=wifi HTTP/1.1"+
						"Cookie: JSESSIONID=PCFhmJC2hRNknuBftoYAaw"+
						"Host: a1.go2yd.com"+
						"Accept-Encoding: gzip, deflate"+
						"X-Tingyun-Lib-Type-N-ST: 3;1505203226496"+
						"Connection: Keep-Alive"+
						"User-Agent: Dalvik/1.6.0 (Linux; U; Android 4.2.2; Coolpad 8297 Build/JDQ39)");
		try {
//			ydzxCommentIos(task);
			ydzxCommentAndroid(task);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void toRun(TaskGuideBean task){
		if(task.getAppInfo()!=null)
			task.setAppInfo(task.getAppInfo().replaceAll("\n", ""));
		if(task.getIsApp()==2){
			ydzxCommentIos(task);
		}else{
			ydzxCommentAndroid(task);
		}
	}
	/**
	 * 一点资讯评论ios
	 * @param comment  评论内容
	 * @return
	 * @throws Exception
	 */
	public static void ydzxCommentIos(TaskGuideBean task){
		
		logger.info("ydzx comment ios start...");
		
		try{
		Map<String,String> commentMap = getCommentParam(task);
		
		String preUrl = "https://a1.go2yd.com/Website/interact/add-comment?appid="+commentMap.get("appid")+"" +
						"&comment="+task.getCorpus()+"" +
						"&cv="+commentMap.get("cv")+"" +
						"&distribution="+commentMap.get("distribution")+"" +
						"&docid="+commentMap.get("articleId")+"" +
						"&net="+commentMap.get("net")+"" +
						"&platform="+commentMap.get("platform")+"" +
						"&show_media_name="+commentMap.get("show_media_name")+"" +
						"&title_sn="+commentMap.get("title_sn")+"" +
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
		
		logger.info("ydzx comment ios result:::"+result);
		
		JSONObject parseResult = JSON.parseObject(result);
		String status = parseResult.getString("status");
		
		if(StringUtils.isNotBlank(status) && "success".equals(status)){
			
			logger.info("ydzx comment ios success...");
			MQSender.toMQ(task,"");
		}else{
			
			logger.info("ydzx comment ios fail...");
			MQSender.toMQ(task,"失败");
		}
		}catch(Exception e){
			
			e.printStackTrace();
			logger.info("ydzx comment ios exception...");
			MQSender.toMQ(task,"失败2");
		}
	}
	
	/**
	 * 获取评论参数
	 * @param task header数据
	 * @return
	 */
	public static Map<String, String> getCommentParam(TaskGuideBean task) {
		
		String headers = task.getAppInfo();
		String articleUrl = task.getAddress();
		
		Map<String,String> headerMap = new HashMap<String,String>();
		
		String articleId = articleUrl.substring(articleUrl.indexOf("article")+8,articleUrl.indexOf("?"));
		headerMap.put("articleId", articleId);
		
		String appid = headers.substring(headers.indexOf("appid")+6,headers.indexOf("&comment")).trim();
		headerMap.put("appid", appid);
		
		String cv = headers.substring(headers.indexOf("&cv")+4,headers.indexOf("distribution")-1).trim();
		headerMap.put("cv", cv);
		
		String distribution = headers.substring(headers.indexOf("distribution")+13,headers.indexOf("docid")-1).trim();
		headerMap.put("distribution", distribution);
		
		String net = headers.substring(headers.indexOf("net")+4,headers.indexOf("platform")-1).trim();
		headerMap.put("net", net);
		
		String platform = headers.substring(headers.indexOf("platform")+9,headers.indexOf("show_media_name")-1).trim();
		headerMap.put("platform", platform);
		
		String show_media_name = headers.substring(headers.indexOf("show_media_name")+16,headers.indexOf("title_sn")-1).trim();
		headerMap.put("show_media_name", show_media_name);
		
		String title_sn = headers.substring(headers.indexOf("title_sn")+9,headers.indexOf("version")-1).trim();
		headerMap.put("title_sn", title_sn);
		
		String version = headers.substring(headers.indexOf("version")+8,headers.indexOf("HTTP/1.1")).trim();
		headerMap.put("version", version);
		
		String cookie = headers.substring(headers.indexOf("Cookie")+7,headers.indexOf("User-Agent")).trim();
		headerMap.put("cookie", cookie);
		
		String userAgent = headers.substring(headers.indexOf("User-Agent")+11,headers.indexOf("Accept-Language")).trim();
		headerMap.put("userAgent", userAgent);
		
		return headerMap;
	}

	/**
	 * 一点资讯评论android
	 * article  文章路径
	 * comment  评论的内容
	 * @return
	 */
	public static void ydzxCommentAndroid(TaskGuideBean task){
		
		logger.info("ydzx comment android start...");
		
		try{
		Map<String,String> commentMap = YDZXCommentApp.getCommentAdroid(task);
		
		String preUrl = "https://a1.go2yd.com/Website/interact/add-comment?platform="+commentMap.get("platform")+"" +
						"&docid="+commentMap.get("articleId")+"" +
						"&appid="+commentMap.get("appid")+"" +
						"&itemid="+commentMap.get("articleId")+"" +
						"&impid="+commentMap.get("impid")+"" +
						"&title_sn="+commentMap.get("title_sn")+"" +
						"&cv="+commentMap.get("cv")+"" +
						"&comment="+URLEncoder.encode(task.getCorpus(),"utf-8")+"" +
						"&distribution="+commentMap.get("distribution")+"" +
						"&meta="+commentMap.get("meta")+"" +
						"&version="+commentMap.get("version")+"" +
						"&net="+commentMap.get("net")+"";
		
		URL url = new URL(preUrl);
		
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();

		openConnection.addRequestProperty("Cookie", commentMap.get("cookie"));
		openConnection.addRequestProperty("Host", "a1.go2yd.com");
		openConnection.addRequestProperty("X-Tingyun-Lib-Type-N-ST", commentMap.get("xtingyunId"));
		openConnection.addRequestProperty("Connection", "Keep-Alive");
		openConnection.addRequestProperty("User-Agent", commentMap.get("userAgent"));
		
		openConnection.connect();
		
		Scanner sc = new Scanner(openConnection.getInputStream(),"utf-8");
		
		String result = "";
		while(sc.hasNext()){
			
			result += sc.nextLine();
		}
		
		logger.info("ydzx comment android result :::"+result);
		JSONObject parseResult = JSON.parseObject(result);
		String status = parseResult.getString("status");
		
		if(StringUtils.isNotBlank(status) && "success".equals(status)){
			
			logger.info("ydzx comment android success...");
			MQSender.toMQ(task,"");
		}else{
			
			logger.info("ydzx comment android fail...");
			MQSender.toMQ(task,"失败");
		}
		}catch(Exception e){
			
			e.printStackTrace();
			logger.info("ydzx comment android exception...");
			MQSender.toMQ(task,"失败2");
		}
	}
	
	/**
	 * 获取android评论参数
	 * @param task header信息
	 * @return
	 */
	public static Map<String, String> getCommentAdroid(TaskGuideBean task) {
		
        Map<String,String> headerMap = new HashMap<String,String>();
		
		String headers = task.getAppInfo();
		String articleUrl = task.getAddress();
		
		String articleId = articleUrl.substring(articleUrl.indexOf("article")+8,articleUrl.indexOf("?"));
		headerMap.put("articleId", articleId);
		
		String platform = headers.substring(headers.indexOf("platform")+9,headers.indexOf("docid")-1).trim();
		headerMap.put("platform", platform);
		
		String appid = headers.substring(headers.indexOf("appid")+6,headers.indexOf("itemid")-1).trim();
		headerMap.put("appid", appid);
		
		String impid = headers.substring(headers.indexOf("impid")+6,headers.indexOf("title_sn")-1).trim();
		headerMap.put("impid", impid);
		
		String title_sn = headers.substring(headers.indexOf("title_sn")+9,headers.indexOf("&cv")).trim();
		headerMap.put("title_sn", title_sn);
		
		String cv = headers.substring(headers.indexOf("&cv")+4,headers.indexOf("&comment")).trim();
		headerMap.put("cv", cv);
		
		String distribution = headers.substring(headers.indexOf("distribution")+13,headers.indexOf("meta")-1).trim();
		headerMap.put("distribution", distribution);
		
		String meta = headers.substring(headers.indexOf("meta")+5,headers.indexOf("version")-1).trim();
		headerMap.put("meta", meta);
		
		String version = headers.substring(headers.indexOf("version")+8,headers.indexOf("&net")).trim();
		headerMap.put("version", version);
		
		String net = headers.substring(headers.indexOf("&net")+5,headers.indexOf("HTTP/1.1")).trim();
		headerMap.put("net", net);
		
		String cookie = headers.substring(headers.indexOf("Cookie")+7,headers.indexOf("Host")).trim();
		headerMap.put("cookie", cookie);
		
		String xtingyunId = headers.substring(headers.indexOf("X-Tingyun-Lib-Type-N-ST")+24,headers.indexOf("Connection")).trim();
		headerMap.put("xtingyunId", xtingyunId);
		
		String userAgent = headers.substring(headers.indexOf("User-Agent")+11).trim();
		headerMap.put("userAgent", userAgent);
		
		return headerMap;
	}
	
}
