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
import com.longriver.netpro.common.sina.SData;
import com.longriver.netpro.common.sina.SinaIdMidConverter;
import com.longriver.netpro.common.sina.WeiboSina;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 微博二级评论
 * @author rhy
 * @2017-11-2 上午9:13:57
 * @version v1.0
 */
public class WeiboSecondComment {

	private static Logger logger = Logger.getLogger(WeiboSecondComment.class);
	
	public static void main(String[] args){
		
		TaskGuideBean task = new TaskGuideBean();
		task.setAddress("https://weibo.com/3945917804/Fs8SQz6nA?filter=hot&root_comment_id=0&type=comment@4167078917391530");
		task.setAddress("https://weibo.com/3945917804/Fs8SQz6nA?filter=hot&root_comment_id=0&type=comment@@@4167083463879582");
		task.setAddress("https://weibo.com/6046682731/FtltOqcHY?filter=hot&root_comment_id=0&type=comment#_rnd1509690340516@4169998191054996");
		task.setAddress("https://weibo.com/1901597310/Ft2zTfR2o?filter=hot&root_comment_id=0&type=comment@4169638516161094");
		task.setNick("17076537114");
		task.setPassword("lx1314");
		task.setNick("13014992630");
		task.setPassword("lx1314");
		task.setCorpus("just 它");
		task.setCorpus("怎么读");
		task.setCorpus("这图满分");
		task.setCorpus("好评一个");
		task.setCorpus("这个多好次了");
		
//		String cookie = getWeiboCookie(task);
//		String address = URLDecoder.decode(task.getAddress(),"utf-8");
//		Map<String,String> paramMap = getCommentParam(address,cookie);
		toComment(task);
	}

	/**
	 * 微博二级评论
	 * @param task
	 */
	public static void toComment(TaskGuideBean task) {
		
		try{
			
		String cookie = getWeiboCookie(task);
		String address = URLDecoder.decode(task.getAddress(),"utf-8").trim();
		Map<String,String> paramMap = getCommentParam(task,address,cookie);
			
		URL url = new URL("https://weibo.com/aj/v6/comment/add?ajwvr=6&__rnd="+System.currentTimeMillis());
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		
		String param = "act=reply" +
				"&mid="+paramMap.get("mid")+"" +
				"&cid="+paramMap.get("cid")+"" +
				"&uid="+paramMap.get("uid")+"" +
				"&forward=0" +
				"&isroot=0" +
				"&content="+URLEncoder.encode(task.getCorpus(),"utf-8")+
				"&ouid=" +
				"&nick=" +
				"&ispower=1" +
				"&status_owner_user="+paramMap.get("status_owner_user")+"" +
				"&canUploadImage=0" +
				"&module=scommlist" +
				"&dissDataFromFeed=%5Bobject%20Object%5D" +
				"&root_comment_id="+paramMap.get("cid")+"" +
				"&approvalComment=false" +
				"&location="+paramMap.get("location")+"" +
				"&pdetail="+paramMap.get("pdetail")+"" +
				"&_t=0";

		openConnection.addRequestProperty("Host", "weibo.com");
		openConnection.addRequestProperty("Connection", "keep-alive");
		openConnection.addRequestProperty("Content-Length", String.valueOf(param.length()));
		openConnection.addRequestProperty("Origin", "https://weibo.com");
		openConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		openConnection.addRequestProperty("X-Requested-With", "XMLHttpRequest");
		openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36 LBBROWSER");
		openConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		openConnection.addRequestProperty("Accept", "*/*");
		openConnection.addRequestProperty("Referer", paramMap.get("address"));
		openConnection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
		openConnection.addRequestProperty("Cookie", cookie);
		
		openConnection.setDoInput(true);
		openConnection.setDoOutput(true);
		openConnection.setRequestMethod("POST");
		openConnection.setUseCaches(false);
		
		PrintWriter pw = new PrintWriter(openConnection.getOutputStream());
		pw.print(param);
		pw.flush();
		pw.close();
		
		
		Scanner sc = new Scanner(openConnection.getInputStream(),"utf-8");
		String result = "";
		while(sc.hasNext()){
			
			result += sc.nextLine();
		}
		
		System.out.println(result);
		JSONObject parseResult = JSON.parseObject(result);
		String code = parseResult.getString("code");
		if(code!=null && "100000".equals(code)){
			
			logger.info("weibo second comment success 100000");
			task.setCode(0);
			isSuccess(task, "");
		}else if(code!=null && "100003".equals(code)){
			task.setCode(10);
			logger.info("weibo second comment fail 100003");
			isSuccess(task, "未实名认证");
		}else if(code!=null && "100001".equals(code)){
			task.setCode(4);
			logger.info("weibo second comment fail 100001");
			isSuccess(task, "相同内容请隔10分钟再进行发布");
		}else {
			task.setCode(8);
			logger.info("weibo second comment fail");
			isSuccess(task, "评论失败");
		}
		
		}catch(Exception e){
			task.setCode(8);
			isSuccess(task, "评论异常");
			e.printStackTrace();
		}
	}

