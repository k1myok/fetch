package com.longriver.netpro.webview.controller;

import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Date;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import org.apache.commons.lang3.StringUtils;

import com.longriver.netpro.common.sina.SData;
import com.longriver.netpro.common.sina.WeiboSina;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;

import com.longriver.netpro.webview.controller.WeiboPicComment;

import com.longriver.netpro.webview.entity.TaskGuideBean;


public class WeiboPublish{
	
	public static void main(String ss[]){
		TaskGuideBean bb = new TaskGuideBean();
		
		bb.setNick("15851418283");
		bb.setPassword("lx1314");//可登录,发帖受限
		bb.setNick("18833518933");
		bb.setPassword("lx1314");//系统繁忙,需解除账号异常
		bb.setNick("13230053563");
		bb.setPassword("lx1314");//1. 报错失败,可能是用户名密码错误
		bb.setNick("13226172899");
		bb.setPassword("lx1314");//虽然界面上显示  解除账号异常,但是可以发出去
		bb.setNick("18229656966");
		bb.setPassword("srigju101");//

//		bb.setNick("lilei1929@163.com");
//		bb.setPassword("lilei419688..");
		
		bb.setCorpus("冷,不过很好");
//		bb.setNick("18833518933");
//		bb.setPassword("lx1314");//系统繁忙,需解除账号异常
//		bb.setNick("13230053563");
//		bb.setPassword("lx1314");//1. 报错失败,可能是用户名密码错误
//		bb.setNick("13226172899");
//		bb.setPassword("lx1314");//虽然界面上显示  解除账号异常,但是可以发出去
//		bb.setNick("18229656966");
//		bb.setPassword("srigju101");//

		bb.setNick("lilei1929@163.com");
		bb.setPassword("lilei419688..");
		
		bb.setCorpus("风小了,雾霾又来了");
		
		sina(bb);
	}
	
