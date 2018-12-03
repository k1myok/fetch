package com.longriver.netpro.webview.controller;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 腾讯新闻点赞app
 * @author rhy
 * @2017-7-3 上午11:39:20
 * @version v1.0
 */
public class TengxunPraiseApp {

	static Logger logger = Logger.getLogger(TengxunPraiseApp.class);
	//点赞需要replay_id,且一个用户只能点赞一次
	public static void main(String[] args) {
		
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("https://view.inews.qq.com/a/BJC2017072302156704");
		task.setAddress("https://view.inews.qq.com/a/20170726A00Y4G00");
		
		task.setAddress("https://view.inews.qq.com/a/20170725A00VXW00");
		task.setAddress("https://view.inews.qq.com/a/20170726A00Y4G00");
		
		task.setPraiseWho("6294798248700455304");//reply_id
		task.setPraiseWho("6295761571830748010");
		
//		task.setPraiseWho("6295413116843407541");
		task.setPraiseWho("6295776276431717240");
		task.setAppInfo("POST http://w.inews.qq.com/shareQQNewsMulti?apptype=ios&startarticleid=&__qnr=1f287fa9ea14&global_info=0%7C&omgid=222809444b46884db68aa8c674d2383222600010112603&idfa=9CB3856F-83EC-4194-9533-1EA3FD4150AA&qqnews_refpage=QNCommonListController&isJailbreak=0&appver=9.2_qqnews_5.3.7&network_type=wifi&device_model=iPhone8%2C1&omgbizid=8cc4857ebc8f2e42004bec2df8bc2a35be580060112603&screen_height=667&devid=BCD310C0-A5C0-4999-8F99-B1CA7B63BBF1&screen_scale=2&screen_width=375&store=1&activefrom=icon HTTP/1.1"+
							"Host: w.inews.qq.com"+
							"Content-Type: application/x-www-form-urlencoded"+
							"Accept: */*"+
							"Connection: keep-alive"+
							"qn-sig: 64FD824717E3BBA9EA88411E224BFB52"+
							"idfa: 9CB3856F-83EC-4194-9533-1EA3FD4150AA"+
							"appver: 9.2_qqnews_5.3.7"+
							"Accept-Language: zh-Hans-CN;q=1, en-CN;q=0.9"+
							"qn-rid: 1f287fa9f388"+
							"qqnetwork: wifi"+
							"deviceToken: <eca6fca6 0cd2c789 21c25859 483e93ec 7ec4288d cd1d85e5 3e70563b a28ec4e8>"+
							"devid: bcd310c0-a5c0-4999-8f99-b1ca7b63bbf1"+
							"User-Agent: QQNews/5.3.7 (iPhone; iOS 9.2; Scale/2.00)"+
							"Referer: http://inews.qq.com/inews/iphone/"+
							"Content-Length: 2612"+
							"Accept-Encoding: gzip, deflate"+
							"Connection: keep-alive"+
							"Cookie: luin=o2051106923;%20lskey=00030000222442ef2b60b02782d6da020756826eda96a1e40d8e73819d2bc63e8ef214a85064654d4c425017;%20uin=o2051106923;%20skey=MfTTKYVIS8;%20a2=1717f4560cd896a102612bb69b7be06f0c565997290d55929d202265c51122d7d7453ca370633ece13c197d9cee8cb690591a601bc42681c2addd2b0d598e3bc93d3e7471c291e1a;%20logintype=0"+
							"store: 1"+
							
	"						graphicLiveChlid=&seq_no=221401054975%24%24%241-3--%E5%8C%97%E4%BA%AC-1&title=%E5%8C%97%E4%BA%AC%E4%BB%8A%E6%9C%89%E4%B8%AD%E9%9B%A8%E5%BD%B1%E5%93%8D%E6%97%A9%E9%AB%98%E5%B3%B0%20%E5%8F%91%E5%B8%83%E5%9C%B0%E8%B4%A8%E7%81%BE%E5%AE%B3%E8%93%9D%E8%89%B2%E9%A2%84%E8%AD%A6&url=https%3A%2F%2Fview.inews.qq.com%2Fa%2F20170726A00Y4G00&aType=dingyue&summary=%E4%B8%AD%E5%9B%BD%E5%A4%A9%E6%B0%94%E7%BD%91%E8%AE%AF%20%E4%BB%8A%E5%A4%A9%EF%BC%8826%E6%97%A5%EF%BC%89%E5%8C%97%E4%BA%AC%E5%B8%82%E5%A4%A7%E9%83%A8%E5%88%86%E5%9C%B0%E5%8C%BA%E6%9C%89%E4%B8%AD%E9%9B%A8%EF%BC%8C%E5%B1%80%E5%9C%B0%E7%9F%AD%E6%97%B6%E9%9B%A8%E5%BC%BA%E8%BE%83%E5%A4%A7%EF%BC%8C%E5%B9%B6%E5%8F%AF%E8%83%BD%E4%BC%B4%E6%9C%89%E5%BC%B1%E9%9B%B7%E7%94%B5%E3%80%82%E5%AF%B9%E6%97%A9%E9%AB%98%E5%B3%B0%E5%87%BA%E8%A1%8C%E6%9C%89%E4%B8%80%E5%AE%9A%E5%BD%B1%E5%93%8D%EF%BC%8C%E8%AF%B7%E5%B0%BD%E9%87%8F%E9%80%89%E6%8B%A9%E5%85%AC%E5%85%B1%E4%BA%A4%E9%80%9A%E5%B7%A5%E5%85%B7%E5%87%BA%E8%A1%8C%EF%BC%8C%E6%B3%A8%E6%84%8F%E4%BA%A4%E9%80%9A%E5%AE%89%E5%85%A8%E3%80%82%E5%90%8C%E6%97%B6%E5%8F%97%E9%99%8D%E6%B0%B4%E5%BD%B1%E5%93%8D%EF%BC%8C%E4%BB%8A%E5%A4%A9%E6%B0%94%E6%B8%A9%E4%B8%8B%E9%99%8D%E6%98%8E%E6%98%BE%EF%BC%8C%E6%9C%80%E9%AB%98%E6%B0%94%E6%B8%A9%E9%A2%84%E8%AE%A1%E5%8F%AA%E6%9C%8924%E2%84%83%E5%B7%A6%E5%8F%B3%EF%BC%8C%E4%BD%93%E6%84%9F%E6%B8%85%E5%87%89%E3%80%82%E8%87%AA%E6%98%A8%E5%A4%A9%E5%90%8E%E5%8D%8A%E5%A4%9C%E5%BC%80%E5%A7%8B%EF%BC%8C%E5%8C%97%E4%BA%AC%E9%83%A8%E5%88%86%E5%9C%B0%E5%8C%BA%E5%BC%80%E5%A7%8B%E5%87%BA%E7%8E%B0%E9%99%8D%E6%B0%B4%E5%A4%A9%E6%B0%94%EF%BC%8C26%E6%97%A500%E6%97%B6%E8%87%B306%E6%97%B6%E5%85%A8%E5%B8%82%E5%B9%B3%E5%9D%87%E9%99%8D%E9%9B%A8%E9%87%8F1....&img=http%3A%2F%2Finews.gtimg.com%2Fnewsapp_bt%2F0%2F1841907361%2F641&article_id=20170726A00Y4G00&shareType=qqcomment&seq_str=5E217D961135E1A0C859ED70AB788BF1&chlid=news_news_bj&comment_id=2046499411&shareInfo2wx=&alg_version=6&media_id=5017616&reasonInfo=&content_qqweibo=%E6%9C%80%E5%A4%A7%E7%9A%84%E8%B0%8E%E8%A8%80%20%7C%7C%20%23%E8%85%BE%E8%AE%AF%E6%96%B0%E9%97%BB%23%20%E5%8C%97%E4%BA%AC%E4%BB%8A%E6%9C%89%E4%B8%AD%E9%9B%A8%E5%BD%B1%E5%93%8D%E6%97%A9%E9%AB%98%E5%B3%B0%20%E5%8F%91%E5%B8%83%E5%9C%B0%E8%B4%A8%E7%81%BE%E5%AE%B3%E8%93%9D%E8%89%B2%E9%A2%84%E8%AD%A6%20https%3A%2F%2Fview.inews.qq.com%2Fa%2F20170726A00Y4G00&openWeibo=no&desc=%E6%9C%80%E5%A4%A7%E7%9A%84%E8%B0%8E%E8%A8%80%7C%7C%E5%8C%97%E4%BA%AC%E4%BB%8A%E6%9C%89%E4%B8%AD%E9%9B%A8%E5%BD%B1%E5%93%8D%E6%97%A9%E9%AB%98%E5%B3%B0%20%E5%8F%91%E5%B8%83%E5%9C%B0%E8%B4%A8%E7%81%BE%E5%AE%B3%E8%93%9D%E8%89%B2%E9%A2%84%E8%AD%A6&content=%E6%9C%80%E5%A4%A7%E7%9A%84%E8%B0%8E%E8%A8%80&article_type=0");
		task.setAppInfo("POST http://w.inews.qq.com/shareQQNewsMulti?uid=55eeab1433d06c91&store=562&hw=Coolpad_Coolpad8297&devid=863777020216411&orig_store=562&screen_width=720&real_device_height=4.0&mac=18%3Adc%3A56%3Ad0%3A11%3A46&appver=17_android_5.3.91&origin_imei=863777020216411&is_chinamobile_oem=0&real_device_width=2.25&patchver=5391&sceneid=&mid=dda3efcceb03107b44eea3d98f2f0207876ba07a&dpi=320&apptype=android&isoem=0&screen_height=1280&qqnetwork=wifi&rom_type=&omgbizid=75950ddb730d4a4cdecb6c70b670c7eeb1960050212603&Cookie=lskey%3D00030000e3bfab6150eea7327d6cba979aba099048109d26866b2ba5759ef91e048a35547f9f7c4c7505c98f%3Bluin%3Do2051106923%3Bskey%3DMfTTKYVIS8%3Buin%3Do2051106923%3B%20logintype%3D0%3B%20main_login%3Dqq%3B%20&qn-rid=9eab6012-4144-40f8-9e00-c734bb2443b1&network_type=wifi&imsi_history=0&global_info=2%7C&qn-sig=c34a196f46755ad9a40dd1225920af83&activefrom=icon&imsi=&omgid=ce2b6853d43a9b45cbebf839f2d4e17bb50b0010211910 HTTP/1.1"+
							"Host: w.inews.qq.com"+
							"Accept-Encoding: gzip,deflate"+
							"Referer: http://inews.qq.com/inews/android/"+
							"User-Agent: %E8%85%BE%E8%AE%AF%E6%96%B0%E9%97%BB5391(android)"+
							"Cookie: lskey=00030000e3bfab6150eea7327d6cba979aba099048109d26866b2ba5759ef91e048a35547f9f7c4c7505c98f;luin=o2051106923;skey=MfTTKYVIS8;uin=o2051106923; logintype=0; main_login=qq;"+
							"Content-Type: application/x-www-form-urlencoded"+
							"Connection: Keep-Alive"+
							"Content-Length: 2358"+
							
	"						summary=%E4%B8%AD%E5%9B%BD%E5%A4%A9%E6%B0%94%E7%BD%91%E8%AE%AF%E4%BB%8A%E5%A4%A9%EF%BC%8826%E6%97%A5%EF%BC%89%E5%8C%97%E4%BA%AC%E5%B8%82%E5%A4%A7%E9%83%A8%E5%88%86%E5%9C%B0%E5%8C%BA%E6%9C%89%E4%B8%AD%E9%9B%A8%EF%BC%8C%E5%B1%80%E5%9C%B0%E7%9F%AD%E6%97%B6%E9%9B%A8%E5%BC%BA%E8%BE%83%E5%A4%A7%EF%BC%8C%E5%B9%B6%E5%8F%AF%E8%83%BD%E4%BC%B4%E6%9C%89%E5%BC%B1%E9%9B%B7%E7%94%B5%E3%80%82%E5%AF%B9%E6%97%A9%E9%AB%98%E5%B3%B0%E5%87%BA%E8%A1%8C%E6%9C%89%E4%B8%80%E5%AE%9A%E5%BD%B1%E5%93%8D%EF%BC%8C%E8%AF%B7%E5%B0%BD%E9%87%8F%E9%80%89%E6%8B%A9%E5%85%AC%E5%85%B1%E4%BA%A4%E9%80%9A%E5%B7%A5%E5%85%B7%E5%87%BA%E8%A1%8C%EF%BC%8C%E6%B3%A8%E6%84%8F%E4%BA%A4%E9%80%9A%E5%AE%89%E5%85%A8%E3%80%82%E5%90%8C%E6%97%B6%E5%8F%97%E9%99%8D%E6%B0%B4%E5%BD%B1%E5%93%8D%EF%BC%8C%E4%BB%8A%E5%A4%A9%E6%B0%94%E6%B8%A9%E4%B8%8B%E9%99%8D%E6%98%8E%E6%98%BE%EF%BC%8C%E6%9C%80%E9%AB%98%E6%B0%94%E6%B8%A9%E9%A2%84%E8%AE%A1%E5%8F%AA%E6%9C%8924%E2%84%83%E5%B7%A6%E5%8F%B3%EF%BC%8C%E4%BD%93%E6%84%9F%E6%B8%85%E5%87%89%E3%80%82%E8%87%AA%E6%98%A8%E5%A4%A9%E5%90%8E%E5%8D%8A%E5%A4%9C%E5%BC%80%E5%A7%8B%EF%BC%8C%E5%8C%97%E4%BA%AC%E9%83%A8%E5%88%86%E5%9C%B0%E5%8C%BA%E5%BC%80%E5%A7%8B%E5%87%BA%E7%8E%B0%E9%99%8D%E6%B0%B4%E5%A4%A9%E6%B0%94%EF%BC%8C26%E6%97%A500%E6%97%B6%E8%87%B306%E6%97%B6%E5%85%A8%E5%B8%82%E5%B9%B3%E5%9D%87%E9%99%8D%E9%9B%A8%E9%87%8F1....&comment_id=2046499411&content_qqweibo=%E6%97%A9%E4%B8%8A%E4%B8%8B%E7%9A%84%E5%A5%BD%E5%A4%A7%E5%91%80%20%7C%7C%20%23%E6%88%91%E5%9C%A8%E7%9C%8B%E6%96%B0%E9%97%BB%23%20%E5%8C%97%E4%BA%AC%E4%BB%8A%E6%9C%89%E4%B8%AD%E9%9B%A8%E5%BD%B1%E5%93%8D%E6%97%A9%E9%AB%98%E5%B3%B0%20%E5%8F%91%E5%B8%83%E5%9C%B0%E8%B4%A8%E7%81%BE%E5%AE%B3%E8%93%9D%E8%89%B2%E9%A2%84%E8%AD%A6https%3A%2F%2Fview.inews.qq.com%2Fa%2F20170726A00Y4G00&img=http%3A%2F%2Finews.gtimg.com%2Fnewsapp_bt%2F0%2F1841907361%2F641&openWeibo=no&pid=&type=0&chlid=news_news_bj&url=https%3A%2F%2Fview.inews.qq.com%2Fa%2F20170726A00Y4G00&content=%E6%97%A9%E4%B8%8A%E4%B8%8B%E7%9A%84%E5%A5%BD%E5%A4%A7%E5%91%80&title=%E5%8C%97%E4%BA%AC%E4%BB%8A%E6%9C%89%E4%B8%AD%E9%9B%A8%E5%BD%B1%E5%93%8D%E6%97%A9%E9%AB%98%E5%B3%B0%20%E5%8F%91%E5%B8%83%E5%9C%B0%E8%B4%A8%E7%81%BE%E5%AE%B3%E8%93%9D%E8%89%B2%E9%A2%84%E8%AD%A6&article_id=20170726A00Y4G00&seq_str=request_publish1501055541598&aType=&seq_no=202601055518%24%24%241-3--%E5%8C%97%E4%BA%AC-1&reasonInfo=&cattr=&alg_version=6&vid=&shareType=qqcomment&expid=");
		
		try {
			
//			tengxunPriseIos(task);
			tengxunPriseAndroid(task);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void toRun(TaskGuideBean task){
		if(task.getIsApp()==2){
			tengxunPriseIos(task);
		}else{
			tengxunPriseAndroid(task);
		}
	}
	/**
	 * 腾讯新闻点赞ios
	 * @throws Exception
	 */
	public static void tengxunPriseIos(TaskGuideBean task){
		
		try{
			
		String newId = task.getAddress();
		newId = newId.substring(newId.lastIndexOf("/")+1);

		Map<String,String> headerMap = TengxunCommentApp.getHeaderParam(task.getAppInfo());
		
  		String preUrl = "http://w.inews.qq.com/supportQQNewsComment?apptype="+headerMap.get("apptype")+"" +
				"&startarticleid=" +
 				"&__qnr="+headerMap.get("__qnr")+"" +
				"&global_info="+headerMap.get("global_info")+"" +
				"&omgid="+headerMap.get("omgid")+"" +
				"&idfa="+headerMap.get("idfa")+"" +
				"&qqnews_refpage="+headerMap.get("qqnews_refpage")+"" +
				"&isJailbreak="+headerMap.get("isJailbreak")+"" +
				"&appver="+headerMap.get("appver")+"" +
				"&network_type="+headerMap.get("network_type")+"" +
				"&device_model="+headerMap.get("device_model")+"" +
				"&omgbizid="+headerMap.get("omgbizid")+"" +
				"&screen_height="+headerMap.get("screen_height")+"" +
				"&devid="+headerMap.get("devid")+"" +
				"&screen_scale="+headerMap.get("screen_scale")+"" +
				"&screen_width="+headerMap.get("screen_width")+"" +
				"&store="+headerMap.get("store")+"" +
				"&activefrom="+headerMap.get("activefrom")+"";
		
		URL url = new URL(preUrl);
		
		HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
		
		Map<String, String> newsMap = TengxunCommentApp.getNewsParam(task.getAddress());
		
		String param ="rid="+task.getPraiseWho()+"" +
		 		"&article_id="+newId+"" +
		 		"&comment_id="+newsMap.get("commentId")+"" +
		 		"&chlid=" +
		 		"&coral_uid=" +
		 		"&coral_uin=";
		
		openConnection.setDoInput(true);
		openConnection.setDoOutput(true);
		openConnection.setUseCaches(false);
		openConnection.setRequestMethod("POST");
		
		PrintWriter pw = new PrintWriter(openConnection.getOutputStream());
		pw.print(param);
		pw.flush();
		
		Scanner sc = new Scanner(openConnection.getInputStream());
		
		String result = "";
		while(sc.hasNext()){
			
			result += sc.nextLine();
		}
		logger.info("tengxun praise app result :::"+result);
		
		if(result != null && result.equals("0")){
			
			logger.info("tengxun praise ios success...");
			MQSender.toMQ(task,"");
		}else{
			
			logger.info("tengxun praise ios fail...");
			MQSender.toMQ(task,"失败");
		}
		}catch(Exception e){
			
			e.printStackTrace();
			logger.info("tengxun praise ios exception");
			MQSender.toMQ(task,"失败");
		}
	}
	
	/**
	 * 腾讯点赞android
	 * @return
	 * @throws Exception 
	 */
	public static void tengxunPriseAndroid(TaskGuideBean task){
		
		logger.info("tengxun prise android start...");
		
		try{
			
		String newId = task.getAddress();
 		newId = newId.substring(newId.lastIndexOf("/")+1);
		
		Map<String,String> headerMap = getHeaderAndroid(task.getAppInfo());
			
		String preUrl = "http://w.inews.qq.com/supportQQNewsComment?uid="+headerMap.get("uid")+"" +
				"&store="+headerMap.get("store")+"" +
				"&hw="+headerMap.get("hw")+"" +
				"&devid="+headerMap.get("devid")+"" +
				"&orig_store="+headerMap.get("orig_store")+"" +
				"&screen_width="+headerMap.get("screen_width")+"" +
				"&real_device_height="+headerMap.get("real_device_height")+"" +
				"&mac="+headerMap.get("mac")+"" +
				"&appver="+headerMap.get("appver")+"" +
				"&origin_imei="+headerMap.get("origin_imei")+"" +
				"&is_chinamobile_oem="+headerMap.get("is_chinamobile_oem")+"" +
				"&real_device_width="+headerMap.get("real_device_width")+"" +
				"&patchver="+headerMap.get("patchver")+"" +
				"&sceneid=" +
				"&mid="+headerMap.get("mid")+"" +
				"&dpi="+headerMap.get("dpi")+"" +
				"&apptype="+headerMap.get("apptype")+"" +
				"&isoem="+headerMap.get("isoem")+"" +
				"&screen_height="+headerMap.get("screen_height")+"" +
				"&qqnetwork="+headerMap.get("qqnetwork")+"" +
				"&rom_type=" +
				"&omgbizid="+headerMap.get("omgbizid")+"" +
				"&Cookie="+headerMap.get("cookies")+"" +
				"&qn-rid="+headerMap.get("qn_rid")+"" +
				"&network_type="+headerMap.get("network_type")+"" +
				"&imsi_history="+headerMap.get("imsi_history")+"" +
				"&global_info="+headerMap.get("global_info")+"" +
				"&qn-sig="+headerMap.get("qn_sig")+"" +
				"&activefrom="+headerMap.get("activefrom")+"" +
				"&imsi=" +
				"&omgid="+headerMap.get("omgid")+"";
		
		URL url = new URL(preUrl);
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		
		String param = "comment_id=2012492624" +
						"&coral_uid=" +
						"&cattr=" +
						"&rid="+task.getPraiseWho()+"" +
						"&coral_uin=" +
						"&url="+task.getAddress()+"";
		
//		openConnection.addRequestProperty("Host", "w.inews.qq.com");
////		openConnection.addRequestProperty("Accept-Encoding", "gzip,deflate");
//		openConnection.addRequestProperty("Referer", "http://inews.qq.com/inews/android/");
//		openConnection.addRequestProperty("User-Agent", "%E8%85%BE%E8%AE%AF%E6%96%B0%E9%97%BB5391(android)");
//		openConnection.addRequestProperty("Cookie", "lskey=00030000edaf88a2bac083d7489db90023db52891b5ddc735df2bfa5bd233b7d9ca29bd4dccdf97021ad6830;luin=o2337761771;skey=MyrlOJb8q6;uin=o2337761771; logintype=0; main_login=qq;");
//		openConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//		openConnection.addRequestProperty("Connection", "Keep-Alive");
//		openConnection.addRequestProperty("Content-Length", String.valueOf(param.length()));
		
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
		
		logger.info("tengxun praise android result :::"+result);
		
		if(result != null && result.equals("0")){
			
			logger.info("tengxun praise android success...");
			MQSender.toMQ(task,"");
		}else{
			
			logger.info("tengxun praise android fail...");
			MQSender.toMQ(task,"失败");
		}
		}catch(Exception e){
			
			e.printStackTrace();
			logger.info("tengxun praise android exception...");
			MQSender.toMQ(task,"失败2");
		}
		
	}

	/**
	 * 获取android头参数
	 * @param appHeaders app头信息
	 * @return
	 */
	private static Map<String, String> getHeaderAndroid(String headers) {
		
		Map<String,String> headerMap = new HashMap<String,String>();
		
		String uid = headers.substring(headers.indexOf("uid")+4,headers.indexOf("store")-1);
		headerMap.put("uid", uid);
		
		String store = headers.substring(headers.indexOf("store")+6,headers.indexOf("hw")-1);
		headerMap.put("store", store);
		
		String hw = headers.substring(headers.indexOf("&hw")+4,headers.indexOf("devid")-1);
		headerMap.put("hw", hw);
		
		String devid = headers.substring(headers.indexOf("devid")+6,headers.indexOf("orig_store")-1);
		headerMap.put("devid", devid);
		
		String orig_store = headers.substring(headers.indexOf("orig_store")+11,headers.indexOf("screen_width")-1);
		headerMap.put("orig_store", orig_store);
		
		String screen_width = headers.substring(headers.indexOf("screen_width")+13,headers.indexOf("real_device_height")-1);
		headerMap.put("screen_width", screen_width);
		
		String real_device_height = headers.substring(headers.indexOf("real_device_height")+19,headers.indexOf("mac")-1);
		headerMap.put("real_device_height", real_device_height);
		
		String mac = headers.substring(headers.indexOf("mac")+4,headers.indexOf("appver")-1);
		headerMap.put("mac", mac);
		
		String appver = headers.substring(headers.indexOf("appver")+7,headers.indexOf("origin_imei")-1);
		headerMap.put("appver", appver);
		
		String origin_imei = headers.substring(headers.indexOf("origin_imei")+12,headers.indexOf("is_chinamobile_oem")-1);
		headerMap.put("origin_imei", origin_imei);
		
		String is_chinamobile_oem = headers.substring(headers.indexOf("is_chinamobile_oem")+19,headers.indexOf("real_device_width")-1);
		headerMap.put("is_chinamobile_oem", is_chinamobile_oem);
		
		String real_device_width = headers.substring(headers.indexOf("real_device_width")+18,headers.indexOf("sceneid")-1);
		headerMap.put("real_device_width", real_device_width);
		
		String mid = headers.substring(headers.indexOf("mid")+4,headers.indexOf("dpi")-1);
		headerMap.put("mid", mid);
		
		String dpi = headers.substring(headers.indexOf("dpi")+4,headers.indexOf("apptype")-1);
		headerMap.put("dpi", dpi);
		
		String apptype = headers.substring(headers.indexOf("apptype")+8,headers.indexOf("isoem")-1);
		headerMap.put("apptype", apptype);
		
		String isoem = headers.substring(headers.indexOf("isoem")+6,headers.indexOf("screen_height")-1);
		headerMap.put("isoem", isoem);
		
		String screen_height = headers.substring(headers.indexOf("screen_height")+14,headers.indexOf("qqnetwork")-1);
		headerMap.put("screen_height", screen_height);
		
		String qqnetwork = headers.substring(headers.indexOf("qqnetwork")+10,headers.indexOf("rom_type")-1);
		headerMap.put("qqnetwork", qqnetwork);
		
		String omgbizid = headers.substring(headers.indexOf("omgbizid")+9,headers.indexOf("Cookie")-1);
		headerMap.put("omgbizid", omgbizid);
		
		String cookies = headers.substring(headers.indexOf("Cookie")+7,headers.indexOf("qn-rid")-1);
		headerMap.put("cookies", cookies);
		
		String qn_rid = headers.substring(headers.indexOf("qn-rid")+7,headers.indexOf("network_type")-1);
		headerMap.put("qn_rid", qn_rid);
		
		String network_type = headers.substring(headers.indexOf("network_type")+13,headers.indexOf("imsi_history")-1);
		headerMap.put("network_type", network_type);
		
		String imsi_history = headers.substring(headers.indexOf("imsi_history")+13,headers.indexOf("global_info")-1);
		headerMap.put("imsi_history", imsi_history);
		
		String global_info = headers.substring(headers.indexOf("global_info")+12,headers.indexOf("qn-sig")-1);
		headerMap.put("global_info", global_info);
		
		String qn_sig = headers.substring(headers.indexOf("qn-sig")+7,headers.indexOf("activefrom")-1);
		headerMap.put("qn_sig", qn_sig);

		String activefrom = headers.substring(headers.indexOf("activefrom")+11,headers.indexOf("&omgid")).replace("&imsi=", "").trim();
		headerMap.put("activefrom", activefrom);
		
		String omgid = headers.substring(headers.indexOf("omgid")+6,headers.indexOf("HTTP/1.1"));
		headerMap.put("omgid", omgid);
		
		String rheaders = headers.substring(headers.indexOf("Host"));
		String user_agent = rheaders.substring(rheaders.indexOf("User-Agent")+11,rheaders.indexOf("Cookie"));
		headerMap.put("user_agent", user_agent);
		
		String cookie = rheaders.substring(rheaders.indexOf("Cookie")+7,rheaders.indexOf("Content-Type")).trim();
		headerMap.put("cookie", cookie);
		
		return headerMap;
	}
}
