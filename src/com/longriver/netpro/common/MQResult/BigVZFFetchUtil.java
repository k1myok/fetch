	package com.longriver.netpro.common.MQResult;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import com.longriver.netpro.common.threadpool.FetchFixedThreadPool;
import com.longriver.netpro.util.JsonHelper;
import com.longriver.netpro.webview.entity.FetchTaskRiceverBean;

public class BigVZFFetchUtil implements MessageListener{
	private static Logger logger = Logger.getLogger(BigVZFFetchUtil.class);
	@Override
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		try {
			logger.info("Message received: successed  BigVZFFetchUtil");
			logger.debug("BigVZFFetchUtil=="+textMessage.getText());
			FetchTaskRiceverBean jh1 = (FetchTaskRiceverBean)JsonHelper.parseJson2Object(textMessage.getText(),FetchTaskRiceverBean.class);
    		List<FetchTaskRiceverBean> list1 = jh1.getRlist();
    		for(int i=0;i<list1.size();i++){
    			list1.get(i).setDesign("bigVzf");
    		}
    		//放到连接池中
    		FetchFixedThreadPool.addThread(list1);
		} catch (JMSException ex) {
			logger.error("JMS error", ex);
		}
	}
}
