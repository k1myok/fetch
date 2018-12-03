package com.longriver.netpro.fetchScript;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;

import com.longriver.netpro.common.sina.SinaIdMidConverter;
import com.longriver.netpro.util.DateUtil;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.controller.InterfaceCJ;
import com.longriver.netpro.webview.entity.FetchTaskRiceverBean;

public class SinaZFetch {
	
	private static Logger logger = Logger.getLogger(SinaZFetch.class);
	public static String cookie = "SUB=_2AkMu56BQf8NxqwJRmP0WyGrlaIlzyg_EieKYu1GLJRMxHRl-yT83qhcjtRClTBrheXgz_g93mXDgsXRSN4bIyw..";
	public static String ctime_aboce = "2017-09-14 12:00:00";
	public static void main(String agrs[]){
		try{
			FetchTaskRiceverBean f = new FetchTaskRiceverBean();
			f.setCount("1");
			f.setUrl("2418432711"); //南昌发布
//			f.setUrl("27509527"); //uid不同,参数Pl_Official_MyProfileFeed__后面的数字会变
			sina(f);
//			getCookie();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void sina(FetchTaskRiceverBean f) throws Exception{
		//每三天获得一次cookie
		long days = DateUtil.getDateTimeMin(ctime_aboce,DateUtil.getCurrentTime(),"day");
 	    if(cookie.equals("") || days>3){
 	    	ctime_aboce = DateUtil.getNowDate(0)+" 12:00:00";
 	    	cookie = getCookie();
 	    	System.out.println("非登录获得cookie");
 	    }else{
 	    	System.out.println("直接获得cookie");
 	    }
 	    
		String addr = "https://weibo.com/u/"+f.getUrl();
		
        URL u45 = new URL(addr);
        HttpURLConnection c45 = (HttpURLConnection) u45.openConnection();
        c45.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        c45.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
        c45.addRequestProperty("Connection", "keep-alive");
        c45.addRequestProperty("Cookie", cookie);
        c45.addRequestProperty("Host", "weibo.com");
        c45.setConnectTimeout(1000*30);
        c45.setReadTimeout(1000*30);
        c45.connect();
        InputStream i45 = c45.getInputStream();
        Scanner s45 = new Scanner(i45, "utf-8");
        String pdetail = ""; //后面要用的id
        while(s45.hasNext()){
        	String scsc = s45.nextLine();
        	if(scsc.indexOf("$CONFIG['page_id']") > -1){
        		pdetail = scsc.substring(scsc.indexOf("$CONFIG['page_id']")).replace("$CONFIG['page_id']", "");
        		pdetail = pdetail.substring(pdetail.indexOf("'") + 1);
        		pdetail = pdetail.substring(0, pdetail.indexOf("'"));
        	}
        }
        
        System.out.println("pdetail=="+pdetail);
        String m = SinaIdMidConverter.midToId(f.getUrl());
        System.out.println("m=="+m);
        //t 页数
        if(f.getCount()==null || f.getCount().equals("")) f.setCount("2");
        int pages = Integer.parseInt(f.getCount());
		for(int t=0;t<pages;t++){
			getZhiFas(cookie,pdetail,f,t);
		}
        
	}
	public static void getZhiFas(String cookie,String pdetail,FetchTaskRiceverBean f,int page){
		try{
			URL uf1 = new URL("https://weibo.com/u/"+f.getUrl()+"?" +
					"pids=Pl_Official_MyProfileFeed__"+24 + //24是可变的
					"&is_search=0" +
					"&visible=0&is_all=1&is_tag=0&profile_ftype=1" +
					"&page=" +page+
					"&ajaxpagelet=1&ajaxpagelet_v6=1" +
					"&__ref=/u/"+f.getUrl()+
					"&_t=FM_"+new Date().getTime());
		    HttpURLConnection cf1 = (HttpURLConnection) uf1.openConnection();
		    cf1.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		    cf1.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		    cf1.addRequestProperty("Connection", "keep-alive");
		    cf1.addRequestProperty("Cookie", cookie);
		    cf1.addRequestProperty("Host", "weibo.com");
		    cf1.addRequestProperty("Referer", "https://weibo.com/u/"+f.getUrl());
		    cf1.addRequestProperty("Upgrade-Insecure-Requests", "1");
		    cf1.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:55.0) Gecko/20100101 Firefox/55.0");
		    cf1.setConnectTimeout(1000*30);
		    cf1.setReadTimeout(1000*30);
		    cf1.connect();
			InputStream iff1 = cf1.getInputStream();
			Scanner sf1 = new Scanner(iff1,"utf-8");
			StringBuffer ct = new StringBuffer();
			while(sf1.hasNext()){
				String scsc = sf1.nextLine();
				System.out.println("scsc=="+scsc);
				scsc = scsc.substring(scsc.indexOf("\"html\":\"")+8);
				ct.append(scsc);
			}
			for(int j=1;j<=2;j++){
				URL uf = new URL("https://weibo.com/p/aj/v6/mblog/mbloglist?" +
						"ajwvr=6&domain=100106&is_search=0&visible=0&is_all=1" +
						"&is_tag=0&profile_ftype=1" +
						"&page="+page +
						"&pagebar=" +(j-1)+
						"&pl_name=Pl_Official_MyProfileFeed__24" +
						"&id=" +pdetail+
						"&script_uri=/u/"+f.getUrl()+
						"&feed_type=0" +
						"&pre_page=2&domain_op=100106" +
						"&__rnd="+new Date().getTime());
				HttpURLConnection cf= (HttpURLConnection) uf.openConnection();
				cf.addRequestProperty("Host", "weibo.com");
				cf.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
				cf.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
				cf.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
				cf.addRequestProperty("Cookie", cookie);
				cf.addRequestProperty("Connection", "keep-alive");
				cf.setConnectTimeout(1000*30);
			    cf.setReadTimeout(1000*30);
			    cf.connect();
				 
				InputStream iff = cf.getInputStream();
				Scanner sf = new Scanner(iff);
				//String mid = "";
				while(sf.hasNext()){
					String scsc = sf.nextLine();
					System.out.println("scsc=="+scsc);
					scsc = scsc.substring(scsc.indexOf("\"data\":\"")+8);
					ct.append(scsc);
				}
			}
			//System.out.println(ct);
			parseHtmlComment(ct.toString(),f.getTaskid(),f.getUrl(),f);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public static String getCookie(){
		String cookie = "";
		try {
			URL uf2 = new URL("https://passport.weibo.com/visitor/visitor?a=incarnate&t=JVgfdUVWHpsfTtQUR3lG%2Br58v38cJiRC1oZDPysvp00%3D&w=2&c=095&gc=&cb=cross_domain&from=weibo&_rand=0.502839476801455");
			//_FakeX509TrustManager.allowAllSSL();
			HttpURLConnection cf2 = (HttpURLConnection) uf2.openConnection();
			cf2.addRequestProperty("Host", "passport.weibo.com");
			cf2.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36");
			cf2.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			cf2.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
			cf2.setConnectTimeout(1000*30);
		    cf2.setReadTimeout(1000*30);
			cf2.connect();
			Map<String, List<String>> headers2 = cf2.getHeaderFields(); 
			for(Map.Entry<String,List<String>> entry : headers2.entrySet()){
			    if(entry.getKey() != null && (entry.getKey().indexOf("Set-Cookie") > -1)){
			        for(String value : entry.getValue()){
			        	if(value.contains("SUB="))
			        		cookie += cookie + value.substring(0, value.indexOf(";") + 1);
			        }  
			    }
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(cookie);
		return cookie;
	}
	/**
	 * 发送到mq
	 * @param listto
	 * @param fetchTaskRicever
	 */
	public static void toExec(List<FetchTaskRiceverBean> listto,FetchTaskRiceverBean fetchTaskRicever){
		logger.info("直发采集返回结果到mq------------------------------------");
		logger.info("直发采集返回结果到mq------------------------------------");
		fetchTaskRicever.setRlist(listto);
		
    	InterfaceCJ.getResultJava(fetchTaskRicever);
	}
	
	public static void parseHtmlComment(String html,String tpid,String id,FetchTaskRiceverBean f){
		String  testTemp = html;
		System.out.println(testTemp);
		String  test = testTemp;
		String  s = "WB_from S_txt2";
		 int count = 0;  
	        for(int i=0; i<test.length();){  
	            int c = -1;  
	            c = test.indexOf(s);  
	            if(c != -1){  
	            	test = test.substring(c+ s.length());  
	                count ++;  
	            }else {  
	                break;  
	            }  
	        }  
	        List<Map> list = new ArrayList<Map>();
	        List<FetchTaskRiceverBean> listto = new ArrayList<FetchTaskRiceverBean>();
	        for(int i=0;i<count;i++){
					try {
						Map testMap = new HashMap();
						int indd = html.indexOf("WB_from S_txt2");
						if(indd==-1) continue;
						String temp = html.substring(indd);
						if(temp.contains("WB_feed_expand")){
							System.out.println("转发");
						}
						temp =temp.substring(temp.indexOf("href")+7);
						String href = temp.substring(0,temp.indexOf("\""));
						temp =temp.substring(temp.indexOf("title")+8);
						String publishTiem = temp.substring(0,temp.indexOf("\""));
						publishTiem = publishTiem.replace("\\", "");
						temp =temp.substring(temp.indexOf("WB_text W_f14")+8);
						temp =temp.substring(temp.indexOf(">")+1);
						String content = temp.substring(0,temp.indexOf("<\\/div>"));
						content = StringUtil.filterHTMl(StringUtil.decodeUnicode(content));
						System.out.println(content);
						System.out.println(count);
						System.out.println(publishTiem);
						System.out.println(i);
						temp =temp.substring(temp.indexOf("allowForward"));
			    		temp =temp.substring(temp.indexOf("&#xe607;")+8);
			    		temp =temp.substring(temp.indexOf("<em>")+4);
			    		String zhuafaNum = temp.substring(0,temp.indexOf("<"));
			    		temp =temp.substring(temp.indexOf("&#xe608;")+8);
			    		temp =temp.substring(temp.indexOf("<em>")+4);
			    		String plNum =  temp.substring(0,temp.indexOf("<"));
			    		String praiseNum = "";
						logger.info("https://weibo.com"+href);
						logger.info("https://weibo.com"+StringEscapeUtils.unescapeJava(href));
						
						html = temp;
						if(href.contains(id)){
							FetchTaskRiceverBean d = new FetchTaskRiceverBean();
							d.setUrl("https://weibo.com"+StringEscapeUtils.unescapeJava(href));
							d.setContent(content.trim());
							d.setPt(publishTiem+":00"); 
							d.setCts(plNum);
							d.setFds(zhuafaNum);
							d.setAc(praiseNum);
							listto.add(d);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
	    		
	        }
	        toExec(listto,f);
	        
	}

}
