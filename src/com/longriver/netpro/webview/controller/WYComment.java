package com.longriver.netpro.webview.controller;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.util.DriverGet;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 网易新闻评论
 */
public class WYComment {
	public static void main(String s[]){
		TaskGuideBean b = new TaskGuideBean();
//		b.setNick("xyzxwdyc@163.com");
//		b.setPassword("Yn19830205");
		
		b.setNick("ybdy2015@163.com");
		b.setPassword("sccony12345678");
		
		b.setNick("wardenhy@163.com");
		b.setPassword("yzy19807012");
		b.setNick("m17173425505@163.com");
		b.setPassword("chwx123456");
//		b.setNick("wardenhy@163.com");
//		b.setPassword("yzy19807012");
		
		b.setAddress("http://travel.163.com/17/1121/00/D3NN3J8D00067VF3.html");
//		b.setAddress("http://news.163.com/17/1229/01/D6PN9013000187VI.html");
//		b.setAddress("http://auto.163.com/17/0915/15/CUCSULN70008856R.html");
//		b.setAddress("http://comment.news.163.com/news2_bbs/CSBTRI2N000189FH.html");
		b.setCorpus("拍的很漂亮啊..");
		b.setCorpus("不错不错");
		wangyi(b);
//		getPwd("123");
//		Map<String,String> mm = new HashMap<String,String>();
//		getVcode(mm,b,0);
//		String ssssss = "{\"against\":0,\"boardId\":\"news_guonei8_bbs\",\"channelId\":\"0001\",\"cmtAgainst\":7,\"cmtVote\":3456,\"createTime\":\"2017-11-20 07:16:58\",\"docId\":\"D3LTGGSU0001875N\",\"isAudit\":true,\"modifyTime\":\"2017-11-20 11:36:08\",\"pdocId\":\"D3LTGGSU0001875N\",\"rcount\":1186,\"status\":{\"label\":\"on\",\"against\":\"on\",\"joincount\":\"on\",\"audio\":\"off\",\"web\":\"on\",\"app\":\"on\"},\"tcount\":68,\"title\":\".\",\"url\":\"http://news.163.com/17/1120/07/D3LTGGSU0001875N.html\",\"vote\":377}";
//		JSONObject parseObject = JSON.parseObject(ssssss);
//		String boardId = parseObject.getString("boardId");
//		System.out.println(boardId);
		
	}
	/**
	 * 新闻评论链接都可
	 */
	public static void wangyi(TaskGuideBean taskdo){
		try {
			System.out.println("网易新闻评论");
			Map<String,String> mapuurl = formatUrl(taskdo);
			String address = mapuurl.get("address");
			String boardId = mapuurl.get("boardId");//money_bbs
			String newsId = mapuurl.get("newsId");
			
			String tiec = getTiec(address);
			
			Map<String,String> mapcookie = login(taskdo,0,"",tiec);//登录
			// /CP6J5TRE000187VI.html
			String borderId = newsId.substring(1, newsId.indexOf("."));//"C1HBF6HB00014PRF";
			Map<String,String> map = getParams(borderId,taskdo.getAddress(),mapcookie.get("cookie"));//获得参数和新cookie
			
			String params = "board="+boardId+
					"&content="+URLEncoder.encode(taskdo.getCorpus(), "utf-8")+
					"&ntoken="+map.get("ntoken")+
					"&parentId=";
			System.out.println("params:"+params);
			URL upT = new URL("http://comment.news.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads/"+borderId+"/comments?ibc=newspc");
			HttpURLConnection cpT = (HttpURLConnection) upT.openConnection();
			cpT.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			cpT.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			cpT.addRequestProperty("Connection", "keep-alive");
			cpT.addRequestProperty("Content-Length", String.valueOf(params.length()));
			cpT.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			cpT.addRequestProperty("Host", "comment.news.163.com");
			cpT.addRequestProperty("Referer", taskdo.getAddress());
			cpT.addRequestProperty("Cookie", map.get("cookie"));
			cpT.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:50.0) Gecko/20100101 Firefox/50.0");
			cpT.addRequestProperty("X-Requested-With", "XMLHttpRequest");
			cpT.setDoOutput(true);
			cpT.setDoInput(true);
			
			PrintWriter out = new PrintWriter(cpT.getOutputStream());
			out.print(params);
			out.flush();
			
			BufferedReader in =  new BufferedReader(new InputStreamReader(cpT.getInputStream(),"gb2312"));
			String line;
			String scsc="";
			while ((line = in.readLine()) != null) {
				scsc += line;
			    System.out.println("line ntoken=="+line);
			}
			String content = "失败";
			if(scsc.contains("201")){
				content = "";
				System.out.println("发表成功!");
			}
			MQSender.toMQ(taskdo,content);
		} catch (Exception e) {
			e.printStackTrace();
			MQSender.toMQ(taskdo,"失败!");
		}
	}
	public static Map<String,String> formatUrl(TaskGuideBean taskdo){
		Map<String,String> map = new HashMap<String,String>();
		String address = taskdo.getAddress();
		System.out.println("address=="+address);
		System.out.println("address=="+address);
		if(address.contains("comment.")){//评论页面
			String newsId = address.substring(address.lastIndexOf("/"));
			String m1 = address.substring(0, address.lastIndexOf("/"));
			int t1 = m1.lastIndexOf("/");
			String boardId = m1.substring(t1+1);
			map.put("boardId", boardId);
			map.put("address", address);
			map.put("newsId", newsId);
		}else{//新闻页
			//http://sdk.comment.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads/CNG3EBG9000189FH?ibc=jssdk&callback=tool1004129723668182531_1498116955413&_=1498116955414
			String newId = address.substring(address.lastIndexOf("/"), address.lastIndexOf("."));
			String newsId = address.substring(address.lastIndexOf("/"));
			try {
				URL url = new URL("http://sdk.comment.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads"+newId+"?ibc=jssdk&callback=tool1004129723668182531_"+new Date().getTime()+"&_="+new Date().getTime());
				URLConnection openConnection = url.openConnection();
				openConnection.connect();
				InputStream inputStream = openConnection.getInputStream();
				
				BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
				StringBuffer sb = new StringBuffer();
				String result;
				while((result = br.readLine())!=null){
					sb.append(br.readLine()).append("\n");
				}
				System.out.println("sb.toString()=="+sb.toString());
				result = sb.toString().replace(")", "").replace(";", "");
				System.out.println("result=="+result);
				JSONObject parseObject = JSON.parseObject(result);
				String boardId = parseObject.getString("boardId");
				address = "http://comment.news.163.com/"+boardId+newsId;
				map.put("boardId", boardId);
				map.put("address", address);
				map.put("newsId", newsId);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	public static Map<String,String> login(TaskGuideBean taskdo,int num,String cookiel,String tiec){
		System.out.println("login.cookiel=="+cookiel);
		Map<String,String> map = new HashMap<String,String>();
		try {
			String tips[] = taskdo.getNick().split("@");
			
			String tk = getTk(taskdo.getNick(),taskdo.getAddress(),cookiel,tiec);
			String pwd = taskdo.getPassword();
			JSONObject jb = new JSONObject();
			int pwdKeyUp = 1;
			if(num>0) pwdKeyUp=0;
			System.out.println("pwdKeyUp=="+pwdKeyUp);
			jb.put("d", 10);
			jb.put("domains", "");
			jb.put("l", 1);
			jb.put("pd", "tie");
			jb.put("pkid", "cGTVPrV");
			jb.put("pw", getPwd(pwd));
			jb.put("pwdKeyUp", pwdKeyUp);
			jb.put("t", new Date().getTime());
			jb.put("tk", tk);
			jb.put("topURL", taskdo.getAddress());
			jb.put("un", taskdo.getNick());
			String params = jb.toJSONString();
			System.out.println("params::"+params);
			
			String cktmp = "JSESSIONID-WYTXZDL=0Lt0wreg%2BpkbLlJGjXu629MtB11gF4U7ygwuqoIUT%2B%2BxkXmYXHoZlEf8LLroY%2Bu0O2mOsFejwX5hfuGXur765sRrU5eTmMQ6o1q5pbSl6fyjpKkO%2FQ91G1af5bTsbqzT%2FT3cDze9g2Uvg0xmi1yshE6Gi3wkbpLeh%2FAqyfPSNS14t8Gk%3A1496196703163;"+ 
					"ui_tip_cookie="+tips[0]+"%261%261%261%7C;"+
					"__utma=187553192.776028588.1476063854.1487641037.1487641037.1;"+ 
					"__utmz=187553192.1487641037.1.1.utmcsr=reg.163.com|utmccn=(referral)|utmcmd=referral|utmcct=/;"+ 
					"NTES_CMT_USER_INFO=458996%7C%E5%B0%91%E9%99%B5%E9%87%8E%E8%80%81%E6%9D%8E%E9%99%B5%E5%B0%91%7C%7Cfalse%7CbGlsZWkxOTI5QDE2My5jb20%3D;"+ 
					"__utmz=205185811.1489046228.3.1.utmcsr=comment.news.163.com|utmccn=(referral)|utmcmd=referral|utmcct=/news3_bbs/BSII26VE00014SEH.html; "+
					"UM_distinctid=15acbca9f8f16c-0272296afe370c8-79681735-15f900-15acbca9f90132; "+tiec;
					
			//E24A030AA5B375E1870FA6C163683C5938B52CB667FAE54057964D2C9E404E8162F447C78BF18F920FF494F6C4CB0972EB7618969209CA37A1DE9F9395800D61F6EBAE0ED2ACB8FEBF09F152A082E73B567A7F9E2E0649C965BA6F96C7B287CFCD9C1F88078EA8F6B7EFF6DC14F80A6B;
			map.put("cktmp", cktmp);
			URL upT = new URL("https://dl.reg.163.com/l");
			HttpURLConnection cpT = (HttpURLConnection) upT.openConnection();
			cpT.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			cpT.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			cpT.addRequestProperty("Connection", "keep-alive");
			cpT.addRequestProperty("Content-Type", "application/json");
			cpT.addRequestProperty("Host", "dl.reg.163.com");
			cpT.addRequestProperty("Referer", "https://dl.reg.163.com/src/mp-agent-finger.html?WEBZJVersion="+new Date().getTime()+"&pkid=cGTVPrV&product=tie");
			cpT.addRequestProperty("Cookie", cktmp);
			cpT.addRequestProperty("Upgrade-Insecure-Requests", "1");
			cpT.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:50.0) Gecko/20100101 Firefox/50.0");
			cpT.setDoOutput(true);
			cpT.setDoInput(true);
			
			PrintWriter out = new PrintWriter(cpT.getOutputStream());
			out.print(params);
			out.flush();
			
			BufferedReader in =  new BufferedReader(new InputStreamReader(cpT.getInputStream(),"gb2312"));
			String line;
			String scsc="";
            while ((line = in.readLine()) != null) {
            	scsc += line;
                System.out.println(line);
            }
            if(scsc.contains("441") && num<1){//需要验证码
            	getVcode(map,taskdo,0);
            	num++;
            	return login(taskdo,num,map.get("cookiel"),tiec);
            }
			Map<String, List<String>> m1 = cpT.getHeaderFields();
			System.out.println("m1.size()::"+m1.size());
			for(Map.Entry<String,List<String>> entry : m1.entrySet()){
				System.out.println("entry.getKey()::"+entry.getKey());
				if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
					String cookie = "";
					for(String value : entry.getValue()){
						cookie += value+";";
						System.out.println("value=="+value);
					}
					System.out.println("logincookie=="+cookie);
					map.put("cookie", cookie);
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	public static Map<String,String> getVcode(Map<String,String> map,TaskGuideBean taskdo,int numb){
		try {
			URL upT2 = new URL("https://dl.reg.163.com/cp?pd=tie&pkid=cGTVPrV&random="+new Date().getTime());
			HttpURLConnection cpT2 = (HttpURLConnection) upT2.openConnection();
			cpT2.addRequestProperty("Accept", "*/*");
			cpT2.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			cpT2.addRequestProperty("Connection", "keep-alive");
			cpT2.addRequestProperty("Host", "dl.reg.163.com");
			cpT2.addRequestProperty("Referer", "http://webzj.reg.163.com/v1.0.1/pub/index_dl.html?cd%3Dhttp%3A%2F%2Fimg1.cache.netease.com%2Ftie%2Fstatic%2F20161013%2F%26cf%3Dtie_urs_style.css&MGID=1496281060754.622&wdaId=&pkid=cGTVPrV&product=tie");
			cpT2.addRequestProperty("Cookie", map.get("cktmp"));
			cpT2.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:50.0) Gecko/20100101 Firefox/50.0");
			cpT2.connect();
			
			InputStream i211 = cpT2.getInputStream();
			byte[] bs = new byte[1024];
			int len;
			String imgPath = "c:\\wycode.jpg";
			String fPath = "c:\\";
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
			String randRom = RuoKuai.createByPostNew("3040",imgPath);
			System.out.println("result========================"+randRom);
			Map<String, List<String>> m1 = cpT2.getHeaderFields();
			System.out.println("m1.size()::"+m1.size());
			String cookiel = "";
			for(Map.Entry<String,List<String>> entry : m1.entrySet()){
				System.out.println("entry.getKey()::"+entry.getKey());
				if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
					for(String value : entry.getValue()){
						cookiel += value+";";
						System.out.println("cookiel=="+cookiel);
					}
				}
			}
			map.put("cookiel", cookiel);
			String params2 = "cap="+randRom+
			"&pd=tie&pkid=cGTVPrV"+
			"&un="+URLEncoder.encode(taskdo.getNick(), "utf-8")+
			"&topURL="+URLEncoder.encode(taskdo.getAddress(), "utf-8");
			System.out.println("codePas:"+params2);
			URL cpT3 = new URL("https://dl.reg.163.com/vfcp");
			HttpURLConnection upT3 = (HttpURLConnection) cpT3.openConnection();
			upT3.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			upT3.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			upT3.addRequestProperty("Connection", "keep-alive");
			upT3.addRequestProperty("Content-Length", String.valueOf(params2.length()));
			upT3.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			upT3.addRequestProperty("Host", "dl.reg.163.com");
			upT3.addRequestProperty("Referer", "https://dl.reg.163.com/src/mp-agent-finger.html?WEBZJVersion=1492501603093&pkid=cGTVPrV&product=tie");
			upT3.addRequestProperty("Cookie", map.get("cktmp") + cookiel);
			upT3.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:50.0) Gecko/20100101 Firefox/50.0");
			upT3.setDoOutput(true);
			upT3.setDoInput(true);
			
			PrintWriter out2 = new PrintWriter(upT3.getOutputStream());
			out2.print(params2);
			out2.flush();
			
			BufferedReader in2 =  new BufferedReader(new InputStreamReader(upT3.getInputStream(),"gb2312"));
			String line2;
			String scsc2="";
			while ((line2 = in2.readLine()) != null) {
				scsc2 += line2;
			    System.out.println("codeline=="+line2);
			    if(scsc2.contains("441") && numb<3){
			    	System.out.println("getVcode=="+numb);
			    	numb++;
			    	return getVcode(map,taskdo,numb);
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	//获得ntoken
	public static Map<String,String> getParams(String borderId,String address,String cookie){
		String ntoken = "";
		Map<String,String> map = new HashMap<String,String>();
		//http://comment.news.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads/C1HBF6HB00014PRF/comments/gentoken?ibc=newspc
		try {                //http://sdk.comment.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads/BSII26VE00014SEH/comments?ibc=jssdk&_=1499672321725
			URL upT = new URL("http://comment.news.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads/"+borderId+"/comments/gentoken?ibc=newspc");
			HttpURLConnection cpT = (HttpURLConnection) upT.openConnection();
			cpT.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			cpT.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			cpT.addRequestProperty("Connection", "keep-alive");
			cpT.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			cpT.addRequestProperty("Host", "comment.news.163.com");
			cpT.addRequestProperty("Referer", address);
			cpT.addRequestProperty("Cookie", cookie);
			cpT.addRequestProperty("X-Requested-With", "XMLHttpRequest");
			cpT.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:50.0) Gecko/20100101 Firefox/50.0");
			cpT.setDoOutput(true);
			cpT.setDoInput(true);
			
			PrintWriter out = new PrintWriter(cpT.getOutputStream());
			out.print("");
			out.flush();
			
			BufferedReader in =  new BufferedReader(new InputStreamReader(cpT.getInputStream(),"gb2312"));
			String line;
			String scsc="";
			while ((line = in.readLine()) != null) {
				scsc += line;
			    System.out.println("line ntoken=="+line);
			}
			JSONObject jb_topic = JSONObject.parseObject(scsc);
			ntoken = jb_topic.getString("gentoken");
			map.put("ntoken", ntoken);
			//cookie中添加WEB_TOKEN
			Map<String, List<String>> m1 = cpT.getHeaderFields();
			System.out.println("m1.size()::"+m1.size());
			for(Map.Entry<String,List<String>> entry : m1.entrySet()){
				System.out.println("entry.getKey()::"+entry.getKey());
				if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
					for(String value : entry.getValue()){
						System.out.println("newvalue=="+value);
						cookie += value;
					}
					System.out.println("cookie=="+cookie);
					map.put("cookie", cookie);
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	public static String getTk(String userid,String url,String cc,String tiec){
		String tk = "";
		//https://dl.reg.163.com/gt?un=lilei1929%40163.com&pkid=cGTVPrV&pd=tie&topURL=http%3A%2F%2Fcomment.news.163.com%2Fnews_guonei8_bbs%2FC1HBF6HB00014PRF.html&nocache=1495850984374
		try {
			String url_topic = "https://dl.reg.163.com/gt?un="+URLDecoder.decode(userid,"utf-8")+"&pkid=cGTVPrV&pd=tie&topURL="+URLDecoder.decode(url,"utf-8")+"&nocache="+new Date().getTime();
			URL up33 = new URL(url_topic);
			
			HttpURLConnection c31 = (HttpURLConnection) up33.openConnection();
//			c31.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			c31.addRequestProperty("Accept", "*/*");
//			c31.addRequestProperty("Accept-Encoding", "gzip, deflate, br");
			c31.addRequestProperty("Content-Type", "application/json");
			c31.addRequestProperty("Referer", "https://dl.reg.163.com/src/mp-agent-finger.html?WEBZJVersion=1499243813862&pkid=cGTVPrV&product=tie");
			c31.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			c31.addRequestProperty("Connection", "keep-alive");
			c31.addRequestProperty("Host", "dl.reg.163.com");
//			c31.addRequestProperty("Cookie", "l_s_tiecGTVPrV=E24A030AA5B375E1870FA6C163683C59E6C897A9EDC918198C675599A5A2AAE5CE81847B06170AF8F5C8B9FBE66DBF7AAE2CE44314477E019B34FB78BF4E91865754DC050E7BCFEC0E93EFCFF6645ACC9668111149F5050912F5AC6D687EF67888CD86E69AAB37573AFE3011B7E80F8A;" +
//					""+cc);
//			c31.addRequestProperty("Cookie", "l_s_tiecGTVPrV=E24A030AA5B375E1870FA6C163683C5938B52CB667FAE54057964D2C9E404E813EFA7014B72B63985B9D155CD89D5E1B6DCD4477C3AE18BEBFB153A348A2941F703F102BD86F480A2A2BDF11CC9E169E62F957E30A81C98685FDBEA47ACB0354CD9C1F88078EA8F6B7EFF6DC14F80A6B;" +
//					""+cc);
			System.out.println(cc);
			c31.addRequestProperty("Cookie", tiec +
					""+cc);
//			c31.addRequestProperty("Cookie", "vjuids=248e54134.15d2b4a8ec4.0.c916c8fb5c2b18; vjlast=1499669893.1499669893.30; _ntes_nnid=5a374f1f3d5c1a7348f5e244aca2948e,1499669892812; Province=04887; City=0471; _ntes_nuid=5a374f1f3d5c1a7348f5e244aca2948e; __s_=1; webzjcookiecheck=1; JSESSIONID-WYTXZDL=T6e2gqQ%2BA%5CFvc9tJhxNTSo0fTvIz9SNdc9BL1ArPEd19aH%5C%2BRgA9D4Pim%5C1veU%2FhyxFH6Bmm967UnOvIyswq%2FqOc%2Ba7T8FsZTQeI7Aqt2nx23wiqRgebAB0VPg%2F8eh4Rtie4za7I%2Fk7EVD3xT%2FIMqkwXkqRN8HLLtr5lvMn6Ur%5C2LZ%2FL%3A1499670495459; _ihtxzdilxldP8_=30; l_s_tiecGTVPrV=E24A030AA5B375E1870FA6C163683C5938B52CB667FAE54057964D2C9E404E81662CC7E1C38F69029AB5076122B80494D47DE9AEE0559C94A4C5858ED4BFA7D137B273E422D6122011825A2DC2F7A64D67384DCB0F4C58725F82E16B2CD076F8; UM_distinctid=15d2b506c5d571-096c49537d5e4b-12646f4a-1fa400-15d2b506c5e3f8; ne_analysis_trace_id=1499670277853; vinfo_n_f_l_n3=1635bed623a7d134.1.0.1499670277910.0.1499670338425; s_n_f_l_n3=1635bed623a7d1341499670277910; P_INFO=m__6_3xg@163.com|1499670129|2|tie|00&99|bej&1498554743&tie#bej&null#10#0#0|&0||m__6_3xg@163.com; NTES_CMT_USER_INFO=68497815%7Cm__6_3xg%7C%7Cfalse%7CbV9fNl8zeGdAMTYzLmNvbQ%3D%3D; cm_newmsg=user%3Dm__6_3xg%26new%3D-1%26total%3D-1; SID=e94107c1-e011-4e54-bd57-f52854df37c8");
			c31.addRequestProperty("Upgrade-Insecure-Requests", "1");
			c31.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:50.0) Gecko/20100101 Firefox/50.0");
			c31.connect();
			BufferedReader in =  new BufferedReader(new InputStreamReader(c31.getInputStream(),"gb2312"));
			String line;
			String scsc="";
            while ((line = in.readLine()) != null) {
            	scsc += line;
                System.out.println(line);
            }
			JSONObject jb_topic = JSONObject.parseObject(scsc);
			System.out.println(jb_topic.getString("tk"));
			return jb_topic.getString("tk");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tk;
	}
	public static String  getPwd(String pw){
		 // 使用HtmlUnitDriver 是不需要 安装 浏览器 和 驱动支持  
		String newpwd = "";
		try {
			WebElement search;
			HtmlUnitDriver webDriver = new HtmlUnitDriver();  
			webDriver.setJavascriptEnabled(true); // 设置支持 js脚本解析 ，是不是跟 安卓的 webview设置很像？  
			webDriver.get("file:///C:/163pwd/reg.html?pwd="+pw);  
			search = webDriver.findElement(By.id("lll"));  
			System.out.println();
			System.out.println();
			System.out.println("pwd::"+search.getText());
			newpwd = search.getText();
			System.out.println();
			System.out.println();
			DriverGet.quit(webDriver);
		} catch (RuntimeException e) {
			System.out.println("加密出错");
		}  
		return newpwd;
	}
	public String formatString(String str) {
        if (str != null) {            
        	try {                
        		str = new String(str.getBytes("ISO-8859-1"), "UTF-8");            
        		} catch (Exception e) {}        
        		}
        return str;    
    }
	
	public static String getTiec(String address){
		
		try {
			
		String preUrl = "https://dl.reg.163.com/ini?pd=tie&pkid=cGTVPrV&pkht=tie.163.com&topURL="+address+"&nocache="+new Date().getTime();
		
		URL url = new URL(preUrl);
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		
		String headerField = openConnection.getHeaderField("Set-Cookie");
		openConnection.connect();
		System.out.println("headerField=="+headerField);
		String tiec = headerField.substring(0, headerField.indexOf(";")+1);
		System.out.println("tiec=="+tiec);
		return tiec;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
