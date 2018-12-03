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

import com.longriver.netpro.common.sina.SData;
import com.longriver.netpro.common.sina.WeiboSina;

public class WeiboAddHuaFen {
	
	
	public void sina(String userId, int idx) throws Exception{
		
		String takeComment = "1";
		
		SData data =new SData();
		
		String pwd = "ilovey111";
		String addr = "http://weibo.com/234150789";
		String contents = "";

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
            String m = "";
            System.out.println("------------------------------------WeiboTransmit Sina-----------------------------------------");
            URL u5 = new URL("http://weibo.com//aj/f/followed?ajwvr=6&__rnd=" + new Date().getTime()+"&");
            HttpURLConnection c5 = (HttpURLConnection) u5.openConnection();
            String requestUrl = "http://weibo.com/p/aj/relation/follow?ajwvr=6&__rnd="+ new Date().getTime();
            Map<String, Object> requestParamsMap = new HashMap<String, Object>();
            uid="1022%3A1008080bace14e828f6f63b44b62948e27c4b5";
            requestParamsMap.put("uid", uid);  
            requestParamsMap.put("objectid", "1022%3A1008080bace14e828f6f63b44b62948e27c4b5"); 
            requestParamsMap.put("f", "1");  
            requestParamsMap.put("extra", "");  
            requestParamsMap.put("refer_sort", "");  
            requestParamsMap.put("refer_flag", "");  
            requestParamsMap.put("location", "page_100808_home");  
            requestParamsMap.put("oid", "0bace14e828f6f63b44b62948e27c4b5");  
            requestParamsMap.put("wforce", "1");  
            requestParamsMap.put("nogroup", "1");  
            requestParamsMap.put("fnick", "");  
            requestParamsMap.put("_t", "0"); 
            requestParamsMap.put("template", "4"); 
            requestParamsMap.put("isinterest", "true"); 
            PrintWriter printWriter = null;  
            BufferedReader bufferedReader = null;  
            // BufferedReader bufferedReader = null;  
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
                httpURLConnection.addRequestProperty("Referer", "http://weibo.com/p/1008080bace14e828f6f63b44b62948e27c4b5/home?from=page_100808&mod=TAB");
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
                System.out.println(responseResult);
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
	public static void main(String agrs[]){
		try{
			String userId = "ukqvavful@sina.com,ukukcfqjutek@sina.com,ukwpgdmfwvxu522@sina.com,ukyauyzhdrsk885@sina.com,ukyoozxzvz5@sina.com,uldulxojq590@sina.com,uleoneszfklx37@sina.com,ulhwobviekpv91@sina.com,ulikgbthgn@sina.com,ullvujfah@sina.com,ullzoldmsxbz06@sina.com,ulnsiowlahs@sina.com,ulpegodgala@sina.com,ulqvfsfulf79@sina.com,ulqvqdgog@sina.com,ulthvjjosx11@sina.com,uluyhiayqqf@sina.com,umduekbdd678@sina.com,umfqsdylpzdb7@sina.com,umgxzoqjcz@sina.com,umhuhrbttlpa30@sina.com,umkcdqoera97@sina.com,umlotjsgmvp@sina.com,ummolgfgvs0@sina.com,umotdoizhc@sina.com,umqvvbwmhqj78@sina.com,umsjazeppiw4@sina.com,umwkwaaxx649@sina.com,undchgsjtcmi4@sina.com,undpohvwbvjh971@sina.com,unehhbwmhut65@sina.com,unfsibehicl083@sina.com,unfxiiqicglm0@sina.com,ungovqlmtuk915@sina.com,ungtrpvowqk@sina.com,unhmzewfoi89@sina.com,unipxtflvsje8@sina.com,unjqrgdslezp55@sina.com,unnofzyck6@sina.com,unpartqbem@sina.com,unqvapicgyks7@sina.com,unrardyzuc816@sina.com,unttfhojjyos068@sina.com,unukmbnpffk@sina.com,unuolupnsvbl@sina.com,unvbsvsaaoyk193@sina.com,unxidhjbzbmt@sina.com,unzjwwehcvwv066@sina.com,unztitdcmcrw@sina.com,uochxkairqc@sina.com,uodbegbxblc692@sina.com,uomnriequts81@sina.com,uopmbxhgdsy3@sina.com,uorxnueda9@sina.com,uosanvvqjlr@sina.com,uotndvxhmdgq83@sina.com,uouyrweaz81@sina.com,uowccmtsmrkj@sina.com,uowsizxxgxde52@sina.com,upapvketvbhj87@sina.com,upaznwgmqy@sina.com,upejcoyhuee8@sina.com,upfijwndz@sina.com,uphbgllpq10@sina.com,upnanyytp@sina.com,upnfqubotxox84@sina.com,upnmpujcvf@sina.com,uppqeqdpbber@sina.com,upqrrlwuhwei1@sina.com,uprbkqgsvqs@sina.com,upsinezzs6@sina.com,upuuncigkz4@sina.com,upvntdzvv@sina.com,upxxlpnkbqsb18@sina.com,uqbjnboah7@sina.com,uqdvnqsnhciw@sina.com,uqdzdzcisok94@sina.com,uqexoglua27@sina.com,uqfzhqcwenbo11@sina.com,uqmeezijnco5@sina.com,uqmigzmgs@sina.com,uqmununnzqhk@sina.com,uqngmgqwy2@sina.com,uqwikqphf42@sina.com,uqxjvtfobwqb6@sina.com,uqzdjathpf03@sina.com,uraynccao8@sina.com,urcfnnwbknyx@sina.com,urdjepkuglwm8@sina.com,uremzjufmty@sina.com,uroeyelxw@sina.com,urruqehxkr01@sina.com,urugqpaxvo95@sina.com,urunkdpje@sina.com,ururbuhawc1@sina.com,urvydizhsygj1@sina.com,urwayrwni6@sina.com,uryrjidsqtrf@sina.com,urysayszu@sina.com,urzpwrqeyl53@sina.com,usacskvkul5@sina.com,usbycfksu6@sina.com";
			String a[] = userId.split(",");
			for(int i=0;i<a.length;i++){
				new WeiboAddHuaFen().sina(a[i], 0);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
