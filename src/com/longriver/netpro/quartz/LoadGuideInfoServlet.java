package com.longriver.netpro.quartz;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.longriver.netpro.common.asdl.ConnectNetWork;
import com.longriver.netpro.common.threadpool.GuideFixedThreadPool;
import com.longriver.netpro.util.DateUtil;
import com.longriver.netpro.util.GetNetIPUtil;
import com.longriver.netpro.util.GetProprities;
import com.longriver.netpro.util.HttpDeal;
import com.longriver.netpro.util.JsonHelper;
import com.longriver.netpro.util.KillProcess;
import com.longriver.netpro.webview.entity.TaskGuideBean;

public class LoadGuideInfoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(LoadGuideInfoServlet.class);
	public static ExecutorService pool = Executors.newCachedThreadPool();//无界线程池，可以进行自动线程回收
//	public static ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(100);
	private static int numb_no = 0; //无任务次数
	private static int numb_times = 0; //接收任务次数
//	public static String IP = "";	//记录ip
//	public static String NOWIP = "192.168.1.1";	//记录ip
	public static boolean noThread = true;	//
	public static boolean qieip = true;	//
	public static boolean record = false;	//是否接收
	
	public void init() throws ServletException {
		FutureTask<String> task = new FutureTask<String>(new Callable<String>(){  
	           @Override  
	           public String call() throws Exception {  
	              taskstart(); //使用另一个线程来执行该方法，会避免占用Tomcat的启动时间  
	              return "Collection Completed";  
	           }  
	      });  
	      new Thread(task).start();
	}
	public void taskstart(){
		while(true){
			int numberQieIP = Integer.parseInt(GetProprities.paramsConfig.getProperty("numberQieIP"));
			try {
				if(!qieip){
					System.out.println(DateUtil.getCurrentTime()+",切ip,failed");
					qieIP(); Thread.sleep(30 * 1000l);
					continue ;
				}
				String json = null;
				System.out.println("noThread:"+noThread);
				if(noThread){
					json = HttpDeal.get(GetProprities.PaginationConfig.getProperty("mqurl"));
					System.out.println("共接收数:"+numb_times+", 接收的数据json:"+json);
				}
				if(json!=null && !json.trim().equals("")){
					noThread = false;record = true;
					numb_times++;
					numb_no = 0;
					TaskGuideBean task = (TaskGuideBean)JsonHelper.parseJson2Object(json,TaskGuideBean.class);
					GuideFixedThreadPool p = new GuideFixedThreadPool(task);
					pool.execute(p);
				}else{
					numb_no++;
					int activeNumber = ((ThreadPoolExecutor)pool).getActiveCount();//活动的线程数
					System.out.println("Thread:"+noThread+",活动线程:"+activeNumber+",配置切ip:"+numberQieIP+",切IP:"+numb_times%3);
					if(record && activeNumber<1){
						record = false;noThread = true;
						if(numb_times%3 == 0) qieIP();//接收三个任务换一次ip
					}
					if(numb_no>=45){
						noThread = true;
						numb_no = 0; //计数器归0
						qieIP();
						KillProcess.kill();
					}
				}
				int timeSpan = Integer.parseInt(GetProprities.paramsConfig.getProperty("timeSpan"));
				Thread.sleep(timeSpan * 1000l);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public static void qieIP(){
		boolean cut = false;
		boolean conn = false;
		try {
			Thread.sleep(500);
			for(int i=0;i<5;i++){
				boolean has = ConnectNetWork.cutAdsl(GetProprities.AsdlConfig.getProperty("asdlname"));
				if(has){
					logger.info("cut成功!");
					cut = true;
					break;
				}
				Thread.sleep(1000);
			}
			for(int i=0;i<5;i++){
				boolean cnt = ConnectNetWork.connAdsl(GetProprities.AsdlConfig.getProperty("asdlname"),GetProprities.AsdlConfig.getProperty("asdluser"),GetProprities.AsdlConfig.getProperty("asdlpwd"));
				if(cnt){
					logger.info("conn成功!");
					conn = true;
					break;
				}
			}
			Thread.sleep(5 * 1000l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(cut && conn) qieip = true;
		else qieip = false;
//		IP = GetNetIPUtil.getPublicIP();
	}
}