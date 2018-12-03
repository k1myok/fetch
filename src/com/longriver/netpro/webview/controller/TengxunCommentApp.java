package com.longriver.netpro.webview.controller;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 腾讯新闻app评论
 * @author rhy
 * @2017-7-3 上午9:42:52
 * @version v1.0
 */
public class TengxunCommentApp {

	static Logger logger = Logger.getLogger(TengxunCommentApp.class);
	
	public static void main(String[] args) {
		
		TaskGuideBean task = new TaskGuideBean();
		
		task.setAddress("https://view.inews.qq.com/a/BJC2017072302032402");
		task.setAddress("https://view.inews.qq.com/a/20170723A02ZIY00");
		
		task.setAddress("https://view.inews.qq.com/a/BJC201707240169940H");
//		task.setAddress("https://view.inews.qq.com/a/BJC2017072401714506");
		task.setCorpus("八达岭怎么老有事呀");
		task.setCorpus("2000万，你逗我了吧");
		task.setAppInfo("POST http://w.inews.qq.com/shareQQNewsMulti?apptype=ios&startarticleid=&__qnr=1f258f08776b&global_info=0%7C&omgid=222809444b46884db68aa8c674d2383222600010112603&idfa=9CB3856F-83EC-4194-9533-1EA3FD4150AA&qqnews_refpage=QNCommonListController&isJailbreak=0&appver=9.2_qqnews_5.3.7&network_type=wifi&device_model=iPhone8%2C1&omgbizid=8cc4857ebc8f2e42004bec2df8bc2a35be580060112603&screen_height=667&devid=BCD310C0-A5C0-4999-8F99-B1CA7B63BBF1&screen_scale=2&screen_width=375&store=1&activefrom=icon HTTP/1.1"+
							"Host: w.inews.qq.com"+
							"Content-Type: application/x-www-form-urlencoded"+
							"Accept: */*"+
							"Connection: keep-alive"+
							"qn-sig: BD8E640E3C87D5A0B2BDF4D56A61778B"+
							"idfa: 9CB3856F-83EC-4194-9533-1EA3FD4150AA"+
							"appver: 9.2_qqnews_5.3.7"+
							"Accept-Language: zh-Hans-CN;q=1, en-CN;q=0.9"+
							"qn-rid: 1f258f08efb3"+
							"qqnetwork: wifi"+
							"deviceToken: <eca6fca6 0cd2c789 21c25859 483e93ec 7ec4288d cd1d85e5 3e70563b a28ec4e8>"+
							"devid: bcd310c0-a5c0-4999-8f99-b1ca7b63bbf1"+
							"User-Agent: QQNews/5.3.7 (iPhone; iOS 9.2; Scale/2.00)"+
							"Referer: http://inews.qq.com/inews/iphone/"+
							"Content-Length: 2645"+
							"Accept-Encoding: gzip, deflate"+
							"Connection: keep-alive"+
							"Cookie: luin=o2337761771;%20lskey=00030000e595eceb28df06fd033fa1cf68e51520978e3e13202fbad1566684088e6878d8f72993f74cc0835c;%20uin=o2337761771;%20skey=MfcpO9pDqq;%20a2=5778213c8a9ecb05752e82b51d927ec155f6edc1406f0311991bc367195660fe20ae832c5db9bc446d094f6833af95e3228ebc41b0260c2ada64445ee3cd1f58a292136ff32c4806;%20main_login=qq;%20logintype=0"+
							"store: 1"+
							
	"						graphicLiveChlid=&seq_no=228600861744%24%24%242-3--%E5%8C%97%E4%BA%AC-1&title=%E6%96%B0%E5%BB%BA%E4%BA%AC%E9%9B%84%E9%93%81%E8%B7%AF%E9%A6%96%E6%AC%A1%E7%8E%AF%E8%AF%84%20%E5%8C%97%E4%BA%AC%E4%B8%89%E7%AB%99%E5%9D%87%E8%AE%BE%E5%9C%A8%E5%A4%A7%E5%85%B4&url=https%3A%2F%2Fview.inews.qq.com%2Fa%2FBJC2017072400507602&summary=%E6%9C%AC%E6%8A%A5%E8%AE%AF%EF%BC%88%E8%AE%B0%E8%80%85%E7%8E%8B%E8%96%87%EF%BC%89%E5%8C%97%E4%BA%AC%E8%87%B3%E9%9B%84%E5%AE%89%E6%96%B0%E5%8C%BA%E8%A6%81%E4%BF%AE%E9%93%81%E8%B7%AF%E4%BA%86%EF%BC%81%E6%97%A5%E5%89%8D%EF%BC%8C%E4%B8%AD%E5%9B%BD%E9%93%81%E8%B7%AF%E8%AE%BE%E8%AE%A1%E9%9B%86%E5%9B%A2%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8%E5%9C%A8%E5%85%B6%E5%AE%98%E6%96%B9%E7%BD%91%E7%AB%99%E4%B8%8A%E5%85%AC%E5%B8%83%E4%BA%86%E2%80%9C%E6%96%B0%E5%BB%BA%E9%93%81%E8%B7%AF%E5%8C%97%E4%BA%AC%E8%87%B3%E9%9B%84%E5%AE%89%E2%80%9D%E7%9A%84%E7%AC%AC%E4%B8%80%E6%AC%A1%E7%8E%AF%E8%AF%84%E5%85%AC%E7%A4%BA%E4%BF%A1%E6%81%AF%E3%80%82%E4%BA%AC%E9%9B%84%E9%93%81%E8%B7%AF%E5%85%A8%E9%95%BF100.283%E5%85%AC%E9%87%8C%EF%BC%8C%E5%8C%97%E4%BA%AC%E5%A2%83%E5%86%85%E8%AE%BE%E4%B8%89%E7%AB%99%EF%BC%8C%E4%B8%94%E5%9D%87%E5%9C%A8%E5%A4%A7%E5%85%B4%E5%8C%BA%EF%BC%8C%E7%BB%88%E7%82%B9%E4%B8%BA%E9%9B%84%E5%AE%89%E4%B8%9C%E7%AB%99%E3%80%82%E4%BB%8E%E9%A6%96%E6%AC%A1%E7%8E%AF%E8%AF%84%E5%85%AC%E7%A4%BA%E7%9A%84%E4%BF%A1%E6%81%AF%E6%9D%A5%E7%9C%8B%EF%BC%8C%E6%96%B0%E5%BB%BA%E9%93%81%E8%B7%AF%E5%8C%97%E4%BA%AC%E8%87%B3%E9%9B%84%E5%AE%89%E9%93%81%E8%B7%AF%E4%BD%8D%E4%BA%8E%E5%8C%97%E4%BA%AC%E5%B8%82%E5%92%8C%E6%B2%B3%E5%8C%97%E7%9C%81%E5%A2%83%E5%86%85%EF%BC%8C%E7%BA%BF%E8%B7%AF%E8%B5%B7%E8%87%AA%E6%9D%8E%E8%90%A5%EF%BC%8C%E7%BB%8F%E5%BB%8A&img=http%3A%2F%2Finews.gtimg.com%2Fnewsapp_ls%2F0%2F1833459470_294195%2F0&flag=0&article_id=BJC2017072400507600&shareType=qqcomment&seq_str=36CA929D0D61F7D16A3D12859E883097&chlid=news_news_bj&comment_id=2043174722&alg_version=6&shareInfo2wx=&reasonInfo=&content_qqweibo=%E5%9C%A8%E5%A4%A7%E5%85%B4%E5%91%80%EF%BC%8C%E5%A5%BD%E8%BF%9C%E5%91%80%20%7C%7C%20%23%E8%85%BE%E8%AE%AF%E6%96%B0%E9%97%BB%23%20%E6%96%B0%E5%BB%BA%E4%BA%AC%E9%9B%84%E9%93%81%E8%B7%AF%E9%A6%96%E6%AC%A1%E7%8E%AF%E8%AF%84%20%E5%8C%97%E4%BA%AC%E4%B8%89%E7%AB%99%E5%9D%87%E8%AE%BE%E5%9C%A8%E5%A4%A7%E5%85%B4%20https%3A%2F%2Fview.inews.qq.com%2Fa%2FBJC2017072400507602&openWeibo=no&desc=%E5%9C%A8%E5%A4%A7%E5%85%B4%E5%91%80%EF%BC%8C%E5%A5%BD%E8%BF%9C%E5%91%80%7C%7C%E6%96%B0%E5%BB%BA%E4%BA%AC%E9%9B%84%E9%93%81%E8%B7%AF%E9%A6%96%E6%AC%A1%E7%8E%AF%E8%AF%84%20%E5%8C%97%E4%BA%AC%E4%B8%89%E7%AB%99%E5%9D%87%E8%AE%BE%E5%9C%A8%E5%A4%A7%E5%85%B4&content=%E5%9C%A8%E5%A4%A7%E5%85%B4%E5%91%80%EF%BC%8C%E5%A5%BD%E8%BF%9C%E5%91%80&article_type=0");
		
		task.setAppInfo("POST http://w.inews.qq.com/shareQQNewsMulti?uid=55eeab1433d06c91&store=562&hw=Coolpad_Coolpad8297&devid=863777020216411&orig_store=562&screen_width=720&real_device_height=4.0&mac=18%3Adc%3A56%3Ad0%3A11%3A46&appver=17_android_5.3.91&origin_imei=863777020216411&is_chinamobile_oem=0&real_device_width=2.25&patchver=5391&sceneid=&mid=dda3efcceb03107b44eea3d98f2f0207876ba07a&dpi=320&apptype=android&isoem=0&screen_height=1280&qqnetwork=wifi&rom_type=&omgbizid=75950ddb730d4a4cdecb6c70b670c7eeb1960050212603&Cookie=lskey%3D00030000110bb59180f61901f3cbb99c1c4b253e233dd80520d910ed3f0ace7262539793dbd7f7a3299cd466%3Bluin%3Do2051106923%3Bskey%3DMygLxzUaIO%3Buin%3Do2051106923%3B%20logintype%3D0%3B%20main_login%3Dqq%3B%20&qn-rid=514df968-91a4-47b8-bedd-69473f8f0396&network_type=wifi&imsi_history=0&global_info=2%7C&qn-sig=b95b2f593580922fd9913d7d6eb4ef0b&activefrom=icon&imsi=&omgid=ce2b6853d43a9b45cbebf839f2d4e17bb50b0010211910 HTTP/1.1"+
							"Host: w.inews.qq.com"+
							"Accept-Encoding: gzip,deflate"+
							"Referer: http://inews.qq.com/inews/android/"+
							"User-Agent: %E8%85%BE%E8%AE%AF%E6%96%B0%E9%97%BB5391(android)"+
							"Cookie: lskey=00030000110bb59180f61901f3cbb99c1c4b253e233dd80520d910ed3f0ace7262539793dbd7f7a3299cd466;luin=o2051106923;skey=MygLxzUaIO;uin=o2051106923; logintype=0; main_login=qq;"+
							"Content-Type: application/x-www-form-urlencoded"+
							"Connection: Keep-Alive"+
							"Content-Length: 868"+
							
							"summary=&comment_id=2043317627&content_qqweibo=%E6%A1%A5%E5%BA%95%E5%BE%85%E4%B8%80%E6%99%9A%20%7C%7C%20%23%E6%88%91%E5%9C%A8%E7%9C%8B%E6%96%B0%E9%97%BB%23%20%E4%BD%A0%E6%AC%A0%E6%89%80%E6%9C%89%E7%9A%84%E5%8C%97%E4%BA%AC%E5%AD%A9%E5%AD%90%E4%BB%AC%E4%B8%80%E4%BA%BA%E4%BA%94%E5%A5%97%E6%88%BFhttps%3A%2F%2Fview.inews.qq.com%2Fa%2FBJC2017072401714504&img=http%3A%2F%2Finews.gtimg.com%2Fnewsapp_bt%2F0%2F1833064856%2F641&openWeibo=no&pid=&type=0&chlid=news_news_bj&url=https%3A%2F%2Fview.inews.qq.com%2Fa%2FBJC2017072401714504&content=%E6%A1%A5%E5%BA%95%E5%BE%85%E4%B8%80%E6%99%9A&title=%E4%BD%A0%E6%AC%A0%E6%89%80%E6%9C%89%E7%9A%84%E5%8C%97%E4%BA%AC%E5%AD%A9%E5%AD%90%E4%BB%AC%E4%B8%80%E4%BA%BA%E4%BA%94%E5%A5%97%E6%88%BF&article_id=BJC2017072401714504&seq_str=request_publish1500877874037&aType=&seq_no=&reasonInfo=&cattr=&alg_version=&vid=&shareType=qqcomment&expid=");
		try {
			
//			tengxunCommentIos(task);
			tengxunCommentAndriod(task);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void toRun(TaskGuideBean task){
		task.setAppInfo(task.getAppInfo().replaceAll("\n", ""));
		if(task.getIsApp()==2){
			tengxunCommentIos(task);
		}else{
			tengxunCommentAndriod(task);
		}
	}
	/**
	 * 腾讯评论ios
	 * @param comment
	 * @return
	 * @throws Exception
	 */
	public static void tengxunCommentIos(TaskGuideBean task){
		
		logger.info("tengxun comment ios start...");
		
		try{
			
		String comment = task.getCorpus();
		Map<String,String> newsMap = getNewsParam(task.getAddress());
		Map<String,String> headerMap = getHeaderParam(task.getAppInfo());
		
		String preUrl = "http://w.inews.qq.com/shareQQNewsMulti?apptype="+headerMap.get("apptype")+"" +
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
		
//		preUrl = URLDecoder.decode(preUrl,"utf-8");
		URL url = new URL(preUrl);
		
		HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
		
 		String param = "graphicLiveChlid=" +
 				"&seq_no=" +
 				"&title="+newsMap.get("title")+"" +
 				"&url="+task.getAddress()+""+
 				"&img=" +
 				"&flag=0" +
 				"&article_id="+newsMap.get("newId")+"" +
 				"&shareType=qqcomment" +
 				"&seq_str="+UUID.randomUUID().toString().replaceAll("-", "").toUpperCase()+"" +
 				"&chlid=" +
 				"&comment_id="+newsMap.get("commentId")+"" +
 				"&alg_version=" +
 				"&shareInfo2wx=" +
 				"&media_id=" +
 				"&reasonInfo=" +
 				"&content_qqweibo=" +
 				"&openWeibo=no" +
 				"&desc=" +
 				"&content="+comment+"&article_type=0";
 		
 		openConnection.addRequestProperty("Host", "w.inews.qq.com");
 		openConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
 		openConnection.addRequestProperty("Accept", "*/*");
		openConnection.addRequestProperty("Connection", "keep-alive");
		openConnection.addRequestProperty("qn-sig", headerMap.get("qnsig"));
		openConnection.addRequestProperty("idfa", headerMap.get("idfas"));
		openConnection.addRequestProperty("appver", headerMap.get("appvers"));
		openConnection.addRequestProperty("qn-rid", headerMap.get("qnrid"));
		openConnection.addRequestProperty("qqnetwork", headerMap.get("qqnetwork"));
		openConnection.addRequestProperty("deviceToken", headerMap.get("deviceToken"));
		openConnection.addRequestProperty("devid", headerMap.get("devids"));
		openConnection.addRequestProperty("User-Agent", headerMap.get("userAgent"));
		openConnection.addRequestProperty("Referer", "http://inews.qq.com/inews/iphone/");
		openConnection.addRequestProperty("Content-Length", String.valueOf(param.length()));
		openConnection.addRequestProperty("Connection", "keep-alive");
		openConnection.addRequestProperty("Cookie", headerMap.get("cookie"));
		openConnection.addRequestProperty("store", headerMap.get("store"));
		
		openConnection.setDoInput(true);
		openConnection.setDoOutput(true);
		openConnection.setUseCaches(false);
		openConnection.setRequestMethod("POST");
		
		PrintWriter pw = new PrintWriter(openConnection.getOutputStream());
		pw.print(param);
		pw.flush();
		
		Scanner sc = new Scanner(openConnection.getInputStream(),"UTF-8");
		String result = "";
		while(sc.hasNext()){
			result += sc.nextLine() + "\n";
		}
		logger.info("tengxun comment ios result :::"+result);
		
		JSONObject parseResult = JSON.parseObject(result);
		String comments = parseResult.getString("comment");
		JSONObject parseComments = JSON.parseObject(comments);
		String ret = parseComments.getString("ret");
		if(ret != null && ret.equals("0")){
			
			logger.info("tengxun comment ios success...");
			MQSender.toMQ(task,"");
		}else{
			
			logger.info("tengxun comment ios fail...");
			MQSender.toMQ(task,"失败");
		}
		}catch(Exception e){
			
			e.printStackTrace();
			logger.info("tengxun comment ios exception...");
			MQSender.toMQ(task,"失败2");
		}
	}
	/**
	 * 获取ios的头信息
	 * @param appHeaders
	 * @return
	 */
	public static Map<String, String> getHeaderParam(String headers) {

		Map<String,String> headerMap = new HashMap<String,String>();
		
		String apptype = headers.substring(headers.indexOf("apptype")+8,headers.indexOf("startarticleid")-1).trim();
		headerMap.put("apptype", apptype);
		
		String __qnr = headers.substring(headers.indexOf("__qnr")+6,headers.indexOf("global_info")-1).trim();
		headerMap.put("__qnr", __qnr);
		
		String global_info = headers.substring(headers.indexOf("global_info")+12,headers.indexOf("omgid")-1).trim();
		headerMap.put("global_info", global_info);
		
		String omgid = headers.substring(headers.indexOf("omgid")+6,headers.indexOf("idfa")-1).trim();
		headerMap.put("omgid", omgid);
		
		String idfa = headers.substring(headers.indexOf("idfa")+5,headers.indexOf("qqnews_refpage")-1).trim();
		headerMap.put("idfa", idfa);
		
		String qqnews_refpage = headers.substring(headers.indexOf("qqnews_refpage")+15,headers.indexOf("isJailbreak")-1).trim();
		headerMap.put("qqnews_refpage", qqnews_refpage);
		
		String isJailbreak = headers.substring(headers.indexOf("isJailbreak")+12,headers.indexOf("appver")-1).trim();
		headerMap.put("isJailbreak", isJailbreak);
		
		String appver = headers.substring(headers.indexOf("appver")+7,headers.indexOf("network_type")-1).trim();
		headerMap.put("appver", appver);
		
		String network_type = headers.substring(headers.indexOf("network_type")+13,headers.indexOf("device_model")-1).trim();
		headerMap.put("network_type", network_type);
		
		String device_model = headers.substring(headers.indexOf("device_model")+13,headers.indexOf("omgbizid")-1).trim();
		headerMap.put("device_model", device_model);
		
		String omgbizid = headers.substring(headers.indexOf("omgbizid")+9,headers.indexOf("screen_height")-1).trim();
		headerMap.put("omgbizid", omgbizid);
		
		String screen_height = headers.substring(headers.indexOf("screen_height")+14,headers.indexOf("devid")-1).trim();
		headerMap.put("screen_height", screen_height);
		
		String devid = headers.substring(headers.indexOf("devid")+6,headers.indexOf("screen_scale")-1).trim();
		headerMap.put("devid", devid);
		
		String screen_scale = headers.substring(headers.indexOf("screen_scale")+13,headers.indexOf("screen_width")-1).trim();
		headerMap.put("screen_scale", screen_scale);
		
		String screen_width = headers.substring(headers.indexOf("screen_width")+13,headers.indexOf("store")-1).trim();
		headerMap.put("screen_width", screen_width);
		
		String store = headers.substring(headers.indexOf("store")+6,headers.indexOf("activefrom")-1).trim();
		headerMap.put("store", store);
		
		String activefrom = headers.substring(headers.indexOf("activefrom")+11,headers.indexOf("HTTP/1.1")).trim();
		headerMap.put("activefrom", activefrom);
		
		String rheaders = headers.substring(headers.indexOf("Host"));
		String qnsig = rheaders.substring(rheaders.indexOf("qn-sig")+7,rheaders.indexOf("idfa")).trim();
		headerMap.put("qnsig", qnsig);
		
		String idfas = rheaders.substring(rheaders.indexOf("idfa")+5,rheaders.indexOf("appver")).trim();
		headerMap.put("idfas", idfas);
		
		String appvers = rheaders.substring(rheaders.indexOf("appver")+7,rheaders.indexOf("Accept-Language")).trim();
		headerMap.put("appvers", appvers);
		
		String qnrid = rheaders.substring(rheaders.indexOf("qn-rid")+7,rheaders.indexOf("qqnetwork")).trim();
		headerMap.put("qnrid", qnrid);
		
		String qqnetwork = rheaders.substring(rheaders.indexOf("qqnetwork")+10,rheaders.indexOf("deviceToken")).trim();
		headerMap.put("qqnetwork", qqnetwork);
		
		String deviceToken = rheaders.substring(rheaders.indexOf("deviceToken")+12,rheaders.indexOf("devid:")).trim();
		headerMap.put("deviceToken", deviceToken);
		
		String devids = rheaders.substring(rheaders.indexOf("devid:")+6,rheaders.indexOf("User-Agent")).trim();
		headerMap.put("devids", devids);
		
		String userAgent = rheaders.substring(rheaders.indexOf("User-Agent")+11,rheaders.indexOf("Referer")).trim();
		headerMap.put("userAgent", userAgent);
		
		String cookie = rheaders.substring(rheaders.indexOf("Cookie")+7,rheaders.indexOf("store")).trim();
		headerMap.put("cookie", cookie);
		return headerMap;
	}

	/**
	 * 获取评论参数
	 * @return
	 */
	@SuppressWarnings("unused")
	public static Map<String, String> getNewsParam(String articelUrl) throws Exception{
		
		Map<String,String> newsMap = new HashMap<String,String>();
		
		URL url = new URL(articelUrl);
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		openConnection.connect();
		
		Scanner sc = new Scanner(openConnection.getInputStream(),"utf-8");
		
		String result = "";
		while(sc.hasNext()){
			
			result += sc.nextLine() + "\n";
		}
		String newId = articelUrl;
		newId = newId.substring(newId.lastIndexOf("/")+1);
		newsMap.put("newId", newId);
		
		String content = result.substring(result.indexOf("contentModel")+14);
		content = content.substring(0,content.indexOf(";")).trim();
		JSONObject parseContent = JSON.parseObject(content);
		
		String img_url = parseContent.getString("img_url");
		newsMap.put("img_url", "img_url");
		
		String title = parseContent.getString("title");
		newsMap.put("title", title);
		
		String src = parseContent.getString("src");
		newsMap.put("src", src);

		String commentId = result.substring(result.indexOf("commentId")+11).replaceAll("\"", "").trim();
		commentId = commentId.substring(0,commentId.indexOf(";"));
		newsMap.put("commentId", commentId);
		
		return newsMap;
	}

	/**
	 * 腾讯评论andriod
	 * @return
	 * @throws Exception 
	 */
	public static void tengxunCommentAndriod(TaskGuideBean task){
		
		logger.info("tengxun comment andriod start...");
		
		try{
			
		Map<String, String> newsMap = getNewsParam(task.getAddress());
		
		Map<String,String> headerMap = getHeaderAndroid(task.getAppInfo());
		
		String preUrl = "http://w.inews.qq.com/shareQQNewsMulti?uid="+headerMap.get("uid")+"" +
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
		
		String param = "summary=" +
				"&comment_id="+newsMap.get("commentId")+"" +
				"&content_qqweibo=" +
				"&img=" +
				"&openWeibo=no" +
				"&pid=" +
				"&type=0" +
				"&chlid=" +
				"&url="+task.getAddress()+"" +
				"&content="+task.getCorpus()+"" +
				"&title="+newsMap.get("title")+"" +
				"&article_id="+newsMap.get("newId")+"" +
				"&seq_str=request_publish"+new Date().getTime()+"" +
				"&aType" +
				"&seq_no=" +
				"&reasonInfo=" +
				"&cattr=" +
				"&alg_version=" +
				"&vid=" +
				"&shareType=qqcomment" +
				"&expid=";
		openConnection.addRequestProperty("Host", "w.inews.qq.com");
		openConnection.addRequestProperty("Referer", "http://inews.qq.com/inews/android/");
		openConnection.addRequestProperty("User-Agent", headerMap.get("user_agent"));
		openConnection.addRequestProperty("Cookie", headerMap.get("cookie"));
		openConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		openConnection.addRequestProperty("Connection", "Keep-Alive");
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
		
			result += sc.nextLine();
		}
		
		logger.info("tengxun comment andriod result :::"+result);
		
		JSONObject parseResult = JSON.parseObject(result);
		String comments = parseResult.getString("comment");
		JSONObject parseComments = JSON.parseObject(comments);
		String ret = parseComments.getString("ret");
		if(ret != null && ret.equals("0")){
			
			logger.info("tengxun comment android success...");
			MQSender.toMQ(task,"");
		}else{
			
			logger.info("tengxun comment android fail...");
			MQSender.toMQ(task,"失败");
		}
		}catch(Exception e){
			
			e.printStackTrace();
			logger.info("tengxun comment android exception...");
			MQSender.toMQ(task,"失败2");
		}
	}
	/**
	 * 获取android的头信息
	 * @param headers
	 * @return
	 */
	public static Map<String, String> getHeaderAndroid(String headers) {
		
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
		
		String real_device_width = headers.substring(headers.indexOf("real_device_width")+18,headers.indexOf("patchver")-1);
		headerMap.put("real_device_width", real_device_width);
		
		String patchver = headers.substring(headers.indexOf("patchver")+9,headers.indexOf("sceneid")-1);
		headerMap.put("patchver", patchver);
		
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
