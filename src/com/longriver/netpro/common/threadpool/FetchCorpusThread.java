package com.longriver.netpro.common.threadpool;

import java.io.File;

import com.longriver.netpro.fetchScript.*;
import com.longriver.netpro.webview.entity.FetchTaskRiceverBean;
 
public class FetchCorpusThread extends Thread {
	
	private FetchTaskRiceverBean bb;
	public FetchCorpusThread(FetchTaskRiceverBean a){
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
    	System.out.println("getDbdesign=="+fetchTaskRicever.getDbdesign());
    	System.out.println("getType=="+fetchTaskRicever.getType());
    	System.out.println("getDbdesign=="+fetchTaskRicever.getDbdesign());
    	if(fetchTaskRicever.getType().equals("weibo.sina.comment.corpus")){
//    		SinaWeiboCommentFetch.sina(fetchTaskRicever);
    		SinaWeiboCommentFetch2.getComment(fetchTaskRicever);
    		return ;
    	}else if(fetchTaskRicever.getType().equals("news.sina.comment.corpus")){
    		SinaNewsFetch.sina(fetchTaskRicever);
    		return ;
    	}else if(fetchTaskRicever.getType().equals("news.sohu.comment.corpus")){
    		if(fetchTaskRicever.getUrl().contains("www.")){
    			SohuFetch2.getCommentParams(fetchTaskRicever);
    		}else{
    			SohuFetch.sohu(fetchTaskRicever);
    		}
    		return ;
    	}else if(fetchTaskRicever.getType().equals("news.ifeng.comment.corpus")){
    		IFengNewsFetch.sina(fetchTaskRicever);
    		return ;
    	}else if(fetchTaskRicever.getType().equals("news.163.comment.corpus")){
    		WY163NewsFetch.toRun(fetchTaskRicever);
    		return ;
    	}else if(fetchTaskRicever.getType().equals("news.qq.comment.corpus")){
    		QQNewCommentFetch.toRun(fetchTaskRicever);
    		return ;
    	}
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