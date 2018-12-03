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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.common.ifeng.IFengScript;
import com.longriver.netpro.common.sohu._FakeX509TrustManager;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

public class IFNewsComment2{
	
	public static void main(String s[]){
		TaskGuideBean ff = new TaskGuideBean();
//		ff.setAddress("http://gentie.ifeng.com/view.html?docUrl=http%3A%2F%2Fnews.ifeng.com%2Fa%2F20171008%2F52489527_0.shtml&docName=%E3%80%8A%E4%B8%8D%E5%BF%98%E5%88%9D%E5%BF%83%20%E7%BB%A7%E7%BB%AD%E5%89%8D%E8%BF%9B%E3%80%8B%E7%AC%AC%E5%9B%9B%E9%9B%86%E3%80%8A%E5%87%9D%E5%BF%83%E9%93%B8%E9%AD%82%E3%80%8B&skey=7e38a4&pcUrl=http://news.ifeng.com/a/20171008/52489527_0.shtml");
		ff.setAddress("http://news.ifeng.com/a/20171008/52489527_0.shtml");
		ff.setAddress("http://news.ifeng.com/a/20180409/57432619_0.shtml");
//		ff.setAddress("http://news.ifeng.com/a/20171021/52731405_0.shtml");
		ff.setNick("15652240394");
		ff.setPassword("lilei419688");
//		ff.setNick("Dqyesmars");
//		ff.setPassword("dongyuqi123123");
//		ff.setNick("13690730863");
//		ff.setPassword("qqqwww");
		ff.setCorpus("治国有常，而利民为本");
		ff.setCorpus("就在今天");
		ifeng(ff);
//		toComment();
//		getCookie1();
	}
	
	public static void ifeng(TaskGuideBean taskdo){
		try {
//			String uid = IFengScript.getUid();
			String cookie = login(taskdo,0);
			System.out.println("cookie=="+cookie);
			if(cookie!=null && !cookie.equals("")){
				toComment(taskdo,cookie);
			}else{
				MQSender.toMQ(taskdo,"登录失败");
				System.out.println("凤凰登录失败!");
			}
		}catch (Exception e) {
			MQSender.toMQ(taskdo,"报错失败");
			e.printStackTrace();
		}
		
	}
	public static void toComment(TaskGuideBean taskdo,String cookie){
		String contents = taskdo.getCorpus();
		String sUrl = taskdo.getAddress();
		try {
			Map<String,String> map = getParams(sUrl);
			String docUrl = URLEncoder.encode(map.get("docUrl"), "utf-8");
			String docName = URLEncoder.encode(map.get("docName"), "utf-8");
			String pp = "callback=postCmt&docUrl="+docUrl+
				"&docName="+docName+"&speUrl=&skey="+map.get("skey")+
				"&format=js&content="+URLEncoder.encode(contents, "utf-8")+"&callback=postCmt";
			System.out.println("params::"+pp);
			String url = "http://comment.ifeng.com/post.php?"+pp;
			URL u3 = new URL(url);
			//System.out.println(u3);
			HttpURLConnection c3 = (HttpURLConnection) u3.openConnection();
			c3.addRequestProperty("Accept", "*/*");
			c3.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			c3.addRequestProperty("Connection", "Keep-Alive");
			c3.addRequestProperty("Host", "comment.ifeng.com");
			c3.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			c3.addRequestProperty("Referer", "http://gentie.ifeng.com/view.html?docUrl="+docUrl+"&docName="+docName+"&skey="+map.get("skey")+"&pcUrl="+sUrl);
			c3.addRequestProperty("Cookie", cookie);
//			c3.addRequestProperty("Cookie", "sid=34F58D6FA03A1269ABD91CBAD7993F46user62138439;");
			c3.connect();
			InputStream i3 = c3.getInputStream();
			Scanner s3 = new Scanner(i3, "utf-8");
			String content = "失败";
			while(s3.hasNext()){
				String scsc = s3.nextLine();
				System.out.println(scsc);
				int code = getCode(scsc);
				if(code==117){
					System.out.println("请您进行手机核验");
					content = "请您进行手机核验";
					break;
				}if(code==109){
					System.out.println("标题和url不一致！,有可能是一致的");
					content = "标题和url不一致";
					break;
				}else if(code==1){
					content = "";
					System.out.println("凤凰发帖成功!");
					break;
				}else{
					content = "失败";
				}
				if(scsc.contains("code")){
					break;
				}
			}
			MQSender.toMQ(taskdo,content);
		} catch (Exception e) {
			MQSender.toMQ(taskdo,"报错失败");
			e.printStackTrace();
		}
	}
	public static int getCode(String scsc){
		int i1 = scsc.indexOf("={");
		scsc = scsc.substring(i1+1, scsc.length()-1);
		JSONObject jb = JSONObject.parseObject(scsc);
		int code = jb.getIntValue("code");
		return code;
	}
	public static Map<String,String> getParams(String sUrl) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		if(sUrl != null && sUrl.indexOf("http://gentie.ifeng") > -1 && sUrl.indexOf("docUrl=") > -1){
			sUrl = URLDecoder.decode(sUrl, "utf-8");
			sUrl = sUrl.substring(sUrl.indexOf("docUrl=")).replace("docUrl=", "");
			if(sUrl.indexOf("&") > -1){
				sUrl = sUrl.substring(0, sUrl.indexOf("&"));
			}
			sUrl = URLDecoder.decode(sUrl, "utf-8");
		}
		//http://news.ifeng.com/a/20171008/52489527_0.shtml
		URL u21 = new URL(sUrl);
		HttpURLConnection c21 = (HttpURLConnection) u21.openConnection();
		c21.connect();
		InputStream i21 = c21.getInputStream();
		Scanner s21 = new Scanner(i21, "utf-8");
		
