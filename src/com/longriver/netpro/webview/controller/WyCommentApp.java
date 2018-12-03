package com.longriver.netpro.webview.controller;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.common.sohu._FakeX509TrustManager;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 网易新闻评论app
 * @author rhy
 * @2017-7-4 上午9:46:49
 * @version v1.0
 */
public class WyCommentApp{

	static Logger logger = Logger.getLogger(WyCommentApp.class);
	
	public static void main(String[] args) {
		
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("https://c.m.163.com/news/a/CPP8NQJ704388CSB.html?spss=newsapp&spsw=1");
//		task.setAddress("https://c.m.163.com/news/a/CPQ3ML8K04388CSA.html?spss=newsapp&spsw=1");
//		task.setAddress("https://c.m.163.com/news/a/CPRP650005128C0H.html?spss=newsapp&spsw=1");
//		task.setAddress("https://c.m.163.com/news/a/CPROMBBK0521HHGL.html?spss=newsapp&spsw=1");
		task.setAppInfo("POST https://comment.api.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads/CQBKHA4Q00258105/app/comments HTTP/1.1"+
						"Host: comment.api.163.com"+
						"User-U: tVl+oG3ytlRJ35qpAVrWa4HneQ4Vgz8zRIWyqxFlJl4="+
						"Content-Type: application/x-www-form-urlencoded; charset=utf-8"+
						"Accept: */*"+
						"User-C: 5pys5Zyw"+
						"Proxy-Connection: keep-alive"+
						"User-N: dXPVwUd66CZx28CELg7kcGHyGkU+H+zP680eQhe05Dws33Wo4yNleT2y/f335nR2"+
						"User-id: KzWxRQxanDVY+3YBxL9l/opYE8Y5FnkmDu1h394q4fT57PSvyhVNzgLgYsHmXiR1Q7Z4Ouny9+XM7yblpX4A694ZRL0MIJQQ8KOb5fiSUJuMVMtqqnzmaVeVeFXjZ10SePBK0dNsyevylzp8V9OOiA=="+
						"User-tk: HhKkk3yENUQVStIg0xbFPY/EWR/Vz9G/1J8rsJXIC0148ErR02zJ6/KXOnxX046I"+
						"Accept-Language: zh-Hans;q=1.0"+
						"Accept-Encoding: gzip;q=1.0, compress;q=0.5"+
						"X-Trace-Id: 1501148474_5371375600_<__NSConcreteUUID 0x1401e3320> E1D6316B-8869-4ED9-A2EF-36FE4E20AEE7"+
						"Content-Length: 442"+
						"User-Agent: NewsApp/25.0 iOS/9.2 (iPhone8,1)"+
						"Connection: keep-alive"+
						"User-D: rZRJVwdP5pmHqKNR6P2Xiw8KK8URxv7TvGEhrMrhxPU1r/qvnEh0+vwl+lF2Eouz"+
						
						"body=%E4%BB%80%E4%B9%88%E6%97%B6%E5%80%99%E5%BC%80%E5%A7%8B%E5%91%80&fingerprint=rZRJVwdP5pmHqKNR6P2Xiw8KK8URxv7TvGEhrMrhxPU1r/qvnEh0%2Bvwl%2BlF2Eouz&fingerprintType=iOS&ursid=61179795AE536550BE2ADBDAC99E22D5EAFAA1832617A5AED00B0620DBA7EFA556EF1BC4BCA66104804E0889C39A2A4B&urstoken=6F327EF3DA324139D36612B01C0AB69B4884F8129D5779F8DF5EC7F1CF2FF50C15001F4221837997D68862268F1FED4F09DE0A3B2683CC989F2CCE3F7DC30266F35C441CBF8EFF41AE79C8C4B6527C80");
		
//		task.setAppInfo("POST https://comment.api.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads/CPRSSUIK05128C0H/app/comments HTTP/1.1"+
//							"data4-Sent-Millis: 1500602277837"+
//							"User-N: dXPVwUd66CZx28CELg7kcHaegtXum0DP+OralgEMsgGQtRCQJ0Ucn3nlEXiWD5Mi"+
//							"User-tk: Hz6pn7B4w1KKw1j88osZC5+KbHIrWaBuFWjnyw/CPjh48ErR02zJ6/KXOnxX046I"+
//							"httpDNSIP: 60.207.246.216"+
//							"Add-To-Queue-Millis: 1500602277809"+
//							"User-id: qJsRFKWixSfHU7IPcWkO43dOc1o8ht86kcJfp9Aq8Edf3DA4dlNzdhQIuUfwdla0ks0Bj9IZXzPjMovgIwUFN+tXIE3sMj+lCkAwCvUcRLAE8dGpw/Sc8ErITq5c6VjnePBK0dNsyevylzp8V9OOiA=="+
//							"User-C: 5pys5Zyw"+
//							"User-U: b1gbagMP8R6OU0ocut+nxRL7HDUxyPKDuqXg8sOgz5U="+
//							"User-D: fG73y5/ITC4CBBKqQmeNkA=="+
//							"User-Agent: NewsApp/24.1 Android/4.2.2 (Coolpad/Coolpad 8297)"+
//							"X-NR-Trace-Id: 1500602277844_1121669208_863777020216411"+
//							"Content-Type: application/x-www-form-urlencoded; charset=UTF-8"+
//							"Content-Length: 672"+
//							"Host: comment.api.163.com"+
//							"Connection: Keep-Alive"+
//							"Accept-Encoding: gzip"+
//							
//							"ursid=78DCB52FF9168DD68670F6E55F733C96D615A8AD7AC8D3E57A4A138E53BCD6A29E7C6780DB3CC1460F067065168D0BB2&modelId=2121&fingerprint=fG73y5%2FITC4CBBKqQmeNkA%3D%3D&body=%E9%82%A3%E5%B0%B1%E5%A4%AA%E5%A5%BD%E4%BA%86%E5%90%A7&urstoken=D3C77A3640872952A0F3C05B783B1F3C482FB68F8AA481F4D194AF824D3D3187A189CB0071743763534DFD20241F5F16820B745B938CF8120967A7125FFBE9FA355B3324896D871AED91119610CC73FB60393F3BB775F70F6C528A9743363BBB77823BEAE8FFAF257DEDD395C1C25511&hidename=false&token=hpEAZKuQc%2F9iIvjMDSxAuhykAZU%3D&nickname=%E6%96%B0%E9%97%BB%E5%AE%A2%E6%88%B7%E7%AB%AF%E7%94%A8%E6%88%B7&userid=kxtre8_qnp%40163.com&from=ph&fingerprintType=android&board=dy_wemedia_bbs&ip=0.0.0.0&");
		task.setCorpus("环境应该不错");
		try {
			wangyiCommentIos(task);
//			wangyiCommentAndriod(task);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void toRun(TaskGuideBean task){
		if(task.getAppInfo()!=null)
			task.setAppInfo(task.getAppInfo().replaceAll("\n", ""));
		if(task.getIsApp()==2){
			wangyiCommentIos(task);
		}else{
			wangyiCommentAndriod(task);
		}
	}
	/**
	 * 网易新闻评论ios
	 * @param task
	 */
	public static void wangyiCommentIos(TaskGuideBean task){

		logger.info("wangyi comment app start...");
		_FakeX509TrustManager.allowAllSSL();
		try{
		String newsId = getNewsId(task.getAddress());
		String comment = task.getCorpus();
		String preUrl = "https://comment.api.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads/"+newsId+"/app/comments";
		
		Map<String,String> headerMap = getHeaderParam(task.getAppInfo());
		
		String param = "body="+comment+"" +
				"&fingerprint="+headerMap.get("fingerprint")+"" +
				"&fingerprintType="+headerMap.get("fingerprintType")+"" +
				"&ursid="+headerMap.get("ursid")+"" +
				"&urstoken="+headerMap.get("urstoken");

		URL url = new URL(preUrl);
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		
		openConnection.addRequestProperty("Host","comment.api.163.com");
		openConnection.addRequestProperty("User-U",headerMap.get("useru"));
		openConnection.addRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=utf-8");
		openConnection.addRequestProperty("Accept","*/*");
		openConnection.addRequestProperty("User-C",headerMap.get("userc"));
		openConnection.addRequestProperty("Proxy-Connection","keep-alive");
		openConnection.addRequestProperty("User-N",headerMap.get("usern"));
		openConnection.addRequestProperty("User-id",headerMap.get("userId"));
		openConnection.addRequestProperty("User-tk",headerMap.get("userTk"));
		openConnection.addRequestProperty("Accept-Language","zh-Hans;q=1.0");
//		hearders.put("Accept-Encoding","gzip;q=1.0, compress;q=0.5");
		openConnection.addRequestProperty("X-Trace-Id",headerMap.get("xtraceId"));
		openConnection.addRequestProperty("Content-Length",String.valueOf(param.length()));
		openConnection.addRequestProperty("User-Agent","NewsApp/24.3 iOS/9.2 (iPhone8,1)");
		openConnection.addRequestProperty("Connection","keep-alive");
		openConnection.addRequestProperty("User-D",headerMap.get("userd"));
		
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
			
			result += sc.nextLine();
		}
		
		logger.info("wangyi comment app result :::"+result);
		
		JSONObject parseResult = JSON.parseObject(result);
		String code = parseResult.getString("code");
		
		if(code != null && code.equals("1")){
			
			logger.info("wangyi comment ios success...");
			MQSender.toMQ(task,"");
		}else{
			
			logger.info("wangyi comment ios fail...");
			MQSender.toMQ(task,"失败");
		}
		}catch(Exception e){
		
			e.printStackTrace();
			logger.info("wangyi comment ios exception...");
			MQSender.toMQ(task,"失败2");
		}
			
	}
	
