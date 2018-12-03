package com.longriver.netpro.webview.controller;

import java.io.InputStream;
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
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;

import com.alibaba.fastjson.JSON;
import com.longriver.netpro.common.sina.SData;
import com.longriver.netpro.common.sina.SinaIdMidConverter;
import com.longriver.netpro.common.sina.WeiboSina;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;
/**
 * 微博评论赞
 */
public class WeiboCommentPraise {
	
	private static Logger logger = Logger.getLogger(WeiboCommentPraise.class);
	
	public static void main(String arsg[]){
		try{
			TaskGuideBean bb = new TaskGuideBean();
			bb.setNick("15558480767");
			bb.setPassword("a123456a");
			bb.setAddress("http://weibo.com/5029134285/E6wYd84aY?type=comment#_rnd1473234102022");
			bb.setPraiseWho("object_id=4017096650617534");
			bb.setNick("17080625988");
			bb.setPassword("chwx9828875061");

			bb.setPraiseContent("");
			
			bb.setAddress("https://weibo.com/2831076357/EaoS51hpA?from=page_1005052831076357_profile&wvr=6&mod=weibotime&type=comment#_rnd1531107155892");
			bb.setNick("15002237262");
			bb.setPassword("fenglong19940117");
			bb.setPraiseWho("object_id=4235192317320467&object_type=comment&o_uid=2831076357&commentmid=4024788890305262");
			sina(bb);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	public static void sina(TaskGuideBean taskdo){
		SData data =new SData();
		
		try {
			String commentId = taskdo.getPraiseWho();
			String addr = taskdo.getAddress();
			String userId =taskdo.getNick();
			String pwd = taskdo.getPassword();
			
			data.put("user_account_id", userId);
			data.put("user_account_pw", pwd);
			data.put("mission_addr", addr);
			
			String midEncode = addr.substring(addr.lastIndexOf("/")).replace("/", "");
			if(midEncode.indexOf("?") > -1){
				midEncode = midEncode.substring(0, midEncode.indexOf("?"));
			}
			if(midEncode.indexOf("#") > -1){
				midEncode = midEncode.substring(0, midEncode.indexOf("#"));
			}
			System.out.println("22222222222222222222");
			
			

			String cookie = "";
			String uid = "";
			String content = "";
			String link = "";
	      
	 	    
	        cookie = WeiboSina.getCookie(data,0);
	        if(!cookie.equals("")){
	        	logger.info("3==-------------------------------------");
				logger.info("uid=="+uid);
				URL u45 = new URL(addr);
				HttpURLConnection c45 = (HttpURLConnection) u45.openConnection();
				c45.addRequestProperty("Host", "weibo.com");
//				c45.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
				c45.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
				c45.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
				c45.addRequestProperty("Cookie", cookie);
				c45.addRequestProperty("Connection", "keep-alive");
				c45.setConnectTimeout(1000 * 30);
		        c45.setReadTimeout(1000 * 20);
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
						if(location.indexOf("'")>-1)
							location = location.substring(location.indexOf("'") + 1);
						if(location.indexOf("'")>-1)
							location = location.substring(0, location.indexOf("'"));
					}
				}
				//System.out.println(pdetail);
				logger.info("11111111111111111111111111111111==");
				String m = "";
				String tm = data.getString("mission_addr");
				if(tm.indexOf("?") > -1){
					tm = tm.substring(0, tm.indexOf("?"));
				}
				if(tm.indexOf("/") > -1){
					tm = tm.substring(tm.lastIndexOf("/")).replace("/", "").replace("?", "").trim();
				}
				m = SinaIdMidConverter.midToId(midEncode);
				
				URL u5 = new URL("https://weibo.com/aj/v6/like/objectlike?ajwvr=6");
				//http://weibo.com/aj/v6/mblog/forward?ajwvr=6&domain=5234172433&__rnd=1417000316699
				HttpURLConnection c5 = (HttpURLConnection) u5.openConnection();
				String rid = "";
				if(data.getString("mission_addr").indexOf("rid=") > -1){
				    rid = data.getString("mission_addr");
				    rid = rid.substring(rid.indexOf("rid=") + 4);
				}
				if(rid.indexOf("&") > -1){
					rid = rid.substring(0, rid.indexOf("&"));
				}
				System.out.println("555555555555555555555555555");
				String paramss = commentId+"&object_type=comment&location=page_100505_single_weibo";
				//System.out.println(paramss);
				c5.addRequestProperty("Host", "weibo.com");
//				c5.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
				c5.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
				c5.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
				c5.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
				c5.addRequestProperty("X-Requested-With", "XMLHttpRequest");
				c5.addRequestProperty("Referer", data.getString("mission_addr"));
				c5.addRequestProperty("Content-Length", String.valueOf(paramss.length()));
				c5.addRequestProperty("Cookie", cookie);
				c5.addRequestProperty("Connection", "keep-alive");
				c5.addRequestProperty("Pragma", "no-cache");
				c5.addRequestProperty("Cache-Control", "no-cache");
				c5.setDoInput(true);
				c5.setDoOutput(true);
				c5.setConnectTimeout(1000 * 30);
		        c5.setReadTimeout(1000 * 20);
				PrintWriter o5 = new PrintWriter(c5.getOutputStream());
				o5.print(paramss);
				o5.flush();
				InputStream i5 = null;
				try{
					i5 = c5.getInputStream();
				}catch(Exception e){
					System.out.println("777777777777");
					e.printStackTrace();
				}
				Scanner s5 = new Scanner(i5);
				String mid = "";
				boolean b = false;
				while(s5.hasNext()){
					String scsc = s5.nextLine();
					System.out.println("scsc=="+scsc);
					content = WeiboSina.getResultContent(scsc,content,taskdo);
				}
	        }else{
	        	content = WeiboSina.getDengluResult(cookie,content,taskdo);
	        }
	        if(StringUtils.isBlank(content)){//成功时保存cookie信息
	        	taskdo.setCookieData(cookie);
	        }
	        MQSender.toMQ(taskdo,content);
		}catch (Exception e) {
			taskdo.setCode(8);
			MQSender.toMQ(taskdo,"发帖异常报错失败!");
			e.printStackTrace();
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
	public String formatString(String str) {
        if (str != null) {            
        	try {                
        		str = new String(str.getBytes("ISO-8859-1"), "UTF-8");            
        		} catch (Exception e) {}        
        		}
        return str;    
    }

}
