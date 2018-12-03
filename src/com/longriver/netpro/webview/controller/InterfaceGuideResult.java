package com.longriver.netpro.webview.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.longriver.netpro.common.threadpool.MessageSender;
import com.longriver.netpro.util.GetProprities;
import com.longriver.netpro.webview.entity.GuideAccount;

@Controller
@RequestMapping("interfaceGuideResult")
public class InterfaceGuideResult{
	
	private Logger logger = Logger.getLogger(InterfaceCJ.class);

	private static final long serialVersionUID = 5681499047675460852L;
	private static MessageSender tt = new MessageSender(GetProprities.PaginationConfig.getProperty("mqGuideresult"),
												GetProprities.getMQAddress(),
												GetProprities.PaginationConfig.getProperty("mquser"),
												GetProprities.PaginationConfig.getProperty("mqpassword"));
	
	@RequestMapping(value="getResult.do",method={RequestMethod.POST,RequestMethod.GET})
	public void getResult(HttpServletRequest request){
		
		System.out.println(GetProprities.getMQAddress());
		System.out.println(GetProprities.PaginationConfig.getProperty("mqGuideresult"));
		
		String id = request.getParameter("id");
		String an = request.getParameter("acid");
		String content = request.getParameter("e");
		String mark = request.getParameter("mark");
		String url = request.getParameter("url");
		if(content!=null && !content.equals("")){
			try {
				content = new String(content.getBytes("ISO-8859-1"), "UTF-8");
			} catch (Exception e) {
			}
		}
		GuideAccount info = new GuideAccount();
		info.setAn(an);
		info.setContent(content);
		info.setId(id);
		info.setMark(mark);
		info.setUrl(url);
		String json = JSON.toJSONString(info);
		logger.info("guide result::"+json);
		tt.sendJsonFetch(json);
	}
	
	public static void main(String args[]){
		System.out.println(GetProprities.PaginationConfig.getProperty("mqGuideresult")+"==="+GetProprities.PaginationConfig.getProperty("mqurl"));
	}
}