	/**
	 * 获取头信息
	 * @param appHeaders
	 * @return
	 */
	private static Map<String, String> getHeaderParam(String headers) {

		Map<String,String> headerMap = new HashMap<String,String>();
		
		String useru = headers.substring(headers.indexOf("User-U")+7,headers.indexOf("Content-Type")).trim(); 
		headerMap.put("useru", useru);
		
		String userc = headers.substring(headers.indexOf("User-C")+7,headers.indexOf("Proxy-Connection")).trim();
		headerMap.put("userc", userc);
		
		String usern = headers.substring(headers.indexOf("User-N")+7,headers.indexOf("User-id")).trim();
		headerMap.put("usern", usern);
		
		String userId = headers.substring(headers.indexOf("User-id")+8,headers.indexOf("User-tk")).trim();
		headerMap.put("userId", userId);
		
		String userTk = headers.substring(headers.indexOf("User-tk")+8,headers.indexOf("Accept-Language")).trim();
		headerMap.put("userTk", userTk);
		
		String xtraceId = headers.substring(headers.indexOf("X-Trace-Id")+11,headers.indexOf("Content-Length")).trim();
		headerMap.put("xtraceId", xtraceId);
		
		String userd = headers.substring(headers.indexOf("User-D")+7,headers.indexOf("body")).trim();
		headerMap.put("userd", userd);
		
		String fingerprint = headers.substring(headers.indexOf("fingerprint")+12,headers.indexOf("fingerprintType")-1).trim();
		headerMap.put("fingerprint", fingerprint);
		
		String fingerprintType = headers.substring(headers.indexOf("fingerprintType")+16,headers.indexOf("ursid")-1).trim();
		headerMap.put("fingerprintType", fingerprintType);
		
		String ursid = headers.substring(headers.indexOf("ursid")+6,headers.indexOf("urstoken")-1).trim();
		headerMap.put("ursid", ursid);
		
		String urstoken = headers.substring(headers.indexOf("urstoken")+9).trim();
		headerMap.put("urstoken", urstoken);
		
		return headerMap;
	}

