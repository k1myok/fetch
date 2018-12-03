package com.longriver.netpro.webview.controller;

import java.io.InputStream;
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

import com.longriver.netpro.common.sina.SData;
import com.longriver.netpro.common.sina.WeiboSina;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 微博举报
 * @author rhy
 * @2017-6-12 下午5:37:44
 * @version v1.0
 */
public class WeiboJuBao {

	
	public static void main(String arsg[]){
		try{
			TaskGuideBean bb = new TaskGuideBean();
//			bb.setNick("15558480767");
//			bb.setPassword("a123456a");
			bb.setNick("15223879746");
			bb.setPassword("lx1314");
			bb.setAddress("http://weibo.com/2662279417/F728bBYZO?from=page_1005052662279417_profile&wvr=6&mod=weibotime&type=comment");
//			bb.setAddress("http://service.account.weibo.com/reportspam?rid=4118601013278224&type=1&from=10101&url=&bottomnav=1&wvr=6");
			weiboReport(bb);
//			String addr = "http://service.account.weibo.com/reportspam?rid=4118601013278224&type=1&from=10101&url=&bottomnav=1&wvr=6";
//			String type=addr.substring(addr.indexOf("type"),addr.indexOf("&from"));
//			String from =addr.substring(addr.indexOf("from"),addr.indexOf("&url"));
//			System.out.println(type);
//			System.out.println(from);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings({ "unused", "unchecked" })
	public static void weiboReport(TaskGuideBean taskdo){
		SData data =new SData();
		String address = taskdo.getAddress();
		String userId = taskdo.getNick();
		String pwd = taskdo.getPassword();
		String category = taskdo.getPraiseWho(); //举报类型
		try {
			String addr = URLDecoder.decode(address,"utf-8");
			addr = new String(addr.getBytes("iso8859-1"),"utf-8");
			
			data.put("user_account_id", userId);
			data.put("user_account_pw", pwd);
			data.put("mission_addr", addr);

//			String uid = "";
//			String content = "";
//			String link = "";
	 	    
			String cookie = WeiboSina.getCookie(data,0);
	        if(!cookie.equals("")){
				URL u45 = new URL(addr);
				HttpURLConnection c45 = (HttpURLConnection) u45.openConnection();
				c45.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//				c45.addRequestProperty("Accept-Encoding", "gzip, deflate");
				c45.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
				c45.addRequestProperty("Cache-Control", "max-age=0");
				c45.addRequestProperty("Connection", "keep-alive");
				c45.addRequestProperty("Cookie", cookie);
				c45.addRequestProperty("Host", "service.account.weibo.com");
				c45.addRequestProperty("Referer", "http://d.weibo.com/102803_ctg1_4388_-_ctg1_4388");
				c45.addRequestProperty("Upgrade-Insecure-Requests", "1");
				c45.addRequestProperty("User-Agent", "User-Agent");
				c45.connect();
				
				//http://service.account.weibo.com/reportspam?rid=4118601013278224&type=1&from=10101&url=&bottomnav=1&wvr=6
				String type=addr.substring(addr.indexOf("type"),addr.indexOf("&from"));
				String from =addr.substring(addr.indexOf("from"),addr.indexOf("&url"));
				URL url = new URL("https://service.account.weibo.com/success?"+type+"&"+from+"&cat="+category);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				
				connection.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//				connection.addRequestProperty("Accept-Encoding", "");
				connection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
				connection.addRequestProperty("Connection", "keep-alive");
				connection.addRequestProperty("Cookie", cookie);
				connection.addRequestProperty("Host", "service.account.weibo.com");
				connection.addRequestProperty("Upgrade-Insecure-Requests", "1");
				connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0");
				connection.connect();
				
				InputStream is = connection.getInputStream();
				
				Scanner sc = new Scanner(is,"utf-8");
				
				String result = "";
				while(sc.hasNext()){
					result += sc.nextLine();
					
					System.out.println(result);
				}
			
				if(result.contains("您的举报已提交，我们将尽快处理")){
					System.out.println("举报成功");
					MQSender.toMQ(taskdo,"");
				}else{
					System.out.println("请核实账号");
					MQSender.toMQ(taskdo,"");
				}
				}else{
					MQSender.toMQ(taskdo,"");
				}
		    } catch (Exception e) {
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
