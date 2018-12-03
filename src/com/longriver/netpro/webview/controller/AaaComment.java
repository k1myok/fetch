package com.longriver.netpro.webview.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.RandomStringUtils;

import com.alibaba.fastjson.JSON;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

//东方网
public class AaaComment {
	
	public static String dir = "c:\\";
	public static String path = "c:\\dongfangwang.jpg";
	public static void main(String arsg[]){
		TaskGuideBean b = new TaskGuideBean();
		b.setAddress("http://news.eastday.com/c/20160713/u1a9522779.html");
		b.setAddress("http://sh.eastday.com/m/20170110/u1a10239114.html");
		b.setCorpus("emeenen");
//		b.setNick("lilei1929");
//		b.setPassword("lilei419688");
		toRun(b);
	}
	public static void toRun22(TaskGuideBean taskdo){
		System.out.println("------------");
		try {
			String cookie = "" +
//					"_ga=GA1.2.526393226.1524639484; _gid=GA1.2.1173184401.1524639484; " +
					"SessionID=55809D74D1AFFDBA; CityId=142110000; " +
//					"uid=33518898; WapVer1=0; isnewboard=yes; " +
					"xiciaccount=eyJpdiI6ImhNUWtpVlZndTdkd1lJejlaV1VqUGc9PSIsInZhbHVlIjoiS2wwNFwvRXM2cjlTNlpVTCtVcmxRMnJReUZ4SzN1dlFnY3dEMWpnTkpFWXduMVVQUkZXZHNaY0xxVVI1M1ZuS0kiLCJtYWMiOiJmODU3NjVlNDE2N2ZkZGU2MjAzZWJhMzU4ZjhlMWNjYTBkNjRjNzVhZmI4ZDI3YmRmZWI4ZWQ3NjU4YWRjZDM1In0%3D; " +
//					"ServerID=; " +
//					"lastPingTime=1524639598562; " +
//					"bdshare_firstime=1524639580116; " +
//					"__utma=207079066.526393226.1524639484.1524639581.1524639581.1; __utmb=207079066.3.9.1524639627958; __utmc=207079066; " +
//					"__utmz=207079066.1524639581.1.1.utmcsr=xici.net|utmccn=(referral)|utmcmd=referral|utmcct=/; __utmt=1; " +
//					"Hm_lvt_aefe6b1937699307f217175cc526c1e1=1524639585; Hm_lpvt_aefe6b1937699307f217175cc526c1e1=1524639593; " +
					"token=auth; " +
//					"HostID=55809d74d1affdba%2D6f9bae0f24d8581f05b2f9b9e383a14e" +
					"";
			//发帖
	        URL fu3 = new URL("http://www.xici.net/d246882276.htm");
	    	HttpURLConnection fc3 = (HttpURLConnection) fu3.openConnection();
			fc3.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			fc3.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
			fc3.addRequestProperty("Connection", "keep-alive");
			fc3.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			fc3.addRequestProperty("Cookie", cookie);
			fc3.addRequestProperty("Referer", "http://www.xici.net/");
			fc3.addRequestProperty("Host", "www.xici.net");
			fc3.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:59.0) Gecko/20100101 Firefox/59.0");
			fc3.connect();
			Map<String, List<String>> m133 = fc3.getHeaderFields();
			for(Map.Entry<String,List<String>> entry : m133.entrySet()){
				if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
					for(String value : entry.getValue()){
						System.out.println("cookie==="+value.substring(0, value.indexOf(";")));
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public static void toRun(TaskGuideBean taskdo){
		
		try {
			//西祠发帖测
			URL fu3 = new URL("http://www.xici.net/b1487143/put.asp");
			HttpURLConnection fc3 = (HttpURLConnection) fu3.openConnection();
			String fp = "lDocId=246860107&puttype=1&OpType=&rate=0&lSubDoc=0&isEdit=0&DocVote_a=&doc_type=0&refblock=&doc_refer=&refType=&sTitle=&h=3C4727561C8FED87A1662EBAB3FA659E&reAndRef=&doc_text=%3Cp%3E%BA%DC%CF%B2%BB%B6%2C%B2%BB%B4%ED%3Cbr%2F%3E%3C%2Fp%3E";
			fp = "lDocId=246864811&puttype=1&OpType=&rate=0&lSubDoc=0&isEdit=0&DocVote_a=&doc_type=0&refblock=&doc_refer=&refType=&sTitle=&h=FC3FA3717D66A2567077C0F0E3DFD8CD&reAndRef=&doc_text=%3Cp%3E666%3Cbr%2F%3E%3C%2Fp%3E";
			fp = "lDocId=246864811&puttype=1&OpType=&rate=0&lSubDoc=0&isEdit=0&DocVote_a=&doc_type=0&refblock=&doc_refer=&refType=&sTitle=&h=676983B885EE6A2B83B2D9CD5D295019&reAndRef=&doc_text=%3Cp%3E%B2%BB%B4%ED%3Cbr%2F%3E%3C%2Fp%3E";
			String cookie = "" +
//	    			"_ga=GA1.2.1810185110.1524455178; " +
//	    			"_gid=GA1.2.565411818.1524455178; " +
//	    			"WapVer1=0; " +
			"token=d1b31803faf019a693bc96a3d89490c4; " +
//	    			"HostID=56dc4daec9e4a077%2D8c0b317c097579a02fad26f515181b88; " +
			"CityId=142110000; bdshare_firstime=1524455225506; " +
			
//	    			"Hm_lvt_aefe6b1937699307f217175cc526c1e1=1524546341,1524560647,1524561233,1524618752;" +
//	    			"__utma=207079066.1810185110.1524455178.1524618752.1524618752.13; " +
//	    			"__utmz=207079066.1524563719.11.8.utmcsr=3g.xici.net|utmccn=(referral)|utmcmd=referral|utmcct=/; " +
//	    			"Hm_lvt_0d1e34d57076692d8c11893ab7c4a63f=1524455256; " +
//	    			"UM_distinctid=162f09d3120367-0804c456256913-4c584130-15f900-162f09d31223a1; " +
			
//	    			"CNZZDATA1255058011=519861490-1524453650-http%253A%252F%252Fwww.xici.net%252F%7C1524459051; " +
//	    			"isnewboard=yes; " +
			"xiciaccount=eyJpdiI6ImwxZVYxR1ppZlNsNzZwWXFRbVVpZWc9PSIsInZhbHVlIjoiQ1p1WGJNVkFRcVRcL1dcL2FjcUl3YXFRYm5YSUJpMFVZbzQ0RHpRbnVZTnBrRjJkQnQ3OTFGK3hTVmdYZmpJK1p3IiwibWFjIjoiNDVhOTY4NjMwNGU5MmU4NGY4ZWNlYTdmOTY5YWI3YzgzM2Q3MTc1MDUzMDA3ODE4YjlhMjViZDVkNTNlMzQ1MyJ9; " +
//	    			"ServerID=; " +
			"SessionID=56DC4DAEC9E4A077; " +
//	    			"uid=33518898; lastPingTime=1524619020004; " +
//	    			"Hm_lpvt_aefe6b1937699307f217175cc526c1e1=1524619016; " +
//	    			"__utmb=207079066.6.9.1524619044402; __utmc=207079066; __utmt=1; " +
//	    			"remember_82e5d2c56bdd0811318f0cf078b78bfc=eyJpdiI6IllEc2tEQkpJdGd3RzBwK1VxR3czbnc9PSIsInZhbHVlIjoibzZ5STMxVDkyZjZvS2JWREIwSURmWStCSDUzSlBvNDREc0ZuUjg3aUhXbz0iLCJtYWMiOiIwZDQ4MWU4N2YyY2QxNTFlNThlYWJjZjBiMTk3ZTNjYmIyZjNmMTcyNGY0ZGU3NjU4NGJhZjEwZjk0ZTM4ZTcwIn0%3D; _gat=1" +
			"";
			cookie = "" +
//					"_ga=GA1.2.526393226.1524639484; _gid=GA1.2.1173184401.1524639484; " +
					"SessionID=55809D74D1AFFDBA; " +
					"CityId=142110000; " +
//					"uid=33518898; WapVer1=0; isnewboard=yes; " +
					"xiciaccount=eyJpdiI6ImhNUWtpVlZndTdkd1lJejlaV1VqUGc9PSIsInZhbHVlIjoiS2wwNFwvRXM2cjlTNlpVTCtVcmxRMnJReUZ4SzN1dlFnY3dEMWpnTkpFWXduMVVQUkZXZHNaY0xxVVI1M1ZuS0kiLCJtYWMiOiJmODU3NjVlNDE2N2ZkZGU2MjAzZWJhMzU4ZjhlMWNjYTBkNjRjNzVhZmI4ZDI3YmRmZWI4ZWQ3NjU4YWRjZDM1In0%3D;" +
//					" ServerID=; lastPingTime=1524642541714; bdshare_firstime=1524639580116; __utma=207079066.526393226.1524639484.1524639581.1524639581.1; __utmb=207079066.14.9.1524642569398; __utmc=207079066; __utmz=207079066.1524639581.1.1.utmcsr=xici.net|utmccn=(referral)|utmcmd=referral|utmcct=/; Hm_lvt_aefe6b1937699307f217175cc526c1e1=1524639585; Hm_lpvt_aefe6b1937699307f217175cc526c1e1=1524642456; " +
					"token=c5905a533c3a50b42d0ef7224ce3c92b; " +
//					"HostID=55809d74d1affdba%2D6f9bae0f24d8581f05b2f9b9e383a14e" +
					"";
			cookie = "" +
//					"_ga=GA1.2.526393226.1524639484; _gid=GA1.2.1173184401.1524639484; " +
					"SessionID=55809D74D1AFFDBA; " +
					"CityId=142110000;" +
//					" uid=33518898; WapVer1=0; isnewboard=yes; " +
					"xiciaccount=eyJpdiI6ImhNUWtpVlZndTdkd1lJejlaV1VqUGc9PSIsInZhbHVlIjoiS2wwNFwvRXM2cjlTNlpVTCtVcmxRMnJReUZ4SzN1dlFnY3dEMWpnTkpFWXduMVVQUkZXZHNaY0xxVVI1M1ZuS0kiLCJtYWMiOiJmODU3NjVlNDE2N2ZkZGU2MjAzZWJhMzU4ZjhlMWNjYTBkNjRjNzVhZmI4ZDI3YmRmZWI4ZWQ3NjU4YWRjZDM1In0%3D;" +
//					"ServerID=; lastPingTime=1524646194566; bdshare_firstime=1524639580116; __utma=207079066.526393226.1524639484.1524646191.1524646191.3; __utmc=207079066; __utmz=207079066.1524646191.2.2.utmcsr=xici.net|utmccn=(referral)|utmcmd=referral|utmcct=/b1487143/put.asp; Hm_lvt_aefe6b1937699307f217175cc526c1e1=1524639585; Hm_lpvt_aefe6b1937699307f217175cc526c1e1=1524646190; " +
					"token=ab678f6f05a6eb20fc71d1d39e9db4d0; " +
//					"HostID=55809d74d1affdba%2D6f9bae0f24d8581f05b2f9b9e383a14e; __Corpize_UID_BID=dcf174cafff86a1d174d0db97fc3348a; _gat=1; __utmb=207079066.1.9.1524646206136; __utmt=1; __utmd=1" +
					"";
			System.out.println("fp=="+fp);
			fc3.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			fc3.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
			fc3.addRequestProperty("Cache-Control", "no-cache");
			fc3.addRequestProperty("Connection", "keep-alive");
			fc3.addRequestProperty("Content-Length", "236");
			fc3.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			fc3.addRequestProperty("Cookie", cookie);
			fc3.addRequestProperty("Host", "www.xici.net");
			fc3.addRequestProperty("Pragma", "no-cache");
			fc3.addRequestProperty("Referer", "http://www.xici.net/d246860107.htm");
			fc3.addRequestProperty("Upgrade-Insecure-Requests", "1");
			fc3.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:59.0) Gecko/20100101 Firefox/59.0");
			fc3.setInstanceFollowRedirects(true);
			fc3.setDoInput(true);
			fc3.setDoOutput(true);
			PrintWriter fo3 = new PrintWriter(fc3.getOutputStream());
			fo3.print(fp);
			fo3.flush();
			InputStream fi = fc3.getInputStream();
			Scanner fs = new Scanner(fi);
			while(fs.hasNext()){
				String scsc2 = fs.nextLine();
				System.out.println("scsc2=="+scsc2);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}
