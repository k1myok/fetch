package com.longriver.netpro.webview.controller;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Scanner;

import com.alibaba.fastjson.JSON;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

public class IFengNewsSupport2 {
	
	public static void main(String agrs[]){
		TaskGuideBean b = new TaskGuideBean();
//		b.setAddress("http://gentie.ifeng.com/view.html?docUrl=http%3A%2F%2Fnews.ifeng.com%2Fa%2F20170323%2F50818191_0.shtml#");
//		b.setPraiseWho("807674965");
//		b.setPraiseContent("70后算什么年轻人，有病！2 ");
		b.setAddress("http://gentie.ifeng.com/view.html?docUrl=sub_15991154&docName=%E8%B4%BE%E4%B9%83%E4%BA%AE%E4%BD%A0%E8%BF%99%E5%90%83%E9%A5%AD%E7%9A%84%E6%A0%B7%E5%AD%90%EF%BC%8C%E4%B8%8D%E6%80%95%E9%81%AD%E6%9D%8E%E5%B0%8F%E7%92%90%E6%AF%8D%E5%A5%B3%E5%AB%8C%E5%BC%83%E5%90%97%EF%BC%9F&skey=173087&pcUrl=http://ent.ifeng.com/a/20170519/42936171_0.shtml");
		b.setPraiseWho("848605568");
		b.setPraiseContent("可以这样的话我也愿意啊 但我没那个命 只能真正的当乞丐了");
//		b.setAddress("http://gentie.ifeng.com/view.html?docUrl=http%3A%2F%2Fnews.ifeng.com%2Fa%2F20170323%2F50818191_0.shtml#");
//		b.setPraiseWho("807618995");
//		b.setPraiseContent("工作经历还是很丰富的，从干部梯队建设的角度来讲，也需要培养女同志的");
//		b.setAddress("http://gentie.ifeng.com/view.html?docUrl=http%3A%2F%2Fnews.ifeng.com%2Fa%2F20161121%2F50292946_0.shtml&docName=%E6%B5%99%E6%B1%9F%E4%B8%BA%E4%BD%95%E5%81%8F%E7%88%B1%E8%BF%99%E7%B1%BB%E5%B9%B2%E9%83%A8%E5%BD%93%E2%80%9C%E4%B8%80%E6%8A%8A%E6%89%8B%E2%80%9D%EF%BC%9F%20&skey=924b24");
//		b.setPraiseWho("752807826");
//		b.setPraiseContent("确实需要狮子型干部来担当大梁");
		b.setAddress("http://gentie.ifeng.com/view.html?docUrl=http%3A%2F%2Fnews.ifeng.com%2Fa%2F20161216%2F50428149_0.shtml&docName=%E6%99%8B%E5%8D%87%E5%89%AF%E9%83%A8%E4%B8%80%E5%B9%B4%E5%B0%B1%E8%90%BD%E9%A9%AC%20%E6%AD%A4%E4%BA%BA%E4%B8%8D%E8%B5%B0%E5%AF%BB%E5%B8%B8%E8%B7%AF&skey=bc050e");
		b.setPraiseWho("915256465");
		try{
			for(int i=0;i<10;i++){
				ifeng(b);
//				System.out.println("==i=="+i);
//				Thread.sleep(1000l*2);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void ifeng(TaskGuideBean taskdo) throws Exception{
		
		Thread.sleep(30000);
		String commentId = taskdo.getPraiseWho();
		String sUrl = URLDecoder.decode(taskdo.getAddress(),"utf-8");
		System.out.println("Url="+sUrl+"|||||||||");
		sUrl = taskdo.getAddress();
		System.out.println("Url="+sUrl+"|||||||||");
		
//		if(sUrl.startsWith("http://gentie")){
//			URL u = new URL(sUrl.trim());
//			HttpURLConnection c3 = (HttpURLConnection) u.openConnection();
//			c3.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
//			c3.addRequestProperty("Accept", "text/javascript, application/javascript, */*");
//			c3.addRequestProperty("Accept-Language", "zh-cn");
//			c3.addRequestProperty("Referer", sUrl);
//			c3.addRequestProperty("Connection", "Keep-Alive");
//			c3.addRequestProperty("Host", "gentie.ifeng.com");
//			c3.addRequestProperty("x-requested-with", "XMLHttpRequest");
//			c3.connect();
//			
//			
//			
//			
//			Scanner s = new Scanner(c3.getInputStream(), "utf-8");
//			while(s.hasNext()){
//				String line = s.nextLine();
//				if(line != null && line.indexOf("name=\"docUrl\" value=\"") > -1){
//					sUrl = line.substring(line.indexOf("name=\"docUrl\" value=\"")).replace("name=\"docUrl\" value=\"", "");
//					sUrl = sUrl.substring(0, sUrl.indexOf("\""));
//				}
//			}
//		}
		
//		URL u2 = new URL(sUrl);
//		HttpURLConnection c2 = (HttpURLConnection) u2.openConnection();
//		c2.connect();
//		InputStream i2 = c2.getInputStream();
//		Scanner s2 = new Scanner(i2, "utf-8");
//		
//		String docUrl = "";
//		String docName = "";
//		String skey = "";
//		
//		while(s2.hasNext()){
//			String scsc = s2.nextLine();
//			System.out.println(scsc);
//			if(scsc.indexOf("\"docUrl\"") > -1){
//				docUrl = scsc.replace("\"docUrl\"", "");
//				if(docUrl.indexOf("\"")>-1)
//					docUrl = docUrl.substring(docUrl.indexOf("\"") + 1, docUrl.lastIndexOf("\""));
//			}
//			if(scsc.indexOf("\"docName\"") > -1){
//				docName = scsc.replace("\"docName\"", "");
//				if(docName.indexOf("\"") > -1)
//					docName = docName.substring(docName.indexOf("\"") + 1, docName.lastIndexOf("\""));
//			}
//			if(scsc.indexOf("\"skey\"") > -1){
//				skey = scsc.replace("\"skey\"", "");
//				if(skey.indexOf("\"")>-1)
//					skey = skey.substring(skey.indexOf("\"") + 1, skey.lastIndexOf("\""));
//			}
//			
//		}
		
		//http://comment.ifeng.com/vote/?cmtId=322863597&docUrl=http://news.ifeng.com/a/20141219/42751098_0.shtml&_=1419444687431
		//http://comment.ifeng.com/view.php?docUrl=http%3A%2F%2Fnews.ifeng.com%2Fa%2F20141219%2F42751098_0.shtml&docName=%E4%BA%BA%E6%B0%91%E6%97%A5%E6%8A%A5%E6%B5%B7%E5%A4%96%E7%89%88%EF%BC%9A%E4%B8%AD%E5%9B%BD%E4%B8%8D%E4%BC%9A%E5%A4%A7%E8%A7%84%E6%A8%A1%E5%87%8F%E6%8C%81%E7%BE%8E%E5%80%BA&skey=59acc2
		String url2 = sUrl;
		String docUrl = "";
		String docName = "";
		String skey = "";
		if(url2.indexOf("http://gentie") > -1){
			docUrl = url2.substring(url2.indexOf("docUrl=")).replace("docUrl=", "");
			if(docUrl.indexOf("&") > -1){
				docUrl = docUrl.substring(0, docUrl.indexOf("&"));
			}
			docUrl = URLDecoder.decode(docUrl, "utf-8");
			
			if(url2.indexOf("docName=")!=-1)
				docName = url2.substring(url2.indexOf("docName=")).replace("docName=", "");
			if(docName.indexOf("&") > -1){
				docName = docName.substring(0, docName.indexOf("&"));
			}
			docName = URLDecoder.decode(docName, "utf-8");
			
			if(url2.indexOf("skey=")!=-1){
				skey = url2.substring(url2.indexOf("skey=")).replace("skey=", "");
			}
			if(skey.indexOf("&") > -1){
				skey = skey.substring(0, skey.indexOf("&"));
			}
			skey = URLDecoder.decode(skey, "utf-8");
		}
		String kUrl = "http://comment.ifeng.com/view.php"
				+ "?docUrl=" + URLEncoder.encode(docUrl, "utf-8")
				+ "&docName=" + URLEncoder.encode(docName, "utf-8")
				+ "&skey=" + URLEncoder.encode(skey, "utf-8");
		//http://comment.ifeng.com/vote/?cmtId=322863597&docUrl=http://news.ifeng.com/a/20141219/42751098_0.shtml&_=1419444687431
		URL u3 = new URL("http://comment.ifeng.com/vote.php?cmtId=" + commentId + "&docUrl=" + docUrl + "&_=" + new Date().getTime());
		HttpURLConnection c3 = (HttpURLConnection) u3.openConnection();
		c3.addRequestProperty("Host", "comment.ifeng.com");
		c3.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
		c3.addRequestProperty("Accept", "text/javascript, application/javascript, */*");
		c3.addRequestProperty("Accept-Language", "zh-cn");
		c3.addRequestProperty("Referer", kUrl);
		//c3.addRequestProperty("Cookie", cookie);
		c3.addRequestProperty("Connection", "Keep-Alive");
		c3.connect();
		InputStream i3 = c3.getInputStream();
		Scanner s3 = new Scanner(i3, "utf-8");
		String result = "";
		while(s3.hasNext()){
			result = s3.nextLine();
			System.out.println(result);
		}
		if(result != null && result.contains("已经点过了噢")){
			MQSender.toMQ(taskdo,"失败");
		}else{
			MQSender.toMQ(taskdo,"");
		}
	}
	
}
