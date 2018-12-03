package com.longriver.netpro.common.threadpool;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.longriver.netpro.util.DeleteLog;
import com.longriver.netpro.util.StringUtil;
import com.longriver.netpro.webview.entity.TaskGuideBean;


public class GuideFixedThreadPool extends Thread{
	private static Logger logger = Logger.getLogger(GuideFixedThreadPool.class);
	// 创建一个可重用固定线程数的线程池
	public static ExecutorService poolvps = Executors.newFixedThreadPool(1);
	public static ExecutorService pool = Executors.newFixedThreadPool(5);
	public static ExecutorService poolSingle = Executors.newSingleThreadExecutor();//这个线程池只有一个线程在工作
	public static int qieTimes = 0; //计数器
	public static int qieNumber = 2; //计数多少次切换一次ip
	public static int temp = 11;
	private TaskGuideBean task;
	
	public GuideFixedThreadPool(TaskGuideBean task){
		this.task = task;
	}
	public void run() {
		addThread(task.getRlist(),task);
	} 
	public synchronized static void addThread(List<TaskGuideBean> list1,TaskGuideBean jh1){
		System.out.println("addThread");
		//删除占用内存的文件
		if(temp>10){
			temp = 0;
			DeleteLog.deleteTomcat_temp();
		}
		temp++;
		for(int i=0;i<list1.size();i++){
			logger.info("use vps service,size=="+list1.size());
			try {//先格式化链接,防止后面总忘记格式
				if(list1.get(i).getAddress()!=null)
					list1.get(i).setAddress(URLDecoder.decode(list1.get(i).getAddress().trim(),"utf-8"));
				if(jh1.getAddress()!=null)

//					jh1.setAddress(URLDecoder.decode(jh1.getAddress().trim(),"utf-8"));

				if(list1.get(i).getCorpus()!=null)
					list1.get(i).setCorpus(StringUtil.decode(list1.get(i).getCorpus().trim()));
				if(list1.get(i).getNick()!=null)
					list1.get(i).setNick(StringUtil.decode(list1.get(i).getNick().trim()));
				if(list1.get(i).getTestAccount()!=null)
					list1.get(i).setTestAccount(StringUtil.decode(list1.get(i).getTestAccount().trim()));
				System.out.println("nick=="+list1.get(i).getNick());
				System.out.println("Address=="+jh1.getAddress());
				System.out.println("Corpus=="+list1.get(i).getCorpus());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			GuideThreadVps t1 = new GuideThreadVps(list1.get(i),jh1);
			t1.run();
			logger.info("session end");
		}
	}
	
    public static void main(String[] args) {
    }

}
