package com.longriver.netpro.webview.controller;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.Logger;

import com.longriver.netpro.common.sina.SData;
import com.longriver.netpro.common.sina.SinaIdMidConverter;
import com.longriver.netpro.common.sina.WeiboSina;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.TaskGuideBean;

public class WeiboPost {
	private static Logger logger = Logger.getLogger(WeiboPost.class);
	
	public static void main(String args[]){
//		String userId = "15250221843";
//		String pwd = "lx1314";
//		String addr = "http://weibo.com/2549964007/E03Agg8Ob?type=comment#_rnd1469417038214";
//		String contents = "赞一个";
		
		TaskGuideBean s = new TaskGuideBean();
//		s.setNick("2498126309@qq.com");
//		s.setPassword("lilei419688");
		s.setNick("lilei1929@163.com");
		s.setPassword("lilei419688..");
		s.setNick("13226172899"); //异常账号
		s.setPassword("lx1314");
		s.setAddress("https://weibo.com/76649/FDIsr3oFD?from=page_10050576649_profile&wvr=6&mod=weibotime&type=comment#_rnd1515572964664");
		s.setCorpus("hahha,不错");
		s.setNick("18833518933");
		s.setPassword("lx1314");//
		s.setNick("15542643414");
		s.setPassword("hz17042");//
		s.setNick("18229656966");
		s.setPassword("srigju101");//
		s.setNick("lilei1929@163.com");
		s.setPassword("lilei419688..");
		s.setNick("39121975@qq.com");
		s.setPassword("s1s2m3949");//
//		s.setNick("13226172899"); //异常账号
//		s.setPassword("lx1314");
		s.setAddress("https://weibo.com/76649/FDIsr3oFD");
		s.setCorpus("emmm");
		s.setNick("15002237262");
		s.setPassword("fenglong1994");//
		sina(s);
		
	}
	
