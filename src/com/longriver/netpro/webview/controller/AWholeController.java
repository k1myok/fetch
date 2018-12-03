package com.longriver.netpro.webview.controller;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.longriver.netpro.common.asdl.ConnectNetWork;
import com.longriver.netpro.util.GetProprities;
import com.longriver.netpro.webview.vcode.RuoKuai;

@Controller
@RequestMapping("aWholeController")
public class AWholeController{
	public int number=1;
	public static String codeDir = "c:\\";
	public static String codePath = "c:\\tttppp.jpg";
	
	public static void main(String ar[]){
	}
	@RequestMapping(value="writeMap.do",method={RequestMethod.POST,RequestMethod.GET})
	public void writeMap(HttpServletRequest request,HttpServletResponse response){
		response.setContentType("text/html; charset=UTF-8"); //转码
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print("执行中...");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String urlid = request.getParameter("uid");
		WeiboReader.sinaReader(urlid);
	}
	public void todo(){
		System.out.println("开始: "+number);
		getCookie();
		try {
			Thread.sleep(5000);
			ConnectNetWork.cutAdsl(GetProprities.AsdlConfig.getProperty("asdlname"));
			Thread.sleep(5000);
			ConnectNetWork.connAdsl(GetProprities.AsdlConfig.getProperty("asdlname"),GetProprities.AsdlConfig.getProperty("asdluser"),GetProprities.AsdlConfig.getProperty("asdlpwd"));
			Thread.sleep(5000);
			number++;
			if(number==50000) return ;
			todo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void getCookie(){
		System.out.println("11111111111111111111");
		URL fu;
		String cookie = "";
		try {
				fu = new URL("http://vote5.gmw.cn/dovote/getcode?"+new Date().getTime());
				System.out.println("222222222222222222222");
				HttpURLConnection fc = (HttpURLConnection) fu.openConnection();
				fc.addRequestProperty("Host", "vote5.gmw.cn");
				fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
				fc.addRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
				fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
				fc.addRequestProperty("Upgrade-Insecure-Requests", "1");
				fc.addRequestProperty("Referer", "http://topics.gmw.cn/node_106094.htm?from=groupmessage&isappinstalled=0");
				fc.addRequestProperty("Connection", "keep-alive");
				fc.connect();
				System.out.println("3333333333333333");
				Map<String, List<String>> m1 = fc.getHeaderFields();
				for(Map.Entry<String,List<String>> entry : m1.entrySet()){
					if(entry.getKey() != null && entry.getKey().indexOf("Set-Cookie") > -1){
						for(String value : entry.getValue()){
							cookie = cookie + value.substring(0, value.indexOf(";")) + ";";
						}
					}
				}
				System.out.println("444444444444444444");
				//////////////////////////////////
				InputStream i2 = fc.getInputStream();
		        byte[] bs = new byte[1024];  
		        int len;
		        System.out.println("555555555555");
		        OutputStream os = new FileOutputStream(codePath);  
		        while ((len = i2.read(bs)) != -1) {  
		        	os.write(bs, 0, len);  
		        }  
		        os.close();  
		        i2.close();  
		        fc.disconnect();
		        System.out.println("66666666666");
				String randRom = RuoKuai.createByPostNew("3040",codePath);
				System.out.println("result========================"+randRom);
				toupiao(cookie,randRom);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void toupiao(String cookie,String vcode){
		System.out.println("66666666666666666666");
		URL fu;
		try {
			fu = new URL("http://vote5.gmw.cn/dovote/tovote?vid=279&t_4370=85&vcode="+vcode+"&jsoncallback=jQuery17203006214169606357_1492147305365&_="+new Date().getTime());
			HttpURLConnection fc = (HttpURLConnection) fu.openConnection();
			fc.addRequestProperty("Host", "vote5.gmw.cn");
			fc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
			fc.addRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
			fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			fc.addRequestProperty("Upgrade-Insecure-Requests", "1");
			fc.addRequestProperty("Referer", "http://topics.gmw.cn/node_106094.htm?from=groupmessage&isappinstalled=0");
			fc.addRequestProperty("Connection", "keep-alive");
			fc.addRequestProperty("Cookie", cookie);
			fc.connect();
			System.out.println("结束----------------");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
