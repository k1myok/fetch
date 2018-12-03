package com.longriver.netpro.webview.cookieController;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Scanner;

import org.junit.Test;

import com.longriver.netpro.common.sina.SinaIdMidConverter;
import com.longriver.netpro.common.sina.WeiboSina;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.TaskGuideBean;

/**
 * 微博评论cookie方式
 * @author rhy
 * @date 2018-6-27 上午10:25:20
 * @version V1.0
 */
public class WeiboCommentCookie {

	public static void main(String[] args) {
		
		TaskGuideBean task = new TaskGuideBean();
		task.setCookieData("sso_info=v02m6alo5qztLGNs4S5jYOUsYyzlLONh5ymnZalpI-TmLSNo6C2jZOAsI2jpMA=;" +
				"LT=1529978428;" +
				"ALF="+System.currentTimeMillis()+";" +
				"ALC=ac%3D0%26bt%3D1529978428%26cv%3D5.0%26et%3D1561514428%26ic%3D1969532984%26login_time%3D1529978428%26scf%3D%26uid%3D6468650069%26vf%3D0%26vs%3D0%26vt%3D0%26es%3Dc25bed05193a112ac6dc1a946c0d6f48;" +
				"SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhakIHWNxfiCgkTk3PkD9l_5NHD95QcShqRSo-7ehq4Ws4DqcjeeKMp1KBfeKefe0xN;" +
				"SUB=_2A252Ne5sDeRhGeBK7VoX9S7MzTWIHXVVQ1ikrDV_PUNbm9BeLVf3kW9NR5JbtJWIJx7srpLkYdks7_1Sy92jUZvb;" +
				"tgc=TGT-NjQ2ODY1MDA2OQ==-1529978428-gz-7998234C9D1EFD57ED529DF1D1FC0CFF-1;");
//		
//		SINAGLOBAL=7758333409141.176.1526112777379; 
//		_s_tentry=news.ctocio.com.cn; 
//		YF-Ugrow-G0=b02489d329584fca03ad6347fc915997; 
//		SSOLoginState=1529547427; 
//		wvr=6; 
//		YF-V5-G0=8a3c37d39afd53b5f9eb3c8fb1874eec; 
//		UOR=,,login.sina.com.cn; 
//		Apache=8923913637284.559.1529547430199; 
//		ULV=1529547430218:5:2:2:8923913637284.559.1529547430199:1528960009074; 
//		YF-Page-G0=3d55e26bde550ac7b0d32a2ad7d6fa53; 
//		wb_timefeed_6020335519=1; 
//		SCF=AkMJrgksAyF_oG0WvRAyV4wN7oaR8IoiFUWRyOeRgW1ZNZU2KqCDxfjXHaGwO1GH_dYy_OuO5uC6kYJsZM70NOA.; 
//		SUB=_2A252N3HeDeRhGeBO6VIS8yvJyjWIHXVVReQWrDV8PUJbmtBeLUTakW9NSjgY3kJyq8_mzoOwDY5bIxdjU9nj1omQ; 
//		SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WFW2a34Hb0XLnwUxIAgg13j5JpX5K-hUgL.Foq7eo50e0-feK.2dJLoIEBLxKnLB.2LB-zLxK.LBK.LB-eLxKBLBonLB.zLxK.L1-zLB-2t; 
//		SUHB=0S7C8AtI91szf3; 
//		ALF=1561605388
				
//		SINAGLOBAL=4754518750597.381.1528708304377; 
//		YF-Page-G0=c81c3ead2c8078295a6f198a334a5e82; 
//		_s_tentry=-; 
//		Apache=3038739197785.891.1529629501310; 
//		ULV=1529629501377:2:2:1:3038739197785.891.1529629501310:1528708304644; 
//		YF-Ugrow-G0=57484c7c1ded49566c905773d5d00f82; 
//		YF-V5-G0=f59276155f879836eb028d7dcd01d03c; 
//		login_sid_t=acb0ef3c3d3b3d1ef9a4c927a72cd2ba; 
//		wb_timefeed_6468650069=1; 
//		WBtopGlobal_register_version=65304e661ef12b19; 
//		cross_origin_proto=SSL; 
//		WBStorage=5548c0baa42e6f3d|undefined; 
//		UOR=,,login.sina.com.cn; 
//		wb_view_log=1920*10801; 
//		SSOLoginState=1530067654; 
//		SCF=Ao77XKmAufgdCmR6SXY7BHVAXRR5WOb5x6CQm6Lb5icePFl6SDSL_Xh-zIxHtRKvKR4_yi0hgasKWeHfrpdVatA.; 
//		SUB=_2A252NoqWDeRhGeBK7lIZ8SnPzjWIHXVVRfterDV8PUNbmtBeLXP9kW9NR7He9i9i6lgq-Wybv0AtJCKwWGAFkoLa; 
//		SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhKE0U.siOBwUW2XQM_PNfH5JpX5K2hUgL.FoqXSK5ReKM0SK.2dJLoI74DqNLkdsLV90241Kepentt; 
//		SUHB=04WuIzGn1gcdH-; 
//		ALF=1561603654; 
//		un=17080625988; 
//		wvr=6
			
		//浏览器cookie
		task.setCookieData("Apache=3727636104298.231.1530071976156; ULV=1530071976223:1:1:1:3727636104298.231.1530071976156:; SINAGLOBAL=3727636104298.231.1530071976156; YF-Ugrow-G0=9642b0b34b4c0d569ed7a372f8823a8e; SUB=_2A252N3vwDeRhGeBK7VoX8y_LyT-IHXVVReo4rDV8PUNbmtBeLUfekW9NR5J5nhtmZrmmFp4Ew2y39BWi7-bEzUwf; SCF=AkaCFOPJ65LpQmHwRspkT2N2XrwU1HFh9fNeP15KHg8R1Jx7JGaRBSRdd0e8q1GruPvIYxXmm6rEatfMKO7wa0w.; SUHB=0laFYww0sAo0DD; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WFpHiWo1mZPd7a6JD6s7HE05JpX5K2hUgL.FoqXSonce02Neoe2dJLoI0jLxKqL1heL1h-LxK-L1K-L1hqLxKBLBonL12zLxKML1-2L1hBLxKBLB.eLBKBLxKqL1hBL1-9k1hnNeBtt; ALF=1561607967; YF-Page-G0=8fee13afa53da91ff99fc89cc7829b07; YF-V5-G0=b4445e3d303e043620cf1d40fc14e97a; SSOLoginState=1530071968; _s_tentry=-; ");
		task.setCorpus("现在学生学习可真好");
		task.setCorpus("皇家马德里的球迷");
		task.setCorpus("一首凉凉送给德国对");
		task.setAddress("https://weibo.com/5056645402/G3mKyjCnM?from=page_1005055056645402_profile&wvr=6&mod=weibotime");
		task.setUrl("http://weibo.com/6468631723/");
		toComment(task);
	}
	