	/**
	 * 获取评论参数
	 * @param task
	 * @return
	 */
	private static Map<String, String> getCommentParam(TaskGuideBean task,String address,String cookie) {
	
		try{
			
		Map<String,String> paramMap = new HashMap<String,String>();
		String mid = getMid(address);
		String cid = getCid(task,address);
		address = address.substring(0,address.indexOf("@")).replaceAll("@", "").trim();
		
		paramMap.put("mid", mid);
		paramMap.put("cid", cid);
		paramMap.put("address", address);
		
		URL url = new URL(address);
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		
		openConnection.addRequestProperty("Host", "weibo.com");
		openConnection.addRequestProperty("Connection", "keep-alive");
		openConnection.addRequestProperty("Cache-Control", "max-age=0");
		openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.75 Safari/537.36");
		openConnection.addRequestProperty("Upgrade-Insecure-Requests", "1");
		openConnection.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		openConnection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
		openConnection.addRequestProperty("Cookie", cookie);
		openConnection.connect();
		
		Scanner sc = new Scanner(openConnection.getInputStream(),"utf-8");
		
		String result = "";
		while(sc.hasNext()){
			
			result += sc.nextLine()+"\n";
			
		}
		
		String uid = result.substring(result.indexOf("$CONFIG['uid']")+14);
		uid = uid.substring(0,uid.indexOf(";")).replaceAll("=", "").replaceAll("'", "").trim();
		paramMap.put("uid", uid);
		
		String status_owner_user = result.substring(result.indexOf("$CONFIG['oid']")+14);
		status_owner_user = status_owner_user.substring(0,status_owner_user.indexOf(";")).replaceAll("=", "").replaceAll("'", "").trim();
		paramMap.put("status_owner_user", status_owner_user);
		
		String location = result.substring(result.indexOf("$CONFIG['location']")+19);
		location = location.substring(0,location.indexOf(";")).replaceAll("=", "").replaceAll("'", "").trim();
		paramMap.put("location", location);
		
		String pdetail = result.substring(result.indexOf("$CONFIG['domain']")+17);
		pdetail = pdetail.substring(0,pdetail.indexOf(";")).replaceAll("=", "").replaceAll("'", "").trim();
		pdetail = pdetail+status_owner_user;
		paramMap.put("pdetail", pdetail);
		
		return paramMap;
		}catch(Exception e){
			
			logger.info("get comment param exception");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取cid
	 * @param address
	 * @return
	 */
	private static String getCid(TaskGuideBean task,String address) {
		
		try{
			if(address.indexOf("@")>0){
				
				String cid = address.substring(address.indexOf("@")+1).replaceAll("@", "").trim();
				
				return cid;
			}else{
				
				isSuccess(task, "链接问题");
				logger.info("weibo comment url problem");
			}
		}catch(Exception e){

			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取mid
	 * @param address
	 * @return
	 */
	private static String getMid(String address) {
		
		try{
		String mid = address.substring(address.lastIndexOf("/")+1);
		
		if(mid.indexOf("?") > -1){
			mid = mid.substring(0, mid.indexOf("?"));
		}
		if(mid.indexOf("#") > -1){
			mid = mid.substring(0, mid.indexOf("#"));
		}
		mid = SinaIdMidConverter.midToId(mid);
		
		return mid;
		}catch(Exception e){
			
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取cookie
	 * @param task
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static String getWeiboCookie(TaskGuideBean task) {
		
		try{
		SData data =new SData();
		data.put("user_account_id", task.getNick());
		data.put("user_account_pw", task.getPassword());
		
//		String cookie = WeiboSina.getCookie(data,0);
		String cookie = WeiboSina.getCookietime(data,0);
		
		return cookie;
		
		}catch(Exception e){
			
			logger.info("get weibo cookie exception");
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 判断是否成功
	 */
	public static void isSuccess(TaskGuideBean task,String msg){
		MQSender.toMQ(task,msg);
	}
	
}
