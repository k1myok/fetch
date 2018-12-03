package com.longriver.netpro.webview.controller;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.longriver.netpro.common.sohu._FakeX509TrustManager;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 网易新闻点赞app
 * @author rhy
 * @2017-7-4 下午2:49:17
 * @version v1.0
 */
public class WyPraiseApp {

	static Logger logger = Logger.getLogger(WyPraiseApp.class);
	
	public static void main(String[] args) {
		
		TaskGuideBean task = new TaskGuideBean();
		//commentId=
		task.setAddress("https://c.m.163.com/news/a/CPQ3ML8K04388CSA.html?spss=newsapp&spsw=1");
//		task.setAddress("https://c.m.163.com/news/a/CPP8NQJ704388CSB.html?spss=newsapp&spsw=1");
		task.setAddress("https://c.m.163.com/news/a/CPS83L81000187VI.html?spss=newsapp&spsw=1");
		
//		task.setAddress("https://c.m.163.com/news/a/CPS4IG010001875O.html?spss=newsapp&spsw=1");
//		task.setAddress("https://c.m.163.com/news/a/CPS1N0H90005877V.html?spss=newsapp&spsw=1");
		task.setPraiseWho("132740107");
//		task.setPraiseWho("132345985");
		task.setPraiseWho("133204722");
		task.setPraiseWho("133287242");
		
		task.setAppInfo("POST https://comment.api.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads/CPS83L81000187VI/app/comments HTTP/1.1"+
							"Host: comment.api.163.com"+
							"User-U: Tthv3T+um3b/LjnL0iIgBQ=="+
							"Content-Type: application/x-www-form-urlencoded; charset=utf-8"+
							"Accept: */*"+
							"User-C: 5pys5Zyw"+
							"Proxy-Connection: keep-alive"+
							"User-N: dXPVwUd66CZx28CELg7kcGHyGkU+H+zP680eQhe05Dws33Wo4yNleT2y/f335nR2"+
							"User-id: 3pox9VsN/mcUO6qIfCXt2lzGDPymMwIllNC0R63CP5wxN9Rkxp9pj69yukxBQkuKU6v1LyI5NlCG64sNoEvUletXIE3sMj+lCkAwCvUcRLAE8dGpw/Sc8ErITq5c6VjnePBK0dNsyevylzp8V9OOiA=="+
							"User-tk: W4ibxLieVFCW8ZUUKaF8SZaCjFJDpcZHzKE3LaPa5DR48ErR02zJ6/KXOnxX046I"+
							"Accept-Language: zh-Hans;q=1.0"+
							"Accept-Encoding: gzip;q=1.0, compress;q=0.5"+
							"X-Trace-Id: 1500619499_5778847904_<__NSConcreteUUID 0x1587d0da0> E1D6316B-8869-4ED9-A2EF-36FE4E20AEE7"+
							"Content-Length: 451"+
							"User-Agent: NewsApp/24.3 iOS/9.2 (iPhone8,1)"+
							"Connection: keep-alive"+
							"User-D: rZRJVwdP5pmHqKNR6P2Xiw8KK8URxv7TvGEhrMrhxPU1r/qvnEh0+vwl+lF2Eouz"+
							
							"body=%E9%A3%9E%E6%9C%BA%E4%B9%9F%E6%98%AF%E9%82%A3%E4%B8%9C%E8%A5%BF%E5%91%80&fingerprint=rZRJVwdP5pmHqKNR6P2Xiw8KK8URxv7TvGEhrMrhxPU1r/qvnEh0%2Bvwl%2BlF2Eouz&fingerprintType=iOS&ursid=388213AACF35419CF3E3811D24CEAED0C6B1BEF357BFCFAB5AA56DF9C1F531009E7C6780DB3CC1460F067065168D0BB2&urstoken=C453CA543A0DE86BF50E01C51F27F4A45E7102F54C57B6894B43E357A8985BE21FAA205EFBD455D2406CE43A062EEB76B94D1CC8AB06AE43BC7E526EEE0A0B8B0B78B3AF4E8607D1C052D1D0B47D3D2D");
		
		task.setAppInfo("POST https://comment.api.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads/CPSD0TML0514DL55/app/comments HTTP/1.1"+
							"data4-Sent-Millis: 1500618084250"+
							"User-N: dXPVwUd66CZx28CELg7kcHaegtXum0DP+OralgEMsgGQtRCQJ0Ucn3nlEXiWD5Mi"+
							"User-tk: cHVAbKawo0zVtzBH2BkMqX4zzURw8J9BGo45kDMj0Ql48ErR02zJ6/KXOnxX046I"+
							"httpDNSIP: 116.242.0.151"+
							"Add-To-Queue-Millis: 1500618083932"+
							"User-id: qJsRFKWixSfHU7IPcWkO43dOc1o8ht86kcJfp9Aq8Edf3DA4dlNzdhQIuUfwdla0ks0Bj9IZXzPjMovgIwUFN+tXIE3sMj+lCkAwCvUcRLAE8dGpw/Sc8ErITq5c6VjnePBK0dNsyevylzp8V9OOiA=="+
							"User-C: 6KaB6Ze7OjropoHpl7s%3D"+
							"User-U: jUdpBWRsIw4sSplMgBsVzhL7HDUxyPKDuqXg8sOgz5U="+
							"User-D: fG73y5/ITC4CBBKqQmeNkA=="+
							"User-Agent: NewsApp/24.1 Android/4.2.2 (Coolpad/Coolpad 8297)"+
							"X-NR-Trace-Id: 1500618084288_1161425752_863777020216411"+
							"Content-Type: application/x-www-form-urlencoded; charset=UTF-8"+
							"Content-Length: 665"+
							"Host: comment.api.163.com"+
							"Connection: Keep-Alive"+
							"Accept-Encoding: gzip"+
							
							"ursid=78DCB52FF9168DD68670F6E55F733C96D615A8AD7AC8D3E57A4A138E53BCD6A29E7C6780DB3CC1460F067065168D0BB2&modelId=2121&fingerprint=fG73y5%2FITC4CBBKqQmeNkA%3D%3D&body=%E5%A5%BD%E5%8F%AF%E6%80%95%E5%91%80%E7%9D%80&urstoken=F0BF267CB99B3A1979667CD3388A69269C40AD9920B51E8F7984C3B1ADE64393BF7FD56B1FFFA47C2603D503ED64ECAA820B745B938CF8120967A7125FFBE9FA355B3324896D871AED91119610CC73FB60393F3BB775F70F6C528A9743363BBB77823BEAE8FFAF257DEDD395C1C25511&hidename=false&token=gGURPp%2FUrUM7mlvFanB1d9%2BFAX4%3D&nickname=%E6%96%B0%E9%97%BB%E5%AE%A2%E6%88%B7%E7%AB%AF%E7%94%A8%E6%88%B7&userid=e_1x16__2m%40163.com&from=ph&fingerprintType=android&board=dy_wemedia_bbs&ip=0.0.0.0&");
		try {
//			wangyiPriseIos(task);
			wangyiPriseAndroid(task);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void toRun(TaskGuideBean task){
		if(task.getAppInfo()!=null)
			task.setAppInfo(task.getAppInfo().replaceAll("\n", ""));
		if(task.getIsApp()==2){
			wangyiPriseIos(task);
		}else{
			wangyiPriseAndroid(task);
		}
	}
	/**
	 * 
	 * @param task
	 * @return
	 * @throws Exception
	 */
	public static void wangyiPriseIos(TaskGuideBean task){
		
		logger.info("wangyi prise ios start...");                                     
		_FakeX509TrustManager.allowAllSSL();
		try{
		String newsId = WyCommentApp.getNewsId(task.getAddress());
		
		String preUrl = "https://comment.api.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads/"+newsId+"/app/comments/"+newsId+"_"+task.getPraiseWho()+"/action/upvote";
		URL url = new URL(preUrl);
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		
		Map<String,String> headerMap = getHeaderIos(task.getAppInfo());
		//ios
		String param = "fingerprint="+headerMap.get("fingerprint")+"" +
					   "&fingerprintType="+headerMap.get("fingerprintType")+"" +
					   "&ursid="+headerMap.get("ursid")+"" +
					   "&urstoken="+headerMap.get("urstoken")+"";

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
		
		logger.info("wangyi prise ios result :::"+result);
		if(result != null && result.equals("")){
			
			logger.info("wangyi praise ios success...");
			MQSender.toMQ(task,"");
		}else{
			
			logger.info("wangyi praise ios fail...");
			MQSender.toMQ(task,"失败");
		}
		}catch(Exception e){
			e.printStackTrace();
			logger.info("wangyi praise ios exception...");
			MQSender.toMQ(task,"失败2");
		}
	}

	/**
	 * 获取ios头信息
	 * @param appHeaders
	 * @return
	 */
	private static Map<String, String> getHeaderIos(String headers) {

		Map<String,String> headerMap = new HashMap<String, String>();
		
		String fingerprint = headers.substring(headers.indexOf("fingerprint")+12,headers.indexOf("fingerprintType")-1);
		headerMap.put("fingerprint", fingerprint);
		
		String fingerprintType = headers.substring(headers.indexOf("fingerprintType")+16,headers.indexOf("&ursid")-1);
		headerMap.put("fingerprintType", fingerprintType);
		
		String ursid = headers.substring(headers.indexOf("ursid")+6,headers.indexOf("urstoken")-1);
		headerMap.put("ursid", ursid);
		
		String urstoken = headers.substring(headers.indexOf("urstoken")+9);
		headerMap.put("urstoken", urstoken);
		
		return headerMap;
	}
	/**
	 * 网易点赞android
	 * @param task
	 * @return
	 * @throws Exception
	 */
	public static void wangyiPriseAndroid(TaskGuideBean task){
		
		logger.info("wangyi praise android start...");
		_FakeX509TrustManager.allowAllSSL();
		try{
		String newsId = WyCommentApp.getNewsId(task.getAddress());
		
		String preUrl = "https://comment.api.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads/"+newsId+"/app/comments/"+newsId+"_"+task.getPraiseWho()+"/action/upvote";
		URL url = new URL(preUrl);
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		
		Map<String,String> headerMap = getHeaderAndroid(task.getAppInfo());

		//android
		String param = "fingerprintType="+headerMap.get("fingerprintType")+"" +
				"&ursid="+headerMap.get("ursid")+"" +
				"&fingerprint="+headerMap.get("fingerprint")+"" +
				"&urstoken="+headerMap.get("urstoken")+"";
		
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
		
		logger.info("wangyi praise android result :::"+result);
		if(result != null && result.equals("")){
			
			logger.info("wangyi praise android success...");
			MQSender.toMQ(task,"");
		}else{
			
			logger.info("wangyi praise android fail...");
			MQSender.toMQ(task,"失败");
		}
		}catch(Exception e){
			
			e.printStackTrace();
			logger.info("wangyi prise android exception...");
			MQSender.toMQ(task,"失败2");
		}
	}

	/**
	 * 获取android头信息
	 * @param appHeaders
	 * @return
	 */
	private static Map<String, String> getHeaderAndroid(String headers) {
		
		Map<String,String> headerMap = new HashMap<String,String>();
		
		String fingerprintType = headers.substring(headers.indexOf("fingerprintType")+16,headers.indexOf("board")-1);
		headerMap.put("fingerprintType", fingerprintType);
		
		String ursid = headers.substring(headers.indexOf("ursid")+6,headers.indexOf("modelId")-1);
		headerMap.put("ursid", ursid);
		
		String fingerprint = headers.substring(headers.indexOf("fingerprint")+12,headers.indexOf("body")-1);
		headerMap.put("fingerprint", fingerprint);
		
		String urstoken = headers.substring(headers.indexOf("urstoken")+9,headers.indexOf("hidename")-1);
		headerMap.put("urstoken", urstoken);
		
		return headerMap;
	}

}
