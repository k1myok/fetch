package com.longriver.netpro.common.threadpool;

import java.io.File;

import com.longriver.netpro.fetchScript.IFengNewsFetch;
import com.longriver.netpro.fetchScript.SinaNewsFetch;
import com.longriver.netpro.fetchScript.SinaWeiboCommentFetch2;
import com.longriver.netpro.fetchScript.SinaZFetch;
import com.longriver.netpro.fetchScript.SohuFetch;
import com.longriver.netpro.fetchScript.SohuFetch2;
import com.longriver.netpro.fetchScript.WY163NewsFetch;
import com.longriver.netpro.webview.entity.FetchTaskRiceverBean;

public class FetchThread extends Thread {
	
	private FetchTaskRiceverBean bb;
	public FetchThread(FetchTaskRiceverBean a){
		bb = a;
	}
	
    @Override
    public void run() {
    	try {
			fetchDo(bb);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
  //开启采集
	public void fetchDo(FetchTaskRiceverBean fetchTaskRicever) throws Exception{
    	System.out.println("采集类型...."+fetchTaskRicever.getType());
    	if(fetchTaskRicever.getType().equals("weibo.sina.comment")){
    		SinaWeiboCommentFetch2.getComment(fetchTaskRicever);
    		return ;
    	}else if(fetchTaskRicever.getType().equals("weibo.sina.repost")){
    		SinaWeiboCommentFetch2.getComment(fetchTaskRicever);
    		return ;
    	}else if(fetchTaskRicever.getType().equals("news.sina.comment")){
    		SinaNewsFetch.sina(fetchTaskRicever);
    		return ;
    	}else if(fetchTaskRicever.getType().equals("news.sohu.comment")){
    		if(fetchTaskRicever.getUrl().contains("www.")){
    			SohuFetch2.getCommentParams(fetchTaskRicever);
    		}else{
    			SohuFetch.sohu(fetchTaskRicever);
    		}
    		return ;
    	}else if(fetchTaskRicever.getType().equals("news.ifeng.comment")){
    		IFengNewsFetch.sina(fetchTaskRicever);
    		return ;
    	}else if(fetchTaskRicever.getType().equals("news.163.comment")){
    		WY163NewsFetch.toRun(fetchTaskRicever);
    		return ;
    	}else if(fetchTaskRicever.getType().equals("weibo.sina.status")){
    		SinaZFetch.sina(fetchTaskRicever);
    		return ;
    	}
    	System.out.println("去执行火车头采集....");
    	System.out.println("去执行火车头采集....");
    }
    public void mkDir(String path){
    	  File file = new File(path);
		  //判断文件夹是否存在,如果不存在则创建文件夹
		  if (!file.exists()) {
			  file.mkdir();
		  }
    }
    public void deleteFile(String path){
    	File resultFile=new File(path);
 		if(resultFile.exists()){
 			resultFile.delete();
 		}
    }
} 