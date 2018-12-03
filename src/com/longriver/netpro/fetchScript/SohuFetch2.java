package com.longriver.netpro.fetchScript;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.fetchScript.util.DownloadMessageUtils;
import com.longriver.netpro.fetchScript.util.JdbcDistr;
import com.longriver.netpro.util.DateUtil;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.FetchTaskRiceverBean;

/**
 * 搜狐评论抓取
 * @author rhy
 * @2017-5-18 下午1:39:31
 * @version v1.0
 */
public class SohuFetch2 {
	//发帖需要topic_id,media_id,topic_title 获得topic_id需要 topic_source_id
		public static void getCommentParams(FetchTaskRiceverBean ftrb){
			
			try {
			String url = URLDecoder.decode(ftrb.getUrl(),"utf-8");
			Map<String,String> map = new HashMap<String,String>();
				Document doc =Jsoup.connect(url).get();
				String tmp = doc.toString();
				tmp = tmp.substring(tmp.indexOf("window.sohu_mp.article("));
				int first = tmp.indexOf("({");
				int last = tmp.indexOf("});");
				tmp = tmp.substring(first+1, last+1);
				System.out.println("tmp=="+tmp);
				JSONObject jb = JSONObject.parseObject(tmp);
				map.put("news_id", jb.getString("news_id"));
				map.put("cms_id", jb.getString("cms_id"));
				map.put("media_id", jb.getString("media_id"));
				map.put("title", jb.getString("title"));
				
				//获得topic_id
				String url_topic = "http://changyan.sohu.com/api/2/topic/count?client_id=cyqemw6s1&topic_source_id="+jb.getString("cms_id")+"&callback=jQuery112407337340160700102_"+new Date().getTime()+"&_="+new Date().getTime();
				URL up33 = new URL(url_topic);
				HttpURLConnection c31 = (HttpURLConnection) up33.openConnection();
				c31.addRequestProperty("Host", "changyan.sohu.com");
				c31.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0");
				c31.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
				c31.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
				c31.addRequestProperty("Referer", url);
				c31.connect();
				InputStream i11 = c31.getInputStream();
				Scanner s11 = new Scanner(i11, "gb2312");
				String scsc = "";
				while(s11.hasNext()){
					scsc += s11.nextLine();
					System.out.println("scsc=="+scsc);
				}
				int jsons = scsc.lastIndexOf("{");
				int jsone = scsc.indexOf("}");
				scsc = scsc.substring(jsons, jsone+1);
				JSONObject jb_topic = JSONObject.parseObject(scsc);
				map.put("topic_id", jb_topic.getString("id"));
				System.out.println("topic_id=="+jb_topic.getString("id"));
				
				getSohuComment(ftrb,jb_topic.getString("id"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		/**
		 * 得到评论信息
		 * @param topic_id
		 */
		@SuppressWarnings("rawtypes")
		private static void getSohuComment(FetchTaskRiceverBean ftrb,String topic_id) {
			
			//http://apiv2.sohu.com/api/comment/list?callback=jQuery112409024685600306839_1495085611921&page_size=10&topic_id=3092815748&page_no=3&_=1495085611997
			for(int page_no=1;page_no<50;page_no++){
				if(page_no==1)JdbcDistr.hasInfo(ftrb);
				String url = "http://apiv2.sohu.com/api/comment/list?callback=jQuery112409024685600306839_"+new Date().getTime()+"&page_size=10&topic_id="+topic_id+"&page_no="+page_no+"&_="+ new Date().getTime();
	 			String htmlAll = DownloadMessageUtils.Download(url);//抓取数据
	 			htmlAll = htmlAll.substring(htmlAll.indexOf("(")+1,htmlAll.lastIndexOf(")"));
				JSONObject parseObject = JSONObject.parseObject(htmlAll);
				String string = parseObject.getString("jsonObject");
				
				JSONObject parseObject2 = JSONObject.parseObject(string);
				String st = parseObject2.getString("comments");
				JSONArray parseArray = JSONObject.parseArray(st);
				List<FetchTaskRiceverBean> list = new ArrayList<FetchTaskRiceverBean>();
				List<Map> mapList = new ArrayList<Map>();
				for(int j=0;j<parseArray.size();j++){
					JSONObject jsonObject = parseArray.getJSONObject(j);
					String content = jsonObject.getString("content");
					if(StringUtils.isBlank(content)){
						break;
					}
					getChildrenComment(mapList,list,ftrb,jsonObject);
					System.out.println("mapList.size()=="+mapList.size());
				}
				System.out.println("mapList.size()=="+mapList.size());
				JdbcDistr.distr(ftrb,mapList,list);
			}
		}
		/**
		 * 抓取子评论
		 */
		@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
		public static void getChildrenComment(List<Map> mapList,List<FetchTaskRiceverBean> list,FetchTaskRiceverBean ftrb,JSONObject jsonObject){
			
			String content = jsonObject.getString("content");
			String floor_count = jsonObject.getString("floor_count");
			String create_time = DateUtil.long2Date2(jsonObject.getString("create_time").trim());
			String passport = jsonObject.getString("passport");
			JSONObject parseObject3 = JSONObject.parseObject(passport);
			String nickname = parseObject3.getString("nickname");
			System.out.println("create_time=="+create_time);
			System.out.println("nickname=="+nickname);
			System.out.println("content=="+content);
		
			FetchTaskRiceverBean d = new FetchTaskRiceverBean();
    		d.setAn(nickname);
    		d.setContent(content);
    		d.setPt(create_time);
    		list.add(d);
    		
			Map map = new HashMap();
    		map.put("urlid", ftrb.getId());
    		map.put("content", content);
    		map.put("postTime", create_time);
    		mapList.add(map);
    		
		}
		
		public static void main(String[] args) {
			FetchTaskRiceverBean b=new FetchTaskRiceverBean();
			b.setUrl("http://www.sohu.com/a/141143027_585752?qq-pf-to=pcqq.c2c");
			try {
				getCommentParams(b);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}
