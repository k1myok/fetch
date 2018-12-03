package com.longriver.netpro.common.activemq.service.impl;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.longriver.netpro.common.activemq.service.IProductService;
import com.longriver.netpro.quartz.LoadGuideInfoServlet;
import com.nmedia.util.SpringUtil;

/**
 * mq生产者
 * @author rhy
 * @date 2018-6-27 下午3:39:10
 * @version V1.0
 */
@Service(value="productService")
public class ProductSerivceImpl implements IProductService {

	private static Logger logger = LoggerFactory.getLogger(ProductSerivceImpl.class);
//	@Resource(name="myResultTemplate")
//	private JmsTemplate jmsTemplate;
	
	
	@Override
	public void sendMessage(Destination destination, String msg) {
		JmsTemplate jmsTemplate = (JmsTemplate) SpringUtil.getObject("myResultTemplate");
		jmsTemplate.send(destination,new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				
				return session.createTextMessage("消息结果");
			}
		});
	}
	
	/* 
	 * 发送结果
	 */
	@Override
	public void sendMessage(final String msg) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:config/spring/mqGuide-context.xml");
		JmsTemplate jmsTemplate = (JmsTemplate) context.getBean("myResultTemplate");
		jmsTemplate.send(new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				
				return session.createTextMessage(msg);
			}
		});
	}

}
