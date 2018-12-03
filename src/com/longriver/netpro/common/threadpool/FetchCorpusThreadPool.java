package com.longriver.netpro.common.threadpool;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.longriver.netpro.webview.entity.FetchTaskRiceverBean;

public class FetchCorpusThreadPool {
	
	// 创建一个可重用固定线程数的线程池
	public static ExecutorService pool = Executors.newFixedThreadPool(2);
	
	public static void addThread(List<FetchTaskRiceverBean> list1){
		if(null == list1 || list1.size()<=0) return ;
		for(int i=0;i<list1.size();i++){
			Thread t1 = new FetchCorpusThread(list1.get(i));
			pool.execute(t1);
		}
	}
	
    public static void main(String[] args) {
        // 创建实现了Runnable接口对象，Thread对象当然也实现了Runnable接口
        Thread t1 = new FetchThread(null);
        Thread t2 = new FetchThread(null);
        // 将线程放入池中进行执行
        pool.execute(t1);
        pool.execute(t2);
        // 关闭线程池
        pool.shutdown();

    }

}
