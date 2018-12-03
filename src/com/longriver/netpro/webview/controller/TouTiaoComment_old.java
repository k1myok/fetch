package com.longriver.netpro.webview.controller;

import java.io.IOException;  
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.ByteArrayOutputStream;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;


public class TouTiaoComment_old {
	private static Logger logger = Logger.getLogger(TouTiaoComment_old.class);
	
	public static void main(String args[]){
		try{
			TaskGuideBean taskdo = new TaskGuideBean();
//			taskdo.setNick("18854579641");
//			taskdo.setPassword("Mopao7077");
			taskdo.setNick("15999726521");
			taskdo.setPassword("Kaoshun91");
//			taskdo.setNick("18610647727");
//			taskdo.setPassword("070710");
//			taskdo.setNick("wljiaj492711725@163.com");
//			taskdo.setPassword("Jufei607");
//			taskdo.setAddress("http://www.toutiao.com/group/6339620629188215041/");
//			taskdo.setAddress("http://www.toutiao.com/a6373421470982684930/#comment_area");
//			taskdo.setAddress("http://www.toutiao.com/a6376393265406755073/#p=1");
//			taskdo.setPraiseWho("54016307750");//未成年人不可能捐肾的，医院不可能批准的，要么是小编乱编，要不就是中国太大了，农村地区是法律管不到的地方。
//			taskdo.setAddress("http://www.toutiao.com/a6373421470982684930/");
//			taskdo.setAddress("http://www.toutiao.com/a6401951026417058049/?tt_from=weixin&utm_campaign=client_share&app=news_article&utm_source=weixin&iid=6867373614&utm_medium=toutiao_android&wxshare_count=1");
//			taskdo.setAddress("http://www.toutiao.com/a6412416210768593154/#comment_area");
			taskdo.setAddress("http://www.toutiao.com/a6466594468135961102/");
//			taskdo.setAddress("http://www.toutiao.com/a6375828888539840770/#comment_area");
//			taskdo.setPraiseWho("6826042570");
			taskdo.setCorpus("yunsi.");
			toutiaoComment(taskdo);
//			toutiaoDigg(taskdo);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void toutiaoComment(TaskGuideBean taskdo) throws Exception{
		logger.info("toutiaoComment");
		String userName =taskdo.getNick();
		String userPwd = taskdo.getPassword();
		String url = taskdo.getAddress();
//		String url = taskdo.getAddress();
		String content = taskdo.getCorpus();
		String groupId = "";
		if(taskdo.getAddress().contains("group")){
			groupId = taskdo.getAddress().substring(taskdo.getAddress().indexOf("group")).replace("group", "").replace("/", "").replace("%2F", "");
		}
		
		Map<String,String> a =new HashMap<String,String>();
		
		if(groupId.equals("")){
			a = getGroupAndItem(url);
		}
		
		String cookie = "";
//		String userName = "18610647727";
//		String userPwd = "685599";
//		String content = "为什么高手都在民间，期待期待啊！！";
		String csrftoken = "";
		if(userName.contains("@")){ //邮箱登录
					try{
						URL u1 = new URL(url);
						HttpURLConnection fc11 = (HttpURLConnection) u1.openConnection();
						fc11.addRequestProperty("Accept", "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01");
						fc11.addRequestProperty("Accept-Language", "zh-cn");
						fc11.addRequestProperty("Connection", "Keep-Alive");
						fc11.addRequestProperty("X-Requested-With", "XMLHttpRequest");
						fc11.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
						fc11.connect();
						InputStream fi11 = fc11.getInputStream();
						Scanner fs11 = new Scanner(fi11);
						Map<String, List<String>> m1 = fc11.getHeaderFields();
						for(Map.Entry<String,List<String>> entry : m1.entrySet()){
							System.out.println(entry.getKey());
							if(entry.getKey() != null && entry.getKey().equals("Set-Cookie")){
								for(String value : entry.getValue()){
									if(value.contains("csrftoken")){
										csrftoken = value.substring(0, value.indexOf(";"));
									}
									cookie = cookie + value.substring(0, value.indexOf(";")) + ";";
								}
							}
						}	
						
					}catch(Exception e){
						e.printStackTrace();
					}
				
				System.out.println("cookie=="+cookie);
				
				csrftoken = csrftoken.replace("csrftoken=", "").replace(";", "");
			    URL u1 = new URL("http://toutiao.com/auth/login/");
			    String params = "name_or_email="+URLEncoder.encode(userName,"UTF-8")+"&password="+URLEncoder.encode(userPwd,"UTF-8")+"&captcha=";
				
				HttpURLConnection c1 = (HttpURLConnection) u1.openConnection();
				c1.addRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
				c1.addRequestProperty("Host", "toutiao.com");
				c1.addRequestProperty("Connection", "Keep-Alive");
				c1.addRequestProperty("Referer", url);
				c1.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
				c1.addRequestProperty("Cookie", cookie);
				c1.addRequestProperty("X-CSRFToken", csrftoken);
				
				c1.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36");
				c1.addRequestProperty("X-Requested-With", "XMLHttpRequest");
				c1.setDoInput(true);
				c1.setDoOutput(true);
				PrintWriter out = new PrintWriter(c1.getOutputStream());
				out.print(params);
				out.flush();
		        Map<String, List<String>> m11 = c1.getHeaderFields();
				for(Map.Entry<String,List<String>> entry : m11.entrySet()){
					if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
						for(String value : entry.getValue()){
							cookie = cookie + value.substring(0, value.indexOf(";")) + ";";
						}
					}
				}
		}else{ //手机号登录
					long  _rticket= new Date().getTime();
					
					System.out.println("_rticket==========================="+_rticket);
					URL u1 = new URL("https://security.snssdk.com/user/mobile/login/v2/?iid=6179367143&device_id=24729295476"
						+ "&ac=wifi"
						+ "&channel=xiaomi&aid=13"
						+ "&app_name=news_article"
						+ "&version_code=586&version_name=5.8.6"
						+ "&device_platform=android"
						+ "&ab_version=87587%2C83098%2C79287%2C87331%2C87982%2C88303%2C31211%2C88378%2C88701%2C88716%2C88798%2C88185%2C88671%2C88456%2C88445%2C88725%2C88320%2C87497%2C87988&ab_client=a1%2Cc4%2Ce1%2Cf2%2Cg2%2Cf7&abflag=3&ssmix=a&device_type=MI+NOTE+LTE&device_brand=Xiaomi&language=zh&os_api=19&os_version=4.4.4&uuid=867993024531475&openudid=8bddc56565d32cfa&manifest_version_code=586&resolution=1080*1920&dpi=440&update_version_code=5860"
						+ ""
						+ "&_rticket="+_rticket);	
					long iid = 6434442483l;
					Random random = new Random();
					int num = random.nextInt(1000);
					iid = iid+num;
					String params = "mix_mode=1"
					+ "&password="+encode(md5JieMi(userPwd))
					+ "&mobile="+encode(md5JieMi(userName))
					+ "&iid="+iid
					+ "&device_id=24729295476"
					+ "&ac=wifi&channel=xiaomi&aid=13"
					+ "&app_name=news_article"
					+ "&version_code=591&version_name=5.9.1"
					+ "&device_platform=android"
					+ "&ab_version=95357%2C97429%2C98928%2C99094%2C95737%2C97930%2C98105%2C99297%2C98782%2C97143%2C90764%2C98687%2C98581%2C95444%2C92439%2C99294%2C98670%2C99193%2C99211%2C99024%2C93448%2C99334%2C99237%2C99295%2C99036%2C98044%2C96058%2C98574"
					+ "&ab_client=a1%2Cc4%2Ce1%2Cf2%2Cg2%2Cf7"
					+ "&abflag=3&ssmix=a"
					+ "&device_type=MI%20NOTE%20LTE"
					+ "&device_brand=Xiaomi&language=zh&os_api=19"
					+ "&os_version=4.4.4&uuid=867993024531475"
					+ "&openudid=8bddc56565d32cfa&manifest_version_code=591"
					+ "&resolution=1080*1920&dpi=440&update_version_code=5914"
					+ "&_rticket=1482978861576";
				
					System.out.println("userName=="+encode(md5JieMi(userName)));
					System.out.println("userPwd=="+md5JieMi(userPwd));
					
					HttpURLConnection c1 = (HttpURLConnection) u1.openConnection();
					c1.setDoInput(true);
					c1.setDoOutput(true);
					PrintWriter out = new PrintWriter(c1.getOutputStream());
					out.print(params);
					out.flush();
			                
			        Map<String, List<String>> m11 = c1.getHeaderFields();
					for(Map.Entry<String,List<String>> entry : m11.entrySet()){
						if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
							for(String value : entry.getValue()){
								cookie = cookie + value.substring(0, value.indexOf(";")) + ";";
							}
						}
					}
					
					String ss = changeInputStream(c1.getInputStream(), "UTF-8");
			    	System.out.println("ss=="+ss);
			    	System.out.println("cookie=="+cookie);
		}
		URL u3= new URL("http://isub.snssdk.com/2/data/post_message/");
		String paramss="";
		if(groupId.equals("")){
			 paramss = "group_id="+a.get("groupid")
						+ "&item_id="+a.get("itemid")
						+ "&aggr_type=1"
						+ "&forum_id=0"
						+ "&text="+URLEncoder.encode(content,"UTF-8")
						+ "&is_comment=0"
						+ "&action=share&read_pct=100"
						+ "&staytime_ms=2426&iid=6434442483"
						+ "&device_id=24729295476&ac=wifi"
						+ "&channel=xiaomi&aid=13&app_name=news_article"
						+ "&version_code=589&version_name=5.8.9&device_platform=android"
						+ "&ab_client=a1%2Cc4%2Ce1%2Cf2%2Cg2%2Cf7"
						+ "&abflag=3"
						+ "&ssmix=a"
						+ "&device_type=MI%20NOTE%20LTE"
						+ "&device_brand=Xiaomi&language=zh"
						+ "&os_api=19"
						+ "&os_version=4.4.4&uuid=867993024531475"
						+ "&openudid=8bddc56565d32cfa"
						+ "&manifest_version_code=589&resolution=1080*1920"
						+ "&dpi=440&update_version_code=5894"
						+ "&_rticket=1480470489741";
				
		}else{
				paramss = "group_id="+groupId
				+ "&item_id="
				+ "&aggr_type=1"
				+ "&forum_id=0"
				+ "&text="+URLEncoder.encode(content,"UTF-8")
				+ "&is_comment=0"
				+ "&action=share&read_pct=100"
				+ "&staytime_ms=2426&iid=6434442483"
				+ "&device_id=24729295476&ac=wifi"
				+ "&channel=xiaomi&aid=13&app_name=news_article"
				+ "&version_code=589&version_name=5.8.9&device_platform=android"
				+ "&ab_client=a1%2Cc4%2Ce1%2Cf2%2Cg2%2Cf7"
				+ "&abflag=3"
				+ "&ssmix=a"
				+ "&device_type=MI%20NOTE%20LTE"
				+ "&device_brand=Xiaomi&language=zh"
				+ "&os_api=19"
				+ "&os_version=4.4.4&uuid=867993024531475"
				+ "&openudid=8bddc56565d32cfa"
				+ "&manifest_version_code=589&resolution=1080*1920"
				+ "&dpi=440&update_version_code=5894"
				+ "&_rticket=1480470489741";
		}
		HttpURLConnection c3 = (HttpURLConnection) u3.openConnection();
		c3.setRequestMethod("GET" );
		c3.addRequestProperty("Host", "isub.snssdk.com");
		c3.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36 SE 2.X MetaSr 1.0");
		c3.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		c3.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
		c3.addRequestProperty("Cookie", cookie);
		c3.addRequestProperty("Connection", "Keep-Alive");
		c3.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		c3.setDoInput(true);
		c3.setDoOutput(true);
		PrintWriter o5 = new PrintWriter(c3.getOutputStream());
		o5.print(paramss);
		o5.flush();
		