	/**
	 * 微博评论
	 * @param task
	 */
	public static void toComment(TaskGuideBean task) {
		try{
		URL url = new URL("https://weibo.com/aj/v6/comment/add?ajwvr=6&__rnd="+System.currentTimeMillis());
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		
		String mid = getMid(task);
// 		String uid = task.getCookieData().substring(task.getCookieData().indexOf("uid%3D")+6);
// 		uid = uid.substring(0,uid.indexOf("%26"));
		String uid = "6468631723";
 		String pdetail = getPdetail(task);
		String param = "act=post" +
				"&mid="+mid+"" +
				"&uid="+uid+"" +
				"&forward=0" +
				"&isroot=0" +
				"&content="+URLEncoder.encode(task.getCorpus(),"utf-8")+"" +
				"&location=page_100505_single_weibo" +
				"&module=bcommlist" +
				"&pdetail="+pdetail+"" +
				"&_t=0";
		
		connection.addRequestProperty("Host", "weibo.com");
		connection.addRequestProperty("Connection", "keep-alive");
		connection.addRequestProperty("Content-Length", String.valueOf(param.length()));
		connection.addRequestProperty("Origin", "https://weibo.com");
		connection.addRequestProperty("X-Requested-With", "XMLHttpRequest");
		connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36 LBBROWSER");
		connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.addRequestProperty("Accept", "*/*");
		connection.addRequestProperty("Referer", task.getAddress());
		connection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
		connection.addRequestProperty("Cookie", task.getCookieData());
		
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		connection.setRequestMethod("POST");
		
		PrintWriter pw = new PrintWriter(connection.getOutputStream());
		pw.print(param);
		pw.flush();
		
		Scanner sc = new Scanner(connection.getInputStream(),"utf-8");
		String result = "";
		while(sc.hasNext()){
			
			result += sc.nextLine();
		}
		String content = "";
		content = WeiboSina.getResultContent(result,content,task);
		
		isSuccess(task, content);
		
		}catch(Exception e){
			
			e.printStackTrace();
			isSuccess(task, "发生异常");
		}
	}
	/**
	 * 获取pdetail
	 * @param task
	 * @return
	 */
	private static String getPdetail(TaskGuideBean task) {
		
		String pdetail = task.getAddress().substring(task.getAddress().indexOf("from=page_")+10,task.getAddress().indexOf("_profile"));
		System.out.println(pdetail);
		return pdetail;
	}
	/**
	 * 获取mid
	 * @param task
	 * @return
	 */
	public static String getMid(TaskGuideBean task) {
		String midEncode = task.getAddress().substring(task.getAddress().lastIndexOf("/")).replace("/", "");
		if(midEncode.indexOf("?") > -1){
			midEncode = midEncode.substring(0, midEncode.indexOf("?"));
		}
		if(midEncode.indexOf("#") > -1){
			midEncode = midEncode.substring(0, midEncode.indexOf("#"));
		}
		String mid = SinaIdMidConverter.midToId(midEncode);
		System.out.println(mid);
		return mid;
	}
	/**
	 * 判断是否成功
	 */
	public static void isSuccess(TaskGuideBean task,String msg){
		MQSender.toMQ(task,msg);
	}
	
	@Test
	public void getTask() throws IOException{
		URL u5 = new URL("https://weibo.com/aj/comment/add?_wv=5&__rnd=" + new Date().getTime());
	    HttpURLConnection c5 = (HttpURLConnection) u5.openConnection();
	    
//	    c5.getHeaderField(name);
	}
}