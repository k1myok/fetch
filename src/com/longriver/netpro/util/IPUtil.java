package com.longriver.netpro.util;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.longriver.netpro.common.asdl.ConnectNetWork;

public class IPUtil {
	
	public static void qieIP(){
		try {
			for(int i=0;i<5;i++){
				boolean has = ConnectNetWork.cutAdsl(GetProprities.AsdlConfig.getProperty("asdlname"));
				Thread.sleep(1000);
				if(has) break;
			}
			for(int i=0;i<5;i++){
				boolean cnt = ConnectNetWork.connAdsl(GetProprities.AsdlConfig.getProperty("asdlname"),GetProprities.AsdlConfig.getProperty("asdluser"),GetProprities.AsdlConfig.getProperty("asdlpwd"));
				Thread.sleep(1000);  
				if(cnt){
					System.out.println("ip切换成功");
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据IP查询所在城市
	 * @param ip String IP地址
	 * @return value String 所在城市
	 * */
	public static String getArea(String ip){
		String value = "未知";
		String soupData = "";
		if (ip != null && !ip.equals("") && !ip.equals("0:0:0:0:0:0:0:1")) {
			String url = "http://ip138.com/ips1388.asp?ip="+ip+"&action=2";//IP转换连接   IP地址库
			try {
				//System.out.println(url);
				Document doc2 =Jsoup.connect(url).get();
				Element masthead =doc2.select(".ul1").first(); //找到class为toc的元素
				Elements content =masthead.getElementsByTag("li"); //找到a属性的元素集合
				for (Element link : content) {
					soupData =link.text();
					break;
				}
			} catch (IOException e) {
				//e.printStackTrace();
				value = "未知";
			}
			if(soupData.length()>6){
				soupData = soupData.substring(6);
				String values[] = soupData.split(" ");
				value = values[0];
			}
		}
		
		return value;		
	}
	public static void main(String[] args) {
		java.util.Scanner in = new java.util.Scanner(System.in);
		String ip = "117.100.206.174";
		String city = "117.100.206.174";
		while (in.hasNext()) {
			ip = in.next();
			city = getArea(ip);
			System.out.println(ip + "所在的城市: " + city);
		}
		
	}
}
