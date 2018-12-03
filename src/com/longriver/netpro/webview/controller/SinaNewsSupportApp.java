package com.longriver.netpro.webview.controller;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 新浪新闻点赞
 * @author rhy
 * @2017-6-28 下午1:52:35
 * @version v1.0
 */
public class SinaNewsSupportApp{

	static Logger logger = Logger.getLogger(SinaNewsSupportApp.class);
	
	public static void main(String[] args) {
		
		TaskGuideBean task = new TaskGuideBean();
		String articleUrl = "http://bj.sina.cn/news/2017-07-17/detail-ifyiakwa4260402.d.html?sinawapsharesource=newsapp";//新闻地址
//		articleUrl = "https://news.sina.cn/gn/2017-07-17/detail-ifyiaewh9482341.d.html?sinawapsharesource=newsapp";//新闻地址
		String commentId = "596F07EA-A4F2879-15382FE9E-957-8C5";//点赞id  内容: 每次拉后腿,诶呦
		task.setAddress(articleUrl);
		task.setPraiseWho(commentId);
		try {
//			sinaPraiseIos(task);
			sinaPraiseAndroid(task);
		} catch (Exception e) {
			logger.error("sina comment exception...");
			e.printStackTrace();
		}
	}
	public static void toRun(TaskGuideBean task){
		if(task.getIsApp()==2){
			sinaPraiseIos(task);
		}else{
			sinaPraiseAndroid(task);
		}
	}
	/**
	 * @param articleUrl 新闻链接
	 * @param commentId 评论id
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public static void sinaPraiseIos(TaskGuideBean task){
//		String articleUrl,String commentId
		logger.info("sina prise start...");
		
		try{
		String articleUrl =	task.getAddress();
		String commentId = task.getPraiseWho();
		
		articleUrl = URLDecoder.decode(articleUrl,"utf-8");
		String preUrl = "http://newsapi.sina.cn/?resource=comment/agree" +
				"&accessToken=2.00AhFUNGe3vYNC7561bcd0d7H7yvKB" +
				"&chwm=3023_0001" +
				"&city=WMXX2972&connectionType=2" +
				"&deviceId=7bedd60fcbb47ced5ec0a4cc56fd07772ed92c78" +
				"&deviceModel=apple-iphone6" +
				"&from=6063193012" +
				"&idfa=9CB3856F-83EC-4194-9533-1EA3FD4150AA" +
				"&idfv=BD7D3D44-8719-45F2-81FA-6B3909440586" +
				"&imei=7bedd60fcbb47ced5ec0a4cc56fd07772ed92c78" +
				"&location=39.977565%2C116.386466" +
				"&osVersion=9.2" +
				"&resolution=750x1334" +
				"&seId=43cd9ab890" +
				"&sfaId=7bedd60fcbb47ced5ec0a4cc56fd07772ed92c78" +
				"&ua=apple-iphone6__SinaNews__6.3.1__iphone__9.2" +
				"&unicomFree=0" +
				"&weiboSuid=ffcd03ac17" +
				"&weiboUid=5696061086" +
				"&wm=b207" +
				"&rand=610" +
				"&urlSign=f867a91751";
		
					  //e66e185856
		String nick = "";
		String toMid = commentId;
		
		String accessToken = preUrl.substring(preUrl.indexOf("accessToken")+12);
		accessToken = accessToken.substring(0,accessToken.indexOf("&"));
		
		JSONObject parseDate = SinaNewsCommentApp.getParamValue(articleUrl);
		
		String channel = parseDate.getString("channel");
		String newsid = parseDate.getString("newsid");
		String product = parseDate.getString("product");
		String index = parseDate.getString("index");
		
		String param = "accessToken="+accessToken+"" +
				"&commentId="+newsid+"_0_"+channel+"__"+product+"-"+index+"&nick=&toMid="+toMid;
		
		URL url = new URL(preUrl);
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		
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
		logger.info("sina prise ios:::"+result);
		
		JSONObject parseResult = JSON.parseObject(result);
		String data = parseResult.getString("data");
		
		if(StringUtils.isBlank(data)){
			
			logger.info("sina prise ios fail---");
		}
		JSONObject parseData = JSON.parseObject(data);
		String msg = parseData.getString("msg");
		
		if(StringUtils.isBlank(msg)){
			MQSender.toMQ(task,"");
		}else{
			MQSender.toMQ(task,"失败");
			logger.info("sina prise ios fail...");
		}
		}catch(Exception e){
			e.printStackTrace();
			MQSender.toMQ(task,"失败2");
		}
	}
	
	/**
	 * 新浪新闻点赞android
	 * @param articleUrl 新闻链接
	 * @param commentId  评论id
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public static void sinaPraiseAndroid(TaskGuideBean task){
		
		logger.info("sina prise android start...");
		
		try{
		String articleUrl =	task.getAddress();
		String commentId = task.getPraiseWho();
			
		articleUrl = URLDecoder.decode(articleUrl,"utf-8");
		String preUrl = "http://newsapi.sina.cn/?resource=comment/agree" +
				"&seId=12a45f9526" +
				"&deviceId=fb44c231fa7bc8b7" +
				"&from=6063395012" +
				"&weiboUid=5697715845" +
				"&weiboSuid=7a65ad7398" +
				"&imei=863777020216411" +
				"&wm=b207" +
				"&chwm=12040_0001" +
				"&oldChwm=12040_0001" +
				"&osVersion=4.2.2" +
				"&connectionType=2" +
				"&resolution=720x1280" +
				"&city=WMXX2972" +
				"&deviceModel=Coolpad__Coolpad__Coolpad+8297" +
				"&location=39.977537%2C116.386427" +
				"&link=" +
				"&mac=18%3Adc%3A56%3Ad0%3A11%3A46" +
				"&ua=Coolpad-Coolpad+8297__sinanews__6.3.3__android__4.2.2" +
				"&osSdk=17" +
				"&cmd_mac=18%3Adc%3A56%3Ad0%3A11%3A46%0A" +
				"&aId=01AojFQdXsjOGreJWz8NFLv2aMwY3lOvcwKLeWkgjeTXlOMk8." +
				"&lDid=2463f80a-91a8-42b8-a473-b28767588853" +
				"&accessToken=2.00fKCbNGe3vYNC6f1ce83775ZYk51D" +
				"&urlSign=20b1a17715" +
				"&rand=751";
		
		URL url = new URL(preUrl);
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		
		String toMid = commentId;
		
		String accessToken = preUrl.substring(preUrl.indexOf("accessToken")+12);
		accessToken = accessToken.substring(0,accessToken.indexOf("&"));
		
		JSONObject parseDate = SinaNewsCommentApp.getParamValue(articleUrl);
		
		String channel = parseDate.getString("channel");
		String newsid = parseDate.getString("newsid");
		String product = parseDate.getString("product");
		String index = parseDate.getString("index");
		
		String param = "accessToken="+accessToken+"" +
				"&nick=" +
				"&toMid="+toMid+"" +
				"&commentId="+newsid+"_0_"+channel+"__"+product+"-"+index+"";
		
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
		
		logger.info("sina prise android result:::"+result);
		
		JSONObject parseResult = JSON.parseObject(result);
		String data = parseResult.getString("data");
		
		if(StringUtils.isBlank(data)){
			
			logger.info("sina prise android fail---");
		}
		JSONObject parseData = JSON.parseObject(data);
		String msg = parseData.getString("msg");
		
		if(StringUtils.isBlank(msg)){
			logger.info("sina prise android success...");
			MQSender.toMQ(task,"");
		}else{
			MQSender.toMQ(task,"失败");
		}
		}catch(Exception e){
			MQSender.toMQ(task,"失败2");
			e.printStackTrace();
			logger.info("sina prise android exception...");
		}
	}
}
