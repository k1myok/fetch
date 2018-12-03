package com.longriver.netpro.fetchScript;
import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
import java.net.HttpURLConnection;
import java.net.URL;  
import java.net.URLConnection;  
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;  
import java.util.Map;  
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.fetchScript.util.JdbcDistr;
import com.longriver.netpro.util.DateUtil;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.FetchTaskRiceverBean;
  
public class ToutiaoNewFetch {
	private static Logger logger = Logger.getLogger(ToutiaoNewFetch.class);
	 public static void main(String[] args) {  
		 FetchTaskRiceverBean bb = new FetchTaskRiceverBean();
		 bb.setUrl("http://www.toutiao.com/a6466594468135961102/");
		 bb.setUrl("http://www.toutiao.com/a6466744091248951821/");
		 bb.setUrl("http://toutiao.com/a6301622097621893377/#p=1");
//		 getGroupIdandItemsId(bb.getUrl());
		 toRun(bb);
	}  
	  
	 public static void toRun(FetchTaskRiceverBean fetchTaskRicever){
		 	logger.info("ToutiaoNewFetch");
		 	System.out.println("今日头条采集");
		 	String url=fetchTaskRicever.getUrl();//"http://toutiao.com/a6301622097621893377/#p=1";
		 	Map<String,String> map = getGroupIdandItemsId(url);
		 	if(map.size()==0){
		 		System.out.println("解析item_id失败!");return;
		 	}
		 	String group_id = map.get("group_id");
		 	String item_id = map.get("item_id");
		    String cmtUrl = "http://www.toutiao.com/api/comment/list/?" +
		    		"group_id=" +group_id+
		    		"&item_id=" +item_id+
		    		"&offset=0&count=10";  
	        String cmt = openUrl(cmtUrl);
	        parseCmt(cmt,fetchTaskRicever);
	 }
	 
	@SuppressWarnings("unchecked")
	public static void parseCmt(String cmt,FetchTaskRiceverBean fetchTaskRicever){
		List<Map> mapList = new ArrayList<Map>();
		JSONObject jb = JSONObject.parseObject(cmt);
		JSONObject jbdata = jb.getJSONObject("data");
		JSONArray jr = jbdata.getJSONArray("comments");
		Iterator<Object> it = jr.iterator();
		List<FetchTaskRiceverBean> listto = new ArrayList<FetchTaskRiceverBean>();
		while (it.hasNext()){
			JSONObject jbsub = (JSONObject) it.next();
			String content = StringUtil.decodeUnicode(jbsub.getString("text"));
			String postTime = DateUtil.long2Date(jbsub.getString("create_time"));
			String nick = StringUtil.decodeUnicode(jbsub.getJSONObject("user").getString("name"));
			System.out.println("nick="+nick+", postTime="+postTime+", content="+content);
			Map map = new HashMap();
	    	map.put("nick", nick);
	    	map.put("postTime", postTime);
	    	map.put("content", content);
	    	mapList.add(map);
	    	
	    	FetchTaskRiceverBean d = new FetchTaskRiceverBean();
    		d.setAn(nick);
    		d.setContent(content);
    		d.setPt(postTime); 
    		listto.add(d);
		}
		JdbcDistr.distr(fetchTaskRicever,mapList,listto);
	}
    public static String openUrl(String currentUrl) {  
        InputStream is = null;  
        BufferedReader br = null;  
        URL url;  
        StringBuffer html = new StringBuffer();  
        try {  
            url = new URL(currentUrl);  
            URLConnection conn = url.openConnection();  
            conn.setReadTimeout(5000);  
            conn.connect();  
            logger.info("openUrl---------22222222222");
            is = conn.getInputStream();  
            br = new BufferedReader(new InputStreamReader(is,"utf-8"));  
            String str;  
            while (null != (str = br.readLine())) {  
                html.append(str);  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if (br != null) {  
                try {  
                    br.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
            if (is != null) {  
                try {
                    is.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
  
        }  
        return html.toString();  
    }
    public static Map<String,String> getGroupIdandItemsId(String addr){
    	Map<String,String> map = new HashMap<String,String>();
    	try {
			URL u45 = new URL(addr);
			HttpURLConnection c45 = (HttpURLConnection) u45.openConnection();
			c45.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			c45.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			c45.addRequestProperty("Cache-Control", "max-age=0");
			c45.addRequestProperty("Connection", "keep-alive");
			c45.addRequestProperty("Host", "www.toutiao.com");
			c45.addRequestProperty("Upgrade-Insecure-Requests", "1");
			c45.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			c45.connect();
			InputStream i45 = c45.getInputStream();
	        Scanner s45 = new Scanner(i45, "utf-8");
	        StringBuffer sb = new StringBuffer();
	        while(s45.hasNext()){
	        	String scsc = s45.nextLine();
	        	System.out.println(scsc);
	        	sb.append(scsc);
	        }
	        String resp = sb.toString();
	        int ind = resp.indexOf("commentInfo:");
	        resp = resp.substring(ind);
	        int ind2 = resp.indexOf("{");
	        int ind3 = resp.indexOf("}");
	        resp = resp.substring(ind2, ind3+1);
	        int tow = resp.indexOf(",", resp.indexOf(",")+1);
//		    System.out.println("resp1=="+resp);
	        resp = resp.substring(0, tow)+"}";
		    System.out.println("resp2=="+resp);
		    if(resp.contains("groupId")){
		    	int dd1 = resp.indexOf("groupId");
		    	int dd2 = resp.indexOf(",");
		    	String groupId = resp.substring(dd1+8, dd2).replaceAll("'", "").trim();
		    	resp = resp.substring(dd2+1);
		    	int dd3 = resp.indexOf("itemId");
		    	int towdian = resp.indexOf("'", resp.indexOf("'")+1);
		    	String itemId = resp.substring(dd3+7, towdian).replaceAll("'", "").trim();
		    	System.out.println("groupId=="+groupId);
		    	System.out.println("itemId==="+itemId);
		    	map.put("group_id", groupId);
		        map.put("item_id", itemId);
		    }else{//group_id
		    	JSONObject jsStr = JSONObject.parseObject(resp);
		        String group_id = (String)jsStr.getString("group_id");
		        String item_id = (String)jsStr.getString("item_id");
		        System.out.println("group_id=="+group_id);
		        System.out.println("item_id==="+item_id);
		        map.put("group_id", group_id);
		        map.put("item_id", item_id);
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
    }
}