	/**
	 * 获取评论
	 * @param decode
	 * @return
	 */
	static String getNewsId(String articleUrl){
		
		articleUrl = articleUrl.substring(0, articleUrl.lastIndexOf(".html"));
		String newsId = articleUrl.substring(articleUrl.lastIndexOf("/")+1);
		return newsId;
	}

	/**
	 * 网易新闻android
	 * @return
	 */
	public static void wangyiCommentAndriod(TaskGuideBean task){
		
		logger.info("wangyi comment android start...");
		_FakeX509TrustManager.allowAllSSL();
		try{
			
		String newId = getNewsId(task.getAddress());
		String preUrl = "https://comment.api.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads/"+newId+"/app/comments";
		
		String comment = task.getCorpus();
		URL url = new URL(preUrl);
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		
		Map<String,String> headerMap = getHeaderAndroid(task.getAppInfo());
		String param = "ursid="+headerMap.get("ursid")+"" +
				"&modelId="+headerMap.get("medelId")+"" +
				"&fingerprint="+headerMap.get("fingerprint")+"" +
				"&body="+URLDecoder.decode(comment,"utf-8")+"" +
				"&urstoken="+headerMap.get("urstoken")+"" +
				"&hidename=false" +
				"&token="+headerMap.get("token")+"" +
				"&nickname="+headerMap.get("nickname")+"" +
				"&userid="+headerMap.get("userid")+"" +
				"&from="+headerMap.get("from")+"" +
				"&fingerprintType=android" +
				"&board="+headerMap.get("board")+"" +
				"&ip=0.0.0.0";
		//pgv_pvi=3178511360
		openConnection.addRequestProperty("data4-Sent-Millis", String.valueOf(new Date().getTime()));
		openConnection.addRequestProperty("User-N", headerMap.get("usern"));
		openConnection.addRequestProperty("User-tk", headerMap.get("userTk"));
//		openConnection.addRequestProperty("User-L", headerMap.get("userl"));
		openConnection.addRequestProperty("httpDNSIP", headerMap.get("httpDnsIp"));
		openConnection.addRequestProperty("Add-To-Queue-Millis", String.valueOf(new Date().getTime()));
		openConnection.addRequestProperty("User-id", headerMap.get("userId"));
		openConnection.addRequestProperty("User-C", headerMap.get("userc"));
		openConnection.addRequestProperty("User-U", headerMap.get("useru"));
		openConnection.addRequestProperty("User-D", headerMap.get("userd"));
		openConnection.addRequestProperty("User-Agent", headerMap.get("userAgent"));
		openConnection.addRequestProperty("X-NR-Trace-Id", headerMap.get("xnrTraceId"));
		openConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		openConnection.addRequestProperty("Content-Length", String.valueOf(param.length()));
		openConnection.addRequestProperty("Host", "comment.api.163.com");
		openConnection.addRequestProperty("Connection", "Keep-Alive");
//		openConnection.addRequestProperty("Accept-Encoding", "gzip");
			
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
			
			result += sc.nextLine();
		}
		
		logger.info("wangyi comment andriod result :::"+result);
		
		JSONObject parseResult = JSON.parseObject(result);
		String code = parseResult.getString("code");
		
		if(code != null && code.equals("1")){
			
			logger.info("wangyi comment andriod success...");
			MQSender.toMQ(task,"");
		}else{
			
			logger.info("wangyi comment andriod fail...");
			MQSender.toMQ(task,"失败");
		}
		}catch(Exception e){
			
			e.printStackTrace();
			logger.info("wangyi comment android exception...");
			MQSender.toMQ(task,"失败2");
		}
	}
	/**
	 * 获取头信息
	 * @param appHeaders
	 * @return
	 */
	private static Map<String, String> getHeaderAndroid(String headers) {
		
		Map<String,String> headerMap = new HashMap<String,String>();
		
		String usern = headers.substring(headers.indexOf("User-N")+7,headers.indexOf("User-tk")).trim(); 
		headerMap.put("usern", usern);
		
		String userTk = headers.substring(headers.indexOf("User-tk")+8,headers.indexOf("httpDNSIP")).trim();
		headerMap.put("userTk", userTk);
		
//		String userl = headers.substring(headers.indexOf("User-L")+7,headers.indexOf("httpDNSIP")).trim();
//		headerMap.put("userl", userl);
		
		String httpDnsIp = headers.substring(headers.indexOf("httpDNSIP")+10,headers.indexOf("Add-To-Queue-Millis")).trim();
		headerMap.put("httpDnsIp", httpDnsIp);
		
		String userId = headers.substring(headers.indexOf("User-id")+8,headers.indexOf("User-C")).trim();
		headerMap.put("userId", userId);
		
		String userc = headers.substring(headers.indexOf("User-C")+7,headers.indexOf("User-U")).trim();
		headerMap.put("userc", userc);
		
		String useru = headers.substring(headers.indexOf("User-U")+7,headers.indexOf("User-D")).trim();
		headerMap.put("useru", useru);
		
		String userd = headers.substring(headers.indexOf("User-D")+7,headers.indexOf("User-Agent")).trim();
		headerMap.put("userd", userd);
		
		String userAgent = headers.substring(headers.indexOf("User-Agent")+11,headers.indexOf("X-NR-Trace-Id")).trim();
		headerMap.put("userAgent", userAgent);
		
		String xnrTraceId = headers.substring(headers.indexOf("X-NR-Trace-Id")+14,headers.indexOf("Content-Type")).trim();
		headerMap.put("xnrTraceId", xnrTraceId);
		
		String ursid = headers.substring(headers.indexOf("ursid")+6,headers.indexOf("modelId")-1).trim();
		headerMap.put("ursid", ursid);
		
		String medelId = headers.substring(headers.indexOf("modelId")+8,headers.indexOf("fingerprint")-1).trim();
		headerMap.put("medelId", medelId);
		
		String fingerprint = headers.substring(headers.indexOf("fingerprint")+12,headers.indexOf("body")-1).trim();
		headerMap.put("fingerprint", fingerprint);
		
		String urstoken = headers.substring(headers.indexOf("urstoken")+9,headers.indexOf("hidename")-1).trim();
		headerMap.put("urstoken", urstoken);
		
		String token = headers.substring(headers.indexOf("&token")+7, headers.indexOf("nickname")-1).trim();
		headerMap.put("token", token);
		
		String nickname = headers.substring(headers.indexOf("nickname")+9,headers.indexOf("userid")-1).trim();
		headerMap.put("nickname", nickname);
		
		String userid = headers.substring(headers.indexOf("userid")+7,headers.indexOf("from")-1).trim();
		headerMap.put("userid", userid);
		
		String from = headers.substring(headers.indexOf("from")+5,headers.indexOf("fingerprintType")-1).trim();
		headerMap.put("from", from);
		
		String board = headers.substring(headers.indexOf("board")+6,headers.indexOf("&ip")).trim();
		headerMap.put("board", board);
		
		return headerMap;
	}
}
