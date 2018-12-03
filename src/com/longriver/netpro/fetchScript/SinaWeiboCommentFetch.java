package com.longriver.netpro.fetchScript;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.common.sina.SData;
import com.longriver.netpro.common.sina.SinaIdMidConverter;
import com.longriver.netpro.common.sina.WeiboSina;
import com.longriver.netpro.fetchScript.util.Jdbc2Mysql;
import com.longriver.netpro.fetchScript.util.Jdbc2MysqlBJWXB;
import com.longriver.netpro.fetchScript.util.Jdbc2MysqlNC;
import com.longriver.netpro.fetchScript.util.JdbcDistr;
import com.longriver.netpro.util.DateUtil;
import com.longriver.netpro.webview.controller.InterfaceCJ;
import com.longriver.netpro.webview.entity.FetchTaskRiceverBean;

public class SinaWeiboCommentFetch {
	
	public static String cookie = "SUB=_2AkMu56BQf8NxqwJRmP0WyGrlaIlzyg_EieKYu1GLJRMxHRl-yT83qhcjtRClTBrheXgz_g93mXDgsXRSN4bIyw..";
	public static String ctime_aboce = "2016-07-01 12:00:00";
	public static String userId = "18476653748";
	public static String pwd = "lx1314";
	
	public static void main(String agrs[]){
		try{
			FetchTaskRiceverBean fetchTaskRicever  = new FetchTaskRiceverBean();
			fetchTaskRicever.setUrl("https://weibo.com/1638782947/Fr3APmmwL?ref=home&rid=2_0_8_1413094955393027113&type=comment");
			fetchTaskRicever.setCid("2_networkLocal");
			sina(fetchTaskRicever);
		}catch(Exception e){
			e.printStackTrace();
		}
//		String2DateStr("今天 10:18:00");
	}
	
