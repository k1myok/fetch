package com.longriver.netpro.webview.controller;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 搜狐新闻跟帖app
 * @author rhy
 * @2017-6-28 下午4:33:48
 * @version v1.0
 */
public class SohuCommentApp {
	
	static Logger logger = Logger.getLogger(SohuCommentApp.class);
	
	public static void main(String[] args) {
		
		TaskGuideBean task = new TaskGuideBean();
		String articleUrl ="http://3g.k.sohu.com/t/n210503948";
//		articleUrl = "http://3g.k.sohu.com/t/n210566042?showType=";
//		articleUrl = "http://3g.k.sohu.com/t/n210644087?showType=";
//		articleUrl = "http://3g.k.sohu.com/t/n210503948?showType=";
		articleUrl = "http://3g.k.sohu.com/t/n211062405?showType=";
		String comment = "加大力度是好的";
		task.setAddress(articleUrl);
		task.setCorpus(comment);
		
		String appHeaders = "POST http://api.k.sohu.com/api/comment/userComment.go HTTP/1.1"+
							"Host: api.k.sohu.com"+
							"Content-Type: multipart/form-data; boundary=Boundary+619571036E876E87"+
							"Cookie: SUV=1707191531571012; ppinf=2|1500449515|1501659115|bG9naW5pZDowOnx1c2VyaWQ6MjU6c29odWI5MDY0NDY5ODAzNUBzb2h1LmNvbXxzZXJ2aWNldXNlOjMwOjAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMHxjcnQ6MTA6MjAxNy0wNy0xOXxlbXQ6MTowfGFwcGlkOjQ6MTEwNnx0cnVzdDoxOjF8cGFydG5lcmlkOjE6MHxyZWxhdGlvbjowOnx1dWlkOjA6fHVpZDowOnx1bmlxbmFtZTozNjolRTglQkYlOTElRTUlOUMlQTglRTUlOTIlQUIlRTUlQjAlQkF8; pprdig=HAVI96xeJdKGiEL0avRVxXN_meNaB2e3BVRcYhEfPW60Vd3QDZhx9bDYj7wO_DxLI5xiTt3p_AEs6WHfXG5XXUmqUuIjQH3aHejEme8TvdUmVl0YOsU4pHfqfrXL6RaY5wwwpI2q5jAYdSKOxBpRq-9CLg7fRhSj_v5TWlnbYUk; ppsmu=1|1500449515|1501659115|dXNlcmlkOjI1OnNvaHViOTA2NDQ2OTgwMzVAc29odS5jb218dWlkOjA6fHV1aWQ6MDo|D3St-8eUMoQSKTdakJjcEi7-gM5j0M8qaBXgdaD5cVU8LovEa7KFF_OQqA-n791iL16Z_9IF8OblHEYBiC6q-g; sohunews_redirect=done"+
							"Connection: keep-alive"+
							"Connection: keep-alive"+
							"Accept: */*"+
							"User-Agent: sohunews/5.9.0 (iPhone; iOS 9.2; Scale/2.00)"+
							"Accept-Language: zh-Hans-CN;q=1, en-CN;q=0.9"+
							"Content-Length: 1546"+
							"Accept-Encoding: gzip, deflate"+
							
							"--Boundary+619571036E876E87"+
							"Content-Disposition: form-data; name=\"apiVersion\""+
							
							"37"+
							"--Boundary+619571036E876E87"+
							"Content-Disposition: form-data; name=\"author\""+
							
							"近在咫尺"+
							"--Boundary+619571036E876E87"+
							"Content-Disposition: form-data; name=\"bid\""+
							
							"Y29tLnNvaHUubmV3c3BhcGVy"+
							"--Boundary+619571036E876E87"+
							"Content-Disposition: form-data; name=\"busiCode\""+
							
							"2"+
							"--Boundary+619571036E876E87"+
							"Content-Disposition: form-data; name=\"comtProp\""+
							
							"0"+
							"--Boundary+619571036E876E87"+
							"Content-Disposition: form-data; name=\"cont\""+
							
							"什么时候也去呀"+
							"--Boundary+619571036E876E87"+
							"Content-Disposition: form-data; name=\"contType\""+
							
							"text"+
							"--Boundary+619571036E876E87"+
							"Content-Disposition: form-data; name=\"gid\""+
							
							"01010111060001dec0fe662b5f50ebfc1476993202d5aa"+
							"--Boundary+619571036E876E87"+
							"Content-Disposition: form-data; name=\"id\""+
							
							"210503948"+
							"--Boundary+619571036E876E87"+
							"Content-Disposition: form-data; name=\"p1\""+
							
							"NjI4NjAwNTc3NTE5ODc2MTAwNw=="+
							"--Boundary+619571036E876E87"+
							"Content-Disposition: form-data; name=\"passport\""+
							
							"sohub90644698035@sohu.com"+
							"--Boundary+619571036E876E87"+
							"Content-Disposition: form-data; name=\"pid\""+
							
							"6293341187558781006"+
							"--Boundary+619571036E876E87"+
							"Content-Disposition: form-data; name=\"sid\""+
							
							"9"+
							"--Boundary+619571036E876E87"+
							"Content-Disposition: form-data; name=\"token\""+
							
							"c0dfcc703ba398281faec0dd2415573d"+
							"--Boundary+619571036E876E87"+
							"Content-Disposition: form-data; name=\"u\""+
							
							"1"+
							"--Boundary+619571036E876E87"+
							"Content-Disposition: form-data; name=\"userId\""+
							
							"sohub90644698035@sohu.com"+
							"--Boundary+619571036E876E87--";
		//安卓
		appHeaders = "POST http://api.k.sohu.com/api/comment/userComment.go HTTP/1.1\nUser-Agent: SohuNews/5.9.2 BuildCode/125\nContent-Length: 3138\nContent-Type: multipart/form-data; boundary=5ysVUBChre5luqJpJB_7r2FzpQhxVee\nHost: api.k.sohu.com\nConnection: Keep-Alive\n\n--5ysVUBChre5luqJpJB_7r2FzpQhxVee\nContent-Disposition: form-data; name=\"id\"\nContent-Type: text/plain; charset=US-ASCII\nContent-Transfer-Encoding: 8bit\n\n211062405\n--5ysVUBChre5luqJpJB_7r2FzpQhxVee\nContent-Disposition: form-data; name=\"author\"\nContent-Type: text/plain; charset=US-ASCII\nContent-Transfer-Encoding: 8bit\n\n%E6%9F%B3%E7%B5%AE%E6%89%8D%E9%AB%98\n--5ysVUBChre5luqJpJB_7r2FzpQhxVee\nContent-Disposition: form-data; name=\"comtProp\"\nContent-Type: text/plain; charset=US-ASCII\nContent-Transfer-Encoding: 8bit\n\n1\n--5ysVUBChre5luqJpJB_7r2FzpQhxVee\nContent-Disposition: form-data; name=\"token\"\nContent-Type: text/plain; charset=US-ASCII\nContent-Transfer-Encoding: 8bit\n\ne258823fdcce3fb46dd504e224183241\n--5ysVUBChre5luqJpJB_7r2FzpQhxVee\nContent-Disposition: form-data; name=\"p1\"\nContent-Type: text/plain; charset=US-ASCII\nContent-Transfer-Encoding: 8bit\n\nNjI4ODU5NTMwNTQ1ODIxNjk4MQ%3D%3D\n--5ysVUBChre5luqJpJB_7r2FzpQhxVee\nContent-Disposition: form-data; name=\"gid\"\nContent-Type: text/plain; charset=US-ASCII\nContent-Transfer-Encoding: 8bit\n\n02ffff11061111535d23efc36406accf9bb4480d72b3f4\n--5ysVUBChre5luqJpJB_7r2FzpQhxVee\nContent-Disposition: form-data; name=\"pid\"\nContent-Type: text/plain; charset=US-ASCII\nContent-Transfer-Encoding: 8bit\n\n6293378479954505852\n--5ysVUBChre5luqJpJB_7r2FzpQhxVee\nContent-Disposition: form-data; name=\"passport\"\nContent-Type: text/plain; charset=US-ASCII\nContent-Transfer-Encoding: 8bit\n\nsohub90644698018@sohu.com\n--5ysVUBChre5luqJpJB_7r2FzpQhxVee\nContent-Disposition: form-data; name=\"cont\"\nContent-Type: text/plain; charset=US-ASCII\nContent-Transfer-Encoding: 8bit\n\n%E6%88%91%E4%B9%9F%E6%98%AF%E6%9C%8D%E4%BA%86%E4%BD%A0%E4%BA%86\n--5ysVUBChre5luqJpJB_7r2FzpQhxVee\nContent-Disposition: form-data; name=\"busiCode\"\nContent-Type: text/plain; charset=US-ASCII\nContent-Transfer-Encoding: 8bit\n\n2\n--5ysVUBChre5luqJpJB_7r2FzpQhxVee\nContent-Disposition: form-data; name=\"contType\"\nContent-Type: text/plain; charset=US-ASCII\nContent-Transfer-Encoding: 8bit\n\ntext\n--5ysVUBChre5luqJpJB_7r2FzpQhxVee\nContent-Disposition: form-data; name=\"innerIp\"\nContent-Type: text/plain; charset=US-ASCII\nContent-Transfer-Encoding: 8bit\n\n192.168.20.107\n--5ysVUBChre5luqJpJB_7r2FzpQhxVee\nContent-Disposition: form-data; name=\"stationId\"\nContent-Type: text/plain; charset=US-ASCII\nContent-Transfer-Encoding: 8bit\n\n\n--5ysVUBChre5luqJpJB_7r2FzpQhxVee\nContent-Disposition: form-data; name=\"macAaddress\"\nContent-Type: text/plain; charset=US-ASCII\nContent-Transfer-Encoding: 8bit\n\n18:dc:56:d0:11:46\n--5ysVUBChre5luqJpJB_7r2FzpQhxVee\nContent-Disposition: form-data; name=\"longitude\"\nContent-Type: text/plain; charset=US-ASCII\nContent-Transfer-Encoding: 8bit\n\n116.380333\n--5ysVUBChre5luqJpJB_7r2FzpQhxVee\nContent-Disposition: form-data; name=\"osType\"\nContent-Type: text/plain; charset=US-ASCII\nContent-Transfer-Encoding: 8bit\n\nandroid\n--5ysVUBChre5luqJpJB_7r2FzpQhxVee\nContent-Disposition: form-data; name=\"latitude\"\nContent-Type: text/plain; charset=US-ASCII\nContent-Transfer-Encoding: 8bit\n\n39.975966\n--5ysVUBChre5luqJpJB_7r2FzpQhxVee--";
		appHeaders = appHeaders.replaceAll("\n", "");					
		task.setAppInfo(appHeaders);
		try {
//			sohuCommentIos(task);
			sohuCommentAndroid(task);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void toRun(TaskGuideBean task){
		task.setAppInfo(task.getAppInfo().replaceAll("\n", ""));
		if(task.getIsApp()==2){
			sohuCommentIos(task);
		}else{
			sohuCommentAndroid(task);
		}
	}
	/**
	 * 搜狐新闻评论ios
	 * @param task
	 */
	@SuppressWarnings("unused")
	public static void sohuCommentIos(TaskGuideBean task){
		
		logger.info("sohu comment ios start...");
		try{
		String articleUrl = task.getAddress();
		
		Map<String,String> headerMap = getHeaderParam(task.getAppInfo());
		JSONObject parseData = getCommentParam(articleUrl);
		
		String newsId = "";
		if(parseData != null){
		newsId = parseData.getString("newsId");
		}
		
		String preUrl = "http://api.k.sohu.com/api/comment/userComment.go";
						
		URL url = new URL(preUrl);
		HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
		
		String par = "";
		String boundary = "--Boundary+DF717FD94E65C816";
		String comment = task.getCorpus();
		StringBuffer sb = new StringBuffer();
		sb.append(boundary).append("\r\n");
		sb.append("Content-Disposition: form-data; name=\"apiVersion\"").append("\r\n").append("\r\n");
		sb.append(headerMap.get("apiVersion")).append("\r\n");
		
		sb.append(boundary).append("\r\n");
		sb.append("Content-Disposition: form-data; name=\"author\"").append("\r\n").append("\r\n");
		sb.append(headerMap.get("author")).append("\r\n");
		
		sb.append(boundary).append("\r\n");
		sb.append("Content-Disposition: form-data; name=\"bid\"").append("\r\n").append("\r\n");
		sb.append(headerMap.get("bid")).append("\r\n");
		
		sb.append(boundary).append("\r\n");
		sb.append("Content-Disposition: form-data; name=\"busiCode\"").append("\r\n").append("\r\n");
		sb.append(headerMap.get("busiCode")).append("\r\n");
		
		sb.append(boundary).append("\r\n");
		sb.append("Content-Disposition: form-data; name=\"comtProp\"").append("\r\n").append("\r\n");
		sb.append(headerMap.get("comtProp")).append("\r\n");
		
		sb.append(boundary).append("\r\n");
		sb.append("Content-Disposition: form-data; name=\"cont\"").append("\r\n").append("\r\n");
		sb.append(comment).append("\r\n");
		
		sb.append(boundary).append("\r\n");
		sb.append("Content-Disposition: form-data; name=\"contType\"").append("\r\n").append("\r\n");
		sb.append(headerMap.get("contType")).append("\r\n");
		
		sb.append(boundary).append("\r\n");
		sb.append("Content-Disposition: form-data; name=\"gid\"").append("\r\n").append("\r\n");
		sb.append(headerMap.get("gid")).append("\r\n");
		
		sb.append(boundary).append("\r\n");
		sb.append("Content-Disposition: form-data; name=\"id\"").append("\r\n").append("\r\n");
		sb.append(newsId).append("\r\n");
		
		sb.append(boundary).append("\r\n");
		sb.append("Content-Disposition: form-data; name=\"p1\"").append("\r\n").append("\r\n");
		sb.append(headerMap.get("p1")).append("\r\n");
		
		sb.append(boundary).append("\r\n");
		sb.append("Content-Disposition: form-data; name=\"passport\"").append("\r\n").append("\r\n");
		sb.append(headerMap.get("passport")).append("\r\n");
		
		sb.append(boundary).append("\r\n");
		sb.append("Content-Disposition: form-data; name=\"pid\"").append("\r\n").append("\r\n");
		sb.append(headerMap.get("pid")).append("\r\n");
		
		sb.append(boundary).append("\r\n");
		sb.append("Content-Disposition: form-data; name=\"sid\"").append("\r\n").append("\r\n");
		sb.append(headerMap.get("sid")).append("\r\n");
		
		sb.append(boundary).append("\r\n");
		sb.append("Content-Disposition: form-data; name=\"token\"").append("\r\n").append("\r\n");
		sb.append(headerMap.get("token")).append("\r\n");
		
		sb.append(boundary).append("\r\n");
		sb.append("Content-Disposition: form-data; name=\"u\"").append("\r\n").append("\r\n");
		sb.append("1").append("\r\n");
		
		sb.append(boundary).append("\r\n");
		sb.append("Content-Disposition: form-data; name=\"userId\"").append("\r\n").append("\r\n");
		sb.append(headerMap.get("userId")).append("\r\n");
		sb.append("--Boundary+DF717FD94E65C816--").append("\r\n");
		
		openConnection.addRequestProperty("Host", "api.k.sohu.com");
		openConnection.addRequestProperty("Content-Type", "multipart/form-data; boundary=Boundary+DF717FD94E65C816");
		openConnection.addRequestProperty("Cookie", headerMap.get("cookie"));
		openConnection.addRequestProperty("Connection", "keep-alive");
		openConnection.addRequestProperty("Connection", "keep-alive");
		openConnection.addRequestProperty("Accept", "*/*");
		openConnection.addRequestProperty("User-Agent", "sohunews/5.9.0 (iPhone; iOS 9.2; Scale/2.00)");
		openConnection.addRequestProperty("Accept-Language", "zh-Hans-CN;q=1, en-CN;q=0.9");
		openConnection.addRequestProperty("Content-Length",String.valueOf(sb.toString().length()));
		openConnection.addRequestProperty("Accept-Encoding", "gzip, deflate");
		
		
		openConnection.setDoInput(true);
		openConnection.setDoOutput(true);
		openConnection.setUseCaches(false);  
		openConnection.setRequestMethod("POST");
		
		
		PrintWriter pw = new PrintWriter(openConnection.getOutputStream());
		pw.print(sb.toString());
		pw.flush();
		
		Scanner sc = new Scanner(openConnection.getInputStream(),"utf-8");
		
		String result = "";
		while(sc.hasNext()){
			result += sc.nextLine();
		}
		
		JSONObject parseResult = JSON.parseObject(result);
		result = parseResult.getString("isSuccess");
		
		logger.info("sohu comment ios result:::"+result);
		if(result != null && !result.equals("F")){
			MQSender.toMQ(task,"");
		}else{
			
			logger.info("sohu comment ios fail");
			MQSender.toMQ(task,"失败");
		}
		}catch(Exception e){
			
			e.printStackTrace();
			MQSender.toMQ(task,"失败2");
		}
	}
	
	/**
	 * 获取header参数
	 * @param appHeaders header值
	 * @return
	 */
	public static Map<String,String> getHeaderParam(String headers) {
		
		Map<String,String> headerMap = new HashMap<String,String>();
		
		String cookie = headers.substring(headers.indexOf("Cookie")+7, headers.indexOf("Connection")).trim();
		headerMap.put("cookie", cookie);
		
		String apiVersion = headers.substring(headers.indexOf("apiVersion")+11);
		apiVersion = apiVersion.substring(0,apiVersion.indexOf("--Boundary")).trim();
		headerMap.put("apiVersion", apiVersion);
		
		String author = headers.substring(headers.indexOf("author")+7);
		author = author.substring(0,author.indexOf("--Boundary")).trim();
		headerMap.put("author", author);
		
		String bid = headers.substring(headers.indexOf("bid")+4);
		bid = bid.substring(0,bid.indexOf("--Boundary")).trim();
		headerMap.put("bid", bid);
		
		String busiCode = headers.substring(headers.indexOf("busiCode")+9);
		busiCode = busiCode.substring(0,busiCode.indexOf("--Boundary")).trim();
		headerMap.put("busiCode", busiCode);
		
		String comtProp = headers.substring(headers.indexOf("comtProp")+9);
		comtProp = comtProp.substring(0,comtProp.indexOf("--Boundary")).trim();
		headerMap.put("comtProp", comtProp);
		
		String contType = headers.substring(headers.indexOf("contType")+9);
		contType = contType.substring(0,contType.indexOf("--Boundary")).trim();
		headerMap.put("contType", contType);
		
		String gid = headers.substring(headers.indexOf("gid")+4);
		gid = gid.substring(0,gid.indexOf("--Boundary")).trim();
		headerMap.put("gid", gid);
		
		String p1 = headers.substring(headers.indexOf("p1")+3);
		p1 = p1.substring(0,p1.indexOf("--Boundary")).trim();
		headerMap.put("p1", p1);
		
		String passport = headers.substring(headers.indexOf("passport")+9);
		passport = passport.substring(0,passport.indexOf("--Boundary")).trim();
		headerMap.put("passport", passport);
		
		String pid = headers.substring(headers.indexOf("pid")+4);
		pid = pid.substring(0,pid.indexOf("--Boundary")).trim();
		headerMap.put("pid", pid);
		
		String sid = headers.substring(headers.indexOf("sid")+4);
		sid = sid.substring(0,sid.indexOf("--Boundary")).trim();
		headerMap.put("sid", sid);
		
		String token = headers.substring(headers.indexOf("token")+6);
		token = token.substring(0,token.indexOf("--Boundary")).trim();
		headerMap.put("token", token);
		
		String userId = headers.substring(headers.indexOf("userId")+7);
		userId = userId.substring(0,userId.indexOf("--Boundary")).trim();
		headerMap.put("userId", userId);
		
		return headerMap;
	}

	/**
	 * 获得评论相关的参数
	 * @param articleUrl
	 */
	@SuppressWarnings("unused")
	public static JSONObject getCommentParam(String articleUrl) {

		try {
			
			URL url = new URL(articleUrl);
			HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
			openConnection.connect();

			Scanner sc = new Scanner(openConnection.getInputStream(), "utf-8");
			String result = "";
			
			while (sc.hasNext()) {

				result += sc.nextLine() +"\n";
			}
			
			result = result.substring(result.indexOf("CONFIG")+8);
			result = result.substring(0,result.indexOf(";"));
			JSONObject parseResult = JSON.parseObject(result);
		
			return parseResult;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 搜狐新闻评论android
	 * @return
	 */
	public static void sohuCommentAndroid(TaskGuideBean task){
		logger.info("sohu comment android start...");
		try{
		String articleUrl = task.getAddress();
		String content = task.getCorpus();
		System.out.println("corpus=="+content);
		String preUrl = "http://api.k.sohu.com/api/comment/userComment.go";
		
		Map<String,String> headerMap = getHeaderAndroid(task.getAppInfo());
		JSONObject parseData = getCommentParam(articleUrl);
		String newsId = "";
		if(parseData != null){
		newsId = parseData.getString("newsId");
		}
		
		URL url = new URL(preUrl);
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		
		StringBuffer sb = new StringBuffer();
		sb.append("--IHb8p_qsyUUP_uJSW4781NnGE8tzNmgyOjAVfH2u").append("\n");
		sb.append("Content-Disposition: form-data; name=\"id\"").append("\n");
		sb.append("Content-Type: text/plain; charset=US-ASCII").append("\n");
		sb.append("Content-Transfer-Encoding: 8bit").append("\n").append("\n");
		sb.append(newsId).append("\n");
		System.out.println("newsId=="+newsId);
		
		sb.append("--IHb8p_qsyUUP_uJSW4781NnGE8tzNmgyOjAVfH2u").append("\n");
		sb.append("Content-Disposition: form-data; name=\"author\"").append("\n");
		sb.append("Content-Type: text/plain; charset=US-ASCII").append("\n");
		sb.append("Content-Transfer-Encoding: 8bit").append("\n").append("\n");
		sb.append(StringUtil.encode(headerMap.get("author"))).append("\n");
		System.out.println(StringUtil.encode(headerMap.get("author")));
		System.out.println("author=="+StringUtil.encode(headerMap.get("author")));
				
		sb.append("--IHb8p_qsyUUP_uJSW4781NnGE8tzNmgyOjAVfH2u").append("\n");
		sb.append("Content-Disposition: form-data; name=\"comtProp\"").append("\n");
		sb.append("Content-Type: text/plain; charset=US-ASCII").append("\n");
		sb.append("Content-Transfer-Encoding: 8bit").append("\n").append("\n");
		sb.append(headerMap.get("comtProp")).append("\n");
		System.out.println("comtProp=="+headerMap.get("comtProp"));
		
		sb.append("--IHb8p_qsyUUP_uJSW4781NnGE8tzNmgyOjAVfH2u").append("\n");
		sb.append("Content-Disposition: form-data; name=\"token\"").append("\n");
		sb.append("Content-Type: text/plain; charset=US-ASCII").append("\n");
		sb.append("Content-Transfer-Encoding: 8bit").append("\n").append("\n");
		sb.append(headerMap.get("token")).append("\n");
		System.out.println("token=="+headerMap.get("token"));
		
		sb.append("--IHb8p_qsyUUP_uJSW4781NnGE8tzNmgyOjAVfH2u").append("\n");
		sb.append("Content-Disposition: form-data; name=\"p1\"").append("\n");
		sb.append("Content-Type: text/plain; charset=US-ASCII").append("\n");
		sb.append("Content-Transfer-Encoding: 8bit").append("\n").append("\n");
		sb.append(headerMap.get("p1")).append("\n");
		System.out.println("p1=="+headerMap.get("p1"));
		
		sb.append("--IHb8p_qsyUUP_uJSW4781NnGE8tzNmgyOjAVfH2u").append("\n");
		sb.append("Content-Disposition: form-data; name=\"gid\"").append("\n");
		sb.append("Content-Type: text/plain; charset=US-ASCII").append("\n");
		sb.append("Content-Transfer-Encoding: 8bit").append("\n").append("\n");
		sb.append(headerMap.get("gid")).append("\n");
		System.out.println("gid=="+headerMap.get("gid"));
		
		sb.append("--IHb8p_qsyUUP_uJSW4781NnGE8tzNmgyOjAVfH2u").append("\n");
		sb.append("Content-Disposition: form-data; name=\"pid\"").append("\n");
		sb.append("Content-Type: text/plain; charset=US-ASCII").append("\n");
		sb.append("Content-Transfer-Encoding: 8bit").append("\n").append("\n");
		sb.append(headerMap.get("pid")).append("\n");
		System.out.println("pid=="+headerMap.get("pid"));

		sb.append("--IHb8p_qsyUUP_uJSW4781NnGE8tzNmgyOjAVfH2u").append("\n");
		sb.append("Content-Disposition: form-data; name=\"passport\"").append("\n");
		sb.append("Content-Type: text/plain; charset=US-ASCII").append("\n");
		sb.append("Content-Transfer-Encoding: 8bit").append("\n").append("\n");
		sb.append(headerMap.get("passport")).append("\n");
		System.out.println("passport=="+headerMap.get("passport"));
		
		sb.append("--IHb8p_qsyUUP_uJSW4781NnGE8tzNmgyOjAVfH2u").append("\n");
		sb.append("Content-Disposition: form-data; name=\"cont\"").append("\n");
		sb.append("Content-Type: text/plain; charset=US-ASCII").append("\n");
		sb.append("Content-Transfer-Encoding: 8bit").append("\n").append("\n");
		sb.append(URLEncoder.encode(content,"utf-8")).append("\n");
		System.out.println("content=="+URLEncoder.encode(content,"utf-8"));
		
		sb.append("--IHb8p_qsyUUP_uJSW4781NnGE8tzNmgyOjAVfH2u").append("\n");
		sb.append("Content-Disposition: form-data; name=\"busiCode\"").append("\n");
		sb.append("Content-Type: text/plain; charset=US-ASCII").append("\n");
		sb.append("Content-Transfer-Encoding: 8bit").append("\n").append("\n");
		sb.append(headerMap.get("busiCode")).append("\n");
		System.out.println("busiCode=="+headerMap.get("busiCode"));
		
		sb.append("--IHb8p_qsyUUP_uJSW4781NnGE8tzNmgyOjAVfH2u").append("\n");
		sb.append("Content-Disposition: form-data; name=\"contType\"").append("\n");
		sb.append("Content-Type: text/plain; charset=US-ASCII").append("\n");
		sb.append("Content-Transfer-Encoding: 8bit").append("\n").append("\n");
		sb.append(headerMap.get("contType")).append("\n");
		System.out.println("contType=="+headerMap.get("contType"));
		
		sb.append("--IHb8p_qsyUUP_uJSW4781NnGE8tzNmgyOjAVfH2u").append("\n");
		sb.append("Content-Disposition: form-data; name=\"innerIp\"").append("\n");
		sb.append("Content-Type: text/plain; charset=US-ASCII").append("\n");
		sb.append("Content-Transfer-Encoding: 8bit").append("\n").append("\n");
		sb.append(headerMap.get("innerIp")).append("\n");
		System.out.println("innerIp=="+headerMap.get("innerIp"));
		
		sb.append("--IHb8p_qsyUUP_uJSW4781NnGE8tzNmgyOjAVfH2u").append("\n");
		sb.append("Content-Disposition: form-data; name=\"stationId\"").append("\n");
		sb.append("Content-Type: text/plain; charset=US-ASCII").append("\n");
		sb.append("Content-Transfer-Encoding: 8bit").append("\n").append("\n");
		
		sb.append("--IHb8p_qsyUUP_uJSW4781NnGE8tzNmgyOjAVfH2u").append("\n");
		sb.append("Content-Disposition: form-data; name=\"macAaddress\"").append("\n");
		sb.append("Content-Type: text/plain; charset=US-ASCII").append("\n");
		sb.append("Content-Transfer-Encoding: 8bit").append("\n").append("\n");
		sb.append(headerMap.get("macAaddress")).append("\n");
		System.out.println("macAaddress=="+headerMap.get("macAaddress"));
		
		sb.append("--IHb8p_qsyUUP_uJSW4781NnGE8tzNmgyOjAVfH2u").append("\n");
		sb.append("Content-Disposition: form-data; name=\"longitude\"").append("\n");
		sb.append("Content-Type: text/plain; charset=US-ASCII").append("\n");
		sb.append("Content-Transfer-Encoding: 8bit").append("\n").append("\n");
		sb.append(headerMap.get("longitude")).append("\n");
		System.out.println("longitude=="+headerMap.get("longitude"));
		
		sb.append("--IHb8p_qsyUUP_uJSW4781NnGE8tzNmgyOjAVfH2u").append("\n");
		sb.append("Content-Disposition: form-data; name=\"osType\"").append("\n");
		sb.append("Content-Type: text/plain; charset=US-ASCII").append("\n");
		sb.append("Content-Transfer-Encoding: 8bit").append("\n").append("\n");
		sb.append(headerMap.get("osType")).append("\n");
		System.out.println("osType=="+headerMap.get("osType"));
		
		sb.append("--IHb8p_qsyUUP_uJSW4781NnGE8tzNmgyOjAVfH2u").append("\n");
		sb.append("Content-Disposition: form-data; name=\"stationId\"").append("\n");
		sb.append("Content-Type: text/plain; charset=US-ASCII").append("\n");
		sb.append("Content-Transfer-Encoding: 8bit").append("\n").append("\n");
		sb.append(headerMap.get("stationId")).append("\n");
		System.out.println("stationId=="+headerMap.get("stationId"));
		
		sb.append("--IHb8p_qsyUUP_uJSW4781NnGE8tzNmgyOjAVfH2u").append("\n");
		sb.append("Content-Disposition: form-data; name=\"latitude\"").append("\n");
		sb.append("Content-Type: text/plain; charset=US-ASCII").append("\n");
		sb.append("Content-Transfer-Encoding: 8bit").append("\n").append("\n");
		sb.append(headerMap.get("latitude")).append("\n");
		System.out.println("latitude=="+headerMap.get("latitude"));
		sb.append("--IHb8p_qsyUUP_uJSW4781NnGE8tzNmgyOjAVfH2u--").append("\n").append("\n");
		
		System.out.println(sb);
		openConnection.addRequestProperty("User-Agent", "SohuNews/5.9.2 BuildCode/125");
		openConnection.addRequestProperty("Content-Length", String.valueOf(sb.toString().length()));
		openConnection.addRequestProperty("Content-Type", "multipart/form-data; boundary=IHb8p_qsyUUP_uJSW4781NnGE8tzNmgyOjAVfH2u");
		openConnection.addRequestProperty("Host", "api.k.sohu.com");
		openConnection.addRequestProperty("Connection", "Keep-Alive");
		
		openConnection.setDoInput(true);
		openConnection.setDoOutput(true);
		openConnection.setUseCaches(false);
		openConnection.setRequestMethod("POST");
		
		PrintWriter pw = new PrintWriter(openConnection.getOutputStream());
		pw.print(sb.toString());
		pw.flush();
		
		Scanner sc = new Scanner(openConnection.getInputStream(),"utf-8");
		
		String result = "";
		while(sc.hasNext()){
			
			result += sc.nextLine();
		}
		
		logger.info("sohu comment android result :::"+result);
		
		JSONObject parseResult = JSON.parseObject(result);
		result = parseResult.getString("isSuccess");
		
		logger.info("sohu comment ios result:::"+result);
		if(result != null && !result.equals("F")){
			
			logger.info("sohu comment android success...");
			MQSender.toMQ(task,"");
		}else{
			
			logger.info("sohu comment android fail...");
			MQSender.toMQ(task,"失败");
		}
		}catch(Exception e){
			
			logger.info("sohu comment android exception...");
			e.printStackTrace();
			MQSender.toMQ(task,"失败2");
		}
	}
	/**
	 * 获取android的头信息
	 * @param appHeaders
	 * @return
	 */
	private static Map<String, String> getHeaderAndroid(String headers) {
		
		Map<String,String> headerMap = new HashMap<String,String>();
		String author = headers.substring(headers.indexOf("author")+7);
		author = author.substring(0,author.indexOf("--"));
		author = author.substring(author.indexOf("8bit")+4);
		headerMap.put("author", StringUtil.decode(author));
		
		String comtProp = headers.substring(headers.indexOf("comtProp")+9);
		comtProp = comtProp.substring(0,comtProp.indexOf("--"));
		comtProp = comtProp.substring(comtProp.indexOf("8bit")+4);
		headerMap.put("comtProp", comtProp);
		
		String token = headers.substring(headers.indexOf("token")+6);
		token = token.substring(0,token.indexOf("--"));
		token = token.substring(token.indexOf("8bit")+4);
		headerMap.put("token", token);
		
		String p1 = headers.substring(headers.indexOf("p1")+3);
		p1 = p1.substring(0,p1.indexOf("--"));
		p1 = p1.substring(p1.indexOf("8bit")+4);
		headerMap.put("p1", p1);
		
		String passport = headers.substring(headers.indexOf("passport")+9);
		passport = passport.substring(0,passport.indexOf("--"));
		passport = passport.substring(passport.indexOf("8bit")+4);
		headerMap.put("passport", passport);
		
		String cont = headers.substring(headers.indexOf("cont")+5);
		cont = cont.substring(0,cont.indexOf("--"));
		cont = cont.substring(cont.indexOf("8bit")+4);
		headerMap.put("cont", cont);
		
		String busiCode = headers.substring(headers.indexOf("busiCode")+9);
		busiCode = busiCode.substring(0,busiCode.indexOf("--"));
		busiCode = busiCode.substring(busiCode.indexOf("8bit")+4);
		headerMap.put("busiCode", busiCode);
		
		String contType = headers.substring(headers.indexOf("contType")+9);
		contType = contType.substring(0,contType.indexOf("--"));
		contType = contType.substring(contType.indexOf("8bit")+4);
		headerMap.put("contType", contType);
		
		String innerIp = headers.substring(headers.indexOf("innerIp")+8);
		innerIp = innerIp.substring(0,innerIp.indexOf("--"));
		innerIp = innerIp.substring(innerIp.indexOf("8bit")+4);
		headerMap.put("innerIp", innerIp);
		
		String gid = headers.substring(headers.indexOf("gid")+4);
		gid = gid.substring(0,gid.indexOf("--"));
		gid = gid.substring(gid.indexOf("8bit")+4);
		headerMap.put("gid", gid);
		
		String pid = headers.substring(headers.indexOf("pid")+4);
		pid = pid.substring(0,pid.indexOf("--"));
		pid = pid.substring(pid.indexOf("8bit")+4);
		headerMap.put("pid", pid);
		
		String stationId = headers.substring(headers.indexOf("stationId")+10);
		stationId = stationId.substring(0,stationId.indexOf("--"));
		stationId = stationId.substring(stationId.indexOf("8bit")+4);
		headerMap.put("stationId", stationId);
		
		String macAaddress = headers.substring(headers.indexOf("macAaddress")+12);
		macAaddress = macAaddress.substring(0,macAaddress.indexOf("--"));
		macAaddress = macAaddress.substring(macAaddress.indexOf("8bit")+4);
		headerMap.put("macAaddress", macAaddress);
		
		String longitude = headers.substring(headers.indexOf("longitude")+10);
		longitude = longitude.substring(0,longitude.indexOf("--"));
		longitude = longitude.substring(longitude.indexOf("8bit")+4);
		headerMap.put("longitude", longitude);
		
		String osType = headers.substring(headers.indexOf("osType")+7);
		osType = osType.substring(0,osType.indexOf("--"));
		osType = osType.substring(osType.indexOf("8bit")+4);
		headerMap.put("osType", osType);
		
		String latitude = headers.substring(headers.indexOf("latitude")+9);
		latitude = latitude.substring(0,latitude.indexOf("--"));
		latitude = latitude.substring(latitude.indexOf("8bit")+4);
		headerMap.put("latitude", latitude);
		
		return headerMap;
	}

}