	public static void sina(TaskGuideBean taskdo){
		logger.info("------------------------------------ Sina- weibo post----------------------------------------");
		String takeComment = "0";
		
		SData data =new SData();
		try {
			String userId =taskdo.getNick();
			String pwd = taskdo.getPassword();
			String addr = taskdo.getAddress();
			String contents = taskdo.getCorpus();
			if(contents==null || contents.trim().equals("")){
				contents = "转发微博";
			}
			logger.debug("222222222222222222222222=="+addr);
			
			data.put("user_account_id", userId);
			data.put("user_account_pw", pwd);
			data.put("mission_addr", addr);
			data.put("mission_contents", "");
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
			String content = "未定义失败";
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
				logger.debug("uid=="+uid);
				logger.debug("6666666666666666666");
				URL u45 = new URL(addr);
				HttpURLConnection c45 = (HttpURLConnection) u45.openConnection();
				c45.addRequestProperty("Host", "weibo.com");
		//		c45.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
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
//					System.out.println("scsc=="+scsc);
					if(scsc.indexOf("$CONFIG['page_id']") > -1){
						pdetail = scsc.substring(scsc.indexOf("$CONFIG['page_id']")).replace("$CONFIG['page_id']", "");
						if(pdetail.indexOf("'")>-1)
							pdetail = pdetail.substring(pdetail.indexOf("'") + 1);
						if(pdetail.indexOf("'")>-1)
							pdetail = pdetail.substring(0, pdetail.indexOf("'"));
					}
					if(scsc.indexOf("$CONFIG['location']") > -1){
						location = scsc.substring(scsc.indexOf("$CONFIG['location']")).replace("$CONFIG['location']", "");
						if(location.indexOf("'")>-1)
							location = location.substring(location.indexOf("'") + 1);
						if(location.indexOf("'")>-1)
							location = location.substring(0, location.indexOf("'"));
					}
					if(!pdetail.equals("") && !location.equals("")) break;
				}
				//System.out.println(pdetail);
				
		//    URL u451 = new URL("http://api.t.sina.com.cn/queryid.json?mid=" + midEncode + "&isBase62=1&type=1");
		//    HttpURLConnection c451 = (HttpURLConnection) u451.openConnection();
		//    c451.connect();
		//    InputStream i451 = c451.getInputStream();
		//    Scanner s451 = new Scanner(i451);
				String m = "";
		//    while(s451.hasNext()){
		//    	String scsc = s451.nextLine();
		//    	if(scsc.indexOf("id") > -1){
		//    		m = scsc.substring(scsc.indexOf("{\"id\":\"")).replace("{\"id\":\"", "");
		//    		m = m.substring(0, m.indexOf("\""));
		//    	}
		//    }
				logger.debug("777777777777777777");
				String tm = data.getString("mission_addr");
				if(tm.indexOf("?") > -1){
					tm = tm.substring(0, tm.indexOf("?"));
				}
				if(tm.indexOf("/") > -1){
					tm = tm.substring(tm.lastIndexOf("/")).replace("/", "").replace("?", "").trim();
				}
				m = SinaIdMidConverter.midToId(midEncode);
				System.out.println("------------------------------------WeiboTransmit Sina-----------------------------------------");
		//    System.out.println(uid + "|" + m);
		//    System.out.println(cookie);
				String rid = "";
				if(data.getString("mission_addr").indexOf("rid=") > -1){
				    rid = data.getString("mission_addr");
				    rid = rid.substring(rid.indexOf("rid=") + 4);
				}
				if(rid.indexOf("&") > -1){
					rid = rid.substring(0, rid.indexOf("&"));
				}
				String paramss = "pic_src="
						+ "&appkey="
						+ "&mid=" + m
						+ "&style_type=1"
						+ "&mark="
						+ "&reason=" + URLEncoder.encode(contents, "utf-8")
						+ "&location=" + location
						+ "&pdetail=" + pdetail
						+ "&module="
						+ "&page_module_id="
						+ "&refer_sort="
						+ "&rank=0"
						+ "&rankid="
						+ "&group_source=group_all"
						+ "&rid=" + rid
						+ "&_t=0";
				if(takeComment.equals("1")){
					paramss = paramss + "&is_comment_base=1";
				}
				URL u5 = new URL("https://weibo.com/aj/v6/mblog/forward?ajwvr=6&domain=100505&__rnd=" + new Date().getTime());
				//http://weibo.com/aj/v6/mblog/forward?ajwvr=6&domain=5234172433&__rnd=1417000316699
				HttpURLConnection c5 = (HttpURLConnection) u5.openConnection();
				//System.out.println(paramss);
				c5.addRequestProperty("Host", "weibo.com");
		//		c5.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
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
				// 必须设置false，否则会自动redirect到Location的地址  
				c5.setInstanceFollowRedirects(false); 
				c5.setConnectTimeout(1000 * 30);
				c5.setReadTimeout(1000 * 20);
				c5.setDoInput(true);
				c5.setDoOutput(true);
				PrintWriter o5 = new PrintWriter(c5.getOutputStream());
				o5.print(paramss);
				o5.flush();
				InputStream i5 = null;
				try{
					i5 = c5.getInputStream();
				}catch(Exception e){
					e.printStackTrace();
				}
				logger.info("weiboPost exec...");
				//返回状态码和跳转地址判断
				if(c5.getResponseCode()==302 || 
						(c5.getHeaderField("Location")!=null && c5.getHeaderField("Location").contains("security.weibo.com"))){
					content = "帐号异常";
					taskdo.setCode(2);
				}
				Scanner s5 = new Scanner(i5);
				while(s5.hasNext()){
					String scsc = StringUtil.decodeUnicode(s5.nextLine());
					System.out.println("scsc================"+scsc);
					content = WeiboSina.getResultContent(scsc,content,taskdo);
				}
		
				logger.info("uid=="+uid);
				link = "http://weibo.com/" + uid + "/";// + SinaIdMidConverter.idToMid(mid);
			}else{
				content = WeiboSina.getDengluResult(cookie,content,taskdo);
	 		}
		 		
			System.out.println(link);
			//service.execute("tb_weibo_return", missionId, statusCd, link);
			taskdo.setUrl(link);
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

	
	
	
	public String formatString(String str) {
        if (str != null) {
        	try {                
        		str = new String(str.getBytes("ISO-8859-1"), "UTF-8");            
        		} catch (Exception e) {}        
        		}
        return str;    
    }
	
}
