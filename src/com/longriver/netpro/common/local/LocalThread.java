package com.longriver.netpro.common.local;

import java.io.File;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

import com.longriver.netpro.util.Configur;
import com.longriver.netpro.util.DateUtil;
import com.longriver.netpro.util.GetProprities;
import com.longriver.netpro.util.TaskConfig;

public class LocalThread{
	private Logger logger = Logger.getLogger(LocalThread.class.getName());
    public static void guideDo(Map<String,String> map){
	     //浏览器sdk需要放到项目目录下的tools 目录中
		 TaskConfig conf = new TaskConfig();
		 //配置文件
		 Configur pc = GetProprities.paramsConfig;
		 conf.SKey=pc.getProperty("key");
		 //路径
		 String dir = pc.getProperty("baseDir");
		 String logDir = dir+pc.getProperty("logDir");
		 //把配置文件里面的key,value转化成 map
		 String dateCurrentTime =DateUtil.getCurrentTimeMillis();
  		 String fileN = dateCurrentTime+"_"+(int)(Math.random()*1000);
			 conf.TaskName=(int)Math.random()*1000+"";
			 conf.TaskFile=dir+"local/"+map.get("sc");
			 conf.LogFilePath=logDir+fileN+"_log.txt";
			 conf.ResultFile=logDir+fileN+"_result.xml";
			 //启动引导
//			  conf.Varlist=Varlist;
			 File logFile=new File(conf.LogFilePath);
			 if( logFile.exists()) logFile.delete();
			 File resultFile=new File(conf.ResultFile);
			 if(resultFile.exists()) resultFile.delete();
			 try{
				 ProcessBuilder pb = new ProcessBuilder(pc.getProperty("taskRunner"), conf.ToArgString());
			     pb.redirectErrorStream(true);
			     Process ps = pb.start();
			     Scanner scanner = new Scanner(ps.getInputStream());
			     StringBuilder result = new StringBuilder();
			     while (scanner.hasNextLine()) {
			     	result.append(scanner.nextLine());
			     	result.append(System.getProperty("line.separator"));
			     }
			     scanner.close();
			 }catch(Exception e){
				 e.printStackTrace();
			 }
    }
} 