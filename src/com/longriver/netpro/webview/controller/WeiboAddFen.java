package com.longriver.netpro.webview.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.common.sina.SData;
import com.longriver.netpro.common.sina.WeiboSina;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

public class WeiboAddFen {
	private static Logger logger = Logger.getLogger(WeiboAddFen.class);
	
	public static void main(String agrs[]){
		try{
			TaskGuideBean task = new TaskGuideBean();
			task.setNick("lilei1929@163.com");
			task.setPassword("lilei419688..");
			task.setAddress("https://weibo.com/u/6540778299");
			sina(task);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void sina(TaskGuideBean taskdo) throws Exception{
		
		String takeComment = "1";
		
		SData data =new SData();
//		String userId = "lilei1929@163.com";
//		String pwd = "LILEI419516688";
//		String addr = "http://weibo.com/sztv?topnav=1&wvr=6&topsug=1";
//		String contents = "";
		
		String pwd = taskdo.getPassword();
		String userId = taskdo.getNick();
		String addr = URLDecoder.decode(taskdo.getAddress(), "UTF-8");
		logger.info("pwd=="+pwd);
		logger.info("userId=="+userId);
		logger.info("addr=="+addr);
		String contents = "";
		String oid = "";
		
		data.put("user_account_id", userId);
		data.put("user_account_pw", pwd);
		data.put("mission_addr", addr);
		data.put("mission_contents", contents);
		data.put("mission_take_comment_flag", takeComment);
		
    	String midEncode = addr.substring(addr.lastIndexOf("/")).replace("/", "");
    	if(midEncode.indexOf("?") > -1){
    		midEncode = midEncode.substring(0, midEncode.indexOf("?"));
    	}
    	if(midEncode.indexOf("#") > -1){
    		midEncode = midEncode.substring(0, midEncode.indexOf("#"));
    	}
		
		String cookie = "";
		String uid = "";
		String content = "";
		String link = "";
      
 	    
        cookie = WeiboSina.getCookie(data,0);
        if(!cookie.equals("")){
        	URL u45 = new URL(addr);
            HttpURLConnection c45 = (HttpURLConnection) u45.openConnection();
            c45.addRequestProperty("Host", "weibo.com");
            c45.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
            c45.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            c45.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
            c45.addRequestProperty("Cookie", cookie);
            c45.addRequestProperty("Connection", "keep-alive");
            c45.connect();
            InputStream i45 = c45.getInputStream();
            Scanner s45 = new Scanner(i45, "utf-8");
            String pdetail = "";
            String location = "";
            while(s45.hasNext()){
            	String scsc = s45.nextLine();
            	if(scsc.indexOf("$CONFIG['page_id']") > -1){
            		pdetail = scsc.substring(scsc.indexOf("$CONFIG['page_id']")).replace("$CONFIG['page_id']", "");
            		pdetail = pdetail.substring(pdetail.indexOf("'") + 1);
            		pdetail = pdetail.substring(0, pdetail.indexOf("'"));
            	}
            	if(scsc.indexOf("$CONFIG['location']") > -1){
            		location = scsc.substring(scsc.indexOf("$CONFIG['location']")).replace("$CONFIG['location']", "");
            		location = location.substring(pdetail.indexOf("'") + 1);
            		location = location.substring(0, location.indexOf("'"));
            	}
            }
            URL u451 = new URL(addr);
            HttpURLConnection c451 = (HttpURLConnection) u451.openConnection();
            c451.addRequestProperty("Host", "weibo.com");
            c451.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
            c451.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            c451.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
            c451.addRequestProperty("Cookie", cookie);
            c451.addRequestProperty("Connection", "keep-alive");
           c451.connect();
            InputStream i451 = c451.getInputStream();
            Scanner s451 = new Scanner(i451);
            String m = "";
            while(s451.hasNext()){
            	String scsc = s451.nextLine();
            	if(scsc.indexOf("$CONFIG['oid']") > -1){
            		oid = scsc.substring(scsc.indexOf("$CONFIG['oid']")).replace("$CONFIG['oid']", "");
            		oid = oid.substring(oid.indexOf("'") + 1);
            		oid = oid.substring(0, oid.indexOf("'"));
            		System.out.println(oid);
            	}
            }
            System.out.println("------------------------------------ Sina weibo jiafen-----------------------------------------");
            URL u5 = new URL("https://weibo.com/aj/f/followed?ajwvr=6&__rnd=" + new Date().getTime()+"&");
            HttpURLConnection c5 = (HttpURLConnection) u5.openConnection();
            String requestUrl = "https://weibo.com/aj/f/followed?ajwvr=6&__rnd=" + new Date().getTime();  
            Map<String, Object> requestParamsMap = new HashMap<String, Object>();
            uid=oid;
            requestParamsMap.put("uid", uid);  
            requestParamsMap.put("objectid", ""); 
            requestParamsMap.put("f", "1");  
            requestParamsMap.put("extra", "");  
            requestParamsMap.put("refer_sort", "");  
            requestParamsMap.put("refer_flag", "");  
            requestParamsMap.put("location", "page_100505_home");  
            requestParamsMap.put("oid", uid);  
            requestParamsMap.put("wforce", "1");  
            requestParamsMap.put("nogroup", "false");  
            requestParamsMap.put("fnick", "%E4%B8%80%E5%8A%A0%E8%A1%A3%E6%BC%94%E5%87%BA%E8%A1%A8%E6%BC%94%E6%9C%8D%E9%A5%B0");  
            requestParamsMap.put("_t", "0"); 
            PrintWriter printWriter = null;  
            BufferedReader bufferedReader = null;  
            StringBuffer responseResult = new StringBuffer();  
            StringBuffer params = new StringBuffer();  
            HttpURLConnection httpURLConnection = null;  
            // 组织请求参数  
            Iterator it = requestParamsMap.entrySet().iterator();  
            while (it.hasNext()) {  
                Map.Entry element = (Map.Entry) it.next();  
                params.append(element.getKey());  
                params.append("=");  
                params.append(element.getValue());  
                params.append("&");  
            }  
            if (params.length() > 0) {  
                params.deleteCharAt(params.length() - 1);  
            }  
      
            try {  
                URL realUrl = new URL(requestUrl);  
                // 打开和URL之间的连接  
                httpURLConnection = (HttpURLConnection) realUrl.openConnection();  
                // 设置通用的请求属性  
                httpURLConnection.setRequestProperty("accept", "*/*");  
                httpURLConnection.setRequestProperty("connection", "Keep-Alive");  
                httpURLConnection.setRequestProperty("Content-Length", String  
                        .valueOf(params.length()));  
                httpURLConnection.addRequestProperty("Host", "weibo.com");
                httpURLConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
                httpURLConnection.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
                httpURLConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                httpURLConnection.addRequestProperty("X-Requested-With", "XMLHttpRequest");
                httpURLConnection.addRequestProperty("Referer", "http://weibo.com/p/1005052656650751/follow?relate=fans&from=100505&wvr=6&mod=headfans&noscale_head=1");
                httpURLConnection.addRequestProperty("Cookie", cookie);
                httpURLConnection.addRequestProperty("Connection", "keep-alive");
                httpURLConnection.addRequestProperty("Pragma", "no-cache");
                httpURLConnection.addRequestProperty("Cache-Control", "no-cache");
                httpURLConnection.setInstanceFollowRedirects(false);
                // 发送POST请求必须设置如下两行  
                httpURLConnection.setDoOutput(true);  
                httpURLConnection.setDoInput(true); 
                // 获取URLConnection对象对应的输出流  
                printWriter = new PrintWriter(httpURLConnection.getOutputStream());  
                // 发送请求参数  
                printWriter.write(params.toString());  
                // flush输出流的缓冲  
                printWriter.flush();  
                // 根据ResponseCode判断连接是否成功  
                int responseCode = httpURLConnection.getResponseCode();  
                if (responseCode != 200) {  
                   // log.error(" Error===" + responseCode);  
                } else {  
                   // log.info("Post Success!");  
                }  
                // 定义BufferedReader输入流来读取URL的ResponseData  
                bufferedReader = new BufferedReader(new InputStreamReader(  
                        httpURLConnection.getInputStream()));  
                String line;  
                while ((line = bufferedReader.readLine()) != null) {  
                	String newStr = new String(line.getBytes(), "gb2312"); 
                    responseResult.append(newStr);  
                }  
                logger.info(responseResult);
                String result = responseResult.toString();
                JSONObject parseObject = JSON.parseObject(result);
                String code = parseObject.getString("code");
                
                if(code!=null && "100000".equals(code)){
        			
        			logger.info("weibo second comment success 100000");
        			//成功时保存cookie信息
    		        taskdo.setCookieData(cookie);
        			isSuccess(taskdo, "");
        		}else if(code!=null && "100001".equals(code)){
        			
        			logger.info("weibo second comment fail 100001");
        			isSuccess(taskdo, "请至少选择1位用户才可以进行操作");
        		}else if(code!=null && "100003".equals(code)){
        			
        			logger.info("weibo second comment fail 100003");
        			isSuccess(taskdo, "未实名认证");
        		}else if(code!=null && "100006".equals(code)){
        			
        			logger.info("weibo second comment fail 100003");
        			isSuccess(taskdo, "账号被冻结");
        		}else {
        			
        			logger.info("weibo second comment fail");
        			isSuccess(taskdo, "加粉失败");
        		}
            } catch (Exception e) {  
                //log.error("send post request error!" + e);  
            } finally {  
                httpURLConnection.disconnect();  
                try {  
                    if (printWriter != null) {  
                        printWriter.close();  
                    }  
                    if (bufferedReader != null) {  
                        bufferedReader.close();  
                    }  
                } catch (IOException ex) {  
                    ex.printStackTrace();  
                }  
      
            }  
        }else{
        	content = "账号密码错误";
        	isSuccess(taskdo, "账号密码错误");
			System.out.println("账号密码错误");
        }
	}
	
	public static String getSP(String pubkey, String servertime, String nonce, String password){
		String rsaPublickey= toHex(pubkey);
		String message = String.valueOf(servertime) + '\t' + String.valueOf(nonce) + '\n' + password;
		byte[] b = null;
		try {
			b = encrypt(getPublicKey(rsaPublickey, "65537"), message.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sp = charAt16(b);
		return sp;
	}
	
	public static byte[] encrypt(RSAPublicKey publicKey,byte[] srcBytes) throws Exception{  
		if(publicKey!=null){  
			Cipher cipher;
			try {
				cipher = Cipher.getInstance("RSA");
				cipher.init(Cipher.ENCRYPT_MODE, publicKey);
	            return cipher.doFinal(srcBytes);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
			} catch (BadPaddingException e) {
				e.printStackTrace();
			}  
			
		}  
		return null;  
	}  
	
	public static RSAPublicKey getPublicKey(String modulus, String exponent) {  
        try {  
            
            BigInteger b2 = new BigInteger(exponent);  
            BigInteger b1 = new BigInteger(modulus);  
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    } 
	
	public static String toHex(String s){
		String[] ss = s.toUpperCase().split("");
		int c = s.length();
		BigDecimal l = new BigDecimal(16);
		BigDecimal l2 = new BigDecimal(0);
		for(int i = 0; i < ss.length; i++){
			if(!ss[i].equals("")){
				for(int j = 0; j < c - i - 1; j++){
					l = l.multiply(new BigDecimal(16));
				}
				if(i == ss.length - 1){
					l = new BigDecimal(1);
				}
				if(ss[i].equals("A")){
					l2 = l2.add(l.multiply(new BigDecimal(10)));
				}else if(ss[i].equals("B")){
					l2 = l2.add(l.multiply(new BigDecimal(11)));
				}else if(ss[i].equals("C")){
					l2 = l2.add(l.multiply(new BigDecimal(12)));
				}else if(ss[i].equals("D")){
					l2 = l2.add(l.multiply(new BigDecimal(13)));
				}else if(ss[i].equals("E")){
					l2 = l2.add(l.multiply(new BigDecimal(14)));
				}else if(ss[i].equals("F")){
					l2 = l2.add(l.multiply(new BigDecimal(15)));
				}else{
					l2 = l2.add(new BigDecimal(Integer.valueOf(ss[i])).multiply(l));
				}
				
				l = new BigDecimal(16);
			}
		}
		return String.valueOf(l2);
	}
	
	public static String charAt16(byte[] b){
		String ss = "";
		for(int i = 0; i < b.length; i++){
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) { 
				hex = '0' + hex; 
			}
			ss = ss + hex.toLowerCase();
		}
		return ss;
	}
		
		/**
		 * 判断是否成功
		 */
		public static void isSuccess(TaskGuideBean task,String msg){
			MQSender.toMQ(task,msg);
		}
}
