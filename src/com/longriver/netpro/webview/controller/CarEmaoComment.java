package com.longriver.netpro.webview.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;


//一猫汽车网
public class CarEmaoComment {
	
	public static String dir = "c:\\";
	public static String path = "c:\\emaoVcode.jpg";
	public static void main(String arsg[]){
		try{
//			TaskGuideBean b = new TaskGuideBean();
//			b.setNick("hawkfirm");
//			b.setPassword("chwx1234");
////			b.setNick("13714148235");
////			b.setPassword("shizhuang");
//			b.setAddress("http://news.emao.com/news/201607/22635.html");
////			b.setAddress("http://news.emao.com/news/201607/1114082.html");
//			b.setCorpus("好车一个,需要优惠");
//			toRun(b);
			String tem = "EastUid=991;sess=qjg1m93tjpab9nejda5p9lo7n0; expires=Tue, 10-Jan-2017 07:59:26 GMT; path=/; domain=eastday.com";
			 String sess = "";
			 if(tem.contains("sess")){
				int k1 = tem.indexOf("sess=");
				System.out.println(k1);
				tem = tem.substring(k1);
				System.out.println(tem);
				int k2 = tem.indexOf(";");
				System.out.println(k2);
				System.out.println(tem);
				sess = tem.substring(5, k2);
				System.out.println(sess);
			 }
			TaskGuideBean b = new TaskGuideBean();
			b.setNick("hawkfirm");
			b.setPassword("chwx1234");
//			b.setNick("13714148235");
//			b.setPassword("shizhuang");
			b.setAddress("http://news.emao.com/news/201607/22635.html");
//			b.setAddress("http://news.emao.com/news/201607/1114082.html");
			b.setCorpus("一直关注呢");
			toRun(b);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void toRun(TaskGuideBean taskdo){
		try {
			String userId =taskdo.getNick();
			String pwd = taskdo.getPassword();
			String contents = taskdo.getCorpus();
			if(contents.length()<5){
				contents += "..........";
			}
			String sUrl =taskdo.getAddress();
			int ind1 = sUrl.indexOf(".htm");
			String urlt = sUrl.substring(0, ind1);
			int ind2 = urlt.lastIndexOf("/");
			String postid = urlt.substring(ind2+1);
			System.out.println("postid--===="+postid);
			String cookie = "";
		
			String commPlace = getCommPlace(sUrl,postid);
					URL fu = new URL("http://passport.emao.com/imgvalicode?t=1");
					HttpURLConnection fc = (HttpURLConnection) fu.openConnection();
					fc.addRequestProperty("Host", "passport.emao.com");
					fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
					fc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
					fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
					fc.addRequestProperty("Referer", "http://passport.emao.com/login/?backUrl=http%3A%2F%2Fnews.emao.com%2Fnews%2F201607%2F22607.html");
					fc.addRequestProperty("Connection", "keep-alive");
					fc.connect();
					InputStream i2 = fc.getInputStream();
			        byte[] bs = new byte[1024];  
			        int len;  
			       File sf=new File(dir);  
			       if(!sf.exists()){  
			           sf.mkdirs();  
			       }  
			       OutputStream os = new FileOutputStream(path);  
			        while ((len = i2.read(bs)) != -1) {  
			          os.write(bs, 0, len);  
			        }  
			        os.close();  
			        i2.close();  

					String randRom = RuoKuai.createByPostNew("3050",path);
					System.out.println("result========================"+randRom);
					Map<String, List<String>> m1 = fc.getHeaderFields();
					for(Map.Entry<String,List<String>> entry : m1.entrySet()){
						if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
							for(String value : entry.getValue()){
								cookie = cookie + value.substring(0, value.indexOf(";")) + ";";
							}
						}
					}
					 System.out.println(cookie);
					 cookie=getCookie(userId,pwd,randRom,URLEncoder.encode(sUrl, "utf-8"),cookie);
					 System.out.println(cookie);
					 if(cookie!=null && !cookie.equals("")){
						 sendConent(contents,cookie,commPlace,taskdo,postid);
					 }else{
						 MQSender.toMQ(taskdo,"失败");
					 }
		}catch (Exception e) {
			MQSender.toMQ(taskdo,"报错失败");
			e.printStackTrace();
		}
		
	}
	public static String getCookie(String username,String pwd,String vercode,String url,String cookie){
		URL fu;
		try {
			String loginType = "1";
			if(isMobileNO(username)){
				loginType = "2";
			}
				fu = new URL("http://passport.emao.com/docdlogin");
				HttpURLConnection fc = (HttpURLConnection) fu.openConnection();
				String fp = "account="+username
						+ "&pw="+pwd
						+ "&loginType="+loginType
						+ "&imgValicode="+vercode
						+ "&rememberMe=0"
						+ "&backUrl="+URLEncoder.encode(url, "utf-8")
						+ "&t=0.48487575980834663";
				System.out.println(fp);
				fc.addRequestProperty("Host", "passport.emao.com");
				fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
				fc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
				fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
				fc.addRequestProperty("Referer", "http://passport.emao.com/login/?backUrl="+url);
				fc.addRequestProperty("Connection", "keep-alive");
				fc.addRequestProperty("Content-Length", String.valueOf(fp.length()));
				fc.addRequestProperty("Cookie", cookie);
				fc.setInstanceFollowRedirects(false);
				fc.setDoInput(true);
				fc.setDoOutput(true);
				PrintWriter fo = new PrintWriter(fc.getOutputStream());
				fo.print(fp);
				fo.flush();
				Map<String, List<String>> m1 = fc.getHeaderFields();
				for(Map.Entry<String,List<String>> entry : m1.entrySet()){
					System.out.println(entry.getKey());
					if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
						for(String value : entry.getValue()){
							cookie = cookie + value.substring(0, value.indexOf(";")) + ";";
						}
					}
				}
				InputStream i1 = fc.getInputStream();
				Scanner s = new Scanner(i1);
				while(s.hasNext()){
					String scsc2 = s.nextLine();
					System.out.println(scsc2);
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cookie;
	}
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	public static String getCommPlace(String url,String postid){
		String content = "";
		try {
			if(postid.length()>5){
				 String regex2 = "\"comment_place\", ([\\d\\D]*?), newsId";  
			     String html = openUrl(url,"utf-8");  
			     content = getContent(html,regex2);  
			}else{
				 String regex2 = "'comment_place', ([\\d\\D]*?), newsId";  
			     String html = openUrl(url,"utf-8");  
			     content = getContent(html,regex2);  
			}
			
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}
	public static void sendConent(String contents,String cookie,String commPlace,TaskGuideBean taskdo,String postid){
		URL fu;
		try {
					fu = new URL("http://comment.emao.com/json/add?"
							+ "cb=jQuery11110004889945033937693_1468219921223"
							+ "&objType="+commPlace
							+ "&objId="+postid
							+ "&content="+URLEncoder.encode(contents,"utf-8")
							+ "&_=1468219992231");
				
				HttpURLConnection fc = (HttpURLConnection) fu.openConnection();
				
				fc.addRequestProperty("Host", "comment.emao.com");
				fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
				fc.addRequestProperty("Accept", "*");
				fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
				fc.addRequestProperty("Connection", "keep-alive");
				fc.addRequestProperty("Cookie", cookie);
				fc.connect();
				InputStream fi = fc.getInputStream();
				Scanner fs = new Scanner(fi);
				while(fs.hasNext()){
					String scsc2 = fs.nextLine();
					System.out.println(scsc2);
					if(scsc2.contains("100004")){
						MQSender.toMQ(taskdo,"失败");
						return ;
					}
				}
				
				
				URL f1 = new URL("http://comment.emao.com/json/toplist?cb=jQuery1111018989824131131172_1468220926212"
						+ "&objType=39&objId="+postid+"&pageNum=1&_=1468220926222");
			
			HttpURLConnection h = (HttpURLConnection) f1.openConnection();
			
			h.addRequestProperty("Host", "comment.emao.com");
			h.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
			h.addRequestProperty("Accept", "*");
			h.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			h.addRequestProperty("Connection", "keep-alive");
			h.addRequestProperty("Cookie", cookie);
			h.connect();
			InputStream i1 = h.getInputStream();
			Scanner s = new Scanner(i1);
			while(s.hasNext()){
				String scsc2 = s.nextLine();
				System.out.println(scsc2);
			}
				
			MQSender.toMQ(taskdo,"");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** 
     * ����url����url��html���� 
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
      
    private static String getContent(String text,String regex) {  
        String content = "";  
        Pattern pattern = Pattern.compile(regex);  
        Matcher matcher = pattern.matcher(text);  
        while(matcher.find()) {  
            content = matcher.group(1).toString();  
            System.out.println(content);
        }  
       
        return content;  
    }  
	
}