	    System.out.println(c3.getResponseCode());
	    String ct = "失败";
	    if (c3.getResponseCode() == 200) { 
	    	String ss = changeInputStream(c3.getInputStream(), "UTF-8");
	    	System.out.println(ss);
	    	if(ss.contains("success")) ct = "";
        } 
	    
	    logger.info("toutiao comments end...");
	    MQSender.toMQ(taskdo,ct);
	}
	private static String changeInputStream(InputStream inputStream,  
            String encode) {  
        // TODO Auto-generated method stub  
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();  
        byte[] data = new byte[1024];  
        int len = 0;  
        String result = "";  
        if (inputStream != null) {  
            try {  
                while ((len = inputStream.read(data)) != -1) {  
                    outputStream.write(data, 0, len);  
                }  
                result = new String(outputStream.toByteArray(), encode);  
            } catch (IOException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        }  
        return result;  
    }  


public static Map<String,String> getGroupAndItem(String sUrl){
	Map<String,String> a = new HashMap<String,String>();
	try{
		URL u2 = new URL(sUrl);
		HttpURLConnection c2 = (HttpURLConnection) u2.openConnection();
		c2.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
		c2.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		c2.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
		c2.addRequestProperty("Connection", "keep-alive");
		c2.addRequestProperty("Host", "www.toutiao.com");
		//c2.addRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
		c2.addRequestProperty("Referer", sUrl);
		
		c2.connect();
		InputStream f= c2.getInputStream();
		Scanner fs = new Scanner(f);
		String groupid = "";
		String itemid = "";
		StringBuffer temp = new StringBuffer();
		while(fs.hasNext()){
			String scsc11 = fs.nextLine();
			temp.append(scsc11);
			
		}	
		String regexGroup = "group_id: '([\\d\\D]*?)',"; 
	    groupid = getContent(regexGroup,temp.toString());
	    if(groupid.equals("")){
	    	String regexGroupOther = "'groupid'   :([\\d\\D]*?)',"; 
	    	groupid = getContent(regexGroupOther,temp.toString());
	    	groupid = groupid.replaceAll("'", "").trim();
	    }
	    String regexitemid = "item_id: '([\\d\\D]*?)',"; 
	    itemid = getContent(regexitemid,temp.toString());
	    itemid = itemid.replaceAll("'", "").replace("|", "").trim();
	    if(itemid.equals("")){
	    	String regexGroupOther = "'itemid'    :([\\d\\D]*?)'"; 
	    	itemid = getContent(regexGroupOther,temp.toString());
	    }
		a.put("groupid", groupid);
		a.put("itemid", itemid);
	}catch(Exception e){
		e.printStackTrace();
	}
	return a;
}
private static String getContent(String regex,String text) {  
    String content = "";  
    Pattern pattern = Pattern.compile(regex);  
    Matcher matcher = pattern.matcher(text);  
    while(matcher.find()) {  
        content = matcher.group(1).toString();  
    }  
   
    return content;  
} 
public void toutiaoReplyComment() throws Exception{
	
	String cookie = "";
	URL u1 = new URL("https://ic.snssdk.com/user/mobile/login/?password=685599&mobile=18610647727");

	HttpURLConnection c1 = (HttpURLConnection) u1.openConnection();
	c1.setDoInput(true);
	c1.setDoOutput(true);
	c1.connect();
	String session_value = c1.getHeaderField("Set-Cookie" );
    String[] sessionId = session_value.split(";");
    System.out.println(sessionId[0]);         

    cookie = sessionId[0];
 	URL u3 = new URL("http://isub.snssdk.com/2/data/post_message/?group_id=6218838600352530946&text="+URLEncoder.encode("人渣啊，人渣。那女孩也真是，干嘛让自己那么贱1","UTF-8")+"&is_comment=0&action=share&reply_to_comment_id=3905492935");
	HttpURLConnection c3 = (HttpURLConnection) u3.openConnection();
	c3.setRequestMethod("GET" );
	c3.addRequestProperty("Host", "isub.snssdk.com");
	c3.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36 SE 2.X MetaSr 1.0");
	c3.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
	c3.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
	//c3.addRequestProperty("Referer", "");
	c3.addRequestProperty("Cookie", cookie);
	c3.addRequestProperty("Connection", "Keep-Alive");
	c3.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	c3.setDoInput(true);
	c3.setDoOutput(true);
	c3.connect();
	
    System.out.println(c3.getResponseCode());
    if (c3.getResponseCode() == 200) {  
         System.out.println(changeInputStream(c3.getInputStream(), "UTF-8"));
    } 
}
public static String md5JieMi(String src) {
    // char psw = '0x05';
     char srcArray[] = src.toCharArray();
     for (int i = 0; i < src.length(); i++) {
         srcArray[i] = (char) (5 ^ srcArray[i]);
     }
     return new String(srcArray);
 }
 
 
 public static String encode(String str) {
     // 根据默认编码获取字节数组
     byte[] bytes = str.getBytes();
     StringBuilder sb = new StringBuilder(bytes.length * 2);
     // 将字节数组中每个字节拆解成2位16进制整数
     for (int i = 0; i < bytes.length; i++) {
     sb.append(hexString.charAt((bytes[i] & 0xff) >> 4));
     sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
     }
     return sb.toString().toLowerCase();
 }
 private static String hexString = "0123456789ABCDEF";
 
 public static void toutiaoDigg(TaskGuideBean taskdo) throws Exception{
		
		String userName =taskdo.getNick();
		String userPwd = taskdo.getPassword();
		String comment_id = taskdo.getPraiseWho();//"5916353478";
		String url = taskdo.getAddress();
		
		String cookie = "";
//		String userName = "13532317911";
//		String userPwd = "a123456";
		String csrftoken = "";
		if(userName.contains("@")){
			
					try{
						URL u1 = new URL(url);
						HttpURLConnection fc11 = (HttpURLConnection) u1.openConnection();
						fc11.addRequestProperty("Accept", "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01");
						fc11.addRequestProperty("Accept-Language", "zh-cn");
						fc11.addRequestProperty("Connection", "Keep-Alive");
						fc11.addRequestProperty("X-Requested-With", "XMLHttpRequest");
						fc11.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
						fc11.connect();
						InputStream fi11 = fc11.getInputStream();
						Scanner fs11 = new Scanner(fi11);
						Map<String, List<String>> m1 = fc11.getHeaderFields();
						for(Map.Entry<String,List<String>> entry : m1.entrySet()){
							System.out.println(entry.getKey());
							if(entry.getKey() != null && entry.getKey().equals("Set-Cookie")){
								for(String value : entry.getValue()){
									if(value.contains("csrftoken")){
										csrftoken = value.substring(0, value.indexOf(";"));
									}
									cookie = cookie + value.substring(0, value.indexOf(";")) + ";";
								}
							}
						}	
						
					}catch(Exception e){
						e.printStackTrace();
					}
				
				System.out.println(cookie);
				
				csrftoken = csrftoken.replace("csrftoken=", "").replace(";", "");
		    URL u1 = new URL("http://toutiao.com/auth/login/");
		    String params = "name_or_email="+URLEncoder.encode(userName,"UTF-8")+"&password="+URLEncoder.encode(userPwd,"UTF-8")+"&captcha=";
			
			HttpURLConnection c1 = (HttpURLConnection) u1.openConnection();
			c1.addRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
			c1.addRequestProperty("Host", "toutiao.com");
			c1.addRequestProperty("Connection", "Keep-Alive");
			c1.addRequestProperty("Referer", url);
			c1.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			c1.addRequestProperty("Cookie", cookie);
			c1.addRequestProperty("X-CSRFToken", csrftoken);
			
			c1.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36");
			c1.addRequestProperty("X-Requested-With", "XMLHttpRequest");
			c1.setDoInput(true);
			c1.setDoOutput(true);
			PrintWriter out = new PrintWriter(c1.getOutputStream());
			out.print(params);
			out.flush();
	        Map<String, List<String>> m11 = c1.getHeaderFields();
			for(Map.Entry<String,List<String>> entry : m11.entrySet()){
				if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
					for(String value : entry.getValue()){
							cookie = cookie + value.substring(0, value.indexOf(";")) + ";";
					}
				}
			}
		}else{
			long  _rticket= new Date().getTime();
			
			System.out.println("_rticket==============================================================="+_rticket);
			URL u1 = new URL("https://security.snssdk.com/user/mobile/login/v2/?iid=6179367143&device_id=24729295476"
				+ "&ac=wifi"
				+ "&channel=xiaomi&aid=13"
				+ "&app_name=news_article"
				+ "&version_code=586&version_name=5.8.6"
				+ "&device_platform=android"
				+ "&ab_version=87587%2C83098%2C79287%2C87331%2C87982%2C88303%2C31211%2C88378%2C88701%2C88716%2C88798%2C88185%2C88671%2C88456%2C88445%2C88725%2C88320%2C87497%2C87988&ab_client=a1%2Cc4%2Ce1%2Cf2%2Cg2%2Cf7&abflag=3&ssmix=a&device_type=MI+NOTE+LTE&device_brand=Xiaomi&language=zh&os_api=19&os_version=4.4.4&uuid=867993024531475&openudid=8bddc56565d32cfa&manifest_version_code=586&resolution=1080*1920&dpi=440&update_version_code=5860"
				+ ""
				+ "&_rticket="+_rticket);	
		long iid = 6434442483l;
		Random random = new Random();
		int num = random.nextInt(1000);
		iid = iid+num;
		String params = "mix_mode=1"
			+ "&password="+encode(md5JieMi(userPwd))
			+ "&mobile="+encode(md5JieMi(userName))
			+ "&iid="+iid
			+ "&device_id=24729295476"
			+ "&ac=wifi&channel=xiaomi&aid=13"
			+ "&app_name=news_article"
			+ "&version_code=590&version_name=5.9.0"
			+ "&device_platform=android"
			+ "&ab_version=93616%2C83098%2C89596%2C89186%2C87331%2C93903%2C93392%2C93419%2C93094%2C92848%2C91588%2C90764%2C93895%2C93467%2C82680%2C93318%2C92438%2C93156%2C92952%2C93357%2C92631%2C92544%2C93448%2C93772%2C92487%2C87498%2C93890%2C87988&ab_client=a1%2Cc4%2Ce1%2Cf2%2Cg2%2Cf7&abflag=3&ssmix=a&device_type=MI%20NOTE%20LTE&device_brand=Xiaomi&language=zh&os_api=19&os_version=4.4.4&uuid=867993024531475&openudid=8bddc56565d32cfa&manifest_version_code=590&resolution=1080*1920&dpi=440&update_version_code=5904&_rticket=1480928107659";

				System.out.println(encode(md5JieMi(userName)));
				System.out.println(encode(md5JieMi(userPwd)));
			HttpURLConnection c1 = (HttpURLConnection) u1.openConnection();
			c1.setDoInput(true);
			c1.setDoOutput(true);
			PrintWriter out = new PrintWriter(c1.getOutputStream());
			out.print(params);
			out.flush();
		            
		    Map<String, List<String>> m11 = c1.getHeaderFields();
			for(Map.Entry<String,List<String>> entry : m11.entrySet()){
				if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
					for(String value : entry.getValue()){
						cookie = cookie + value.substring(0, value.indexOf(";")) + ";";
					}
				}
			}
		}
		String groupId = "";
		String itemsid = "";
		if(taskdo.getAddress().contains("group")){
			groupId = taskdo.getAddress().substring(taskdo.getAddress().indexOf("group")).replace("group", "").replace("/", "").replace("%2F", "");
		}
		String urluu = taskdo.getAddress();
		if(groupId.equals("")){
			Map<String,String> a = getGroupAndItem(urluu);
			groupId = a.get("groupid");
			itemsid = a.get("itemid");
		}
		
	       System.out.println("cookie=="+cookie);
			URL u3 = new URL("http://isub.snssdk.com/2/data/comment_action/?");
//			String paramss = "comment_id="+comment_id+"&action=digg&app_name=news_article";
			String paramss = "comment_id="+taskdo.getPraiseWho()+"&action=digg&group_id="+groupId+"&item_id="+itemsid;
			HttpURLConnection c3 = (HttpURLConnection) u3.openConnection();
			c3.addRequestProperty("Host", "isub.snssdk.com");
			c3.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36 SE 2.X MetaSr 1.0");
			c3.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			c3.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			c3.addRequestProperty("Cookie", cookie);
			c3.addRequestProperty("Content-Length", String.valueOf(paramss.length()));
			c3.addRequestProperty("Connection", "Keep-Alive");
			c3.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			c3.setDoInput(true);
			c3.setDoOutput(true);
	        PrintWriter o5 = new PrintWriter(c3.getOutputStream());
			o5.print(paramss);
			o5.flush();
			
			c3.connect();
			
			
		    System.out.println(c3.getResponseCode());
		    String ct = "失败";
		    if (c3.getResponseCode() == 200) { 
		    	String ss = changeInputStream(c3.getInputStream(), "UTF-8");
		    	System.out.println(ss);
		    	if(ss.contains("success")) ct = "";
	        } 
		    MQSender.toMQ(taskdo,ct);
			
		}

}
