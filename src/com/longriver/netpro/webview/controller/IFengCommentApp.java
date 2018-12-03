package com.longriver.netpro.webview.controller;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * @author rhy
 * @2017-7-5 上午11:49:44
 * @version v1.0
 */
public class IFengCommentApp{

	static Logger logger = Logger.getLogger(IFengCommentApp.class);
	
	public static void main(String[] args) {
		
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("https://share.iclient.ifeng.com/shareNews?forward=1&aid=cmpp_030010051499246&aman=aa3051M4dff3aeF133O594R914dce3q8f60583mbc9");
		task.setAddress("https://share.iclient.ifeng.com/shareNews?forward=1&aid=cmpp_030200051512021&aman=aa4051s4dfe3aez133W59459143ce3s8f6Q5830bc9");
		
//		task.setAddress("https://share.iclient.ifeng.com/news/shareNews?forward=1&aid=125851297&aman=114a612U020a7777368");
//		task.setAddress("https://share.iclient.ifeng.com/news/shareNews?forward=1&aid=125859001&aman=114r6126020P777q368");
//		task.setAddress("https://share.iclient.ifeng.com/news/shareNews?forward=1&aid=125849733&aman=114r612A020k777g368");
		task.setCorpus("完全动态了");
		task.setCorpus("换个链接试试");
		
		task.setCorpus("这小编，是水牙签");
		task.setAppInfo("POST https://icomment.ifeng.com/wappost.php HTTP/1.1"+
						"Host: icomment.ifeng.com"+
						"Content-Type: application/x-www-form-urlencoded"+
						"Accept: */*"+
						"Connection: keep-alive"+
						"Proxy-Connection: keep-alive"+
						"Cookie: sid=6AC8B7C1081E7F9C73E813B9795DFFE1user60891932"+
						"User-Agent: IfengNews/5.6.3 (iPhone; iOS 9.2; Scale/2.00)"+
						"Accept-Language: zh-Hans-CN;q=1, en-CN;q=0.9"+
						"Content-Length: 1257"+
						"Accept-Encoding: gzip, deflate"+
						
						"client=1&content=%E6%9C%80%E8%BF%91%E8%BF%99%E4%B8%AA%E6%96%B0%E9%97%BB%E6%8C%BA%E7%81%AB%E7%9A%84%E5%91%80&docName=%E5%BC%A0%E5%AE%87%EF%BC%9A%E5%A5%BD%E7%9A%84%E6%8A%95%E8%B5%84%E5%B9%B3%E5%8F%B0%20%E5%B8%A6%E4%BD%A0%E8%BF%9C%E7%A6%BB2000%E4%B8%87%E4%BA%BA%E7%9A%84%E5%81%87%E8%A3%85%E7%94%9F%E6%B4%BB&docUrl=http%3A//sn.ifeng.com/a/20170727/5861011_0.shtml&ext2=%7B%0A%20%20%22docId%22%20%3A%20%22imcp_126010836%22%2C%0A%20%20%22docUrl%22%20%3A%20%22https%3A%5C/%5C/api.iclient.ifeng.com%5C/ipadtestdoc?aid%3D126010836%22%2C%0A%20%20%22sub_type%22%20%3A%20%22source%22%2C%0A%20%20%22from%22%20%3A%20%22sj%22%2C%0A%20%20%22isTrends%22%20%3A%200%2C%0A%20%20%22userimg%22%20%3A%20%22%22%2C%0A%20%20%22type%22%20%3A%20%22doc%22%2C%0A%20%20%22device_type%22%20%3A%20%22iPhone8%2C1%22%2C%0A%20%20%22comment_verify%22%20%3A%20%22loc_bj%22%2C%0A%20%20%22sub_name%22%20%3A%20%22%E4%B8%AD%E5%9B%BD%E7%BD%91%22%2C%0A%20%20%22sub_id%22%20%3A%20%22%E4%B8%AD%E5%9B%BD%E7%BD%91%22%2C%0A%20%20%22comment_level%22%20%3A%200%2C%0A%20%20%22guid%22%20%3A%20%2260891932%22%2C%0A%20%20%22nickname%22%20%3A%20%22%22%2C%0A%20%20%22isSync%22%20%3A%201%0A%7D&rt=sj&sid=6AC8B7C1081E7F9C73E813B9795DFFE1user60891932&skey=602234&userName=%E5%AE%A2%E6%88%B7%E7%AB%AF%E7%94%A8%E6%88%B7");
		
//		task.setAppInfo("GET http://icomment.ifeng.com/wappost.php?quoteId=null" +
//				"&docName=%E5%93%8D%E5%BA%94%E7%A9%BA%E8%B0%83%E9%AB%98%E4%B8%80%E5%BA%A6+%E5%95%86%E5%AE%B6%E8%8A%82%E8%83%BD%E5%90%84%E6%9C%89%E6%8B%9B" +
//				"&docUrl=sub_23521967" +
//				"&docId=imcp_125851297" +
//				"&client=1" +
//				"&content=25%E5%BA%A6%E4%B9%9F%E5%86%B7%E7%9A%84%E4%B8%8D%E8%A1%8C" +
//				"&rt=sj" +
//				"&skey=61E37F" +
//				"&ext2=%7B%22comment_level%22%3A%220%22%2C%22comment_verify%22%3A%22local%22%2C%22device_type%22%3A%22Coolpad-8297%22%2C%22docId%22%3A%22imcp_125851297%22%2C%22docUrl%22%3A%22http%3A%2F%2Fapi.iclient.ifeng.com%2Fipadtestdoc%3Faid%5Cu003d125851297%22%2C%22from%22%3A%22sj%22%2C%22guid%22%3A%2262135899%22%2C%22isSync%22%3A%221%22%2C%22isTrends%22%3A%220%22%2C%22lat%22%3A%2239.983606%22%2C%22location%22%3A%22%E5%8C%97%E5%9C%9F%E5%9F%8E%E8%A5%BF%E8%B7%AF%22%2C%22lon%22%3A%22116.392927%22%2C%22nickname%22%3A%22%E6%89%8B%E6%9C%BA%E7%94%A8%E6%88%B75412%22%2C%22sub_id%22%3A%22%E4%B8%9C%E6%96%B9%E7%BD%91-%E7%A4%BE%E4%BC%9A%22%2C%22sub_name%22%3A%22%E4%B8%9C%E6%96%B9%E7%BD%91-%E7%A4%BE%E4%BC%9A%22%2C%22sub_type%22%3A%22source%22%2C%22type%22%3A%22doc%22%7D HTTP/1.1"+
//							"Connection: Close"+
//							"Cookie: sid=EFEA6528451E64C6FC922440C7708DD7user62135899"+
//							"User-Agent: Dalvik/1.6.0 (Linux; U; Android 4.2.2; Coolpad 8297 Build/JDQ39)"+
//							"Host: icomment.ifeng.com"+
//							"Accept-Encoding: gzip"
//
//							);
		try {
//			ifengCommentAndroid(task);
			ifengCommentIos(task);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void toRun(TaskGuideBean task){
		task.setAppInfo(task.getAppInfo().replaceAll("\n", ""));
		if(task.getIsApp()==2){
			ifengCommentIos(task);
		}else{
			ifengCommentAndroid(task);
		}
	}
	/**
	 * 凤凰评论ios
	 * @param content  评论内容
	 * @return
	 * @throws Exception 
	 */
	public static void ifengCommentIos(TaskGuideBean task){
		
		logger.info("ifeng comment ios start...");
		try{
		String aid = getAid(task.getAddress());
		
		Map<String,String> newsMap = getNewsParam("http://api.3g.ifeng.com/ipadtestdoc?aid="+aid+"");
		
		Map<String,String> commentMap = getCommentParam(task.getAppInfo());
		
		String content = task.getCorpus();
		String preUrl = "https://icomment.ifeng.com/wappost.php";
		
		com.longriver.netpro.common.sohu._FakeX509TrustManager.allowAllSSL();
		URL url = new URL(preUrl);
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
//		
		String param = "client=1" +
				"&content="+URLEncoder.encode(content,"utf-8")+""+
				"&docName="+newsMap.get("docName")+"" +
				"&docUrl="+newsMap.get("docUrl")+"" +
				"&ext2={"+
					  "\"docId\" : \'"+newsMap.get("docUrl")+"\',"+
					  "\"docUrl\" : \'"+newsMap.get("edocUrl")+"\',"+
					  "\"sub_type\" : \"\","+
					  "\"from\" : \"\","+
					  "\"isTrends\" : \"\","+
					  "\"userimg\" : \"\","+
					  "\"type\" : \'"+newsMap.get("type")+"\',"+
					  "\"device_type\" : \"iPhone8,1\","+
					  "\"comment_verify\" : \"\","+
					  "\"sub_name\" : \'"+newsMap.get("source")+"\',"+
					  "\"sub_id\" : \'"+newsMap.get("source")+"\',"+
					  "\"comment_level\" : \"\","+
					  "\"guid\" : \"\","+
					  "\"nickname\" : \"\","+
					  "\"isSync\" : 1"+
					"}"+
				"&rt=sj" +
				"&sid=D3D380A0E312CBA464A11A5D6AD35578user62135912" +
				"&skey=" +
				"&userName=客户端用户";
		
		openConnection.addRequestProperty("Host", "icomment.ifeng.com");
		openConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		openConnection.addRequestProperty("Accept", "*/*");
		openConnection.addRequestProperty("Connection", "keep-alive");
		openConnection.addRequestProperty("Proxy-Connection", "keep-alive");
		openConnection.addRequestProperty("Cookie", commentMap.get("cookie"));
		openConnection.addRequestProperty("User-Agent", commentMap.get("userAgent"));
		openConnection.addRequestProperty("Content-Length", String.valueOf(param.length()));
		
		openConnection.setDoInput(true);
		openConnection.setDoOutput(true);
		openConnection.setUseCaches(false);
		openConnection.setRequestMethod("POST");
		
		PrintWriter pw = new PrintWriter(openConnection.getOutputStream());
		pw.print(param);
		pw.flush();
		
		Scanner sc = new Scanner(openConnection.getInputStream(),"utf-8");
		String result = "";
		while(sc.hasNext()){
			
			result +=sc.nextLine();
		}
		
		logger.info("ifeng comment ios result :::"+result);
		
		if(result != null && result.equals("1")){
			
			logger.info("ifeng comment ios success...");
			MQSender.toMQ(task,"");
		}else{
			
			MQSender.toMQ(task,"失败");
		}
		}catch(Exception e){
			
			e.printStackTrace();
			MQSender.toMQ(task,"报错失败");
		}
	}
		
	/**
	 * 获取评论参数
	 * @param appHeaders headers信息
	 * @return
	 */
	private static Map<String, String> getCommentParam(String appHeaders) {

		Map<String,String> commentMap = new HashMap<String,String>();
		
		String cookie = appHeaders.substring(appHeaders.indexOf("Cookie")+7,appHeaders.indexOf("User-Agent")).trim();
        String userAgent = appHeaders.substring(appHeaders.indexOf("User-Agent")+11,appHeaders.indexOf("Accept-Language")).trim();
        
        commentMap.put("cookie", cookie);
        commentMap.put("userAgent",userAgent);
        
		return commentMap;
	}
	/**
	 * 获取新闻id
	 * @param address 新闻链接
	 * @return
	 */
	public static String getAid(String address) {

		String aid = address.substring(address.indexOf("&aid")+5,address.indexOf("&aman"));
		
		return aid;
	}
	/**
	 * 得到评论的相关参数
	 * @param articleUrl
	 * @throws Exception
	 */
	public static Map<String,String> getNewsParam(String articleUrl) throws Exception{
		
		Map<String,String> newsMap = new HashMap<String,String>();
		
		URL url = new URL(articleUrl);
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		openConnection.connect();
		
		Scanner sc = new Scanner(openConnection.getInputStream(),"utf-8");
		
		String result = "";
		while(sc.hasNext()){
			
			result += sc.nextLine();
		}
		JSONObject parseResult = JSON.parseObject(result);
		String meta = parseResult.getString("meta");
		JSONObject parseMeta = JSON.parseObject(meta);
		String id = parseMeta.getString("id");
		String type = parseMeta.getString("type");
		String documentId = parseMeta.getString("documentId");
		
		String body = parseResult.getString("body");
		JSONObject parseBody = JSON.parseObject(body);
		String title = parseBody.getString("title");
		String source = parseBody.getString("source");
		String commentsUrl = parseBody.getString("commentsUrl");
		
		newsMap.put("edocUrl", id);
		newsMap.put("type", type);
		newsMap.put("docId", documentId);
		newsMap.put("docName", title);
		newsMap.put("docUrl", commentsUrl);
		newsMap.put("source", source);
		
		return newsMap;
	}
	/**
	 * 凤凰评论android
	 * @param content  评论内容
	 * @return
	 * @throws Exception
	 */
	public static void ifengCommentAndroid(TaskGuideBean task){
		
		logger.info("ifeng comment android start ...");
		
		try{
			
		String aid = getAid(task.getAddress());
		Map<String,String> newsMap = getNewsParam("http://api.iclient.ifeng.com/ipadtestdoc?aid="+aid+"");
		
		Map<String,Object> headerMap = getCommentAndroid(task.getAppInfo());
		
		String device_type = ((JSONObject)headerMap.get("parseExt")).getString("device_type");
		String lat = ((JSONObject)headerMap.get("parseExt")).getString("lat");
		String location = ((JSONObject)headerMap.get("parseExt")).getString("location");
		String lon = ((JSONObject)headerMap.get("parseExt")).getString("lon");
		String nickname = ((JSONObject)headerMap.get("parseExt")).getString("nickname");
		
		String preUrl = "http://icomment.ifeng.com/wappost.php?quoteId=null" +
				"&docName="+URLEncoder.encode(newsMap.get("docName"),"utf-8")+"" +
				"&docUrl="+newsMap.get("docUrl")+"" +
				"&docId="+newsMap.get("docId")+"" +
				"&client=1" +
				"&content="+URLEncoder.encode(task.getCorpus(),"utf-8")+"" +
				"&rt=sj" +
				"&skey=" +
				"&ext2={\"comment_level\":\"\"," +
						"\"comment_verify\":\"\"," +
								"\"device_type\":\""+device_type+"\"," +
										"\"docId\":\""+newsMap.get("docId")+"\"," +
												"\"docUrl\":\""+newsMap.get("edocUrl")+"\"," +
													"\"from\":\"\"," +
													"\"guid\":\"\"," +
													"\"isSync\":\"1\"," +
													"\"isTrends\":\"\"," +
													"\"lat\":\""+lat+"\"," +
													"\"location\":\""+URLEncoder.encode(location,"utf-8")+"\"," +
													"\"lon\":\""+lon+"\"," +
													"\"nickname\":\""+URLEncoder.encode(nickname,"utf-8")+"\"," +
													"\"sub_id\":\""+URLEncoder.encode(newsMap.get("source"),"utf-8")+"\"," +
													"\"sub_name\":\""+URLEncoder.encode(newsMap.get("source"),"utf-8")+"\"," +
													"\"sub_type\":\"\"," +
													"\"type\":\""+newsMap.get("type")+"\"}";
		
		URL url = new URL(preUrl);
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		
		openConnection.addRequestProperty("Connection", "Close");
		openConnection.addRequestProperty("Cookie", headerMap.get("cookie").toString());
		openConnection.addRequestProperty("User-Agent", headerMap.get("userAgent").toString());
		openConnection.addRequestProperty("Host", "icomment.ifeng.com");
		openConnection.connect();
		
		Scanner sc = new Scanner(openConnection.getInputStream(),"utf-8");
		
		String result = "";
		while(sc.hasNext()){
			
			result += sc.nextLine();
		}
		
		logger.info("ifeng comment android result :::"+result);
		if(result != null && result.equals("1")){
			
			MQSender.toMQ(task,"");
		}else{
			
			MQSender.toMQ(task,"失败");
		}
		}catch(Exception e){
			
			e.printStackTrace();
			MQSender.toMQ(task,"报错失败");
		}
	}

	/**
	 * 获取android的header参数
	 * @param appHeaders header信息
	 * @return
	 */
	private static Map<String, Object> getCommentAndroid(String appHeaders) throws Exception{
		
		Map<String,Object> commentMap = new HashMap<String,Object>();
		
		
		String cookie = appHeaders.substring(appHeaders.indexOf("Cookie")+7,appHeaders.indexOf("User-Agent")).trim();
        String userAgent = appHeaders.substring(appHeaders.indexOf("User-Agent")+11,appHeaders.indexOf("Host")).trim();
        
        String result = URLDecoder.decode(appHeaders,"utf-8");
        
        String ext2 = result.substring(result.indexOf("ext2")+5,result.indexOf("HTTP/1.1")).trim();
    	
    	JSONObject parseExt = JSON.parseObject(ext2);
    	
        commentMap.put("cookie", cookie);
        commentMap.put("userAgent",userAgent);
        commentMap.put("parseExt", parseExt);
        
		return commentMap;
	}
	
}
