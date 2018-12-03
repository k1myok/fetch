package com.longriver.netpro.webview.controller;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Scanner;

import com.alibaba.fastjson.JSON;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

//中国汽车消费网
public class CarZGQCXFWComment {
	public static void main(String arsg[]){
		try{
			TaskGuideBean b = new TaskGuideBean();
			b.setNick("hawkfirm");
			b.setPassword("123123000");
			b.setAddress("http://inf.315che.com/n/2016_07/693821/");
			b.setCorpus("好就是好,加油");
			b.setCorpus("这还凑合吧");
			toRun(b);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("null")
	public static void toRun(TaskGuideBean taskdo){
		
		try {
			
			String contents = taskdo.getCorpus();
			String sUrl = taskdo.getAddress();
			
			int a=sUrl.lastIndexOf("/");
			String id=sUrl.substring(a-6,a);
	        System.out.println(id);
	        
			        URL fu = new URL("http://inf.315che.com/che/addComment");
			    	HttpURLConnection fc = (HttpURLConnection) fu.openConnection();
			    	String fp = "type=1&catalogid="+id+"&content=" + URLEncoder.encode(contents, "utf-8");
					System.out.println(fp);
					fc.addRequestProperty("Host", "inf.315che.com");
					fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
					fc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
					fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
					fc.addRequestProperty("Referer", sUrl);
					fc.addRequestProperty("Connection", "keep-alive");
					fc.addRequestProperty("Content-Length", String.valueOf(fp.length()));
					fc.setInstanceFollowRedirects(false);
					fc.setDoInput(true);
					fc.setDoOutput(true);
					PrintWriter fo = new PrintWriter(fc.getOutputStream());
					fo.print(fp);
					fo.flush();
					InputStream fi = fc.getInputStream();
					Scanner fs = new Scanner(fi);
					while(fs.hasNext()){
						String scsc2 = fs.nextLine();
						System.out.println(scsc2);
					}
					MQSender.toMQ(taskdo,"");
		}catch (Exception e) {
			MQSender.toMQ(taskdo,"报错失败");
		}
		
	}
	
	
}