	public static void sina(TaskGuideBean taskdo){
		
		if(StringUtils.isNotBlank(taskdo.getPicData())){
			
			WeiboPicComment.toComment(taskdo);
			return;
		}
		SData data =new SData();
		String content = "未定义失败";
		String cookie = "";
		try {
			String userId =taskdo.getNick();
			String pwd = taskdo.getPassword();
			String contents = taskdo.getCorpus();

			data.put("user_account_id", userId);
			data.put("user_account_pw", pwd);
			data.put("mission_contents", contents);
			String uid = "";
			
			taskdo.setCode(9);
			String link = "";
	 	    
			cookie = WeiboSina.getCookie(data,0);
			
	        if(cookie.length()>10){
	        	if(uid==null||uid.equals("")){
	        		int idx = cookie.indexOf("uid%3D");
	        		if(idx>-1 && cookie.length()>(idx+6))
	 					uid = cookie.substring(cookie.indexOf("uid%3D")+6);
		     		if(uid.indexOf("%26")>-1)
		     			uid = uid.substring(0,uid.indexOf("%26"));
		     	}
	        	long rnd = new Date().getTime();
				URL u5 = new URL("https://weibo.com/p/aj/v6/mblog/add?domain=100505&ajwvr=6&__rnd=" + rnd);
				//http://weibo.com/p/aj/v6/mblog/add?domain=100505&ajwvr=6&__rnd=1416423644659
				HttpURLConnection c5 = (HttpURLConnection) u5.openConnection();
				String paramss = "text=" + URLEncoder.encode(contents, "utf-8")
						+ "&pic_id="
						+ "&rank=0"
						+ "&rankid="
						+ "&_surl="
						+ "&hottopicid="
						+ "&location=home"
						+ "&module=stissue"
						+ "&_t=0";
				c5.addRequestProperty("Host", "weibo.com");
//				c5.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
				c5.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
				c5.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
				c5.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
				c5.addRequestProperty("X-Requested-With", "XMLHttpRequest");
				c5.addRequestProperty("Referer", "http://weibo.com/u/" + uid + "/home?leftnav=1&wvr=5");
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
					taskdo.setCode(8);
					MQSender.toMQ(taskdo,"报错失败!");
					return;
				}
				Scanner s5 = new Scanner(i5, "utf-8");
				while(s5.hasNext()){
					String scsc = StringUtil.decodeUnicode(s5.nextLine());
					System.out.println("scsc================"+scsc);
					content = WeiboSina.getResultContent(scsc,content,taskdo);
				}
				link = "http://weibo.com/" + uid + "/";// + SinaIdMidConverter.idToMid(mid);
				taskdo.setUrl(link);
	        }else{
	        	content = "账号密码错误";
				System.out.println("账号密码错误");
	        }
			MQSender.toMQ(taskdo,content);
		}catch (Exception e) {
			MQSender.toMQ(taskdo,"报错失败!");
	        content = WeiboSina.getDengluResult(cookie,content,taskdo);
	        if(StringUtils.isBlank(content)){//成功时保存cookie信息
	        	taskdo.setCookieData(cookie);
	        }
			MQSender.toMQ(taskdo,content);
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
	
	
	
//	@SuppressWarnings("null")
//	public void sina(SData data, int idx) throws Exception{
//		String username = data.getString("user_account_id");
//		String password = data.getString("user_account_pw");;
//		String contents = data.getString("mission_contents");
//		
//		String _t_wm = "";
//		String user_last_login_name = "";
//		String sub = "";
//		String login = "true";
//		String _ttt_user_config_h5 = "%7B%22ShowMblogPic%22%3A1%2C%22ShowUserInfo%22%3A1%2C%22MBlogPageSize%22%3A10%2C%22ShowPortrait%22%3A1%2C%22CssType%22%3A0%2C%22Lang%22%3A1%7D";
//		String uid = "";
//		
//		URL u1 = new URL("https://m.weibo.cn/login?ns=1&backURL=http%3A%2F%2Fm.weibo.cn%2F&backTitle=%CE%A2%B2%A9&vt=4&");
//		HttpURLConnection c1 = (HttpURLConnection) u1.openConnection();
//		String param = "check=1"
//				+ "&backURL=http%3A%2F%2Fm.weibo.cn%2F"
//				+ "&uname=" + URLEncoder.encode(username, "gb2312")
//				+ "&pwd=" + URLEncoder.encode(password, "gb2312");
//		c1.addRequestProperty("Host", "m.weibo.cn");
//		c1.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0");
//		c1.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//		c1.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
//		c1.addRequestProperty("Referer", "https://m.weibo.cn/login?ns=1&backURL=http%3A%2F%2Fm.weibo.cn%2F&backTitle=%CE%A2%B2%A9&vt=4&");
//		c1.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//		c1.addRequestProperty("Content-Length", String.valueOf(param.length()));
//		c1.setDoInput(true);
//		c1.setDoOutput(true);
//		c1.connect();
//		c1.setInstanceFollowRedirects(false);
//		PrintWriter o1 = new PrintWriter(c1.getOutputStream());
//		o1.print(param);
//		o1.flush();
//		Map<String, List<String>> h1 = c1.getHeaderFields();
//		for(Map.Entry<String,List<String>> entry : h1.entrySet()){
//			if(entry.getKey() != null && (entry.getKey().indexOf("Set-Cookie") > -1)){
//				for(String value : entry.getValue()){
//					if(value.indexOf("SUB=") > -1){
//						sub = value.substring(0, value.indexOf(";") + 1);
//					}
//					if(value.indexOf("_T_WM=") > -1){
//						_t_wm = value.substring(0, value.indexOf(";") + 1);
//					}
//					if(value.indexOf("USER_LAST_LOGIN_NAME=") > -1){
//						user_last_login_name = value.substring(0, value.indexOf(";") + 1);
//					}
//				}
//			}
//		}
//		InputStream i1 = c1.getInputStream();
//		Scanner s1 = new Scanner(i1, "utf-8");
//		while(s1.hasNext()){
//			String scsc = s1.nextLine();
//			System.out.println("111111111:" + scsc);
//			if(scsc.indexOf("name=\"code\"") > -1){
//				idx++;
//				if(idx < 5){
//					Thread.sleep(1000 * 60 * 2);
//					sina(data, idx);
//					String x = null;
//					x.equals("");
//				}
//				if(idx == 5){
//					LoginValidation v = new LoginValidation();
//					v.execute2("tb_weibo_return", data.getString("mission_id"));
//				}
//				LoginValidation v = new LoginValidation();
//				v.execute("tb_weibo_return", data.getString("mission_id"));
//			}
//		}
//		
//		URL u = new URL("http://m.weibo.cn");
//		HttpURLConnection c = (HttpURLConnection) u.openConnection();
//		c.addRequestProperty("Host", "m.weibo.cn");
//		c.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0");
//		c.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//		c.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
//		c.addRequestProperty("Cookie", _t_wm + user_last_login_name + sub + login + _ttt_user_config_h5);
//		c.addRequestProperty("Connection", "keep-alive");
//		InputStream i = c.getInputStream();
//		Scanner s = new Scanner(i);
//		while(s.hasNext()){
//			String scsc = s.nextLine();
//			if(scsc.indexOf("avatar J-avatar") > -1){
//				uid = scsc.substring(scsc.indexOf("avatar J-avatar")).replace("avatar J-avatar\"", "");
//				uid = uid.substring(uid.indexOf("\"") + 1);
//				uid = uid.substring(0, uid.indexOf("\""));
//				uid = uid.substring(uid.lastIndexOf("/") + 1);
//			}
//		}
//		System.out.println("-----------------------------------1111111111111111111111111111111111111111--------------------------------");
//		
//		URL u2 = new URL("http://m.weibo.cn/mblogDeal/addAMblog?uicode=20000060&rl=1");
//		HttpURLConnection c2 = (HttpURLConnection) u2.openConnection();
//		String params = "content=" + URLEncoder.encode(contents, "utf-8")
//				+ "&pic=";
//		c2.addRequestProperty("Host", "m.weibo.cn");
//		c2.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0");
//		c2.addRequestProperty("Accept", "application/json");
//		c2.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
//		c2.addRequestProperty("X-Requested-With", "XMLHttpRequest");
//		c2.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//		c2.addRequestProperty("Referer", "http://m.weibo.cn/?vt=4");
//		c2.addRequestProperty("Content-Length", String.valueOf(params.length()));
//		c2.addRequestProperty("Cookie", _t_wm + user_last_login_name + sub + login + _ttt_user_config_h5);
//		c2.addRequestProperty("Connection", "keep-alive");
//		c2.addRequestProperty("Pragma", "no-cache");
//		c2.addRequestProperty("Cache-Control", "no-cache");
//		c2.setDoInput(true);
//		c2.setDoOutput(true);
//		c2.connect();
//		PrintWriter o2 = new PrintWriter(c2.getOutputStream());
//		o2.print(params);
//		o2.flush();
//		InputStream i2 = c2.getInputStream();
//		Scanner s2 = new Scanner(i2);
//		String rid = "";
//		boolean b = false;
//		String link = "error";
//		while(s2.hasNext()){
//			String scsc = s2.nextLine();
//			if(scsc.indexOf("id") > -1){
//				rid = scsc.substring(scsc.indexOf("\"id\":")).replace("\"id\":", "");
//				rid = rid.substring(0, rid.indexOf(","));
//				rid = rid.replace("\"", "");
//				rid = rid.replace("\"", "");
//				b = true;
//			}
//			if(scsc.indexOf("\"ok\":\"-8\"") > -1){
//				link = "账户异常";
//			}
//		}
//		
//		URL uf = new URL("http://api.t.sina.com.cn/querymid.json?id=" + rid);
//		HttpURLConnection cf = (HttpURLConnection) uf.openConnection();
//		cf.connect();
//		InputStream iff = cf.getInputStream();
//		Scanner sf = new Scanner(iff);
//		String mid = "";
//		
//		while(sf.hasNext()){
//			String scsc = sf.nextLine();
//			if(scsc.indexOf("mid") > -1){
//				mid = scsc.substring(scsc.indexOf("\"mid\":")).replace("\"mid\":", "");
//				mid = mid.substring(0, mid.indexOf("}"));
//				mid = mid.replace("\"", "");
//				mid = mid.replace("\"", "");
//			}
//		}
//		
//		
//		String missionId = data.getString("mission_id");
//		String statusCd = "0";
//		if(b){
//			statusCd = "1";
//			link = "http://weibo.com/" + uid + "/" + mid;
//		}
//		
//		service.execute("tb_weibo_return", missionId, statusCd, link);
//		
//	}
	public String formatString(String str) {
        if (str != null) {            
        	try {                
        		str = new String(str.getBytes("ISO-8859-1"), "UTF-8");            
        		} catch (Exception e) {}        
        		}
        return str;    
    }
	
	
}
