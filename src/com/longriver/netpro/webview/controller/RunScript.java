package com.longriver.netpro.webview.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.longriver.netpro.common.asdl.ConnectNetWork;
import com.longriver.netpro.common.local.LocalThread;
import com.longriver.netpro.common.local.TouPiao;
import com.longriver.netpro.util.Configur;
import com.longriver.netpro.util.GetProprities;
import com.longriver.netpro.util.TaskConfig;

@Controller
@RequestMapping("RunScript")
public class RunScript{
	
	@RequestMapping(value="getResult.do",method={RequestMethod.POST,RequestMethod.GET})
	public void ifeng(HttpServletRequest request){
		Map<String,String> map = new HashMap<String,String>();
		String sc = request.getParameter("sc");
		map.put("sc", sc);
		LocalThread.guideDo(map);
		
	}
	@RequestMapping(value="qieIp.do",method={RequestMethod.POST,RequestMethod.GET})
	public void qieIp(HttpServletRequest request){
		int num = 0;
		while(true){
			try {
				qieIP();
				Thread.sleep(20*1000);  
				System.out.println("切换ip次数: "+num);
			} catch (InterruptedException e) {
				System.out.println("InterruptedException");
			} catch (Exception e) {
				System.out.println("Exception");
			}
		}
	}
	@RequestMapping(value="toRun.do",method={RequestMethod.POST,RequestMethod.GET})
	public void toRun(HttpServletRequest request){
		int num = 0;
		while(true){
			try {
				Thread.sleep(3000);  
				qieIP();
				for(int i=0;i<3;i++){
					TouPiao.ifeng();
					num++;
				}
				System.out.println("点的次数: "+num);
			} catch (InterruptedException e) {
				System.out.println("InterruptedException");
			} catch (Exception e) {
				System.out.println("Exception");
			}
		}
	}
	@RequestMapping(value="toRunScript.do",method={RequestMethod.POST,RequestMethod.GET})
	public void toRunScript(HttpServletRequest request){
		sc();
	}
	public void sc(){
		TaskConfig conf = new TaskConfig();
		 //配置文件
		 Configur pc = GetProprities.paramsConfig;
		 Map<String, String> paramsConfig = pc.getPropertyMap();
		 String spath = paramsConfig.get("localScript");
		//路径
		 String dir = pc.getProperty("baseDir");
		 conf.SKey=pc.getProperty("key");
		 conf.TaskName="123";
		 conf.TaskFile=dir+spath;
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
	public void qieIP(){
		try {
			for(int i=0;i<5;i++){
				boolean has = ConnectNetWork.cutAdsl(GetProprities.AsdlConfig.getProperty("asdlname"));
				Thread.sleep(100);
				if(has) break;
			}
			Thread.sleep(500);  
			ConnectNetWork.connAdsl(GetProprities.AsdlConfig.getProperty("asdlname"),GetProprities.AsdlConfig.getProperty("asdluser"),GetProprities.AsdlConfig.getProperty("asdlpwd"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
