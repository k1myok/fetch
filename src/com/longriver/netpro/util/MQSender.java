package com.longriver.netpro.util;

import java.util.HashMap;
import java.util.Map;


import com.alibaba.fastjson.JSON;

import javax.annotation.Resource;
import javax.jms.Destination;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jms.core.JmsTemplate;

import com.alibaba.fastjson.JSON;
import com.longriver.netpro.common.activemq.SendMessageByMq;
import com.longriver.netpro.common.activemq.controller.ProductController;
import com.longriver.netpro.common.activemq.service.IProductService;
import com.longriver.netpro.common.activemq.service.impl.ProductSerivceImpl;

import com.longriver.netpro.common.threadpool.MessageSender;
import com.longriver.netpro.webview.entity.GuideAccount;
import com.longriver.netpro.webview.entity.TaskGuideBean;

public class MQSender {

	
	@Resource(name="productService")
	private IProductService productService;

	public static MessageSender sender777 = new MessageSender(GetProprities.PaginationConfig.getProperty("mqGuideresult"),
			GetProprities.getMQAddress(),
			GetProprities.PaginationConfig.getProperty("mquser"),
			GetProprities.PaginationConfig.getProperty("mqpassword"));
	

	public static void mysender(String mark,String json){
		Map<String, String> params = new HashMap<String,String>();
		params.put("mark", mark);
		params.put("content", json);
		try {
			HttpDeal.post(GetProprities.PaginationConfig.getProperty("mqsenderurl"),params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void mysender(String mark,String json,int number){
		Map<String, String> params = new HashMap<String,String>();
		params.put("mark", mark);
		params.put("content", json);
		String suc = "0";
		try {
			suc = HttpDeal.post(GetProprities.PaginationConfig.getProperty("mqsenderurl"),params);
		} catch (Exception e) {
			System.out.println("连接超时,继续尝试..."+suc);
			suc = "0";
		}
//		if(suc==null) suc = "0";
//		if("0".equals(suc) || "".equals(suc)){
//			if(number>=30) return ;
//			number++;
//			try {
//				Thread.sleep(1000*5);
//				mysender(mark,json,number);
//			} catch (InterruptedException e) {}
//		}

	}
	/**
	 * 格式好后发送
	 * @param taskdo
	 * @param content 为""表示成功,否则失败!
	 */
	public static void toMQ(TaskGuideBean taskdo,String content){
		GuideAccount info = new GuideAccount();

		info.setNick(taskdo.getNick());//返回的登录名 注册后可能会变
		if(StringUtils.isNotBlank(taskdo.getDesign()) && taskdo.getDesign().contains("AI_")){
			info.setAn(taskdo.getId()); //引导结果中guide_result 中ID
		}else{
			info.setAn(taskdo.getAn()); //引导结果中guide_result 中ID
		}
		info.setContent(StringUtil.encode(content));
		info.setId(taskdo.getId2());//引导任务guide 中id
		info.setMark(taskdo.getMark());
		info.setUrl(taskdo.getUrl());
		info.setAu(taskdo.getPassword());//找回密码时的新密码
		info.setDesign(taskdo.getDesign());//注册账号还是引导任务
		info.setCode(taskdo.getCode()+"");
		info.setTestAccount(StringUtil.encode(taskdo.getTestAccount()));
		info.setCookieData(taskdo.getCookieData());//发帖cookie信息
		info.setIsreg(taskdo.getIsreg());
		if(taskdo.getPng()!=null && taskdo.getPng().length()>100)
			info.setPng(taskdo.getPng().replaceAll("\r\n", ""));
		String json = JSON.toJSONString(info);
		System.out.println(taskdo.getCode()+" toMQ,返回结果:"+content);
		if(taskdo.getPng()==null || taskdo.getPng().length()<10)
			System.out.println("toMQ data:"+json);
		
//		new SendMessageByMq().send("1", GetProprities.PaginationConfig.getProperty("mqip"), GetProprities.PaginationConfig.getProperty("result"), json, null);
		
		if(StringUtils.isNotBlank(info.getDesign()) && info.getDesign().contains("AI_")){//新引导系统
			
			IProductService mqSender = new ProductSerivceImpl();
			mqSender.sendMessage(json);
		}else{
			
			MQSender.mysender(taskdo.getMark(),json,1);
		}

	}
	
}
