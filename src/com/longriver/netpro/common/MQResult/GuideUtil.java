package com.longriver.netpro.common.MQResult;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import com.longriver.netpro.util.JsonHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.common.threadpool.GuideFixedThreadPool;
import com.longriver.netpro.util.JsonHelper;
import com.longriver.netpro.util.MQSender;
import com.longriver.netpro.webview.entity.TaskGuideBean;

public class GuideUtil implements MessageListener{
	private static Logger logger = Logger.getLogger(GuideUtil.class);
	@Override
	public void onMessage(Message message) {
//		TextMessage textMessage = (TextMessage) message;
//		try {
//			logger.info("Message received: successed  GuideUtil");
//			logger.info("Message received: successed  GuideUtil");
//			String value = textMessage.getText();
//			System.out.println("接收的数据: "+value);
//			logger.info("result=="+value);
//			TaskGuideBean jh1 = (TaskGuideBean)JsonHelper.parseJson2Object(value,TaskGuideBean.class);
//    		List<TaskGuideBean> list1 = jh1.getRlist();
//    		if(list1!=null){
//    			GuideFixedThreadPool.addThread(list1, jh1);
//    		}
//    		System.out.println("接收任务执行完毕");
//    		System.out.println("接收任务执行完毕");
//    		System.out.println("接收任务执行完毕");
//		} catch (JMSException ex) {
//			logger.error("JMS error...");
//			logger.error("JMS error", ex);
//		}catch (Exception ex) {
//			logger.error("JMS end....");
//		}
		TextMessage textMessage = (TextMessage) message;
		try {
			logger.info("Message received: successed  GuideUtil------------------------>>>");
			String value = textMessage.getText();
			System.out.println("接收的数据: "+value);
			logger.info("result=="+value);
			JSONObject parseObject = JSON.parseObject(value);
			String design = parseObject.getString("design");
			if(StringUtils.isNotBlank(design) && "AI_ACCOUNT".equals(design)){
				Thread.sleep(5000);
			}
			else{
				Thread.sleep(5000);
			}
			/*String taskTimes = parseObject.getString("taskTimes");//任务次数
			String cookieData = parseObject.getString("cookieData");
			if("0".equals(taskTimes) && StringUtils.isNotBlank(cookieData)){
				
			}else{
				Thread.sleep(30000);
			}*/
			System.out.println(parseObject);
			TaskGuideBean jh1 = (TaskGuideBean)JsonHelper.parseJson2Object(value,TaskGuideBean.class);
    		List<TaskGuideBean> list1 = jh1.getRlist();
    		if(list1!=null){
//    			MQSender.toMQ(jh1, "");
    			GuideFixedThreadPool.addThread(list1, jh1);
    		}
    		System.out.println("接收任务执行完毕-------------------->>>");
		} catch (JMSException ex) {
			logger.error("JMS error", ex);
		}catch (Exception ex) {
			logger.error("JMS end....");
			ex.printStackTrace();
		}
	}
}
