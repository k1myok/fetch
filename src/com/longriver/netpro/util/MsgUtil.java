package com.longriver.netpro.util;

import com.longriver.netpro.fetchScript.util.Jdbc2MysqlSpcard;
import com.longriver.netpro.fetchScript.util.Jdbc2MysqlSpcard3;
import com.longriver.netpro.fetchScript.util.Jdbc2MysqlSpcardNew;

/**
 * 收发短信工具类
 * @author lilei
 */
public class MsgUtil {
	
	/**
	 * 发短信
	 * 返回1成功  0失败
	 * 4/5/6设备1,2,3
	 */
	public static int sendMsg(int shebei,String tel,String msg,String totel,String port){
		int suc = 0;
		try {
			System.out.println("发送短信号码:"+tel);
			int portNumber = Integer.parseInt(port)/1000;
			int id = 0;
			if(shebei==4) id = Jdbc2MysqlSpcard.sendMsg(msg, totel, portNumber);
			else if(shebei==5) id = Jdbc2MysqlSpcardNew.sendMsg(msg, totel, portNumber);
			else if(shebei==6) id = Jdbc2MysqlSpcard3.sendMsg(msg, totel, portNumber);

			else System.out.println("未查询到要使用的设备");
			System.out.println("插入短信成功");
			for(int i=0;i<10;i++){
				Thread.sleep(1000 * 5);
				if(shebei==4) suc = Jdbc2MysqlSpcard.getResultFsong(id);
				else if(shebei==5) suc = Jdbc2MysqlSpcardNew.getResultFsong(id);
				else if(shebei==6) suc = Jdbc2MysqlSpcard3.getResultFsong(id);

				System.out.println("suc=="+suc);
				if(suc>1){
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(suc==2) return 1;
		else return 0;
	}
	/*
	 * 收短信
	 * tel
	 * port
	 * history 1 可以查过去的
	 */
	public static String getMsg(int shebei,String tel){
		String msg = "";
		try {
			tel = String.valueOf(Integer.parseInt(tel)/1000);
			for(int i=0;i<15;i++){
				Thread.sleep(1000 * 5);
				if(shebei==4) msg = Jdbc2MysqlSpcard.getResultHis(tel);
				else if(shebei==5) msg = Jdbc2MysqlSpcardNew.getResultHis(tel);
				else if(shebei==6) msg = Jdbc2MysqlSpcard3.getResultHis(tel);
				else System.out.println("未查询到要使用的设备");
				if(msg!=null && !msg.equals("")){
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}
	/**
	 * 接收新浪短信
	 * @param shebei
	 * @param port
	 * @return
	 */
	public static String getSinaCodeMsg(int shebei,String port){
		//欢迎使用网易邮箱服务，验证码698283。网易自营电商<网易严选>1000元礼包派发中，下载APP领取 u.163.com/y
		int portNumber = Integer.parseInt(port)/1000;
		String msg = getMsg(shebei,portNumber+"");
		if(msg==null || msg.equals("")) return null;
		int index1 = msg.indexOf("：")+1;
		int index2 = msg.indexOf("（");
		return msg.substring(index1, index2);
	}
	/**
	 * 接收网易短信
	 * @param shebei
	 * @param port
	 * @return
	 */
	public static String getWyCodeMsg(int shebei,String port){
//		String ss = "欢迎使用网易邮箱服务，验证码698283。网易自营电商<网易严选>1000元礼包派发中，下载APP领取 u.163.com/y";
		int portNumber = Integer.parseInt(port)/1000;
		String msg = getMsg(shebei,portNumber+"");
		if(msg==null || msg.equals("")) return null;
		int index1 = msg.indexOf("验证码")+3;
		int index2 = msg.indexOf("。");
		return msg.substring(index1, index2);
	}
	/**
	 * 切换卡池
	 * @param shebei
	 * @param port
	 * @return
	 */
	public static int switchCard(int shebei,String port){
		System.out.println("设备:"+shebei);
		int suc = 0;
		int result = 0;
		try {
			int portNumber = Integer.parseInt(port)%1000;
			String qieport = "AP$SIM=0,"+portNumber;
			
			if(shebei==4){
				suc = Jdbc2MysqlSpcard.switchCard(qieport);
				if(suc<=0) suc = Jdbc2MysqlSpcard.switchCard(qieport);//插入失败,再试一次
			}else if(shebei==5){
				suc = Jdbc2MysqlSpcardNew.switchCard(qieport);
				if(suc<=0) suc = Jdbc2MysqlSpcardNew.switchCard(qieport);//插入失败,再试一次
			}else if(shebei==6){
				suc = Jdbc2MysqlSpcard3.switchCard(qieport);
				if(suc<=0) suc = Jdbc2MysqlSpcard3.switchCard(qieport);//插入失败,再试一次
			}else System.out.println("未查询到要使用的设备");
			
			if(suc>0){
				for(int i=0;i<20;i++){
					if(shebei==4) result = Jdbc2MysqlSpcard.switchCardResult(suc);
					else if(shebei==5) result = Jdbc2MysqlSpcardNew.switchCardResult(suc);
					else if(shebei==6) result = Jdbc2MysqlSpcard3.switchCardResult(suc);

					if(result>0){
						System.out.println("卡池切换成功!");
						break;
					}
					Thread.sleep(1000*5);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(result==2) return 1;
		else return 0;
	}
	public static void main(String[] args) {
//		String ss = "欢迎使用网易邮箱服务，验证码698283。网易自营电商<网易严选>1000元礼包派发中，下载APP领取 u.163.com/y";
//		int index1 = ss.indexOf("验证码")+3;
//		int index2 = ss.indexOf("。");
//		System.out.println(ss.substring(index1, index2));
//		System.out.println(getSinaCodeMsg("11032"));
//		System.out.println(getMsg("11"));
//		System.out.println(1002/1000);
	}
}
