package com.longriver.netpro.fetchScript;

import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
import java.net.URL;  
import java.net.URLConnection;  
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;  
import java.util.Map;  
import java.util.regex.Matcher;  
import java.util.regex.Pattern;  

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.longriver.netpro.fetchScript.util.Jdbc2Mysql;
import com.longriver.netpro.fetchScript.util.JdbcDistr;
import com.longriver.netpro.util.DateUtil;
import com.longriver.netpro.util.UrlUtil;
import com.longriver.netpro.webview.entity.FetchTaskRiceverBean;
  
public class QQNewCommentFetch {
	
	private static Logger logger = Logger.getLogger(QQNewCommentFetch.class);
	
	 public static void main(String[] args) {
			FetchTaskRiceverBean f = new FetchTaskRiceverBean();
//		 	f.setUrl("http://cq.qq.com/a/20160706/009433.htm");
//		 	f.setUrl("http://coral.qq.com/2124496173");
		 	f.setUrl("http://coral.qq.com/1972026188");
		 	try {
				toRun(f);
			} catch (Exception e) {
				e.printStackTrace();
			}
		 }  
	 
	public static void toRun(FetchTaskRiceverBean fetchTaskRicever) throws Exception {
		String url = fetchTaskRicever.getUrl();
		url = UrlUtil.getQQCommentUrl(url);
		//新闻正文正则  
        String regex1 = "<div id=\"Cnt-Main-Article-QQ\" bossZone=\"content\">([\\d\\D]*?)</div>";  
        //评论ID正则  
        String regex2 = "cmt_id = ([\\d]*?);";  
        //获取网页源代码  
        String html = openUrl(url,"gb2312");  
        //获取新闻正则  
        String content = getContent(regex1,html);  
        //获取评论ID  
        String cmtId = getContent(regex2,html);  
        System.out.println(cmtId);  
          
        //拼接评论地址  
        String cmtUrl = "http://coral.qq.com/article/"+cmtId+"/comment?commentid=0&reqnum=50";  
        String cmt = openUrl(cmtUrl,"gb2312");  
  
        JSONObject jsonMap = new JSONObject();  
        Map map2 = jsonMap.fromObject(cmt);  
        Map<String,List> data = (Map)map2.get("data");  
        List<Map<String,Object>> comments = data.get("commentid");  
        List<Map> mapList = new ArrayList<Map>();
        List<FetchTaskRiceverBean> list = new ArrayList<FetchTaskRiceverBean>();
        for(Map<String,Object> m : comments){  
            String cmtContent = (String)m.get("content"); //评论  
            System.out.println(cmtContent);
            System.out.println(m.get("time"));
            JSONObject  userinfo = (JSONObject)m.get("userinfo"); 
            String nick = (String)userinfo.get("nick");
            Map map = new HashMap();
    		map.put("content", cmtContent);
    		map.put("nick", nick);
    		map.put("urlid", fetchTaskRicever.getId());
    		map.put("postTime", DateUtil.mill2Date(Long.valueOf(m.get("time").toString())));
    		System.out.println(nick+"----"+DateUtil.mill2Date(Long.valueOf(m.get("time").toString())));
    		mapList.add(map);
    		
    		FetchTaskRiceverBean d = new FetchTaskRiceverBean();
    		d.setAn(nick);
    		d.setContent(cmtContent);
    		d.setPt(DateUtil.mill2Date(Long.valueOf(m.get("time").toString())));
    		list.add(d);
        }
        JdbcDistr.hasInfo(fetchTaskRicever);
        JdbcDistr.distr(fetchTaskRicever,mapList,list);
	}
	
	
	    /** 
	     * 访问url返回url的html代码 
	     */  
	    public static String openUrl(String currentUrl,String charset) {  
	        InputStream is = null;  
	        BufferedReader br = null;  
	        URL url;  
	        StringBuffer html = new StringBuffer();  
	        try {  
	            url = new URL(currentUrl);  
	            URLConnection conn = url.openConnection();  
	            conn.setReadTimeout(5000);  
	            conn.connect();  
	            is = conn.getInputStream();  
	            br = new BufferedReader(new InputStreamReader(is,charset));  
	            String str;  
	            while (null != (str = br.readLine())) {  
	                html.append(str).append("\n");
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
	      
	    private static String getContent(String regex,String text) {  
	        String content = "";  
	        Pattern pattern = Pattern.compile(regex);  
	        Matcher matcher = pattern.matcher(text);  
	        while(matcher.find()) {  
	            content = matcher.group(1).toString();  
	        }  
	        return content;  
	    }  
}
