package com.longriver.netpro.webview.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.longriver.netpro.common.threadpool.MessageSender;
import com.longriver.netpro.util.GetProprities;
import com.longriver.netpro.util.JsonHelper;
import com.longriver.netpro.webview.entity.FetchTaskRiceverBean;

@Controller
@RequestMapping("interfaceEffect")
public class InterfaceEffect{
	
	private static Logger logger = Logger.getLogger(InterfaceEffect.class);

	private static final long serialVersionUID = 5681499047675460852L;
	private static MessageSender tt = new MessageSender(GetProprities.PaginationConfig.getProperty("effectResult"),
			GetProprities.getMQAddress(),
			GetProprities.PaginationConfig.getProperty("mquser"),
			GetProprities.PaginationConfig.getProperty("mqpassword"));
	
	@RequestMapping(value="getResult.do",method={RequestMethod.POST,RequestMethod.GET})
	public void getResult(HttpServletRequest request){
		String reply = request.getParameter("reply");
		String click = request.getParameter("click");
		try {
			Integer.parseInt(reply);
		} catch (NumberFormatException e) {
			reply = "0";
		}
		try {
			Integer.parseInt(click);
		} catch (NumberFormatException e) {
			click = "0";
		}
		String id = request.getParameter("id");
		String mark = request.getParameter("mark");
		logger.info("read .txt file...");
		FetchTaskRiceverBean ss = new FetchTaskRiceverBean();
		ss.setId(id);
		ss.setMark(mark);
		ss.setFds(reply);//回复数
		ss.setCts(click);//阅读数
		//效果
		tt.sendJsonFetch(JsonHelper.Object2Json(ss));
		System.out.println(GetProprities.PaginationConfig.getProperty("effectResult"));
		logger.info("effect result json == "+JsonHelper.Object2Json(ss));
	}
	
}
