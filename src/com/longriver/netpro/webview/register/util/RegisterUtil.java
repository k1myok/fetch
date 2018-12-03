package com.longriver.netpro.webview.register.util;

import java.util.ResourceBundle;

import com.longriver.netpro.common.threadpool.GuideThreadVps;
import com.longriver.netpro.fetchScript.util.Jdbc2MysqlNewVersion;
import com.longriver.netpro.webview.entity.TaskGuideBean;
import com.longriver.netpro.webview.register.WangyiMail;

/**
 * 账号注册工具类
 * @author rhy
 * @date 2018-4-2 上午9:37:04
 * @version V1.0
 */
public class RegisterUtil {

	
	public static void getRegAccount(){
		
		ResourceBundle bundle = ResourceBundle.getBundle("config//properties//paramsConfig");
		String isreg = bundle.getString("isreg");
		
		if(isreg == null || !"1".equals(isreg)){
			
			return;
		}
		
		GuideThreadVps.qieIP();
		TaskGuideBean task = new TaskGuideBean();
		
		String platid = bundle.getString("platid");
		
		if(platid == null){
			
			return;
		}
		task = Jdbc2MysqlNewVersion.getAccount(platid);
		
		if(task != null){
			WangyiMail.toComment(task);
		}
	}
	
}
