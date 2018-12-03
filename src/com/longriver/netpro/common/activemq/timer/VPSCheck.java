package com.longriver.netpro.common.activemq.timer;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.longriver.netpro.common.activemq.service.IProductService;
import com.longriver.netpro.webview.entity.GuideAccount;

/**
 * 服务器检测
 * @author rhy
 * @date 2018-6-23 下午2:14:50
 * @version V1.0
 */
public class VPSCheck {

	private static Logger logger = LoggerFactory.getLogger(VPSCheck.class);
	
	@Resource(name="productService")
	private IProductService productService;
	
	/**
	 * 服务器检测
	 */
	public void getStatus(){
		
		logger.info("检测vps开始了------------------->>>");
		
		/*GuideAccount guide = new GuideAccount();
		
		guide.setDesign("AI_CHECK");
		guide.setId("1");
		guide.setContent("服务器检测");
		this.productService.sendMessage(JSON.toJSONString(guide));*/
	}
}