	public static void sina(FetchTaskRiceverBean fetchTaskRicever) throws Exception{
		int ccid = 1;
		String cidAndMark[] = fetchTaskRicever.getCid().split("_");
		if(cidAndMark.length>1){ //不是本项目发送的,不接收
			if(cidAndMark[0].trim().equals("1") || fetchTaskRicever.getCid().equals("1")){
				ccid = 1; //转发
			}else{
				ccid = 2; //评论
			}
		}
		String addr = fetchTaskRicever.getUrl();
    	String midEncode = addr.substring(addr.lastIndexOf("/")).replace("/", "");
    	if(midEncode.indexOf("?") > -1){
    		midEncode = midEncode.substring(0, midEncode.indexOf("?"));
    	}
    	if(midEncode.indexOf("#") > -1){
    		midEncode = midEncode.substring(0, midEncode.indexOf("#"));
    	}
    	String id = SinaIdMidConverter.midToId(midEncode);
    	System.out.println("id=="+id);
    	if(ccid==1){  
    		getZhuans(id,cookie,fetchTaskRicever);
    	}else{
    		getComments(id,fetchTaskRicever);
    	}	
	}
	public static  void getComments(String id,FetchTaskRiceverBean b){
		try{
			int ppage = 1;
			String urls = "https://weibo.com/aj/v6/comment/big?" +
					"ajwvr=&id="+id+"&root_comment_max_id=" +
					"&root_comment_max_id_type=&root_comment_ext_param=" +
					"&page="+ppage+"&filter=&sum_comment_number=&filter_tips_before=" +
					"&from=singleWeiBo&__rnd="+System.currentTimeMillis();
			URL uf1 = new URL(urls);
		    HttpURLConnection cf1 = (HttpURLConnection) uf1.openConnection();
		    cf1.addRequestProperty("Host", "weibo.com");
		    cf1.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
		    cf1.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		    cf1.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		    cf1.addRequestProperty("Cookie", cookie);
		    cf1.addRequestProperty("Connection", "keep-alive");
		    cf1.setConnectTimeout(1000*30);
		    cf1.setReadTimeout(1000*30);
		    cf1.connect();
			 
			InputStream iff1 = cf1.getInputStream();
			Scanner sf1 = new Scanner(iff1);
			String scscAll = "";
			while(sf1.hasNext()){
				scscAll += sf1.nextLine();
				System.out.println(scscAll);
			}
			JSONObject parsePageSource = JSON.parseObject(scscAll);
			String code = parsePageSource.getString("code");
			int page = 1;
			if(code != null && "100000".equals(code)){
				String data = parsePageSource.getString("data");
				JSONObject parseData = JSON.parseObject(data);
				
				String total = parseData.getString("count");
				page = Integer.parseInt(total)/15;
				if(Integer.parseInt(total)%15>0){
					page +=1;
				};
			}
			StringBuffer ct = new StringBuffer();
			for(int j=1;j<=page;j++){
				ppage = j;
				URL uf = new URL(urls);
				System.out.println(urls);
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
				String html = "";
				while(sf.hasNext()){
					String scsc = sf.nextLine();
						html = scsc.substring(scsc.indexOf("\"html")+8,scsc.indexOf(",\"page")-1);
					ct.append(html);
				}
				if(j==1){
					JdbcDistr.hasInfo(b);
				}
				parseHtmlComment(ct.toString(),b);
				ct = ct.delete(0,ct.length());
				if(b.getCount()!=null && !b.getCount().equals("")){
					if(b.getCount().equals(j+"")) break;
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public static void getZhuans(String id,String cookie,FetchTaskRiceverBean b){
		try{
			URL uf1 = new URL("http://weibo.com/aj/v6/mblog/info/big?ajwvr=6&id="+id+"&page=1&__rnd=" + new Date().getTime());
			//&max_id=3923961391468663
		    HttpURLConnection cf1 = (HttpURLConnection) uf1.openConnection();
		    cf1.addRequestProperty("Host", "weibo.com");
		    cf1.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
		    cf1.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		    cf1.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		    cf1.addRequestProperty("Cookie", cookie);
		    cf1.addRequestProperty("Connection", "keep-alive");
		    cf1.setConnectTimeout(1000*30);
		    cf1.setReadTimeout(1000*30);
		    cf1.connect();
			 
		    String pageNO = "";
			InputStream iff1 = cf1.getInputStream();
			Scanner sf1 = new Scanner(iff1);
			//String mid = "";
			
			while(sf1.hasNext()){
				String scsc = sf1.nextLine();
				System.out.println("scsc=="+scsc);
				if(scsc.indexOf("totalpage") > -1){
					pageNO = scsc.substring(scsc.indexOf("totalpage")).replace("totalpage", "");
					//pageNO = pageNO.substring(0, pageNO.indexOf(","));
					pageNO = pageNO.replace("\"", "").replace(":", "").trim();
					pageNO = pageNO.substring(0,pageNO.indexOf(","));
					
				}
			}
			if(pageNO.equals("")){
				System.out.println("没有数据,跳出!");
				return ;
			}
			int pageNum = Integer.parseInt(pageNO);
			if(pageNum>=30) pageNum=30;
			StringBuffer ct = new StringBuffer();
			for(int j=1;j<=pageNum;j++){
				URL uf = new URL("http://weibo.com/aj/v6/mblog/info/big?ajwvr=6&id="+id+"&page="+j+"&__rnd=" + new Date().getTime());
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
				String html = "";
				while(sf.hasNext()){
					String scsc = sf.nextLine();
						html = scsc.substring(scsc.indexOf("\"html")+8,scsc.indexOf(",\"page")-1);
					ct.append(html);
				}
				
				parseHtmlZhuan(ct.toString(),b);
				//设置采集页数
				if(b.getCount()!=null && !b.getCount().equals("")){
					if(b.getCount().equals(j+"")) break;
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void parseHtmlComment(String html,FetchTaskRiceverBean b){
		String  testTemp = html;
		String  test = testTemp;
		String  s = "WB_text";
		 int count = 0;  
	        //一共有str的长度的循环次数  
	        for(int i=0; i<test.length();){  
	            int c = -1;
	            c = test.indexOf(s);  
	            //如果有S这样的子串。则C的值不是-1.  
	            if(c != -1){  
	                //这里的c+1 而不是 c+ s.length();这是因为。如果str的字符串是“aaaa”， s = “aa”，则结果是2个。但是实际上是3个子字符串  
	                //将剩下的字符冲洗取出放到str中
	            	test = test.substring(c+ s.length());  
	                count ++;  
	            }  
	            else {  
	                //i++;  
	                break;  
	            }  
	        }  
	        List<Map> mapList = new ArrayList<Map>();
	        List<FetchTaskRiceverBean> list = new ArrayList<FetchTaskRiceverBean>();
	        for(int i=0;i<count;i++){
	        	Map testMap = new HashMap();
	        	String temp = html.substring(html.indexOf("WB_text"));
	    		temp = temp.substring(temp.indexOf("ucardconf"));
	    		temp = temp.substring(temp.indexOf(">"));
	    		String nickname = temp.substring(temp.indexOf(">")+1,temp.indexOf("<\\/a>"));
	    		String content = temp.substring(temp.indexOf("<\\/a>")+5,temp.indexOf("<\\/div>"));
	    		temp = temp.substring(temp.indexOf("WB_from S_txt2"));
	    		String publishTiem = temp.substring(temp.indexOf("WB_from S_txt2")+17,temp.indexOf("<\\/div>"));
	    		
	    		html = temp;
	    		Map map = new HashMap();
	    		map.put("urlid", b.getId());
	    		map.put("content", filterHTMl(decodeUnicode(content)));
	    		String tt = decodeUnicode(publishTiem);
	    		System.out.println("tt==="+tt);
	    		String ttt = String2DateStr(tt);
	    		System.out.println("ttt==="+ttt);
	    		map.put("postTime", ttt);
	    		mapList.add(map);
	    		
	    		FetchTaskRiceverBean d = new FetchTaskRiceverBean();
	    		d.setAn(decodeUnicode(nickname));
//	    		d.setContent(filterHTMl(decodeUnicode(content)));
	    		d.setContent(decodeUnicode(content));
	    		System.out.println(decodeUnicode(content));
	    		d.setPt(String2DateStr(decodeUnicode(publishTiem)));
	    		list.add(d);
	    		
	        }
	        if(list.size()==0){
	        	System.out.println("-----------没有采集到---------------------------");
	        	System.out.println("-----------没有采集到---------------------------");
	        	return ;
	        }
	        
	        JdbcDistr.distr(b,mapList,list);
	        
	}
	public static void parseHtmlZhuan(String html,FetchTaskRiceverBean b){
		String  testTemp = html;
		String  test = testTemp;
		String  s = "WB_text";
		 int count = 0;  
	        //一共有str的长度的循环次数  
	        for(int i=0; i<test.length();){  
	            int c = -1;  
	            c = test.indexOf(s);  
	            //如果有S这样的子串。则C的值不是-1.  
	            if(c != -1){  
	                //这里的c+1 而不是 c+ s.length();这是因为。如果str的字符串是“aaaa”， s = “aa”，则结果是2个。但是实际上是3个子字符串  
	                //将剩下的字符冲洗取出放到str中  
	            	test = test.substring(c+ s.length());  
	                count ++;  
	            }else {
	                System.out.println("没有");  
	                break;  
	            }  
	        }  
//	        List<Map> list = new ArrayList<Map>();
	        List<FetchTaskRiceverBean> list = new ArrayList<FetchTaskRiceverBean>();
	        List<Map> mapList = new ArrayList<Map>();
	        for(int i=0;i<count;i++){
	        	Map testMap = new HashMap();
	        	String temp = html.substring(html.indexOf("WB_text"));
	    		temp = temp.substring(temp.indexOf("node-type"));
	    		temp = temp.substring(temp.indexOf(">"));
	    		String nickname = temp.substring(temp.indexOf(">")+1,temp.indexOf("<\\/a>"));
	    		String content = temp.substring(temp.indexOf("<\\/a>")+5,temp.indexOf("<\\/div>"));
	    		temp = temp.substring(temp.indexOf("WB_from S_txt2"));
	    		String publishTiem = temp.substring(temp.indexOf("WB_from S_txt2")+17,temp.indexOf("<\\/div>"));
	    		publishTiem = publishTiem.substring(publishTiem.indexOf(">")+1,publishTiem.indexOf("<\\/a>"));
	    		
	    		html = temp;
	    		FetchTaskRiceverBean d = new FetchTaskRiceverBean();
	    		d.setAn(decodeUnicode(nickname));
	    		d.setContent(filterHTMl(decodeUnicode(content)));
	    		d.setPt(String2DateStr(decodeUnicode(publishTiem)));
	    		list.add(d);
//	    		testMap.put("authorName", decodeUnicode(nickname));
//	    		testMap.put("content", filterHTMl(decodeUnicode(content)));
//	    		testMap.put("postTime", String2DateStr(decodeUnicode(publishTiem)));
//	    		testMap.put("tpid", tpid);
//	    		testMap.put("cid", "1");
//	    		list.add(testMap);
	    		System.out.println("nickname=="+decodeUnicode(nickname));
	    		System.out.println("content=="+filterHTMl(decodeUnicode(content)));
	    		System.out.println();
	        }
	        if(list.size()==0){
	        	System.out.println("-----------没有采集到---------------------------");
	        	System.out.println("-----------没有采集到---------------------------");
	        	return ;
	        }
	        b.setRlist(list);
	        InterfaceCJ.getResultJava(b);
	        
	        
	        
	}
	public static String decodeUnicode(String theString) {      
	    char aChar;      
	     int len = theString.length();      
	    StringBuffer outBuffer = new StringBuffer(len);      
	    for (int x = 0; x < len;) {      
	     aChar = theString.charAt(x++);      
	     if (aChar == '\\') {      
	      aChar = theString.charAt(x++);      
	      if (aChar == 'u') {      
	       // Read the xxxx      
	       int value = 0;      
	       for (int i = 0; i < 4; i++) {      
	        aChar = theString.charAt(x++);      
	        switch (aChar) {      
	        case '0':      
	        case '1':      
	        case '2':      
	        case '3':      
	       case '4':      
	        case '5':      
	         case '6':      
	          case '7':      
	          case '8':      
	          case '9':      
	           value = (value << 4) + aChar - '0';      
	           break;      
	          case 'a':      
	          case 'b':      
	          case 'c':      
	          case 'd':      
	          case 'e':      
	          case 'f':      
	           value = (value << 4) + 10 + aChar - 'a';      
	          break;      
	          case 'A':      
	          case 'B':      
	          case 'C':      
	          case 'D':      
	          case 'E':      
	          case 'F':      
	           value = (value << 4) + 10 + aChar - 'A';      
	           break;      
	          default:      
	           throw new IllegalArgumentException(      
	             "Malformed   \\uxxxx   encoding.");      
	          }      
	   
	        }      
	         outBuffer.append((char) value);      
	        } else {      
	         if (aChar == 't')      
	          aChar = '\t';      
	         else if (aChar == 'r')      
	          aChar = '\r';      
	   
	         else if (aChar == 'n')      
	   
	          aChar = '\n';      
	   
	         else if (aChar == 'f')      
	   
	          aChar = '\f';      
	   
	         outBuffer.append(aChar);      
	   
	        }      
	   
	       } else     
	   
	       outBuffer.append(aChar);      
	   
	      }      
	   
	      return outBuffer.toString();      
	     }     
	
	
	public static List<SData> readAddrsFile(String filePath){
    	List<SData> datas = new ArrayList<SData>();
        try {
                String encoding="GBK";
                File file=new File(filePath);
                if(file.isFile() && file.exists()){ //閸掋倖鏌囬弬鍥︽閺勵垰鎯佺�涙ê婀�
                    InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file),encoding);//閼板啳妾婚崚鎵椽閻焦鐗稿锟�
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String lineTxt = null;
                    while((lineTxt = bufferedReader.readLine()) != null){
                    	SData data = new SData();
                    	String tcpid=lineTxt.split(",")[0];
                    	String addr=lineTxt.split(",")[1];
                    	data.put("tcpid", tcpid);
                    	data.put("addr", addr);
                    	datas.add(data);
                        //System.out.println(lineTxt);
                    }
                    read.close();
        }else{
            System.out.println("閹靛彞绗夐崚鐗堝瘹鐎规氨娈戦弬鍥︽");
        }
        } catch (Exception e) {
            System.out.println("鐠囪褰囬弬鍥︽閸愬懎顔愰崙娲晩");
            e.printStackTrace();
        }
        return datas;
    }
	
	
	/**
	 * 过滤掉字符串中HTML标签
	 * @param input
	 * @return
	 */
	public static String filterHTMl(String input){
		if(input == null) return input;
		String str = input.replaceAll("<[a-zA-Z]+[1-9]?[^><]*>", "").replaceAll("</[a-zA-Z]+[1-9]?>", "");
		str = str.replaceAll("&nbsp;", "");
		str = str.replace("：", "");
		return str;
	}
	public static String String2DateStr(String date){
		String dd = date.trim();
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
		return dd;
	}
	
}
