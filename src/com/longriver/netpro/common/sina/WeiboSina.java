package com.longriver.netpro.common.sina;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.longriver.netpro.common.sohu._FakeX509TrustManager;

import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;


public class WeiboSina {
	
	public static String imgPath = "c:\\vcode\\sinacode.jpg";
	public static String fPath = "c:\\";
	public static String jsFileName = "c:\\sina.js";   // 鐠囪褰噅s閺傚洣娆�   
	public static boolean flag = true;
	//登录超时处理,防止任务卡死
	public static String getCookie(final SData data,final int idx) throws Exception{
		System.out.println("getCookie=============");
		flag = true;
        try {  
        	return getCookietime(data,idx);
        }catch (Exception e) {  
            System.out.println("处理超时啦.");  
            try {
            	Thread.sleep(1000*4);
				return getCookietime(data,idx);
			} catch (Exception e1) {
				e1.printStackTrace();
				return "10";
			}
        }  
	}
	public static String getCookietime(SData data,int idx) throws Exception{
		String username = data.getString("user_account_id");
		String password = data.getString("user_account_pw");;
		String ssoLoginState = "";
        String alf = "";
        String subp = "";
        String sub = "";
        String sup = "";
        String sue = "";
        String sus = "";
        String utrs2 = "";
        String utrs1 = "";
        String cookie = "";
        String pcid = "";
        String link = "error";
        String content = "";
        boolean iswrongpwd = false;
        boolean pin = true;
        String pUrl="";
		
		String su = URLEncoder.encode(Base64Utils.encode(URLEncoder.encode(username, "utf-8").getBytes()), "utf-8");
		URL u2 = new URL("http://login.sina.com.cn/sso/prelogin.php?entry=weibo&callback=sinaSSOController.preloginCallBack&su=" + su + "&rsakt=mod&checkpin=1&client=ssologin.js(v1.4.18)&_=" + new Date().getTime());
		HttpURLConnection c2 = (HttpURLConnection) u2.openConnection();
		c2.setConnectTimeout(1000 * 45);
		c2.setReadTimeout(1000 * 20);
		c2.connect();
		InputStream i2 = c2.getInputStream();
		Scanner s2 = new Scanner(i2);
		String servertime = "";
		String pubkey = "";
		String nonce = "";
		String rsakv = "";
		while(s2.hasNext()){
			String scsc = s2.nextLine();
			if(scsc.indexOf("servertime") > -1){

				if(scsc.indexOf("\"servertime\"")>-1)
					servertime = scsc.substring(scsc.indexOf("\"servertime\"")).replace("\"servertime\"", "").trim();
				if(servertime.indexOf(",")>-1)
					servertime = servertime.substring(1, servertime.indexOf(","));
			}
			if(scsc.indexOf("pubkey") > -1){
				if(scsc.indexOf("\"pubkey\"")>-1)
					pubkey = scsc.substring(scsc.indexOf("\"pubkey\"")).replace("\"pubkey\"", "").trim();
				if(pubkey.indexOf(",")>-1)
					pubkey = pubkey.substring(2, pubkey.indexOf(",")).replace("\"", "");
			}
			if(scsc.indexOf("nonce") > -1){
				if(scsc.indexOf("\"nonce\"")>-1)
					nonce = scsc.substring(scsc.indexOf("\"nonce\"")).replace("\"nonce\"", "").trim();
				if(nonce.indexOf(",")>-1)
					nonce = nonce.substring(2, nonce.indexOf(",")).replace("\"", "");
			}
			if(scsc.indexOf("rsakv") > -1){
				if(scsc.indexOf("\"rsakv\"")>-1)
					rsakv = scsc.substring(scsc.indexOf("\"rsakv\"")).replace("\"rsakv\"", "").trim();
				if(rsakv.indexOf(",")>-1)
					rsakv = rsakv.substring(2, rsakv.indexOf(",")).replace("\"", "");
			}
			if(scsc.indexOf("pcid") > -1){
				if(scsc.indexOf("\"pcid\"")>-1)
					pcid = scsc.substring(scsc.indexOf("\"pcid\"")).replace("\"pcid\"", "").trim();
				if(pcid.indexOf(",")>-1)
					pcid = pcid.substring(2, pcid.indexOf(",")).replace("\"", "");
			}
			if(scsc.indexOf("showpin") > -1){
				if(scsc.indexOf("\"showpin\"")>-1){
					String showpin = scsc.substring(scsc.indexOf("\"showpin\"")).replace("\"showpin\"", "").trim();
					if(showpin.indexOf(",")>-1)
						showpin = showpin.substring(1, showpin.indexOf(",")).replace("\"", "");
					//System.out.println(showpin);
					if(showpin.equals("1")){
						pin = false;
					}
				}
			}
		}

		
		if(flag){
			System.out.println("1111111111111111111");
			URL u3 = new URL("http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.18)");
			HttpURLConnection c3 = (HttpURLConnection) u3.openConnection();
			String params = "";
			System.out.println("pcid====="+pcid+"==rsakv=="+rsakv+"===servertime===="+servertime+"===nonce=="+nonce+"==pubkey===="+pubkey);
			params = "entry=weibo"
					+ "&gateway=1"
					+ "&from="
					+ "&savestate=7"
					+ "&useticket=1"
					+ "&pagerefer=http://login.sina.com.cn/sso/logout.php?entry=miniblog&r=http%3A%2F%2Fweibo.com%2Flogout.php%3Fbackurl%3D%252F"
					+ "&vsnf=1"
					+ "&su=" + su
					+ "&service=miniblog"
					+ "&servertime=" + servertime
					+ "&nonce=" + nonce
					+ "&pwencode=rsa2"
					+ "&rsakv=" + rsakv
					+ "&sp="+ getSpJs(pubkey, servertime, nonce, password)
					+ "&sr=1440*900"
					+ "&encoding=UTF-8"
					+ "&prelt=285"
					+ "&url=http://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack"
					+ "&returntype=META";
			c3.addRequestProperty("Host", "login.sina.com.cn");
//			c3.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			c3.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		    c3.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
		    c3.addRequestProperty("Referer", "http://weibo.com/");
		    c3.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		    c3.addRequestProperty("Content-Length", String.valueOf(params.length()));
		    //c3.addRequestProperty("Cookie", cookie);
			c3.setDoOutput(true);
			c3.setDoInput(true);

			c3.setConnectTimeout(1000 * 30);
			c3.setReadTimeout(1000 * 20);
			c3.connect();
			PrintWriter out = new PrintWriter(c3.getOutputStream());
			out.print(params);
			out.flush();
			InputStream i3 = c3.getInputStream();
		    Scanner s3 = new Scanner(i3);
		    while(s3.hasNext()){
		    	String scsc = s3.nextLine();
		    	System.out.println(scsc);
		    	if(scsc.indexOf("location.replace") > -1 && scsc.indexOf("passport.weibo.com") > -1){
		    		pUrl = scsc.substring(scsc.indexOf("location.replace"));
		    		pUrl = pUrl.substring(pUrl.indexOf("'") + 1);
		    		pUrl = pUrl.substring(0, pUrl.indexOf("'"));
		    	}
		    	if(scsc.indexOf("retcode=2070") > -1|| scsc.indexOf("retcode=4049") > -1){
		    		System.out.println("scsc=="+scsc);
		    		idx++;
					if(idx < 3){
						flag = false;
						cookie  = getCookietime(data, idx);
						return cookie;
					}
		    	}
		    	if(scsc.indexOf("retcode=101") > -1){
					System.out.println("用户名密码错误-0");
		    	}
		    
		    }
		   
		    	Map<String, List<String>> headers2 = c3.getHeaderFields(); 
			    for(Map.Entry<String,List<String>> entry : headers2.entrySet()){
			        if(entry.getKey() != null && (entry.getKey().indexOf("Set-Cookie") > -1)){
			            for(String value : entry.getValue()){  
			            	cookie = cookie + value.substring(0, value.indexOf(";") + 1);
			            }  
			        }
			    }
			    System.out.println(c3.getHeaderField("Location"));
		    
		}else{
			
			URL u211 = new URL("http://login.sina.com.cn/cgi/pin.php?r=37865954&s=0&p="+pcid);
			HttpURLConnection c211 = (HttpURLConnection) u211.openConnection();
//			c211.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			c211.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			c211.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			//c211.addRequestProperty("Connection", "keep-alive");
			//c211.addRequestProperty("Cookie", cookie);
			c211.setConnectTimeout(1000 * 30);
			c211.connect();
			
			InputStream i211 = c211.getInputStream();
			
		    byte[] bs = new byte[1024];  
		    int len;  
		   File sf=new File(fPath);  
		   if(!sf.exists()){  
		       sf.mkdirs();  
		   }  
		   OutputStream os = new FileOutputStream(imgPath);  
		    while ((len = i211.read(bs)) != -1) {  
		      os.write(bs, 0, len);  
		    }  
		    os.close();  
		    i211.close();  
		    
		
		    String randRom = RuoKuai.createByPostNew("3050",imgPath);
			System.out.println("result========================"+randRom);
			//randRom = "3131";



			URL u31 = new URL("http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.18)");
			HttpURLConnection c31 = (HttpURLConnection) u31.openConnection();
			String params1 = "";
			params1 = "entry=weibo"
					+ "&gateway=1"
					+ "&from="
					+ "&pcid="+pcid
					+ "&savestate=7"
					+ "&useticket=1"
					+ "&door="+randRom
					+ "&pagerefer=http://login.sina.com.cn/sso/logout.php?entry=miniblog&r=http%3A%2F%2Fweibo.com%2Flogout.php%3Fbackurl%3D%252F"
					+ "&vsnf=1"
					+ "&su=" + su
					+ "&service=miniblog"
					+ "&servertime=" + servertime
					+ "&nonce=" + nonce
					+ "&pwencode=rsa2"
					+ "&rsakv=" + rsakv
					+ "&sp="+ getSpJs(pubkey, servertime, nonce, password)
					+ "&sr=1440*900"
					+ "&encoding=UTF-8"
					+ "&prelt=285"
					+ "&url=http://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack"
					+ "&returntype=META";
			c31.addRequestProperty("Host", "login.sina.com.cn");
			c31.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
//			c31.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			c31.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			c31.addRequestProperty("Referer", "http://weibo.com/");
			c31.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			c31.addRequestProperty("Content-Length", String.valueOf(params1.length()));
		    //c3.addRequestProperty("Cookie", cookie);
			c31.setDoOutput(true);
			c31.setDoInput(true);
			c31.setConnectTimeout(1000 * 30);
			c31.setReadTimeout(1000 * 20);
			c31.connect();
			PrintWriter out1 = new PrintWriter(c31.getOutputStream());
			out1.print(params1);
			out1.flush();
			Map<String, List<String>> headers2 = c31.getHeaderFields(); 
		    for(Map.Entry<String,List<String>> entry : headers2.entrySet()){
		        if(entry.getKey() != null && (entry.getKey().indexOf("Set-Cookie") > -1)){
		            for(String value : entry.getValue()){  
		            	cookie = cookie + value.substring(0, value.indexOf(";") + 1);
		            }  
		        }
		    }
			InputStream i31 = c31.getInputStream();
		    Scanner s31 = new Scanner(i31,"gb2312");
		    while(s31.hasNext()){
		    	String scsc = s31.nextLine();
		         System.out.println(scsc);
		    	if(scsc.indexOf("location.replace") > -1 && scsc.indexOf("passport.weibo.com") > -1){
		    		pUrl = scsc.substring(scsc.indexOf("location.replace"));
		    		pUrl = pUrl.substring(pUrl.indexOf("'") + 1);
		    		pUrl = pUrl.substring(0, pUrl.indexOf("'"));
		    	}
		    	if(scsc.indexOf("retcode=2070") > -1 || scsc.indexOf("retcode=4049") > -1){
		    		Thread.sleep(1000 * 10);
					idx++;
					if(idx < 3){
						flag = false;
						cookie  = getCookietime(data, idx);
						return cookie;
					}
		    	}
		    	if(scsc.indexOf("retcode=101") > -1){
					System.out.println("11-用户名密码错误!");
					iswrongpwd = true;
		    	}
		    
		    }
		}
		if(cookie.equals("")){
			if(flag){
				cookie="1";//用户名密码错误
			}else{
				if(iswrongpwd) cookie="1";//用户名密码错误
				else cookie="6";//验证码错误
			}
		}
		System.out.println("需要验证码:"+!flag);
		System.out.println("cookie=="+cookie);
		return cookie;
}
public static String  getSpJs(String publickey,String servertime,
		String nonce,String pw) throws ScriptException, NoSuchMethodException, IOException{
	ScriptEngineManager sem = new ScriptEngineManager();
	ScriptEngine engine = sem.getEngineByName("javascript");	
	Object result  ;
	FileReader reader = new FileReader(jsFileName);   
	engine.eval(reader); 
	if(engine instanceof Invocable) {    
		Invocable invoke = (Invocable)engine;    

		// c = merge(2, 3);    
		result = ((Invocable)engine).invokeFunction("sinaRsa", publickey.trim(),
				servertime.trim(), nonce.trim(), pw.trim());

		}else{
			result = new String("");
		}

		reader.close();  
					
	return (String)result;
	
}
	public static String getUid(String cookie) throws Exception{
		String uid = "";

		uid = cookie.substring(cookie.indexOf("uid%3D")+6, cookie.indexOf("%26name"));
		return uid;
	}
	public static String getResultContent(String scsc,String content,TaskGuideBean taskdo){
		System.out.println("登录="+scsc);
		if(scsc.contains("100000")){
			content = ""; //你的微博发布成功。目前服务器数据同步可能会有延时，所以麻烦耐心等待1-2分钟哦，非常感谢。
			taskdo.setCode(100);
		}else{
			if(scsc.indexOf("100001") > -1 && scsc.indexOf("提交失败") > -1){
//				content = "weibo_100001_该微博评论有限制.";
				taskdo.setCode(5);
				content = "微博评论有限制";
			}
			if(scsc.contains("100001")&& scsc.contains("微博发布")){
				taskdo.setCode(100);
				content = ""; //你的微博发布成功。目前服务器数据同步可能会有延时，所以麻烦耐心等待1-2分钟哦，非常感谢。
			}
			if(scsc.indexOf("100003") > -1){
				taskdo.setCode(3);
				content = "您的帐号存在高危风险，系统暂时锁定了部分功能.";//可登录,发帖受限
			}
			if(scsc.indexOf("unfreeze") > -1){
				taskdo.setCode(2);
				content = "账户已被冻结";
			}
			if(scsc.contains("100001")&& scsc.contains("目标用户不存在")){
				taskdo.setCode(2);
				content = "目标用户不存在";
			}
			if(scsc.indexOf("100001") > -1 && scsc.indexOf("相同内容") > -1){
				taskdo.setCode(4);
				content = "weibo_100002_相同账号，相同内容请间隔10分钟再进行发布.";
			}
			if(scsc.indexOf("100001") > -1 && scsc.indexOf("系统繁忙") > -1){
				taskdo.setCode(2);
				content = "账户已被冻结"; ///需 解除帐号异常
			}
			if(scsc.indexOf("100001") > -1 && scsc.indexOf("评论过了") > -1){
				taskdo.setCode(4);
				content = "weibo_100002_不要太贪心哦，该微博已经评论过了。建议半小时后再评论！";
			}
			if(scsc.indexOf("100001") > -1 && scsc.indexOf("微博不存在") > -1){
				taskdo.setCode(7);
				content = "抱歉，当前微博不存在";
			}
			if(scsc.indexOf("100013") > -1){
				taskdo.setCode(22); //{"code":"100013","msg":"该昵称不允许注册","data":null}
				content = "该昵称不允许注册";
			}
			if(scsc.indexOf("M00003") > -1){
				taskdo.setCode(10); //{"code":"M00003","msg":"请先登录","data":{}}
				content = "请先登录";
			}else{
				
				content = "未知错误";
			}
		}
		return content;
	}
	public static String getDengluResult(String cookie,String content,TaskGuideBean taskdo){
		if(cookie.equals("6")){
    		taskdo.setCode(6);
    		content = "验证码错误";
    	}else if(cookie.equals("1")){
    		taskdo.setCode(1);
    		content = "账号密码错误";
    	}else if(cookie.equals("10")){
    		taskdo.setCode(10);
    		content = "登录失败";
    	}else{
    		taskdo.setCode(8);
    		content = "登录异常报错失败!";
    	}
		return content;
	}
	public static String getPwd(String username,String password){
		try {
			String ssoLoginState = "";
			String alf = "";
			String subp = "";
			String sub = "";
			String sup = "";
			String sue = "";
			String sus = "";
			String utrs2 = "";
			String utrs1 = "";
			String cookie = "";
			String pcid = "";
			String link = "error";
			String content = "";
			boolean iswrongpwd = false;
			boolean pin = true;
			String pUrl="";
			
			String su = URLEncoder.encode(Base64Utils.encode(URLEncoder.encode(username, "utf-8").getBytes()), "utf-8");
			URL u2 = new URL("http://login.sina.com.cn/sso/prelogin.php?entry=weibo&callback=sinaSSOController.preloginCallBack&su=" + su + "&rsakt=mod&checkpin=1&client=ssologin.js(v1.4.18)&_=" + new Date().getTime());
			HttpURLConnection c2 = (HttpURLConnection) u2.openConnection();
			c2.setConnectTimeout(1000 * 45);
			c2.setReadTimeout(1000 * 20);
			c2.connect();
			InputStream i2 = c2.getInputStream();
			Scanner s2 = new Scanner(i2);
			String servertime = "";
			String pubkey = "";
			String nonce = "";
			String rsakv = "";
			while(s2.hasNext()){
				String scsc = s2.nextLine();
				if(scsc.indexOf("servertime") > -1){
					if(scsc.indexOf("\"servertime\"")>-1)
						servertime = scsc.substring(scsc.indexOf("\"servertime\"")).replace("\"servertime\"", "").trim();
					if(servertime.indexOf(",")>-1)
						servertime = servertime.substring(1, servertime.indexOf(","));
				}
				if(scsc.indexOf("pubkey") > -1){
					if(scsc.indexOf("\"pubkey\"")>-1)
						pubkey = scsc.substring(scsc.indexOf("\"pubkey\"")).replace("\"pubkey\"", "").trim();
					if(pubkey.indexOf(",")>-1)
						pubkey = pubkey.substring(2, pubkey.indexOf(",")).replace("\"", "");
				}
				if(scsc.indexOf("nonce") > -1){
					if(scsc.indexOf("\"nonce\"")>-1)
						nonce = scsc.substring(scsc.indexOf("\"nonce\"")).replace("\"nonce\"", "").trim();
					if(nonce.indexOf(",")>-1)
						nonce = nonce.substring(2, nonce.indexOf(",")).replace("\"", "");
				}
				if(scsc.indexOf("rsakv") > -1){
					if(scsc.indexOf("\"rsakv\"")>-1)
						rsakv = scsc.substring(scsc.indexOf("\"rsakv\"")).replace("\"rsakv\"", "").trim();
					if(rsakv.indexOf(",")>-1)
						rsakv = rsakv.substring(2, rsakv.indexOf(",")).replace("\"", "");
				}
				if(scsc.indexOf("pcid") > -1){
					if(scsc.indexOf("\"pcid\"")>-1)
						pcid = scsc.substring(scsc.indexOf("\"pcid\"")).replace("\"pcid\"", "").trim();
					if(pcid.indexOf(",")>-1)
						pcid = pcid.substring(2, pcid.indexOf(",")).replace("\"", "");
				}
				if(scsc.indexOf("showpin") > -1){
					if(scsc.indexOf("\"showpin\"")>-1){
						String showpin = scsc.substring(scsc.indexOf("\"showpin\"")).replace("\"showpin\"", "").trim();
						if(showpin.indexOf(",")>-1)
							showpin = showpin.substring(1, showpin.indexOf(",")).replace("\"", "");
						//System.out.println(showpin);
						if(showpin.equals("1")){
							pin = false;
						}
					}
				}
			}
			return getSpJs(pubkey, servertime, nonce, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
