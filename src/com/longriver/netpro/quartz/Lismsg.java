package com.longriver.netpro.quartz;

import org.apache.log4j.Logger;

import com.longriver.netpro.common.asdl.ConnectNetWork;
import com.longriver.netpro.util.GetProprities;
/**
 * 接收引导结果
 * @author lilei
 */
public class Lismsg{
	private static Logger logger = Logger.getLogger(Lismsg.class);
	private static int numb_yes = 1; //有任务时记录
	private static int numb_no = 1; //无任务时候
	private static int flagIp = 1;	//第一次获得当前ip
	private static int flagQieIp = 0;	//切ip异常的情况下,控制切ip的频率
	public static String IP = "";	//记录ip
	private static int rnum = 0;

	public static void initJobTrigger(){
//		System.out.println("Lismsg.initJobTrigger");
//		String json = HttpDeal.get(GetProprities.PaginationConfig.getProperty("mqurl"));
//		
//		if(StringUtils.isBlank(json)){
//			
//			return;
//		}
//		TaskGuideBean jh1 = (TaskGuideBean)JsonHelper.parseJson2Object(json,TaskGuideBean.class);
//		
//		if(jh1 != null){
//		List<TaskGuideBean> list1 = jh1.getRlist();
//		if(list1!=null){
//			GuideFixedThreadPool.addThread(list1, jh1);
//		}
//		}
//		if(flagIp==1){//第一次运行获得当前ip
//			IP = GetNetIPUtil.getPublicIP();
//			flagIp = 0;
//			System.out.println(DateUtil.getCurrentTime()+"当前ip:"+IP);
//		}
//		//切换两次ip的情况下,查看ip是否切换成功
//		//如果没成功,则认为vps出错,并每几分钟尝试切ip
//		if(numb_no==0 || numb_yes==0){
//			String nowIP = GetNetIPUtil.getPublicIP();
//			System.out.println(DateUtil.getCurrentTime()+" "+flagQieIp+"->10 原IP:"+IP+" ,当前IP:"+nowIP);
//			if(nowIP.equals(IP)){
//				System.out.println(DateUtil.getCurrentTime()+" "+flagQieIp+" vps切换ip异常");
//				flagQieIp++;
//				if(flagQieIp>10){
//					qieIP(3);//一直不切ip的情况下尝试切IP
//					flagQieIp = 0;
//				}
//				return;
//			}else{
//				IP = nowIP;
//				numb_no = 1;
//				numb_yes = 1;
//			}
//		}
//		String json = HttpDeal.get(GetProprities.PaginationConfig.getProperty("mqurl"));
//		if(json!=null && !json.trim().equals("")){
//			logger.info("引导任务: "+json);
//			System.out.println("已接收任务数:"+rnum);
//			numb_yes++;
//			rnum++;
//			System.out.println("1111:");
//			try {
//				TaskGuideBean jh1 = (TaskGuideBean)JsonHelper.parseJson2Object(json,TaskGuideBean.class);
//				List<TaskGuideBean> list1 = jh1.getRlist();
//				if(list1!=null){
//					System.out.println("555:"+rnum);
//					GuideFixedThreadPool.addThread(list1, jh1);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			if(numb_yes>8) numb_yes = 0;//切换两次ip后归0
//		}else{
//			System.out.println(Thread.currentThread().getName());
//			System.out.println(DateUtil.getCurrentTime()+" "+numb_no+" ,无任务!,已接收数:"+rnum);
//			numb_no++;
//			if(numb_no>=15){
//				qieIP(1);
//				numb_no = 0; //切换两次ip后归0
//				System.out.println("切换过ip-1");
//			}
//		}
	}
	public static void qieIP(int time){
		try {
			for(int i=0;i<5;i++){
				boolean has = ConnectNetWork.cutAdsl(GetProprities.AsdlConfig.getProperty("asdlname"));
				if(has){
					logger.info("cut成功!");
					Thread.sleep(1000 * time);
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
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
