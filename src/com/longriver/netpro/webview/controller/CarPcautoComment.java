package com.longriver.netpro.webview.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

//太平洋汽车网
public class CarPcautoComment {

	public static void main(String arsg[]){
		try{
			TaskGuideBean b = new TaskGuideBean();
			b.setNick("13366550854@163.com");
			b.setPassword("shikai");
			b.setAddress("http://www.pcauto.com.cn/qcbj/853/8533691.html");
			b.setCorpus("k看着是不错,不知道感受如何");
			b.setCorpus("这,有点不好说呢");
			toRun(b);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void toRun(TaskGuideBean taskdo){
		try {
			String userId = URLEncoder.encode(taskdo.getNick(), "utf-8");
			String pwd = taskdo.getPassword();
			String contents = taskdo.getCorpus();
			String sUrl = taskdo.getAddress();

			String url = URLEncoder.encode(sUrl, "utf-8");
			String html = openUrl(sUrl, "gbk");
			String regex2 = "<title>([\\d\\D]*?)</title>";
			String title = URLEncoder.encode(
					URLEncoder.encode(getContent(regex2, html), "utf-8"),
					"utf-8");
			String cookie = "";
			// _FakeX509TrustManager.allowAllSSL();
			URL u1 = new URL(
					"http://passport2.pcauto.com.cn/passport2/passport/login.jsp");

			HttpURLConnection c1 = (HttpURLConnection) u1.openConnection();
			String fa = "auto_login=3600&"
					+ "return=http%3A%2F%2Fwww1.pcauto.com.cn%2Fglobal%2F2013%2Fblank.html"
					+ "&username=" + userId + "&password=" + pwd
					+ "&loginBtn=%B5%C7%26%23160%3B%C2%BC";
			c1.addRequestProperty("Host", "passport2.pcauto.com.cn");
			c1.addRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.2; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0");
			c1.addRequestProperty("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			c1.addRequestProperty("Accept-Language	",
					"zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			c1.addRequestProperty("Connection", "keep-alive");
			c1.addRequestProperty("Referer", sUrl);
			c1.addRequestProperty("X-Requested-With", "XMLHttpRequest");
			c1.addRequestProperty("Content-Length", String.valueOf(fa.length()));
			c1.setInstanceFollowRedirects(false);
			c1.setDoInput(true);
			c1.setDoOutput(true);
			PrintWriter foa = new PrintWriter(c1.getOutputStream());
			foa.print(fa);
			foa.flush();
			InputStream fi1 = c1.getInputStream();
			Map<String, List<String>> m1 = c1.getHeaderFields();
			for (Map.Entry<String, List<String>> entry : m1.entrySet()) {
				if (entry.getKey() != null
						&& entry.getKey().indexOf("Set-Cookie") > -1) {
					for (String value : entry.getValue()) {
						cookie = cookie
								+ value.substring(0, value.indexOf(";")) + ";";
					}
				}
			}
			System.out.println(cookie);

			URL fu = new URL(
					"http://cmt.pcauto.com.cn/action/comment/create.jsp");
			HttpURLConnection fc = (HttpURLConnection) fu.openConnection();
			String fp = "isEncode=1&content="
					+ URLEncoder.encode(contents, "gbk") + ""
					+ "&captcha=&needCaptcha=0&" + "url=" + url + "&title="
					+ title
					+ "&columnId=&area=&replyFloor2=0&partId=&syncsites=";
			System.out.println(fp);
			fc.addRequestProperty("Host", "cmt.pcauto.com.cn");
			fc.addRequestProperty(
					"User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
			fc.addRequestProperty("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			fc.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			fc.addRequestProperty("Referer", sUrl);
			fc.addRequestProperty("Cookie", cookie);
			fc.addRequestProperty("Connection", "keep-alive");
			fc.addRequestProperty("Content-Length", String.valueOf(fp.length()));
			fc.setInstanceFollowRedirects(false);
			fc.setDoInput(true);
			fc.setDoOutput(true);
			PrintWriter fo = new PrintWriter(fc.getOutputStream());
			fo.print(fp);
			fo.flush();
			InputStream fi = fc.getInputStream();
			Scanner fs = new Scanner(fi);
			while (fs.hasNext()) {
				String scsc2 = fs.nextLine();
				System.out.println(scsc2);
			}
			MQSender.toMQ(taskdo,"");
		} catch (Exception e) {
			MQSender.toMQ(taskdo,"报错失败");
			e.printStackTrace();
		}

	}

	public static String openUrl(String currentUrl, String charset) {
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
			br = new BufferedReader(new InputStreamReader(is, charset));
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

	private static String getContent(String regex, String text) {
		String content = "";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			content = matcher.group(1).toString();
			System.out.println(content);
		}
		return content;
	}

}
