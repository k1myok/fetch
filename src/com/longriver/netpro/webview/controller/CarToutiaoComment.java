package com.longriver.netpro.webview.controller;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.alibaba.fastjson.JSON;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.vcode.RuoKuai;

/**
 * 汽车头条
 */
public class CarToutiaoComment {
	public static String imgPath = "c:\\cartoutiao.jpg";
	public static boolean ccodeisTrue = false;
	public static void main(String s[]){
		TaskGuideBean b = new TaskGuideBean();
		b.setNick("lilei1929@163.com");
		b.setPassword("lilei41968800");
		b.setAddress("http://www.qctt.cn/news/116180.html");
		b.setCorpus("不错不错....");
		toRun(b);
	}
	public static void toRun(TaskGuideBean taskdo){
		try {
			String userId =taskdo.getNick();
			String pwd = taskdo.getPassword();
			String contents = taskdo.getCorpus();
			String sUrl = taskdo.getAddress();
			int ind1 = sUrl.indexOf(".htm");
			String urlt = sUrl.substring(0, ind1);
			int ind2 = urlt.lastIndexOf("/");
			String postid = urlt.substring(ind2+1);
			
			String prxUrl = sUrl.replace("http://", "");
			prxUrl = prxUrl.substring(0,prxUrl.indexOf("/"));
			String cookie = "";
			
			String param = "user_login=" + URLEncoder.encode(userId, "utf-8")
			+ "&user_pass=" + URLEncoder.encode(pwd, "utf-8")
			+ "&wp-submit=%E7%99%BB%E5%BD%95"
			+ "&redirect_to=http%3A%2F%2Fwww.qctt.cn%2Fwp-admin%2Fpost-new.php&testcookie=1";

			URL u1 = new URL("http://www.qctt.cn/index/common/checklogin");
			HttpURLConnection c1 = (HttpURLConnection) u1.openConnection();
			c1.addRequestProperty("Host", "www.qctt.cn");
			c1.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
			c1.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			c1.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
			c1.addRequestProperty("Referer", "http://www.qctt.cn/common/login");
			c1.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			c1.setDoInput(true);
			c1.setDoOutput(true);
			PrintWriter out = new PrintWriter(c1.getOutputStream());
			out.print(param);
			out.flush();
			Map<String, List<String>> m1 = c1.getHeaderFields();
			for(Map.Entry<String,List<String>> entry : m1.entrySet()){
				if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
					for(String value : entry.getValue()){
						cookie = cookie + value.substring(0, value.indexOf(";")) + ";";
					}
				}
			}
			System.out.println("cookie=="+cookie);
			//下载验证码
			loadCode(cookie,sUrl,0);
			System.out.println("code istrue=="+ccodeisTrue);
			if(ccodeisTrue){
				//评论
				URL fu = new URL("http://www.qctt.cn/news/addcomment?t=" + new Date().getTime());
				HttpURLConnection fc = (HttpURLConnection) fu.openConnection();
				String fp = "comment=" + URLEncoder.encode(contents, "utf-8")+"&postid="+postid;
				fc.addRequestProperty("Host", "www.qctt.cn");
				fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
				fc.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
				fc.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
				fc.addRequestProperty("Referer", sUrl);
				fc.addRequestProperty("Cookie", cookie);
				fc.addRequestProperty("Connection", "keep-alive");
				fc.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
				fc.addRequestProperty("Content-Length", String.valueOf(fp.length()));
				fc.setInstanceFollowRedirects(false);
				fc.setDoInput(true);
				fc.setDoOutput(true);
				PrintWriter fo = new PrintWriter(fc.getOutputStream());
				fo.print(fp);
				fo.flush();
				InputStream fi = fc.getInputStream();
				Scanner fs = new Scanner(fi);
				while(fs.hasNext()){
					String scsc2 = fs.nextLine();
					System.out.println(scsc2);
				}
			}else{
				MQSender.toMQ(taskdo,"失败");
			}
			
			MQSender.toMQ(taskdo,"");
		}catch (Exception e) {
			MQSender.toMQ(taskdo,"报错失败");
			e.printStackTrace();
		}
		
		
	}
	public String formatString(String str) {
        if (str != null) {            
        	try {                
        		str = new String(str.getBytes("ISO-8859-1"), "UTF-8");            
        		} catch (Exception e) {}        
        		}
        return str;    
    }
	public static void loadCode(String cookie,String sUrl,int times) throws Exception{
		String randRom = "1234";
		URL u211 = new URL("http://www.qctt.cn/index/common/verify");
		HttpURLConnection c211 = (HttpURLConnection) u211.openConnection();
		c211.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
		c211.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		c211.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
		c211.addRequestProperty("Connection", "keep-alive");
		c211.addRequestProperty("Cache-Control", "max-age=0");
		c211.addRequestProperty("Host", "www.qctt.cn");
		c211.addRequestProperty("Referer", "http://www.qctt.cn/news/116180.html");
		c211.addRequestProperty("Cookie", cookie);
		c211.connect();
		
		InputStream i211 = c211.getInputStream();
		
	    byte[] bs = new byte[1024];  
	    int len;  
	   OutputStream os = new FileOutputStream(imgPath);  
	    while ((len = i211.read(bs)) != -1) {  
	      os.write(bs, 0, len);  
	    }  
	    os.close();  
	    i211.close();  
	    
	    randRom = RuoKuai.createByPostNew("1040",imgPath);
		System.out.println("result========================"+randRom);
		
		//验证码确认
		String codep = "code=" + randRom;
		
		URL fu11 = new URL("http://www.qctt.cn/common/check_code?t=" + new Date().getTime());
		HttpURLConnection fc11 = (HttpURLConnection) fu11.openConnection();
		fc11.addRequestProperty("Accept", "*/*");
		fc11.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		fc11.addRequestProperty("Connection", "Keep-Alive");
		fc11.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		fc11.addRequestProperty("Cookie", cookie);
		fc11.addRequestProperty("Host", "www.qctt.cn");
		fc11.addRequestProperty("Referer", sUrl);
		fc11.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
		fc11.addRequestProperty("x-requested-with", "XMLHttpRequest");
		fc11.setInstanceFollowRedirects(false);
		fc11.setDoInput(true);
		fc11.setDoOutput(true);
		PrintWriter foc = new PrintWriter(fc11.getOutputStream());
		foc.print(codep);
		foc.flush();
		InputStream fic = fc11.getInputStream();
		Scanner fsc = new Scanner(fic);
		boolean c = false;
		while(fsc.hasNext()){
			String scsc2 = fsc.nextLine();
			System.out.println(scsc2);
			if(scsc2.contains("1")){
				ccodeisTrue = true;
				return;
			}
		}
		System.out.println(times);
		if(!c && times<3){
			times = times+1;
			ccodeisTrue = false;
			loadCode(cookie,sUrl,times);
		}
	}
	
}
