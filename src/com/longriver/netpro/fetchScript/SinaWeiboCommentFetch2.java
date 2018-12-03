package com.longriver.netpro.fetchScript;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.common.sina.SinaIdMidConverter;
import com.longriver.netpro.fetchScript.util.Jdbc2MysqlNewVersion;
import com.longriver.netpro.fetchScript.util.JdbcDistr;
import com.longriver.netpro.util.DateUtil;
import com.longriver.netpro.webview.entity.FetchTaskRiceverBean;

/**
 * 新浪微博语料抓取
 * @author rhy
 * @2017-10-18 上午10:50:30
 * @version v1.0
 */
public class SinaWeiboCommentFetch2 {

	public static void main(String[] args) {
		FetchTaskRiceverBean fetchTaskRicever  = new FetchTaskRiceverBean();
		fetchTaskRicever.setUrl("http://weibo.com/3535242572/FijkohzPI?refer_flag=1001030106_&type=comment");
		fetchTaskRicever.setUrl("https://weibo.com/2624755655/Fr01aCxfl?type=repost");
		fetchTaskRicever.setUrl("https://weibo.com/2960359192/FqWz87EeM?type=repost#_rnd1508391271970");
		fetchTaskRicever.setUrl("https://weibo.com/2687299131/Fr3ICxgcG");
		fetchTaskRicever.setUrl("https://weibo.com/2662279417/G4iFYk1Ry?type=repost");
		fetchTaskRicever.setUrl("https://weibo.com/3960011834/GcVB2BhJ8");
		fetchTaskRicever.setCid("1_networkLocal");
		getComment(fetchTaskRicever);
		
	}
	public static void getComment(FetchTaskRiceverBean fetchTaskRicever) {
		try{
			int ccid = 1;
			String cidAndMark[] = fetchTaskRicever.getCid().split("_");
			if(cidAndMark.length>1){ //不是本项目发送的,不接收
				if(cidAndMark[0].trim().equals("1") || fetchTaskRicever.getCid().equals("1")){
					ccid = 1; //转发
				}else{
					ccid = 2; //评论
				}
			}
		String urls = URLDecoder.decode(fetchTaskRicever.getUrl(),"utf-8");
		String newsId = getNewsIds(urls);
		int pageCount = 15;
			
		HtmlUnitDriver driver = new HtmlUnitDriver(true);
		String urlss = "";
		if(urls!= null && ccid==1){
			
			urlss = "https://weibo.com/aj/v6/mblog/info/big?ajwvr=&id="+newsId+"&max_id=&page=1&__rnd="+System.currentTimeMillis();
	//		https://weibo.com/aj/v6/mblog/info/big?ajwvr=6&id=4164349289184267&max_id=4164518453723486&page=2&__rnd=1508382332761
			pageCount = 20;
		}else{
			urlss = "https://weibo.com/aj/v6/comment/big?ajwvr=&id="+newsId+"&root_comment_max_id=&root_comment_max_id_type=&root_comment_ext_param=&page=1&filter=&sum_comment_number=&filter_tips_before=&from=singleWeiBo&__rnd="+System.currentTimeMillis();
	//		urls = "https://weibo.com/aj/v6/comment/big?ajwvr=6&id=4164349289184267&from=singleWeiBo&__rnd=1508377798373";
			pageCount = 15;
		}
		driver.get(urlss);
		
		Thread.sleep(15000);
		String pageSource = driver.getPageSource();
		System.out.println("-------------");
//		System.out.println(pageSource);
//		
		JSONObject parsePageSource = JSON.parseObject(pageSource);
		String code = parsePageSource.getString("code");
		
		
		if(code != null && "100000".equals(code)){
		String data = parsePageSource.getString("data");
		JSONObject parseData = JSON.parseObject(data);
		
		String total = parseData.getString("count");
		int page = Integer.parseInt(total)/pageCount;
		if(Integer.parseInt(total)%pageCount>0){
			page +=1;
		};
		for(int i=1;i<=page;i++){
			
			if(i==1){
				JdbcDistr.hasInfo(fetchTaskRicever);
			}
			
			String weibourl = "";
			if(urls!= null && ccid==1){//转发
				weibourl = "https://weibo.com/aj/v6/mblog/info/big?ajwvr=&id="+newsId+"&max_id=&page="+i+"&__rnd="+System.currentTimeMillis();
			}else{
				weibourl = "https://weibo.com/aj/v6/comment/big?ajwvr=&id="+newsId+"&root_comment_max_id=&root_comment_max_id_type=&root_comment_ext_param=&page="+i+"&filter=&sum_comment_number=&filter_tips_before=&from=singleWeiBo&__rnd="+System.currentTimeMillis();
			}
			
			System.out.println("==========="+weibourl);
			
			driver.get(weibourl);
			
//			Thread.sleep(5000);
			String source = driver.getPageSource();
			JSONObject pageSources = JSON.parseObject(source);
			String datas = pageSources.getString("data");
			JSONObject parseDatas = JSON.parseObject(datas);
			
			String htmls = parseDatas.getString("html");
			
			Document doc = Jsoup.parse(htmls);
			
			Elements elementsList = doc.getElementsByClass("list_li");
			List<Map> mapList = new ArrayList<Map>();
	        List<FetchTaskRiceverBean> list = new ArrayList<FetchTaskRiceverBean>();
			for (Element element : elementsList) {
				Elements content = element.getElementsByClass("list_con");
				String username = content.get(0).getElementsByClass("WB_text").get(0).getElementsByTag("a").get(0).text().trim();
				String text = content.get(0).getElementsByClass("WB_text").text().trim().substring(username.length()+1);
				String time = content.get(0).getElementsByClass("WB_from").get(0).text();
				String praise = content.get(0).getElementsByClass("line").get(2).getElementsByTag("em").get(1).text().trim();
				time = String2DateStr(time);
				Map map = new HashMap();
				if(text != null &&text.length()>0){
					map.put("content", text);
					map.put("postTime", time);
					map.put("urlid", fetchTaskRicever.getId());
					mapList.add(map);
					System.out.println(username+","+time+","+text+",praise="+praise);
					FetchTaskRiceverBean d = new FetchTaskRiceverBean();
		    		d.setAn(username);
		    		d.setContent(text);
		    		d.setPt(time);
		    		list.add(d);
				}
			}
			JdbcDistr.distr(fetchTaskRicever,mapList,list);
		}
		}
		
		}catch(Exception e){
			
			e.printStackTrace();
			
		}
	}
	private static String getNewsIds(String urls) {
		
		String newsId = urls.substring(urls.lastIndexOf("/")+1);
		if(newsId.indexOf("?") > -1){
			newsId = newsId.substring(0, newsId.indexOf("?"));
    	}
    	if(newsId.indexOf("#") > -1){
    		newsId = newsId.substring(0, newsId.indexOf("#"));
    	}
    	newsId = SinaIdMidConverter.midToId(newsId);
		return newsId;
	}
	/**
	 * 获取新闻id
	 * @param urls
	 * @return
	 */
	private static String getNewsId(String urls) {
		
		try{
		
		java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("config\\properties\\paramsConfig");
		String firefoxUrl = bundle.getString("firefoxurl");
		
		System.setProperty("webdriver.firefox.bin",firefoxUrl);
		WebDriver driver = new FirefoxDriver(); 
		driver.get(urls);
		
		Thread.sleep(10000);
		String pageSource = driver.getPageSource();
		
		driver.quit();
		System.out.println(pageSource);
		
		String newsId = pageSource.substring(pageSource.lastIndexOf("FM.view"));
		
		newsId = newsId.substring(newsId.indexOf("act=")+4);
		newsId = newsId.substring(0,newsId.indexOf("\"")-1).trim();
		
		System.out.println("-------------------");
		System.out.println(newsId);
		
		return newsId;
		}catch(Exception e){
			
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 格式化时间
	 * @param date
	 * @return
	 */
	public static String String2DateStr(String date){
		
		String dd = date.trim();
		try{
		if(date.contains("秒")){
			dd = DateUtil.getCurrentTime();
		}else if(date.contains("分钟")){
			int min = date.indexOf("分钟前");
			int m = Integer.parseInt(date.substring(0, min));
			dd = DateUtil.subMine(DateUtil.getCurrentTime(),-m);
		}else if(date.contains("今天")){
			String ddd = DateUtil.getNowDate(0)+" ";
			//今天 10:18:00
			dd = date.replace("今天 ", ddd);
		}else{
			dd = DateUtil.String2DateStr(date);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return dd;
	}
	
}

