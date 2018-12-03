package com.longriver.netpro.webview.controller;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.alibaba.fastjson.JSON;
import com.longriver.netpro.common.sohu._FakeX509TrustManager;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 界面网 jiemian
 */
public class CarJMComment {
	
	public static void main(String arsg[]){
		try{
			TaskGuideBean b = new TaskGuideBean();
			b.setNick("13366550854@163.com");
			b.setPassword("shikai");
			b.setAddress("http://www.jiemian.com/article/737456.html");
			b.setCorpus("很加油");
			b.setCorpus("很加油,就是很好");
			toRun(b);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void toRun(TaskGuideBean taskdo){
		
		try {
			String userId = taskdo.getNick();
			String pwd = taskdo.getPassword();
			String contents = taskdo.getCorpus();
			String sUrl =taskdo.getAddress();
			int ind1 = sUrl.indexOf(".htm");
			String urlt = sUrl.substring(0, ind1);
			int ind2 = urlt.lastIndexOf("/");
			String postid = urlt.substring(ind2+1);
			
			String cookie="";
			String urluserid = URLEncoder.encode(userId, "utf-8");
			String loginUrl = "https://passport.jiemian.com/user/logindo?from=jm&backurl=https%253A%252F%252Fa.jiemian.com%252Findex.php%253Fm%253Duser%2526a%253Ddynamic%2526id%253D111971673&account="+urluserid+"&password="+pwd+"&verify_code=&remember=on&callback=jQuery110207651941264959382_1484014375880&_=1484014375881";
		    URL u1 = new URL(loginUrl);
		  
		    _FakeX509TrustManager.allowAllSSL();//设置证书
			HttpURLConnection c1 = (HttpURLConnection) u1.openConnection();
			c1.addRequestProperty("Accept","text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01");
			c1.addRequestProperty("Accept-Language	","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			c1.addRequestProperty("Connection","keep-alive");
			c1.addRequestProperty("Host", "passport.jiemian.com");
			c1.addRequestProperty("Referer", "https://passport.jiemian.com/user/loginbox?backurl=https%3A%2F%2Fa.jiemian.com%2Findex.php%3Fm%3Duser%26a%3Ddynamic%26id%3D111971673");
			c1.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:49.0) Gecko/20100101 Firefox/49.0");
			c1.addRequestProperty("X-Requested-With", "XMLHttpRequest");
			c1.connect();
			Map<String, List<String>> m1 = c1.getHeaderFields();
			for(Map.Entry<String,List<String>> entry : m1.entrySet()){
				System.out.println("key=="+entry.getKey());
				System.out.println("value=="+entry.getValue());
				if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
					for(String value : entry.getValue()){
						cookie = cookie + value.substring(0, value.indexOf(";")) + ";";
					}
				}
			}
			System.out.println("cookie=="+cookie);
			
			URL fu = new URL("http://a.jiemian.com/index.php?m=comment&a=apiAddP");
			HttpURLConnection fc = (HttpURLConnection) fu.openConnection();
			String fp = "aid="+postid+"&content=" + URLEncoder.encode(contents, "utf-8");
			System.out.println(fp);
			fc.addRequestProperty("Host", "a.jiemian.com");
			fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
			fc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			fc.addRequestProperty("Referer", sUrl);
			fc.addRequestProperty("Cookie", cookie);
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
			if(cookie!=null && !cookie.equals("")){
				MQSender.toMQ(taskdo,"");
			}else{
				MQSender.toMQ(taskdo,"失败");
			}
					
		}catch (Exception e) {
			MQSender.toMQ(taskdo,"报错失败");
			e.printStackTrace();
		}
		
	}
	
	
}
