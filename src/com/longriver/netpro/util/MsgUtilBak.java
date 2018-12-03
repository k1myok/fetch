package com.longriver.netpro.util;

import java.util.HashMap;
import java.util.Map;

import com.longriver.netpro.fetchScript.util.Jdbc2MysqlMyMQ;

/**
 * 收发短信工具类
 * @author lilei
 */
public class MsgUtilBak {
	/*
	 * 发短信
	 * tel 发短信手机号
	 * msg 内容
	 * totel 发送到
	 * port 端口
	 */
	public static int sendMsg(String tel,String msg,String totel,String port){
		Map<String, String> params = new HashMap<String,String>();
		params.put("mbno", tel);
		params.put("msg", msg);
		params.put("totel", totel.replaceAll(" ", ""));
		params.put("comport", port);
//		String address = "http://118.190.172.70:8088/proMQ/phoneMsg/sendMsg.do";
//		System.out.println("params="+params.toString());
//		HttpDeal.post(GetProprities.PaginationConfig.getProperty("sendMsg"),params);
		int suc = 0;
		try {
			int id = Jdbc2MysqlMyMQ.sendMsg(tel, msg, totel, port);
			System.out.println("插入短信成功");
			for(int i=0;i<10;i++){
				Thread.sleep(1000 * 5);
				suc = Jdbc2MysqlMyMQ.getResultFsong(id);
				if(suc>0){
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return suc;
	}
	/*
	 * 收短信
	 * tel
	 * port
	 * history 1 可以查过去的
	 */
	public static String getMsg(String tel,String port,int history){
		Map<String, String> params = new HashMap<String,String>();
		params.put("mbno", tel);
		params.put("comport", port);
//		String address = "http://118.190.172.70:8088/proMQ/phoneMsg/getMsg.do";
//		return HttpDeal.post(GetProprities.PaginationConfig.getProperty("getMsg"),params);
		String msg = "";
		try {
			if(history==1){
				msg = Jdbc2MysqlMyMQ.getResultHis(tel);
			}else{
				int id = Jdbc2MysqlMyMQ.addTelMsg(tel, port);
				for(int i=0;i<10;i++){
					Thread.sleep(1000 * 5);
					msg = Jdbc2MysqlMyMQ.getResult(id);
					if(msg!=null && !msg.equals("")){
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}
	public static String getSinaCodeMsg(String tel,String port,int history){
		//您申请注册微博的验证码为：737568（30分钟内有效，如非本人操作请忽略或咨询4000960960，本条免费）。【新浪】
		String msg = getMsg(tel,port,history);
		if(msg==null || msg.equals("")) return null;
		int index1 = msg.indexOf("：")+1;
		int index2 = msg.indexOf("（");
		return msg.substring(index1, index2);
	}
	
	public static void main(String[] args) {
//		String ss = "您申请注册微博的验证码为：737568（30分钟内有效，如非本人操作请忽略或咨询4000960960，本条免费）。【新浪】";
//		System.out.println(getSinaCodeMsg(ss));
	}
}
