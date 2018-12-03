package com.longriver.netpro.webview.controller;

import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 凤凰新闻点赞app
 * @author rhy
 * @2017-7-5 下午3:34:50
 * @version v1.0
 */
public class IFengPraiseApp {

	static Logger logger = Logger.getLogger(IFengPraiseApp.class);
	
	public static void main(String[] args) {
		
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("https://share.iclient.ifeng.com/shareNews?forward=1&aid=cmpp_030010051501867&aman=aaz05154dfh3ae6133i594b914Tce3o8f6s583Ebc9");
		task.setAddress("https://share.iclient.ifeng.com/news/shareNews?forward=1&aid=125874109&aman=aaf051O4dfx3ae2133A594m914Yce3E8f635835bc9");
		
		task.setAddress("https://share.iclient.ifeng.com/news/shareNews?forward=1&aid=125879051&aman=114w612K0200777e368");
		task.setAddress("https://share.iclient.ifeng.com/shareNews?forward=1&aid=cmpp_034240051505394&aman=114t612Y020X777d368");
		task.setPraiseWho("906739992");
		task.setPraiseWho("906643536");
		
		task.setPraiseWho("907038453");
		task.setPraiseWho("907311814");
		
		task.setAppInfo("");
		try {
			ifengPraise(task);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 凤凰新闻点赞--ios和android是一样的
	 * @param commentId 点赞id
	 * @return
	 * @throws Exception
	 */
	public static void ifengPraise(TaskGuideBean task){
		
		logger.info("ifeng prise app start...");
		
		try{
		String aid = IFengCommentApp.getAid(task.getAddress());
		
		Map<String,String> newsMap = IFengCommentApp.getNewsParam("http://api.3g.ifeng.com/ipadtestdoc?aid="+aid+"");
			
		String commentId = task.getPraiseWho();
		String preUrl = "https://icomment.ifeng.com/vote.php?cmtId="+commentId+"" +
 				"&docUrl="+newsMap.get("docUrl")+"" +
				"&job=up&"+new Date().getTime()+"&rt=sj";
		
		URL url = new URL(preUrl);
		URLConnection openConnection = url.openConnection();
		openConnection.connect();
		
		Scanner sc = new Scanner(openConnection.getInputStream(),"utf-8");
		String result = "";
		
		while(sc.hasNext()){
			
			result += sc.nextLine();
		}
		
		logger.info("ifeng prise result :::"+result);
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
}
