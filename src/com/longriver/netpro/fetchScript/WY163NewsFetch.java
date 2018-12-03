package com.longriver.netpro.fetchScript;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.longriver.netpro.fetchScript.util.Jdbc2Mysql;
import com.longriver.netpro.fetchScript.util.Jdbc2MysqlBJWXB;
import com.longriver.netpro.fetchScript.util.Jdbc2MysqlNC;
import com.longriver.netpro.fetchScript.util.JdbcDistr;
import com.longriver.netpro.fetchScript.util.JsonHelpBean;
import com.longriver.netpro.fetchScript.util.JsonHelper;
import com.longriver.netpro.util.DateUtil;
import com.longriver.netpro.webview.controller.InterfaceCJ;
import com.longriver.netpro.webview.entity.FetchTaskRiceverBean;

public class WY163NewsFetch {
	
	public static void main(String agrs[]){
		FetchTaskRiceverBean bb = new FetchTaskRiceverBean();
//		bb.setUrl("http://comment.news.163.com/news3_bbs/BD50D5K500014Q4P.html");
//		bb.setUrl("http://comment.news.163.com/health3_bbs/CKKFEKI504388CSB.html");
		bb.setUrl("http://comment.news.163.com/news2_bbs/CNVBCE400001899N.html");
		try {
			toRun(bb);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void toRun(FetchTaskRiceverBean fetchTaskRicever) throws Exception {
		try{
			int pageSize = 30;
//			int pageSize = getPageSize(fetchTaskRicever);
			System.out.println("pageSize=="+pageSize);
			List<FetchTaskRiceverBean> listto = new ArrayList<FetchTaskRiceverBean>();
			List<Map> mapList = new ArrayList<Map>();
			for(int i=0;i<pageSize;i++){
				if(i==0){
					JdbcDistr.hasInfo(fetchTaskRicever);
				}
				//if((pageSize<3 && pageSize>0) || i!=0&&i%3==0){
					try{
						String  testTemp = cj(fetchTaskRicever.getUrl(),i);
						System.out.println("testTemp=="+testTemp);
						if(testTemp.contains("\"comments\":{}")) break;
						String  test = testTemp;
						String  s = "against";
						 int count = 0;  
					        //一共有str的长度的循环次数  
					        for(int j=0; j<test.length();){  
					            int c = -1;  
					            c = test.indexOf(s);  
					            //如果有S这样的子串。则C的值不是-1.  
					            if(c != -1){  
					                //这里的c+1 而不是 c+ s.length();这是因为。如果str的字符串是“aaaa”， s = “aa”，则结果是2个。但是实际上是3个子字符串  
					                //将剩下的字符冲洗取出放到str中  
					            	test = test.substring(c+1);  
					                count ++;  
					               
					            }  
					            else {  
					                //i++;  
					                System.out.println("没有");  
					                break;  
					            }  
					        }  
					        System.out.println("count====="+count);  
					        StringBuffer aa = new StringBuffer();
					        aa.append("{'comments':[");
				for(int j=0;j<count;j++){
					String ta = testTemp.substring(testTemp.indexOf(s)-2,testTemp.indexOf("vote")-1);
					String temp = testTemp.substring(testTemp.indexOf("vote"));
					String at = temp.substring(temp.indexOf("vote"),temp.indexOf("}")+1);
					
					aa.append(ta+"\""+at);
					if(j+1!=count){
						aa.append(",");
					}
					
					testTemp = testTemp.substring(testTemp.indexOf("vote")+4);
				}
					aa.append("]}");
					 String bb =aa.toString();
					 bb = bb.replaceAll("'\\[", "\\[");
					 bb = bb.replaceAll("\\]'", "\\]");
					 System.out.println("bb = "+bb);       
					 
					 JsonHelpBean jh = (JsonHelpBean)JsonHelper.parseJson2Object(bb,JsonHelpBean.class);
					 
					 List<JsonHelpBean> list = jh.getComments();
						for(int j=0;j<list.size();j++){
							System.out.println("内容:: "+list.get(j).getContent());
							JsonHelpBean jj = list.get(j).getUser();
							System.out.println("nick=="+jj.getNickname());
							System.out.println(list.get(j).getCreateTime().trim());
							FetchTaskRiceverBean d = new FetchTaskRiceverBean();
				    		d.setAn(jj.getNickname()==null?"":jj.getNickname().replaceAll("\"", "").trim());
				    		d.setContent(list.get(j).getContent()==null?"":list.get(j).getContent().replaceAll("\"", "").trim());
				    		d.setPt(list.get(j).getCreateTime().trim()); 
				    		listto.add(d);
				    		
				    		Map map = new HashMap();
				    		map.put("urlid", fetchTaskRicever.getId());
				    		map.put("content", list.get(j).getContent()==null?"":list.get(j).getContent().replaceAll("\"", "").trim());
				    		map.put("postTime", list.get(j).getCreateTime().trim());
				    		mapList.add(map);
							
						}
						//每10页发送一次
						if(i!=0 && i%10==0){
							System.out.println("每10页发送一次:: "+i%10);
							JdbcDistr.distr(fetchTaskRicever,mapList,listto);
							System.out.println("每10页发送一次:: "+i%10);
							mapList.clear();
							listto.clear();
						}
					    
					    
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			JdbcDistr.distr(fetchTaskRicever,mapList,listto);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static int getPageSize(FetchTaskRiceverBean fetchTaskRicever) throws Exception {
//		String url = "http://comment.news.163.com/news3_bbs/BBDSN4HM00014AED.html";
		String url = fetchTaskRicever.getUrl();
		String boardId = url.substring(url.lastIndexOf("/")+1,url.indexOf(".html"));		
		
		//String m = "";
		String tcount = "";
		
		URL u2 = new URL("http://comment.news.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads/"+boardId+"?callback=getData");
		HttpURLConnection c2 = (HttpURLConnection) u2.openConnection();
		c2.connect();
		InputStream i2 = c2.getInputStream();
		Scanner s2 = new Scanner(i2, "utf-8");
		
		while(s2.hasNext()){
			String scsc = s2.nextLine();
			System.out.println("scsc======"+scsc);
			if(scsc.indexOf("tcount") > -1 ){
				tcount = scsc.substring(scsc.indexOf("tcount")+8,scsc.indexOf("title")-2);
				System.out.println("tcount================"+tcount);
			}
			
			
		}
		int sumCount = 0;
		int pageSize=0;
		if(!tcount.equals("")){
			sumCount = Integer.parseInt(tcount);
			pageSize = (int)sumCount/30;
		}
		return pageSize;
	}
	public static String cj(String url,int pageSize) throws Exception {
		StringBuffer  aa = new StringBuffer();
		String boardId = url.substring(url.lastIndexOf("/")+1,url.indexOf(".html"));		
		int offset = 0;
		int limit = 30;
		//for(int i=pageSize-3;i<pageSize;i++){
			
			offset = limit * pageSize;

			URL uf = new URL("http://comment.news.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads/"+boardId+"/comments/newList?&offset=" + offset + "&limit=" + limit + "&showLevelThreshold=72&headLimit=1&tailLimit=2&callback=getData&_=" + new Date().getTime());

	        HttpURLConnection cf = (HttpURLConnection) uf.openConnection();
			
	        cf.setRequestProperty("contentType", "GBK");  
	        cf.setRequestProperty("Content-type", "text/html");
	        cf.setRequestProperty("Accept-Charset", "utf-8");
	        cf.setRequestProperty("contentType", "utf-8");
	        
			cf.addRequestProperty("Host", "comment.news.163.com");
			cf.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:40.0) Gecko/20100101 Firefox/40.0");
			cf.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			cf.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
			cf.addRequestProperty("Connection", "keep-alive");
			cf.addRequestProperty("X-Requested-With", "XMLHttpRequest");
			cf.connect();
			 
			BufferedReader sf = new BufferedReader(new InputStreamReader(cf.getInputStream(),"utf-8"));
//			InputStream iff =new InputStreamReader(cf.getInputStream(),"gbk");
//			Scanner sf = new Scanner(iff);
			//String mid = "";
			String str;  
			while(null != (str = sf.readLine())){
				aa.append(str);
			}
			
			System.out.println(aa);
		//}
		
		return aa.toString();
		
	}
	
}
