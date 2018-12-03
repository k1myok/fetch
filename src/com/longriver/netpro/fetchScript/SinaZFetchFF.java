package com.longriver.netpro.fetchScript;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;

import com.longriver.netpro.common.sina.SData;
import com.longriver.netpro.common.sina.SinaIdMidConverter;
import com.longriver.netpro.common.sohu._FakeX509TrustManager;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.controller.InterfaceCJ;
import com.longriver.netpro.webview.entity.FetchTaskRiceverBean;

public class SinaZFetchFF {
	
	private static Logger logger = Logger.getLogger(SinaZFetchFF.class);
	
	public static void main(String agrs[]){
		try{
			FetchTaskRiceverBean f = new FetchTaskRiceverBean();
			f.setCount("2");
			f.setUrl("5327146193");
			sina(f);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void sina(FetchTaskRiceverBean f) throws Exception{
		String id = "";
		SData data =new SData();
		String userId = "lilei1929@163.com";
		String pwd = "lilei516688..";
		if(f.getUname()!=null && !f.getUname().equals("")){
			userId = f.getUname();
			pwd = f.getUpwd();
			System.out.println("从前台发送过来采集用户名密码uname:"+userId+"pwd:"+pwd);
		}else{
			System.out.println("写死的用户名密码");
		}
		
		data.put("user_account_id", userId);
		data.put("user_account_pw", pwd);
		URL uf2 = new URL("https://passport.weibo.com/visitor/visitor?a=incarnate&t=JVgfdUVWHpsfTtQUR3lG%2Br58v38cJiRC1oZDPysvp00%3D&w=2&c=095&gc=&cb=cross_domain&from=weibo&_rand=0.502839476801455");
		String cookie = "";
		//_FakeX509TrustManager.allowAllSSL();
		
	    HttpURLConnection cf2 = (HttpURLConnection) uf2.openConnection();
	    cf2.addRequestProperty("Host", "passport.weibo.com");
	    cf2.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36");
	    cf2.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
	    cf2.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
	    
	    cf2.connect();
	    Map<String, List<String>> headers2 = cf2.getHeaderFields(); 
	    for(Map.Entry<String,List<String>> entry : headers2.entrySet()){
	        if(entry.getKey() != null && (entry.getKey().indexOf("Set-Cookie") > -1)){
	            for(String value : entry.getValue()){  
	            	cookie = cookie + value.substring(0, value.indexOf(";") + 1);
	            }  
	        }
	    }
    		String addr = "https://weibo.com/u/"+f.getUrl();
    		System.out.println(addr);
    		String tid = f.getTaskId();
    		String midEncode = addr.substring(addr.lastIndexOf("/")).replace("/", "");
    		if(midEncode.indexOf("?") > -1){
    			midEncode = midEncode.substring(0, midEncode.indexOf("?"));
    		}
    		if(midEncode.indexOf("#") > -1){
    			midEncode = midEncode.substring(0, midEncode.indexOf("#"));
    		}
    		
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
        //System.out.println(pdetail);
        
//        URL u451 = new URL("http://api.t.sina.com.cn/queryid.json?mid=" + midEncode + "&isBase62=1&type=1");
//        HttpURLConnection c451 = (HttpURLConnection) u451.openConnection();
//        c451.connect();
//        InputStream i451 = c451.getInputStream();
//        Scanner s451 = new Scanner(i451);
        String m = "";
//        while(s451.hasNext()){
//        	String scsc = s451.nextLine();
//        	if(scsc.indexOf("id") > -1){
//        		m = scsc.substring(scsc.indexOf("{\"id\":\"")).replace("{\"id\":\"", "");
//        		m = m.substring(0, m.indexOf("\""));
//        	}
//        }
        m = SinaIdMidConverter.midToId(midEncode);
        id =m;
        //t 页数
        if(f.getCount()==null || f.getCount().equals("")) f.setCount("2");
        int pages = Integer.parseInt(f.getCount());
		for(int t=0;t<pages;t++){
			try {
				getZhiFas(id,cookie,tid,midEncode,pdetail,f,t);
			} catch (Exception e) {
				logger.info("----------------------------------------------");
			}
		}
        
	}
	public static void getZhiFas(String id,String cookie,String tcpid,String midEncode,String pdetail,FetchTaskRiceverBean f,int t){
		try{
			//http://weibo.com/u/2638123791?pids=Pl_Official_MyProfileFeed__25&is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=3&ajaxpagelet=1&ajaxpagelet_v6=1&__ref=/u/2638123791&_t=FM_145386127441337
			URL uf1 = new URL("https://weibo.com/u/"+midEncode+"?pids=Pl_Official_MyProfileFeed__25&is_all=1&is_tag=0&profile_ftype=1&page="+t+"&ajaxpagelet=1&ajaxpagelet_v6=1&__ref=/u/"+midEncode);
			//&max_id=3923961391468663
		    HttpURLConnection cf1 = (HttpURLConnection) uf1.openConnection();
		    cf1.addRequestProperty("Host", "weibo.com");
		    cf1.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
		    cf1.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		    cf1.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		    cf1.addRequestProperty("Cookie", cookie);
		    cf1.addRequestProperty("Connection", "keep-alive");
		    cf1.connect();
			 
		    String pageNO = "";
			InputStream iff1 = cf1.getInputStream();
			Scanner sf1 = new Scanner(iff1,"utf-8");
			//String mid = "";
			StringBuffer ct = new StringBuffer();
			while(sf1.hasNext()){
				String scsc = sf1.nextLine();
				scsc = scsc.substring(scsc.indexOf("\"html\":\"")+8);
				ct.append(scsc);
				
			}
			
			//http://weibo.com/p/aj/v6/mblog/mbloglist?ajwvr=6&domain=100106&profile_ftype=1&is_all=1&pre_page=1&page=1&pagebar=0&filtered_min_id=&pl_name=Pl_Official_MyProfileFeed__25&id=1001062638123791&script_uri=/u/2638123791&feed_type=0&domain_op=100106&__rnd=145386134540
			for(int j=1;j<=2;j++){
				int k =0;
				if(j==1){
					k=0;
				}else
					k=1;
				URL uf = new URL("https://weibo.com/p/aj/v6/mblog/mbloglist?ajwvr=6&domain=100106&profile_ftype=1&is_all=1&pre_page="+t+"&page="+t+"&pagebar="+k+"&filtered_min_id=&pl_name=Pl_Official_MyProfileFeed__25&id="+pdetail+"&script_uri=/u/"+midEncode+"&feed_type=0&domain_op=100106&__rnd=145386134540");
				//System.out.println("http://weibo.com/p/aj/v6/mblog/mbloglist?ajwvr=6&domain=100106&profile_ftype=1&is_all=1&pre_page=1&page=1&pagebar="+k+"&filtered_min_id=&pl_name=Pl_Official_MyProfileFeed__25&id="+pdetail+"&script_uri=/u/"+midEncode+"&feed_type=0&domain_op=100106&__rnd=145386134540");
				HttpURLConnection cf= (HttpURLConnection) uf.openConnection();
				cf.addRequestProperty("Host", "weibo.com");
				cf.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
				cf.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
				cf.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
				cf.addRequestProperty("Cookie", cookie);
				cf.addRequestProperty("Connection", "keep-alive");
			    cf.connect();
				 
				InputStream iff = cf.getInputStream();
				Scanner sf = new Scanner(iff);
				//String mid = "";
				while(sf.hasNext()){
					String scsc = sf.nextLine();
					scsc = scsc.substring(scsc.indexOf("\"data\":\"")+8);
					ct.append(scsc);
				}
			}
			//System.out.println(ct);
			parseHtmlComment(ct.toString(),tcpid,midEncode,f);
		}catch(Exception e){
			e.printStackTrace();
		}
		
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
	                //i++;  
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
			    		
//			    		temp = temp.substring(temp.indexOf("W_icon icon_praised_b"));
//			    		System.out.println("praise=="+temp);
//			    		String praiseNum = temp.substring(temp.indexOf("<em>")+4,temp.indexOf("<\\/em>"));
			    		String praiseNum = "";
						logger.info("http://weibo.com"+href);
						logger.info("http://weibo.com"+StringEscapeUtils.unescapeJava(href));
						
						html = temp;
						if(href.contains(id)){
//							testMap.put("authorName", "http://weibo.com"+href);
//							testMap.put("postTime", publishTiem);
//							testMap.put("tpid", tpid);
//							testMap.put("cid", "2");
//							testMap.put("content", content);
//							
//							list.add(testMap);
//							
							FetchTaskRiceverBean d = new FetchTaskRiceverBean();
							d.setUrl("http://weibo.com"+StringEscapeUtils.unescapeJava(href));
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

