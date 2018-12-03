package com.longriver.netpro.webview.controller;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 新浪新闻跟帖app
 * @author rhy
 * @2017-6-28 上午9:38:52
 * @version v1.0
 */
public class SinaNewsCommentApp {

	static Logger logger = Logger.getLogger(SinaNewsCommentApp.class);
	
	public static void main(String[] args) {
	
		TaskGuideBean task = new TaskGuideBean();
		String articleUrl = "http://bj.sina.cn/news/2017-07-17/detail-ifyiakwa4236651.d.html?sinawapsharesource=newsapp";
		articleUrl = "http://bj.sina.cn/news/2017-07-17/detail-ifyiakur9022346.d.html?sinawapsharesource=newsapp";
		articleUrl = "http://bj.sina.cn/news/2017-07-17/detail-ifyiakwa4260402.d.html?sinawapsharesource=newsapp";
//		articleUrl = "http://bj.sina.cn/news/2017-07-17/detail-ifyiakur9020965.d.html?sinawapsharesource=newsapp";
		String content = "挣点钱嘛am";
		task.setAddress(articleUrl);
		task.setCorpus(content);
		String appHeaders = "POST http://newsapi.sina.cn/?resource=comment/post&accessToken=2.00AhFUNGe3vYNC7561bcd0d7H7yvKB&chwm=3023_0001&city=WMXX2972&connectionType=2&deviceId=7bedd60fcbb47ced5ec0a4cc56fd07772ed92c78&deviceModel=apple-iphone6&from=6063193012&idfa=9CB3856F-83EC-4194-9533-1EA3FD4150AA&idfv=BD7D3D44-8719-45F2-81FA-6B3909440586&imei=7bedd60fcbb47ced5ec0a4cc56fd07772ed92c78&location=39.977565%2C116.386466&osVersion=9.2&resolution=750x1334&seId=f1a88dd74d&sfaId=7bedd60fcbb47ced5ec0a4cc56fd07772ed92c78&ua=apple-iphone6__SinaNews__6.3.1__iphone__9.2&unicomFree=0&weiboSuid=ffcd03ac17&weiboUid=5696061086&wm=b207&rand=601&urlSign=63d9f3aa24 HTTP/1.1"+
				"Host: newsapi.sina.cn"+
				"Content-Type: application/x-www-form-urlencoded"+
				"Cookie: SUB=_2A250aRpsDeRhGeNI4lQR9i_MwzqIHXVX5UAkrDV9PUJbldBeLRnckWs9gluGwNK0X1BVOBsA6Icipj3u6g..; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhXBXyTYZqHJa.7hYL-.9q45NHD95QfSo.cehqpehncWs4Dqcjci--fiK.7i-8hi--fi-8siK.Ei--4i-2fi-zpi--Ni-iFiKysi--fi-isi-2Ni--ciKnEi-20"+
				"Connection: keep-alive"+
				"Connection: keep-alive"+
				"Accept: application/json"+
				"User-Agent: SinaNews/6.3.1 (iPhone; iOS 9.2; Scale/2.00)"+
				"Accept-Language: zh-Hans-CN;q=1, en-CN;q=0.9"+
				"Content-Length: 584"+
				"Accept-Encoding: gzip, deflate"+

					"accessToken=2.00AhFUNGe3vYNC7561bcd0d7H7yvKB&commentId=comos-fyiakwa4260402_0_default__fyiakwa4260402-comos-bj-cms&content=%E5%B0%91%E4%B8%80%E4%B8%AA%E9%9B%B6%E6%98%AF%E6%88%91%E7%9A%84%E5%B7%A5%E8%B5%84&link=http%3A//bj.sina.cn/news/2017-07-17/detail-ifyiakwa4260402.d.html?sinawapsharesource%3Dnewsapp%26from%3Dwb2source&nick=%E5%90%AC%E5%AF%92%E9%A5%B1%E7%BB%8F%E5%BF%A7%E6%82%A3&source=2032681696&title=%E5%8C%97%E4%BA%AC%E8%81%8C%E5%B7%A5%E5%B9%B3%E5%9D%87%E5%B7%A5%E8%B5%8492477%E5%85%83%20%E6%9C%80%E4%BD%8E%E5%B7%A5%E8%B5%84%E4%BF%9D%E9%9A%9C%E7%BA%BF23120%E5%85%83&toShare=1";
		
		appHeaders = "POST http://newsapi.sina.cn/?resource=comment/post&locFrom=0&seId=00385930af&deviceId=fb44c231fa7bc8b7&from=6063395012&weiboUid=5696280009&weiboSuid=11fe06a57b&imei=863777020216411&wm=b207&chwm=12040_0001&oldChwm=12040_0001&osVersion=4.2.2&connectionType=2&resolution=720x1280&city=WMXX2972&deviceModel=Coolpad__Coolpad__Coolpad+8297&location=39.977581%2C116.386426&link=&mac=18%3Adc%3A56%3Ad0%3A11%3A46&ua=Coolpad-Coolpad+8297__sinanews__6.3.3__android__4.2.2&osSdk=17&cmd_mac=18%3Adc%3A56%3Ad0%3A11%3A46%0A&aId=01AojFQdXsjOGreJWz8NFLv2aMwY3lOvcwKLeWkgjeTXlOMk8.&lDid=2463f80a-91a8-42b8-a473-b28767588853&accessToken=2.00BeAVNGe3vYNCa98a413b15IbAdLD&urlSign=d83528daaf&rand=264 HTTP/1.1"+
				"X-User-Agent: Coolpad-Coolpad 8297__sinanews__6.3.3__android__4.2.2"+
				"User-Agent: Coolpad-Coolpad 8297__sinanews__6.3.3__android__4.2.2"+
				"Content-Type: application/x-www-form-urlencoded; charset=UTF-8"+
				"Host: newsapi.sina.cn"+
				"Connection: Keep-Alive"+
				"Accept-Encoding: gzip"+
				"Content-Length: 704"+
				
				"content=%E4%BB%80%E4%B9%88%E6%97%B6%E5%80%99%E7%9A%84%E4%BA%8B%E5%91%80%EF%BC%8C%E8%BF%99%E6%98%AF&title=%E5%8C%97%E4%BA%AC%E4%B8%80%E5%A4%84%E5%85%B1%E4%BA%AB%E5%BA%8A%E9%93%BA%E5%85%B3%E9%97%A8%E5%81%9C%E4%B8%9A+%E7%AE%A1%E7%90%86%E8%80%85%E7%A7%B0%E8%A2%AB%E6%9F%A5%E5%B0%81&toShare=1&nick=%E5%AF%92%E5%AF%92%E5%8C%97%E9%97%A8%E9%94%81%E9%92%A5&accessToken=2.00BeAVNGe3vYNCa98a413b15IbAdLD&source=2032681696&link=http%3A%2F%2Fbj.sina.cn%2Fnews%2F2017-07-17%2Fdetail-ifyiakur9020965.d.html%3Fsinawapsharesource%3Dnewsapp&config=OS_TYPE%3D14%26OS_LANG%3Dzh%26SOFT_TYPE%3DSinaNews_v633%26LATITUDE%3D39.977581%26LONGITUDE%3D116.386426&commentId=comos-fyiakur9020965_0_default__fyiakur9020965-comos-bj-cms&";
		task.setAppInfo(appHeaders);
//		content = "加上了title";
//		content = "就这样写吧";
		try {
//			sinaCommentIos(task);
			sinaCommentAndroid(task);
		} catch (Exception e) {
			logger.error("sina comment exception...");
			e.printStackTrace();
		}
	}
	public static void toRun(TaskGuideBean task){
		if(task.getIsApp()==2){
			sinaCommentIos(task);
		}else{
			sinaCommentAndroid(task);
		}
	}
	@SuppressWarnings("unused")
	public static void sinaCommentIos(TaskGuideBean task){
//		String articleUrl,String content
		//http://bj.sina.cn/news/2017-07-12/detail-ifyhwehx5767070.d.html?sinawapsharesource=newsapp
		try{
			
		String articleUrl = task.getAddress();
		String content = task.getCorpus();
		String appHeaders = task.getAppInfo();
		
		JSONObject parseResults = getParamValue(articleUrl);
		String channel = parseResults.getString("channel");
		String newsid = parseResults.getString("newsid");
		String product = parseResults.getString("product");
		String index = parseResults.getString("index");
		
		logger.info("sina comment ios start...");
//		String nick = "听寒饱经忧患";
		String nick = "";
//		nick = "易冰必由之路";
		String title = "";
		String preUrl = "http://newsapi.sina.cn/?resource=comment/post" +
				"&accessToken=2.00oa7VNGe3vYNCf29b85e6150xW4G1" +
				"&chwm=3023_0001" +
				"&city=WMXX2972&connectionType=2" +
				"&deviceId=7bedd60fcbb47ced5ec0a4cc56fd07770289784d" +
				"&deviceModel=apple-iphone6" +
				"&from=6063193012" +
				"&idfa=9CB3856F-83EC-4194-9533-1EA3FD4150AA" +
				"&idfv=BD7D3D44-8719-45F2-81FA-6B3909440586" +
				"&imei=7bedd60fcbb47ced5ec0a4cc56fd07770289784d" +
				"&location=39.977565%2C116.386466" +
				"&osVersion=9.2" +
				"&resolution=750x1334" +
				"&seId=eca0ad16a8" +
				"&sfaId=7bedd60fcbb47ced5ec0a4cc56fd07770289784d" +
				"&ua=apple-iphone6__SinaNews__6.3.1__iphone__9.2" +
				"&unicomFree=0" +
				"&weiboSuid=4ab5bbacc2" +
				"&weiboUid=5696379754" +
				"&wm=b207" +
				"&rand=627" +
				"&urlSign=44b0fdc6de";//
		

		Map<String,String> headerMap = getHeaders(task.getAppInfo());
		
		preUrl = headerMap.get("preUrl");
		String cookie = headerMap.get("cookie");
		
		URL url = new URL(preUrl);
		HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
		
		String accessToken = preUrl.substring(preUrl.indexOf("accessToken")+12,preUrl.indexOf("chwm")-1);
		String news = articleUrl.substring(articleUrl.indexOf("//")+2,articleUrl.indexOf("."));
		//切换用户需要要换param、Cookie的值
//		http://bj.sina.cn/news/2017-07-17/detail-ifyiakwa4236651.d.html?sinawapsharesource=newsapp&from=wb2source
//		commentId=comos-fyhmpew3616676_0_gn__fyhmpew3616676-comos-news-cms
		//"&commentId="+newsid+"_0_"+channel+"__"+index+"-"+product+"-"+news+"-cms"+
		String param = "accessToken="+accessToken+"" +
				       "&commentId="+newsid+"_0_"+channel+"__"+index+"-"+product+"-"+news+"-cms"+
				       "&content="+URLEncoder.encode(content,"utf-8")+""+
					   "&link="+articleUrl+"&from=wb2source" +
					   "&nick="+nick+"" +
					   "&source=2032681696" +
					   "&title=" +
					   "&toShare=1";

		openConnection.addRequestProperty("Host", "newsapi.sina.cn");
		openConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//		openConnection.addRequestProperty("Cookie", "SINAGLOBAL=8278094271663.576.1500280105546; SUB=_2A250aAVdDeRhGeNI4lQS9yfLzjiIHXVX5bUVrDV9PUJbldBeLXOlkWsVkdclPnx8FJHGh1Gpw25k1Nrf0g..; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9W5Bx8gKoeeEZaxquPu-XLA85NHD95QfSo.ce0M4S0-XWs4Dqcjci--ciK.RiK.0i--fiKnci-z7i--fi-isiKnfi--NiK.Xi-zpi--Xi-z4iKyFi--Ri-zNi-8s; ULV=1500280105547:1:1:1:8278094271663.576.1500280105546:; genTime=1500280104; historyRecord={\"href\":\"http:////mil.sina.cn/mil_zt/zybj\",\"refer\":\"\"}; sina_ucode=YQpQMcpcYZ; statuid=__222.36.47.50_1500280104_0.61331100; statuidsrc=Mozilla%2F5.0+%28iPhone%3B+CPU+iPhone+OS+9_2+like+Mac+OS+X%29+AppleWebKit%2F601.1.46+%28KHTML%2C+like+Gecko%29+Mobile%2F13C75%3B+apple-iphone6__sinanews__6.3.1__iOS__9.2%60222.36.47.50%60http%3A%2F%2Fmil.sina.cn%2Fmil_zt%2Fzybj%3Fcre%3Dtianyi%26mod%3Dnmil%26loc%3D0%26r%3D-1%26doct%3D0%26rfunc%3D64%26tj%3Dnone%26s%3D0%26tr%3D73%26fromsinago%3D1%60%60__222.36.47.50_1500280104_0.61331100; ustat=__222.36.47.50_1500280104_0.61331100; vt=4");
		openConnection.addRequestProperty("Cookie", cookie);
		openConnection.addRequestProperty("Connection", "keep-alive");
		openConnection.addRequestProperty("Connection", "keep-alive");
		openConnection.addRequestProperty("Accept", "application/json");
		openConnection.addRequestProperty("User-Agent", "SinaNews/6.3.1 (iPhone; iOS 9.2; Scale/2.00)");
		openConnection.addRequestProperty("Accept-Language", "zh-Hans-CN;q=1, en-CN;q=0.9");
		openConnection.addRequestProperty("Content-Length", String.valueOf(param.length()));
		openConnection.addRequestProperty("Accept-Encoding", "gzip, deflate");
		
		openConnection.setDoInput(true);
		openConnection.setDoOutput(true);
//		openConnection.setUseCaches(false);
//		openConnection.setRequestMethod("POST");
		
		PrintWriter pw = new PrintWriter(openConnection.getOutputStream());
		pw.print(param);
		pw.flush();
		
		Scanner sc = new Scanner(openConnection.getInputStream(),"utf-8");
		String result = "";
		while(sc.hasNext()){
			result += sc.nextLine();
		}
		logger.info("sina comment ios:::"+result);
		
		JSONObject parseResult = JSON.parseObject(result);
		String data = parseResult.getString("data");
		
		JSONObject parseData = JSON.parseObject(data);
		String status = parseData.getString("message");
		
		if(status.equals("ok")){
			MQSender.toMQ(task,"");
			return;
		}else{
			MQSender.toMQ(task,"失败");
			return;
		}
		
		}catch(Exception e){
			e.printStackTrace();
			MQSender.toMQ(task,"失败");
			return;
		}
		
	}
	/**
	 * 获取跟帖相关参数
	 * @param articleUrl
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getParamValue(String articleUrl) throws Exception{
	
		URL url = new URL(articleUrl);
		URLConnection openConnection = url.openConnection();
		openConnection.connect();
		
		Scanner sc = new Scanner(openConnection.getInputStream(),"utf-8");
		
		String result = "";
		while(sc.hasNext()){
			
			result += sc.nextLine();
		}
		
		result = result.substring(result.indexOf("commentConfig")+15);
		result = result.substring(0,result.indexOf(";"));
		result = "{"+result.substring(result.indexOf("channel"));
		JSONObject parseResult = JSON.parseObject(result);
		return parseResult;
	}

	/**
	 * 新浪新闻android
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unused")
	public static void sinaCommentAndroid(TaskGuideBean task){
		
		logger.info("sina comment android start...");
		try{
			
		String articleUrl = task.getAddress();
		String content = task.getCorpus();
		String appHeaders = task.getAppInfo();
		
		JSONObject parseResults = getParamValue(articleUrl);
		String channel = parseResults.getString("channel");
		String newsid = parseResults.getString("newsid");
		String product = parseResults.getString("product");
		String index = parseResults.getString("index");
		
		String preUrl = "http://newsapi.sina.cn/?resource=comment/post" +
				"&locFrom=0" +
				"&seId=20c6e5d0b1" +
				"&deviceId=fb44c231fa7bc8b7" +
				"&from=6063395012" +
				"&weiboUid=5696280009" +
				"&weiboSuid=11fe06a57b" +
				"&imei=863777020216411" +
				"&wm=b207" +
				"&chwm=12040_0001" +
				"&oldChwm=12040_0001" +
				"&osVersion=4.2.2" +
				"&connectionType=2" +
				"&resolution=720x1280" +
				"&city=WMXX2972" +
				"&deviceModel=Coolpad__Coolpad__Coolpad+8297" +
				"&location=39.977544%2C116.386426&link=" +
				"&mac=18%3Adc%3A56%3Ad0%3A11%3A46" +
				"&ua=Coolpad-Coolpad+8297__sinanews__6.3.3__android__4.2.2" +
				"&osSdk=17" +
				"&cmd_mac=18%3Adc%3A56%3Ad0%3A11%3A46%0A" +
				"&aId=01AojFQdXsjOGreJWz8NFLv2aMwY3lOvcwKLeWkgjeTXlOMk8.&lDid=2463f80a-91a8-42b8-a473-b28767588853" +
				"&accessToken=2.00BeAVNGe3vYNCa98a413b15IbAdLD" +
				"&urlSign=e6ee918308" +
				"&rand=903";
		
		Map<String,String> headerMap = getHeaders(task.getAppInfo());
		
		preUrl = headerMap.get("preUrl");
//		String cookie = headerMap.get("cookie");
		URL url = new URL(preUrl);
		
		HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
		
		String accessToken = preUrl.substring(preUrl.indexOf("accessToken")+12);
		accessToken = accessToken.substring(0,accessToken.indexOf("&"));
		String news = articleUrl.substring(articleUrl.indexOf("//")+2,articleUrl.indexOf("."));
		
		String param = "content="+URLDecoder.decode(content,"utf-8")+"" +
				"&title=" +
				"&toShare=1" +
				"&nick=" +
				"&accessToken="+accessToken+"" +
				"&source=2032681696" +
				"&link="+articleUrl+"" +
				"&config=OS_TYPE=14&OS_LANG=zh&SOFT_TYPE=SinaNews_v633&LATITUDE=39.97747&LONGITUDE=116.386414" +
				"&commentId="+newsid+"_0_"+channel+"__"+index+"-"+product+"-"+news+"-cms";
		
		openConnection.addRequestProperty("X-User-Agent", "Coolpad-Coolpad 8297__sinanews__6.3.3__android__4.2.2");
		openConnection.addRequestProperty("User-Agent", "Coolpad-Coolpad 8297__sinanews__6.3.3__android__4.2.2");
		openConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		openConnection.addRequestProperty("Host", "newsapi.sina.cn");
		openConnection.addRequestProperty("Connection", "Keep-Alive");
		openConnection.addRequestProperty("Accept-Encoding", "gzip");
		openConnection.addRequestProperty("Content-Length", String.valueOf(param.length()));
		
		openConnection.setDoInput(true);
		openConnection.setDoOutput(true);
		openConnection.setUseCaches(false);
		openConnection.setRequestMethod("POST");
		
		PrintWriter pw = new PrintWriter(openConnection.getOutputStream());
		pw.print(param);
		pw.flush();
		
		Scanner sc = new Scanner(openConnection.getInputStream(),"utf-8");
		
		String result = "";
		while(sc.hasNext()){
			
			result += sc.nextLine();
		}
		
		logger.info("sina comment android result:::"+result);
		
		JSONObject parseResult = JSON.parseObject(result);
		String data = parseResult.getString("data");
		
		if(StringUtils.isNotBlank(data)){
		JSONObject parseData = JSON.parseObject(data);
		String status = parseData.getString("message");
		
		if(status.equals("ok")){
			MQSender.toMQ(task,"");
			return;
		}else{
			MQSender.toMQ(task,"失败");
			return;
		}
		}else{
			MQSender.toMQ(task,"失败");
			return;
		}
		}catch(Exception e){
			MQSender.toMQ(task,"失败2");
			return;
		}
	}

	/**
	 * 获取头信息
	 * @param appHeaders
	 * @return
	 */
	private static Map<String, String> getHeaders(String appHeaders) {
		
		Map<String,String> map = new HashMap<String, String>();
		appHeaders = appHeaders.substring(appHeaders.indexOf("http"));
		String preUrl = appHeaders.substring(0, appHeaders.indexOf("HTTP/1.1")).trim();
		String cookie = appHeaders.substring(appHeaders.indexOf("Cookie")+7, appHeaders.indexOf("Connection")).trim();
		
		map.put("preUrl", preUrl);
		map.put("cookie", cookie);
		
		return map;
	}
}
