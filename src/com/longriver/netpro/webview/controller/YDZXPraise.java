package com.longriver.netpro.webview.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 一点资讯点赞
 * @author rhy
 * @date 2018-3-8 上午10:02:40
 * @version V1.0
 */
public class YDZXPraise {

	public static void main(String[] args) {
		try{
			//手机号账号,前面需加86
			TaskGuideBean task = new TaskGuideBean();
			task.setNick("17173425505");
			task.setPassword("chwx123456");
			task.setAddress("http://www.yidianzixun.com/article/0IUFrTNG");
			
			task.setAddress("http://www.yidianzixun.com/article/0IUFUMKu");
			task.setCorpus("心更难咋样");
			task.setPraiseWho("p58fphcre86rx2t");
			task.setPraiseWho("p57nhv99gtrg");
			
			toPraise(task);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 一点资讯点赞
	 * @param task
	 */
	public static void toPraise(TaskGuideBean task) {
		
		try {
			Map<String,String> paramMap = YDZXComment.getCookie(task);
			
			if(StringUtils.isBlank(paramMap.get("cookie"))){
				
				isSuccess(task, paramMap.get("msg"));
				return;
			}
			
			String address = URLDecoder.decode(task.getAddress(),"utf-8");
			URL url = new URL("http://www.yidianzixun.com/home/q/addupcomment?commentid="+task.getPraiseWho()+"&appid=yidian&_="+new Date().getTime());
			
			HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();

			openConnection.addRequestProperty("Host", "www.yidianzixun.com");
			openConnection.addRequestProperty("Connection", "keep-alive");
			openConnection.addRequestProperty("Accept", "*/*");
			openConnection.addRequestProperty("X-Requested-With", "XMLHttpRequest");
			openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.75 Safari/537.36");
			openConnection.addRequestProperty("Referer", address);
			openConnection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
			openConnection.addRequestProperty("Cookie", paramMap.get("cookie"));
			
			openConnection.connect();
			
			
			BufferedReader br = new BufferedReader(new InputStreamReader(openConnection.getInputStream(),"utf-8"));
			
			String line;
			StringBuffer sb = new StringBuffer();
			
			while((line = br.readLine()) != null){
				sb.append(line);
			}
			
			System.out.println(sb.toString());
			
			JSONObject parseObject = JSON.parseObject(sb.toString());
			String status = parseObject.getString("status");
			
			if(StringUtils.isNotBlank(status) && "success".equals(status)){
				
				isSuccess(task, "");
			}else{
				
				isSuccess(task, "点赞失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 判断是否成功
	 */
	public static void isSuccess(TaskGuideBean task,String msg){
			MQSender.toMQ(task,msg);
	}
}
