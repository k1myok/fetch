package com.longriver.netpro.common.local;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.bouncycastle.util.encoders.Base64;

public class TouPiao{
	
	
	@SuppressWarnings("null")
	public static void ifeng() throws Exception{
		
		String param = "id=8852";
		URL u1 = new URL("http://112.124.118.30:8080/dcvote/servlet/UserVote");
		HttpURLConnection c1 = (HttpURLConnection) u1.openConnection();
		c1.addRequestProperty("Host", "112.124.118.30:8080");
		c1.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
		c1.addRequestProperty("Accept", "*/*");
		c1.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		c1.addRequestProperty("Referer", "http://112.124.118.30:8080/dcvote/wx/page.jsp?un=a&str=8852&from=groupmessage&isappinstalled=0");
		c1.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		c1.addRequestProperty("Content-Length", String.valueOf(param.length()));
		c1.setInstanceFollowRedirects(false);
		c1.setDoInput(true);
		c1.setDoOutput(true);
		PrintWriter out = new PrintWriter(c1.getOutputStream());
		out.print(param);
		out.flush();
		
		InputStream i11 = c1.getInputStream();
		Scanner s11 = new Scanner(i11);
		while(s11.hasNext()){
			String scsc = s11.nextLine();
			System.out.println(scsc);
		}
		
		
		//service.execute("tb_news_return", missionId, statusCd, link);
	}
	
	public static void main(String agrs[]){
		try{
			for(int i=0;i<100;i++){
				new TouPiao().ifeng();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
