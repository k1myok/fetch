package com.longriver.netpro.webview.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.longriver.netpro.common.asdl.ConnectNetWork;
import com.longriver.netpro.common.threadpool.MessageSender;
import com.longriver.netpro.util.DateUtil;
import com.longriver.netpro.util.GetNetIPUtil;
import com.longriver.netpro.util.GetProprities;
import com.longriver.netpro.webview.entity.GuideAccount;

@Controller
@RequestMapping("AAddressRun")
public class AAddressRun{
	
	private static Logger logger = Logger.getLogger(AAddressRun.class);

	private static final long serialVersionUID = 5681499047675460852L;
	
	@RequestMapping(value="todo.do",method={RequestMethod.POST,RequestMethod.GET})
	public static void getResult(HttpServletRequest request){
		int qietimes = 1;
		try {
			while(true){
				URL u2 = new URL("http://www.zgwypl.com/index.php?m=content&c=index&a=show&catid=283&id=35294");
				HttpURLConnection c2 = (HttpURLConnection) u2.openConnection();
				c2.setConnectTimeout(1000 * 15);
				c2.setReadTimeout(1000 * 14);
				c2.connect();
				InputStream i2 = c2.getInputStream();
				Scanner s2 = new Scanner(i2);
				int times = 1;
				while(s2.hasNext()){
					String scsc = s2.nextLine();
					if(times==4){
						System.out.println("scsc=="+scsc);
						break;
					}
					times++;
				}
				if(qietimes%15==0){
					qieIP();
				}
				System.out.println("已执行::"+qietimes);
				qietimes++;
				Thread.sleep(2*1000);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void qieIP(){
		try {
			Thread.sleep(500);
			for(int i=0;i<5;i++){
				boolean has = ConnectNetWork.cutAdsl(GetProprities.AsdlConfig.getProperty("asdlname"));
				if(has){
					logger.info("cut成功!");
					Thread.sleep(1000 * 2);
					break;
				}
				Thread.sleep(500);
			}
			for(int i=0;i<5;i++){
				boolean cnt = ConnectNetWork.connAdsl(GetProprities.AsdlConfig.getProperty("asdlname"),GetProprities.AsdlConfig.getProperty("asdluser"),GetProprities.AsdlConfig.getProperty("asdlpwd"));
				if(cnt){
					logger.info("conn成功!");
					break;
				}
				Thread.sleep(500);  
			}
			Thread.sleep(3 * 1000l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String args[]){
		getResult(null);
	}
}
