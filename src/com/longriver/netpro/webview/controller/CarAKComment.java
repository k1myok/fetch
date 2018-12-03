package com.longriver.netpro.webview.controller;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import com.alibaba.fastjson.JSON;
import com.longriver.netpro.common.sohu.QCZJScript;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;
/**
 * 爱卡汽车评论
 */
public class CarAKComment {
	
	public static void main(String arsg[]){
		try{
			TaskGuideBean b = new TaskGuideBean();
			b.setNick("18610647727");
			b.setPassword("wangmeng121");
			b.setAddress("http://info.xcar.com.cn/201607/news_1948107_1.html");
//			b.setAddress("http://info.xcar.com.cn/201604/news_1923491_1.html");
			b.setCorpus("争取能买一个");
			b.setCorpus("争取能买一个!!!");
			yc(b);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void yc(TaskGuideBean taskdo){
		try {
			String userId = taskdo.getNick();
			String pwd = taskdo.getPassword();
			String sUrl = taskdo.getAddress();
			String contents = taskdo.getCorpus();
			
			String cookie = "";
			String newId = sUrl.substring(sUrl.lastIndexOf("/")+1,sUrl.indexOf(".html"));
			newId = newId.substring(newId.indexOf("_")+1,newId.lastIndexOf("_"));
			URL u1 = new URL("http://reg.xcar.com.cn/login.php?callback=jQuery183023459003842435777_1459925497119&_="+new Date().getTime());
			HttpURLConnection c1 = (HttpURLConnection) u1.openConnection();
			//_FakeX509TrustManager.allowAllSSL();
			c1.addRequestProperty("Host", "reg.xcar.com.cn");
			c1.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			c1.addRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
			c1.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
			c1.addRequestProperty("Referer", sUrl);
			c1.addRequestProperty("X-Requested-With", "XMLHttpRequest");
			c1.connect();
			InputStream f= c1.getInputStream();
			Scanner fs = new Scanner(f);
			String chash="";
			String chk_k="";
			String chk_seed="";
			String chk_u="";
			String dhash="";
			String ehash="";
			String formhash="";
			while(fs.hasNext()){
				String scsc11 = fs.nextLine();
				if(scsc11.indexOf("chash") > -1){
					chash = scsc11.substring(scsc11.indexOf("chash")).replace("chash", "");
					chash = chash.substring(chash.indexOf(":")+1, chash.indexOf(","));
					chash=chash.replace("\"", "");
				}
				if(scsc11.indexOf("chk_k") > -1){
					chk_k = scsc11.substring(scsc11.indexOf("chk_k")).replace("chk_k", "");
					chk_k = chk_k.substring(chk_k.indexOf(":")+1, chk_k.indexOf(","));
					chk_k=chk_k.replace("\"", "");
				}
				if(scsc11.indexOf("chk_seed") > -1){
					chk_seed = scsc11.substring(scsc11.indexOf("chk_seed")).replace("chk_seed", "");
					chk_seed = chk_seed.substring(chk_seed.indexOf(":")+1, chk_seed.indexOf(","));
					chk_seed=chk_seed.replace("\"", "");
				}
				if(scsc11.indexOf("dhash") > -1){
					dhash = scsc11.substring(scsc11.indexOf("dhash")).replace("dhash", "");
					dhash = chk_seed.substring(dhash.indexOf(":")+1, dhash.indexOf(","));
					dhash=dhash.replace("\"", "");
				}
				if(scsc11.indexOf("ehash") > -1){
					ehash = scsc11.substring(scsc11.indexOf("ehash")).replace("ehash", "");
					ehash = ehash.substring(ehash.indexOf(":")+1, ehash.indexOf(","));
					ehash=ehash.replace("\"", "");
				}
				if(scsc11.indexOf("formhash") > -1){
					formhash = scsc11.substring(scsc11.indexOf("formhash")).replace("formhash", "");
					formhash = formhash.substring(formhash.indexOf(":")+1, formhash.indexOf(","));
					formhash=formhash.replace("\"", "");
				}
				if(scsc11.indexOf("chk_u") > -1){
					chk_u = scsc11.substring(scsc11.indexOf("chk_u")).replace("chk_u", "");
					chk_u = chk_u.substring(chk_u.indexOf(":")+1, chk_u.indexOf("}"));
					chk_u=chk_u.replace("\"", "");
				}
			}			
			String params = "&chash="+chash+"&chk_k="+chk_k+"&chk_seed="+chk_seed+"&chk_u="+chk_u+"&dhash="+dhash+"&formhash="+formhash;
			cookie = getCookies(sUrl,params,userId,pwd);
				System.out.println(cookie);	
				if(cookie!=null && !cookie.equals("")){
					sentContent(cookie,contents,sUrl,newId,taskdo);
				}else{
					MQSender.toMQ(taskdo,"失败");
				}
		} catch (Exception e) {
			MQSender.toMQ(taskdo,"报错失败");
			e.printStackTrace();
		}
	}
	public static String getCookies(String url,String params,String user,String pwd){
		String cookies = "";
		try{
			URL u1 = new URL("http://reg.xcar.com.cn/login.php?action=login&callback=jQuery18303083275160752237_1459925219559"+params+"&username="+user+"&password="+new QCZJScript().getUid(pwd)+"&_="+new Date().getTime());
			HttpURLConnection fc11 = (HttpURLConnection) u1.openConnection();
			fc11.addRequestProperty("Accept", "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01");
			fc11.addRequestProperty("Accept-Language", "zh-cn");
			fc11.addRequestProperty("Connection", "Keep-Alive");
			fc11.addRequestProperty("X-Requested-With", "XMLHttpRequest");
			fc11.addRequestProperty("Host", "reg.xcar.com.cn");
			fc11.addRequestProperty("Referer", url);
			fc11.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			fc11.connect();
			InputStream fi11 = fc11.getInputStream();
			Scanner fs11 = new Scanner(fi11);
			Map<String, List<String>> m1 = fc11.getHeaderFields();
			for(Map.Entry<String,List<String>> entry : m1.entrySet()){
				if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
					for(String value : entry.getValue()){
						cookies = cookies + value.substring(0, value.indexOf(";")) + ";";
					}
				}
			}	
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return cookies;
	}
	
	public  static void sentContent(String cookie,String content,String sUrl,String newId,TaskGuideBean taskdo){
		try{
			URL fu11 = new URL("http://comment.xcar.com.cn/icomment_news2.php");
			String param = "action=Dresponse&cid="+newId
					+ "&content=" + URLEncoder.encode(content, "utf-8")
					+ "&ctype=0&curl="+URLEncoder.encode(sUrl, "utf-8")
					+ "&istoeditor=&date="+new Date().getTime();
			System.out.println(param);
			HttpURLConnection fc11 = (HttpURLConnection) fu11.openConnection();
			fc11.addRequestProperty("Accept", "text/html, */*; q=0.01");
			fc11.addRequestProperty("Accept-Language", "zh-cn");
			fc11.addRequestProperty("Connection", "Keep-Alive");
			fc11.addRequestProperty("Host", "comment.xcar.com.cn");
			fc11.addRequestProperty("Cookie", cookie);
			fc11.addRequestProperty("Referer","http://comment.xcar.com.cn/icomment_news2.php?cid="+newId+"&curl="+sUrl);
			fc11.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			fc11.addRequestProperty("Content-Length", String.valueOf(param.length()));
			fc11.setDoInput(true);
			fc11.setDoOutput(true);
			PrintWriter out = new PrintWriter(fc11.getOutputStream());
			out.print(param);
			out.flush();
			InputStream f= fc11.getInputStream();
			Scanner fs = new Scanner(f);
			while(fs.hasNext()){
				String scsc11 = fs.nextLine();
				System.out.println(scsc11);
			}
			MQSender.toMQ(taskdo,"");
		}catch(Exception e){
			MQSender.toMQ(taskdo,"失败");
			e.printStackTrace();
		}
		
		
	}
	public void test(){
		try{
			URL fu11 = new URL("http://api.map.baidu.com/geocoder/v2/?ak=T7YvwECXPcoulwf8lXaFnqn9DBGQ0Goc&callback=renderReverse&location=39.975608,116.380304&output=json&pois=1");
			
			HttpURLConnection fc11 = (HttpURLConnection) fu11.openConnection();
			fc11.connect();
			InputStream f= fc11.getInputStream();
			Scanner fs = new Scanner(f);
			while(fs.hasNext()){
				String scsc11 = fs.nextLine();
				System.out.println(scsc11);
			}	
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
}
