package com.longriver.netpro.webview.controller;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.longriver.netpro.common.sina.SData;
import com.longriver.netpro.common.sina.WeiboLoginJietu;
import com.longriver.netpro.common.sina.WeiboSina;
import com.longriver.netpro.util.Configur;
import com.longriver.netpro.util.GeneChar;
import com.longriver.netpro.util.GetProprities;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * sina微博注册后完善资料 添加教育,公司,联系方式等信息
 * @author lilei
 * @2018-01-25 10:10:04
 * @version v1.0
 */
public class WeiboRegPerData {
	public static boolean process = false;
	public static boolean isSend = false;
	public static void main(String[] args) {
		process = true;
		TaskGuideBean task = new TaskGuideBean();
		task.setNick("17194518019");
		task.setNick("17194517981");
		task.setNick("17194517993");
		task.setNick("17194517982");
		task.setPassword("chwx1234");
		torun(task);
	}

	public static void torun(TaskGuideBean task){
		isSend = true;
		toLogin(task);
	}
	public static void toLogin(TaskGuideBean task){
		SData data =new SData();
		data.put("user_account_id", task.getNick());
		data.put("user_account_pw", task.getPassword());
		try {
			String skeyCookie = WeiboSina.getCookie(data,0);
			wanshaninfo(task,skeyCookie);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void wanshaninfo(TaskGuideBean task,String skeyCookie) {
		try{
			//联系信息
			contactInfo(skeyCookie,task);
			//公司信息
			companyInfo(skeyCookie,task);
			//教育信息
			eduInfo(skeyCookie,task);
		}catch(Exception e){
			System.out.println("异常");
			e.printStackTrace();
		}
		System.out.println("完善资料结束------------------");
		System.out.println("完善资料结束------------------");
	}
	public static void eduInfo(String skeyCookie,TaskGuideBean task){
		try {
			URL u5 = new URL("https://account.weibo.com/set/aj/iframe/edupost");
			HttpURLConnection c5 = (HttpURLConnection) u5.openConnection();
			String paramss = "name=北京理工大学&school_type=1&start=2016&remark=&privacy=2&school_province=11&school_id=243976";
			System.out.println(paramss);
			System.out.println("paramss=="+paramss);
			c5.addRequestProperty("Accept", "*/*");
			c5.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
			c5.addRequestProperty("Connection", "keep-alive");
			c5.addRequestProperty("Content-Length", String.valueOf(paramss.length()));
			c5.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			c5.addRequestProperty("Host", "account.weibo.com");
			c5.addRequestProperty("Origin", "https://account.weibo.com");
			c5.addRequestProperty("Referer", "https://account.weibo.com/set/iframe?skin=skin048");
			c5.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3141.7 Safari/537.36");
			c5.addRequestProperty("X-Requested-With", "XMLHttpRequest");
			c5.addRequestProperty("Cookie", skeyCookie);
			
			c5.setDoInput(true);
			c5.setDoOutput(true);
			c5.setConnectTimeout(1000 * 30);
			c5.setReadTimeout(1000 * 20);
			PrintWriter o5 = new PrintWriter(c5.getOutputStream());
			o5.print(paramss);
			o5.flush();
			InputStream i5 = null;
			try{
				i5 = c5.getInputStream();
			}catch(Exception e){
				e.printStackTrace();
			}
			Scanner s5 = new Scanner(i5);
			while(s5.hasNext()){
				String scsc = StringUtil.decodeUnicode(s5.nextLine());
				System.out.println("教育Info=="+scsc);
				String cc = WeiboSina.getResultContent(scsc,"",task);
				System.out.println(task.getCode()+"=="+cc);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	public static void companyInfo(String skeyCookie,TaskGuideBean task){
		try {
			URL u5 = new URL("https://account.weibo.com/set/aj/iframe/companypost");
			HttpURLConnection c5 = (HttpURLConnection) u5.openConnection();
			String paramss = getPrams();
			System.out.println(paramss);
			System.out.println("paramss=="+paramss);
			c5.addRequestProperty("Accept", "*/*");
			c5.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
			c5.addRequestProperty("Connection", "keep-alive");
			c5.addRequestProperty("Content-Length", String.valueOf(paramss.length()));
			c5.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			c5.addRequestProperty("Host", "account.weibo.com");
			c5.addRequestProperty("Origin", "https://account.weibo.com");
			c5.addRequestProperty("Referer", "https://account.weibo.com/set/iframe?skin=skin048");
			c5.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3141.7 Safari/537.36");
			c5.addRequestProperty("X-Requested-With", "XMLHttpRequest");
			c5.addRequestProperty("Cookie", skeyCookie);
			
			c5.setDoInput(true);
			c5.setDoOutput(true);
			c5.setConnectTimeout(1000 * 30);
			c5.setReadTimeout(1000 * 20);
			PrintWriter o5 = new PrintWriter(c5.getOutputStream());
			o5.print(paramss);
			o5.flush();
			InputStream i5 = null;
			try{
				i5 = c5.getInputStream();
			}catch(Exception e){
				e.printStackTrace();
			}
			Scanner s5 = new Scanner(i5);
			while(s5.hasNext()){
				String scsc = StringUtil.decodeUnicode(s5.nextLine());
				System.out.println("公司Info=="+scsc);
				String cc = WeiboSina.getResultContent(scsc,"",task);
				System.out.println(task.getCode()+"=="+cc);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	public static String getPrams(){
		String paramss = "";
		int ind = StringUtil.getRandom();
		try {
			if(ind%4==1){
				paramss = "name="+URLEncoder.encode("北京鼎盛科技","utf-8")+
				"&remark="+URLEncoder.encode("销售部","utf-8")+
				"&start=2017&end=2018&province=11&city=2&privacy=2";
			}else if(ind%4==2){
				paramss = "name="+URLEncoder.encode("若太科技","utf-8")+
				"&remark="+URLEncoder.encode("技术部","utf-8")+
				"&start=2017&end=2018&province=11&city=3&privacy=2";
			}else if(ind%4==3){
				paramss = "name="+URLEncoder.encode("北京天运","utf-8")+
				"&remark="+URLEncoder.encode("技术部","utf-8")+
				"&start=2017&end=2018&province=11&city=4&privacy=2";
			}else{
				paramss = "name="+URLEncoder.encode("北京全创有限公司","utf-8")+
				"&remark="+URLEncoder.encode("市场部","utf-8")+
				"&start=2017&end=2018&province=11&city=6&privacy=2";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return paramss;
	}
	/**
	 * 联系信息
	 */
	public static void contactInfo(String skeyCookie,TaskGuideBean task){
		try {
			URL u5 = new URL("https://account.weibo.com/set/aj/iframe/editconnectpost");
			HttpURLConnection c5 = (HttpURLConnection) u5.openConnection();
			String paramss = "email="+task.getNick()+"@163.com"+
			"&qq=3"+StringUtil.randomQq()+
			"&msn=&pub_email=1&pub_qq=1&pub_msn=1";
			System.out.println("paramss=="+paramss);
			c5.addRequestProperty("Accept", "*/*");
			c5.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
			c5.addRequestProperty("Connection", "keep-alive");
			c5.addRequestProperty("Content-Length", String.valueOf(paramss.length()));
			c5.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			c5.addRequestProperty("Host", "account.weibo.com");
			c5.addRequestProperty("Origin", "https://account.weibo.com");
			c5.addRequestProperty("Referer", "https://account.weibo.com/set/iframe?skin=skin048");
			c5.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3141.7 Safari/537.36");
			c5.addRequestProperty("X-Requested-With", "XMLHttpRequest");
			c5.addRequestProperty("Cookie", skeyCookie);
			
			c5.setDoInput(true);
			c5.setDoOutput(true);
			c5.setConnectTimeout(1000 * 30);
			c5.setReadTimeout(1000 * 20);
			PrintWriter o5 = new PrintWriter(c5.getOutputStream());
			o5.print(paramss);
			o5.flush();
			InputStream i5 = null;
			try{
				i5 = c5.getInputStream();
			}catch(Exception e){
				e.printStackTrace();
			}
			Scanner s5 = new Scanner(i5);
			while(s5.hasNext()){
				String scsc = StringUtil.decodeUnicode(s5.nextLine());
				System.out.println("联系Info=="+scsc);
				String cc = WeiboSina.getResultContent(scsc,"",task);
				System.out.println(task.getCode()+"=="+cc);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	/**
	 * 判断是否成功
	 */
//	public static void isSuccess(TaskGuideBean task,String msg){
//		if(isSend){
//			isSend = false;
//			MQSender.toMQ(task,msg);
//		}
//	}
	
}