		String docUrl = "";
		String docName = "";
		String skey = "";
		
		while(s21.hasNext()){
			String scsc = s21.nextLine();
//			System.out.println("00sc="+scsc);
			if(scsc.indexOf("\"docUrl\"") > -1){
				if(docUrl.equals("")){
					String docUrlpp = scsc.replace("\"docUrl\"", "");
					docUrlpp = docUrlpp.substring(docUrlpp.indexOf("\"") + 1, docUrlpp.lastIndexOf("\""));
					docUrl = docUrlpp;
					System.out.println("docUrl=="+docUrl);
				}
			}
			//docUrl 也可能是这里面的数据
			if(scsc.indexOf("\"commentUrl\"") > -1){
				String docUrlpp = scsc.replace("\"commentUrl\"", "");
				docUrlpp = docUrlpp.substring(docUrlpp.indexOf("\"") + 1, docUrlpp.lastIndexOf("\""));
				docUrl = docUrlpp;
				System.out.println("docUrl=="+docUrl);
			}
			
			if(scsc.indexOf("\"docName\"") > -1){
				System.out.println(scsc);
				if(docName.equals("")){
					String docNamepp = scsc.replace("\"docName\"", "");
					docNamepp = docNamepp.substring(docNamepp.indexOf("\"") + 1, docNamepp.lastIndexOf("\""));
					docName = docNamepp;
					System.out.println("docName=="+docName);
				}
			}
			if(scsc.indexOf("\"skey\"") > -1){
				if(skey.equals("")){
					String skeypp = scsc.replace("\"skey\"", "");
					skeypp = skeypp.substring(skeypp.indexOf("\"") + 1, skeypp.lastIndexOf("\""));
					skey = skeypp;
					System.out.println("skey=="+skey);
				}
			}
			map.put("docUrl", docUrl);
			map.put("docName", docName);
			map.put("skey", skey);
		}
		return map;
	}
	public static String login(TaskGuideBean taskdo,int number){
		System.out.println("login...");
		String userId =taskdo.getNick();
		String pwd = taskdo.getPassword();
		
		String type = "1";
		if(userId.contains("@") && userId.contains(".")){
			type = "2";
		}else if(userId.length()==11 && isNumeric(userId)){
			type = "3";
		}
		System.out.println("type=="+type);
		String nn = "ifengcode";
		String cookie = getCode();
		String randRom = RuoKuai.createByPostNew("3040","c:\\vcode\\"+nn+".jpg");
		System.out.println("验证码:"+randRom);
		String param = "u="+ StringUtil.encode(userId)+"&k="+ pwd+"&auth="+randRom+"&auto=false&cb=&type="+type;
		String sidCookie = "";
		try {
			URL u1 = new URL("https://id.ifeng.com/api/login?callback=jQuery18306936583252257916_1502243243195");
			HttpURLConnection c1 = (HttpURLConnection) u1.openConnection();
			c1.addRequestProperty("Accept", "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01");
			c1.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			c1.addRequestProperty("Connection", "keep-alive");
			c1.addRequestProperty("Content-Length", String.valueOf(param.length()));
			c1.addRequestProperty("Host", "id.ifeng.com");
			c1.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			c1.addRequestProperty("Cookie", cookie);
			c1.addRequestProperty("Referer", "https://id.ifeng.com/user/login");
			c1.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0");
			c1.addRequestProperty("X-Requested-With", "XMLHttpRequest");
			c1.setInstanceFollowRedirects(false);
			c1.setDoInput(true);
			c1.setDoOutput(true);
			PrintWriter out = new PrintWriter(c1.getOutputStream());
			out.print(param);
			out.flush();
			
			InputStream i1 = null;
			try{
				i1 = c1.getInputStream();
				Scanner s5 = new Scanner(i1);
				while(s5.hasNext()){
					String scsc = s5.nextLine();
					scsc = StringUtil.decodeUnicode(scsc);
					System.out.println("scsc================"+scsc);
					if(scsc.contains("验证码错误")){
						System.out.println("验证码错误!");
						number++;
						if(number>=4)
							return "";
						else{
							System.out.println("验证码错误次数:"+number);
							return login(taskdo,number);
						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
			Map<String, List<String>> m11 = c1.getHeaderFields();
			for(Map.Entry<String,List<String>> entry : m11.entrySet()){
				if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
					System.out.println("entry.getKey()=="+entry.getValue());
					for(String value : entry.getValue()){
						if(value.indexOf("sid") > -1){
							System.out.println("sid=="+value);
							sidCookie = value.substring(0, value.indexOf(";")) + ";";
							sidCookie += getCookie_Region();
						}
					}
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sidCookie;
	}
	public static boolean isNumeric(String str){
		  for (int i = 0; i < str.length(); i++){
		   if (!Character.isDigit(str.charAt(i))){
		   		return false;
		   }
		  }
		  return true;
	}
	public static String getCode(){
		String cookie = getCookie1();
		try {
//			_FakeX509TrustManager.allowAllSSL();
			URL u2 = new URL("https://id.ifeng.com/public/authcode");
			HttpURLConnection c2 = (HttpURLConnection) u2.openConnection();
			c2.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36 SE 2.X MetaSr 1.0");
			c2.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			c2.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			c2.addRequestProperty("Connection", "keep-alive");
			c2.addRequestProperty("Host", "id.ifeng.com");
			c2.addRequestProperty("Cookie", cookie);
			c2.addRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
			c2.connect();
			InputStream i2 = c2.getInputStream();
			
			// 1K的数据缓冲  
			byte[] bs = new byte[1024];  
			// 读取到的数据长度  
			int len;  
			// 输出的文件流  
	      File sf=new File("c:\\");  
	      if(!sf.exists()){  
	    	  sf.mkdirs();  
	      }
	      File file =new File("C:\\vcode");    
	    //如果文件夹不存在则创建    
	      if(!file .exists()  && !file .isDirectory()){       
				 System.out.println("//不存在");  
				 file.mkdir();    
	      }
	      String nn = "ifengcode";
	      OutputStream os = new FileOutputStream("c:\\vcode\\"+nn+".jpg");  
		  // 开始读取  
				while ((len = i2.read(bs)) != -1) {  
				  os.write(bs, 0, len);  
				}  
				// 完毕，关闭所有链接  
				os.close();  
				i2.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
        return cookie;
	}
	public static String getCookie1(){
		String cookie = "";
		try {
			_FakeX509TrustManager.allowAllSSL();
			URL u11 = new URL("https://id.ifeng.com/user/login");
			HttpURLConnection c11 = (HttpURLConnection) u11.openConnection();
			c11.connect();
			Map<String, List<String>> m11 = c11.getHeaderFields();
			for(Map.Entry<String,List<String>> entry : m11.entrySet()){
				if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
					for(String value : entry.getValue()){
						if(value.indexOf("PHPSESSID") > -1){
							System.out.println("PHPSESSID=="+value);
							cookie = cookie + value.substring(0, value.indexOf(";")) + ";";
						}
					}
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("cookie1=="+cookie);
		return cookie;
	}
	public static String getCookie_Region(){
		String cookie = "";
		try {
			URL u11 = new URL("http://region.ifeng.com/get?format=js&callback=setRegionCookies");
			HttpURLConnection c11 = (HttpURLConnection) u11.openConnection();
			c11.connect();
			InputStream i11 = c11.getInputStream();
			Scanner s11 = new Scanner(i11);
			while(s11.hasNext()){
				String scsc = s11.nextLine();
				System.out.println("scsc==="+scsc);
				if(scsc.indexOf("setRegionCookies") > -1){
					String prov = scsc.substring(scsc.indexOf("\"") + 1, scsc.indexOf("_"));
					String city = scsc.substring(scsc.indexOf("_") + 1, scsc.lastIndexOf("_"));
					String weather_city = scsc.substring(scsc.indexOf("[") + 1, scsc.indexOf("]"));
					String ip = scsc.substring(scsc.lastIndexOf("[") + 1, scsc.lastIndexOf(".")) + ".x";
					String region_ver = "1.1";
					cookie = "prov=" + prov + "; city=" + city + "; weather_city=" + weather_city + "; region_ip=" + ip + "; region_ver=" + region_ver + "; userid=" + IFengScript.getUid() + "; " + cookie;
				}
			}
//			String vjuids = IFengScript.getVjuids(cookie);
//			String vjlast = IFengScript.getVjlast();
//			cookie = cookie + "vjuids=" + vjuids + "; vjlast=" + vjlast + "; ifengRotator_AP1998=1;";
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("cookie1=="+cookie);
		return cookie;
	}
	
}
