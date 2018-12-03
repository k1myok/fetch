package com.longriver.netpro.webview.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.longriver.netpro.common.threadpool.MessageSender;
import com.longriver.netpro.util.GetProprities;
import com.longriver.netpro.util.JsonHelper;
import com.longriver.netpro.webview.entity.FetchTaskRiceverBean;

@Controller
@RequestMapping("/interfaceCJ")
public class InterfaceCJ{
	
	private static Logger logger = Logger.getLogger(InterfaceCJ.class);

	private static final long serialVersionUID = 5681499047675460852L;
	private static MessageSender c = new MessageSender(GetProprities.PaginationConfig.getProperty("mqcorpusresult"),
					GetProprities.getMQAddress(),GetProprities.PaginationConfig.getProperty("mquser"),GetProprities.PaginationConfig.getProperty("mqpassword"));
	private static MessageSender g = new MessageSender(GetProprities.PaginationConfig.getProperty("mqfetchresult"),
					GetProprities.getMQAddress(),GetProprities.PaginationConfig.getProperty("mquser"),GetProprities.PaginationConfig.getProperty("mqpassword"));
	private static MessageSender bigvzf = new MessageSender(GetProprities.PaginationConfig.getProperty("bigVQueneResult"),
					GetProprities.getMQAddress(),GetProprities.PaginationConfig.getProperty("mquser"),GetProprities.PaginationConfig.getProperty("mqpassword"));
	public static void main(String[] args) {
		c.sendJsonFetch("12345");
	}
	public static void getResultJava(FetchTaskRiceverBean result){
		String json = JsonHelper.Object2Json(result);
		logger.info(json);
		String design = result.getDesign();
		//语料
		if(design != null && design.equals("corpus")){
			System.out.println(GetProprities.PaginationConfig.getProperty("mqcorpusresult"));
			c.sendJsonFetch(json);
		}if(design != null && design.equals("bigVzf")){
			System.out.println(GetProprities.PaginationConfig.getProperty("bigVQueneResult"));
			bigvzf.sendJsonFetch(json);
		}else{ //引导
			System.out.println(GetProprities.PaginationConfig.getProperty("mqfetchresult"));
			g.sendJsonFetch(json);
		}
	}
	/**
	 * http://localhost:8080/cis/web/interfaceCJ
	 * 读取.txt文件
	 * @param filePath
	 */
	public static String readTxtFile(String filePath){
		StringBuffer sb = new StringBuffer();
        try {        	
            String encoding="utf-8";
            File file=new File(filePath);
            System.out.println("filePath:::"+filePath);
            
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    System.out.println(lineTxt);
                    sb.append(lineTxt);
                }
                read.close();
		    }else{
		        System.out.println("找不到指定的文件");
		    }
	    } catch (Exception e) {
	        System.out.println("读取文件内容出错");
	        e.printStackTrace();
	    }
        return sb.toString();
	}
}
