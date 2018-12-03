package com.longriver.netpro.webview.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.bouncycastle.util.encoders.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.longriver.netpro.common.ifeng.IFengScript;
import com.longriver.netpro.common.sina.SData;
import com.longriver.netpro.common.sohu._FakeX509TrustManager;
import com.longriver.netpro.util.DateUtil;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

public class IFNewsComment_bak{
	
	public static void main(String s[]){
		TaskGuideBean ff = new TaskGuideBean();
//		ff.setAddress("http://news.ifeng.com/a/20170704/51368537_0.shtml");
		ff.setAddress("http://news.ifeng.com/a/20171021/52731405_0.shtml");
		ff.setNick("martinarzi4");
		ff.setPassword("z19258");
		ff.setCorpus("全球经济绝不是一个国家或者几个国家的事情");
		ifeng(ff);
	}
	
	public static void ifeng(TaskGuideBean taskdo){
		SData data =new SData();
		
		try {
			String userId =taskdo.getNick();
			String pwd = taskdo.getPassword();
			String contents = taskdo.getCorpus();
			String sUrl = taskdo.getAddress();
			
			String type = "1";
			if(userId.contains("@") && userId.contains(".")){
				type = "2";
			}
			
			data.put("user_account_id", userId);
			data.put("user_account_pw", pwd);
			data.put("mission_addr", sUrl);
			data.put("mission_contents", contents);
			String uid = IFengScript.getUid();

			String cookie = "";
			_FakeX509TrustManager.allowAllSSL();
			URL u2 = new URL("https://id.ifeng.com/public/authcode");
			HttpURLConnection c2 = (HttpURLConnection) u2.openConnection();
			c2.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36 SE 2.X MetaSr 1.0");
			c2.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			c2.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			c2.addRequestProperty("Connection", "keep-alive");
			c2.addRequestProperty("Host", "id.ifeng.com");
			c2.addRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
			c2.connect();
			Map<String, List<String>> m1 = c2.getHeaderFields();
			for(Map.Entry<String,List<String>> entry : m1.entrySet()){
				if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
					for(String value : entry.getValue()){
							cookie = value + ";";
					}
				}
			}
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
	        
			//System.out.println(cookie);

			String result = RuoKuai.createByPost("shikai123456","123456789","3040","60","48367","2146c73b5a1541dc92ee9b9d7e8c634f","c:\\vcode\\"+nn+".jpg");
			
			String randRom = result.substring(result.indexOf("<Result>")+8,result.indexOf("</Result>"));
			//System.out.println("result========================"+result);
			System.out.println("result========================"+randRom);
			
			
				
			String param = "u="+ URLEncoder.encode(userId, "utf-8")+"&k="+ URLEncoder.encode(pwd, "utf-8")+"&auth="+randRom+"&auto=on&comfrom=&type="+type;
			
			
			URL u1 = new URL("https://id.ifeng.com/api/sitelogin");
			HttpURLConnection c1 = (HttpURLConnection) u1.openConnection();
			c1.addRequestProperty("Host", "id.ifeng.com");
			c1.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0)Gecko/20100101 Firefox/31.0");
			c1.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			c1.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
			c1.addRequestProperty("Referer", "http://id.ifeng.com/allsite/login");
			c1.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			c1.addRequestProperty("Content-Length", String.valueOf(param.length()));
			c1.addRequestProperty("Cookie", cookie);
			c1.setInstanceFollowRedirects(false);
			c1.setDoInput(true);
			c1.setDoOutput(true);
			PrintWriter out = new PrintWriter(c1.getOutputStream());
			out.print(param);
			out.flush();
			Map<String, List<String>> m11 = c1.getHeaderFields();
			for(Map.Entry<String,List<String>> entry : m11.entrySet()){
				if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
					for(String value : entry.getValue()){
						if(value.indexOf("sid") > -1){
							cookie = cookie + value.substring(0, value.indexOf(";")) + ";";
						}
					}
				}
			}
			
			URL u11 = new URL("http://region.ifeng.com/get?format=js&callback=setRegionCookies");
			HttpURLConnection c11 = (HttpURLConnection) u11.openConnection();
			c11.connect();
			InputStream i11 = c11.getInputStream();
			Scanner s11 = new Scanner(i11);
			while(s11.hasNext()){
				String scsc = s11.nextLine();
				//System.out.println(scsc);
				if(scsc.indexOf("setRegionCookies") > -1){
					String prov = scsc.substring(scsc.indexOf("\"") + 1, scsc.indexOf("_"));
					String city = scsc.substring(scsc.indexOf("_") + 1, scsc.lastIndexOf("_"));
					String weather_city = scsc.substring(scsc.indexOf("[") + 1, scsc.indexOf("]"));
					String ip = scsc.substring(scsc.lastIndexOf("[") + 1, scsc.lastIndexOf(".")) + ".x";
					String region_ver = "1.1";
					
					cookie = "prov=" + prov + "; city=" + city + "; weather_city=" + weather_city + "; region_ip=" + ip + "; region_ver=" + region_ver + "; userid=" + uid + "; " + cookie;
				}
			}
			
			//System.out.println(cookie);
			System.out.println("-----------------------------------------------111111111111111----------------------------------------------");
			//String ss = URLDecoder.decode(sUrl, "utf-8");
			if(sUrl != null && sUrl.indexOf("http://gentie.ifeng") > -1 && sUrl.indexOf("docUrl=") > -1){
				sUrl = URLDecoder.decode(sUrl, "utf-8");
				sUrl = sUrl.substring(sUrl.indexOf("docUrl=")).replace("docUrl=", "");
				if(sUrl.indexOf("&") > -1){
					sUrl = sUrl.substring(0, sUrl.indexOf("&"));
				}
				sUrl = URLDecoder.decode(sUrl, "utf-8");
			}
			//System.out.println(sUrl);
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
				if(scsc.indexOf("\"docUrl\"") > -1){
					if(docUrl.equals("")){
						String docUrlpp = scsc.replace("\"docUrl\"", "");
						docUrlpp = docUrlpp.substring(docUrlpp.indexOf("\"") + 1, docUrlpp.lastIndexOf("\""));
						docUrl = docUrlpp;
						System.out.println("docUrl=="+docUrl);
					}
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
				
			}
			
			String vjuids = IFengScript.getVjuids(cookie);
			String vjlast = IFengScript.getVjlast();
			
			String kUrl = "http://comment.ifeng.com/view.php"
					+ "?docUrl=" + URLEncoder.encode(docUrl, "utf-8")
					+ "&docName=" + URLEncoder.encode(docName, "utf-8")
					+ "&skey=" + URLEncoder.encode(skey, "utf-8");
			URL ku = new URL(kUrl);
			HttpURLConnection kc = (HttpURLConnection) ku.openConnection();
			kc.connect();
			InputStream ki = kc.getInputStream();
			Scanner ks = new Scanner(ki, "utf-8");
			String cmtpass = "";
			while(ks.hasNext()){
				String scsc = ks.nextLine();
				if(scsc.indexOf("type=\"hidden\"") > -1 && scsc.indexOf("cmtpass") > -1){
					cmtpass = scsc.substring(scsc.indexOf("value")).replace("value=\"", "");
					cmtpass = cmtpass.substring(0, cmtpass.indexOf("\""));
				}
			}
			
			cookie = cookie + "vjuids=" + vjuids + "; vjlast=" + vjlast + "; ifengRotator_AP1998=1";
			//System.out.println(cookie);
			System.out.println("-----------------------------------------------222222222222222----------------------------------------------");
			String params = "cmtpass=" + URLEncoder.encode(cmtpass, "utf-8")
					+ "&docName=" + URLEncoder.encode(docName, "utf-8")
					+ "&docUrl=" + URLEncoder.encode(docUrl, "utf-8")
					+ "&speUrl="
					+ "&content=" + URLEncoder.encode(contents, "utf-8")
					+ "&quoteId=";
			URL u3 = new URL("http://comment.ifeng.com/post/?format=json");
			//System.out.println(u3);
			HttpURLConnection c3 = (HttpURLConnection) u3.openConnection();
			c3.addRequestProperty("Host", "comment.ifeng.com");
			c3.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			c3.addRequestProperty("Accept", "*/*");
			c3.addRequestProperty("Accept-Language", "zh-cn");
			c3.addRequestProperty("Referer", kUrl);
			c3.addRequestProperty("Cookie", cookie);
			c3.addRequestProperty("Connection", "Keep-Alive");
			c3.addRequestProperty("Cache-Control", "no-cache");
			c3.addRequestProperty("Content-Length", String.valueOf(params.length()));
			c3.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			c3.addRequestProperty("x-requested-with", "XMLHttpRequest");
			c3.setDoInput(true);
			c3.setDoOutput(true);
			PrintWriter out3 = new PrintWriter(c3.getOutputStream());
			out3.print(params);
			out3.flush();
			InputStream i3 = c3.getInputStream();
			Scanner s3 = new Scanner(i3, "utf-8");
			boolean b = false;
			String content = "";
			while(s3.hasNext()){
				String scsc = s3.nextLine();
				System.out.println(scsc);
				if(scsc.indexOf("1") > -1){
					b = true;
				}else{
					content = "失败";
				}
			}
			
			String missionId = data.getString("mission_id");
			String statusCd = "0";
			String link = "error";
			if(b){
				statusCd = "1";
				link = "提交成功，需要等待2小时左右才能看到提交内容！";
			}
			//service.execute("tb_news_return", missionId, statusCd, link);
			
			MQSender.toMQ(taskdo,content);
		}catch (Exception e) {
			MQSender.toMQ(taskdo,"报错失败");
			e.printStackTrace();
		}
		
	}
	
	public String formatString(String str) {
        if (str != null) {            
        	try {                
        		str = new String(str.getBytes("ISO-8859-1"), "UTF-8");            
        		} catch (Exception e) {}        
        		}
        return str;    
    }
}
