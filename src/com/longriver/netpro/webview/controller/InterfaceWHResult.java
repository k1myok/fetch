package com.longriver.netpro.webview.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.longriver.netpro.common.threadpool.MessageSender;
import com.longriver.netpro.util.DateUtil;
import com.longriver.netpro.util.GetProprities;
import com.longriver.netpro.webview.entity.GuideAccount;

@Controller
@RequestMapping("interfaceWHResult")
public class InterfaceWHResult{
	
	private Logger logger = Logger.getLogger(InterfaceWHResult.class);

	private static final long serialVersionUID = 5681499047675460852L;
	private static MessageSender tt = new MessageSender(GetProprities.PaginationConfig.getProperty("whQueneResult"),
				GetProprities.getMQAddress(),
				GetProprities.PaginationConfig.getProperty("mquser"),
				GetProprities.PaginationConfig.getProperty("mqpassword"));
	
	@RequestMapping(value="getResult.do",method={RequestMethod.POST,RequestMethod.GET})
	public void getResult(HttpServletRequest request){
		String id = request.getParameter("id");
		String url = request.getParameter("url");
		String funs = request.getParameter("currentFuns");
		String content = request.getParameter("e");
		String mark = request.getParameter("mark");
		logger.info("read .txt file...");
		logger.info("read .txt file...");
		logger.info("read .txt file...");
		GuideAccount info = new GuideAccount();
		info.setFuns(funs);
		info.setUrl(url);
		info.setContent(content);
		info.setId(id);
		info.setMark(mark);
		info.setExecTime(DateUtil.getCurrentTime());
		String json = JSON.toJSONString(info);
		tt.sendJsonFetch(json);
		System.out.println(GetProprities.PaginationConfig.getProperty("whQueneResult"));
		logger.info("weihu result::"+json);
	}
	public static void main(String args[]){
		//InterfaceGuideResult.readTxtFile("E:\\chwx\\log\\65_result.xml");
		String ss = "123_log.xml";
		System.out.println(ss.split("_")[0]);
	}
}
