package com.longriver.netpro.common.activemq.controller;

import javax.annotation.Resource;
import javax.jms.Destination;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.longriver.netpro.common.activemq.service.IProductService;
import com.longriver.netpro.common.activemq.service.impl.ProductSerivceImpl;
import com.nmedia.util.SpringUtil;

/**
 * @author rhy
 * @date 2018-6-13 上午10:19:51
 * @version V1.0
 */
@Controller
@RequestMapping("/mq")
public class ProductController {

//	@Resource(name="myResultQuene")
//	private Destination destination;
	
	@Resource(name="productService")
	private IProductService productService;
	@RequestMapping("/myresult")
	@ResponseBody
	public String getMyResult(){
		Destination destination = (Destination) SpringUtil.getObject("myResultQuene");
		this.productService.sendMessage(destination, "");
		
		return "接收完成";
	}
	
	public void sendMessage(String json) {
		
		IProductService productService = new ProductSerivceImpl();
		System.out.println(json);
		productService.sendMessage(json);
//		this.productService.sendMessage(json);
		
	}
}
