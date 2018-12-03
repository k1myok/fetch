package com.longriver.netpro.webview.controller;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 搜狐新闻点赞app
 * @author rhy
 * @2017-6-29 下午1:55:14
 * @version v1.0
 */
public class SohuPriseApp {

	static Logger logger = Logger.getLogger(SohuPriseApp.class);
	
	public static void main(String[] args) {
		
		TaskGuideBean task = new TaskGuideBean();
		task.setPraiseWho("1157369393");
//		task.setPraiseWho("1157356880");
		task.setAddress("http://3g.k.sohu.com/t/n210858643?showType=");
//		task.setAddress("http://3g.k.sohu.com/t/n210644087?showType=");
		
//		task.setPraiseWho("1157372173");
//		task.setPraiseWho("1157387971");
//		task.setAddress("http://3g.k.sohu.com/t/n210858643?showType=");
//		task.setAddress("http://3g.k.sohu.com/t/n210922172?showType=");
		try {
			sohuPriseIos(task);
//			sohuPriseAndroid(task);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void toRun(TaskGuideBean task){
		if(task.getIsApp()==2){
			sohuPriseIos(task);
		}else{
			sohuPriseAndroid(task);
		}
	}
	/**
	 * 搜狐新闻点赞ios
	 * @param comment
	 */
	@SuppressWarnings("unused")
	public static void sohuPriseIos(TaskGuideBean task){
		
		logger.info("sohu prise iso start ...");
		
		try{
		
		String commentId = task.getPraiseWho();
		String topicId = getTopicId(task);

		String preUrl = "http://api.k.sohu.com/api/comment/dingv3.go?_="+new Date().getTime();
		URL url = new URL(preUrl);
		HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
		
		String param = "commentId="+commentId+"&topicId="+topicId;
		
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
		
		logger.info("sohu prise iso result :::"+result);
		
		JSONObject parseResult = JSON.parseObject(result);
		String status = parseResult.getString("status").trim();
		String msg = parseResult.getString("msg").trim();
		
		if(msg!=null && msg.equals("成功")){
			
			logger.info("sohu prise ios success...");
			MQSender.toMQ(task,"");
		}else{
			
			logger.info("sohu prise ios fail...");
			MQSender.toMQ(task,"失败");
		}
		}catch(Exception e){
			
			e.printStackTrace();
			logger.info("sohu prise ios exception...");
			MQSender.toMQ(task,"失败2");
		}
	}

	/**
	 * 获取topicId
	 * @param preContent
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private static String getTopicId(TaskGuideBean task) throws Exception {
		
		String articleUrl = task.getAddress();
		JSONObject parseComment = SohuCommentApp.getCommentParam(articleUrl);
		String newsId = parseComment.getString("newsId");
		
		String preUrl = "http://api.k.sohu.com/api/comment/getCommentListByCursor.go?busiCode=2" +
				"&apiVersion=37" +
				"&channelId=1" +
				"&cursorId=" +
				"&from=1500512081021" +
				"&gid=01010111060001dec0fe662b5f50ebfc1476993202d5aa" +
				"&id="+newsId+"" +
				"&openType=" +
				"&p1=NjI4NjAwNTc3NTE5ODc2MTAwNw%3D%3D" +
				"&u=1" +
				"&page=1" +
				"&pid=6293341187558781006" +
				"&position=2" +
				"&refer=3" +
				"&rollType=1" +
				"&rt=json" +
				"&size=10" +
				"&source=news" +
				"&subId=" +
				"&type=5" +
				"&refererType=1" +
				"&articleDebug=0" +
				"&_="+new Date().getTime();
		
		URL url = new URL(preUrl);
		HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
		
		openConnection.connect();
		Scanner sc = new Scanner(openConnection.getInputStream(),"utf-8");
		
		String result = "";
		while(sc.hasNext()){
			result += sc.nextLine();
		}
		
		JSONObject parseResult = JSON.parseObject(result);
		String response = parseResult.getString("response");
		
		JSONObject parseResponse = JSON.parseObject(response);
		String topicId = parseResponse.getString("topicId");
		
		return topicId;
	}
	
	/**
	 * 搜狐新闻点赞andriod
	 * @return
	 */
	@SuppressWarnings("unused")
	public static void sohuPriseAndroid(TaskGuideBean task){
		
		logger.info("sohu prise android start...");
		
		try{
		String preUrl = "http://api.k.sohu.com/api/comment/dingv3.go?_="+new Date().getTime();
		URL url = new URL(preUrl);
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		
		String topicId = getTopicIdAndroid(task);
		String param = "commentId="+task.getPraiseWho()+"&topicId="+topicId;
		
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
		
		logger.info("sohu prise android result :::"+result);
		JSONObject parseResult = JSON.parseObject(result);
		String status = parseResult.getString("status").trim();
		String msg = parseResult.getString("msg").trim();
		
		if(msg!=null && msg.equals("成功")){
			
			logger.info("sohu prise android success...");
			MQSender.toMQ(task,"");
		}else{
			
			logger.info("sohu prise android fail...");
			MQSender.toMQ(task,"失败");
		}
		}catch(Exception e){
			
			e.printStackTrace();
			logger.info("sohu prise android exception...");
			MQSender.toMQ(task,"失败");
		}
	}
	/**
	 * 获取android的topicId
	 * @param task
	 * @return
	 */
	private static String getTopicIdAndroid(TaskGuideBean task) throws Exception{
		
		JSONObject parseData = SohuCommentApp.getCommentParam(task.getAddress());
		String newsId = "";
		if(parseData != null){
		newsId = parseData.getString("newsId");
		}
		
		String preUrl = "http://api.k.sohu.com/api/comment/getCommentListByCursor.go?busiCode=2" +
				"&apiVersion=37" +
				"&channelId=1" +
				"&cursorId=" +
				"&from=1500512081021" +
				"&gid=02ffff11061111535d23efc36406accf9bb4480d72b3f4" +
				"&id="+newsId+"" +
				"&openType=" +
				"&p1=NjI4ODU5NTMwNTQ1ODIxNjk4MQ%3D%3D" +
				"&u=1" +
				"&page=1" +
				"&pid=6293378479954505852" +
				"&position=2" +
				"&refer=3" +
				"&rollType=1" +
				"&rt=json" +
				"&size=10" +
				"&source=news" +
				"&subId=" +
				"&type=5" +
				"&refererType=" +
				"&articleDebug=0" +
				"&_="+new Date().getTime();
		
		URL url = new URL(preUrl);
		HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
		
		openConnection.connect();
		Scanner sc = new Scanner(openConnection.getInputStream(),"utf-8");
		
		String result = "";
		while(sc.hasNext()){
			result += sc.nextLine();
		}
		
		JSONObject parseResult = JSON.parseObject(result);
		String response = parseResult.getString("response");
		
		JSONObject parseResponse = JSON.parseObject(response);
		String topicId = parseResponse.getString("topicId");
		
		return topicId;
	}
}
